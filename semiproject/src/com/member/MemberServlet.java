package com.member;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.FileManager;

import net.sf.json.JSONObject;

@WebServlet("/member/*")
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private SessionInfo info = null;
	private String pathname;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path)
			throws ServletException, IOException {
		// ������
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		if (uri.indexOf("login.do") != -1) {
			// �α��� ��
			login(req, resp);
		} else if (uri.indexOf("login_ok.do") != -1) {
			// �α��� ó��
			login_ok(req, resp);
		} else if (uri.indexOf("mypage.do") != -1) {
			mypage(req, resp);
		} else if (uri.indexOf("logout.do") != -1) {
			// �α׾ƿ� ó��
			logout(req, resp);
		} else if (uri.indexOf("member.do") != -1) {
			// ȸ��������
			member(req, resp);
		} else if (uri.indexOf("member_ok.do") != -1) {
			// ȸ������ó��
			member_ok(req, resp);
		} else if (uri.indexOf("pwd.do") != -1) {
			pwd(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			update(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			update_ok(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if (uri.indexOf("delete_ok.do") != -1) {
			delete_ok(req, resp);
		} else if (uri.indexOf("mem_IdCheck.do") != -1) {
			mem_IdCheck(req, resp);
		} 
	}

	protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α��� �������� ������
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}

	protected void login_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α��� ó��
		MemberDAO dao = new MemberDAO();

		// �ĸ����� �ޱ�
		String mem_Id = req.getParameter("mem_Id");
		String mem_Pwd = req.getParameter("mem_Pwd");

		MemberDTO dto = dao.readMember(mem_Id);
		if (dto == null || !mem_Pwd.equals(dto.getMem_Pwd())) {
			req.setAttribute("message", "���̵� �Ǵ� �н����尡 ��ġ���� �ʽ��ϴ�.");
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}

		// �α��� ������ ���ǿ� �����Ѵ�.
		HttpSession session = req.getSession(); // ���ǰ�ü

		info = new SessionInfo();
		info.setMem_Id(dto.getMem_Id());
		info.setMem_Name(dto.getMem_Name());

		// member��� �̸����� ���ǿ� SessionInfo ��ü�� �����Ѵ�.
		session.setAttribute("member", info);

		// �������� �����̷�Ʈ �Ѵ�.
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/main.do");
	}
	protected void mypage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���ǰ����� ȸ������ �ε�
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.readMember(info.getMem_Id());
		
		if (dto == null) {
			// �α��� �������� ������
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		req.setAttribute("dto", dto);
		forward(req, resp, "/WEB-INF/views/member/mypage.jsp");
	}

	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α׾ƿ� ó��
		// ������ ����� �α��� ������ �����.
		HttpSession session = req.getSession();

		session.removeAttribute("member");

		// ���ǿ� ����� ��� ������ ����� ������ �ʱ�ȭ �Ѵ�.
		session.invalidate();

		// /�� �����̷�Ʈ �Ѵ�.
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/");
	}

	protected void member(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ��������
		req.setAttribute("mode", "created");
		req.setAttribute("title", "ȸ�� ����");
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	protected void member_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������ ó��
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();
		
		// �̹��� �뷮/���ڵ�Ÿ�� ����
		String encType="utf-8";
		int maxSize=1*1024*1024;

		// �̹��� ���� ���
		HttpSession session=req.getSession();
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+"photo";
		File f=new File(pathname);
		if(! f.exists())
			f.mkdirs();
				
				
		MultipartRequest mreq=new MultipartRequest(
				req, pathname, maxSize, encType,
				new DefaultFileRenamePolicy());


		dto.setMem_Id(mreq.getParameter("mem_Id"));
		dto.setMem_Name(mreq.getParameter("mem_Name"));
		dto.setMem_Pwd(mreq.getParameter("mem_Pwd"));
		
		if(mreq.getFile("mem_img")!=null) {
			String mem_img=mreq.getFilesystemName("mem_img");
			dto.setMem_img(mem_img);
		}
		
		dto.setBirth(mreq.getParameter("birth"));
		dto.setEmail1(mreq.getParameter("email1"));
		dto.setEmail2(mreq.getParameter("email2"));
		dto.setTel1(mreq.getParameter("tel1"));
		dto.setTel2(mreq.getParameter("tel2"));
		dto.setTel3(mreq.getParameter("tel3"));
		dto.setZip(mreq.getParameter("zip"));
		dto.setAddr1(mreq.getParameter("addr1"));
		dto.setAddr2(mreq.getParameter("addr2"));

		int result = dao.insertMember(dto);
		if (result == 0) {
			String message = "ȸ�� ������ ���� �߽��ϴ�.";

			req.setAttribute("title", "ȸ�� ����");
			req.setAttribute("mode", "created");
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getMem_Name() + "</b>�� ȸ�������� �Ǿ����ϴ�.<br>");
		sb.append("����ȭ������ �̵��Ͽ� �α��� �Ͻñ� �ٶ��ϴ�.<br>");

		req.setAttribute("title", "ȸ�� ����");
		req.setAttribute("message", sb.toString());

		forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	}
	protected void pwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String mode = req.getParameter("update");
		
		
		if(mode == null){
			req.setAttribute("mode", "delete");
			req.setAttribute("title", "ȸ��Ż��");
			req.setAttribute("message", "�ѹ� ������ ���������� �������� �ʽ��ϴ�!");

			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
		} else {
			req.setAttribute("mode", "update");
			req.setAttribute("title", "��������");
			req.setAttribute("message", "ȸ������ ������ �����մϴ�.");

			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
		}
	}
	
	protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// ȸ������ �ε�
		MemberDAO dao = new MemberDAO();

		// �ĸ����� �ޱ�
		String mem_Id = req.getParameter("mem_Id");
		String mem_Pwd = req.getParameter("mem_Pwd");

		MemberDTO dto = dao.readMember(mem_Id);
		if (dto == null || !mem_Pwd.equals(dto.getMem_Pwd())) {
			req.setAttribute("mode", "update");
			req.setAttribute("title", "��������");
			req.setAttribute("message", "���̵� �Ǵ� �н����尡 ��ġ���� �ʽ��ϴ�.");
			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
			return;
		}
		// ȸ��������
		req.setAttribute("dto", dto);
		req.setAttribute("mode", "update");
		req.setAttribute("title", "���� ����");
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	protected void update_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �������� ó��
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();

		// �̹��� �뷮/���ڵ�Ÿ�� ����
		String encType="utf-8";
		int maxSize=1*1024*1024;
		
		// �̹��� ���� ���
		HttpSession session=req.getSession();
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+"photo";
		File f=new File(pathname);
		if(! f.exists())
			f.mkdirs();
				
		MultipartRequest mreq=new MultipartRequest(
				req, pathname, maxSize, encType,
				new DefaultFileRenamePolicy());
				
		String mem_img=mreq.getParameter("mem_img");
		dto.setMem_Id(mreq.getParameter("mem_Id"));
		dto.setMem_Name(mreq.getParameter("mem_Name"));
		dto.setMem_Pwd(mreq.getParameter("mem_Pwd"));
		
		// �̹��� ������ ���ε� �Ѱ��
		if(mreq.getFile("mem_img")!=null) {
			// ���� �̹��� ���� �����
			FileManager.doFiledelete(pathname, mem_img);
			
			// ������ ����� ���ϸ�
			String server_img=mreq.getFilesystemName("mem_img");
			
			// ���� �̸� ����
			server_img = FileManager.doFilerename(pathname, server_img);
			
			dto.setMem_img(server_img);
		} else {
			// ���ο� �̹��� ������ �ø��� ���� ��� ���� �̹��� ���Ϸ�
			dto.setMem_img(mem_img);
		}
		dto.setBirth(mreq.getParameter("birth"));
		dto.setEmail1(mreq.getParameter("email1"));
		dto.setEmail2(mreq.getParameter("email2"));
		dto.setTel1(mreq.getParameter("tel1"));
		dto.setTel2(mreq.getParameter("tel2"));
		dto.setTel3(mreq.getParameter("tel3"));
		dto.setZip(mreq.getParameter("zip"));
		dto.setAddr1(mreq.getParameter("addr1"));
		dto.setAddr2(mreq.getParameter("addr2"));
		int result = dao.updateMember(dto);
		if (result == 0) {
			dto = dao.readMember("mem_Id");
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");
			req.setAttribute("title", "���� ����");
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getMem_Name() + "</b>�� ���������� �����߽��ϴ�.<br>");
		sb.append("�Ʒ� ��ư�� ������ ����ȭ������ �̵��մϴ�.<br>");

		req.setAttribute("title", "���� ���� �Ϸ�!");
		req.setAttribute("message", sb.toString());

		forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "delete");
		req.setAttribute("title", "ȸ��Ż��");
		req.setAttribute("message", "�ѹ� ������ ���������� �������� �ʽ��ϴ�!");

		forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
	}

	protected void delete_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������ �ε�
		MemberDAO dao = new MemberDAO();

		// �ĸ����� �ޱ�
		String mem_Id = req.getParameter("mem_Id");
		String mem_Pwd = req.getParameter("mem_Pwd");

		MemberDTO dto = dao.readMember(mem_Id);
		if (dto == null || !mem_Pwd.equals(dto.getMem_Pwd())) {
			req.setAttribute("mode", "delete");
			req.setAttribute("title", "ȸ��Ż��");
			req.setAttribute("message", "���̵� �Ǵ� �н����尡 ��ġ���� �ʽ��ϴ�.");
			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
			return;
		}

		dao.deleteMember(mem_Id);
		// �α׾ƿ� ó��
		// ������ ����� �α��� ������ �����.
		HttpSession session = req.getSession();
		session.removeAttribute("member");
		// ���ǿ� ����� ��� ������ ����� ������ �ʱ�ȭ �Ѵ�.
		session.invalidate();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getMem_Name() + "</b>�� �׵��� �̿����ּż� �����մϴ�.<br>");
		sb.append("�Ʒ� ��ư�� ������ ����ȭ������ �̵��մϴ�.<br>");

		req.setAttribute("title", "ȸ�� Ż�� �Ϸ�!");
		req.setAttribute("message", sb.toString());

		forward(req, resp, "/WEB-INF/views/member/complete.jsp");

	}

	protected void mem_IdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();
		String mem_Id = req.getParameter("mem_Id");

		String passed = "true";
		MemberDTO dto = dao.readMember(mem_Id);
		if (dto != null)
			passed = "false";

		// JSON ���� => {"Ű1":"��1","Ű2":"��2"}
		// {"passed":"true"}
		JSONObject job = new JSONObject();
		job.put("passed", passed);

		// Ŭ���̾�Ʈ���� ����
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}

}
