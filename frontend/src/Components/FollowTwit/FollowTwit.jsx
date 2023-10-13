import { Avatar } from "@mui/material";
import { useSelector } from "react-redux";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";

const FollowTwit = () => {
const {auth, theme} = useSelector(store=>store);
console.log("followTwitAuth", auth);
return (
    <div className="space-y-5">
        <section>
        <h1 className="py-5 text-xl font-bold opacity-90">Follow Twits</h1>
      </section>
      <section className={`${theme.currentTheme==="dark"?"pt-14":""} space-y-5`}>
        {auth.findUser?.map((item) => (
          <TwitCard twit={item} />
        ))}
      </section>
    </div>
)
};
export default FollowTwit;