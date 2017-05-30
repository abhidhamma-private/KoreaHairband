<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/health/setting.jsp" %>
		
	    <div class="table-responsive" style="clear: both;">
	        <div class="article">
	            <table class="table">
	                 <thead>
	                     <tr>
	                         <th colspan="2" style="text-align:center;">
	                                 	제목
	                         </th>
	                     </tr>
	                <thead>
	                 <tbody>
	                     <tr>
	                         <td style="text-align: left;">
	                             	이름
	                         </td>
	                         <td style="text-align: right;">
	                          	날짜/조회/추천
	                         </td>
	                     </tr>
                         <tr style="border-bottom:none;">
                             <td colspan="2">
                                 <img src="" alt="이미지" style="max-width:100%; height:auto; resize:both;">
                             </td>
                         </tr>
	                     <tr>
	                         <td colspan="2" style="min-height: 30px;">
	                              	내용
	                         </td>
	                     </tr>
	                     <tr>
	                         <td colspan="2" style="min-height: 30px; text-align:center;" >
	                         		<button class="btn">
	                         		<span class="glyphicon glyphicon-star"></span><br>
	                              	추천
	                         		</button>
	                         </td>
	                     </tr>
	                </tbody>
	                <tfoot>
	                	<tr>
	                		<td>	                		
	                		        <button type="button" class="btn" onclick="">수정</button>
	                		        <button type="button" class="btn" onclick="">삭제</button>
	                		        <br>
	                		    <pre>댓글1</pre>
	                		    <pre>댓글2</pre>
	                		    <pre>댓글3</pre>
	                		</td>
	                		<td align="right">
	                		    <button type="button" class="btn"
	                		                onclick="'<%=cp%>/health/notice.do'"> 목록으로 </button>
	                		</td>
	                	</tr>
	                </tfoot>
	            </table>
	       </div>
	   </div>

