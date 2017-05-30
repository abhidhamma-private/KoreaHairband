package com.health;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.FileManager;
import com.util.MyServlet;
import com.util.MyUtil;
@WebServlet("/health/*")
public class HealthServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	
	private SessionInfo info;
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		String cp=req.getContextPath();
		
		//회원session
		HttpSession session=req.getSession();
		info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// 이미지 저장 경로
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+"photo";
		File f=new File(pathname);
		if(! f.exists())
			f.mkdirs();
		
		if(uri.indexOf("board.do")!=-1) {
			forward(req, resp, "/WEB-INF/views/health/board/list.jsp");
		}
		
		if(uri.indexOf("notice.do")!=-1) {
			notice(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			forward(req, resp, "/WEB-INF/views/health/notice/created.jsp");
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		}
	}
	
	protected void notice(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String cp=req.getContextPath();
		noticeDAO dao=new noticeDAO();
		MyUtil util=new MyUtil();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null)
			current_page=Integer.parseInt(page);
		System.out.println("page : " + page);
		// 전체데이터 개수
		int dataCount=dao.datacount();
		

		// 전체페이지수
		int rows=6;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page=total_page;
		
		// 게시물 가져올 시작과 끝위치
		int start=(current_page-1)*rows+1;
		int end=current_page*rows;
		
		// 게시물 가져오기
		List<noticeDTO> list=dao.listNotice(start, end);
		
		// 페이징 처리
		String listUrl=cp+"/health/list.do";
		String articleUrl = cp + "/health/article.do?page="+current_page;
		String paging=util.paging(current_page, total_page, listUrl);
		
		// 포워딩할 list.jsp에 넘길 값
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		
		String path="/WEB-INF/views/health/notice/list.jsp";
		forward(req, resp, path);	
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		String path="/WEB-INF/views/health/created.jsp";
		forward(req, resp, path);
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		noticeDAO dao=new noticeDAO();
		String encType="utf-8";
		int maxSize=5*1024*1024;
		
		MultipartRequest mreq=new MultipartRequest(
				req, pathname, maxSize, encType,
				new DefaultFileRenamePolicy());
		System.out.println(pathname);
		noticeDTO dto=new noticeDTO();
		if(mreq.getFile("upload")!=null) {
			dto.setMem_id(info.getMem_Id());
			dto.setSubject(mreq.getParameter("subject"));
			dto.setContent(mreq.getParameter("content"));
			String saveFilename=mreq.getFilesystemName("upload");
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setSavefilename(saveFilename);
			
			dao.insertNotice(dto);
		}
		resp.sendRedirect(cp+"/health/notice.do");
		
		
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		
		noticeDAO dao=new noticeDAO();
		
		int num=Integer.parseInt(req.getParameter("num"));
		String page=req.getParameter("page");
		
		noticeDTO dto=dao.readnotice(num);
		if(dto==null) {
			resp.sendRedirect(cp+"/health/list.do?page="+page);
			return;
		}
		
/*		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));*/
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		String path="/WEB-INF/views/health/notice/article.jsp";
		forward(req, resp, path);
	}

}
