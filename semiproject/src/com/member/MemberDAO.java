package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn=DBConn.getConnection();
	
	public MemberDTO readMember(String mem_Id) {
		MemberDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			/*
			SELECT m.mem_Id, mem_Name, mem_Pwd, mem_img, birth, email, tel, zip, addr1, addr2, TO_CHAR(created_date, 'YY/MM/DD' ) created 
			FROM member m
			LEFT OUTER JOIN member_info mi
			ON m.mem_Id = mi.mem_Id
			WHERE m.mem_id = 'admin'
			*/
			sb.append(" SELECT m.mem_Id, mem_Name, mem_Pwd, mem_img, birth, email, tel, zip, addr1, addr2, TO_CHAR(created_date, 'YY/MM/DD') created ");
			sb.append(" FROM member m ");
			sb.append(" LEFT OUTER JOIN member_info mi ");
			sb.append(" ON m.mem_Id = mi.mem_Id ");
			sb.append(" WHERE m.mem_id = ? ");
			
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, mem_Id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setMem_Id(rs.getString("mem_Id"));
				dto.setMem_Name(rs.getString("mem_Name"));
				dto.setMem_Pwd(rs.getString("mem_Pwd"));
				dto.setMem_img(rs.getString("mem_img"));
				dto.setBirth(rs.getString("birth"));
				
				dto.setEmail(rs.getString("email"));
				String[] email = dto.getEmail().split("@");
				dto.setEmail1(email[0]);
				dto.setEmail2(email[1]);
				
				dto.setTel(rs.getString("tel"));
				String[] tel = dto.getTel().split("-");
				dto.setTel1(tel[0]);
				dto.setTel2(tel[1]);
				dto.setTel3(tel[2]);
				
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setCreated_Date(rs.getString("created"));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	public int insertMember(MemberDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			/*
			INSERT ALL
			INTO member(mem_Id, mem_Name, mem_Pwd, mem_img) VALUES ('admin', '관리자', 'admin', 'null')
			INTO member_info(mem_id, birth, email, tel, zip, addr1, addr2) VALUES ('admin', '2015-05-29', 'admin@koreahairband.com', '010-1234-5678', '12345', '서울특별시', '영등포구')
			SELECT * FROM DUAL;
			*/
			sb.append(" INSERT ALL ");
			sb.append(" INTO member(mem_Id, mem_Name, mem_Pwd, mem_img) ");
			sb.append(" VALUES (?, ?, ?, ?) ");
			sb.append(" INTO member_info(mem_id, birth, email, tel, zip, addr1, addr2) ");
			sb.append(" VALUES (?, ?, ?, ?, ?, ?, ?) ");
			sb.append(" SELECT * FROM DUAL ");
			
			pstmt=conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getMem_Id());
			pstmt.setString(2, dto.getMem_Name());
			pstmt.setString(3, dto.getMem_Pwd());
			pstmt.setString(4, dto.getMem_img());
			pstmt.setString(5, dto.getMem_Id());
			pstmt.setString(6, dto.getBirth());
			String email = dto.getEmail1()+"@"+dto.getEmail2();
			pstmt.setString(7, email);
			String tel = dto.getTel1()+"-"+dto.getTel2()+"-"+dto.getTel3();
			pstmt.setString(8, tel);
			pstmt.setString(9, dto.getZip());
			pstmt.setString(10, dto.getAddr1());
			pstmt.setString(11, dto.getAddr2());
			
			result=pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
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
	
	public int updateMember(MemberDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			/*
			UPDATE member_info SET birth = '2010-01-01', email = 'test2@test.com', tel = '010-9999-9999', zip = '54321', addr1 = '강원도', addr2 = '춘천시'
			WHERE mem_id = 'test';
			UPDATE member SET mem_Name = '테스트2', mem_Pwd = 'test2', mem_img = 'zzzz', modify_Date = SYSDATE
			WHERE mem_id = 'test';
			 */
			sb.append(" UPDATE member_info SET birth = ?, email = ?, tel = ?, zip = ?, addr1 = ?, addr2 = ? ");
			sb.append(" WHERE mem_id = ? ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getBirth());
			String email = dto.getEmail1()+"@"+dto.getEmail2();
			pstmt.setString(2, email);
			String tel = dto.getTel1()+"-"+dto.getTel2()+"-"+dto.getTel3();
			pstmt.setString(3, tel);
			pstmt.setString(4, dto.getZip());
			pstmt.setString(5, dto.getAddr1());
			pstmt.setString(6, dto.getAddr2());
			pstmt.setString(7, dto.getMem_Id());
			result = pstmt.executeUpdate();
			
			
			pstmt.close();
			sb.setLength(0);
			
						
			sb.append(" UPDATE member SET mem_Name = ?, mem_Pwd = ?, mem_img = ?, modify_Date = SYSDATE ");
			sb.append(" WHERE mem_id = ? ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getMem_Name());
			pstmt.setString(2, dto.getMem_Pwd());
			pstmt.setString(3, dto.getMem_img());
			pstmt.setString(4, dto.getMem_Id());
			result += pstmt.executeUpdate();
			
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
	
	public int deleteMember(String mem_Id) {
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			/*
			DELETE FROM member_info WHERE mem_Id = 'test'
			DELETE FROM member WHERE mem_Id = 'test'
			*/
			sb.append(" DELETE FROM member_info WHERE mem_Id = ? ");
			sb.append(" DELETE FROM member WHERE mem_Id = ? ");
			
			pstmt=conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, mem_Id);
			pstmt.setString(2, mem_Id);

			pstmt.executeUpdate();
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
}
