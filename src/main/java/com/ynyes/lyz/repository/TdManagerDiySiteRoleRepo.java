package com.ynyes.lyz.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ynyes.lyz.entity.TdManagerDiySiteRole;

/**
 * TdManagerDiySiteRole 实体数据库操作接口
 * 
 * @author Shawn
 *
 */

public interface TdManagerDiySiteRoleRepo extends
		PagingAndSortingRepository<TdManagerDiySiteRole, Long>,
		JpaSpecificationExecutor<TdManagerDiySiteRole> 
{

}
