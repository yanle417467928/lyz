package com.ynyes.lyz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.lyz.entity.TdSetting;
import com.ynyes.lyz.repository.TdSettingRepo;

/**
 * TdSetting 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdSettingService {
    
    @Autowired
    TdSettingRepo repository;
    
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
    public void delete(TdSetting e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdSetting> entities)
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
    public TdSetting findOne(Long id)
    {
        if (null == id)
        {
            return null;
        }
        
        return repository.findOne(id);
    }
    
    public TdSetting findTopBy()
    {
        return repository.findTopBy();
    }
    
    /**
     * 查找
     * 
     * @param ids
     * @return
     */
    public List<TdSetting> findAll(Iterable<Long> ids)
    {
        return (List<TdSetting>) repository.findAll(ids);
    }
    
    public List<TdSetting> findAll()
    {
        return (List<TdSetting>) repository.findAll();
    }
    
    public Page<TdSetting> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    public List<TdSetting> findAllOrderBySortIdAsc()
    {
        return (List<TdSetting>) repository.findAll(new Sort(Direction.ASC, "sortId"));
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdSetting save(TdSetting e)
    {
        return repository.save(e);
    }
    
    public List<TdSetting> save(List<TdSetting> entities)
    {
        
        return (List<TdSetting>) repository.save(entities);
    }
}
