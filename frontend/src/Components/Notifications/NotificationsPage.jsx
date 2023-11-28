import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { getAllNotifications } from "../../Store/Notification/Action";
import NotificationsCard from "./NotificationsCard/NotificationsCard";

const NotificationsPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { notification, theme } = useSelector((store) => store);
  const handleBack = () => {
    navigate(-1);
  };
  useEffect(() => {
    dispatch(getAllNotifications());
  }, []);

  useEffect(() => {
    const messageEventListener = (event) => {
      const message = event.data;

      if (message.type === "navigate") {
        // 메시지가 navigate 타입일 때만 경로 변경
        navigate(message.path);
      }
    };

    window.addEventListener("message", messageEventListener);

    return () => {
      window.removeEventListener("message", messageEventListener);
    };
  }, [navigate]);

  return (
    <div>
      <section
        className={`z-50 flex items-center sticky top-0 ${
          theme.currentTheme === "dark" ? " bg-[#0D0D0D]" : "bg-white"
        }`}
      >
        <div className="z-50 flex items-center sticky top-0 space-x-5">
          <KeyboardBackspaceIcon
            className="cursor-pointer"
            onClick={handleBack}
          />
          <h1 className="py-5 text-xl font-bold opacity-90 ml-5">알림</h1>
        </div>
      </section>
      <div id="np" className="space-y-3" style={{ marginTop: 20 }}>
        <section className="space-y-5 customeScrollbar overflow-y-scroll css-scroll hideScrollbar border-gray-700 w-full rounded-md">
          {notification.notifications &&
          notification.notifications.length > 0 ? (
            notification.notifications.map((item) => (
              <NotificationsCard notification={item} key={item.id} />
            ))
          ) : (
            <div>알림이 없습니다.</div>
          )}
        </section>
      </div>

      <ToastContainer
        position="bottom-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
};

export default NotificationsPage;
