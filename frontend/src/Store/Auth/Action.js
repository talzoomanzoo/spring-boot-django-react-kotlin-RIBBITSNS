// authActions.js
import axios from "axios";
import { API_BASE_URL, api } from "../../Config/apiConfig"; // API의 기본 URL 및 axios 클라이언트 객체를 가져온다.
import {
  FIND_USER_BY_ID_FILURE,
  FIND_USER_BY_ID_REQUEST,
  FIND_USER_BY_ID_SUCCESS,
  FOLLOW_USER_FAILURE,
  FOLLOW_USER_REQUEST,
  FOLLOW_USER_SUCCESS,
  GET_PROFILE_REUEST,
  GET_PROFILE_SUCCESS,
  GOOGLE_LOGIN_FAILURE,
  GOOGLE_LOGIN_REQUEST,
  GOOGLE_LOGIN_SUCCESS,
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
} from "./ActionType"; //액션 유형 상수들을 가져옵니다. 이러한 상수들은 Redux 액션 유형을 식별하는 데 사용된다.

export const loginRequest = () => ({
  type: LOGIN_REQUEST,
});

export const loginSuccess = (userData) => ({
  type: LOGIN_SUCCESS,
  payload: userData,
});

export const loginFailure = (error) => ({
  type: LOGIN_FAILURE,
  payload: error,
});

export const registerRequest = () => ({
  type: REGISTER_REQUEST,
});

export const registerSuccess = (userData) => ({
  type: REGISTER_SUCCESS,
  payload: userData,
});

export const registerFailure = (error) => ({
  type: REGISTER_FAILURE,
  payload: error,
});

const getUserProfileRequest = () => ({
  type: GET_PROFILE_REUEST,
});
const getUserProfileSuccess = (user) => ({
  type: GET_PROFILE_SUCCESS,
  payload: user,
});
const getUserProfileFailure = (error) => ({
  type: GET_PROFILE_SUCCESS,
  payload: error,
});
////////////////// 여기까지 Redux 액션 생성자, 각 함수는 특정 액션 유형을 반환하며, 해당 액션의 상태나 데이터를 포함한다.

export const loginUser = (loginData) => async (dispatch) => {
  dispatch(loginRequest());
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/signin`, loginData);
    const user = response.data;
    console.log("login user -: ", user);
    if (user.jwt) {
      localStorage.setItem("jwt", user.jwt);
    }

    dispatch(loginSuccess(user));
  } catch (error) {
    dispatch(loginFailure(error.message || "An error occurred during login."));
  }
};
// 함수는 사용자 로그인을 처리하는 비동기 액션 생성자다. 사용자가 로그인할 때, API를 호출하여 로그인 정보를 제출하고, 
//성공 또는 실패에 따라 각각 loginSuccess 또는 loginFailure 액션을 디스패치한다.

export const loginWithGoogleAction = (data) => async (dispatch) => {
  dispatch({type:GOOGLE_LOGIN_REQUEST});
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/signin/google`, data);
    const user = response.data;
    console.log("login with google user -: ", user);
    if (user.jwt) {
      localStorage.setItem("jwt", user.jwt);
      window.location.replace("/")
    }
    dispatch({type:GOOGLE_LOGIN_SUCCESS,payload:user.jwt});
  } catch (error) {
    dispatch({type:GOOGLE_LOGIN_FAILURE, payload: error.message || "An error occurred during login."});
  }
};
//Google 로그인을 처리하는 비동기 액션 생성자다. Google 로그인 정보를 API로 전송하고, 
//성공 또는 실패에 따라 각각 GOOGLE_LOGIN_SUCCESS 또는 GOOGLE_LOGIN_FAILURE 액션을 디스패치한다.

