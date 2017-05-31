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
		// 포워딩
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		if (uri.indexOf("login.do") != -1) {
			// 로그인 폼
			login(req, resp);
		} else if (uri.indexOf("login_ok.do") != -1) {
			// 로그인 처리
			login_ok(req, resp);
		} else if (uri.indexOf("mypage.do") != -1) {
			mypage(req, resp);
		} else if (uri.indexOf("logout.do") != -1) {
			// 로그아웃 처리
			logout(req, resp);
		} else if (uri.indexOf("member.do") != -1) {
			// 회원가입폼
			member(req, resp);
		} else if (uri.indexOf("member_ok.do") != -1) {
			// 회원가입처리
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
		// 로그인 페이지로 포워딩
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}

	protected void login_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 처리
		MemberDAO dao = new MemberDAO();

		// 파리미터 받기
		String mem_Id = req.getParameter("mem_Id");
		String mem_Pwd = req.getParameter("mem_Pwd");

		MemberDTO dto = dao.readMember(mem_Id);
		if (dto == null || !mem_Pwd.equals(dto.getMem_Pwd())) {
			req.setAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}

		// 로그인 정보를 세션에 저장한다.
		HttpSession session = req.getSession(); // 세션객체

		info = new SessionInfo();
		info.setMem_Id(dto.getMem_Id());
		info.setMem_Name(dto.getMem_Name());

		// member라는 이름으로 세션에 SessionInfo 객체를 저장한다.
		session.setAttribute("member", info);

		// 메인으로 리다이렉트 한다.
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/main.do");
	}
	protected void mypage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 세션값으로 회원정보 로드
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.readMember(info.getMem_Id());
		
		if (dto == null) {
			// 로그인 페이지로 포워딩
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		req.setAttribute("dto", dto);
		forward(req, resp, "/WEB-INF/views/member/mypage.jsp");
	}

	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그아웃 처리
		// 세션의 저장된 로그인 정보를 지운다.
		HttpSession session = req.getSession();

		session.removeAttribute("member");

		// 세션에 저장된 모든 정보를 지우고 세션을 초기화 한다.
		session.invalidate();

		// /로 리다이렉트 한다.
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/");
	}

	protected void member(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입폼
		req.setAttribute("mode", "created");
		req.setAttribute("title", "회원 가입");
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	protected void member_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 처리
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();
		
		// 이미지 용량/인코딩타입 설정
		String encType="utf-8";
		int maxSize=1*1024*1024;

		// 이미지 저장 경로
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
			String message = "회원 가입이 실패 했습니다.";

			req.setAttribute("title", "회원 가입");
			req.setAttribute("mode", "created");
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getMem_Name() + "</b>님 회원가입이 되었습니다.<br>");
		sb.append("메인화면으로 이동하여 로그인 하시기 바랍니다.<br>");

		req.setAttribute("title", "회원 가입");
		req.setAttribute("message", sb.toString());

		forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	}
	protected void pwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String mode = req.getParameter("update");
		
		
		if(mode == null){
			req.setAttribute("mode", "delete");
			req.setAttribute("title", "회원탈퇴");
			req.setAttribute("message", "한번 삭제된 개인정보는 복구되지 않습니다!");

			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
		} else {
			req.setAttribute("mode", "update");
			req.setAttribute("title", "정보수정");
			req.setAttribute("message", "회원님의 정보를 수정합니다.");

			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
		}
	}
	
	protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 회원정보 로드
		MemberDAO dao = new MemberDAO();

		// 파리미터 받기
		String mem_Id = req.getParameter("mem_Id");
		String mem_Pwd = req.getParameter("mem_Pwd");

		MemberDTO dto = dao.readMember(mem_Id);
		if (dto == null || !mem_Pwd.equals(dto.getMem_Pwd())) {
			req.setAttribute("mode", "update");
			req.setAttribute("title", "정보수정");
			req.setAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
			return;
		}
		// 회원수정폼
		req.setAttribute("dto", dto);
		req.setAttribute("mode", "update");
		req.setAttribute("title", "정보 수정");
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	protected void update_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 정보수정 처리
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();

		// 이미지 용량/인코딩타입 설정
		String encType="utf-8";
		int maxSize=1*1024*1024;
		
		// 이미지 저장 경로
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
		
		// 이미지 파일을 업로드 한경우
		if(mreq.getFile("mem_img")!=null) {
			// 기존 이미지 파일 지우기
			FileManager.doFiledelete(pathname, mem_img);
			
			// 서버에 저장된 파일명
			String server_img=mreq.getFilesystemName("mem_img");
			
			// 파일 이름 변경
			server_img = FileManager.doFilerename(pathname, server_img);
			
			dto.setMem_img(server_img);
		} else {
			// 새로운 이미지 파일을 올리지 않은 경우 기존 이미지 파일로
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
			req.setAttribute("title", "정보 수정");
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getMem_Name() + "</b>님 정보수정을 성공했습니다.<br>");
		sb.append("아래 버튼을 누르면 메인화면으로 이동합니다.<br>");

		req.setAttribute("title", "정보 수정 완료!");
		req.setAttribute("message", sb.toString());

		forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "delete");
		req.setAttribute("title", "회원탈퇴");
		req.setAttribute("message", "한번 삭제된 개인정보는 복구되지 않습니다!");

		forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
	}

	protected void delete_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원정보 로드
		MemberDAO dao = new MemberDAO();

		// 파리미터 받기
		String mem_Id = req.getParameter("mem_Id");
		String mem_Pwd = req.getParameter("mem_Pwd");

		MemberDTO dto = dao.readMember(mem_Id);
		if (dto == null || !mem_Pwd.equals(dto.getMem_Pwd())) {
			req.setAttribute("mode", "delete");
			req.setAttribute("title", "회원탈퇴");
			req.setAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
			return;
		}

		dao.deleteMember(mem_Id);
		// 로그아웃 처리
		// 세션의 저장된 로그인 정보를 지운다.
		HttpSession session = req.getSession();
		session.removeAttribute("member");
		// 세션에 저장된 모든 정보를 지우고 세션을 초기화 한다.
		session.invalidate();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getMem_Name() + "</b>님 그동안 이용해주셔서 감사합니다.<br>");
		sb.append("아래 버튼을 누르면 메인화면으로 이동합니다.<br>");

		req.setAttribute("title", "회원 탈퇴 완료!");
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

		// JSON 형식 => {"키1":"값1","키2":"값2"}
		// {"passed":"true"}
		JSONObject job = new JSONObject();
		job.put("passed", passed);

		// 클라이언트에게 전송
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}

}
