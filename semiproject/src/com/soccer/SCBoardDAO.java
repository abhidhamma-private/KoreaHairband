package com.soccer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;


public class SCBoardDAO
{
	private Connection conn = DBConn.getConnection();
	
	
	public int insertSCBoard(SCBoardDTO dto, String mode)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		int seq;
		
		
		try
		{
			sb.append(" SELECT soccer1_seq.NEXTVAL FROM dual ");
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			seq = 0;
			
			if(rs.next())
				seq = rs.getInt(1);
			
			rs.close();
			pstmt.close();
			sb.setLength(0);
			
			dto.setBbs_num(seq);
			
			if(mode.equals("created"))
			{
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			}
			
			else if(mode.equals("reply"))
			{
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				
				dto.setOrderNo(dto.getOrderNo() + 1);
				dto.setDepth(dto.getDepth() + 1);
			}
			
			sb.append(" INSERT INTO soccer1(bbs_num, mem_Id, subject, content, groupNum, depth, orderNo, parent) ");
			sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?) ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setString(2, dto.getMem_Id());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setInt(8, dto.getParent());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			sb.setLength(0);
			
			if(dto.getOriginalFilename() != null)
			{
										// 모든 컬럼을 넣을거면 변수 안써도 됨
				sb.append(" INSERT INTO soccer1_file(file_num, bbs_num, saveFilename, originalFilename, fileSize) ");
				sb.append(" VALUES(soccer1_file_seq.NEXTVAL, ?, ?, ?, ?) ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, dto.getBbs_num());
				pstmt.setString(2, dto.getSaveFilename());
				pstmt.setString(3, dto.getOriginalFilename());
				pstmt.setLong(4, dto.getFileSize());
				
				result = pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
		
		return result;
	}
	
	// 답변일 경우 orderNo 변경
	public int updateOrderNo(int groupNum, int orderNo)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" UPDATE soccer1 SET orderNo = orderNo + 1 WHERE groupNum = ? AND orderNo > ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
		
		return result;
	}
	
	// 전체 데이터 개수
	public int dataCount()
	{
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
							   // COUNT(*)가 NULL이면 0 아니면 Value값 반환
			sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = rs.getInt(1);
			
		} catch(Exception e) {
			
			System.out.println(e.toString());
		
		} finally {
			
			if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
		
		return result;
	}
	
	// 검색했을 경우의 데이터 개수
    public int dataCount(String searchKey, String searchValue)
    {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();

        
        try
        {
        	if(searchKey.equals("created"))
        	{
        		searchValue = searchValue.replaceAll("-", "");
        		sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
        	} 
        	
        	else if(searchKey.equals("mem_Name"))
        		sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id WHERE INSTR(mem_Name, ?) = 1 ");
        	
        	else
        		sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id WHERE INSTR(" + searchKey + ", ?) >= 1 ");
        	
            pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, searchValue);

            rs = pstmt.executeQuery();

            if(rs.next())
                result = rs.getInt(1);
            
        } catch(Exception e) {
        	
            System.out.println(e.toString());
            
        } finally {
			
        	if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
        
        return result;
    }
    
