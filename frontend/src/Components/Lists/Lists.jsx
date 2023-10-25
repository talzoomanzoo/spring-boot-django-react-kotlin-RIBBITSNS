import { useSelector, useDispatch } from "react-redux";
import PlaylistAddIcon from '@mui/icons-material/PlaylistAdd';
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import ListsModel from "./ListsModel";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ListCard from "./ListCard/ListCard";
import { getAllLists } from "../../Store/List/Action";

const Lists = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { list, theme } = useSelector(store => store);
    const handleBack = () => {
        navigate(-1);
    };
    const [openListsModel, setOpenListsModel] = useState();
    const handleCloseListsModel = () => setOpenListsModel(false);
    const handleOpenListsModel = () => setOpenListsModel(true);

    useEffect(() => {
        dispatch(getAllLists())
    }, [list.list])

    console.log("list Lists check",    
                list);
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

            <section
                className={`space-y-5`}> 
                {/* ${theme.currentTheme === "dark" ? "pt-14" : ""}  */}
                {list?.lists?.map((item) => (
                    <ListCard 
                        list={item} />
                ))}
            </section>
        </div>
    )
};

export default Lists;
