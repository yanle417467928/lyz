package com.ynyes.lyz.interfaces.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.lyz.interfaces.entity.TdOrderInf;

public interface TdOrderInfRepo
		extends PagingAndSortingRepository<TdOrderInf, Long>, JpaSpecificationExecutor<TdOrderInf> 
{
	TdOrderInf findByOrderNumber(String orderNumber);
}

