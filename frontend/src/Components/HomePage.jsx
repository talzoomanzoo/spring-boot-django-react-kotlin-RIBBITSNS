import { Grid } from "@mui/material";
import React, { Suspense } from "react";
import { useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";
// import FollowTwit from "./FollowTwit/FollowTwit";
import HomeSection from "./Home/MiddlePart/HomeSection";
// import TwitDetail from "./Home/MiddlePart/TwitDetail";
// import Lists from "./Lists/Lists";
import Navigation from "./Navigation/Navigation";
// import Profile from "./Profile/Profile";
import RightPart from "./RightPart/RightPart";
// import Chatroom from "./Chat/Chat";
// import ListsDetail from "./Lists/ListsDetail";
// import Communities from "./Communities/Communities";

const FollowTwit = React.lazy(() => import("./FollowTwit/FollowTwit"));
const TwitDetail = React.lazy(() => import("./Home/MiddlePart/TwitDetail"));
const Lists = React.lazy(() => import("./Lists/Lists"));
const Profile = React.lazy(() => import("./Profile/Profile"));
const Chatroom = React.lazy(() => import("./Chat/Chat"));
const ListsDetail = React.lazy(() => import("./Lists/ListsDetail"));
const Communities = React.lazy(() => import("./Communities/Communities"));


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
        className={`px-5 lg:px-9 border ${theme.currentTheme === "dark" ? "border-gray-800" : ""
          } `}
      >
        <Routes>
          <Route path="/" element={
              <HomeSection />
          }></Route>
          <Route path="/profile/:id" element={
            <Suspense fallback={<div> Loading... </div>}>
              <Profile />
            </Suspense>}></Route>
          <Route path="/followTwit" element={
            <Suspense fallback={<div> Loading... </div>}>
              <FollowTwit />
            </Suspense>}></Route>
          <Route path="/messages" element={
            <Suspense fallback={<div> Loading... </div>}>
              <Chatroom />
            </Suspense>}></Route>
          <Route path="/lists" element={
            <Suspense fallback={<div> Loading... </div>}>
              <Lists />
            </Suspense>}></Route>
          <Route path="/communities" element={
            <Suspense fallback={<div> Loading... </div>}>
              <Communities />
            </Suspense>}></Route>
          <Route path="/twit/:id" element={
            <Suspense fallback={<div> Loading... </div>}>
              <TwitDetail />
            </Suspense>}></Route>
          <Route path="/lists/:id" element={
            <Suspense fallback={<div> Loading... </div>}>
              <ListsDetail />
            </Suspense>}></Route>
        </Routes>
      </Grid>
      <Grid item xs={0} lg={3} className="hidden lg:block">
        <RightPart className="sticky top-0"/>
      </Grid>
    </Grid>
  );
};

export default HomePage;
