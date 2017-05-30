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
<title> Photo </title>
<style type="text/css"></style>

<script type="text/javascript">
	function isImage(filename)
	{
		var f = /(\.gif|\.jpg|\.jpeg|\.png)$/i;
		return f.test(filename);
	}

    function sendOk()
    {
        var f = document.boardForm;
    	var mode="${mode}";

    	var str = f.subject.value;
        if(!str) {
            alert("제목을 입력하세요. ");
            f.subject.focus();
            return;
        }
        
        str = f.content.value;
        if(!str) {
            alert("설명을 입력하세요. ");
            f.content.focus();
            return;
        }
        
        str = f.upload.value;
        if(mode == "created" && ! isImage(str) || mode == "update" && str != "" && ! isImage(str))
        {
        	alert("Image를 등록하세요.");
        	f.upload.focus();
        	return;
        }

    	if(mode=="created")
    		f.action="<%=cp%>/photo/created_ok.do";
    		
    	else if(mode=="update")
    		f.action="<%=cp%>/photo/update_ok.do";

        f.submit();
    }
</script>
</head>
<body>
<div style="margin: 100px;" align="center">
    <div style="width: 700px;">
        <div>
            <h3> Photo Gallery </h3>
        </div>
        
        <div>
			<form name="boardForm" method="post" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
				  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
				
				  <tr align="left" height="40"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
				      <td style="padding-left:10px;"> 
				          <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="">
				      </td>
				  </tr>
				  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
	
				  <tr align="left" height="40"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">작성자</td>
				      <td style="padding-left:10px;"> 
				          	작성자
				      </td>
				  </tr>
				  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
				
				  <tr align="left"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top"> 설&nbsp;&nbsp;&nbsp;&nbsp;명 </td>
				      <td valign="top" style="padding:5px 0px 5px 10px;"> 
				          <textarea name="content" rows="12" class="boxTA" style="width: 95%;"></textarea>
				      </td>
				  </tr>
				  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
				  
				  <tr align="left" height="40">
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;"> Image </td>
				      <td style="padding-left:10px;"> 
				          <input type="file" name="upload" class="boxTF" size="53" style="height: 25px;" accept="image/*">
				       </td>
				  </tr> 
				  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
				      <td align="center" >
					        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
					        <button type="reset" class="btn">다시입력</button>
					        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/photo/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
					  		
					  		<c:if test="${mode == 'update' }">
					      		<input type="hidden" name="num" value="${dto.num }">
					      		<input type="hidden" name="page" value="${page }">
					      	</c:if> 
					  </td>
			    </tr>
			  </table>
			</form>
        </div>
    </div>
</div>
</body>
</html>