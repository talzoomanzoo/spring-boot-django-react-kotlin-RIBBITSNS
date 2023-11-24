import ListIcon from "@mui/icons-material/List";
import { Button } from "@mui/material";
import { memo, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { useNavigate } from "react-router-dom";
import { deleteList } from "../../../Store/List/Action";
import ListsModel2 from "../ListsModel2";
import "./ListCard.css";

const ListCard = ({ changeLists, list }) => {
  const [refreshLists, setRefreshLists] = useState(0);
  const navigate = useNavigate();
  const [openListsModel, setOpenListsModel] = useState();
  const handleCloseListsModel = () => setOpenListsModel(false);
  const handleOpenListsModel = () => setOpenListsModel(true);
  const dispatch = useDispatch();

  const handleNavigateToListsDetail = async () => {
    navigate(`/lists/${list.id}`);
  };

  // const changeLists = () => {
  //   setRefreshLists((prev) => prev + 1);
  // };

//   useEffect(() => {
// }, [refreshLists]);

  const { auth } = useSelector((store) => store);
  const showDeleteButton = list.user.id === auth.user.id;

  const handleDelete = async () => {
    try {
      dispatch(deleteList(list.id));
    } catch (error) {
      console.error("리스트 삭제 중 오류 발생: ", error);
    }
  };

  return (
    <div class="flex space-x-5 items-center">
      <div
        onClick={handleNavigateToListsDetail}
        className="cursor-pointer"
      />
      <div class="w-full">
        <div class="flex justify-between items-center">
          <div
            onClick={handleNavigateToListsDetail}
            className="flex cursor-pointer items-center list-card w-full space-x-1"
          >
            <li style={{ listStyleType: "none" }} >
              <span>
                <ListIcon style={{ marginBottom: 2, marginLeft: 10, marginRight: 10, }} /> {list.listName}
              </span>
            </li>
            <div>
      </div>
          </div>
          {showDeleteButton ? (
        <>
          <section className="inline-block" style={{marginLeft: 10}}>
            <Button
              onClick={handleDelete}
              sx={{ borderRadius: "20px", boxShadow: "0 19px 38px rgba(0,0,0,0.05), 0 15px 12px rgba(0,0,0,0.05)" }}
              variant="outlined"
              className="rounded-full btn btn-primary items-center btn-ghost btn-close"
            >
              삭제
            </Button>
          </section>

          <section className="inline-block" style={{marginLeft: 3}}>
            <Button
              onClick={handleOpenListsModel}
              //handleClose={handleCloseListsModel}
              sx={{ borderRadius: "20px", boxShadow: "0 19px 38px rgba(0,0,0,0.05), 0 15px 12px rgba(0,0,0,0.05)" }}
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
              changeLists={changeLists}
            />
          </section>
        </>
      ) : (<div style={{ width: "32.5%" }}></div>)
      }
        </div>
      </div>
      < hr
        style={{
          marginTop: 10,
          marginBottom: 1,
          background: "hsla(0, 0%, 80%, 1)",
          color: 'grey',
          borderColor: "hsl(0, 0%, 80%)",
          height: '1px',
        }}
      />
    </div>
  );
};

export default ListCard;
