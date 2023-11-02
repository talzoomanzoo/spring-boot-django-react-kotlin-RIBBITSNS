import {
    COM_CREATE_REQUEST,
    COM_CREATE_SUCCESS,
    COM_CREATE_FAILURE,
    GET_COMS_REQUEST,
    GET_COMS_SUCCESS,
    GET_COMS_FAILURE,
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
        case COM_CREATE_FAILURE:
        case GET_COMS_FAILURE:
        default:
            return state;
    }
};

export default comReducer;