package com.ynyes.lyz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 代收款报表（1.1）
 * @author zp
 *
 */
@Entity
public class TdAgencyFund {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//门店名称
	@Column
	private String diySiteName;
	//门店电话（1.1不需要显示）
	@Column
	private String diySitePhone;
	//主单号
	@Column
	private String mainOrderNumber;
	//订单状态
	@Column
	private Long statusId;
	//订单时间
	@Column
	private Date orderTime;
	
	//可提现金额（1.1不需要显示）
	@Column(scale = 2)
	private Double cashBalanceUsed;
	//不可提现金额（1.1不需要显示）
	@Column(scale = 2)
	private Double unCashBalanceUsed;
	//代收款金额
	@Column(scale = 2)
	private Double payPrice;
	//实际代收款金额
	@Column(scale = 2)
	private Double payed;
	//欠款
	@Column(scale = 2)
	private Double owned;
	//仓库名称（1.1修改字段）
	@Column
	private String whName;
	//配送人员(1.1修改字段)
	@Column
	private String deliveryName;
	//配送人电话(1.1修改字段)
	@Column
	private String deliveryPhone;
	//收获人
	@Column
	private String shippingName;
	//收货人电话
	@Column
	private String shippingPhone;
	//收货人地址
	@Column
	private String shippingAddress;
	//备注信息
	@Column
	private String remark;
	//现金卷额度（1.1不需要显示）
	@Column
	private Double cashCoupon;
	//订单总金额（1.1不需要显示）
	@Column(scale = 2)
	private Double totalPrice;
	//预约配送日期
	@Column
	private String deliveryDate;
	//预约配送时间段
	@Column
	private Long deliveryDetailId;
	//实际配送时间
	@Column
	private Date deliveryTime;
	// 城市（1.1不需要显示）
	@Column
	private String cityName;
	// 配送门店id（1.1不需要显示）
	@Column
	private String diySiteCode;
	// 配送方式名称（1.1不需要显示）
	@Column
	private String deliverTypeTitle;
	//创建人
	private String createUsername;
	//门店id（1.1不需要显示）
	@Column
	private Long diyId;
	
	//-------------------1.1版本添加字段---------------------
	//归属导购姓名
	@Column
	private String sellerName;
	// 归属销顾电话（推荐人电话）
	@Column
	private String sellerPhone;
	//客户姓名
	@Column
	private String userName;
	// 客户电话
	@Column
	private String userPhone;
	// 收款现金
	@Column
	private Double payMoney;
	// 收款pos
	@Column
	private Double payPos;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDiySiteName() {
		return diySiteName;
	}
	public void setDiySiteName(String diySiteName) {
		this.diySiteName = diySiteName;
	}
	public String getDiySitePhone() {
		return diySitePhone;
	}
	public void setDiySitePhone(String diySitePhone) {
		this.diySitePhone = diySitePhone;
	}
	public String getMainOrderNumber() {
		return mainOrderNumber;
	}
	public void setMainOrderNumber(String mainOrderNumber) {
		this.mainOrderNumber = mainOrderNumber;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Double getCashBalanceUsed() {
		return cashBalanceUsed;
	}
	public void setCashBalanceUsed(Double cashBalanceUsed) {
		this.cashBalanceUsed = cashBalanceUsed;
	}
	public Double getUnCashBalanceUsed() {
		return unCashBalanceUsed;
	}
	public void setUnCashBalanceUsed(Double unCashBalanceUsed) {
		this.unCashBalanceUsed = unCashBalanceUsed;
	}
	public Double getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}
	public Double getPayed() {
		return payed;
	}
	public void setPayed(Double payed) {
		this.payed = payed;
	}
	public Double getOwned() {
		return owned;
	}
	public void setOwned(Double owned) {
		this.owned = owned;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getShippingPhone() {
		return shippingPhone;
	}
	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getCashCoupon() {
		return cashCoupon;
	}
	public void setCashCoupon(Double cashCoupon) {
		this.cashCoupon = cashCoupon;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}
	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDiySiteCode() {
		return diySiteCode;
	}
	public void setDiySiteCode(String diySiteCode) {
		this.diySiteCode = diySiteCode;
	}
	public String getDeliverTypeTitle() {
		return deliverTypeTitle;
	}
	public void setDeliverTypeTitle(String deliverTypeTitle) {
		this.deliverTypeTitle = deliverTypeTitle;
	}
	public String getCreateUsername() {
		return createUsername;
	}
	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}
	public Long getDiyId() {
		return diyId;
	}
	public void setDiyId(Long diyId) {
		this.diyId = diyId;
	}
	public String getWhName() {
		return whName;
	}
	public void setWhName(String whName) {
		this.whName = whName;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDeliveryPhone() {
		return deliveryPhone;
	}
	public void setDeliveryPhone(String deliveryPhone) {
		this.deliveryPhone = deliveryPhone;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerPhone() {
		return sellerPhone;
	}
	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public Double getPayPos() {
		return payPos;
	}
	public void setPayPos(Double payPos) {
		this.payPos = payPos;
	}
	
	
}
