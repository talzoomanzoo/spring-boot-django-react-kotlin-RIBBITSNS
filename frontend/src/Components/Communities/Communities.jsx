import GroupAddIcon from "@mui/icons-material/GroupAdd";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { Navigation, Pagination } from 'swiper';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';
import { Swiper, SwiperSlide } from 'swiper/react';
import { getAllComs } from "../../Store/Community/Action";
import ComBottom from "./ComBottom";
import ComCard from "./ComCard";
import ComModel from "./ComModel";

const Communities = ({changePage, sendRefreshPage}) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { com, theme } = useSelector((store) => store);
  const handleBack = () => {
    navigate(-1);
  };
  const [openComModel, setOpenComModel] = useState();
  const handleCloseComModel = () => setOpenComModel(false);
  const handleOpenComModel = () => setOpenComModel(true);
  const [refreshComs, setRefreshComs] = useState(0);

  const changeComs = () => {
    setRefreshComs((prev) => prev + 1);
}

  useEffect(() => {
    dispatch(getAllComs());
    }, [refreshComs, sendRefreshPage]);

  console.log("comcheck", com);

  return (
    <div id="coms" className="space-y-5">
      <section className={`z-50 flex items-center sticky top-0 bg-opacity-95 ${theme.currentTheme==="dark"?" bg-[#0D0D0D]":"bg-white"}`}>
        <div className="z-50 flex items-center sticky top-0 space-x-5">
          <KeyboardBackspaceIcon
            className="cursor-pointer"
            onClick={handleBack}
          />
          <h1 className="py-5 text-xl font-bold opacity-90 ml-5">커뮤니티</h1>
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
          className="font-bold space-y-3"
          style={{ marginTop: 10, fontSize: "larger" }}
        >
          커뮤니티 찾아보기
          <hr
            style={{
              marginTop: 10,
              background: "hsla(0, 0%, 80%, 0.5)",
              height: "6px",
            }}
          />
        </div>

        <div>
          <Swiper
            modules={[Navigation, Pagination]}
            spaceBetween={-50}
            slidesPerView={2}
            navigation
            pagination={{ clickable: true }}
            scrollbar={{ draggable: true }}
          >
            {com.coms?.map((item) => (
              <SwiperSlide>
                <ComCard style={{ marginTop: 10 }} com={item} changeComs={changeComs}/>
              </SwiperSlide>

            ))}
          </Swiper>
        </div>
      </div>

      <section className="space-y-5" style={{ marginTop: 50 }}>
        <div
          className="font-bold space-y-3"
          style={{ marginTop: 10, fontSize: "larger" }}
        >
          내 커뮤니티 리빗 모아보기
          <hr
            style={{
              marginTop: 10,
              background: "hsla(0, 0%, 80%, 0.5)",
              height: "6px",
            }}
          />
        </div>

        <div>
          <ComBottom changePage={changePage} sendRefreshPage= {sendRefreshPage}/>
        </div>
      </section>

      <section>
        <ComModel open={openComModel} handleClose={handleCloseComModel} changeComs={changeComs}/>
      </section>
    </div>
  );
};

export default Communities;
