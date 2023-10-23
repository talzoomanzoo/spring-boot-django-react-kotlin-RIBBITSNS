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
  const [map, setmap] = useState(false);

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

  const Maplocation = () => {
    const [searchKeyword, setSearchKeyword] = useState(""); // 검색어 상태
    const [map, setMap] = useState(null);
    const [notification, setNotification] = useState(null);


    useEffect(() => {
      const container = document.getElementById("map");

      if (container) {
        const options = {
          center: new kakao.maps.LatLng(37.5662952, 126.9757567), // 서울시청을 기본 중심으로 설정
          level: 3, // 초기 지도 확대 레벨
        };

        // 사용자의 현재 위치를 가져오기
        if ("geolocation" in navigator) {
          navigator.geolocation.getCurrentPosition((position) => {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;
            options.center = new kakao.maps.LatLng(latitude, longitude);

            const newMap = new kakao.maps.Map(container, options);
            setMap(newMap); // 지도 상태 업데이트

            // 나머지 코드는 그대로 유지
            const infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
            const places = new kakao.maps.services.Places();
            const searchButton = document.getElementById("search-button");

            searchButton.addEventListener("click", () => {
              places.keywordSearch(searchKeyword, function (data, status) {
                if (status === kakao.maps.services.Status.OK) {
                  const bounds = new kakao.maps.LatLngBounds();

                  for (let i = 0; i < data.length; i++) {
                    const place = data[i];
                    displayMarker(place);
                    bounds.extend(new kakao.maps.LatLng(place.y, place.x));
                  }

                  // 모든 마커가 보이도록 중심좌표와 레벨 설정
                  map.setBounds(bounds);
                } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
                  setNotification("검색 결과가 존재하지 않습니다.");
                } else if (status === kakao.maps.services.Status.ERROR) {
                  setNotification("검색 결과 중 오류가 발생했습니다.");
                }
              });
            });

            function displayMarker(place) {
              const marker = new kakao.maps.Marker({
                map: newMap,
                position: new kakao.maps.LatLng(place.y, place.x),
              });

              kakao.maps.event.addListener(marker, "click", function () {
                const markerPosition = marker.getPosition();
                newMap.setLevel(3); // 레벨을 3으로 설정
                newMap.setCenter(markerPosition); // 클릭한 마커를 중심으로 지도 재설정

                infowindow.setContent(
                  '<div style="padding:5px;font-size:12px;">' +
                    place.place_name +
                    "</div>"
                );
                infowindow.open(newMap, marker);
              });
            }
          });
        }
      }
    }, [searchKeyword]);

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

    const handleSearchKeyPress = (event) => {
      if (event.key === "Enter") {
        // Enter 키가 눌렸을 때 검색 버튼 클릭 이벤트를 호출
        document.getElementById("search-button").click();
      } else {
        alert("키워드를 입력해주세요!");
        return;
      }
    };

    const handleSearchClick = () => {
      if (!searchKeyword.trim()) {
        alert("키워드를 입력해주세요!");
        return;
      }
    };

    useEffect(() => {
      if (notification) {
        alert(notification);
        setNotification(null); // 알림을 표시한 후 상태 초기화
      }
    }, [notification]);

    return (
      <div>
        <div>
          <input
            type="text"
            placeholder="Search for a location"
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            onKeyPress={handleSearchKeyPress} // Enter 키 핸들러 추가
          />
          <button id="search-button" onClick={handleSearchClick}>
            Search
          </button>
        </div>
        <div>
          <div id="map" style={{ width: "100%", height: "400px" }}></div>
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
      location: "",
    },
    onSubmit: toggleMap,
  });

  useEffect(() => {
    formik.setValues({
      location: auth.user.location || address,
    });
  }, [auth.user]);

  return (
    <React.Fragment>
      <section
        className={`z-50 flex items-center sticky top-0 ${
          theme.currentTheme === "light" ? "bg-white" : "bg-[#0D0D0D]"
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
            "https://cdn.pixabay.com/photo/2018/10/16/15/01/background-image-3751623_1280.jpg"
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
              Edit Profile
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

              {/* {auth.findUser?.location ? (
                <div className="flex items-center text-gray-500">
                  <>
                    <button onClick={toggleMap}>
                      <LocationOnIcon />
                    </button>
                    <p className="ml-2">{auth.findUser.location}</p>
                  </>
                </div>
              ) : null} */}

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
                    className={` overflow-y-scroll hideScrollbar absolute z-50 bg-white border rounded-md p-3 w-30 ${
                      theme.currentTheme === "light"
                        ? "bg-white"
                        : "bg-[#151515] border"
                    }`}
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
                    className={` overflow-y-scroll hideScrollbar absolute z-50 bg-white border rounded-md p-3 w-30 ${
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
      {showLocation && <Maplocation />}
      <section>
        <Box sx={{ width: "100%", typography: "body1", marginTop: "20px" }}>
          <TabContext value={tabValue}>
            <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
              <TabList
                onChange={handleTabChange}
                aria-label="lab API tabs example"
              >
                <Tab label="Tweets" value="1" />
                <Tab label="Replies" value="2" />
                <Tab label="Media" value="3" />
                <Tab label="Likes" value="4" />
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
