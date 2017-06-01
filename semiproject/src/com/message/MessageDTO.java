package com.message;

public class MessageDTO {
	private int message_num;
	private String mem_Id1 , mem_Id2 ,mem_Name ,content ,sendDate ,readDate ;
	private boolean check;
	
	
	public int getMessage_num() {
		return message_num;
	}
	public void setMessage_num(int message_num) {
		this.message_num = message_num;
	}
	public String getMem_Name() {
		return mem_Name;
	}
	public void setMem_Name(String mem_Name) {
		this.mem_Name = mem_Name;
	}
	public String getMem_Id1() {
		return mem_Id1;
	}
	public void setMem_Id1(String mem_Id1) {
		this.mem_Id1 = mem_Id1;
	}
	public String getMem_Id2() {
		return mem_Id2;
	}
	public void setMem_Id2(String mem_Id2) {
		this.mem_Id2 = mem_Id2;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getReadDate() {
		return readDate;
	}
	public void setReadDate(String readDate) {
		this.readDate = readDate;
	}
	
	
}
