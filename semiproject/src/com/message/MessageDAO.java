package com.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MessageDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertMessage(MessageDTO dto){ //메세지 보내기 
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="INSERT INTO message (message_num , mem_Id1 , mem_Id2 , content ) VALUES(message_seq.NEXTVAL,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getMem_Id1());
			pstmt.setString(2, dto.getMem_Id2());
			pstmt.setString(3, dto.getContent());
			
			
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public int messageCount(String mem_Id){ //마이페이지 메세지함 데이터 개수  /mem_Id:session 아이디 
		int result =0;
		PreparedStatement pstmt=null;
	    ResultSet rs=null;
	    String sql;
	    
	    try {
	    	sql="SELECT NVL(COUNT(*), 0) FROM message m1 JOIN member m2 ON m1.mem_Id1 = m2.mem_Id WHERE m1.mem_Id2=?";
	    	pstmt=conn.prepareStatement(sql);
	        pstmt.setString(1,mem_Id);

	        rs=pstmt.executeQuery();
	        
            if(rs.next())
                result=rs.getInt(1);
            
            rs.close();
            pstmt.close();
	    	
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return result;
	}
	
	public List<MessageDTO> listMessage(int start, int end, String mem_Id){//마이페이지 메시지함 리스트 
		List<MessageDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try{
			sb.append("SELECT * FROM (");
			sb.append("  SELECT ROWNUM rnum, tb.* FROM (");
        	sb.append("		SELECT message_num, me.mem_Id1 , me.mem_Id2 , mem_Name , content , readDate , sendDate ");
        	sb.append(" 	FROM message me ");
        	sb.append("		JOIN member m ON me.mem_Id1 =m.mem_Id");
        	sb.append(" 	WHERE mem_Id2=?");
        	sb.append("     ORDER BY message_num  DESC  ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");
        	
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, mem_Id);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				MessageDTO dto = new MessageDTO();
				dto.setMessage_num(rs.getInt("message_num"));
				dto.setMem_Id1(rs.getString("mem_Id1"));
				dto.setMem_Id2(rs.getString("mem_Id2"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setContent(rs.getString("content"));	
				dto.setReadDate(rs.getString("readDate"));
				dto.setSendDate(rs.getString("sendDate"));
				list.add(dto);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public MessageDTO readMessage(int message_num){ //메세지함 읽기 

		MessageDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		
		try
		{
			sb.append(" SELECT message_num, me.mem_Id1 , me.mem_Id2 , mem_Name , content , readDate , sendDate  ");
			sb.append(" FROM message me ");
			sb.append(" JOIN member m ON me.mem_Id1 = m.mem_Id ");
			sb.append(" WHERE message_num = ? ");
			
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, message_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				dto = new MessageDTO();
				dto.setMessage_num(rs.getInt("message_num"));
				dto.setMem_Id1(rs.getString("mem_Id1"));
				dto.setMem_Id2(rs.getString("mem_Id2"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setContent(rs.getString("content"));	
				dto.setReadDate(rs.getString("readDate"));
				dto.setSendDate(rs.getString("sendDate"));
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
		
		
		return dto;
		
	}

	public int updateReadDate(int message_num){ //수신확인 

		int result=0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE message SET readDate=SYSDATE WHERE message_num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, message_num);
			

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			
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

	public int deleteMessage(int message_num , String mem_Id){
		int result=0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append(" DELETE FROM message WHERE message_num = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, message_num);
		
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return result;
	}

	public int deleteMessageList(int[] messages ){ //체크박스로 메세지 많이 삭제!
		int result=0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			if(messages==null)
				return result;
			
			sql = "DELETE FROM message WHERE messge_num IN (";
			
			for(int i=0; i<messages.length; i++)
				sql+="?,";
			
			sql=sql.substring(0, sql.length()-1);
			sql+=") ";
			
			pstmt = conn.prepareStatement(sql);
			
			for(int i=0; i<messages.length; i++)
				pstmt.setInt(i+1, messages[i]);
		
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt=null;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

}
