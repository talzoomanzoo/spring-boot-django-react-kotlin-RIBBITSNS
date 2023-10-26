import { useSelector, useDispatch } from "react-redux";
import PlaylistAddIcon from '@mui/icons-material/PlaylistAdd';
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import ListsModel from "./ListsModel";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ListBottom from "./ListCard/ListBottom";
import ListTop from "./ListCard/LIstTop";
import ListCard from "./ListCard/ListCard";
import { getAllLists, getPrivateLists } from "../../Store/List/Action";

const Lists = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { list } = useSelector(store => store);
    const handleBack = () => {
        navigate(-1);
    };
    const [openListsModel, setOpenListsModel] = useState();
    const handleCloseListsModel = () => setOpenListsModel(false);
    const handleOpenListsModel = () => setOpenListsModel(true);
    console.log("list Lists check",
        list);

    useEffect(() => {
        dispatch(getAllLists());
    }, [list.list])

    return (
        <div id="lists" className="space-y-5">
            <section
                className={`z-50 flex items-center sticky top-0 bg-opacity-95`}
                // ${theme.currentTheme === "light" ? "bg-white" : "bg-[#0D0D0D]"
                //     } 
            >
                <div className="z-50 flex items-center sticky top-0 space-x-5">
                    <KeyboardBackspaceIcon
                        className="cursor-pointer"
                        onClick={handleBack}
                    />
                    <h1 className="py-5 text-xl font-bold opacity-90 ml-5">
                        {/* ml-5: margin-left 3rem 크기 */}
                        리스트
                    </h1>
                </div>
                <div
                    className="absolute right-0 cursor-pointer" // 오른쪽 정렬, 클릭 커서
                    onClick={handleOpenListsModel} //리스트 추가
                >
                    <PlaylistAddIcon />
                    <> 리스트 추가 </>
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
                    className={`space-y-5`}>
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
                    className={`space-y-5`}>
                    {list?.lists?.map((item) => (
                        item.privateMode ? (
                            <ListCard
                                style={{ marginTop: 10 }}
                                list={item} />
                        ) : null
                    ))}
                </section>
                
            </div>
        </div>
    )
};

export default Lists;
