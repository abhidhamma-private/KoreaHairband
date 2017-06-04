<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>
<script type="text/javascript">
function article(num) {
	var url="${articleUrl}&num="+num;
	location.href=url;
}

//좋아요

//게시물 좋아요 개수
function countLikeBoard(num) {
	var url="<%=cp%>/health/countLikeBoardN.do";
	$.post(url, {num:num}, function(data){
		var count="좋아요 " + data.countLikeBoard;
		
		$("#countLikeBoard"+num).html(count);
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
		,url:"<%=cp%>/health/insertLikeBoardN.do"
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
</script>
</head>

<body>

<!-- header -->
<div style="width: 900px; margin: 0px auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>
<br><br>
	<div style="width: 900px; margin: 0 auto; position:relative;">
		 <!-- <div style="width:100%; height:100px;">
			<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
				
			</div>
			
			<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
			
			</div>
		  </div> -->
		
		<!-- 로고 이미지 -->
		<img src="<%=cp%>/resource/img/m_infoboard.png" style="margin: 10px">
	
        <div class="table" style="clear: both;">
        	
	            <table class="table table-hover" id="maintable" style="width: 900px; height: 600px; margin: 0 auto;">
	                <thead>
	                    <tr>
	                        <th class="text-center" style="width: 70px;" colspan="6"></th>
	                        
	                    </tr>
	                </thead>
	                <tbody>
	                    
	                    <c:forEach var="dto" items="${list}">
	                
		                    <tr>
		                        <td class="text-center" width="20%" style="float:left" bordercolor="blue">		
										<img src="<%=cp%>/uploads/photo/${dto.savefilename}" alt="이미지없음" style="width:150px; height:150px; border:1px solid #337AB7;">	
								</td>
								
		                        <td width="80%" colspan="5">
			                        <ol class="breadcrumb" style="float:right;">
									  <li><a href="#">날짜 ${dto.created}</a></li>
									  <li>
									  
									  <a id="countLikeBoard${dto.bbs_num}" href="javascript:sendLikeBoard('${dto.bbs_num}')">좋아요 ${dto.likeCount} </a>
									  
									  </li>
									  <li class="active">조회수 ${dto.hitcount}</li>
									</ol>
									
									<div>
										<span onclick="javascript:article('${dto.bbs_num}');">${dto.subject}</span><br>
										<span onclick="javascript:article('${dto.bbs_num}');">${dto.content}</span>
									</div>
		                       	</td>
		                    </tr>
		                 </c:forEach>
		                 	
	                    
	                   
	                    
	                </tbody>
	            </table>
	        </div>
			
			<div style="clear: both; width:900px;">
	        		<div style="float: left; width: 20%; min-width: 85px;">
	        		    <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/notice.do';">전체목록</button>
	        		</div>
	        		
	        		<div style="float: right; width: 20%; min-width: 85px; text-align: right;">
	        		    <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/created.do';"><span class="glyphicon glyphicon glyphicon-pencil"></span> 글쓰기</button>
	        		</div>
	        </div>
			
	       <div id="paging" style="clear: both; width:900px;">
	       		  ${paging}
	              
	                
					
					
			</div>
	   		 </div><br>
	   		
<!-- footer -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>

</body>
</html>
	   		 