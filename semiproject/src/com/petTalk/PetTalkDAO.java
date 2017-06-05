package com.petTalk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;


public class PetTalkDAO {
	private Connection conn = DBConn.getConnection();

	// 데이터 삽입
	public int insertBoard(PetTalkDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("INSERT INTO pet2(bbs_num, notice, category, mem_id, subject, content, hitCount) ");
			sb.append(" VALUES(pet2_seq.NEXTVAL,?,?,?,?,?,?)");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getCategory());
			pstmt.setString(3, dto.getMem_id());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, dto.getHitCount());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	// 데이터 개수 파악
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*),0) FROM pet2";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
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

		return result;
	}

	// 검색상태에서 데이터 개수 파악
	public int dataCount(String searchKey, String searchValue) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*),0) FROM pet2 p JOIN member m ON p.mem_id = m.mem_id";
			if (searchKey.equals("mem_name")) {
				sql += " WHERE INSTR(mem_name, ?) = 1";
			} else if (searchKey.equals("created")) {
				searchValue = searchValue.replaceAll("-||/", "");
				sql += " WHERE TO_CHAR(created, 'YYYY-MM-DD') = ?";
			} else {
				sql += " WHERE INSTR(" + searchKey + ", ?) >= 1";
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
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

		return result;
	}

	// 게시물 리스트 출력
	List<PetTalkDTO> listpetTalk(int start, int end) {
		List<PetTalkDTO> list = new ArrayList<PetTalkDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PetTalkDAO dao = new PetTalkDAO();
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("SELECT * FROM (");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("		SELECT bbs_num, p.mem_id, mem_name, category, subject, hitCount,	");
			sb.append("				created ");
			sb.append("		FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
			sb.append("		ORDER BY bbs_num DESC ");
			sb.append("	)tb WHERE ROWNUM <= ? ");
			sb.append(")WHERE rnum >= ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				PetTalkDTO dto = new PetTalkDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setLike(dao.countLike(dto.getBbs_num()));
				dto.setReply(dao.dataCountReply(dto.getBbs_num()));
				list.add(dto);
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
		return list;
	}

	// 검색시에 게시물 리스트 출력
	public List<PetTalkDTO> listpetTalk(int start, int end, String searchKey, String searchValue) {
		List<PetTalkDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		PetTalkDAO dao = new PetTalkDAO();

		try {
			sb.append("SELECT * FROM (");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("		SELECT bbs_num, p.mem_id, mem_name, category, subject, hitCount,	");
			sb.append("				created ");
			sb.append("		FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
			if (searchKey.equals("mem_name")) {
				sb.append(" WHERE INSTR(mem_name, ?) = 1");
			} else if (searchKey.equals("created")) {
				searchValue = searchValue.replaceAll("-", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + searchKey + ", ?) >= 1");
			}
			sb.append("		ORDER BY bbs_num DESC ");
			sb.append("	)tb WHERE ROWNUM <= ? ");
			sb.append(")WHERE rnum >= ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				PetTalkDTO dto = new PetTalkDTO();

				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setLike(dao.countLike(dto.getBbs_num()));
				dto.setReply(dao.dataCountReply(dto.getBbs_num()));

				list.add(dto);
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
		return list;
	}

	// 조회수 증가시키기
	public int updateHitCount(int bbs_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE pet2 SET hitCount = hitCount+1 WHERE bbs_num=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;
	}

	// 해당 게시물 보기
	public PetTalkDTO readBoard(int bbs_num) {
		PetTalkDTO dto = null;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		try {
			sb.append("SELECT bbs_num, p.mem_id, mem_name, content, category, subject, hitCount,	");
			sb.append("		created ");
			sb.append(" FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
			sb.append(" WHERE bbs_num=?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new PetTalkDTO();

				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCategory(rs.getString("category"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}

	// 이전글
	public PetTalkDTO preReadBoard(int bbs_num, String searchKey, String searchValue) {
		PetTalkDTO dto = null;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		try {
			if (searchValue != null && searchValue.length() != 0) {
				sb.append("	SELECT ROWNUM, tb.* FROM( ");
				sb.append("		SELECT bbs_num, subject, category ");
				sb.append(" 	FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
				if (searchKey.equals("mem_name")) {
					sb.append("		WHERE (INSTR(mem_name, ?)=1 ) ");
				} else if (searchKey.equals("created")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append(" 	WHERE TO_CHAR(created, 'YYYYMMDD') = ?");
				} else {
					sb.append(" 	WHERE INSTR(" + searchKey + ", ?) >= 1");
				}
				sb.append("			AND (bbs_num > ?) ");
				sb.append("		ORDER BY bbs_num ASC");
				sb.append("	)tb WHERE ROWNUM = 1");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, bbs_num);
			} else {
				sb.append("	SELECT ROWNUM, tb.* FROM( ");
				sb.append("		SELECT bbs_num, subject, category ");
				sb.append(" 	FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
				sb.append("		WHERE bbs_num > ?");
				sb.append("		ORDER BY bbs_num ASC");
				sb.append("	)tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, bbs_num);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new PetTalkDTO();

				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
				dto.setCategory(rs.getString("category"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}

	// 다음글
	public PetTalkDTO nextReadBoard(int bbs_num, String searchValue, String searchKey) {
		PetTalkDTO dto = null;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		try {
			if (searchValue != null && searchValue.length() != 0) {
				sb.append("	SELECT ROWNUM, tb.* FROM( ");
				sb.append("		SELECT bbs_num, subject, category ");
				sb.append(" 	FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
				if (searchKey.equals("mem_name")) {
					sb.append("		WHERE (INSTR(mem_name, ?)=1 ) ");
				} else if (searchKey.equals("created")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append(" 	WHERE TO_CHAR(created, 'YYYYMMDD') = ?");
				} else {
					sb.append(" 	WHERE INSTR(" + searchKey + ", ?) >= 1");
				}
				sb.append("			AND (bbs_num < ?) ");
				sb.append("		ORDER BY bbs_num DESC");
				sb.append("	)tb WHERE ROWNUM = 1");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, bbs_num);
			} else {
				sb.append("	SELECT ROWNUM, tb.* FROM( ");
				sb.append("		SELECT bbs_num, subject, category ");
				sb.append(" 	FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
				sb.append("		WHERE bbs_num < ?");
				sb.append("		ORDER BY bbs_num DESC");
				sb.append("	)tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, bbs_num);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new PetTalkDTO();

				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
				dto.setCategory(rs.getString("category"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}

	// 게시글 수정
	public int updateBoard(PetTalkDTO dto, String mem_id) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE pet2 SET subject=?, content=?, category=? WHERE bbs_num=? AND mem_id =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getCategory());
			pstmt.setInt(4, dto.getBbs_num());
			pstmt.setString(5, mem_id);

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	// 게시글 삭제
	public int deleteBoard(int bbs_num, String mem_id) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (mem_id.equals("admin")) {
				sql = "DELETE FROM pet2 WHERE bbs_num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bbs_num);
				result = pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM pet2 WHERE bbs_num=? AND mem_id=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bbs_num);
				pstmt.setString(2, mem_id);
				result = pstmt.executeUpdate();
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
		}

		return result;
	}

	// 공지사항에 체크된것만 리스트 출력

	public List<PetTalkDTO> listpetTalkNotice() {
		List<PetTalkDTO> listNotice = new ArrayList<PetTalkDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PetTalkDAO dao = new PetTalkDAO();
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("		SELECT bbs_num, p.mem_id, mem_name, category, subject, hitCount,	");
			sb.append("				TO_CHAR(created, 'YYYY-MM-DD')created ");
			sb.append("		FROM pet2 p JOIN member m ON p.mem_id = m.mem_id ");
			sb.append("		WHERE notice=1 ");
			sb.append("		ORDER BY bbs_num DESC ");

			pstmt = conn.prepareStatement(sb.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				PetTalkDTO dto = new PetTalkDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
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

	public int countLike(int bbs_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM pet2_like WHERE bbs_num=?";
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

		sql = "INSERT INTO pet2_like(bbs_num, mem_Id) VALUES (?, ?)";
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

	// 게시물의 댓글 및 답글 추가
	public int insertReply(PetReplyDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("INSERT INTO pet2_reply(reply_num, bbs_num, mem_Id, content) ");
			sb.append(" VALUES (pet2_reply_seq.NEXTVAL, ?, ?, ?)");

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
			sql = "SELECT NVL(COUNT(*), 0) FROM pet2_reply WHERE bbs_num=? ";
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
	public List<PetReplyDTO> listReply(int bbs_num, int start, int end) {
		List<PetReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {

			sb.append("SELECT * FROM (");
			sb.append(" SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("		SELECT tbreply.reply_num, bbs_num, mem_Id, mem_Name, content, created");
			sb.append("		FROM ( ");
			sb.append("			SELECT reply_num, r.bbs_num, r.mem_Id, mem_Name, r.content, r.created ");
			sb.append("			FROM pet2_reply r  ");
			sb.append("			JOIN pet2 b ON r.bbs_num = b.bbs_num  ");
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
				PetReplyDTO dto = new PetReplyDTO();

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

		sql = "DELETE FROM pet2_reply ";
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
