import NotificationsIcon from "@mui/icons-material/Notifications";
import React from "react";
import { useSelector } from "react-redux";
import "react-toastify/dist/ReactToastify.css"; // React Toastify 스타일
import "./Notification.css";

const Notifications = () => {
  const userId = useSelector((state) => state.auth.user.id);
  const notificationCount = useSelector(
    (state) => state.notification.notifications[userId] || 0
  );

  return (
    <div>
      <NotificationsIcon alt="" />
      <div className="counter">{notificationCount}</div>
    </div>
  );
};

export default Notifications;
