import { Alert, Box, Button, IconButton } from '@mui/material'
import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import FiberManualRecordIcon from "@mui/icons-material/FiberManualRecord";
import { useNavigate } from 'react-router-dom'
import { verifiedAccountAction } from '../../Store/Payment/Action';

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
const VerifiedSuccess = () => {
    const navigate=useNavigate()
    const {auth}=useSelector(store=>store);
    const dispatch=useDispatch();
    const urlParams = new URLSearchParams(window.location.search);
    

    useEffect(()=>{
dispatch(verifiedAccountAction(urlParams.get("razorpay_payment_link_id")))
    },[])
  return (
    <div className='px-36 flex flex-col h-screen justify-center items-center'> 
    <Box sx={style}>
          
        
            <div className=" space-y-10 p-10 ">
              <div className='flex flex-col items-center justify-center'>
                <img
                  className="w-16 h-16  "
                  src="https://cdn.pixabay.com/photo/2016/01/20/18/59/confirmation-1152155_1280.png"
                  alt=""
                  loading="lazy"
                />
                <Alert  className='my-5 font-bold text-3xl' severity="success">축하드립니다 인증이 되었습니다!</Alert>
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

              <div>
            
    <Button className="rounded-full my-5" sx={{width:"100%",borderRadius:"25px", padding:"12px 0px"}} onClick={()=>navigate(`/profile/${auth.user.id}`)}  variant='contained'>Go To Profile</Button>
            </div>
            </div>
          
   

         
        </Box>
    
    </div>
  )
}

export default VerifiedSuccess