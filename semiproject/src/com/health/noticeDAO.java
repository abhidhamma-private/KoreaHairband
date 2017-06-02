package com.health;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class noticeDAO {
	private Connection conn = DBConn.getConnection();

	public int insertNotice(noticeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "";

		try {
			sql = " INSERT ALL " + " INTO health2(bbs_num, mem_id, subject, content) "
					+ " values(health2_seq.nextval, ?, ?, ?) " + " INTO health2_file(file_num, bbs_num, savefilename) "
					+ " VALUES(health2_file_seq.nextval, health2_seq.currval, ?) " + " SELECT * FROM dual ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getMem_id());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getSavefilename());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public int datacount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";

		try {
			sql = "SELECT COUNT(*) FROM health2";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<noticeDTO> listNotice(int start, int end) {
		List<noticeDTO> list = new ArrayList<>();
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			sql = "		SELECT * FROM (" + " 	SELECT ROWNUM rnum, tb.* FROM("
					+ " 		SELECT H2.mem_id, F2.bbs_num, savefilename, subject, content "
					+ " 		,TO_CHAR(created, 'yy/mm/dd hh24:mi:ss') created, hitcount, NVL(likeCount, 0) likeCount "
					+ " 		FROM health2 H2 JOIN health2_file F2 ON H2.BBS_NUM = F2.BBS_NUM "
					+"          LEFT OUTER JOIN (SELECT NVL(COUNT(*), 0) likeCount, BBS_NUM FROM health2_Like GROUP BY BBS_NUM) lc ON h2.bbs_num = lc.bbs_num "
					+ " 		ORDER BY bbs_num DESC " + "		) tb WHERE ROWNUM<= ?" + ") WHERE rnum >=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				noticeDTO dto = new noticeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setSavefilename(rs.getString("savefilename"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setHitcount(rs.getInt("hitcount"));
				dto.setLikeCount(rs.getShort("likeCount"));
				

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public noticeDTO readnotice(int bbs_num) {
		noticeDTO dto = new noticeDTO();
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			sql = "SELECT * FROM health2 H2 JOIN health2_file F2 ON " + " H2.bbs_num = F2.bbs_num where H2.bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setSavefilename(rs.getString("savefilename"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setHitcount(rs.getInt("hitcount"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	public int deleteNotice(int bbs_num) {
		int result = 0;
		String sql = "";
		PreparedStatement pstmt = null;
		try {
			sql = "DELETE FROM health2_file WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();

			pstmt.close();

			sql = "DELETE FROM health2 WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int updateNotice(noticeDTO dto) {
		int result = 0;
		String sql = "";
		PreparedStatement pstmt = null;

		if (dto.getSavefilename() == null) {
			dto.setSavefilename("파일없음");
		}

		try {
			sql = "DELETE FROM health2_file WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_num());
			result = pstmt.executeUpdate();
			pstmt.close();

			sql = "INSERT INTO health2_file(file_num,BBS_NUM,savefilename) values(health2_file_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setString(2, dto.getSavefilename());

			result = pstmt.executeUpdate();
			pstmt.close();

			sql = "UPDATE health2 SET subject = ?, content = ? WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getBbs_num());

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
			sql = "UPDATE health2 SET hitcount = hitcount +1 WHERE bbs_num=?";
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
				
				sql="INSERT INTO health2_Like(bbs_num, mem_Id) VALUES (?, ?)";
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
					sql="SELECT NVL(COUNT(*), 0) FROM health2_Like WHERE bbs_num=?";
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
	
	

}
