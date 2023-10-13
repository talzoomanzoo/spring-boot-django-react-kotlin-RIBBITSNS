import { useSelector } from "react-redux";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";

const FollowTwit = () => {
  const { auth, theme, twit } = useSelector((store) => store);
  console.log("followTwitAuth", auth);
  return (
    <div className="space-y-5">
      <section>
        <h1 className="py-5 text-xl font-bold opacity-90">Follow Twits</h1>
      </section>
      <section
        className={`${theme.currentTheme === "dark" ? "pt-14" : ""} space-y-5`}
      >
        {auth.findUser?.map((item) => (
          <div>
            <TwitCard twit={item} />
          </div>
        ))}
      </section>
    </div>
  );
};
export default FollowTwit;
