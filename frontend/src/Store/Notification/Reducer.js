import {
    NOTIFICATION_PLUS_FAILURE,
    NOTIFICATION_PLUS_REQUEST,
    NOTIFICATION_PLUS_SUCCESS,
} from "./ActionType";

const initialState = {
  notifications: {}, // 빈 객체로 초기화
};

const notificationReducer = (state = initialState, action) => {
  switch (action.type) {
    case NOTIFICATION_PLUS_REQUEST:
      return {
        ...state,
        loading: true,
        error: null,
        twits:[]
      };
    case NOTIFICATION_PLUS_SUCCESS:
      const userId = action.payload;
      return {
        ...state,
        notifications: {
          ...state.notifications,
          [userId]: (state.notifications[userId] || 0) + 1, // 기존 알림 수를 가져와서 1을 더합니다
        },
      };
    case NOTIFICATION_PLUS_FAILURE:
      return {
        ...state,
        loading: false,
        error: action.payload,
      };
    default:
      return state;
  }
};

export default notificationReducer;
