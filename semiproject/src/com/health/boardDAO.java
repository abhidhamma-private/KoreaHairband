package com.health;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.util.DBConn;

public class boardDAO {
	private Connection conn = DBConn.getConnection();
	
	
	public int insertBoard(boardDTO dto, String mode) {
		//받을것 모드 name subject content savafilename
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		int seq;
		
		try {
			sql="SELECT health1_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			seq=0;
			if(rs.next())
				seq=rs.getInt(1);
			rs.close();
			pstmt.close();
			dto.setBbs_Num(seq);
			
			if(mode.equals("created")) {
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if(mode.equals("reply")) {
				updateOrderNo(dto.getGroupNum(),dto.getOrderNo());
				dto.setDepth(dto.getDepth()+1);
				dto.setOrderNo(dto.getOrderNo()+1);
			}
			
			sql = " INSERT ALL"
				+ "INTO health1(bbs_Num, mem_id, subject, content,groupNum, dept, orederNo, parent) "
				+ " VALUES(?,?,?,?,?,?,?,?)"
				+ " VALUES(health2_file_seq.nextval, health2_seq.currval, ?) "
				+ " SELECT * FROM dual ";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBbs_Num());
			pstmt.setString(2, dto.getMem_Id());
			pstmt.setString(3, dto.getSubject());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setInt(8, dto.getParent());
			pstmt.setString(9, dto.getSavefilename());
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		return result;
	}
	
	public int updateOrderNo(int groupNum, int orderNo) {
		int result =0;
		PreparedStatement pstmt=null;
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
}
