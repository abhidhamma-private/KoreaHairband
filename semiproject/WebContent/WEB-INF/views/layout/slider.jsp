<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>
<div id="slider">
	<figure>
		<img src="<%=cp%>/resource/img/slider1.png" alt="">
		<img src="<%=cp%>/resource/img/slider2.png" alt="">
		<img src="<%=cp%>/resource/img/slider3.png" alt="">
		<img src="<%=cp%>/resource/img/slider4.png" alt="">
	</figure>
</div>
