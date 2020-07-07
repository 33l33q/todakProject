<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java142.todak.board.controller.BoardController"  %>
<%@ page import="java142.todak.board.vo.NoticeVO"  %>
<%@ page import="java.util.ArrayList"  %>

<%@ include file="/WEB-INF/views/commons/bindSession.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>수정창</title>
		<link rel="stylesheet" type="text/css" href="/include/css/default.css"/>
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
		<script type="text/javascript"
		src="../webedit/dist/js/service/HuskyEZCreator.js" charset="utf-8"></script>
		<script type="text/javascript">
		var oEditors = [];
		 $(function(){
				//저장 버튼 클릭 시 처리 이벤트 
				nhn.husky.EZCreator.createInIFrame({
					fOnAppLoad : function() {
						//기존 저장된 내용의 text 내용을 에디터상에 뿌려주고자 할때 사용
						oEditors.getById["bn_content"].exec("PASTE_HTML", [""]);
					},
					oAppRef : oEditors,
					elPlaceHolder : "bn_content", //textarea에서 지정한 id와 일치해야 합니다. 
					//SmartEditor2Skin.html 파일이 존재하는 경로
					sSkinURI : "../webedit/dist/SmartEditor2Skin.html",
					htParams : {
						// 툴바 사용 여부 (true:사용/ false:사용하지 않음),글씨체 포인트,정렬,색상등등
						bUseToolbar : true,
						// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
						bUseVerticalResizer : true,
						// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
						bUseModeChanger : true,
						fOnBeforeUnload : function() {
						}
					},
					fCreator : "createSEditor2"
				});
				
		 		$("#deleteNotice").click(function(){
		 			alert("글 삭제하기");
		 			var bn_num = $("#bn_num").val();
		 			
		 			var hm_empnum = $("#hm_empnum").val();
		 			$("#hm_empnum").val(hm_empnum);
		 			
		 			$("#bn_num").val(bn_num);
		 			$("#n_updateForm").attr({
		 				"method":"get",
		 				"action":"../board/deleteNotice.td"
		 			});
		 			
		 			$("#n_updateForm").submit();
		 		});
				
				$("#updateNotice").click(function() {
					alert("글 수정하기");
					
		 			var bn_num = $("#bn_num").val();
		 			$("#bn_num").val(bn_num);
		 			
		 			alert("bn_num >>> : " +  bn_num);
					
					if(!$("#bn_title").val()) {
						console.log("if(!$('#bn_title').val())");
						alert("제목을 입력하세요");
						$("#bn_title").focus();
						return false;
						}
					
			        var bn_content = oEditors.getById['bn_content'].getIR();
			        var checkarr = ['<p>&nbsp;</p>','&nbsp;','<p><br></p>','<p></p>','<br>'];
			        if(jQuery.inArray(bn_content.toLowerCase(), checkarr) != -1){
			            alert("내용을 입력해 주십시오.");
			            oEditors.getById["bn_content"].exec('FOCUS');
			            return false;
			        }
					
					else{
						var ext1 = $('#bn_image').val().split('.').pop().toLowerCase();
						if(ext1){
							if(jQuery.inArray(ext1,['gif','png','jpg','jpeg']) == -1 ) {
		                        alert('gif,png,jpg,jpeg 사진만 업로드 할 수 있습니다');
								return false;
							}
						}
						 
						 var ext2 = $('#bn_file').val().split('.').pop().toLowerCase();
						 if(ext2){
							 if(jQuery.inArray(ext2,['hwp','pdf','doc','xlsx','xls','xml']) == -1 ) {
	                           alert('hwp,pdf,doc,xlsx,xls,xml 파일만 업로드 할 수 있습니다');
								return false;
							}
						 }
						
						oEditors.getById["bn_content"].exec("UPDATE_CONTENTS_FIELD", []);
						$("#n_updateForm").submit();
						
					
					}
			        
			    	//공개범위 조정하기
					var rangeIndex = $("#rangeIndex").val();
					
					if(rangeIndex == 98){
						$("#bn_deptnum").val(rangeIndex);
						$("#bn_divnum").val(rangeIndex);
					}else{
						if(rangeIndex <= 3){
							$("#bn_deptnum").val("98");
							$("#bn_divnum").val(rangeIndex);
						}else{
							$("#bn_deptnum").val(rangeIndex);
							
							if(rangeIndex < 7) $("#bn_divnum").val("00")
							else if(rangeIndex < 10) $("#bn_divnum").val("01"); 
							else if(rangeIndex < 11) $("#bn_divnum").val("02"); 
							else if(rangeIndex < 12) $("#bn_divnum").val("03");
							else if(rangeIndex < 17) $("#bn_divnum").val("98");
						}
					}
					
			        
					$("#n_updateForm").attr({
						"method":"POST",
						"action":"../board/updateNotice.td"
					});
					
					$("#n_updateForm").submit();
					
				});
				
		 		$("#selectNotice").click(function(){
		 			alert("(log)목록으로 돌아가기");
		 			
		 			var hm_empnum = $("#hm_empnum").val();
		 			$("#hm_empnum").val(hm_empnum);
		 			
		 			alert("hm_empnum >>> : " + hm_empnum);
		 			
		 			$("#n_updateForm")
		 			.attr("action","../board/selectNotice.td")
		            .submit();
		 		});
			});
		
		</script>
