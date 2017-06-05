package com.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.health.boardDTO;
import com.util.DBConn;

public class mainDAO {
	private Connection conn = DBConn.getConnection();
	
	//모든글 좋아요순 3개
	public List<boardDTO> all_bestArticle() {
		List<boardDTO> list = new ArrayList<boardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql="";
		
		
		
		try {
			sql = "SELECT ROWNUM rnum, tb.* FROM ("
				+ " ("
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount,"
				+ " '/soccer/board/article.do?bbs_num=' AS url, '축구-자유게시판' AS boardname, savefilename "
				+ " FROM SOCCER1 H JOIN member M ON H.MEM_ID=M.MEM_ID "
				+ " LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM SOCCER1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " JOIN SOCCER1_file F ON H.bbs_num = F.bbs_num"
				+ " UNION "
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount,"
				+ " '/health/articleB.do?num=' AS url, '건강-정보게시판' AS boardname, savefilename "
				+ " FROM health1 H JOIN member M ON H.MEM_ID=M.MEM_ID "
				+ " LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM health1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " JOIN health1_file F ON H.bbs_num = F.bbs_num "
				+ " UNION "
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/health/article.do?num=' AS url, '건강-자유게시판' AS boardname, savefilename "
				+ " FROM health2 H JOIN member M ON H.MEM_ID=M.MEM_ID LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM health2_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " JOIN health2_file F ON H.bbs_num = F.bbs_num "
				+ " UNION "
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/fashion/article1.do?bbs_num=' AS url, '패션-중고거래' AS boardname, '0' as savefilename "
				+ " FROM fashion1 H JOIN member M ON H.MEM_ID=M.MEM_ID LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM fashion1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " JOIN fashion1_file F ON H.bbs_num = F.bbs_num"
				+ " UNION "
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/food2/article.do?num=' AS url, '요리-레시피공유' AS boardname, savefilename "
				+ " FROM food2 H JOIN member M ON H.MEM_ID=M.MEM_ID LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM food2_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " JOIN food2_file F ON H.bbs_num = F.bbs_num "
				+ " UNION "
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/pet/petInfo/article.do?bbs_num=' AS url, '반려동물-정보공유' AS boardname, savefilename "
				+ " FROM pet1 H JOIN member M ON H.MEM_ID=M.MEM_ID LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM pet1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " JOIN pet1_file F ON H.bbs_num = F.bbs_num "
				+ " UNION "
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/pet/petTalk/article.do?bbs_num=' AS url, '반려동물-자유게시판' AS boardname, savefilename "
				+ " FROM pet2 H JOIN member M ON H.MEM_ID=M.MEM_ID LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM pet2_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " JOIN pet2_file F ON H.bbs_num = F.bbs_num "
				+ " UNION "
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/it/news_article.do?bbs_num=' AS url, 'IT-뉴스/신제품' AS boardname, '0' as savefilename "
				+ " FROM it1 H JOIN member M ON H.MEM_ID=M.MEM_ID LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM it1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
				+ " UNION"
				+ " SELECT h.BBS_Num, M.MEM_NAME, subject, hitCount, TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/it/board_article.do?bbs_num=' AS url, 'IT-자유게시판' AS boardname, '0' as savefilename "
				+ " FROM it2 H JOIN member M ON H.MEM_ID=M.MEM_ID LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM it2_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "

				+ ") ORDER BY likeCount DESC"
				+ " ) tb WHERE ROWNUM <= 3";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				boardDTO dto = new boardDTO();
				dto.setBbs_Num(rs.getInt("Bbs_Num"));
				dto.setMem_Name(rs.getString("mem_name"));
				dto.setSubject(rs.getString("subject"));			
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setLikeCount(rs.getInt("likeCount"));
				dto.setUrl(rs.getString("url"));
				dto.setSavefilename(rs.getString("savefilename"));
				dto.setBoardname(rs.getString("boardname"));
				list.add(dto);
			}
			
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	

	//글 조회 받아야할 변수(테이블명)
		public List<boardDTO> hitArticle(String tablename) {
			List<boardDTO> list = new ArrayList<boardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql="";
			String url1="";
			String url2="";
			if(tablename.equals("health")) {
				url1="/health/article.do?num=";
				url2="/health/articleB.do?num=";
			} else if(tablename.equals("soccer")) {
				url1="/soccer/board/article.do?bbs_num=";
				url2="/soccer/board/article.do?bbs_num=";
			} else if(tablename.equals("fashion")) {
				url1="/fashion/article1.do?bbs_num=";
				url2="/fashion/article2.do?bbs_num=";
			} else if(tablename.equals("it")) {
				url1="/it/news_article.do?bbs_num=";
				url2="/it/board_article.do?bbs_num=";
			} else if(tablename.equals("food")) {
				url1="/food1/article.do?num=";
				url2="/food2/article.do?num=";
			} else if(tablename.equals("pet")) {
				url1="/pet/petInfo/article.do?bbs_num=";
				url2="/pet/petTalk/article.do?bbs_num=";
			} else {
				System.out.println("메인 테이블네임오류");
			}
			
			try {
				sql = "SELECT ROWNUM rnum, tb.* FROM ("
					+ " (SELECT h.BBS_Num, subject, hitCount, "
					+ "  TO_CHAR(created, 'YY/MM/DD') created, '"+url1+"' AS url  "
					+ " FROM "+tablename+"1 H"
					+ " UNION"
					+ "  SELECT h.BBS_Num, subject, hitCount, "
					+ "  TO_CHAR(created, 'YY/MM/DD') created, '"+url2+"' AS url  "
					+ " FROM "+tablename+"2 H "
					+ "  )ORDER BY hitCount DESC"
					+ " ) tb WHERE ROWNUM <= 5";
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					boardDTO dto = new boardDTO();
					dto.setSubject(rs.getString("subject"));
					dto.setBbs_Num(rs.getInt("Bbs_Num"));
					dto.setUrl(rs.getString("url"));
					dto.setHitCount(rs.getInt("hitCount"));
					list.add(dto);
				}

				pstmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
	
	
	//soccer 출력 ( hitcount없음)
	public List<boardDTO> soccer_hitArticle() {
		List<boardDTO> list = new ArrayList<boardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql="";
		
		try {
			sql = "SELECT ROWNUM rnum, tb.* FROM ("
				+ " SELECT h.BBS_Num, M.MEM_NAME,"
				+ " subject, hitCount,"
				+ "  TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/soccer/board/article.do?bbs_num=' AS url "
				+ " FROM SOCCER1 H"
				+ " JOIN member M ON H.MEM_ID=M.MEM_ID"
				+ " LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM SOCCER1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num"
				+ " ORDER BY hitcount DESC"
				+ " ) tb WHERE ROWNUM <= 5";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				boardDTO dto = new boardDTO();
				dto.setSubject(rs.getString("subject"));
				dto.setBbs_Num(rs.getInt("Bbs_Num"));
				dto.setUrl(rs.getString("url"));
				dto.setHitCount(rs.getInt("hitCount"));
				list.add(dto);
			}

			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//추천글
	public List<boardDTO> bestArticle(String tablename) {
		List<boardDTO> list = new ArrayList<boardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql="";
		String url1="";
		String url2="";
		if(tablename.equals("health")) {
			url1="/health/article.do?num=";
			url2="/health/articleB.do?num=";
		} else if(tablename.equals("soccer")) {
			url1="/soccer/board/article.do?bbs_num=";
			url2="/soccer/board/article.do?bbs_num=";
		} else if(tablename.equals("fashion")) {
			url1="/fashion/article1.do?bbs_num=";
			url2="/fashion/article1.do?bbs_num=";
		} else if(tablename.equals("it")) {
			url1="/it/news_article.do?bbs_num=";
			url2="/it/board_article.do?bbs_num=";
		} else if(tablename.equals("food")) {
			url1="/food1/article.do?num=";
			url2="/food2/article.do?num=";
		} else if(tablename.equals("pet")) {
			url1="/pet/petInfo/article.do?bbs_num=";
			url2="/pet/petTalk/article.do?bbs_num=";
		} else {
			System.out.println("메인 테이블네임오류");
		}
		
		try {
sql = " SELECT ROWNUM rnum, tb.* FROM ( "
	+ " (SELECT H.BBS_Num, subject, NVL(likeCount, 0) likeCount, '"+url1+"' AS url "
	+ " FROM "+tablename+"1 H LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, "
	+ "BBS_NUM FROM "+tablename+"1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
	+ " UNION "
	+ "  SELECT H.BBS_Num, subject, NVL(likeCount, 0) likeCount, '"+url2+"' AS url "
	+ "  FROM "+tablename+"2 H LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, "
	+ "BBS_NUM FROM "+tablename+"2_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num) "
	+ "  ORDER BY likeCount DESC "
	+ "  ) tb WHERE ROWNUM <= 5 ";
			
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
			while(rs.next()) {
				boardDTO dto = new boardDTO();
				dto.setSubject(rs.getString("subject"));
				dto.setBbs_Num(rs.getInt("Bbs_Num"));
				dto.setUrl(rs.getString("url"));
				dto.setLikeCount(rs.getInt("likecount"));
				list.add(dto);
			}
			

			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
		//soccer 추천
		public List<boardDTO> soccer_bestArticle() {
			List<boardDTO> list = new ArrayList<boardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql="";
			
			try {
				sql = "SELECT ROWNUM rnum, tb.* FROM ("
					+ " SELECT h.BBS_Num, M.MEM_NAME,"
					+ " subject, hitCount,"
					+ "  TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount, '/soccer/board/article.do?bbs_num=' AS url "
					+ " FROM SOCCER1 H"
					+ " JOIN member M ON H.MEM_ID=M.MEM_ID"
					+ " LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM SOCCER1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num"
					+ " ORDER BY likeCount DESC"
					+ " ) tb WHERE ROWNUM <= 5";
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					boardDTO dto = new boardDTO();
					dto.setSubject(rs.getString("subject"));
					dto.setBbs_Num(rs.getInt("Bbs_Num"));
					dto.setUrl(rs.getString("url"));
					dto.setLikeCount(rs.getInt("likeCount"));
					list.add(dto);
				}
				

				pstmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		
		public List<boardDTO> manyArticle() {
			List<boardDTO> list = new ArrayList<boardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql="";
			
			try {
				sql = " SELECT  distinct sum(memsarticle) Over(PARTITION by mem_NAME)memsarticle , mem_Name FROM ( "
					+ " SELECT  tb.* FROM ( "
					+ " select count(*) memsarticle, mem_Name FROM it1 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM it2 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM fashion1 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM fashion2 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM pet1 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM pet2 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM soccer1 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM soccer2 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM food1 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM food2 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM health1 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsarticle, mem_Name FROM health2 f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name "
					+ " )tb WHERE ROWNUM <= 5 "
					+ " )ORDER by memsarticle desc ";
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					boardDTO dto = new boardDTO();
					dto.setMemsarticle(rs.getInt("memsarticle"));
					dto.setMem_Name(rs.getString("mem_name"));
					list.add(dto);
				}
				

				pstmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		public List<boardDTO> manyreplArticle() {
			List<boardDTO> list = new ArrayList<boardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql="";
			
			try {
				sql = " SELECT  distinct sum(memsrepl) Over(PARTITION by mem_NAME)memsrepl , mem_Name FROM ( "
					+ " SELECT  tb.* FROM ( "
					+ " select count(*) memsrepl, mem_Name FROM it1_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM it2_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM fashion1_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM fashion2_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM pet1_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM pet2_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM soccer1_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM food1_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM food2_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM health1_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION "
					+ " select count(*) memsrepl, mem_Name FROM health2_reply f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name "
					+ " )tb WHERE ROWNUM <= 5 "
					+ " )ORDER by memsrepl desc ";
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					boardDTO dto = new boardDTO();
					dto.setMemsrepl(rs.getInt("memsrepl"));
					dto.setMem_Name(rs.getString("mem_name"));
					list.add(dto);
				}
				

				pstmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		public List<boardDTO> pointMem() {
			List<boardDTO> list = new ArrayList<boardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql="";
			
			try {
				sql = "SELECT ROWNUM rnum, tb.* FROM ("
					+ "SELECT point, MEM_NAME FROM MEMBER ORDER BY POINT DESC"
					+ ") tb WHERE ROWNUM <= 5";
				
		
						
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					boardDTO dto = new boardDTO();
					dto.setPoint(rs.getInt("point"));
					dto.setMem_Name(rs.getString("mem_name"));
					list.add(dto);
				}
				

				pstmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			
			return list;
		}
		
		public List<boardDTO> goodMem() {
		    List<boardDTO> list = new ArrayList<boardDTO>();
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    String sql="";
		    
		    try {
		       sql = "SELECT  distinct sum(memslike) Over(PARTITION by mem_NAME)memslike , mem_Name FROM ( ";
		       sql += " SELECT  tb.* FROM ( ";
		       sql += " select count(*) memslike, mem_Name FROM it1_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM it2_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM fashion1_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM fashion2_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM pet1_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM pet2_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM soccer1_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM food1_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM food2_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM health1_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name UNION ";
		       sql += " select count(*) memslike, mem_Name FROM health2_like f1 JOIN member m ON f1.mem_Id = m.mem_Id group by mem_Name ";
		       sql += " )tb WHERE ROWNUM <= 5 ";
		       sql += ")ORDER by memslike DESC";
		       
		       pstmt = conn.prepareStatement(sql);
		       rs = pstmt.executeQuery();
		       while(rs.next()) {
		          boardDTO dto = new boardDTO();
		          dto.setLikeCount(rs.getInt("memslike"));
		          dto.setMem_Name(rs.getString("mem_Name"));
		          list.add(dto);
		       }
		       

				pstmt.close();
				rs.close();
		    } catch (Exception e) {
		       e.printStackTrace();
		    }
		    return list;
		 }
		
		
		
	
}
