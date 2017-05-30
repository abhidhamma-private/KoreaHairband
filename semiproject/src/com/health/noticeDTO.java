package com.health;

public class noticeDTO {
//bbs_num, mem_id, subject, content,file_num, savefilename
 //health2
private int bbs_num, hitcount;
private String mem_id,subject,content, created;
 //health2_file
private int file_num;
private String savefilename;


public int getHitcount() {
	return hitcount;
}
public void setHitcount(int hitcount) {
	this.hitcount = hitcount;
}
public String getCreated() {
	return created;
}
public void setCreated(String created) {
	this.created = created;
}
public int getBbs_num() {
	return bbs_num;
}
public void setBbs_num(int bbs_num) {
	this.bbs_num = bbs_num;
}
public String getMem_id() {
	return mem_id;
}
public void setMem_id(String mem_id) {
	this.mem_id = mem_id;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public int getFile_num() {
	return file_num;
}
public void setFile_num(int file_num) {
	this.file_num = file_num;
}
public String getSavefilename() {
	return savefilename;
}
public void setSavefilename(String savefilename) {
	this.savefilename = savefilename;
}
}
