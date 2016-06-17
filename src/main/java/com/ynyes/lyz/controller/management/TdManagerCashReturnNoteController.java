package com.ynyes.lyz.controller.management;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ynyes.lyz.entity.TdCashReturnNote;
import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.entity.TdReturnNote;
import com.ynyes.lyz.service.TdCashReturnNoteService;
import com.ynyes.lyz.service.TdOrderService;
import com.ynyes.lyz.service.TdReturnNoteService;
import com.ynyes.lyz.util.SiteMagConstant;

/**
 * <p>标题：TdManagerCashReturnNoteController.java</p>
 * <p>描述：后台与退款申请相关的控制器</p>
 * <p>公司：http://www.ynsite.com</p>
 * @author 作者：DengXiao
 * @version 版本：上午10:43:33
 */
@Controller
@RequestMapping(value = "/Verwalter/cash/return/note")
public class TdManagerCashReturnNoteController {

	@Autowired
	private TdCashReturnNoteService tdCashReturnNoteService;
	
	@Autowired
	private TdOrderService tdOrderService;
	
	@Autowired
	private TdReturnNoteService tdReturnNoteService;
	
	@RequestMapping(value = "/list")
	public String cashReturnNoteList(Integer page, Integer size, String keywords,Long type, String __EVENTTARGET, String __EVENTARGUMENT,
			String __VIEWSTATE, HttpServletRequest req, ModelMap map,Long cityCode,Long diyCode){
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}
		if(null == page){
			page = 0;
		}
		if(null == size){
			size = SiteMagConstant.pageSize;
		}
		
		//表示点击了某项申请的“已经打款”按钮，__EVENTARGUMENT表示打款申请单的id
		if(null != __EVENTARGUMENT){
			returnMoney(__EVENTARGUMENT);
		}
		
		Page<TdCashReturnNote> cashReturnNote_page = tdCashReturnNoteService.findAll(page, size);
		
		map.addAttribute("cashReturnNote_page", cashReturnNote_page);
		map.addAttribute("page", page);
		map.addAttribute("keywords", keywords);
		return "/site_mag/cash_return_note_list";
	}

	/**
	 * 确认打款的方法
	 * @author 作者：DengXiao
	 * @version 版本：2016年6月17日上午11:38:07
	 */
	private void returnMoney(String sId) {
		if(null == sId){
			return;
		}
		
		try {
			Long id = Long.parseLong(sId);
			TdCashReturnNote note = tdCashReturnNoteService.findOne(id);
			//第一步操作：改变退款申请单的状态
			note.setFinishTime(new Date());
			note.setIsOperated(true);
			tdCashReturnNoteService.save(note);
			
			//第二步操作：修改分单状态
			String orderNumber = note.getOrderNumber();
			TdOrder order = tdOrderService.findByOrderNumber(orderNumber);
			/*
			 * 在此进行对分单的操作，如果不需要对分单进行操作请删除上段代码
			 */
			//------------------------对分单的操作---------------------------
			
			
			tdOrderService.save(order);
			//------------------------操作结束------------------------------
			
			
			
			//第三步：对退货单进行操作
			String returnNoteNumber = note.getReturnNoteNumber();
			TdReturnNote returnNote = tdReturnNoteService.findByReturnNumber(returnNoteNumber);
			/*
			 * 在此进行对退货单的操作，如果不需要对退货单进行操作请删除上段代码
			 */
			//------------------------对退货单的操作---------------------------
			
			
			tdReturnNoteService.save(returnNote);
			//------------------------操作结束------------------------------
		} catch (Exception e) {
			//转换类型失败之后也不继续处理
			e.printStackTrace();
			return;
		}
	}
}