    // 전체 리스트
    public List<SCBoardDTO> listSCBoard(int start, int end)
    {
    	List<SCBoardDTO> list = new ArrayList<SCBoardDTO>();
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" SELECT * FROM ( ");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT s1.bbs_num, notice, category, s1.mem_Id, mem_Name, ");
			sb.append("                subject, content, groupNum, orderNo, depth, hitCount, ");
			sb.append("                TO_CHAR(created, 'YYYY-MM-DD') created, ");
			sb.append("                file_num, saveFilename, originalFilename, fileSize ");
			sb.append("         FROM soccer1 s1 ");
			sb.append("         LEFT OUTER JOIN member m ON s1.mem_Id = m.mem_Id ");
			sb.append("         JOIN soccer1_file sf ON s1.bbs_num = sf.bbs_num ");
			sb.append("         ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				SCBoardDTO dto = new SCBoardDTO();
				
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setNotice(rs.getInt("notice"));
				dto.setCategory(rs.getString("category"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setFile_num(rs.getInt("file_num"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getInt("fileSize"));
				
				list.add(dto);
			}
			
		} catch(Exception e) {
        	
            System.out.println(e.toString());
            
        } finally {
			
        	if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return list;
    }
    
    // 검색했을 경우의 리스트
    public List<SCBoardDTO> listSCBoard(int start, int end, String searchKey, String searchValue)
    {
    	List<SCBoardDTO> list = new ArrayList<SCBoardDTO>();
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		
		try
		{
			sb.append(" SELECT * FROM ( ");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT s1.bbs_num, notice, category, s1.mem_Id, mem_Name, ");
			sb.append("                subject, content, groupNum, orderNo, depth, hitCount, ");
			sb.append("                TO_CHAR(created, 'YYYY-MM-DD') created, ");
			sb.append("                file_num, saveFilename, originalFilename, fileSize ");
			sb.append("         FROM soccer1 s1 ");
			sb.append("         LEFT OUTER JOIN member m ON s1.mem_Id = m.mem_Id ");
			sb.append("         JOIN soccer1_file sf ON s1.bbs_num = sf.bbs_num ");
			
			if(searchKey.equals("created"))
			{
				searchValue = searchValue.replaceAll("-", "");
				sb.append("     WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			} 
			
			else if(searchKey.equals("mem_Name"))
				sb.append("     WHERE INSTR(mem_Name, ?) = 1 ");
			
			else
				sb.append("     WHERE INSTR(" + searchKey + ", ?) >= 1 ");
			
			sb.append("         ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				SCBoardDTO dto = new SCBoardDTO();
				
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setNotice(rs.getInt("notice"));
				dto.setCategory(rs.getString("category"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setFile_num(rs.getInt("file_num"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getInt("fileSize"));
				
				list.add(dto);
			}
			
		} catch(Exception e) {
        	
            System.out.println(e.toString());
            
        } finally {
			
        	if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return list;
    }
    
    public SCBoardDTO readSCBoard(int bbs_num)
    {
    	SCBoardDTO dto = null;
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" SELECT s1.bbs_num, notice, category, s1.mem_Id, mem_Name, ");
			sb.append("        subject, content, groupNum, orderNo, depth, hitCount, ");
			sb.append("        TO_CHAR(created, 'YYYY-MM-DD') created, ");
			sb.append("        file_num, saveFilename, originalFilename, fileSize ");
			sb.append(" FROM soccer1 s1 ");
			sb.append(" LEFT OUTER JOIN member m ON s1.mem_Id = m.mem_Id ");
			sb.append(" JOIN soccer1_file sf ON s1.bbs_num = sf.bbs_num ");
			sb.append(" WHERE s1.bbs_num = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, bbs_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				dto = new SCBoardDTO();
				
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setNotice(rs.getInt("notice"));
				dto.setCategory(rs.getString("category"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setFile_num(rs.getInt("file_num"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getInt("fileSize"));
			}
		
		} catch(Exception e) {
        	
            System.out.println(e.toString());
            
        } finally {
			
        	if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return dto;
    }
    
    public int updateHitCount(int bbs_num)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
    	StringBuffer sb = new StringBuffer();
    	
    	
    	try
    	{
			sb.append(" UPDATE soccer1 SET hitCount = hitCount + 1 WHERE bbs_num = ? ");
    		
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return result;
    }
    
    public SCBoardDTO preReadSCBoard(int bbs_num, String searchKey, String searchValue)
    {
    	SCBoardDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
        
        
        try
        {
            if(searchValue.length() != 0)
            {
                sb.append(" SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT bbs_num, subject FROM soccer1 s1 LEFT OUTER JOIN member m ON s1.mem_Id = m.mem_Id ");
                
                if(searchKey.equalsIgnoreCase("created"))
                {
                	searchValue=searchValue.replaceAll("-", "");
                	sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                }
                
                else
                	sb.append(" WHERE (INSTR(" + searchKey + ", ?) >= 1)  ");
                
                sb.append("     	   AND (bbs_num > ? ) ");
                sb.append("     ORDER BY bbs_num ASC ");
                sb.append(" ) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, bbs_num);
			}
            
            else
            {
                sb.append(" SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT bbs_num, subject FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id ");                
                sb.append("     WHERE bbs_num > ? ");
                sb.append("     ORDER BY bbs_num ASC ");
                sb.append(" ) tb WHERE ROWNUM = 1 ");

                pstmt = conn.prepareStatement(sb.toString());
                
                pstmt.setInt(1, bbs_num);
			}

            rs = pstmt.executeQuery();
            
            if(rs.next())
            {
            	dto = new SCBoardDTO();
            	
            	dto.setBbs_num(rs.getInt("bbs_num"));
            	dto.setSubject(rs.getString("subject"));
            }
        
        } catch(Exception e) {
        	
            e.printStackTrace();
        
        } finally {
			
        	if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
        
        return dto;
    }
    
    public SCBoardDTO nextReadSCBoard(int bbs_num, String searchKey, String searchValue)
    {
    	SCBoardDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
        
        
        try
        {
            if(searchValue.length() != 0)
            {
                sb.append(" SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT bbs_num, subject FROM soccer1 s1 LEFT OUTER JOIN member m ON s1.mem_Id = m.mem_Id ");
                
                if(searchKey.equalsIgnoreCase("created"))
                {
                	searchValue=searchValue.replaceAll("-", "");
                	sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                }
                
                else
                	sb.append(" WHERE (INSTR(" + searchKey + ", ?) >= 1)  ");
                
                sb.append("     	   AND (bbs_num < ? ) ");
                sb.append("     ORDER BY bbs_num ASC ");
                sb.append(" ) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, bbs_num);
			}
            
            else
            {
                sb.append(" SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT bbs_num, subject FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id ");                
                sb.append("     WHERE bbs_num < ? ");
                sb.append("     ORDER BY bbs_num ASC ");
                sb.append(" ) tb WHERE ROWNUM = 1 ");

                pstmt = conn.prepareStatement(sb.toString());
                
                pstmt.setInt(1, bbs_num);
			}

            rs = pstmt.executeQuery();
            
            if(rs.next())
            {
            	dto = new SCBoardDTO();
            	
            	dto.setBbs_num(rs.getInt("bbs_num"));
            	dto.setSubject(rs.getString("subject"));
            }
        
        } catch(Exception e) {
        	
            e.printStackTrace();
        
        } finally {
			
        	if(rs != null) {
				try {
					rs.close();	
				} catch(Exception e2) {		}
			}
			
			rs = null;
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
        
        return dto;
    }
    
    public int deleteSCBoardFile(int bbs_num)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
    	StringBuffer sb = new StringBuffer();
    	
    	
    	try
    	{
			sb.append(" DELETE FROM soccer1_file WHERE bbs_num = ? ");
    		
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return result;
    }
    
    public int deleteSCBoard(int bbs_num)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
    	StringBuffer sb = new StringBuffer();
    	
    	
    	try
    	{
			sb.append(" DELETE FROM soccer1 WHERE bbs_num = ? ");
    		
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return result;
    }
}
