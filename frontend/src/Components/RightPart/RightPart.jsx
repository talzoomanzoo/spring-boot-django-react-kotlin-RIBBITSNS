import Brightness4Icon from "@mui/icons-material/Brightness4";
import NightsStayIcon from '@mui/icons-material/NightsStay';
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import SearchIcon from "@mui/icons-material/Search";
import { Avatar, Button } from "@mui/material";
import NotesIcon from '@mui/icons-material/Notes';
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { changeTheme } from "../../Store/Theme/Action";
import SubscriptionModel from "./SubscriptionModel";
// import { searchUser } from "../../Store/Auth/Action";
import { useNavigate } from "react-router-dom";
import { searchAll } from "../../Store/Auth/Action";
import { viewPlus } from "../../Store/Tweet/Action";
import LikeTop from "./LikeTop";
import ViewTop from "./ViewTop";
import "./Scrollbar.css";

const RightPart = () => {
  const { theme, auth } = useSelector((store) => store);
  const [search, setSearch] = useState("");
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [openSubscriptionModal, setOpenSubscriptionModal] = useState(false);
  const handleCloseSubscriptionMadal = () => setOpenSubscriptionModal(false);
  const handleOpenSubscriptionModal = () => setOpenSubscriptionModal(true);


  // const Payment = () => {
  //   useEffect(() => {
  //     const jquery = document.createElement("script");
  //     jquery.src = "http://code.jquery.com/jquery-1.12.4.min.js";
  //     const iamport = document.createElement("script")
  //     iamport.src = "http://cdn.iamport.kr/js/iamport.payment-1.1.7.js";
  //     document.head.appendChild(jquery);
  //     document.head.appendChild(iamport);
  //     return () => {
  //       document.head.removeChild(jquery);
  //       document.head.removeChild(iamport);
  //     };
  //   }, []);
  // }

  // const requestPay = () => {
  //   const { IMP } = window;
  //   IMP.init('imp40731612')

  //   IMP.request_pay({
  //     pg: 
  //   })
  // }
  const [refreshTwits, setRefreshTwits] = useState(0);


  const handleNavigateToTwit = (i) => {
    navigate(`/twit/${i.id}`);
    dispatch(viewPlus(i.id));
  };

  const handleChangeTheme = () => {
    // setRefreshTwits((prev) => prev + 1);
    dispatch(changeTheme(theme.currentTheme === "light" ? "dark" : "light"));
    window.location.reload();
    //setRefreshTwits((prev) => prev + 1);  
  };

  // const handleSearchUser = (event) => {
  //   setSearch(event.target.value)
  //   dispatch(searchUser(event.target.value));
  // };

  const handleSearchAll = (event) => {
    setSearch(event.target.value); // setSearch({search})
    dispatch(searchAll(event.target.value)); // searchAll({search}) -- 비동기로 spring에 보냄
  };

  const navigateToProfile = (id) => {
    navigate(`/profile/${id}`);
    setSearch("");
  };

  return (
    <div className="sticky top-0 customeScrollbar overflow-y-scroll hideScrollbar h-[100vh] css-scroll">
      <div className={`py-5`}>
        <div className="relative flex items-center">
          <input
            value={search}
            onChange={handleSearchAll}
            // onChange={handleSearchUser}
            type="text"
            placeholder="사용자 및 글 검색"
            className={`py-3 rounded-full outline-none text-gray-500 w-full pl-12 ${theme.currentTheme === "light" ? "bg-stone-300" : "bg-[#151515]"}`}
          />
          <span className="absolute top-0 left-0 pl-3 pt-3">
            <SearchIcon className="text-gray-400" />
          </span>
          {search && (
            <div
              className={`absolute z-50 top-14 customeScrollbar overflow-y-scroll hideScrollbar css-scroll h-[40vh] border-gray-400 h-[40vh] w-full rounded-md ${theme.currentTheme === "light" ? "bg-[#dbd9d9]" : "bg-[#151515] border"}`}
            >
              {auth.userSearchResult && auth.userSearchResult.map((item) => (
                <div
                  onClick={() => {
                    if (Array.isArray(item)) {
                      item.forEach((i) => handleNavigateToTwit(i));
                    } else {
                      navigateToProfile(item.id);
                    }
                  }}
                  className={`flex items-center ${theme.currentTheme === "light" ? "hover:bg-[#008000]" : "hover:bg-[#dbd9d9]"}
                  ${theme.currentTheme === "light" ? "text-black hover:text-white" : "text-white  hover:text-black"} p-3 cursor-pointer`}
                  key={item.id} // 각 항목에 고유한 키를 제공합니다.
                >
                  <Avatar alt={item.fullName} src={item.image ? item.image : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"} loading="lazy" />
                  <div className={`ml-2`}>
                    <p>{item.fullName}</p>
                    <p className={`text-sm`}>
                      {item.email.split(" ").join("_").toLowerCase()}
                    </p>
                  </div>
                </div>
              ))}

              {auth.tweetSearchResult && auth.tweetSearchResult.map((item) => (
                <div
                  onClick={() => {
                    if (Array.isArray(item)) {
                      item.forEach((i) => handleNavigateToTwit(i));
                    } else {
                      handleNavigateToTwit(item);
                      // navigateToProfile(item.id);
                    }
                  }}
                  className={`flex items-center ${theme.currentTheme === "light" ? "hover:bg-[#008000]" : "hover:bg-[#dbd9d9]"} 
                  ${theme.currentTheme === "light" ? "text-black hover:text-white" : "text-white  hover:text-black"} p-3 cursor-pointer`}
                  key={item.id} // 각 항목에 고유한 키를 제공합니다.
                >
                  <NotesIcon alt={item.fullName} />
                  <div className={`ml-2 `}>
                    <p>{item.content}</p>
                    <p className={`text-sm`}>
                      @{item.user.fullName.split(" ").join("_").toLowerCase()}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          )}

          <NightsStayIcon
            onClick={handleChangeTheme}
            className="ml-3 cursor-pointer"
          />
        </div>

        <section
          className={`my-5`}
        // ${
        //   theme.currentTheme === "dark" ? " bg-[#151515] p-5 rounded-md" : ""
        // }
        >
          <h1 className="text-xl font-bold" >그린체크</h1>
          <h1 className="font-bold my-2">구독 후 사용해보세요!</h1>
          <Button
            onClick={handleOpenSubscriptionModal}
            variant="contained"
            sx={{ padding: "10px", paddingX: "20px", borderRadius: "25px" }}
          >
            {" "}
            체크 구독
          </Button>
          
        </section>

        <hr
            style={{
              marginTop: 30,
              marginBottom: 1,
              background: 'grey',
              color: 'grey',
              borderColor: 'grey',
              height: '1px',
            }}
          />

        <section
          className={`mt-5 space-y-5 `}
        // ${
        //   theme.currentTheme === "dark" ? " bg-[#151515] p-5 rounded-md" : ""
        // }
        >
          <h1 className="font-bold text-xl py-1">인기글</h1>

          <div>
            <p className="font-bold"> 좋아요 많은 글 </p>
          </div>

          <div>
            <LikeTop />
          </div>

          <div>
            <p className="font-bold"> 조회수 높은 글 </p>
          </div>

          <div>
            <ViewTop />
          </div>

        </section>
      <SubscriptionModel
        open={openSubscriptionModal}
        handleClose={handleCloseSubscriptionMadal}
      />
      </div>
    </div>
  );
};

export default RightPart;
