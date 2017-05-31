package com.petinfo;

import java.io.File;
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
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/pet/petInfo/*")
public class PetInfoServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	private String pathname;
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path)
			throws ServletException, IOException {
		// 포워딩
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		
		String root=session.getServletContext().getRealPath("/");
		pathname=root+File.separator+"uploads"+File.separator+"fashion";
		File f=new File(pathname);
		if(! f.exists()) { // 폴더가 존재하지 않으면
			f.mkdirs();
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
			update(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updated_ok(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String cp = req.getContextPath();
		
		PetInfoDAO dao = new PetInfoDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page!=null)
			current_page=Integer.parseInt(page);
		
		//검색
		String searchKey=req.getParameter("searchKey");
		String searchValue=req.getParameter("searchValue");
		if(searchKey==null) {
			searchKey="subject";
			searchValue="";
		}
		// GET 방식인 경우 디코딩
		if(req.getMethod().equalsIgnoreCase("GET")) {
			searchValue=URLDecoder.decode(searchValue, "utf-8");
		}
		
		int dataCount;
		if(searchValue.length()!=0)
			dataCount = dao.dataCount(searchKey,searchValue);
		else
			dataCount = dao.dataCount();
		
		//전체 페이지수
		int rows = 10; //한 화면에 출력할 페이지 수
		int total_page = util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page=total_page;
		
		//게시그 시작과 끝
		int start = (current_page-1)*rows+1;
		int end= current_page*rows;
		
		List<PetinfoDTO> list = null;
		if(searchValue.length()==0){
			list = dao.listPetInfo(start, end);
		}else{
			list = dao.listPetInfo(start, end, searchKey, searchValue);
		}
		
		List<PetinfoDTO> listNotice=null;
		//if(current_page==1){ -->공지글이 1페이지만 나오게 해줌
		listNotice = dao.listPetInfoNotice();
		Iterator<PetinfoDTO> itNotice=listNotice.iterator();
		while (itNotice.hasNext()) {
			PetinfoDTO dto=itNotice.next();
			dto.setCreated(dto.getCreated().substring(0, 10));
			//}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long gap;
		Date date = new Date();
		int listNum, n=0;
		Iterator<PetinfoDTO> it = list.iterator();
		while(it.hasNext()){
			PetinfoDTO dto = it.next();
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
		
		
		String query="";
		if(searchValue.length()!=0){
			//검색인 경우는 인코딩 필요
			searchValue = URLEncoder.encode(searchValue,"utf-8");
			query="searchKey="+searchKey+"&searchValue="+searchValue;
		}
		
		String listUrl = cp+"/pet/petInfo/list.do";
		String articleUrl = cp+"/pet/petInfo/article.do?page="+current_page;
		if(query.length()!=0){
			listUrl += "?"+query;
			articleUrl += "&"+query;
		}
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		
		
		forward(req, resp, "/WEB-INF/views/pet/petInfo/list.jsp");
	}

	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(info==null){
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		req.setAttribute("mode", "created");
		String path="/WEB-INF/views/pet/petInfo/created.jsp";
		forward(req, resp, path);
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		PetInfoDAO dao = new PetInfoDAO();
		String cp = req.getContextPath();
		if(info==null){
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		String encType="utf-8";
		int maxFilesize=10*1024*1024;
		 MultipartRequest mreq=new MultipartRequest(
		    		req, pathname, maxFilesize, encType,
		    		new DefaultFileRenamePolicy()
		    		);
		
		PetinfoDTO dto = new PetinfoDTO();
		dto.setMem_id(info.getMem_Id());
		/*dto.setNotice(Integer.parseInt(req.getParameter("notice")));*/
		dto.setSubject(mreq.getParameter("subject"));
		if(mreq.getParameter("notice")!=null)
	    	dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		dto.setCategory(mreq.getParameter("category"));
		
		dto.setContent(mreq.getParameter("content"));
		
		dao.insertBoard(dto);
		
		resp.sendRedirect(cp+"/pet/petInfo/list.do");
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		PetInfoDAO dao = new PetInfoDAO();
		MyUtil util = new MyUtil();
		
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		String page=req.getParameter("page");
		String searchKey=req.getParameter("searchKey");
		String searchValue=req.getParameter("searchValue");
		if(searchKey==null) {
			searchKey="subject";
			searchValue="";
		}
		
		searchValue=URLDecoder.decode(searchValue, "utf-8");
		
		dao.updateHitCount(bbs_num);
		
		PetinfoDTO dto = dao.readBoard(bbs_num);
		if(dto==null){
			resp.sendRedirect(cp+"/pet/petInfo/list.do?page="+page);
			return;
		}
		dto.setContent(util.htmlSymbols(dto.getContent()));
		
		PetinfoDTO preRead = dao.preReadBoard(bbs_num, searchKey, searchValue);
		PetinfoDTO nextRead = dao.nextReadBoard(bbs_num, searchValue, searchKey);
		
		String query="page="+page;
		if(searchValue.length()!=0) {
			query+="&searchKey="+searchKey
					+"&searchValue="+URLEncoder.encode(searchValue, "utf-8");
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		req.setAttribute("preRead", preRead);
		req.setAttribute("nextRead", nextRead);
		
		forward(req, resp, "/WEB-INF/views/pet/petInfo/article.jsp");
	}

	protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		PetInfoDAO dao = new PetInfoDAO();
		PetinfoDTO dto = dao.readBoard(bbs_num);
		
		if(info==null){
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		if(!dto.getMem_id().equals(info.getMem_Id())){
			resp.sendRedirect(cp+"/pet/petInfo/list.do?page="+page);
			return;
		}
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");
		
		forward(req, resp, "/WEB-INF/views/pet/petInfo/created.jsp");
	}

	protected void updated_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		PetInfoDAO dao = new PetInfoDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String encType="utf-8";
		int maxFilesize=10*1024*1024;
		MultipartRequest mreq=new MultipartRequest(
				req, pathname, maxFilesize, encType,
				new DefaultFileRenamePolicy()
				);
		
		String page = mreq.getParameter("page");
		
		if(req.getMethod().equals("GET")){
			resp.sendRedirect(cp+"/pet/petInfo/list.do?page="+page);
			return;
		}
		PetinfoDTO dto = new PetinfoDTO();
		dto.setMem_id(info.getMem_Id());
		/*dto.setNotice(Integer.parseInt(req.getParameter("notice")));*/
		dto.setSubject(mreq.getParameter("subject"));
		dto.setCategory(mreq.getParameter("category"));
		dto.setContent(mreq.getParameter("content"));
		dto.setBbs_num(Integer.parseInt(mreq.getParameter("bbs_num")));
		dao.updateBoard(dto, info.getMem_Id());
		
		
		resp.sendRedirect(cp+"/pet/petInfo/list.do?page="+page);
	}	

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		PetInfoDAO dao = new PetInfoDAO();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		dao.deleteBoard(bbs_num, info.getMem_Id());
		
		resp.sendRedirect(cp+"/pet/petInfo/list.do?page="+page);
	}
}

