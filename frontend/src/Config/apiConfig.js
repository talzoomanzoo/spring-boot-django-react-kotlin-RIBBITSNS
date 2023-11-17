import axios from "axios"; //axios 라이브러리를 가져온다.
const jwtToken = localStorage.getItem("jwt") // 웹 브라우저의 로컬스토리지에 "jwt"키를 사용하여 JWT 토큰을 가져와서 jwt token 변수에 저장한다.
export const API_BASE_URL = 'http://3.36.249.200:8080'; // API 서버의 기본 URL을 'http://localhost:8080'으로 설정하고, 다른 파일에서 사용할 수 있도록 API_BASE_URL을 내보낸다.
export const api = axios.create({ // axios의 create 메서드를 사용하여 새로운 axios 인스턴스인 api를 생성한다.
  baseURL: API_BASE_URL, //api의 baseURL 옵션을 API_BASE_URL 상수로 설정하여 모든 API 요청이 해당 주소를 기본으로 사용하게 한다.
  headers: {
    'Authorization':`Bearer ${jwtToken}`, // HTTP 요청 헤더 중 'Authorization' 헤더에 JWT 토큰을 "Bearer" 스키마와 함께 추가하여 서버에 보낸다.
    'Content-Type': 'application/json', // HTTP 요청 헤더 중 'Content-Type' 헤더를 설정하여 데이터가 JSON 형식임을 나타낸다.
  },
});

// 이렇게 코드는 axios를 사용하여 API 서버로 요청을 보낼 때 필요한 설정을 구성한다.

