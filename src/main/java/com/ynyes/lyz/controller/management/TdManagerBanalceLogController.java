package com.ynyes.lyz.controller.management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.lyz.entity.TdBalanceLog;
import com.ynyes.lyz.entity.TdCity;
import com.ynyes.lyz.entity.TdCoupon;
import com.ynyes.lyz.entity.TdDiySite;
import com.ynyes.lyz.entity.TdManager;
import com.ynyes.lyz.entity.TdManagerRole;
import com.ynyes.lyz.entity.TdUser;
import com.ynyes.lyz.service.TdBalanceLogService;
import com.ynyes.lyz.service.TdDiySiteRoleService;
import com.ynyes.lyz.service.TdUserService;
import com.ynyes.lyz.util.SiteMagConstant;

/**
 * <p>
 * 标题：TdManagerBanalceLogController.java
 * </p>
 * <p>
 * 描述：后台管理系统余额变更明细相关的控制器
 * </p>
 * <p>
 * 公司：http://www.ynsite.com
 * </p>
 * 
 * @author 作者：DengXiao
 * @version 版本：下午2:07:56
 */
@Controller
@RequestMapping(value = "/Verwalter/balance")
public class TdManagerBanalceLogController extends TdManagerBaseController {

	@Autowired
	private TdBalanceLogService tdBalanceLogService;
	@Autowired
	private TdDiySiteRoleService tdDiySiteRoleService;
	@Autowired   
	private TdUserService tdUserService;

	@RequestMapping(value = "/list")
	public String balanceList(Integer page, Integer size, String keywords,Long type, String __EVENTTARGET, String __EVENTARGUMENT,
			String __VIEWSTATE, HttpServletRequest req, ModelMap map,Long cityCode,Long diyCode,String startTime, String endTime) {

		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}
		//获取管理员管辖城市
    	List<TdCity> cityList= new ArrayList<TdCity>();
    	//获取管理员管辖门店
    	List<TdDiySite> diyList=new ArrayList<TdDiySite>(); 
    	
    	//管理员获取管辖的城市和门店
    	tdDiySiteRoleService.userRoleCityAndDiy(cityList, diyList, username);
    	
    	if (null != __EVENTTARGET) {
			if (__EVENTTARGET.equalsIgnoreCase("btnPage")) {
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
		}

		if (null != keywords) {
			keywords = keywords.trim();
		}
		
		//修改城市刷新门店列表
		if(cityCode!=null){
			//需要删除的diy
			List<TdDiySite> diyRemoveList=new ArrayList<TdDiySite>(); 
			for (TdDiySite tdDiySite : diyList) {
				if(!cityCode.equals(tdDiySite.getRegionId())){
					diyRemoveList.add(tdDiySite);
					if(tdDiySite.getId()==diyCode){
						diyCode=null;
					}
				}
			}
			diyList.removeAll(diyRemoveList);
		}
		//获取管辖门店id列表
		List<Long> roleDiyIds=new ArrayList<Long>();
		if(diyList!=null && diyList.size()>0){
			for (TdDiySite diy : diyList) {
				roleDiyIds.add(diy.getId());
			}
		}
		
//		Page<TdBalanceLog> balance_page = tdBalanceLogService.findAll(page, size);
		Page<TdBalanceLog> balance_page = tdBalanceLogService.searchList(keywords, roleDiyIds, type, page, size,cityCode,diyCode,startTime,endTime);
		map.addAttribute("balance_page", balance_page);
		
		map.addAttribute("page", page);
		map.addAttribute("size", size);
		map.addAttribute("keywords", keywords);
		map.addAttribute("type", type);
		map.addAttribute("cityList", cityList);
		map.addAttribute("diySiteList", diyList);
		map.addAttribute("cityCode", cityCode);
		map.addAttribute("diyCode", diyCode);
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		
		return "/site_mag/balance_log_list";
	}
	
	/*
	 * 预存款记录报表
	 */
	@RequestMapping(value = "/downdata")
	@ResponseBody
	public String dowmData(HttpServletRequest req, ModelMap map, String begindata, String enddata,
			HttpServletResponse response, Long cityCode, Long diyCode,String keywords,Long type) {

		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}
		
		//获取管理员管辖城市
    	List<TdCity> cityList= new ArrayList<TdCity>();
    	//获取管理员管辖门店
    	List<TdDiySite> diyList=new ArrayList<TdDiySite>(); 
    	
    	//管理员获取管辖的城市和门店
    	tdDiySiteRoleService.userRoleCityAndDiy(cityList, diyList, username);
    	
