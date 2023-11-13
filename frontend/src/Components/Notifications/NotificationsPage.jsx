import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import { useNavigate } from "react-router-dom";
import { getAllNotifications } from "../../Store/Notification/Action";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import NotificationsCard from "./NotificationsCard/NotificationsCard";

const NotificationsPage = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {notification} = useSelector((store) => store);
    const handleBack = () => {
        navigate(-1);
    };
    useEffect(() => {
        dispatch(getAllNotifications());
    }, []);

    return (
        <div id="np" className="space-y-5">
            <section
                className="space-y-5 customeScrollbar overflow-y-scroll css-scroll hideScrollbar border-gray-700 h-[40vh] w-full rounded-md">
                {notification.notifications && notification.notifications.length > 0 ? (
                    notification.notifications.map((item) => <NotificationsCard notification={item} key={item.id} />)
                ) : (
                    <div>알림이 없습니다.</div>
                )}
            </section>

        </div>
    )
}

export default NotificationsPage;