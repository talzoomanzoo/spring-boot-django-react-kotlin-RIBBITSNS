import { memo, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import { findComById } from "../../Store/Community/Action";
import { findTwitsByComId } from "../../Store/Community/Action";
import { useParams, useNavigate } from "react-router";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";

const ComDetail = () => {
    const param = useParams();
    const dispatch = useDispatch();
    const { com, twit, theme } = useSelector(store => store);
    const navigate = useNavigate();
    const handleBack = () => navigate(-1)

    useEffect(() => {
        dispatch(findComById(param.id));
        console.log("log");
        //dispatch(findTwitsByComId(param.id))
    }, [])

    console.log("communities details check", com);

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
                    {`${com.com?.comName}`}
                </h1>
            </section>

            <section>
                <img
                    className="w-[100%] h-[15rem] object-cover"
                    src={
                        com.com?.backgroundImage ||
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

export default ComDetail;
