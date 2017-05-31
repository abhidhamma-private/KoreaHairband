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
<script type="text/javascript">
function sendOk() {
	var f = document.noticeForm;
	var mode = "${mode}";
	

	

	if(mode=="created")
		f.action="<%=cp%>/health/Bcreated_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/health/Bupdate_ok.do";

    f.submit();
	
}


</script>
</head>
<body>
<!-- header -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>
<div style="width: 900px; height: 600px; margin: 0 auto; position:relative;">

    <div class="bodyFrame"  style="float:none; margin-left: auto; margin-right: auto;">
	    <div>
	        <form name="noticeForm" method="post" enctype="multipart/form-data" action="<%=cp%>/health/created_ok.do">
	            <div class="bs-write">
	                <table class="table" style="width: 900px; height: 600px; margin: 0 auto;">
	                    <tbody>
	                        <tr>
	                            <td class="td1">작성자</td>
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
	                                <input type="text" name="subject" class="form-control input-sm" value="${dto.subject}" required="required">
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
	                            	<textarea name="content" class="form-control" rows="13">${dto.content}</textarea>
	                            </td>
	                        </tr>
	                    </tbody>
	                    <tfoot>
	                        <tr>
	                            <td colspan="4" style="text-align: center; padding-top: 15px;">
	                                  <button  class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}<span class="glyphicon glyphicon-ok"></span></button>
	                                  <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/health/notice.do';"> 취소 </button>
								
									  <c:if test="${mode=='update'}">
										<input type="hidden" name="bbs_num" value="${dto.bbs_num}">			        
										<input type="hidden" name="savefilename" value="${dto.savefilename}">			        
										<input type="hidden" name="page" value="${page}">			        
							          </c:if>
								</td>
	                        </tr>
	                    </tfoot>
	                </table>
	            </div>
	        </form>
	    </div>
    
    </div>

</div>
<!-- footer -->
<div style="width: 900px; margin: 0 auto;">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>
</body>
</html>
	
