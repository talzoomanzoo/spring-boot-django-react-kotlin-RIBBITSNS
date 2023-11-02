import { memo, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import { findListById } from "../../Store/List/Action";
import { findTwitsByListId } from "../../Store/Tweet/Action";
import { useParams, useNavigate } from "react-router";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import { Divider } from "@mui/material";

const ListsDetail = () => {
    const param = useParams();
    const dispatch = useDispatch();
    const { list, twit, theme } = useSelector(store => store);
    // useSelector로 twit과 theme이라는 모듈의 상태값을 가져오도록 한 후, twit과 theme의 상태를 변경해서 궁극적으로 스토어의 상태를 변경
    // twit: twitReducer, theme: themeReducer
    // console.log("reply detail", twit.twit?.replyTwits.slice().reverse());
    const navigate = useNavigate();
    const handleBack = () => navigate(-1)
    // 뒤로가기, 앞으로가기는 navigate(1)
    useEffect(() => {
        dispatch(findListById(param.id))
        dispatch(findTwitsByListId(param.id))
    }, [param.id])

    console.log("listname check", list);


    return (
        <div>
            <section
                className={`z-50 flex items-center sticky top-0 ${theme.currentTheme === "light" ? "light" : "dark"
                    } bg-opacity-95`}
            >
                <KeyboardBackspaceIcon
                    className="cursor-pointer"
                    onClick={handleBack}
                />
                <h1 className="py-5 text-xl font-bold opacity-90 ml-5 ${}">
                    {`${list.list?.listName} 리스트의 리빗`}
                </h1>
            </section>
            <section>
                <img
                    className="w-[100%] h-[15rem] object-cover"
                    src={
                        list.findUser?.backgroundImage ||
                        "https://png.pngtree.com/thumb_back/fw800/background/20230304/pngtree-green-base-vector-smooth-background-image_1770922.jpg"
                    }
                    alt=""
                    loading="lazy"
                />
            </section>
            <div style={{ marginTop: 20 }}>
                {twit.twits && twit.twits.length > 0 ?
                    (
                        twit.twits.map((item) => <TwitCard twit={item} key={item.id} />)
                    ) :
                    (
                        <div>게시된 리빗이 없습니다.</div>
                    )}
            </div>
        </div>
    )
}

export default ListsDetail;
