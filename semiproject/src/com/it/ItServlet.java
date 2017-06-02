package com.it;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.MemberDAO;
import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.MyServlet;
import com.util.MyUtil;

import net.sf.json.JSONObject;

@WebServlet("/it/*")
public class ItServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	private SessionInfo info;
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		info = (SessionInfo) session.getAttribute("member");
		if (info == null) { // �α��ε��� ���� ���
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// ������ ������ ���(pathname)
		String root=session.getServletContext().getRealPath("/");
		pathname=root+File.separator+"uploads"+File.separator+"semi";
		File f=new File(pathname);
		if(! f.exists()) {
			f.mkdirs();
		}
		
		// uri�� ���� �۾�
		if (uri.indexOf("news.do") != -1) {
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
			news_delete(req, resp);
		} else if (uri.indexOf("news_like.do") != -1) {
			news_like(req, resp);
		} else if (uri.indexOf("news_likecnt.do") != -1) {
			news_likecnt(req, resp);
		} else if(uri.indexOf("news_insertReply.do")!=-1) {
			news_insertReply(req, resp);
		} else if(uri.indexOf("news_listReply.do")!=-1) {
			news_listReply(req, resp);
		} else if(uri.indexOf("news_deleteReply.do")!=-1) {
			news_deleteReply(req, resp);
		} else	if (uri.indexOf("board.do") != -1) {
			board_list(req, resp);
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
		} else if (uri.indexOf("board_likecnt.do") != -1) {
			board_likecnt(req, resp);
		} else if(uri.indexOf("board_insertReply.do")!=-1) {
			board_insertReply(req, resp);
		} else if(uri.indexOf("board_listReply.do")!=-1) {
			board_listReply(req, resp);
		} else if(uri.indexOf("board_deleteReply.do")!=-1) {
			board_deleteReply(req, resp);
		} 
	}

	
	
	private void news_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����Ʈ
		String cp = req.getContextPath();
		NewsDAO dao = new NewsDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		// �˻�
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if (searchKey == null) {
			searchKey = "subject";
			searchValue = "";
		}
		// GET ����� ��� ���ڵ�
		if (req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
		}

		// ��ü ������ ����
		int dataCount;
		if (searchValue.length() == 0)
			dataCount = dao.dataCount();
		else
			dataCount = dao.dataCount(searchKey, searchValue);

		// ��ü ������ ��
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);

		if (current_page > total_page)
			current_page = total_page;

		// �Խù� ������ ���۰� ��
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;

		// �Խù� ��������
		List<NewsDTO> list = null;
		if (searchValue.length() == 0)
			list = dao.listNews(start, end);
		else
			list = dao.listNews(start, end, searchKey, searchValue);
		// ������ ��������
		List<NewsDTO> listNotice=null;
		//if(current_page==1){ -->�������� 1�������� ������ ����
		listNotice = dao.listNotice();
		Iterator<NewsDTO> itNotice=listNotice.iterator();
		while (itNotice.hasNext()) {
			NewsDTO dto=itNotice.next();
			dto.setCreated(dto.getCreated().substring(0, 10));
		}
		// new,����Ʈ��ȣ
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long gap;
		Date date = new Date();
		int listNum, n=0;
		Iterator<NewsDTO> it = list.iterator();
		while(it.hasNext()){
			NewsDTO dto = it.next();
			listNum = dataCount- (start+n-1);
			dto.setListNum(listNum);
			try{
				Date sDate = format.parse(dto.getCreated());
				gap=(date.getTime()-sDate.getTime())/(60*60*1000); //60*60*1000 -> 1�ð�    .getTime -> �и�������
				dto.setCreated(dto.getCreated().substring(0,10));
				dto.setGap(gap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			n++;
		}
		
		String query = "";
		if (searchValue.length() != 0) {
			// �˻��� ��� �˻��� ���ڵ�
			searchValue = URLEncoder.encode(searchValue, "utf-8");
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
		}

		// ����¡ ó��
		String listUrl = cp + "/it/news.do";
		String articleUrl = cp + "/it/news_article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);

		// �������� JSP�� �ѱ� �Ӽ�
		req.setAttribute("list", list);
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);

		// JSP�� ������
		String path = "/WEB-INF/views/it/news/list.jsp";
		forward(req, resp, path);
	}

	private void news_createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �۾��� ��
		req.setAttribute("mode", "created");
		String path = "/WEB-INF/views/it/news/created.jsp";
		forward(req, resp, path);
	}

	private void news_createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����
		String cp = req.getContextPath();

		NewsDAO dao = new NewsDAO();
		NewsDTO dto = new NewsDTO();
		
		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
		MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
		// mem_Id�� ���ǿ� ����� ����
		dto.setMem_Id(info.getMem_Id());
		
		// ����Ʈ �߰�(�Խ����� 10 / ����� 5)
		MemberDAO mdao = new MemberDAO();
		mdao.updatePoint(info.getMem_Id(), 10);
		
		// �Ķ����
		if(info.getMem_Id().equals("admin") && mreq.getParameter("notice")!=null){
				dto.setCategory("����");
				dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		}else{
			dto.setCategory(mreq.getParameter("category"));
		}
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		dao.insertNews(dto, "created");

		resp.sendRedirect(cp + "/it/news.do");
	}

	private void news_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����
		String cp = req.getContextPath();
		NewsDAO dao = new NewsDAO();

		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if (searchKey == null) {
			searchKey = "subject";
			searchValue = "";
		}

		searchValue = URLDecoder.decode(searchValue, "utf-8");

		// ��ȸ�� ����
		dao.updateHitCount(bbs_num);

		// �Խù� ��������
		NewsDTO dto = dao.readNews(bbs_num);
		
		if (dto == null) { // �Խù��� ������ �ٽ� ����Ʈ��
			resp.sendRedirect(cp + "/it/news.do?page=" + page);
			return;
		}

		int countLike = dao.countLike(bbs_num);
		//dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		// ������ ������
		NewsDTO preReadDto = dao.preReadNews(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		NewsDTO nextReadDto = dao.nextReadNews(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		// ����Ʈ�� ������/�����ۿ��� ����� �Ķ����
		String query = "page=" + page;
		if (searchValue.length() != 0) {
			query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "utf-8");
		}

		// JSP�� ������ �Ӽ�
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);
		req.setAttribute("countLike", countLike);

		// ������
		String path = "/WEB-INF/views/it/news/article.jsp";
		forward(req, resp, path);
	}

	private void news_updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ��
		String cp = req.getContextPath();
		NewsDAO dao = new NewsDAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		NewsDTO dto = dao.readNews(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/it/news.do?page=" + page);
			return;
		}

		// �Խù��� �ø� ����ڰ� �ƴϸ�
		if (!dto.getMem_Id().equals(info.getMem_Id())) {
			resp.sendRedirect(cp + "/it/news.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");

		String path = "/WEB-INF/views/it/news/created.jsp";
		forward(req, resp, path);
	}

	private void news_updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� �Ϸ�
		String cp = req.getContextPath();
		NewsDAO dao = new NewsDAO();


		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
	    MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
	    String page = mreq.getParameter("page");
		/*if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/it/news.do?page=" + page);
			return;
		}*/

		NewsDTO dto = new NewsDTO();
		dto.setBbs_num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		dao.updateNews(dto, info.getMem_Id());

		resp.sendRedirect(cp + "/it/news.do?page=" + page);
	}

	private void news_replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �亯��
		String cp = req.getContextPath();
		NewsDAO dao = new NewsDAO();

		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		NewsDTO dto = dao.readNews(bbs_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/it/news.do?page=" + page);
			return;
		}
		dto.setContent("[" + dto.getSubject() + "] �� ���� �亯�Դϴ�.\n");

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "reply");

		String path = "/WEB-INF/views/it/news/created.jsp";
		forward(req, resp, path);
	}

	private void news_replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �亯�Ϸ�
		String cp = req.getContextPath();
		NewsDAO dao = new NewsDAO();

		// �Ķ����:subject,content,page,
		// groupNum(���� groupNum),
		// depth(���� depth),orderNo(���� orderNo),
		// parent(���� bbs_num)
		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
		MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		String page = mreq.getParameter("page");

		NewsDTO dto = new NewsDTO();
		
		dto.setMem_Id(info.getMem_Id());

		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		dto.setGroupNum(Integer.parseInt(mreq.getParameter("groupNum")));
		dto.setDepth(Integer.parseInt(mreq.getParameter("depth")));
		dto.setOrderNo(Integer.parseInt(mreq.getParameter("orderNo")));
		dto.setParent(Integer.parseInt(mreq.getParameter("parent")));

		dao.insertNews(dto, "reply");
		
		// ����Ʈ �߰�(�Խ����� 10 / ����� 5 / ����� 2)
		MemberDAO mdao = new MemberDAO();
		mdao.updatePoint(info.getMem_Id(), 5);
		
		resp.sendRedirect(cp + "/it/news.do?page=" + page);
	}

	private void news_delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ����
		String cp = req.getContextPath();
		NewsDAO dao = new NewsDAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		NewsDTO dto = dao.readNews(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/it/news.do?page=" + page);
			return;
		}

		// �Խù��� �ø� ����ڳ� admin�� �ƴϸ�
		if (!dto.getMem_Id().equals(info.getMem_Id()) && !info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/it/news.do?page=" + page);
			return;
		}

		dao.deleteNews(bbs_num);
		resp.sendRedirect(cp + "/it/news.do?page=" + page);
	}
	
	private void news_likecnt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NewsDAO dao = new NewsDAO();
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		
		int countLike=dao.countLike(bbs_num);
		JSONObject job=new JSONObject();
		job.put("countLike", countLike);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	private void news_like(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NewsDAO dao = new NewsDAO();
		
		String state="false";
		if(info==null) {
			state="loginFail";
		} else {
			int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));

			int result=dao.updateLike(bbs_num, info.getMem_Id());
			if(result==1)
				state="true";
		}
		
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	
	private void news_listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ����Ʈ(AJAX:TEXT)
		NewsDAO dao = new NewsDAO();
		MyUtil util = new MyUtil();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String pageNo = req.getParameter("pageNo");
		int current_page = 1;
		if (pageNo != null)
			current_page = Integer.parseInt(pageNo);

		int numPerPage = 5;
		int total_page = 0;
		int replyCount = 0;

		replyCount = dao.dataCountReply(bbs_num);
		total_page = util.pageCount(numPerPage, replyCount);
		if (current_page > total_page)
			current_page = total_page;

		int start = (current_page - 1) * numPerPage + 1;
		int end = current_page * numPerPage;

		// ����Ʈ�� ����� ������
		List<NewsReplyDTO> listReply = dao.listReply(bbs_num, start, end);

		// ���͸� <br>
		Iterator<NewsReplyDTO> it = listReply.iterator();
		while (it.hasNext()) {
			NewsReplyDTO dto = it.next();
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}

		// ����¡ó��(�μ�2�� ¥�� js�� ó��)
		String paging = util.paging(current_page, total_page);

		req.setAttribute("listReply", listReply);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		

		// ������
		String path = "/WEB-INF/views/it/news/listReply.jsp";
		forward(req, resp, path);
	}

	private void news_insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ����(JSON)
		NewsDAO dao = new NewsDAO();
		
		String state="false";
		if(info==null) {
			state="loginFail";
		} else {
			int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
			NewsReplyDTO rdto = new NewsReplyDTO();
			rdto.setBbs_num(bbs_num);
			rdto.setMem_Id(info.getMem_Id());
			rdto.setContent(req.getParameter("content"));

			int result=dao.insertReply(rdto);
			if(result==1){
				state="true";
				// ����Ʈ �߰�(�Խ����� 10 / ����� 5 / ����� 2)
				MemberDAO mdao = new MemberDAO();
				mdao.updatePoint(info.getMem_Id(), 2);
			}
		}
		
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	
	protected void news_deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	      NewsDAO dao = new NewsDAO();      
	      
	      int reply_num = Integer.parseInt(req.getParameter("reply_num"));
	      
	      String state="false";
	      if(info==null) {
	         state="loginFail";
	      } else {
	         int result=dao.deleteReply(reply_num, info.getMem_Id());
	         if(result==1) {
	            state="true";
	         }
	      }      
	      
	      JSONObject job=new JSONObject();
	      job.put("state", state);
	      
	      resp.setContentType("text/html;charset=utf-8");
	      PrintWriter out=resp.getWriter();
	      out.print(job.toString());
	   
	   }
	
	
	//���⼭���� �����Խ���
	
	private void board_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����Ʈ
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		// �˻�
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if (searchKey == null) {
			searchKey = "subject";
			searchValue = "";
		}
		// GET ����� ��� ���ڵ�
		if (req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
		}

		// ��ü ������ ����
		int dataCount;
		if (searchValue.length() == 0)
			dataCount = dao.dataCount();
		else
			dataCount = dao.dataCount(searchKey, searchValue);

		// ��ü ������ ��
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);

		if (current_page > total_page)
			current_page = total_page;

		// �Խù� ������ ���۰� ��
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;

		// �Խù� ��������
		List<BoardDTO> list = null;
		if (searchValue.length() == 0)
			list = dao.listBoard(start, end);
		else
			list = dao.listBoard(start, end, searchKey, searchValue);
		// ������ ��������
		List<BoardDTO> listNotice=null;
		//if(current_page==1){ -->�������� 1�������� ������ ����
		listNotice = dao.listNotice();
		Iterator<BoardDTO> itNotice=listNotice.iterator();
		while (itNotice.hasNext()) {
			BoardDTO dto=itNotice.next();
			dto.setCreated(dto.getCreated().substring(0, 10));
		}
		// new,����Ʈ��ȣ
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long gap;
		Date date = new Date();
		int listNum, n=0;
		Iterator<BoardDTO> it = list.iterator();
		while(it.hasNext()){
			BoardDTO dto = it.next();
			listNum = dataCount- (start+n-1);
			dto.setListNum(listNum);
			try{
				Date sDate = format.parse(dto.getCreated());
				gap=(date.getTime()-sDate.getTime())/(60*60*1000); //60*60*1000 -> 1�ð�    .getTime -> �и�������
				dto.setCreated(dto.getCreated().substring(0,10));
				dto.setGap(gap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			n++;
		}
		
		String query = "";
		if (searchValue.length() != 0) {
			// �˻��� ��� �˻��� ���ڵ�
			searchValue = URLEncoder.encode(searchValue, "utf-8");
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
		}

		// ����¡ ó��
		String listUrl = cp + "/it/board.do";
		String articleUrl = cp + "/it/board_article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);

		// �������� JSP�� �ѱ� �Ӽ�
		req.setAttribute("list", list);
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);

		// JSP�� ������
		String path = "/WEB-INF/views/it/board/list.jsp";
		forward(req, resp, path);
	}

	private void board_createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �۾��� ��
		req.setAttribute("mode", "created");
		String path = "/WEB-INF/views/it/board/created.jsp";
		forward(req, resp, path);
	}

	private void board_createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����
		String cp = req.getContextPath();

		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();

		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
		MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
		// mem_Id�� ���ǿ� ����� ����
		dto.setMem_Id(info.getMem_Id());

		// �Ķ����
		if(info.getMem_Id().equals("admin") && mreq.getParameter("notice")!=null){
				dto.setCategory("����");
				dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		}else{
			dto.setCategory(mreq.getParameter("category"));
		}
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		dao.insertBoard(dto, "created");
		// ����Ʈ �߰�(�Խ����� 10 / ����� 5 / ����� 2)
		MemberDAO mdao = new MemberDAO();
		mdao.updatePoint(info.getMem_Id(), 10);

		resp.sendRedirect(cp + "/it/board.do");
	}

	private void board_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����
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

		// ��ȸ�� ����
		dao.updateHitCount(bbs_num);

		// �Խù� ��������
		BoardDTO dto = dao.readBoard(bbs_num);
		
		if (dto == null) { // �Խù��� ������ �ٽ� ����Ʈ��
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		int countLike = dao.countLike(bbs_num);
		//dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		// ������ ������
		BoardDTO preReadDto = dao.preReadBoard(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		BoardDTO nextReadDto = dao.nextReadBoard(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		// ����Ʈ�� ������/�����ۿ��� ����� �Ķ����
		String query = "page=" + page;
		if (searchValue.length() != 0) {
			query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "utf-8");
		}

		// JSP�� ������ �Ӽ�
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);
		req.setAttribute("countLike", countLike);

		// ������
		String path = "/WEB-INF/views/it/board/article.jsp";
		forward(req, resp, path);
	}

	private void board_updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ��
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		BoardDTO dto = dao.readBoard(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		// �Խù��� �ø� ����ڰ� �ƴϸ�
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
		// ���� �Ϸ�
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();


		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
	    MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
	    String page = mreq.getParameter("page");
		/*if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}*/

		BoardDTO dto = new BoardDTO();
		dto.setBbs_num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		dao.updateBoard(dto, info.getMem_Id());

		resp.sendRedirect(cp + "/it/board.do?page=" + page);
	}

	private void board_replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �亯��
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		BoardDTO dto = dao.readBoard(bbs_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}
		dto.setContent("[" + dto.getSubject() + "] �� ���� �亯�Դϴ�.\n");

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "reply");

		String path = "/WEB-INF/views/it/board/created.jsp";
		forward(req, resp, path);
	}

	private void board_replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �亯�Ϸ�
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		// �Ķ����:subject,content,page,
		// groupNum(���� groupNum),
		// depth(���� depth),orderNo(���� orderNo),
		// parent(���� bbs_num)
		
		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
		MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		String page = mreq.getParameter("page");

		BoardDTO dto = new BoardDTO();
		dto.setMem_Id(info.getMem_Id());

		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		dto.setGroupNum(Integer.parseInt(mreq.getParameter("groupNum")));
		dto.setDepth(Integer.parseInt(mreq.getParameter("depth")));
		dto.setOrderNo(Integer.parseInt(mreq.getParameter("orderNo")));
		dto.setParent(Integer.parseInt(mreq.getParameter("parent")));

		dao.insertBoard(dto, "reply");
		// ����Ʈ �߰�(�Խ����� 10 / ����� 5 / ����� 2)
		MemberDAO mdao = new MemberDAO();
		mdao.updatePoint(info.getMem_Id(), 5);

		resp.sendRedirect(cp + "/it/board.do?page=" + page);
	}

	private void board_delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ����
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		BoardDTO dto = dao.readBoard(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		// �Խù��� �ø� ����ڳ� admin�� �ƴϸ�
		if (!dto.getMem_Id().equals(info.getMem_Id()) && !info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/it/board.do?page=" + page);
			return;
		}

		dao.deleteBoard(bbs_num);
		resp.sendRedirect(cp + "/it/board.do?page=" + page);
	}
	
	private void board_likecnt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		
		int countLike=dao.countLike(bbs_num);
		JSONObject job=new JSONObject();
		job.put("countLike", countLike);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	private void board_like(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		
		String state="false";
		if(info==null) {
			state="loginFail";
		} else {
			int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));

			int result=dao.updateLike(bbs_num, info.getMem_Id());
			if(result==1)
				state="true";
		}
		
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	
	private void board_listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ����Ʈ(AJAX:TEXT)
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String pageNo = req.getParameter("pageNo");
		int current_page = 1;
		if (pageNo != null)
			current_page = Integer.parseInt(pageNo);

		int numPerPage = 5;
		int total_page = 0;
		int replyCount = 0;

		replyCount = dao.dataCountReply(bbs_num);
		total_page = util.pageCount(numPerPage, replyCount);
		if (current_page > total_page)
			current_page = total_page;

		int start = (current_page - 1) * numPerPage + 1;
		int end = current_page * numPerPage;

		// ����Ʈ�� ����� ������
		List<BoardReplyDTO> listReply = dao.listReply(bbs_num, start, end);

		// ���͸� <br>
		Iterator<BoardReplyDTO> it = listReply.iterator();
		while (it.hasNext()) {
			BoardReplyDTO dto = it.next();
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}

		// ����¡ó��(�μ�2�� ¥�� js�� ó��)
		String paging = util.paging(current_page, total_page);

		req.setAttribute("listReply", listReply);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		

		// ������
		String path = "/WEB-INF/views/it/board/listReply.jsp";
		forward(req, resp, path);
	}

	private void board_insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ����(JSON)
		BoardDAO dao = new BoardDAO();
		
		String state="false";
		if(info==null) {
			state="loginFail";
		} else {
			int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
			BoardReplyDTO rdto = new BoardReplyDTO();
			rdto.setBbs_num(bbs_num);
			rdto.setMem_Id(info.getMem_Id());
			rdto.setContent(req.getParameter("content"));

			int result=dao.insertReply(rdto);
			if(result==1){
				state="true";
				// ����Ʈ �߰�(�Խ����� 10 / ����� 5 / ����� 2)
				MemberDAO mdao = new MemberDAO();
				mdao.updatePoint(info.getMem_Id(), 2);
			}
		}
		
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	
	protected void board_deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	      BoardDAO dao = new BoardDAO();      
	      
	      int reply_num = Integer.parseInt(req.getParameter("reply_num"));
	      
	      String state="false";
	      if(info==null) {
	         state="loginFail";
	      } else {
	         int result=dao.deleteReply(reply_num, info.getMem_Id());
	         if(result==1) {
	            state="true";
	         }
	      }      
	      
	      JSONObject job=new JSONObject();
	      job.put("state", state);
	      
	      resp.setContentType("text/html;charset=utf-8");
	      PrintWriter out=resp.getWriter();
	      out.print(job.toString());
	}
}
