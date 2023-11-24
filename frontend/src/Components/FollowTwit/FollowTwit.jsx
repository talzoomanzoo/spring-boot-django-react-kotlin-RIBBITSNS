import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { followTwit } from "../../Store/Tweet/Action";
import { useNavigate } from "react-router";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
import { useParams } from "react-router";
const FollowTwit = ({changePage, sendRefreshPage}) => {
  const dispatch = useDispatch();
  const { auth, twit, theme } = useSelector((store) => store);
  const navigate = useNavigate();
  const param = useParams();
  const handleBack = () => navigate(-1);
  useEffect(() => {
    dispatch(followTwit(param.id));
  }, [sendRefreshPage]); 
  console.log("followTwittwit", twit);
  return (
    <div>
            <section
                className={`z-50 flex items-center sticky top-0 bg-opacity-95`}
            >
        <KeyboardBackspaceIcon
          className="cursor-pointer"
          onClick={handleBack}
        />
        <h1 className="py-5 text-xl font-bold opacity-90 ml-5 ${}"> 팔로우한 리빗 </h1>
      </section>
      <section
        className={`${theme.currentTheme === "dark" ? "pt-14" : ""} space-y-5`}
      >
        {twit.twits?.map((item) => (
          <TwitCard twit={item} changePage={changePage}/>
        ))}
      </section>
    </div>
  );
};
export default FollowTwit;
