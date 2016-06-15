package com.ynyes.lyz.controller.management;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ynyes.lyz.entity.TdBalanceLog;
import com.ynyes.lyz.service.TdBalanceLogService;
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

	@RequestMapping(value = "/list")
	public String balanceList(Integer page, Integer size, String keywords,Long type, String __EVENTTARGET, String __EVENTARGUMENT,
			String __VIEWSTATE, HttpServletRequest req, ModelMap map) {

		if (null == page || page < 0) {
			page = 0;
		}

		if (null == size || size <= 0) {
			size = SiteMagConstant.pageSize;
		}

		if (null != keywords) {
			keywords = keywords.trim();
		}

		Page<TdBalanceLog> balance_page = tdBalanceLogService.findAll(page, size);
		map.addAttribute("balance_page", balance_page);
		
		map.addAttribute("page", page);
		map.addAttribute("size", size);
		map.addAttribute("keywords", keywords);
		return "/site_mag/balance_log_list";
	}
}
