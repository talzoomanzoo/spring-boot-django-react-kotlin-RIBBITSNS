import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import PlaylistAddIcon from "@mui/icons-material/PlaylistAdd";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Route, Routes, useNavigate } from "react-router-dom";
import { getAllLists } from "../../Store/List/Action";
import ListCard from "./ListCard/ListCard";
import ListsDetail from "./ListsDetail";
// import { Route, Routes } from "react-router-dom";
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

    useEffect(() => {
        dispatch(getAllLists());
    }, []);

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
                <ListsModel
                    open={openListsModel}
                    handleClose={handleCloseListsModel}
                />
            </section>

            {/* ListTop */}
            <div
                className="space-y-3"
                style={{ marginTop: 10,
                         fontSize: "larger",
                }}>
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
                    className="space-y-5 overflow-y-scroll hideScrollbar border-gray-700 h-[40vh] w-full rounded-md">
                    {list.lists.map((item) => ( !item.privateMode ? (<ListCard style={{ marginTop: 10 }} list={item} />) : null)) }
                </section>
            </div>

            {/* ListBottom */}
            <div
                className="space-y-3"
                style={{ marginTop: 60,
                         fontSize: "larger",
                }}>
                비공개 리스트
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
                    className="space-y-5 overflow-y-scroll hideScrollbar border-gray-700 h-[40vh] w-full rounded-md">
                    {/* {list.lists && list.lists.length > 0 ?
                        ( */}
                        {list.lists.map((item) => ( item.privateMode ? (<ListCard style={{ marginTop: 10 }} list={item} />) : null)) }
                        {/* )) :
                        (
                            <div>게시된 리스트가 없습니다.</div>
                        )} */}
                </section>

            </div>
        </div>
    )
};

export default Lists;
