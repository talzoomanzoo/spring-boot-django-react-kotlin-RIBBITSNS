import ListIcon from "@mui/icons-material/List";
import { Button } from "@mui/material";
import { memo, useState } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { useNavigate } from "react-router-dom";
import { deleteList } from "../../Store/List/Action";
import ListsModel2 from "../Lists/ListsModel2";
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


    const { auth } = useSelector((store) => store);
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
        <div class="flex space-x-5">
            <GroupsIcon
                onClick={handleNavigateToListsDetail}
                className="cursor-pointer"
            />
            <div class="w-full">
                <div class="flex justify-between items-center">
                    <div
                        onClick={handleNavigateToListsDetail}
                        className="flex cursor-pointer items-center space-x-2 com--btn effect04"
                    >
                        <span class="text-xl" data-sm-link-text="CLICK" target="_blank">{com.comName}</span>
                        <p class="text-xl">{com.description}</p>
                    </div>
                </div>
            </div>
            {showDeleteButton && (
                <>
                    <section>
                        <Button
                            onClick={handleOpenListsModel}
                            //handleClose={handleCloseListsModel}
                            sx={{ borderRadius: "20px" }}
                            variant="outlined"
                            className="rounded-full"
                        >
                            관리
                        </Button>
                    </section>

                    <section>
                        <ListsModel2
                            list={com}
                            open={openListsModel}
                            handleClose={handleCloseListsModel}
                        />
                    </section>
                </>
            )}
        </div>
    );
};

export default ComCard;
