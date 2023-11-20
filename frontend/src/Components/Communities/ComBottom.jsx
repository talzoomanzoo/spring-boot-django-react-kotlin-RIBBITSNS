import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { findTwitsByAllComs } from "../../Store/Tweet/Action";
const ComBottom = ({changePage}) => {
    const dispatch = useDispatch();
    const { twit } = useSelector(store => store);

    useEffect(() => {
        dispatch(findTwitsByAllComs());

    }, [])

    return (
        <div>
            {twit.twits && twit.twits.length > 0 ? (
                twit.twits.map((item) => <TwitCard twit={item} key={item.id} changePage={changePage}/>)
            ) : (
                <div>게시된 리빗이 없습니다.</div>
            )}
        </div>
    )
};

export default ComBottom;