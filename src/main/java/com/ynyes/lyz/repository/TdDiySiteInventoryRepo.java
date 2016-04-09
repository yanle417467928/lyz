package com.ynyes.lyz.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.lyz.entity.TdDiySiteInventory;

public interface TdDiySiteInventoryRepo
		extends PagingAndSortingRepository<TdDiySiteInventory, Long>, JpaSpecificationExecutor<TdDiySiteInventory> {

}
