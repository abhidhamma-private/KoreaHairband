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

function countLike(bbs_num) {
	var url="<%=cp%>/pet/petInfo/pet_likecnt.do";
	$.post(url, {bbs_num:bbs_num}, function(data){
		var count=data.countLike;
		
		$("#countLike").html(count);
	}, "json");
}

function updateLike(bbs_num) {
	var uid="${sessionScope.member.mem_Id}";
	if(! uid) {
		login();
		return;
	}

	var query="bbs_num="+bbs_num;

	$.ajax({
		type:"post"
		,url:"<%=cp%>/pet/petInfo/pet_like.do"
		,data:query
		,dataType:"json"
		,success:function(data) {
			var state=data.state;
			if(state=="true") {
				countLike(bbs_num);
			} else if(state=="false") {
				alert("좋아요는 한번만 가능합니다.");
			} else if(state=="loginFail") {
				login();
			}
		}
		,error:function(e) {
			console.log(e.responseText);
		}
	});
}

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
function login() {
	location.href="<%=cp%>/member/login.do";
}

//댓글 리스트
$(function(){
	listPage(1);
});
function listPage(page) {
	var url="<%=cp%>/pet/petInfo/listReply.do";
	var bbs_num="${dto.bbs_num}";
	$.post(url, {bbs_num:bbs_num, pageNo:page}, function(data){
		$("#listReply").html(data);
	});
}
function sendReply(){
	var uid="${sessionScope.member.mem_Id}";
	if(! uid) {
		login();
		return false;
	}

	var bbs_num="${dto.bbs_num}"; // 해당 게시물 번호
	var content=$.trim($("#content").val());
	if(! content ) {
		alert("내용을 입력하세요 !!! ");
		$("#content").focus();
		return false;
	}
	
	var query="bbs_num="+bbs_num;
	query+="&content="+content;
	// query+="&answer=0";
	
	$.ajax({
		type:"post"
		,url:"<%=cp%>/pet/petInfo/insertReply.do"
		,data:query
		,dataType:"json"
		,success:function(data) {
			$("#content").val("");
			
			var state=data.state;
			if(state=="true") {
				listPage(1);
			} else if(state=="false") {
				alert("댓글을 등록하지 못했습니다. !!!");
			} else if(state=="loginFail") {
				login();
			}
		}
		,error:function(e) {
			console.log(e.responseText);
		}
	});	
}

//댓글 삭제
function deleteReply(reply_num, page) {
	var uid="${sessionScope.member.mem_Id}";
	if(! uid) {
		login();
		return false;
	}
	
	if(confirm("게시물을 삭제하시겠습니까 ? ")) {	
		var url="<%=cp%>/pet/petInfo/deleteReply.do";
		$.post(url, {reply_num:reply_num}, function(data){
		        var state=data.state;

				if(state=="loginFail") {
					login();
				} else {
					listPage(page);
				}
		}, "json");
	}
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
				<tr height="35" style="border-bottom: 1px solid #cccccc;">
					<td colspan="2" align="center" style="padding-left: 5px;">

						<button type="button" class="btn btn-default btn-sm wbtn"
							style="background: white; border: 0px;"
							onclick="updateLike('${dto.bbs_num}')">
							<img style="display: block; margin-bottom: 5px;"
								src="<%=cp%>/resource/img/like.png"><span
								style="display: block" id="countLike">${countLike}</span>
						</button>
					</td>
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
							전체목록</button>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="bbs-reply"
		style="width: 780px; margin: 20px auto 0px; border-spacing: 0px;">
		<div class="bbs-reply-write" style="padding-left: 30px">
			<div style="clear: both;">
				<div style="float: left;">
					<span style="font-weight: bold;">댓글쓰기</span> <span>- 타인을
						비방하거나 개인정보를 유출하는 글의 게시를 삼가 주세요.</span>
				</div>
				<div style="float: right; text-align: right;"></div>
			</div>
			<div style="clear: both; padding-top: 10px;">
				<textarea id="content" class="form-control" rows="3"
					style="resize: none; width: 700px;"></textarea>
			</div>
			<div style="text-align: right; padding-top: 10px;">
				<button type="button" class="btn btn-primary btn-sm"
					onclick="sendReply();">
					댓글등록 <span class="glyphicon glyphicon-ok"></span>
				</button>
			</div>
		</div>

		<div id="listReply"></div>
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
