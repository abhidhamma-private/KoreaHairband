<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/slider.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
$(function() {
	 $("select[name=catagoryKey]").hide(); 
	 
	 $("select[name=searchKey]").change(function() {
		var num = $(this).val();
		//var num = $(this).attr("value");
		//alert(num);
		if(num!='category'){
			 $("select[name=catagoryKey]").hide();
		} else {
			$("select[name=catagoryKey]").show(); 
		}
	
	});
});

</script>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>



<div class="f_container">

<table style="width: 780px; margin: 30px auto 0px; border-spacing: 0px;">
<tr height="45">
	<td align="left" class="title">
		<h3>패션-중고거래게시판</h3>
	</td>
</tr>
</table>

<table style="width: 780px; margin: 20px auto 0px; border-spacing: 0px;">
   <tr height="35">
      <td align="left" width="50%">
        1개(1/1 페이지)
      </td>
      <td align="right" >
          <select id="rows" class="selectField" onchange="">
          	<option value="5" >5개씩 보기 </option>
          	<option value="10">10개씩 보기 </option>
          	<option value="20">20개씩 보기 </option>
          	<option value="30">30개씩 보기 </option>
          	
          </select>
      </td>
   </tr>
</table>

<table style="width: 780px; margin: 0px auto; border-spacing: 0px;">
  <tr><td height="1" colspan="6" bgcolor="#cccccc"></td></tr>
  <tr align="center" height="35"> 
      <th width="60" style="color: #787878;">번호</th>
      <th width="80" style="color: #787878;">카테고리</th>
      <th style="color: #787878;">제목</th>
      <th width="100" style="color: #787878;">작성자</th>
      <th width="80" style="color: #787878;">작성일</th>
      <th width="60" style="color: #787878;">조회수</th>
  </tr>
  <tr><td height="1" colspan="6" bgcolor="#cccccc"></td></tr>


  <tr align="center" bgcolor="#ffffff" height="35"> 
      <td align="center">1</td>
      <td align="center">[팝니다]</td>
      <td align="left" style="padding-left: 10px;">
          	사주세욤
      </td>
      <td align="center">수동이</td>
      <td align="center">오늘</td>
      <td align="center">0</td>
  </tr>
  
  <tr><td height="1" colspan="6" bgcolor="#cccccc"></td></tr> 

</table>
 
<table style="width: 780px; margin: 0px auto; border-spacing: 0px;">
   <tr height="35">
	<td align="center">
       
	</td>
   </tr>
</table>

<table style="width: 780px; margin: 10px auto; border-spacing: 0px;">
   <tr height="40">
   
       <td align="right" width="100">
          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/fashion/created1.do';">글올리기</button>
      </td>
      <td align="center">
          <form name="searchForm" action="" method="post">
  
              <select  name="searchKey" class="selectField">
                  <option value="subject">제목</option>
                  <option value="category">말머리</option>
                  <option value="name">작성자</option>
                  <option value="content">내용</option>
                  <option value="created">작성일</option>
            </select>
            
            <select name="catagoryKey" class="selectField">
                  <option value="sell">팝니다</option>
                  <option value="buy">삽니다</option>
            </select>
           
            
            <input type="text" name="searchValue" class="boxTF">
            <button type="button" class="btn" onclick="">검색</button>
        </form>
      </td>
         <td align="left" width="100">
          <button type="button" class="btn" onclick="">새로고침</button>
      </td>
     
   </tr>
</table>


</div>


<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>	