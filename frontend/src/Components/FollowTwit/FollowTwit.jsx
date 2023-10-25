import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { followTwit } from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";
const FollowTwit = () => {
  const dispatch = useDispatch();
  const { twit, theme } = useSelector((store) => store);
  useEffect(() => {
    dispatch(followTwit());
  }, []); // [검사하고자 하는 특정 값]
return (
    <div className="space-y-5">
        <section>
        <h1 className="py-5 text-xl font-bold opacity-90">Following Twits</h1>
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
