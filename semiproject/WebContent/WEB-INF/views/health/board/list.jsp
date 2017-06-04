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
		var divLeft = e.clientX - 510;
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
    var returnpage = "/health/board.do";
    f.mem_Id1.value = mem_Id;  //POST방식으로 넘기고 싶은  
    f.returnpage.value = returnpage;  //POST방식으로 넘기고 싶은  
    f.action="<%=cp%>/message/m_created.do?page="+${page};  //이동할 페이지
    f.method="post";  //POST방식
    f.submit();
}

</script>
<style type="text/css">
ul.list-group{
border-radius:0;
height:150px;
margin:0px;
}
.list-group-item:first-child {
border-top-right-radius:0;
border-top-left-radius:0;
}
.list-group-item:last-child {
border-top-right-radius:0;
border-top-left-radius:0;

}
.list-group-item {
border-top-right-radius:0;
border-top-left-radius:0;
padding: 1px 0px;
height:17%;
}
.popupSelect {
	cursor: pointer;
}

.popupLayer {
	z-index:10;
	cursor: pointer;
	position: absolute;
	display: none;
	background-color: #ffffff;
	border: solid 2px #d0d0d0;
	width: 100px;
	height: 120px;
	padding: 10px;
}
</style>

</head>

<body>

<!-- header -->
<div style="width: 900px; margin: 0px auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>
<br><br>
	
	
	    <form name="popup" method="post">
		<div class="popupLayer">
			<span id="popupID">ID가 나타날곳</span><br>
			<a href="javascript:void(0);" onclick="moveinfo();" id="popupInfo"><span id="popupInfo">회원 정보</span></a><br>
			<a href="javascript:void(0);" onclick="movemsg();"  id="popupMsg"><span id="popupMsg">쪽지 보내기</span></a><br>
			<span onClick="closeLayer(this)" style="cursor:pointer;font-size:1.5em" title="닫기">X</span>
		</div>
		<input type="hidden" name="mem_Id1" value="">
		<input type="hidden" name="returnpage" value="">
		</form>
		
		<!-- 로고 이미지 -->
		<img src="<%=cp%>/resource/img/m_freeboard.png" style="margin: 10px">


		<div class="container" style="width: 900px; margin: 0 auto; padding:0; position:relative;">
			 <div style="width:100%; height:100px;">
				<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
					<table>
					<tr>
		                        <td class="text-center" width="20%" style="float:left;" bordercolor="blue">		
										<img src="<%=cp%>/uploads/semi/${bdto.savefilename}" alt="이미지없음" style="width:150px; height:150px; border:1px solid #337AB7;">	
								</td>
								
		                        <td width="80%" colspan="5">
			                        
			                        <ul class="list-group" style="padding:0; height:150px;">
										<li class="list-group-item" style="font-size:"><span class="glyphicon glyphicon-star"></span> <a href="javascript:article('${bdto.bbs_Num}');" style="text-decoration:none;color:black;">Best article (추천 : ${bdto.likeCount})</a></li>
										<li class="list-group-item">제목 | ${bdto.subject}</li>
										<li class="list-group-item">내용 | ${bdto.content}</li>
										<li class="list-group-item">작성자 | ${bdto.mem_Name}</li>
										<li class="list-group-item">조회수 | ${bdto.hitCount}</li>
										<li class="list-group-item">작성일 | ${bdto.created}</li>	
									</ul>
									
									<div>
										<%-- <span onclick="javascript:article('${dto.bbs_num}');">${dto.subject}</span><br>
										<span onclick="javascript:article('${dto.bbs_num}');">${dto.content}</span> --%>
									</div>
		                       	</td>
		             </tr>
		             </table>
				</div>
				
				<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
					<ul class="list-group" style="padding:0; height:150px;">
						<li class="list-group-item"><span class="glyphicon glyphicon-search"></span> Hit article <span style="float:right">조회수</span></li>
						
						<c:forEach var="hdto" items="${hlist}">
							<li class="list-group-item"><a href="javascript:article('${hdto.bbs_Num}');" style="text-decoration:none;color:black;">${hdto.ranked} | ${hdto.subject} </a><span style="float:right">${hdto.hitCount}</span></li>
						</c:forEach>
						
					</ul>
				</div>
			 </div>
		
		
	
        <div class="table" style="clear: both; width: 900px; margin: 0 auto; position:relative;">
	            <table class="table table-hover" id="maintable">
	                <thead>
	                    <tr>
	                        <th class="text-center" style="width: 70px;">번호</th>
	                        <th class="text-center" style="width: 550px;">제목</th>
	                        <th class="text-center" style="width: 70px;">글쓴이</th>
	                        <th class="text-center" style="width: 70px;">날짜</th>
	                        <th class="text-center" style="width: 70px;">조회수</th> 
	                        <th class="text-center" style="width: 70px;">추천</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <c:forEach var="dto" items="${list}">
	                    <tr>
	                        <td class="text-center">${dto.listNum}</td>
	                        <td>
	                        <c:forEach var="n" begin="1" end="${dto.depth}">
	                                &nbsp;&nbsp;
	                            </c:forEach>
	                            <c:if test="${dto.depth!=0}">
	                                └
	                            </c:if>
	                        <a href="javascript:article('${dto.bbs_Num}');">${dto.subject}</a>
	                        </td>
	                        <td><a class="popupSelect" data-id="${dto.mem_Id}">${dto.mem_Name}</a></td>
	                        <td class="text-center">${dto.created}</td>
	                        <td class="text-center">${dto.hitCount }</td> 
	                        <td class="text-center">${dto.likeCount}</td> 
	                    </tr>
	                    </c:forEach>
	                    
	                </tbody>
	            </table>
	        </div>
			
			<div style="clear: both;">
	        		<div style="float: left; width: 20%; min-width: 85px;">
	        		    <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/board.do';">전체목록</button>
	        		</div>
	        		
	        		<div style="float: right; width: 20%; min-width: 85px; text-align: right;">
	        		    <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/createdB.do';"><span class="glyphicon glyphicon glyphicon-pencil"></span> 글쓰기</button>
	        		</div>
	        </div>
			
	       <div id="paging">
	       		  
	               ${paging}
					
					
			</div>
			<br><br>
	        
	        
	        
	        <div style="margin:0px auto; width: 60%; text-align: center;">
	        		     <form name="searchForm" method="post" class="form-inline">
							  <select class="form-control input-sm" name="searchKey">
							      <option value="subject">제목</option>
							      <option value="userName">작성자</option>
							      <option value="content">내용</option>
							      <option value="created">등록일</option>
							  </select>
							  <input type="text" class="form-control input-sm input-search" name="searchValue">
							  <button type="button" class="btn" onclick="searchList();"><span class="glyphicon glyphicon-search"></span> 검색</button>
	        		     </form>
	        		</div>
	   		 </div><br>
		
	   		
<!-- footer -->
<div style="width: 900px; margin: 0 auto;position:relative;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>

</body>
</html>
	   		 
	   		 