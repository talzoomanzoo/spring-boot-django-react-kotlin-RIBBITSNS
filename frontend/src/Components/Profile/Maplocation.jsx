import { Button } from "@mui/material";
import { useFormik } from "formik";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { updateUserProfile } from "../../Store/Auth/Action";
import "./Map.css";

const Maplocation = (onLocationChange) => {
  const { kakao } = window;
  const [address, setAddress] = useState("");
  const { theme } = useSelector(store => store);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [map, setMap] = useState(null);
  const [searchResults, setSearchResults] = useState([]);
  const [markers, setMarkers] = useState([]);
  const infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const itemsPerPage = 5;
  const [currentMarkers, setCurrentMarkers] = useState([]);
  const [hoveredMarkerIndex, setHoveredMarkerIndex] = useState(null);
  const [showLocation, setShowLocation] = useState(true); // Control map visibility
  const [isLocationSaved, setIsLocationSaved] = useState(false);
  const dispatch = useDispatch();

  const toggleMap = (values) => {
    console.log(values);
    if (isLocationSaved) {
      // 이미 위치가 저장된 경우, 저장된 위치를 업데이트하거나 다른 작업 수행
      // 여기서는 updateUserProfile(values)를 호출하여 위치를 업데이트합니다.
      dispatch(updateUserProfile(values));
    }
    setShowLocation(!isLocationSaved); // Toggle map visibility based on isLocationSaved
    setIsLocationSaved(!isLocationSaved); // Toggle the saved location state
  };

  const formik = useFormik({
    initialValues: {
      location: address,
    },
    onSubmit: toggleMap,
  });

  useEffect(() => {
    // address가 변경될 때마다 location 필드를 업데이트
    console.log("Address Updated:", address); // 주소 확인
    formik.setValues({
      location: address,
    });
  }, [address]);

  useEffect(() => {
    const container = document.getElementById("map");

    if (container) {
      const options = {
        center: new kakao.maps.LatLng(37.5662952, 126.9757567),
        level: 3,
      };

      if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition((position) => {
          const latitude = position.coords.latitude;
          const longitude = position.coords.longitude;
          options.center = new kakao.maps.LatLng(latitude, longitude);

          const map = new kakao.maps.Map(container, options);
          setMap(map);
        });
      }
    }
  }, []);

  useEffect(() => {
    if (map) {
      // 지도 타입 컨트롤을 생성하고 지도에 추가
      const mapTypeControl = new kakao.maps.MapTypeControl();
      map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

      // 지도 줌 컨트롤을 생성하고 지도에 추가
      const zoomControl = new kakao.maps.ZoomControl();
      map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
    }
  }, [map]);

  function getListItem(index, places) {
    return (
      <div className="item" key={index}>
        <span className={"markerbg marker_" + (index + 1)}></span>
        <div className="info">
          <h5 className={`text-black`}>{places.place_name}</h5>
          {places.road_address_name ? (
            <div>
              <span className={`text-black`} >{places.road_address_name}</span>
              <span className="jibun gray">{places.address_name}</span>
            </div>
          ) : (
            <span className={`text-black`} >{places.address_name}</span>
          )}
          <span className="tel">{places.phone}</span>
        </div>
      </div>
    );
  }

  const handleSearch = () => {
    if (!searchKeyword.trim()) {
      alert("키워드를 입력해주세요!");
      return;
    }

    // 현재 열려 있는 infowindow를 닫음
    infowindow.close();

    // 이전 마커들을 제거
    currentMarkers.forEach((marker) => {
      marker.setMap(null);
    });

    const places = new kakao.maps.services.Places();
    places.keywordSearch(searchKeyword, function (data, status) {
      if (status === kakao.maps.services.Status.OK) {
        const bounds = new kakao.maps.LatLngBounds();
        const newMarkers = data.map((place) => {
          return displayMarker(place);
        });

        newMarkers.forEach((marker) => {
          bounds.extend(marker.getPosition());
        });

        // 검색 결과와 새로운 마커 배열을 업데이트
        setSearchResults(data);
        setCurrentMarkers(newMarkers);
        setCurrentPage(1);
        map.setBounds(bounds);
      }
    });
  };

  const createSearchResultItem = (result, index) => {
    let infowindow = null;

    const handleMouseEnter = () => {
      const marker = currentMarkers[index];
      if (marker) {
        if (infowindow) {
          infowindow.close();
        }
        infowindow = new kakao.maps.InfoWindow({
          content:
            '<div style="padding:5px;font-size:12px;color:black;">' +
            result.place_name +
            "</div>",
          position: marker.getPosition(),
        });
        infowindow.open(map, marker);
      }
    };

    const handleMouseLeave = () => {
      if (infowindow) {
        infowindow.close();
      }
    };

    const handleItemClick = (place) => {
      // 나머지 동작 처리 (맵 재설정 또는 기타 작업)
      const marker = currentMarkers[index];
      if (marker) {
        const markerPosition = marker.getPosition();
        map.setLevel(3); // 레벨을 3으로 설정
        map.setCenter(markerPosition); // 클릭한 마커를 중심으로 지도 재설정
        console.log("New Address:", place.address_name); // 주소 확인

        // 주소를 부모 컴포넌트로 전달
        if (typeof onLocationChange === "function") {
          onLocationChange(place.address_name);
        }
      }
    };

    return (
      <li
        className={getMarkerItemClassName(index)}
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
        onClick={handleItemClick}
      >
        {getListItem(indexOfFirstItem + index, result)}
      </li>
    );
  };

  const displayMarker = (place) => {
    console.log("Place Object:", place); // place 객체 내용 확인
    const marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(place.y, place.x),
    });

    // 마커를 markers 배열에 추가
    setMarkers((prevMarkers) => [...prevMarkers, marker]);

    // 마우스가 마커 위에 올라갈 때
    kakao.maps.event.addListener(marker, "mouseover", function () {
      infowindow.close(); // 기존 infowindow를 닫음
      infowindow.setContent(
        '<div style="padding:5px;font-size:12px;color:black;">' +
          place.place_name +
          "</div>"
      );
      infowindow.open(map, marker);
    });

    kakao.maps.event.addListener(marker, "click", function () {
      const markerPosition = marker.getPosition();
      map.setLevel(3); // 레벨을 3으로 설정
      map.setCenter(markerPosition); // 클릭한 마커를 중심으로 지도 재설정
      setAddress(place.place_name); // 주소 업데이트
      infowindow.close(); // 마커 클릭 시 인포윈도우 닫기
    });

    // 마우스가 마커에서 벗어날 때
    kakao.maps.event.addListener(marker, "mouseout", function () {
      infowindow.close(); // infowindow를 닫음
    });
    return marker; // 마커를 반환
  };

  // CSS 클래스를 조작하여 마커 강조 및 투명 처리
  const getMarkerItemClassName = (index) => {
    return `item ${index === hoveredMarkerIndex ? "hovered" : ""}`;
  };
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = searchResults.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageClick = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // 계산된 총 페이지 수
  const totalPageCount = Math.ceil(searchResults.length / itemsPerPage);

  // 페이지 번호 목록 생성
  const pageNumbers = [];
  for (let i = 1; i <= totalPageCount; i++) {
    pageNumbers.push(i);
  }

  return (
    <div>
      {showLocation && (
        <div>
          <div className="mt-2 mb-2 space-y-3">
            <div className="flex items-center text-gray-500">
              <form onSubmit={formik.handleSubmit}>
                <Button type="submit" onClick={toggleMap}>
                  저장
                </Button>
              </form>
              <p className="text-gray-500 ml-3">{address}</p>
            </div>
          </div>

          <div className="map_wrap">
            <div
              id="map"
              style={{
                width: "70%",
                height: "100%",
                position: "relative",
                overflow: "hidden",
              }}
            ></div>
            <div id="list_wrap" className="bg_white">
              <div className="option" style={{ textAlign: "right" }}>
                <div>
                  <form
                    onSubmit={(e) => {
                      e.preventDefault();
                      handleSearch();
                    }}
                  >
                    <input
                      type="text"
                      value={searchKeyword}
                      placeholder="장소·주소 검색"
                      onChange={(e) => setSearchKeyword(e.target.value)}
                      id="keyword"
                      size="15"
                      className={`${theme.currentTheme === "light" ? "" : "text-black"}`}
                    />
                    <Button type="submit">검색하기</Button>
                  </form>
                </div>
              </div>
              <hr />

              <ul id="placesList">
                {currentItems.map((result, index) =>
                  createSearchResultItem(result, index)
                )}
              </ul>

              <div id="pagination">
                <ul className={`page-numbers text-black`}>
                  {pageNumbers.map((number) => (
                    <li key={number} onClick={() => handlePageClick(number)}>
                      {number}
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Maplocation;
