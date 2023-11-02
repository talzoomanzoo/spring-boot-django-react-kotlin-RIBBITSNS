import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import PlaylistAddIcon from "@mui/icons-material/PlaylistAdd";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getAllComs } from "../../Store/Community/Action";
import { getAllLists } from "../../Store/List/Action";
import GroupAddIcon from '@mui/icons-material/GroupAdd';
import ComModel from "./ComModel";
import ComCard from "./ComCard";

const Communities = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { com, list } = useSelector((store) => store);
    const handleBack = () => {
        navigate(-1);
    };
    const [openComModel, setOpenComModel] = useState();
    const handleCloseComModel = () => setOpenComModel(false);
    const handleOpenComModel = () => setOpenComModel(true);

    useEffect(() => {
        dispatch(getAllComs());
        dispatch(getAllLists());
    }, []);

    console.log("comcheck", com);

    return (
        <div id="coms" className="space-y-5">
            <section
                className={`z-50 flex items-center sticky top-0 bg-opacity-95`}
            >
                <div className="z-50 flex items-center sticky top-0 space-x-5">
                    <KeyboardBackspaceIcon
                        className="cursor-pointer"
                        onClick={handleBack}
                    />
                    <h1 className="py-5 text-xl font-bold opacity-90 ml-5">
                        커뮤니티
                    </h1>
                </div>
                <div
                    className="absolute right-0 cursor-pointer" // 오른쪽 정렬, 클릭 커서
                    onClick={handleOpenComModel} //커뮤니티 추가
                >
                    <GroupAddIcon />
                </div>
            </section>

            <div className="overflow-x-scroll hideScrollbar border-gray-700 h-[20vh] w-full rounded-md">
                <div
                    className="space-y-3"
                    style={{ marginTop: 10 }}>
                    커뮤니티 찾아보기
                    <hr
                        style={{
                            marginTop: 10,
                            background: 'grey',
                            color: 'grey',
                            borderColor: 'grey',
                            height: '1px',
                        }}
                    />
                </div>
                <div className= "flex inline-block justify-content">
                    {com.coms.map((item) => (<ComCard style={{ marginTop: 10 }} com={item} />))}
                </div>
            </div>
            <hr
                style={{
                    marginTop: 10,
                    background: 'grey',
                    color: 'grey',
                    borderColor: 'grey',
                    height: '1px',
                }}
            />
            <section className="space-y-5" style={{ marginTop: 50 }}>
                <div
                    className="space-y-3"
                    style={{ marginTop: 10 }}>
                    리빗 모아보기
                    <hr
                        style={{
                            marginTop: 10,
                            background: 'grey',
                            color: 'grey',
                            borderColor: 'grey',
                            height: '1px',
                        }}
                    />
                </div>
            </section>

            <section>
                <ComModel
                    open={openComModel}
                    handleClose={handleCloseComModel}
                />
            </section>
        </div>
    )
};

export default Communities;
