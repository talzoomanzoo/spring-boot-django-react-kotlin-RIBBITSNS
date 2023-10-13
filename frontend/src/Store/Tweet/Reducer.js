// reducer.js
import {
  FIND_TWEET_BY_ID_REQUEST,
  FIND_TWEET_BY_ID_SUCCESS,
  GET_ALL_TWEETS_FAILURE,
  GET_ALL_TWEETS_REQUEST,
  GET_ALL_TWEETS_SUCCESS,
  GET_USERS_TWEET_FAILURE,
  GET_USERS_TWEET_REQUEST,
  GET_USERS_TWEET_SUCCESS,
  LIKE_TWEET_FAILURE,
  LIKE_TWEET_REQUEST,
  LIKE_TWEET_SUCCESS,
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
  FOLLOW_TWIT_REQUEST,
  FOLLOW_TWIT_SUCCESS,
  FOLLOW_TWIT_FAILURE,
} from "./ActionType";

const initialState = {
  loading: false,
  data: null,
  error: null,
  twits: [],
  twit: null
  // usersTwit
};

const tweetReducer = (state = initialState, action) => {
  switch (action.type) {
    case TWEET_CREATE_REQUEST:
    case TWEET_DELETE_REQUEST:
    case USER_LIKE_TWEET_REQUEST:
    case LIKE_TWEET_REQUEST:
    case VIEW_PLUS_REQUEST:
    case RETWEET_CREATE_REQUEST:
    case  FOLLOW_TWIT_REQUEST:  
    case FIND_TWEET_BY_ID_REQUEST:
      return {
        ...state, // Spread Parameter: 배열이 개별 원소가 됨 (아래 이외 다른 데이터는 그대로 유지.)
        loading: true,
        error: null,
      };
    case GET_ALL_TWEETS_REQUEST:
    case GET_USERS_TWEET_REQUEST:
          return {
            ...state,
            loading: true,
            error: null,
            twits:[]
          };
    case TWEET_CREATE_FAILURE:
    case TWEET_DELETE_FAILURE:
    case GET_ALL_TWEETS_FAILURE:
    case GET_USERS_TWEET_FAILURE:
    case USER_LIKE_TWEET_FAILURE:
    case LIKE_TWEET_FAILURE:
    case VIEW_PLUS_FAILURE:
    case RETWEET_CREATE_FAILURE:
      return {
        ...state,
        loading: false,
        error: action.payload,
      };
    case TWEET_CREATE_SUCCESS:
      console.log(" action ", action.payload);
      return {
        ...state,
        loading: false,
        twits: [action.payload, ...state.twits],
        error: null,
      };
    case GET_ALL_TWEETS_SUCCESS:
    case GET_USERS_TWEET_SUCCESS:
    // case USER_LIKE_TWEET_SUCCESS:
      return {
        ...state,
        loading: false,
        twits: action.payload,
        error: null,
      };
      case USER_LIKE_TWEET_SUCCESS:
        return {
          ...state,
          loading: false,
          likedTwits: action.payload,
          error: null,
        };
    case LIKE_TWEET_SUCCESS:
      return{
        ...state,
        loading:false,
        error:null,
        like:action.payload
      }
      case  FOLLOW_TWIT_SUCCESS:
        return {
          ...state,
          loading:false,
          twits:action.payload,
          error:null,
        }
    case VIEW_PLUS_SUCCESS:
        return{
          ...state,
          loading:false,
          error:null,
          count:action.payload
        }
    case TWEET_DELETE_SUCCESS:
      const twitIdToDelete = action.payload;
      return {
        ...state,
        loading: false,
        twits: state.twits.filter((twit) => twit.id !== twitIdToDelete),
        error: null,
      };

      case RETWEET_CREATE_SUCCESS:
        return {
          ...state,
          loading: false,
          retwit: action.payload,
          error: null,
        };

      case FIND_TWEET_BY_ID_SUCCESS:
      case REPLY_TWEET_SUCCESS:
        return {...state,loading:false,twit:action.payload,error:null}

        case UPDATE_TWEET_REQUEST:
          return {
            ...state,
            loading: true,
            error: null,
          };
        case UPDATE_TWEET_SUCCESS:
          return {
            ...state,
            loading: false,
            data: action.payload, // 액션에서 전달된 업데이트된 데이터
            error: null,
          };
        case UPDATE_TWEET_FAILURE:
          return {
            ...state,
            loading: false,
            data: null,
            error: action.error, // 액션에서 전달된 에러 메시지
          };
          case  FOLLOW_TWIT_FAILURE:
    default:
      return state;
  }
};

export default tweetReducer;
