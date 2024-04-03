# For "코딩 안해봤어요"
- 자바 17+ 설치
- vscode 설치
- spring boot extension pack 설치
- extension for java 설치
- App.java 찾아서 Run 누르면 서버 시작, localhost:2385 or 외부아이피:2385 로 접속 되는지 확인

StringValues.java 에 있는 ~PATH = "경로/파일명" 적힌거 자기가 원하는 곳에 파일 생성하고 경로 수정
이후 아래 적힌 그대로 GPT한테 질문

- "스프링부트 웹소켓을 이용한 실시간 1:1 체스대결이 가능한 사이트의 소스코드를 받았어. 대략 어떻게 작동하는 걸지 예상 되니?"
- resources 폴더에 있는 html 하나씩 복붙해서 보여주면서 "이 페이지는 무슨 일을 하는지 알려줘"
- RoomHandler, WebSocketConfig, Chessboard, ChessService, WebController, PostController, AdminController, jsonService, ChesscomApi 9개 코드 복붙해서 "이 java파일들은 무슨 일을 하는지 알려줘"

# 간략한 설명
- WebMvcConfig, WebSocketConfig : 설정파일, 주소창에 들어오는 url에서 기본적으로 권한 설정이나 보안설정 걸어줄수있음.
- RoomHandler : 실시간 통신(웹소켓) 요청 받고 보내는 녀석
- Chessboard, ChessService : 서버에 저장된 체스보드 현황과 선수가 둔 수가 유효한 수인지 판별하거나 겜이 끝난건지 판별하는 알고리즘이 들어있음.
- ~Controller : Config에서 한번 걸러지고온 데이터를 요청 종류별로 나눠서 받는 녀석. Web은 페이지, Post는 수나 회원가입/대기열신청 등 요청, Admin은 관리자만 할수있는 요청을 받음
- jsonService : .json 형태로 저장해놓은 데이터를 불러오고(read), 추가하고(create), 수정하고(update), 삭제하는 (delete) 기능을 모아놓은 녀석 (crud 검색해보면 좋음)
- ChesscomApi : 체닷 비공식 api에 요청보내서 레이팅이나 프로필정보 불러오는 기능 모아놓은 녀석


위에 적힌 파일들이 제일 많이 수정하게될 녀석들, 다른것들도 모르면 gpt한테 물어보면 잘 알려주고 어지간해서 틀린코드 안주는 분야니까 믿고 따라하셈
