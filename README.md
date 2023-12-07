# spring-boot-django-react-kotlin-RIBBITSNS
SpringBoot + Django + React + Kotlin 을 활용한 웹 및 안드로이드앱 SNS 서비스: RIBBIT

## 💻 프로젝트 소개
* 비트교육센터 AI기반 웹서비스 응용SW 개발자 양성과정 3회차: 가상캐릭터 생성 및 윤리성 판별 AI를 활용한 웹 및 앱 SNS 서비스
* 23년도 KDT 벤처스타트업아카데미 통합프로젝트발표회·토크콘서트: 한국소프트웨어산업협회장상 우수상 수상작 (고용노동부, KOSA 한국소프트웨어산업협회 주최)
- 도메인: https://www.ribbit1234.com/
- 웹 및 앱 시연 영상: https://drive.google.com/file/d/1E1qgfPoOpD3sXq8WlT3Jp4KZQGauSyw8/view?usp=sharing
<br>

## ⏰ 개발 기간
* 23.09.11일 - 23.12.11일

### 👤 멤버 구성 및 주요 역할
 - (웹 개발)
   - Julian-Hwang: Bert AI 모델 학습 및 적용, Karlo 이미지 생성 API 적용, 웹소켓 채팅 기능 적용, 도메인 배포, 포트폴리오 작성, 도커 이미지 생성
   - kDyeong: KakaoMaps API 적용, 프론트엔드 기능 최적화, 백엔드 서버 엔티티 및 리포지토리 수정, 도커 이미지 생성
   - Rohjeonghyun: 프론트엔드 UI 및 로고, 사진 디자인
   - talzoomanzoo: 프론트엔드 렌더링 최적화 및 서버 간 통신 관리, 백엔드 서버 전반, PPT제작
 - (앱 개발)
   - hippoddung: 안드로이드 웹 뷰 전반, 스프링부트 엔티티 수정, 도커 이미지 생성

### ⚙️ 개발 환경, 도구, 사용 기술
- **개발 환경**: Windows10, Ubuntu, Android, AWS(EC2, RDS, Route53)
- **개발 언어** : Java, Python, Html5, CSS, JavaScript, Kotlin
- **개발 도구** : SpringToolSuite4, Android Studio, VS Code, Maven, Swagger, Git
- **기술 스택** : React, Axios, Redux, Toastify, React-Router, Tailwind, MUI, Node, Jetpack Compose, Retrofit2, Django, Pytorch, Bert, SpringBoot, Spring Data JPA, Spring Security, Stomp, WebSocket, MySQL, Nginx, Gunicorn, Docker

## 📌 상세 기능
 - 윤리 수치 AI 기능
 - 프로필 이미지 생성 AI 기능
 - 게시글 (리빗) 기능
 - 리스트 기능
 - 커뮤니티 기능
 - 알림 기능
 - 채팅 기능
 - 로그인 기능
  
## 🛠 설계 주안점
 - Springboot, React, Kotlin을 활용한 웹 및 앱 상의 Restful한 통신 구현
 - Django 프레임워크를 이용하여 Karlo 이미지 생성 ai, BERT 윤리수치 ai 서비스 분리하여 구축
 - Https 활용 및 JWT 사용을 통한 보안 강화
 - AWS, Nginx, Gunicorn, Docker를 사용한 도메인 배포
   
## 📓 참고 코드 및 문헌
 - 카카오브레인의 텍스트 기반 이미지 생성 기술 소개, 카카오브레인 (2022). https://www.youtube.com/watch?v=tar1ZzeTRTY
 - 텍스트 윤리검증 데이터, AI Hub (2021). https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=realm&dataSetSn=558.
 - Building A Twitter Full Stack Clone With Spring Boot, React, MySQL, MUI Tailwind  And Formik, Code With Zosh (2023). https://www.youtube.com/watch?v=8vnWmtUoyLE&list=PL7Oro2kvkIzIyKI6dOsc19mUwa58WTXaV (참조 코드 https://cosmofeed.com/vp/654389e13a47e3001e5f2e19)
 - 더북 딥러닝 파이토치 교과서 (2022). https://thebook.io/080289/
