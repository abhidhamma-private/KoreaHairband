﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
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

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
	function deletePhoto(num)
	{
		<c:if test="${sessionScope.member.userId=='admin' || sessionScope.member.userId==dto.userId}">
		    if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		    	 var url="<%=cp%>/photo/delete.do?num="+num+"&page=${page}";
		    	 location.href=url;
		    }	
		</c:if>    
		<c:if test="${sessionScope.member.userId!='admin' && sessionScope.member.userId!=dto.userId}">
		    alert("게시물을 삭제할 수  없습니다.");
		</c:if>
	}
	
	function updatePhoto(num)
	{
		<c:if test="${sessionScope.member.userId==dto.userId}">
		    var url="<%=cp%>/photo/update.do?num="+num+"&page=${page}";
		    location.href=url;
		</c:if>
		
		<c:if test="${sessionScope.member.userId!=dto.userId}">
		   alert("게시물을 수정할 수  없습니다.");
		</c:if>
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
            <h3><span style="font-family: Webdings">2</span> Photo Gallery </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			<tr height="35">
			    <td colspan="2" align="center">
				   	${dto.subject }
			    </td>
			</tr>
			<tr><td colspan="2" height="1" bgcolor="#cccccc" ></td></tr>
			
			<tr height="35">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       	이름 : ${dto.userName }
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created }
			    </td>
			</tr>
			<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			<tr>
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      	<img src="<%=cp%>/uploads/photo/${dto.imageFilename}" width="377" height="377"><br>
			      	${dto.content }<br>
			   </td>
			</tr>
			<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			<tr height="35">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			      	 이전글 :
			        <c:if test="${not empty predto}">
			      	 	<a href="<%=cp%>/photo/article.do?page=${page }&num=${predto.num }">${predto.subject }</a>
			    	</c:if>
			    	 
			    	<c:if test="${empty predto}">
			    	 	이전글이 없습니다.
			    	</c:if>
			    </td>
			</tr>
			<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			<tr height="35">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       	다음글 :
			       	<c:if test="${not empty nextdto}">
			      	 	<a href="<%=cp%>/photo/article.do?page=${page }&num=${nextdto.num }">${nextdto.subject }</a>
			    	</c:if>
			    	 
			    	<c:if test="${empty nextdto}">
			    	 	다음글이 없습니다.
			    	</c:if>
			    </td>
			</tr>
			<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="left">
			       <c:if test="${sessionScope.member.userId==dto.userId}">				    
			          <button type="button" class="btn" onclick="updatePhoto('${dto.num}');">수정</button>
			       </c:if>
			       <c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">				    
			          <button type="button" class="btn" onclick="deletePhoto('${dto.num}');">삭제</button>
			       </c:if>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/photo/list.do?page=${page}';">리스트</button>
			    </td>
			</tr>
			</table>
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