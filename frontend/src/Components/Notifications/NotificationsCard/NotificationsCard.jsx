import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import { viewPlus } from "../../../Store/Tweet/Action";
import { decreaseNotificationCount } from "../../../Store/Notification/Action";
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import { Avatar } from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";

const NotificationsCard = ({ notification }) => {
    const { auth } = useSelector((store) => store);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleNavigateToTwitDetial = () => {
          navigate(`/twit/${notification.twit.id}`);
          dispatch(viewPlus(notification.twit.id));
          window.location.reload();
          dispatch(decreaseNotificationCount(notification.id));
      };
    return (
        <div class="flex space-x-5">
            <div
                className="cursor-pointer"
            />
            <div class="w-full">
                <div class="flex justify-between items-center cursor-pointer">
                    <div
                        className="flex cursor-pointer items-center space-x-1"
                    ><Avatar
                    onClick={() => navigate(`/profile/${notification.user?.id}`)}
                    alt="Avatar"
                    src={
                        notification.user?.image
                          ? notification.user.image
                          : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"
                      }
                      loading="lazy"
                      style={{float:"left"}}
                    />
                        <div style={{width: "800px"}} onClick={handleNavigateToTwitDetial} ><li style={{listStyleType: 'none'}}><div style={{padding:"5px",float:"left"}}><span style={{padding:"5px",float:"left"}}>{notification.user.fullName}이(가)</span><span style={{padding:"5px",float:"left"}}>당신의 리빗을  좋아합니다.</span></div></li></div>
                    </div>
                </div>
                <hr
            style={{
              marginTop: 10,
              background: "hsla(0, 0%, 80%, 0.5)",
              borderColor: "hsl(0, 0%, 80%)",
              height: "5px",
            }}
          />
            </div>
        </div>
    );
};

export default NotificationsCard;