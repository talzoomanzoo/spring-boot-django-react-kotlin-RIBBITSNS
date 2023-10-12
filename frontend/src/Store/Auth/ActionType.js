// authActionTypes.js
//Redux 액션 유형 (Action Types)을 정의하는 파일입니다. Redux에서 액션 유형은 액션을 식별하기 위한 문자열 상수로 사용된다. 
//각 액션 유형은 특정 작업 또는 이벤트를 나타내며, Redux 액션과 리듀서 간의 통신에 사용된다.
export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'LOGIN_FAILURE';
//사용자 로그인과 관련된 액션 유형입니다. LOGIN_REQUEST는 로그인 요청을 시작할 때 사용되고, LOGIN_SUCCESS는 로그인 성공 시 사용되며, LOGIN_FAILURE는 로그인 실패 시 사용됩니다.

export const GOOGLE_LOGIN_REQUEST = 'LOGIN_REQUEST';
export const GOOGLE_LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const GOOGLE_LOGIN_FAILURE = 'LOGIN_FAILURE';
//Google 로그인과 관련된 액션 유형입니다. GOOGLE_LOGIN_REQUEST는 Google 로그인 요청을 시작할 때 사용되고, GOOGLE_LOGIN_SUCCESS는 Google 로그인 성공 시 사용되며, GOOGLE_LOGIN_FAILURE는 Google 로그인 실패 시 사용됩니다.

export const REGISTER_REQUEST = 'REGISTER_REQUEST';
export const REGISTER_SUCCESS = 'REGISTER_SUCCESS';
export const REGISTER_FAILURE = 'REGISTER_FAILURE';
//사용자 등록과 관련된 액션 유형입니다. REGISTER_REQUEST는 등록 요청을 시작할 때 사용되고, REGISTER_SUCCESS는 등록 성공 시 사용되며, REGISTER_FAILURE는 등록 실패 시 사용됩니다.

export const GET_PROFILE_REUEST='GET_PROFILE_REUEST'
export const GET_PROFILE_SUCCESS='GET_PROFILE_SUCCESS'
export const GET_PROFILE_FAILURE='GET_PROFILE_FAILURE'
//사용자 프로필 조회와 관련된 액션 유형입니다. GET_PROFILE_REUEST는 프로필 조회 요청을 시작할 때 사용되고, GET_PROFILE_SUCCESS는 프로필 조회 성공 시 사용되며, GET_PROFILE_FAILURE는 프로필 조회 실패 시 사용됩니다.

export const UPDATE_USER_REQUEST='UPDATE_USER_REQUEST';
export const UPDATE_USER_SUCCESS='UPDATE_USER_SUCCESS';
export const UPDATE_USER_FAILURE='UPDATE_USER_FAILURE';
//사용자 프로필 업데이트와 관련된 액션 유형입니다. UPDATE_USER_REQUEST는 프로필 업데이트 요청을 시작할 때 사용되고, UPDATE_USER_SUCCESS는 업데이트 성공 시 사용되며, UPDATE_USER_FAILURE는 업데이트 실패 시 사용됩니다.

export const FIND_USER_BY_ID_REQUEST='FIND_USER_BY_ID_REQUEST';
export const FIND_USER_BY_ID_SUCCESS='FIND_USER_BY_ID_SUCCESS';
export const FIND_USER_BY_ID_FILURE='FIND_USER_BY_ID_FILURE';
//사용자 ID로 검색과 관련된 액션 유형입니다. FIND_USER_BY_ID_REQUEST는 검색 요청을 시작할 때 사용되고, FIND_USER_BY_ID_SUCCESS는 검색 성공 시 사용되며, FIND_USER_BY_ID_FILURE는 검색 실패 시 사용됩니다.

export const FOLLOW_USER_REQUEST="FOLLOW_USER_REQUEST";
export const FOLLOW_USER_SUCCESS='FOLLOW_USER_SUCCESS';
export const FOLLOW_USER_FAILURE='FOLLOW_USER_FAILURE';
//사용자 팔로우와 관련된 액션 유형입니다. FOLLOW_USER_REQUEST는 팔로우 요청을 시작할 때 사용되고, FOLLOW_USER_SUCCESS는 팔로우 성공 시 사용되며, FOLLOW_USER_FAILURE는 팔로우 실패 시 사용됩니다.

export const SEARCH_USER_REQUEST="SEARCH_USER_REQUEST";
export const SEARCH_USER_SUCCESS='SEARCH_USER_SUCCESS';
export const SEARCH_USER_FAILURE='SEARCH_USER_FAILURE';
//사용자 검색과 관련된 액션 유형입니다. SEARCH_USER_REQUEST는 검색 요청을 시작할 때 사용되고, SEARCH_USER_SUCCESS는 검색 성공 시 사용되며, SEARCH_USER_FAILURE는 검색 실패 시 사용됩니다.

export const SEARCH_TWIT_REQUEST="SEARCH_TWIT_REQUEST";
export const SEARCH_TWIT_SUCCESS="SEARCH_TWIT_SUCCESS";
export const SEARCH_TWIT_FAILURE="SEARCH_TWIT_FAILURE";


export const FOLLOW_TWIT_REQUEST="FOLLOW_TWIT_REQUEST";
export const FOLLOW_TWIT_SUCCESS="FOLLOW_TWIT_SUCCESS";
export const FOLLOW_TWIT_FAILURE="FOLLOW_TWIT_FAILURE";

export const LOGOUT="LOGOUT";
//사용자 로그아웃과 관련된 액션 유형입니다. 사용자 로그아웃 요청 시 사용됩니다.

//액션 유형 상수는 Redux 액션과 리듀서에서 사용되며, 특정 액션을 식별하여 앱의 상태를 업데이트하고, Redux 액션을 처리합니다.