const initialState = {
  notifications: {}, // 빈 객체로 초기화
};

const notificationReducer = (state = initialState, action) => {
  switch (action.type) {
    case 'INCREMENT_NOTIFICATION_COUNT':
      const userId = action.payload;
      console.log("qwer", userId);
      return {
        ...state,
        notifications: {
          ...state.notifications,
          [userId]: (state.notifications[userId] || 0) + 1, // 기존 알림 수를 가져와서 1을 더합니다
        },
      };
    default:
      return state;
  }
};

export default notificationReducer;
