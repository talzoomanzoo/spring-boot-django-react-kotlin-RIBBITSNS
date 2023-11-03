import CloseIcon from "@mui/icons-material/Close";
import { Box, Button, IconButton, Modal, TextField } from "@mui/material";
import { useFormik } from "formik";
import { useState } from "react";
import { Switch } from "react-native";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
    createCom,
} from "../../Store/Community/Action";
import { uploadToCloudinary } from "../../Utils/UploadToCloudinary";
import BackdropComponent from "../Backdrop/Backdrop";
import Loading from "../Profile/Loading/Loading";
//npm install --save react-native-infinite-scroll --save --legacy-peer-deps
//npm install react-native-web

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

const ComModel = ({ handleClose, open }) => {
    const [uploading, setUploading] = useState(false);
    const dispatch = useDispatch();
    const [search, setSearch] = useState("");
    const navigate = useNavigate();
    const { list, theme, auth } = useSelector((store) => store);
    const [backgroundImage, setBackgroundImage] = useState("");
    const [comName, setComName] = useState("");
    const [description, setDescription] = useState("");
    const [isEnabled, setIsEnabled] = useState(false);

    const handleSubmit = (values, actions) => {
        dispatch(createCom(values));
        actions.resetForm();
        setComName("");
        setDescription("");
        setBackgroundImage("");
        console.log("listsmodel values", values);
        handleClose();
        window.location.reload();
    };


    const formik = useFormik({
        initialValues: {
            comName: "",
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
                                <p>커뮤니티 생성</p>
                            </div>
                            <Button type="submit">저장</Button>
                        </div>

                        <div className="customeScrollbar overflow-y-scroll  overflow-x-hidden h-[80vh]">
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
                                        trackColor={{ false: "#767577", true: "#81b0ff" }}
                                        thumbColor={formik.values.privateMode ? "#f5dd4b" : "#f4f3f4"}
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
export default ComModel;
