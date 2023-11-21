import { BusinessCenterSharp } from "@mui/icons-material";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import TabContext from "@mui/lab/TabContext";
import TabList from "@mui/lab/TabList";
import TabPanel from "@mui/lab/TabPanel";
import { Avatar, Box, Button, Modal } from "@mui/material";
import Tab from "@mui/material/Tab";
import React, { Suspense, useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import { FollowUserAction, findUserById } from "../../Store/Auth/Action";
import {
  findTwitsByLikesContainUser,
  getUsersReplies,
  getUsersTweets,
  viewPlus,
} from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
//import Maplocation from "./Maplocation";
//import ProfileModel from "./ProfileModel";
import CloseIcon from "@mui/icons-material/Close";
import ProgressBar from "@ramonak/react-progress-bar";
import "../RightPart/Scrollbar.css";
import Loading from "./Loading/Loading";

const Maplocation = React.lazy(() => import("./Maplocation"));
const ProfileModel = React.lazy(() => import("./ProfileModel"));

const Profile = ({sendRefreshPage, changePage}) => {
  const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 500,
    maxHeight: 500,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 2,
    borderRadius: 3,
    outline: "none",
    overflow: "scroll-y",
  };

  const [address, setAddress] = useState("");
  const [tabValue, setTabValue] = useState("1");
  const [isLocationFormOpen, setLocationFormOpen] = useState(false);
  const { auth, twit, theme } = useSelector((store) => store);
  const [openProfileModel, setOpenProfileModel] = useState(false);
  const [openSnackBar, setOpenSnackBar] = useState(false);
  const [followersClicked, setFollowersClicked] = useState(false);
  const [followingsClicked, setFollowingsClicked] = useState(false);
  const [uploading, setUploading] = useState(false);
  const followersListRef = useRef(null);
  const param = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [isLobitTab, setIsLobitTab] = useState(true);

  const handleToggleLocationForm = () => {
    setLocationFormOpen((prev) => !prev);
  };

  const handleBack = () => {
    navigate(-1);
  };

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);

    setIsLobitTab(newValue === "1");

    if (newValue === "4") {
      dispatch(findTwitsByLikesContainUser(param.id));
    } else if (newValue === "1" || "3") {
      dispatch(getUsersTweets(param.id));
    } else if (newValue === "2") {
      dispatch(getUsersReplies(param.id));
    }
  };

  useEffect(() => {
    dispatch(getUsersTweets(param.id));
  }, [param.id, twit.retwit, sendRefreshPage]);

  useEffect(() => {
    dispatch(findUserById(param.id));
  }, [param.id, auth.user, sendRefreshPage]);

  useEffect(() => {
    setOpenSnackBar(auth.updateUser);
  }, [auth.updateUser, sendRefreshPage]);

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
        !event.target.classList.contains("text-gray-500")
      ) {
        setFollowersClicked(false);
        setFollowingsClicked(false);
      }
    };

    document.addEventListener("click", handleClickOutside);

    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [auth.user, sendRefreshPage]);

  const handleMapLocation = (newAddress) => {
    setAddress(newAddress);
  };

  const [openFollowings, setOpenFollowings] = useState(false);
  const [openFollowers, setOpenFollowers] = useState(false);

  const openFollowingsModal = () => {
    setOpenFollowings(true);
  };

  const closeFollowingsModal = () => {
    setOpenFollowings(false);
  };

  const openFollowersModal = () => {
    setOpenFollowers(true);
  };

  const closeFollowersModal = () => {
    setOpenFollowers(false);
  };

  const openFollowingsCloseFollowers = () => {
    openFollowingsModal();
    closeFollowersModal();
  };

  const openFollowersCloseFollowings = () => {
    openFollowersModal();
    closeFollowingsModal();
  };

  const [totalEthicRateMAX, setTotalEthicRateMAX] = useState(0);
  const [averageEthicRateMAX, setAverageEthicRateMAX] = useState(0);

  useEffect(() => {
    // 리빗 탭에서만 totalEthicRateMAX, averageEthicRateMAX 계산
    if (isLobitTab) {
      const totalEthicRateMAXValue = twit.twits.reduce((sum, tweet) => {
        return sum + (tweet.ethiclabel === 4 ? 0 : tweet.ethicrateMAX || 0);
      },
      0
    );

    // Calculate average ethicrateMAX
    const averageEthicRateMAXValue =
    twit.twits.length > 0
      ? totalEthicRateMAXValue / twit.twits.length
      : 0;

    // 정수로 변환
    const roundedAverageEthicRateMAX = Math.floor(averageEthicRateMAXValue);

    // 상태 업데이트
    setTotalEthicRateMAX(totalEthicRateMAXValue);
    setAverageEthicRateMAX(roundedAverageEthicRateMAX);
    }
    // ... (다른 코드)
  }, [twit.twits, auth.user, sendRefreshPage]);

  return (
    <div>
      <section
        className={`z-50 flex items-center sticky top-0 ${
          theme.currentTheme === "dark" ? " bg-[#0D0D0D]" : "bg-white"
        }`}
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
          loading="lazy"
        />
      </section>
      <section className="pl-6">
        <div className="flex justify-between items-start mt-5 h-[5rem]">
          <Avatar
            alt="Avatar"
            src={
              auth.findUser?.image
                ? auth.findUser.image
                : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"
            }
            className="transform -translate-y-24"
            sx={{ width: "10rem", height: "10rem", border: "4px solid white" }}
            loading="lazy"
          />
          {auth.findUser?.req_user ? (
            <Button
              onClick={handleOpenProfileModel}
              sx={{ borderRadius: "20px", fontFamily: 'ChosunGu' }}
              variant="outlined"
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
        <p className="flex items-center text-gray-500">
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
            width="165px"
            margin="2px 0px 4px 4px"
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
        </p>
        <div>
          <div>
            <div className="flex items-center">
              <h1 className="font-bold text-lg">{auth.findUser?.fullName}</h1>
              {/* {auth.findUser?.verified && (
                <img
                  className="ml-2 w-5 h-5"
                  src="https://abs.twimg.com/responsive-web/client-web/verification-card-v2@3x.8ebee01a.png"
                  alt=""
                  loading="lazy"
                />
              )} */}
            </div>
            <h1 className="text-gray-500">
              {auth.findUser?.email?.toLowerCase()}
            </h1>
            <h1 className="text-gray-500">
              {auth.findUser?.website?.toLowerCase()}
            </h1>
          </div>
          <div className="mt-2 space-y-3">
            {auth.findUser?.bio && <p>{auth.findUser?.bio}</p>}
            <div style={{ flexDirection: "column" }} className="py-1 flex">
              {auth.findUser?.education ? (
                <div className="flex text-gray-500">
                  <>
                    <BusinessCenterSharp />
                    <p className="ml-2">{auth.findUser.education}</p>
                  </>
                </div>
              ) : null}
              {auth.findUser?.joinedAt ? (
                <div className="flex text-gray-500">
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

              <section>
                {auth.findUser?.req_user ? (
                  <button
                    style={{ color: "#008000" }}
                    className="flex text-gray-500"
                    onClick={handleToggleLocationForm}
                  >
                    <LocationOnIcon />
                    <p className="text-gray-500">
                      {auth.findUser?.location || address}
                    </p>
                  </button>
                ) : (
                  <>
                    <LocationOnIcon
                      style={{ color: "#008000" }}
                      className="flex text-gray-500"
                    />
                    <p className="text-gray-500">
                      {auth.findUser?.location || address}
                    </p>
                  </>
                )}
              </section>
            </div>
            <div className="flex items-center space-x-5">
              <div className="flex items-center space-x-1 font-semibold">
                <span onClick={openFollowingsModal} className="text-gray-500 cursor-pointer">
                  {auth.findUser?.followings?.length} followings
                </span>
                <Modal open={openFollowings} onClose={closeFollowingsModal}>
                  <Box sx={style}>
                    <Button
                      sx={{
                        fontSize: "105%",
                        marginRight: "16%",
                        marginLeft: "16%",
                        textDecoration: "underline",
                      }}
                    >
                      followings
                    </Button>
                    <Button
                      sx={{ fontSize: "75%", color: "darkgray" }}
                      onClick={openFollowersCloseFollowings}
                    >
                      followers
                    </Button>
                    <button
                      style={{ marginLeft: "12.7%" }}
                      onClick={() => closeFollowingsModal()}
                    >
                      <CloseIcon
                        className={`${
                          theme.currentTheme === "light"
                            ? "text-black"
                            : "text-white"
                        }`}
                      />
                    </button>
                    <div
                      ref={followersListRef}
                      className={`customeScrollbar overflow-y-scroll css-scroll hideScrollbar h-[40vh]`}
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
                              closeFollowingsModal();
                            }}
                            className="flex items-center hover:bg-green-700 p-3 cursor-pointer"
                            key={item.id}
                          >
                            <Avatar
                              alt={item.fullName}
                              src={item.image}
                              loading="lazy"
                            />
                            <div className="ml-2">
                              <p>{item.fullName}</p>
                              <p className="text-sm text-gray-400">
                                @
                                {item.fullName
                                  .split(" ")
                                  .join("_")
                                  .toLowerCase()}
                              </p>
                            </div>
                          </div>
                        ))}
                    </div>
                  </Box>
                </Modal>
              </div>
              <div className="flex items-center space-x-1 font-semibold">
                <span onClick={openFollowersModal} className="text-gray-500 cursor-pointer">
                  {auth.findUser?.followers?.length} followers
                </span>
                <Modal open={openFollowers} onClose={closeFollowersModal}>
                  <Box sx={style}>
                    <Button
                      sx={{
                        marginRight: "17%",
                        marginLeft: "18%",
                        fontSize: "75%",
                        color: "darkgray",
                      }}
                      onClick={openFollowingsCloseFollowers}
                    >
                      followings
                    </Button>
                    <Button
                      sx={{ fontSize: "105%", textDecoration: "underline" }}
                    >
                      followers
                    </Button>
                    <button
                      style={{ marginLeft: "10%" }}
                      onClick={() => closeFollowersModal()}
                    >
                      <CloseIcon
                        className={`${
                          theme.currentTheme === "light"
                            ? "text-black"
                            : "text-white"
                        }`}
                      />
                    </button>
                    <div
                      ref={followersListRef}
                      className={`customeScrollbar overflow-y-scroll css-scroll hideScrollbar h-[40vh] `}
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
                              closeFollowersModal();
                            }}
                            className="flex items-center hover:bg-green-700 p-3 cursor-pointer"
                            key={item.id}
                          >
                            <Avatar
                              alt={item.fullName}
                              src={item.image}
                              loading="lazy"
                            />
                            <div className="ml-2">
                              <p>{item.fullName}</p>
                              <p className="text-sm text-gray-400">
                                @
                                {item.fullName
                                  .split(" ")
                                  .join("_")
                                  .toLowerCase()}
                              </p>
                            </div>
                          </div>
                        ))}
                    </div>
                  </Box>
                </Modal>
              </div>
            </div>
          </div>
        </div>
      </section>
      {isLocationFormOpen && (
        <Suspense fallback={<div> {uploading ? <Loading /> : null} </div>}>
          <Maplocation onLocationChange={handleMapLocation} />
        </Suspense>
      )}
      <section>
        <Box sx={{ width: "100%", typography: "body1", marginTop: "20px" }}>
          <TabContext value={tabValue}>
            <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
              <TabList
                onChange={handleTabChange}
                aria-label="lab API tabs example"
              >
                <Tab label="리빗" value="1" />
                <Tab label="댓글 단 리빗" value="2" />
                <Tab label="미디어" value="3" />
                <Tab label="좋아요" value="4" />
              </TabList>
            </Box>
            <TabPanel value="1">
              {twit.twits?.map((item) => (
                <div>
                  <TwitCard twit={item} key={item.id} changePage={changePage} />
                  {/* <Divider sx={{ margin: "2rem 0rem" }} /> */}
                </div>
              ))}
            </TabPanel>
            <TabPanel value="2">
              {twit.twits?.map((item) => (
                <div>
                  <TwitCard twit={item} key={item.id} changePage={changePage} />
                  {/* <Divider sx={{ margin: "2rem 0rem" }} /> */}
                </div>
              ))}
            </TabPanel>
            <TabPanel value="3">
              {twit.twits
                .filter((item) => item.image || item.video)
                .map((item) => (
                  <div>
                    <TwitCard
                      twit={item}
                      key={item.id}
                      changePage={changePage}
                    />
                    {/* <Divider sx={{ margin: "2rem 0rem" }} /> */}
                  </div>
                ))}
            </TabPanel>
            <TabPanel value="4">
              {twit.likedTwits?.map((item) => (
                <div>
                <TwitCard twit={item} key={item.id} changePage={changePage}/>
                </div>
              ))}
            </TabPanel>
          </TabContext>
        </Box>
      </section>
      <section>
        <Suspense fallback={<div> Loading... </div>}>
          <ProfileModel
            open={openProfileModel}
            handleClose={handleCloseProfileModel}
          />
        </Suspense>
      </section>
      <section>
        {/* <Backdrop
          sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
          open={twit.loading}
        >
          <CircularProgress color="inherit" />
        </Backdrop> */}
      </section>
      {/* <section>
        <SnackbarComponent
          handleClose={handleCloseSnackBar}
          open={openSnackBar}
          message={"user updated successfully"}
        />
      </section> */}
    </div>
  );
};

export default Profile;
