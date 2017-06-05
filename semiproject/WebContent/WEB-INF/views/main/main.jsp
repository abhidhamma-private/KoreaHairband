<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>
<link rel="stylesheet" href="<%=cp%>/resource/css/bootstrap/style.css" type="text/css"/>

<style type="text/css">
#left {
float:left;
width:430px;
margin:0;
}

#right {
float:right;
width:430px;

}

.boardbox {
text-align:left;
}  */

ul {
list-style:none;
padding:0px;
}

.nav-tabs>li>a {
border-radius:0;
text-align: center;
}

.list-group-item {
border-radius:0px;
height:30px;
line-height:10px;
text-overflow:70px;
}
tr {
margin:0;
padding:0;
}
tr > td {
margin:0;
padding:0;
}
a {
line-height:10px;
}

#rank>li>a,
#rank>li>a:focus,
#rank>li>a:hover {
width:105px;
} 
</style>


</head>
<body>

<!-- header -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/slider.jsp"/>
</div>
<!-- main -->
<div style="width: 900px; height: 330px; margin: 0 auto;">
		<div id="left">
			<!-- 전체인기글 -->
			<div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">
			    <!-- 로고 이미지 -->
				<img src="<%=cp%>/resource/img/mainbbs1.png" style="margin: 10px">
			    
			    <table style="width:430px; height:200px; margin:0">
			    	
			    	<c:forEach var="abdto" items="${ablist}">
				    	<tr>
				    		<td style="width:20%">
				    		<img alt="<%=cp%>/resource/img/hair.jpg" src="<%=cp%>/uploads/semi/${abdto.savefilename}" width="80px" height="80px" >
				    		</td>
				    		<td style="width:80%">
					    		<div style="height:50%">
					    		
					    		<ol class="breadcrumb" style="float:right; font-size:13px; margin:0px">
										  <li><a href="#">${abdto.boardname}</a></li>
										  
										  <li>
										  <a id="countLikeBoard${abdto.bbs_Num}" >좋아요 ${abdto.likeCount} </a> 
										  </li>
										  
										  <li class="active">조회수 ${abdto.hitCount}</li>
								</ol>
								</div>
								
								
								<div style="height:50%">
								<a href="<%=cp%>${abdto.url}${abdto.bbs_Num}">${abdto.subject}</a>
								
								</div>
				    		</td>
				    	</tr>
			    	</c:forEach>
			    </table>
			  </div>
			  
			  	
		</div>
		
		 <div id="right">
		 	<!-- 카테고리별 화제의글  -->	
			<div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">
				<!-- 로고 이미지 -->
				<img src="<%=cp%>/resource/img/mainbbs2.png" style="margin: 10px">
		    	<!-- 메뉴 -->
			    <ul class="nav nav-tabs" id="rank" role="tablist">
			      <li role="presentation" class="active"><a href="#board1"  role="tab" data-toggle="tab" aria-expanded="true"><strong>다작</strong></a></li>
			      <li role="presentation"><a href="#board2" role="tab"  data-toggle="tab" ><strong>댓글수</strong></a></li>
			      <li role="presentation"><a href="#board3" role="tab"  data-toggle="tab" ><strong>포인트</strong></a></li>
			      <li role="presentation"><a href="#board4" role="tab"  data-toggle="tab" ><strong>좋아요</strong></a></li>
			    </ul>
			    
			    <!-- 토글데이터 -->
			    <div id="myTabContent" class="tab-content">
			      <!-- 다작 -->
			      <div role="tabpanel" class="tab-pane fade in active" id="board1" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="memaldto" items="${memal_list}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;${memaldto.mem_Name}<small style="float:right">글쓴수${memaldto.memsarticle}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 댓글수 -->
			      <div role="tabpanel" class="tab-pane " id="board2" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="memredto" items="${memre_list}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;${memredto.mem_Name}<small style="float:right">조회수${memredto.memsrepl}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 포인트 -->
			      <div role="tabpanel" class="tab-pane " id="board3" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="mempodto" items="${mempo_list}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;${mempodto.mem_Name}<small style="float:right">포인트${mempodto.point}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 좋아요 -->
			      <div role="tabpanel" class="tab-pane " id="board4" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="memgooddto" items="${memgood_list}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;${memgooddto.mem_Name}<small style="float:right">좋아요${memgooddto.likeCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			    </div>
		  	</div>
		</div>
	</div> 



