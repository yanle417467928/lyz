package com.ynyes.lyz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdBalanceLog;
import com.ynyes.lyz.repository.TdBalanceLogRepo;

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
		if(null == id){
			return null;
		}
		return repository.findOne(id);
	}
	
	public List<TdBalanceLog> findAll(){
		return (List<TdBalanceLog>) repository.findAll();
	}
	
	/**
	 * 查找指定用户的钱包操作记录（按照生成时间倒序排序）
	 * 
	 * @author dengxiao
	 */
	public List<TdBalanceLog> findByUserIdOrderByCreateTimeDesc(Long userId){
		if(null == userId){
			return null;
		}
		return repository.findByUserIdOrderByCreateTimeDesc(userId);
	}
	
	/**
	 * 查找指定用户的钱包操作记录——分页（按照生成时间倒序排序）
	 * 
	 * @author dengxiao
	 */
	public Page<TdBalanceLog> findByUserIdAndIsSuccessTrueOrderByCreateTimeDesc(Long userId,int page,int size){
		if(null == userId){
			return null;
		}
		PageRequest pageRequest = new PageRequest(page, size);
		return repository.findByUserIdAndIsSuccessTrueOrderByCreateTimeDesc(userId, pageRequest);
	}
}
