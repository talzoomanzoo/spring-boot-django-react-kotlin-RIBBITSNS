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

const ProfileModel = ({ handleClose,open }) => {
  const [uploading,setUploading]=useState(false);
  const dispatch=useDispatch();
  const {auth}=useSelector(store=>store);

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
      location: "",
      bio: "",
      backgroundImage:"",
      image:"",
      education:"",
      birthDate:"",
    },
    onSubmit: handleSubmit,
  });

  useEffect(()=>{

    formik.setValues({
      fullName: auth.user.fullName || "",
      website: auth.user.website || "",
      location: auth.user.location || "",
      bio: auth.user.bio || "",
      backgroundImage: auth.user.backgroundImage || "",
      image: auth.user.image || "",
      education: auth.user.education || "",
      birthDate: auth.user.birthDate || "",
    });

  },[auth.user])

  const [openInputAiKeyword, setOpenInputAiKeyword] = useState(false); // 모달 열기/닫기 상태
  const [keyword, setKeyword] = useState(""); // AI 키워드 입력 상태
  const [image, setImage] = useState(""); // 이미지 상태 추가

  const openInputAiKeywordModal = () => {//keyword를 넣는 모달 열기
    setOpenInputAiKeyword(true);
  };

  const closeInputAiKeywordModal = () => {//keyword를 넣는 모달 닫기
    setOpenInputAiKeyword(false);
  };

  const handleKeywordChange = (event) => {
    setKeyword(event.target.value);
  };

  const handleGenerateImage = async () => {

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


    } catch (error) {
      console.error('이미지 생성 요청 중 오류 발생:', error);
    }
  };

  const handleImageChange=async(event)=>{
    setUploading(true)
    const {name}=event.target;
    const file = event.target.files[0];
    const url=await uploadToCloudinary(file,"image");
    formik.setFieldValue(name,url);
    setUploading(false);

  }

  const handleAIImageChange = async (event) => {//ai이미지를 cloudinary로 업로드하는 함수이다.
    setUploading(true);

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
    setUploading(false);
  };

  return (
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
                <p>Edit Profile</p>
              </div>

              <Button type="submit">Save</Button>
            </div>

            <div className="customeScrollbar overflow-y-scroll  overflow-x-hidden h-[80vh]">
              <div className="">
                <div className="w-full">
                  <div className="relative ">
                    <img
                      src={
                        formik.values.backgroundImage ||
                        "https://cdn.pixabay.com/photo/2018/10/16/15/01/background-image-3751623_1280.jpg"
                      }
                      alt="Img"
                      className="w-full h-[12rem] object-cover object-center"
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
                    <Button onClick={() => fileInputRef.current.click()}>로컬 저장소</Button>
                    <Button onClick={openInputAiKeywordModal}>AI 이미지</Button>
                  </div>
                </div>
              </div>
              <div className="space-y-3">
                <TextField
                  fullWidth
                  id="fullName"
                  name="fullName"
                  label="Full Name"
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
                  label="Bio"
                  value={formik.values.bio}
                  onChange={formik.handleChange}
                  error={formik.touched.bio && Boolean(formik.errors.bio)}
                  helperText={formik.touched.bio && formik.errors.bio}
                />
                <TextField
                  fullWidth
                  id="website"
                  name="website"
                  label="Website"
                  value={formik.values.website}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.website && Boolean(formik.errors.website)
                  }
                  helperText={formik.touched.website && formik.errors.website}
                />
                <TextField
                  fullWidth
                  id="location"
                  name="location"
                  label="Location"
                  value={formik.values.location}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.location && Boolean(formik.errors.location)
                  }
                  helperText={formik.touched.location && formik.errors.location}
                />
                <TextField
                  fullWidth
                  id="education"
                  name="education"
                  label="Education"
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
                  label="Birth Date (XXXX-XX-XX)"
                  value={formik.values.birthDate}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.birthDate && Boolean(formik.errors.birthDate)
                  }
                  helperText={formik.touched.birthDate && formik.errors.birthDate}
                />
              </div>
            </div>
            <BackdropComponent open={uploading}/>
          
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
          <Box
            sx={{
              p: 2,
              bgcolor: "background.paper",
              borderRadius: 3,
              textAlign: "center",
            }}
          >
            <div className="image-source-options">
              <p>Enter a keyword to generate an AI image:</p>
              <input
                type="text"
                value={keyword}
                onChange={handleKeywordChange}
                placeholder="Keyword"
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
                />
                <a
                  href={`http://localhost:8080/download`}
                  download="generated_image.jpg"
                >이미지 다운로드</a>
                <button onClick={handleAIImageChange}>이미지 선택</button>
              </div>
            )}
            <button onClick={handleGenerateImage}>이미지 생성</button>
          </Box>
        </div>
      </Modal>
    </div>
  );
};

export default ProfileModel;
