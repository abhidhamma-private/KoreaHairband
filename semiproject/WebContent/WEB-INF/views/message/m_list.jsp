<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setCharacterEncoding("utf-8");
	String cp=request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
</head>
<body>
<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
<div class="f_container">

		<table
			style="width: 900px; margin: 30px auto 0px; border-spacing: 0px;">
			<tr height="45">
				<td align="left" class="title">
				<h3>${sessionScope.member.mem_Name} 님의 메시지함 ${dataCount }개(${page }/${total_page }페이지)</h3><br>
				읽지않은 메시지 : 3개
				</td>
			</tr>
			
		</table>
		


<table style="width: 900px; margin: 10px auto 0px; border-spacing: 0px;">
<tr height="35">
   <td align="left">
       <input type="button" value="삭제" class="btn" onclick="deleteList();">
   </td>
 
</tr>
</table>
		
 			
 <form method="post" name="listForm">
<table style="width: 900px; margin: 0px auto; border-spacing: 1px; background: #cccccc;">
<tr height="30" bgcolor="#eeeeee" align="center">
	<td width="35">
	    <input type="checkbox" name="chkAll" value="all" onclick="check();">
	</td>
	
	<td width="200">보낸사람</td>
	<td width="400">내용</td>
	<td width="200">날짜</td>
	<td width="100">삭제</td>
	
</tr>


<c:forEach var="dto" items="${list}">
<tr height="25" bgcolor="#ffffff" align="center">
	<td>
	    <input type="checkbox" name="haks" value="${dto.message_num }">
	</td>
	
	<td>${dto.mem_Id1}</td>
	<td align="left" style="padding-left: 10px;">${dto.content}</td>
	<td>${dto.sendDate}</td>
	

	<td>
			<input type="button" value="삭제" onclick="deleteScore()" class="btn">
	</td>
</tr>
</c:forEach>

</table>


</form>
  </div>
  
  
	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
  
</body>
</html>