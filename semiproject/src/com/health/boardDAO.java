package com.health;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class boardDAO {
	private Connection conn = DBConn.getConnection();

	public int insertBoard(boardDTO dto, String mode) {

		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;

		try {
			sql = "SELECT health1_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			seq = 0;
			if (rs.next())
				seq = rs.getInt(1);
			rs.close();
			pstmt.close();
			dto.setBbs_Num(seq);

			if (mode.equals("created")) {
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if (mode.equals("reply")) {
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
			}

			sql = " INSERT ALL " + " INTO health1(bbs_Num, mem_id, subject, content,groupNum, depth, orderNo, parent) "
					+ " VALUES(?,?,?,?,?,?,?,?) " + " INTO health1_file(file_num,bbs_num,savefilename) "
					+ " VALUES(health1_file_seq.nextval, ?, ?) " + " SELECT * FROM dual ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_Num());
			pstmt.setString(2, dto.getMem_Id());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setInt(8, dto.getParent());
			pstmt.setInt(9, dto.getBbs_Num());
			pstmt.setString(10, dto.getSavefilename());
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int updateOrderNo(int groupNum, int orderNo) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE health1 SET orderNo=orderNo+1 WHERE groupNum = ? AND orderNo > ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM health1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<boardDTO> listBoard(int start, int end) {
		List<boardDTO> list = new ArrayList<boardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("         SELECT h.BBS_Num, H.MEM_ID, M.MEM_NAME, ");
			sb.append("               subject,CONTENT, groupNum, orderNo, depth, hitCount, ");
			sb.append("                TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount ");
			sb.append("               FROM health1 H ");
			sb.append("               JOIN member M ON H.MEM_ID=M.MEM_ID ");
			sb.append("               LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM health1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num ");
			sb.append("               ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				boardDTO dto = new boardDTO();
				dto.setBbs_Num(rs.getInt("Bbs_Num"));
				dto.setMem_Id(rs.getString("Mem_Id"));
				dto.setMem_Name(rs.getString("mem_name"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setLikeCount(rs.getInt("likeCount"));
				list.add(dto);
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return list;
	}

	public boardDTO readboard(int bbs_num) {
		boardDTO dto = new boardDTO();
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			sql = " SELECT * FROM health1 H1 JOIN health1_file F1 ON H1.bbs_num = F1.bbs_num where H1.bbs_num=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto.setBbs_Num(rs.getInt("bbs_num"));
				dto.setMem_Id(rs.getString("mem_id"));
				dto.setSavefilename(rs.getString("savefilename"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitcount"));
				dto.setGroupNum(rs.getInt("GroupNum"));
				
				dto.setOrderNo(rs.getInt("orderno"));
				dto.setDepth(rs.getInt("depth"));
				dto.setParent(rs.getInt("parent"));

				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	public int deleteBoard(int bbs_num) {
		int result = 0;
		String sql = "";
		PreparedStatement pstmt = null;
		try {
			sql = "DELETE FROM health1_file WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();

			pstmt.close();

			sql = "DELETE FROM health1 WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int updateBoard(boardDTO dto) {
		int result = 0;
		String sql = "";
		PreparedStatement pstmt = null;

		if (dto.getSavefilename() == null) {
			dto.setSavefilename("파일없음");
		}

		try {
			sql = "DELETE FROM health1_file WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_Num());
			result = pstmt.executeUpdate();

			pstmt.close();

			sql = "INSERT INTO health1_file(file_num,BBS_NUM,savefilename) values(health1_file_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_Num());
			pstmt.setString(2, dto.getSavefilename());

			result = pstmt.executeUpdate();

			pstmt.close();

			sql = "UPDATE health1 SET subject = ?, content = ? WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getBbs_Num());

			result = pstmt.executeUpdate();

			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int updatehitCount(int bbs_num) {
		String sql = "";
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			sql = "UPDATE health1 SET hitcount = hitcount +1 WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시물의 공감 추가
		public int insertLikeBoard(int num, String userId) {
			int result=0;
			PreparedStatement pstmt = null;
			String sql;
			
			sql="INSERT INTO health1_Like(bbs_num, mem_Id) VALUES (?, ?)";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				pstmt.setString(2, userId);
				result = pstmt.executeUpdate();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				if(pstmt!=null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}		
			return result;
		}
		
		// 게시물의 공감 개수
		public int countLikeBoard(int num) {
			int result=0;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			String sql;
			
			try {
				sql="SELECT NVL(COUNT(*), 0) FROM health1_Like WHERE bbs_num=?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				
				rs=pstmt.executeQuery();
				if(rs.next())
					result=rs.getInt(1);
				
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				if(rs!=null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
					
				if(pstmt!=null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}
			}
			
			return result;
		}
		
		public boardDTO bestArticle() {
			boardDTO dto = new boardDTO();
			String sql="";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				sql = "  SELECT h.BBS_Num, H.MEM_ID, M.MEM_NAME, "
						+ " subject,CONTENT, groupNum, orderNo, depth, hitCount, "
						+ " TO_CHAR(created, 'YY/MM/DD') created, NVL(likeCount, 0) likeCount , h1f.SAVEFILENAME "
						+ " FROM health1 H "
						+ " JOIN member M ON H.MEM_ID=M.MEM_ID "
						+ " JOIN HEALTH1_FILE h1f ON H1F.BBS_NUM=H.BBS_NUM "
						+ " LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM health1_Like GROUP BY BBS_NUM) lc ON h.bbs_num = lc.bbs_num "
						+ " ORDER BY likeCount DESC, CREATED DESC";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					dto.setBbs_Num(rs.getInt("Bbs_Num"));
					dto.setMem_Id(rs.getString("Mem_Id"));
					dto.setMem_Name(rs.getString("mem_name"));
					dto.setSubject(rs.getString("subject"));
					dto.setContent(rs.getString("content"));
					dto.setGroupNum(rs.getInt("groupNum"));
					dto.setDepth(rs.getInt("depth"));
					dto.setOrderNo(rs.getInt("orderNo"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setCreated(rs.getString("created"));
					dto.setLikeCount(rs.getInt("likeCount"));
					dto.setSavefilename(rs.getString("savefilename"));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dto;
		}
		
		public List<boardDTO> hitArticle() {
			List<boardDTO> list = new ArrayList<boardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuffer sb = new StringBuffer();
			
			try {
				sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
				sb.append("         SELECT ROW_NUMBER() OVER(ORDER BY HITCOUNT DESC) ranked, BBS_Num, H.MEM_ID, M.MEM_NAME, ");
				sb.append("               subject,CONTENT, groupNum, orderNo, depth, hitCount, ");
				sb.append("                TO_CHAR(created, 'YY/MM/DD') created  ");
				sb.append("               FROM health1 H ");
				sb.append("               JOIN member M ON H.MEM_ID=M.MEM_ID ");
				sb.append("               WHERE ORDERNO=0 ");
				sb.append("               ORDER BY HITCOUNT DESC,groupNum DESC ");
				sb.append("    ) tb WHERE ROWNUM <= 5 ");
				
				pstmt = conn.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();
				while(rs.next()) {
					boardDTO dto = new boardDTO();
					dto.setBbs_Num(rs.getInt("Bbs_Num"));
					dto.setRanked(rs.getInt("ranked"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitcount"));
					list.add(dto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
			
			
		}
		
		// 게시물의 댓글 및 답글 추가
		public int insertReply(ReplyDTO dto) {
			int result=0;
			PreparedStatement pstmt=null;
			StringBuffer sb=new StringBuffer();
			
			try {
				sb.append("INSERT INTO HEALTH1_REPLY(reply_Num, bbs_num, mem_Id, content) ");
				sb.append(" VALUES (HEALTH1_REPLY_seq.NEXTVAL, ?, ?, ?)");
				
				pstmt=conn.prepareStatement(sb.toString());
				pstmt.setInt(1, dto.getBbs_num());
				pstmt.setString(2, dto.getMem_Id());
				pstmt.setString(3, dto.getContent());
				
				result=pstmt.executeUpdate();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				if(pstmt!=null)
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
			}
			
			return result;
		}
		

}
