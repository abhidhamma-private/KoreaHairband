package com.soccer;

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
import com.member.MemberDTO;
import com.member.SessionInfo;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import com.util.FileManager;
import com.util.MyServlet;
import com.util.MyUtil;

import net.sf.json.JSONObject;



@WebServlet("/soccer/*")
public class SoccerServlet extends MyServlet
{
	private static final long serialVersionUID = 1L;

	private SessionInfo info;
	private String pathname;
	
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		info = (SessionInfo) session.getAttribute("member");
		
		if(info == null)
		{
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		// Image는 반드시 Web 경로에 저장해야 출력 가능
		// Image 저장 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "semi";
		File f = new File(pathname);
		
		if(! f.exists())
			f.mkdirs();
		
		// uri에 따른 작업
		// 자유 게시판
		if(uri.indexOf("board.do") != -1)
			scBoardList(req, resp);
		
		else if(uri.indexOf("created.do") != -1)
			createdSCBoardForm(req, resp);
		
		else if(uri.indexOf("created_ok.do") != -1)
			createdSCBoardSubmit(req, resp);
		
		else if(uri.indexOf("article.do") != -1)
			readSCBoard(req, resp);
		
		else if(uri.indexOf("update.do") != -1)
			updateSCBoardForm(req, resp);
		
		else if(uri.indexOf("update_ok.do") != -1)
			updateSCBoardSubmit(req, resp);
		
		else if(uri.indexOf("delete.do") != -1)
			deleteSCBoard(req, resp);
		
		else if(uri.indexOf("reply.do") != -1)
			replyList(req, resp);
		
		else if(uri.indexOf("reply_ok.do") != -1)
			replySubmit(req, resp);
		
		else if(uri.indexOf("deleteReply.do") != -1)
			deleteSCBoardReply(req, resp);
		
		else if(uri.indexOf("likeSCBoard.do") != -1)
			likeSCBoard(req, resp);
		
		else if(uri.indexOf("cancelLikeSCBoard.do") != -1)
			cancelLikeSCBoard(req, resp);
		
		
		// Photo Gallery
		if(uri.indexOf("photo.do") != -1)
			list(req, resp);
		
		else if(uri.indexOf("createdPH.do") != -1)
			createdForm(req, resp);
		
		else if(uri.indexOf("createdPH_ok.do") != -1)
			createdSubmit(req, resp);
		
		else if(uri.indexOf("articlePH.do") != -1)
			article(req, resp);
		
		else if(uri.indexOf("updatePH.do") != -1)
			updateForm(req, resp);
		
		else if(uri.indexOf("updatePH_ok.do") != -1)
			updateSubmit(req, resp);
		
		else if(uri.indexOf("deletePH.do") != -1)
			delete(req, resp);
	}
	
	protected void scBoardList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 자유게시판 게시물 리스트
		SCBoardDAO dao = new SCBoardDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		// 페이지번호 Parameter 받기
		String page = req.getParameter("page");
		int current_page = 1;
		
		// 넘어온 Parameter가 있으면
		if(page != null)
			current_page = Integer.parseInt(page);
		
		// 검색 컬럼과 검색 값 받기
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		
		// 검색 안할경우 기본값 설정
		if(searchKey == null)
		{
			searchKey = "subject";
			searchValue = "";
		}
		
		// Get방식일 경우 처리방법
		if(req.getMethod().equalsIgnoreCase("get"))
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		
		int dataCount;
		
		// 검색했을경우 데이터 개수
		if(searchValue.length() != 0)
			dataCount = dao.dataCount(searchKey, searchValue);
		
		else
			dataCount = dao.dataCount();
			
		// 한 줄에 출력할 데이터 개수
		int rows = 10;
		
