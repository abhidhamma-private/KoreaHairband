package com.food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.util.DBConn;

public class RecipeDAO {
	private Connection conn = DBConn.getConnection();

	public int insertrecipe(RecipeDTO dto, String mode) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;

		try {
			sql = "SELECT food1_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			seq = 0;
			if (rs.next())
				seq = rs.getInt(1);
			rs.close();
			pstmt.close();

			dto.setBbs_num(seq);
			if (mode.equals("created")) {
				// 글쓰기일때
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if (mode.equals("reply")) {
				// 답변일때
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
				dto.setCategory("답변");
			}

			sql = "INSERT INTO food1(";
			sql += " bbs_num, notice, category, mem_Id, subject, content, hitCount, ";
			sql += " groupNum, depth, orderNo, parent ) ";
			sql += "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setInt(2, dto.getNotice());
			pstmt.setString(3, dto.getCategory());
			pstmt.setString(4, dto.getMem_Id());
			pstmt.setString(5, dto.getSubject());
			pstmt.setString(6, dto.getContent());
			pstmt.setInt(7, dto.getHitCount());
			pstmt.setInt(8, dto.getGroupNum());
			pstmt.setInt(9, dto.getDepth());
			pstmt.setInt(10, dto.getOrderNo());
			pstmt.setInt(11, dto.getParent());

			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	// 답변일 경우 orderNo 변경
	public int updateOrderNo(int groupNum, int orderNo) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		sql = "UPDATE food1 SET orderNo=orderNo+1 WHERE groupNum = ? AND orderNo > ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM food1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	// 검색 에서 전체의 개수
	public int dataCount(String searchKey, String searchValue) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			if (searchKey.equals("created")) {
				searchValue = searchValue.replaceAll("-", "");
				sql = "SELECT NVL(COUNT(*), 0) FROM food1 b JOIN member m ON b.mem_Id=m.mem_Id WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ";
			} else if (searchKey.equals("mem_Name")) {
				sql = "SELECT NVL(COUNT(*), 0) FROM food1 b JOIN member m ON b.mem_Id=m.mem_Id WHERE INSTR(mem_Name, ?) = 1 ";
			} else {
				sql = "SELECT NVL(COUNT(*), 0) FROM food1 b JOIN member m ON b.mem_Id=m.mem_Id WHERE INSTR(" + searchKey
						+ ", ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);

			rs = pstmt.executeQuery();

			if (rs.next())
				result = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	// 공지사항에 체크된것만 리스트 출력
	public List<RecipeDTO> listNotice() {
		List<RecipeDTO> listNotice = new ArrayList<RecipeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		RecipeDAO dao = new RecipeDAO();
		
		try {
			sb.append("		SELECT bbs_num, b.mem_Id, mem_Name, category, subject, content, hitCount,	");
			sb.append("				TO_CHAR(created, 'YYYY-MM-DD')created ");
			sb.append("		FROM food1 b JOIN member m ON b.mem_id = m.mem_id ");
			sb.append("		WHERE notice=1 ");
			sb.append("		ORDER BY bbs_num DESC ");

			pstmt = conn.prepareStatement(sb.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				RecipeDTO dto = new RecipeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setLike(dao.countLike(dto.getBbs_num()));
				dto.setReply(dao.dataCountReply(dto.getBbs_num()));

				listNotice.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return listNotice;
	}

	public List<RecipeDTO> listrecipe(int start, int end) {
		List<RecipeDTO> list = new ArrayList<RecipeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		RecipeDAO dao = new RecipeDAO();

		try {
			sb.append("SELECT * FROM ( ");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT bbs_num, b.mem_Id, mem_Name, category, ");
			sb.append("               subject, content, groupNum, orderNo, depth, hitCount, created ");
			sb.append("               FROM food1 b ");
			sb.append("               JOIN member m ON b.mem_Id=m.mem_Id ");
			sb.append("               ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				RecipeDTO dto = new RecipeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setLike(dao.countLike(dto.getBbs_num()));
				dto.setReply(dao.dataCountReply(dto.getBbs_num()));
				list.add(dto);
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return list;
	}

	// 검색에서 리스트
	public List<RecipeDTO> listrecipe(int start, int end, String searchKey, String searchValue) {
		List<RecipeDTO> list = new ArrayList<RecipeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		RecipeDAO dao = new RecipeDAO();

		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("         SELECT bbs_num, b.mem_Id, mem_Name, category, ");
			sb.append("               subject, content, groupNum, orderNo, depth, hitCount, created ");
			sb.append("               FROM food1 b");
			sb.append("               JOIN member m ON b.mem_Id=m.mem_Id");
			if (searchKey.equals("created")) {
				searchValue = searchValue.replaceAll("-", "");
				sb.append("           WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			} else if (searchKey.equals("mem_Name")) {
				sb.append("           WHERE INSTR(mem_Name, ?) = 1 ");
			} else {
				sb.append("           WHERE INSTR(" + searchKey + ", ?) >= 1 ");
			}

			sb.append("               ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ?");
			sb.append(") WHERE rnum >= ?");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				RecipeDTO dto = new RecipeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setLike(dao.countLike(dto.getBbs_num()));
				dto.setReply(dao.dataCountReply(dto.getBbs_num()));
				list.add(dto);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return list;
	}

	public RecipeDTO readrecipe(int bbs_num) {
		RecipeDTO dto = null;
		RecipeDAO dao = new RecipeDAO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT bbs_num, b.mem_Id, mem_Name, category, subject, ");
			sb.append("    content, created, hitCount, groupNum, depth, orderNo, parent ");
			sb.append("    FROM food1 b ");
			sb.append("    JOIN member m ON b.mem_Id=m.mem_Id ");
			sb.append("    WHERE bbs_num=? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new RecipeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getInt("parent"));
				dto.setLike(dao.countLike(dto.getBbs_num()));
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return dto;
	}

	// 이전글
	public RecipeDTO preReadrecipe(int groupNum, int orderNo, String searchKey, String searchValue) {
		RecipeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			if (searchValue != null && searchValue.length() != 0) {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("  SELECT bbs_num, subject  ");
				sb.append("               FROM food1 b");
				sb.append("               JOIN member m ON b.mem_Id=m.mem_Id");
				if (searchKey.equals("created")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append("           WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND ");
				} else if (searchKey.equals("mem_Name")) {
					sb.append("           WHERE (INSTR(mem_Name, ?) = 1 ) AND ");
				} else {
					sb.append("           WHERE (INSTR(" + searchKey + ", ?) >= 1 ) AND ");
				}

				sb.append("     (( groupNum = ? AND orderNo < ?) ");
				sb.append("         OR (groupNum > ? )) ");
				sb.append("         ORDER BY groupNum ASC, orderNo DESC) tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setString(1, searchValue);
				pstmt.setInt(2, groupNum);
				pstmt.setInt(3, orderNo);
				pstmt.setInt(4, groupNum);
			} else {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("     SELECT bbs_num, subject FROM food1 b JOIN member m ON b.mem_Id=m.mem_Id ");
				sb.append("  WHERE (groupNum = ? AND orderNo < ?) ");
				sb.append("         OR (groupNum > ? ) ");
				sb.append("         ORDER BY groupNum ASC, orderNo DESC) tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new RecipeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return dto;
	}

	// 다음글
	public RecipeDTO nextReadrecipe(int groupNum, int orderNo, String searchKey, String searchValue) {
		RecipeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			if (searchValue != null && searchValue.length() != 0) {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("  SELECT bbs_num, subject ");
				sb.append("               FROM food1 b");
				sb.append("               JOIN member m ON b.mem_Id=m.mem_Id");
				if (searchKey.equals("created")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append("           WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND ");
				} else if (searchKey.equals("mem_Id")) {
					sb.append("           WHERE (INSTR(mem_Name, ?) = 1) AND ");
				} else {
					sb.append("           WHERE (INSTR(" + searchKey + ", ?) >= 1) AND ");
				}

				sb.append("     (( groupNum = ? AND orderNo > ?) ");
				sb.append("         OR (groupNum < ? )) ");
				sb.append("         ORDER BY groupNum DESC, orderNo ASC) tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, groupNum);
				pstmt.setInt(3, orderNo);
				pstmt.setInt(4, groupNum);

			} else {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("     SELECT bbs_num, subject FROM food1 b JOIN member m ON b.mem_Id=m.mem_Id ");
				sb.append("  WHERE (groupNum = ? AND orderNo > ?) ");
				sb.append("         OR (groupNum < ? ) ");
				sb.append("         ORDER BY groupNum DESC, orderNo ASC) tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new RecipeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return dto;
	}

	public int updateHitCount(int bbs_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		sql = "UPDATE food1 SET hitCount=hitCount+1 WHERE bbs_num=?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	public int countLike(int bbs_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM food1_like WHERE bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);

			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int updateLike(int bbs_num, String mem_Id) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		sql = "INSERT INTO food1_like(bbs_num, mem_Id) VALUES (?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			pstmt.setString(2, mem_Id);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int updaterecipe(RecipeDTO dto, String mem_Id) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		sql = "UPDATE food1 SET subject=?, content=? ";
		sql += " WHERE bbs_num=? AND mem_Id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getBbs_num());
			pstmt.setString(4, mem_Id);
			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	public int deleterecipe(int bbs_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			// 좋아요 삭제
			sql = "DELETE FROM food1_like WHERE bbs_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();
			pstmt.close();

			// 리플 삭제
			sql = "DELETE FROM food1_reply WHERE bbs_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();
			pstmt.close();

			// 게시글 삭제
			sql = "DELETE FROM food1 WHERE bbs_num IN (SELECT bbs_num FROM food1 START WITH  bbs_num = ? CONNECT BY PRIOR bbs_num = parent)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	// 게시물의 댓글 및 답글 추가
	public int insertReply(RecipeReplyDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("INSERT INTO food1_reply(reply_num, bbs_num, mem_Id, content) ");
			sb.append(" VALUES (food1_reply_seq.NEXTVAL, ?, ?, ?)");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setString(2, dto.getMem_Id());
			pstmt.setString(3, dto.getContent());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
		}

		return result;
	}

	// 게시물의 댓글 개수
	public int dataCountReply(int bbs_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM food1_reply WHERE bbs_num=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);

			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return result;
	}

	// 게시물 댓글 리스트
	public List<RecipeReplyDTO> listReply(int bbs_num, int start, int end) {
		List<RecipeReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {

			sb.append("SELECT * FROM (");
			sb.append(" SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("		SELECT tbreply.reply_num, bbs_num, mem_Id, mem_Name, content, created");
			sb.append("		FROM ( ");
			sb.append("			SELECT reply_num, r.bbs_num, r.mem_Id, mem_Name, r.content, r.created ");
			sb.append("			FROM food1_reply r  ");
			sb.append("			JOIN food1 b ON r.bbs_num = b.bbs_num  ");
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

			rs = pstmt.executeQuery();

			while (rs.next()) {
				RecipeReplyDTO dto = new RecipeReplyDTO();

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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return list;
	}

	// 게시물의 댓글 삭제
	public int deleteReply(int reply_num, String mem_Id) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		sql = "DELETE FROM food1_reply ";
		sql += "  WHERE reply_num = ? ";
		if (!mem_Id.equals("admin"))
			sql += " AND mem_Id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reply_num);
			if (!mem_Id.equals("admin"))
				pstmt.setString(2, mem_Id);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

}
