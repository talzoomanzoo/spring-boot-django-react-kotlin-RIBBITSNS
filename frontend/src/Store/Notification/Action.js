import {
    NOTIFICATION_PLUS_FAILURE,
    NOTIFICATION_PLUS_REQUEST,
    NOTIFICATION_PLUS_SUCCESS,
} from "./ActionType";

export const incrementNotificationCount = (userId) => {
//   type: 'INCREMENT_NOTIFICATION_COUNT',
//   payload: userId, // 사용자 ID를 페이로드로 포함합니다
  return async (dispatch) => {
        dispatch({type:NOTIFICATION_PLUS_REQUEST});
        try {
        //   const {data} = await api.post(`/api/notification/${userId}/Ncount`, {});
          dispatch({type:NOTIFICATION_PLUS_SUCCESS,payload:userId});
        } catch (error) {
          dispatch({type:NOTIFICATION_PLUS_FAILURE,payload:error.message});
        }
      };
};

// export const incrementNotificationCount = (userId) => ({
//       type: 'INCREMENT_NOTIFICATION_COUNT',
//       payload: userId, // 사용자 ID를 페이로드로 포함합니다
//     });
