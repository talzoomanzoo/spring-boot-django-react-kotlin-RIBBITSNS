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
  getUsersReplies,
  getUsersTweets,
  viewPlus,
} from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import SnackbarComponent from "../Snackbar/SnackbarComponent";
import Maplocation from "./Maplocation";
import ProfileModel from "./ProfileModel";

const Profile = () => {
  const [address, setAddress] = useState("");
  const [tabValue, setTabValue] = useState("1");
  const [isLocationFormOpen, setLocationFormOpen] = useState(false); 
  const { auth, twit, theme } = useSelector((store) => store);
  const [openProfileModel, setOpenProfileModel] = useState(false);
  const [openSnackBar, setOpenSnackBar] = useState(false);
  const [followersClicked, setFollowersClicked] = useState(false);
  const [followingsClicked, setFollowingsClicked] = useState(false);
  const followersListRef = useRef(null);
  const param = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleToggleLocationForm = () => {
    setLocationFormOpen((prev) => !prev); 
  };

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
  }, [auth.user]);

  const handleMapLocation = (newAddress) => {
    setAddress(newAddress);
  };

  return (
    <div>
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
        <div className="flex justify-between items-start mt-5 h-[5rem]">
          <Avatar
            alt="Avatar"
            src={auth.findUser?.image?  auth.findUser.image : "https://cdn.pixabay.com/photo/2023/10/24/01/42/01-42-37-630_1280.png"}
            className="transform -translate-y-24"
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
              {auth.findUser?.email?.toLowerCase()}
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

              <section>
                <Button
                  className="flex items-center text-gray-500"
                  onClick={handleToggleLocationForm}
                >
                  <LocationOnIcon />
                  <p className="text-gray-500">
                    {auth.findUser?.location || address}
                  </p>
                </Button>
              </section>

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
                    className={`overflow-y-scroll hideScrollbar absolute z-50 bg-white border rounded-md p-3 w-30`}
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
                    className={`overflow-y-scroll hideScrollbar absolute z-50 bg-white border rounded-md p-3 w-30`}
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
                          className="flex items-center hover-bg-slate-800 p-3 cursor-pointer"
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
      {isLocationFormOpen && (
        <Maplocation onLocationChange={handleMapLocation} />
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
                <Tab label="댓글" value="2" />
                <Tab label="미디어" value="3" />
                <Tab label="좋아요" value="4" />
              </TabList>
            </Box>
            <TabPanel value="1">
              {twit.twits?.map((item) => (
                <div>
                  <TwitCard twit={item} />
                  <Divider sx={{ margin: "2rem 0rem" }} />
                </div>
              ))}
            </TabPanel>
            <TabPanel value="2">
              {twit.twits?.map((item) => (
                <div>
                  <TwitCard twit={item} />
                  <Divider sx={{ margin: "2rem 0rem" }} />
                </div>
              ))}
            </TabPanel>
            <TabPanel value="3">
              {twit.twits
                .filter((item) => item.image || item.video)
                .map((item) => (
                  <div>
                    <TwitCard twit={item} />
                    <Divider sx={{ margin: "2rem 0rem" }} />
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
