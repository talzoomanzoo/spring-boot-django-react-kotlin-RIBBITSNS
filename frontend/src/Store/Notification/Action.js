export const incrementNotificationCount = (userId) => ({
  type: 'INCREMENT_NOTIFICATION_COUNT',
  payload: userId, // 사용자 ID를 페이로드로 포함합니다
});
