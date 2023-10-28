import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import { Avatar, Button, Menu, MenuItem,Modal } from "@mui/material";
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

  return (
    <div className="h-screen sticky top-0 ">
      <div>
        <div className="py-5">
          <img
            className="w-10"
            src="https://cdn.pixabay.com/photo/2013/06/07/09/53/twitter-117595_1280.png"
            alt=""
          />
        </div>
        <div className="space-y-6">
          {navigationMenu.map((item) => (
            <div onClick={()=> item.title==="Profile"?navigate(`/profile/${auth.user?.id}`): navigate(`${item.path}`)} className="cursor-pointer flex space-x-3 items-center">
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
              bgcolor: "#1d9bf0",
            }}
            variant="contained"
            size="large"
            onClick={()=>handleFollowTwit()}
          >
            Tweet
          </Button>
        </div>
      </div>

     <div className="flex items-center  justify-between">
     <div className="flex items-center space-x-3">
        <Avatar
          alt="Remy Sharp"
          src="https://cdn.pixabay.com/photo/2020/07/01/12/58/icon-5359553_960_720.png"
        />

        <div>
          <p className="font-bold">{auth.user?.fullName}</p>
          <p className="opacity-70">@{auth.user?.fullName.split(" ")[0]}</p>
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
        <MenuItem onClick={handleLogout}>Logout</MenuItem>
        <MenuItem onClick={handleopenwithdrawl}>Withdrawal</MenuItem>
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
          <h1>
            정말로 탈퇴하시겠습니까?
          </h1>
          만약 탈퇴를 원하신다면 아래쪽에 본인의 비밀번호를 입력해주세요.
          <p><input type="text" placeholder='비밀번호'></input></p>
          <Button >확인</Button>
          <Button onClick={handleclosewithdrawl}>취소</Button>
        </div>
     </Modal>
    </div>

  );
};

export default Navigation;
