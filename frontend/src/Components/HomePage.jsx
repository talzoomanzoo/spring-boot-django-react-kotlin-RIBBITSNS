import { Grid } from "@mui/material";
import React, { Suspense, useState } from "react";
import { useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";
import HomeSection from "./Home/MiddlePart/HomeSection";
import Navigation from "./Navigation/Navigation";
import Loading from "./Profile/Loading/Loading";
import RightPart from "./RightPart/RightPart";

const FollowTwitEnc = React.lazy(() => import("./FollowTwit/FollowTwitEnc"));
const TwitDetail = React.lazy(() => import("./Home/MiddlePart/TwitDetail"));
const Lists = React.lazy(() => import("./Lists/Lists"));
const Profile = React.lazy(() => import("./Profile/Profile"));
const Chatroom = React.lazy(() => import("./Chat/Chat"));
const ListsDetail = React.lazy(() => import("./Lists/ListsDetail"));
const Communities = React.lazy(() => import("./Communities/Communities"));
const ComDetail = React.lazy(() => import("./Communities/ComDetail"));

const HomePage = () => {
  const { theme } = useSelector((store) => store);
  const [uploading, setUploading] = useState(false);
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
          <Route path="/" element={
              <HomeSection />
          }></Route>
          <Route path="/profile/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Profile />
            </Suspense>}></Route>
          <Route path="/followtwit" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <FollowTwitEnc />
            </Suspense>}></Route>
          <Route path="/messages" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Chatroom />
            </Suspense>}></Route>
          <Route path="/lists" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Lists />
            </Suspense>}></Route>
          <Route path="/communities" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Communities />
            </Suspense>}></Route>
          <Route path="/twit/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <TwitDetail />
            </Suspense>}></Route>
          <Route path="/lists/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <ListsDetail />
            </Suspense>}></Route>
            <Route path="/communities/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <ComDetail />
            </Suspense>}></Route>
        </Routes>
      </Grid>
      <Grid item xs={0} lg={3} className="hidden lg:block">
        <RightPart />
      </Grid>
    </Grid>
  );
};

export default HomePage;
