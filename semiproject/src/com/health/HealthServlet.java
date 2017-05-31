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
		
		//ȸ��session
		HttpSession session=req.getSession();
		info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// �̹��� ���� ���
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+"photo";
		File f=new File(pathname);
		if(! f.exists())
			f.mkdirs();
		
		//notice
		if(uri.indexOf("notice.do")!=-1) {
			notice(req, resp);
			return;
		} else if(uri.indexOf("health/created.do")!=-1) {
			forward(req, resp, "/WEB-INF/views/health/notice/created.jsp");
			return;
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("health/article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("health/update.do")!=-1) {
			updateForm(req, resp);
		}  else if(uri.indexOf("health/update_ok.do")!=-1) {
			System.out.println("������Ʈ�����̿Դ�.");
			updateSubmit(req, resp);
		}  else if(uri.indexOf("health/delete.do")!=-1) {
			delete(req, resp);
		}
		
		
		//board
		if(uri.indexOf("health/board.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("health/Bcreated.do")!=-1) {
			createdFormB(req, resp);
			
		}  else if(uri.indexOf("health/Bcreated_ok.do")!=-1) {
			createdSubmitB(req, resp);
		}
		

	}
	//board
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		forward(req, resp, "/WEB-INF/views/health/board/list.jsp");
		
		
	}
	
	protected void createdFormB(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/health/board/created.jsp");
		//������ set�������. ����Ʈ ������ ������ ǥ�����ش�.
		
	}
	
	protected void createdSubmitB(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		boardDAO dao=new boardDAO();
		String encType="utf-8";
		int maxSize=5*1024*1024;
		
		MultipartRequest mreq=new MultipartRequest(
				req, pathname, maxSize, encType,
				new DefaultFileRenamePolicy());
		System.out.println(pathname);
		boardDTO dto=new boardDTO();
		if(mreq.getFile("upload")!=null) {
			dto.setMem_Id(info.getMem_Id());
			dto.setSubject(mreq.getParameter("subject"));
			dto.setContent(mreq.getParameter("content"));
			String saveFilename=mreq.getFilesystemName("upload");
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setSavefilename(saveFilename);
			
			dao.insertBoard(dto, "created");
		}
		resp.sendRedirect(cp+"/health/board.do");
		return;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//notice
	protected void notice(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String cp=req.getContextPath();
		noticeDAO dao=new noticeDAO();
		MyUtil util=new MyUtil();
		
		String page=req.getParameter("page");
		if(page==null) {
			page = "1";
		}
		int current_page=1;
		if(page!=null)
			current_page=Integer.parseInt(page);
		
		// ��ü������ ����
		int dataCount=dao.datacount();
		

		// ��ü��������
		int rows=4;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page=total_page;
		
		// �Խù� ������ ���۰� ����ġ
		int start=(current_page-1)*rows+1;
		int end=current_page*rows;
		
		// �Խù� ��������
		List<noticeDTO> list=dao.listNotice(start, end);
		
		// ����¡ ó��
		String listUrl=cp+"/health/notice.do";
		String articleUrl = cp + "/health/article.do?page="+current_page;
		String paging=util.paging(current_page, total_page, listUrl);
		
		// �������� list.jsp�� �ѱ� ��
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
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		noticeDAO dao=new noticeDAO();
		
		int num=Integer.parseInt(req.getParameter("num"));
		String page=req.getParameter("page");
		
		noticeDTO dto=dao.readnotice(num);
		if(dto==null) {
			resp.sendRedirect(cp+"/health/notice.do?page="+page);	
			return;
		}
		
		// �Խù��� �ø� ����ڳ� admin�� �ƴϸ�
		if(! dto.getMem_id().equals(info.getMem_Id()) && ! info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp+"/health/notice.do?page="+page);
			return;
		}
		
		// �̹��� ���� �����
		FileManager.doFiledelete(pathname, dto.getSavefilename());
		
		// ���̺� ������ ����
		dao.deleteNotice(num);
		
		resp.sendRedirect(cp+"/health/notice.do?page="+page);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		noticeDAO dao=new noticeDAO();
	
		String page=req.getParameter("page");
		System.out.println("page : " +page);
		int num=Integer.parseInt(req.getParameter("num"));
		noticeDTO dto=dao.readnotice(num);
		
		
		
		if(dto==null) {
			resp.sendRedirect(cp+"/health/notice.do?page="+page);
			return;
		}
		
		// �Խù��� �ø� ����ڰ� �ƴϸ�
		if(! dto.getMem_id().equals(info.getMem_Id())) {
			resp.sendRedirect(cp+"/health/notice.do?page="+page);
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		req.setAttribute("mode", "update");
		String path="/WEB-INF/views/health/notice/created.jsp";
		forward(req, resp, path);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("������Ʈ����ԿԴ�.");
		String cp=req.getContextPath();
		noticeDAO dao=new noticeDAO();
		
		String encType="utf-8";
		int maxSize=5*1024*1024;
		
		MultipartRequest mreq=new MultipartRequest(
				req, pathname, maxSize, encType,
				new DefaultFileRenamePolicy());
		
		String page=mreq.getParameter("page");
		String imageFilename=mreq.getParameter("imageFilename");
		
		noticeDTO dto=new noticeDTO();
		System.out.println("rrrrr"+mreq.getParameter("bbs_num"));
		dto.setBbs_num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		
		// �̹��� ������ ���ε� �Ѱ��
		if(mreq.getFile("upload")!=null) {
			// ���� �̹��� ���� �����
			FileManager.doFiledelete(pathname, imageFilename);
			
			// ������ ����� ���ϸ�
			String saveFilename=mreq.getFilesystemName("upload");
			
			// ���� �̸� ����
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			
			dto.setSavefilename(saveFilename);
		} else {
			// ���ο� �̹��� ������ �ø��� ���� ��� ���� �̹��� ���Ϸ�
			dto.setSavefilename(imageFilename);
		}
		
		int result = dao.updateNotice(dto);
		System.out.println("result : " + result);
		resp.sendRedirect(cp+"/health/notice.do?page="+page);	
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
