import ListIcon from "@mui/icons-material/List";
import { Button } from "@mui/material";
import { memo, useState } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { useNavigate } from "react-router-dom";
import { deleteList } from "../../../Store/List/Action";
import ListsModel2 from "../ListsModel2";

const ListCard = ({ list }) => {
  const navigate = useNavigate();
  const [openListsModel, setOpenListsModel] = useState();
  const handleCloseListsModel = () => setOpenListsModel(false);
  const handleOpenListsModel = () => setOpenListsModel(true);
  const dispatch = useDispatch();

  const handleNavigateToListsDetail = () => {
    navigate(`/lists/${list.id}`);
  };

  console.log("list log", list);

  const { auth } = useSelector((store) => store);
  const showDeleteButton = list.user.id === auth.user.id;

  const handleDelete = async () => {
    try {
      dispatch(deleteList(list.id));
      //   handleClose();
      window.location.reload();
    } catch (error) {
      console.error("리스트 삭제 중 오류 발생: ", error);
    }
  };

  return (
    <div class="flex space-x-5">
      <ListIcon
        onClick={handleNavigateToListsDetail}
        className="cursor-pointer"
      />
      <div class="w-full">
        <div class="flex justify-between items-center">
          <div
            onClick={handleNavigateToListsDetail}
            className="flex cursor-pointer items-center space-x-2"
          >
            <span class="text-xl">{list.listName}</span>
          </div>
        </div>
      </div>
      {showDeleteButton && (
        <>
          <section>
            <Button
              onClick={handleDelete}
              sx={{ borderRadius: "20px" }}
              variant="outlined"
              className="rounded-full"
            >
              삭제
            </Button>
          </section>

          <section>
            <Button
              onClick={handleOpenListsModel}
             //handleClose={handleCloseListsModel}
              sx={{ borderRadius: "20px" }}
              variant="outlined"
              className="rounded-full"
            >
              수정
            </Button>
          </section>

          <section>
            <ListsModel2
              list={list}
              open={openListsModel}
              handleClose={handleCloseListsModel}
            />
          </section>
        </>
      )}
    </div>
  );
};

export default ListCard;
