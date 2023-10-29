import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { followTwit } from "../../Store/Tweet/Action";
import { useNavigate } from "react-router";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
const FollowTwit = () => {
  const dispatch = useDispatch();
  const { twit, theme } = useSelector((store) => store);
  const navigate = useNavigate();
  const handleBack = () => navigate(-1)
  useEffect(() => {
    dispatch(followTwit());
  }, []); // [검사하고자 하는 특정 값]
  console.log("followTwittwit", twit);
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
        <h1 className="py-5 text-xl font-bold opacity-90 ml-5 ${}"> {"팔로우 한 리빗"} </h1>
      </section>
      <section
        className={`${theme.currentTheme === "dark" ? "pt-14" : ""} space-y-5`}
      >
        {twit.twits?.map((item) => (
          <TwitCard twit={item} />
        ))}
      </section>
    </div>
  );
};
export default FollowTwit;
