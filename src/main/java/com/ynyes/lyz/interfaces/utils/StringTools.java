package com.ynyes.lyz.interfaces.utils;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ynyes.lyz.entity.TdCoupon;

public class StringTools {
	
	public static String getEbsWebServiceUrlByLocalHost()
	{
		String ebsUrlReal = "http://erpap.zghuarun.com:8008/webservices/SOAProvider/plsql/cux_app_webservice_pkg?wsdl";
		String ebsUrlTest = "http://erptest.zghuarun.com:8030/webservices/SOAProvider/plsql/cux_app_webservice_pkg/?wsdl";
//		String ebsUrlTest = "http://182.92.160.220:8199/WmsInterServer.asmx?wsdl";
		try
		{
			InetAddress address = InetAddress.getLocalHost();
			String hostAddress = address.getHostAddress();
			if (hostAddress.equalsIgnoreCase("101.200.128.65")) 
			{
				return ebsUrlReal;
			}
			else
			{
				return ebsUrlTest;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return ebsUrlTest;
		}
	}
	
	public static String interfaceMessage(String resultStr) 
	{
		// "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>XML参数错误</MESSAGE></STATUS></RESULTS>";
		if (!resultStr.contains("<CODE>") || !resultStr.contains("</CODE>") || !resultStr.contains("<MESSAGE>") || !resultStr.contains("</MESSAGE>")) 
		{
			return "返回XML格式错误错误:"+resultStr;
		}
		String regEx = "<CODE>([\\s\\S]*?)</CODE>";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(resultStr);
		
		if (mat.find()) 
		{
			System.out.println("CODE is :" + mat.group(0));
			String code = mat.group(0).replace("<CODE>", "");
			code = code.replace("</CODE>", "").trim();
			if (Integer.parseInt(code) == 0) 
			{
				return null;
			} else
			{
				String errorMsg = "<MESSAGE>([\\s\\S]*?)</MESSAGE>";
				pat = Pattern.compile(errorMsg);
				mat = pat.matcher(resultStr);
				if (mat.find()) 
				{
					System.out.println("ERRORMSG is :" + mat.group(0));
					String msg = mat.group(0).replace("<MESSAGE>", "");
					msg = msg.replace("</MESSAGE>", "").trim();
					return msg;
				}
			}
		}
		return null;
	}
	
	public static Integer coupontypeWithCoupon(TdCoupon coupon)
	{
		// app:优惠券限用分类类型ID: 1. 通用现金券；2. 指定商品现金券；3. 产品券;
		// ebs: 券类型:1.产品券，2.产品现金券，3.通用现金券，4.电子产品券，5.电子现金券
		
		if (coupon == null || coupon.getTypeCategoryId() == null) 
		{
			return null;
		}
		
		Long typeCategoryId = coupon.getTypeCategoryId();
		
		if (typeCategoryId == 1)
		{
			if (coupon.getIsBuy() == null || coupon.getIsBuy() == false)
			{
				return 3;
			}
			else
			{
				return 5;
			}
		}
		else if (typeCategoryId == 2) 
		{
			if (coupon.getIsBuy() == null || coupon.getIsBuy() == false)
			{
				return 2;
			}
			else
			{
				return 4;
			}
		}
		else 
		{
			return 1;
		}
	}
		
	
	public static String getProductStrByOrderNumber(String orderNumber)
	{
		String templeStr = null;
		if (orderNumber.contains("XN"))
		{
			return null;
		}
		if (orderNumber.length() < 20) 
		{
			return null;
		}
		templeStr = orderNumber.substring(orderNumber.length() - 20);
		
		return orderNumber.replace(templeStr, "");
	}
	
	public static String booleanToStr(Boolean bool)
	{
		if (bool == null) 
		{
			return null;
		}
		if (bool == true)
		{
			return "Y";
		}
		else
		{
			return "N";
		}
	}
	/**
	 * 根据订单号返回类型
	 * @param orderNumber
	 * @return
	 */
	public static String getProdectTypeByOrderNumber(String orderNumber){
		if(orderNumber==null){
			return "";
		}
		String type= orderNumber.substring(0, 2);
		if(type.equalsIgnoreCase("LY")){
			type="LYZ";
		}
		return type;
		
	}
}
