<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Insert title here</title>

<script type="text/javascript" src="<%=cp%>/resource/editor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=cp%>/resource/editor/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>

<script type="text/javascript">
	function isImage(filename)
	{
		var f = /(\.gif|\.jpg|\.jpeg|\.png)$/i;
		return f.test(filename);
	}

    function sendOk()
    {
        var f = document.boardForm;
    	var mode = "${mode}";

    	var str = f.subject.value;
    	
        if(!str)
        {
            alert("제목을 입력하세요. ");
            f.subject.focus();
            return;
        }
        
        str = f.upload.value;
        
        if(mode == "created" && ! isImage(str) || mode == "update" && str != "" && ! isImage(str))
        {
        	alert("Image를 등록하세요.");
        	f.upload.focus();
        	return;
        }

    	if(mode == "created")
    		f.action = "<%=cp%>/soccer/photo/createdPH_ok.do";
    		
    	else if(mode == "update")
    		f.action = "<%=cp%>/soccer/photo/updatePH_ok.do";

        f.submit();
    }
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div style="margin: 100px;" align="center">
    <div style="width: 700px;">
        <div>
            <h3> Photo Gallery </h3>
        </div>
        
        <div>
			<form id="boardForm" name="boardForm" method="post" enctype="multipart/form-data">
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
				          	${sessionScope.member.mem_Name }
				      </td>
				  </tr>
				  <tr><td colspan="2" height="1" bgcolor="#cccccc"></td></tr>
				
				  <tr align="left"> 
				      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top"> 설&nbsp;&nbsp;&nbsp;&nbsp;명 </td>
				      <td valign="top" style="padding:5px 0px 5px 10px;"> 
				          <textarea name="content" id="content" rows="12" class="boxTA" style="width: 95%;"></textarea>
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
				        <button type="button" id="save" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
				        <button type="reset" class="btn">다시입력</button>
				        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/soccer/photo.do';">${mode=='update'?'수정취소':'등록취소'}</button>
				  		
				  		<c:if test="${mode == 'update' }">
				      		<input type="hidden" name="bbs_num" value="${dto.bbs_num }">
				      		<input type="hidden" name="page" value="${page }">
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
	    elPlaceHolder: "content",//comtent: textarea 에  id와 같음
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