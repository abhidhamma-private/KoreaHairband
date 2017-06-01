<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String cp = request.getContextPath();
%>

<div style="clear: both; padding-top: 20px; width: 900px;">
	<div style="float: left;">
		<span style="color: #3EA9CD; font-weight: bold;">댓글
			${replyCount}개</span> <span>[댓글 목록, ${pageNo}/${total_page} 페이지]</span>
	</div>
	<div style="float: right; text-align: right;"></div>
</div>

<div style="clear: both; padding-top: 5px;">

	<c:forEach var="vo" items="${listReply}">
		<!-- 리플 내용 리스트 시작 -->
		<div
			style="clear: both; margin-top: 5px; padding: 10px; border: #d5d5d5 solid 1px; min-height: 30px;">
			<div style="clear: both;">
				<div style="float: left;">${vo.mem_Name}| ${vo.created}</div>
				<div style="float: right; text-align: rigth;">
					<c:if
						test="${sessionScope.member.mem_Id==vo.mem_Id || sessionScope.member.mem_Id=='admin'}">
						<a onclick='deleteReply("${vo.reply_num}", "${pageNo}");'>삭제</a>
					</c:if>
					<c:if
						test="${sessionScope.member.mem_Id!=vo.mem_Id && sessionScope.member.mem_Id!='admin'}">
						<a href='#'>신고</a>
					</c:if>
				</div>
			</div>
			<div style="clear: both; padding: 5px 0 5px 0px; min-height: 5px;">
				${vo.content}</div>

		</div>

	</c:forEach>
</div>

<div style="clear: both; padding-top: 10px; text-align: center;">
	${paging}</div>
