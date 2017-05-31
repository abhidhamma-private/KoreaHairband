package com.it;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/it/*")
public class ItServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		if (info == null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// uri에 따른 작업
		if (uri.indexOf("board.do") != -1) {
			board(req, resp);
		} else if (uri.indexOf("board_created.do") != -1) {
			board_createdForm(req, resp);
		} else if (uri.indexOf("board_created_ok.do") != -1) {
			board_createdSubmit(req, resp);
		} else if (uri.indexOf("board_article.do") != -1) {
			board_article(req, resp);
		} else if (uri.indexOf("board_update.do") != -1) {
			board_updateForm(req, resp);
		} else if (uri.indexOf("board_update_ok.do") != -1) {
			board_updateSubmit(req, resp);
		} else if (uri.indexOf("board_reply.do") != -1) {
			board_replyForm(req, resp);
		} else if (uri.indexOf("board_reply_ok.do") != -1) {
			board_replySubmit(req, resp);
		} else if (uri.indexOf("board_delete.do") != -1) {
			board_delete(req, resp);
		} else if (uri.indexOf("board_like.do") != -1) {
			board_like(req, resp);
		/*} else if (uri.indexOf("news_list.do") != -1) {
			news_list(req, resp);
		} else if (uri.indexOf("news_created.do") != -1) {
			news_createdForm(req, resp);
		} else if (uri.indexOf("news_created_ok.do") != -1) {
			news_createdSubmit(req, resp);
		} else if (uri.indexOf("news_article.do") != -1) {
			news_article(req, resp);
		} else if (uri.indexOf("news_update.do") != -1) {
			news_updateForm(req, resp);
		} else if (uri.indexOf("news_update_ok.do") != -1) {
			news_updateSubmit(req, resp);
		} else if (uri.indexOf("news_reply.do") != -1) {
			news_replyForm(req, resp);
		} else if (uri.indexOf("news_reply_ok.do") != -1) {
			news_replySubmit(req, resp);
		} else if (uri.indexOf("news_delete.do") != -1) {
			news_delete(req, resp);*/
		}
	}

	private void board(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		// 검색
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if (searchKey == null) {
			searchKey = "subject";
			searchValue = "";
		}
		// GET 방식인 경우 디코딩
		if (req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
		}

		// 전체 데이터 개수
		int dataCount;
		if (searchValue.length() == 0)
			dataCount = dao.dataCount();
		else
			dataCount = dao.dataCount(searchKey, searchValue);

		// 전체 페이지 수
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);

		if (current_page > total_page)
			current_page = total_page;

		// 게시물 가져올 시작과 끝
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;

		// 게시물 가져오기
		List<BoardDTO> list = null;
		if (searchValue.length() == 0)
			list = dao.listBoard(start, end);
		else
			list = dao.listBoard(start, end, searchKey, searchValue);

		// 리스트 글번호 만들기
		int listNum, n = 0;
		Iterator<BoardDTO> it = list.iterator();
		while (it.hasNext()) {
			BoardDTO dto = it.next();
			listNum = dataCount - (start + n - 1);
			dto.setListNum(listNum);
			n++;
		}

		String query = "";
		if (searchValue.length() != 0) {
			// 검색인 경우 검색값 인코딩
			searchValue = URLEncoder.encode(searchValue, "utf-8");
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
		}

		// 페이징 처리
		String listUrl = cp + "/it/board.do";
		String articleUrl = cp + "/it/board_article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}

		String paging = util.paging(current_page, total_page, listUrl);

		// 포워딩할 JSP로 넘길 속성
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);

		// JSP로 포워딩
		String path = "/WEB-INF/views/it/board/list.jsp";
		forward(req, resp, path);
	}

	private void board_createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		req.setAttribute("mode", "created");
		String path = "/WEB-INF/views/it/board/created.jsp";
		forward(req, resp, path);
	}

	private void board_createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 저장
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();

		// mem_Id는 세션에 저장된 정보
		dto.setMem_Id(info.getMem_Id());

		// 파라미터
		dto.setCategory(req.getParameter("category"));
		dto.setSubject(req.getParameter("subject"));
		dto.setContent(req.getParameter("content"));

		dao.insertBoard(dto, "created");

		resp.sendRedirect(cp + "/it/board.do");
	}

	private void board_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 보기
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if (searchKey == null) {
			searchKey = "subject";
			searchValue = "";
		}

		searchValue = URLDecoder.decode(searchValue, "utf-8");

		// 조회수 증가
		dao.updateHitCount(bbs_num);

		// 게시물 가져오기
		BoardDTO dto = dao.readBoard(bbs_num);
		
		if (dto == null) { // 게시물이 없으면 다시 리스트로
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		dto.setLike(dao.countLike(bbs_num));
		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

		// 이전글 다음글
		BoardDTO preReadDto = dao.preReadBoard(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		BoardDTO nextReadDto = dao.nextReadBoard(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		// 리스트나 이전글/다음글에서 사용할 파라미터
		String query = "page=" + page;
		if (searchValue.length() != 0) {
			query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "utf-8");
		}

		// JSP로 전달할 속성
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);

		// 포워딩
		String path = "/WEB-INF/views/it/board/article.jsp";
		forward(req, resp, path);
	}

	private void board_updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		BoardDTO dto = dao.readBoard(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		// 게시물을 올린 사용자가 아니면
		if (!dto.getMem_Id().equals(info.getMem_Id())) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");

		String path = "/WEB-INF/views/it/board/created.jsp";
		forward(req, resp, path);
	}

	private void board_updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		String page = req.getParameter("page");

		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		BoardDTO dto = new BoardDTO();
		dto.setBbs_num(Integer.parseInt(req.getParameter("bbs_num")));
		dto.setSubject(req.getParameter("subject"));
		dto.setContent(req.getParameter("content"));

		dao.updateBoard(dto, info.getMem_Id());

		resp.sendRedirect(cp + "/it/board.do?page=" + page);
	}

	private void board_replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변폼
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		BoardDTO dto = dao.readBoard(bbs_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}
		dto.setContent("[" + dto.getSubject() + "] 에 대한 답변입니다.\n");

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "reply");

		String path = "/WEB-INF/views/it/board/created.jsp";
		forward(req, resp, path);
	}

	private void board_replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변완료
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		// 파라미터:subject,content,page,
		// groupNum(부의 groupNum),
		// depth(부의 depth),orderNo(부의 orderNo),
		// parent(부의 bbs_num)
		String page = req.getParameter("page");

		BoardDTO dto = new BoardDTO();
		dto.setMem_Id(info.getMem_Id());

		dto.setSubject(req.getParameter("subject"));
		dto.setContent(req.getParameter("content"));
		dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
		dto.setDepth(Integer.parseInt(req.getParameter("depth")));
		dto.setOrderNo(Integer.parseInt(req.getParameter("orderNo")));
		dto.setParent(Integer.parseInt(req.getParameter("parent")));

		dao.insertBoard(dto, "reply");

		resp.sendRedirect(cp + "/it/board.do?page=" + page);
	}

	private void board_delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		BoardDTO dto = dao.readBoard(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		// 게시물을 올린 사용자나 admin이 아니면
		if (!dto.getMem_Id().equals(info.getMem_Id()) && !info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		dao.deleteBoard(bbs_num);
		resp.sendRedirect(cp + "/it/board.do?page=" + page);
	}
	
	private void board_like(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 좋아요
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		int page = Integer.parseInt(req.getParameter("page"));
		String mem_Id = info.getMem_Id();
		
		int result = dao.updateLike(bbs_num, mem_Id);
		if(result > 0){
			resp.sendRedirect(cp + "/it/board_article.do?bbs_num="+bbs_num+"&page=" + page);
		}
	}
}
