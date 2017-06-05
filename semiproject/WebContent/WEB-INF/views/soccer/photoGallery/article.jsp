<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>

<style type="text/css">
	.td_cont img{max-width:900px}
</style>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">
	function deletePhoto(bbs_num)
	{
		<c:if test="${sessionScope.member.mem_Id=='admin' || sessionScope.member.mem_Id==dto.mem_Id}">
		    if(confirm("게시물을 삭제 하시겠습니까 ?"))
		    {
		    	 var url = "<%= cp %>/soccer/photo/deletePH.do?bbs_num=" + bbs_num + "&page=${page}";
		    	 location.href=url;
		    }	
		</c:if>    
		
		<c:if test="${sessionScope.member.mem_Id!='admin' && sessionScope.member.mem_Id!=dto.mem_Id}">
		    alert("게시물을 삭제할 수  없습니다.");
		</c:if>
	}
	
	function updatePhoto(bbs_num)
	{
		<c:if test="${sessionScope.member.mem_Id==dto.mem_Id}">
		    var url="<%=cp%>/soccer/photo/updatePH.do?bbs_num=" + bbs_num + "&page=${page}";
		    location.href=url;
		</c:if>
		
		<c:if test="${sessionScope.member.mem_Id!=dto.mem_Id}">
		   alert("게시물을 수정할 수  없습니다.");
		</c:if>
	}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div>
    <div class="body-container" style="width: 700px; margin:0 auto" align="center">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> Photo Gallery </h3>
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
			       	작성자 : ${dto.mem_Name }
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created }
			    </td>
			</tr>
			<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			<tr>
			  <td class="td_cont" colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      	<img src="<%=cp%>/uploads/semi/${dto.imageFilename}" width="377" height="377"><br>
			      	${dto.content }<br>
			   </td>
			</tr>
			<tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			<tr height="35">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			      	 이전글 :
			        <c:if test="${not empty predto}">
			      	 	<a href="<%=cp%>/soccer/photo/articlePH.do?page=${page }&bbs_num=${predto.bbs_num }">${predto.subject }</a>
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
			      	 	<a href="<%=cp%>/soccer/photo/articlePH.do?page=${page }&bbs_num=${nextdto.bbs_num }">${nextdto.subject }</a>
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
			    <td width="300" align="left">
			       <c:if test="${sessionScope.member.mem_Id==dto.mem_Id}">				    
			          <button type="button" class="btn" onclick="updatePhoto('${dto.bbs_num}');">수정</button>
			       </c:if>
			       <c:if test="${sessionScope.member.mem_Id==dto.mem_Id || sessionScope.member.mem_Id=='admin'}">				    
			          <button type="button" class="btn" onclick="deletePhoto('${dto.bbs_num}');">삭제</button>
			       </c:if>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/soccer/photo.do?page=${page}';">리스트</button>
			    </td>
			</tr>
			</table>
        </div>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>