package com.health;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
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
		String cp = req.getContextPath();

		// ȸ��session
		HttpSession session = req.getSession();
		info = (SessionInfo) session.getAttribute("member");
		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// �̹���������
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "photo";
		File f = new File(pathname);
		if (!f.exists())
			f.mkdirs();

		// notice
		if (uri.indexOf("notice.do") != -1) {
			notice(req, resp);
			return;
		} else if (uri.indexOf("created.do") != -1) {
			forward(req, resp, "/WEB-INF/views/health/notice/created.jsp");
		} else if (uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}

		// board
		if (uri.indexOf("board.do") != -1) {
			board(req, resp);
		} else if (uri.indexOf("createdB.do") != -1) {
			createdFormB(req, resp);
		} else if (uri.indexOf("created_okB.do") != -1) {
			createdSubmitB(req, resp);
		} else if (uri.indexOf("articleB.do") != -1) {
			articleB(req, resp);
		} else if (uri.indexOf("updateB.do") != -1) {
			updateFormB(req, resp);
		} else if (uri.indexOf("update_okB.do") != -1) {
			updateSubmitB(req, resp);
		} else if (uri.indexOf("deleteB.do") != -1) {
			deleteB(req, resp);
		}
	}

	// notice
	protected void notice(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		noticeDAO dao = new noticeDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		if (page == null) {
			page = "1";
		}
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		int dataCount = dao.datacount();

		int rows = 4;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page)
			current_page = total_page;

		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;

		List<noticeDTO> list = dao.listNotice(start, end);

		String listUrl = cp + "/health/notice.do";
		String articleUrl = cp + "/health/article.do?page=" + current_page;
		String paging = util.paging(current_page, total_page, listUrl);

		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);

		String path = "/WEB-INF/views/health/notice/list.jsp";
		forward(req, resp, path);
	}

	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		String path = "/WEB-INF/views/health/notice/created.jsp";
		forward(req, resp, path);
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cp = req.getContextPath();
		noticeDAO dao = new noticeDAO();
		String encType = "utf-8";
		int maxSize = 5 * 1024 * 1024;

		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());

		noticeDTO dto = new noticeDTO();
		if (mreq.getFile("upload") != null) {
			dto.setMem_id(info.getMem_Id());
			dto.setSubject(mreq.getParameter("subject"));
			dto.setContent(mreq.getParameter("content"));
			String saveFilename = mreq.getFilesystemName("upload");
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setSavefilename(saveFilename);

			dao.insertNotice(dto);
		}
		resp.sendRedirect(cp + "/health/notice.do");

	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();

		noticeDAO dao = new noticeDAO();

		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");

		noticeDTO dto = dao.readnotice(num);
		if (dto == null) {
			resp.sendRedirect(cp + "/health/list.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);

		String path = "/WEB-INF/views/health/notice/article.jsp";
		forward(req, resp, path);
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		noticeDAO dao = new noticeDAO();

		String page = req.getParameter("page");

		int num = Integer.parseInt(req.getParameter("num"));
		noticeDTO dto = dao.readnotice(num);

		if (dto == null) {
			resp.sendRedirect(cp + "/health/notice.do?page=" + page);
			return;
		}

		if (!dto.getMem_id().equals(info.getMem_Id())) {
			resp.sendRedirect(cp + "/health/notice.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);

		req.setAttribute("mode", "update");
		String path = "/WEB-INF/views/health/notice/created.jsp";
		forward(req, resp, path);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String cp = req.getContextPath();
		noticeDAO dao = new noticeDAO();

		String encType = "utf-8";
		int maxSize = 5 * 1024 * 1024;

		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());

		String page = mreq.getParameter("page");
		String imageFilename = mreq.getParameter("imageFilename");

		noticeDTO dto = new noticeDTO();

		dto.setBbs_num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		if (mreq.getFile("upload") != null) {
			FileManager.doFiledelete(pathname, imageFilename);

			String saveFilename = mreq.getFilesystemName("upload");

			saveFilename = FileManager.doFilerename(pathname, saveFilename);

			dto.setSavefilename(saveFilename);
		} else {
			dto.setSavefilename(imageFilename);
		}

		dao.updateNotice(dto);

		resp.sendRedirect(cp + "/health/notice.do?page=" + page);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		noticeDAO dao = new noticeDAO();

		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");

		noticeDTO dto = dao.readnotice(num);
		if (dto == null) {
			resp.sendRedirect(cp + "/health/notice.do?page=" + page);
			return;
		}

		if (!dto.getMem_id().equals(info.getMem_Id()) && !info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/health/notice.do?page=" + page);
			return;
		}

		FileManager.doFiledelete(pathname, dto.getSavefilename());

		dao.deleteNotice(num);

		resp.sendRedirect(cp + "/health/notice.do?page=" + page);
	}

	// board
	protected void board(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		boardDAO dao = new boardDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();

		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		int dataCount = dao.dataCount();
		int rows = 8;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page)
			current_page = total_page;
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;

		List<boardDTO> list;
		list = dao.listBoard(start, end);

		int listNum, n = 0;
		Iterator<boardDTO> it = list.iterator();
		while (it.hasNext()) {
			boardDTO dto = it.next();
			listNum = dataCount - (start + n - 1);
			dto.setListNum(listNum);
			n++;
		}
		String listUrl = cp + "/health/board.do";
		String articleUrl = cp + "/health/articleB.do?page=" + current_page;
		String paging = util.paging(current_page, total_page, listUrl);

		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);

		forward(req, resp, "/WEB-INF/views/health/board/list.jsp");
	}

	protected void createdFormB(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");

		forward(req, resp, "/WEB-INF/views/health/board/created.jsp");
	}

	protected void createdSubmitB(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cp = req.getContextPath();
		boardDAO dao = new boardDAO();
		String encType = "utf-8";
		int maxSize = 5 * 1024 * 1024;

		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());

		boardDTO dto = new boardDTO();
		if (mreq.getFile("upload") != null) {
			dto.setMem_Id(info.getMem_Id());
			dto.setSubject(mreq.getParameter("subject"));
			dto.setContent(mreq.getParameter("content"));
			String saveFilename = mreq.getFilesystemName("upload");
			saveFilename = FileManager.doFilerename(pathname, saveFilename);

			dto.setSavefilename(saveFilename);

			dao.insertBoard(dto, "created");
		} else {
			dto.setMem_Id(info.getMem_Id());
			dto.setSubject(mreq.getParameter("subject"));
			dto.setContent(mreq.getParameter("content"));
			dto.setSavefilename("���Ͼ���");

			dao.insertBoard(dto, "created");
		}

		resp.sendRedirect(cp + "/health/board.do");
		return;
	}

	protected void articleB(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();

		boardDAO dao = new boardDAO();

		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");
		dao.updatehitCount(num);
		boardDTO dto = dao.readboard(num);
		if (dto == null) {
			resp.sendRedirect(cp + "/health/list.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);

		String path = "/WEB-INF/views/health/board/article.jsp";
		forward(req, resp, path);
	}

	protected void updateFormB(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		boardDAO dao = new boardDAO();

		String page = req.getParameter("page");

		int num = Integer.parseInt(req.getParameter("num"));
		boardDTO dto = dao.readboard(num);

		if (dto == null) {
			resp.sendRedirect(cp + "/health/board.do?page=" + page);
			return;
		}

		if (!dto.getMem_Id().equals(info.getMem_Id())) {
			resp.sendRedirect(cp + "/health/board.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);

		req.setAttribute("mode", "update");
		String path = "/WEB-INF/views/health/board/created.jsp";
		forward(req, resp, path);
	}

	protected void updateSubmitB(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cp = req.getContextPath();
		boardDAO dao = new boardDAO();

		String encType = "utf-8";
		int maxSize = 5 * 1024 * 1024;

		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());

		String page = mreq.getParameter("page");
		String imageFilename = mreq.getParameter("imageFilename");

		boardDTO dto = new boardDTO();

		dto.setBbs_Num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		if (mreq.getFile("upload") != null) {
			FileManager.doFiledelete(pathname, imageFilename);
			String saveFilename = mreq.getFilesystemName("upload");
			saveFilename = FileManager.doFilerename(pathname, saveFilename);

			dto.setSavefilename(saveFilename);
		} else {
			dto.setSavefilename(imageFilename);
		}

		dao.updateBoard(dto);
		resp.sendRedirect(cp + "/health/board.do?page=" + page);
	}

	protected void deleteB(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		boardDAO dao = new boardDAO();

		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");

		boardDTO dto = dao.readboard(num);
		if (dto == null) {
			resp.sendRedirect(cp + "/health/board.do?page=" + page);
			return;
		}

		if (!dto.getMem_Id().equals(info.getMem_Id()) && !info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/health/board.do?page=" + page);
			return;
		}

		FileManager.doFiledelete(pathname, dto.getSavefilename());

		dao.deleteBoard(num);

		resp.sendRedirect(cp + "/health/board.do?page=" + page);
	}

}
