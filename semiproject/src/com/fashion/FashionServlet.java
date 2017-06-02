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
	
	
	// ������ ������ ���(pathname)
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
		// ��� �߰�
		insertReply(req, resp);
	} else if(uri.indexOf("listReply.do")!=-1) {
		// ��� ����Ʈ
		listReply(req, resp);
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
		
		//�������ۺ��� - Id �޾ƿ���
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
		
		
		//�Խù�����Ʈ
		List<Fashion1DTO> list;
		
		if(searchValue.length() == 0 )
			list = dao1.listBoard(start, end);
		
		else
			list = dao1.listBoard(start, end, searchKey, searchValue, category);
		
		  
		// �Խù� ��ȣ ����
		int listNum, n = 0;
		
		long gap;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	   
		
		boolean checkImg = false;
		
		
				// ��� �÷���(Collection)���� ���� ������ ���� �� �ִ� Interface
		Iterator<Fashion1DTO> it = list.iterator();
		
		while(it.hasNext()){
			Fashion1DTO dto1 = it.next();
			listNum = dataCount - (start + n - 1);
			dto1.setListNum(listNum);
			
			//new ����
			try
			{
				Date sDate = format.parse(dto1.getCreated());
				gap = (date.getTime() - sDate.getTime()) / (60 * 60* 1000); //�����ġ�� 
				
				dto1.setCreated(dto1.getCreated().substring(0, 10));
				dto1.setGap(gap);
				
			} catch(ParseException e) {

			}
			
		//������ ���� img�±� ������ 
			if(dto1.getContent().contains("<img"))
				checkImg=true;
			
				dto1.setCheckImg(checkImg);
			
			
			//System.out.println(dto1.getGap());
			//System.out.println(dto1.getContent());
			n++;
		}
		String query = "";
		
		// �˻��ϴ� ��� ����¡ �� �ۺ��⿡ ����� Parameter
		if(searchValue.length() != 0){
			query = "searchKey=" + searchKey + "&searchValue=" + searchValue;
			
			if(category.length() !=0 )
				query +="&category="+category;
			
		}
		
	/*	//�������ۺ��� �˻��Ѱ�� 
		if(searchMyList.length() !=0 ){
			query ="mem_Id="+searchMyList;
		}*/
		
		// �� ����Ʈ �ּ�
		String listUrl = cp + "/fashion/list1.do";
		// �ۺ��� �ּ�
		String articleUrl = cp + "/fashion/article1.do?page=" + current_page;
		
		if(query.length() != 0)
		{
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		// ����¡ ó��
		String paging = util.paging(current_page, total_page, listUrl);
		
		// Forwarding �������� �ѱ� ������
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		
		
		forward(req, resp, "/WEB-INF/views/fashion/list1.jsp");
		  
	}
	protected void createdForm1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");// �۾��� ��
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
		if(mreq.getParameter("notice")!=null)//�����ΰ��
		    	dto1.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		  */
		dao1.insertBoard(dto1);
		
		
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
		
		// �˻� ���� ��� �⺻�� ����
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
		
		// �Խù� ���� �������� Forwarding
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
		// ����
				Fashion1DAO dao1 = new Fashion1DAO();
				int bbs_num = Integer.parseInt(req.getParameter("bbs_num"));
				String cp = req.getContextPath();
				String page = req.getParameter("page");
				
				// �˻� �÷��� �˻� �� �ޱ�
				String searchKey = req.getParameter("searchKey");
				String searchValue = req.getParameter("searchValue");
				
				String category = req.getParameter("category");
				
				// �˻� ���� ��� �⺻�� ����
				if(searchKey == null)
				{
					searchKey = "subject";
					searchValue = "";
				}
				
				if(category ==null)
					category="";
				
				
				searchValue = URLDecoder.decode(searchValue, "UTF-8");

				dao1.deleteBoard(bbs_num, info.getMem_Id());
				
				// ����Ʈ�� ������/�����ۿ��� ����� Parameter
				String query = cp + "/fashion/list1.do?page=" + page;
				
				// �˻��ϴ� ��� ����¡ �� �ۺ��⿡ ����� Parameter
				if(searchValue.length() != 0){
					query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
				
					if(category.length() !=0 )
						query +="&category="+category;	
				}
				// Server���� Server��(Servlet => jsp ������ �Ѱ���)
				// Client�� �ּҰ� �����		request, response ��ü �ʱ�ȭ
				resp.sendRedirect(query);
				
				
				// Server���� Client���� �ٸ� �ּҷ� �������ϵ��� �䱸
				// Client�� �ּҰ� ������� ����		request, response ��ü�� �Ѱ���
				// forward(req, resp, "/WEB-INF/views/bbs/list.jsp");
	}
	
	private void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ����Ʈ(AJAX:TEXT)
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

		// ����Ʈ�� ����� ������
		List<F1replyDTO> listReply = dao.listReply(bbs_num, start, end);

		// ���͸� <br>
		Iterator<F1replyDTO> it = listReply.iterator();
		while (it.hasNext()) {
			F1replyDTO dto = it.next();
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			System.out.println(dto.getContent());
		}

		// ����¡ó��(�μ�2�� ¥�� js�� ó��)
		String paging = util.paging(current_page, total_page);

		req.setAttribute("listReply", listReply);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		

		// ������
		String path = "/WEB-INF/views/fashion/listReply.jsp";
		forward(req, resp, path);
	}

	private void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ����(JSON)
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
		}
		
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
}
