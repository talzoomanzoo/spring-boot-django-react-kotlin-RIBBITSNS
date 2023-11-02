import AccountBoxIcon from '@mui/icons-material/AccountBox';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ExploreIcon from '@mui/icons-material/Explore';
import ForumIcon from '@mui/icons-material/Forum';
import GroupsIcon from '@mui/icons-material/Groups';
import HouseIcon from '@mui/icons-material/House';
import ListIcon from '@mui/icons-material/List';
import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import NotificationsIcon from '@mui/icons-material/Notifications';

export const navigationMenu=[
    {
        title:"홈",
        icon:<HouseIcon/>,
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
        icon:<ForumIcon/>,
        path:"/messages"
    },
    {
        title:"리스트" ,
        icon:<ListIcon/>,
        path:"/lists"
    },
    {
        title:"커뮤니티" ,
        icon:<GroupsIcon/>,
        path:"/communities"
    },
    {
        title: "그린체크",
        icon:<CheckCircleIcon/>,
        path:"/verified"
    },
    {
        title:"프로필" ,
        icon:<AccountBoxIcon/>,
        path:"/profile"
        
    },
    {
        title:"더보기" ,
        icon:<MoreHorizIcon/>,
        path:"/more"
    },
]