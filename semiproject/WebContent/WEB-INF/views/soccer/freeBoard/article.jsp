<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>
		
		<script type="text/javascript">
			//댓글
			function login() {
				location.href="<%=cp%>/member/login.do";
			}
			
			//댓글 리스트
			$(function() {
				listPage(1);
			});
			
			function listPage(page)
			{
				var url = "<%= cp %>/soccer/board/reply.do";
				var bbs_num = "${dto.bbs_num }";
				
				$.post(url, {bbs_num:bbs_num, pageNo:page}, function(data) {
					$("#listReply").html(data);
				});
			}
			
			//댓글 추가
			function sendReply()
			{
				var mid = "${sessionScope.member.mem_Id}";
				
				if(! mid)
				{
					login();
					return false;
				}
			
				var bbs_num = "${dto.bbs_num }"; 	// 해당 게시물 번호
				var content = $.trim($("#content").val());
				
				if(! content)
				{
					alert("내용을 입력하세요 !!! ");
					$("#content").focus();
					return false;
				}
				
				var query = "bbs_num=" + bbs_num;
				query += "&content=" + content;
				// query+="&answer=0";
				
				$.ajax({
					type : "post"
					,url : "<%= cp %>/soccer/board/reply_ok.do"
					,data : query
					,dataType : "json"
					
					,success : function(data) {
						$("#content").val("");
						
						var state = data.state;
						
						if(state == "true")
						{
							listPage(1);
						}
						
						else if(state == "false")
						{
							alert("댓글 등록 실패!!!");
						}
						
						else if(state == "loginFail")
						{
							login();
						}
					}
					
					,error:function(e) {
						console.log(e.responseText);
					}
				});
			}
			
			function deleteReply(reply_num, pageNo)
			{
				var mid = "${sessionScope.member.mem_Id}";
				
				if(! mid)
				{
					login();
					return;
				}
				
				if(confirm("위 자료를 삭제 하시 겠습니까 ?"))
				{
					var url = "<%=cp%>/soccer/board/deleteReply.do";
		
					$.post(url, {reply_num:reply_num, pageNo:pageNo}, function(data) {
						var state = data.state;
						
						if(state == "loginFail")
							login();
						
						else if(state == "true")
							listPage(pageNo);
						
					}, "json");
				}
				
				else
					return;
			}
		</script>
		
		<script type="text/javascript">
			function deleteSCBoard(bbs_num)
			{
				<c:if test="${sessionScope.member.mem_Id == 'admin' || sessionScope.member.mem_Id == dto.mem_Id}">
				    if(confirm("게시물을 삭제하시겠습니까 ?"))
				    {
				    	 var url = "<%= cp %>/soccer/board/delete.do?bbs_num=" + bbs_num + "&page=${page}";
				    	 location.href=url;
				    }
				</c:if>
				
				<c:if test="${sessionScope.member.mem_Id != 'admin' && sessionScope.member.mem_Id != dto.mem_Id}">
				    alert("게시물을 삭제할 수 없습니다!");
				</c:if>
			}
			
			function updateSCBoard(bbs_num)
			{
				<c:if test = "${sessionScope.member.mem_Id == dto.mem_Id}">
				    var url = "<%= cp %>/soccer/board/update.do?bbs_num=" + bbs_num + "&page=${page}";
				    location.href=url;
				</c:if>
				
				<c:if test="${sessionScope.member.mem_Id != dto.mem_Id}">
				   alert("게시물을 수정할 수  없습니다.");
				</c:if>
			}
		</script>
		
		<script type="text/javascript">
			function likeSCBoard(bbs_num, page)
			{
				<c:if test = "${checkLike == 0}">
					if(confirm("좋아요를 하시겠습니까 ?"))
				    {
						var url = "<%= cp %>/soccer/board/likeSCBoard.do?bbs_num=" + bbs_num + "&page=${page}";
				    	location.href=url;
				    }
				</c:if>
				
				<c:if test="${checkLike == 1}">
					if(confirm("좋아요를 취소하시겠습니까 ?"))
				    {
						var url = "<%= cp %>/soccer/board/cancelLikeSCBoard.do?bbs_num=" + bbs_num + "&page=${page}";
				    	location.href=url;
				    }
				</c:if>
			}
		</script>
	</head>
	
	<body>
		<div class="header">
		    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
		</div>
			
		<div class="container" align="center">
		    <div class="body-container" style="width: 870px;">
		        <div class="body-title">
		            <h3> SCBoard Read..... </h3>
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
						       	이름 : ${dto.mem_Name }
						    </td>
						    <td width="50%" align="right" style="padding-right: 5px;">
						       	작성일 : ${dto.created }
						    </td>
						</tr>
						<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
						<tr>
						   <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
						      	<img src="<%=cp%>/uploads/semi/${dto.saveFilename}" width="377" height="377" align="middle"><br>
						      	${dto.content }<br>
						   </td>
						</tr>
						<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
						
						<tr height="35">
						    <td colspan="2" align="left" style="padding-left: 5px;">
						      	 이전글 :
						        <c:if test="${not empty predto}">
						      	 	<a href="<%=cp%>/soccer/board/article.do?page=${page }&bbs_num=${predto.bbs_num }">${predto.subject }</a>
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
						      	 	<a href="<%=cp%>/soccer/board/article.do?page=${page }&bbs_num=${nextdto.bbs_num }">${nextdto.subject }</a>
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
						    <td width="500" align="left">
						        <c:if test="${sessionScope.member.mem_Id == dto.mem_Id}">				    
						            <button type="button" class="btn" onclick="updateSCBoard('${dto.bbs_num}');"> 게시물 수정 </button>
						        </c:if>
						       
						        <c:if test="${sessionScope.member.mem_Id == dto.mem_Id || sessionScope.member.mem_Id == 'admin'}">				    
						            <button type="button" class="btn" onclick="deleteSCBoard('${dto.bbs_num}');"> 게시물 삭제 </button>
						        </c:if>
						       
						        <button type="button" class="btn btn-default btn-sm wbtn" style="background: white; border: 0px;" onclick="likeSCBoard('${dto.bbs_num }', '${page }')"><img style="display:block; margin-bottom:5px;" src="<%=cp%>/resource/img/like.png"><span style="display:block" id="countLike">${cntLike }</span></button>
						    </td>
						
						    <td align="right">
						        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/soccer/board.do?page=${page}';">리스트</button>
						    </td>
						</tr>
					</table>
		        </div>
		    </div>
		</div><br><br><br>
		
		<div class="bbs-reply" align="center">
		    <div class="bbs-reply-write">
		    	<div style="clear: both; width: 700px;">
		     		<div align="left"><span style="font-weight: bold;">댓글 작성</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가 주세요.</span></div>
		        </div>
		        <div style="clear: both; padding-top: 10px;">
		            <textarea id="content" class="form-control" rows="3" style=" resize:none; width: 700px;"></textarea>
		        </div>
		        <div style="padding-top: 10px;">
		            <button type="button" class="btn btn-primary btn-sm" onclick="sendReply();"> 댓글 등록 <span class="glyphicon glyphicon-ok"></span></button>
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