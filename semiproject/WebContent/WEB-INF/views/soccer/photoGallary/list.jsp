<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>

<style type="text/css">
.imgLayout{
	width: 190px;
	height: 205px;
	padding: 10px 5px 10px;
	margin: 5px;
	border: 1px solid #DAD9FF;
}
.subject {
     width:180px;
     height:25px;
     line-height:25px;
     margin:5px auto;
     border-top: 1px solid #DAD9FF;
     display: inline-block;
     white-space:nowrap;
     overflow:hidden;
     text-overflow:ellipsis;
     cursor: pointer;
}
</style>

<script type="text/javascript">
function article(num)
{
	var url="${articleUrl}&num="+num;
	location.href=url;
}
</script>
</head>

<body>
	
<div class="container" align="center">
    <div class="body-container" style="width: 630px;">
        <div class="body-title">
            <h3> 포토 갤러리 </h3>
        </div>
        
        <div>

			<table style="width: 630px; margin: 0px auto; border-spacing: 0px;">
                       <tr>
			     <td width="210" align="center">
			        <div class="imgLayout">
			             <img src="<%=cp%>/uploads/photo/${dto.imageFilename}" width="180" height="180" border="0">
			             <span class="subject" onclick="javascript:article('${dto.num}');" >
			                   	제목
			             </span>
			         </div>
			     </td>
				</tr>
			</table>           
           
           <table style="width:100%; border-spacing:0px;">
              <tr height="50">
                 <td align="center">
                  	 페이징
                 </td>
              </tr>
           </table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="center" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/photo/created.do';">사진 등록하기</button>
			      </td>
			   </tr>
			</table>
        </div>
    </div>
</div>
</body>
</html>