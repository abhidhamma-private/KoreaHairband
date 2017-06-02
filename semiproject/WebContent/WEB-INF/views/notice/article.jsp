<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항(${dto.subject })</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">

function updateBoard(){
	<c:if test="${sessionScope.member.mem_Id==dto.mem_id}">
			var bbs_num = "${dto.not_num}";
			var page = "${page}";
			var query = "not_num="+not_num+"&page="+page;
	 		var url = "<%=cp%>/notice/update.do?" + query;
			location.href=url;
	</c:if>

	<c:if test="${sessionScope.member.mem_Id!=dto.mem_id}">
	  		alert("게시물을 수정할 수  없습니다.");
	</c:if>
}

function deleteBoard(){
	<c:if test="${sessionScope.member.mem_Id=='admin' || sessionScope.member.mem_Id==dto.mem_id}">
   		var bbs_num = "${dto.not_num}";
   		var page = "${page}";
   		var query = "not_num="+not_num+"&page="+page;
   		var url = "<%=cp%>/notice/delete.do?" + query;

   		if(confirm("위 자료를 삭제 하시 겠습니까 ? "))
   	 	location.href=url;
	</c:if>    
	<c:if test="${sessionScope.member.mem_Id!='admin' && sessionScope.member.mem_Id!=dto.mem_id}">
 	   alert("게시물을 삭제할 수 없습니다.");
	</c:if>
}
function login() {
	location.href="<%=cp%>/member/login.do";
}

</script>
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="f_container">

		<div>
			<table
				style="width: 900px; margin: 20px auto 0px; border-spacing: 0px;">
				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>

				<tr height="50">
					<td width="55%" align="left" style="padding-left: 5px;">
						${dto.subject}</td>
					<td width="30%" align="right" style="padding-right: 5px;">
						${dto.created} <i></i>조회수 : ${dto.hitCount}
					</td>

				</tr>
				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>

				<tr height="25">
					<td align="left" style="padding-left: 5px;">이름 :
						${dto.mem_name}</td>
				</tr>
				<tr>
					<td colspan="3" align="left" style="padding: 10px 5px;"
						valign="top" height="200">${dto.content }</td>
				</tr>
				<tr height="35" style="border-bottom: 1px solid #cccccc;">
					<td colspan="2" align="center" style="padding-left: 5px;">
					</td>
				</tr>
				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>
				<tr height="35" style="border-bottom: 1px solid #cccccc;">
					<td colspan="2" align="left" style="padding-left: 5px;">이전글 :
						<c:if test="${not empty preRead }">
							<a
								href="<%=cp%>/notice/article.do?${query}&num=${preRead.not_num}">${preRead.subject}</a>
						</c:if>

					</td>
				</tr>
				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>
				<tr height="35" style="border-bottom: 1px solid #cccccc;">
					<td colspan="2" align="left" style="padding-left: 5px;">다음글 :
						<c:if test="${not empty nextRead }">
							<a
								href="<%=cp%>/notice/article.do?${query}&num=${nextRead.not_num}">${nextRead.subject}</a>
						</c:if>

					</td>
				</tr>
				<tr>
					<td>
						<c:if test="${sessionScope.member.mem_Id==dto.mem_id}">
							<button type="button" class="btn" onclick="updateBoard();">수정</button>
						</c:if> <c:if
							test="${sessionScope.member.mem_Id==dto.mem_id || sessionScope.member.mem_Id=='admin'}">
							<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
						</c:if>
					</td>
					<td align="right">
						<button type="button" class="btn btn-default btn-sm wbtn"
							onclick="javascript:location.href='<%=cp%>/notice/list.do?${query}';">
							전체목록</button>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>
