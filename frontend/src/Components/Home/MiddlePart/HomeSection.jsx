import FmdGoodIcon from "@mui/icons-material/FmdGood";
import ImageIcon from "@mui/icons-material/Image";
import SlideshowIcon from "@mui/icons-material/Slideshow";
import TagFacesIcon from "@mui/icons-material/TagFaces";
import { Avatar, Button } from "@mui/material";
import EmojiPicker from "emoji-picker-react";
import { useFormik } from "formik";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import * as Yup from "yup";
import { api } from "../../../Config/apiConfig";
import { getAllTweets } from "../../../Store/Tweet/Action";
import { uploadToCloudinary } from "../../../Utils/UploadToCloudinary";
import BackdropComponent from "../../Backdrop/Backdrop";
import Loading from "../../Profile/Loading/Loading";
import Maplocation from "../../Profile/Maplocation";
import TwitCard from "./TwitCard/TwitCard";

import {
  TWEET_CREATE_FAILURE,
  TWEET_CREATE_REQUEST,
  TWEET_CREATE_SUCCESS,
} from "../../../Store/Tweet/ActionType";

const validationSchema = Yup.object().shape({
  content: Yup.string().required("내용이 없습니다"),
});

const createTweetRequest = () => ({
  type: TWEET_CREATE_REQUEST,
});

const createTweetSuccess = (data) => ({
  type: TWEET_CREATE_SUCCESS,
  payload: data,
});

const createTweetFailure = (error) => ({
  type: TWEET_CREATE_FAILURE,
  payload: error,
});

const HomeSection = () => {
  const [loading, setLoading] = useState(false);
  const [uploadingImage, setUploadingImage] = useState(false);
  const [selectedImage, setSelectedImage] = useState("");
  const [selectedVideo, setSelectedVideo] = useState("");
  const [isLocationFormOpen, setLocationFormOpen] = useState(false);
  const [openEmoji, setOpenEmoji] = useState(false);
  const [refreshTwits, setRefreshTwits] = useState(0); // Define setRefreshTwits
  const dispatch = useDispatch();
  const { twit, auth, theme } = useSelector((store) => store);
  const [notificationSent, setNotificationSent] = useState(false); // 푸시 알림을 이미 보냈는지 확인하는 상태 추가
  const jwtToken = localStorage.getItem("jwt");
  const [address, setAddress] = useState("");
  const handleOpenEmoji = () => setOpenEmoji(true);
  const handleCloseEmoji = () => setOpenEmoji(false);

  useEffect(() => {
    dispatch(getAllTweets());
  }, [dispatch]);

  const handleToggleLocationForm = () => {
    setLocationFormOpen((prev) => !prev);
  };

  const HomeCreateTweet = (tweetData) => {
    return async (dispatch) => {
      setLoading(true);
      dispatch(createTweetRequest());
      try {
        const { data } = await api.post(
          "http://localhost:8080/api/twits/create",
          tweetData
        );
        
        dispatch(createTweetSuccess(data));

        const response = await ethicreveal(data.id, data.content);
        handleSendPushNotification();
      } catch (error) {
        dispatch(createTweetFailure(error.message));
      } finally {
        setLoading(false); // 로딩 완료
      }
    };
  };

  const handleSubmit = (values, actions) => {
    dispatch(HomeCreateTweet(values));
    actions.resetForm();
    setSelectedImage("");
    setSelectedVideo("");
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
      location: "",
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

  const handleMapLocation = async (newAddress) => {
    setAddress(newAddress);
    formik.setFieldValue("location", newAddress);
  };

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

  return (
    <div className="space-y-5">
      <section>
        <h1 className="py-5 text-xl font-bold opacity-90">홈</h1>
      </section>
      <section className="pb-10">
        {/* ${theme.currentTheme==="dark"?" bg-[#151515] p-10 rounded-md mb-10":""} */}
        <div className="flex space-x-5 ">
          <Avatar alt="Avatar" src={auth.user?.image? auth.user.image : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"} loading="lazy" />
          <div className="w-full">
            <form onSubmit={formik.handleSubmit}>
              <div>
                <input
                  type="text"
                  name="content"
                  placeholder="뭔 일 있음?"
                  className={`border-none outline-none text-xl bg-transparent`}
                  {...formik.getFieldProps("content")}
                />
                {formik.errors.content && formik.touched.content && (
                  <div className="text-red-500">{formik.errors.content}</div>
                )}
              </div>

              {!uploadingImage && (
                <div>
                  {selectedImage && (
                    <img className="w-[28rem]" src={selectedImage} alt="" loading="lazy" />
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
          {isLocationFormOpen && (
            <Maplocation onLocationChange={handleMapLocation} />
          )}
          {loading ? <Loading /> : null}
          {twit.twits && twit.twits.length > 0 ? (
            twit.twits.map((item) => <TwitCard twit={item} key={item.id} />)
          ) : (
            <div>게시된 리빗이 없습니다.</div>
          )}
        </div>
      </section>
      <section>
        <BackdropComponent open={uploadingImage} />
      </section>
    </div>
  );
};

export default HomeSection;
