import BarChartIcon from "@mui/icons-material/BarChart";
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import FileUploadIcon from "@mui/icons-material/FileUpload";
import FmdGoodIcon from "@mui/icons-material/FmdGood";
import ImageIcon from "@mui/icons-material/Image";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import RepeatIcon from "@mui/icons-material/Repeat";
import SlideshowIcon from "@mui/icons-material/Slideshow";
import TagFacesIcon from "@mui/icons-material/TagFaces";
import {
  Avatar,
  Button,
  Menu,
  MenuItem,
  Modal,
  TextareaAutosize,
} from "@mui/material";
import EmojiPicker from "emoji-picker-react";
import { useFormik } from "formik";
import React, { useEffect, useState } from "react";
import Linkify from "react-linkify";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
// import { ToastContainer, toast } from "react-toastify";
import GroupsIcon from "@mui/icons-material/Groups";
import ProgressBar from "@ramonak/react-progress-bar";
import "react-toastify/dist/ReactToastify.css"; // React Toastify 스타일
import * as Yup from "yup";
import { API_BASE_URL } from "../../../../Config/apiConfig";
import {
  decreaseNotificationCount,
  incrementNotificationCount,
} from "../../../../Store/Notification/Action";
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
import Loading from "../../../Profile/Loading/Loading";
import "../TwitMap.css";
import ReplyModal from "./ReplyModal";

const validationSchema = Yup.object().shape({
  content: Yup.string().required("내용이 없습니다"),
});

