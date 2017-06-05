package com.notice;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/notice/*")
public class NoticeServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path)
			throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		if (info == null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		if (uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if (uri.indexOf("created.do") != -1) {
			createdForm(req, resp);
		} else if (uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			deleted(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();

		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if (searchKey == null) {
			searchKey = "subject";
			searchValue = "";
		}
		if (req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
		}

		int rows = 10;
		int dataCount, total_page;

		if (searchValue.length() != 0) {
			dataCount = dao.dataCount(searchKey, searchValue);
		} else
			dataCount = dao.dataCount();
		total_page = util.pageCount(rows, dataCount);

		if (current_page > total_page)
			current_page = total_page;

		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;

		List<NoticeDTO> list;
		if (searchValue.length() != 0)
			list = dao.listNotice(searchKey, searchValue, start, end);
		else
			list = dao.listNotice(start, end);

		/*
		 * listNotice = dao.listNotice(); Iterator<NoticeDTO> itNotice =
		 * listNotice.iterator();
		 * 
		 * while (itNotice.hasNext()) { NoticeDTO dto = itNotice.next();
		 * dto.setCreated(dto.getCreated().substring(0, 10));* }
		 */

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long gap;
		Date date = new Date();
		int listNum, n = 0;
		Iterator<NoticeDTO> it = list.iterator();

		while (it.hasNext()) {
			NoticeDTO dto = it.next();
			listNum = dataCount - (start + n - 1);
			dto.setListNum(listNum);
			try {
				Date sDate = format.parse(dto.getCreated());
				gap = (date.getTime() - sDate.getTime()) / (60 * 60 * 1000); // 60*60*1000
																				// ->
																				// 1시간
																				// .getTime
																				// ->
																				// 밀리세컨드
				dto.setCreated(dto.getCreated().substring(0, 10));
				dto.setGap(gap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			n++;
		}

		String query = "";
		if (searchValue.length() != 0) {
			// 검색인 경우는 인코딩 필요
			searchValue = URLEncoder.encode(searchValue, "utf-8");
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
		}

		String listUrl = cp + "/notice/list.do";
		String articleUrl = cp + "/notice/article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		String paging = util.paging(current_page, total_page, listUrl);

		req.setAttribute("list", list);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);

		forward(req, resp, "/WEB-INF/views/notice/list.jsp");
	}

	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();

		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// admin만 글을 등록
		if (!info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/notice/list.do");
			return;
		}

		req.setAttribute("mode", "created");
		String path = "/WEB-INF/views/notice/created.jsp";
		forward(req, resp, path);
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();

		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// 관리자만 글을 등록할 수 있게
		if (!info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/notice/list.do");
			return;
		}

		NoticeDTO dto = new NoticeDTO();

		dto.setMem_id(info.getMem_Id());
		if (req.getParameter("notice") != null)
			dto.setNotice(Integer.parseInt(req.getParameter("notice")));
		dto.setSubject(req.getParameter("subject"));
		dto.setContent(req.getParameter("content"));

		dao.insertNotice(dto);

		resp.sendRedirect(cp + "/notice/list.do");

	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();

		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		int not_num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");

		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if (searchKey == null) {
			searchKey = "subject";
			searchValue = "";
		}
		searchValue = URLDecoder.decode(searchValue, "utf-8");

		// 조회수
		dao.updateHitCount(not_num);

		// 게시물 가져오기
		NoticeDTO dto = dao.readNotice(not_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/notice/list.do?page=" + page);
			return;
		}

		/* dto.setContent(dto.getContent().replaceAll("\n", "<br>")); */

		// 이전글/다음글
		NoticeDTO preRead = dao.preReadNotice(dto.getNot_num(), searchKey, searchValue);
		NoticeDTO nextRead = dao.nextReadNotice(dto.getNot_num(), searchKey, searchValue);

		String query = "page=" + page;
		if (searchValue.length() != 0) {
			query += "&searchKey=" + searchKey;
			query += "&searchValue=" + URLEncoder.encode(searchValue, "utf-8");
		}

		req.setAttribute("dto", dto);
		req.setAttribute("preRead", preRead);
		req.setAttribute("nextRead", nextRead);
		req.setAttribute("query", query);
		req.setAttribute("page", page);

		String path = "/WEB-INF/views/notice/article.jsp";
		forward(req, resp, path);
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();

		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		String page = req.getParameter("page");
		int num = Integer.parseInt(req.getParameter("num"));

		NoticeDTO dto = dao.readNotice(num);
		if (dto == null) {
			resp.sendRedirect(cp + "/notice/list.do?page=" + page);
			return;
		}

		// 글을 등록한 사람만 수정 가능
		if (!info.getMem_Id().equals(dto.getMem_id())) {
			resp.sendRedirect(cp + "/notice/list.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);

		req.setAttribute("mode", "update");
		String path = "/WEB-INF/views/notice/created.jsp";
		forward(req, resp, path);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();

		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		// 관리자만 글을 등록할 수 있게
		if (!info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/notice/list.do");
			return;
		}

		NoticeDTO dto = new NoticeDTO();

		dto.setMem_id(info.getMem_Id());
		if (req.getParameter("notice") != null)
			dto.setNotice(Integer.parseInt(req.getParameter("notice")));
		dto.setSubject(req.getParameter("subject"));
		dto.setContent(req.getParameter("content"));

		dao.insertNotice(dto);

		resp.sendRedirect(cp + "/notice/list.do");
		

	}

	protected void deleted(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();

		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		int not_num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");

		NoticeDTO dto = dao.readNotice(not_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/notice/list.do?page=" + page);
			return;
		}

		// 글 등록한 사람, admin 만 삭제 가능

		if (!info.getMem_Id().equals(dto.getMem_id()) && !info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/notice/list.do?page=" + page);
			return;
		}

		dao.deleteNotice(not_num);

		resp.sendRedirect(cp + "/notice/list.do?page=" + page);
	}
}
