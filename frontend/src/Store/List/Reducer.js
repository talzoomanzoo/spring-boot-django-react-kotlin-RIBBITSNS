import {
    LIST_CREATE_FAILURE,
    LIST_CREATE_SUCCESS,
    LIST_CREATE_REQUEST,
    GET_LISTS_FAILURE,
    GET_LISTS_SUCCESS,
    GET_LISTS_REQUEST,
    UPDATE_LIST_REQUEST,
    UPDATE_LIST_SUCCESS,
    UPDATE_LIST_FAILURE,
    ADD_USER_REQUEST,
    ADD_USER_SUCCESS,
    ADD_USER_FAILURE,
    GET_USER_REQUEST,
    GET_USER_SUCCESS,
    GET_USER_FAILURE,
    LIST_DELETE_REQUEST,
    LIST_DELETE_SUCCESS,
    LIST_DELETE_FAILURE,
    SET_PRIVATE_REQUEST,
    SET_PRIVATE_SUCCESS,
    SET_PRIVATE_FAILURE,
    FIND_TWEET_BY_LIST_ID_REQUEST,
    FIND_TWEET_BY_LIST_ID_SUCCESS,
    FIND_TWEET_BY_LIST_ID_FAILURE,
} from "./ActionType";

const initialState = {
    loading: false,
    data: null,
    error: null,
    lists: [],
    list: null,
};

const listReducer = (state = initialState, action) => {
    switch (
    action.type
    ) {
        case GET_LISTS_REQUEST:
        case LIST_CREATE_REQUEST:
        case SET_PRIVATE_REQUEST:
        case FIND_TWEET_BY_LIST_ID_REQUEST:
        case UPDATE_LIST_REQUEST:
            return {
                ...state,
                loading: true,
                error: null,
            };
        case GET_USER_REQUEST:
        case ADD_USER_REQUEST:
            return {
                ...state,
                loading: true,
                error: null
            };
        case LIST_DELETE_REQUEST:
        case GET_LISTS_FAILURE:
        case LIST_CREATE_FAILURE:
        case SET_PRIVATE_FAILURE:
        case UPDATE_LIST_FAILURE:
            return {
                ...state,
                loading: false,
                data: null,
                error: action.error,
            };
        case GET_USER_FAILURE:
        case ADD_USER_FAILURE:
        case LIST_DELETE_FAILURE:
        case FIND_TWEET_BY_LIST_ID_FAILURE:
        case FIND_TWEET_BY_LIST_ID_SUCCESS:
            return {
                ...state,
                loading: false,
                twits: action.payload,
                error: null,
            }
        case LIST_DELETE_SUCCESS:
            const ListIdToDelete = action.payload;
            return {
                ...state,
                loading: false,
                lists: state.lists.filter((list) => list.id !== ListIdToDelete),
                error: null,
            };
        case LIST_CREATE_SUCCESS:
            console.log("list action", action.payload);
            return {
                ...state,
                loading: false,
                list: action.payload,
                error: null,
            };
        case GET_LISTS_SUCCESS:
            return {
                ...state,
                loading: false,
                lists: action.payload,
                error: null,
            };
        case UPDATE_LIST_SUCCESS:
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
                list: action.payload,
                error: null,
            };
        case GET_USER_SUCCESS:
            return {
                ...state,
                loading: false,
                list: action.payload,
                error: null,
            }
        case SET_PRIVATE_SUCCESS:
            return {
                ...state,
                loading: false,
                error: null,
                private: action.payload,
            }
        default:
            return state;
    }
};

export default listReducer;