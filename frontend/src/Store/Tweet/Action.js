// actions.js

import { api } from "../../Config/apiConfig";
//import { ethicreveal } from "../../Components/Home/MiddlePart/TwitCard/TwitCard";
import {
  FIND_BY_TOP_LIKES_FAILURE,
  FIND_BY_TOP_LIKES_REQUEST,
  FIND_BY_TOP_LIKES_SUCCESS,
  FIND_BY_TOP_VIEWS_FAILURE,
  FIND_BY_TOP_VIEWS_REQUEST,
  FIND_BY_TOP_VIEWS_SUCCESS,
  FIND_TWEET_BY_ALL_COMS_FAILURE,
  FIND_TWEET_BY_ALL_COMS_REQUEST,
  FIND_TWEET_BY_ALL_COMS_SUCCESS,
  FIND_TWEET_BY_COM_ID_FAILURE,
  FIND_TWEET_BY_COM_ID_REQUEST,
  FIND_TWEET_BY_COM_ID_SUCCESS,
  FIND_TWEET_BY_ID_FAILURE,
  FIND_TWEET_BY_ID_REQUEST,
  FIND_TWEET_BY_ID_SUCCESS,
  FIND_TWEET_BY_LIST_ID_FAILURE,
  FIND_TWEET_BY_LIST_ID_REQUEST,
  FIND_TWEET_BY_LIST_ID_SUCCESS,
  FOLLOW_TWIT_FAILURE,
  FOLLOW_TWIT_REQUEST,
  FOLLOW_TWIT_SUCCESS,
  GET_ALL_TWEETS_FAILURE,
  GET_ALL_TWEETS_REQUEST,
  GET_ALL_TWEETS_SUCCESS,
  GET_USERS_REPLIES_FAILURE,
  GET_USERS_REPLIES_REQUEST,
  GET_USERS_REPLIES_SUCCESS,
  GET_USERS_TWEET_FAILURE,
  GET_USERS_TWEET_REQUEST,
  GET_USERS_TWEET_SUCCESS,
  LIKE_TWEET_FAILURE,
  LIKE_TWEET_REQUEST,
  LIKE_TWEET_SUCCESS,
  REPLY_TWEET_FAILURE,
  REPLY_TWEET_REQUEST,
  REPLY_TWEET_SUCCESS,
  RETWEET_CREATE_FAILURE,
  RETWEET_CREATE_REQUEST,
  RETWEET_CREATE_SUCCESS,
  TWEET_CREATE_FAILURE,
  TWEET_CREATE_REQUEST,
  TWEET_CREATE_SUCCESS,
  TWEET_DELETE_FAILURE,
  TWEET_DELETE_REQUEST,
  TWEET_DELETE_SUCCESS,
  UPDATE_TWEET_FAILURE,
  UPDATE_TWEET_REQUEST,
  UPDATE_TWEET_SUCCESS,
  USER_LIKE_TWEET_FAILURE,
  USER_LIKE_TWEET_REQUEST,
  USER_LIKE_TWEET_SUCCESS,
  VIEW_PLUS_FAILURE,
  VIEW_PLUS_REQUEST,
  VIEW_PLUS_SUCCESS,
} from "./ActionType";

export const createTweetRequest = () => ({
  type: TWEET_CREATE_REQUEST,
});

export const createTweetSuccess = (data) => ({
  type: TWEET_CREATE_SUCCESS,
  payload: data,
});

export const createTweetFailure = (error) => ({
  type: TWEET_CREATE_FAILURE,
  payload: error,
});

// Action for deleting a tweet
export const deleteTweetRequest = () => ({
  type: TWEET_DELETE_REQUEST,
});

export const deleteTweetSuccess = (tweetId) => ({
  type: TWEET_DELETE_SUCCESS,
  payload: tweetId,
});

export const deleteTweetFailure = (error) => ({
  type: TWEET_DELETE_FAILURE,
  payload: error,
});

// Action for getting all tweets
export const getAllTweetsRequest = () => ({
  type: GET_ALL_TWEETS_REQUEST,
});

export const getAllTweetsSuccess = (tweets) => ({
  type: GET_ALL_TWEETS_SUCCESS,
  payload: tweets,
});

export const getAllTweetsFailure = (error) => ({
  type: GET_ALL_TWEETS_FAILURE,
  payload: error,
});

export const findTopLikesRequest = () => ({
  type: FIND_BY_TOP_LIKES_REQUEST,
});

export const findTopLikesSuccess = (tweets) => ({
  type: FIND_BY_TOP_LIKES_SUCCESS,
  payload: tweets,
});

export const findTopLikesFailure = (error) => ({
  type: FIND_BY_TOP_LIKES_FAILURE,
  payload: error,
});

export const findTopViewsRequest = () => ({
  type: FIND_BY_TOP_VIEWS_REQUEST,
});

export const findTopViewsSuccess = (tweets) => ({
  type: FIND_BY_TOP_VIEWS_SUCCESS,
  payload: tweets,
});

