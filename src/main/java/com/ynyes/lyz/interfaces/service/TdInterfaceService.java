package com.ynyes.lyz.interfaces.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdCoupon;
import com.ynyes.lyz.entity.TdDiySite;
import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.entity.TdOrderGoods;
import com.ynyes.lyz.entity.TdPayType;
import com.ynyes.lyz.entity.TdReturnNote;
import com.ynyes.lyz.interfaces.entity.TdCashReciptInf;
import com.ynyes.lyz.interfaces.entity.TdCashRefundInf;
import com.ynyes.lyz.interfaces.entity.TdOrderCouponInf;
import com.ynyes.lyz.interfaces.entity.TdOrderGoodsInf;
import com.ynyes.lyz.interfaces.entity.TdOrderInf;
import com.ynyes.lyz.interfaces.entity.TdOrderReceiveInf;
import com.ynyes.lyz.interfaces.entity.TdReturnCouponInf;
import com.ynyes.lyz.interfaces.entity.TdReturnGoodsInf;
import com.ynyes.lyz.interfaces.entity.TdReturnOrderInf;
import com.ynyes.lyz.interfaces.entity.TdReturnTimeInf;
import com.ynyes.lyz.interfaces.utils.EnumUtils.INFTYPE;
import com.ynyes.lyz.interfaces.utils.InterfaceConfigure;
import com.ynyes.lyz.interfaces.utils.StringTools;
import com.ynyes.lyz.service.TdCouponService;
import com.ynyes.lyz.service.TdDiySiteService;
import com.ynyes.lyz.service.TdOrderService;
import com.ynyes.lyz.service.TdPayTypeService;


@Service
@Transactional
public class TdInterfaceService {

	@Autowired
	private TdOrderInfService tdOrderInfService;
	
	@Autowired
	private TdOrderGoodsInfService tdOrderGoodsInfService;
	
	@Autowired
	private TdDiySiteService tdDiySiteService;
	
	@Autowired
	private TdPayTypeService tdPayTypeService;
	
	@Autowired
	private TdCouponService tdCouponService;
	
	@Autowired
	private TdOrderCouponInfService tdOrderCouponInfService;
	
	@Autowired
	private TdOrderReceiveInfService tdOrderReceiveInfService;
	
	@Autowired
	private TdReturnOrderInfService tdReturnOrderInfService;
	
	@Autowired
	private TdReturnGoodsInfService TdReturnGoodsInfService;
	
	@Autowired
	private TdReturnTimeInfService tdReturnTimeInfService;
	
	@Autowired
	private TdOrderService tdOrderService;
	
	static Call call;
	
