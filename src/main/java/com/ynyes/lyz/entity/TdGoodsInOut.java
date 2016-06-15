package com.ynyes.lyz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 出退货明细报表
 * 增加门店id
 * @author zp
 *
 */
@Entity
public class TdGoodsInOut {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//门店名称
	@Column
	private String diySiteName;
	//主单号（订单显示XN开头，退单显示T开头）
	@Column
	private String mainOrderNumber;
	//分单号
	@Column
	private String orderNumber;
	//订单状态
	@Column
	private Long statusId;
	//订单日期（下单日期）
	@Column
	private Date orderTime;
	//销售日期（订单为出货日期，退单为验货确认日期）
	@Column
	private Date salesTime;
	//客户名称
	@Column
	private String realName;
	//客户电话
	@Column
	private String username;
	//品牌
	@Column
	private String brandTitle;
	//商品类别
	@Column
	private String categoryTitle;
	//导购
	@Column
	private String sellerRealName;
	//配送方式
	@Column
	private String deliverTypeTitle;
	//产品编号
	@Column
	private String sku;
	//产品名称
	@Column
	private String goodsTitle;
	//数量
	@Column
	private Long quantity;
	//单价
	@Column
	private Double price;
	//总价
	@Column
	private Double totalPrice;
	//中转仓
	@Column
	private String whNo;
	//配送人员
	@Column
	private String deliverRealName;
	//配送人电话
	@Column
	private String deliverUsername;
	//退货人姓名
	@Column
	private String shippingName;
	//退货人电话
	@Column
	private String shippingPhone;
	//退货地址
	@Column
	private String shippingAddress;
	//客户备注
	@Column
	private String remarkInfo;
	//门店code
	@Column
	private String diySiteCode;
	//城市名称
	@Column
	private String cityName;
	//创建用户
	@Column
	private String createUsername;
	//门店id
	@Column
	private Long diyId;
	
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
	public String getMainOrderNumber() {
		return mainOrderNumber;
	}
	public void setMainOrderNumber(String mainOrderNumber) {
		this.mainOrderNumber = mainOrderNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getSalesTime() {
		return salesTime;
	}
	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBrandTitle() {
		return brandTitle;
	}
	public void setBrandTitle(String brandTitle) {
		this.brandTitle = brandTitle;
	}
	public String getCategoryTitle() {
		return categoryTitle;
	}
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}
	public String getSellerRealName() {
		return sellerRealName;
	}
	public void setSellerRealName(String sellerRealName) {
		this.sellerRealName = sellerRealName;
	}
	public String getDeliverTypeTitle() {
		return deliverTypeTitle;
	}
	public void setDeliverTypeTitle(String deliverTypeTitle) {
		this.deliverTypeTitle = deliverTypeTitle;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getGoodsTitle() {
		return goodsTitle;
	}
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getWhNo() {
		return whNo;
	}
	public void setWhNo(String whNo) {
		this.whNo = whNo;
	}
	public String getDeliverRealName() {
		return deliverRealName;
	}
	public void setDeliverRealName(String deliverRealName) {
		this.deliverRealName = deliverRealName;
	}
	public String getDeliverUsername() {
		return deliverUsername;
	}
	public void setDeliverUsername(String deliverUsername) {
		this.deliverUsername = deliverUsername;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getRemarkInfo() {
		return remarkInfo;
	}
	public void setRemarkInfo(String remarkInfo) {
		this.remarkInfo = remarkInfo;
	}
	public String getDiySiteCode() {
		return diySiteCode;
	}
	public void setDiySiteCode(String diySiteCode) {
		this.diySiteCode = diySiteCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	
}
