import { useSelector } from "react-redux";

const NotificationsCard = ({ notification }) => {
    const { auth } = useSelector((store) => store);
    return (
        <div class="flex space-x-5">
            <div
                //onClick={handleNavigateToListsDetail}
                className="cursor-pointer"
            />
            <div class="w-full">
                <div class="flex justify-between items-center">
                    <div
                        //onClick={handleNavigateToListsDetail}
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