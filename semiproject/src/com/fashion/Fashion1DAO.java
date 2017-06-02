package com.fashion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


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
	
	//검색없음
	
	public int dataCount(){
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try
		{
			sql="SELECT NVL(COUNT(*), 0) FROM fashion1";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			if(rs.next())
				result=rs.getInt(1);
			
			rs.close();
			pstmt.close();
			
		} catch(Exception e) {
			
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	
	public List<Fashion1DTO>listBoard(int start , int end){
		List<Fashion1DTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		F1replyDAO dao = new F1replyDAO();
		try{
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("         SELECT bbs_num, m.mem_Id, mem_Name,");
			sb.append("                subject, category, notice, hitCount,");
			sb.append("                created , content");
			sb.append("         FROM fashion1 f1");
			sb.append("         JOIN member m ON f1.mem_Id=m.mem_Id");
			sb.append("         ORDER BY bbs_num DESC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				Fashion1DTO dto1=new Fashion1DTO();
				
				dto1.setBbs_num(rs.getInt("bbs_num"));
				dto1.setMem_Id(rs.getString("mem_Id"));
				dto1.setMem_Name(rs.getString("mem_Name"));
				dto1.setSubject(rs.getString("subject"));
				dto1.setCategory(rs.getString("category"));
				dto1.setNotice(rs.getInt("notice"));
				dto1.setHitCount(rs.getInt("hitCount"));
				dto1.setCreated(rs.getString("created"));
				dto1.setContent(rs.getString("content"));
				dto1.setReply(dao.dataCountReply(dto1.getBbs_num()));

				list.add(dto1);
			}
			
			rs.close();
			pstmt.close();
			
		} catch(Exception e) {
			
			System.out.println(e.toString());
		}
		return list;
	}
	

	//검색 있음
	
	public int dataCount(String searchKey, String searchValue ,String category){
		int result =0;
		 PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        String sql;

	        try
	        {
	        	if(searchKey.equals("category")){
	     
	        		sql="SELECT NVL(COUNT(*), 0) FROM fashion1 f1 JOIN member m ON f1.mem_Id=m.mem_Id  WHERE CATEGORY='"+category+"' AND INSTR(content, ?) >= 1 ";
	        	}
	        	else if(searchKey.equals("created"))
	        	{
	        		searchValue=searchValue.replaceAll("-", "");
	        		sql="SELECT NVL(COUNT(*), 0) FROM fashion1 f1 JOIN member m ON f1.mem_Id=m.mem_Id  WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ";
	        	} 
	        	
	        	else if(searchKey.equals("mem_Name"))
	        		sql="SELECT NVL(COUNT(*), 0) FROM fashion1 f1 JOIN member m ON f1.mem_Id=m.mem_Id WHERE INSTR(mem_Name, ?) = 1 ";
	        	
	        	else if(searchKey.equals("mem_Id"))
	        		sql="SELECT NVL(COUNT(*), 0) FROM fashion1 f1 JOIN member m ON f1.mem_Id=m.mem_Id WHERE m.mem_Id = ? ";
	        	
	        	else
	        		sql="SELECT NVL(COUNT(*), 0) FROM fashion1 f1 JOIN member m ON f1.mem_Id=m.mem_Id WHERE INSTR(" + searchKey + ", ?) >= 1 ";
	        	
	            pstmt=conn.prepareStatement(sql);
	            pstmt.setString(1, searchValue);

	            rs=pstmt.executeQuery();

	            if(rs.next())
	                result=rs.getInt(1);
	            
	            rs.close();
	            pstmt.close();
	            
	        } catch(Exception e) {
	        	
	            System.out.println(e.toString());
	        }
	        
		return result;
	}
	
	
	public List<Fashion1DTO>listBoard(int start , int end , String searchKey , String searchValue , String category){
		List<Fashion1DTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try{
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("         SELECT bbs_num, m.mem_Id, mem_Name,");
			sb.append("                subject, category, notice, hitCount,");
			sb.append("                created , content ");
			sb.append("         FROM fashion1 f1");
			sb.append("         JOIN member m ON f1.mem_Id=m.mem_Id");
			
		
        	if(searchKey.equals("created"))
        	{
        		searchValue=searchValue.replaceAll("-", "");
        		sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ");
        	} 
        	
        	else if(searchKey.equals("mem_Name"))
        		sb.append(" WHERE INSTR(mem_Name, ?) = 1 ");
        	
        	else if(searchKey.equals("subject"))
        		sb.append( " WHERE INSTR(subject, ?) >= 1 ");
        	
        	else if(searchKey.equals("content"))
        		sb.append( " WHERE INSTR(content, ?) >= 1 ");
        	
         	else if(searchKey.equals("mem_Id"))
        		sb.append( " WHERE  m.mem_Id = ? ");
        	
        	else 
            	sb.append(" WHERE category='" +category+ "' AND INSTR(content, ?) >= 1 ");
          
        		
        	
			sb.append("         ORDER BY bbs_num  DESC  ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				Fashion1DTO dto1=new Fashion1DTO();
				
				dto1.setBbs_num(rs.getInt("bbs_num"));
				dto1.setMem_Id(rs.getString("mem_Id"));
				dto1.setMem_Name(rs.getString("mem_Name"));
				dto1.setSubject(rs.getString("subject"));
				dto1.setCategory(rs.getString("category"));
				dto1.setNotice(rs.getInt("notice"));
				dto1.setHitCount(rs.getInt("hitCount"));
				dto1.setCreated(rs.getString("created"));
				dto1.setContent(rs.getString("content"));
				
				list.add(dto1);
			}
			
			rs.close();
			pstmt.close();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		return list;
	}
	

	// 이전글
	public Fashion1DTO preReadFashion1(int bbs_num, String searchKey, String searchValue ,String category) {
	    	Fashion1DTO dto1=null;

	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuffer sb = new StringBuffer();

	        try {
	            if(searchValue.length() != 0) {
	                sb.append("SELECT ROWNUM, tb.* FROM ( ");
	                sb.append("     SELECT bbs_num, subject FROM fashion1 n JOIN member m ON n.mem_Id=m.mem_Id ");
	                
	                if(searchKey.equalsIgnoreCase("created"))
	                	sb.append("     WHERE (TO_CHAR(created, 'YYYY-MM-DD') = ?)  ");
	                
	                else if(searchKey.equals("category"))
	                	sb.append("     WHERE category='" +category+ "' AND INSTR(content, ?) >= 1 ");

	             	else if(searchKey.equals("mem_Id"))
	            		sb.append( " WHERE  m.mem_Id = ? ");
	                
	                else
	                	sb.append("     WHERE (" + searchKey + " LIKE '%' || ? || '%')  ");
	                
	                sb.append("         AND (bbs_num > ? ) ");
	                sb.append("         ORDER BY bbs_num ASC ");
	                sb.append("      ) tb WHERE ROWNUM=1 ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setString(1, searchValue);
	                pstmt.setInt(2, bbs_num);
				} else {
	                sb.append("SELECT ROWNUM, tb.* FROM ( ");
	                sb.append("     SELECT bbs_num, subject FROM fashion1 n JOIN member m ON n.mem_Id=m.mem_Id ");                
	                sb.append("     WHERE bbs_num > ? ");
	                sb.append("         ORDER BY bbs_num ASC ");
	                sb.append("      ) tb WHERE ROWNUM=1 ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, bbs_num);
				}

	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto1=new Fashion1DTO();
	                dto1.setBbs_num(rs.getInt("bbs_num"));
	                dto1.setSubject(rs.getString("subject"));
	            }
	            rs.close();
	            pstmt.close();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    
	        return dto1;
	    }

	 // 다음글
	public Fashion1DTO nextReadFashion1(int bbs_num, String searchKey, String searchValue  ,String category) {
	    	Fashion1DTO dto1=null;

	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuffer sb = new StringBuffer();

	        try {
	            if(searchValue.length() != 0) {
	                sb.append("SELECT ROWNUM, tb.* FROM ( ");
	                sb.append("     SELECT bbs_num, subject FROM fashion1 n JOIN member m ON n.mem_Id=m.mem_Id ");
	              
	                
	                if(searchKey.equalsIgnoreCase("created"))
	                	sb.append("     WHERE (TO_CHAR(created, 'YYYY-MM-DD') = ?)  ");
	                else if(searchKey.equals("category"))
	                	sb.append("     WHERE category='" +category+ "' AND INSTR(content, ?) >= 1 ");
	                
	            	else if(searchKey.equals("mem_Id"))
	            		sb.append( " WHERE  m.mem_Id = ? ");
	                else
	                	sb.append("     WHERE (" + searchKey + " LIKE '%' || ? || '%')  ");
	                
	                
	                sb.append("         AND (bbs_num < ? ) ");
	                sb.append("         ORDER BY bbs_num DESC ");
	                sb.append("      ) tb WHERE ROWNUM=1 ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setString(1, searchValue);
	                pstmt.setInt(2, bbs_num);
				
	            
	            } else {
	                sb.append("SELECT ROWNUM, tb.* FROM ( ");
	                sb.append("     SELECT bbs_num, subject FROM fashion1 n JOIN member m ON n.mem_Id=m.mem_Id ");
	                sb.append("     WHERE bbs_num < ? ");
	                sb.append("         ORDER BY bbs_num DESC ");
	                sb.append("      ) tb WHERE ROWNUM=1 ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, bbs_num);
				}

	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto1=new Fashion1DTO();
	                dto1.setBbs_num(rs.getInt("bbs_num"));
	                dto1.setSubject(rs.getString("subject"));
	            }
	            rs.close();
	            pstmt.close();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    
	        return dto1;
	    }
		
		
	public Fashion1DTO readBoard(int bbs_num)
		{
			Fashion1DTO dto1=null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuffer sb = new StringBuffer();
			
			
			try
			{
				sb.append(" SELECT bbs_num, m.mem_Id, mem_Name, subject, content, category, notice ,hitCount, created ");
				sb.append(" FROM Fashion1 f1 ");
				sb.append(" JOIN member m ON f1.mem_Id = m.mem_Id ");
				sb.append(" WHERE bbs_num = ? ");
				
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, bbs_num);
				
				rs = pstmt.executeQuery();
				
				if(rs.next())
				{
					dto1 = new Fashion1DTO();
					
					dto1.setBbs_num(rs.getInt("bbs_num"));
					dto1.setMem_Id(rs.getString("mem_Id"));
					dto1.setMem_Name(rs.getString("mem_Name"));
					dto1.setSubject(rs.getString("subject"));
					dto1.setContent(rs.getString("content"));
					dto1.setCategory(rs.getString("category"));
					dto1.setNotice(rs.getInt("notice"));
					dto1.setHitCount(rs.getInt("hitCount"));
					dto1.setCreated(rs.getString("created"));
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				
			} finally {
				
				if (rs != null)
				{
					try {
						rs.close();
					} catch (Exception e2) {

					}
				}

				if (pstmt != null)
				{
					try {
						pstmt.close();
					} catch (Exception e2) {

					}
				}

				rs = null;
				pstmt = null;
			}
			
			return dto1;
		}
		
		
	public int updateHitCount(int bbs_num)
		{
			int result = 0;
			
			PreparedStatement pstmt = null;
			StringBuffer sb = new StringBuffer();

			try {
				sb.append(" UPDATE fashion1 SET hitCount = hitCount + 1 WHERE bbs_num = ? ");

				pstmt = conn.prepareStatement(sb.toString());
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

				pstmt = null;
			}
			
			return result;
		}

	
	public int deleteBoard(int bbs_num, String mem_Id)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			if(mem_Id.equals("admin"))
			{
				sb.append(" DELETE FROM fashion1 WHERE bbs_num = ? ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, bbs_num);
				
				result = pstmt.executeUpdate();
			}
			
			else
			{
				sb.append(" DELETE FROM fashion1 WHERE bbs_num = ? AND mem_Id = ? ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, bbs_num);
				pstmt.setString(2, mem_Id);
				
				result = pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			
			pstmt = null;
		}
		
		return result;
	}
	
	public int updateBoard(Fashion1DTO dto1, String mem_Id)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" UPDATE fashion1 SET subject = ?, content = ? , category= ?  WHERE bbs_num = ? AND mem_Id = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto1.getSubject());
			pstmt.setString(2, dto1.getContent());
			pstmt.setString(3, dto1.getCategory());
			pstmt.setInt(4, dto1.getBbs_num());
			pstmt.setString(5, mem_Id);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			
			pstmt = null;
		}
		
		return result;
	}
	

}
