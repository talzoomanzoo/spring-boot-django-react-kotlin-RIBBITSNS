import React, { useState } from "react";
import { Suspense/*, Loading*/ } from "react";
import Loading from "./../Profile/Loading/Loading";
import { useParams } from "react-router";
const FollowTwit = React.lazy(() => import("./FollowTwit"));


const FollowTwitEnc = () => {
    // const dispatch = useDispatch();
    // const navigate = useNavigate();
    // const { com } = useSelector((store) => store);
    const [uploading, setUploading] = useState(false);

    return (
        <div>
            <Suspense fallback={<div> {uploading ? <Loading /> : null} </div>}>
                <FollowTwit />
            </Suspense>
        </div>


    )
};

export default FollowTwitEnc;
