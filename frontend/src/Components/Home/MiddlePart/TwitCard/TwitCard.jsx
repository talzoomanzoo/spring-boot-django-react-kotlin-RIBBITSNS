import BarChartIcon from "@mui/icons-material/BarChart";
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import FileUploadIcon from "@mui/icons-material/FileUpload";
import FmdGoodIcon from "@mui/icons-material/FmdGood";
import ImageIcon from "@mui/icons-material/Image";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import RepeatIcon from "@mui/icons-material/Repeat";
import SlideshowIcon from "@mui/icons-material/Slideshow";
import TagFacesIcon from "@mui/icons-material/TagFaces";
import {
  Avatar,
  Button,
  Menu,
  MenuItem,
  TextareaAutosize,
} from "@mui/material";
import EmojiPicker from "emoji-picker-react";
import { useFormik } from "formik";
import React, { useEffect, useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import * as Yup from "yup";
import { css } from '@emotion/react';
import {
  createRetweet,
  createTweet,
  deleteTweet,
  getTime,
  likeTweet,
  updateTweet,
  viewPlus,
} from "../../../../Store/Tweet/Action";
import { uploadToCloudinary } from "../../../../Utils/UploadToCloudinary";
import BackdropComponent from "../../../Backdrop/Backdrop";
import ReplyModal from "./ReplyModal";
import { BounceLoader } from 'react-spinners';//npm install react-spinners --save 명령어로 설치진행
import axios from 'axios';
import { api } from "../../../../Config/apiConfig";
import {
  UPDATE_TWEET_FAILURE,
  UPDATE_TWEET_REQUEST,
  UPDATE_TWEET_SUCCESS,

} from "../../../../Store/Tweet/ActionType";

const validationSchema = Yup.object().shape({
  content: Yup.string().required("Tweet text is required"),
});

const TwitCard = ({ twit }) => {
  const [selectedImage, setSelectedImage] = useState(twit.image);
  const [selectedVideo, setSelectedVideo] = useState(twit.video);
  const [uploadingImage, setUploadingImage] = useState(false);
  const [openEmoji, setOpenEmoji] = useState(false);
  const handleOpenEmoji = () => setOpenEmoji(!openEmoji);
  const handleCloseEmoji = () => setOpenEmoji(false);

  const dispatch = useDispatch();
  const { auth } = useSelector((store) => store);
  const [isLiked, setIsLiked] = useState(twit.liked);
  const [likes, setLikes] = useState(twit.totalLikes);

  const [isEditing, setIsEditing] = useState(false); // 편집 상태를 관리하는 상태 변수
  const [editedContent, setEditedContent] = useState(twit.content); // 편집된 내용을 관리하는 상태 변수

  const [sentence, setSentence] = useState(twit.sentence);//sentence는 윤리수치에 해당하는 문장이 담아진다.
  const [isLoading, setIsLoading] = useState(false);//로딩창의 띄어짐의 유무를 판단한다. default는 true이다.
  const jwtToken = localStorage.getItem("jwt");

  const [isEdited, setIsEdited] = useState(twit.edited);
  const [datetime, setDatetimes] = useState(twit.createdAt);
  const [edittime, setEdittimes] = useState(twit.editedAt);
  const [isRetwit, setIsRetwit] = useState(
    twit.retwitUsersId.includes(auth.user.id)
  );
  const [retwit, setRetwit] = useState(twit.totalRetweets);
  const [openReplyModel, setOpenReplyModel] = useState();
  const location = useLocation();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const openDeleteMenu = Boolean(anchorEl);

  const handleOpenDeleteMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleCloseDeleteMenu = () => {
    setAnchorEl(null);
  };

  const handleLikeTweet = (num) => {
    dispatch(likeTweet(twit.id));
    setIsLiked(!isLiked);
    setLikes(likes + num);
  };
  const handleCreateRetweet = () => {
    dispatch(createRetweet(twit.id));
    setRetwit(isRetwit ? retwit - 1 : retwit + 1);
    setIsRetwit(!retwit);
  };
  const handleCloseReplyModel = () => setOpenReplyModel(false);

  const handleOpenReplyModel = () => setOpenReplyModel(true);
  //const handleNavigateToTwitDetial = () => navigate(`/twit/${twit.id}`);

  const handleNavigateToTwitDetial = () => {
    if (!isEditing) {
      navigate(`/twit/${twit.id}`);
      dispatch(viewPlus(twit.id));
      window.location.reload();
    }
  };

  const handleDeleteTwit = async () => {
    try {
      dispatch(deleteTweet(twit.id));
      handleCloseDeleteMenu();
      window.location.reload();
      
      const currentId = window.location.pathname.replace(/^\/twit\//, "");
      if (location.pathname === `/twit/${currentId}`) {
        window.location.reload();
      } else {
        navigate(".", { replace: true });
      }
    } catch (error) {
      console.error("게시글 삭제 중 오류 발생:", error);
    }
  };

  const handleEditClick = () => {
    setIsEditing(true); // 편집 모드로 변경
  };

  const handleCloseEditClick = () => {
    setIsEditing(false);
  };

  //const [isEditing, setIsEditing] = useState(false); // 편집 상태를 관리하는 상태 변수
  //const [editedContent, setEditedContent] = useState(twit.content); // 편집된 내용을 관리하는 상태 변수

  //const [isEdited, setIsEdited] = useState(twit.isEdited);
  //const [edittimes, setEdittimes] = useState(twit.editedAt);

  const updateTweet = (twit) => {
    return async (dispatch) => {
      console.log("twitContent", twit.content); // 넘어 온 것 확인
      console.log("tr", twit);
      dispatch({type:UPDATE_TWEET_REQUEST});
      try {
        const {data} = await api.post(`/api/twits/edit`, twit);
        console.log("edited twit", data)
        console.log("data.id: ",data.id);
        console.log("data.id: ",data.content);
  
        //const response = await ethicreveal(data.id,data.content);
        dispatch({type:UPDATE_TWEET_SUCCESS,payload:data});
      } catch (error) {
        dispatch({type:UPDATE_TWEET_FAILURE,payload:error.message});
      }
    }
  }

  const handleSaveClick = async () => {
    try {
      setIsLoading(true);
      const currentTime = new Date();
      setEditedContent(editedContent);
      setSelectedImage(selectedImage);
      setSelectedVideo(selectedVideo);
      setIsEdited(true);
      setEdittimes(currentTime);
      //setSentence(sentence);

      twit.content = editedContent;
      twit.image = selectedImage;
      twit.video = selectedVideo;
      twit.edited = true;
      twit.editedAt = currentTime;
      //twit.sentence = sentence;
      //console.log("currTime", currentTime);

      await ethicreveal(twit.id, twit.content);
      await dispatch(updateTweet(twit));

      setEditedContent("");
      setSelectedImage("");
      setSelectedVideo("");
      setIsEditing(false);
      console.log("edit test", twit);
      //window.location.reload();
      setIsLoading(false);
      handleCloseEditClick();
    } catch (error) {
      //console.error("Error updating twit:", error);
    }
  };

  const ethicreveal = async(twitid,twitcontent)=>{
    try {
      const response = await fetch("http://localhost:8080/api/ethic/reqsentence",{
        method:'POST',
        headers:{
          'Content-Type': 'application/json',
          'Authorization':`Bearer ${jwtToken}`,
        },
        body: JSON.stringify({
          id: twitid,
          content: twitcontent,
        }),
      });
      console.log("response: ",response);
      console.log("jwt: ",jwtToken);
      if(response.status===200){
        const responseData = await response.json();
        setSentence(responseData.sentence);
      }
    } catch (error) {
      console.error("Error fetching ethic data:", error);
    }
  
  };

  const handleCancelClick = () => {
    // 편집 모드를 종료하고 편집 상태를 초기화합니다.
    setIsEditing(false);
    setEditedContent(twit.content); // 수정된 내용 초기화
    handleCloseEditClick();
  };

  const handleSubmit = (values, actions) => {
    dispatch(createTweet(values));
    actions.resetForm();
    setSelectedImage("");
    setSelectedVideo("");
    handleCloseEmoji();
  };

  const formik = useFormik({
    initialValues: {
      content: "",
      image: "",
      video: "",
    },
    validationSchema,
    onSubmit: handleSubmit,
  });

  const handleEmojiClick = (value) => {
    const { emoji } = value;
    formik.setFieldValue("content", formik.values.content + emoji);
    const newContent = editedContent + emoji;
    setEditedContent(newContent);
  };

  const handleSelectImage = async (event) => {
    setUploadingImage(true);
    const imgUrl = await uploadToCloudinary(event.target.files[0], "image");
    //console.log("e.tar.val.I", event.target.value);
    formik.setFieldValue("image", imgUrl);
    setSelectedImage(imgUrl);
    setUploadingImage(false);
  };

  const handleSelectVideo = async (event) => {
    setUploadingImage(true);
    const videoUrl = await uploadToCloudinary(event.target.files[0], "video");
    //console.log("e.tar.val.V", event.target.value);
    formik.setFieldValue("video", videoUrl);
    setSelectedVideo(videoUrl);
    setUploadingImage(false);
  };

  const currTimestamp = new Date().getTime();
  const datefinal = new Date(datetime).getTime();
  const timeAgo = getTime(datefinal, currTimestamp);

  const dateedit = new Date(edittime).getTime();
  //console.log("dateedit", dateedit);
  const timeEdit = getTime(dateedit, currTimestamp);
  //console.log("timeedit", timeEdit);

  console.log("twitTest", twit);
  return (
    
    <div className="">
      {isLoading && (
        <div>
          Loading...
        </div>
      )}
      {auth.user?.id !== twit.user.id &&
      // auth.user notnull 일때, auth.user.id 가 twit.user.id 와 일치하지 않고,
        location.pathname === `/profile/${auth.user?.id}` && (
          // 현재 url의 pathname이 /profile/${auth.user?.id} 이면
          <div className="flex items-center font-semibold text-gray-700 py-2">
            <RepeatIcon />
            <p className="ml-3">You Retweet</p>
          </div>
          // 해당 표시를 해라
        )}
        
      <div className="flex space-x-5 "> 
        <Avatar
          onClick={() => navigate(`/profile/${twit.user.id}`)}
          alt="Avatar"
          src={twit.user.image}
          className="cursor-pointer"
        />
        <div className="w-full">
          <div className="flex justify-between items-center ">
            <div
              onClick={() => navigate(`/profile/${twit.user.id}`)}
              className="flex cursor-pointer items-center space-x-2"
            >
              <span className="font-semibold">{twit.user.fullName}</span>
              <span className=" text-gray-600">
                {twit.edited === true ? (
                  <p>
                    @{twit.user.fullName.toLowerCase().split(" ").join("_")} ·{" "}
                    {timeAgo} {"· " + timeEdit + " 수정됨"}
                  </p>
                ) : (
                  <p>
                    @{twit.user.fullName.toLowerCase().split(" ").join("_")} ·{" "}
                    {timeAgo}
                  </p>
                )}
              </span>
              {twit.user.verified && (
                <img
                  className="ml-2 w-5 h-5"
                  src="https://abs.twimg.com/responsive-web/client-web/verification-card-v2@3x.8ebee01a.png"
                  alt=""
                />
              )}
            </div>
            <div>
              <Button onClick={handleOpenDeleteMenu}>
                <MoreHorizIcon
                  id="basic-button"
                  aria-controls={openDeleteMenu ? "basic-menu" : undefined}
                  aria-haspopup="true"
                  aria-expanded={openDeleteMenu ? "true" : undefined}
                />
              </Button>

              <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={openDeleteMenu}
                onClose={handleCloseDeleteMenu}
                MenuListProps={{
                  "aria-labelledby": "basic-button",
                }}
              >
                {isEditing ? (
                  <div>
                    <MenuItem onClick={handleSaveClick}>Save</MenuItem>
                    <MenuItem onClick={handleCancelClick}>Cancel</MenuItem>
                  </div>
                ) : (
                  <div>
                    {twit.user.id === auth.user.id && (
                      <MenuItem onClick={handleDeleteTwit}>Delete</MenuItem>
                    )}
                    {twit.user.id === auth.user.id && (
                      <MenuItem onClick={handleEditClick}>Edit</MenuItem>
                    )}
                    <MenuItem onClick={handleNavigateToTwitDetial}>
                      Details
                    </MenuItem>
                  </div>
                )}
              </Menu>
            </div>
          </div>

          <div className="mt-2 ">
            <div
              className="cursor-pointer"
              onClick={handleNavigateToTwitDetial}
            >
              {isEditing ? (
                <div>
                  <TextareaAutosize
                    minRows={0}
                    maxRows={0}
                    value={editedContent}
                    onChange={(e) => setEditedContent(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter" && !e.shiftKey) {
                        e.preventDefault(); // 엔터 키의 기본 동작을 막음
                        handleSaveClick(); // Save 이벤트를 발생시킴
                      } else if (e.key === "Enter" && e.shiftKey) {
                      }
                    }}
                    style={{ width: "450px" }}
                  />
                  {!uploadingImage && (
                    <div>
                      {selectedImage && ( // 편집 모드일 때 이미지가 선택된 경우에만 표시
                        <img
                          className="w-[28rem] border border-gray-400 p-5 rounded-md"
                          src={selectedImage}
                          alt=""
                        />
                      )}
                      {selectedVideo && (
                        <div className="flex flex-col items-center w-full border border-gray-400 rounded-md">
                          <video
                            className="max-h-[40rem] p-5"
                            controls
                            src={selectedVideo}
                          />
                        </div>
                      )}
                    </div>
                  )}
                </div>
              ) : (
                <div>
                  <p className="mb-2 p-0 ">
                    {isEditing ? editedContent : twit.content}
                  </p>
                  
                  {sentence &&(
                    <p>{sentence}</p>
                  )}
                  
                  {twit.image && (
                    <img
                      className="w-[28rem] border border-gray-400 p-5 rounded-md"
                      src={twit.image}
                      alt=""
                    />
                  )}
                  {twit.video && (
                    <div className="flex flex-col items-center w-full border border-gray-400 rounded-md">
                      <video
                        className="max-h-[40rem] p-5"
                        controls
                        src={twit.video}
                      />
                    </div>
                  )}
                </div>
              )}
            </div>

            <div className="flex justify-between items-center mt-5">
              <div className="flex space-x-5 items-center">
                {isEditing && (
                  <>
                    <label className="flex items-center space-x-2 rounded-md cursor-pointer">
                      <ImageIcon className="text-[#1d9bf0]" />
                      <input
                        type="file"
                        name="imageFile"
                        className="hidden"
                        onChange={handleSelectImage}
                      />
                    </label>
                    <label className="flex items-center space-x-2 rounded-md cursor-pointer">
                      <SlideshowIcon className="text-[#1d9bf0]" />
                      <input
                        type="file"
                        name="videoFile"
                        className="hidden"
                        onChange={handleSelectVideo}
                      />
                    </label>
                    <FmdGoodIcon className="text-[#1d9bf0]" />
                    <div className="relative">
                      <TagFacesIcon
                        onClick={handleOpenEmoji}
                        className="text-[#1d9bf0] cursor-pointer"
                      />
                      {openEmoji && (
                        <div className="absolute top-10 z-50 ">
                          <EmojiPicker
                            // theme={theme.currentTheme}
                            onEmojiClick={handleEmojiClick}
                            lazyLoadEmojis={true}
                          />
                        </div>
                      )}
                    </div>
                  </>
                )}
              </div>
            </div>
            <div className="py-5 flex flex-wrap justify-between items-center">
              {!isEditing && (
                <>
                  <div className="space-x-3 flex items-center text-gray-600">
                    <ChatBubbleOutlineIcon
                      className="cursor-pointer"
                      onClick={handleOpenReplyModel}
                    />
                    {twit.totalReplies > 0 && <p>{twit.totalReplies}</p>}
                    {/* twit 객체의 totalReplies 속성 값이 0보다 큰 경우에만 해당 값을 포함하는 <p> 태그로 래핑 시도*/}
                  </div>
                  <div
                    className={`${
                      isRetwit ? "text-pink-600" : "text-gray-600"
                    } space-x-3 flex items-center`}
                  >
                    <RepeatIcon
                      className={` cursor-pointer`}
                      onClick={handleCreateRetweet}
                    />
                    {retwit > 0 && <p>{retwit}</p>}
                  </div>
                  <div
                    className={`${
                      isLiked ? "text-pink-600" : "text-gray-600"
                    } space-x-3 flex items-center `}
                  >
                    {isLiked ? (
                      <FavoriteIcon onClick={() => handleLikeTweet(-1)} />
                    ) : (
                      <FavoriteBorderIcon onClick={() => handleLikeTweet(1)} />
                    )}
                    {likes > 0 && <p>{likes}</p>}
                  </div>
                  <div className="space-x-3 flex items-center text-gray-600">
                    <BarChartIcon />
                    <p>{twit.viewCount}</p>
                  </div>
                  <div className="flex items-center text-gray-600">
                    <FileUploadIcon />
                  </div>
                </>
              )}
            </div> 
          </div>
        </div>
      </div>

      <ReplyModal
        twitData={twit}
        open={openReplyModel}
        handleClose={handleCloseReplyModel}
      />
      
      <section>
        <BackdropComponent open={uploadingImage} />
      </section>

    </div>
  );
};

export default TwitCard;