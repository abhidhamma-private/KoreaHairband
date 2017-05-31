package com.it;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;


public class BoardDAO {
	private Connection conn=DBConn.getConnection();
	public int insertBoard(BoardDTO dto, String mode) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		int seq;
		
		try {
			sql="SELECT it2_seq.NEXTVAL FROM dual";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			seq=0;
			if(rs.next())
				seq=rs.getInt(1);
			rs.close();
			pstmt.close();
			
			dto.setBbs_num(seq);
			if(mode.equals("created")) {
				// 글쓰기일때
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if(mode.equals("reply")) {
				// 답변일때
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
				dto.setCategory("답변");
			}
			
			sql = "INSERT INTO it2(";
			sql += " bbs_num, category, mem_Id, subject, content, hitCount, ";
			sql += " groupNum, depth, orderNo, parent ) ";
			sql += "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setString(2, dto.getCategory());
			pstmt.setString(3, dto.getMem_Id());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, dto.getHitCount());
			pstmt.setInt(7, dto.getGroupNum());
			pstmt.setInt(8, dto.getDepth());
			pstmt.setInt(9, dto.getOrderNo());
			pstmt.setInt(10, dto.getParent());
			
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 답변일 경우 orderNo 변경
	public int updateOrderNo(int groupNum, int orderNo) {
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		sql = "UPDATE it2 SET orderNo=orderNo+1 WHERE groupNum = ? AND orderNo > ?";
		
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
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM it2";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next())
				result=rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 검색 에서 전체의 개수
    public int dataCount(String searchKey, String searchValue) {
        int result=0;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	if(searchKey.equals("created")) {
        		searchValue=searchValue.replaceAll("-", "");
        		sql="SELECT NVL(COUNT(*), 0) FROM it2 b JOIN member m ON b.mem_Id=m.mem_Id WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ";
        	} else if(searchKey.equals("mem_Name")) {
        		sql="SELECT NVL(COUNT(*), 0) FROM it2 b JOIN member m ON b.mem_Id=m.mem_Id WHERE INSTR(mem_Name, ?) = 1 ";
        	} else {
        		sql="SELECT NVL(COUNT(*), 0) FROM it2 b JOIN member m ON b.mem_Id=m.mem_Id WHERE INSTR(" + searchKey + ", ?) >= 1 ";
        	}
        	
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1, searchValue);

            rs=pstmt.executeQuery();

            if(rs.next())
                result=rs.getInt(1);
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return result;
    }
	
	public List<BoardDTO> listBoard(int start, int end) {
		List<BoardDTO> list=new ArrayList<BoardDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT * FROM ( ");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT bbs_num, b.mem_Id, mem_Name, category, ");
			sb.append("               subject, content, groupNum, orderNo, depth, hitCount, ");
			sb.append("               TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append("               FROM it2 b ");
			sb.append("               JOIN member m ON b.mem_Id=m.mem_Id ");
			sb.append("               ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				BoardDTO dto=new BoardDTO();
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
    public List<BoardDTO> listBoard(int start, int end, String searchKey, String searchValue) {
        List<BoardDTO> list=new ArrayList<BoardDTO>();

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("         SELECT bbs_num, b.mem_Id, mem_Name, category, ");
			sb.append("               subject, content, groupNum, orderNo, depth, hitCount,");
			sb.append("               TO_CHAR(created, 'YYYY-MM-DD') created");
			sb.append("               FROM it2 b");
			sb.append("               JOIN member m ON b.mem_Id=m.mem_Id");
			if(searchKey.equals("created")) {
				searchValue=searchValue.replaceAll("-", "");
				sb.append("           WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			} else if(searchKey.equals("mem_Name")) {
				sb.append("           WHERE INSTR(mem_Name, ?) = 1 ");
			} else {
				sb.append("           WHERE INSTR(" + searchKey + ", ?) >= 1 ");
			}
			
			sb.append("               ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ?");
			sb.append(") WHERE rnum >= ?");
            
			pstmt=conn.prepareStatement(sb.toString());
            
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
            
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
                BoardDTO dto=new BoardDTO();
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
                list.add(dto);
            }
            rs.close();
            pstmt.close();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }
	
	public BoardDTO readBoard(int bbs_num) {
		BoardDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT bbs_num, b.mem_Id, mem_Name, category, subject, ");
			sb.append("    content, created, hitCount, groupNum, depth, orderNo, parent ");
			sb.append("    FROM it2 b ");
			sb.append("    JOIN member m ON b.mem_Id=m.mem_Id ");
			sb.append("    WHERE bbs_num=? ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new BoardDTO();
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
				
			}
			rs.close();
			pstmt.close();
					
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;
	}
	
    // 이전글
    public BoardDTO preReadBoard(int groupNum, int orderNo, String searchKey, String searchValue) {
        BoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue!=null && searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("  SELECT bbs_num, subject  ");
    			sb.append("               FROM it2 b");
    			sb.append("               JOIN member m ON b.mem_Id=m.mem_Id");
    			if(searchKey.equals("created")) {
    				searchValue=searchValue.replaceAll("-", "");
    				sb.append("           WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND ");
    			} else if(searchKey.equals("mem_Name")) {
    				sb.append("           WHERE (INSTR(mem_Name, ?) = 1 ) AND ");
    			} else {
    				sb.append("           WHERE (INSTR(" + searchKey + ", ?) >= 1 ) AND ");
            	}
            
                sb.append("     (( groupNum = ? AND orderNo < ?) ");
                sb.append("         OR (groupNum > ? )) ");
                sb.append("         ORDER BY groupNum ASC, orderNo DESC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, groupNum);
                pstmt.setInt(3, orderNo);
                pstmt.setInt(4, groupNum);
			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT bbs_num, subject FROM it2 b JOIN member m ON b.mem_Id=m.mem_Id ");                
                sb.append("  WHERE (groupNum = ? AND orderNo < ?) ");
                sb.append("         OR (groupNum > ? ) ");
                sb.append("         ORDER BY groupNum ASC, orderNo DESC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new BoardDTO();
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
    public BoardDTO nextReadBoard(int groupNum, int orderNo, String searchKey, String searchValue) {
        BoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue!=null && searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("  SELECT bbs_num, subject ");
    			sb.append("               FROM it2 b");
    			sb.append("               JOIN member m ON b.mem_Id=m.mem_Id");
    			if(searchKey.equals("created")) {
    				searchValue=searchValue.replaceAll("-", "");
    				sb.append("           WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND ");
    			} else if(searchKey.equals("mem_Id")) {
    				sb.append("           WHERE (INSTR(mem_Name, ?) = 1) AND ");
    			} else {
    				sb.append("           WHERE (INSTR(" + searchKey + ", ?) >= 1) AND ");
    			}
    			
                sb.append("     (( groupNum = ? AND orderNo > ?) ");
                sb.append("         OR (groupNum < ? )) ");
                sb.append("         ORDER BY groupNum DESC, orderNo ASC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, groupNum);
                pstmt.setInt(3, orderNo);
                pstmt.setInt(4, groupNum);

			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT bbs_num, subject FROM it2 b JOIN member m ON b.mem_Id=m.mem_Id ");
                sb.append("  WHERE (groupNum = ? AND orderNo > ?) ");
                sb.append("         OR (groupNum < ? ) ");
                sb.append("         ORDER BY groupNum DESC, orderNo ASC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new BoardDTO();
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
		
		sql = "UPDATE it2 SET hitCount=hitCount+1 WHERE bbs_num=?";
		
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
	
	public int countLike(int bbs_num){
 		int result = 10;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM it2_like WHERE bbs_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				result=rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
		
	}
	public int updateLike(int bbs_num, String mem_Id) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " SELECT NVL(COUNT(*), 0) FROM it2 b JOIN it2_like l ON b.bbs_num = l.bbs_num WHERE b.bbs_num = ? AND l.mem_id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			pstmt.setString(2, mem_Id);
			result = pstmt.executeUpdate();
			
			if(result == 1){
				return result;
			}
			
			pstmt.close();
			sql="";
			
			sql=" INSERT INTO it2_like VALUES (?, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			pstmt.setString(2, mem_Id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	public int updateBoard(BoardDTO dto, String mem_Id) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		sql="UPDATE it2 SET subject=?, content=? ";
		sql+= " WHERE bbs_num=? AND mem_Id=?";
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
	
	public int deleteBoard(int bbs_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="DELETE FROM it2 WHERE bbs_num IN (SELECT bbs_num FROM it2 START WITH  bbs_num = ? CONNECT BY PRIOR bbs_num = parent)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
}
