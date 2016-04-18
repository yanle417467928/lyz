package com.ynyes.lyz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdDiySiteInventory;
import com.ynyes.lyz.repository.TdDiySiteInventoryRepo;

@Service
@Transactional
public class TdDiySiteInventoryService {

	@Autowired
	private TdDiySiteInventoryRepo repository;

	public TdDiySiteInventory save(TdDiySiteInventory e) {
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

	public TdDiySiteInventory findOne(Long id) {
		if (null == id) {
			return null;
		}
		return repository.findOne(id);
	}

	public List<TdDiySiteInventory> findAll() {
		return (List<TdDiySiteInventory>) repository.findAll();
	}
	
	public Page<TdDiySiteInventory> findAll(int page,int size)
	{
		PageRequest pageRequest = new PageRequest(page, size);
		return repository.findAll(pageRequest);
	}

}
