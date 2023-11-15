import { GET_USERS_REPLIES_REQUEST } from "../Tweet/ActionType";
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

const initialState = {
  loading: false,
  data: null,
  error: null,
  notifications: [],
  notification: null,
};

const notificationReducer = (state = initialState, action) => {
  switch (action.type) {
    case NOTIFICATION_PLUS_REQUEST:
      return {
        ...state,
        loading: true,
        error: null,
      };
    case NOTIFICATION_PLUS_SUCCESS:
      return {
        ...state,
        loading: false,
        notifiation: action.payload,
        error: null,
      };
    case NOTIFICATION_PLUS_FAILURE:
    case NOTIFICATION_MINUS_REQUEST:
    case NOTIFICATION_MINUS_SUCCESS:
      const notificationIdToDelete = action.payload;
      return {
        ...state,
        loading: false,
        notifications: state.notifications.filter((notification) => notification.id !== notificationIdToDelete),
        error: null,
      };
    case NOTIFICATION_MINUS_FAILURE:
    case GET_NOTE_REQUEST:
    case GET_NOTE_SUCCESS:
      return {
          ...state,
          loading: false,
          notifications: action.payload,
          error: null,
      };
    case GET_NOTE_FAILURE:
    default:
      return state;
  };
};

export default notificationReducer;
