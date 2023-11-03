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
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import * as Yup from "yup";
import { api } from "../../../../Config/apiConfig";
import {
  createRetweet,
  createTweet,
  deleteTweet,
  getTime,
  likeTweet,
  viewPlus,
} from "../../../../Store/Tweet/Action";
import {
  UPDATE_TWEET_FAILURE,
  UPDATE_TWEET_REQUEST,
  UPDATE_TWEET_SUCCESS,
} from "../../../../Store/Tweet/ActionType";
import { uploadToCloudinary } from "../../../../Utils/UploadToCloudinary";
import BackdropComponent from "../../../Backdrop/Backdrop";
import Maplocation from "../../../Profile/Maplocation";
import ReplyModal from "./ReplyModal";
import Loading from "../../../Profile/Loading/Loading";

const validationSchema = Yup.object().shape({
  content: Yup.string().required("내용이 없습니다"),
});

const TwitCard = ({ twit }) => {
  const [selectedImage, setSelectedImage] = useState(twit.image);
  const [selectedVideo, setSelectedVideo] = useState(twit.video);
  // const [uploadingImage, setUploadingImage] = useState(false);
  const [loading, setLoading] = useState(false);
  const [openEmoji, setOpenEmoji] = useState(false);
  const handleOpenEmoji = () => setOpenEmoji(!openEmoji);
  const handleCloseEmoji = () => setOpenEmoji(false);

  const dispatch = useDispatch();
  const { theme, auth } = useSelector((store) => store);
  const [isLiked, setIsLiked] = useState(twit.liked);
  const [likes, setLikes] = useState(twit.totalLikes);

  const [isEditing, setIsEditing] = useState(false); // 편집 상태를 관리하는 상태 변수
  const [editedContent, setEditedContent] = useState(twit.content); // 편집된 내용을 관리하는 상태 변수

  const [sentence, setSentence] = useState(twit.sentence); //sentence는 윤리수치에 해당하는 문장이 담아진다.
  //  const [isLoading, setIsLoading] = useState(false); //로딩창의 띄어짐의 유무를 판단한다. default는 true이다.

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
  const [isLocationFormOpen, setLocationFormOpen] = useState(false);
  const [address, setAddress] = useState("");
  const [refreshTwits, setRefreshTwits] = useState(0);

  const handleMapLocation = (newAddress) => {
    setAddress(newAddress);
  };

  const handleToggleLocationForm = () => {
    setLocationFormOpen((prev) => !prev);
  };

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
    window.location.reload();
  };

  const handleCreateRetweet = () => {
    if (auth.user.id !== twit.user.id) {
      dispatch(createRetweet(twit.id));
      setRetwit(isRetwit ? retwit - 1 : retwit + 1);
      setIsRetwit(!retwit);
    } else {
      console.log("unable to create reribbit")
    }
  };

  const handleCloseReplyModel = () => setOpenReplyModel(false);
  const handleOpenReplyModel = () => setOpenReplyModel(true);
  //const handleNavigateToTwitDetial = () => navigate(`/twit/${twit.id}`);

  // useEffect(() => {
  //   dispatch(getAllTweets());
  // }, [refreshTwits]);

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
      //setRefreshTwits((prev) => prev + 1);

      const currentId = window.location.pathname.replace(/^\/twit\//, "");
      if (location.pathname === `/twit/${currentId}`) {
        window.location.reload();
        //setRefreshTwits((prev) => prev + 1);
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
      dispatch({ type: UPDATE_TWEET_REQUEST });
      try {
        const { data } = await api.post(`/api/twits/edit`, twit);

        //const response = await ethicreveal(data.id,data.content);
        dispatch({ type: UPDATE_TWEET_SUCCESS, payload: data });
      } catch (error) {
        dispatch({ type: UPDATE_TWEET_FAILURE, payload: error.message });
      }
    };
  };

  const handleSaveClick = async () => {
    setLoading(true);
    try {
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

      //setEditedContent("");
      //setSelectedImage("");
      //setSelectedVideo("");
      setIsEditing(false);
      //window.location.reload();
      //setRefreshTwits((prev) => prev + 1);
      setLoading(false);
      handleCloseEditClick();
    } catch (error) {
      //console.error("Error updating twit:", error);
    }
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
        const responseData = await response.json();
        setSentence(responseData.sentence);
        setRefreshTwits((prev) => prev + 1);
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
    setLoading(true);
    const imgUrl = await uploadToCloudinary(event.target.files[0], "image");
    //console.log("e.tar.val.I", event.target.value);
    formik.setFieldValue("image", imgUrl);
    setSelectedImage(imgUrl);
    setLoading(false);
  };

  const handleSelectVideo = async (event) => {
    setLoading(true);
    const videoUrl = await uploadToCloudinary(event.target.files[0], "video");
    //console.log("e.tar.val.V", event.target.value);
    formik.setFieldValue("video", videoUrl);
    setSelectedVideo(videoUrl);
    setLoading(false);
  };

  const currTimestamp = new Date().getTime();
  const datefinal = new Date(datetime).getTime();
  const timeAgo = getTime(datefinal, currTimestamp);

  const dateedit = new Date(edittime).getTime();
  const timeEdit = getTime(dateedit, currTimestamp);

  return (
    <div className="">
      {loading ? <Loading /> : null}
      {auth.findUser?.id !== twit.user.id &&
        location.pathname === `/profile/${auth.findUser?.id}` &&
        twit.retwitUsersId.length > 0 ?
        (
          <div className="flex items-center font-semibold text-yellow-500 py-2">
            <RepeatIcon />
            <p className="ml-3">Reribbit</p>
          </div>
        ) :
        null
      }
      <div className="flex space-x-5 ">
        <Avatar
          onClick={() => navigate(`/profile/${twit.user.id}`)}
          alt="Avatar"
          src={twit.user.image ? twit.user.image : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"}
          className="cursor-pointer"
          loading="lazy"
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
              {/* <span className="flex items-center text-gray-500">
                <LocationOnIcon />
                <p className="text-gray-500">
                  {auth.findUser?.location || address}
                </p>
              </span> */}
              {twit.user.verified && (
                <img
                  className="ml-2 w-5 h-5"
                  src=""
                  alt=""
                  loading="lazy"
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
                    <MenuItem onClick={handleSaveClick}>저장</MenuItem>
                    <MenuItem onClick={handleCancelClick}>취소</MenuItem>
                  </div>
                ) : (
                  <div>
                    {twit.user.id === auth.user.id && (
                      <MenuItem onClick={handleDeleteTwit}>삭제</MenuItem>
                    )}
                    {twit.user.id === auth.user.id && (
                      <MenuItem onClick={handleEditClick}>수정</MenuItem>
                    )}
                    <MenuItem onClick={handleNavigateToTwitDetial}>
                      자세히
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
                    className={`${theme.currentTheme === "light"
                      ? "bg-white"
                      : "bg-[#151515]"
                      }`}
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
                  {!loading && (
                    <div>
                      {selectedImage && ( // 편집 모드일 때 이미지가 선택된 경우에만 표시
                        <img
                          className="w-[28rem] border border-gray-400 p-5 rounded-md"
                          src={selectedImage}
                          alt=""
                          loading="lazy"
                        />
                      )}
                      {selectedVideo && (
                        <div className="flex flex-col items-center w-full border border-gray-400 rounded-md">
                          <video
                            className="max-h-[40rem] p-5"
                            controls
                            // autoPlay
                            // muted
                            src={selectedVideo}
                            loading="lazy"
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

                  {sentence && <p>{sentence}</p>}

                  {twit.image && (
                    <img
                      className="w-[28rem] border border-gray-400 p-5 rounded-md"
                      src={twit.image}
                      alt=""
                      loading="lazy"
                    />
                  )}
                  {twit.video && (
                    <div className="flex flex-col items-center w-full border border-gray-400 rounded-md">
                      <video
                        className="max-h-[40rem] p-5"
                        controls
                        // autoPlay
                        // muted
                        src={twit.video}
                        loading="lazy"
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
                      <ImageIcon className="text-[#42c924]" />
                      <input
                        type="file"
                        name="imageFile"
                        className="hidden"
                        onChange={handleSelectImage}
                      />
                    </label>
                    <label className="flex items-center space-x-2 rounded-md cursor-pointer">
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
            {isLocationFormOpen && (
              <Maplocation onLocationChange={handleMapLocation} />
            )}
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
                    className={`${isRetwit ? "text-yellow-500" : "text-gray-600"
                      } space-x-3 flex items-center`}
                  >
                    <RepeatIcon
                      className={` cursor-pointer`}
                      onClick={handleCreateRetweet}
                    />
                    {retwit > 0 && <p>{retwit}</p>}
                  </div>
                  <div
                    className={`${isLiked ? "text-yellow-500" : "text-gray-600"
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
          <hr
            style={{
              marginTop: 3,
              marginBottom: 10,
              background: 'grey',
              color: 'grey',
              borderColor: 'grey',
              height: '1px',
            }}
          />
        </div>
      </div>

      <ReplyModal
        twitData={twit}
        open={openReplyModel}
        handleClose={handleCloseReplyModel}
      />

      <section>
      {loading ? <Loading/> : null}
      </section>
    </div>
  );
};

export default TwitCard;
