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
		//����Ʈ ����� �� 17��
		
		//��ü ��õ3�� 1�� ablist
		List<boardDTO> ablist = dao.all_bestArticle();
		
		//������ 2�� shlist sblist
		List<boardDTO> shlist = dao.soccer_hitArticle();
		List<boardDTO> sblist = dao.soccer_bestArticle();
		
		//�ǰ� 2�� hhlist hblist
		List<boardDTO> hhlist = dao.hitArticle("health");
		List<boardDTO> hblist = dao.bestArticle("health");
		
		//�м� 2�� fshlist fsblist
		List<boardDTO> fshlist = dao.hitArticle("fashion");
		List<boardDTO> fsblist = dao.bestArticle("fashion");
		
		//Ǫ�� 2�� fhlist fblist
		List<boardDTO> fhlist = dao.hitArticle("food");
		List<boardDTO> fblist = dao.bestArticle("food");
		
		//�ݷ����� 2�� phlist pblist
		List<boardDTO> phlist = dao.hitArticle("pet");
		List<boardDTO> pblist = dao.bestArticle("pet");
		
		//IT 2�� ithlist itblist
		List<boardDTO> ithlist = dao.hitArticle("it");
		List<boardDTO> itblist = dao.bestArticle("it");
		
		//ī�װ� 4�� memal_list, memre_list, nlist, clist
		List<boardDTO> memal_list = dao.manyArticle();
		List<boardDTO> memre_list = dao.manyreplArticle();
		List<boardDTO> mempo_list = dao.pointMem();
		List<boardDTO> memgood_list = dao.goodMem();
		
		//����Ʈ ����� �� 17��
		//��ü ��õ3�� 1��
		req.setAttribute("ablist", ablist);
		//������ 2��
		req.setAttribute("shlist", shlist);
		req.setAttribute("sblist", sblist);
		//�ǰ� 2��
		req.setAttribute("hhlist", hhlist);
		req.setAttribute("hblist", hblist);
		//�м� 2��
		req.setAttribute("fshlist", fshlist);
		req.setAttribute("fsblist", fsblist);
		//Ǫ�� 2��
		req.setAttribute("fhlist", fhlist);
		req.setAttribute("fblist", fblist);
		//�ݷ����� 2��
		req.setAttribute("phlist", phlist);
		req.setAttribute("pblist", pblist);
		//IT 2��
		req.setAttribute("ithlist", ithlist);
		req.setAttribute("itblist", itblist);
		//ȸ����ŷ 4��
		req.setAttribute("memal_list", memal_list);
		req.setAttribute("memre_list", memre_list);
		req.setAttribute("mempo_list", mempo_list);
		req.setAttribute("memgood_list", memgood_list);
		
		forward(req, resp, "/WEB-INF/views/main/main.jsp");
		
		
	}
}
