import { Button } from "@mui/material";
import { memo, useState } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { useNavigate } from "react-router-dom";
import { deleteList } from "../../Store/List/Action";
import ComModel2 from "./ComModel2";
import GroupsIcon from '@mui/icons-material/Groups';
import "./ComCard.css";

const ComCard = ({ com }) => {
    const navigate = useNavigate();
    const [openListsModel, setOpenListsModel] = useState();
    const handleCloseListsModel = () => setOpenListsModel(false);
    const handleOpenListsModel = () => setOpenListsModel(true);
    const dispatch = useDispatch();

    const handleNavigateToListsDetail = async () => {
        navigate(`/communities/${com.id}`);
    };


    const { auth, theme } = useSelector((store) => store);
    const showDeleteButton = com.user.id === auth.user.id;

    const handleDelete = async () => {
        try {
            dispatch(deleteList(com.id));
            //   handleClose();
            window.location.reload();
        } catch (error) {
            console.error("리스트 삭제 중 오류 발생: ", error);
        }
    };

    return (
            <section className="space-x-5 py-3 rounded-full items-center justify-content">
                <section className="my-5 space-x-5 items-center justify-content mt-5" style={{ marginTop: 3 }}>
                    
                        <GroupsIcon />
                        {showDeleteButton && (
                            <>
                                <Button
                                    onClick={handleOpenListsModel}
                                    //handleClose={handleCloseListsModel}
                                    sx={{ borderRadius: "20px" }}
                                    variant="outlined"
                                    className="rounded-full"
                                >
                                    관리
                                </Button>
                            

                        <section>
                            <ComModel2
                                com={com}
                                open={openListsModel}
                                handleClose={handleCloseListsModel}
                            />
                        </section>
                    </>
                )}
                
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
                        onClick={handleNavigateToListsDetail}>
                        <div className="text-xl items-center justify-content" >{com.comName}</div>
                        <div className="text-xl items-center justify-content">{com.description}</div>
                    </section>
                </section>
            </section>

    );
};

export default ComCard;
