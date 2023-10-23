import axios from "axios";
import { API_BASE_URL, api } from "../../Config/apiConfig";
import { 
    LIST_CREATE_FAILURE, 
    LIST_CREATE_SUCCESS, 
    LIST_CREATE_REQUEST,
    GET_LISTS_REQUEST,
    GET_LISTS_SUCCESS,
    GET_LISTS_FAILURE,
    UPDATE_LIST_REQUEST,
    UPDATE_LIST_SUCCESS,
    UPDATE_LIST_FAILURE,
    ADD_USER_REQUEST,
    ADD_USER_SUCCESS,
    ADD_USER_FAILURE,
    GET_USER_REQUEST,
    GET_USER_SUCCESS,
    GET_USER_FAILURE,
} from "./ActionType";

export const createListRequest= () => ({
    type:LIST_CREATE_REQUEST,
});

export const createListSuccess=(data) => ({
    type: LIST_CREATE_SUCCESS,
    payload: data,
});

export const createListFailure=(error) => ({
    type: LIST_CREATE_FAILURE,
    payload:error,
})

export const getAllListsRequest = () => ({
    type:GET_LISTS_REQUEST,
})

export const getAllListsSuccess = (lists) => ({
    type: GET_LISTS_SUCCESS,
    payload: lists,
});

export const getAllListsFailure = (error) => ({
    type: GET_LISTS_FAILURE,
    payload: error,
})

export const createListModel = (reqData) => {
    return async(dispatch) => {
    dispatch(createListRequest());
    try {
        const {data} = await api.post("/api/lists/create", reqData);
        console.log("created list", data)
        dispatch(createListSuccess(data));
    } catch (error) {
        dispatch(createListFailure(error.message));
    }
};
};

export const updateListModel = (reqData) => async (dispatch) => {
    dispatch({type: UPDATE_LIST_REQUEST})
    try {
        const response = await api.post(`/api/lists/update`, reqData);
        const list = response.data
        dispatch ({type: UPDATE_LIST_SUCCESS, payload: list})
    } catch (error) {
        dispatch({type:UPDATE_LIST_FAILURE, payload: error.message});
    }
};

export const addUserAction = (listId, userId) => async (dispatch) => {
    dispatch({type: ADD_USER_REQUEST})
    //dispatch({type: ADD_USER_USERDTO_REQUEST})
    try {
        const response1 = await api.post(`/api/lists/${listId}/add1/${userId}`);
        //const response2 = await api.post(`/api/users/${listId}/add2/${userId}`);
        const list = response1.data;
        //const users= response2.data;

        console.log("added list -:", list);
        //console.log("added list users -:", users);

        dispatch({type:ADD_USER_SUCCESS, payload:list});
        //dispatch({type:ADD_USER_USERDTO_SUCCESS, payload:users})
    } catch (error) {
        dispatch({type:ADD_USER_FAILURE, payload:error.message});
        //dispatch({type:ADD_USER_USERDTO_FAILURE, payload:error.message});
    }
};

export const getUserAction = (listId) => async (dispatch) => {
    dispatch({type: GET_USER_REQUEST})
    try {
        console.log("listId get", listId);
        const response = await api.get(`/api/lists/${listId}/get`);
        const list = response.data
        console.log("getUserAction list data", list);
        dispatch({type: GET_USER_SUCCESS, payload: list});
    } catch (error) {
        dispatch({type: GET_USER_FAILURE, payload: error.message});
    }
}


export const getAllLists = () => {
    return async (dispatch) => {
        dispatch(getAllListsRequest());
        try {
            const response = await api.get("/api/lists/");
            console.log("all lists", response.data)
            dispatch(getAllListsSuccess(response.data));
        } catch (error) {
            dispatch(getAllListsFailure(error.message));
        }
    };
};