import FmdGoodIcon from "@mui/icons-material/FmdGood";
import ImageIcon from "@mui/icons-material/Image";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import SlideshowIcon from "@mui/icons-material/Slideshow";
import TagFacesIcon from "@mui/icons-material/TagFaces";
import { Avatar, Button, Tooltip } from "@mui/material";
import ProgressBar from "@ramonak/react-progress-bar";
import EmojiPicker from "emoji-picker-react";
import { useFormik } from "formik";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router";
import { ToastContainer } from "react-toastify";
import * as Yup from "yup";
import { api } from "../../Config/apiConfig";
import { findComById } from "../../Store/Community/Action";
import { findTwitsByComId } from "../../Store/Tweet/Action";
import {
  COM_TWEET_CREATE_FAILURE,
  COM_TWEET_CREATE_REQUEST,
  COM_TWEET_CREATE_SUCCESS,
} from "../../Store/Tweet/ActionType";
import { uploadToCloudinary } from "../../Utils/UploadToCloudinary";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import "../Home/MiddlePart/TwitMap.css";

// const Maplocation = React.lazy(() => import("../Profile/Maplocation"));
const Loading = React.lazy(() => import("../Profile/Loading/Loading"));

const ComDetail = ({ changePage, sendRefreshPage }) => {
  const [tooltipOpen, setTooltipOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [uploadingImage, setUploadingImage] = useState(false);
  const [selectedImage, setSelectedImage] = useState("");
  const [selectedVideo, setSelectedVideo] = useState("");
  const [isLocationFormOpen, setLocationFormOpen] = useState(false);
  const [openEmoji, setOpenEmoji] = useState(false);
  const [refreshTwits, setRefreshTwits] = useState(0); // Define setRefreshTwits
  const dispatch = useDispatch();
  const { com, twit, auth, theme } = useSelector((store) => store);
  const [notificationSent, setNotificationSent] = useState(false); // 푸시 알림을 이미 보냈는지 확인하는 상태 추가
  const jwtToken = localStorage.getItem("jwt");
  const [address, setAddress] = useState("");
  const handleOpenEmoji = () => setOpenEmoji(true);
  const handleCloseEmoji = () => setOpenEmoji(false);
  const { kakao } = window;
  const [searchKeyword, setSearchKeyword] = useState("");
  const [map, setMap] = useState(null);
  const [searchResults, setSearchResults] = useState([]);
  const [markers, setMarkers] = useState([]);
  const infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const itemsPerPage = 5;
  const [currentMarkers, setCurrentMarkers] = useState([]);
  const [hoveredMarkerIndex, setHoveredMarkerIndex] = useState(null);
  const [showLocation, setShowLocation] = useState(true);
  const navigate = useNavigate();
  
  useEffect(() => {
    const messageEventListener = (event) => {
      const message = event.data;

      if (event.source !== window.parent) {
        // 메시지가 부모 창에서 온 것이 아닌 경우 처리하지 않음
        return;
      }

      if (message.type === "navigate") {
        // 메시지가 navigate 타입일 때만 경로 변경
        navigate(message.path);
      }
    };

    window.addEventListener("message", messageEventListener);

    return () => {
      window.removeEventListener("message", messageEventListener);
    };
  }, [navigate]);

  useEffect(() => {
    if (isLocationFormOpen && showLocation) {
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
    }
  }, [isLocationFormOpen, showLocation]);

  const formikLocation = useFormik({
    initialValues: {
      location: address,
    },
    onSubmit: (values) => {
      // 주소값만 저장하고 formikLocation reset
      setAddress(values.location);
      formikLocation.resetForm();
    },
  });

  useEffect(() => {
    const container = document.getElementById("map");
    dispatch(findComById(param.id));
    dispatch(findTwitsByComId(param.id));

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
  }, [sendRefreshPage, refreshTwits]);

  useEffect(() => {
    if (map) {
      const mapTypeControl = new kakao.maps.MapTypeControl();
      map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
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
              <span className={`text-black`}>{places.road_address_name}</span>
              <span className="jibun gray">{places.address_name}</span>
            </div>
          ) : (
            <span className={`text-black`}>{places.address_name}</span>
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
    infowindow.close();

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
      const marker = currentMarkers[index];
      if (marker) {
        const markerPosition = marker.getPosition();
        map.setLevel(3); // 레벨을 3으로 설정
        map.setCenter(markerPosition); // 클릭한 마커를 중심으로 지도 재설정
        setAddress(place.place_name); // 주소 업데이트
        infowindow.close(); // 마커 클릭 시 인포윈도우 닫기
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
    const marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(place.y, place.x),
    });

    setMarkers((prevMarkers) => [...prevMarkers, marker]);
    kakao.maps.event.addListener(marker, "mouseover", function () {
      infowindow.close();
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
      setLocationFormOpen(false);
    });

    kakao.maps.event.addListener(marker, "mouseout", function () {
      infowindow.close(); // infowindow를 닫음
    });
    return marker; // 마커를 반환
  };

  const getMarkerItemClassName = (index) => {
    return `item ${index === hoveredMarkerIndex ? "hovered" : ""}`;
  };
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = searchResults.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageClick = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const totalPageCount = Math.ceil(searchResults.length / itemsPerPage);
  const pageNumbers = [];
  for (let i = 1; i <= totalPageCount; i++) {
    pageNumbers.push(i);
  }

  const validationSchema = Yup.object().shape({
    content: Yup.string().required("내용이 없습니다"),
  });

  const param = useParams();
  const handleBack = () => navigate(-1);

  const createComTweetRequest = (comId) => ({
    type: COM_TWEET_CREATE_REQUEST,
  });

  const createComTweetSuccess = (data) => ({
    type: COM_TWEET_CREATE_SUCCESS,
    payload: data,
  });

  const createComTweetFailure = (error) => ({
    type: COM_TWEET_CREATE_FAILURE,
    payload: error,
  });

  const handleToggleLocationForm = () => {
    setLoading(true);
    setLocationFormOpen((prev) => !prev);
    setLoading(false);
  };

  console.log("comDetail auth", auth);
  console.log("comDetail com", com);

  const authCheck = (com) => {
    for (let i = 0; i < com.com?.followingsc?.length; i++) {
      if (com.com?.followingsc[i].id === auth.user.id) {
        return true;
      }
    }
  };

  const ComCreateTweet = (tweetData) => {
    return async (dispatch) => {
      setLoading(true);
      dispatch(createComTweetRequest(param.id));
      try {
        const { data } = await api.post(
          `http://localhost:8080/api/twits/${param.id}/create`,
          tweetData
        );

        dispatch(createComTweetSuccess(data));

        const response = await ethicreveal(data.id, data.content);
        handleSendPushNotification();
      } catch (error) {
        dispatch(createComTweetFailure(error.message));
      } finally {
        setLoading(false); // 로딩 완료
      }
    };
  };

  const handleSubmit = (values, actions) => {
    if (values.content.trim() !== "") {
      // 게시글이 비어있지 않을 때만 실행
      const tweetData = {
        content: values.content,
        image: values.image,
        video: values.video,
        location: address, // 저장한 주소값을 사용
      };

      dispatch(ComCreateTweet(tweetData));

      actions.resetForm();
      setSelectedImage("");
      setSelectedVideo("");
      setAddress(""); // 게시글을 작성하고 나면 주소값 초기화
    }
    handleCloseEmoji();
  };

  const ethicreveal = async (twitid, twitcontent) => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/ethic/reqsentence",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwtToken}`,
          },
          body: JSON.stringify({
            id: twitid,
            content: twitcontent,
          }),
        }
      );
      if (response.status === 200) {
        setLoading(false);
        setRefreshTwits((prev) => prev + 1);
      }
    } catch (error) {
      console.error("Error fetching ethic data:", error);
    }
  };

  const formik = useFormik({
    initialValues: {
      content: "",
      image: "",
      video: "",
      location: address,
    },
    validationSchema,
    onSubmit: handleSubmit,
  });

  const handleSelectImage = async (event) => {
    setUploadingImage(true);
    const imgUrl = await uploadToCloudinary(event.target.files[0], "image");
    formik.setFieldValue("image", imgUrl);
    setSelectedImage(imgUrl);
    setUploadingImage(false);
  };

  const handleSelectVideo = async (event) => {
    setUploadingImage(true);
    const videoUrl = await uploadToCloudinary(event.target.files[0], "video");
    formik.setFieldValue("video", videoUrl);
    setSelectedVideo(videoUrl);
    setUploadingImage(false);
  };

  // const handleMapLocation = async (newAddress) => {
  //     setAddress(newAddress);
  //     formik.setFieldValue("location", newAddress);
  // };

  const handleEmojiClick = (value) => {
    const { emoji } = value;
    formik.setFieldValue("content", formik.values.content + emoji);
  };

  const handleSendPushNotification = () => {
    if (!notificationSent && Notification.permission === "granted") {
      const options = {
        body: "리빗이 성공적으로 게시되었습니다.",
        icon: "URL_TO_ICON",
        badge: "URL_TO_BADGE",
      };
      new Notification("게시 완료", options);
      setNotificationSent(true); // 알림을 보냈음을 표시
    } else if (Notification.permission !== "denied") {
      Notification.requestPermission().then((permission) => {
        if (permission === "granted") {
          handleSendPushNotification();
        }
      });
    }
  };

  const [totalEthicRateMAX, setTotalEthicRateMAX] = useState(0);
  const [averageEthicRateMAX, setAverageEthicRateMAX] = useState(0);

  useEffect(() => {
    // Calculate total ethicrateMAX
    const totalEthicRateMAXValue = twit.twits.reduce((sum, tweet) => {
      // ethiclabel이 4인 경우 0으로 포함하여 합산
      return sum + (tweet.ethiclabel === 4 ? 0 : tweet.ethicrateMAX || 0);
    }, 0);

    // Calculate average ethicrateMAX
    const averageEthicRateMAXValue =
      twit.twits.length > 0 ? totalEthicRateMAXValue / twit.twits.length : 0;

    // 정수로 변환
    const roundedAverageEthicRateMAX = Math.floor(averageEthicRateMAXValue);

    // 상태 업데이트
    setTotalEthicRateMAX(totalEthicRateMAXValue);
    setAverageEthicRateMAX(roundedAverageEthicRateMAX);

    // ... (다른 코드)
  }, [twit.twits, auth.user]);

  return (
    <div>
      <div className="flex">
        <section
          className={`z-50 flex items-center sticky top-0`}
        >
          <KeyboardBackspaceIcon
            className="cursor-pointer"
            onClick={handleBack}
          />
          <div className="ml-5 flex" style={{ minWidth: "200px", flex: 1 }}>
            <h1 className="py-5 text-xl font-bold opacity-90 overflow-hidden">
              {com.com?.comName}
            </h1>
          </div>
        </section>

        <div className="flex mt-5 ml-auto" style={{ width: "200px" }}>
          <Tooltip
            title="게시글의 윤리수치를 분석해 그래프로 보여줍니다"
            open={tooltipOpen}
            onClose={() => setTooltipOpen(false)}
            arrow
          >
            <InfoOutlinedIcon
              fontSize="small"
              style={{ cursor: "pointer" }}
              onClick={() => setTooltipOpen(!tooltipOpen)}
            />
          </Tooltip>
          {`${
            averageEthicRateMAX < 25
              ? "😄"
              : averageEthicRateMAX < 50
              ? "😅"
              : averageEthicRateMAX < 75
              ? "☹️"
              : "🤬"
          }`}
          <ProgressBar
            completed={averageEthicRateMAX}
            width="165px" // Set the fixed width for ProgressBar
            margin="2px 4px 4px 0" // Margin to right-align the ProgressBar
            bgColor={`${
              averageEthicRateMAX < 25
                ? "hsla(195, 100%, 35%, 0.8)"
                : averageEthicRateMAX < 50
                ? "hsla(120, 100%, 25%, 0.7)"
                : averageEthicRateMAX < 75
                ? "hsla(48, 100%, 40%, 0.8)"
                : "red"
            }`}
          />
        </div>
      </div>

      <section>
        <img
          className="w-[100%] h-[15rem] object-cover"
          src={
            com.com?.backgroundImage ||
            "https://png.pngtree.com/thumb_back/fw800/background/20230304/pngtree-green-base-vector-smooth-background-image_1770922.jpg"
          }
          alt=""
          loading="lazy"
        />
      </section>

      {authCheck(com) ? (
        <section className="pb-10" style={{ marginTop: 30 }}>
          <div className="flex space-x-5 ">
            <Avatar
              alt="Avatar"
              src={
                auth.user?.image
                  ? auth.user.image
                  : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"
              }
              loading="lazy"
            />
            <div className="w-full">
              <form onSubmit={formik.handleSubmit}>
                <div>
                  <input
                    type="text"
                    name="content"
                    placeholder="커뮤니티에 일상을 공유해 보세요!"
                    className={`border-none outline-none text-xl bg-transparent`}
                    size="50"
                    {...formik.getFieldProps("content")}
                  />
                  {formik.errors.content && formik.touched.content && (
                    <div className="text-red-500">{formik.errors.content}</div>
                  )}
                </div>

                {/* <section>
                  <img
                    className="w-[100%] h-[15rem] object-cover"
                    src={
                      com.com?.backgroundImage ||
                      "https://t1.daumcdn.net/cfile/tistory/174FF7354E6ACC7606"
                    }
                    alt=""
                    loading="lazy"
                  />
                </section> */}
                {!uploadingImage && (
                  <div>
                    {selectedImage && (
                      <img
                        className="w-[28rem]"
                        src={selectedImage}
                        alt=""
                        loading="lazy"
                      />
                    )}

                    {selectedVideo && <video controls src={selectedVideo} />}
                  </div>
                )}

                <div className="flex justify-between items-center mt-5">
                  <div className="flex space-x-5 items-center">
                    <label className="flex items-center space-x-2 rounded-md cursor-pointer">
                      <ImageIcon className="text-[#42c924]" />
                      <input
                        type="file"
                        name="imageFile"
                        className="hidden"
                        onChange={handleSelectImage}
                      />
                    </label>

                    <label className="flex items-center space-x-2  rounded-md cursor-pointer">
                      <SlideshowIcon className="text-[#42c924]" />
                      <input
                        type="file"
                        name="videoFile"
                        className="hidden"
                        onChange={handleSelectVideo}
                      />
                    </label>

                    <label className="flex items-center space-x-2 rounded-md cursor-pointer">
                      <FmdGoodIcon
                        className="text-[#42c924]"
                        onClick={handleToggleLocationForm}
                      />
                      {address}
                    </label>

                    <div className="relative">
                      <TagFacesIcon
                        onClick={handleOpenEmoji}
                        className="text-[#42c924] cursor-pointer"
                      />
                      {openEmoji && (
                        <div className="absolute top-10 z-50">
                          <EmojiPicker
                            theme={theme.currentTheme}
                            onEmojiClick={handleEmojiClick}
                            lazyLoadEmojis={true}
                          />
                        </div>
                      )}
                    </div>
                  </div>

                  <div>
                    <Button
                      type="submit"
                      variant="contained"
                      sx={{
                        bgcolor: "#42c924",
                        borderRadius: "20px",
                        paddingY: "8px",
                        paddingX: "20px",
                        color: "white",
                      }}
                    >
                      Ribbit
                    </Button>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div style={{ marginTop: 20 }}>
            {loading ? <Loading /> : null}
            {isLocationFormOpen && showLocation && (
              <div>
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
                          <li
                            key={number}
                            onClick={() => handlePageClick(number)}
                          >
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
        </section>
      ) : null}

      <div style={{ marginTop: 20 }}>
        {loading ? <Loading /> : null}
        {twit.twits && twit.twits.length > 0 ? (
          twit.twits.map((item) => (
            <TwitCard twit={item} key={item.id} changePage={changePage} />
          ))
        ) : (
          <div>게시된 리빗이 없습니다.</div>
        )}
      </div>

      <ToastContainer
        position="bottom-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
};

export default ComDetail;
