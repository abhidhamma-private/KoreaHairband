package com.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.health.boardDTO;
import com.util.MyServlet;

@WebServlet("/main.do")
public class MainServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		if(uri.indexOf("main.do")!=-1) {
			main(req, resp);
		}
	}
	
	protected void main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		mainDAO dao = new mainDAO();
		//리스트 만들거 총 17개
		
		//전체 추천3위 1개 ablist
		List<boardDTO> ablist = dao.all_bestArticle();
		
		//스포츠 2개 shlist sblist
		List<boardDTO> shlist = dao.soccer_hitArticle();
		List<boardDTO> sblist = dao.soccer_bestArticle();
		
		//건강 2개 hhlist hblist
		List<boardDTO> hhlist = dao.hitArticle("health");
		List<boardDTO> hblist = dao.bestArticle("health");
		
		//패션 2개 fshlist fsblist
		List<boardDTO> fshlist = dao.hitArticle("fashion");
		List<boardDTO> fsblist = dao.bestArticle("fashion");
		
		//푸드 2개 fhlist fblist
		List<boardDTO> fhlist = dao.hitArticle("food");
		List<boardDTO> fblist = dao.bestArticle("food");
		
		//반려동물 2개 phlist pblist
		List<boardDTO> phlist = dao.hitArticle("pet");
		List<boardDTO> pblist = dao.bestArticle("pet");
		
		//IT 2개 ithlist itblist
		List<boardDTO> ithlist = dao.hitArticle("it");
		List<boardDTO> itblist = dao.bestArticle("it");
		
		//카테고리 4개 memal_list, memre_list, nlist, clist
		List<boardDTO> memal_list = dao.manyArticle();
		List<boardDTO> memre_list = dao.manyreplArticle();
		List<boardDTO> mempo_list = dao.pointMem();
		List<boardDTO> memgood_list = dao.goodMem();
		
		//리스트 만들거 총 17개
		//전체 추천3위 1개
		req.setAttribute("ablist", ablist);
		//스포츠 2개
		req.setAttribute("shlist", shlist);
		req.setAttribute("sblist", sblist);
		//건강 2개
		req.setAttribute("hhlist", hhlist);
		req.setAttribute("hblist", hblist);
		//패션 2개
		req.setAttribute("fshlist", fshlist);
		req.setAttribute("fsblist", fsblist);
		//푸드 2개
		req.setAttribute("fhlist", fhlist);
		req.setAttribute("fblist", fblist);
		//반려동물 2개
		req.setAttribute("phlist", phlist);
		req.setAttribute("pblist", pblist);
		//IT 2개
		req.setAttribute("ithlist", ithlist);
		req.setAttribute("itblist", itblist);
		//회원랭킹 4개
		req.setAttribute("memal_list", memal_list);
		req.setAttribute("memre_list", memre_list);
		req.setAttribute("mempo_list", mempo_list);
		req.setAttribute("memgood_list", memgood_list);
		
		forward(req, resp, "/WEB-INF/views/main/main.jsp");
		
		
	}
}
