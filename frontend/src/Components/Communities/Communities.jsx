import GroupAddIcon from '@mui/icons-material/GroupAdd';
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { EffectCoverflow, Pagination } from "swiper";
import "swiper/css";
import "swiper/css/effect-coverflow";
import { Swiper, SwiperSlide } from "swiper/react";
import { getAllComs } from "../../Store/Community/Action";
import ComBottom from "./ComBottom";
import ComCard from "./ComCard";
import ComModel from "./ComModel";

const Communities = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { com } = useSelector((store) => store);
    const handleBack = () => {
        navigate(-1);
    };
    const [openComModel, setOpenComModel] = useState();
    const handleCloseComModel = () => setOpenComModel(false);
    const handleOpenComModel = () => setOpenComModel(true);

    useEffect(() => {
        dispatch(getAllComs());
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
                    <GroupAddIcon /> 커뮤니티 추가

                </div>
            </section>

            <div>
                <div
                    className="space-y-3"
                    style={{ marginTop: 10, fontSize: "larger", }}>
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
                

                <section className="space-y-5 customeScrollbar overflow-y-scroll css-scroll hideScrollbar border-gray-700 h-[40vh] w-full rounded-md">
                            {com.coms.map((item) => (<ComCard style={{ marginTop: 10 }} com={item} />))}
                            {/* com.privateMode? && auth.user.id == */}
                </section>
                </div>
            </div>

            <section className="space-y-5" style={{ marginTop: 50 }}>
                <div
                    className="space-y-3"
                    style={{ marginTop: 10, fontSize: "larger", }}>
                    내 커뮤니티 리빗 모아보기
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

                <div>
                    <ComBottom />
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
