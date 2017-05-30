<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/health/setting.jsp" %>




<div class="container">
    <div class="bodyFrame col-sm-10"  style="float:none; margin-left: auto; margin-right: auto;">
	    <div>
	        <form name="noticeForm" method="post" enctype="multipart/form-data">
	            <div class="bs-write">
	                <table class="table">
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
	                            <td class="td1" colspan="4" style="padding-bottom: 0px;"></td>
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
	                                  <button type="submit" class="btn"> 확인 <span class="glyphicon glyphicon-ok"></span></button>
	                                  <button type="button" class="btn" onclick="javascript:location.href='#';"> 취소 </button>
								</td>
	                        </tr>
	                    </tfoot>
	                </table>
	            </div>
	        </form>
	    </div>
    
    </div>
</div>


	