export const findTopViewsFailure = (error) => ({
  type: FIND_BY_TOP_VIEWS_FAILURE,
  payload: error,
});

export const getAllTweets = () => {
  return async (dispatch) => {
    dispatch(getAllTweetsRequest());
    try {
      const response = await api.get("/api/twits/");
      console.log("all tweets ", response.data);
      dispatch(getAllTweetsSuccess(response.data));
    } catch (error) {
      dispatch(getAllTweetsFailure(error.message));
    }
  };
};

export const findByTopLikes = () => {
  return async (dispatch) => {
    dispatch({ type: FIND_BY_TOP_LIKES_REQUEST });
    try {
      const response = await api.get("/api/twits/toplikes");
      dispatch({ type: FIND_BY_TOP_LIKES_SUCCESS, payload: response.data });
    } catch (error) {
      dispatch({ type: FIND_BY_TOP_LIKES_SUCCESS, payload: error.message });
    }
  };
};

export const findByTopViews = () => {
  return async (dispatch) => {
    dispatch(findTopViewsRequest());
    try {
      const response = await api.get("/api/twits/topviews");
      dispatch(findTopViewsSuccess(response.data));
    } catch (error) {
      dispatch(findTopViewsFailure(error.message));
    }
  };
};

export const getUsersTweets = (userId) => {
  return async (dispatch) => {
    dispatch({ type: GET_USERS_TWEET_REQUEST });
    try {
      const response = await api.get(`/api/twits/user/${userId}`);
      console.log("users tweets ", response.data);
      dispatch({ type: GET_USERS_TWEET_SUCCESS, payload: response.data });
    } catch (error) {
      dispatch({ type: GET_USERS_TWEET_FAILURE, payload: error.message });
    }
  };
};

export const getUsersReplies = (userId) => {
  return async (dispatch) => {
    dispatch({ type: GET_USERS_REPLIES_REQUEST });
    try {
      const response = await api.get(`/api/twits/user/${userId}/replies`);
      console.log("users replies", response.data);
      dispatch({ type: GET_USERS_REPLIES_SUCCESS, payload: response.data });
    } catch (error) {
      dispatch({ type: GET_USERS_REPLIES_FAILURE, payload: error.message });
    }
  };
};

export const findTwitsByLikesContainUser = (userId) => {
  return async (dispatch) => {
    dispatch({ type: USER_LIKE_TWEET_REQUEST });
    try {
      const response = await api.get(`/api/twits/user/${userId}/likes`);
      console.log("liked tweets ", response.data);
      dispatch({ type: USER_LIKE_TWEET_SUCCESS, payload: response.data });
    } catch (error) {
      dispatch({ type: USER_LIKE_TWEET_FAILURE, payload: error.message });
    }
  };
};

export const findTwitsById = (twitId) => {
  return async (dispatch) => {
    dispatch({ type: FIND_TWEET_BY_ID_REQUEST });
    try {
      const response = await api.get(`/api/twits/${twitId}`);
      console.log("find tweets by id ", response.data);
      dispatch({ type: FIND_TWEET_BY_ID_SUCCESS, payload: response.data });
    } catch (error) {
      dispatch({ type: FIND_TWEET_BY_ID_FAILURE, payload: error.message });
    }
  };
};

export const findTwitsByAllComs = () => {
  return async (dispatch) => {
      dispatch({type:FIND_TWEET_BY_ALL_COMS_REQUEST});
      try{
        const response = await api.get(`/api/twits/allComs`);
        dispatch({type: FIND_TWEET_BY_ALL_COMS_SUCCESS, payload: response.data});
      } catch (error) {
        dispatch({type: FIND_TWEET_BY_ALL_COMS_FAILURE, payload:error.message});
      }
  }
};

export const findTwitsByListId = (listId) => {
  return async (dispatch) => {
    dispatch({ type: FIND_TWEET_BY_LIST_ID_REQUEST });
    try {
      const response = await api.get(`/api/twits/${listId}/listTwit`);
      dispatch({ type: FIND_TWEET_BY_LIST_ID_SUCCESS, payload: response.data });
    } catch (error) {
      dispatch({ type: FIND_TWEET_BY_LIST_ID_FAILURE, payload: error.message });
    }
  }
};

export const findTwitsByComId = (comId)  => {
  return async (dispatch) => {
      dispatch({type: FIND_TWEET_BY_COM_ID_REQUEST})
      try {
        const response = await api.get(`/api/twits/${comId}/comTwit`);
        dispatch({type:FIND_TWEET_BY_COM_ID_SUCCESS,payload:response.data});
      } catch (error) {
        dispatch({type:FIND_TWEET_BY_COM_ID_FAILURE,payload:error.message});
      }
    };
};

