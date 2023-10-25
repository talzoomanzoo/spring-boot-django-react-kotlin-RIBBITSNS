import { BusinessCenterSharp } from "@mui/icons-material";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import TabContext from "@mui/lab/TabContext";
import TabList from "@mui/lab/TabList";
import TabPanel from "@mui/lab/TabPanel";
import {
  Avatar,
  Backdrop,
  Box,
  Button,
  CircularProgress,
  Divider,
} from "@mui/material";
import Tab from "@mui/material/Tab";
import { useFormik } from "formik";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import {
  FollowUserAction,
  findUserById,
  updateUserProfile,
} from "../../Store/Auth/Action";
import {
  findTwitsByLikesContainUser,
  getUsersReplies,
  getUsersTweets,
  viewPlus,
} from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import SnackbarComponent from "../Snackbar/SnackbarComponent";
import "./Map.css";
import ProfileModel from "./ProfileModel";

const Profile = () => {
  const { kakao } = window;
  const [address, setAddress] = useState("");
  const [tabValue, setTabValue] = useState("1");
  const { auth, twit, theme } = useSelector((store) => store);
  const [openProfileModel, setOpenProfileModel] = useState(false);
  const [openSnackBar, setOpenSnackBar] = useState(false);
  const [followersClicked, setFollowersClicked] = useState(false);
  const [followingsClicked, setFollowingsClicked] = useState(false);
  const followersListRef = useRef(null);
  const [showLocation, setShowLocation] = useState(false);
  const [isLocationSaved, setIsLocationSaved] = useState(false);
  const param = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const handleBack = () => {
    navigate(-1);
  };
  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
    if (newValue === "4") {
      dispatch(findTwitsByLikesContainUser(param.id));
    } else if (newValue === "1") {
      dispatch(getUsersTweets(param.id));
    } else if (newValue === "2") {
      dispatch(getUsersReplies(param.id));
    }
  };

  useEffect(() => {
    dispatch(getUsersTweets(param.id));
  }, [param.id, twit.retwit]);

  useEffect(() => {
    dispatch(findUserById(param.id));
  }, [param.id, auth.user]);

  useEffect(() => {
    setOpenSnackBar(auth.updateUser);
  }, [auth.updateUser]);

  const handleCloseProfileModel = () => setOpenProfileModel(false);

  const handleOpenProfileModel = () => setOpenProfileModel(true);

  const handleCloseSnackBar = () => setOpenSnackBar(false);

  const handleFollowUser = () => {
    dispatch(FollowUserAction(param.id));
  };

  const handleNavigateToTwit = (i) => {
    navigate(`/twit/${i.id}`);
    dispatch(viewPlus(i.id));
  };

  const navigateToProfile = (id) => {
    navigate(`/profile/${id}`);
  };

  const handleFollowersClick = () => {
    setFollowersClicked(!followersClicked);
  };

  const handleFollowingsClick = () => {
    setFollowingsClicked(!followingsClicked);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        followersListRef &&
        followersListRef.current &&
        !followersListRef.current.contains(event.target) &&
        !event.target.classList.contains("text-gray-500") // 예외를 추가하여 리스트 항목 클릭 시 숨기지 않음
      ) {
        setFollowersClicked(false);
        setFollowingsClicked(false);
      }
    };

    document.addEventListener("click", handleClickOutside);

    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [auth.user]);

  const MapLocation = () => {
    const [searchKeyword, setSearchKeyword] = useState("");
    const [map, setMap] = useState(null);
    const [searchResults, setSearchResults] = useState([]);
    const [markers, setMarkers] = useState([]);
    const infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
    const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
    const itemsPerPage = 10;
    const [currentMarkers, setCurrentMarkers] = useState([]);
    const [hoveredMarkerIndex, setHoveredMarkerIndex] = useState(null);

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
            <h5>{places.place_name}</h5>
            {places.road_address_name ? (
              <div>
                <span>{places.road_address_name}</span>
                <span className="jibun gray">{places.address_name}</span>
              </div>
            ) : (
              <span>{places.address_name}</span>
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
              '<div style="padding:5px;font-size:12px;">' +
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
          '<div style="padding:5px;font-size:12px;">' +
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
      <div className="map_wrap">
        <div
          id="map"
          style={{
            width: "100%",
            height: "100%",
            position: "relative",
            overflow: "hidden",
          }}
        ></div>

        <div id="menu_wrap" className="bg_white">
          <div className="option">
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
                <button type="submit">검색하기</button>
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
            <ul className="page-numbers">
              {pageNumbers.map((number) => (
                <li key={number} onClick={() => handlePageClick(number)}>
                  {number}
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    );
  };

  const toggleMap = (values) => {
    console.log(values);
    if (isLocationSaved) {
      // 이미 위치가 저장된 경우, 저장된 위치를 업데이트하거나 다른 작업 수행
      // 여기서는 updateUserProfile(values)를 호출하여 위치를 업데이트합니다.
      dispatch(updateUserProfile(values));
    } else {
      setShowLocation(!showLocation);
    }
    setIsLocationSaved(!isLocationSaved); // 저장 상태를 토글
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

  return (
    <React.Fragment>
      <section
        className={`z-50 flex items-center sticky top-0 ${
          theme.currentTheme === "light" ? "light" : "dark"
        } bg-opacity-95`}
      >
        <KeyboardBackspaceIcon
          className="cursor-pointer"
          onClick={handleBack}
        />
        <h1 className="py-5 text-xl font-bold opacity-90 ml-5">
          {auth.findUser?.fullName}
        </h1>
      </section>
      <section>
        <img
          className="w-[100%] h-[15rem] object-cover"
          src={
            auth.findUser?.backgroundImage ||
            "https://png.pngtree.com/thumb_back/fw800/background/20230304/pngtree-green-base-vector-smooth-background-image_1770922.jpg"
          }
          alt=""
        />
      </section>

      <section className="pl-6">
        <div className=" flex justify-between items-start mt-5 h-[5rem]">
          <Avatar
            alt="Avatar"
            src={auth.findUser?.image}
            className="transform -translate-y-24 "
            sx={{ width: "10rem", height: "10rem", border: "4px solid white" }}
          />
          {auth.findUser?.req_user ? (
            <Button
              onClick={handleOpenProfileModel}
              sx={{ borderRadius: "20px" }}
              variant="outlined"
              className="rounded-full"
            >
              프로필 변경
            </Button>
          ) : (
            <Button
              onClick={handleFollowUser}
              sx={{ borderRadius: "20px" }}
              variant="outlined"
              className="rounded-full"
            >
              {auth.findUser?.followed ? "Unfollow" : "Follow"}
            </Button>
          )}
        </div>
        <div>
          <div>
            <div className="flex items-center">
              <h1 className="font-bold text-lg">{auth.findUser?.fullName}</h1>
              {auth.findUser?.verified && (
                <img
                  className="ml-2 w-5 h-5"
                  src="https://abs.twimg.com/responsive-web/client-web/verification-card-v2@3x.8ebee01a.png"
                  alt=""
                />
              )}
            </div>
            <h1 className="text-gray-500">
              @{auth.findUser?.fullName?.toLowerCase()}
            </h1>
          </div>
          <div className="mt-2 space-y-3">
            {auth.findUser?.bio && <p>{auth.findUser?.bio}</p>}
            <div className="py-1 flex space-x-5">
              {auth.findUser?.education ? (
                <div className="flex items-center text-gray-500">
                  <>
                    <BusinessCenterSharp />
                    <p className="ml-2">{auth.findUser.education}</p>
                  </>
                </div>
              ) : null}

              <div className="flex items-center text-gray-500">
                <form onSubmit={formik.handleSubmit}>
                  <button onClick={toggleMap}>
                    <LocationOnIcon />
                  </button>
                </form>
                <p className="text-gray-500">
                  {auth.findUser?.location || address}
                </p>
              </div>

              {auth.findUser?.joinedAt ? (
                <div className="flex items-center text-gray-500">
                  <>
                    <CalendarMonthIcon />
                    <p className="ml-2">
                      {`${auth.findUser.joinedAt?.substr(0, 4) || ""}년 ${
                        auth.findUser.joinedAt?.substring(5, 7) || ""
                      }월 ${
                        auth.findUser.joinedAt?.substring(8, 10) || ""
                      }일에 가입함`}
                    </p>
                  </>
                </div>
              ) : null}
            </div>
            <div className="flex items-center space-x-5">
              <div className="flex items-center space-x-1 font-semibold">
                <span onClick={handleFollowingsClick} className="text-gray-500">
                  {auth.findUser?.followings?.length} followings
                </span>

                {followingsClicked && (
                  <div
                    ref={followersListRef}
                    className={` overflow-y-scroll hideScrollbar absolute z-50 bg-white border rounded-md p-3 w-30 `}
                    // ${
                    //   theme.currentTheme === "light"
                    //     ? "bg-white"
                    //     : "bg-[#151515] border"
                    // }
                  >
                    {auth.findUser?.followings &&
                      auth.findUser?.followings.map((item) => (
                        <div
                          onClick={() => {
                            if (Array.isArray(item)) {
                              item.forEach((i) => handleNavigateToTwit(i));
                            } else {
                              navigateToProfile(item.id);
                            }
                            handleFollowingsClick();
                          }}
                          className="flex items-center hover:bg-slate-800 p-3 cursor-pointer"
                          key={item.id}
                        >
                          <Avatar alt={item.fullName} src={item.image} />
                          <div className="ml-2">
                            <p>{item.fullName}</p>
                            <p className="text-sm text-gray-400">
                              @
                              {item.fullName.split(" ").join("_").toLowerCase()}
                            </p>
                          </div>
                        </div>
                      ))}
                  </div>
                )}
              </div>

              <div className="flex items-center space-x-1 font-semibold">
                <span onClick={handleFollowersClick} className="text-gray-500">
                  {auth.findUser?.followers?.length} followers
                </span>

                {followersClicked && (
                  <div
                    ref={followersListRef}
                    className={` overflow-y-scroll hideScrollbar absolute z-50 bg-white border rounded-md p-3 w-30 
                    ${
                      theme.currentTheme === "light"
                        ? "bg-white"
                        : "bg-[#151515] border"
                    }`}
                  >
                    {auth.findUser?.followers &&
                      auth.findUser?.followers.map((item) => (
                        <div
                          onClick={() => {
                            if (Array.isArray(item)) {
                              item.forEach((i) => handleNavigateToTwit(i));
                            } else {
                              navigateToProfile(item.id);
                            }
                            handleFollowersClick();
                          }}
                          className="flex items-center hover:bg-slate-800 p-3 cursor-pointer"
                          key={item.id}
                        >
                          <Avatar alt={item.fullName} src={item.image} />
                          <div className="ml-2">
                            <p>{item.fullName}</p>
                            <p className="text-sm text-gray-400">
                              @
                              {item.fullName.split(" ").join("_").toLowerCase()}
                            </p>
                          </div>
                        </div>
                      ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </section>
      {showLocation && <MapLocation />}
      <section>
        <Box sx={{ width: "100%", typography: "body1", marginTop: "20px" }}>
          <TabContext value={tabValue}>
            <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
              <TabList
                onChange={handleTabChange}
                aria-label="lab API tabs example"
              >
                <Tab label="리빗" value="1" />
                <Tab label="댓글" value="2" />
                <Tab label="미디어" value="3" />
                <Tab label="좋아요" value="4" />
              </TabList>
            </Box>

            <TabPanel value="1">
              {twit.twits?.map((item) => (
                <div>
                  <TwitCard twit={item} />
                  <Divider sx={{ margin: "2rem 0rem" }} />{" "}
                </div>
              ))}
            </TabPanel>

            <TabPanel value="2">
              {twit.twits?.map((item) => (
                <div>
                  <TwitCard twit={item} />
                  <Divider sx={{ margin: "2rem 0rem" }} />{" "}
                </div>
              ))}
            </TabPanel>

            <TabPanel value="3">
              {twit.twits
                .filter((item) => item.image || item.video)
                .map((item) => (
                  <div>
                    <TwitCard twit={item} />
                    <Divider sx={{ margin: "2rem 0rem" }} />{" "}
                  </div>
                ))}
            </TabPanel>

            <TabPanel value="4">
              {twit.likedTwits?.map((item) => (
                <TwitCard twit={item} />
              ))}
            </TabPanel>
          </TabContext>
        </Box>
      </section>
      <section>
        <ProfileModel
          open={openProfileModel}
          handleClose={handleCloseProfileModel}
          // location={address}
        />
      </section>
      <section>
        <Backdrop
          sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
          open={twit.loading}
        >
          <CircularProgress color="inherit" />
        </Backdrop>
      </section>
      <section>
        <SnackbarComponent
          handleClose={handleCloseSnackBar}
          open={openSnackBar}
          message={"user updated successfully"}
        />
      </section>
    </React.Fragment>
  );
};

export default Profile;