const TwitCard = ({ twit, changePage, sendRefreshPage }) => {
  const { com } = useSelector((store) => store);
  const [selectedImage, setSelectedImage] = useState(twit.image);
  const [selectedVideo, setSelectedVideo] = useState(twit.video);
  const [selectedLocation, setSelectedLocation] = useState(twit.location);
  const [loading, setLoading] = useState(false);
  const [openEmoji, setOpenEmoji] = useState(false);
  const handleOpenEmoji = () => setOpenEmoji(!openEmoji);
  const handleCloseEmoji = () => setOpenEmoji(false);
  const [twits, setTwits] = useState([]);
  const dispatch = useDispatch();
  const { theme, auth } = useSelector((store) => store);
  const [isLiked, setIsLiked] = useState(twit.liked);
  const [likes, setLikes] = useState(twit.totalLikes);
  const [isEditing, setIsEditing] = useState(false); // 편집 상태를 관리하는 상태 변수
  const [editedContent, setEditedContent] = useState(twit.content); // 편집된 내용을 관리하는 상태 변수

  const [ethiclabel, setEthiclabel] = useState(twit.ethiclabel);
  const [ethicrateMAX, setEthicrateMAX] = useState(twit.ethicrateMAX); //윤리수치 최대 수치
  console.log("twit: ", twit);
  //  const [isLoading, setIsLoading] = useState(false); //로딩창의 띄어짐의 유무를 판단한다. default는 true이다.
  const jwtToken = localStorage.getItem("jwt");
  const [isEdited, setIsEdited] = useState(twit.edited);
  const [datetime, setDatetimes] = useState(twit.createdAt);
  const [edittime, setEdittimes] = useState(twit.editedAt);
  const [retwit, setRetwit] = useState(twit.totalRetweets);
  const [openReplyModel, setOpenReplyModel] = useState();
  const location = useLocation();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const openDeleteMenu = Boolean(anchorEl);
  const [isLocationFormOpen, setLocationFormOpen] = useState(false);
  const [address, setAddress] = useState("");
  const [refreshTwits, setRefreshTwits] = useState(0);
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
  const [isLocationSaved, setIsLocationSaved] = useState(false);
  const [message, setMessage] = useState("");
  const [openAlertModal, setOpenAlertModal] = useState();
  const handleCloseAlertModal = () => setOpenAlertModal(false);
  const handleOpenAlertModal = () => setOpenAlertModal(true);

  // const authCheck = (auth) => {
  //   for (let i = 0; i < twit.retwitUsersId?.length; i++) {
  //     if (auth.findUser?.id === twit.retwitUsersId) {
  //       return true;
  //     }
  //   }
  // };

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
  }, [isLocationFormOpen, showLocation, sendRefreshPage]);

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
    console.log("Address Updated:", address); // 주소 확인
    formikLocation.setValues({
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

  const [isRetwit, setIsRetwit] = useState(
    twit.retwitUsersId?.includes(auth.user.id)
  );

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
    changePage();
    setIsLiked(!isLiked);
    setLikes(likes + num);
    dispatch(likeTweet(twit.id));
  };

  const handleIncrement = () => {
    const twitId = twit.id;
    dispatch(incrementNotificationCount(twitId));
  };
  const handleDecrease = () => {
    const TuserId = twit.user.id;
    dispatch(decreaseNotificationCount(TuserId));
  };

  const handleCreateRetweet = () => {
    if (auth.user.id !== twit.user.id) {
      dispatch(createRetweet(twit.id));
      setRetwit(isRetwit ? retwit - 1 : retwit + 1);
      setIsRetwit(!retwit);
      changePage();
    } else {
      handleOpenAlertModal();
    }
  };

  const handleCloseReplyModel = () => setOpenReplyModel(false);
  const handleOpenReplyModel = () => setOpenReplyModel(true);

  const handleNavigateToTwitDetial = () => {
    if (!isEditing) {
      navigate(`/twit/${twit.id}`);
      dispatch(viewPlus(twit.id));
      setRefreshTwits((prev) => prev + 1);
      changePage();
    }
  };

  const handleDeleteTwit = async () => {
    try {
      dispatch(deleteTweet(twit.id));
      handleCloseDeleteMenu();
      changePage();
      const currentId = window.location.pathname.replace(/^\/twit\//, "");
      if (location.pathname === `/twit/${currentId}`) {
        changePage();
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
    setLocationFormOpen(false);
    setIsEditing(false);
  };

  const handleSaveClick = async () => {
    setLoading(true);
    setLocationFormOpen(false);

    try {
      //const currentTime = new Date();
      setEditedContent(editedContent);
      setSelectedImage(selectedImage);
      setSelectedVideo(selectedVideo);
      setSelectedLocation(address);
      setIsEdited(true);
      //setEdittimes(currentTime);

      twit.content = editedContent;
      twit.location = address;
      twit.image = selectedImage;
      twit.video = selectedVideo;
      twit.edited = true;
      //twit.editedAt = currentTime;

      await ethicreveal(twit.id, twit.content);
      await dispatch(updateTweet(twit));

      setIsEditing(false);
      setLoading(false);
      handleCloseEditClick();
    } catch (error) {}
  };

  const ethicreveal = async (twitid, twitcontent) => {
    try {
      const response = await fetch(API_BASE_URL + "/api/ethic/reqsentence", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`,
        },
        body: JSON.stringify({
          id: twitid,
          content: twitcontent,
        }),
      });
      console.log("response.statis: ", response);
      if (response.status === 200) {
        const responseData = await response.json();
        console.log("responseData: ", response);
        setEthiclabel(responseData.ethiclabel);
        setEthicrateMAX(responseData.ethicrateMAX);
        setRefreshTwits((prev) => prev + 1);
      }
    } catch (error) {
      console.error("Error fetching ethic data:", error);
    }
  };

  const handleCancelClick = () => {
    setIsEditing(false);
    setEditedContent(twit.content); // 수정된 내용 초기화
    handleCloseEditClick();
  };

  const handleSubmit = (values, actions) => {
    dispatch(createTweet(values));
    actions.resetForm();
    setSelectedImage("");
    setSelectedVideo("");
    setSelectedLocation("");
    handleCloseEmoji();
  };

  const formikTweet = useFormik({
    initialValues: {
      content: "",
      image: "",
      video: "",
      location: address,
    },
    validationSchema,
    onSubmit: handleSubmit,
  });

  const handleEmojiClick = (value) => {
    const { emoji } = value;
    formikTweet.setFieldValue("content", formikTweet.values.content + emoji);
    const newContent = editedContent + emoji;
    setEditedContent(newContent);
  };

  const handleSelectImage = async (event) => {
    setLoading(true);
    const imgUrl = await uploadToCloudinary(event.target.files[0], "image");
    //console.log("e.tar.val.I", event.target.value);
    formikTweet.setFieldValue("image", imgUrl);
    setSelectedImage(imgUrl);
    setLoading(false);
  };

  const handleSelectVideo = async (event) => {
    setLoading(true);
    const videoUrl = await uploadToCloudinary(event.target.files[0], "video");
    //console.log("e.tar.val.V", event.target.value);
    formikTweet.setFieldValue("video", videoUrl);
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
      {auth.findUser?.id !== twit.user?.id &&
      location.pathname === `/profile/${auth.findUser?.id}` &&
      twit.retwitUsersId?.length > 0 ? (
        <div className="flex items-center font-semibold text-yellow-500 py-2">
          <RepeatIcon />

          <p className="ml-3">Reribbit</p>
        </div>
      ) : null}
      <div className="flex space-x-5 ">
        <Avatar
          onClick={() => navigate(`/profile/${twit.user?.id}`)}
          alt="Avatar"
          src={
            twit.user.image
              ? twit.user.image
              : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"
          }
          className="cursor-pointer"
          loading="lazy"
          style={{ marginTop: 13 }}
        />
        <div className="w-full" style={{ marginTop: 15, marginBottom: 15 }}>
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

              <span className="flex items-center text-gray-500">
                <LocationOnIcon />
                <p className="text-gray-500">{twit.location || address}</p>
              </span>

              <span className="flex items-center text-gray-500">
                {twit.com ? (
                  <p className="text-gray-500">
                    <GroupsIcon sx={{ marginRight: "7px" }} />
                    {twit.comName}
                  </p>
                ) : null}
              </span>

              {twit.user.verified && (
                <img className="ml-2 w-5 h-5" src="" alt="" loading="lazy" />
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
                    <MenuItem
                      onClick={() => {
                        handleNavigateToTwitDetial();
                        handleCloseDeleteMenu(); // "자세히" 클릭 시 메뉴 닫기
                      }}
                    >
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
                    className={`${
                      theme.currentTheme === "light"
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
                    <Linkify
                      componentDecorator={(
                        decoratedHref,
                        decoratedText,
                        key
                      ) => (
                        <a
                          key={key}
                          href={decoratedHref}
                          target="_blank"
                          rel="noopener noreferrer"
                          style={{ color: "blue", textDecoration: "underline" }}
                        >
                          {decoratedText}
                        </a>
                      )}
                    >
                      {isEditing ? editedContent : twit.content}
                    </Linkify>
                  </p>

                  <p>
                    {twit.isReply === false && ethiclabel === 0 && (
                      <div className="flex items-center font-bold rounded-md">
                        폭력성
                        {`${
                          ethicrateMAX < 25
                            ? "😄"
                            : ethicrateMAX < 50
                            ? "😅"
                            : ethicrateMAX < 75
                            ? "☹️"
                            : "🤬"
                        }`}
                        <ProgressBar
                          completed={ethicrateMAX}
                          width="450%"
                          margin="2px 0px 4px 4px"
                          bgColor={`${
                            ethicrateMAX < 25
                              ? "hsla(195, 100%, 35%, 0.8)"
                              : ethicrateMAX < 50
                              ? "hsla(120, 100%, 25%, 0.7)"
                              : ethicrateMAX < 75
                              ? "hsla(48, 100%, 40%, 0.8)"
                              : "red"
                          }`}
                        />
                      </div>
                    )}
                    {twit.reply === false && ethiclabel === 1 && (
                      <div className="flex items-center font-bold rounded-md">
                        선정성
                        {`${
                          ethicrateMAX < 25
                            ? "😄"
                            : ethicrateMAX < 50
                            ? "😅"
                            : ethicrateMAX < 75
                            ? "☹️"
                            : "🤬"
                        }`}
                        <ProgressBar
                          completed={ethicrateMAX}
                          width="450%"
                          margin="2px 0px 4px 4px"
                          bgColor={`${
                            ethicrateMAX < 25
                              ? "hsla(195, 100%, 35%, 0.8)"
                              : ethicrateMAX < 50
                              ? "hsla(120, 100%, 25%, 0.7)"
                              : ethicrateMAX < 75
                              ? "hsla(48, 100%, 40%, 0.8)"
                              : "red"
                          }`}
                        />
                      </div>
                    )}
                    {twit.reply === false && ethiclabel === 2 && (
                      <div className="flex items-center font-bold rounded-md">
                        욕설
                        {`${
                          ethicrateMAX < 25
                            ? "😄"
                            : ethicrateMAX < 50
                            ? "😅"
                            : ethicrateMAX < 75
                            ? "☹️"
                            : "🤬"
                        }`}
                        <ProgressBar
                          completed={ethicrateMAX}
                          width="450%"
                          margin="2px 0px 4px 4px"
                          bgColor={`${
                            ethicrateMAX < 25
                              ? "hsla(195, 100%, 35%, 0.8)"
                              : ethicrateMAX < 50
                              ? "hsla(120, 100%, 25%, 0.7)"
                              : ethicrateMAX < 75
                              ? "hsla(48, 100%, 40%, 0.8)"
                              : "red"
                          }`}
                        />
                      </div>
                    )}
                    {twit.reply === false && ethiclabel === 3 && (
                      <div className="flex items-center font-bold rounded-md">
                        차별성
                        {`${
                          ethicrateMAX < 25
                            ? "😄"
                            : ethicrateMAX < 50
                            ? "😅"
                            : ethicrateMAX < 75
                            ? "☹️"
                            : "🤬"
                        }`}
                        <ProgressBar
                          completed={ethicrateMAX}
                          width="450%"
                          margin="2px 0px 4px 4px"
                          bgColor={`${
                            ethicrateMAX < 25
                              ? "hsla(195, 100%, 35%, 0.8)"
                              : ethicrateMAX < 50
                              ? "hsla(120, 100%, 25%, 0.7)"
                              : ethicrateMAX < 75
                              ? "hsla(48, 100%, 40%, 0.8)"
                              : "red"
                          }`}
                        />
                      </div>
                    )}
                  </p>

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
                      {/* <p className="text-gray-500 ml-3">{twit.twits?.location || address}</p> */}
                      {twit.twits?.location || address}
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
            <div style={{ marginTop: 20 }}>
              {isEditing && isLocationFormOpen && showLocation && (
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
                              className={`${
                                theme.currentTheme === "light"
                                  ? ""
                                  : "text-black"
                              }`}
                            />
                            <Button type="submit">검색하기</Button>
                          </form>
                        </div>
                      </div>

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
                      isRetwit ? "text-yellow-500" : "text-gray-600"
                    } space-x-3 flex items-center`}
                  >
                    <RepeatIcon
                      className={`cursor-pointer`}
                      onClick={() => {
                        handleCreateRetweet();
                      }}
                    />
                    {retwit > 0 && <p>{retwit}</p>}
                  </div>
                  <div
                    className={`${
                      isLiked ? "text-yellow-500" : "text-gray-600"
                    } space-x-3 flex items-center `}
                  >
                    {isLiked ? (
                      <FavoriteIcon
                        onClick={() => {
                          handleLikeTweet(-1);
                        }}
                      />
                    ) : (
                      <FavoriteBorderIcon
                        onClick={() => {
                          handleLikeTweet(1);
                          handleIncrement();
                        }}
                      />
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
              marginTop: 10,
              marginBottom: 1,
              background: "hsla(0, 0%, 80%, 1)",
              color: "grey",
              borderColor: "hsl(0, 0%, 80%)",
              height: "1px",
            }}
          />
        </div>
      </div>

      <ReplyModal
        twitData={twit}
        open={openReplyModel}
        handleClose={handleCloseReplyModel}
        changePage={changePage}
      />

      <section>{loading ? <Loading /> : null}</section>

      <section>
        <Modal
          open={openAlertModal}
          handleClose={handleCloseAlertModal}
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <div
            className={`withdrawal-modal outline-none ${
              theme.currentTheme === "light" ? "bg-gray-200" : "bg-stone-950"
            }`}
            style={{ padding: "20px", borderRadius: "8px" }}
          >
            <p id="description">자신의 게시글은 리빗할 수 없습니다.</p>
            <Button
              style={{ marginLeft: "195px" }}
              onClick={handleCloseAlertModal}
            >
              확인
            </Button>
          </div>
        </Modal>
      </section>
    </div>
  );
};

export default TwitCard;
