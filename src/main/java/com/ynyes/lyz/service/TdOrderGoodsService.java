package com.ynyes.lyz.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.entity.TdOrderGoods;
import com.ynyes.lyz.repository.TdOrderGoodsRepo;


/**
 * TdOrderGoods 服务类
 * 
 * @author Sharon
 *
 */

@Service
@Transactional
public class TdOrderGoodsService {
    
    @Autowired
    TdOrderGoodsRepo repository;
    @Autowired
    TdCartGoodsService tdCartGoodsService;
    
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
    public void delete(TdOrderGoods e)
    {
        if (null != e)
        {
            repository.delete(e);
        }
    }
    
    public void delete(List<TdOrderGoods> entities)
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
    public TdOrderGoods findOne(Long id)
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
    public List<TdOrderGoods> findAll(Iterable<Long> ids)
    {
        return (List<TdOrderGoods>) repository.findAll(ids);
    }
    
    public List<TdOrderGoods> findAll()
    {
        return (List<TdOrderGoods>) repository.findAll();
    }
    
    public Page<TdOrderGoods> findAllOrderBySortIdAsc(int page, int size)
    {
        PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, "sortId"));
        
        return repository.findAll(pageRequest);
    }
    
    /**
     * 保存
     * 
     * @param e
     * @return
     */
    public TdOrderGoods save(TdOrderGoods e)
    {
        
        return repository.save(e);
    }
    
    public List<TdOrderGoods> save(List<TdOrderGoods> entities)
    {
        
        return (List<TdOrderGoods>) repository.save(entities);
    }
    
    /**
     * 修改订单商品归属活动
     * @param order 订单
     * @param cost 活动所需要的商品及其数量的列表
     * @param activityId 活动id
     * @author zp
     */
    public void updateOrderGoodsActivity(TdOrder order,Map<Long, Long> cost,Long activityId){
    	// 获取用户的已选
    	List<TdOrderGoods> orderGoodsList= order.getOrderGoodsList();
    	//循环所有商品
    	if(orderGoodsList!=null && orderGoodsList.size()>0){
    		for (TdOrderGoods orderGood : orderGoodsList) {
    			//判断是否是要修改的商品
    			for (Long id : cost.keySet()) {
    				if(orderGood.getGoodsId().equals(id)){
        				String activityIds=orderGood.getActivityId();
        				if(StringUtils.isNotBlank(activityIds)){
        					orderGood.setActivityId(activityIds+","+activityId.toString());
        				}else{
        					orderGood.setActivityId(activityId.toString());
        				}
        				
        			}
				}
			}
    	}
    	//保存
    	order.setOrderGoodsList(orderGoodsList);
    }
}
