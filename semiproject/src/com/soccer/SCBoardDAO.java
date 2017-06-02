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
			
			sb.append(" INSERT INTO soccer1(bbs_num, mem_Id, subject, content, category, groupNum, depth, orderNo, parent, notice) ");
			sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setString(2, dto.getMem_Id());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getCategory());
			pstmt.setInt(6, dto.getGroupNum());
			pstmt.setInt(7, dto.getDepth());
			pstmt.setInt(8, dto.getOrderNo());
			pstmt.setInt(9, dto.getParent());
			pstmt.setInt(10, dto.getNotice());
			
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
			sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 WHERE notice = 0 ");
			
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
        		sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id WHERE notice = 0 AND TO_CHAR(created, 'YYYYMMDD') = ? ");
        	} 
        	
        	else if(searchKey.equals("mem_Name"))
        		sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id WHERE notice = 0 AND INSTR(mem_Name, ?) = 1 ");
        	
        	else
        		sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id WHERE notice = 0 AND INSTR(" + searchKey + ", ?) >= 1 ");
        	
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
    
    // 공지 리스트
    public List<SCBoardDTO> listSCBoardNotice()
    {
    	List<SCBoardDTO> list = new ArrayList<SCBoardDTO>();
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
			sb.append(" LEFT OUTER JOIN soccer1_file sf ON s1.bbs_num = sf.bbs_num ");
			sb.append(" WHERE notice = 1 ");
			sb.append(" ORDER BY groupNum DESC, orderNo ASC ");

			pstmt = conn.prepareStatement(sb.toString());

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
			sb.append("         LEFT OUTER JOIN soccer1_file sf ON s1.bbs_num = sf.bbs_num ");
			sb.append("         ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? AND notice = 0 ");
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
			sb.append("         LEFT OUTER JOIN soccer1_file sf ON s1.bbs_num = sf.bbs_num ");
			
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
			sb.append("    ) tb WHERE ROWNUM <= ? AND notice = 0 ");
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
			sb.append(" LEFT OUTER JOIN soccer1_file sf ON s1.bbs_num = sf.bbs_num ");
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
                
                sb.append("     	   AND (bbs_num > ? ) AND notice = 0 ");
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
                sb.append("     WHERE bbs_num > ? AND notice = 0 ");
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
                
                sb.append("     	   AND (bbs_num < ? ) AND notice = 0 ");
                sb.append("     ORDER BY bbs_num DESC ");
                sb.append(" ) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, bbs_num);
			}
            
            else
            {
                sb.append(" SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT bbs_num, subject FROM soccer1 s1 JOIN member m ON s1.mem_Id = m.mem_Id ");                
                sb.append("     WHERE bbs_num < ? AND notice = 0 ");
                sb.append("     ORDER BY bbs_num DESC ");
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
    
    public int updateSCBoard(SCBoardDTO dto)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
    	StringBuffer sb = new StringBuffer();
    	
		
		try
		{
			sb.append(" UPDATE soccer1 SET subject = ?, content = ?, category = ?, notice = ? ");
			sb.append(" WHERE bbs_num = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getCategory());
			pstmt.setInt(4, dto.getNotice());
			pstmt.setInt(5, dto.getBbs_num());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			sb.setLength(0);
			
			sb.append(" UPDATE soccer1_file SET saveFilename = ?, originalFilename = ?, fileSize = ? ");
			sb.append(" WHERE file_num = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getSaveFilename());
			pstmt.setString(2, dto.getOriginalFilename());
			pstmt.setLong(3, dto.getFileSize());
			pstmt.setInt(4, dto.getFile_num());
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch(Exception e) {		}
			}
			
			pstmt = null;
		}
    	
    	return result;
    }
    
    public int addSCBoardFile(SCBoardDTO dto)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
    	StringBuffer sb = new StringBuffer();
    	
		
		try
		{
			sb.append(" INSERT INTO soccer1_file(file_num, bbs_num, saveFilename, originalFilename, fileSize) ");
			sb.append(" VALUES(soccer1_file_seq.NEXTVAL, ?, ?, ?, ?) ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setString(2, dto.getSaveFilename());
			pstmt.setString(3, dto.getOriginalFilename());
			pstmt.setLong(4, dto.getFileSize());
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch(Exception e) {		}
			}
			
			pstmt = null;
		}
    	
    	return result;
    }
    
    public SCBoardDTO readSCBoardFile(int bbs_num)
    {
    	SCBoardDTO dto = null;
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" SELECT file_num, bbs_num, saveFilename, originalFilename, fileSize ");
			sb.append(" FROM soccer1_file ");
			sb.append(" WHERE bbs_num = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, bbs_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				dto = new SCBoardDTO();
				
				dto.setFile_num(rs.getInt("file_num"));
				dto.setBbs_num(rs.getInt("bbs_num"));
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
    
    public int insertReply(SCBoardDTO dto)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
    	StringBuffer sb = new StringBuffer();
    	
    	
    	try
    	{
    		sb.append(" INSERT INTO soccer1_reply (reply_num, bbs_num, mem_Id, content, answer) ");
			sb.append(" VALUES (soccer1_reply_seq.NEXTVAL, ?, ?, ?, ?) ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getBbs_num());
			pstmt.setString(2, dto.getMem_Id());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, 0);
			
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
    
    // 게시물의 댓글 개수
    public int dataCountReply(int bbs_num)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
    	StringBuffer sb = new StringBuffer();
    	ResultSet rs = null;
    	
    	
    	try
    	{
    		sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1_reply WHERE bbs_num = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = rs.getInt(1);
			
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
    
    public List<SCBoardDTO> replyListSCBoard(int bbs_num, int start, int end)
    {
    	List<SCBoardDTO> list = new ArrayList<SCBoardDTO>();
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" SELECT * FROM ( ");
			sb.append(" 	SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT tbreply.reply_num, bbs_num, mem_Id, mem_Name, content, created FROM ( ");
			sb.append("				SELECT reply_num, sr.bbs_num, sr.mem_Id, mem_Name, sr.content, sr.created ");
			sb.append("				FROM soccer1_reply sr ");
			sb.append("				LEFT OUTER JOIN soccer1 s1 ON sr.bbs_num = s1.bbs_num ");
			sb.append("				LEFT OUTER JOIN member m ON sr.mem_Id = m.mem_Id  ");
			sb.append("				WHERE sr.bbs_num = ? ");
			sb.append("			) tbreply ");
			sb.append("			ORDER BY reply_num DESC ");
			sb.append("		) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				SCBoardDTO dto = new SCBoardDTO();
				
				dto.setReply_num(rs.getInt("reply_num"));
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				
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
    
    public int deleteReply(int reply_num, String mem_Id)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" DELETE FROM soccer1_reply WHERE reply_num = ? ");
			
			if(! mem_Id.equals("admin"))
				sb.append(" AND mem_Id = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, reply_num);
			pstmt.setString(2, mem_Id);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt!=null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return result;
    }
    
    public int insertLike(int bbs_num, String mem_Id)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" INSERT INTO soccer1_like (bbs_num, mem_Id) ");
			sb.append(" VALUES (?, ?) ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, bbs_num);
			pstmt.setString(2, mem_Id);
			
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
    
    public int readLikeSCBoard(int bbs_num, String mem_Id)
    {
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		int result = 0;
    	
		
		try
		{
			sb.append(" SELECT count(*) cnt FROM soccer1_like WHERE bbs_num = ? AND mem_Id = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, bbs_num);
			pstmt.setString(2, mem_Id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				if(rs.getInt("cnt") > 0)
					result = 1;
			}
			
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
    
    public String checkLikeSCBoard(int bbs_num, String mem_Id)
    {
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		String result = null;
    	
		
		try
		{
			sb.append(" SELECT mem_Id FROM soccer1_like WHERE bbs_num = ? AND mem_Id = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, bbs_num);
			pstmt.setString(2, mem_Id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = rs.getString("mem_Id");
			
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
    
    // 좋아요 개수
 	public int dataCountLike(int bbs_num)
 	{
 		int result = 0;
 		PreparedStatement pstmt = null;
 		ResultSet rs = null;
 		StringBuffer sb = new StringBuffer();
 		
 		
 		try
 		{
 							   // COUNT(*)가 NULL이면 0 아니면 Value값 반환
 			sb.append(" SELECT NVL(COUNT(*), 0) FROM soccer1_like WHERE bbs_num = ? ");
 			
 			pstmt = conn.prepareStatement(sb.toString());
 			pstmt.setInt(1, bbs_num);
 			
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
 	
 	public int deleteLike(int bbs_num, String mem_Id)
    {
    	int result = 0;
    	PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" DELETE FROM soccer1_like WHERE bbs_num = ? AND mem_Id = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, bbs_num);
			pstmt.setString(2, mem_Id);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt!=null) {
				try {
					pstmt.close();	
				} catch(Exception e2) {		}
			}
			
			pstmt = null;
		}
    	
    	return result;
    }
}
