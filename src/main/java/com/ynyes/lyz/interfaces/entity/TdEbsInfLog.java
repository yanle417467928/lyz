package com.ynyes.lyz.interfaces.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TdEbsInfLog extends TdInfBaseEntity
{
	public static final Integer SEND_SUCCEED = 0; //发送成功
	public static final Integer SEND_FAILURE = 1; //发送失败
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String errorInfo;
	
	@Column(length = 10)
	private String sendFlag;
	
	@Column(length = 20)
	private String tableName;
	
	@Column
	private String orderNumber;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getErrorInfo()
	{
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo)
	{
		this.errorInfo = errorInfo;
	}

	public String getSendFlag()
	{
		return sendFlag;
	}

	public void setSendFlag(String sendFlag)
	{
		this.sendFlag = sendFlag;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString()
	{
		return "TdEbsInfLog [id=" + id + ", errorInfo=" + errorInfo + ", sendFlag=" + sendFlag + ", tableName="
		        + tableName + ", orderNumber=" + orderNumber + "]";
	} 
	
}
