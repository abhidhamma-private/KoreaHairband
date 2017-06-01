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
<script type="text/javascript">
function deleteMessage() {
	var f=document.articleForm;
	if(confirm("쪽지를 삭제 하시겠습니까 ?")) {
		f.action = "<%=cp%>/message/m_delete.do";
		f.submit();
	}
}
function sendMessage(){
	var f=document.articleForm;
	f.action = "<%=cp%>/message/m_created.do";
	f.submit();
}
</script>
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
				<h3>${sessionScope.member.mem_Name} 님의 메시지</h3><br>
				
				</td>
			</tr>
			
		</table>
		
		<form method="post" name="articleForm">
		
       <input type="button" value="삭제" class="btn" onclick="deleteMessage();">
  	   <input type="button" value="답장" class="btn" onclick="sendMessage();">
  	   <input type="hidden" name="message_num" value="${dto.message_num}">
	   <input type="hidden"  name="mem_Id1" value="${dto.mem_Id1}">

		<hr>
		보낸사람:${dto.mem_Id1}<br>
		보낸시간:${dto.sendDate}<br>
		읽은시간:${dto.readDate}<br>
		<hr>
		내용:
		${dto.content }
		<hr>
			
		</form>
		
</div>


<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>