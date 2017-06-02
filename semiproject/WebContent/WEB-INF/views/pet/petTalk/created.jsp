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
<title>반려동물 정보공유 작성중...</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />

<!-- 아래 2개 인클루드 -->
<script type="text/javascript" src="<%=cp%>/resource/editor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=cp%>/resource/editor/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
function sendOk(){
	var f = document.talkCreateForm;
	
	var str = f.subject.value;
    if(!str) {
        alert("제목을 입력하세요. ");
        f.subject.focus();
        return;
    }

	var mode="${mode}";
	if(mode=="created")
		f.action="<%=cp%>/pet/petTalk/created_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/pet/petTalk/update_ok.do";

    f.submit();
}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 900px;">
        <div class="body-title">
        </div>
        
        <div>
			<form name="talkCreateForm" id="boardForm" method="post" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject}">
			      </td>
			  </tr>
			  <c:if test="${sessionScope.member.mem_Id=='admin'}">
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">공지여부</td>
			      <td style="padding-left:10px;"> 
			        <input type="checkbox" name="notice" value="1" ${dto.notice==1 ? "checked='checked' ":"" } > 공지
			      </td>
			  </tr>
			  </c:if>
	     	  <c:if test="${mode=='created'}">
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">카테고리</td>
			      <td style="padding-left:10px;"> 
			          <select name="category" class="selectField">
			                  <option value="잡담">잡담</option>
			                  <option value="질문">질문</option>
			                  <option value="중고거래">중고거래</option>
			                  <option value="무료나눔">무료나눔</option>
			          </select>
			      </td>
			  </tr>
		      </c:if>
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.mem_Name}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			        <textarea id="content" name="content" rows="12" class="boxTA" style="width: 95%;"><c:if test="${mode=='created'}">${dto.content}</c:if></textarea>
			      </td>
			  </tr>
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" id="save" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/pet/petTalk/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			        
			         <c:if test="${mode=='update'}">
			         	 <input type="hidden" name="bbs_num" value="${dto.bbs_num}">
			        	 <input type="hidden" name="page" value="${page}">
			        </c:if>
			        			        
			        <c:if test="${mode=='reply'}">
			        	<input type="hidden" name="groupNum" value="${dto.groupNum}">
			        	<input type="hidden" name="orderNo" value="${dto.orderNo}">
			        	<input type="hidden" name="depth" value="${dto.depth}">
			        	<input type="hidden" name="parent" value="${dto.bbs_num}">
			        	<input type="hidden" name="page" value="${page}">
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
    var sHTML = '<img src="<%=cp%>/uploads/semi/'+filepath+'">';
    oEditors.getById["content"].exec("PASTE_HTML", [sHTML]);
}
 
</script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>