package com.ynyes.lyz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.lyz.entity.TdDiySiteInventory;

public interface TdDiySiteInventoryRepo
		extends PagingAndSortingRepository<TdDiySiteInventory, Long>, JpaSpecificationExecutor<TdDiySiteInventory> {

	Page<TdDiySiteInventory> findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(String code,String title,Pageable pageable);
	
	Page<TdDiySiteInventory> findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(String code,String title,Pageable pageable);
	
}
