package com.fashion;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.MyServlet;

@WebServlet("/fashion/*")
public class FashionServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	private SessionInfo info;
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("utf-8");
	
	String uri = req.getRequestURI();
	String cp =req.getContextPath();
	

	HttpSession session=req.getSession();
	info=(SessionInfo)session.getAttribute("member");
	if(info==null) {
		resp.sendRedirect(cp+"/member/login.do");
		return;
	}
	
	
	// 파일을 저장할 경로(pathname)
	String root=session.getServletContext().getRealPath("/");
	pathname=root+File.separator+"uploads"+File.separator+"fashion";
	File f=new File(pathname);
	if(! f.exists()) {
		f.mkdirs();
	}
	
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
		req.setAttribute("mode", "created");// 글쓰기 폼
		forward(req, resp, "/WEB-INF/views/fashion/created1.jsp");
	
	}
	protected void createdSubmit1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		Fashion1DAO dao1 = new Fashion1DAO();
		Fashion1DTO dto1 = new Fashion1DTO();
		
		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
	    MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
		dto1.setCategory(mreq.getParameter("catagory"));
		dto1.setMem_Id(info.getMem_Id());
		dto1.setSubject(mreq.getParameter("subject"));
		dto1.setContent(mreq.getParameter("content"));
	//	dto1.setCategory(mreq.getParameter("category"));
		
		if(mreq.getParameter("notice")!=null)//공지인경우
		    	dto1.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		  
		dao1.insertBoard(dto1);
		
		
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
