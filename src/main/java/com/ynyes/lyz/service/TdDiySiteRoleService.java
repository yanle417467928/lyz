package com.ynyes.lyz.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.lyz.entity.TdManager;
import com.ynyes.lyz.entity.TdManagerDiySiteRole;
import com.ynyes.lyz.entity.TdManagerRole;
import com.ynyes.lyz.repository.TdManagerDiySiteRoleRepo;

/**
 * TdManagerDiySiteRole 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdDiySiteRoleService {
    
    @Autowired
    TdManagerDiySiteRoleRepo repository;
    
    /**
     * 删除
     * 
     * @param id 菜单项ID
     */
    public void delete(Long id)
    {
        if (null != id)
        {
            repository.delete(id);
        }
    }
    
    /**
     * 删除
     * 
     * @param e 菜单项
     */
    public void delete(TdManagerDiySiteRole e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdManagerDiySiteRole> entities)
    {
        if (null != entities)
        {
            repository.delete(entities);
        }
    }
    
    /**
     * 查找
     * 
     * @param id ID
     * @return
     */
    public TdManagerDiySiteRole findOne(Long id)
    {
        if (null == id)
        {
            return null;
        }
        
        return repository.findOne(id);
    }
    
    /**
     * 查找
     * 
     * @param ids
     * @return
     */
    public List<TdManagerDiySiteRole> findAll(Iterable<Long> ids)
    {
        return (List<TdManagerDiySiteRole>) repository.findAll(ids);
    }
    
    public List<TdManagerDiySiteRole> findAll()
    {
        return (List<TdManagerDiySiteRole>) repository.findAll();
    }
    
    public Page<TdManagerDiySiteRole> findAll(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
        
        return repository.findAll(pageRequest);
    }
    
    public List<TdManagerDiySiteRole> findAllOrderBySortIdAsc()
    {
        return (List<TdManagerDiySiteRole>) repository.findAll(new Sort(Direction.ASC, "sortId"));
    }
    
    
    public List<TdManagerDiySiteRole> save(List<TdManagerDiySiteRole> entities)
    {
        return (List<TdManagerDiySiteRole>) repository.save(entities);
    }

    public TdManagerDiySiteRole save(TdManagerDiySiteRole entity)
    {
        return repository.save(entity);
    }
    /**
	 * 根据角色名查询
	 * @param title 角色名
	 * @return 结果集
	 * @author zp
	 */
    public TdManagerDiySiteRole findByTitle(String title){
    	List<TdManagerDiySiteRole> diySiteRoleList= repository.findByTitle(title);
    	if(diySiteRoleList!=null && diySiteRoleList.size()>0){
    		return diySiteRoleList.get(0);
    	}
    	return null;
    }
    /**
     * 获取管理员管辖门店id
     * @param tdManagerRole 管理员权限
     * @param tdManager 管理员
     * @return
     */
    public List<String> userRoleDiyId(TdManagerRole tdManagerRole,TdManager tdManager){
    	//用户管辖门店编号 超级管理员为空
    	List<String> roleDiyIds=new ArrayList<String>();

    	//非超级管理员 添加管辖门店
    	if(!tdManagerRole.getIsSys()){
    		roleDiyIds.add(tdManager.getDiyCode());
    	}
    	//查询用户管辖门店权限
    	TdManagerDiySiteRole diySiteRole= this.findByTitle(tdManagerRole.getTitle());
    	//非超级管理员 添加管辖门店和城市
    	if(diySiteRole!=null && !diySiteRole.getIsSys()){
    		//不为空是添加管辖门店
    		if(StringUtils.isNotBlank(diySiteRole.getDiySiteTree())){
    			//分割字符串 
    			String[] diyIds= diySiteRole.getDiySiteTree().replace("[", ",").replace("]", ",").split(",");
    			//循环添加管辖门店
    			for (String diyId : diyIds) {
    				//判断不为空添加管辖门店  (去掉用户可能重复存在的门店编号:roleDiyIds.contains(diyId))
    				if(StringUtils.isNotBlank(diyId)){
    					roleDiyIds.add(diyId);
    				}
    			}
    		}

    	}
    	return roleDiyIds;
	}
}
