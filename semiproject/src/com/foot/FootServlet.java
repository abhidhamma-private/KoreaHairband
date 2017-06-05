package com.foot;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.util.MyServlet;

@WebServlet("/foot/*")
public class FootServlet extends MyServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		
		if (uri.indexOf("policy.do") != -1) {
			policy(req, resp);
		} else if (uri.indexOf("info.do") != -1) {
			info(req, resp);
		} else if (uri.indexOf("privacy.do") != -1) {
			privacy(req, resp);
		} else if (uri.indexOf("service.do") != -1) {
			service_policy(req, resp);
		} else if (uri.indexOf("proposal.do") != -1) {
			proposal(req, resp);
		} 

	}
	

	protected void policy(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String path = "/WEB-INF/views/foot/policy.jsp";
		System.out.println(path);
		forward(req, resp, path);
	}

	protected void info(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String path = "/WEB-INF/views/foot/info.jsp";
		System.out.println(path);
		forward(req, resp, path);
	}

	protected void privacy(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String path = "/WEB-INF/views/foot/privacy.jsp";
		System.out.println(path);
		forward(req, resp, path);
	}

	
	protected void service_policy(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String path = "/WEB-INF/views/foot/service.jsp";
		System.out.println(path);
		forward(req, resp, path);
	}
	
	
	protected void proposal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String path = "/WEB-INF/views/foot/proposal.jsp";
		System.out.println(path);
		forward(req, resp, path);
	}

	
}
