<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/resource/css/setting.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유게시판</title>

<link rel="stylesheet" type="text/css" href="<%=cp%>/css/header.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/content.css" />
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/footer.css" />
<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>



</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="border: 0px;resize:none">
		<!-- 로고 이미지 -->
		<img src="<%=cp%>/resource/img/f_proposal.png" style="margin: 10px"><br>
        <div class="body-title">
        <textarea rows="39px" cols="125px" style="border: 0px;resize:none" >

제휴제안 안내
KoreaHairand홈페이지-제휴제안 메뉴에서 
제휴제안 내용을 등록합니다.
등록 완료 메일이 입력하신 메일 주소로 발송됩니다.
담당자와의 면담을 전제로 한 제휴제안은 지양해 주십시오.
특허 및 디자인, 저작권 등 각종 지식재산권의 공개 내지 공유가
필요한 제휴 요청의 경우 반드시 권리 등록을 완료하시고 당사로 
제휴제안 요청을 하여 주시기 바랍니다.
제휴제안서에는 제안자의 영업비밀이나 기밀사항에 해당하지 
않는 '공개가 가능한 내용'만 서술하여 주시기 바랍니다.


제휴제안 접수
KoreaHairand에서 제휴제안 내용을 접수하고, 
접수가 완료되었음을 제안자의 메일로 안내해 드립니다.
제휴제안 재등록 요청시(첨부 파일 이상 등)에는 
제휴제안 내용을 수정 또는 보완해주십시오


제휴제안 검토
KoreaHairand의 담당자가 등록하신 제휴제안 내용을 
면밀히 검토합니다.
등록된 제휴제안 내용은 5일 이내에 처리하는 것을 
기본으로 하고 있습니다.
(단, 주요 사안의 경우 시간이 조금 더 소요될 수 있습니다.)
담당자가 제휴내용을 검토 중에 추가로 필요한 자료가 있을 경우, 
제휴제안 재문의를 하게 되며, 이 때 필요한 자료를 
추가로 등록해주시면 됩니다.
제안 내용 및 관련자료는 제휴 검토 목적으로만 이용됩니다.
			

제휴제안 종료
제휴제안 내용의 검토 결과를 회신 드립니다.
검토 완료 메일이 입력하신 메일 주소로 발송됩니다.
제휴 진행이 결정되면 , 담당자가 제휴를 위한 
별도 절차를 안내하고 해당 제안 건을 종료합니다.
제휴제안이 받아들여지지 않은 경우
제휴제안 내용 및 관련 자료는 즉시 파기됩니다.
</textarea>
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