package com.ynyes.lyz.interfaces.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class TdInfBaseEntity {
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date initDate;
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;
//	
//	
//	// N:未传送 Y:传送成功 F:传送失败
//	@Column(length=2)
//	private String sendFlag;
//	
//	// 日志
//	@Column
//	private String errorMsg;

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
//
//	public String getSendFlag() {
//		return sendFlag;
//	}
//
//	public void setSendFlag(String sendFlag) {
//		this.sendFlag = sendFlag;
//	}
	
}
