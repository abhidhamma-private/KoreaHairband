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
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>
<br><br>
	<div style="width: 900px; height: 600px; margin: 0 auto;">
		 <!-- <div style="width:100%; height:100px;">
			<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
				
			</div>
			
			<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
			
			</div>
		  </div> -->
		
	
	
        <div class="table-responsive" style="clear: both;">
        	
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
									  <li><a href="#">좋아요</a></li>
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
	        		    <button type="button" class="btn" onclick="javascript:location.href='/';">전체목록</button>
	        		</div>
	        		
	        		<div style="float: right; width: 20%; min-width: 85px; text-align: right;">
	        		    <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/created.do';"><span class="glyphicon glyphicon glyphicon-pencil"></span> 글쓰기</button>
	        		</div>
	        </div>
			
	       <div id="paging" style="clear: both; width:900px;">
	       		  ${paging}
	              
	                
					
					
			</div>
	        
	        
	        
	        <div style="width: 60%; text-align: center; margin: 0 auto;">
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
	   		 </div>
	   		 <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
<!-- footer -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>

</body>
</html>
	   		 