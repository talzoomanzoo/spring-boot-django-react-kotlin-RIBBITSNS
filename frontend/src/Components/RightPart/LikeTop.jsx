import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { findByTopLikes } from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";

const LikeTop = ({sendRefreshPage, changePage}) => {
    const dispatch = useDispatch();
    const { twit } = useSelector(store => store);
    //const [refreshTwits, setRefreshTwits] = useState(0);
    //twit.topLikesTwits.sort((a, b) => b.totalLikes - a.totalLikes);

    useEffect(() => {
        dispatch(findByTopLikes());
        //console.log("useEffect test2");
    }, [sendRefreshPage])

    return (
            <div>
                {twit?.topLikesTwits && twit.topLikesTwits?.length > 0 ?
                    (
                        twit.topLikesTwits?.map((item) => <TwitCard twit={item} key={item.id} changePage={changePage}/>)
                    ) :
                    (
                        <div>게시된 리빗이 없습니다.</div>
                    )}
            </div>
    )
};

export default LikeTop;