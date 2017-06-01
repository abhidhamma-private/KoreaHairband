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
<title>자유게시판</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
function searchList() {
		var f = document.searchForm;
		f.action = "<%=cp%>/pet/petTalk/list.do";
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
			style="width: 780px; margin: 30px auto 0px; border-spacing: 0px;">
			<tr height="45">
				<td align="left" class="title">
					<h3>자유게시판</h3>
				</td>
			</tr>
		</table>

		<table
			style="width: 780px; margin: 20px auto 0px; border-spacing: 0px;">
			<tr height="35">
				<td align="left" width="50%">${dataCount}개
					(${page}/${total_page}페이지)</td>
			</tr>
		</table>

		<table style="width: 780px; margin: 0px auto; border-spacing: 0px;">
			<tr>
				<td height="1" colspan="6" bgcolor="#cccccc"></td>
			</tr>
			<tr align="center" height="35">
				<th width="60" style="color: #787878;">번호</th>
				<th style="color: #787878;">제목</th>
				<th width="100" style="color: #787878;">작성자</th>
				<th width="80" style="color: #787878;">작성일</th>
				<th width="60" style="color: #787878;">조회수</th>
			</tr>
			<tr>
				<td height="1" colspan="6" bgcolor="#cccccc"></td>
			</tr>

			<c:forEach var="dto" items="${listNotice}">
				<tr align="center" bgcolor="#ffffff" height="35"
					style="border-bottom: 1px solid #cccccc;">
					<td><span
						style="display: inline-block; width: 50px; height: 18px; line-height: 18px; background: #ED4C00; color: #FFFFFF;">공지</span>
					</td>
					<td align="left" style="padding-left: 10px;"><a
						href="${articleUrl}&bbs_num=${dto.bbs_num}">[${dto.category}]${dto.subject }</a>
					</td>
					<td align="center">${dto.mem_name }</td>
					<td align="center">${dto.created }</td>
					<td align="center">${dto.hitCount}</td>
				</tr>
			</c:forEach>

			<c:forEach var="dto" items="${list}">
				<tr align="center" bgcolor="#ffffff" height="35">
					<td align="center">${dto.listNum }</td>
					<td align="left" style="padding-left: 10px;"><a
						href="${articleUrl}&bbs_num=${dto.bbs_num}">[${dto.category}]${dto.subject}</a>
						<c:if test="${dto.gap<1}">
							<img src="<%=cp%>/resource/img/new.gif">
						</c:if></td>
					<td align="center">${dto.mem_name }</td>
					<td align="center">${dto.created }</td>
					<td align="center">${dto.hitCount}</td>
				</tr>
			</c:forEach>

			<tr>
				<td height="1" colspan="6" bgcolor="#cccccc"></td>
			</tr>

		</table>

		<table style="width: 780px; margin: 0px auto; border-spacing: 0px;">
			<tr height="35">
				<td align="center">${paging}</td>
			</tr>
		</table>

		<table style="width: 780px; margin: 10px auto; border-spacing: 0px;">
			<tr height="40">

				<td align="right" width="100">
					<button type="button" class="btn"
						onclick="javascript:location.href='<%=cp%>/pet/petTalk/created.do';">글올리기</button>
				</td>
				<td align="center">
					<form name="searchForm" method="post">
						<select name="searchKey" class="selectField">
							<option value="subject">제목</option>
							<option value="mem_name">작성자</option>
							<option value="content">내용</option>
							<option value="created">작성일</option>
							<option value="category">카테고리</option>
						</select> <input type="text" name="searchValue" class="boxTF">
						<button type="button" class="btn" onclick="searchList()">검색</button>
					</form>
				</td>
				<td align="left" width="100">
					<button type="button" class="btn"
						onclick="javascript:location.href='<%=cp%>/pet/petTalk/list.do';">리스트</button>
				</td>

			</tr>
		</table>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

</body>
</html>
