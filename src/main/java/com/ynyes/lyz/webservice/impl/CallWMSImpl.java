/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
// START SNIPPET: service
package com.ynyes.lyz.webservice.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.geronimo.mail.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ynyes.lyz.entity.TdBackDetail;
import com.ynyes.lyz.entity.TdBackMain;
import com.ynyes.lyz.entity.TdDeliveryInfo;
import com.ynyes.lyz.entity.TdDeliveryInfoDetail;
import com.ynyes.lyz.entity.TdDiySiteInventory;
import com.ynyes.lyz.entity.TdGoods;
import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.entity.TdReturnNote;
import com.ynyes.lyz.entity.TdTbwRecd;
import com.ynyes.lyz.entity.TdTbwRecm;
import com.ynyes.lyz.entity.TdUser;
import com.ynyes.lyz.service.TdBackDetailService;
import com.ynyes.lyz.service.TdBackMainService;
import com.ynyes.lyz.service.TdDeliveryInfoDetailService;
import com.ynyes.lyz.service.TdDeliveryInfoService;
import com.ynyes.lyz.service.TdDiySiteInventoryLogService;
import com.ynyes.lyz.service.TdDiySiteInventoryService;
import com.ynyes.lyz.service.TdGoodsService;
import com.ynyes.lyz.service.TdOrderService;
import com.ynyes.lyz.service.TdPriceCountService;
import com.ynyes.lyz.service.TdReturnNoteService;
import com.ynyes.lyz.service.TdTbwRecdService;
import com.ynyes.lyz.service.TdTbwRecmService;
import com.ynyes.lyz.service.TdUserService;
import com.ynyes.lyz.webservice.ICallWMS;

@WebService
public class CallWMSImpl implements ICallWMS {
	
	@Autowired
	private TdDeliveryInfoService tdDeliveryInfoService;
	
	@Autowired
	private TdDeliveryInfoDetailService tdDeliveryInfoDetailService;
	
	@Autowired
	private TdGoodsService tdGoodsService;
	
	@Autowired
	private TdOrderService tdOrderService;
	
	@Autowired
	private TdBackMainService tdBackMainService;
	
	@Autowired
	private TdBackDetailService tdBackDetailService;
	
	@Autowired
	private TdUserService tdUserService;
	
	@Autowired
	private TdPriceCountService tdPriceCountService;
	
	@Autowired
	private TdReturnNoteService tdReturnNoteService;
	
	@Autowired
	private TdDiySiteInventoryService tdDiySiteInventoryService;
	
	@Autowired
	private TdDiySiteInventoryLogService tdDiySiteInventoryLogService;
	
	@Autowired
	private TdTbwRecdService tdTbwRecdService;
	
	@Autowired
	private TdTbwRecmService tdTbwRecmService;

