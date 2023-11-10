import { api } from "../../Config/apiConfig";
import {
    COM_CREATE_REQUEST,
    COM_CREATE_SUCCESS,
    COM_CREATE_FAILURE,
    GET_COMS_REQUEST,
    GET_COMS_SUCCESS,
    GET_COMS_FAILURE,
    UPDATE_COM_REQUEST,
    UPDATE_COM_SUCCESS,
    UPDATE_COM_FAILURE,
    ADD_USER_REQUEST,
    ADD_USER_SUCCESS,
    ADD_USER_FAILURE,
    GET_USER_REQUEST,
    GET_USER_SUCCESS,
    GET_USER_FAILURE,
    SIGNUP_REQUEST,
    SIGNUP_SUCCESS,
    SIGNUP_FAILURE,
    FIND_COM_BY_ID_REQUEST,
    FIND_COM_BY_ID_SUCCESS,
    FIND_COM_BY_ID_FAILURE,
    ADD_USER_SIGNUP_REQUEST,
    ADD_USER_SIGNUP_SUCCESS,
    ADD_USER_SIGNUP_FAILURE,
    REMOVE_FOLLOW_REQUEST,
    REMOVE_FOLLOW_SUCCESS,
    REMOVE_FOLLOW_FAILURE,
} from "./ActionType";

export const createComRequest = () => ({
    type: COM_CREATE_REQUEST,
});

export const createComSuccess = (data) => ({
    type: COM_CREATE_SUCCESS,
    payload: data,
});

export const createComFailure = (error) => ({
    type: COM_CREATE_FAILURE,
    payload: error,
});

export const getAllComsRequest = () => ({
    type: GET_COMS_REQUEST,
});

export const getAllComsSuccess = (coms) => ({
    type: GET_COMS_SUCCESS,
    payload: coms,
});

export const getAllComsFailure = (error) => ({
    type: GET_COMS_FAILURE,
    payload: error,
});

export const createCom = (reqData) => {
    return async (dispatch) => {
        dispatch(createComRequest());
        try {
            const { data } = await api.post("/api/communities/create", reqData);
            console.log("created list", data);
            dispatch(createComSuccess(data));
        } catch (error) {
            dispatch(createComFailure(error.message));
        }
    };
};

export const getAllComs = () => {
    return async (dispatch) => {
        dispatch(getAllComsRequest());
        try {
            const response = await api.get("/api/communities/");
            console.log("all lists", response.data);
            dispatch(getAllComsSuccess(response.data));
        } catch (error) {
            dispatch(getAllComsFailure(error.message));
        }
    };
};

export const updateCom = (reqData) => async (dispatch) => {
    dispatch({ type: UPDATE_COM_REQUEST });
    try {
        const response = await api.post(`/api/communities/update`, reqData);
        const com = response.data;
        dispatch({ type: UPDATE_COM_SUCCESS, payload: com });
    } catch (error) {
        dispatch({ type: UPDATE_COM_FAILURE, payload: error.message });
    }
};

export const addUserActionCom = (comId, userId) => async (dispatch) => {
    dispatch({ type: ADD_USER_REQUEST });
    //dispatch({type: ADD_USER_USERDTO_REQUEST})
    try {
        const response = await api.post(`/api/communities/${comId}/add2/${userId}`);
        const com = response.data;
        dispatch({ type: ADD_USER_SUCCESS, payload: com });
    } catch (error) {
        dispatch({ type: ADD_USER_FAILURE, payload: error.message });
    }
};

export const addUserActionSignup = (comId, userId) => async (dispatch) => {
    dispatch({ type: ADD_USER_SIGNUP_REQUEST });
    //dispatch({type: ADD_USER_USERDTO_REQUEST})
    try {
        const response = await api.post(`/api/communities/${comId}/signupok/${userId}`);
        const com = response.data;
        dispatch({ type: ADD_USER_SIGNUP_SUCCESS, payload: com });
    } catch (error) {
        dispatch({ type: ADD_USER_SIGNUP_FAILURE, payload: error.message });
    }
};

export const getUserActionCom = (comId) => async (dispatch) => {
    dispatch({ type: GET_USER_REQUEST });
    try {
        console.log("comId get", comId);
        const response = await api.get(`/api/communities/${comId}/get`);
        const com = response.data;
        console.log("getUserAction list data", com);
        dispatch({ type: GET_USER_SUCCESS, payload: com });
    } catch (error) {
        dispatch({ type: GET_USER_FAILURE, payload: error.message });
    }
};

export const addReady = (comId) => async (dispatch) => {
    dispatch({ type: SIGNUP_REQUEST });
    try {
        const response = await api.post(`/api/communities/${comId}/signup`, comId);
        const signup = response.data;
        dispatch({ type: SIGNUP_SUCCESS, payload: signup});
    } catch (error) {
        dispatch({ type: SIGNUP_FAILURE, payload: error.message });
    }
};

export const removeFollow = (comId) => async(dispatch) => {
    dispatch({type: REMOVE_FOLLOW_REQUEST});
    try{
        const response = await api.post(`/api/communities/${comId}/signout`, comId);
        const signout = response.data;
        dispatch({type: REMOVE_FOLLOW_SUCCESS, payload: signout});
    } catch (error) {
        dispatch({type: REMOVE_FOLLOW_FAILURE, payload: error.message});
    }
}

export const findComById = (comId) =>  async(dispatch) => {
        dispatch({ type: FIND_COM_BY_ID_REQUEST });
        try {
            const response = await api.get(`/api/communities/${comId}`);
            const com = response.data;
            dispatch({ type: FIND_COM_BY_ID_SUCCESS, payload: com });
            console.log("findComById check", com);
        } catch (error) {
            dispatch({ type: FIND_COM_BY_ID_FAILURE, payload: error.message });
            console.log("findComById error", error.message);
        }
    };
