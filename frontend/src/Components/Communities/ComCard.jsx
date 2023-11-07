import { memo, useState, useRef } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { useNavigate } from "react-router-dom";
import { deleteList } from "../../Store/List/Action";
import ComModel2 from "./ComModel2";
import GroupsIcon from '@mui/icons-material/Groups';
import {
    Avatar,
    Box,
    Button,
    Divider,
    Modal,
} from "@mui/material";
import "./ComCard.css";
import ComModel3 from "./ComModel3";

const ComCard = ({ com }) => {

    const style = {
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        width: 500,
        maxHeight: 500,
        bgcolor: "background.paper",
        boxShadow: 24,
        p: 2,
        borderRadius: 3,
        outline: "none",
        overflow: "scroll-y",
    }

    const navigate = useNavigate();
    const [openComModel, setOpenComModel] = useState();
    const [openComModel3, setOpenComModel3] = useState();
    const handleCloseComModel = () => setOpenComModel(false);
    const handleOpenComModel = () => setOpenComModel(true);
    const handleCloseComModel3 = () => setOpenComModel3(false);
    const handleOpenComModel3 = () => setOpenComModel3(true);
    const dispatch = useDispatch();
    const MembersListRef = useRef(null);

    const [openMembers, setOpenMembers] = useState(false);

    const openMembersModal = () => {
        setOpenMembers(true);
    };

    const closeMembersModal = () => {
        setOpenMembers(false);
    };

    const navigateToProfile = (id) => {
        navigate(`/profile/${id}`);
    };

    const handleNavigateToComDetail = async() => {
        navigate(`/communities/${com.id}`);
    };


    const { auth } = useSelector((store) => store);

    const showDeleteButton = com.user.id === auth.user.id;

    const handleDelete = async () => {
        try {
            dispatch(deleteList(com.id));
            //   handleClose();
            window.location.reload();
        } catch (error) {
            console.error("com del error:", error);
        }
    };

    console.log("comcard com", com);

    return (
        <section className="space-x-5 py-3 rounded-full items-center justify-content">
            <section className="my-5 space-x-5 items-center justify-content mt-5" style={{ marginTop: 3 }}>

                <GroupsIcon className="cursor-pointer" onClick={openMembersModal} />

                <Modal
                    open={openMembers}
                    onClose={closeMembersModal}
                >
                    <Box
                        sx={style}
                    >
                        <Button sx={{ fontSize: "105%", textDecoration: "underline", left: "25%"}}>Members of {com.comName}</Button>
                        <div
                            ref={MembersListRef}
                            className={`overflow-y-scroll hideScrollbar h-[40vh]`}>
                            {com.followingsc?.map((item) => (
                                <div
                                    onClick={() => { navigateToProfile(item.id); closeMembersModal(); }}
                                    className="flex items-center hover:bg-green-700 p-3 cursor-pointer"
                                    key={item.id}>
                                    <Avatar alt={item.fullName} src={item.image} loading="lazy" />
                                    <div className="ml-2">
                                        <p>{item.fullName}</p>
                                        <p className="text-sm text-gray-400">
                                            @
                                            {item.fullName.split(" ").join("_").toLowerCase()}
                                        </p>
                                    </div>
                                    {item.id === com.user.id ? 
                                        <Button sx={{ fontSize: "105%", left: "50%", color: "gray"}}>관리자</Button>
                                        : null}
                                </div>
                            ))}
                        </div>
                    </Box>
                </Modal>

                {showDeleteButton ? (
                        <Button
                            onClick={handleOpenComModel}
                            //handleClose={handleCloseListsModel}
                            sx={{ borderRadius: "20px" }}
                            variant="outlined"
                            className="rounded-full"
                        >
                            관리
                        </Button>
                        ) :
                        (
                            <Button
                                onClick={handleOpenComModel3}
                                //handleClose={handleCloseListsModel}
                                sx={{ borderRadius: "20px" }}
                                variant="outlined"
                                className="rounded-full"
                            >
                                정보 보기
                            </Button>
                            ) }


                        <section>
                            <ComModel2
                                com={com}
                                open={openComModel}
                                handleClose={handleCloseComModel}
                            />
                        </section>

                        <section>
                            <ComModel3
                                com={com}
                                open={openComModel3}
                                handleClose={handleCloseComModel3}
                            />
                        </section>


                <hr
                    style={{
                        marginTop: 10,
                        marginBottom: 1,
                        background: 'grey',
                        color: 'grey',
                        borderColor: 'grey',
                        height: '1px',
                    }}
                />
                <section
                    className="mt-5 items-center justify-content cursor-pointer"
                    onClick={handleNavigateToComDetail}>
                    <div className="text-xl items-center justify-content" >{com.comName}</div>
                </section>
            </section>
        </section>

    );
};

export default ComCard;
