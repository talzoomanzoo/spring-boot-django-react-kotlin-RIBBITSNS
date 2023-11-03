import CloseIcon from "@mui/icons-material/Close";
import {
  Box,
  IconButton,
  Modal
} from "@mui/material";
import React, { useEffect, useState } from "react";


import { useDispatch, useSelector } from "react-redux";

import FiberManualRecordIcon from "@mui/icons-material/FiberManualRecord";
import { makePaymentAction } from "../../Store/Payment/Action";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 600,
  //   height: "90vh",
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 2,
  borderRadius: 3,
  outline: "none",
  overflow: "scroll-y",
};

const SubscriptionModel = ({ handleClose, open }) => {
  const dispatch = useDispatch();
  const { auth } = useSelector((store) => store);
  const [plan,setPlan]=useState('yearly');

  useEffect(() => {}, [auth.user]);

  const makePayment=()=>{
console.log("plan ",plan)
    dispatch(makePaymentAction(plan));
  }

  return (
    <div>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <IconButton onClick={handleClose} aria-label="delete">
                <CloseIcon />
              </IconButton>
            </div>
          </div>
          <div className="flex justify-center py-10">
            <div className="w-[80%] space-y-10 hideScrollbar overflow-y-scroll  overflow-x-hidden ">
              <div className="p-5  rounded-md flex items-center justify-between bg-black shadow-lg">
                <h1 className="text-xl pr-5">
                  그린 가입자는 승인이 되면 '그린체크' 표시가 나타납니다.
                </h1>
                <img
                  className="w-24 h-24"
                  src="https://cdn.pixabay.com/photo/2016/01/20/18/59/confirmation-1152155_1280.png"
                  alt=""
                  loading="lazy"
                />
              </div>

              <div className="flex justify-between border rounded-full px-5 py-3">
                <div>
                  {" "}
                  <span className={`${plan==="yearly"?"text-white":"text-gray-400"} cursor-pointer`} onClick={()=>setPlan("yearly")}>매년</span>{" "}
                  <span  onClick={()=>setPlan("monthly")} className="text-green-500 text-sm ml-5">SAVE 90%</span>
                </div>
                <p onClick={()=>setPlan("monthly")} className={`${plan==="monthly"?"text-white":"text-gray-400"} cursor-pointer`}>월간</p>
              </div>

              <div className="space-y-3">
                <div className="flex items-center space-x-5">
                  <FiberManualRecordIcon sx={{ width: "7px", height: "7px" }} />
                  <p className=" text-xs">
                  대화 및 검색에서 우선 순위 지정
                  </p>
                </div>
                <div className="flex items-center space-x-5">
                  <FiberManualRecordIcon
                    sx={{
                      width: "7px",
                      height: "7px",
                      padding: "0px",
                      border: "",
                    }}
                  />
                  <p className=" text-xs">
                  귀하를 위한 광고와 팔로우 타임라인의 광고 사이에 
                  약 2배 더 많은 리빗이 표시
                  </p>
                </div>
                <div className="flex items-center space-x-5">
                  <FiberManualRecordIcon sx={{ width: "7px", height: "7px" }} />
                  <p className=" text-xs">
                  리빗에 굵은 기울임꼴 텍스트를 추가
                  </p>
                </div>
                <div className="flex items-center space-x-5">
                  <FiberManualRecordIcon sx={{ width: "7px", height: "7px" }} />
                  <p className=" text-xs">
                  더 긴 동영상과 1080p 동영상 업로드를 게시
                  </p>
                </div>

                <div className="flex items-center space-x-5">
                  <FiberManualRecordIcon sx={{ width: "7px", height: "7px" }} />
                  <p className=" text-xs">
                  리빗 편집, 폴더 북마크 및 새로운 기능에 대한 
                    조기 액세스를 포함한 모든 그린 기능이 가능
                  </p>
                </div>
              </div>

              <div onClick={makePayment} className=" cursor-pointer flex justify-center bg-white text-black rounded-full px-5 py-3">
                <span className="line-through italic">126,000₩</span>{" "}
                <span className="px-5">100,000₩/year</span>
              </div>
            </div>
          </div>

          {/* <BackdropComponent open={uploading} /> */}
        </Box>
      </Modal>
    </div>
  );
};

export default SubscriptionModel;
