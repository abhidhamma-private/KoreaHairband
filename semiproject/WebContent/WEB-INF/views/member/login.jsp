<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>

<style type="text/css">
.lbl {
   position:absolute; 
   margin-left:15px; margin-top: 17px;
   color: #999999; font-size: 11pt;
}
.loginTF {
  width: 340px; height: 35px;
  padding: 5px;
  padding-left: 15px;
  border:1px solid #999999;
  color:#333333;
  margin-top:5px; margin-bottom:5px;
  font-size:14px;
  border-radius:4px;
}
</style>

<script type="text/javascript">
function bgLabel(ob, id) {
    if(!ob.value) {
	    document.getElementById(id).style.display="";
    } else {
	    document.getElementById(id).style.display="none";
    }
}

function sendLogin() {
    var f = document.loginForm;

	var str = f.mem_Id.value;
    if(!str) {
        alert("아이디를 입력하세요. ");
        f.mem_Id.focus();
        return;
    }

    str = f.mem_Pwd.value;
    if(!str) {
        alert("패스워드를 입력하세요. ");
        f.mem_Pwd.focus();
        return;
    }

    f.action = "<%=cp%>/member/login_ok.do";
    f.submit();
}
</script>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
<div style="width: 900px; margin: 0 auto;">
	<div>

	    <div style="margin: 80px auto 70px; width:360px;">
	    	<div style="text-align: center;">
	        	<!-- 로고 이미지 -->
				<img src="<%=cp%>/resource/img/login.png" style="margin: 10px"><br>
	        </div>
	        
			<form name="loginForm" method="post" action="">
			  <table style="margin: 15px auto; width: 360px; border-spacing: 0px;">
			  <tr align="center" height="60"> 
			      <td> 
	                <label for="mem_Id" id="lblMemId" class="lbl" >아이디</label>
			        <input type="text" name="mem_Id" id="mem_Id" class="loginTF" maxlength="15"
			                   tabindex="1"
	                           onfocus="document.getElementById('lblMemId').style.display='none';"
	                           onblur="bgLabel(this, 'lblMemId');">
			      </td>
			  </tr>
			  <tr align="center" height="60"> 
			      <td>
			        <label for="mem_Pwd" id="lblMemPwd" class="lbl" >패스워드</label>
			        <input type="password" name="mem_Pwd" id="mem_Pwd" class="loginTF" maxlength="20" 
			                   tabindex="2"
	                           onfocus="document.getElementById('lblMemPwd').style.display='none';"
	                           onblur="bgLabel(this, 'lblMemPwd');">
			      </td>
			  </tr>
			  <tr align="center" height="65" > 
			      <td>
			        <button type="button" onclick="sendLogin();" class="btnConfirm">로그인</button>
			      </td>
			  </tr>

			  <tr align="center" height="45">
			      <td>
			       		<a href="<%=cp%>/">아이디찾기</a>&nbsp;&nbsp;&nbsp;
			       		<a href="<%=cp%>/">패스워드찾기</a>&nbsp;&nbsp;&nbsp;
			       		<a href="<%=cp%>/member/member.do">회원가입</a>
			      </td>
			  </tr>
			  
			  <tr align="center" height="40" >
			    	<td><span style="color: blue;">${message}</span></td>
			  </tr>
			  
			  </table>
			</form>           
		</div>

	</div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>