package com.ynyes.lyz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdCity;
import com.ynyes.lyz.entity.TdDiySiteInventory;
import com.ynyes.lyz.entity.TdGoods;
import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.entity.TdOrderGoods;
import com.ynyes.lyz.repository.TdDiySiteInventoryRepo;
import com.ynyes.lyz.repository.TdGoodsRepo;

@Service
@Transactional
public class TdDiySiteInventoryService {

	@Autowired
	private TdDiySiteInventoryRepo repository;
	
	@Autowired
	private TdGoodsRepo goodsRepo;

	public TdDiySiteInventory save(TdDiySiteInventory e) {
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

	public TdDiySiteInventory findOne(Long id) {
		if (null == id) {
			return null;
		}
		return repository.findOne(id);
	}

	public List<TdDiySiteInventory> findAll() {
		return (List<TdDiySiteInventory>) repository.findAll();
	}
	
	public TdDiySiteInventory findByGoodsCodeAndDiySiteId(String goodsCode,Long siteId)
	{
		if (StringUtils.isBlank(goodsCode) || siteId == null)
		{
			return null;
		}
		return repository.findByGoodsCodeAndDiySiteId(goodsCode, siteId);
	}
	
//	public TdDiySiteInventory findBygoodsCodeAndDiySiteIdNull(String goodsCode)
//	{
//		if (StringUtils.isBlank(goodsCode))
//		{
//			return null;
//		}
//		return repository.findBygoodsCodeAndDiySiteIdNull(goodsCode);
//	}
	
	public Page<TdDiySiteInventory> findAll(String keywords,int page,int size)
	{
		PageRequest pageRequest = new PageRequest(page, size);
		if (StringUtils.isNotBlank(keywords)) 
			return repository.findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(keywords, keywords, pageRequest);
		return repository.findAll(pageRequest);
	}
	
	public Page<TdDiySiteInventory> findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(String keywords,int page,int size)
	{
		PageRequest pageRequest = new PageRequest(page, size);
		return repository.findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(keywords, keywords, pageRequest);
	}
	
	public Page<TdDiySiteInventory> findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(Long regionId,String keywords,int page,int size)
	{
		PageRequest pageRequest = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords))
			return repository.findByRegionIdOrderByIdAsc(regionId, pageRequest);
		return repository.findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(regionId, keywords, regionId, keywords, pageRequest);
	}
	
	public Page<TdDiySiteInventory> findBySiteIdAndKeywords(Long siteId,String keywords,int page,int size)
	{
		PageRequest pageable = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords))
			return repository.findByDiySiteId(siteId, pageable);
		return repository.findByDiySiteIdAndGoodsCodeContainingOrDiySiteIdAndGoodsTitleContainingOrderByIdAsc(siteId, keywords, siteId, keywords, pageable);
	}
	
	public Page<TdDiySiteInventory> findByRegionIdAndKeywords(Long regionId,String keywords,int page,int size) 
	{
		PageRequest pageable = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords)) 
			return repository.findByRegionIdOrderByIdAsc(regionId, pageable);
		return repository.findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(regionId, keywords, regionId, keywords, pageable);
	}
	
	public Page<TdDiySiteInventory> findByRegionIdOnlyAndKeywords(Long regionId,String keywords,int page,int size) 
	{
		PageRequest pageable = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords)) 
			return repository.findByRegionIdAndDiySiteIdIsNull(regionId, pageable);
		return repository.findByRegionIdAndDiySiteIdIsNullAndGoodsCodeContainingOrRegionIdAndDiySiteIdIsNullAndGoodsTitleContainingOrderByIdAsc(regionId, keywords, regionId, keywords, pageable);
	}
	
	public List<TdDiySiteInventory> findByDiySiteId(Long code)
	{
		return repository.findByDiySiteId(code);
	}
	public TdDiySiteInventory findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(String goodsCode,Long regionId)
	{
		if (StringUtils.isBlank(goodsCode) || regionId == null)
			return null;
		return repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(goodsCode, regionId);
	}
	
	/**
	 * 根据订单扣减库存
	 * @param order 订单
	 * @param city 城市
	 * @author zp
	 */
	public void changeGoodsInventory(TdOrder order,TdCity city){
		if(!"门店自提".equals(order.getDeliverTypeTitle())){ //扣减城市库存
			//商品列表
			List<TdOrderGoods> orderGoodsList= order.getOrderGoodsList();
			for (TdOrderGoods tdOrderGoods : orderGoodsList) {
				if(tdOrderGoods!=null){
					TdDiySiteInventory  diySiteInventory = repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(tdOrderGoods.getSku(), city.getSobIdCity());
					//扣减库存
					if(diySiteInventory!=null  ){
						diySiteInventory.setInventory(diySiteInventory.getInventory()-tdOrderGoods.getQuantity());
					}else{
						diySiteInventory=saveInventoryByGoods(order, tdOrderGoods, city, 1L);
						diySiteInventory.setInventory(-tdOrderGoods.getQuantity());
					}
					repository.save(diySiteInventory);
				}

			}
			//赠品列表
			List<TdOrderGoods> giftGoodsList= order.getGiftGoodsList();
			for (TdOrderGoods giftGoods : giftGoodsList) {
				if(giftGoods!=null){
					TdDiySiteInventory  diySiteInventory = repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(giftGoods.getSku(), city.getSobIdCity());
					//扣减库存
					if(diySiteInventory!=null  ){
						diySiteInventory.setInventory(diySiteInventory.getInventory()-giftGoods.getQuantity());
					}else{
						diySiteInventory=saveInventoryByGoods(order, giftGoods, city, 1L);
						diySiteInventory.setInventory(-giftGoods.getQuantity());
					}
					repository.save(diySiteInventory);
				}

			}
			//小辅料列表
			List<TdOrderGoods> presentedList= order.getPresentedList();
			for (TdOrderGoods presented : presentedList) {
				if(presented!=null){
					TdDiySiteInventory  diySiteInventory = repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(presented.getSku(), city.getSobIdCity());
					//扣减库存
					if(diySiteInventory!=null  ){
						diySiteInventory.setInventory(diySiteInventory.getInventory()-presented.getQuantity());
					}else{
						diySiteInventory=saveInventoryByGoods(order, presented, city, 1L);
						diySiteInventory.setInventory(-presented.getQuantity());
					}
					repository.save(diySiteInventory);
				}

			}
		}else{ //扣减门店库存
			//商品列表
			List<TdOrderGoods> orderGoodsList= order.getOrderGoodsList();
			for (TdOrderGoods tdOrderGoods : orderGoodsList) {
				if(tdOrderGoods!=null){
					TdDiySiteInventory  diySiteInventory = repository.findByGoodsCodeAndDiySiteId(tdOrderGoods.getSku(), order.getDiySiteId());
					//扣减库存
					if(diySiteInventory!=null  ){
						diySiteInventory.setInventory(diySiteInventory.getInventory()-tdOrderGoods.getQuantity());
					}else{
						diySiteInventory=saveInventoryByGoods(order, tdOrderGoods, city, 2L);
						diySiteInventory.setInventory(-tdOrderGoods.getQuantity());
					}
					repository.save(diySiteInventory);
				}
			}
			//赠品列表
			List<TdOrderGoods> giftGoodsList= order.getGiftGoodsList();
			for (TdOrderGoods giftGoods : giftGoodsList) {
				if(giftGoods!=null){
					TdDiySiteInventory  diySiteInventory = repository.findByGoodsCodeAndDiySiteId(giftGoods.getSku(), order.getDiySiteId());
					//扣减库存
					if(diySiteInventory!=null  ){
						diySiteInventory.setInventory(diySiteInventory.getInventory()-giftGoods.getQuantity());
					}else{
						diySiteInventory=saveInventoryByGoods(order, giftGoods, city, 2L);
						diySiteInventory.setInventory(-giftGoods.getQuantity());
					}
					repository.save(diySiteInventory);
				}
				
			}
			//小辅料列表
			List<TdOrderGoods> presentedList= order.getPresentedList();
			for (TdOrderGoods presented : presentedList) {
				if(presented!=null){
					TdDiySiteInventory  diySiteInventory = repository.findByGoodsCodeAndDiySiteId(presented.getSku(), order.getDiySiteId());
					//扣减库存
					if(diySiteInventory!=null  ){
						diySiteInventory.setInventory(diySiteInventory.getInventory()-presented.getQuantity());
					}else{
						diySiteInventory=saveInventoryByGoods(order, presented, city, 2L);
						diySiteInventory.setInventory(-presented.getQuantity());
					}
					repository.save(diySiteInventory);
				}
			}
		}

	}
	/**
	 * 新增库存
	 * @param tdGoods 订单商品
	 * @param city 城市
	 * @param type 1城市库存 2门店库存
	 */
	public TdDiySiteInventory saveInventoryByGoods(TdOrder order,TdOrderGoods orderGoods,TdCity city,Long type){
		TdDiySiteInventory inventory = new TdDiySiteInventory();
		TdGoods tdGoods= goodsRepo.findOne(orderGoods.getGoodsId());
		if(type==1L){//城市
			inventory.setInventory(0L);
			inventory.setGoodsCode(tdGoods.getCode());
			inventory.setGoodsId(tdGoods.getId());
			inventory.setCategoryId(tdGoods.getCategoryId());
			inventory.setCategoryIdTree(tdGoods.getCategoryIdTree());
			inventory.setCategoryTitle(tdGoods.getCategoryTitle());
			inventory.setGoodsTitle(tdGoods.getTitle());
			inventory.setRegionId(city.getSobIdCity());
			inventory.setRegionName(city.getCityName());
		}else if(type==2L){//门店
			inventory.setInventory(0L);
			inventory.setDiySiteId(order.getDiySiteId());
			inventory.setDiySiteName(order.getDiySiteName());
			inventory.setGoodsCode(tdGoods.getCode());
			inventory.setGoodsId(tdGoods.getId());
			inventory.setCategoryId(tdGoods.getCategoryId());
			inventory.setCategoryIdTree(tdGoods.getCategoryIdTree());
			inventory.setCategoryTitle(tdGoods.getCategoryTitle());
			inventory.setDiyCode(order.getDiySiteCode());
			inventory.setGoodsTitle(tdGoods.getTitle());
			inventory.setRegionId(city.getSobIdCity());
			inventory.setRegionName(city.getCityName());
		}
		return inventory;
	}
	
}
