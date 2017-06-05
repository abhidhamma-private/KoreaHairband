<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>pt>
<script type="text/javascript">
function deleteList() {
	var f=document.listForm;
	var cnt=0;
	
	if(f.messages==undefined) {
		return;		
	}
	
	if(f.messages.length!=undefined) {// 체크박스가 둘 이상인 경우
		for(var i=0; i<f.messages.length; i++) {
			if(f.messages[i].checked)
				cnt++;
		}
	} else {
		// 체크박스가 하나인 경우
		if(f.messages.checked)
			cnt++;
	}
	
	if(cnt==0) {
		alert("선택한 항목이 없습니다.");
		return;
	}
	
	if(confirm("선택 항목을 삭제 하시겠습니까 ?")) {
		f.action="<%=cp%>/message/m_deleteList.do";
		f.submit();
	}
}

function check() {
	var f=document.listForm;
	
	if(f.messages==undefined)
		return;
	
	if(f.messages.length!=undefined) { // 체크박스가 둘 이상인 경우
		for(var i=0; i<f.messages.length; i++) {
			if(f.chkAll.checked)
				f.messages[i].checked=true;
			else
				f.messages[i].checked=false;
		}
	} else { // 체크박스가 하나인 경우
		if(f.chkAll.checked)
			f.messages.checked=true;
		else
			f.messages.checked=false;
	}
}

function deleteMessage() {
	var f=document.listForm;
	if(confirm("쪽지를 삭제 하시겠습니까 ?")) {
		f.action = "<%=cp%>/message/m_delete.do";
		f.submit();
		<%-- var url="<%=cp%>/message/m_delete.do";
		location.href=url --%>
	}
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
				<img src="<%=cp%>/resource/img/msg.png" style="margin: 10px"><br>
				<h4>[${sessionScope.member.mem_Name}]님의 쪽지함 ${dataCount }개(${page }/${total_page }페이지)</h4><br>
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
<table style="width: 900px; margin: 0px auto; border-spacing: 1px; background: #eeeeee;">
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
<tr height="25" bgcolor="white" align="center">
	<td>
	    <input type="checkbox" name="messages" value="${dto.message_num}">
	</td>
	
	<td>${dto.mem_Id1}</td>
	<td align="left" style="padding-left: 10px;"><a href="<%=cp %>/message/m_article.do?message_num=${dto.message_num}">${dto.content}</a></td>
	<td>${dto.sendDate}</td>
	

	<td>
			<input type="button" value="삭제" onclick="deleteMessage()"class="btn">
			<input type="hidden" name="message_num" value="${dto.message_num}">
	</td>
</tr>
</c:forEach>

</table>

<table style="width: 900px; margin: 0px auto; border-spacing: 0px;">
			<tr height="35">
				<td align="center">${paging}</td>
			</tr>
		</table>

</form>
  </div>
  
  
	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
  
</body>
</html>