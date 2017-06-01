<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/health/setting.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<script type="text/javascript">
function article(num) {
	var url="${articleUrl}&num="+num;
	location.href=url;
}

</script>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/slider.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
</head>

<body>

<!-- header -->
<div style="width: 900px; margin: 0px 600px 0px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>
<br><br>
	


		<div class="container" style="width: 900px; margin: 0 auto; padding:0; position:relative;">
			 <div style="width:100%; height:100px;">
				<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
					<table>
					<tr>
		                        <td class="text-center" width="20%" style="float:left;" bordercolor="blue">		
										<img src="<%=cp%>/uploads/photo/${bdto.savefilename}" alt="이미지없음" style="width:150px; height:150px; border:1px solid #337AB7;">	
								</td>
								
		                        <td width="80%" colspan="5">
			                        
			                        <ul class="list-group" style="padding:0;">
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
					<ul class="list-group" style="padding:0;">
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
	                        <td class="text-center">${dto.mem_Name}</td>
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
	   		 
	   		 