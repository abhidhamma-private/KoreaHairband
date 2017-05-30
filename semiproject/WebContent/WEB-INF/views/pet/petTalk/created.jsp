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
<title> Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/slider.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>




<div class="f_container">

<table style="width: 780px; margin: 30px auto 0px; border-spacing: 0px;">
<tr height="45">
	<td align="left" class="title">
		<h3>패션-중고거래게시판</h3>
	</td>
</tr>
</table>
  
        <div>
			<form name="boardForm" id="boardForm" method="post" enctype="multipart/form-data">
			  <table style="width: 780px; margin: 20px auto 0px; border-spacing: 0px;">
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
			        <!--  ${sessionScope.member.userName} --> 
			      </td>
			  </tr> 
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			  
			    <tr align="left" height="40"> 
			      <td width="100" style="text-align: center;">카테고리</td>
			      <td style="padding-left:10px;"> 
			       <select name="catagoryKey" class="selectField">
                 	 <option value="sell">팝니다</option>
                 	 <option value="buy">삽니다</option>
          		  </select>
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
			
			  <table style="width:780px; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" id="save" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/photo/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>

					<c:if test="${mode=='update'}">
					<!-- 히든으로 넘겨주는값들 ==>multipartRequest객체로받아야함-->
					<input type="hidden" name="num" value="${dto.num}">
					<input type="hidden" name="page" value="${page}">
					<input type="hidden" name="imageFilename" value="${dto.imageFilename}">
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