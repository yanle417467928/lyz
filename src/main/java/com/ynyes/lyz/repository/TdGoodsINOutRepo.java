package com.ynyes.lyz.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.lyz.entity.TdGoodsInOut;


/**
 * TdGoodsINOutRepo 进出货明细表
 * 
 * @author 
 *
 */

public interface TdGoodsINOutRepo extends PagingAndSortingRepository<TdGoodsInOut, Long>, JpaSpecificationExecutor<TdGoodsInOut> {
	
	/**
	 * 调用存储过程
	 * @return
	 */
	@Query(value = "{call insert_goods_in_out_initial(?1,?2,?3)}",nativeQuery = true)
	void callinsertGoodsInOutInitial(Date start,Date end,String username);
}
