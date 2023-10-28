import { memo, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import { findTwitsByListId } from "../../Store/Tweet/Action";
import { useParams, useNavigate } from "react-router";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import { Divider } from "@mui/material";

const ListsDetail = () => {
    const param = useParams();
    // twit/83
    const dispatch = useDispatch();
    const { twit, theme } = useSelector(store => store);
    // useSelector로 twit과 theme이라는 모듈의 상태값을 가져오도록 한 후, twit과 theme의 상태를 변경해서 궁극적으로 스토어의 상태를 변경
    // twit: twitReducer, theme: themeReducer
    console.log("detail", twit);
    // console.log("reply detail", twit.twit?.replyTwits.slice().reverse());
    const navigate = useNavigate();

    const handleBack = () => navigate(-1)
    // 뒤로가기, 앞으로가기는 navigate(1)
    useEffect(() => {
        dispatch(findTwitsByListId(param.id))
    }, [param.id])

    return (
        <div>
            <section
                className={`z-50 flex items-center sticky top-0 
        ${theme.currentTheme === "light" ? "bg-white" : "bg-[#0D0D0D]"
                    // theme, 즉 themeReducer의 initialState 속성의 currentTheme 변경
                    // 속성이 light이면,전자 아니면 후자
                    } bg-opacity-95`}
            >
                <KeyboardBackspaceIcon
                    className="cursor-pointer"
                    onClick={handleBack}
                />
                <h1 className="py-5 text-xl font-bold opacity-90 ml-5 ${}">
                    {"의 리빗"}
                </h1>
            </section>
            {twit?.twit && <TwitCard twit={twit?.twit} />}
            {/* twit.twit가 참이라면 TwitCard 렌더링 됨 */}
            <Divider sx={{ margin: "2rem 0rem" }} />

            <div>
                {/* {twit?.twit?.replyTwits?.slice().reverse().map((item) => <TwitCard twit={item} />)} */}
                {/* twit.twit notnull 일때 replyTwits 역순 배열 */}
            </div>
        </div>
    )
}

export default ListsDetail;
