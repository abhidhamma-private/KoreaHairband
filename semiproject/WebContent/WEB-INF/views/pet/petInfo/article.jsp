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
<title>동물자유게시판 - ${dto.subject}</title>
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
/* 레이어를 닫아준다. */
function closeLayer( obj ) {
	$(obj).parent().hide();
}

$(function(){
	/* 클릭 클릭시 클릭을 클릭한 위치 근처에 레이어가 나타난다. */
	$('.popupSelect').click(function(e)
	{
		var mem_Id = $(this).attr("data-id"); //값을 받아오는 태그
		
		$("#popupID").html(mem_Id);
		<%-- $("#popupInfo").attr("href", "<%=cp%>/member/infopage.do") --%>

		var sWidth = window.innerWidth;
		var sHeight = window.innerHeight;

		var oWidth = $('.popupLayer').width();
		var oHeight = $('.popupLayer').height();

		// 레이어가 나타날 위치를 셋팅한다.
		var divLeft = e.clientX - 470;
		var divTop = e.clientY;

		// 레이어가 화면 크기를 벗어나면 위치를 바꾸어 배치한다.
		if( divLeft + oWidth > sWidth ) divLeft -= oWidth;
		if( divTop + oHeight > sHeight ) divTop -= oHeight;

		// 레이어 위치를 바꾸었더니 상단기준점(0,0) 밖으로 벗어난다면 상단기준점(0,0)에 배치하자.
		if( divLeft < 0 ) divLeft = 0;
		if( divTop < 0 ) divTop = 0;

		$('.popupLayer').css({
			"top": divTop,
			"left": divLeft,
			"position": "absolute"
		}).show();
	});

});

function moveinfo(){
    var f=document.popup;  //폼 name
    var mem_Id = $('#popupID').html();
    f.mem_Id1.value = mem_Id;  //POST방식으로 넘기고 싶은 값
    f.action="<%=cp%>/member/infopage.do";  //이동할 페이지
    f.method="post";  //POST방식
    f.submit();
}

function movemsg(){
    var f=document.popup;  //폼 name
    var mem_Id = $('#popupID').html();
    var returnpage = "/pet/petInfo/list.do";
    f.mem_Id1.value = mem_Id;  //POST방식으로 넘기고 싶은  
    f.returnpage.value = returnpage;  //POST방식으로 넘기고 싶은  
    f.action="<%=cp%>/message/m_created.do?page="+${page};  //이동할 페이지
    f.method="post";  //POST방식
    f.submit();
}

</script>
<style type="text/css">
.td_cont img{max-width:900px}

.popupSelect {
	cursor: pointer;
}

.popupLayer {
	cursor: pointer;
	position: absolute;
	display: none;
	background-color: #ffffff;
	border: solid 2px #d0d0d0;
	width: 100px;
	height: 100px;
	padding: 10px;
}
</style>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 900px;">
        <div class="body-title">
        </div>
        
        <form name="popup" method="post">
		<div class="popupLayer">
			<span id="popupID">ID가 나타날곳</span><br>
			<hr>
			<a href="javascript:void(0);" onclick="moveinfo();" id="popupInfo"><span id="popupInfo">회원 정보</span></a><br>
			<a href="javascript:void(0);" onclick="movemsg();"  id="popupMsg"><span id="popupMsg">쪽지 보내기</span></a><br>
			<span onClick="closeLayer(this)" style="cursor:pointer;font-size:1.5em" title="닫기">X</span>
		</div>
		<input type="hidden" name="mem_Id1" value="">
		<input type="hidden" name="returnpage" value="">
		</form>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center">
				    ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       이름 : <a class="popupSelect" data-id="${dto.mem_id}">${dto.mem_name}</a>
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created} | 조회 ${dto.hitCount}
			    </td>
			    
			</tr>
			
			<tr>
			  <td class="td_cont" colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center" style="padding-left: 5px;">
			      <button type="button" class="btn btn-default btn-sm wbtn" style="background: white; border: 0px;" onclick="updateLike('${dto.bbs_num}')"><img style="display:block; margin-bottom:5px;" src="<%=cp%>/resource/img/like.png"><span style="display:block" id="countLike">${countLike}</span></button>
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       이전글 :
			         <c:if test="${not empty preRead}">
			              <a href="<%=cp%>/pet/petInfo/article.do?${query}&bbs_num=${preRead.bbs_num}">${preRead.subject}</a>
			        </c:if>
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			    다음글 :
			         <c:if test="${not empty nextRead}">
			              <a href="<%=cp%>/pet/petInfo/article.do?${query}&bbs_num=${nextRead.bbs_num}">${nextRead.subject}</a>
			        </c:if>
			    </td>
			</tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="left">
			       <c:if test="${sessionScope.member.mem_Id==dto.mem_id}">				    
			          <button type="button" class="btn" onclick="updateBoard();">수정</button>
			       </c:if>
			       <c:if test="${sessionScope.member.mem_Id==dto.mem_id || sessionScope.member.mem_Id=='admin'}">				    
			          <button type="button" class="btn" onclick="deleteBoard();">삭제</button>
			       </c:if>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/pet/petInfo/list.do?${query}';">리스트</button>
			    </td>
			</tr>
			</table>
        </div>
		<div class="bbs-reply">
	           <div class="bbs-reply-write">
	               <div style="clear: both;">
	           	       <div style="float: left;"><span style="font-weight: bold;">Comment</span><span> - 댓글 작성</span></div>
	           	       <div style="float: right; text-align: right;"></div>
	               </div>
	               <div style="clear: both; padding-top: 10px;">
	                   <textarea id="content" class="form-control" rows="5" style="width: 900px"></textarea>
	               </div>
	               <div style="text-align: right; padding-top: 10px;">
	                   <button type="button" class="btn btn-primary btn-sm" onclick="sendReply();"> 댓글등록 <span class="glyphicon glyphicon-ok"></span></button>
	               </div>           
	           </div>
	       
	           <div id="listReply"></div>
	       </div>
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
