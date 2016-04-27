package com.ynyes.lyz.controller.management;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.lyz.entity.TdDeliveryInfo;
import com.ynyes.lyz.entity.TdDeliveryType;
import com.ynyes.lyz.entity.TdDiySite;
import com.ynyes.lyz.entity.TdManager;
import com.ynyes.lyz.entity.TdManagerRole;
import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.entity.TdOwnMoneyRecord;
import com.ynyes.lyz.entity.TdPayType;
import com.ynyes.lyz.entity.TdPriceList;
import com.ynyes.lyz.entity.TdReturnNote;
import com.ynyes.lyz.entity.TdShippingAddress;
import com.ynyes.lyz.entity.TdUser;
import com.ynyes.lyz.entity.TdWareHouse;
import com.ynyes.lyz.service.TdAgencyFundService;
import com.ynyes.lyz.service.TdArticleService;
import com.ynyes.lyz.service.TdCityService;
import com.ynyes.lyz.service.TdCommonService;
import com.ynyes.lyz.service.TdDeliveryInfoDetailService;
import com.ynyes.lyz.service.TdDeliveryInfoService;
import com.ynyes.lyz.service.TdDeliveryTypeService;
import com.ynyes.lyz.service.TdDistrictService;
import com.ynyes.lyz.service.TdDiySiteService;
import com.ynyes.lyz.service.TdGatheringService;
import com.ynyes.lyz.service.TdGoodsService;
import com.ynyes.lyz.service.TdManagerLogService;
import com.ynyes.lyz.service.TdManagerRoleService;
import com.ynyes.lyz.service.TdManagerService;
import com.ynyes.lyz.service.TdOrderGoodsService;
import com.ynyes.lyz.service.TdOrderService;
import com.ynyes.lyz.service.TdOwnMoneyRecordService;
import com.ynyes.lyz.service.TdPayTypeService;
import com.ynyes.lyz.service.TdPriceCountService;
import com.ynyes.lyz.service.TdPriceListService;
import com.ynyes.lyz.service.TdProductCategoryService;
import com.ynyes.lyz.service.TdReturnNoteService;
import com.ynyes.lyz.service.TdSalesDetailService;
import com.ynyes.lyz.service.TdSettingService;
import com.ynyes.lyz.service.TdShippingAddressService;
import com.ynyes.lyz.service.TdSubdistrictService;
import com.ynyes.lyz.service.TdUserService;
import com.ynyes.lyz.service.TdWareHouseService;
import com.ynyes.lyz.util.SiteMagConstant;

/**
 * 后台首页控制器
 * 
 * @author Sharon
 */

@Controller
@RequestMapping(value = "/Verwalter/order")
public class TdManagerOrderController {

	@Autowired
	TdProductCategoryService tdProductCategoryService;

	@Autowired
	TdArticleService tdArticleService;

	@Autowired
	TdGoodsService tdGoodsService;

	@Autowired
	TdPayTypeService tdPayTypeService;

	@Autowired
	TdDeliveryTypeService tdDeliveryTypeService;

	@Autowired
	TdDiySiteService tdDiySiteService;

	@Autowired
	TdOrderService tdOrderService;

	@Autowired
	TdUserService tdUserService;

	@Autowired
	TdManagerLogService tdManagerLogService;

	@Autowired
	TdSettingService tdSettingService;

	@Autowired
	TdShippingAddressService tdShippingAddressService;

	@Autowired
	TdCityService tdCityService;

	@Autowired
	private TdDistrictService tdDistrictService;

	@Autowired
	private TdSubdistrictService tdSubdistrictService;

	@Autowired
	private TdPriceListService tdPriceListService;
	
	@Autowired
	private TdManagerService tdManagerService;
	
	@Autowired
	private TdManagerRoleService tdManagerRoleService;
	
	@Autowired
	private TdOwnMoneyRecordService tdOwnMoneyRecordService;
	
	@Autowired
	private TdDeliveryInfoService tdDeliveryInfoService;
	
	@Autowired
	private TdDeliveryInfoDetailService tdDeliveryInfoDetailService;
	
	@Autowired
	private TdOrderGoodsService tdOrderGoodsService;
	
	@Autowired
	private TdCommonService tdCommonService;
	
	@Autowired
	private TdReturnNoteService tdReturnNoteService;
	
	@Autowired
	private TdWareHouseService tdWareHouseService;
	
	@Autowired
	private TdAgencyFundService tdAgencyFundService;
	
	@Autowired
	private TdSalesDetailService tdSalesDetailService;
	
	@Autowired
	private TdGatheringService tdGatheringService;
	
	@Autowired
	private TdPriceCountService tdPriceCountService;
	
