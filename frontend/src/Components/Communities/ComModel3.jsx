import CloseIcon from "@mui/icons-material/Close";
import {
    Box,
    Button,
    IconButton,
    Modal,
    TextField,
} from "@mui/material";
import { useFormik } from "formik";
import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
    updateCom,
    addReady,
    removeFollow,
} from "../../Store/Community/Action";
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

const ComModel3 = ({ com, handleClose, open }) => {
    const [uploading, setUploading] = useState(false);
    const dispatch = useDispatch();
    const { auth } = useSelector((store) => store);

    const handleSubmit = (values) => {
        dispatch(updateCom(values));
        handleClose();
        window.location.reload();
    };

    const handleSignup = (comId) => {
        dispatch(addReady(comId));
        handleClose();
        window.location.reload();
    };

    const handleSignout = (comId) => {
        dispatch(removeFollow(comId));
        handleClose();
        window.location.reload();
    }

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

    useEffect(() => {
        formik.setValues({
            comName: com.comName || "",
            description: com.description || "",
            backgroundImage: com.backgroundImage || "",
        });
    }, []);

    const handleImageChange = async (event) => {
        setUploading(true);
        const { name } = event.target;
        const file = event.target.files[0];
        const url = await uploadToCloudinary(file, "image");
        formik.setFieldValue(name, url);
        setUploading(false);
    };

    const itemsCheck = () => {
        for (let i = 0; i < com.followingscReady.length; i++) {
            if (com.followingscReady[i].id === auth.user.id) {
                return true;
            }
        }
    }

    const signupCheck = () => {
        for (let i = 0; i < com.followingsc.length; i++) {
            if (com.followingsc[i].id === auth.user.id) {
                return true;
            }
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
                                <p>커뮤니티 정보</p>
                            </div>
                        </div>
                        <div className="customeScrollbar overflow-y-scroll  overflow-x-hidden h-[50vh]">
                            <div className="">
                                <div className="w-full">
                                    <div className="relative">
                                        <img
                                            src={
                                                formik.values?.backgroundImage ||
                                                "https://png.pngtree.com/thumb_back/fw800/background/20230304/pngtree-green-base-vector-smooth-background-image_1770922.jpg"
                                            }
                                            alt="Img"
                                            className="w-full h-[10rem] object-cover object-center"
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
                            </div>
                            <div className="w-full transform -translate-y-10 translate-x-4 h-[1rem]"></div>
                            <div className="space-y-3">
                                <TextField
                                    fullWidth
                                    id="comName"
                                    label="커뮤니티 이름"
                                    value={formik.values.comName}
                                    disabled="true"
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
                                    disabled="true"
                                    error={
                                        formik.touched.description &&
                                        Boolean(formik.errors.description)
                                    }
                                    helperText={
                                        formik.touched.description && formik.errors.description
                                    }
                                />
                                <div className="space-y-3" style={{ marginTop: 5 }}>

                                    <div className="flex items-center justify-between font-xl">
                                        {com.privateMode ? "비공개 커뮤니티입니다." : "공개 커뮤니티입니다."}
                                        <></>
                                        {itemsCheck() ? <span className="flex items-center">  가입승인 대기중입니다. <Button onClick={() => handleSignup(com.id)}> 가입신청 취소</Button> </span> :
                                            signupCheck() ? <span className="flex items-center">  해당 커뮤니티 회원입니다. <Button onClick={() => handleSignout(com.id)}> 커뮤니티 탈퇴 </Button> </span> :
                                                <Button onClick={() => handleSignup(com.id)}> 가입신청 </Button>}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <BackdropComponent open={uploading} />
                    </form>
                </Box>
            </Modal>
        </div>
    );
};

export default ComModel3;
