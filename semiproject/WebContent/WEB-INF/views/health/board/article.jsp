<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/health/setting.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/slider.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript">
function deleteBoard(num) {
   if(confirm("게시물을 삭제 하시겠습니까 ?")) {
    	 
    		 var url="<%=cp%>/health/deleteB.do?num="+num+"&page=${page}";
        	 location.href=url;	  	 
    }	
 
}

function updateBoard(num) {
		var url="<%=cp%>/health/updateB.do?num="+num+"&page=${page}";
	    location.href=url;
}

//좋아요

//게시물 좋아요 개수
function countLikeBoard(num) {
	var url="<%=cp%>/health/countLikeBoard.do";
	$.post(url, {num:num}, function(data){
		var count=data.countLikeBoard;
		
		$("#countLikeBoard").html(count);
	}, "json");
}

//게시물 좋아요 추가
function login() {
	location.href="<%=cp%>/member/login.do";
}

function sendLikeBoard(num) {
	var uid="${sessionScope.member.mem_Id}";
	if(! uid) {
		login();
		return;
	}

	msg="게시물에 공감하십니까 ?";
	if(! confirm(msg))
		return;
	
	var query="num="+num;

	$.ajax({
		type:"post"
		,url:"<%=cp%>/health/insertLikeBoard.do"
		,data:query
		,dataType:"json"
		,success:function(data) {
			var state=data.state;
			if(state=="true") {
				countLikeBoard(num);
			} else if(state=="false") {
				alert("좋아요는 한번만 가능합니다. !!!");
			} else if(state=="loginFail") {
				login();
			}
		}
		,error:function(e) {
			console.log(e.responseText);
		}
	});
}
///////////////////////////////////댓글
// 댓글
function login() {
	location.href="<%=cp%>/member/login.do";
}

// 댓글 리스트
$(function(){
	listPage(1);
});

function listPage(page) {
	var url="<%=cp%>/health/listReply.do";
	var num="${dto.bbs_Num}";
	$.post(url, {num:num, pageNo:page}, function(data){
		$("#listReply").html(data);
	});
}

// 댓글 추가
function sendReply() {
	var uid="${sessionScope.member.mem_Id}";
	if(! uid) {
		login();
		return false;
	}

	var num="${dto.bbs_Num}"; // 해당 게시물 번호
	var content=$.trim($("#content").val());
	if(! content ) {
		alert("내용을 입력하세요 !!! ");
		$("#content").focus();
		return false;
	}
	
	var query="num="+num;
	query+="&content="+content;
	// query+="&answer=0";
	
	$.ajax({
		type:"post"
		,url:"<%=cp%>/health/insertReply.do"
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

</script>

</head>	
<body>
<!-- header -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>	
	    <div class="table-responsive" style="clear: both; position:relative;">
	        <div class="article">
	            <table class="table" style="width: 900px; height: 600px; margin: 0 auto;">
	                 <thead>
	                     <tr>
	                         <th colspan="2" style="text-align:center;">
	                                 	${dto.subject }
	                         </th>
	                     </tr>
	                <thead>
	                 <tbody>
	                     <tr>
	                         <td style="text-align: left;">
	                             	${sessionScope.member.mem_Name}
	                         </td>
	                         <td style="text-align: right;">
	                          	날짜 ${dto.created}/조회 ${dto.hitCount }/좋아요
	                         </td>
	                     </tr>
                         <tr style="border-bottom:none;">
                             <td colspan="2">
                                
                             <c:if test="${dto.savefilename!='0'}">
                                 <img src="<%=cp%>/uploads/photo/${dto.savefilename}" alt="이미지" style="max-width:100%; height:auto; resize:both;">
                             </c:if>
                             	
                             </td>
                         </tr>
	                     <tr>
	                         <td colspan="2" style="min-height: 30px;">
	                              	<pre>${dto.content}</pre>
	                         </td>
	                     </tr>
	                     <tr>
	                         <td colspan="2" style="min-height: 30px; text-align:center;" >
	                         		
	                         		
	                         		
	                         		<button class="btn" onclick="sendLikeBoard('${dto.bbs_Num}')">
	                         		<span class="glyphicon glyphicon-star"></span><br>
	                         		<c:if test="${empty countLikeBoard} ">
	                         			<span id="countLikeBoard">0</span>
	                         		</c:if>
	                         		
	                         		<span id="countLikeBoard">${countLikeBoard}</span>
	                       				
	                         		</button>
	                         </td>
	                     </tr>
	                </tbody>
	                <tfoot>
	                	<tr>
	                		<td>	                		
	                		        <button type="button" class="btn" onclick="updateBoard(${dto.bbs_Num});">수정</button>
	                		        <button type="button" class="btn" onclick="deleteBoard(${dto.bbs_Num});">삭제</button>
	                		        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/reply.do?num=${dto.bbs_Num}&page=${page}'">답글</button>
	                		        <br><br>
	                		        
	          
	          
	                		</td>
	                		<td align="right">
	                		    <button type="button" class="btn"
	                		                onclick="javascript:location.href='<%=cp%>/health/board.do'"> 목록으로 </button>
	                		</td>
	                	</tr>
	                </tfoot>
	            </table>
	            <!-- <div class="bbs-reply" style="width:900px; margin:0px auto;">
	           <div class="bbs-reply-write">
	               <div style="clear: both;">
	           	       <div style="float: left;"><span style="font-weight: bold;">댓글쓰기</span><span> 댓글을 쓰시오 당장 당장!</span></div>
	           	       <div style="float: right; text-align: right;"></div>
	               </div>
	               <div style="clear: both; padding-top: 10px;">
	                   <textarea id="content" class="form-control" rows="3"></textarea>
	               </div>
	               <div style="text-align: right; padding-top: 10px;">
	                   <button type="button" class="btn btn-primary btn-sm" onclick="sendReply();"> 댓글등록 <span class="glyphicon glyphicon-ok"></span></button>
	               </div>           
	           </div>
	       
	           <div id="listReply"></div>
	       		</div> --><br>
	       </div>
	   </div>
	   
	   
	   
<!-- footer -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>
</body>
</html>
