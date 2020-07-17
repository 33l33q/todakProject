<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java142.todak.scheduler.controller.SchedulerController" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>출퇴근 버튼</title>
		<link rel="stylesheet" type="text/css" href="/include/css/default.css"/>
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
		<script type="text/javascript">
		
		$(document).ready(function(){

			
			$("#goWork").click(function(){
				console.log("출근창으로 이동하기");
				
				window.open("", "pop", "width=1400, height=280");
				$("#commuteIndex").attr({
	 				"method":"post",
	 				"action":"../scheduler/goWork.td",
	 				"target" : "pop"
	 			});
	 			$("#commuteIndex").submit();
			});
			
			$("#goHome").click(function(){
				console.log("퇴근창으로 이동하기");
				window.open("", "pop", "width=1400, height=280");
				$("#commuteIndex").attr({
	 				"method":"post",
	 				"action":"../scheduler/goHome.td",
	 				"target" : "pop"
	 			});
	 			$("#commuteIndex").submit();
			});
		});
		
		</script>

		<div>
			<form id="commuteIndex" name="commuteIndex" enctype="application/x-www-form-urlencoded" method="POST">
			<input type="hidden" id="hm_empnum" name="hm_empnum" value="">
				<table align="center">
					<tr>
						<td><input type="button" id="goWork" name="goWork" value="출근" class="button" style="width:74px;height:30px;"></td>
						<td><input type="button" id="goHome" name="goHome" value="퇴근" class="button" style="width:74px;height:30px;"></td>	
					</tr>
				</table>
			</form>
		</div>
	</head>
	<body>
	</body>
</html>