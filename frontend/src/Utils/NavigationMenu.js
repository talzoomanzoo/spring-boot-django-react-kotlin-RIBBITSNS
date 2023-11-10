import AccountBoxIcon from "@mui/icons-material/AccountBox";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import ForumIcon from "@mui/icons-material/Forum";
import GroupsIcon from "@mui/icons-material/Groups";
import HouseIcon from "@mui/icons-material/House";
import ListIcon from "@mui/icons-material/List";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import Notifications from "../Components/Notifications/Notifications";

export const navigationMenu = [
  {
    id: 1,
    title: "홈",
    icon: <HouseIcon />,
    path: "/",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 3,
    title: "알림",
    icon: <Notifications />,
    path: "/notifications",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 4,
    title: "메시지",
    icon: <ForumIcon />,
    path: "/messages",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 5,
    title: "리스트",
    icon: <ListIcon />,
    path: "/lists",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 6,
    title: "커뮤니티",
    icon: <GroupsIcon />,
    path: "/communities",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 7,
    title: "그린체크",
    icon: <CheckCircleIcon />,
    path: "/verified",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 8,
    title: "프로필",
    icon: <AccountBoxIcon />,
    path: "/profile",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 8,
    title: "팔로우 한 리빗",
    icon: <AccountBoxIcon />,
    path: "/followtwit",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
  {
    id: 9,
    title: "더보기",
    icon: <MoreHorizIcon />,
    path: "/more",
    pattern: {
      y: 16,
      squares: [
        [0, 1],
        [1, 3],
      ],
    },
  },
];
