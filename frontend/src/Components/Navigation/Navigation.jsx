import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import { Avatar, Button, Menu, MenuItem } from "@mui/material";
import React from "react";
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

  return (
    <div className="h-screen sticky top-0 ">
      <div>
        <div className="py-5">
          <img
            className="w-10"
            src="https://waifu2x.booru.pics/outfiles/a2936a8caed993f9e006506b1afc9ace572e8d66_s2_n2_y1.png"
            alt=""
            onClick={()=> navigate(`/`)}
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
            ribbit
          </Button>
        </div>
      </div>

     <div className="flex items-center  justify-between">
     <div className="flex items-center space-x-3">
        <Avatar
          alt="Remy Sharp"
          src="https://cdn.pixabay.com/photo/2023/10/24/01/42/01-42-37-630_1280.png"
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
        <MenuItem onClick={handleLogout}>로그아웃</MenuItem>
      </Menu>
    
     </div>
    </div>
  );
};

export default Navigation;