	public String GetWMSInfo(String STRTABLE, String STRTYPE, String XML)
	{
		System.out.println("MDJ:WMSInfo called：" + STRTABLE);

		if (null == STRTABLE || STRTABLE.isEmpty() || STRTABLE.equals("?"))
		{
			return "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>STRTABLE参数错误</MESSAGE></STATUS></RESULTS>";
		}

		if (null == XML || XML.isEmpty() || XML.equals("?")) 
		{
			return "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>XML参数错误</MESSAGE></STATUS></RESULTS>";
		}
		
		String XMLStr = XML.trim();
		
		XMLStr = XML.replace("\n", "");
		
		byte[] decoded = Base64.decode(XMLStr);

		String decodedXML = null;

		try
		{
			decodedXML = new String(decoded, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println("UnsupportedEncodingException for decodedXML");
			e.printStackTrace();
		}
		
		if (null == decodedXML || decodedXML.isEmpty()) 
		{
			return "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>解密后XML数据为空</MESSAGE></STATUS></RESULTS>";
		}

		System.out.println(decodedXML);

		// 解析XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try
		{
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
			return "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>解密后xml参数错误</MESSAGE></STATUS></RESULTS>";
		}

		Document document = null;

		InputSource is = new InputSource(new StringReader(decodedXML));

		try
		{
			document = builder.parse(is);
		}
		catch (SAXException | IOException e)
		{
			e.printStackTrace();
			return "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>解密后xml格式不对</MESSAGE></STATUS></RESULTS>";
		}
		NodeList nodeList = document.getElementsByTagName("TABLE");
		
		if (STRTABLE.equalsIgnoreCase("tbw_send_task_m"))
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				String c_task_no = null;//任务编号
				String c_begin_dt = null;//开始时间
				String c_end_dt = null;//结束时间
				String c_wh_no = null;//仓库编号
				String c_op_status = null;//操作状态(初始、作业中、完成、结案)
				String c_op_user = null;//作业人员
				String c_modified_userno = null;//修改人员
				String c_owner_no = null;//委托业主
				String c_reserved1 = null;//分单号
				String c_Driver = null;//送货员
				
				Node node = nodeList.item(i);
				NodeList childNodeList = node.getChildNodes();

				for (int idx = 0; idx < childNodeList.getLength(); idx++)
				{
 					Node childNode = childNodeList.item(idx);
					
					if (childNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						// 比较字段名
						if (childNode.getNodeName().equalsIgnoreCase("c_task_no"))
						{
							// 有值
							if (null != childNode.getChildNodes().item(0))
							{
								c_task_no = childNode.getChildNodes().item(0).getNodeValue();
							}
							// 空值
							else
							{
								c_task_no = null;
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_begin_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_begin_dt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_end_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_end_dt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_wh_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_wh_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_op_status"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_op_status = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_op_user"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_op_user = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_modified_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_modified_userno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_owner_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_owner_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_reserved1"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_reserved1 = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_Driver"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_Driver = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						
					}
				}
				
				//保存 修改
				TdDeliveryInfo tdDeliveryInfo = tdDeliveryInfoService.findByTaskNo(c_task_no);
				if (tdDeliveryInfo == null)
				{
					tdDeliveryInfo = new TdDeliveryInfo();
				}
				tdDeliveryInfo.setTaskNo(c_task_no);
				tdDeliveryInfo.setWhNo(c_wh_no);
				tdDeliveryInfo.setDriver(c_Driver);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				if (c_begin_dt != null)
				{
					try
					{
						Date startdate = sdf.parse(c_begin_dt);
						tdDeliveryInfo.setBeginDt(startdate);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				if (c_end_dt != null)
				{
					try 
					{
						Date enddate = sdf.parse(c_end_dt);
						tdDeliveryInfo.setEndDt(enddate);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				
				tdDeliveryInfo.setOrderNumber(c_reserved1);
				tdDeliveryInfo.setOpStatus(c_op_status);
				tdDeliveryInfo.setOpUser(c_op_user);
				tdDeliveryInfo.setModifiedUserno(c_modified_userno);
				tdDeliveryInfo.setOwnerNo(c_owner_no);
				tdDeliveryInfoService.save(tdDeliveryInfo);
			}
			return "<RESULTS><STATUS><CODE>0</CODE><MESSAGE></MESSAGE></STATUS></RESULTS>";
		}
		else if (STRTABLE.equalsIgnoreCase("tbw_send_task_d"))
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				String c_task_no = null;//任务编号
				String c_begin_dt = null;//开始时间
				String c_end_dt = null;//结束时间
				String c_wh_no = null;//仓库编号
				String c_op_status = null;//操作状态(初始、作业中、完成、结案)
				String c_op_user = null;//作业人员
				String c_modified_userno = null;//修改人员
				String c_owner_no = null;//委托业主
				String c_gcode = null;//商品编号
				Double c_d_ack_qty = null; //实回数量
				Double c_d_request_qty = null;//请求数量
				String c_reserved1 = null;//分单号
				
				
				Node node = nodeList.item(i);
				NodeList childNodeList = node.getChildNodes();

				for (int idx = 0; idx < childNodeList.getLength(); idx++)
				{
					Node childNode = childNodeList.item(idx);

					if (childNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						// 比较字段名
						if (childNode.getNodeName().equalsIgnoreCase("c_task_no"))
						{
							// 有值
							if (null != childNode.getChildNodes().item(0))
							{
								c_task_no = childNode.getChildNodes().item(0).getNodeValue();
							}
							// 空值
							else
							{
								c_task_no = null;
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_reserved1"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_reserved1 = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_begin_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_begin_dt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_end_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_end_dt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_wh_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_wh_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_op_status"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_op_status = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_op_user"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_op_user = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_modified_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_modified_userno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_owner_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_owner_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_gcode"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_gcode = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_d_ack_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_d_ack_qty = Double.parseDouble(childNode.getChildNodes().item(0).getNodeValue());
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_d_request_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								String string =  childNode.getChildNodes().item(0).getNodeValue();
								c_d_request_qty = Double.parseDouble(string);
							}
						}

					}
				}
				//保存 修改
				TdDeliveryInfoDetail infoDetail = new TdDeliveryInfoDetail();
				infoDetail.setTaskNo(c_task_no);
				infoDetail.setWhNo(c_wh_no);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				if (c_begin_dt != null)
				{
					try 
					{
						Date startdate = sdf.parse(c_begin_dt);
						infoDetail.setBeginDt(startdate);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				if (c_end_dt != null)
				{
					try 
					{
						Date enddate = sdf.parse(c_end_dt);
						infoDetail.setEndDt(enddate);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				infoDetail.setOpStatus(c_op_status);
				infoDetail.setOpUser(c_op_user);
				infoDetail.setModifiedUserno(c_modified_userno);
				infoDetail.setOwnerNo(c_owner_no);
				infoDetail.setgCode(c_gcode);
				infoDetail.setRequstNumber(c_d_request_qty);
				infoDetail.setBackNumber(c_d_ack_qty);
				infoDetail.setSubOrderNumber(c_reserved1);
				tdDeliveryInfoDetailService.save(infoDetail);
				String siteCode = null;
				Long diySiteId = null;
				Long userId= null;
				Boolean isSendOrder = true;
				TdUser tdUser = null;
				Long sobId = null;
				String orderNumber = null;
				if (c_reserved1 != null)
				{
					TdOrder tdOrder = tdOrderService.findByOrderNumber(c_reserved1);
					if (tdOrder != null && tdOrder.getStatusId() != null && tdOrder.getStatusId() == 3L)
					{
						tdOrder.setStatusId(4L);
						userId = tdOrder.getRealUserId();
						if (tdOrder.getDeliverTypeTitle().equalsIgnoreCase("门店自提")) 
						{
							isSendOrder = false;
						}
						siteCode = tdOrder.getDiySiteCode();
						diySiteId = tdOrder.getDiySiteId();
						tdOrderService.save(tdOrder);
					}
				}
				if (userId != null)
				{
					tdUser = tdUserService.findOne(userId);
				}
				if (tdUser != null)
				{
					sobId = tdUser.getCityId();
				}
				String gCode = infoDetail.getgCode();
				Long backquantity = Math.round(infoDetail.getBackNumber() == null ? 0 : infoDetail.getBackNumber());
				if (isSendOrder) 
				{
					TdDiySiteInventory inventory = tdDiySiteInventoryService.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(gCode, sobId);
					if (inventory != null && inventory.getInventory() != null)
					{
						inventory.setInventory(inventory.getInventory() - backquantity);
						tdDiySiteInventoryLogService.saveChangeLog(inventory, 0 - backquantity, orderNumber, null);
					}
				}
				else
				{
					TdDiySiteInventory inventory = tdDiySiteInventoryService.findByGoodsCodeAndDiySiteId(gCode, diySiteId);
					if (inventory != null && inventory.getInventory() != null)
					{
						inventory.setInventory(inventory.getInventory() - backquantity);
						tdDiySiteInventoryLogService.saveChangeLog(inventory, 0 - backquantity, orderNumber, null);
					}
				}
				
//				TdGoods tdGoods = tdGoodsService.findByCode(infoDetail.getgCode());
//				List<TdDiySiteInventory> inventoryList = tdGoods.getInventoryList();
//				if (tdGoods != null && tdGoods.getLeftNumber() != null)
//				{
//					tdGoods.setLeftNumber(tdGoods.getLeftNumber() - backquantity >= 0 ? tdGoods.getLeftNumber() - backquantity : 0);
//				}
//				if (tdGoods != null && siteCode != null && inventoryList !=null && inventoryList.size() >= 1)
//				{
//					for (int inventoryIndex = 0; inventoryIndex < inventoryList.size(); inventoryIndex++) 
//					{
//						TdDiySiteInventory siteInventory = inventoryList.get(inventoryIndex);
//						if (siteInventory.getDiyCode().equals(siteCode)) 
//						{
//							siteInventory.setInventory(siteInventory.getInventory() - backquantity);
//							tdDiySiteInventoryService.save(siteInventory);
//							break;
//						}
//					}
//				}
//				tdGoodsService.save(tdGoods, "WMS:goods");

			}
			return "<RESULTS><STATUS><CODE>0</CODE><MESSAGE></MESSAGE></STATUS></RESULTS>";
		}
		else if (STRTABLE.equalsIgnoreCase("tbw_back_m"))// 退货入库单 主档
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				// 仓库编号
				String c_wh_no = null;
				// 委托业主
				String c_owner_no = null;
				// 返配验收单号
				String c_rec_no = null;
				// 打印次数
				Integer c_print_times = null;
				// 返配单号
				String c_back_no = null;
				// 返配单类型（一般返配）
				String c_back_type = null;
				// 返配单分类(存储型、越库型)
				String c_back_class = null;
				// 客户信息
				String c_customer_no = null;
				// 月台
				String c_plat_no = null;
				// 验收人员
				String c_rec_user = null;
				// 操作工具(表单,pda,电子标签)
				String c_op_tools = null;
				// 操作状态(初始、作业中、完成、结案)
				String c_op_status = null;
				// 备注
				String c_note = null;
				// 建立人员
				String c_mk_userno = null;
				// 修改人员
				String c_modified_userno = null;
				// 门店退单
				String c_po_no = null;
				// 开始操作时间
				String c_begin_dt = null;
				// 结束操作时间
				String c_end_dt = null;
				// 建立时间
				String c_mk_dt = null;
				// 修改时间
				String c_modified_dt = null;
				
				// 配送人员
				String driver = null;
				
				Node node = nodeList.item(i);
				NodeList childNodeList = node.getChildNodes();

				for (int idx = 0; idx < childNodeList.getLength(); idx++)
				{
 					Node childNode = childNodeList.item(idx);
					
					if (childNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						// 比较字段名
						if (childNode.getNodeName().equalsIgnoreCase("c_wh_no"))
						{
							// 有值
							if (null != childNode.getChildNodes().item(0))
							{
								c_wh_no = childNode.getChildNodes().item(0).getNodeValue();
							}
							// 空值
							else
							{
								c_wh_no = null;
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_owner_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_owner_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_driver"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								driver = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_rec_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_rec_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_print_times"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_print_times = Integer.parseInt(childNode.getChildNodes().item(0).getNodeValue());
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_back_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_back_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_back_type"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_back_type = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_back_class"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_back_class = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_customer_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_customer_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_plat_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_plat_no = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_rec_user"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_rec_user = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_op_tools"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_op_tools = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_note"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_note =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_mk_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_mk_userno =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_modified_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_modified_userno =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_po_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_po_no =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_begin_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_begin_dt =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_end_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_end_dt =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_mk_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_mk_dt =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_modified_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_modified_dt =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
					}
				}
				
				//保存 修改
				TdBackMain tdBackMain = tdBackMainService.findByRecNo(c_rec_no);
				if (tdBackMain == null)
				{
					tdBackMain = new TdBackMain();
					tdBackMain.setRecNo(c_rec_no);
				}
				tdBackMain.setOwnerNo(c_owner_no);
				tdBackMain.setWhNo(c_wh_no);
				tdBackMain.setPrintTimes(c_print_times);
				tdBackMain.setBackClass(c_back_class);
				tdBackMain.setBackNo(c_back_no);
				tdBackMain.setBackType(c_back_type);
				tdBackMain.setCustomerNo(c_customer_no);
				tdBackMain.setPlatNo(c_plat_no);
				tdBackMain.setRecUser(c_rec_user);
				tdBackMain.setOpTools(c_op_tools);
				tdBackMain.setOpStatus(c_op_status);
				tdBackMain.setNote(c_note);
				tdBackMain.setMkUserno(c_mk_userno);
				tdBackMain.setModifiedUserno(c_modified_userno);
				tdBackMain.setPoNo(c_po_no);
				tdBackMain.setDriver(driver);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				if (c_begin_dt != null)
				{
					try
					{
						Date c_begin = sdf.parse(c_begin_dt);
						tdBackMain.setBeginDt(c_begin);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				if (c_end_dt != null)
				{
					try 
					{
						Date enddate = sdf.parse(c_end_dt);
						tdBackMain.setEndDt(enddate);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				if (c_mk_dt != null)
				{
					try 
					{
						Date c_mk = sdf.parse(c_mk_dt);
						tdBackMain.setMkDt(c_mk);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				if (c_modified_dt != null)
				{
					try 
					{
						Date c_modified = sdf.parse(c_modified_dt);
						tdBackMain.setModifiedDt(c_modified);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				tdBackMainService.save(tdBackMain);
				
				TdReturnNote tdReturnNote = tdReturnNoteService.findByReturnNumber(c_po_no);
				if (tdReturnNote != null) 
				{
//					tdReturnNote.setStatusId(5L);
//					
//					TdOrder order = tdOrderService.findByOrderNumber(tdReturnNote.getOrderNumber());
//					if (order != null)
//					{
//						if (order.getStatusId() == 9 || order.getStatusId() == 10 || order.getStatusId() == 11 || order.getStatusId() == 12)
//						{
//							order.setStatusId(12L);
//							tdOrderService.save(order);
//						}
//						TdUser tdUser = tdUserService.findByUsername(order.getUsername());
//						tdPriceCountService.cashAndCouponBack(order, tdUser);
//					}
					tdReturnNote.setDriver(driver);
					tdReturnNoteService.save(tdReturnNote);
				}
				
//				if (c_reserved1 != null) 
//				{
//					TdOrder tdOrder = tdOrderService.findByOrderNumber(c_reserved1);
//					if (tdOrder != null && tdOrder.getStatusId() != null && tdOrder.getStatusId() == 3L) {
//						tdOrder.setStatusId(4L);
//						tdOrderService.save(tdOrder);
//					}
//				}
			}
			return "<RESULTS><STATUS><CODE>0</CODE><MESSAGE></MESSAGE></STATUS></RESULTS>";
		}
		else if (STRTABLE.equalsIgnoreCase("tbw_back_d"))// 退货入库单 详细
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				// 委托业主	
				String ownerNo = null;

				//  任务编号
				String recNo = null;

				// 任务id
				String recId = null;

				// 商品编号
				String gcode = null;

				// 包装数量
				String packQty = null;

				// 价格
				String price = null;
				
				//验收赠品数量
				String giftQty = null;

				// 验收赠品不良品数量
				String badQty = null;

				// 验收数量
				String recQty = null;

				// 作业人员
				String recUser = null;

				//月台
				String platNo = null;

				// 操作工具(表单,pda,电子标签)
				String opTools = null;

				// 状态（初始、作业中、完成、结案）
				String opStatus = null;

				// 预留
				String reserved1 = null;
				
				// 预留
				String reserved2 = null;

				// 预留
				String reserved3 = null;

				// 预留
				String reserved4 = null;

				// 预留
				String reserved5 = null;
					
				// 备注
				String note = null;

				// 建立人员
				String mkUserno = null;

				// 修改人员
				String modifiedUserno = null;

				// 建立时间
				String c_mk_dt = null;

				// 修改时间
				String c_modified_dt = null;
				
				Node node = nodeList.item(i);
				NodeList childNodeList = node.getChildNodes();

				for (int idx = 0; idx < childNodeList.getLength(); idx++)
				{
 					Node childNode = childNodeList.item(idx);
					
					if (childNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						// 比较字段名
						if (childNode.getNodeName().equalsIgnoreCase("c_owner_no"))
						{
							// 有值
							if (null != childNode.getChildNodes().item(0))
							{
								ownerNo = childNode.getChildNodes().item(0).getNodeValue();
							}
							// 空值
							else
							{
								ownerNo = null;
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_rec_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_rec_id"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recId = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_gcode"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								gcode = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_pack_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								packQty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_price"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								price = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_gift_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								giftQty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_bad_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								badQty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_rec_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recQty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_rec_user"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recUser = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_plat_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								platNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_op_tools"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								opTools =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_op_status"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								opStatus =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_reserved1"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved1 =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_reserved2"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved2 =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_reserved3"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved3 =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_reserved4"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved4 =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_reserved5"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved5 =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_note"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								note =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_mk_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								mkUserno =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_mk_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_mk_dt =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_modified_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_mk_dt =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						else if (childNode.getNodeName().equalsIgnoreCase("c_modified_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_modified_dt =  childNode.getChildNodes().item(0).getNodeValue();
							}
						}
					}
				}
				
				//保存 修改
				TdBackDetail tdBackDetail =  new TdBackDetail();
				tdBackDetail.setOwnerNo(ownerNo);
				tdBackDetail.setRecNo(recNo);
				tdBackDetail.setRecId(recId);
				tdBackDetail.setGcode(gcode);
				tdBackDetail.setPackQty(packQty);
				tdBackDetail.setPrice(price);
				tdBackDetail.setGiftQty(giftQty);
				tdBackDetail.setBadQty(badQty);
				tdBackDetail.setRecQty(recQty);
				tdBackDetail.setRecUser(recUser);
				tdBackDetail.setPlatNo(platNo);
				tdBackDetail.setOpTools(opTools);
				tdBackDetail.setOpStatus(opStatus);
				tdBackDetail.setReserved1(reserved1);
				tdBackDetail.setReserved2(reserved2);
				tdBackDetail.setReserved3(reserved3);
				tdBackDetail.setReserved4(reserved4);
				tdBackDetail.setReserved5(reserved5);
				tdBackDetail.setNote(note);
				tdBackDetail.setMkUserno(mkUserno);
				tdBackDetail.setMkDt(c_mk_dt);
				tdBackDetail.setModifiedUserno(modifiedUserno);
				tdBackDetail.setModifiedDt(c_modified_dt);
				tdBackDetailService.save(tdBackDetail);
				
				
			}
			return "<RESULTS><STATUS><CODE>0</CODE><MESSAGE></MESSAGE></STATUS></RESULTS>";
		}
		else if (STRTABLE.equalsIgnoreCase("tbw_rec_d"))
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				// 委托业主	
				String ownerNo = null;
				//  任务编号
				String recNo = null;
				// 任务id
				Long recId = null;
				// 商品编号
				String gcode = null;
				// 包装数量
				Long packQty = null;
				// 价格
				String price = null;
				// 品质(良品、不良品、赠品)
				String itemType = null;
				//验收赠品数量
				String giftQty = null;
				// 验收赠品不良品数量
				String badQty = null;
				// 验收数量(红冲数量)
				String recQty = null;
				// 作业人员
				String recUserno = null;
				//月台
				String platNo = null;
				// 操作工具(表单,pda,电子标签)
				String opTools = null;
				// 状态（初始、作业中、完成、结案）
				String opStatus = null;
				// 预留
				String reserved1 = null;
				// 预留
				String reserved2 = null;
				// 预留
				String reserved3 = null;
				// 预留
				String reserved4 = null;
				// 预留
				String reserved5 = null;
				// 备注
				String note = null;
				// 建立人员
				String mkUserno = null;
				// 建立时间
				String mkDt = null;
				// 修改人员
				String modifiedUserno = null;
				// 修改时间
				String modifiedDt = null;
				// 分播数量
				String c_dps_qty = null;
				Node node = nodeList.item(i);
				NodeList childNodeList = node.getChildNodes();

				for (int idx = 0; idx < childNodeList.getLength(); idx++)
				{
					Node childNode = childNodeList.item(idx);
					if (childNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						if (childNode.getNodeName().equalsIgnoreCase("c_owner_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								ownerNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_rec_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_rec_id"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recId = Long.parseLong(childNode.getChildNodes().item(0).getNodeValue());
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_gcode"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								gcode = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_pack_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								packQty = Long.parseLong(childNode.getChildNodes().item(0).getNodeValue());
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_price"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								price = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_item_type"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_gift_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								giftQty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_bad_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								badQty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_rec_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recQty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_rec_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								recUserno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_plat_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								platNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_op_tools"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								opTools = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_op_status"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								opStatus = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_reserved1"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved1 = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_reserved2"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved2 = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_reserved3"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved3 = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_reserved4"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved4 = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_reserved5"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								reserved5 = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_note"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								note = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_mk_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								mkUserno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_mk_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								mkDt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_modified_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								modifiedUserno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_modified_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								modifiedDt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_dps_qty"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								c_dps_qty = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
					}
				}
				//保存 修改
				TdTbwRecd recd = new TdTbwRecd();
				recd.setOwnerNo(ownerNo);
				recd.setRecNo(recNo);
				recd.setRecId(recId);
				recd.setGcode(gcode);
				recd.setPackQty(packQty);
				recd.setPrice(price);
				recd.setItemType(itemType);
				recd.setGiftQty(giftQty);
				recd.setBadQty(badQty);
				recd.setRecQty(recQty);
				recd.setMkUserno(mkUserno);
				recd.setRecUserno(recUserno);
				recd.setPlatNo(platNo);
				recd.setOpTools(opTools);
				recd.setOpStatus(opStatus);
				recd.setReserved1(reserved1);
				recd.setReserved2(reserved2);
				recd.setReserved3(reserved3);
				recd.setReserved4(reserved4);
				recd.setReserved5(reserved5);
				recd.setNote(note);
				recd.setMkUserno(mkUserno);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				if (mkDt != null)
				{
					try
					{
						Date md_dt = sdf.parse(mkDt);
						recd.setMkDt(md_dt);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				recd.setModifiedUserno(modifiedUserno);
				if (modifiedDt != null)
				{
					try
					{
						Date modified_dt = sdf.parse(modifiedDt);
						recd.setMkDt(modified_dt);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				recd.setDpsQty(c_dps_qty);
				recd.setInitTime(new Date());
				tdTbwRecdService.save(recd);
			}
		}
		else if (STRTABLE.equalsIgnoreCase("tbw_rec_m"))
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				// 仓库编号
				String cWhNo = null;
				// 委托业主
				String cOwnerNo = null;
				// 验收单号(当类型是红冲时为:红冲单号)
				String cRecNo = null;
				// 打印次数
				Integer cPrintTimes = null;
				// 验收汇总
				String cGatherRecNo = null;
				// 进货汇总
				String cGatherNo = null;
				// 进货单分类(存储型、越库型)
				String cInClass = null;
				// 进货单号(当类型是红冲时为:验收单号)
				String cInNo = null;
				// 进货单类型(一般订单，紧急订单,红冲多，红冲少)
				String cInType = null;
				// 供应商编码
				String cProviderNo = null;
				// 月台
				String cPlatNo = null;
				// 验收人员
				String cRecUserno = null;
				// 操作工具(表单,pda,电子标签)
				String cOpTools = null;
				// 操作状态(初始、作业中、完成、结案)
				String cOpStatus = null;
				// 开始操作时间
				String cBeginDt = null;
				// 结束操作时间
				String cEndDt = null;
				// 备注
				String cNote = null;
				// 建立人员
				String cMkUserno = null;
				// 建立时间
				String cMkDt = null;
				// 修改人员
				String cModifiedUserno = null;
				// 修改时间
				String cModifiedDt = null;
				// 采购单号
				String cPoNo = null;

				Node node = nodeList.item(i);
				NodeList childNodeList = node.getChildNodes();
				
				// 遍历所有TABLE中的字段
				for (int idx = 0; idx < childNodeList.getLength(); idx++)
				{
					Node childNode = childNodeList.item(idx);
					
					if (childNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						if (childNode.getNodeName().equalsIgnoreCase("c_wh_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cWhNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_owner_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cOwnerNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_rec_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cRecNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_wh_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cWhNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_print_times"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cPrintTimes = Integer.parseInt(childNode.getChildNodes().item(0).getNodeValue());
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_gather_rec_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cGatherRecNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_gather_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cGatherNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_in_class"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cInClass = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_in_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cInNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_in_type"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cInType = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_provider_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cProviderNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_plat_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cPlatNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_rec_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cRecUserno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_op_tools"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cOpTools = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_op_status"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cOpStatus = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_begin_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cBeginDt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_end_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cEndDt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_note"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cNote = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_mk_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cMkUserno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_mk_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cMkDt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_modified_userno"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cModifiedUserno = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_modified_dt"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cModifiedDt = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
						if (childNode.getNodeName().equalsIgnoreCase("c_po_no"))
						{
							if (null != childNode.getChildNodes().item(0))
							{
								cPoNo = childNode.getChildNodes().item(0).getNodeValue();
							}
						}
					}
				}
				// 保存修改
				TdTbwRecm recm = new TdTbwRecm();
				recm.setcWhNo(cWhNo);
				recm.setcOwnerNo(cOwnerNo);
				recm.setcRecNo(cRecNo);
				recm.setcPrintTimes(cPrintTimes);
				recm.setcGatherRecNo(cGatherRecNo);
				recm.setcGatherNo(cGatherNo);
				recm.setcInClass(cInClass);
				recm.setcInNo(cInNo);
				recm.setcInType(cInType);
				recm.setcProviderNo(cProviderNo);
				recm.setcPlatNo(cPlatNo);
				recm.setcRecUserno(cRecUserno);
				recm.setcOpTools(cOpTools);
				recm.setcOpStatus(cOpStatus);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				if (cBeginDt != null)
				{
					try
					{
						Date c_beginDt = sdf.parse(cBeginDt);
						recm.setcBeginDt(c_beginDt);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				if (cEndDt != null)
				{
					try
					{
						Date c_endDt = sdf.parse(cEndDt);
						recm.setcEndDt(c_endDt);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				recm.setcNote(cNote);
				recm.setcMkUserno(cMkUserno);
				if (cMkDt != null)
				{
					try
					{
						Date c_mkD = sdf.parse(cMkDt);
						recm.setcBeginDt(c_mkD);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				recm.setcModifiedUserno(cModifiedUserno);
				if (cModifiedDt != null)
				{
					try
					{
						Date c_modifi = sdf.parse(cModifiedDt);
						recm.setcBeginDt(c_modifi);
					}
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				recm.setcPoNo(cPoNo);
				recm.setInitTime(new Date());
				tdTbwRecmService.save(recm);
				
			}
		}
		return "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>不支持该表数据传输："+ STRTABLE +"</MESSAGE></STATUS></RESULTS>";
	}
	
//	private TdRequisitionGoods saveRequisitionGoodsFromOrderGoods(TdOrderGoods orderGoods)
//	{
//		TdRequisitionGoods requisitionGoods = new TdRequisitionGoods();
//		requisitionGoods.setGoodsCode(orderGoods.getSku());
//		requisitionGoods.setGoodsTitle(orderGoods.getGoodsTitle());
//		requisitionGoods.setPrice(orderGoods.getPrice());
//		requisitionGoods.setQuantity(orderGoods.getQuantity());
//		requisitionGoods.setSubOrderNumber(tdOrder.getOrderNumber());
//		requisitionGoods.setOrderNumber(mainOrderNumber);
//		tdRequisitionGoodsService.save(requisitionGoods);
//	}
	
}
// END SNIPPET: service
