import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { findByTopLikes } from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import { useState } from "react";

const LikeTop = ({sendRefreshPage, changePage}) => {
    const dispatch = useDispatch();
    const { twit } = useSelector(store => store);

    useEffect(() => {
        dispatch(findByTopLikes());
    }, [sendRefreshPage])

    return (
            <div>
                {twit?.topLikesTwits && twit.topLikesTwits?.length > 0 ?
                    (
                        twit.topLikesTwits?.map((item) => <TwitCard twit={item} key={item.id} changePage={changePage} sendRefreshPage={sendRefreshPage}/>)
                    ) :
                    (
                        <div>게시된 리빗이 없습니다.</div>
                    )}
            </div>
    )
};

export default LikeTop;