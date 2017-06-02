package com.soccer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;


public class SCPhotoDAO
{
	private Connection conn = DBConn.getConnection();
	
	
	public int insertPhoto(SCPhotoDTO dto)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" INSERT INTO soccer2(bbs_num, mem_Id, subject, content, imageFilename) VALUES(soccer2_seq.NEXTVAL, ?, ?, ?, ?) ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, dto.getMem_Id());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getImageFilename());

			result = pstmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if(pstmt != null) {
				try {
					pstmt.close();

				} catch (Exception e2) {

				}
			}

			pstmt = null;
		}
		
		return result;
	}
	
	public int dataCount()
	{
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		// NVL(value, 1) -> value가 null일 경우 1을 반환	그렇지 않을 경우 value값을 반환
		sql = " SELECT NVL(COUNT(bbs_num), 0) cnt FROM soccer2 ";

		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next())
				result = rs.getInt(1); // result = rs.getInt("cnt");

		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {

			if (rs != null) {
				try {
					rs.close();

				} catch (Exception e2) {

				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();

				} catch (Exception e2) {

				}
			}

			rs = null;
			pstmt = null;
		}
		
		return result;
	}
	
	public List<SCPhotoDTO> listPhoto(int start, int end)
	{
		List<SCPhotoDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		
		try
		{
			sql = " SELECT * FROM ( ";
			sql += " 	SELECT ROWNUM rnum, tb.* FROM ( ";
			sql += " 		SELECT s2.mem_Id, mem_Name, s2.bbs_num, subject, content, imageFilename, created, file_num, saveFilename, originalFilename, fileSize ";
			sql += " 		FROM soccer2 s2 ";
			sql += "		LEFT OUTER JOIN member m ON s2.mem_Id = m.mem_Id ";
			sql += "		LEFT OUTER JOIN soccer2_file s2f ON s2.bbs_num = s2f.bbs_num ";
			sql += " 		ORDER BY s2.bbs_num DESC ";
			sql += " 	) tb WHERE ROWNUM <= ? ";
			sql += " ) WHERE rnum >= ? ";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				SCPhotoDTO dto = new SCPhotoDTO();
				
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setCreated(rs.getString("created"));
				dto.setFile_num(rs.getInt("file_num"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getLong("fileSize"));

				list.add(dto);
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (rs != null) {
				try {
					rs.close();

				} catch (Exception e2) {

				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();

				} catch (Exception e2) {

				}
			}

			rs = null;
			pstmt = null;
		}
		
		return list;
	}
	
	public SCPhotoDTO readPhoto(int bbs_num)
	{
		SCPhotoDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		
		try
		{
			sql += " SELECT s2.mem_Id, mem_Name, s2.bbs_num, subject, content, imageFilename, created, file_num, saveFilename, originalFilename, fileSize ";
			sql += " FROM soccer2 s2 ";
			sql += " LEFT OUTER JOIN member m ON s2.mem_Id = m.mem_Id ";
			sql += " LEFT OUTER JOIN soccer2_file s2f ON s2.bbs_num = s2f.bbs_num ";
			sql += " WHERE s2.bbs_num = ? ";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, bbs_num);

			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				dto = new SCPhotoDTO();
				
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setCreated(rs.getString("created"));
				dto.setFile_num(rs.getInt("file_num"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getLong("fileSize"));
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (rs != null) {
				try {
					rs.close();

				} catch (Exception e2) {

				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();

				} catch (Exception e2) {

				}
			}

			rs = null;
			pstmt = null;
		}
		
		return dto;
	}
	
	// 이전글
	public SCPhotoDTO preReadPhoto(int bbs_num)
	{
		SCPhotoDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" SELECT ROWNUM, tb.* FROM ( ");
			sb.append(" 	SELECT s2.mem_Id, mem_Name, s2.bbs_num, subject, content, imageFilename, created, file_num, saveFilename, originalFilename, fileSize ");
			sb.append(" 	FROM soccer2 s2 ");
			sb.append("		LEFT OUTER JOIN member m ON s2.mem_Id = m.mem_Id ");
			sb.append("		LEFT OUTER JOIN soccer2_file s2f ON s2.bbs_num = s2f.bbs_num ");
			sb.append("     WHERE s2.bbs_num > ? ");
			sb.append("     ORDER BY s2.bbs_num ASC ");
			sb.append(" ) tb WHERE ROWNUM = 1 ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);

			rs = pstmt.executeQuery();

			if (rs.next())
			{
				dto = new SCPhotoDTO();
				
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
		}

		return dto;
	}

	// 다음글
	public SCPhotoDTO nextReadPhoto(int bbs_num)
	{
		SCPhotoDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		
		try
		{
			sb.append(" SELECT ROWNUM, tb.* FROM ( ");
			sb.append(" 	SELECT s2.mem_Id, mem_Name, s2.bbs_num, subject, content, imageFilename, created, file_num, saveFilename, originalFilename, fileSize ");
			sb.append(" 	FROM soccer2 s2 ");
			sb.append("		LEFT OUTER JOIN member m ON s2.mem_Id = m.mem_Id ");
			sb.append("		LEFT OUTER JOIN soccer2_file s2f ON s2.bbs_num = s2f.bbs_num ");
			sb.append("     WHERE s2.bbs_num < ? ");
			sb.append("     ORDER BY s2.bbs_num ASC ");
			sb.append(" ) tb WHERE ROWNUM = 1 ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, bbs_num);

			rs = pstmt.executeQuery();

			if (rs.next())
			{
				dto = new SCPhotoDTO();
				
				dto.setBbs_num(rs.getInt("bbs_num"));
				dto.setSubject(rs.getString("subject"));
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			
			rs = null;
			pstmt = null;
		}

		return dto;
	}
	
	public int updatePhoto(SCPhotoDTO dto)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		
		try
		{
			sql = " UPDATE soccer2 SET subject = ?, content = ?, category = ?, imageFilename = ? ";
			sql += " WHERE bbs_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getCategory());
			pstmt.setString(4, dto.getImageFilename());
			pstmt.setInt(5, dto.getBbs_num());

			result = pstmt.executeUpdate();
			
			pstmt.close();
			
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
	
	public int updatePhotoFile(SCPhotoDTO dto)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		
		try
		{
			sql=" UPDATE soccer2_file SET saveFilename = ?, originalFilename = ?, fileSize = ? ";
			sql+= " WHERE file_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSaveFilename());
			pstmt.setString(2, dto.getOriginalFilename());
			pstmt.setLong(3, dto.getFileSize());
			pstmt.setInt(4, dto.getFile_num());

			result = pstmt.executeUpdate();
			
			pstmt.close();
			
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
	
	public int deletePhoto(int bbs_num)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		
		try
		{
			sql=" DELETE FROM soccer2 WHERE bbs_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbs_num);

			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch(Exception e) {
				}
			}
			
			pstmt = null;
		}
		
		return result;
	}
	
	public int deletePhotoFile(int file_num)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		
		try
		{
			sql = " DELETE FROM soccer2_file WHERE file_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, file_num);

			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch(Exception e) {
				}
			}
			
			pstmt = null;
		}
		
		return result;
	}
}
