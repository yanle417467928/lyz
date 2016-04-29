package com.ynyes.lyz.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.lyz.entity.TdDiySiteInventory;

public interface TdDiySiteInventoryRepo extends PagingAndSortingRepository<TdDiySiteInventory, Long>, JpaSpecificationExecutor<TdDiySiteInventory> 
{
	TdDiySiteInventory findByGoodsCodeAndDiySiteId(String goodsCode,Long siteId);
	
	TdDiySiteInventory findBygoodsCodeAndDiySiteIdNull(String goodsCode);

	Page<TdDiySiteInventory> findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(String code,String title,Pageable pageable);
	
	Page<TdDiySiteInventory> findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(Long regionId,String code,Long regionid,String title,Pageable pageable);
	
	Page<TdDiySiteInventory> findByDiySiteIdAndGoodsCodeContainingOrDiySiteIdAndGoodsTitleContainingOrderByIdAsc(Long siteId,String code,Long siteid,String title,Pageable pageable);
	
	List<TdDiySiteInventory> findByDiySiteId(Long siteId);
	
	Page<TdDiySiteInventory> findByDiySiteId(Long siteId,Pageable pageable);
	
	Page<TdDiySiteInventory> findByRegionIdOrderByIdAsc(Long regionId,Pageable pageable);
	
	TdDiySiteInventory findByGoodsCodeAndRegionId(String goodsCode,Long regionId);
}
