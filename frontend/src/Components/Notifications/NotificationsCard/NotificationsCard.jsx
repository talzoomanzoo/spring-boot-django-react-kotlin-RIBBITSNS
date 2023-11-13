import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import { viewPlus } from "../../../Store/Tweet/Action";
import { decreaseNotificationCount } from "../../../Store/Notification/Action";

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
                onClick={handleNavigateToTwitDetial}
                className="cursor-pointer"
            />
            <div class="w-full">
                <div class="flex justify-between items-center">
                    <div
                        onClick={handleNavigateToTwitDetial}
                        className="flex cursor-pointer items-center space-x-1"
                    >
                        <li className="list-css"><span>{auth.user.fullName}의 게시물을 {notification.user.fullName}이 좋아합니다.</span></li>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default NotificationsCard;