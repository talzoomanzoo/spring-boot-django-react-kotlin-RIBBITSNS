import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router";
import { ToastContainer } from "react-toastify";
import { followTwit } from "../../Store/Tweet/Action";
import TwitCard from "../Home/MiddlePart/TwitCard/TwitCard";

const FollowTwit = ({ changePage, sendRefreshPage }) => {
  const dispatch = useDispatch();
  const { auth, twit, theme } = useSelector((store) => store);
  const navigate = useNavigate();
  const param = useParams();
  const handleBack = () => navigate(-1);

  useEffect(() => {
    dispatch(followTwit(param.id));
  }, [sendRefreshPage]);
  console.log("followTwittwit", twit);

  useEffect(() => {
    const messageEventListener = (event) => {
      const message = event.data;

      if (message.type === "navigate") {
        // 메시지가 navigate 타입일 때만 경로 변경
        navigate(message.path);
      }
    };

    window.addEventListener("message", messageEventListener);

    return () => {
      window.removeEventListener("message", messageEventListener);
    };
  }, [navigate]);

  return (
    <div>
            <section
                className={`z-50 flex items-center sticky top-0 bg-opacity-95`}
            >
        <KeyboardBackspaceIcon
          className="cursor-pointer"
          onClick={handleBack}
        />
        <h1 className="py-5 text-xl font-bold opacity-90 ml-5 ${}">
          {" "}
          팔로우한 리빗{" "}
        </h1>
      </section>
      <section
        className={`${theme.currentTheme === "dark" ? "pt-14" : ""} space-y-5`}
      >
        {twit.twits?.map((item) => (
          <TwitCard twit={item} changePage={changePage} />
        ))}
      </section>

      <ToastContainer
        position="bottom-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
};
export default FollowTwit;
