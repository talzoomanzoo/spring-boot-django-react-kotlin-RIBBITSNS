import React, { useState } from "react";
import { Suspense } from "react";
import { useParams } from "react-router";
import Loading from "../Profile/Loading/Loading";
const FollowTwit = React.lazy(() => import("./FollowTwit"));
const FollowTwitEnc = ({changePage}) => {
    // const dispatch = useDispatch();
    // const navigate = useNavigate();
    // const { com } = useSelector((store) => store);
    const [uploading, setUploading] = useState(false);

    return (
        <div>
            <Suspense fallback={<div> {uploading ? <Loading /> : null} </div>}>
                <FollowTwit changePage={changePage}/>
            </Suspense>
        </div>


    )
};

export default FollowTwitEnc;
