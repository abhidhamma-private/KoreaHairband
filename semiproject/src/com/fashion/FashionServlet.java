package com.fashion;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.MyServlet;

@WebServlet("/fashion/*")
public class FashionServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("utf-8");
	
	String uri = req.getRequestURI();
	String cp =req.getContextPath();
	
	if(uri.indexOf("list1.do")!=-1){
		list1(req, resp);
	} else if(uri.indexOf("created1.do")!=-1){
		createdForm1(req, resp);
	} else if(uri.indexOf("created1_ok.do")!=-1){
		createdSubmit1(req, resp);
	} else if(uri.indexOf("article1.do")!=-1){
		article1(req, resp);
	} else if(uri.indexOf("update1.do")!=-1){
		updateForm1(req, resp);
	} else if(uri.indexOf("update1_ok.do")!=-1){
		updateSubmit1(req, resp);
	} else if(uri.indexOf("delete1.do")!=-1){
		delete1(req, resp);
	} else if(uri.indexOf("list2.do")!=-1){
		list1(req, resp);
	} else if(uri.indexOf("created2.do")!=-1){
		createdForm1(req, resp);
	} else if(uri.indexOf("created2_ok.do")!=-1){
		createdSubmit1(req, resp);
	} else if(uri.indexOf("article2.do")!=-1){
		article1(req, resp);
	} else if(uri.indexOf("update2.do")!=-1){
		updateForm1(req, resp);
	} else if(uri.indexOf("update2_ok.do")!=-1){
		updateSubmit1(req, resp);
	} else if(uri.indexOf("delete2.do")!=-1){
		delete1(req, resp);
	}
	
	}
	
	protected void list1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String cp =req.getContextPath();
		
		forward(req, resp, "/WEB-INF/views/fashion/list1.jsp");
		  
	}
	protected void createdForm1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");// ±Û¾²±â Æû
		forward(req, resp, "/WEB-INF/views/fashion/created1.jsp");
	
	}
	protected void createdSubmit1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/fashion/list1.do");
	}
	protected void article1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		forward(req, resp, "/WEB-INF/views/fashion/article1.jsp");
		
	}
	protected void updateForm1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	protected void updateSubmit1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}
	
	protected void delete1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}
	

}
