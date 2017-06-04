<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/resource/css/setting.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style type="text/css">
.imgLayout {
	width: 190px;
	height: 205px;
	padding: 10px 5px 10px;
	margin: 5px;
	border: 1px solid #DAD9FF;
}

.subject {
	width: 180px;
	height: 25px;
	line-height: 25px;
	margin: 5px auto;
	border-top: 1px solid #DAD9FF;
	display: inline-block;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	cursor: pointer;
}
</style>

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">
	function article(bbs_num) {
		var url = "${articleUrl}&bbs_num=" + bbs_num;
		location.href = url;
	}
</script>

</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="soccerphoto" align="center">
		<div class="body-container" style="width: 630px;">
			<!-- 로고 이미지 -->
			<img src="<%=cp%>/resource/img/m_photoboard.png" style="margin: 10px">
			<div align="center">

				<table style="width: 630px; margin: 0px auto; border-spacing: 0px;">
					<c:forEach var="pdto" items="${listPhoto}" varStatus="status">
						<c:if test="${status.index==0}">
							<tr>
						</c:if>
						<c:if test="${status.index!=0 && status.index%3==0}">
							<c:out value="</tr><tr>" escapeXml="false" />
						</c:if>
						<td width="210" align="center">
							<div class="imgLayout">
								<img src="<%=cp%>/uploads/semi/${pdto.imageFilename}"
									width="180" height="180" border="0"> <span
									class="subject"
									onclick="javascript:article('${pdto.bbs_num }');">
									${pdto.subject}<br>
								</span>
							</div>
						</td>
					</c:forEach>

					<c:set var="n" value="${list.size()}" />
					<c:if test="${n>0 && n%3!=0}">
						<c:forEach var="i" begin="${n%3+1}" end="3" step="1">
							<td width="210">
								<div class="imgLayout">&nbsp;</div>
							</td>
						</c:forEach>
					</c:if>

					<c:if test="${n!=0 }">
						<c:out value="</tr>" escapeXml="false" />
					</c:if>
				</table>

				<table style="width: 100%; border-spacing: 0px;">
					<tr height="50">
						<td align="center"><c:if test="${dataCount==0}">
                   		등록된 게시물이 없습니다.
                   </c:if> <c:if test="${dataCount!=0}">
                   		${paging}
                   </c:if></td>
					</tr>
				</table>

				<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
					<tr height="40">
						<td align="left" width="100">&nbsp;</td>
						<td align="center">&nbsp;</td>
						<td align="right" width="100">
							<button type="button" class="btn"
								onclick="javascript:location.href='<%=cp%>/soccer/photo/createdPH.do';">등록하기</button>
						</td>
					</tr>
				</table>

			</div>
		</div>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>