export const updateTweet = (twit) => {
  console.log("twit", twit);
  return async (dispatch) => {
    dispatch({ type: UPDATE_TWEET_REQUEST });
    try {
      const { data } = await api.post(`/api/twits/edit`, twit);
      console.log("data", data);
      //const response = await ethicreveal(data.id,data.content);
      dispatch({ type: UPDATE_TWEET_SUCCESS, payload: data });
    } catch (error) {
      dispatch({ type: UPDATE_TWEET_FAILURE, payload: error.message });
    }
  };
};

export const createTweet = (tweetData) => {
  return async (dispatch) => {
    dispatch(createTweetRequest());
    try {
      const { data } = await api.post("/api/twits/create", tweetData);
      console.log("tweetData: ", tweetData);
      console.log("created twit ", data);
      dispatch(createTweetSuccess(data));
      console.log("data.id: ", data.id);
      console.log("data.id: ", data.content);
      console.log("data.location: ", data.location);
    } catch (error) {
      dispatch(createTweetFailure(error.message));
    }
  };
};

export const createTweetReply = (tweetData) => {
  return async (dispatch) => {
    dispatch({ type: REPLY_TWEET_REQUEST });
    console.log("td", tweetData);
    try {
      const { data } = await api.post("/api/twits/reply", tweetData);
      console.log("reply twit ", data);
      dispatch({ type: REPLY_TWEET_SUCCESS, payload: data });
    } catch (error) {
      dispatch({ type: REPLY_TWEET_FAILURE, payload: error.message });
    }
  };
};

export const createRetweet = (twitId) => {
  return async (dispatch) => {
    dispatch({ type: RETWEET_CREATE_REQUEST });
    try {
      const response = await api.put(`/api/twits/${twitId}/retwit`);
      dispatch({
        type: RETWEET_CREATE_SUCCESS,
        payload: response.data, // Assuming the response contains the created retweet data
      });
    } catch (error) {
      dispatch({
        type: RETWEET_CREATE_FAILURE,
        payload: error.message, // Or handle the error as required
      });
    }
  };
};

export const likeTweet = (twitId) => {
  return async (dispatch) => {
    dispatch({ type: LIKE_TWEET_REQUEST });
    try {
      const { data } = await api.post(`/api/${twitId}/like`, {});
      console.log("like twit ", data);
      dispatch({ type: LIKE_TWEET_SUCCESS, payload: data });
    } catch (error) {
      dispatch({ type: LIKE_TWEET_FAILURE, payload: error.message });
    }
  };
};

export const viewPlus = (twitId) => {
  return async (dispatch) => {
    dispatch({ type: VIEW_PLUS_REQUEST });
    try {
      const { data } = await api.post(`/api/twits/${twitId}/count`, {});
      console.log("view plus ", data);
      dispatch({ type: VIEW_PLUS_SUCCESS, payload: data });
    } catch (error) {
      dispatch({ type: VIEW_PLUS_FAILURE, payload: error.message });
    }
  };
};

// export const notificationPlus = (twitId) => {
//   return async (dispatch) => {
//     dispatch({type:NOTIFICATION_PLUS_REQUEST});
//     try {
//       const {data} = await api.post(`/api/twits/${twitId}/Ncount`, {});
//       console.log("notification plus ",data)
//       dispatch({type:NOTIFICATION_PLUS_SUCCESS,payload:data});
//     } catch (error) {
//       dispatch({type:NOTIFICATION_PLUS_FAILURE,payload:error.message});
//     }
//   };
// };

export const deleteTweet = (tweetId) => {
  return async (dispatch) => {
    dispatch(deleteTweetRequest());
    try {
      await api.delete(`/api/twits/${tweetId}`);
      dispatch(deleteTweetSuccess(tweetId));
      console.log("delete twit ", tweetId);
    } catch (error) {
      dispatch(deleteTweetFailure(error.message));
    }
  };
};

export const followTwit = () => async (dispatch) => {
  dispatch({ type: FOLLOW_TWIT_REQUEST });
  try {
    const response = await api.get(`/api/followtwit/`);
    const user = response.data;
    dispatch({ type: FOLLOW_TWIT_SUCCESS, payload: user });
    console.log("find by twit user -: ", user);
  } catch (error) {
    dispatch({ type: FOLLOW_TWIT_FAILURE, payload: error.message });
  }
};

export const getTime = (datetime, currTimestamp) => {
  const totalMilliseconds = currTimestamp - datetime;
  //console.log("totalMilliseconds+", totalMilliseconds);
  const totalSeconds = Math.floor(totalMilliseconds / 1000);
  const totalMinutes = Math.floor(totalSeconds / 60);
  const totalHours = Math.floor(totalMinutes / 60);
  const totalDays = Math.floor(totalHours / 24);

  if (totalDays > 0) {
    return `${totalDays}일 전`;
  } else if (totalHours > 0) {
    return `${totalHours}시간 전`;
  } else if (totalMinutes > 0) {
    return `${totalMinutes}분 전`;
  } else {
    return `${totalSeconds}초 전`;
  }
};
