package com.sitrack.alphabetSoup.dto;

public class CreateGameDTO {

	private Integer w;
	private Integer h;
	private Integer ltr;
	private Integer rtl;
	private Integer ttb;
	private Integer btt;
	private Integer d;

	private Integer now;
	
	public CreateGameDTO() {}

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public Integer getLtr() {
		return ltr;
	}

	public void setLtr(Integer ltr) {
		this.ltr = ltr;
	}

	public Integer getRtl() {
		return rtl;
	}

	public void setRtl(Integer rtl) {
		this.rtl = rtl;
	}

	public Integer getTtb() {
		return ttb;
	}

	public void setTtb(Integer ttb) {
		this.ttb = ttb;
	}

	public Integer getBtt() {
		return btt;
	}

	public void setBtt(Integer btt) {
		this.btt = btt;
	}

	public Integer getD() {
		return d;
	}

	public void setD(Integer d) {
		this.d = d;
	}

	public Integer getNow() {
		return this.now;
	}

	public void setNow(Integer now) {
		this.now = now;
	}
}
