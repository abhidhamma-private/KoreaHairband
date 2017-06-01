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
		
		<hr>
	받는사람:${mem_Id1}<br><br>
	<textarea rows="20" cols="100" name=""></textarea><br>
	<hr>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>


</body>
</html>