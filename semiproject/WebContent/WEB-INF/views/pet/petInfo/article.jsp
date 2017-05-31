<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>반려동물 정보공유(${dto.subject })</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">

function updateBoard(){
	<c:if test="${sessionScope.member.mem_Id==dto.mem_id}">
			var bbs_num = "${dto.bbs_num}";
			var page = "${page}";
			var query = "bbs_num="+bbs_num+"&page="+page;
	 		var url = "<%=cp%>/pet/petInfo/update.do?" + query;
			location.href=url;
	</c:if>

	<c:if test="${sessionScope.member.mem_Id!=dto.mem_id}">
	  		alert("게시물을 수정할 수  없습니다.");
	</c:if>
}

function deleteBoard(){
	<c:if test="${sessionScope.member.mem_Id=='admin' || sessionScope.member.mem_Id==dto.mem_id}">
   		var bbs_num = "${dto.bbs_num}";
   		var page = "${page}";
   		var query = "bbs_num="+bbs_num+"&page="+page;
   		var url = "<%=cp%>/pet/petInfo/delete.do?" + query;

   		if(confirm("위 자료를 삭제 하시 겠습니까 ? "))
   	 	location.href=url;
	</c:if>    
	<c:if test="${sessionScope.member.mem_Id!='admin' && sessionScope.member.mem_Id!=dto.mem_id}">
 	   alert("게시물을 삭제할 수 없습니다.");
	</c:if>
}

</script>
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>




	<div class="f_container">

		<table
			style="width: 780px; margin: 30px auto 0px; border-spacing: 0px;">
			<tr height="45">
				<td align="left" class="title">
					<h3>반려동물 정보공유</h3>
				</td>
			</tr>
		</table>
		<div>
			<table
				style="width: 780px; margin: 20px auto 0px; border-spacing: 0px;">
				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>

				<tr height="50">
					<td width="55%" align="left" style="padding-left: 5px;">
						[${dto.category}]${dto.subject}</td>
					<td width="30%" align="right" style="padding-right: 5px;">
						${dto.created} <i></i>조회수 : ${dto.hitCount}
					</td>

				</tr>
				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>

				<tr height="25">
					<td align="left" style="padding-left: 5px;">이름 :
						${dto.mem_name}</td>
				</tr>
				<tr>
					<td colspan="3" align="left" style="padding: 10px 5px;"
						valign="top" height="200">${dto.content }</td>
				</tr>

				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>

				<tr height="35" style="border-bottom: 1px solid #cccccc;">
					<td colspan="2" align="left" style="padding-left: 5px;">이전글 :
						<c:if test="${not empty preRead }">
							<a
								href="<%=cp%>/pet/petInfo/article.do?${query}&bbs_num=${preRead.bbs_num}">${preRead.subject}</a>
						</c:if>

					</td>
				</tr>
				<tr>
					<td colspan="3" height="1" bgcolor="#cccccc"></td>
				</tr>
				<tr height="35" style="border-bottom: 1px solid #cccccc;">
					<td colspan="2" align="left" style="padding-left: 5px;">다음글 :
						<c:if test="${not empty nextRead }">
							<a
								href="<%=cp%>/pet/petInfo/article.do?${query}&bbs_num=${nextRead.bbs_num}">${nextRead.subject}</a>
						</c:if>

					</td>
				</tr>
				<tr>
					<td>
						<%-- <button type="button" class="btn"
							onclick="javascript:location.href='<%=cp%>/pet/petInfo/reply.do?bbs_num=${dto.bbs_num}&page=${page}';">답변</button> --%>
						<c:if test="${sessionScope.member.mem_Id==dto.mem_id}">
							<button type="button" class="btn" onclick="updateBoard();">수정</button>
						</c:if> <c:if
							test="${sessionScope.member.mem_Id==dto.mem_id || sessionScope.member.mem_Id=='admin'}">
							<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
						</c:if>
					</td>
					<td align="right">
						<button type="button" class="btn btn-default btn-sm wbtn"
							onclick="javascript:location.href='<%=cp%>/pet/petInfo/list.do?${query}';">
							목록으로</button>
					</td>
				</tr>
			</table>
		</div>
		<!-- 			
			
			
		<div  style="padding-top: 20px; width: 780px;  margin: 20px auto 0px;" >
		
		
           <form name="guestForm" method="post" action="">
             <div class="guest-write">
                 <div style="clear: both;">
                         <span style="font-weight: bold;">댓글쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가 주세요.</span>
                 </div>
                 <div style="clear: both; padding-top: 10px;">
                      
                       <textarea name="content" id="content" class="boxTF" rows="1" style="display:inline-block; width: 90%; margin:0px; padding: 6px 12px; box-sizing:border-box;" required="required"></textarea>
                       <button type="button" id="btnSend" class="btn" style="padding:8px 8px; display: inline-block; vertical-align: top;" > 등록 </button>
                  </div>
                  
            </div>
           </form>
         
           <div id="listGuest" style="width:100%; margin: 0px auto;"></div>
                    
        </div> -->


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
