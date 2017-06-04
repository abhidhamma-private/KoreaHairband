<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>
<style>
#profile-container {
	width: 900px;
	margin: 0px auto;
	padding: 0px;
	border: 0px;
}

#profile-header {
	padding: 0px 10px 0px 10px;
	text-align: center;
	margin-bottom: 5px;
	border: 0px;
}

#profile-info {
	width: 600px;
	height: 320px;
	padding: 0px 10px 10px 10px;
	margin-bottom: 20px;
	float: right;
	border: 0px;
}

#profile-photo {
	width: 250px;
	height: 320px;
	padding: 0px 10px 10px 10px;
	margin-bottom: 20px;
	float: left;
	border: 0px;
}

#profile-footer {
	clear: both;
	margin: auto;
	text-align: center;
	padding: 0px 10px 10px 10px;
	border: 0;
}
</style>

</head>
<body>
	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div id="profile-container">
		<div id="profile-header">
			<!-- 로고 이미지 -->
			<img src="<%=cp%>/resource/img/mypage.png" style="margin: 10px"><br>
		</div>
		
		<div id="profile-photo">
			<!-- 로고 이미지 -->
			<img src="<%=cp%>/resource/img/profilephoto.png" style="margin: 10px">
			<c:if test="${dto.mem_img!=null||dto.mem_img=='null'}}">
				<img src="<%=cp%>/uploads/photo/${dto.mem_img}"
					style="max-width: 250px; height: auto; resize: both;">
			</c:if>
			<c:if test="${dto.mem_img==null||dto.mem_img=='null'}">
				<img src="<%=cp%>/resource/img/noimg.png"
					style="max-width: 250px; height: auto; resize: both;">
			</c:if>
		</div>
		<div id="profile-info">
			<!-- 로고 이미지 -->
			<img src="<%=cp%>/resource/img/memberinfo.png" style="margin: 10px"><br>
			ID : ${dto.mem_Id}<br>
			이름 : ${dto.mem_Name}<br>
			포인트 : ${dto.point}<br>
			생일 : ${dto.birth}<br>
			이메일 : ${dto.email}<br>
			연락처 : ${dto.tel}<br>
			우편번호 : ${dto.zip}<br>
			주소 : ${dto.addr1} ${dto.addr2 }<br>
			가입일 : ${dto.created_Date}<br>
			최근정보수정일 : ${dto.modify_Date}<br>
			회원상태 :
			<c:if test="${dto.enabled==1}">정상</c:if>
			<c:if test="${dto.enabled==0}">활동정지</c:if>
		</div>
		<div id="profile-footer">
			<a href="<%=cp%>/member/pwd.do?update"><span class="glyphicon glyphicon-update"></span> 정보수정</a>
			<i></i>
			<a href="<%=cp%>/member/pwd.do"><span class="glyphicon glyphicon-delete"></span> 회원탈퇴</a> <br>
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