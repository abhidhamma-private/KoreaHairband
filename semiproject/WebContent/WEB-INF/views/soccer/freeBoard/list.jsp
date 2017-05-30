<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>List Test</title>

<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">
	function searchList()
	{
		var f=document.searchForm;
		f.action="<%=cp%>/soccer/board.do";
		f.submit();
	}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div>
    <div style="width: 870px; margin: 0px auto;" align="center">
        <div style="margin: 30px auto; width: 800px;">
            <h2> 자유게시판 리스트 </h2> 
        </div>
        
        <div>
			<table style="width: 870px; margin: 20px auto 0px; border-spacing: 0px;">
			   <tr height="35">
			      <td colspan="2" align="right">
			      		${dataCount }개(현재 : ${page }페이지 / 전체 : ${total_page }페이지)
					  <select id="rows" name="rows" class="selectField" onchange="selectList();">
					  	  <option value="3"> 3개씩 보기 </option>
						  <option value="5"> 5개씩 보기 </option>
						  <option value="7"> 7개씩 보기 </option>
						  <option value="10"> 10개씩 보기 </option>
						  <option value="15"> 15개씩 보기 </option>
					  </select>
				  </td>
			   </tr>
			</table>
			 
			<table style="width: 870px; margin: 5px auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="center" bgcolor="#eeeeee" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <th width="60" style="color: #787878;">번호</th>
			      <th width="100" style="color: #787878;">사진</th>
			      <th style="color: #787878;">제목</th>
			      <th width="100" style="color: #787878;">작성자</th>
			      <th width="80" style="color: #787878;">작성일</th>
			      <th width="60" style="color: #787878;">조회수</th>
			  </tr>
			 
			  <c:forEach var="dto" items="${list }">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid #cccccc;">
			      <td> ${dto.listNum } </td>
			  	  <td> <img src="<%=cp%>/uploads/files/${dto.saveFilename}" width="77" height="77" border="0"> </td>
			      <td align="left" style="padding-left: 10px;">
			      	   <c:forEach var="n" begin="1" end="${dto.depth }">
			      	       &nbsp;
			      	   </c:forEach>
			      	   
			      	   <c:if test="${dto.depth != 0 }"> ┖&nbsp; </c:if>
			      
			           <a href="${articleUrl }&bbs_num=${dto.bbs_num }">${dto.subject }</a>
			      </td>
			      <td>${dto.mem_Name }</td>
			      <td>${dto.created }</td>
			      <td>${dto.hitCount }</td>
			  </tr>
			  </c:forEach>
			</table>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
				<tr height="35">
					<td align="center">
				         ${paging }
					</td>
				</tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/soccer/board.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="" method="post">
			              <select name="searchKey" class="selectField">
			              	  <option value="subject" selected="selected">제목</option>
			                  <option value="mem_Name">작성자</option>
			                  <option value="content">내용</option>
			                  <option value="created">등록일</option>
			            </select>
			            <input type="text" name="searchValue" class="boxTF">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
			      <td align="right" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/soccer/board/created.do';">글올리기</button>
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