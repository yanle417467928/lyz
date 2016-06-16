package com.ynyes.lyz.controller.management;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ynyes.lyz.entity.TdBalanceLog;
import com.ynyes.lyz.entity.TdCity;
import com.ynyes.lyz.entity.TdDiySite;
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
public class TdManagerBanalceLogController {

	@Autowired
	private TdBalanceLogService tdBalanceLogService;
	@Autowired
	private TdDiySiteRoleService tdDiySiteRoleService;
	@Autowired   
	private TdUserService tdUserService;

	@RequestMapping(value = "/list")
	public String balanceList(Integer page, Integer size, String keywords,Long type, String __EVENTTARGET, String __EVENTARGUMENT,
			String __VIEWSTATE, HttpServletRequest req, ModelMap map,Long cityCode,Long diyCode) {

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
		//查询的用户列表
		List<TdUser> userList=null;
		List<Long> userIdList=new ArrayList<Long>();
		//选择了门店 查询门店下面的所有用户
		if(diyCode!=null){
			userList= tdUserService.findByUpperDiySiteId(diyCode);
			cityCode=null;
		}
		//选择城市 查询城市下面所有用户
		if(cityCode==null){
			userList=tdUserService.findByCityId(cityCode);
		}
		//默认查询全部
		if(userList==null){
			userList=tdUserService.findAll();
		}
		//取出用户id列表
		if(userIdList!=null){
			for (TdUser user : userList) {
				userIdList.add(user.getId());
			}
		}
		
		
//		Page<TdBalanceLog> balance_page = tdBalanceLogService.findAll(page, size);
		Page<TdBalanceLog> balance_page = tdBalanceLogService.searchList(keywords, userIdList, type, page, size);
		map.addAttribute("balance_page", balance_page);
		
		map.addAttribute("page", page);
		map.addAttribute("size", size);
		map.addAttribute("keywords", keywords);
		map.addAttribute("type", type);
		map.addAttribute("cityList", cityList);
		map.addAttribute("diySiteList", diyList);
		map.addAttribute("cityCode", cityCode);
		map.addAttribute("diyCode", diyCode);
		
		return "/site_mag/balance_log_list";
	}
}
