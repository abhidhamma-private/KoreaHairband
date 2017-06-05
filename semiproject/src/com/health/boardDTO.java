package com.health;

public class boardDTO {
	private String url;
	//글번호 매길때 쓰는것
	private int listNum;
	//베스트글
	private int likeCount;
	//히트아티클
	private int ranked;
	//글쓴사람수
	private int memsarticle;
	//리플수
	private int memsrepl;
	//포인트
	private int point;
	//보드네임
	private String boardname;
	
	//health1
	private int bbs_Num;
	private String mem_Id, mem_Name;
	private String subject, content;
	private String created;
	private int hitCount;
	private int groupNum, orderNo, depth, parent;
	
	
	//health1_file
	private int file_num;
	private String savefilename;
	
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int getBbs_Num() {
		return bbs_Num;
	}
	public void setBbs_Num(int bbs_Num) {
		this.bbs_Num = bbs_Num;
	}
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
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	public int getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
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
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getRanked() {
		return ranked;
	}
	public void setRanked(int ranked) {
		this.ranked = ranked;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getMemsarticle() {
		return memsarticle;
	}
	public int getMemsrepl() {
		return memsrepl;
	}
	public void setMemsarticle(int memsarticle) {
		this.memsarticle = memsarticle;
	}
	public void setMemsrepl(int memsrepl) {
		this.memsrepl = memsrepl;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getBoardname() {
		return boardname;
	}
	public void setBoardname(String boardname) {
		this.boardname = boardname;
	}
	
}
