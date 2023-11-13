import {
  NOTIFICATION_PLUS_FAILURE,
  NOTIFICATION_PLUS_REQUEST,
  NOTIFICATION_PLUS_SUCCESS,
  NOTIFICATION_MINUS_REQUEST, 
  NOTIFICATION_MINUS_SUCCESS,
  NOTIFICATION_MINUS_FAILURE,
  GET_NOTE_REQUEST,
  GET_NOTE_SUCCESS,
  GET_NOTE_FAILURE,
} from "./ActionType";
import { api } from "../../Config/apiConfig";

export const getAllNotificationsRequest = () => ({
  type: GET_NOTE_REQUEST,
});

export const getAllNotificationsSuccess = (notifications) => ({
  type: GET_NOTE_SUCCESS,
  payload: notifications,
})

export const getAllNotificationsFailure = (error) => ({
  type: GET_NOTE_FAILURE,
  payload: error,
});

export const incrementNotificationCount = (twitId) => {
  //   type: 'INCREMENT_NOTIFICATION_COUNT',
  //   payload: userId, // 사용자 ID를 페이로드로 포함합니다
  return async (dispatch) => {
    dispatch({ type: NOTIFICATION_PLUS_REQUEST });
    try {
      //const { data } = await api.post(`/api/notification/${userId}/Ncount`, {});
      const { data } = await api.post(`/api/notification/${twitId}`);
      dispatch({ type: NOTIFICATION_PLUS_SUCCESS, payload: data });
    } catch (error) {
      dispatch({ type: NOTIFICATION_PLUS_FAILURE, payload: error.message });
    }
  };
};

export const decreaseNotificationCount = (noteId) => {
  return async (dispatch) => {
    dispatch({ type: NOTIFICATION_MINUS_REQUEST});
    try {
      const { data } = await api.delete(`/api/notification/${noteId}`);
      dispatch({ type: NOTIFICATION_MINUS_SUCCESS, payload: data});
    } catch (error) {
      dispatch({ type: NOTIFICATION_MINUS_FAILURE, payload: error.message });
    }
  };
};

export const getAllNotifications = () => {
  return async (dispatch) => {
    dispatch(getAllNotificationsRequest());
    try {
      const response = await api.get(`/api/notification/all`);
      dispatch(getAllNotificationsSuccess(response.data));
    } catch (error) {
      dispatch(getAllNotificationsFailure(error.message));
    }
  };
};

// export const incrementNotificationCount = (userId) => ({
//       type: 'INCREMENT_NOTIFICATION_COUNT',
//       payload: userId, // 사용자 ID를 페이로드로 포함합니다
//     });
