package com.member;

// 세션에 저장할 객체의 클래스
public class SessionInfo {
	private String mem_Id, mem_Name;
	private String memRoll;
	public String getMem_Id() {
		return mem_Id;
	}
	public void setMem_Id(String mem_Id) {
		this.mem_Id = mem_Id;
	}
	public String getMem_Name() {
		return mem_Name;
	}
	public void setMem_Name(String mem_Name) {
		this.mem_Name = mem_Name;
	}
	public String getMemRoll() {
		return memRoll;
	}
	public void setMemRoll(String memRoll) {
		this.memRoll = memRoll;
	}
	
	
}
