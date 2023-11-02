import { Button, Grid } from "@mui/material";
import { GoogleLogin } from "@react-oauth/google";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { loginWithGoogleAction } from "../../Store/Auth/Action";
import AuthModel from "./AuthModel";

const Authentication = () => {
  const [authModelOpen, setAuthModelOpen] = useState(false);
  const { auth } = useSelector((store) => store);
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleAuthModelClose = () => {
    setAuthModelOpen(false);
    window.location.replace("/");
  };

  const handleAuthModelOpen = (path) => {
    setAuthModelOpen(true);
    navigate(path);
  };

  useEffect(() => {
    if (location.pathname === "/signin" || location.pathname === "/signup") {
      setAuthModelOpen(true);
    }
  }, [location.pathname]);

  const loginWithGoole = (res) => {
    console.log("res : ", res);
    dispatch(loginWithGoogleAction(res));
    // return
  };

  return (
    <div className="">
      {" "}
      <Grid className="overflow-y-hidden" container>
        <Grid className="hidden lg:block" item lg={7}>
          <img
            className="w-full h-screen"
            src="https://cdn.pixabay.com/photo/2018/04/14/05/26/green-3318343_1280.jpg"
            alt=""
          />

          <div className="absolute top-[26%] left-[19%]">
            <svg
              height="300"
              width="300"
              viewBox="0 0 24 24"
              aria-hidden="true"
              class="r-jwli3a r-4qtqp9 r-yyyyoo r-labphf r-1777fci r-dnmrzs r-494qqr r-bnwqim r-1plcrui r-lrvibr"
            ></svg>
          </div>

          <img
            className=" w-[50rem] absolute -top-5"
            style={{
              height: "300px",
              width: "300px",
              marginTop: "300px",
              marginLeft: "20%",
            }}
            src="https://cdn.pixabay.com/photo/2023/10/26/06/44/06-44-04-156_1280.png"
            alt=""
          />
        </Grid>
        <Grid className="px-10" item lg={5} xs={12}>
          <div className="py-10">
            <img
              className="w-16"
              src="https://pbs.twimg.com/media/F1iAD_iaYAAu7I3?format=jpg&name=small"
              alt=""
            />
          </div>

          <h1 className="font-bold text-7xl">
            RIBBIT
            <img
              src="https://cdn.pixabay.com/photo/2023/10/24/01/42/01-42-37-630_1280.png"
              style={{
                height: "80px",
                width: "80px",
                float: "right",
                marginRight: "54%",
              }}
            />
          </h1>

          <h1 className="font-bold text-3xl py-16">리빗 가입하기</h1>

          <div className="w-[60%]">
            <div className="w-full">
              {/* <button 
            className="w-full flex justify-center items-center border border-gray-400 py-2 px-7 rounded-full bg-white shadow-md text-gray-600">
              <img
                src="https://www.google.com/images/hpp/ic_wahlberg_product_core_48.png8.png"
                alt="Google Logo"
                className="h-6 w-6 mr-2"
              />
              Sign Up with Google
            </button> */}
              <GoogleLogin
                width={330}
                onSuccess={loginWithGoole}
                onError={() => {
                  console.log("Login Failed");
                }}
              />
              <p className="py-5 text-center">또는</p>
              <Button
                onClick={() => handleAuthModelOpen("/signup")}
                sx={{
                  width: "100%",
                  borderRadius: "29px",
                  py: "7px",
                  bgcolor: "#008000",
                }}
                variant="contained"
                size="large"
              >
                가입하기
              </Button>
              <p className="text-sm mt-2">
                가입하시면 서비스 약관 및 개인정보 보호정책에 동의하게 됩니다.
                (쿠키 사용을 포함한 정책.)
              </p>
            </div>
            <div className="mt-10">
              <h1 className="font-bold text-xl mb-5">계정이 있으신가요?</h1>
              <Button
                onClick={() => handleAuthModelOpen("/signin")}
                sx={{
                  width: "100%",
                  borderRadius: "29px",
                  py: "7px",
                }}
                variant="outlined"
                size="large"
              >
                로그인
              </Button>
            </div>
          </div>
        </Grid>
      </Grid>
      <AuthModel isOpen={authModelOpen} handleClose={handleAuthModelClose} />
    </div>
  );
};

export default Authentication;
