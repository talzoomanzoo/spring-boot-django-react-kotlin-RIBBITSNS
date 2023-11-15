import CloseIcon from "@mui/icons-material/Close";
import { Box, Button, IconButton, Modal, TextField } from "@mui/material";
import { useFormik } from "formik";
import { useState } from "react";
import { Switch } from "react-native";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  createListModel,
} from "../../Store/List/Action";
import { uploadToCloudinary } from "../../Utils/UploadToCloudinary";
import BackdropComponent from "../Backdrop/Backdrop";
import Loading from "../Profile/Loading/Loading";
//npm install --save react-native-infinite-scroll --save --legacy-peer-deps
//npm install react-native-web
import "./ListCard/ListCard.css";
import "../RightPart/Scrollbar.css";
const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 600,
  height: 600,
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 2,
  borderRadius: 3,
  outline: "none",
  overflow: "scroll-y",
};

const ListsModel = ({ handleClose, open }) => {
  const [uploading, setUploading] = useState(false);
  const dispatch = useDispatch();
  const [search, setSearch] = useState("");
  const navigate = useNavigate();
  const { list, theme, auth } = useSelector((store) => store);
  const [backgroundImage, setBackgroundImage] = useState("");
  const [listName, setListName] = useState("");
  const [description, setDescription] = useState("");

  const handleSubmit = (values, actions) => {
    dispatch(createListModel(values));
    actions.resetForm();
    setListName("");
    setDescription("");
    setBackgroundImage("");
    console.log("listsmodel values", values);
    handleClose();
    window.location.reload();
  };


  const formik = useFormik({
    initialValues: {
      listName: "",
      description: "",
      backgroundImage: "",
      privateMode: false,
    },
    onSubmit: handleSubmit,
  });
  

  const handleImageChange = async (event) => {
    setUploading(true);
    const { name } = event.target;
    const file = event.target.files[0];
    const url = await uploadToCloudinary(file, "image");
    formik.setFieldValue(name, url);
    setUploading(false);
  };

  // const toggleSwitch = async () => {
  //   setIsEnabled((previousState) => !previousState);
  //   formik.setFieldValue("privateMode", isEnabled); //여기부터 고치기
  //   console.log("isEnabled", { isEnabled });
  //   //dispatch(setPrivate(list.id));
  // };

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
                <p>리스트 추가</p>
              </div>
              <Button type="submit">저장</Button>
            </div>

            <div className="customeScrollbar css-scroll overflow-y-scroll hideScrollbar overflow-x-hidden h-[55vh]">
              <div className="">
                <div className="w-full">
                  <div className="relative">
                    <img
                      src={
                        formik.values?.backgroundImage ||
                        "https://png.pngtree.com/thumb_back/fw800/background/20230304/pngtree-green-base-vector-smooth-background-image_1770922.jpg"
                      }
                      alt="Img"
                      className="w-full h-[12rem] object-cover object-center"
                      loading="lazy"
                    />
                    <input
                      type="file"
                      className="absolute top-0 left-0 w-full h-full opacity-0 cursor-pointer"
                      onChange={handleImageChange}
                      name="backgroundImage"
                    />
                  </div>
                </div>
                <div className="w-full transform -translate-y-20 translate-x-4 h-[3rem]"></div>
              </div>

              <div className="space-y-3">
                <TextField
                  fullWidth
                  id="listName"
                  label="리스트 이름"
                  value={formik.values.listName}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.listName && Boolean(formik.errors.listName)
                  }
                  helperText=""
                  placeholder="10자 이내로 작성하세요"
                  inputProps={{ maxLength: 10}}
                />
                <TextField
                  fullWidth
                  multiline
                  rows={4}
                  id="description"
                  name="description"
                  label="리스트 정보"
                  value={formik.values.description}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.description &&
                    Boolean(formik.errors.description)
                  }
                  helperText={
                    formik.touched.description && formik.errors.description
                  }
                />
              </div>

              <div className="space-y-3" style={{ marginTop: 10 }}>
                <hr
                  style={{
                    background: "grey",
                    color: "grey",
                    borderColor: "grey",
                    height: "1px",
                  }}
                />

                <div className="flex items-center justify-between font-xl">
                  {" "}
                  비공개 활성화
                  <Switch
                    name="privateMode"
                    style={{
                      marginTop: 10,
                      marginRight: 20,
                    }}
                    trackColor={{ false: "#767577", true: "#36d916" }}
                    //thumbColor={formik.values.privateMode ? "#f5dd4b" : "#f4f3f4"}
                    ios_backgroundColor="#3e3e3e"
                    //onValueChange={toggleSwitch}
                    //value={isEnabled}
                    value={formik.values.privateMode}
                    onValueChange={value => formik.setFieldValue('privateMode', value)}
                    error={
                      formik.touched.description &&
                      Boolean(formik.errors.description)
                    }
                    helperText={
                      formik.touched.description && formik.errors.description
                    }
                  />
                </div>

                <hr
                  style={{
                    marginTop: 20,
                    background: "grey",
                    color: "grey",
                    borderColor: "grey",
                    height: "1px",
                  }}
                />
              </div>
            </div>
            {uploading ? <Loading/> : null}
          </form>
        </Box>
      </Modal>
    </div>
  );
};
export default ListsModel;
