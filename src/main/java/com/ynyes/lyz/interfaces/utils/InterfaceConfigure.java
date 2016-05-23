package com.ynyes.lyz.interfaces.utils;

public class InterfaceConfigure 
{
	/*
	 * 根据不同的服务器注释不同的部分
	 */
//	static String wmsUrl = "http://101.200.75.73:8999/WmsInterServer.asmx?wsdl"; // 正式
	static String wmsUrl = "http://182.92.160.220:8199/WmsInterServer.asmx?wsdl"; // 测试
	String ebsUrlReal = "http://erpap.zghuarun.com:8008/webservices/SOAProvider/plsql/cux_app_webservice_pkg?wsdl";
	String ebsUrlTest = "http://erptest.zghuarun.com:8030/webservices/SOAProvider/plsql/cux_app_webservice_pkg/?wsdl";
	
	/*-----测试环境 start----*/
	
	/*
	 * 在测试服务器抛给WMS的WEBSERVICE接口地址
	 */
	static String WMS_WS_URL = "http://182.92.160.220:8199/WmsInterServer.asmx?wsdl";
	
	/*
	 * 在测试服务器抛给EBS的WEBSERVICE接口地址
	 */
	static String EBS_WS_URL = "http://erptest.zghuarun.com:8030/webservices/SOAProvider/plsql/cux_app_webservice_pkg/?wsdl";
	
	/*-----测试环境 start----*/
	
	/*-----正式环境 start----*/
	
	// 在正式服务器抛给WMS的WEBSERVICE接口地址
//	static String WMS_WS_URL = "http://101.200.75.73:8999/WmsInterServer.asmx?wsdl";
	
	// 在正式服务器抛给EBS的WEBSERVICE接口地址
//	static String EBS_WS_URL = "http://erpap.zghuarun.com:8008/webservices/SOAProvider/plsql/cux_app_webservice_pkg?wsdl";
	
	/*-----正式环境 start----*/

	/**
	 * 抛给WMS的WEBSERVICE接口地址
	 * @return WMS_WS_URL
	 */
	public static String getWMS_WS_URL() {
		return WMS_WS_URL;
	}

	/**
	 * 抛给EBS的WEBSERVICE接口地址
	 * @return EBS_WS_URL
	 */
	public static String getEBS_WS_URL() {
		return EBS_WS_URL;
	}
	
	
	/*-----测试环境   end----*/
}
