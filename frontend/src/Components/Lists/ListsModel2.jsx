import { Avatar, IconButton, Modal } from "@mui/material";
import { useFormik } from "formik";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { uploadToCloudinary } from "../../Utils/UploadToCloudinary";
import {
    Box,
    Button,
    TextField,
}
    from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import BackdropComponent from "../Backdrop/Backdrop";
import { updateListModel, addUserAction, getUserAction } from "../../Store/List/Action";
import { searchUser } from "../../Store/Auth/Action";
import SearchIcon from "@mui/icons-material/Search";
import { useNavigate } from "react-router-dom";
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import { createRoot } from 'react-dom/client';
import React from "react";
import { memo } from "react";

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

const ListsModel2 = memo(({ list, handleClose, open }) => {
    //const param=useParams();
    const [uploading, setUploading] = useState(false);
    const [search, setSearch] = useState("");
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { theme, auth } = useSelector((store) => store);
    const [followingsClicked, setFollowingsClicked] = useState(false);

    const handleSubmit = (values) => {
        dispatch(updateListModel(values))
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

    useEffect(() => {

        formik.setValues({
            id: list.id || "",
            listName: list.listName || "",
            description: list.description || "",
            backgroundImage: list.backgroundImage || "",
        });


        if (document.getElementById("element") != null ) {
            const domNode2 = document.getElementById("element");
            const element1 = createRoot(domNode2);
            element1.render(<Element list={list} />);
        } else {
            console.log("not exists");
        };

    }, [list.followings])

    const handleImageChange = async (event) => {

        setUploading(true)
        const { name } = event.target;
        const file = event.target.files[0];
        const url = await uploadToCloudinary(file, "image");
        formik.setFieldValue(name, url);
        setUploading(false);

    };

    const handleSearchUser = (event) => {
        setSearch(event.target.value)
        dispatch(searchUser(event.target.value));
    };

    const navigateToProfile = (id) => {
        navigate(`/profile/${id}`);
        setSearch("");
    };


    //element.render(<Element />);
    //document.body.appendChild(domNode);

    const handleAddUser = (listId, userId) => {
        dispatch(addUserAction(listId, userId));
        // const domNode = document.getElementById('lists');
        // const element = createRoot(domNode);
        // console.log("domNode check", domNode);
        // element.render();
        dispatch(getUserAction(listId));


        //console.log("handleAddUserlist check", list);
        setSearch("");
        //console.log("add user id", userId);
        //console.log("add list id", listId);
    }

    const handleFollowingsClick = () => {
        setFollowingsClicked(!followingsClicked);
    };

    //console.log("list followings check", list);
    //console.log("auth userSearchcheck1", auth.userSearchResult);

    const Element = memo(({ list }) => {
        console.log("element list check", list);
        return (
            <div className="customeScrollbar overflow-y-scroll  overflow-x-hidden h-[80vh]">
                <section
                    className={`${theme.currentTheme === "dark" ? "pt-14" : ""} space-y-5`}
                >

                    {list.followings?.map((item) => (
                        <div
                            style={{ paddingRight: 300 }}
                            onClick={() => {
                                if (Array.isArray(item)) {
                                    item.forEach((i) => navigateToProfile(i));
                                } else {
                                    navigateToProfile(item.id);
                                }
                                handleFollowingsClick();
                            }}
                            className="flex items-center hover:bg-slate-800 p-3 cursor-pointer"
                            key={item.id}
                        >
                            <Avatar alt={item.fullName} src={item.image} />
                            <div className="ml-2">
                                <p>{item.fullName}</p>
                                <p className="text-sm text-gray-400">
                                    @
                                    {item.fullName.split(" ").join("_").toLowerCase()}
                                </p>
                            </div>
                        </div>
                    ))}

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
                                <p>Edit List</p>
                            </div>
                            <Button type="submit">Save</Button>
                        </div>

                        <div className="customeScrollbar overflow-y-scroll  overflow-x-hidden h-[80vh]">
                            <div className="">
                                <div className="w-full" >
                                    <div className="relative">
                                        <img
                                            src={
                                                formik.values.backgroundImage ||
                                                "https://i.stack.imgur.com/H0xdb.png"
                                                // "https://cdn.pixabay.com/photo/2018/10/16/15/01/background-image-3751623_1280.jpg"
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

                                <div className="w-full transform -translate-y-20 translate-x-4 h-[3rem]">
                                </div>
                            </div>
                            <div className="space-y-3">
                                <TextField
                                    fullWidth
                                    id="listName"
                                    label="List Name"
                                    value={formik.values.listName}
                                    onChange={formik.handleChange}
                                    error={formik.touched.listName && Boolean(formik.errors.listName)}
                                    helperText=""
                                />
                                <TextField
                                    fullWidth
                                    multiline
                                    rows={4}
                                    id="description"
                                    name="description"
                                    label="Description"
                                    value={formik.values.description}
                                    onChange={formik.handleChange}
                                    error={formik.touched.description && Boolean(formik.errors.description)}
                                    helperText={formik.touched.description && formik.errors.description}
                                />
                            </div>
                            <div
                                className="relative flex items-center"
                                style={{ marginTop: 20 }}>
                                <input
                                    value={search}
                                    onChange={handleSearchUser}
                                    type="text"
                                    placeholder="Search User to Add or Remove"
                                    className={`py-3 rounded-full onutline-none text-gray-500 w-full pl-12 ${theme.currentTheme === "light" ? "bg-slate-300" : "bg-[#151515]"
                                        }`}
                                />
                                <span className="absolute top-0 left-0 pl-3 pt-3">
                                    <SearchIcon className="text-gray-500" />
                                </span>
                                {search && (
                                    <div
                                        className={` overflow-y-scroll hideScrollbar absolute z-50 top-14  border-gray-700 h-[40vh] w-full rounded-md ${theme.currentTheme === "light"
                                            ? "bg-white"
                                            : "bg-[#151515] border"
                                            }`}
                                    >
                                        {auth.userSearchResult && auth.userSearchResult.map((item) => (
                                            <div className="flex float items-center">
                                                <div
                                                    style={{ paddingRight: 300 }}
                                                    onClick={() => {
                                                        if (Array.isArray(item)) {
                                                            item.forEach((i) => navigateToProfile(i));
                                                        } else {
                                                            navigateToProfile(item.id);
                                                        }
                                                    }}
                                                    className="flex float-left hover:bg-slate-800 p-3 cursor-pointer"
                                                    key={item.id}
                                                >
                                                    <Avatar alt={item.fullName} src={item.image} />
                                                    <div className="ml-2">
                                                        <p>{item.fullName}</p>
                                                        <p className="text-sm text-gray-400">
                                                            @{item.fullName.split(" ").join("_").toLowerCase()}
                                                        </p>
                                                    </div>
                                                </div>
                                                {item.hasFollowedLists ?
                                                    (
                                                        <RemoveIcon
                                                            style={{ marginLeft: 20 }}
                                                            className="flex float-right hover:bg-slate-800 absolute right-0 cursor-pointer"
                                                            //absolute right-0
                                                            onClick={() => { handleAddUser(list.id, item.id, list) }}>
                                                        </RemoveIcon>
                                                    )
                                                    : (
                                                        <AddIcon
                                                            style={{ marginLeft: 20 }}
                                                            className="flex float-right hover:bg-slate-800 absolute right-0 cursor-pointer"
                                                            //absolute right-0
                                                            onClick={() => { handleAddUser(list.id, item.id, list) }}>
                                                        </AddIcon>
                                                    )}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>

                            {/* 여기 */}
                            <div id="element">
                                <div>
                                    <Element list={list} />
                                </div>
                            </div>

                        </div>
                        <BackdropComponent open={uploading} />
                    </form>
                </Box>
            </Modal>
        </div>
    )
});
export default ListsModel2;