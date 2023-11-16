import {
    Avatar,
    Box,
    Button,
    Modal
} from "@mui/material";
import { useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { deleteCom } from "../../Store/Community/Action";
import ComModel2 from "./ComModel2";
import ComModel3 from "./ComModel3";
import PeopleIcon from '@mui/icons-material/People';
import "../RightPart/Scrollbar.css";
import CloseIcon from "@mui/icons-material/Close";

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
            //   handleClose();
            window.location.reload();
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
        <div class="flex space-x-5">
            <div
                
                className="cursor-pointer"
            />
            <div class="w-full">
                <div class="flex justify-between items-center">
                    <div
                        className="flex cursor-pointer items-center space-x-1">
                        <li style={{ listStyleType: "none", fontSize: "larger" }} ><PeopleIcon style={{fontSize: "larger"}} onClick={openMembersModal}/><span style={{padding: "10px"}} onClick={() => {handleNavigateToComDetail(com);}}>{com.comName}</span></li>
                    </div>
                    <Modal
                        open={openMembers}
                        onClose={closeMembersModal}
                    >
                        <Box
                            sx={style}
                        >
                            <Button sx={{ fontSize: "105%", textDecoration: "underline", left: "32%" }}>Members of {com.comName}</Button>
                            <button style={{marginLeft: "55%"}} onClick={() => closeMembersModal()}><CloseIcon className={`${theme.currentTheme === "light" ? "text-black" : "text-white"}`} /></button>
                            <div
                                ref={MembersListRef}
                                className={`overflow-y-scroll css-scroll hideScrollbar h-[40vh]`}>
                                {com.followingsc?.map((item) => (
                                    <div
                                        style={{ marginLeft: "5%", marginTop: "2%"}}
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
                </div>
            </div>
            {showDeleteButton ? (
                <>
                  <Button
              onClick={handleOpenComModel}
              sx={{ borderRadius: "20px", }}
              variant="outlined"
              className="rounded-full"
            >
              관리
            </Button>
            <Button
              onClick={handleDelete}
              sx={{ borderRadius: "20px"}}
              variant="outlined"
              className="rounded-full"
            >
              삭제
            </Button>
            </>
            ):(
                <Button
                onClick={handleOpenComModel3}
                sx={{ borderRadius: "20px", width: "20px" }}
                variant="outlined"
                className="rounded-full btn btn-primary btn-ghost btn-close"
              >
                정보 보기
              </Button>
            )}
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
            <section>
                <Modal
                open={openAlertModal}
                handleClose={handleCloseAlertModal}
                style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                }}>
                    <div className={`withdrawal-modal outline-none ${theme.currentTheme === "light" ? "bg-gray-200" : "bg-stone-950"}`}  style={{padding: "20px", borderRadius: "8px" }}>
                    <p id="description">
                    해당 커뮤니티는 비공개입니다.
                    </p>
                    <Button style={{marginLeft: "165px"}} onClick={handleCloseAlertModal}>확인</Button>
                    </div>
                </Modal>
            </section>
        </div>
    );
};

export default ComCard;