import CloseIcon from "@mui/icons-material/Close";
import {
  Avatar,
  Box,
  Button,
  IconButton,
  Modal,
  TextField,
} from "@mui/material";
import { useFormik } from "formik";
import React, { useEffect, useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { updateUserProfile } from "../../Store/Auth/Action";
import { uploadToCloudinary } from "../../Utils/UploadToCloudinary";
import BackdropComponent from "../Backdrop/Backdrop";
import "./ProfileModel.css";
import axios from 'axios';
import Loading from "./Loading/Loading";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 600,
  //   height: "90vh",
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 2,
  borderRadius: 3,
  outline: "none",
  overflow: "scroll-y",
};

const ProfileModel = ({ handleClose, open }) => {
  // const [uploading,setUploading] = useState(false);
  const [ loading, setLoading ] = useState(false);
  const dispatch = useDispatch();
  const { auth, theme } = useSelector(store => store);

  const fileInputRef = useRef(null);

  const handleSubmit = (values) => {
    dispatch(updateUserProfile(values))
    console.log(values);
    handleClose()
  };
  const formik = useFormik({
    initialValues: {
      fullName: "",
      website: "",
      // location: "",
      bio: "",
      backgroundImage: "",
      image: "",
      education: "",
      birthDate: "",
    },
    onSubmit: handleSubmit,
  });

  useEffect(() => {
    formik.setValues({
      fullName: auth.user.fullName || "",
      website: auth.user.website || "",
      // location: auth.user.location || "",
      bio: auth.user.bio || "",
      backgroundImage: auth.user.backgroundImage || "",
      image: auth.user.image? auth.user.image : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png" || "",
      education: auth.user.education || "",
      birthDate: auth.user.birthDate || "",
    });

  }, [auth.user]);

  const handleOnClick = () => {
    handleGenerateImage();
  };

  const handleOnKeyPress = e => {
    if (e.key === 'Enter') {
      handleOnClick();
    }
  }

  const [openInputAiKeyword, setOpenInputAiKeyword] = useState(false); // 모달 열기/닫기 상태
  const [keyword, setKeyword] = useState(""); // AI 키워드 입력 상태
  const [image, setImage] = useState(""); // 이미지 상태 추가

  const openInputAiKeywordModal = () => { //keyword를 넣는 모달 열기
    setOpenInputAiKeyword(true);
  };

  const closeInputAiKeywordModal = () => { //keyword를 넣는 모달 닫기
    setOpenInputAiKeyword(false);
  };

  const handleKeywordChange = (event) => {
    setKeyword(event.target.value);
  };

  const handleGenerateImage = async () => {
    setLoading(true);
    try {
      const url = 'http://localhost:8080/sendprompt';

      const requestdata = {
        keyword: keyword,
      };

      const response = await axios.get(url, {
        params: requestdata,
      });

      const generatedImage = response.data.image_url;

      setImage(generatedImage);

      setLoading(false);
    } catch (error) {
      console.error('이미지 생성 요청 중 오류 발생:', error);
    }
  };

  const handleImageChange = async (event) => {
    // setUploading(true)
    setLoading(true);
    const { name } = event.target;
    const file = event.target.files[0];
    const url = await uploadToCloudinary(file, "image");
    formik.setFieldValue(name, url);
    // setUploading(false);
    setLoading(false);
  }

  const handleAIImageChange = async (event) => {//ai이미지를 cloudinary로 업로드하는 함수이다.
    setLoading(true);

    const response = await fetch('http://localhost:8080/webptojpg', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ karlourl: image }), // image 변수에 있는 webp URL을 전송한다..
    });

    const blob = await response.blob();
    const file = new File([blob], 'output.jpg', { type: 'image/jpeg' });
    const url = await uploadToCloudinary(file, "image");
    formik.setFieldValue("image", url);
    setLoading(false);
    closeInputAiKeywordModal();

  };

  return (
    <React.Fragment>
      <section
        className={`z-50 flex items-center sticky top-0 ${theme.currentTheme === "light" ? "light" : "dark"
          } bg-opacity-95`}
      >
        <div>
          <Modal
            open={open}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
          >
            <Box sx={style}>
              <form onSubmit={formik.handleSubmit}>
                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    <IconButton onClick={handleClose} aria-label="delete">
                      <CloseIcon />
                    </IconButton>
                    <p>프로필 변경</p>
                  </div>

                  <Button type="submit">저장</Button>
                </div>

                <div className="customeScrollbar overflow-y-scroll  overflow-x-hidden h-[80vh]">
                  <div className="">
                    <div className="w-full">
                      <div className="relative ">
                        <img
                          src={
                            formik.values.backgroundImage ||
                            "https://png.pngtree.com/thumb_back/fw800/background/20230304/pngtree-green-base-vector-smooth-background-image_1770922.jpg"
                          }
                          alt="Img"
                          className="w-full h-[12rem] object-cover object-center"
                          loading="lazy"
                        />
                        {/* Hidden file input */}
                        <input
                          type="file"
                          className="absolute top-0 left-0 w-full h-full opacity-0 cursor-pointer"
                          onChange={handleImageChange}
                          name="backgroundImage"
                        />
                      </div>
                    </div>

                    <div className="w-full transform -translate-y-20 translate-x-4 h-[6rem]">
                      <div className="relative borde ">
                        <Avatar
                          src={
                            formik.values.image
                          }
                          alt="Img"
                          sx={{
                            width: "10rem",
                            height: "10rem",
                            border: "4px solid white",
                          }}
                          loading="lazy"
                        />
                        {/* Hidden file input */}
                        <input
                          type="file"
                          ref={fileInputRef}
                          className="absolute top-0 left-0 w-[10rem] h-full opacity-0 cursor-pointer"
                          onChange={handleImageChange}
                          style={{ display: "none" }}
                          name="image"
                        />
                      </div>
                      <div style={{ position: 'absolute', top: '109px', right: '250px' }}>
                        <Button onClick={() => fileInputRef.current.click()}>내 보관함</Button>
                        <Button onClick={openInputAiKeywordModal}>AI 프로필</Button>
                      </div>
                    </div>
                  </div>
                  <div className="space-y-3">
                    <TextField
                      fullWidth
                      id="fullName"
                      name="fullName"
                      label="이름"
                      value={formik.values.fullName}
                      onChange={formik.handleChange}
                      error={formik.touched.name && Boolean(formik.errors.fullName)}
                      helperText={formik.touched.name && formik.errors.fullName}
                    />
                    <TextField
                      fullWidth
                      multiline
                      rows={4}
                      id="bio"
                      name="bio"
                      label="자기소개"
                      value={formik.values.bio}
                      onChange={formik.handleChange}
                      error={formik.touched.bio && Boolean(formik.errors.bio)}
                      helperText={formik.touched.bio && formik.errors.bio}
                    />
                    <TextField
                      fullWidth
                      id="website"
                      name="website"
                      label="링크"
                      value={formik.values.website}
                      onChange={formik.handleChange}
                      error={
                        formik.touched.website && Boolean(formik.errors.website)
                      }
                      helperText={formik.touched.website && formik.errors.website}
                    />
                    {/* <TextField
                  fullWidth
                  id="location"
                  name="location"
                  label="내 위치"
                  value={formik.values.location}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.location && Boolean(formik.errors.location)
                  }
                  helperText={formik.touched.location && formik.errors.location}
                /> */}
                    <TextField
                      fullWidth
                      id="education"
                      name="education"
                      label="학교"
                      value={formik.values.education}
                      onChange={formik.handleChange}
                      error={
                        formik.touched.education && Boolean(formik.errors.education)
                      }
                      helperText={formik.touched.education && formik.errors.education}
                    />
                    <TextField
                      fullWidth
                      id="birthDate"
                      name="birthDate"
                      label="생년월일 (XXXX-XX-XX)"
                      value={formik.values.birthDate}
                      onChange={formik.handleChange}
                      error={
                        formik.touched.birthDate && Boolean(formik.errors.birthDate)
                      }
                      helperText={formik.touched.birthDate && formik.errors.birthDate}
                    />
                  </div>
                </div>
                {loading ? <Loading/> : null}

              </form>
            </Box>

          </Modal>


          <Modal
            open={openInputAiKeyword}
            onClose={closeInputAiKeywordModal}
            aria-labelledby="image-source-modal-title"
            aria-describedby="image-source-modal-description"
          >
            <div className="image-source-modal">
            {loading ? <Loading/> : null}
              <Box
                sx={{
                  position: "absolute",
                  top: "50%",
                  left: "50%",
                  transform: "translate(-50%, -50%)",
                  width: 600,
                  //   height: "90vh",
                  bgcolor: "background.paper",
                  boxShadow: 24,
                  p: 2,
                  borderRadius: 3,
                  outline: "none",
                  overflow: "scroll-y",
                }}
              >
                <div
                  className="image-source-options">
                  <p
                    align="center"
                  >키워드를 입력하여 이미지를 생성해주세요!</p>
                  <TextField
                    style={{
                      objectFit: 'cover',
                      display: 'block',
                      margin: '0 auto',
                    }}
                    align="center"
                    type="text"
                    value={keyword}
                    onChange={handleKeywordChange}
                    placeholder="키워드 입력"
                    onKeyUp={handleOnKeyPress}
                  />
                </div>
                {image && (
                  <div>
                    <img
                      src={image}
                      alt="Generated Image"
                      style={{
                        width: '200px',
                        height: '200px',
                        objectFit: 'cover',
                        display: 'block',
                        margin: '0 auto',
                      }}
                      loading="lazy"
                    />
                    <Button
                      style={{
                        textAlign: "right",
                        objectFit: 'cover',
                        display: 'block',
                        margin: '0 auto',
                      }}
                    >
                    <a
                      href={`http://localhost:8080/download`}
                      download="generated_image.jpg"
                    >이미지 저장</a>
                    </Button>
                    <Button
                      style={{
                        textAlign: "right",
                        objectFit: 'cover',
                        display: 'block',
                        margin: '0 auto',
                      }}
                      onClick={handleAIImageChange}>이미지 선택</Button>
                  </div>
                )}
                <Button
                id="myBtn"
                  style={{
                    // color: "#008000",
                    textAlign: "center",
                    objectFit: 'cover',
                    display: 'block',
                    margin: '0 auto',
                  }}
                  onClick={handleGenerateImage}>AI 프로필 생성</Button>
              </Box>
            </div>
          </Modal>
        </div>
      </section>
    </React.Fragment >
  );
};

export default ProfileModel;
