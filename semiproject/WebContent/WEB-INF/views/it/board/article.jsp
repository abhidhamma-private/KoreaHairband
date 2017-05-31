<%@ page contentType="text/html; charset=UTF-8" %>
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

<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">
function deleteBoard() {
<c:if test="${sessionScope.member.mem_Id=='admin' || sessionScope.member.mem_Id==dto.mem_Id}">
    var bbs_num = "${dto.bbs_num}";
    var page = "${page}";
    var query = "bbs_num="+bbs_num+"&page="+page;
    var url = "<%=cp%>/it/board_delete.do?" + query;

    if(confirm("위 자료를 삭제 하시 겠습니까 ? "))
    	location.href=url;
</c:if>    
<c:if test="${sessionScope.member.mem_Id!='admin' && sessionScope.member.mem_Id!=dto.mem_Id}">
    alert("게시물을 삭제할 수  없습니다.");
</c:if>
}
function likeBoard() {
<c:if test="${sessionScope.member.mem_Id==dto.mem_Id}">
    alert("자신의 게시물에는 좋아요를 할 수 없습니다.");
</c:if>
<c:if test="${sessionScope.member.mem_Id!=dto.mem_Id}">
    var bbs_num = "${dto.bbs_num}";
    var page = "${page}";
    var query = "bbs_num="+bbs_num+"&page="+page;
    var url = "<%=cp%>/it/board_like.do?" + query;
    
    location.href=url;
</c:if>
}
function updateBoard() {
<c:if test="${sessionScope.member.mem_Id==dto.mem_Id}">
    var bbs_num = "${dto.bbs_num}";
    var page = "${page}";
    var query = "bbs_num="+bbs_num+"&page="+page;
    var url = "<%=cp%>/it/board_update.do?" + query;

    location.href=url;
</c:if>

<c:if test="${sessionScope.member.mem_Id!=dto.mem_Id}">
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
    <div class="body-container" style="width: 900px;">
        <div class="body-title">
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center">
			        <c:if test="${dto.depth!=0}">
			              [답변]
			        </c:if>
				    ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       이름 : ${dto.mem_Name}
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created} | 조회 ${dto.hitCount}
			    </td>
			</tr>
			
			<tr>
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center" style="padding-left: 5px;">
			       <a href="javascript:likeBoard()"><img src="<%=cp%>/resource/img/like.png"></a><br>
			        		${dto.like}개<br><br>
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       이전글 :
			         <c:if test="${not empty preReadDto}">
			              <a href="<%=cp%>/it/board_article.do?${query}&bbs_num=${preReadDto.bbs_num}">${preReadDto.subject}</a>
			        </c:if>
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			    다음글 :
			         <c:if test="${not empty nextReadDto}">
			              <a href="<%=cp%>/it/board_article.do?${query}&bbs_num=${nextReadDto.bbs_num}">${nextReadDto.subject}</a>
			        </c:if>
			    </td>
			</tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="left">
			       <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/it/board_reply.do?bbs_num=${dto.bbs_num}&page=${page}';">답변</button>
			       <c:if test="${sessionScope.member.mem_Id==dto.mem_Id}">				    
			          <button type="button" class="btn" onclick="updateBoard();">수정</button>
			       </c:if>
			       <c:if test="${sessionScope.member.mem_Id==dto.mem_Id || sessionScope.member.mem_Id=='admin'}">				    
			          <button type="button" class="btn" onclick="deleteBoard();">삭제</button>
			       </c:if>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/it/board.do?${query}';">리스트</button>
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