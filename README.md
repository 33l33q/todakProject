# 프로젝트명 : **TodakTodak Project**
 
 ### Spring Framework를 기반으로 한 web기반 그룹웨어
 요약 포트폴리오 : https://drive.google.com/file/d/1quuvr9HUn2JweVipuPSgN5qaQ-bJ9CE5/view?usp=sharing

## 1.요건정리
 - 목적 : 주 52시간 탄력근무제를 기반으로한 단체를 대상으로 한 그룹웨어 제작 
 - 구조 :  Spring Framework
 - 담당 기능 : 게시판 글 및 댓글 CRUD(입력, 수정, 삭제, 전체조회, 검색, like검색) 및 출퇴근 등록
![siteMap](https://user-images.githubusercontent.com/62315622/85417656-d3297a00-b5aa-11ea-8c83-cb26ab1f3d52.png)

 - 담당 업무 : 통합, 전체 CSS 총괄
 - 제작 기간 : 총 9주 (2020.01.17 ~2020.03.27)
 - 제작 일정
      일정	|	작업내역	| 상세설명 |
      ----- 	|	--------	| --------	|
      1주차	| 분석/설계| RFP / 요구사항 정의 및 요구사항 정의서 / 요건정의서 / WBS 프로젝트 / 일정 / 명명규칙 정의 작성 |
      2주차	|	분석/설계| 화면 설계서 / 테이블 설계서 / ERD / 클래스설계서 / 프로그램목록 / 시스템 아키텍처 작성 |
      3주차	|개발| 공통클래스 개발 및 배포: 파일 업로드, 다운로드 기능 / 채번 기능 개발 |
      4주차	|개발| 공지사항 게시판 개발 : 로그인 계정의 부서, 직책 권한에 따라 게시글 작성 및 공개 권한을 차별화함	|
      5주차	|개발| 게시글 확인 기능 개발 : 확인자는 버튼을 통해 글을 읽었음을 표시하고 작성자는 해당 리스트 열람	|
      6주차	|개발| 건의사항 게시판 개발: 익명게시판으로  Ajax를 이용한 댓글 및 추천기능 |
      7주차	|개발| 출퇴근 등록 기능 개발 : 출근 및 퇴근 시간을 등록해 주간근무, 월근무 시간 등을 계산함	|
      8주차	|통합작업| 통합 및 단위테스트, 통합 테스트	|
      9주차	|통합작업|CSS 작업 및 프로젝트 발표 문서 작업	|


### ERD
![erd](https://user-images.githubusercontent.com/62315622/84861319-b7563d80-b0ab-11ea-9bd2-f81b7a11dc53.PNG)


### 시스템 아키텍쳐
![systemArchitecture](https://user-images.githubusercontent.com/62315622/84861320-b7563d80-b0ab-11ea-8250-3652e8384f39.PNG)

## 2. 개발환경 
  종류                 | 프로그램 및 언어 |
  -------------------- |--------------|
  Server-Side Script   |   jsp-api 2.0, Servlet api 3.2   |
  Client-Side Script   |   HTML5, CSS3, jQuery, Ajax, JavaScript(ECMAScript3), BootStrap4   |
  Back End   |   JDK 1.7, Spring framework 3, Mybatis 3, Mybatis-spring 1.2, Oracle11g R 2 (AWS EC R2), 파일업로드 api – cos.jar, Log4j 1.2   |
  Data Handling   |   xml, plain-text, json   |
  Tool   |   eclipse luna, Apache Tomcat 8.0, SQL Developer, STS, Erdwin, Git Bash, GitHub, StarUML, AWS   |
  Open API   |   Smart-editor v2   |




## 3. 실행화면

![실행화면8](https://user-images.githubusercontent.com/62315622/87771975-0c78a100-c85c-11ea-9550-bf858343abde.PNG)
![실행화면9](https://user-images.githubusercontent.com/62315622/87771953-07b3ed00-c85c-11ea-9ca5-91db8502de4a.PNG)
![실행화면10](https://user-images.githubusercontent.com/62315622/87771957-097db080-c85c-11ea-81bb-9ec53e84d669.PNG)
![실행화면11](https://user-images.githubusercontent.com/62315622/87771959-0a164700-c85c-11ea-8005-18fd3ddd61a8.PNG)
![실행화면12](https://user-images.githubusercontent.com/62315622/87771964-0aaedd80-c85c-11ea-847c-15f7a8387fb2.PNG)
![실행화면13](https://user-images.githubusercontent.com/62315622/87771967-0b477400-c85c-11ea-8ae8-4da00c008b76.PNG)
![실행화면14](https://user-images.githubusercontent.com/62315622/87771969-0b477400-c85c-11ea-9501-8bd996ebcfe4.PNG)
![실행화면15](https://user-images.githubusercontent.com/62315622/87771972-0be00a80-c85c-11ea-8fc0-b6c5a057b16f.PNG)
