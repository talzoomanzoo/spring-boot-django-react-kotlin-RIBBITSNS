import NotificationsIcon from "@mui/icons-material/Notifications";
import React from "react";
import { useSelector, useDispatch } from "react-redux";
import "react-toastify/dist/ReactToastify.css"; // React Toastify 스타일
import "./Notification.css";
import { getAllNotifications } from "../../Store/Notification/Action";
import { useEffect } from "react";

const Notifications = () => {
  //const userId = useSelector((state) => state.auth.user.id);
  
  // const notificationCount = useSelector(
  //   (state) => state.notification.notifications[userId] || 0
  // );
  const dispatch = useDispatch();
  const { notification } = useSelector((store) => store);
  console.log("notifciation check",  notification);

  useEffect(() => {
    dispatch(getAllNotifications());
}, []);
  
  const notificationCount = notification.notifications?.length;

  return (
    <div>
      <NotificationsIcon alt="" />
      <div className="counter">
        {notificationCount}
        </div>
    </div>
  );
};

export default Notifications;
