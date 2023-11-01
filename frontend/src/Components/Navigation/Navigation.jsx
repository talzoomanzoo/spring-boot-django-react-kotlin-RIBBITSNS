import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import { Avatar, Button, Menu, MenuItem, Modal } from "@mui/material";
import React,{useState} from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout } from "../../Store/Auth/Action";
import { followTwit } from '../../Store/Tweet/Action';
import { navigationMenu } from "../../Utils/NavigationMenu";

const Navigation = () => {
  const {auth}=useSelector(store=>store);
  const [anchorEl, setAnchorEl] = React.useState(null);
  const openLogoutMenu = Boolean(anchorEl);
  const dispatch=useDispatch();
  const navigate=useNavigate();

  const handleOpenLogoutMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout=()=>{
    dispatch(logout())
    handleClose()
  }
  const handleFollowTwit=()=> {
    navigate(`/followTwit`)
    dispatch(followTwit())
  }

  const [openwithdrawl, setopenwithdrawl] = useState(false);
  const handleopenwithdrawl = () => {
    setopenwithdrawl(true);
  };

  const handleclosewithdrawl = () => {
    setopenwithdrawl(false);
  };

  const jwtToken = localStorage.getItem("jwt");
  const accountwithdrawal = async()=>{
    try {
      const response = await fetch("http://localhost:8080/api/users/withdraw",{
        method:'POST',
        headers:{
          'Authorization':`Bearer ${jwtToken}`,
        },
      });
      if(response.status === 200){
        localStorage.removeItem("jwt");
        window.location.reload();
      }
    } catch (error) {
      console.error("Failed to delete:", error);
    }
  };

  return (
    <div className="h-screen sticky top-0 ">
      <div>
        <div className="cursor-pointer py-5">
          <img
            className="w-10"
            src="https://cdn.pixabay.com/photo/2023/10/26/06/44/06-44-04-156_1280.png"
            alt=""
            onClick={()=> navigate(`/`)}
            loading="lazy"
          />
        </div>
        <div className="space-y-6">
          {navigationMenu.map((item) => (
            <div onClick={()=> item.title==="프로필"?navigate(`/profile/${auth.user?.id}`): navigate(`${item.path}`)} className="cursor-pointer flex space-x-3 items-center">
              {item.icon}
              <p className="text-xl">{item.title}</p>
            </div>
          ))}
        </div>
        <div className="py-10">
          <Button
            type="button"
            sx={{
              width: "100%",
              borderRadius: "29px",
              py: "15px",
              bgcolor: "#42c924",
            }}
            variant="contained"
            size="large"
            onClick={()=>handleFollowTwit()}
          >
            Followed Ribbits
          </Button>
        </div>
      </div>

     <div className="flex items-center  justify-between">
     <div className="flex items-center space-x-3">
        <Avatar
          alt="Remy Sharp"
          src={auth.user?.image? auth.user.image : "https://cdn.pixabay.com/photo/2023/10/24/01/42/01-42-37-630_1280.png"}
          loading="lazy"
        />

        <div>
          <p className="font-bold">{auth.user?.fullName}</p>
          <p className="opacity-70">{auth.user?.email.split(" ")[0]}</p>
        </div>
      </div>
      <Button
        id="basic-button"
        aria-controls={openLogoutMenu ? 'basic-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={openLogoutMenu ? 'true' : undefined}
        onClick={handleOpenLogoutMenu}
      >
          <MoreHorizIcon/>
      </Button>
      <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={openLogoutMenu}
        onClose={handleClose}
        MenuListProps={{
          'aria-labelledby': 'basic-button',
        }}
      >
        <MenuItem onClick={handleLogout}>로그아웃</MenuItem>
        <MenuItem onClick={handleopenwithdrawl}>회원탈퇴</MenuItem>
      </Menu>
    
     </div>
     <Modal
        open={openwithdrawl}
        onClose={handleclosewithdrawl}
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
     >
        <div className="withdrawal-modal" style={{ background: "white", padding: "20px", borderRadius: "8px" }}>
          <p id="description">
            정말로 탈퇴하시겠습니까? 탈퇴하는 순간 모든 게시물을 삭제 되어집니다.
          </p>
          <Button onClick={accountwithdrawal}>확인</Button>
          <Button onClick={handleclosewithdrawl}>취소</Button>
        </div>
     </Modal>
     
    </div>
  );
};

export default Navigation;
