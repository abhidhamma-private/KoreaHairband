<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 작성중...</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />

<!-- 아래 2개 인클루드 -->
<script type="text/javascript" src="<%=cp%>/resource/editor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=cp%>/resource/editor/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
function sendOk(){
	var f = document.noticeForm;
	
	var str = f.subject.value;
    if(!str) {
        alert("제목을 입력하세요. ");
        f.subject.focus();
        return;
    }

	var mode="${mode}";
	if(mode=="created")
		f.action="<%=cp%>/notice/created_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/notice/update_ok.do";

    f.submit();
}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>




<div class="f_container">

        <div>
			<form name="noticeForm" id="boardForm" method="post">
			  <table style="width: 900px; margin: 20px auto 0px; border-spacing: 0px;">
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			  <tr align="left" height="40"> 
			      <td width="100" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject}">
			      </td>
			  </tr>
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			  <tr align="left" height="40"> 
			      <td width="100" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			       	${sessionScope.member.mem_Name}
			      </td>
			  </tr> 
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
	
			  <tr align="left"> 
			      <td width="100" style="text-align: center; padding-top:5px;" valign="top">설&nbsp;&nbsp;&nbsp;&nbsp;명</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			          <textarea id="content" name="content" rows="12" class="boxTA" style="width: 95%;">${dto.content}</textarea>
			      </td>
			  </tr>
			
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			  </table>
			
			  <table style="width:900px; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" id="save" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/notice/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>

					<c:if test="${mode=='update'}">
					<input type="hidden" name="not_num" value="${dto.not_num}">
					<input type="hidden" name="page" value="${page}">
					</c:if>

				  </td>
			    </tr>
			  </table>
			</form>
        </div>


</div>


<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>	