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
    TextField,
} from "@mui/material";
import { useFormik } from "formik";
import React, { memo, useEffect, useState } from "react";
import { createRoot } from "react-dom/client";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { searchUser } from "../../Store/Auth/Action";
import {
    addUserActionCom,
    updateCom,
    addUserActionSignup,
} from "../../Store/Community/Action";
import { uploadToCloudinary } from "../../Utils/UploadToCloudinary";
import BackdropComponent from "../Backdrop/Backdrop";
import "../RightPart/Scrollbar.css";
import { Switch } from 'react-native'; // 여기서만 import 할것, switch 건들 ㄴㄴ
import "../RightPart/Scrollbar.css";
import TabContext from "@mui/lab/TabContext";
import TabList from "@mui/lab/TabList";
import TabPanel from "@mui/lab/TabPanel";
import Tab from "@mui/material/Tab";

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

const ComModel2 = ({ changeComs, com, handleClose, open }) => {
    const [uploading, setUploading] = useState(false);
    const [search, setSearch] = useState("");
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { theme, auth } = useSelector((store) => store);
    const [followingscClicked, setFollowingscClicked] = useState(false);
    const [refreshComModel, setRefreshComModel] = useState(0);

    const changeComModel = () => {
        setRefreshComModel((prev) => prev + 1);
    }
    const [tabValue, setTabValue] = useState(1);

    const handleSubmit = (values) => {
        dispatch(updateCom(values));
        handleClose();
        changeComs();
    };

    const formik = useFormik({
        initialValues: {
            id: "",
            comName: "",
            description: "",
            backgroundImage: "",
            privateMode: false,
        },
        onSubmit: handleSubmit,
    });

    const itemsCheck = (item) => {
        for (let i = 0; i < com.followingsc.length; i++) {
            if (com.followingsc[i].id === item.id) {
                return true;
            }
        }
    }

    useEffect(() => {
        formik.setValues({
            id: com.id || "",
            comName: com.comName || "",
            description: com.description || "",
            backgroundImage: com.backgroundImage || "",
            privateMode: com.privateMode || "",
        });
    }, [refreshComModel]);

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

    const handleAddUserCom = (comId, userId) => {
        dispatch(addUserActionCom(comId, userId));
        setSearch("");
        changeComModel();
        changeComs();
    };

    const handleAddUserSignup = (comId, userId) => {
        dispatch(addUserActionSignup(comId, userId));
        changeComModel();
        changeComs();
    }

    const handleFollowingscClick = () => {
        setFollowingscClicked(!followingscClicked);
    };

    const handleTabChange = (event, newValue) => {
        setTabValue(newValue);
        if (newValue === 2) {

        } else if (newValue === 1) {

        }
    }

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
                                <p>커뮤니티 관리</p>
                            </div>
                            <div>
                                <Button type="submit">저장</Button>
                            </div>
                        </div>

                        <div className="customeScrollbar overflow-y-scroll css-scroll overflow-x-hidden h-[80vh]">
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
                                    id="comName"
                                    label="커뮤니티 이름"
                                    value={formik.values.comName}
                                    onChange={formik.handleChange}
                                    error={
                                        formik.touched.comName && Boolean(formik.errors.comName)
                                    }
                                    helperText=""
                                />
                                <TextField
                                    fullWidth
                                    multiline
                                    rows={4}
                                    id="description"
                                    name="description"
                                    label="커뮤니티 정보"
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

                            <Box sx={{ width: "100%", typography: "body1", marginTop: "20px" }}>
                                <TabContext value={tabValue}>
                                    <Box sx={{ borderBottom: 1, borderColor: "divider", }}>
                                        <TabList
                                            onChange={handleTabChange}
                                            aria-label="lab API tabs example"

                                        >
                                            <Tab label="커뮤니티 사용자 검색" value={1} sx={{ marginLeft: "11%" }} />
                                            <Tab label="가입 신청 승인 대기 목록" value={2} sx={{ marginLeft: "12%" }} />
                                        </TabList>
                                    </Box>
                                    <TabPanel value={1}>
                                        <div
                                            className="relative flex items-center"
                                        >
                                            <input
                                                value={search}
                                                onChange={handleSearchUser}
                                                type="text"
                                                placeholder="사용자를 검색하여 추가하거나 삭제할 수 있습니다."
                                                className={`py-3 rounded-full onutline-none text-gray-500 w-full pl-12 ${theme.currentTheme === "light" ? "bg-stone-300" : "bg-[#151515]"
                                                    }`}
                                            />
                                            <span className="absolute top-0 left-0 pl-3 pt-3">
                                                <SearchIcon className="text-gray-500" />
                                            </span>
                                            {search && (
                                                <div
                                                    className={`css-scroll overflow-y-scroll hideScrollbar absolute z-50 top-14  border-gray-700 h-[40vh] w-full rounded-md ${theme.currentTheme === "light" ? "bg-white" : "bg-[#151515] border"
                                                        }`}
                                                >
                                                    {auth.userSearchResult &&
                                                        auth.userSearchResult.map((item) => (
                                                            <div
                                                                className={` flex float items-center `}
                                                            // ${
                                                            //   theme.currentTheme === "light"
                                                            //     ? "hover:bg-[#008000]"
                                                            //     : "hover:bg-[#dbd9d9]"
                                                            // } 
                                                            //   ${
                                                            //     theme.currentTheme === "light"
                                                            //       ? "text-black hover:text-white"
                                                            //       : "text-white  hover:text-black"
                                                            //   }
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
                                                                    className={`flex items-center left-3 justify-content hover:bg-green-700 relative right-5 cursor-pointer`}
                                                                    key={item.id}
                                                                >
                                                                    <Avatar alt={item.fullName} src={item.image} loading="lazy" />
                                                                    <div className="ml-2">
                                                                        <p>{item.fullName}</p>
                                                                        <p className="text-sm">
                                                                            @{item.fullName.split(" ").join("_").toLowerCase()}
                                                                        </p>
                                                                    </div>
                                                                </div>
                                                                {itemsCheck(item) ? (
                                                                    <RemoveIcon
                                                                        style={{ marginLeft: 30 }}
                                                                        className="flex hover:bg-green-700 float-right absolute right-5 cursor-pointer"
                                                                        onClick={() => {
                                                                            handleAddUserCom(com.id, item.id);
                                                                        }}
                                                                    ></RemoveIcon>
                                                                ) : (
                                                                    <AddIcon
                                                                        style={{ marginLeft: 0 }}
                                                                        className={`flex hover:bg-green-700 float-right absolute right-5 cursor-pointer`}
                                                                        onClick={() => {
                                                                            handleAddUserCom(com.id, item.id);
                                                                        }}
                                                                    ></AddIcon>
                                                                )}
                                                            </div>
                                                        ))}
                                                </div>
                                            )}
                                        </div>

                                        <div className="overflow-y-scroll hideScrollbar css-scroll border-gray-700 h-[20vh] w-full rounded-md">
                                            <section className="space-y-5">
                                                <div className="flex justify-between" style={{ flexDirection: "column" }}>
                                                    {com.followingsc?.map((item) => (
                                                        <div className="flex justify-between items-center" key={item.id}>
                                                            <div
                                                                style={{ paddingRight: 300, marginTop: 10, }}
                                                                onClick={() => {
                                                                    if (Array.isArray(item)) {
                                                                        item.forEach((i) => navigateToProfile(i));
                                                                    } else {
                                                                        navigateToProfile(item.id);
                                                                    }
                                                                    handleFollowingscClick();
                                                                }}
                                                                className="flex items-center absolute left-2 justify-between hover:bg-green-700 relative right-5 cursor-pointer"
                                                            >
                                                                <Avatar alt={item.fullName} src={item.image} loading="lazy" />
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
                                                                        handleAddUserCom(com.id, item.id);
                                                                    }}
                                                                ></RemoveIcon>
                                                            ) : (
                                                                <AddIcon
                                                                    className="flex hover:bg-green-700 relative right-5 cursor-pointer"
                                                                    onClick={() => {
                                                                        handleAddUserCom(com.id, item.id);
                                                                    }}
                                                                ></AddIcon>
                                                            )}
                                                        </div>
                                                    ))}
                                                </div>
                                            </section>
                                        </div>

                                    </TabPanel>
                                    <TabPanel value={2}>
                                        <div className="overflow-y-scroll css-scroll hideScrollbar border-gray-700 h-[20vh] w-full rounded-md">
                                            <section className="space-y-5">
                                                <div className="flex justify-between" style={{ flexDirection: "column" }}>
                                                    {com.followingscReady?.map((item) => (
                                                        <div className="flex justify-between items-center" key={item.id}>
                                                            <div
                                                                style={{ paddingRight: 300, marginTop: 10, }}
                                                                onClick={() => {
                                                                    if (Array.isArray(item)) {
                                                                        item.forEach((i) => navigateToProfile(i));
                                                                    } else {
                                                                        navigateToProfile(item.id);
                                                                    }
                                                                    handleFollowingscClick();
                                                                }}
                                                                className="flex items-center absolute left-2 justify-between hover:bg-green-700 relative right-5 cursor-pointer"
                                                            >
                                                                <Avatar alt={item.fullName} src={item.image} loading="lazy" />
                                                                <div className="ml-2">
                                                                    <p>{item.fullName}</p>
                                                                    <p className="text-sm text-gray-400">
                                                                        {item.email.split(" ").join("_").toLowerCase()}
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            {itemsCheck(item) ? (
                                                                null
                                                            ) : (
                                                                <AddIcon
                                                                    className="flex hover:bg-green-700 relative right-5 cursor-pointer"
                                                                    onClick={() => {
                                                                        handleAddUserSignup(com.id, item.id);
                                                                    }}
                                                                ></AddIcon>
                                                            )}
                                                        </div>
                                                    ))}
                                                </div>
                                            </section>
                                        </div>
                                    </TabPanel>
                                </TabContext>
                            </Box>

                            <div
                                className="space-y-3"
                                style={{ marginTop: 10 }}>

                                <hr
                                    style={{
                                        background: "grey",
                                        color: "grey",
                                        borderColor: "grey",
                                        height: "1px",
                                    }}
                                />

                                <div className="flex items-center justify-between font-xl">
                                    비공개 활성화
                                    <Switch
                                        name="privateMode"
                                        style={{
                                            marginTop: 10,
                                            marginRight: 20,
                                        }}
                                        trackColor={{ false: "#767577", true: "#36d916" }}
                                        // thumbColor={formik.values.privateMode ? "#f5dd4b" : "#f4f3f4"}
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
                        <BackdropComponent open={uploading} />
                    </form>
                </Box>
            </Modal>
        </div>
    );
};

export default ComModel2;