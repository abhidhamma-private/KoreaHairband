<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>
<div class="footer">
	<div class="footermenu">
		<ul>
			<li><a href='<%=cp%>/foot/policy.do'>정책 및 약관</a></li>
			<li><a href='<%=cp%>/foot/info.do'>사이트 소개</a></li>
			<li><a href='<%=cp%>/foot/proposal.do'>제휴 제안</a></li>
			<li><a href='<%=cp%>/foot/service.do'>이용 약관</a></li>
			<li><a href='<%=cp%>/foot/privacy.do'>고객정보취급방침</a></li>
		</ul>
	</div>
</div>