<%
		NoticeVO nvo = new NoticeVO();
		Object obj =  request.getAttribute("updateList");
		ArrayList<NoticeVO> sList = (ArrayList<NoticeVO>)obj;
		
		nvo = sList.get(0);
		//System.out.println(nvo.getBn_num());
		

%>
	</head>
	<body>
		 <header class="header"> 
            <%@ include file="/WEB-INF/views/commons/header.jsp" %>
         </header>

         <aside class="sidebar">
            <%@ include file="/WEB-INF/views/commons/sidebar.jsp" %>
         </aside>
         
         <div class="container">
	         
	<%
			String hm_empnum = sManager.getUserID(session.getId());
	%>
			
		<div id="boardTit" align = "center"><h3>글수정</h3></div>
		<form id="n_updateForm" name="n_updateForm" enctype="multipart/form-data">
			<input type="hidden" name="hm_name" id="hm_name" value="<%=nvo.getHm_name() %>">
			<input type="hidden" name="bn_num" id="bn_num" value="<%=nvo.getBn_num() %>">
			<input type="hidden" name="hm_empnum" id="hm_empnum" value="<%=hm_empnum%>">	
				<table id="boardWrite" align="center">
						<tr>
							<td width="600">작성자</td>
							<td><b><%=nvo.getHm_name() %></b></td>
							
							<td align="right">
								<label>게시글 공개 범위</label>
								<select id="rangeIndex" name="rangeIndex">
									<option value="98">전체공지</option>
									<option value="00" style="font-weight:bold">경영지원본부</option>
									<option value="04">인재경영팀</option>
									<option value="05">재무팀</option>
									<option value="06">관리팀</option>
									<option value="01" style="font-weight:bold">전략기획본부</option>
									<option value="07">기획팀</option>
									<option value="08">소통협력팀</option>
									<option value="09">IT팀</option>
									<option value="02" style="font-weight:bold">마케팅본부</option>
									<option value="10">홍보팀</option>
									<option value="03" style="font-weight:bold">나눔사업본부</option>
									<option value="11">배분팀</option>
									<option value="12">혁신사업팀</option>
								</select>
								<input type="hidden" name="bn_divnum" id="bn_divnum" >
								<input type="hidden" name="bn_deptnum" id="bn_deptnum" >
							</td>
						</tr>
						<tr>
							<td>글제목</td>
							<td><input size="65" type="text" id="bn_title" name="bn_title"  size="80" value="<%= nvo.getBn_title()%>"></td>
						</tr>
						<tr>
							<td>내용</td>
							<td height="200" width="800">
							<textarea name="bn_content" id="bn_content" rows="10" cols="70"><%=nvo.getBn_content() %></textarea></td>
						</tr>
	<%
					String bn_image = nvo.getBn_image(); 
					if(bn_image != null){
	
	%>
						<tr>
							<td>이미지</td>
							<td><img src="../<%=bn_image%>" style="max-width:90%;">
							<input type="file" name="bn_image" id="bn_image" value="<%=nvo.getBn_image()%> "></td>
						</tr>
	<%
					}else{
	%>
						<tr>
							<td>이미지</td>
							<td><input type="file" name="bn_image" id="bn_image"></td>
						</tr>
	<%
					}
					String bn_file = nvo.getBn_file();
					if(bn_file != null){
	%>
						<tr>
							<td colspan = 2>첨부파일</td>
							<td><input type="file" name="bn_file" id="bn_file" value=<%= bn_file %>></td>
						</tr>
	<%
					}else{
	%>
						<tr>
							<td>첨부파일</td>
							<td><input type="file" name="bn_file" id="bn_file"></td>
						</tr>
	<%
					}
	%>
							<td>
						</tr>
				</table>
					<div class="noticesearch_align" align="right">
						<input type="button" value="수정" class="button" id="updateNotice" name="updateNotice"/>
						<input type="button" value="삭제" class="button" id="deleteNotice" name="deleteNotice"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" value="목록" class="button" id="selectNotice" name="selectNotice"/>
					</div>
			</form>
         </div>
	</body>
</html>