		// 총 페이지 수 계산
		int total_page = util.pageCount(rows, dataCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		// 게시물을 가져올 시작과 끝 설정
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;
		
		// 게시물 리스트
		List<SCBoardDTO> list;
		
		if(searchValue.length() == 0)
			list = dao.listSCBoard(start, end);
		
		else
			list = dao.listSCBoard(start, end, searchKey, searchValue);
		
		// 공지글
		List<SCBoardDTO> listNotice = dao.listSCBoardNotice();
		Iterator<SCBoardDTO> itNotice = listNotice.iterator();
		
		while(itNotice.hasNext())
		{
			SCBoardDTO dto = itNotice.next();
			dto.setCreated(dto.getCreated().substring(0, 10));
		}
		
		// 게시물 번호 생성
		int listNum, n = 0;
		
		// 모든 컬렉션(Collection)으로 부터 정보를 얻을 수 있는 Interface
		Iterator<SCBoardDTO> it = list.iterator();

		long gap;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		while(it.hasNext())
		{
			SCBoardDTO dto = it.next();
			listNum = dataCount - (start + n - 1);
			dto.setListNum(listNum);
			
			// 날짜 차이(시간)
			try
			{
				Date sDate = format.parse(dto.getCreated());
				gap = (date.getTime() - sDate.getTime()) / (60 * 60 * 1000);
				
				dto.setCreated(dto.getCreated().substring(0, 10));
				dto.setGap(gap);
				
			} catch(Exception e) {
				
				e.printStackTrace();
			}
			
			n++;
		}
		
		String query = "";
		
		// 검색하는 경우 페이징 및 글보기에 사용할 Parameter
		if(searchValue.length() != 0)
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
		
		// 글 리스트 주소
		String listUrl = cp + "/soccer/board.do";
		// 글보기 주소
		String articleUrl = cp + "/soccer/board/article.do?page=" + current_page;
		
		if(query.length() != 0)
		{
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}

		// 페이징 처리
		String paging = util.paging(current_page, total_page, listUrl);
		
		// Forwarding 페이지에 넘길 데이터
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		
		forward(req, resp, "/WEB-INF/views/soccer/freeBoard/list.jsp");
		
		
		
		// JSON으로 보내는 방법
		/*
		List<SCBoardDTO> list = dao.listSCBoard(start, end);
		Iterator<SCBoardDTO> it = list.iterator();
		
		while(it.hasNext())
		{
			SCBoardDTO dto = it.next();
			dto.setContent(util.htmlSymbols(dto.getContent()));
		}
		
		// 페이징 처리
		String paging = util.paging(current_page, total_page);
		
		JSONObject job = new JSONObject();
		
		job.put("list", list);
		job.put("dataCount", dataCount);
		job.put("total_page", total_page);
		job.put("pageNo", current_page);
		job.put("paging", paging);
		
		resp.setContentType("text/html; CHARSET=UTF-8;");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
		*/
		
		/*
			req.setAttribute("data", job.toString());
			forward(req, resp, "/WEB-INF/views/guest/list.jsp");
		*/
	}
	