export const registerUser = (userData) => async (dispatch) => {
  dispatch(registerRequest());
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/signup`, userData);
    const user = response.data;
    console.log("created user - : ", user);
    if (user.jwt) {
      localStorage.setItem("jwt", user.jwt);
    }
    dispatch(registerSuccess(user));
  } catch (error) {
    dispatch(
      registerFailure(error.message || "An error occurred during registration.")
    );
  }
};
//사용자 등록을 처리하는 비동기 액션 생성자다. 사용자 등록 정보를 API로 전송하고, 
//성공 또는 실패에 따라 각각 registerSuccess 또는 registerFailure 액션을 디스패치한다.

export const getUserProfile = (jwt) => async (dispatch) => {
  dispatch(getUserProfileRequest());
  try {
    const response = await axios.get(`${API_BASE_URL}/api/users/profile`,{
      headers:{
        "Authorization":`Bearer ${jwt}`,

      }
    });
    const user = response.data;
    console.log("login user -: ", user);
   
    dispatch(getUserProfileSuccess(user));
  } catch (error) {
    dispatch(
      getUserProfileFailure(error.message || "An error occurred during login.")
    );
  }
};
//사용자 프로필을 가져오는 비동기 액션 생성자다. 사용자 등록 정보를 API로 전송하고, 
//성공 또는 실패에 따라 각각 getUserProfileSuccess 또는 getUserProfileFailure 액션을 디스패치한다.

export const findUserById = (userId) => async (dispatch) => {
  dispatch({type:FIND_USER_BY_ID_REQUEST})
  try {
    const response = await api.get(`/api/users/${userId}`);
    const user = response.data;
    console.log("find by id user -: ", user);
   
    dispatch({type:FIND_USER_BY_ID_SUCCESS,payload:user});
  } catch (error) {
    dispatch(
      {type:FIND_USER_BY_ID_FILURE,error:error.message}
    );
  }
};
//사용자를 ID로 찾는 비동기 액션 생성자다. 사용자 등록 정보를 API로 전송하고, 
//성공 또는 실패에 따라 각각 FIND_USER_BY_ID_SUCCESS 또는 FIND_USER_BY_ID_FAILURE 액션을 디스패치한다.

export const searchUser = (query) => async (dispatch) => {
  dispatch({type:SEARCH_USER_REQUEST})
  try {
    const response = await api.get(`/api/users/search1?query=${query}`);
    const users = response.data;
    console.log("search result -: ", users);
   
    dispatch({type:SEARCH_USER_SUCCESS,payload:users});
  } catch (error) {
    dispatch(
      {type:SEARCH_USER_FAILURE,error:error.message}
    );
  }
};

export const searchAll = (query) => async (dispatch) => {
  dispatch({type:SEARCH_USER_REQUEST})
  dispatch({type:SEARCH_TWIT_REQUEST})
  try {
    const response1 = await api.get(`/api/users/search1?query=${query}`);
    const users = response1.data;
    const response2 = await api.get(`/api/twits/search2?query=${query}`);
    const twits = response2.data;
    console.log("search users -: ", users);
    console.log("search twits -:", twits);
    dispatch({type:SEARCH_USER_SUCCESS,payload:users});
    dispatch({type:SEARCH_TWIT_SUCCESS,payload:twits});
  } catch (error) {
    dispatch(
      {type:SEARCH_USER_FAILURE,error:error.message}
    );
    dispatch(
      {type:SEARCH_TWIT_FAILURE,error:error.message}
    );
  }
};
    
export const updateUserProfile = (reqData) => async (dispatch) => {
  console.log("update profile reqData",reqData)
  dispatch({type:UPDATE_USER_REQUEST})
  try {
    const response = await api.put(`/api/users/update`,reqData);
    const user = response.data;
    console.log("updated user -: ", user);
   
    dispatch({type:UPDATE_USER_SUCCESS,payload:user});
  } catch (error) {
    dispatch({type:UPDATE_USER_FAILURE,payload:error.message});
  }
};
//사용자 프로필을 업데이트하는 비동기 액션 생성자다. 사용자 등록 정보를 API로 전송하고, 
//성공 또는 실패에 따라 각각 UPDATE_USER_SUCCESS 또는 UPDATE_USER_FAILURE 액션을 디스패치한다.

export const FollowUserAction = (userId) => async (dispatch) => {
  // console.log("updatte profile reqData",reqData)
  dispatch({type:FOLLOW_USER_REQUEST})
  try {
    const response = await api.put(`/api/users/${userId}/follow`);
    const user = response.data;
    console.log("follow user -: ", user);
   
    dispatch({type:FOLLOW_USER_SUCCESS,payload:user});
  } catch (error) {
    console.log("catch error ",error)
    dispatch({type:FOLLOW_USER_FAILURE,payload:error.message});
  }
};
//사용자를 팔로우하는 비동기 액션 생성자다. 사용자 등록 정보를 API로 전송하고, 
//성공 또는 실패에 따라 각각 FOLLOW_USER_SUCCESS 또는 FOLLOW_USER_FAILURE 액션을 디스패치한다.

export const logout = () => (dispatch) => {
  localStorage.removeItem("jwt");
  dispatch({ type: LOGOUT, payload: null });
};
//사용자 로그아웃을 처리하며, 로컬 스토리지에서 JWT 토큰을 제거하고 LOGOUT 액션을 디스패치한다.

// Redux 액션을 정의하고, 이러한 액션을 사용하여 Redux 상태를 변경하거나 API와 상호작용하는 데 사용된다.

// const persistConfig = {
//   key: 'root',
//   storage,
// };

// // Redux Toolkit을 사용한 Slice 생성
// const authSlice = createSlice({
//   name: 'auth',
//   initialState,
//   reducers: {
//     loginSuccess: (state, action) => {
//       state.user = action.payload;
//     },
//     // 다른 액션 및 리듀서들...
//     logout: (state) => {
//       // 로그아웃 시에 상태 초기화
//       state.user = null;
//     },
//   },
// });

// // rootReducer에 Redux Persist 설정 적용
// const persistedReducer = persistReducer(persistConfig, authSlice.reducer);

// export { persistStore }; // persistor를 사용하기 위해 export

// export const { loginSuccess, logout } = authSlice.actions;

// export default persistedReducer; 
