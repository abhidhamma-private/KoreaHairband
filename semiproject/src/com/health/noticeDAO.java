package com.health;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class noticeDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertNotice(noticeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql="";
		
		try {
			sql = " INSERT ALL "
				+ " INTO health2(bbs_num, mem_id, subject, content) "
				+ " values(health2_seq.nextval, ?, ?, ?) "
				+ " INTO health2_file(file_num, bbs_num, savefilename) "
				+ " VALUES(health2_file_seq.nextval, health2_seq.currval, ?) "
				+ " SELECT * FROM dual ";
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
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		try {
			sql = "SELECT COUNT(*) FROM health2";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();		
			if(rs.next())
				result = rs.getInt(1);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<noticeDTO> listNotice(int start, int end) {
		List<noticeDTO> list = new ArrayList<>();
		String sql="";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			sql = "		SELECT * FROM ("
					+ " 	SELECT ROWNUM rnum, tb.* FROM("
					+ " 		SELECT H2.mem_id, F2.bbs_num, savefilename, subject, content "
					+ " 		,TO_CHAR(created, 'yy/mm/dd hh24:mi:ss') created, hitcount "
					+ " 		FROM health2 H2 JOIN health2_file F2 ON H2.BBS_NUM = F2.BBS_NUM "
					+ " 		ORDER BY bbs_num DESC "
					+ "		) tb WHERE ROWNUM<= ?"
					+ ") WHERE rnum >=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				noticeDTO dto = new noticeDTO();
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setSavefilename(rs.getString("savefilename"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setHitcount(rs.getInt("hitcount"));
				
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
			sql = "SELECT * FROM health2 H2 JOIN health2_file F2 ON "
					+ " H2.bbs_num = F2.bbs_num where H2.bbs_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
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
}
