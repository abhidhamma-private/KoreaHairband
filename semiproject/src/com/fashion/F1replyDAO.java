package com.fashion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class F1replyDAO {
	private Connection conn = DBConn.getConnection();
	// 게시물의 댓글 및 답글 추가
	
		public int insertReply(F1replyDTO dto) {
			int result=0;
			PreparedStatement pstmt=null;
			StringBuffer sb=new StringBuffer();
			
			try {
				sb.append("INSERT INTO fashion1_reply(reply_num, bbs_num, mem_Id, content) ");
				sb.append(" VALUES (fashion1_reply_seq.NEXTVAL, ?, ?, ?)");
				
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

		// 게시물의 댓글 개수
		public int dataCountReply(int bbs_num) {
			int result=0;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			String sql;
			
			try {
				sql="SELECT NVL(COUNT(*), 0) FROM fashion1_reply WHERE bbs_num=? ";
				pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1, bbs_num);
				
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

		// 게시물 댓글 리스트
		public List<F1replyDTO> listReply(int bbs_num, int start, int end) {
			List<F1replyDTO> list=new ArrayList<>();
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			StringBuffer sb=new StringBuffer();
	 		
			try {
			
				sb.append("SELECT * FROM (");
				sb.append(" SELECT ROWNUM rnum, tb.* FROM (");
				sb.append("		SELECT tbreply.reply_num, bbs_num, mem_Id, mem_Name, content, created");
				sb.append("		FROM ( ");
				sb.append("			SELECT reply_num, r.bbs_num, r.mem_Id, mem_Name, r.content, r.created ");
				sb.append("			FROM fashion1_reply r  ");
				sb.append("			JOIN fashion1 f1 ON r.bbs_num = f1.bbs_num  ");
				sb.append("			JOIN member  m ON r.mem_Id = m.mem_Id  ");
				sb.append("			WHERE r.bbs_num= ? ");
				sb.append("		) tbreply ");
				sb.append("		ORDER BY reply_num DESC");
				sb.append("	) tb WHERE ROWNUM <= ? ");
				sb.append(") WHERE rnum >= ? ");
				
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, bbs_num);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);

				rs=pstmt.executeQuery();
				
				while(rs.next()) {
					F1replyDTO dto=new F1replyDTO();
					
					dto.setReply_num(rs.getInt("reply_num"));
					dto.setBbs_num(rs.getInt("bbs_num"));
					dto.setMem_Id(rs.getString("mem_Id"));
					dto.setMem_Name(rs.getString("mem_Name"));
					dto.setContent(rs.getString("content"));
					dto.setCreated(rs.getString("created"));
					
					
					list.add(dto);
				}
				
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
			return list;
		}

/*		// 게시물의 댓글 삭제
		public int deleteReply(int reply_num, String mem_Id) {
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			sql="DELETE FROM fashion1_reply ";
			sql+="  WHERE reply_num IN  ";
			sql+="  (SELECT reply_num FROM fashion1_reply START WITH reply_num = ?";
			if(! mem_Id.equals("admin"))
				sql+=" AND mem_Id = ?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, reply_num);
				if(! mem_Id.equals("admin"))
					pstmt.setString(2, mem_Id);
				
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
*/
	
}
