import React, { useState } from "react";
import SearchIcon from "@mui/icons-material/Search";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import { Avatar, Button } from "@mui/material";
import Brightness4Icon from "@mui/icons-material/Brightness4";
import { useDispatch, useSelector } from "react-redux";
import { changeTheme } from "../../Store/Theme/Action";
import SubscriptionModel from "./SubscriptionModel";
//import { searchUser } from "../../Store/Auth/Action";
import { searchAll } from "../../Store/Auth/Action";
import { useNavigate } from "react-router-dom";
// import { searchUser } from "../../Store/Auth/Action";
import { viewPlus } from "../../Store/Tweet/Action";

const RightPart = () => {
  const { theme, auth } = useSelector((store) => store);
  const [search, setSearch] = useState("");
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [openSubscriptionModal, setOpenSubscriptionModal] = useState(false);
  const handleCloseSubscriptionMadal = () => setOpenSubscriptionModal(false);
  const handleOpenSubscriptionModal = () => setOpenSubscriptionModal(true);

  const handleNavigateToTwit = (i) => {
    navigate(`/twit/${i.id}`);
    dispatch(viewPlus(i.id));
  };

  const handleChangeTheme = () => {
    dispatch(changeTheme(theme.currentTheme === "dark" ? "light" : "dark"));
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
    <div className="py-5 sticky top-0 overflow-y-hidden">
      <div className="hideScrollbar overflow-y-scroll">
        <div className="relative flex items-center">
          <input
            value={search}
            onChange={handleSearchAll}
            // onChange={handleSearchUser}
            type="text"
            placeholder="Search Twitter"
            className={`py-3 rounded-full outline-none text-gray-500 w-full pl-12 ${
              theme.currentTheme === "light" ? "bg-slate-300" : "bg-[#151515]"
            }`}
          />
          <span className="absolute top-0 left-0 pl-3 pt-3">
            <SearchIcon className="text-gray-500" />
          </span>
          {search && (
            <div
              className={` overflow-y-scroll hideScrollbar absolute z-50 top-14  border-gray-700 h-[40vh] w-full rounded-md ${
                theme.currentTheme === "light"
                  ? "bg-white"
                  : "bg-[#151515] border"
              }`}
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
                  className="flex items-center hover:bg-slate-800 p-3 cursor-pointer"
                  key={item.id} // 각 항목에 고유한 키를 제공합니다.
                >
                  <Avatar alt={item.fullName} src={item.image} />
                  <div className="ml-2">
                    <p>{item.fullName}</p>
                    <p className="text-sm text-gray-400">
                      @{item.fullName.split(" ").join("_").toLowerCase()}
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
                    }
                  }}
                  className="flex items-center hover:bg-slate-800 p-3 cursor-pointer"
                  key={item.id} // 각 항목에 고유한 키를 제공합니다.
                >
                  <p>{item.content}</p>
                </div>
              ))}
            </div>
          )}

          <Brightness4Icon
            onClick={handleChangeTheme}
            className="ml-3 cursor-pointer"
          />
        </div>

        <section
          className={`my-5 ${
            theme.currentTheme === "dark" ? " bg-[#151515] p-5 rounded-md" : ""
          }`}
        >
          <h1 className="text-xl font-bold">Get Verified</h1>
          <h1 className="font-bold my-2">Subscribe to unlock new features</h1>
          <Button
            onClick={handleOpenSubscriptionModal}
            variant="contained"
            sx={{ padding: "10px", paddingX: "20px", borderRadius: "25px" }}
          >
            {" "}
            Get verified
          </Button>
        </section>

        <section
          className={`mt-7 space-y-5 ${
            theme.currentTheme === "dark" ? " bg-[#151515] p-5 rounded-md" : ""
          }`}
        >
          <h1 className="font-bold text-xl py-1">What’s happening</h1>

          <div>
            <p className="text-sm">FIFA Women's World Cup · LIVE </p>
            <p className="font-bold">Philippines vs Switzerland</p>
          </div>

          <div className="flex justify-between w-full">
            <div>
              <p>Entertainment · Trending</p>
              <p className="font-bold">#TheMarvels</p>
              <p>34.3K Tweets</p>
            </div>

            <MoreHorizIcon />
          </div>
          <div className="flex justify-between w-full">
            <div>
              <p>Entertainment · Trending</p>
              <p className="font-bold">#TheMarvels</p>
              <p>34.3K Tweets</p>
            </div>

            <MoreHorizIcon />
          </div>

          <div className="flex justify-between w-full">
            <div>
              <p>Entertainment · Trending</p>
              <p className="font-bold">#TheMarvels</p>
              <p>34.3K Tweets</p>
            </div>

            <MoreHorizIcon />
          </div>
        </section>
      </div>
      <SubscriptionModel
        open={openSubscriptionModal}
        handleClose={handleCloseSubscriptionMadal}
      />
    </div>
  );
};

export default RightPart;
