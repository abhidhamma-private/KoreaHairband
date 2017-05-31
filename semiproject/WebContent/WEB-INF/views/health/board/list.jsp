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
<div style="width: 900px; margin: 0px auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>
<br><br>
	


		<div class="container" style="width: 900px; margin: 0 auto; padding:0; position:relative;">
		 <!-- <div style="width:100%; height:100px;">
			<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
				
			</div>
			
			<div style="border:1px solid #337AB7; float:left; height:150px;width:50%">
			
			</div>
		</div> -->
		
	
	
        <div class="table" style="clear: both; width: 900px; margin: 0 auto; position:relative;">
	            <table class="table table-hover" id="maintable">
	                <thead>
	                    <tr>
	                        <th class="text-center" style="width: 70px;">1</th>
	                        <th class="text-center">제목</th>
	                        <th class="text-center" style="width: 100px;">글쓴이</th>
	                        <th class="text-center" style="width: 100px;">날짜</th>
	                        <th class="text-center" style="width: 70px;">조회수</th> 
	                        <th class="text-center" style="width: 70px;">추천</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    
	                    <tr>
	                        <td class="text-center">1</td>
	                        <td><a href="#">[건강]제목입니다...</a></td>
	                        <td class="text-center">스프링</td>
	                        <td class="text-center">2010-10-10</td>
	                        <td class="text-center">10</td> 
	                        <td class="text-center">0</td> 
	                    </tr>
	                    
	                </tbody>
	            </table>
	        </div>
			
			<div style="clear: both;">
	        		<div style="float: left; width: 20%; min-width: 85px;">
	        		    <button type="button" class="btn" onclick="javascript:location.href='/';">전체목록</button>
	        		</div>
	        		
	        		<div style="float: right; width: 20%; min-width: 85px; text-align: right;">
	        		    <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/Bcreated.do';"><span class="glyphicon glyphicon glyphicon-pencil"></span> 글쓰기</button>
	        		</div>
	        </div>
			
	       <div id="paging">
	       		  
	              
	                <ul class="pagination">
					  
					  <li><a href="#"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
					  <li><a href="#">1</a></li>
					  <li><a href="#">2</a></li>
					  <li><a href="#">3</a></li>
					  <li><a href="#">4</a></li>
					  <li><a href="#">5</a></li>
					  <li><a href="#"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
					</ul>
					
					
			</div>
	        
	        
	        
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
	   		 
	   		 