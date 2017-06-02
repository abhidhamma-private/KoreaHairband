$(function(){

	$('.join').click(function(){
		//$(".modalbg1, #joinmodal").fadeIn(800);
	/*	$("#joinmodal").fadeIn(800);*/
		$(".join_modal").css({"display":"block"});
	});
	
	$('.close1').click(function(){
		/*$(".join_modal").fadeOut(800);*/
		$(".join_modal").css({"display":"none"});
	});
	
	
	$('.m_send').click(function(){
		alert("dd");
		$(".modalbg #loginmodal1").fadeIn(800);
		$(".modal").css({"display":""});
	});
	
	$('.close').click(function(){
		$(".modalbg").fadeOut(800);
	});
	
	
	
	
	//left-bar ����
	$(".hamburger").click(function(){
		
		$(".whole").css({"display":"block"});
		$(".left_slide_menu").stop().animate({"margin-left":"0"},700);
	});
	
	//left-bar �ݱ�
	$(".close_menu").click(function(){
		$(".whole").fadeOut(700);
		/* $(".whole").css({"display":"none"}); */
		$(".left_slide_menu").stop().animate({"margin-left":"-300px"},700);
	});
	
});