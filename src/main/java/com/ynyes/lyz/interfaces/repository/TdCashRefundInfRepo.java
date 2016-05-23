package com.ynyes.lyz.interfaces.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.lyz.interfaces.entity.TdCashRefundInf;

public interface TdCashRefundInfRepo extends PagingAndSortingRepository<TdCashRefundInf, Long>, JpaSpecificationExecutor<TdCashRefundInf> 
{
//	List<TdReturnOrderInf> findByorderHeaderId(Long headerId);
}

