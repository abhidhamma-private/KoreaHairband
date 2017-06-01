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
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<!-- 아래 2개 인클루드 -->
<script type="text/javascript" src="<%=cp%>/resource/editor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=cp%>/resource/editor/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>

<script type="text/javascript">
function sendOk() {
	var f = document.boardForm;
 	var mode="${mode}";

	if(mode=="created")
		f.action="<%=cp%>/fashion/created1_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/fashion/update1_ok.do";

    f.submit();
}


</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>




<div class="f_container">

<table style="width: 900px; margin: 30px auto 0px; border-spacing: 0px;">
<tr height="45">
	<td align="left" class="title">
		<a href ="<%=cp%>/fashion/list1.do"><h3>패션-중고거래게시판</h3></a>
	</td>
</tr>
</table>
  
        <div>
			<form name="boardForm" id="boardForm" method="post" enctype="multipart/form-data">
			  <table style="width: 900px; margin: 20px auto 0px; border-spacing: 0px;">
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			
			  <tr align="left" height="40"> 
			      <td width="100" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto1.subject}">
			      </td>
			  </tr>
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>

			  <tr align="left" height="40"> 
			      <td width="100" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			      
			          ${sessionScope.member.mem_Name} 
			      </td>
			  </tr> 
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			  
			    <tr align="left" height="40"> 
			      <td width="100" style="text-align: center;">카테고리</td>
			      <td style="padding-left:10px;"> 
			       <select name="catagory" class="selectField">
                 	 <option value="sell"${dto1.category=="sell"? "selected='selected'":""}>팝니다</option>
                 	 <option value="buy" ${dto1.category=="buy"? "selected='selected'":""}>삽니다</option>
                 	<c:if test="${mode=='update'}">
                 	 <option value="complete" ${dto1.category=="complete"? "selected='selected'":""}>판매완료</option>
                 	</c:if>
          		  </select>
			      </td>
			  </tr>
			  
			    <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			  
	
			  <tr align="left"> 
			      <td width="100" style="text-align: center; padding-top:5px;" valign="top">설&nbsp;&nbsp;&nbsp;&nbsp;명</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			          <textarea id="content" name="content" rows="12" class="boxTA" style="width: 95%;">${dto1.content}</textarea>
			      </td>
			  </tr>
			
			  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
			  </table>
			 
			  <table style="width:900px; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" id="save" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/fashion/list1.do';">${mode=='update'?'수정취소':'등록취소'}</button>

					<c:if test="${mode=='update'}">
					<!-- 히든으로 넘겨주는값들 ==>multipartRequest객체로받아야함-->
					<input type="hidden" name="bbs_num" value="${dto1.bbs_num}">
					<input type="hidden" name="page" value="${page}">
				
					</c:if>

				  </td>
			    </tr>
			  </table>
			</form>
        </div>


</div>


<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript">
 
var oEditors = [];
nhn.husky.EZCreator.createInIFrame({
    oAppRef: oEditors,
    elPlaceHolder: "content",//comtemt: textarea 에  id와 같음
    sSkinURI: "<%=cp%>/resource/editor/SmartEditor2Skin.html",
    htParams : {  // endito파일 경로 맞추기 
    	// 툴바 사용 여부 (true:사용/ false:사용하지 않음) 
    	bUseToolbar : true,	
    	// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음) 
    	bUseVerticalResizer : true,	
    	// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음) 
    	bUseModeChanger : true,	
    	fOnBeforeUnload : function(){ 
    		
    		} 
    	}, 
    	fOnAppLoad : function(){ 
    		//기존 저장된 내용의 text 내용을 에디터상에 뿌려주고자 할때 사용 
    		oEditors.getById["content"].exec("PASTE_HTML", []); 
    		},
    		 
    	    fCreator: "createSEditor2"
    	});
   

 
 
//‘저장’ 버튼을 누르는 등 저장을 위한 액션을 했을 때 submitContents가 호출된다고 가정한다.
/* function submitContents(elClickedObj) {
    // 에디터의 내용이 textarea에 적용된다.
    oEditors.getById["BOARD_CONTENT"].exec("UPDATE_CONTENTS_FIELD", [ ]);
 
    // 에디터의 내용에 대한 값 검증은 이곳에서
    // document.getElementById("BOARD_CONTENT").value를 이용해서 처리한다.
  
    try {
        elClickedObj.form.submit();
    } catch(e) {
     
    }
}  */
$("#save").click(function(){ 
	oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", [""]); 
	$("#boardForm").submit(); 
	})


// textArea에 이미지 첨부
function pasteHTML(filepath){
    var sHTML = '<img src="<%=cp%>/uploads/fashion/'+filepath+'">';
    oEditors.getById["content"].exec("PASTE_HTML", [sHTML]);
}
 
</script>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>	