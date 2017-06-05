<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>
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
				<!-- 로고 이미지 -->
				<img src="<%=cp%>/resource/img/msg_send.png" style="margin: 10px"><br>
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