    	//获取管辖门店id列表
    	List<Long> roleDiyIds=new ArrayList<Long>();
    	if(diyList!=null && diyList.size()>0){
    		for (TdDiySite diy : diyList) {
    			roleDiyIds.add(diy.getId());
    		}
    	}
    	//报表数据
    	List<TdBalanceLog> balance_page = tdBalanceLogService.searchList(keywords, roleDiyIds, type,cityCode,diyCode,begindata,enddata);
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("领用记录报表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		// 列宽
		int[] widths = { 25, 13, 25, 25, 18, 11, 13, 11, 19, 12, 9, 13, 13, 13, 13, 40, 40 };
		sheetColumnWidth(sheet, widths);

		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setWrapText(true);

		// 优惠券名称、金额、领卷时间、领用用户、是否使用、使用的时间、使用订单号
		HSSFRow row = sheet.createRow((int) 0);
//
//		String[] cellValues = { "优惠券类型", "优惠券名称", "金额", "劵的来源", "劵的归属", "领卷时间", "领用用户", "领用人姓名", "券状态", "失效时间", "是否使用",
//				"使用时间", "使用订单号", "城市名称", "门店名称" };
//		cellDates(cellValues, style, row);
//
//		// 第五步，设置值
//		List<TdCoupon> coupon = null;
//		// coupon=tdCouponService.findByIsDistributtedTrueOrderByIdDesc();
//
//		if (tdManagerRole.getIsSys() && null != cityId) {
//			coupon = tdCouponService.findByGetTimeAndCityIdOrderByGetTimeDesc(date1, date2, cityId);
//		} else {
//			TdCity city = tdCityService.findByCityName(user.getCityName());
//			diyCode = user.getDiyCode();
//			if (null != city) {
//				coupon = tdCouponService.findByGetTimeAndCityIdOrderByGetTimeDesc(date1, date2, city.getId());
//			}
//		}
//		// 获取所有的会员
//		List<TdUser> userList = tdUseService.findByUserTypeOrderByIdDesc(0L);
//		// 存放会员信息的map
//		Map<String, Object> userMap = new HashMap<String, Object>();
//		if (userList != null && userList.size() > 0) {
//			for (TdUser tdUser : userList) {
//				userMap.put(tdUser.getUsername() + "name", tdUser.getRealName());
//				userMap.put(tdUser.getUsername() + "diyName", tdUser.getDiyName());
//				userMap.put(tdUser.getUsername() + "diyCode", tdUser.getDiyCode());
//			}
//		}
//
//		Integer i = 0;
//		for (TdCoupon tdCoupon : coupon) {
//			if (StringUtils.isBlank(diyCode)
//					|| (null != diyCode && diyCode.equals(userMap.get(tdCoupon.getUsername() + "diyName")))) {
//
//				row = sheet.createRow((int) i + 1);
//				if (null != tdCoupon.getTypeCategoryId()) {// 优惠券类型
//					if (tdCoupon.getTypeCategoryId().equals(1L)) {
//						row.createCell(0).setCellValue("通用现金券");
//					}
//					if (tdCoupon.getTypeCategoryId().equals(2L)) {
//						row.createCell(0).setCellValue("指定商品现金券");
//					}
//					if (tdCoupon.getTypeCategoryId().equals(3L)) {
//						row.createCell(0).setCellValue("产品券");
//					}
//				}
//
//				if (null != tdCoupon.getTypeTitle()) {// 优惠券名称
//					row.createCell(1).setCellValue(tdCoupon.getTypeTitle());
//				}
//				if (null != tdCoupon.getPrice()) {// 金额
//					row.createCell(2).setCellValue(tdCoupon.getPrice());
//				}
//				if (null != tdCoupon.getTypeId()) {// 劵的来源
//					if (tdCoupon.getCustomerId() != null) {
//						row.createCell(3).setCellValue("期初导入");
//					} else {
//						if (tdCoupon.getTypeId().equals(1L)) {
//							row.createCell(3).setCellValue("促销发劵");
//						}
//						if (tdCoupon.getTypeId().equals(2L)) {
//							row.createCell(3).setCellValue("抢劵");
//						}
//					}
//
//				}
//				if (null != tdCoupon.getBrandTitle()) {// 劵的归属
//					row.createCell(4).setCellValue(tdCoupon.getBrandTitle());
//				}
//				if (null != tdCoupon.getGetTime()) {// 领卷时间
//					Date getTime = tdCoupon.getGetTime();
//					String couponTimeStr = getTime.toString();
//					row.createCell(5).setCellValue(couponTimeStr);
//				}
//				if (null != tdCoupon.getUsername()) {// 领用用户
//					row.createCell(6).setCellValue(tdCoupon.getUsername());
//				}
//				if (null != userMap.get(tdCoupon.getUsername() + "name")) {// 领用人姓名
//					row.createCell(7).setCellValue(userMap.get(tdCoupon.getUsername() + "name").toString());
//				}
//				if (null != tdCoupon.getIsOutDate()) {// 券状态
//					if (tdCoupon.getIsOutDate()) {
//						row.createCell(8).setCellValue("失效");
//					} else {
//						row.createCell(8).setCellValue("生效");
//					}
//				}
//				if (null != tdCoupon.getExpireTime()) {// 失效时间
//					row.createCell(9).setCellValue(tdCoupon.getExpireTime().toString());
//				}
//
//				if (null != tdCoupon.getIsUsed()) {// 是否使用
//					if (tdCoupon.getIsUsed()) {
//						row.createCell(10).setCellValue("是");
//						String couponUserTimeStr = "";
//						if (null != tdCoupon.getUseTime()) {
//							Date userTime = tdCoupon.getUseTime();
//							couponUserTimeStr = userTime.toString();
//						}
//						// 使用时间
//						row.createCell(11).setCellValue(couponUserTimeStr);
//						// 使用单号
//						row.createCell(12).setCellValue(tdCoupon.getOrderNumber());
//					} else {
//						row.createCell(10).setCellValue("否");
//					}
//
//				}
//				if (null != tdCoupon.getCityName()) {
//					row.createCell(13).setCellValue(tdCoupon.getCityName());
//				}
//				if (null != userMap.get(tdCoupon.getUsername() + "diyName")) {
//					row.createCell(14).setCellValue(userMap.get(tdCoupon.getUsername() + "diyName").toString());
//				}
//
//				i++;
//			}
//		}
//
//		String exportAllUrl = SiteMagConstant.backupPath;
//		download(wb, exportAllUrl, response, "领用记录");
		return "";
	}
}
