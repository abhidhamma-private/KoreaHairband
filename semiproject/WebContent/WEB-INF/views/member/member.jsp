<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>

<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">
	function memberOk() {
		var f = document.memberForm;
		var str;

		str = f.mem_Id.value;
		str = str.trim();
		if (!str) {
			alert("아이디를 입력하세요. ");
			f.mem_Id.focus();
			return;
		}
		if (!/^[a-z][a-z0-9_]{4,9}$/i.test(str)) {
			alert("아이디는 5~10자이며 첫글자는 영문자이어야 합니다.");
			f.mem_Id.focus();
			return;
		}
		f.mem_Id.value = str;

		str = f.mem_Pwd.value;
		str = str.trim();
		if (!str) {
			alert("패스워드를 입력하세요. ");
			f.mem_Pwd.focus();
			return;
		}
		if (!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(str)) {
			alert("패스워드는 5~10자이며 하나 이상의 숫자나 특수문자가 포함되어야 합니다.");
			f.mem_Pwd.focus();
			return;
		}
		f.mem_Pwd.value = str;

		if (str != f.mem_PwdCheck.value) {
			alert("패스워드가 일치하지 않습니다. ");
			f.mem_PwdCheck.focus();
			return;
		}

		str = f.mem_Name.value;
		str = str.trim();
		if (!str) {
			alert("이름을 입력하세요. ");
			f.mem_Name.focus();
			return;
		}
		f.mem_Name.value = str;

		str = f.birth.value;
		str = str.trim();
		if (!str || !isValidDateFormat(str)) {
			alert("생년월일를 입력하세요[YYYY-MM-DD]. ");
			f.birth.focus();
			return;
		}

		str = f.tel1.value;
		str = str.trim();
		if (!str) {
			alert("전화번호를 입력하세요. ");
			f.tel1.focus();
			return;
		}

		str = f.tel2.value;
		str = str.trim();
		if (!str) {
			alert("전화번호를 입력하세요. ");
			f.tel2.focus();
			return;
		}
		if (!/^(\d+)$/.test(str)) {
			alert("숫자만 가능합니다. ");
			f.tel2.focus();
			return;
		}

		str = f.tel3.value;
		str = str.trim();
		if (!str) {
			alert("전화번호를 입력하세요. ");
			f.tel3.focus();
			return;
		}
		if (!/^(\d+)$/.test(str)) {
			alert("숫자만 가능합니다. ");
			f.tel3.focus();
			return;
		}

		str = f.email1.value;
		str = str.trim();
		if (!str) {
			alert("이메일을 입력하세요. ");
			f.email1.focus();
			return;
		}

		str = f.email2.value;
		str = str.trim();
		if (!str) {
			alert("이메일을 입력하세요. ");
			f.email2.focus();
			return;
		}

		var mode = "${mode}";
		if (mode == "created") {
			f.action = "<%=cp%>/member/member_ok.do";
		} else if (mode == "update") {
			f.action = "<%=cp%>/member/update_ok.do";
		}

		f.submit();
	}

	function changeEmail() {
		var f = document.memberForm;

		var str = f.selectEmail.value;
		if (str != "direct") {
			f.email2.value = str;
			f.email2.readOnly = true;
			f.email1.focus();
		} else {
			f.email2.value = "";
			f.email2.readOnly = false;
			f.email1.focus();
		}
	}

	function mem_IdCheck() {
		var mem_Id = $("#mem_Id").val().trim();
		if (!/^[a-z][a-z0-9_]{4,9}$/i.test(mem_Id)) {
			$("#mem_Id").parent().next(".help-block").html("첫글자가 영문자인 5~10자 이내의 문자이어야 가능합니다.");
			// alert("첫글자가 영문자인 5~10자 이내의 문자이어야 가능합니다.");
			$("#mem_Id").focus();
			return;
		}

		var url = "<%=cp%>/member/mem_IdCheck.do";
		var query = "mem_Id=" + mem_Id;

		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data) {
				var passed = data.passed;
				if (passed == "true") {
					var s = "<span style='color:blue;'>" + $("#mem_Id").val()
						+ "</span> 아이디는 사용 가능합니다.";
					$("#mem_Id").parent().next(".help-block").html(s);
				} else {
					var s = "<span style='color:red;'>" + $("#mem_Id").val()
						+ "</span> 아이디는 사용할 수 없습니다.";
					$("#mem_Id").parent().next(".help-block").html(s);
					$("#mem_Id").val("");
					$("#mem_Id").focus();
				}
			},
			error : function(e) {
				console.log(e.responseText);
			}
		});
	}
