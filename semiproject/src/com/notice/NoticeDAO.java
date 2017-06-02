package com.notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class NoticeDAO {
	private Connection conn = DBConn.getConnection();
	
	//공지사항 작성
	public int insertNotice(NoticeDTO dto){
		int result=0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("INSERT INTO notice( ");
			sb.append("	not_num, notice, mem_id, subject, content, hitcount) ");
			sb.append("	VALUES(notice_seq.NEXTVAL, ?, ?, ?, ?, ?)");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getMem_id());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getHitCount());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
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
	
	//공지사항 갯수 세기
	public int dataCount(){
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM notice";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				result=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null){
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
		return result;
	}
	
	//검색시 전체 개수
	public int dataCount(String searchKey, String searchValue){
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(searchKey.equalsIgnoreCase("created")){
				searchValue=searchValue.replaceAll("-", "");
				sql="SELECT NVL(COUNT(*),0) FROM notice n JOIN member m ON n.mem_id=m.mem_id WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
			}else{
				sql="SELECT NVL(COUNT(*),0) FROM notice n JOIN member m ON n.mem_id=m.mem_id WHERE INSTR(" +searchKey+",?) >=1 ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				result=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null){
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
		return result;
	}
	
	public List<NoticeDTO> listNotice(int start, int end){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM( ");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("		SELECT not_num, n.mem_id, mem_name ");
			sb.append("				,subject, hitCount, created ");
			sb.append("		FROM notice n JOIN member m ON n.mem_Id = m.mem_id ");
			sb.append("		ORDER BY not_num DESC ");
			sb.append("	)tb WHERE ROWNUM <= ? ");
			sb.append(")WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				NoticeDTO dto = new NoticeDTO();
				
				dto.setNot_num(rs.getInt("not_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setSubject(rs.getString("subject"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null){
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
		return list;
	}

	//검색에서 리스트
	public List<NoticeDTO> listNotice(String searchKey, String searchValue, int start, int end){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM( ");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("		SELECT not_num, n.mem_id, mem_name ");
			sb.append("				,subject, hitCount, created ");
			sb.append("		FROM notice n JOIN member m ON n.mem_Id = m.mem_id ");
			if(searchKey.equalsIgnoreCase("created")) {
				searchValue=searchValue.replaceAll("-", "");
				sb.append("           WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			} else {
				sb.append("           WHERE INSTR(" + searchKey + ", ?) >= 1 ");
			}
			sb.append("		ORDER BY not_num DESC ");
			sb.append("	)tb WHERE ROWNUM <= ? ");
			sb.append(")WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				NoticeDTO dto = new NoticeDTO();
				
				dto.setNot_num(rs.getInt("not_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setSubject(rs.getString("subject"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null){
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
		return list;
	}
	
	//공지글 리스트
	public List<NoticeDTO> listNotice() {
		List<NoticeDTO> list=new ArrayList<NoticeDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("   SELECT not_num, n.mem_id, mem_name");
			sb.append("            ,subject , hitCount ");
			sb.append("            ,created ");
			sb.append("         FROM notice n JOIN member m ON n.mem_id=m.mem_id");
			sb.append("         WHERE notice=1 ");
			sb.append("	      ORDER BY not_num DESC");

			pstmt=conn.prepareStatement(sb.toString());
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto=new NoticeDTO();
				
				dto.setNot_num(rs.getInt("not_num"));;
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null){
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
		return list;
	}
	
	//게시글 읽기
	public NoticeDTO readNotice(int not_num) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		sql = "SELECT not_num, notice, n.mem_id, mem_name, subject, content, hitCount, created ";
		sql+= "  FROM notice n JOIN member m ON n.mem_id=m.mem_id WHERE not_num = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, not_num);
			
			rs = pstmt.executeQuery();
			
			if( rs.next()) {
				dto = new NoticeDTO();
				dto.setNot_num(rs.getInt("not_num"));
				dto.setNotice(rs.getInt("notice"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
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
		return dto;
	}
	
    // 이전글
    public NoticeDTO preReadNotice(int not_num, String searchKey, String searchValue) {
    	NoticeDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT not_num, subject FROM notice n JOIN member m ON n.mem_id=m.mem_id ");
                if(searchKey.equalsIgnoreCase("created")) {
                	searchValue=searchValue.replaceAll("-", "");
                	sb.append("     WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                } else {
                	sb.append("     WHERE (INSTR(" + searchKey + ", ?) >= 1)  ");
                }
                sb.append("         AND (not_num > ? ) ");
                sb.append("         ORDER BY not_num ASC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, not_num);
			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT not_num, subject FROM notice n JOIN member m ON n.mem_id=m.mem_id ");                
                sb.append("     WHERE not_num > ? ");
                sb.append("         ORDER BY not_num ASC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, not_num);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new NoticeDTO();
                dto.setNot_num(rs.getInt("not_num"));
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
    public NoticeDTO nextReadNotice(int not_num, String searchKey, String searchValue) {
    	NoticeDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT not_num, subject FROM notice n JOIN member m ON n.mem_id=m.mem_id ");
                if(searchKey.equalsIgnoreCase("created")) {
                	searchValue=searchValue.replaceAll("-", "");
                	sb.append("     WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                } else {
                	sb.append("     WHERE (INSTR(" + searchKey + ", ?) >= 1)  ");
                }
                sb.append("         AND (not_num < ? ) ");
                sb.append("         ORDER BY not_num DESC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, not_num);
			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT not_num, subject FROM notice n JOIN member m ON n.mem_id=m.mem_id ");
                sb.append("     WHERE not_num < ? ");
                sb.append("         ORDER BY not_num DESC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, not_num);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new NoticeDTO();
                dto.setNot_num(rs.getInt("not_num"));
                dto.setSubject(rs.getString("subject"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    
        return dto;
    }

	public int updateHitCount(int not_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE notice SET hitCount=hitCount+1 WHERE not_num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, not_num);
			result = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	public int updateNotice(NoticeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE notice SET notice=?, subject=?, content=? ";
			sql+= " WHERE not_num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(7, dto.getNot_num());
			result = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	public int deleteNotice(int not_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="DELETE FROM notice WHERE not_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, not_num);
			result = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
}
