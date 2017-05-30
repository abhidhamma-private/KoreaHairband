<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/health/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<script type="text/javascript">
<%-- function sendOk() {
	
		f.action="<%=cp%>/health/created_ok.do";
	

    f.submit();
} --%>

</script>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/slider.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
</head>
<body>
<!-- header -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>
<div style="width: 900px; height: 600px; margin: 0 auto;">
<div class="container" >
    <div class="bodyFrame col-sm-10"  style="float:none; margin-left: auto; margin-right: auto;">
	    <div>
	        <form name="noticeForm" method="post" enctype="multipart/form-data" action="<%=cp%>/health/created_ok.do">
	            <div class="bs-write">
	                <table class="table" style="width: 900px; height: 600px; margin: 0 auto;">
	                    <tbody>
	                        <tr>
	                            <td class="td1">작성자명</td>
	                            <td class="td2 col-md-5 col-sm-5">
	                                <p class="form-control-static">${sessionScope.member.mem_Name}</p>
	                            </td>
	                            <td class="td1" align="center">&nbsp;</td>
	                            <td class="td2 col-md-5 col-sm-5">
	                                &nbsp;
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="td1">제목</td>
	                            <td colspan="3" class="td3">
	                                <input type="text" name="subject" class="form-control input-sm" value="" required="required">
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="td1" colspan="1" style="padding-bottom: 0px;">
	                            	파일첨부
	                            </td>
	                            <td colspan="3"><input type="file" name="upload" class="boxTF" size="53" style="height: 25px;" 
			                     accept="image/*">
	                            </td>
	                        </tr>
	                        <tr>
	                            <td colspan="4" class="td4">
	                            	<textarea name="content" class="form-control" rows="13"></textarea>
	                            </td>
	                        </tr>
	                    </tbody>
	                    <tfoot>
	                        <tr>
	                            <td colspan="4" style="text-align: center; padding-top: 15px;">
	                                  <button  class="btn" >${mode=='update'?'수정완료':'등록하기'}<span class="glyphicon glyphicon-ok"></span></button>
	                                  <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/notice.do';"> 취소 </button>
								</td>
	                        </tr>
	                    </tfoot>
	                </table>
	            </div>
	        </form>
	    </div>
    
    </div>
</div>
</div>
<!-- footer -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>
</body>
</html>
	
