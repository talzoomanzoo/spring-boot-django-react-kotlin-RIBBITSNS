import { Grid } from "@mui/material";
import React from "react";
import { useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";
import FollowTwit from "./FollowTwit/FollowTwit";
import HomeSection from "./Home/MiddlePart/HomeSection";
import TwitDetail from "./Home/MiddlePart/TwitDetail";
import Lists from "./Lists/Lists";
import Navigation from "./Navigation/Navigation";
import Profile from "./Profile/Profile";
import RightPart from "./RightPart/RightPart";
import Chat from "./Chat/Chat";
import ChatList from "./Chat/ChatList";


const HomePage = () => {
  const { theme } = useSelector((store) => store);
  return (
    <Grid container className="px-5 lg:px-36 justify-between" xs={12}>
      <Grid item xs={0} lg={2.5} className="hidden lg:block  w-full relative">
        <Navigation />
      </Grid>
      <Grid
        item
        xs={12}
        lg={6}
        className={`px-5 lg:px-9 border ${
          theme.currentTheme === "dark" ? "border-gray-800" : ""
        } `}
      >
        <Routes>
          <Route path="/" element={<HomeSection />}></Route>
          <Route path="/profile/:id" element={<Profile />}></Route>
          <Route path="/followTwit" element={<FollowTwit />}></Route>
          <Route path="/messages" element={<ChatList />}></Route>
          <Route path="/lists" element={<Lists />}></Route>
          <Route path="/twit/:id" element={<TwitDetail />}></Route>
        </Routes>
      </Grid>
      <Grid item xs={0} lg={3} className="hidden lg:block ">
        <RightPart />
      </Grid>
    </Grid>
  );
};

export default HomePage;
