package com.ynyes.lyz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdBalanceLog;
import com.ynyes.lyz.repository.TdBalanceLogRepo;
import com.ynyes.lyz.util.Criteria;
import com.ynyes.lyz.util.Restrictions;

@Service
@Transactional
public class TdBalanceLogService {

	@Autowired
	private TdBalanceLogRepo repository;

	public TdBalanceLog save(TdBalanceLog e) {
		if (null == e) {
			return null;
		}
		return repository.save(e);
	}

	public void delete(Long id) {
		if (null != id) {
			repository.delete(id);
		}
	}

	public TdBalanceLog findOne(Long id) {
		if (null == id) {
			return null;
		}
		return repository.findOne(id);
	}

	public List<TdBalanceLog> findAll() {
		return (List<TdBalanceLog>) repository.findAll();
	}

	/**
	 * 查找指定用户的钱包操作记录（按照生成时间倒序排序）
	 * 
	 * @author dengxiao
	 */
	public List<TdBalanceLog> findByUserIdOrderByCreateTimeDesc(Long userId) {
		if (null == userId) {
			return null;
		}
		return repository.findByUserIdOrderByCreateTimeDesc(userId);
	}

	/**
	 * 查找指定用户的钱包操作记录——分页（按照生成时间倒序排序）
	 * 
	 * @author dengxiao
	 */
	public Page<TdBalanceLog> findByUserIdAndIsSuccessTrueOrderByCreateTimeDesc(Long userId, int page, int size) {
		if (null == userId) {
			return null;
		}
		PageRequest pageRequest = new PageRequest(page, size);
		return repository.findByUserIdAndIsSuccessTrueOrderByCreateTimeDesc(userId, pageRequest);
	}

	public Page<TdBalanceLog> findAll(int page, int size) {
		PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "finishTime"));
		return repository.findAll(pageRequest);
	}
	
	public Page<TdBalanceLog> searchList(String keywords,List<Long> userIdList,Long type, int page, int size){
		PageRequest pageRequest = new PageRequest(page, size);
		Criteria<TdBalanceLog> c = new Criteria<TdBalanceLog>();
		//用户名
		if (StringUtils.isNotBlank(keywords)) {
			c.add(Restrictions.or(Restrictions.like("orderNumber",keywords, true),Restrictions.like("username", keywords, true)));
		}
//		if(userIdList!=null && userIdList.size()>0){
//			c.add(Restrictions.in("userId", userIdList, true));
//		}
		if (type!=null) {
			c.add(Restrictions.eq("type", type, true));
		}
		
		c.setOrderByDesc("finishTime");
		return repository.findAll(c,pageRequest);
	}
}
