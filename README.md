# 프로젝트명 : **TodakTodak Project**
 Spring Framework를 기반으로 한 web기반 그룹웨어

## 1.요건정리
 - 목적 : 주 52시간 탄력근무제를 기반으로한 단체를 대상으로 한 그룹웨어 제작 
 - 구조 :  Spring Framework
 - 담당 기능 : 게시판 글 및 댓글 CRUD(입력, 수정, 삭제, 전체조회, 검색, like검색) 및 출퇴근 등록
 ![슬라이드19](https://user-images.githubusercontent.com/62315622/84861319-b7563d80-b0ab-11ea-9bd2-f81b7a11dc53.PNG)
 - 담당 업무 : 통합, 전체 CSS 총괄
 - 제작 기간 : 총 9주 (2020.01.17 ~2020.03.27)
 - 제작 일정
      일정	|	작업내역	|
      ----- 	|	--------	|
      1주차	|	RFP 및 요건정의서 작성	|
      2주차	|	설계서 작성	|
      3주차	|	공통클래스 개발	|
      4주차	|	공지사항 게시판 개발	|
      5주차	|	공지사항 게시판 확인버튼	|
      6주차	|	건의사항 게시판 개발	|
      7주차	|	출퇴근 등록	|
      8주차	|	통합 및 단위테스트, 통합 테스트	|
      9주차	|	CSS 작업 및 프로젝트 발표 문서 작업	|


> 테이블 정의서

- 메인테이블 

HUMAN_MEMBER	사원인사정보 테이블					
						
컬럼ID		|	데이터 타입(크기)		|	컬럼명
-----  	|	--------	|	---
HM_EMPNUM		|	VARCHAR2(13 BYTE )		|	사원번호
HM_DEPTNUM		|	VARCHAR2(4 BYTE )		|	부서번호
HM_ID		|	VARCHAR2(50 BYTE )		|	아이디
HM_PW		|	VARCHAR2(200 BYTE )		|	비밀번호
HM_NAME		|	VARCHAR2(50 BYTE )		|	성명
HM_BIRTH		|	VARCHAR2(8 BYTE )		|	생년월일
HM_RESIDENTNUM		|	VARCHAR2(13 BYTE )		|	주민등록번호
HM_HP		|	VARCHAR2(11 BYTE )		|	휴대폰번호
HM_EXTENSIONNUM		|	VARCHAR2(4 BYTE )		|	내선번호
HM_EMAIL		|	VARCHAR2(50 BYTE )		|	이메일
HM_ADDR		|	VARCHAR2(500 BYTE )		|	도로명주소
HM_POSTCODE		|	VARCHAR2(5 BYTE )		|	우편번호
HM_HIREDATE		|	VARCHAR2(8 BYTE )		|	입사일
HM_RESIGNDATE		|	VARCHAR2(8 BYTE )		|	퇴사일
HM_PICTURE		|	VARCHAR2(500 BYTE )		|	사진
HM_BANK		|	VARCHAR2(2 BYTE )		|	은행
HM_DEPOSITORS		|	VARCHAR2(50 BYTE )		|	예금주명
HM_ACCOUNTNUM		|	VARCHAR2(14 BYTE )		|	계좌번호
HM_EDUCATION		|	VARCHAR2(2 BYTE )		|	최종학력
HM_EDUCONTENTS		|	VARCHAR2(500 BYTE )		|	학력내용
HM_WORKEXPERIENCE		|	VARCHAR2(2 BYTE )		|	경력
HM_WORKCONTENTS		|	VARCHAR2(500 BYTE )		|	경력내용
HM_POSITION		|	VARCHAR2(2 BYTE )		|	직급
HM_DUTY		|	VARCHAR2(2 BYTE )		|	직책
HM_EMPLOYMENTTYPE		|	VARCHAR2(2 BYTE )		|	고용형태
HM_UPDATEDATE		|	VARCHAR2(8 BYTE )		|	수정일
HM_INSERTDATE		|	VARCHAR2(8 BYTE )		|	생성일
HM_DELETEYN		|	VARCHAR2(1 BYTE )		|	퇴사여부
						
- 출퇴근 등록 테이블	

HUMAN_COMMUTE	출퇴근 등록					
						
컬럼ID		|	데이터 타입(크기)		|	컬럼명
----- 		|	--------		|	---
HC_COMNUM		|	VARCHAR2(13 BYTE )	 PK	|	출근번호
HM_EMPNUM		|	VARCHAR2(13 BYTE )	 FK	|	사원번호
HM_NAME		|	VARCHAR2(50 BYTE )		|	성명
HM_DEPTNUM		|	VARCHAR2(3 BYTE )		|	부서번호
HC_DATE		|	VARCHAR2(8 BYTE )		|	날짜
HC_WORKIN		|	VARCHAR2(4 BYTE )		|	출근시간
HC_WORKOUT		|	VARCHAR2(4 BYTE )		|	퇴근시간
HC_DAYHOUR		|	VARCHAR2(4 BYTE )		|	일근무시간
HC_WEEKHOUR		|	VARCHAR2(4 BYTE )		|	주근무시간
HC_LASTHOUR		|	VARCHAR2(4 BYTE )		|	남은근무시간
HC_TOTALHOUR		|	VARCHAR2(4 BYTE )		|	목표근무시간
HC_EXTRAWORKING		|	VARCHAR2(4 BYTE )		|	추가근무시간
HC_TANDA		|	VARCHAR2(2 BYTE )		|	근무형태
						
- 공지사항 관련 테이블

BOARD_NOTICE	공지사항	
						
컬럼ID		|	데이터 타입(크기)		|	컬럼명
----- 		|	--------		|	---
BN_NUM		|	VARCHAR2(11 BYTE )	PK	|	공지사항글번호
HM_EMPNUM		|	VARCHAR2(13 BYTE )	FK	|	사원번호
HM_NAME		|	VARCHAR2(50 BYTE )		|	성명
HM_DUTY		|	VARCHAR2(2 BYTE )		|	직책
BN_DEPTNUM		|	VARCHAR2(4 BYTE )		|	부서공개범위
BN_DIVNUM		|	VARCHAR2(4 BYTE )		|	본부공개범위
BN_TITLE		|	VARCHAR2(50 BYTE )		|	글제목
BN_CONTENT		|	VARCHAR2(2000 BYTE )		|	글내용
BN_IMAGE		|	VARCHAR2(500 BYTE )		|	첨부이미지
BN_FILE		|	VARCHAR2(1000 BYTE )		|	첨부파일
BN_DELETEYN		|	VARCHAR2(1 BYTE )		|	삭제여부
BN_INSERTDATE		|	VARCHAR2(8 BYTE )		|	생성일
BN_UPDATEDATE		|	VARCHAR2(8 BYTE )		|	수정일
BN_HITNUM		|	VARCHAR2(10 BYTE )		|	조회수
						
						
BOARD_NOCHECK	공지사항 확인등록 테이블
						
컬럼ID		|	데이터 타입(크기)		|	컬럼명
----- 		|	--------		|	---
BN_CHECKNUM		|	VARCHAR2(11 BYTE )	PK	|	공지사항확인번호
BN_NUM		|	VARCHAR2(9 BYTE )	FK	|	공지사항글번호
HM_EMPNUM		|	VARCHAR2(13 BYTE )	FK	|	사원번호
HM_NAME		|	VARCHAR2(50 BYTE )		|	성명
HM_DEPTNUM		|	VARCHAR2(4 BYTE )		|	부서번호
BN_CHECKDATE		|	VARCHAR2(8 BYTE )		|	확인날짜
BN_CHECKYN		|	VARCHAR2(1 BYTE )		|	확인여부
						
- 건의사항 게시판 관련 테이블	

BOARD_SUGGESTION	건의사항					
						
컬럼ID		|	데이터 타입(크기)		|	컬럼명
----- 		|	--------	|	---
BS_NUM		|	VARCHAR2(11 BYTE )	PK	|	건의사항글번호
HM_EMPNUM		|	VARCHAR2(13 BYTE )	FK	|	사원번호
BS_TITLE		|	VARCHAR2(50 BYTE )		|	글제목
BS_CONTENT		|	VARCHAR2(2000 BYTE )		|	글내용
BS_IMAGE		|	VARCHAR2(500 BYTE )		|	첨부이미지
BS_INSERTDATE		|	VARCHAR2(8 BYTE )		|	생성일
BS_UPDATEDATE		|	VARCHAR2(8 BYTE )		|	수정일
BS_DELETEYN		|	VARCHAR2(1 BYTE )		|	삭제여부
BN_HITNUM		|	VARCHAR2(10 BYTE )		|	조회수
						
						
BOARD_SUREPLY	건의사항 댓글					
						
컬럼ID		|	데이터 타입(크기)		|	컬럼명
----- 	|	--------	|	---
BSR_NUM		|	VARCHAR2(13 BYTE )	PK	|	건의사항댓글번호
BS_NUM		|	VARCHAR2(11 BYTE )	FK	|	건의사항글번호
HM_EMPNUM		|	VARCHAR2(13 BYTE )		|	사원번호
BSR_CONTENT		|	VARCHAR2(600 BYTE )		|	댓글내용
BSR_DELETEYN		|	VARCHAR2(1 BYTE )		|	삭제여부
BSR_INSERTDATE		|	VARCHAR2(8 BYTE )		|	생성일
BSR_UPDATEDATE		|	VARCHAR2(8 BYTE )		|	수정일
						
						
BOARD_SULIKE	건의사항좋아요					
						
컬럼ID		|	데이터 타입(크기)		|	컬럼명
----- 		|	--------		|	---
BSL_NUM		|	VARCHAR2(13 BYTE )	PK	|	건의사항좋아요번호
BS_NUM		|	VARCHAR2(11 BYTE )	FK	|	건의사항글번호
HM_EMPNUM		|	VARCHAR2(13 BYTE )		|	사원번호
BSL_LIKEYN		|	VARCHAR2(1 BYTE )		|	건의사항좋아요여부
BSL_DISLIKEYN		|	VARCHAR2(1 BYTE )		|	건의사항싫어요여부



시스템 아키텍쳐
![슬라이드20](https://user-images.githubusercontent.com/62315622/84861320-b7563d80-b0ab-11ea-8250-3652e8384f39.PNG)


## 2. 개발환경 : HTML5, CSS3, jQuery, Ajax, JavaScript(ECMAScript3), BootStrap4, jsp-api 2.0, Servlet api 3.2, JDK 1.7, Spring framework 3,Mybatis 3, Mybatis-spring 1.2, Oracle11g R 2 (AWS EC R2), 파일업로드 api – cos.jar, Log4j 1.2, xml, plain-text, eclipse luna, Apache Tomcat 8.0, SQL Developer, STS, Erdwin, Git Bash, GitHub, StarUML, AWS, Smart-editor v2

![슬라이드18](https://user-images.githubusercontent.com/62315622/84861317-b6bda700-b0ab-11ea-93ea-0b6934662a2d.PNG)

## 3. 실행화면