	/**
	 * 访问WebService的服务端
	 * 
	 * @return Call
	 */
	public static Call getCall()
	{
		if (call != null) 
		{
			return call;
		}
		try 
		{
//			String urlname = "http://erptest.zghuarun.com:8030/webservices/SOAProvider/plsql/cux_app_webservice_pkg/?wsdl";
//			String urlname = getEbsWebServiceUrlByLocalHost();

			String AUTH_PREFIX = "wsse";
			String AUTH_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
			SOAPFactory soapFactory =SOAPFactory.newInstance();
			SOAPElement wsSecHeaderElm = soapFactory.createElement("Security", AUTH_PREFIX, AUTH_NS);
			SOAPElement userNameTokenElm = soapFactory.createElement("UsernameToken",AUTH_PREFIX, AUTH_NS);
			SOAPElement userNameElm = soapFactory.createElement("Username",AUTH_PREFIX, AUTH_NS);
			SOAPElement passwdElm = soapFactory.createElement("Password",AUTH_PREFIX, AUTH_NS);
			passwdElm.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");

			userNameElm.addTextNode("runpeng");
			passwdElm.addTextNode("A11111");

			userNameTokenElm.addChildElement(userNameElm);
			userNameTokenElm.addChildElement(passwdElm);
			wsSecHeaderElm.addChildElement(userNameTokenElm);
			SOAPHeaderElement soapHeaderElement =  new SOAPHeaderElement(wsSecHeaderElm);
//			soapHeaderElement.setMustUnderstand(true);
			org.apache.axis.client.Service s = new  org.apache.axis.client.Service();
			call = (Call) s.createCall();
//			call.setTimeout(new Integer(5000));
			QName EBSQName = new QName("http://xmlns.oracle.com/apps/cux/soaprovider/plsql/cux_app_webservice_pkg/get_xml/", "GET_XML");
			call.setOperationName(EBSQName);
			call.setTimeout(100000);
			call.setEncodingStyle("UTF-8");
			call.setTargetEndpointAddress(InterfaceConfigure.getEBS_WS_URL());
			call.setReturnType(XMLType.XSD_STRING);
			call.addHeader(soapHeaderElement);
			QName TableQName = new QName("STRTABLE");
			QName TypeQName = new QName("STRTYPE");
			QName XMLQName = new QName("XML");
			call.addParameter(TableQName, XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter(TypeQName, XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter(XMLQName, XMLType.XSD_STRING, ParameterMode.IN);
//			Object[] fn01 = {"TD_ORDER","1","<ERP><TABLE><SOB_ID>2033</SOB_ID><ORDER_HEADER_ID>13</ORDER_HEADER_ID><ORDER_NUMBER>YR20160519105514037938</ORDER_NUMBER><ORDER_DATE>2016-05-19 10:55:14.0</ORDER_DATE><MAIN_ORDER_NUMBER>XN20160519105514037938</MAIN_ORDER_NUMBER><PRODUCT_TYPE>YR</PRODUCT_TYPE><ORDER_TYPE_ID>4</ORDER_TYPE_ID><USERID>16670</USERID><USERNAME>MDJ</USERNAME><USERPHONE>18523633631</USERPHONE><DIY_SITE_ID>33</DIY_SITE_ID><DIY_SITE_CODE>1001</DIY_SITE_CODE><DIY_SITE_NAME>第大店</DIY_SITE_NAME><DIY_SITE_PHONE>13283889821</DIY_SITE_PHONE><PROVINCE></PROVINCE><CITY>郑州市</CITY><DISCTRICT>二七区</DISCTRICT><SHIPPING_NAME>MDJ</SHIPPING_NAME><SHIPPING_PHONE>18523633631</SHIPPING_PHONE><DELIVER_TYPE_TITLE>送货上门</DELIVER_TYPE_TITLE><ISONLINEPAY>N</ISONLINEPAY><PAY_TYPE>货到付款</PAY_TYPE><PAY_DATE>2016-05-19 10:55:19.0</PAY_DATE><PAY_AMT>0.0</PAY_AMT><PREPAY_AMT>0.0</PREPAY_AMT><REC_AMT>17.0</REC_AMT><ATTRIBUTE1></ATTRIBUTE1><ATTRIBUTE2></ATTRIBUTE2><ATTRIBUTE3></ATTRIBUTE3><ATTRIBUTE4></ATTRIBUTE4><ATTRIBUTE5></ATTRIBUTE5></TABLE></ERP>"};
//			String val = (String)call.invoke(fn01);
//			System.out .println( "getSecurityToken(correct):"  + val);
//			Object[] fn02 = { "john" , "john2" , null ,null };
//			String va2 = (String)call.invoke(fn02);
//			System.out .println( "getSecurityToken(wrong):"  + va2);
			return call;
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	
/**     -=-=-=-=-=-=-     生成实体类       -=-=-=-=-=-=-       **/
	
	/**
	 * 根据订单生成销售订单相关数据
	 * @param tdOrder
	 */
	public void initOrderInf(TdOrder tdOrder)
	{
		if (tdOrder == null)
		{
			return ;
		}
		TdOrderInf orderInf = tdOrderInfService.findByOrderNumber(tdOrder.getOrderNumber());
		if (orderInf != null) 
		{
			return;
		}
		orderInf = new TdOrderInf();
		TdDiySite diySite = tdDiySiteService.findOne(tdOrder.getDiySiteId());
		if (diySite != null)
		{
			orderInf.setSobId(diySite.getRegionId());
		}
		
//		orderInf.setHeaderId(tdOrder.getId());
		orderInf.setOrderNumber(tdOrder.getOrderNumber());
		orderInf.setOrderDate(tdOrder.getOrderTime());
		orderInf.setMainOrderNumber(tdOrder.getMainOrderNumber());
		orderInf.setProductType(StringTools.getProductStrByOrderNumber(tdOrder.getOrderNumber()));
		orderInf.setOrderTypeId(4);
		orderInf.setUserid(tdOrder.getRealUserId());
		orderInf.setUsername(tdOrder.getRealUserRealName());
		orderInf.setUserphone(tdOrder.getRealUserUsername());
		orderInf.setDiySiteId(tdOrder.getDiySiteId());
		orderInf.setDiySiteCode(tdOrder.getDiySiteCode());
		orderInf.setDiySiteName(tdOrder.getDiySiteName());
		orderInf.setDiySitePhone(tdOrder.getDiySitePhone());
		orderInf.setProvince(tdOrder.getProvince());
		orderInf.setCity(tdOrder.getCity());
		orderInf.setDisctrict(tdOrder.getDisctrict());
		orderInf.setShippingName(tdOrder.getShippingName());
		orderInf.setShippingPhone(tdOrder.getShippingPhone());
		orderInf.setDeliverTypeTitle(tdOrder.getDeliverTypeTitle());
		orderInf.setIsonlinepay(booleanStrByPayTypeId(tdOrder.getPayTypeId()));
		orderInf.setPayType(tdOrder.getPayTypeTitle());
		orderInf.setPayDate(tdOrder.getPayTime());
		orderInf.setPayAmt(booleanByStr(orderInf.getIsonlinepay())?tdOrder.getTotalPrice():0);
		orderInf.setPrepayAmt(tdOrder.getCashBalanceUsed() + tdOrder.getUnCashBalanceUsed());
		orderInf.setRecAmt(tdOrder.getTotalPrice());
		tdOrderInfService.save(orderInf);
		
		
		//商品TdOrderGoodsInf
		List<TdOrderGoods> goodsList = tdOrder.getOrderGoodsList();
		if (goodsList != null && goodsList.size() > 0)
		{
			for (TdOrderGoods tdOrderGoods : goodsList)
			{
				TdOrderGoodsInf goodsInf = new TdOrderGoodsInf();
				goodsInf.setOrderHeaderId(orderInf.getHeaderId());
				goodsInf.setGoodsId(tdOrderGoods.getId());
				goodsInf.setGoodsTitle(tdOrderGoods.getGoodsTitle());
				goodsInf.setGoodsSubTitle(tdOrderGoods.getGoodsSubTitle());
				goodsInf.setSku(tdOrderGoods.getSku());
				goodsInf.setQuantity(tdOrderGoods.getQuantity());
//				goodsInf.setJxPrice(tdOrderGoods.getRealPrice());  //针对JX商：b2c到店自提不传，
				goodsInf.setLsPrice(tdOrderGoods.getPrice());
				goodsInf.setGiftFlag("N");
				goodsInf.setPromotion(tdOrderGoods.getActivityId());
				tdOrderGoodsInfService.save(goodsInf);
			}
		}
		//小辅料TdOrderGoodsInf
		List<TdOrderGoods> gifList = tdOrder.getGiftGoodsList();
		if (gifList != null && gifList.size() > 0)
		{
			for (TdOrderGoods tdOrderGoods : gifList)
			{
				TdOrderGoodsInf goodsInf = new TdOrderGoodsInf();
				goodsInf.setOrderHeaderId(orderInf.getHeaderId());
				goodsInf.setGoodsId(tdOrderGoods.getId());
				goodsInf.setGoodsTitle(tdOrderGoods.getGoodsTitle());
				goodsInf.setGoodsSubTitle(tdOrderGoods.getGoodsSubTitle());
				goodsInf.setSku(tdOrderGoods.getSku());
				goodsInf.setQuantity(tdOrderGoods.getQuantity());
//				goodsInf.setJxPrice(tdOrderGoods.getRealPrice());
				goodsInf.setLsPrice(tdOrderGoods.getPrice());
				goodsInf.setGiftFlag("Y");
				goodsInf.setPromotion(tdOrderGoods.getActivityId());
				tdOrderGoodsInfService.save(goodsInf);
			}
		}
		//小辅料TdOrderGoodsInf
		List<TdOrderGoods> presentedList = tdOrder.getPresentedList();
		if (presentedList != null && presentedList.size() > 0) 
		{
			for (TdOrderGoods tdOrderGoods : presentedList)
			{
				TdOrderGoodsInf goodsInf = new TdOrderGoodsInf();
				goodsInf.setOrderHeaderId(orderInf.getHeaderId());
				goodsInf.setGoodsId(tdOrderGoods.getId());
				goodsInf.setGoodsTitle(tdOrderGoods.getGoodsTitle());
				goodsInf.setGoodsSubTitle(tdOrderGoods.getGoodsSubTitle());
				goodsInf.setSku(tdOrderGoods.getSku());
				goodsInf.setQuantity(tdOrderGoods.getQuantity());
//				goodsInf.setJxPrice(tdOrderGoods.getRealPrice());
				goodsInf.setLsPrice(tdOrderGoods.getGiftPrice());
				goodsInf.setGiftFlag("Y");
				goodsInf.setPromotion(tdOrderGoods.getActivityId());
				tdOrderGoodsInfService.save(goodsInf);
			}
		}
		
		String cashCouponId = tdOrder.getCashCouponId();
		String productCouponId = tdOrder.getProductCouponId();
		String couponIdsStr = cashCouponId + productCouponId;
		String[] couponIds = couponIdsStr.split(",");
		for (String string : couponIds)
		{
			if (org.apache.commons.lang3.StringUtils.isNotBlank(string))
			{
				long couponId = Long.parseLong(string);
				TdCoupon tdCoupon = tdCouponService.findOne(couponId);
				if (tdCoupon != null)
				{
					//优惠券
					TdOrderCouponInf couponInf = new TdOrderCouponInf();
					couponInf.setOrderHeaderId(orderInf.getHeaderId());
					couponInf.setCouponTypeId(StringTools.coupontypeWithCoupon(tdCoupon));
					couponInf.setSku(tdCoupon.getSku());
					couponInf.setQuantity(1L);
					couponInf.setPrice(tdCoupon.getRealPrice());
					couponInf.setHistoryFlag(StringTools.getHistoryFlagByCouponType(tdCoupon.getTypeCategoryId()));
					tdOrderCouponInfService.save(couponInf);
				}
			}
		}
	}
	
	/**
	 * 生成销售单的收货时间，收到货的时间
	 * @param tdOrder
	 * @return
	 */
	public TdOrderReceiveInf initOrderReceiveByOrder(TdOrder tdOrder)
	{
		TdOrderReceiveInf orderReceiveInf = new TdOrderReceiveInf();
		if (tdOrder == null || tdOrder.getDeliverTypeTitle() == null)
		{
			return null;
		}
		if(tdOrder.getDeliverTypeTitle().equalsIgnoreCase("门店自提"))
		{
			TdDiySite diySite= tdDiySiteService.findOne(tdOrder.getDiySiteId());
			if(diySite!=null){
				orderReceiveInf.setSobId(diySite.getRegionId());
			}
			TdOrderInf tdOrderInf = tdOrderInfService.findByOrderNumber(tdOrder.getOrderNumber());
			if (tdOrderInf != null) 
			{
				orderReceiveInf.setHeaderId(tdOrderInf.getHeaderId());
			}
			orderReceiveInf.setOrderNumber(tdOrder.getOrderNumber());
			orderReceiveInf.setReceiveDate(new Date());
			orderReceiveInf.setDeliverTypeTitle("门店自提");
			return tdOrderReceiveInfService.save(orderReceiveInf);
		}
		return null;
	}
	
	public TdCashReciptInf initCashReciptByOrder(TdOrder tdOrder)
	{
		if (tdOrder == null)
		{
			return null;
		}
		TdCashReciptInf cashReciptInf = new TdCashReciptInf();
//		cashReciptInf.setSobId();
//		cashReciptInf.setReceiptId();
//		cashReciptInf.setReceiptNumber();
//		cashReciptInf.setUserid();
//		cashReciptInf.setUsername();
//		cashReciptInf.setUserphone();
//		cashReciptInf.setDiySiteCode();
//		cashReciptInf.setReceiptClass();
//		cashReciptInf.setOrderHeaderId();
//		cashReciptInf.setOrderNumber();
//		cashReciptInf.setProductType();
//		cashReciptInf.setReceiptType();
//		cashReciptInf.setReceiptDate();
//		cashReciptInf.setAmount();

		return cashReciptInf;
	}
	
	/**
	 * 到店退货单退货时间表,收到货的时间
	 * @param returnNote
	 * @return
	 */
	public TdReturnTimeInf initReturnTimeByReturnNote(TdReturnNote returnNote)
	{
		TdReturnOrderInf returnOrderInf = tdReturnOrderInfService.findByReturnNumber(returnNote.getReturnNumber());
		if (returnOrderInf == null)
		{
			return null;
		}
		TdReturnTimeInf timeInf = new TdReturnTimeInf();
		TdDiySite diySite= tdDiySiteService.findOne(returnNote.getDiySiteId());
		if(diySite!=null){
			timeInf.setSobId(diySite.getRegionId());
		}
		timeInf.setRtHeaderId(returnOrderInf.getRtHeaderId());
		timeInf.setReturnNumber(returnOrderInf.getOrderNumber());
		timeInf.setReturnDate(new Date());
		return tdReturnTimeInfService.save(timeInf);
	}
	
	/**
	 * 根据退货单生成相应的销退单
	 * @param returnNote
	 */
	public void initReturnOrder(TdReturnNote returnNote)
	{
		if(returnNote==null){
			return;
		}

		TdReturnOrderInf returnOrderInf = tdReturnOrderInfService.findByReturnNumber(returnNote.getReturnNumber());
		TdOrderInf tdOrderInf = tdOrderInfService.findByOrderNumber(returnNote.getOrderNumber());
		if (returnOrderInf != null || tdOrderInf == null)
		{
			return ;
		}

		returnOrderInf = new TdReturnOrderInf();
		TdDiySite diySite= tdDiySiteService.findOne(returnNote.getDiySiteId());
		if(diySite!=null){
			returnOrderInf.setSobId(diySite.getRegionId());
		}
//		returnOrderInf.setRtHeaderId(returnNote);
		
		returnOrderInf.setReturnNumber(returnNote.getReturnNumber());
		returnOrderInf.setReturnDate(returnNote.getOrderTime());
		TdOrder order= tdOrderService.findByOrderNumber(returnNote.getOrderNumber());
		if(order!=null){
			//是否整单退
			Boolean isFull=true;
			for (TdOrderGoods orderGood : order.getOrderGoodsList()) {
				//是否退货
				Boolean isReturn=false;
				for(TdOrderGoods returnGood : returnNote.getReturnGoodsList()){
					if(orderGood.getSku().equals(returnGood.getSku()) && orderGood.getQuantity().equals(returnGood.getQuantity())){
						isReturn=true;
					}
				}
				if(!isReturn){
					isFull=false;
					break;
				}
			}
			if(isFull){
				returnOrderInf.setRtFullFlag("Y");
			}else{
				returnOrderInf.setRtFullFlag("N");
			}
			returnOrderInf.setOrderHeaderId(tdOrderInf.getHeaderId());
		}
		
		
		returnOrderInf.setOrderNumber(returnNote.getOrderNumber());
		returnOrderInf.setProdectType(StringTools.getProductStrByOrderNumber(returnNote.getOrderNumber()));
		returnOrderInf.setDiySiteCode(returnNote.getDiyCode());
//		returnOrderInf.setRefundType(returnNote);
		returnOrderInf.setAuditDate(returnNote.getCheckTime());
		returnOrderInf.setRefundAmount(returnNote.getTurnPrice());
		returnOrderInf.setPrepayAmt(returnNote.getTurnPrice());
		tdReturnOrderInfService.save(returnOrderInf);
		//退货单商品
		List<TdOrderGoods> goodsList = returnNote.getReturnGoodsList();
		if (goodsList != null && goodsList.size() > 0)
		{
			for (TdOrderGoods tdOrderGoods : goodsList) 
			{
				TdReturnGoodsInf goodsInf = new TdReturnGoodsInf();
				goodsInf.setRtHeaderId(returnOrderInf.getRtHeaderId());
				goodsInf.setSku(tdOrderGoods.getSku());
				goodsInf.setQuantity(tdOrderGoods.getQuantity());
//				goodsInf.setJxPrice(tdOrderGoods.g());
				goodsInf.setLsPrice(tdOrderGoods.getReturnUnitPrice());
				TdReturnGoodsInfService.save(goodsInf);
			}
		}
		
//		TdReturnCouponInf tdReturnCouponInf =new TdReturnCouponInf();
		
//		tdReturnCouponInf.setRtHeaderId(rtHeaderId);
	}
	
	/**
	 * 根据订单生成退货时的优惠券
	 * @param tdOrder
	 * @param type
	 */
	public void initReturnCouponInfByOrder(TdOrder tdOrder,Integer type)
	{
		// type 0:取消订单 1:其他退货
		TdOrderInf tdOrderInf = tdOrderInfService.findByOrderNumber(tdOrder.getOrderNumber());
		TdReturnOrderInf returnOrderInf = tdReturnOrderInfService.findByOrderNumber(tdOrder.getOrderNumber());
		if (tdOrderInf == null || returnOrderInf == null)
		{
			return ;
		}
		List<TdOrderCouponInf> couponInfs = tdOrderCouponInfService.findByorderHeaderId(tdOrderInf.getHeaderId());
		if (type == 0) 
		{
			if (couponInfs != null && couponInfs.size() >0)
			{
				for (TdOrderCouponInf tdOrderCouponInf : couponInfs) 
				{
					TdReturnCouponInf returnCouponInf = new TdReturnCouponInf();
					returnCouponInf.setRtHeaderId(returnOrderInf.getRtHeaderId());
					returnCouponInf.setCouponTypeId(tdOrderCouponInf.getCouponTypeId());
					returnCouponInf.setSku(tdOrderCouponInf.getSku());
					returnCouponInf.setPrice(tdOrderCouponInf.getPrice());
					returnCouponInf.setQuantity(1L);
				}
			}
		}
		else if (type == 1)
		{
			List<TdCoupon> coupons = tdCouponService.findByTypeIdAndOrderId(3l, tdOrder.getId());
			if (coupons != null && coupons.size() > 0)
			{
				for (TdCoupon tdCoupon : coupons) 
				{
					TdReturnCouponInf returnCouponInf = new TdReturnCouponInf();
					returnCouponInf.setRtHeaderId(returnOrderInf.getRtHeaderId());
					returnCouponInf.setCouponTypeId(StringTools.coupontypeWithCoupon(tdCoupon));
					returnCouponInf.setSku(tdCoupon.getSku());
					returnCouponInf.setPrice(tdCoupon.getPrice());
					returnCouponInf.setQuantity(1L);
				}
			}
		}
	}
	
	public Boolean booleanByStr(String YN)
	{
		if (YN == null)
		{
			return false;
		}
		if (YN.equalsIgnoreCase("Y")) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String booleanStrByPayTypeId(Long payTypeId)
	{
		if (payTypeId == null)
		{
			return null;
		}
		TdPayType payType = tdPayTypeService.findOne(payTypeId);
		if (payType != null && payType.getIsOnlinePay() == true)
		{
			return "Y";
		}
		return "N";
	}
	
	
	/**     -=-=-=-=-=-=-     生成XML       -=-=-=-=-=-=-       **/
	
	public String XmlByOrder(TdOrder tdOrder, INFTYPE type)
	{
		TdOrderInf tdOrderInf = tdOrderInfService.findByOrderNumber(tdOrder.getOrderNumber());
		List<String> stringList = new ArrayList<String>();
		switch (type)
		{
		case ORDERINF :
		{
			String orderInfXml = XMLWithEntity(tdOrderInf, INFTYPE.ORDERINF);
			stringList.add(orderInfXml);
			break;
		}
		case ORDERGOODSINF :
		{
			List<TdOrderGoodsInf> goodsInfs = tdOrderGoodsInfService.findByOrderHeaderId(tdOrderInf.getHeaderId());
			for (TdOrderGoodsInf tdOrderGoodsInf : goodsInfs) 
			{
				String goodsInfXml = XMLWithEntity(tdOrderGoodsInf, INFTYPE.ORDERGOODSINF);
				if (org.apache.commons.lang3.StringUtils.isNotBlank(goodsInfXml))
				{
					stringList.add(goodsInfXml);
				}
			}
			break;
		}
		case ORDERCOUPONINF :
		{
			List<TdOrderCouponInf> couponInfs = tdOrderCouponInfService.findByorderHeaderId(tdOrderInf.getHeaderId());
			for (TdOrderCouponInf tdOrderCouponInf : couponInfs) 
			{
				String couponInfXml = XMLWithEntity(tdOrderCouponInf, INFTYPE.ORDERCOUPONINF);
				if (org.apache.commons.lang3.StringUtils.isNotBlank(couponInfXml))
				{
					stringList.add(couponInfXml);
				}
			}
			break;
		}
		default:
			break;
		}
		return XmlWithTables(stringList);
		
	}
	
	/**
	 * 生成xml的table
	 * @param entity
	 * @param type
	 * @return
	 */
	public String XMLWithEntity(Object entity,INFTYPE type)
	{
		String xml = null;
		if (entity == null)
		{
			return "XMLWITHENTITY:null";
		}
		switch (type)
		{
		case ORDERINF:
		{
			TdOrderInf object = (TdOrderInf)entity;
			xml =   "<TABLE><SOB_ID>" + object.getSobId() + "</SOB_ID>"
					+ "<ORDER_HEADER_ID>" + object.getHeaderId() + "</ORDER_HEADER_ID>"
					+ "<ORDER_NUMBER>" + object.getOrderNumber() + "</ORDER_NUMBER>"
					+ "<ORDER_DATE>" + object.getOrderDate() + "</ORDER_DATE>"
					+ "<MAIN_ORDER_NUMBER>" + object.getMainOrderNumber() + "</MAIN_ORDER_NUMBER>"
					+ "<PRODUCT_TYPE>" + object.getProductType() + "</PRODUCT_TYPE>"
					+ "<ORDER_TYPE_ID>" + object.getOrderTypeId() + "</ORDER_TYPE_ID>"
					+ "<USERID>" + object.getUserid() + "</USERID>"
					+ "<USERNAME>" + object.getUsername() + "</USERNAME>"
					+ "<USERPHONE>" + object.getUserphone() + "</USERPHONE>"
					+ "<DIY_SITE_ID>" + object.getDiySiteId() + "</DIY_SITE_ID>"
					+ "<DIY_SITE_CODE>" + object.getDiySiteCode() + "</DIY_SITE_CODE>"
					+ "<DIY_SITE_NAME>" + object.getDiySiteName() + "</DIY_SITE_NAME>"
					+ "<DIY_SITE_PHONE>" + object.getDiySitePhone() + "</DIY_SITE_PHONE>"
					+ "<PROVINCE>" + object.getProvince() + "</PROVINCE>"
					+ "<CITY>" + object.getCity() + "</CITY>"
					+ "<DISCTRICT>" + object.getDisctrict() + "</DISCTRICT>"
					+ "<SHIPPING_NAME>" + object.getShippingName() + "</SHIPPING_NAME>"
					+ "<SHIPPING_PHONE>" + object.getShippingPhone() + "</SHIPPING_PHONE>"
					+ "<DELIVER_TYPE_TITLE>" + object.getDeliverTypeTitle() + "</DELIVER_TYPE_TITLE>"
					+ "<ISONLINEPAY>" + object.getIsonlinepay() + "</ISONLINEPAY>"
					+ "<PAY_TYPE>" + object.getPayType() + "</PAY_TYPE>"
					+ "<PAY_DATE>" + object.getPayDate() + "</PAY_DATE>"
					+ "<PAY_AMT>" + object.getPayAmt() + "</PAY_AMT>"
					+ "<PREPAY_AMT>" + object.getPrepayAmt() + "</PREPAY_AMT>"
					+ "<REC_AMT>" + object.getRecAmt() + "</REC_AMT>"
					+ "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					+ "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					+ "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					+ "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					+ "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case ORDERGOODSINF:
		{
			TdOrderGoodsInf object = (TdOrderGoodsInf)entity;
			xml =   "<TABLE><ORDER_HEADER_ID>" + object.getOrderHeaderId() + "</ORDER_HEADER_ID>"
					+ "<ORDER_LINE_ID>" + object.getOrderLineId() + "</ORDER_LINE_ID>"
					+ "<GOODS_ID>" + object.getGoodsId() + "</GOODS_ID>"
					+ "<GOODS_TITLE>" + object.getGoodsTitle() + "</GOODS_TITLE>"
					+ "<GOODS_SUB_TITLE>" + object.getGoodsSubTitle() + "</GOODS_SUB_TITLE>"
					+ "<SKU>" + object.getSku() + "</SKU>"
					+ "<QUANTITY>" + object.getQuantity() + "</QUANTITY>"
					+ "<JX_PRICE>" + object.getJxPrice() + "</JX_PRICE>"
					+ "<LS_PRICE>" + object.getLsPrice() + "</LS_PRICE>"
					+ "<GIFT_FLAG>" + object.getGiftFlag() + "</GIFT_FLAG>"
					+ "<PROMOTION>" + object.getPromotion() + "</PROMOTION>"
					+ "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					+ "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					+ "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					+ "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					+ "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case ORDERCOUPONINF:
		{
			TdOrderCouponInf object = (TdOrderCouponInf)entity;
			xml =   "<TABLE><ORDER_HEADER_ID>" + object.getOrderHeaderId() + "</ORDER_HEADER_ID>"
					+ "<LINE_ID>" + object.getLineId() + "</LINE_ID>"
					+ "<COUPON_TYPE_ID>" + object.getCouponTypeId() + "</COUPON_TYPE_ID>"
					+ "<SKU>" + object.getSku() + "</SKU>"
					+ "<QUANTITY>" + object.getQuantity() + "</QUANTITY>"
					+ "<PRICE>" + object.getPrice() + "</PRICE>"
					+ "<HISTORY_FLAG>" + object.getHistoryFlag() + "</HISTORY_FLAG>"
					+ "<PROMOTION>" + object.getPromotion() + "</PROMOTION>"
					+ "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					+ "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					+ "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					+ "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					+ "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case ORDERRECEIVEINF:
		{
			TdOrderReceiveInf object = (TdOrderReceiveInf)entity;
			xml =   "<TABLE><SOB_ID>" + object.getSobId() + "</SOB_ID>"
					+ "<HEADER_ID>" + object.getHeaderId() + "</HEADER_ID>"
					+ "<ORDER_NUMBER>" + object.getOrderNumber() + "</ORDER_NUMBER>"
					+ "<RECEIVE_DATE>" + object.getReceiveDate() + "</RECEIVE_DATE>"
					+ "<DELIVER_TYPE_TITLE>" + object.getDeliverTypeTitle() + "</DELIVER_TYPE_TITLE>"
					+ "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					+ "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					+ "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					+ "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					+ "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case RETURNORDERINF:
		{
			TdReturnOrderInf object = (TdReturnOrderInf)entity;
			xml =   "<TABLE><SOB_ID>" + object.getSobId() + "</SOB_ID>"
					 + "<RT_HEADER_ID>" + object.getRtHeaderId() + "</RT_HEADER_ID>"
					 + "<RETURN_NUMBER>" + object.getReturnNumber() + "</RETURN_NUMBER>"
					 + "<RETURN_DATE>" + object.getReturnDate() + "</RETURN_DATE>"
					 + "<RT_FULL_FLAG>" + object.getRtFullFlag() + "</RT_FULL_FLAG>"
					 + "<ORDER_HEADER_ID>" + object.getOrderHeaderId() + "</ORDER_HEADER_ID>"
					 + "<ORDER_NUMBER>" + object.getOrderNumber() + "</ORDER_NUMBER>"
					 + "<PRODECT_TYPE>" + object.getProdectType() + "</PRODECT_TYPE>"
					 + "<DIY_SITE_CODE>" + object.getDiySiteCode() + "</DIY_SITE_CODE>"
					 + "<REFUND_TYPE>" + object.getRefundType() + "</REFUND_TYPE>"
					 + "<AUDIT_DATE>" + object.getAuditDate() + "</AUDIT_DATE>"
					 + "<REFUND_AMOUNT>" + object.getRefundAmount() + "</REFUND_AMOUNT>"
					 + "<PREPAY_AMT>" + object.getPrepayAmt() + "</PREPAY_AMT>"
					 + "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					 + "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					 + "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					 + "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					 + "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case RETURNGOODSINF:
		{
			TdReturnGoodsInf object = (TdReturnGoodsInf)entity;
			xml =   "<TABLE><RT_HEADER_ID>" + object.getRtHeaderId() + "</RT_HEADER_ID>"
					 + "<RT_LINE_ID>" + object.getRtLineId() + "</RT_LINE_ID>"
					 + "<SKU>" + object.getSku() + "</SKU>"
					 + "<QUANTITY>" + object.getQuantity() + "</QUANTITY>"
					 + "<JX_PRICE>" + object.getJxPrice() + "</JX_PRICE>"
					 + "<LS_PRICE>" + object.getLsPrice() + "</LS_PRICE>"
					 + "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					 + "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					 + "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					 + "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					 + "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case RETURNCOUPONINF:
		{
			TdReturnCouponInf object = (TdReturnCouponInf)entity;
			xml =   "<TABLE><RT_HEADER_ID>" + object.getRtHeaderId() + "</RT_HEADER_ID>"
					 + "<LINE_ID>" + object.getLineId() + "</LINE_ID>"
					 + "<COUPON_TYPE_ID>" + object.getCouponTypeId() + "</COUPON_TYPE_ID>"
					 + "<SKU>" + object.getSku() + "</SKU>"
					 + "<QUANTITY>" + object.getQuantity() + "</QUANTITY>"
					 + "<PRICE>" + object.getPrice() + "</PRICE>"
					 + "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					 + "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					 + "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					 + "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					 + "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case RETURNTIMEINF:
		{
			TdReturnTimeInf object = (TdReturnTimeInf)entity;
			xml =   "<TABLE><SOB_ID>" + object.getSobId() + "</SOB_ID>"
					+ "<RT_HEADER_ID>" + object.getRtHeaderId() + "</RT_HEADER_ID>"
					+ "<RETURN_NUMBER>" + object.getReturnNumber() + "</RETURN_NUMBER>"
					+ "<RETURN_DATE>" + object.getReturnDate() + "</RETURN_DATE>"
					+ "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					+ "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					+ "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					+ "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					+ "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case CASHRECIPTINF:
		{
			TdCashReciptInf object = (TdCashReciptInf)entity;
			xml =   "<TABLE><SOB_ID>" + object.getSobId() + "</SOB_ID>"
					+ "<RECEIPT_ID>" + object.getReceiptId() + "</RECEIPT_ID>"
					+ "<RECEIPT_NUMBER>" + object.getReceiptNumber() + "</RECEIPT_NUMBER>"
					+ "<USERID>" + object.getUserid() + "</USERID>"
					+ "<USERNAME>" + object.getUsername() + "</USERNAME>"
					+ "<USERPHONE>" + object.getUserphone() + "</USERPHONE>"
					+ "<DIY_SITE_CODE>" + object.getDiySiteCode() + "</DIY_SITE_CODE>"
					+ "<RECEIPT_CLASS>" + object.getReceiptClass() + "</RECEIPT_CLASS>"
					+ "<ORDER_HEADER_ID>" + object.getOrderHeaderId() + "</ORDER_HEADER_ID>"
					+ "<ORDER_NUMBER>" + object.getOrderNumber() + "</ORDER_NUMBER>"
					+ "<PRODUCT_TYPE>" + object.getProductType() + "</PRODUCT_TYPE>"
					+ "<RECEIPT_TYPE>" + object.getReceiptType() + "</RECEIPT_TYPE>"
					+ "<RECEIPT_DATE>" + object.getReceiptDate() + "</RECEIPT_DATE>"
					+ "<AMOUNT>" + object.getAmount() + "</AMOUNT>"
					+ "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					+ "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					+ "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					+ "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					+ "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		case CASHREFUNDINF:
		{
			TdCashRefundInf object = (TdCashRefundInf)entity;
			xml =   "<TABLE><SOB_ID>" + object.getSobId() + "</SOB_ID>"
					+ "<REFUND_ID>" + object.getRefundId() + "</REFUND_ID>"
					+ "<REFUND_NUMBER>" + object.getRefundNumber() + "</REFUND_NUMBER>"
					+ "<USERID>" + object.getUserid() + "</USERID>"
					+ "<USERNAME>" + object.getUsername() + "</USERNAME>"
					+ "<USERPHONE>" + object.getUserphone() + "</USERPHONE>"
					+ "<DIY_SITE_CODE>" + object.getDiySiteCode() + "</DIY_SITE_CODE>"
					+ "<REFUND_CLASS>" + object.getRefundClass() + "</REFUND_CLASS>"
					+ "<RT_HEADER_ID>" + object.getRtHeaderId() + "</RT_HEADER_ID>"
					+ "<RETURN_NUMBER>" + object.getReturnNumber() + "</RETURN_NUMBER>"
					+ "<PRODUCT_TYPE>" + object.getProductType() + "</PRODUCT_TYPE>"
					+ "<REFUND_TYPE>" + object.getRefundType() + "</REFUND_TYPE>"
					+ "<REFUND_DATE>" + object.getRefundDate() + "</REFUND_DATE>"
					+ "<AMOUNT>" + object.getAmount() + "</AMOUNT>"
					+ "<DESCRIPTION>" + object.getDescription() + "</DESCRIPTION>"
					+ "<ATTRIBUTE1>" + object.getAttribute1() + "</ATTRIBUTE1>"
					+ "<ATTRIBUTE2>" + object.getAttribute2() + "</ATTRIBUTE2>"
					+ "<ATTRIBUTE3>" + object.getAttribute3() + "</ATTRIBUTE3>"
					+ "<ATTRIBUTE4>" + object.getAttribute4() + "</ATTRIBUTE4>"
					+ "<ATTRIBUTE5>" + object.getAttribute5() + "</ATTRIBUTE5></TABLE>";
			break;
		}
		default:
			break;
		}
		return xml;
	}
	
	/**
	 * 生成完整的XML
	 * @param Tables
	 * @return
	 */
	public String XmlWithTables(List<String> Tables)
	{
		if (Tables == null)
		{
			return null;
		}
		String xmlEnd = "<ERP>";
		if (Tables.size() > 0)
		{
			for (String string : Tables) 
			{
				xmlEnd += string;
			}
			xmlEnd += "</ERP>";
			xmlEnd=xmlEnd.replace("null", "");
		}
		else 
		{
			xmlEnd = null;
		}
		
		return xmlEnd;
	}
	
	/**     -=-=-=-=-=-=-     单个实体类发送EBS       -=-=-=-=-=-=-       **/
	
	
	/**
	 * 通过相关的实体进行单一的发送给EBS
	 * @param object
	 * @param type
	 */
	public void ebsWithObject(Object object, INFTYPE type) 
	{
		switch (type) 
		{
			case ORDERRECEIVEINF:
			{
				TdOrderReceiveInf orderReceiveInf = (TdOrderReceiveInf)object;
				String orderInfXML = this.XMLWithEntity(orderReceiveInf, INFTYPE.ORDERRECEIVEINF);
				Object[] orderInf = { "TD_ORDER", "1", orderInfXML };
				try
				{
					String result = (String)TdInterfaceService.getCall().invoke(orderInf);
					System.out.println(result);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			break;
			default:
				break;
		}
	}

}
