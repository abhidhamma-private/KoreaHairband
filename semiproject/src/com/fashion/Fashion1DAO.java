package com.fashion;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.util.DBConn;

public class Fashion1DAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertBoard(Fashion1DTO dto){
		int result =0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append(" INSERT INTO fashion1(bbs_num , mem_Id , category, notice , subject, content )");
			sb.append("VALUES(fashion1_seq.NEXTVAL ,?,?,?,?,?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getMem_Id());
			pstmt.setString(2, dto.getCategory());
			pstmt.setInt(3, dto.getNotice());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			
			result = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e2) {	}
			}
			pstmt=null;
		}
		return result;
	}
}
