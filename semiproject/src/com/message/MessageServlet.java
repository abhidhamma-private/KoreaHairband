package com.message;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/message/*")
public class MessageServlet extends MyServlet{
	private static final long serialVersionUID = 1L;
	private SessionInfo info;

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
		
		if(uri.indexOf("m_list.do")!=-1){
			m_list(req, resp);
		} else if(uri.indexOf("m_created.do")!=-1){
			m_SendForm(req, resp);
		} else if(uri.indexOf("m_created_ok.do")!=-1){
			m_SendSubmint(req, resp);
		} else if(uri.indexOf("m_article.do")!=-1){
			m_Article(req, resp);
		}else if(uri.indexOf("m_delete.do")!=-1){
			m_Delete(req, resp);
		} else if(uri.indexOf("m_deleteList.do")!=-1){
			m_DeleteList(req, resp);
		}
			
	}
	
	protected void m_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String cp =req.getContextPath();
		
		MessageDAO dao = new MessageDAO();
		MyUtil util = new MyUtil();
		
		int dataCount = dao.messageCount(info.getMem_Id());
		System.out.println(info.getMem_Id());
		//String page = req.getParameter("page");
		int current_page =1;
		int rows=10;
		
		int total_page = util.pageCount(rows, dataCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;
		
		
		//쪽지함 리스트
		List<MessageDTO> list = dao.listMessage(start, end, info.getMem_Id());
		
		// 메세지 리스트 주소
		String listUrl = cp + "/message/m_list.do";
		// 글보기 주소
		String articleUrl = cp + "/message/m_article.do?page=" + current_page;
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		
		forward(req, resp, "/WEB-INF/views/message/m_list.jsp");

	
	}
	protected void m_SendForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	protected void m_SendSubmint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	protected void m_Article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	protected void m_Delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	protected void m_DeleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	
}
