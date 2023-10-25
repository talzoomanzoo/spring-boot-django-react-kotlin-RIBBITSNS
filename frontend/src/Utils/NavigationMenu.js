import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ExploreIcon from '@mui/icons-material/Explore';
import GroupIcon from '@mui/icons-material/Group';
import HomeIcon from '@mui/icons-material/Home';
import ListAltIcon from '@mui/icons-material/ListAlt';
import MessageIcon from '@mui/icons-material/Message';
import NotificationsIcon from '@mui/icons-material/Notifications';
import PendingIcon from '@mui/icons-material/Pending';
import VerifiedIcon from '@mui/icons-material/Verified';

export const navigationMenu=[
    {
        title:"홈",
        icon:<HomeIcon/>,
        path:"/"
    },
    {
        title: "탐색",
        icon:<ExploreIcon/>,
        path:"/explore"
    },
    {
        title: "알림",
        icon:<NotificationsIcon/>,
        path:"/notifications"
    },
    {
        title:"메시지" ,
        icon:<MessageIcon/>,
        path:"/messages"
    },
    {
        title:"리스트" ,
        icon:<ListAltIcon/>,
        path:"/lists"
    },
    {
        title:"커뮤니티" ,
        icon:<GroupIcon/>,
        path:"/communities"
    },
    {
        title: "그린체크",
        icon:<VerifiedIcon/>,
        path:"/verified"
    },
    {
        title:"프로필" ,
        icon:<AccountCircleIcon/>,
        path:"/profile"
        
    },
    {
        title:"더보기" ,
        icon:<PendingIcon/>,
        path:"/more"
    },
]