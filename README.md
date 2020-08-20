# 김혜민 LINE Games(주) 과제 제출

### 1. 프로젝트 소개

1. 게시판 서버 : bulletin-board-server
2. 게시판 웹 : bulletin-board-web

###  2. 프로젝트 구동방법

- 방법 1. STS에서 소스를 빌드 후에 Run - Spring Boot App 으로 구동.
- 방법 2. Maven 빌드 방법으로 Goals를 spring-boot:run로 설정 후 실행.

#### 3. 특징

- 특징 1. 회원가입 기능은 없습니다. 테스트용으로 사용자를 추가하거나 아래의 정보로 로그인 하면 됩니다. 
  - 등록되어 있는 로그인 정보 (아이디 / 비밀번호): 
    - whily312 / test
    -  test / 0000
  - 사용자 등록
    - me.line.games.InsertUserDBInit : 해당 클래스에 테스트 사용자 정보 추가하면 서버가 구동시에 Insert 하게 됩니다.

