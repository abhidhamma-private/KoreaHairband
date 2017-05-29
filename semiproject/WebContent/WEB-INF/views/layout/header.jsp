<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>
<div class="container">
	<div id="page-header">
		<div class="header-brand">
			<a href="main.jsp"><img src="<%=cp%>/resource/img/toplogo.png"></a>
		</div>
		<div class="login header-login">
			<c:if test="${empty sessionScope.member}">
				<a href="<%=cp%>/member/login.do"><span
					class="glyphicon glyphicon-log-in"></span> 로그인</a>
				<i></i>
				<a href="<%=cp%>/member/member.do"><span
					class="glyphicon glyphicon-user"></span> 회원가입</a>
			</c:if>
			<c:if test="${not empty sessionScope.member}">
				<span style="color: blue;">${sessionScope.member.userName}</span>님 <i></i>
				<c:if test="${sessionScope.member.userId=='admin'}">
					<a href="<%=cp%>/admin/main.do">관리자</a>
					<i></i>
				</c:if>
				<a href="<%=cp%>/member/logout.do"><span
					class="glyphicon glyphicon-log-out"></span> 로그아웃</a>
			</c:if>
		</div>
	</div>
</div>

<div class="container">
	<div id="page-header-navi">
		<div class="header-menu">
			<ul>
				<li><a href='main.jsp'>홈</a></li>
				<li class='active sub'><a href='#'>스포츠</a>
					<ul>
						<li><a href='#'>자유게시판</a></li>
						<li><a href='#'>포토갤러리</a></li>
					</ul></li>

				<li class='active sub'><a href='#'>건강</a>
					<ul>
						<li><a href='#'>자유게시판</a></li>
						<li><a href='#'>정보게시판</a></li>
					</ul></li>
				<li class='active sub'><a href='#'>패션</a>
					<ul>
						<li><a href='#'>자유게시판</a></li>
						<li><a href='#'>중고거래</a></li>
					</ul></li>
				<li class='active sub'><a href='#'>푸드</a>
					<ul>
						<li><a href='#'>레시피요청</a></li>
						<li><a href='#'>레시피공유</a></li>
					</ul></li>
				<li class='active sub'><a href='#'>반려동물</a>
					<ul>
						<li><a href='#'>정보공유</a></li>
						<li><a href='#'>자유게시판</a></li>
					</ul></li>
				<li class='active sub'><a href='#'>IT</a>
					<ul>
						<li><a href='#'>뉴스/신제품</a></li>
						<li><a href='#'>자유게시판</a></li>
					</ul></li>
				<li class='active sub'><a href='#'>고객센터</a>
					<ul>
						<li><a href='#'>공지사항</a></li>
						<li><a href='#'>자주묻는질문</a></li>
						<li><a href='#'>Q&A</a></li>
					</ul></li>
			</ul>
		</div>
	</div>
</div>