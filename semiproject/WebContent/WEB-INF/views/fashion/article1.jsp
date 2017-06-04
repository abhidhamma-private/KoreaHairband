<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>
<script type="text/javascript">


function openLayer(targetID, options){
	var $layer = $('#'+targetID);
	var $close = $layer.find('.close');
	var width = $layer.outerWidth();
	var ypos = options.top;
	var xpos = options.left;
	var marginLeft = 0;
	
	if(xpos==undefined){
		xpos = '50%';
		marginLeft = -(width/2);
	}

	if(!$layer.is(':visible')){
		$layer.css({'top':ypos+'px','left':xpos,'margin-left':marginLeft})
			.show();
	}

	$close.bind('click',function(){
		if($layer.is(':visible')){
			$layer.hide();
		}
		return false;
	});
}
function deleteBoard(bbs_num)
{
	<c:if test="${sessionScope.member.mem_Id == 'admin' || sessionScope.member.mem_Id == dto1.mem_Id}">
		var page = "${page}";
		var query = "bbs_num=" + bbs_num + "&${query}";
	
		if(confirm("게시물을 삭제 하시겠습니까 ?"))
		{
			var url="<%=cp%>/fashion/delete1.do?" + query;
			location.href=url;
		}
	</c:if>
	
	<c:if test="${sessionScope.member.mem_Id != 'admin' && sessionScope.member.mem_Id != dto1.mem_Id}">
		alert("게시물을 삭제할 수 없습니다!!!!!!!");
	</c:if>
}


function updateBoard(bbs_num)
{
	<c:if test="${sessionScope.member.mem_Id == dto1.mem_Id}">
		var page = "${page}";
		var query = "bbs_num=" + bbs_num + "&${query}";
		var url="<%=cp%>/fashion/update1.do?" + query;
		
		location.href=url;
	</c:if>
	
	<c:if test="${sessionScope.member.mem_Id != dto1.mem_Id}">
		alert("게시물을 수정할 수 없습니다!!!!!!!");
	</c:if>
}

//댓글
function login() {
	location.href="<%=cp%>/member/login.do";
}

//댓글 리스트
$(function(){
	listPage(1);
});

function listPage(page) {
	var url="<%=cp%>/fashion/listReply.do";
	var bbs_num="${dto1.bbs_num}";
	$.post(url, {bbs_num:bbs_num, pageNo:page}, function(data){
		$("#listReply").html(data);
	});
}

//댓글 추가
function sendReply() {
	var uid="${sessionScope.member.mem_Id}";
	if(! uid) {
		login();
		return false;
	}

	var bbs_num="${dto1.bbs_num}"; // 해당 게시물 번호
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
		,url:"<%=cp%>/fashion/insertReply.do"
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
<style type="text/css">
	.layer-popup {display:none; position:absolute; left:50%; top:175px; z-index:10; padding:30px 30px 35px; margin-left:-235px; background-color:#fff; border:1px solid #000;}
	#modal {display:none;background-color:#FFFFFF;position:absolute;top:300px;left:200px;padding:10px;border:2px solid #E2E2E2;z-Index:9999}
	.td_cont img{max-width:900px}
</style>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="f_container">

<table style="width: 900px; margin: 30px auto 0px; border-spacing: 0px;">
<tr height="45">
	<td align="left" class="title">
		<h3><a href ="<%=cp%>/fashion/list1.do">패션-중고거래게시판</a></h3>
	</td>
</tr>
</table>

		<table style="width:900px; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
		
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/fashion/list1.do?${query}';">리스트</button>
			    </td>
			</tr>
			</table>

       <div>
			<table style="width: 900px; margin: 20px auto 0px; border-spacing: 0px;">
			<tr><td colspan="3" height="1" bgcolor="#cccccc"></td></tr>
			
			<tr height="50">
			    <td width="15%" align="left" style="padding-left: 5px;">
			       [${dto1.category }]
			    </td>
			    <td width="55%" align="left" style="padding-left: 5px;">
			     ${dto1.subject }
			    </td>
			    
			       <td width="30%" align="right" style="padding-right: 5px;">
			      | 조회수 : ${dto1.hitCount }
			    </td>
			    
			   
			</tr>
			
			<tr><td colspan="3" height="1" bgcolor="#cccccc" ></td></tr>
			
			<tr height="25">
			    <td align="left" style="padding-left: 5px;">
			       작성자 :<a href="#" class="cbtn" onclick="openLayer('poster',{top:350, left:120});return false;">${dto1.mem_Name }</a>
			    </td>
			    <td colspan="2" align="right" style="padding-right: 5px;">
			   		     ${dto1.created }
			    </td>
			</tr>
			<tr>
			  <td class="td_cont" colspan="3" align="left" style="padding: 10px 5px;" valign="top" height="200">
			  ${dto1.content }
			   </td>
			</tr>
			
			<tr><td colspan="3" height="1" bgcolor="#cccccc"></td></tr>
			
		<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			      	이전글 : 
					 <c:if test="${not empty predto1}">
			      	 	<a href="<%=cp%>/fashion/article1.do?${query}&bbs_num=${predto1.bbs_num }">${predto1.subject }</a>
			    	 </c:if>
			    	 
			    	 <c:if test="${empty predto1}">
			    	 	이전글이 없습니다.
			    	 </c:if>

			    </td>
			</tr>
			<tr><td colspan="3" height="1" bgcolor="#cccccc"></td></tr>
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			     	다음글 : 
					 <c:if test="${not empty nextdto1}">
			      	 	<a href="<%=cp%>/fashion/article1.do?${query}&bbs_num=${nextdto1.bbs_num }">${nextdto1.subject }</a>
			    	 </c:if>
			    	 
			    	 <c:if test="${empty nextdto1}">
			    	 	다음글이 없습니다.
			    	 </c:if>
			    </td>
			</tr>
			<tr><td colspan="3" height="1" bgcolor="#cccccc"></td></tr>
			</table>
			</div>
			
		<div id="poster" class="layer-popup">
			<button id="button">쪽지보내기</button><br>
			<a href="#" class="close">X</a>
		</div>
			
			<table style="width:900px; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="right">
			    	
			    
			    	<c:if test="${sessionScope.member.mem_Id == dto1.mem_Id }">
			         	<button type="button" class="btn" onclick="updateBoard('${dto1.bbs_num}');">수정</button>
			        </c:if>
			          	
			        <c:if test="${sessionScope.member.mem_Id == dto1.mem_Id || sessionScope.member.mem_Id == 'admin' }">  	
			          	<button type="button" class="btn" onclick="deleteBoard('${dto1.bbs_num}');">삭제</button>
			    	</c:if>
			    </td>
			
			   
			</tr>
			</table>
			
		<div  style="padding-top: 20px; width: 900px;  margin: 20px auto 0px;" >

	</div>
</div>

  <div class="bbs-reply">
	           <div class="bbs-reply-write">
	               <div style="clear: both;">
	           	       <div style="float: left;"><span style="font-weight: bold;">댓글쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가 주세요.</span></div>
	           	       <div style="float: right; text-align: right;"></div>
	               </div>
	               <div style="clear: both; padding-top: 10px;">
	                   <textarea id="content" class="form-control" rows="3" style=" resize:none; width: 700px;"></textarea>
	               </div>
	               <div style="text-align: right; padding-top: 10px;">
	                   <button type="button" class="btn btn-primary btn-sm" onclick="sendReply();"> 댓글등록 <span class="glyphicon glyphicon-ok"></span></button>
	               </div>           
	           </div>
	       
	        <div id="listReply"></div> 
	       </div>




<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>	