	 /**
	 * @author lc
	 * @注释：下载
	 */
	public Boolean download(HSSFWorkbook wb, String exportUrl, HttpServletResponse resp,String fileName){
		String filename="table";
		try {
			filename = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			System.out.println("下载文件名格式转换错误！");
		}
		try
		{
			OutputStream os;
			try {
				os = resp.getOutputStream();
				try {
					
					resp.reset();
					resp.setHeader("Content-Disposition", "attachment; filename=" + filename +".xls");
					resp.setContentType("application/octet-stream; charset=utf-8");
					wb.write(os);
					os.flush();
				}
				finally {
					if (os != null) {
						os.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch (Exception e)  
		{  
			e.printStackTrace();  
		} 
		return true;	
	}

	// 城市选择
	@RequestMapping(value = "/city", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> citySelect() {
		Map<String, Object> map = new ModelMap();
		map.put("code", 0);
		map.put("city", tdCityService.findAll());
		map.put("code", 1);
		return map;
	}

	// 行政区划选择
	@RequestMapping(value = "/district", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> districtSelect(Long cityId) {
		Map<String, Object> map = new ModelMap();
		map.put("code", 0);
		map.put("district", tdDistrictService.findByCityIdOrderBySortIdAsc(cityId));
		map.put("code", 1);
		return map;
	}

	// 行政街道选择
	@RequestMapping(value = "/subdistrict", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> subdistrictSelect(Long districtId) {
		Map<String, Object> map = new ModelMap();
		map.put("code", 0);
		map.put("subdistrict", tdSubdistrictService.findByDistrictIdOrderBySortIdAsc(districtId));
		map.put("code", 1);
		return map;
	}

	// 订单设置
	@RequestMapping(value = "/setting/{type}/list")
	public String setting(@PathVariable String type, Integer page, Integer size, String keywords, String __EVENTTARGET,
			String __EVENTARGUMENT, String __VIEWSTATE, Long[] listId, Integer[] listChkId, Double[] listSortId,
			ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		if (null != __EVENTTARGET) {
			if (__EVENTTARGET.equalsIgnoreCase("btnDelete")) {
				btnDelete(type, listId, listChkId);

				if (type.equalsIgnoreCase("pay")) {
					tdManagerLogService.addLog("delete", "删除支付方式", req);
				} else if (type.equalsIgnoreCase("delivery")) {
					tdManagerLogService.addLog("delete", "删除配送方式", req);
				} else if (type.equalsIgnoreCase("diysite")) {
					tdManagerLogService.addLog("delete", "删除门店", req);
				} else if (type.equalsIgnoreCase("codDistrict")) {
					tdManagerLogService.addLog("delete", "删除货到付款地区", req);
				}
			} else if (__EVENTTARGET.equalsIgnoreCase("btnSave")) {
				btnSave(type, listId, listSortId);

				if (type.equalsIgnoreCase("pay")) {
					tdManagerLogService.addLog("edit", "修改支付方式", req);
				} else if (type.equalsIgnoreCase("delivery")) {
					tdManagerLogService.addLog("edit", "修改配送方式", req);
				} else if (type.equalsIgnoreCase("diysite")) {
					tdManagerLogService.addLog("edit", "修改门店", req);
				} else if (type.equalsIgnoreCase("codDistrict")) {
					tdManagerLogService.addLog("edit", "修改货到付款地区", req);
				}
			} else if (__EVENTTARGET.equalsIgnoreCase("btnPage")) {
				if (null != __EVENTARGUMENT) {
					page = Integer.parseInt(__EVENTARGUMENT);
				}
			}
		}

		if (null == page || page < 0) {
			page = 0;
		}

		if (null == size || size <= 0) {
			size = SiteMagConstant.pageSize;
			;
		}

		map.addAttribute("page", page);
		map.addAttribute("size", size);
		map.addAttribute("keywords", keywords);
		map.addAttribute("__EVENTTARGET", __EVENTTARGET);
		map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
		map.addAttribute("__VIEWSTATE", __VIEWSTATE);

		if (null != type) {
			if (type.equalsIgnoreCase("pay")) // 支付方式
			{
				if (null == keywords) {
					map.addAttribute("pay_type_page", tdPayTypeService.findAllOrderBySortIdAsc(page, size));
				} 
				else
				{
					map.addAttribute("pay_type_page", tdPayTypeService.searchAllOrderBySortIdAsc(keywords, page, size));
				}

				return "/site_mag/pay_type_list";
			} else if (type.equalsIgnoreCase("delivery")) // 配送方式
			{
				if (null == keywords) {
					map.addAttribute("delivery_type_page", tdDeliveryTypeService.findAllOrderBySortIdAsc(page, size));
				} else {
					map.addAttribute("delivery_type_page",
							tdDeliveryTypeService.searchAllOrderBySortIdAsc(keywords, page, size));
				}

				return "/site_mag/delivery_type_list";
			} else if (type.equalsIgnoreCase("diysite")) // 门店
			{
				if (null == keywords) {
					map.addAttribute("diy_site_page", tdDiySiteService.findAllOrderBySortIdAsc(page, size));
				} else {
					map.addAttribute("diy_site_page", tdDiySiteService.searchAllOrderBySortIdAsc(keywords, page, size));
				}

				return "/site_mag/diy_site_list";
			}
			// else if (type.equalsIgnoreCase("codDistrict")) // 货到付款地区
			// {
			// if (null == keywords)
			// {
			// map.addAttribute("cod_district_page",
			// tdShippingAddressService.findByIsCod(page, size));
			// }
			// else
			// {
			// map.addAttribute("cod_district_page",
			// tdShippingAddressService.searchBykeywords(keywords, page, size));
			// }
			//
			// return "/site_mag/cod_district_list";
			// }
		}

		return "/site_mag/pay_type_list";
	}
	
	
	// 欠款审核
	@RequestMapping(value = "/own/list")
	public String ownList(HttpServletRequest req, Integer page, Long statusId, Integer size,ModelMap map, String __EVENTTARGET,
			String __EVENTARGUMENT, String __VIEWSTATE,Long[] listChkId,Long payLeft)
	{
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) 
		{
			return "redirect:/Verwalter/login";
		}
		TdManager tdManager = tdManagerService.findByUsernameAndIsEnableTrue(username);
		TdManagerRole tdManagerRole = null;
		String diyCode = null;
		if (null != tdManager && null != tdManager.getRoleId())
		{
			tdManagerRole = tdManagerRoleService.findOne(tdManager.getRoleId());
			if (tdManagerRole == null)
			{
				return "redirect:/Verwalter/login";
			}
			if (tdManagerRole.getTitle().equalsIgnoreCase("门店")) 
			{
				diyCode = tdManagerRole.getTitle();
			}
		}
		
		if (null == page || page < 0) 
		{
			page = 0;
		}
		
		if (null != __EVENTTARGET)
		{
			if (__EVENTTARGET.equalsIgnoreCase("btnPage")) 
			{
				if (null != __EVENTARGUMENT)
				{
					page = Integer.parseInt(__EVENTARGUMENT);
				}
			}
		}

		if (null == size || size <= 0) 
		{
			size = SiteMagConstant.pageSize;
		}
		
		if (tdManagerRole.getTitle().equalsIgnoreCase("门店"))
		{
			map.addAttribute("own_page",tdOwnMoneyRecordService.findByDiyCodeOrderByIdDesc(tdManager.getDiyCode(), page, size));
		}
		else
		{
			map.addAttribute("own_page",tdOwnMoneyRecordService.findAllOrderBySortIdAsc(page, size));
		}
		if (null != __EVENTTARGET)
		{
			if (__EVENTTARGET.equalsIgnoreCase("btnPage")) 
			{
				if (null != __EVENTARGUMENT)
				{
					page = Integer.parseInt(__EVENTARGUMENT);
				}
			}
			else if (__EVENTTARGET.equalsIgnoreCase("statusId")) 
			{
				if (null != statusId)
				{
					if (statusId == 0)
					{
						map.remove("own_page");
						map.addAttribute("own_page", tdOwnMoneyRecordService.findByDiyCodeAndIsEnableOrderByIdDesc(diyCode, false, page, size));
					}
					else if (statusId == 1)
					{
						map.remove("own_page");
						map.addAttribute("own_page", tdOwnMoneyRecordService.findByDiyCodeAndIsEnableOrderByIdDesc(diyCode, true, page, size));
					}
				}
			}
			else if (__EVENTTARGET.equalsIgnoreCase("btnEnable")) 
			{
				if (null != listChkId)
				{
					btnEnale(listChkId);
				}
			}
			else if (__EVENTTARGET.equalsIgnoreCase("btnNotEnable")) 
			{
				if (null != listChkId)
				{
					btnNotEnale(listChkId);
				}
			}
			else if (__EVENTTARGET.equalsIgnoreCase("payLeft")) 
			{
				if (null != payLeft)
				{
					if (payLeft == 0)
					{
						map.remove("own_page");
						map.addAttribute("own_page", tdOwnMoneyRecordService.findByDiyCodeAndIsPayedOrderByIdDesc(diyCode, false, page, size));
					}
					else if (payLeft == 1)
					{
						map.remove("own_page");
						map.addAttribute("own_page", tdOwnMoneyRecordService.findByDiyCodeAndIsPayedOrderByIdDesc(diyCode, true, page, size));
					}
				}
			}
			
		}
		map.addAttribute("page", page);
		map.addAttribute("size", size);
		map.addAttribute("__EVENTTARGET", __EVENTTARGET);
		map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
		map.addAttribute("__VIEWSTATE", __VIEWSTATE);
		map.addAttribute("statusId",statusId);
		map.addAttribute("payLeft",payLeft);
		return "/site_mag/own_list";
	}

	// 订单设置编辑
	@RequestMapping(value = "/setting/{type}/edit")
	public String edit(@PathVariable String type, Long id, String __VIEWSTATE, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		map.addAttribute("__VIEWSTATE", __VIEWSTATE);

		if (null != type) {
			if (type.equalsIgnoreCase("pay")) // 支付方式
			{
				if (null != id) {
					map.addAttribute("pay_type", tdPayTypeService.findOne(id));
				}

				return "/site_mag/pay_type_edit";
			} else if (type.equalsIgnoreCase("delivery")) // 配送方式
			{
				if (null != id) {
					map.addAttribute("delivery_type", tdDeliveryTypeService.findOne(id));
				}

				return "/site_mag/delivery_type_edit";
			} else if (type.equalsIgnoreCase("diysite")) // 门店
			{
				map.addAttribute("price_list", tdPriceListService.findAll());
				if (null != id) {
					TdDiySite tdDiySite = tdDiySiteService.findOne(id);
					List<TdPriceList> price_list = tdPriceListService.findBySobId(tdDiySite.getRegionId());
					if (null != price_list) {
						for (int i = 0; i < price_list.size(); i++) {
							TdPriceList priceList = price_list.get(i);
							Date nowTime = new Date();
							if (priceList.getActiveFlag().equalsIgnoreCase("N")) {
								price_list.remove(priceList);
							}
							if (priceList.getStartDateActive() != null) {
								if (priceList.getStartDateActive().getTime() > nowTime.getTime()) {
									price_list.remove(priceList);
								}
							}
							if (priceList.getEndDateActive() != null) {
								if (priceList.getEndDateActive().getTime() < nowTime.getTime()) {
									price_list.remove(priceList);
								}
							}
						}
					}
					map.addAttribute("choose_price_list", tdPriceListService.findBySobId(tdDiySite.getRegionId()));

					map.addAttribute("diy_site", tdDiySiteService.findOne(id));
				}

				return "/site_mag/diy_site_edit";
			} else if (type.equalsIgnoreCase("codDistrict")) // 货到付款地区
			{
				if (null != id) {
					map.addAttribute("cod_district", tdShippingAddressService.findOne(id));
				}

				return "/site_mag/cod_district_edit";
			}
		}

		return "/site_mag/pay_type_edit";
	}

	@RequestMapping(value = "/edit")
	public String orderEdit(Long id, Long statusId, String __VIEWSTATE, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		map.addAttribute("__VIEWSTATE", __VIEWSTATE);
		map.addAttribute("statusId", statusId);
		if (null != id) {
			TdOrder order=tdOrderService.findOne(id);
			map.addAttribute("order", tdOrderService.findOne(id));
			//仓库
			if(null != order){
				List<TdDeliveryInfo> deliveryList=tdDeliveryInfoService.findByOrderNumberOrderByBeginDtDesc(order.getMainOrderNumber());
				if(null!=deliveryList && deliveryList.size()>0){
					List<TdWareHouse> wareHouseList= tdWareHouseService.findBywhNumberOrderBySortIdAsc(deliveryList.get(0).getWhNo());
					if(null != wareHouseList && wareHouseList.size()>0){
						map.addAttribute("tdWareHouse", wareHouseList.get(0));
					}
				}
			}
		}
		return "/site_mag/order_edit";
	}

	@RequestMapping(value = "/save")
	public String orderEdit(TdOrder tdOrder, Long statusId, String __VIEWSTATE, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username)
		{
			return "redirect:/Verwalter/login";
		}

		map.addAttribute("__VIEWSTATE", __VIEWSTATE);
		map.addAttribute("statusId", statusId);

		tdOrderService.save(tdOrder);

		tdManagerLogService.addLog("edit", "修改订单", req);

		return "redirect:/Verwalter/order/list/" + statusId;
	}

	// 订单列表
	@RequestMapping(value = "/list/{statusId}")
	public String goodsListDialog(String keywords, @PathVariable Long statusId, Integer page, Integer size,
			String __EVENTTARGET, String __EVENTARGUMENT, String __VIEWSTATE, Long[] listId, Integer[] listChkId,
			ModelMap map, HttpServletRequest req,String orderStartTime,String orderEndTime,String realName,String sellerRealName,String shippingAddress,String shippingPhone,
			String deliveryTime,String userPhone,Long orderStatusId,String shippingName,String sendTime,String diyCode,String city) {
		String username = (String) req.getSession().getAttribute("manager");

		if (null == username)
		{
			return "redirect:/Verwalter/login";
		}

		TdManager tdManager = tdManagerService.findByUsernameAndIsEnableTrue(username);
		TdManagerRole tdManagerRole = null;
		if (null != tdManager && null != tdManager.getRoleId())
		{
			tdManagerRole = tdManagerRoleService.findOne(tdManager.getRoleId());
		}
		if (tdManagerRole == null)
		{
			return "redirect:/Verwalter/login";
		}
		
		if (null == page || page < 0) {
			page = 0;
		}

		if (null == size || size <= 0) {
			size = SiteMagConstant.pageSize;
		}
		
		if (null != __EVENTTARGET) {
			if (__EVENTTARGET.equalsIgnoreCase("btnCancel"))
			{
				btnCancel(listId, listChkId);
				tdManagerLogService.addLog("cancel", "取消订单", req);
			} 
			else if (__EVENTTARGET.equalsIgnoreCase("btnConfirm"))
			{
				btnConfirm(listId, listChkId);
				tdManagerLogService.addLog("confirm", "确认订单", req);
			}
			else if (__EVENTTARGET.equalsIgnoreCase("btnDelete"))
			{
				btnDelete(listId, listChkId);
				tdManagerLogService.addLog("delete", "删除订单", req);
			}
			else if (__EVENTTARGET.equalsIgnoreCase("btnPage")) 
			{
				if (null != __EVENTARGUMENT) 
				{
					page = Integer.parseInt(__EVENTARGUMENT);
				}
			}else if(__EVENTTARGET.equals("btnSearch")){
				page=0;
			}
		}

//		if (tdManagerRole.getTitle().equalsIgnoreCase("门店"))
//		{
//			if (null != statusId) 
//			{
//				if (null != keywords && !keywords.equalsIgnoreCase("")) 
//				{
//					map.addAttribute("order_page",tdOrderService.findByDiySiteCodeAndOrderNumberContainingOrDiySiteCodeAndUsernameContainingOrderByIdDesc(tdManager.getDiyCode(), keywords, keywords, 0, 0));
//				}
//				else
//				{
//					if (statusId.equals(0L)) // 全部订单
//					{
//						map.addAttribute("order_page", tdOrderService.findByDiyCode(tdManager.getDiyCode(), page, size));
//					}
//					else
//					{
//						map.addAttribute("order_page", tdOrderService.findByDiyCodeAndStatusIdOrderByIdDesc(tdManager.getDiyCode(), statusId, page, size));
//					}
//				}
//			}
//		}
//		else 
//		{
			if (null != statusId)
			{
				//判断用户是否输入了查询条件
//				Boolean searchCondition= judgeSearchCondition(keywords,orderStartTime,orderEndTime, realName, sellerRealName, shippingAddress, shippingPhone,
//						 deliveryTime, userPhone, shippingName, sendTime);
//				if (searchCondition) 
//				{
				String diySiteCode="";
				if (tdManagerRole.getTitle().equalsIgnoreCase("门店")){
					diySiteCode=tdManager.getDiyCode();
				}else if(tdManagerRole.getIsSys()){
					diySiteCode=diyCode;
				}
				List<String> usernameList=new ArrayList<String>();
				Boolean isNotFindUser=false;
				if(StringUtils.isNotBlank(realName)){ //根据会员真实姓名查询用户名
					List<TdUser> userList= tdUserService.findByRealName(realName);
					if(null != userList && userList.size()>0){
						for (TdUser tdUser : userList) {
							usernameList.add(tdUser.getUsername());
						}
					}else{
						isNotFindUser=true; 
					}
				}
				if(!isNotFindUser){
						map.addAttribute("order_page", tdOrderService.findAll(keywords,orderStartTime,orderEndTime, usernameList, sellerRealName, shippingAddress, shippingPhone,
					 deliveryTime, userPhone, shippingName, sendTime,statusId,diySiteCode,city, size, page));
				}
//				}
//				else
//				{
//					if (statusId.equals(0L)) // 全部订单
//					{
//						map.addAttribute("order_page", tdOrderService.findAllOrderByIdDesc(page, size));
//					} 
//					else 
//					{
//						map.addAttribute("order_page", tdOrderService.findByStatusIdOrderByIdDesc(statusId, page, size));
//					}
//				}
			}
//		}
		
		@SuppressWarnings("unchecked")
		Page<TdOrder> orders1 = (Page<TdOrder>)map.get("order_page");
		if (orders1 != null)
		{
			List<TdOrder> orderlist = orders1.getContent();
			Map<String,String> nameMap = getUserRealNameFormTdOder(orderlist);
			if (nameMap != null)
			{
				map.addAttribute("name_map",nameMap);
			}
		}
		//城市和门店信息
		if (tdManagerRole.getIsSys()){
			map.addAttribute("diySiteList",tdDiySiteService.findAll());
			map.addAttribute("cityList", tdCityService.findAll());
		}
		// 参数注回
		map.addAttribute("orderNumber", keywords);
		map.addAttribute("orderStartTime", orderStartTime);
		map.addAttribute("orderEndTime", orderEndTime);
		map.addAttribute("realName", realName);
		map.addAttribute("sellerRealName", sellerRealName);
		map.addAttribute("shippingAddress", shippingAddress);
		map.addAttribute("shippingPhone", shippingPhone);
		map.addAttribute("deliveryTime", deliveryTime);
		map.addAttribute("userPhone", userPhone);
		map.addAttribute("shippingName", shippingName);
		map.addAttribute("sendTime", sendTime);
		map.addAttribute("statusId", statusId);
		map.addAttribute("diyCode", diyCode);
		map.addAttribute("cityname", city);
		
		map.addAttribute("page", page);
		map.addAttribute("size", size);
		map.addAttribute("keywords", keywords);
		map.addAttribute("statusId", statusId);
		map.addAttribute("__EVENTTARGET", __EVENTTARGET);
		map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
		map.addAttribute("__VIEWSTATE", __VIEWSTATE);

		return "/site_mag/order_list";
	}

	@RequestMapping(value = "/setting/pay/save", method = RequestMethod.POST)
	public String save(TdPayType tdPayType, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		if (null == tdPayType.getId()) {
			tdManagerLogService.addLog("add", "新增支付方式", req);
		} else {
			tdManagerLogService.addLog("edit", "修改支付方式", req);
		}
		tdPayTypeService.save(tdPayType);

		return "redirect:/Verwalter/order/setting/pay/list";
	}

	@RequestMapping(value = "/setting/codDistrict/save", method = RequestMethod.POST)
	public String codDistrictsave(String province, String city, String disctrict, Long codDistrictId, ModelMap map,
			HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		if (null == codDistrictId) {
			tdManagerLogService.addLog("add", "新增货到付款地区", req);
			TdShippingAddress tdShippingAddress = new TdShippingAddress();
			if (null != province) {
				tdShippingAddress.setProvince(province);
			}
			if (null != city) {
				tdShippingAddress.setCity(city);
			}
			if (null != disctrict) {
				tdShippingAddress.setDisctrict(disctrict);
			}
			// tdShippingAddress.setIsCod(true);
			tdShippingAddressService.save(tdShippingAddress);
		} else {
			tdManagerLogService.addLog("edit", "修改货到付款地区", req);

			TdShippingAddress tdShippingAddress = tdShippingAddressService.findOne(codDistrictId);
			if (null != province) {
				tdShippingAddress.setProvince(province);
			}
			if (null != city) {
				tdShippingAddress.setCity(city);
			}
			if (null != disctrict) {
				tdShippingAddress.setDisctrict(disctrict);
			}

			tdShippingAddressService.save(tdShippingAddress);
		}

		return "redirect:/Verwalter/order/setting/codDistrict/list";
	}

	@RequestMapping(value = "/setting/delivery/save", method = RequestMethod.POST)
	public String save(TdDeliveryType tdDeliveryType, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		if (null == tdDeliveryType.getId()) {
			tdManagerLogService.addLog("add", "新增配送方式", req);
		} else {
			tdManagerLogService.addLog("edit", "修改配送方式", req);
		}

		tdDeliveryTypeService.save(tdDeliveryType);

		return "redirect:/Verwalter/order/setting/delivery/list";
	}

	@RequestMapping(value = "/setting/diysite/save", method = RequestMethod.POST)
	public String save(TdDiySite tdDiySite, String[] hid_photo_name_show360, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

//		if (tdDiySite.getCityId() != null)
//		{
//			TdCity tdCity = tdCityService.findOne(tdDiySite.getCityId());
//			tdDiySite.setRegionId(tdCity.getSobIdCity());
//		}
		
		if (null == tdDiySite.getId()) {
			tdManagerLogService.addLog("add", "新增门店", req);
		} else {
			tdManagerLogService.addLog("edit", "修改门店", req);
		}

		tdDiySiteService.save(tdDiySite);

		return "redirect:/Verwalter/order/setting/diysite/list";
	}

	@RequestMapping(value = "/dialog/contact")
	public String addressDialog(ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		return "/site_mag/dialog_contact";
	}

	@RequestMapping(value = "/dialog/delivery")
	public String sendDialog(String orderNumber, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		if (null != orderNumber && !orderNumber.isEmpty()) {
			map.addAttribute("order", tdOrderService.findByOrderNumber(orderNumber));
		}

		map.addAttribute("delivery_type_list", tdDeliveryTypeService.findByIsEnableTrue());

		return "/site_mag/dialog_delivery";
	}

	@RequestMapping(value = "/dialog/print")
	public String printDialog(String orderNumber, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		if (null != orderNumber && !orderNumber.isEmpty()) {
			TdOrder order = tdOrderService.findByOrderNumber(orderNumber);
			map.addAttribute("order", order);
			map.addAttribute("now", new Date());
			map.addAttribute("manager", req.getSession().getAttribute("manager"));

			if (null != order) {
				// map.addAttribute("user",
				// tdUserService.findByUsernameAndIsEnabled(order.getUsername()));
			}
		}

		return "/site_mag/dialog_order_print";
	}

	@RequestMapping(value = "/param/edit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> paramEdit(String orderNumber, String type, String data, String name, String address,
			String postal, String mobile, String expressNumber, Long deliverTypeId, ModelMap map,
			HttpServletRequest req) {

		Map<String, Object> res = new HashMap<String, Object>();

		res.put("code", 1);

		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			res.put("message", "请重新登录");
			return res;
		}

		if (null != orderNumber && !orderNumber.isEmpty() && null != type && !type.isEmpty()) {
			TdOrder order = tdOrderService.findByOrderNumber(orderNumber);

			// 修改备注
			if (type.equalsIgnoreCase("editMark")) {
				order.setRemarkInfo(data);
			}
			// 修改商品总金额
			else if (type.equalsIgnoreCase("editTotalGoodsPrice")) {
				double goodsPrice = Double.parseDouble(data);
				order.setTotalGoodsPrice(goodsPrice);
				order.setTotalPrice(goodsPrice + order.getPayTypeFee() + order.getDeliverFee());
			}
			// 修改配送费用
			else if (type.equalsIgnoreCase("editDeliveryPrice")) {
				double deliveryPrice = Double.parseDouble(data);
				order.setDeliverFee(deliveryPrice);
				order.setTotalPrice(deliveryPrice + order.getPayTypeFee() + order.getTotalGoodsPrice());
			}
			// 修改支付手续费
			else if (type.equalsIgnoreCase("editPayPrice")) {
				double payPrice = Double.parseDouble(data);
				order.setPayTypeFee(payPrice);
				order.setTotalPrice(payPrice + order.getTotalGoodsPrice() + order.getDeliverFee());
			}
			// 修改联系方式
			else if (type.equalsIgnoreCase("editContact")) {
				order.setShippingName(name);
				order.setShippingAddress(address);
				order.setShippingPhone(mobile);
				order.setPostalCode(postal);
			}
			// 确认订单
			else if (type.equalsIgnoreCase("orderConfirm")) {
				if (order.getStatusId().equals(1L)) {
					order.setStatusId(2L);
					order.setCheckTime(new Date());
				}
			}
			// 确认付款
			else if (type.equalsIgnoreCase("orderPay")) {
				if (order.getStatusId().equals(2L)) {
					order.setStatusId(3L);
					order.setPayTime(new Date());

				}
			}
			// 货到付款确认付款
			else if (type.equalsIgnoreCase("orderPayOffline")) {
				if (order.getStatusId().equals(2L) && !order.getIsOnlinePay()) {
					order.setStatusId(5L);
					order.setPayTime(new Date());
				}
			}
			// 确认发货
			else if (type.equalsIgnoreCase("orderDelivery")) {
				if (order.getStatusId().equals(3L)) {
					order.setStatusId(4L);
					order.setSendTime(new Date());
				}
			}
			// 确认收货
			else if (type.equalsIgnoreCase("orderReceive")) {
				if (order.getStatusId().equals(4L)) {
					order.setStatusId(5L);
					order.setReceiveTime(new Date());
				}
			}
			// 确认完成
			else if (type.equalsIgnoreCase("orderFinish")) {
				if (order.getStatusId().equals(5L)) {
					order.setStatusId(6L);
					order.setFinishTime(new Date());

					// tdUserService.addTotalSpend(order.getUsername(),
					// order.getTotalPrice());
				}
			}
			// 确认取消
			else if (type.equalsIgnoreCase("orderCancel")) {
				if (order.getStatusId().equals(1L) || order.getStatusId().equals(2L) || order.getStatusId().equals(3L)) // zhangji
				{
					if (StringUtils.isNotBlank(order.getRemarkInfo()))
					{
						order.setRemarkInfo(order.getRemarkInfo() + "管理员取消订单：" + username);
					}
					else
					{
						order.setRemarkInfo("管理员取消订单：" + username);
					}
					if (null != order && order.getStatusId().equals(3L)) 
					{
						TdReturnNote returnNote = tdCommonService.MakeReturnNote(order,1L,username);
						String realUserName = order.getRealUserUsername();
						if (StringUtils.isBlank(realUserName))
						{
							realUserName = order.getUsername();
						}
						if (StringUtils.isNotBlank(realUserName))
						{
							TdUser realUser = tdUserService.findByUsername(realUserName);
							tdPriceCountService.cashAndCouponBack(order, realUser);
						}
						tdCommonService.sendBackMsgToWMS(returnNote);
					}
					order.setStatusId(7L);
					order.setCancelTime(new Date());
				}
			}
			tdOrderService.save(order);
			tdManagerLogService.addLog("edit", "修改订单", req);

			res.put("code", 0);
			res.put("message", "修改成功!");
			return res;
		}

		res.put("message", "参数错误!");
		return res;
	}

	/*---------------------------------------导入表格 begin -------------------------------*/

	/**************
	 * -----------------------------导入表格 end-----------------------------------
	 */

	@ModelAttribute
	public void getModel(@RequestParam(value = "payTypeId", required = false) Long payTypeId,
			@RequestParam(value = "deliveryTypeId", required = false) Long deliveryTypeId,
			@RequestParam(value = "diySiteId", required = false) Long diySiteId, Model model) {
		if (null != payTypeId) {
			model.addAttribute("tdPayType", tdPayTypeService.findOne(payTypeId));
		}

		if (null != deliveryTypeId) {
			model.addAttribute("tdDeliveryType", tdDeliveryTypeService.findOne(deliveryTypeId));
		}

		if (null != diySiteId) {
			model.addAttribute("tdDiySite", tdDiySiteService.findOne(diySiteId));
		}
	}
	
	@RequestMapping(value = "/own/edit")
	public String ownEdit(Long id, String __VIEWSTATE, ModelMap map, HttpServletRequest req) {
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}

		map.addAttribute("__VIEWSTATE", __VIEWSTATE);
		if (null != id) {
			TdOwnMoneyRecord own= tdOwnMoneyRecordService.findOne(id);
			String mainOrderNumber= own.getOrderNumber();
			System.out.println(mainOrderNumber.substring(2,mainOrderNumber.length()));
			List<TdOrder> orderList=tdOrderService.findByOrderNumberContaining(mainOrderNumber.substring(2,mainOrderNumber.length()));
			map.addAttribute("orderList",orderList);
			map.addAttribute("order", orderList.get(0));
			map.addAttribute("consult", own);
			//仓库
			if(null != orderList){
				List<TdDeliveryInfo> deliveryList=tdDeliveryInfoService.findByOrderNumberOrderByBeginDtDesc(mainOrderNumber);
				if(null!=deliveryList && deliveryList.size()>0){
					List<TdWareHouse> wareHouseList= tdWareHouseService.findBywhNumberOrderBySortIdAsc(deliveryList.get(0).getWhNo());
					if(null != wareHouseList && wareHouseList.size()>0){
						map.addAttribute("tdWareHouse", wareHouseList.get(0));
					}
				}
			}
		}
		return "/site_mag/own_edit";
	}
	@RequestMapping(value = "/own/save")
	@ResponseBody
	public Map<String, Object> ownSave(Long id,Long type, HttpServletRequest req){
		Map<String, Object> map=new HashMap<String,Object>();
		Long[] ids={id};
		if(type==1){
			btnEnale(ids);
		}else{
			btnNotEnale(ids);
		}
		map.put("code", 0);
		return map;
	}

	private void btnSave(String type, Long[] ids, Double[] sortIds) {
		if (null == type || type.isEmpty()) {
			return;
		}

		if (null == ids || null == sortIds || ids.length < 1 || sortIds.length < 1) {
			return;
		}

		for (int i = 0; i < ids.length; i++) {
			Long id = ids[i];

			if (type.equalsIgnoreCase("pay")) {
				TdPayType e = tdPayTypeService.findOne(id);

				if (null != e) {
					if (sortIds.length > i) {
						e.setSortId(new Double(sortIds[i]));
						tdPayTypeService.save(e);
					}
				}
			} else if (type.equalsIgnoreCase("delivery")) {
				TdDeliveryType e = tdDeliveryTypeService.findOne(id);

				if (null != e) {
					if (sortIds.length > i) {
						e.setSortId(new Double(sortIds[i]));
						tdDeliveryTypeService.save(e);
					}
				}
			} else if (type.equalsIgnoreCase("diysite")) {
				TdDiySite e = tdDiySiteService.findOne(id);

				if (null != e) {
					if (sortIds.length > i) {
						e.setSortId(new Double(sortIds[i]));
						tdDiySiteService.save(e);
					}
				}
			} else if (type.equalsIgnoreCase("codDistrict")) {
				TdShippingAddress e = tdShippingAddressService.findOne(id);

				if (null != e) {
					if (sortIds.length > i) {
						e.setSortId(new Double(sortIds[i]));
						tdShippingAddressService.save(e);
					}
				}
			}
		}
	}

	private void btnDelete(String type, Long[] ids, Integer[] chkIds) {
		if (null == type || type.isEmpty()) {
			return;
		}

		if (null == ids || null == chkIds || ids.length < 1 || chkIds.length < 1) {
			return;
		}

		for (int chkId : chkIds) {
			if (chkId >= 0 && ids.length > chkId) {
				Long id = ids[chkId];

				if (type.equalsIgnoreCase("pay")) {
					tdPayTypeService.delete(id);
				} else if (type.equalsIgnoreCase("delivery")) {
					tdDeliveryTypeService.delete(id);
				} else if (type.equalsIgnoreCase("diysite")) {
					tdDiySiteService.delete(id);
				} else if (type.equalsIgnoreCase("codDistrict")) {
					tdShippingAddressService.delete(id);
				}
			}
		}
	}

	private void btnConfirm(Long[] ids, Integer[] chkIds) {
		if (null == ids || null == chkIds || ids.length < 1 || chkIds.length < 1) {
			return;
		}

		for (int chkId : chkIds) {
			if (chkId >= 0 && ids.length > chkId) {
				Long id = ids[chkId];

				TdOrder tdOrder = tdOrderService.findOne(id);

				// 只有待确认(1L)订单能进行确认，确认后状态为待发货(3L)
				if (tdOrder.getStatusId().equals(1L)) {
					tdOrder.setStatusId(3L);
					tdOrder.setCheckTime(new Date()); // 确认时间
					tdOrderService.save(tdOrder);
				}
			}
		}
	}

	private void btnCancel(Long[] ids, Integer[] chkIds) {
		if (null == ids || null == chkIds || ids.length < 1 || chkIds.length < 1) {
			return;
		}

		for (int chkId : chkIds) {
			if (chkId >= 0 && ids.length > chkId) {
				Long id = ids[chkId];

				TdOrder tdOrder = tdOrderService.findOne(id);

				// 只有待确认(1L)、待付款(2L)订单能进行删除，确认后状态为已取消(7L)
				if (tdOrder.getStatusId().equals(1L) || tdOrder.getStatusId().equals(2L)) {
					tdOrder.setStatusId(7L);
					tdOrder.setCancelTime(new Date()); // 取消时间
					tdOrderService.save(tdOrder);
				}
			}
		}
	}

	private void btnDelete(Long[] ids, Integer[] chkIds) {
		if (null == ids || null == chkIds || ids.length < 1 || chkIds.length < 1) {
			return;
		}

		for (int chkId : chkIds) {
			if (chkId >= 0 && ids.length > chkId) {
				Long id = ids[chkId];

				TdOrder tdOrder = tdOrderService.findOne(id);

				// 只有已取消(7L)订单能进行删除
				if (tdOrder.getStatusId().equals(7L)) {
					tdOrderService.delete(tdOrder);
				}
			}
		}
	}
	private void btnEnale(Long[] chkIds) {
		if ( null == chkIds || chkIds.length < 1) {
			return;
		}

		for (Long chkId : chkIds) {
			if (chkId >= 0) {
				TdOwnMoneyRecord ownMoneyRecord = tdOwnMoneyRecordService.findOne(chkId);
				if (ownMoneyRecord != null)
				{
					
					/*//修改订单实际付款  逻辑修改
					TdOrder order=tdOrderService.findByOrderNumber(ownMoneyRecord.getOrderNumber());
					order.setActualPay(order.getActualPay()==null?0:order.getActualPay()+ownMoneyRecord.getPayed());
					tdOrderService.save(order);*/
					
					ownMoneyRecord.setIsEnable(true);
					ownMoneyRecord.setIspassed(true);
					tdOwnMoneyRecordService.save(ownMoneyRecord);
				}
			}
		}
	}
	private void btnNotEnale(Long[] chkIds) {
		if ( null == chkIds || chkIds.length < 1) {
			return;
		}

		for (Long chkId : chkIds) {
			if (chkId >= 0) {
				TdOwnMoneyRecord ownMoneyRecord = tdOwnMoneyRecordService.findOne(chkId);
				if (ownMoneyRecord != null)
				{
					ownMoneyRecord.setIsEnable(true);
					ownMoneyRecord.setIspassed(false);
					tdOwnMoneyRecordService.save(ownMoneyRecord);
				}
			}
		}
	}
	
	private Map<String,String> getUserRealNameFormTdOder(List<TdOrder> orders)
	{
		
		Map<String, String> map = new HashMap<>();
		for (TdOrder tdOrder : orders)
		{
			String username = tdOrder.getUsername();
			if(map.containsKey(username))
			{
				continue;
			}
			else
			{
				TdUser tdUser = tdUserService.findByUsername(username);
				if(null != tdUser && StringUtils.isNotBlank(username)){
					map.put(username, tdUser.getRealName());
				}
			}
		}

		return map;
	}
    
	
	
}
