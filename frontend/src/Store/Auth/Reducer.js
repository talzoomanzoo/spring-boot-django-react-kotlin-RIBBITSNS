// authReducer.js
//Redux 상태를 관리하고 액션에 따라 해당 상태를 업데이트합니다.
import {
  FIND_USER_BY_ID_FILURE,
  FIND_USER_BY_ID_REQUEST,
  FIND_USER_BY_ID_SUCCESS,
  FOLLOW_USER_FAILURE,
  FOLLOW_USER_REQUEST,
  FOLLOW_USER_SUCCESS,
  GET_PROFILE_FAILURE,
  GET_PROFILE_REUEST,
  GET_PROFILE_SUCCESS,
  LOGIN_FAILURE,
  LOGIN_REQUEST,
  LOGIN_SUCCESS,
  LOGOUT,
  REGISTER_FAILURE,
  REGISTER_REQUEST,
  REGISTER_SUCCESS,
  SEARCH_TWIT_FAILURE,
  SEARCH_TWIT_REQUEST,
  SEARCH_TWIT_SUCCESS,
  SEARCH_USER_FAILURE,
  SEARCH_USER_REQUEST,
  SEARCH_USER_SUCCESS,
  UPDATE_USER_FAILURE,
  UPDATE_USER_REQUEST,
  UPDATE_USER_SUCCESS,
} from "./ActionType";

const initialState = {
  user: null,
  loading: false,
  error: null,
  jwt: null,
  updateUser: false,
  userSearchResult: [],
  tweetSearchResult: [],
};
// 초기 상태를 정의합니다. 초기 상태는 사용자 정보, 로딩 상태, 오류 정보, JWT 토큰, 사용자 정보 업데이트 여부, 검색 결과 배열을 포함합니다.

const authReducer = (state = initialState, action) => {
  //Redux 리듀서 함수를 정의합니다. 이 함수는 현재 상태(state)와 디스패치된 액션(action)을 받아 새로운 상태를 반환합니다.
  switch (
    action.type //액션 유형에 따라 다른 동작을 수행하기 위한 switch 문을 시작합니다.
  ) {
    case LOGIN_REQUEST:
    case REGISTER_REQUEST:
    case GET_PROFILE_REUEST: //LOGIN_REQUEST, REGISTER_REQUEST, GET_PROFILE_REUEST 등의 요청 액션은 로딩 상태를 true로 설정하고 오류를 초기화합니다.
    case FIND_USER_BY_ID_REQUEST:
    case SEARCH_TWIT_REQUEST:
    case FOLLOW_USER_REQUEST:
      return { ...state, loading: true, error: null };
    case SEARCH_USER_REQUEST: //검색 결과 배열을 초기화하고 로딩 상태를 true로 설정합니다.
      return { ...state, userSearchResult: [], loading: true, error: null };

    case UPDATE_USER_REQUEST: //사용자 정보 업데이트 상태를 false로 설정하고 로딩 상태를 true로 설정합니다.
      return { ...state, loading: true, error: null, updateUser: false };

    case GET_PROFILE_SUCCESS: //GET_PROFILE_SUCCESS는 사용자 정보를 업데이트하고 updateUser 플래그를 true로 설정합니다.
      return { ...state, loading: false, error: null, user: action.payload };

    case UPDATE_USER_SUCCESS:
      return {
        ...state,
        loading: false,
        error: null,
        user: action.payload,
        updateUser: true,
      };

    case LOGIN_SUCCESS:
    case REGISTER_SUCCESS: //JWT 토큰을 저장하고 로딩 상태를 false로 설정합니다.
      return { ...state, loading: false, jwt: action.payload, error: null };

    case FIND_USER_BY_ID_SUCCESS: //특정 사용자 검색 결과를 업데이트합니다.
      return {
        ...state,
        loading: false,
        findUser: action.payload,
        error: null,
      };

    case SEARCH_USER_SUCCESS: //검색 결과를 업데이트합니다.
      return {
        ...state,
        loading: false,
        userSearchResult: action.payload,
        error: null,
      };

    case SEARCH_TWIT_SUCCESS:
      return {
        ...state,
        loading: false,
        tweetSearchResult: action.payload,
        error: null,
      };

    case FOLLOW_USER_SUCCESS: //팔로우한 사용자 정보를 업데이트합니다.
      return {
        ...state,
        loading: false,
        findUser: action.payload,
        error: null,
      };

    case LOGIN_FAILURE:
    case REGISTER_FAILURE:
    case GET_PROFILE_FAILURE:
    case UPDATE_USER_FAILURE:
    case FIND_USER_BY_ID_FILURE:
    case FOLLOW_USER_FAILURE:
    case SEARCH_USER_FAILURE: //실패 액션(*_FAILURE)은 오류 정보를 업데이트하고 로딩 상태를 false로 설정합니다.
      return { ...state, loading: false, error: action.payload };
    case SEARCH_TWIT_FAILURE:
      return { ...state, loading: false, error: action.payload };
    case LOGOUT: //LOGOUT 액션은 초기 상태로 리셋합니다.
      return { ...initialState };
    default:
      return state;
  }
};

export default authReducer; //Redux 스토어에서 액션을 디스패치할 때 액션 유형에 따라 상태를 업데이트하고 앱의 전역 상태를 관리합니다.