	protected void createdSCBoardForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 글쓰기 폼
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/soccer/freeBoard/created.jsp");
	}
	
	protected void createdSCBoardSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 게시물 저장
		String cp = req.getContextPath();
		
		SCBoardDAO dao = new SCBoardDAO();
		SCBoardDTO dto = new SCBoardDTO();
		
		String encType = "UTF-8";
		int maxFilesize = 50 * 1024 * 1024;	// 50MB로 제한
		
	    MultipartRequest mreq = new MultipartRequest(req, pathname, maxFilesize, encType, new DefaultFileRenamePolicy());
		
	    dto.setMem_Id(info.getMem_Id());
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		dto.setCategory(mreq.getParameter("category"));
		
		if(info.getMem_Id().equals("admin"))
			dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));

		// 파일을 Upload한 경우
	    if(mreq.getFile("upload") != null)
	    {
	    	dto.setSaveFilename(mreq.getFilesystemName("upload"));
	    	dto.setOriginalFilename(mreq.getOriginalFileName("upload"));
		    dto.setFileSize(mreq.getFile("upload").length());
	    }
		
		dao.insertSCBoard(dto, "created");
		
		MemberDAO mdao = new MemberDAO();
		MemberDTO mdto = mdao.readMember(info.getMem_Id());
		mdto.setPoint(100);
		
		resp.sendRedirect(cp + "/soccer/board.do");
	}
	
	protected void readSCBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 글보기
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		SCBoardDAO dao = new SCBoardDAO();
		String cp = req.getContextPath();
		
		if(info == null)
		{
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		
		if(searchKey == null)
		{
			searchKey = "subject";
			searchValue = "";
		}
		
		searchValue = URLDecoder.decode(searchValue, "utf-8");
		
		// 조회수
		dao.updateHitCount(bbs_num);
		
		// 게시물 가져오기
		SCBoardDTO dto = dao.readSCBoard(bbs_num);
		
		if(dto == null)
		{
			resp.sendRedirect(cp+"/soccer/board.do?page="+page);
			return;
		}
		
		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		
		// 이전글  & 다음글
		SCBoardDTO preReadDto = dao.preReadSCBoard(bbs_num, searchKey, searchValue);
		SCBoardDTO nextReadDto = dao.nextReadSCBoard(bbs_num, searchKey, searchValue);
	
		String query = "page=" + page;
		
		if(searchValue.length()!=0)
		{
			query += "&searchKey=" + searchKey;
			query += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		int checkLike = dao.readLikeSCBoard(bbs_num, info.getMem_Id());
		String likeMem_Id = dao.checkLikeSCBoard(bbs_num, info.getMem_Id());
		int cntLike = dao.dataCountLike(bbs_num);
		
		req.setAttribute("likeMem_Id", likeMem_Id);
		req.setAttribute("checkLike", checkLike);
		req.setAttribute("cntLike", cntLike);
		
		req.setAttribute("dto", dto);
		req.setAttribute("predto", preReadDto);
		req.setAttribute("nextdto", nextReadDto);
		req.setAttribute("query", query);
		req.setAttribute("page", page);
		
		String path = "/WEB-INF/views/soccer/freeBoard/article.jsp";
		forward(req, resp, path);
	}
	
	protected void updateSCBoardForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 수정 폼
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		SCBoardDAO dao = new SCBoardDAO();
		String cp = req.getContextPath();
		
		if(info == null)
		{
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		
		SCBoardDTO dto = dao.readSCBoard(bbs_num);
		
		if(dto == null)
		{
			resp.sendRedirect(cp + "/soccer/board.do?page=" + page);
			return;
		}
		
		// 글을 등록한 사람만 수정 가능
		if(! info.getMem_Id().equals(dto.getMem_Id()))
		{
			resp.sendRedirect(cp + "/soccer/board.do?page=" + page);
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		req.setAttribute("mode", "update");
		String path = "/WEB-INF/views/soccer/freeBoard/created.jsp";
		forward(req, resp, path);
	}
	
	protected void updateSCBoardSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 수정 완료
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		SCBoardDAO dao = new SCBoardDAO();
		String cp = req.getContextPath();
		
		if(info == null)
		{
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		String encType = "UTF-8";
		int maxFilesize = 50 * 1024 * 1024;	// 50MB로 제한
		 
	    MultipartRequest mreq = new MultipartRequest(req, pathname, maxFilesize, encType, new DefaultFileRenamePolicy());
		
		String page = mreq.getParameter("page");
		int bbs_num = Integer.parseInt(mreq.getParameter("bbs_num"));
	
		SCBoardDTO dto = dao.readSCBoard(bbs_num);
		SCBoardDTO fdto = dao.readSCBoardFile(bbs_num);
		
		if(fdto == null && mreq.getFile("upload") != null)
		{
			// new를 꼭 해줘야하는 이유를 모르겠네...
			fdto = new SCBoardDTO();
			
			fdto.setBbs_num(bbs_num);
			fdto.setSaveFilename(mreq.getFilesystemName("upload"));
	    	fdto.setOriginalFilename(mreq.getOriginalFileName("upload"));
		    fdto.setFileSize(mreq.getFile("upload").length());
		    
		    dao.addSCBoardFile(fdto);
		}
		
		if(mreq.getParameter("notice") != null)
			dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		
		// 새로 파일을 Upload한 경우
		if(mreq.getFile("upload") != null)
		{
			FileManager.doFiledelete(pathname, dto.getSaveFilename());
			
	    	dto.setSaveFilename(mreq.getFilesystemName("upload"));
	    	dto.setOriginalFilename(mreq.getOriginalFileName("upload"));
		    dto.setFileSize(mreq.getFile("upload").length());
		}
		
		dao.updateSCBoard(dto);
		
		resp.sendRedirect(cp + "/soccer/board.do?page=" + page);
	}
	
	protected void deleteSCBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 글 삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		SCBoardDAO dao = new SCBoardDAO();
		String cp = req.getContextPath();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		
		SCBoardDTO dto = dao.readSCBoard(bbs_num);
		
		if(dto == null)
		{
			resp.sendRedirect(cp+"/soccer/board.do?page="+page);
			return;
		}
		
		if(info == null || ! info.getMem_Id().equals(dto.getMem_Id())) 
		{
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		// 파일 삭제
		if(dto.getSaveFilename() != null && dto.getSaveFilename().length() != 0)
		{
			FileManager.doFiledelete(pathname, dto.getSaveFilename());
			dao.deleteSCBoardFile(bbs_num);
		}
		
		dao.deleteSCBoard(bbs_num);
		
		req.setAttribute("page", page);

		resp.sendRedirect(cp+"/soccer/board.do?page="+page);
	}
	
	protected void replyList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCBoardDAO dao = new SCBoardDAO();
		MyUtil util = new MyUtil();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String pageNo = req.getParameter("pageNo");
		int current_page = 1;
		
		if(pageNo != null)
			current_page = Integer.parseInt(pageNo);

		int numPerPage = 5;
		int total_page = 0;
		int replyCount = 0;

		replyCount = dao.dataCountReply(bbs_num);
		total_page = util.pageCount(numPerPage, replyCount);
		
		if(current_page > total_page)
			current_page = total_page;

		int start = (current_page - 1) * numPerPage + 1;
		int end = current_page * numPerPage;
		
		List<SCBoardDTO> replyList = dao.replyListSCBoard(bbs_num, start, end);
		Iterator<SCBoardDTO> it = replyList.iterator();
		
		// 엔터 처리 => <br>로 바꿔주는 방법
		while(it.hasNext())
		{
			SCBoardDTO rdto = it.next();
			rdto.setContent(rdto.getContent().replaceAll("\n", "<br>"));
		}
		
		// 페이징 처리(인수 2개 짜리 js로 처리)
		String paging = util.paging(current_page, total_page);

		req.setAttribute("replyList", replyList);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		

		// 포워딩
		String path = "/WEB-INF/views/soccer/freeBoard/reply.jsp";
		forward(req, resp, path);
	}
	
	protected void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 리플 저장(JSON)
		SCBoardDAO dao = new SCBoardDAO();
		
		String state="false";
		
		if(info == null)
			state = "loginFail";
		
		else
		{
			int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
			SCBoardDTO rdto = new SCBoardDTO();
			
			rdto.setBbs_num(bbs_num);
			rdto.setMem_Id(info.getMem_Id());
			rdto.setContent(req.getParameter("content"));

			int result = dao.insertReply(rdto);
			
			if(result == 1)
				state = "true";
		}
		
		/*
		MemberDAO mdao = new MemberDAO();
		MemberDTO mdto = mdao.readMember(info.getMem_Id());
		*/
		
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
			
	protected void deleteSCBoardReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCBoardDAO dao = new SCBoardDAO();
		
		String state = "true";
		
		if(info == null)
			state = "loginFail";
		
		else
		{
			int reply_num = Integer.parseInt(req.getParameter("reply_num"));
			int result = dao.deleteReply(reply_num, info.getMem_Id());
			
			if(result < 1)
				state = "false";
		}
		
		JSONObject job = new JSONObject();
		
		job.put("state", state);
		
		resp.setContentType("text/html; CHARSET=UTF-8;");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
	
	protected void likeSCBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCBoardDAO dao = new SCBoardDAO();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		
		dao.insertLike(bbs_num, info.getMem_Id());
		
		// Forwarding 페이지에 넘길 데이터
		req.setAttribute("page", page);
		req.setAttribute("bbs_num", bbs_num);
		
		forward(req, resp, "/soccer/board/article.do?page=" + page + "&bbs_num=" + bbs_num);
	}
	
	protected void cancelLikeSCBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCBoardDAO dao = new SCBoardDAO();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		
		dao.deleteLike(bbs_num, info.getMem_Id());
		
		// Forwarding 페이지에 넘길 데이터
		req.setAttribute("page", page);
		req.setAttribute("bbs_num", bbs_num);
		
		forward(req, resp, "/soccer/board/article.do?page=" + page + "&bbs_num=" + bbs_num);
	}
	
	
	
	
	
	
	
	
	// Photo Gallery
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCPhotoDAO dao = new SCPhotoDAO();
		String cp = req.getContextPath();
		MyUtil util = new MyUtil();
		
		// 페이지번호 Parameter 받기
		String page = req.getParameter("page");
		int current_page = 1;
		
		// 넘어온 Parameter가 있으면
		if(page != null)
			current_page = Integer.parseInt(page);
		
		int dataCount = dao.dataCount();
		
		// 한 줄에 출력할 데이터 개수
		int rows = 6;
		// 총 페이지 수 계산
		int total_page = util.pageCount(rows, dataCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		// 게시물을 가져올 시작과 끝 설정
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;
		
		// 사진 리스트
		List<SCPhotoDTO> listPhoto = dao.listPhoto(start, end);
		
		// 사진 리스트 주소
		String listUrl = cp + "/soccer/photo.do";
		
		// 글보기 주소
		String articleUrl = cp + "/soccer/photo/articlePH.do?page=" + current_page;
		
		// 페이징 처리
		String paging = util.paging(current_page, total_page, listUrl);
		
		// Forwarding 페이지에 넘길 데이터
		req.setAttribute("listPhoto", listPhoto);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		
		forward(req, resp, "/WEB-INF/views/soccer/photoGallery/list.jsp");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCPhotoDAO dao = new SCPhotoDAO();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		
		// 페이지번호 Parameter 받기
		String page = req.getParameter("page");
		
		SCPhotoDTO dto = dao.readPhoto(bbs_num);
		dto.setMem_Name(info.getMem_Name());
		
		SCPhotoDTO predto = dao.preReadPhoto(bbs_num);
		SCPhotoDTO nextdto = dao.nextReadPhoto(bbs_num);
		
		// JSP에 넘길 데이터
		req.setAttribute("page", page);
		req.setAttribute("dto", dto);
		req.setAttribute("predto", predto);
		req.setAttribute("nextdto", nextdto);
		
		forward(req, resp, "/WEB-INF/views/soccer/photoGallery/article.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/soccer/photoGallery/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String cp = req.getContextPath();
		
		SCPhotoDAO dao = new SCPhotoDAO();
		SCPhotoDTO dto = new SCPhotoDTO();
		
		String encType = "utf-8";
		int maxFilesize = 10*1024*1024;	// 10MB로 제한
		
	    MultipartRequest mreq = new MultipartRequest(req, pathname, maxFilesize, encType, new DefaultFileRenamePolicy());
		
	    // Image 파일을 Upload한 경우
	    if(mreq.getFile("upload") != null)
	    {
	    	dto.setMem_Id(info.getMem_Id());
	    	dto.setSubject(mreq.getParameter("subject"));
		    dto.setContent(mreq.getParameter("content"));
		    
		    // 파일 이름을 가져옴
		    String saveFilename = mreq.getFilesystemName("upload");
		    
		    // 파일이름에 한글이나 공백 등이 있을경우 문제가 생기기때문에 이름을 다시 재정의
		    saveFilename = FileManager.doFilerename(pathname, saveFilename);
		    dto.setImageFilename(saveFilename);
		    
		    // 저장
		    dao.insertPhoto(dto);
	    }
		
		resp.sendRedirect(cp + "/soccer/photo.do");
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCPhotoDAO dao = new SCPhotoDAO();
		String cp = req.getContextPath();
		
		if(info == null)
		{
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		
		SCPhotoDTO dto = dao.readPhoto(bbs_num);
		
		if(dto == null)
		{
			resp.sendRedirect(cp + "/soccer/photo.do?page=" + page);
			return;
		}
		
		// 글을 등록한 사람만 수정 가능
		if(! info.getMem_Id().equals(dto.getMem_Id()))
		{
			resp.sendRedirect(cp+"/soccer/photo.do?page="+page);
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		req.setAttribute("mode", "update");
		String path="/WEB-INF/views/soccer/photoGallery/created.jsp";
		
		forward(req, resp, path);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCPhotoDAO dao = new SCPhotoDAO();
		String cp = req.getContextPath();
		
		if(info == null)
		{
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		String encType = "utf-8";
		int maxFilesize = 10 * 1024 * 1024;
		
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxFilesize, encType, new DefaultFileRenamePolicy());
		
		SCPhotoDTO dto = new SCPhotoDTO();
		
		int bbs_num = Integer.parseInt(mreq.getParameter("bbs_num"));
		String page = mreq.getParameter("page");
		
		dto = dao.readPhoto(bbs_num);
		
		dto.setBbs_num(bbs_num);
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		
		// 기존 파일 삭제(새로 업로드할 이미지를 선택했을 경우)
		if(mreq.getFile("upload")!=null)
		{
			FileManager.doFiledelete(pathname, dto.getImageFilename());
			
			// 파일 이름을 가져옴
		    String saveFilename = mreq.getFilesystemName("upload");
		    // 파일이름에 한글이나 공백 등이 있을경우 문제가 생기기때문에 이름을 다시 재정의
		    saveFilename = FileManager.doFilerename(pathname, saveFilename);
		    dto.setImageFilename(saveFilename);
		}
		
		dao.updatePhoto(dto);
		
		resp.sendRedirect(cp + "/soccer/photo.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		SCPhotoDAO dao = new SCPhotoDAO();
		String cp = req.getContextPath();
	
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		
		SCPhotoDTO dto = dao.readPhoto(bbs_num);
		
		if(dto == null)
		{
			resp.sendRedirect(cp + "/soccer/photo.do?page=" + page);
			return;
		}
		
		if(info == null || ! info.getMem_Id().equals(dto.getMem_Id()))
		{
			resp.sendRedirect(cp + "/soccer/photo.do?page=" + page);
			return;
		}
		
		// 파일 삭제
		FileManager.doFiledelete(pathname, dto.getImageFilename());
		
		dao.deletePhoto(dto.getBbs_num());

		req.setAttribute("page", page);
		req.setAttribute("mode", "update");

		resp.sendRedirect(cp + "/soccer/photo.do?page=" + page);
	}
}
