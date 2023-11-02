import { api } from "../../Config/apiConfig";
import {
    COM_CREATE_REQUEST,
    COM_CREATE_SUCCESS,
    COM_CREATE_FAILURE,
    GET_COMS_REQUEST,
    GET_COMS_SUCCESS,
    GET_COMS_FAILURE,
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