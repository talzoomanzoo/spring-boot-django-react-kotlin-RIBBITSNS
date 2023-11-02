import AddIcon from "@mui/icons-material/Add";
import CloseIcon from "@mui/icons-material/Close";
import RemoveIcon from "@mui/icons-material/Remove";
import SearchIcon from "@mui/icons-material/Search";
import {
  Avatar,
  Box,
  Button,
  IconButton,
  Modal,
  Switch,
  TextField,
} from "@mui/material";
import { useFormik } from "formik";
import React, { memo, useEffect, useState } from "react";
import { createRoot } from "react-dom/client";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { searchUser } from "../../Store/Auth/Action";
import {
  addUserAction,
  getUserAction,
  setPrivate,
  updateListModel,
} from "../../Store/List/Action";
import { uploadToCloudinary } from "../../Utils/UploadToCloudinary";
import BackdropComponent from "../Backdrop/Backdrop";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 600,
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 2,
  borderRadius: 3,
  outline: "none",
  overflow: "scroll-y",
};

const ListsModel2 = memo(({ list, handleClose, open }) => {
  const [uploading, setUploading] = useState(false);
  const [search, setSearch] = useState("");
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { theme, auth } = useSelector((store) => store);
  const [followingsClicked, setFollowingsClicked] = useState(false);
  const showDeleteButton = list.user.id === auth.user.id;

  const handleSubmit = (values) => {
    dispatch(updateListModel(values));
    handleClose();
    window.location.reload();
  };

  const formik = useFormik({
    initialValues: {
      id: "",
      listName: "",
      description: "",
      backgroundImage: "",
    },
    onSubmit: handleSubmit,
  });

  const itemsCheck = (item) => {
    return list.followings.some((following) => following.id === item.id);
  };

  useEffect(() => {
    formik.setValues({
      id: list.id || "",
      listName: list.listName || "",
      description: list.description || "",
      backgroundImage: list.backgroundImage || "",
    });

    if (document.getElementById("element") !== null) {
      const domNode = document.getElementById("element");
      const element1 = createRoot(domNode);
      element1.render(<Element listVal={list} />);
    } else {
      console.log("not exists");
    }
  }, [list.followings, list.hasFollowedLists]);

  const handleImageChange = async (event) => {
    setUploading(true);
    const { name } = event.target;
    const file = event.target.files[0];
    const url = await uploadToCloudinary(file, "image");
    formik.setFieldValue(name, url);
    setUploading(false);
  };

  const handleSearchUser = (event) => {
    setSearch(event.target.value);
    dispatch(searchUser(event.target.value));
  };

  const navigateToProfile = (id) => {
    navigate(`/profile/${id}`);
    setSearch("");
  };

  const handleAddUser = (listId, userId) => {
    dispatch(addUserAction(listId, userId));
    dispatch(getUserAction(listId));

    if (document.getElementById("element") !== null) {
      const domNode = document.getElementById("element");
      const element1 = createRoot(domNode);
      element1.render(<Element listVal={list} />);
    } else {
      console.log("not exists");
    }

    setSearch("");
  };

  const handleFollowingsClick = () => {
    setFollowingsClicked(!followingsClicked);
  };

  const [isEnabled, setIsEnabled] = useState(list.privateMode);

  const toggleSwitch = () => {
    setIsEnabled((previousState) => !previousState);
    dispatch(setPrivate(list.id));
  };

  const Element = memo(({ listVal }) => {
    return (
      <div className="overflow-y-scroll hideScrollbar border-gray-700 h-[20vh] w-full rounded-md">
        <section className="space-y-5">
          <div
            className="flex justify-between"
            style={{ flexDirection: "column" }}
          >
            {listVal.followings?.map((item) => (
              <div className="flex justify-between items-center" key={item.id}>
                <div
                  style={{ paddingRight: 300, marginTop: 10 }}
                  onClick={() => {
                    if (Array.isArray(item)) {
                      item.forEach((i) => navigateToProfile(i));
                    } else {
                      navigateToProfile(item.id);
                    }
                    handleFollowingsClick();
                  }}
                  className="flex items-center absolute left-2 justify-between hover:bg-green-700 relative right-5 cursor-pointer"
                >
                  <Avatar alt={item.fullName} src={item.image} />
                  <div className="ml-2">
                    <p>{item.fullName}</p>
                    <p className="text-sm text-gray-400">
                      {item.email.split(" ").join("_").toLowerCase()}
                    </p>
                  </div>
                </div>
                {itemsCheck(item) ? (
                  <RemoveIcon
                    style={{ marginLeft: 30 }}
                    className="flex hover:bg-green-700 relative right-5 cursor-pointer"
                    onClick={() => {
                      handleAddUser(list.id, item.id, list);
                    }}
                  ></RemoveIcon>
                ) : (
                  <AddIcon
                    className="flex hover:bg-green-700 relative right-5 cursor-pointer"
                    onClick={() => {
                      handleAddUser(list.id, item.id, list);
                    }}
                  ></AddIcon>
                )}
              </div>
            ))}
          </div>
        </section>
      </div>
    );
  });

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
                <p>리스트 수정</p>
              </div>
              <div>
                {showDeleteButton && <Button type="submit">저장</Button>}
              </div>
            </div>

            <div className="customeScrollbar overflow-y-scroll  overflow-x-hidden h-[80vh]">
              <div className="">
                <div className="w-full">
                  <div className="relative">
                    <img
                      src={
                        formik.values.backgroundImage ||
                        "https://i.stack.imgur.com/H0xdb.png"
                      }
                      alt="Img"
                      className="w-full h-[12rem] object-cover object-center"
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

              <div
                className="relative flex items-center"
                style={{ marginTop: 20 }}
              >
                <input
                  value={search}
                  onChange={handleSearchUser}
                  type="text"
                  placeholder="사용자를 검색하여 추가하거나 삭제할 수 있습니다."
                  className={`py-3 rounded-full onutline-none text-gray-500 w-full pl-12 ${
                    theme.currentTheme === "light"
                      ? "bg-stone-300"
                      : "bg-[#151515]"
                  }`}
                />
                <span className="absolute top-0 left-0 pl-3 pt-3">
                  <SearchIcon className="text-gray-500" />
                </span>
                {search && (
                  <div
                    className={`overflow-y-scroll hideScrollbar absolute z-50 top-14  border-gray-700 h-[40vh] w-full rounded-md ${
                      theme.currentTheme === "light"
                        ? "bg-white"
                        : "bg-[#151515] border"
                    }`}
                  >
                    {auth.userSearchResult &&
                      auth.userSearchResult.map((item) => (
                        <div
                          className={` flex float items-center ${
                            theme.currentTheme === "light"
                              ? "hover:bg-[#008000]"
                              : "hover:bg-[#dbd9d9]"
                          } 
                            ${
                              theme.currentTheme === "light"
                                ? "text-black hover:text-white"
                                : "text-white  hover:text-black"
                            }`}
                        >
                          <div
                            style={{ paddingRight: 300 }}
                            onClick={() => {
                              if (Array.isArray(item)) {
                                item.forEach((i) => navigateToProfile(i));
                              } else {
                                navigateToProfile(item.id);
                              }
                            }}
                            className={`py-3 flex float-left justify-content w-full p-3 cursor-pointer`}
                            key={item.id}
                          >
                            <Avatar alt={item.fullName} src={item.image} />
                            <div className="ml-2">
                              <p>{item.fullName}</p>
                              <p className="text-sm">
                                @
                                {item.fullName
                                  .split(" ")
                                  .join("_")
                                  .toLowerCase()}
                              </p>
                            </div>
                          </div>
                          {itemsCheck(item) ? (
                            <RemoveIcon
                              style={{ marginLeft: 0 }}
                              className="flex float-right absolute right-5 cursor-pointer"
                              onClick={() => {
                                handleAddUser(list.id, item.id, list);
                              }}
                            ></RemoveIcon>
                          ) : (
                            <AddIcon
                              style={{ marginLeft: 0 }}
                              className={`flex float-right absolute right-5 cursor-pointer`}
                              onClick={() => {
                                handleAddUser(list.id, item.id, list);
                              }}
                            ></AddIcon>
                          )}
                        </div>
                      ))}
                  </div>
                )}
              </div>
              <div id="element">
                <div>
                  <Element listVal={list} />
                </div>
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
                  리스트 비공개 활성화
                  <Switch
                    trackColor={{ false: "#767577", true: "#81b0ff" }}
                    thumbColor={isEnabled ? "#f5dd4b" : "#f4f3f4"}
                    ios_backgroundColor="#3e3e3e"
                    onValueChange={toggleSwitch}
                    value={isEnabled}
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
            <BackdropComponent open={uploading} />
          </form>
        </Box>
      </Modal>
    </div>
  );
});

export default ListsModel2;
