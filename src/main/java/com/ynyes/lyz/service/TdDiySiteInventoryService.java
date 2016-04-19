package com.ynyes.lyz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdDiySiteInventory;
import com.ynyes.lyz.repository.TdDiySiteInventoryRepo;

@Service
@Transactional
public class TdDiySiteInventoryService {

	@Autowired
	private TdDiySiteInventoryRepo repository;

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
	
	public TdDiySiteInventory findBygoodsCodeAndDiySiteIdNull(String goodsCode)
	{
		if (StringUtils.isBlank(goodsCode))
		{
			return null;
		}
		return repository.findBygoodsCodeAndDiySiteIdNull(goodsCode);
	}
	
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
	
	public List<TdDiySiteInventory> findByDiySiteId(Long code)
	{
		return repository.findByDiySiteId(code);
	}
	

}
