package com.ynyes.lyz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.lyz.entity.TdManagerDiySiteRole;
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
}
