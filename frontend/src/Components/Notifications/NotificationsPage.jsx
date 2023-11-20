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
        <div>
            <section className={`z-50 flex items-center sticky top-0 bg-opacity-95`}>
            <div className="z-50 flex items-center sticky top-0 space-x-5">
                    <KeyboardBackspaceIcon
                        className="cursor-pointer"
                        onClick={handleBack}
                    />
                    <h1 className="py-5 text-xl font-bold opacity-90 ml-5">
                        알림
                    </h1>
                </div>
            </section>
        <div id="np" className="space-y-3" style={{ marginTop: 20}}>
            <section
                className="space-y-5 customeScrollbar overflow-y-scroll css-scroll hideScrollbar border-gray-700 h-[40vh] w-full rounded-md">
                {notification.notifications && notification.notifications.length > 0 ? (
                    notification.notifications.map((item) => <NotificationsCard notification={item} key={item.id} />)
                ) : (
                    <div>알림이 없습니다.</div>
                )}
            </section>

        </div>
        </div>
    )
}

export default NotificationsPage;