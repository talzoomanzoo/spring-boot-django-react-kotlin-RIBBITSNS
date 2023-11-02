import { applyMiddleware, combineReducers, legacy_createStore } from "redux";
import thunk from "redux-thunk";
import authReducer from "./Auth/Reducer";
import listReducer from "./List/Reducer";
import notificationReducer from "./Notification/Reducer";
import { themeReducer } from "./Theme/Reducer";
import tweetReducer from "./Tweet/Reducer";

const rootReducers=combineReducers({
auth:authReducer,
twit:tweetReducer,
theme:themeReducer,
list:listReducer,
notification: notificationReducer,
})
export const store=legacy_createStore(rootReducers,applyMiddleware(thunk))