</script>
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="container">
		<div class="body-container" style="width: 700px;">
			<div class="body-title">
				<h3>
					<span style="font-family: Webdings">2</span> ${title}
				</h3>
			</div>

			<div>
				<form name="memberForm" method="post">
					<table
						style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">아이디</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<input type="text" name="mem_Id" id="mem_Id"
										value="${dto.mem_Id}" onchange="mem_IdCheck();"
										style="width: 95%;"
										${mode=="update" ? "readonly='readonly' ":""} maxlength="15"
										class="boxTF" placeholder="아이디">
								</p>
								<p class="help-block">아이디는 5~10자 이내이며, 첫글자는 영문자로 시작해야 합니다.</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">패스워드</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<input type="password" name="mem_Pwd" maxlength="15"
										class="boxTF" style="width: 95%;" placeholder="패스워드">
								</p>
								<p class="help-block">패스워드는 5~10자 이내이며, 하나 이상의 숫자나 특수문자가
									포함되어야 합니다.</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">패스워드 확인</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<input type="password" name="mem_PwdCheck" maxlength="15"
										class="boxTF" style="width: 95%;" placeholder="패스워드 확인">
								</p>
								<p class="help-block">패스워드를 한번 더 입력해주세요.</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">이름</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<input type="text" name="mem_Name" value="${dto.mem_Name}"
										maxlength="30" class="boxTF" style="width: 95%;"
										${mode=="update" ? "readonly='readonly' ":""} placeholder="이름">
								</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">생년월일</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<input type="text" name="birth" value="${dto.birth}"
										maxlength="10" class="boxTF" style="width: 95%;"
										placeholder="생년월일">
								</p>
								<p class="help-block">생년월일은 2000-01-01 형식으로 입력 합니다.</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">이메일</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<select name="selectEmail" onchange="changeEmail();"
										class="selectField">
										<option value="">선 택</option>
										<option value="naver.com"
											${dto.email2=="naver.com" ? "selected='selected'" : ""}>네이버
											메일</option>
										<option value="hanmail.net"
											${dto.email2=="hanmail.net" ? "selected='selected'" : ""}>한
											메일</option>
										<option value="hotmail.com"
											${dto.email2=="hotmail.com" ? "selected='selected'" : ""}>핫
											메일</option>
										<option value="gmail.com"
											${dto.email2=="gmail.com" ? "selected='selected'" : ""}>지
											메일</option>
										<option value="direct">직접입력</option>
									</select> <input type="text" name="email1" value="${dto.email1}"
										size="13" maxlength="30" class="boxTF"> @ <input
										type="text" name="email2" value="${dto.email2}" size="13"
										maxlength="30" class="boxTF" readonly="readonly">
								</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">전화번호</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<select class="selectField" id="tel1" name="tel1">
										<option value="">선 택</option>
										<option value="010"
											${dto.tel1=="010" ? "selected='selected'" : ""}>010</option>
										<option value="011"
											${dto.tel1=="011" ? "selected='selected'" : ""}>011</option>
										<option value="016"
											${dto.tel1=="016" ? "selected='selected'" : ""}>016</option>
										<option value="017"
											${dto.tel1=="017" ? "selected='selected'" : ""}>017</option>
										<option value="018"
											${dto.tel1=="018" ? "selected='selected'" : ""}>018</option>
										<option value="019"
											${dto.tel1=="019" ? "selected='selected'" : ""}>019</option>
									</select> - <input type="text" name="tel2" value="${dto.tel2}"
										class="boxTF" maxlength="4"> - <input type="text"
										name="tel3" value="${dto.tel3}" class="boxTF" maxlength="4">
								</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">우편번호</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<input type="text" name="zip" value="${dto.zip}" class="boxTF">
									<button type="button" class="btn">우편번호</button>
								</p>
							</td>
						</tr>

						<tr>
							<td width="100" valign="top"
								style="text-align: right; padding-top: 5px;"><label
								style="font-weight: 900;">주소</label></td>
							<td style="padding: 0 0 15px 15px;">
								<p style="margin-bottom: 5px;">
									<input type="text" name="addr1" value="${dto.addr1}"
										maxlength="50" class="boxTF" style="width: 95%;"
										placeholder="기본 주소">
								</p>
								<p style="margin-bottom: 5px;">
									<input type="text" name="addr2" value="${dto.addr2}"
										maxlength="50" class="boxTF" style="width: 95%;"
										placeholder="나머지 주소">
								</p>
							</td>
						</tr>
						<c:if test="${mode=='created'}">
							<tr>
								<td width="100" valign="top"
									style="text-align: right; padding-top: 5px;"><label
									style="font-weight: 900;">약관동의</label></td>
								<td style="padding: 0 0 15px 15px;">
									<p style="margin-top: 7px; margin-bottom: 5px;">
										<label> <input id="agree" name="agree" type="checkbox"
											checked="checked"
											onchange="form.sendButton.disabled = !checked"> <a
											href="#">이용약관</a>에 동의합니다.
										</label>
									</p>
								</td>
							</tr>
						</c:if>
					</table>

					<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
						<tr height="45">
							<td align="center">
								<button type="button" name="sendButton" class="btn"
									onclick="memberOk();">${mode=="created"?"회원가입":"정보수정"}</button>
								<button type="reset" class="btn">다시입력</button>
								<button type="button" class="btn"
									onclick="javascript:location.href='<%=cp%>/';">${mode=="created"?"가입취소":"수정취소"}</button>
							</td>
						</tr>
						<tr height="30">
							<td align="center" style="color: blue;">${message}</td>
						</tr>
					</table>
				</form>
			</div>

		</div>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

	<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>