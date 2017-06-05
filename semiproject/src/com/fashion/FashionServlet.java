	package com.fashion;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.it.BoardDTO;
import com.it.BoardReplyDTO;
import com.member.MemberDAO;
import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import com.util.MyServlet;
import com.util.MyUtil;

import net.sf.json.JSONObject;

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
	pathname=root+File.separator+"uploads"+File.separator+"semi";
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
	} else if(uri.indexOf("insertReply.do")!=-1) {
		// 댓글 추가
		insertReply(req, resp);
	} else if(uri.indexOf("listReply.do")!=-1) {
		// 댓글 리스트
		listReply(req, resp);
	} else	if (uri.indexOf("Fashion2.do") != -1) {
		Fashion2_list(req, resp);
	} else if (uri.indexOf("Fashion2_created.do") != -1) {
		Fashion2_createdForm(req, resp);
	} else if (uri.indexOf("Fashion2_created_ok.do") != -1) {
		Fashion2_createdSubmit(req, resp);
	} else if (uri.indexOf("Fashion2_article.do") != -1) {
		Fashion2_article(req, resp);
	} else if (uri.indexOf("Fashion2_update.do") != -1) {
		Fashion2_updateForm(req, resp);
	} else if (uri.indexOf("Fashion2_update_ok.do") != -1) {
		Fashion2_updateSubmit(req, resp);
	} else if (uri.indexOf("Fashion2_reply.do") != -1) {
		Fashion2_replyForm(req, resp);
	} else if (uri.indexOf("Fashion2_reply_ok.do") != -1) {
		Fashion2_replySubmit(req, resp);
	} else if (uri.indexOf("Fashion2_delete.do") != -1) {
		Fashion2_delete(req, resp);
	} else if (uri.indexOf("Fashion2_like.do") != -1) {
		Fashion2_like(req, resp);
	} else if (uri.indexOf("Fashion2_likecnt.do") != -1) {
		Fashion2_likecnt(req, resp);
	} else if(uri.indexOf("Fashion2_insertReply.do")!=-1) {
		Fashion2_insertReply(req, resp);
	} else if(uri.indexOf("Fashion2_listReply.do")!=-1) {
		Fashion2_listReply(req, resp);
	} else if(uri.indexOf("Fashion2_deleteReply.do")!=-1) {
		Fashion2_deleteReply(req, resp);
	} 
	}
	
	protected void list1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String cp =req.getContextPath();
		
		Fashion1DAO dao1 = new Fashion1DAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		int current_page =1;
		
		if(page != null)
			current_page = Integer.parseInt(page);
		
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		String category = req.getParameter("category");
		
		//내가쓴글보기 - Id 받아오기
		String searchMyList="";
		if(req.getParameter("mylist")!=null){
			searchMyList = info.getMem_Id();
			
		}
		
		if(searchValue ==null){
			searchKey="subject";
			searchValue ="";
			
		}
		
		if(category ==null)
			category="";
		
		
		if(searchMyList!=null && searchMyList.length()!=0){
			searchKey="mem_Id";
			searchValue  = searchMyList;
		}
		
		if(req.getMethod().equalsIgnoreCase("get"))
			searchValue=URLDecoder.decode(searchValue,"UTF-8");
		
		int dataCount ;
		
		if(searchValue.length()!=0 )
			dataCount = dao1.dataCount(searchKey, searchValue, category);
			
		else
			dataCount = dao1.dataCount();
		
		int rows=10;
		
		int total_page = util.pageCount(rows, dataCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		
		int start = (current_page - 1) * rows + 1;
		int end = current_page * rows;
		
		
		//게시물리스트
		List<Fashion1DTO> list;
		
		if(searchValue.length() == 0 )
			list = dao1.listBoard(start, end);
		
		else
			list = dao1.listBoard(start, end, searchKey, searchValue, category);
		
		  
		// 게시물 번호 생성
		int listNum, n = 0;
		
		long gap;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	   
		
		boolean checkImg = false;
		
		
				// 모든 컬렉션(Collection)으로 부터 정보를 얻을 수 있는 Interface
		Iterator<Fashion1DTO> it = list.iterator();
		
		while(it.hasNext()){
			Fashion1DTO dto1 = it.next();
			listNum = dataCount - (start + n - 1);
			dto1.setListNum(listNum);
			
			//new 띄우기
			try
			{
				Date sDate = format.parse(dto1.getCreated());
				gap = (date.getTime() - sDate.getTime()) / (60 * 60* 1000); //여기고치기 
				
				dto1.setCreated(dto1.getCreated().substring(0, 10));
				dto1.setGap(gap);
				
			} catch(ParseException e) {

			}
			
		//컨텐츠 에서 img태그 있으면 
			if(dto1.getContent().contains("<img"))
				checkImg=true;
			
				dto1.setCheckImg(checkImg);
			
			
			//System.out.println(dto1.getGap());
			//System.out.println(dto1.getContent());
			n++;
		}
		String query = "";
		
		// 검색하는 경우 페이징 및 글보기에 사용할 Parameter
		if(searchValue.length() != 0){
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
			
			if(category.length() !=0 )
				query +="&category="+category;
			
		}
		
	/*	//내가쓴글보기 검색한경우 
		if(searchMyList.length() !=0 ){
			query ="mem_Id="+searchMyList;
		}*/
		
		// 글 리스트 주소
		String listUrl = cp + "/fashion/list1.do";
		// 글보기 주소
		String articleUrl = cp + "/fashion/article1.do?page=" + current_page;
		
		if(query.length() != 0)
		{
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		// 페이징 처리
		String paging = util.paging(current_page, total_page, listUrl);
		
		// Forwarding 페이지에 넘길 데이터
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		
		
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
	
		/*
		if(mreq.getParameter("notice")!=null)//공지인경우
		    	dto1.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		  */
		dao1.insertBoard(dto1);
		// 포인트 추가(게시판은 10 / 답글은 5 / 댓글은 2)
				MemberDAO mdao = new MemberDAO();
				mdao.updatePoint(info.getMem_Id(), 10);
		
		resp.sendRedirect(cp+"/fashion/list1.do");
	}
	protected void article1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		Fashion1DAO dao1 = new Fashion1DAO();
		
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		String category = req.getParameter("category");
		
		if(searchKey ==null){
			searchKey="subject";
			searchValue ="";
			
		}
		
		if(category ==null)
			category="";
		
		if(req.getMethod().equalsIgnoreCase("get"))
			searchValue=URLDecoder.decode(searchValue,"UTF-8");
		
		
		
		Fashion1DTO dto1 = dao1.readBoard(bbs_num);
		
		if(dto1 == null)
		{
			resp.sendRedirect(cp + "/fashion/list1.do?page=" + page);
			return;
		}
		
		dao1.updateHitCount(bbs_num);

		Fashion1DTO predto1 = dao1.preReadFashion1(dto1.getBbs_num(), searchKey, searchValue ,dto1.getCategory());
		Fashion1DTO nextdto1 = dao1.nextReadFashion1(dto1.getBbs_num(), searchKey, searchValue ,dto1.getCategory());
		
		
		
		String query ="page="+page;
		
		if(searchValue.length() != 0){
			query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue ,"UTF-8");
			
			if(category.length()!=0)
				query+="&categoty="+category;
			
		}
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		
		req.setAttribute("dto1", dto1);
		req.setAttribute("predto1", predto1);
		req.setAttribute("nextdto1", nextdto1);
		
		forward(req, resp, "/WEB-INF/views/fashion/article1.jsp");
	}
	protected void updateForm1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		String category = req.getParameter("category");
		
		// 검색 안할 경우 기본값 설정
		if(searchKey == null)
		{
			searchKey = "subject";
			searchValue = "";
		}

		if(category ==null)
			category="";
		
		searchValue = URLDecoder.decode(searchValue, "UTF-8");
		
		Fashion1DAO dao1 = new Fashion1DAO();
		Fashion1DTO dto1 = dao1.readBoard(bbs_num);
		
		if(dto1 == null || ! dto1.getMem_Id().equals(info.getMem_Id()))
		{
			String query = "page=" + page;
			
			if(searchValue.length() != 0){
				query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");

				if(category.length() !=0 )
					query +="&category="+category;
			}
			
			resp.sendRedirect(cp + "/fashion/list1.do?" + query);
			return;
		}
		
		// 게시물 수정 페이지로 Forwarding
		req.setAttribute("dto1", dto1);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");
		req.setAttribute("searchKey", searchKey);
		req.setAttribute("searchValue", searchValue);
		
		forward(req, resp, "/WEB-INF/views/fashion/created1.jsp");
		
		
	}
	protected void updateSubmit1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Fashion1DAO dao1 = new Fashion1DAO();
		Fashion1DTO dto1 = new Fashion1DTO();
		String cp = req.getContextPath();
		

		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
		MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
		String page= mreq.getParameter("page");
		dto1.setBbs_num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dto1.setCategory(mreq.getParameter("catagory"));
		dto1.setMem_Id(info.getMem_Id());
		dto1.setSubject(mreq.getParameter("subject"));
		dto1.setContent(mreq.getParameter("content"));
		

		
		dao1.updateBoard(dto1, info.getMem_Id());
		
		
		resp.sendRedirect(cp + "/fashion/list1.do?page="+page);
		
		
	}
	protected void delete1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
				Fashion1DAO dao1 = new Fashion1DAO();
				int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
				String cp = req.getContextPath();
				String page = req.getParameter("page");
				
				// 검색 컬럼과 검색 값 받기
				String searchKey = req.getParameter("searchKey");
				String searchValue = req.getParameter("searchValue");
				
				String category = req.getParameter("category");
				
				// 검색 안할 경우 기본값 설정
				if(searchKey == null)
				{
					searchKey = "subject";
					searchValue = "";
				}
				
				if(category ==null)
					category="";
				
				
				searchValue = URLDecoder.decode(searchValue, "UTF-8");

				dao1.deleteBoard(bbs_num, info.getMem_Id());
				
				// 리스트나 이전글/다음글에서 사용할 Parameter
				String query = cp + "/fashion/list1.do?page=" + page;
				
				// 검색하는 경우 페이징 및 글보기에 사용할 Parameter
				if(searchValue.length() != 0){
					query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
				
					if(category.length() !=0 )
						query +="&category="+category;	
				}
				// Server에서 Server로(Servlet => jsp 등으로 넘겨줌)
				// Client의 주소가 변경됨		request, response 객체 초기화
				resp.sendRedirect(query);
				
				
				// Server에서 Client에게 다른 주소로 재전송하도록 요구
				// Client의 주소가 변경되지 않음		request, response 객체도 넘겨줌
				// forward(req, resp, "/WEB-INF/views/bbs/list.jsp");
	}
	
	private void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리플 리스트(AJAX:TEXT)
		F1replyDAO dao = new F1replyDAO();
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

		// 리스트에 출력할 데이터
		List<F1replyDTO> listReply = dao.listReply(bbs_num, start, end);

		// 엔터를 <br>
		Iterator<F1replyDTO> it = listReply.iterator();
		while (it.hasNext()) {
			F1replyDTO dto = it.next();
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			System.out.println(dto.getContent());
		}

		// 페이징처리(인수2개 짜리 js로 처리)
		String paging = util.paging(current_page, total_page);

		req.setAttribute("listReply", listReply);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		

		// 포워딩
		String path = "/WEB-INF/views/fashion/listReply.jsp";
		forward(req, resp, path);
	}

	private void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리플 저장(JSON)
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		F1replyDAO dao = new F1replyDAO();
		
		String state="false";
		if(info==null) {
			state="loginFail";
		} else {
			int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
			F1replyDTO rdto = new F1replyDTO();
			rdto.setBbs_num(bbs_num);
			rdto.setMem_Id(info.getMem_Id());
			rdto.setContent(req.getParameter("content"));

			int result=dao.insertReply(rdto);
			if(result==1)
				state="true";
			MemberDAO mdao = new MemberDAO();
			mdao.updatePoint(info.getMem_Id(), 2);
		}
		
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	
	private void Fashion2_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		String cp = req.getContextPath();
		Fashion2DAO dao = new Fashion2DAO();
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
		List<Fashion2DTO> list = null;
		if (searchValue.length() == 0)
			list = dao.listFashion2(start, end);
		else
			list = dao.listFashion2(start, end, searchKey, searchValue);
		// 공지글 가져오기
		List<Fashion2DTO> listNotice=null;
		//if(current_page==1){ -->공지글이 1페이지만 나오게 해줌
		listNotice = dao.listNotice();
		Iterator<Fashion2DTO> itNotice=listNotice.iterator();
		while (itNotice.hasNext()) {
			Fashion2DTO dto=itNotice.next();
			dto.setCreated(dto.getCreated().substring(0, 10));
		}
		// new,리스트번호
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long gap;
		Date date = new Date();
		int listNum, n=0;
		Iterator<Fashion2DTO> it = list.iterator();
		while(it.hasNext()){
			Fashion2DTO dto = it.next();
			listNum = dataCount- (start+n-1);
			dto.setListNum(listNum);
			try{
				Date sDate = format.parse(dto.getCreated());
				gap=(date.getTime()-sDate.getTime())/(60*60*1000); //60*60*1000 -> 1시간    .getTime -> 밀리세컨드
				dto.setCreated(dto.getCreated().substring(0,10));
				dto.setGap(gap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			n++;
		}
		
		String query = "";
		if (searchValue.length() != 0) {
			// 검색인 경우 검색값 인코딩
			searchValue = URLEncoder.encode(searchValue, "utf-8");
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
		}

		// 페이징 처리
		String listUrl = cp + "/fashion/Fashion2.do";
		String articleUrl = cp + "/fashion/Fashion2_article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);

		// 포워딩할 JSP로 넘길 속성
		req.setAttribute("list", list);
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);

		// JSP로 포워딩
		String path = "/WEB-INF/views/fashion/list2.jsp";
		forward(req, resp, path);
	}

	private void Fashion2_createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		req.setAttribute("mode", "created");
		String path = "/WEB-INF/views/fashion/created2.jsp";
		forward(req, resp, path);
	}

	private void Fashion2_createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 저장
		String cp = req.getContextPath();

		Fashion2DAO dao = new Fashion2DAO();
		Fashion2DTO dto = new Fashion2DTO();

		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
		MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
		// mem_Id는 세션에 저장된 정보
		dto.setMem_Id(info.getMem_Id());

		// 파라미터
		if(info.getMem_Id().equals("admin") && mreq.getParameter("notice")!=null){
				dto.setCategory("공지");
				dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		}else{
			dto.setCategory(mreq.getParameter("category"));
		}
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		dao.insertFashion2(dto, "created");
		// 포인트 추가(게시판은 10 / 답글은 5 / 댓글은 2)
		MemberDAO mdao = new MemberDAO();
		mdao.updatePoint(info.getMem_Id(), 10);

		resp.sendRedirect(cp + "/fashion/Fashion2.do");
	}

	private void Fashion2_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 보기
		String cp = req.getContextPath();
		Fashion2DAO dao = new Fashion2DAO();

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
		Fashion2DTO dto = dao.readFashion2(bbs_num);
		
		if (dto == null) { // 게시물이 없으면 다시 리스트로
			resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
			return;
		}

		int countLike = dao.countLike(bbs_num);
		//dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		// 이전글 다음글
		Fashion2DTO preReadDto = dao.preReadFashion2(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		Fashion2DTO nextReadDto = dao.nextReadFashion2(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
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
		req.setAttribute("countLike", countLike);

		// 포워딩
		String path = "/WEB-INF/views/fashion/article2.jsp";
		forward(req, resp, path);
	}

	private void Fashion2_updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		String cp = req.getContextPath();
		Fashion2DAO dao = new Fashion2DAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		Fashion2DTO dto = dao.readFashion2(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
			return;
		}

		// 게시물을 올린 사용자가 아니면
		if (!dto.getMem_Id().equals(info.getMem_Id())) {
			resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");

		String path = "/WEB-INF/views/fashion/created2.jsp";
		forward(req, resp, path);
	}

	private void Fashion2_updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		String cp = req.getContextPath();
		Fashion2DAO dao = new Fashion2DAO();


		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
	    MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
	    String page = mreq.getParameter("page");
		/*if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
			return;
		}*/

		Fashion2DTO dto = new Fashion2DTO();
		dto.setBbs_num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));

		dao.updateFashion2(dto, info.getMem_Id());

		resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
	}

	private void Fashion2_replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변폼
		String cp = req.getContextPath();
		Fashion2DAO dao = new Fashion2DAO();

		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page = req.getParameter("page");
		Fashion2DTO dto = dao.readFashion2(bbs_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
			return;
		}
		dto.setContent("[" + dto.getSubject() + "] 에 대한 답변입니다.\n");

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "reply");

		String path = "/WEB-INF/views/fashion/created2.jsp";
		forward(req, resp, path);
	}

	private void Fashion2_replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변완료
		String cp = req.getContextPath();
		Fashion2DAO dao = new Fashion2DAO();

		// 파라미터:subject,content,page,
		// groupNum(부의 groupNum),
		// depth(부의 depth),orderNo(부의 orderNo),
		// parent(부의 bbs_num)
		
		String encType="utf-8";
		int maxFilesize=10*1024*1024;//10MB
		
		MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		String page = mreq.getParameter("page");

		Fashion2DTO dto = new Fashion2DTO();
		dto.setMem_Id(info.getMem_Id());

		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		dto.setGroupNum(Integer.parseInt(mreq.getParameter("groupNum")));
		dto.setDepth(Integer.parseInt(mreq.getParameter("depth")));
		dto.setOrderNo(Integer.parseInt(mreq.getParameter("orderNo")));
		dto.setParent(Integer.parseInt(mreq.getParameter("parent")));

		dao.insertFashion2(dto, "reply");
		// 포인트 추가(게시판은 10 / 답글은 5 / 댓글은 2)
		MemberDAO mdao = new MemberDAO();
		mdao.updatePoint(info.getMem_Id(), 5);

		resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
	}

	private void Fashion2_delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		String cp = req.getContextPath();
		Fashion2DAO dao = new Fashion2DAO();

		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		Fashion2DTO dto = dao.readFashion2(bbs_num);

		if (dto == null) {
			resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
			return;
		}

		// 게시물을 올린 사용자나 admin이 아니면
		if (!dto.getMem_Id().equals(info.getMem_Id()) && !info.getMem_Id().equals("admin")) {
			resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
			return;
		}

		dao.deleteFashion2(bbs_num);
		resp.sendRedirect(cp + "/fashion/Fashion2.do?page=" + page);
	}
	
	private void Fashion2_likecnt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Fashion2DAO dao = new Fashion2DAO();
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		
		int countLike=dao.countLike(bbs_num);
		JSONObject job=new JSONObject();
		job.put("countLike", countLike);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}

	private void Fashion2_like(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Fashion2DAO dao = new Fashion2DAO();
		
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
	
	private void Fashion2_listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리플 리스트(AJAX:TEXT)
		Fashion2DAO dao = new Fashion2DAO();
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

		// 리스트에 출력할 데이터
		List<Fashion2ReplyDTO> listReply = dao.listReply(bbs_num, start, end);

		// 엔터를 <br>
		Iterator<Fashion2ReplyDTO> it = listReply.iterator();
		while (it.hasNext()) {
			Fashion2ReplyDTO dto = it.next();
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}

		// 페이징처리(인수2개 짜리 js로 처리)
		String paging = util.paging(current_page, total_page);

		req.setAttribute("listReply", listReply);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		

		// 포워딩
		String path = "/WEB-INF/views/fashion/listReply2.jsp";
		forward(req, resp, path);
	}

	private void Fashion2_insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리플 저장(JSON)
		Fashion2DAO dao = new Fashion2DAO();
		
		String state="false";
		if(info==null) {
			state="loginFail";
		} else {
			int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
			Fashion2ReplyDTO rdto = new Fashion2ReplyDTO();
			rdto.setBbs_num(bbs_num);
			rdto.setMem_Id(info.getMem_Id());
			rdto.setContent(req.getParameter("content"));

			int result=dao.insertReply(rdto);
			if(result==1){
				state="true";
				// 포인트 추가(게시판은 10 / 답글은 5 / 댓글은 2)
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
	
	protected void Fashion2_deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	      Fashion2DAO dao = new Fashion2DAO();      
	      
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
