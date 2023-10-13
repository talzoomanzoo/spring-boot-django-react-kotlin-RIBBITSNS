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
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import { FollowUserAction, findUserById } from "../../Store/Auth/Action";
import {
  findTwitsByLikesContainUser,
  getUsersTweets,
  viewPlus,
} from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import SnackbarComponent from "../Snackbar/SnackbarComponent";
import ProfileModel from "./ProfileModel";

const Profile = () => {
  const [tabValue, setTabValue] = React.useState("1");
  const { auth, twit, theme } = useSelector((store) => store);
  const [openProfileModel, setOpenProfileModel] = useState();
  const [openSnackBar, setOpenSnackBar] = useState(false);
  const [followersClicked, setFollowersClicked] = useState(false);
  const [followingsClicked, setFollowingsClicked] = useState(false);
  const followersListRef = useRef(null);
  const [showLocation, setShowLocation] = useState(false);

  const param = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const handleBack = () => {
    navigate(-1);
  };
  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
    if (newValue === 4) {
      dispatch(findTwitsByLikesContainUser(param.id));
    } else if (newValue === 1) {
      dispatch(getUsersTweets(param.id));
    }
  };
  useEffect(() => {
    dispatch(getUsersTweets(param.id));
    dispatch(findTwitsByLikesContainUser(param.id));
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

  // console.log("find user ",auth.findUser)

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
  }, []);

  const Maplocation = () => {
    setShowLocation(!showLocation);
    if (showLocation) {
      // Kakao 지도 API 스크립트가 로드되면 실행될 콜백 함수
      const initKakaoMap = () => {
        const container = document.getElementById("map"); // 지도를 표시할 DOM 컨테이너
        const options = {
          center: new window.kakao.maps.LatLng(33.5563, 126.79581), // 지도 중심 좌표
          level: 3, // 지도 확대 레벨
        };

        // Kakao Map을 생성하고 표시
        const map = new window.kakao.maps.Map(container, options);
      };

      // Kakao 지도 API 스크립트가 이미 로드되었는지 확인
      if (window.kakao && window.kakao.maps) {
        // Kakao 지도 API 스크립트가 이미 로드된 경우
        initKakaoMap();
      } else {
        // Kakao 지도 API 스크립트가 아직 로드되지 않았다면, 로드될 때까지 기다린 후 지도 초기화
        const script = document.createElement("script");
        script.src =
          "//dapi.kakao.com/v2/maps/sdk.js?appkey=9f8bcc84eccd63ea9cc0b870b3f73612";
        script.async = true;
        script.onload = initKakaoMap; // Kakao 지도 API 스크립트 로드 후 초기화 함수 호출
        document.head.appendChild(script);
      }
    }
  };

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

              {auth.findUser?.location ? (
                <div className="flex items-center text-gray-500">
                  <>
                    <button onClick={Maplocation}>
                      <LocationOnIcon />
                    </button>
                    <p className="ml-2">{auth.findUser.location}</p>
                  </>
                </div>
              ) : null}
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
                <span
                  onClick={handleFollowingsClick} // followers 텍스트 클릭 시 handleFollowersClick 함수 실행
                  className="text-gray-500"
                >
                  {auth.findUser?.followings.length} followings
                </span>

                {followingsClicked && ( // followersClicked 상태에 따라 followers 리스트를 렌더링합니다.
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
                <span
                  onClick={handleFollowersClick} // followers 텍스트 클릭 시 handleFollowersClick 함수 실행
                  className="text-gray-500"
                >
                  {auth.findUser?.followers.length} followers
                </span>

                {followersClicked && ( // followersClicked 상태에 따라 followers 리스트를 렌더링합니다.
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
              {twit.twits
                // .filter((item) => item.user.id === auth.user.id)
                .filter((item) => {
                  console.log(item);
                  return item.user.id === auth.user.id;
                })
                .map((item) => (
                  <div>
                    <TwitCard twit={item} />
                    <Divider sx={{ margin: "2rem 0rem" }} />{" "}
                  </div>
                ))}
              {twit.twit?.replyTwits
                .filter((item) => {
                  console.log(item);
                  return item.user.id === auth.user.id;
                })
                .map((item, index) => (
                  <React.Fragment key={item.id}>
                    <div>
                      <TwitCard twit={item} />
                      <Divider sx={{ margin: "2rem 0rem" }} />{" "}
                    </div>

                    {index !== twit.twit?.replyTwits.length - 1 && (
                      <Divider sx={{ margin: "2rem 0rem" }} />
                    )}
                  </React.Fragment>
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
