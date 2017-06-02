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
function submitMessage(){
	f=document.createdForm;
	f.action = "<%=cp%>/message/m_created_ok.do";
	f.submit();
	alert("쪽지 전송완료!");
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
				<h3>쪽지쓰기 </h3><br>
				
				</td>
			</tr>
			
		</table>
		
	<form method="post" name="createdForm">
	  <input type="button" value="보내기" class="btn" onclick="submitMessage();">
  	  <input type="button" value="취소" class="btn" onclick="">
  	  	
		<hr>
	받는사람:${mem_Id1}<br><br>
	<textarea rows="20" cols="100" name="content"></textarea><br>
	<input type="hidden" name="mem_Id2" value="${mem_Id1}">
	
	<input type="hidden" name="returnpage" value="${returnpage}">

	
 	</form>
	
</div>
<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>


</body>
</html>