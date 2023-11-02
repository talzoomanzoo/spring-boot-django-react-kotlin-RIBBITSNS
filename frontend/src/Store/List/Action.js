import { api } from "../../Config/apiConfig";
import {
    ADD_USER_FAILURE,
    ADD_USER_REQUEST,
    ADD_USER_SUCCESS,
    GET_LISTS_FAILURE,
    GET_LISTS_REQUEST,
    GET_LISTS_SUCCESS,
    GET_USER_FAILURE,
    GET_USER_REQUEST,
    GET_USER_SUCCESS,
    LIST_CREATE_FAILURE,
    LIST_CREATE_REQUEST,
    LIST_CREATE_SUCCESS,
    LIST_DELETE_FAILURE,
    LIST_DELETE_REQUEST,
    LIST_DELETE_SUCCESS,
    SET_PRIVATE_FAILURE,
    SET_PRIVATE_REQUEST,
    SET_PRIVATE_SUCCESS,
    UPDATE_LIST_FAILURE,
    UPDATE_LIST_REQUEST,
    UPDATE_LIST_SUCCESS,
    FIND_LIST_BY_ID_REQUEST,
    FIND_LIST_BY_ID_SUCCESS,
    FIND_LIST_BY_ID_FAILURE,
} from "./ActionType";

export const deleteListRequest = () => ({
  type: LIST_DELETE_REQUEST,
});

export const deleteListSuccess = (listId) => ({
  type: LIST_DELETE_SUCCESS,
  payload: listId,
});

export const deleteListFailure = (error) => ({
  type: LIST_DELETE_FAILURE,
  payload: error,
});

export const createListRequest = () => ({
  type: LIST_CREATE_REQUEST,
});

export const createListSuccess = (data) => ({
  type: LIST_CREATE_SUCCESS,
  payload: data,
});

export const createListFailure = (error) => ({
  type: LIST_CREATE_FAILURE,
  payload: error,
});

export const getAllListsRequest = () => ({
  type: GET_LISTS_REQUEST,
});

export const getAllListsSuccess = (lists) => ({
  type: GET_LISTS_SUCCESS,
  payload: lists,
});

export const getAllListsFailure = (error) => ({
  type: GET_LISTS_FAILURE,
  payload: error,
});

export const createListModel = (reqData) => {
  return async (dispatch) => {
    dispatch(createListRequest());
    try {
      const { data } = await api.post("/api/lists/create", reqData);
      console.log("created list", data);
      dispatch(createListSuccess(data));
    } catch (error) {
      dispatch(createListFailure(error.message));
    }
  };
};

export const updateListModel = (reqData) => async (dispatch) => {
  dispatch({ type: UPDATE_LIST_REQUEST });
  try {
    const response = await api.post(`/api/lists/update`, reqData);
    const list = response.data;
    dispatch({ type: UPDATE_LIST_SUCCESS, payload: list });
  } catch (error) {
    dispatch({ type: UPDATE_LIST_FAILURE, payload: error.message });
  }
};

export const addUserAction = (listId, userId) => async (dispatch) => {
  dispatch({ type: ADD_USER_REQUEST });
  //dispatch({type: ADD_USER_USERDTO_REQUEST})
  try {
    const response1 = await api.post(`/api/lists/${listId}/add1/${userId}`);
    //const response2 = await api.post(`/api/users/${listId}/add2/${userId}`);
    const list = response1.data;
    //const users= response2.data;

    console.log("added list -:", list);
    //console.log("added list users -:", users);

    dispatch({ type: ADD_USER_SUCCESS, payload: list });
    //dispatch({type:ADD_USER_USERDTO_SUCCESS, payload:users})
  } catch (error) {
    dispatch({ type: ADD_USER_FAILURE, payload: error.message });
    //dispatch({type:ADD_USER_USERDTO_FAILURE, payload:error.message});
  }
};

export const getUserAction = (listId) => async (dispatch) => {
  dispatch({ type: GET_USER_REQUEST });
  try {
    console.log("listId get", listId);
    const response = await api.get(`/api/lists/${listId}/get`);
    const list = response.data;
    console.log("getUserAction list data", list);
    dispatch({ type: GET_USER_SUCCESS, payload: list });
  } catch (error) {
    dispatch({ type: GET_USER_FAILURE, payload: error.message });
  }
};

export const setPrivate= (listId) => async(dispatch) => {
    dispatch({type: SET_PRIVATE_REQUEST})
    try {
        const response = await api.post(`/api/lists/${listId}/setPrivate`);
        const list = response.data;
        dispatch({type: SET_PRIVATE_SUCCESS, payload: list});
    } catch (error) {
        dispatch({type: SET_PRIVATE_FAILURE, payload: error.message});
    }
};

export const getAllLists = () => {
  return async (dispatch) => {
    dispatch(getAllListsRequest());
    try {
      const response = await api.get("/api/lists/");
      console.log("all lists", response.data);
      dispatch(getAllListsSuccess(response.data));
    } catch (error) {
      dispatch(getAllListsFailure(error.message));
    }
  };
};

export const findListById = (listId) => {
  return async(dispatch) => {
    dispatch({type: FIND_LIST_BY_ID_REQUEST});
    try {
      const response = await api.get(`/api/lists/${listId}`);
      dispatch({type: FIND_LIST_BY_ID_SUCCESS, payload: response.data});
    } catch (error) {
      dispatch({type: FIND_LIST_BY_ID_FAILURE, payload: error.message});
    }
  }
};

export const deleteList = (listId) => {
  return async (dispatch) => {
    dispatch(deleteListRequest());
    try {
      await api.delete(`/api/lists/${listId}`);
      dispatch(deleteListSuccess(listId));
      console.log("delete list", listId);
    } catch (error) {
      dispatch(deleteListFailure(error.message));
    }
  };
};
