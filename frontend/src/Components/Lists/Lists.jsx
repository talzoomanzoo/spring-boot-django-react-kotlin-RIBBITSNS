import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import PlaylistAddIcon from "@mui/icons-material/PlaylistAdd";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Route, Routes, useNavigate } from "react-router-dom";
import { getAllLists } from "../../Store/List/Action";
import ListCard from "./ListCard/ListCard";
import ListsDetail from "./ListsDetail";
import ListsModel from "./ListsModel";

const Lists = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { list } = useSelector((store) => store);
  const handleBack = () => {
    navigate(-1);
  };
  const [openListsModel, setOpenListsModel] = useState();
  const handleCloseListsModel = () => setOpenListsModel(false);
  const handleOpenListsModel = () => setOpenListsModel(true);
  console.log("list Lists check", list);

  useEffect(() => {
    dispatch(getAllLists());
  }, [list.list]);

    return (
        <div id="lists" className="space-y-5">
            <section
                className={`z-50 flex items-center sticky top-0 bg-opacity-95`}
            >
                <div className="z-50 flex items-center sticky top-0 space-x-5">
                    <KeyboardBackspaceIcon
                        className="cursor-pointer"
                        onClick={handleBack}
                    />
                    <h1 className="py-5 text-xl font-bold opacity-90 ml-5">
                        리스트
                    </h1>
                </div>
                <div
                    className="absolute right-0 cursor-pointer" // 오른쪽 정렬, 클릭 커서
                    onClick={handleOpenListsModel} //리스트 추가
                >
                    <PlaylistAddIcon />
                </div>
            </section>

      <section>
        <ListsModel open={openListsModel} handleClose={handleCloseListsModel} />
      </section>

            {/* ListTop */}
            <div
                className="space-y-3"
                style={{ marginTop: 10 }}>
                공개 리스트
                <hr
                    style={{
                        marginTop: 10,
                        background: 'grey',
                        color: 'grey',
                        borderColor: 'grey',
                        height: '1px',
                    }}
                />
                <section
                    className="space-y-5">
                    {list?.lists?.map((item) => (
                        !item.privateMode ? (
                            <ListCard
                                style={{ marginTop: 10 }}
                                list={item} />

                        ) : null
                    ))}
                </section>

            </div>

            {/* ListBottom */}
            <div
                className="space-y-3"
                style={{ marginTop: 10 }}>
                비공개 리스트
                <hr
                    className="overflow-y-scroll hideScrollbar border-gray-700 h-[20vh] w-full rounded-md"
                    style={{
                        marginTop: 10,
                        background: 'grey',
                        color: 'grey',
                        borderColor: 'grey',
                        height: '1px',
                    }}
                />
                <section
                    className="space-y-5">
                    {list?.lists?.map((item) => (
                        item.privateMode ? (
                            <ListCard
                                style={{ marginTop: 10 }}
                                list={item} />
                        ) : null
                    ))}
                </section>

                <Routes>
                    <Route path="/lists/:id" element={<ListsDetail />}></Route>
                </Routes>
                
            </div>
        </div>
    )
};

export default Lists;
