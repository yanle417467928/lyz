package com.ynyes.lyz.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdDiySiteInventory;
import com.ynyes.lyz.entity.TdDiySiteInventoryLog;
import com.ynyes.lyz.repository.TdDiySiteInventoryLogRepo;

import scala.noinline;

@Service
@Transactional
public class TdDiySiteInventoryLogService {

	@Autowired
	private TdDiySiteInventoryLogRepo repository;

	public TdDiySiteInventoryLog save(TdDiySiteInventoryLog e) {
		if (null == e) {
			return null;
		}
		return repository.save(e);
	}

	public void delete(Long id) {
		if (null != id) {
			repository.delete(id);
		}
	}

	public TdDiySiteInventoryLog findOne(Long id) {
		if (null == id) {
			return null;
		}
		return repository.findOne(id);
	}

	public List<TdDiySiteInventoryLog> findAll() 
	{
		return (List<TdDiySiteInventoryLog>) repository.findAll();
	}
	
	public void saveWithRequsition(HttpServletRequest req,Long oldValue,Long newValue)
	{
		TdDiySiteInventoryLog log = new TdDiySiteInventoryLog();
		log.setChangeDate(new Date());
	}
	
	public Boolean saveChangeLog(TdDiySiteInventory diySiteInventory,Long changeValue,String orderNumber,HttpServletRequest req)
	{
		String username = null;
		
		String changeType = "管理员修改";
		if (orderNumber == null && req != null)
		{
			username = (String) req.getSession().getAttribute("manager");
			changeType = "订单修改";
			if (username == null)
			{
				return false;
			}
		}
		
		TdDiySiteInventoryLog log = new TdDiySiteInventoryLog();
		log.setDiySiteTitle(diySiteInventory.getDiySiteName());
		log.setRegionName(diySiteInventory.getRegionName());
		log.setRegionId(diySiteInventory.getRegionId());
		log.setDiySiteId(diySiteInventory.getDiySiteId());
		log.setGoodsId(diySiteInventory.getGoodsId());
		log.setGoodsTitle(diySiteInventory.getGoodsTitle());
		log.setGoodsSku(diySiteInventory.getGoodsCode());
		log.setChangeValue(changeValue);
		log.setChangeDate(new Date());
		log.setDescription(changeType);
		log.setOrderNumber(orderNumber);
		log.setManager(username);
		return true;
		
	}
}
