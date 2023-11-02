import NotificationsIcon from "@mui/icons-material/Notifications";
import React from "react";
import { useSelector } from "react-redux";
import "./Notification.css";

const Notifications = () => {
    const userId = useSelector((state) => state.auth.user.id); // 예시로 Redux Store의 'auth' 슬라이스에서 userId를 가져오는 예시
    const notificationCount = useSelector((state) => state.notification.notifications[userId] || 0);
    console.log("zxcv", notificationCount);
    console.log("uiop", userId);
  
    return (
      <div className="icon">
        <NotificationsIcon className="iconImg" alt=""/>
        <div className="counter">{notificationCount}</div>
      </div>
    );
  };

export default Notifications;
