import GroupsIcon from '@mui/icons-material/Groups';
import {
    Avatar,
    Box,
    Button,
    Modal
} from "@mui/material";
import { useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { useNavigate } from "react-router-dom";
import { deleteCom } from "../../Store/Community/Action";
import "./ComCard.css";
import ComModel2 from "./ComModel2";
import ComModel3 from "./ComModel3";
import "../RightPart/Scrollbar.css";
import CloseIcon from "@mui/icons-material/Close";

const ComCard = ({ changeComs, com, changePage }) => {

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
    const [openAlertModal, setOpenAlertModal] = useState();
    const handleCloseComModel = () => setOpenComModel(false);
    const handleOpenComModel = () => setOpenComModel(true);
    const handleCloseComModel3 = () => setOpenComModel3(false);
    const handleOpenComModel3 = () => setOpenComModel3(true);
    const handleCloseAlertModal = () => setOpenAlertModal(false);
    const handleOpenAlertModal = () => setOpenAlertModal(true);
    const dispatch = useDispatch();
    const MembersListRef = useRef(null);

    const [openMembers, setOpenMembers] = useState(false);
    const {theme} = useSelector((store) => store);

    const handleDelete = async () => {
        try {
            dispatch(deleteCom(com.id));
            changeComs();
        } catch (error) {
            console.error("커뮤니티 삭제 중 오류 발생: ", error);
        }
    };

    const openMembersModal = () => {
        setOpenMembers(true);
    };

    const closeMembersModal = () => {
        setOpenMembers(false);
    };

    const navigateToProfile = (id) => {
        navigate(`/profile/${id}`);
    };
    const { auth } = useSelector((store) => store);

    console.log("comCard comCheck", com);

    const handleNavigateToComDetail = (com) => {
        if (com.privateMode === true) {
            for (let i = 0; i < com.followingsc.length; i++) {
                if (com.followingsc[i].id === auth.user.id) {
                    navigate(`/communities/${com.id}`);
                } else {
                    handleOpenAlertModal();
                }
            }
        } else {
            navigate(`/communities/${com.id}`);
        }
    }

    const showDeleteButton = com.user.id === auth.user.id;

    return (
    <section className="space-x-5 py-3 rounded-full items-center justify-content">
        <section className="my-5 space-x-5 items-center justify-content mt-5" style={{ marginTop: 3 }}>
            <div className="card">
            <GroupsIcon className="cursor-pointer" onClick={openMembersModal} style={{marginTop: 3, marginLeft: 10}}/>

            <Modal
                open={openMembers}
                onClose={closeMembersModal}
            >
                <Box
                    sx={style}
                >
                    <Button sx={{ maxWidth: "200px", fontSize: "105%", textDecoration: "underline", left: "137px" }}>Members of {com.comName}</Button>
                    <button style={{ position: "fixed", top: "25px", right: "30px"}} onClick={() => closeMembersModal()}><CloseIcon className={`${theme.currentTheme === "light" ? "text-black" : "text-white"}`} /></button>
                    <div
                        ref={MembersListRef}
                        className={`overflow-y-scroll css-scroll hideScrollbar h-[40vh]`}>
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
                                    <Button sx={{ fontSize: "105%", left: "50%", color: "gray" }}>관리자</Button>
                                    : null}
                            </div>
                        ))}
                    </div>
                </Box>
            </Modal>

            {showDeleteButton ? (
                <>
                <Button
                    onClick={handleOpenComModel}
                    //handleClose={handleCloseListsModel}
                    sx={{ borderRadius: "20px", marginLeft: "33%" }}
                    variant="outlined"
                    className="rounded-full"
                >
                    관리
                </Button>

                <Button
                    onClick={handleDelete}
                    //handleClose={handleCloseListsModel}
                    sx={{ borderRadius: "20px", marginLeft: "2%"}}
                    variant="outlined"
                    className="rounded-full"
                >
                    삭제
                </Button>
                </>
            ) :
                (
                    <Button
                        onClick={handleOpenComModel3}
                        //handleClose={handleCloseListsModel}
                        sx={{ borderRadius: "20px", marginLeft: "45%"}}
                        variant="outlined"
                        className="rounded-full"
                    >
                        정보 보기
                    </Button>
                )}


            <section>
                <ComModel2
                    com={com}
                    open={openComModel}
                    handleClose={handleCloseComModel}
                    changeComs={changeComs}
                />
            </section>

            <section>
                <ComModel3
                    com={com}
                    open={openComModel3}
                    handleClose={handleCloseComModel3}
                    changeComs={changeComs}
                />
            </section>

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
                    <div className={`withdrawal-modal outline-none ${theme.currentTheme === "light" ? "bg-gray-200" : "bg-stone-950"}`}  style={{padding: "20px", borderRadius: "8px" }}>
                        <p id="description">
                            해당 커뮤니티는 비공개입니다. 
                        </p>
                        <Button style={{marginLeft: "165px"}} onClick={handleCloseAlertModal}>확인</Button>
                    </div>
                </Modal>
            </section>


            <hr
                style={{
                    marginTop: 10,
                    marginBottom: 1,
                    background: "hsla(0, 0%, 80%, 1)",
                    color: 'grey',
                    borderColor: "hsl(0, 0%, 80%)",
                    height: '1px',
                }}
            />
            <section
                style={{
                    height: "100px",
                    textAlign: "center"
                }}
                className="mt-5 items-center justify-content cursor-pointer"
                onClick={() => handleNavigateToComDetail(com)}>
                <div className="text-xl items-center justify-content" >{com.comName}</div>
            </section>
            </div>
        </section>
    </section >

    );
};

export default ComCard;