package com.ynyes.lyz.interfaces.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ynyes.lyz.interfaces.entity.TdInfBaseEntity;

@Entity
public class TdReturnCouponInf extends TdInfBaseEntity
{
	//退货券头id
	@Column
	private Long rtHeaderId;
	
	//券行id（app唯一）
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lineId;
	
	//券类型（1.产品券，2.产品现金券，3.通用现金券，4.电子产品券，5.电子现金券）
	@Column
	private Long couponTypeId;
	
	//券对应的产品SKU
	@Column(length = 50)
	private String sku;
	
	//券的退回数量
	@Column
	private Long quantity;
	
	//使用面值价格
	//（1.只有产品券无价格面值，
	//2.通用现金券使用金额超过购买产品应收总金额，通用现金券金额传计算后的面值价格）
	@Column
	private Double price;
	
	//预留字段1
	@Column
	private String attribute1;
	
	//预留字段2
	@Column
	private String attribute2;
	
	//预留字段3
	@Column
	private String attribute3;
	
	//预留字段4
	@Column
	private String attribute4;
	
	//预留字段5
	@Column
	private String attribute5;
	
}
