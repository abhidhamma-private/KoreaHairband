package com.petinfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class PetInfoDAO {
	private Connection conn = DBConn.getConnection();
	
	//데이터 삽입
	public int insertBoard(PetinfoDTO dto){
		int result=0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("INSERT INTO pet1(bbs_num, notice, category, mem_id, subject, content, hitCount) ");
			sb.append(" VALUES(pet1_seq.NEXTVAL,?,?,?,?,?,?)");
			
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
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	//데이터 개수 파악
	public int dataCount(){
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM pet1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
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
	
	//검색상태에서 데이터 개수 파악
	public int dataCount(String searchKey, String searchValue){
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;	
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM pet1 p JOIN member m ON p.mem_id = m.mem_id";
			if(searchKey.equals("mem_name")){
				sql += " WHERE INSTR(mem_name, ?) = 1";
			}else if(searchKey.equals("created")){
				searchValue=searchValue.replaceAll("-||/", "");
				sql += " WHERE TO_CHAR(created, 'YYYY-MM-DD') = ?";
			}else{
				sql += " WHERE INSTR(" + searchKey + ", ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			
			rs=pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
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
	
	//게시물 리스트 출력
	List<PetinfoDTO> listPetInfo(int start, int end){
		List<PetinfoDTO> list=new ArrayList<PetinfoDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("		SELECT bbs_num, p.mem_id, mem_name, category, subject, hitCount,	");
			sb.append("				TO_CHAR(created, 'YYYY-MM-DD')created ");
			sb.append("		FROM pet1 p JOIN member m ON p.mem_id = m.mem_id ");
			sb.append("		ORDER BY bbs_num DESC ");
			sb.append("	)tb WHERE ROWNUM <= ? ");
			sb.append(")WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				PetinfoDTO dto = new PetinfoDTO();
				
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("hitCount"));
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
	
	//검색시에 게시물 리스트  출력
	public List<PetinfoDTO> listPetInfo(int start, int end, String searchKey, String searchValue){
		List<PetinfoDTO> list = new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append("		SELECT bbs_num, p.mem_id, mem_name, category, subject, hitCount,	");
			sb.append("				TO_CHAR(created, 'YYYY-MM-DD')created ");
			sb.append("		FROM pet1 p JOIN member m ON p.mem_id = m.mem_id ");
			if(searchKey.equals("mem_name")){
				sb.append( " WHERE INSTR(mem_name, ?) = 1");
			}else if(searchKey.equals("created")){
				searchValue=searchValue.replaceAll("-", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ?");
			}else{
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
			
			while(rs.next()){
				PetinfoDTO dto = new PetinfoDTO();
				
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("hitCount"));
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
	
	//조회수 증가시키기
	public int updateHitCount(int bbs_num){
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE pet1 SET hitCount = hitCount+1 WHERE bbs_num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
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
	
	//해당 게시물 보기
	public PetinfoDTO readBoard(int bbs_num){
		PetinfoDTO dto = null;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		
		try {
			sb.append("SELECT bbs_num, p.mem_id, mem_name, category, subject, hitCount,	");
			sb.append("		created ");
			sb.append(" FROM pet1 p JOIN member m ON p.mem_id = m.mem_id ");
			sb.append(" WHERE bbs_num=?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				dto = new PetinfoDTO();
				dto.setBbs_num(rs.getInt("bbs_nem"));
				dto.setMem_id(rs.getString("mem_name"));
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
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
	
	//이전글
	
	
	//다음글
	
	
	//게시글 수정
	
	
	//게시글 삭제
	
	
}