<div style="width: 900px; height: 330px; margin: 0 auto;">
		<div id="right">
			<!-- 전체인기글 -->
			
			
			<!-- 게시판별 조회수글 -->
			<div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">
			    <!-- 로고 이미지 -->
				<img src="<%=cp%>/resource/img/mainbbs4.png" style="margin: 10px">
			    <ul class="nav nav-tabs" role="tablist">
			      <li role="presentation" class="active"><a href="#board5" id="home-tab" role="tab" data-toggle="tab" aria-expanded="true"><strong>스포츠</strong></a></li>
			      <li role="presentation"><a href="#board6" role="tab" id="profile-tab" data-toggle="tab" ><strong>건강</strong></a></li>
			      <li role="presentation"><a href="#board7" role="tab" id="profile-tab" data-toggle="tab" ><strong>패션</strong></a></li>
			      <li role="presentation"><a href="#board8" role="tab" id="profile-tab" data-toggle="tab" ><strong>푸드</strong></a></li>
			      <li role="presentation"><a href="#board9" role="tab" id="profile-tab" data-toggle="tab" ><strong>반려동물</strong></a></li>
			      <li role="presentation"><a href="#board10" role="tab" id="profile-tab" data-toggle="tab" ><strong>IT</strong></a></li>
			    </ul>
			    <div id="myTabContent" class="tab-content">
			      <!-- 스포츠 -->
			      <div role="tabpanel" class="tab-pane fade in active" id="board5" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="shdto" items="${shlist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${shdto.url}${shdto.bbs_Num}">${shdto.subject}</a><small style="float:right">조회수${shdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 건강 -->
			      <div role="tabpanel" class="tab-pane " id="board6" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="hhdto" items="${hhlist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${hhdto.url}${hhdto.bbs_Num}">${hhdto.subject}</a><small style="float:right">조회수${hhdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			       <!-- 패션 -->
			      <div role="tabpanel" class="tab-pane " id="board7" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="fshdto" items="${fshlist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${fshdto.url}${fshdto.bbs_Num}">${fshdto.subject}</a><small style="float:right">조회수${fshdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 푸드 -->
			      <div role="tabpanel" class="tab-pane" id="board8" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="fhdto" items="${fhlist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${fhdto.url}${fhdto.bbs_Num}">${fhdto.subject}</a><small style="float:right">조회수${fhdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 반려동물 -->
			      <div role="tabpanel" class="tab-pane" id="board9" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="phdto" items="${phlist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${phdto.url}${phdto.bbs_Num}">${phdto.subject}</a><small style="float:right">조회수${phdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- IT -->
			      <div role="tabpanel" class="tab-pane" id="board10" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="ithdto" items="${ithlist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${ithdto.url}${ithdto.bbs_Num}">${ithdto.subject}</a><small style="float:right">조회수${ithdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			    </div>
			  </div>
			  
			  	
		</div>
		<!-- right끝 -->
		
		 <div id="left">
		 	<!-- 카테고리별 화제의글  -->	
		 	<!-- 게시판별 추천글 -->
			<div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">
				<!-- 로고 이미지 -->
				<img src="<%=cp%>/resource/img/mainbbs3.png" style="margin: 10px">
		    	<!-- 메뉴 -->
			    <ul class="nav nav-tabs" role="tablist">
			      <li role="presentation" class="active"><a href="#board11" id="home-tab" role="tab" data-toggle="tab" aria-expanded="true"><strong>스포츠</strong></a></li>
			      <li role="presentation"><a href="#board12" role="tab" id="profile-tab" data-toggle="tab" ><strong>건강</strong></a></li>
			      <li role="presentation"><a href="#board13" role="tab" id="profile-tab" data-toggle="tab" ><strong>패션</strong></a></li>
			      <li role="presentation"><a href="#board14" role="tab" id="profile-tab" data-toggle="tab" ><strong>푸드</strong></a></li>
			      <li role="presentation"><a href="#board15" role="tab" id="profile-tab" data-toggle="tab" ><strong>반려동물</strong></a></li>
			      <li role="presentation"><a href="#board16" role="tab" id="profile-tab" data-toggle="tab" ><strong>IT</strong></a></li>
			    </ul>
			    
			    <!-- 토글데이터 -->
			    <div id="myTabContent" class="tab-content">
			      		<!-- 스포츠 -->
			      <div role="tabpanel" class="tab-pane fade in active" id="board11" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="sbdto" items="${sblist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${sbdto.url}${sbdto.bbs_Num}">${sbdto.subject}</a><small style="float:right">조회수${sbdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 건강 -->
			      <div role="tabpanel" class="tab-pane " id="board12" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="hbdto" items="${hblist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${hbdto.url}${hbdto.bbs_Num}">${hbdto.subject}</a><small style="float:right">조회수${hbdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			       <!-- 패션 -->
			      <div role="tabpanel" class="tab-pane" id="board13" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="fsbdto" items="${fsblist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${fsbdto.url}${fsbdto.bbs_Num}">${fsbdto.subject}</a><small style="float:right">조회수${fsbdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 푸드 -->
			      <div role="tabpanel" class="tab-pane" id="board14" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="fbdto" items="${fblist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${fbdto.url}${fbdto.bbs_Num}">${fbdto.subject}</a><small style="float:right">조회수${fbdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- 반려동물 -->
			      <div role="tabpanel" class="tab-pane" id="board15" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="pbdto" items="${pblist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${pbdto.url}${pbdto.bbs_Num}">${pbdto.subject}</a><small style="float:right">조회수${pbdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			      
			      <!-- IT -->
			      <div role="tabpanel" class="tab-pane" id="board16" aria-labelledBy="home-tab">
			        	<div class="boardbox">
							<ul class="list-group">
								<c:forEach var="itbdto" items="${itblist}" varStatus="status">
								<li class="list-group-item">${status.count}&nbsp;<a href="<%=cp%>${itbdto.url}${itbdto.bbs_Num}">${itbdto.subject}</a><small style="float:right">조회수${itbdto.hitCount}</small></li>
								</c:forEach>
							</ul>
						</div>
			      </div>
			    </div>
		  	</div>
		</div>
		<!-- right끝 -->
	</div> 
</div>

<!-- footer -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>

</body>
</html>