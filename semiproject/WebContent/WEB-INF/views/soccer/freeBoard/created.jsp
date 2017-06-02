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
	<title>  </title>
	
	<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
	<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
	<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
	
	<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
	
	<script type="text/javascript">
	    function sendOk()
	    {
	        var f = document.boardForm;
	
	    	var str = f.subject.value;
	        if(!str) {
	            alert("제목을 입력하세요. ");
	            f.subject.focus();
	            return;
	        }
	
	    	str = f.content.value;
	        if(!str) {
	            alert("내용을 입력하세요. ");
	            f.content.focus();
	            return;
	        }
	
	    	var mode="${mode}";
	    	
	    	if(mode=="created")
	    		f.action="<%=cp%>/soccer/board/created_ok.do";
	    		
	    	else if(mode=="update")
	    		f.action="<%=cp%>/soccer/board/update_ok.do";
	    		
	       	else if(mode=="reply")
	       		f.action="<%=cp%>/soccer/board/reply_ok.do";
	
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
	        <div style="margin: 30px auto; width: 870px;">
	            <h3> 자유게시판(답변가능) 글 작성 </h3>
	        </div>
	        
	        <div>
				<form name="boardForm" method="post" enctype="multipart/form-data">
				  <table style="width: 100%; margin: 20px auto 0px; border: 1px solid #cccccc; border-spacing: 1px; border-collapse: collapse;">
				  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
				      <td style="padding-left:10px;"> 
				        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject }">
				      </td>
				  </tr>
				
				  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">작성자</td>
				      <td style="padding-left:10px;"> 
				          ${sessionScope.member.mem_Name }
				      </td>
				  </tr>
				  
				  <c:if test="${sessionScope.member.mem_Id == 'admin' }">
					  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
					      <td width="100" bgcolor="#eeeeee" style="text-align: center;"> 공지 </td>
					      <td style="padding-left:10px;"> 
					          <select id="notice" name="notice" class="selectField">
							  	  <option value="0"> 공지사항 설정 해제 </option>
								  <option value="1"> 공지사항 설정 </option>
							  </select>
					      </td>
					  </tr>
				  </c:if>
				
				  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
				      <td valign="top" style="padding:5px 0px 5px 10px;"> 
				        <textarea name="content" rows="12" class="boxTA" style="width: 95%;">${dto.content }</textarea>
				      </td>
				  </tr>
				  
				  <tr align="left" height="40">
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;"> Image </td>
				      <td style="padding-left:10px;"> 
				          <input type="file" name="upload" class="boxTF" size="53" style="height: 25px;">
				      </td>
				  </tr>
				  </table>
				
				  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
				     <tr height="45"> 
				      <td align="center" >
				        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
				        <button type="reset" class="btn">다시입력</button>
				        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/soccer/board.do';">${mode=='update'?'수정취소':'등록취소'}</button>
				      
				      	<c:if test="${mode == 'update' }">
				      		<input type="hidden" name="bbs_num" value="${dto.bbs_num }">

				      		<input type="hidden" name="page" value="${page }">
				      		<input type="hidden" name="searchKey" value="${searchKey }">
				      		<input type="hidden" name="searchValue" value="${searchValue }">
				      	</c:if>
				      	
				      	<c:if test="${mode == 'reply' }">
				      		<input type="hidden" name="bbs_num" value="${dto.bbs_num }">
				      		<input type="hidden" name="page" value="${page }">
				      		<input type="hidden" name="searchKey" value="${searchKey }">
				      		<input type="hidden" name="searchValue" value="${searchValue }">
				      		<input type="hidden" name="groupNum" value="${dto.groupNum }">
				      		<input type="hidden" name="orderNo" value="${dto.orderNo }">
				      		<input type="hidden" name="depth" value="${dto.depth }">
				      		<input type="hidden" name="parent" value="${dto.bbs_num }">
				      		<input type="hidden" name="mem_Id" value="${dto.mem_Id }">
				      	</c:if>
				      </td>
				    </tr>
				  </table>
				</form>
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