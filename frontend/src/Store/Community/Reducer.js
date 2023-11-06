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
} from "./ActionType";

const initialState = {
    loading: false,
    data: null,
    error: null,
    coms: [],
    com: null,
};

const comReducer = (state = initialState, action) => {
    switch (
    action.type
    ) {
        case COM_CREATE_REQUEST:
        case GET_COMS_REQUEST:
        case GET_USER_REQUEST:
        case UPDATE_COM_REQUEST:
            return {
                ...state,
                loading: true,
                error: null,
            };
        case ADD_USER_REQUEST:
            return {
                ...state,
                loading: true,
                error: null
            };
        case COM_CREATE_SUCCESS:
            return {
                ...state,
                loading: false,
                com: action.payload,
                error: null,
            };
        case GET_COMS_SUCCESS:
            return {
                ...state,
                loading: false,
                coms: action.payload,
                error: null,
            };
        case UPDATE_COM_SUCCESS:
            return {
                ...state,
                loading: false,
                data: action.payload,
                error: null,
            };
        case ADD_USER_SUCCESS:
            return {
                ...state,
                loading: false,
                com: action.payload,
                error: null,
            };
        case GET_USER_SUCCESS:
            return {
                ...state,
                loading: false,
                com: action.payload,
                error: null,
            }
        case COM_CREATE_FAILURE:
        case GET_COMS_FAILURE:
        case GET_USER_FAILURE:
        case UPDATE_COM_FAILURE:
        case ADD_USER_FAILURE:
        default:
            return state;
    }
};

export default comReducer;