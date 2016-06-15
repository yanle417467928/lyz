package com.ynyes.lyz.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ynyes.lyz.entity.TdCity;
import com.ynyes.lyz.entity.TdDiySiteInventory;
import com.ynyes.lyz.entity.TdGoods;
import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.entity.TdOrderGoods;
import com.ynyes.lyz.entity.TdReturnNote;
import com.ynyes.lyz.repository.TdCityRepo;
import com.ynyes.lyz.repository.TdDiySiteInventoryRepo;
import com.ynyes.lyz.repository.TdGoodsRepo;
import com.ynyes.lyz.repository.TdOrderRepo;

@Service
@Transactional
public class TdDiySiteInventoryService {

	@Autowired
	private TdDiySiteInventoryRepo repository;

	@Autowired
	private TdGoodsRepo goodsRepo;

	@Autowired
	private TdOrderRepo orderRepo;

	@Autowired
	private TdCityRepo tdCityRepo;

	@Autowired
	private TdDiySiteInventoryLogService tdDiySiteInventoryLogService;

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

	public TdDiySiteInventory findByGoodsCodeAndDiySiteId(String goodsCode, Long siteId) {
		if (StringUtils.isBlank(goodsCode) || siteId == null) {
			return null;
		}
		return repository.findByGoodsCodeAndDiySiteId(goodsCode, siteId);
	}

	// public TdDiySiteInventory findBygoodsCodeAndDiySiteIdNull(String
	// goodsCode)
	// {
	// if (StringUtils.isBlank(goodsCode))
	// {
	// return null;
	// }
	// return repository.findBygoodsCodeAndDiySiteIdNull(goodsCode);
	// }

	public Page<TdDiySiteInventory> findAll(String keywords, int page, int size) {
		PageRequest pageRequest = new PageRequest(page, size);
		if (StringUtils.isNotBlank(keywords))
			return repository.findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(keywords, keywords,
					pageRequest);
		return repository.findAll(pageRequest);
	}

	public Page<TdDiySiteInventory> findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(String keywords,
			int page, int size) {
		PageRequest pageRequest = new PageRequest(page, size);
		return repository.findByGoodsCodeContainingOrGoodsTitleContainingOrderByIdAsc(keywords, keywords, pageRequest);
	}

	public Page<TdDiySiteInventory> findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(
			Long regionId, String keywords, int page, int size) {
		PageRequest pageRequest = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords))
			return repository.findByRegionIdOrderByIdAsc(regionId, pageRequest);
		return repository.findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(regionId,
				keywords, regionId, keywords, pageRequest);
	}

	public Page<TdDiySiteInventory> findBySiteIdAndKeywords(Long siteId, String keywords, int page, int size) {
		PageRequest pageable = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords))
			return repository.findByDiySiteId(siteId, pageable);
		return repository.findByDiySiteIdAndGoodsCodeContainingOrDiySiteIdAndGoodsTitleContainingOrderByIdAsc(siteId,
				keywords, siteId, keywords, pageable);
	}

	public Page<TdDiySiteInventory> findByRegionIdAndKeywords(Long regionId, String keywords, int page, int size) {
		PageRequest pageable = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords))
			return repository.findByRegionIdOrderByIdAsc(regionId, pageable);
		return repository.findByRegionIdAndGoodsCodeContainingOrRegionIdAndGoodsTitleContainingOrderByIdAsc(regionId,
				keywords, regionId, keywords, pageable);
	}

	public Page<TdDiySiteInventory> findByRegionIdOnlyAndKeywords(Long regionId, String keywords, int page, int size) {
		PageRequest pageable = new PageRequest(page, size);
		if (StringUtils.isBlank(keywords))
			return repository.findByRegionIdAndDiySiteIdIsNull(regionId, pageable);
		return repository
				.findByRegionIdAndDiySiteIdIsNullAndGoodsCodeContainingOrRegionIdAndDiySiteIdIsNullAndGoodsTitleContainingOrderByIdAsc(
						regionId, keywords, regionId, keywords, pageable);
	}

	public List<TdDiySiteInventory> findByDiySiteId(Long code) {
		return repository.findByDiySiteId(code);
	}

	public TdDiySiteInventory findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(String goodsCode, Long regionId) {
		if (StringUtils.isBlank(goodsCode) || regionId == null)
			return null;
		return repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(goodsCode, regionId);
	}

	/**
	 * 根据订单增减库存
	 * 
	 * @param order
	 *            订单
	 * @param type
	 *            1赠 2减
	 * @param changeName
	 *            修改库存类型名称
	 * @author zp
	 */
	public void changeGoodsInventory(TdOrder order, Long type, HttpServletRequest req, String changeName) {
		TdCity city = tdCityRepo.findByCityName(order.getCity());
		// 订单类型
		Long orderType = 1L;
		if (order.getIsCoupon() != null && order.getIsCoupon() == true) {
			return;
		}
		// 判断订单类型
		if ("门店自提".equals(order.getDeliverTypeTitle())) {
			orderType = 2L;
			changeName = "自提单" + changeName;
		} else {
			changeName = "配送" + changeName;
		}

		// 商品列表
		List<TdOrderGoods> orderGoodsList = order.getOrderGoodsList();
		for (TdOrderGoods tdOrderGoods : orderGoodsList) {
			if (tdOrderGoods != null) {
				TdDiySiteInventory diySiteInventory = null;
				if (orderType == 2L) {
					// 门店库存
					diySiteInventory = repository.findByGoodsCodeAndDiySiteId(tdOrderGoods.getSku(),
							order.getDiySiteId());
				} else {
					// 城市库存
					diySiteInventory = repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(tdOrderGoods.getSku(),
							city.getSobIdCity());
				}

				// 要修改库存数量
				Long quantitiy = tdOrderGoods.getQuantity();
				if (type == 2L) { // type为2时减库存
					quantitiy = -quantitiy;
				}

				// 修改库存
				if (diySiteInventory != null) {
					diySiteInventory.setInventory(diySiteInventory.getInventory() + quantitiy);
				} else {
					diySiteInventory = saveInventoryByGoods(order, tdOrderGoods, city, orderType);
					diySiteInventory.setInventory(quantitiy);
				}
				// 保存
				repository.save(diySiteInventory);
				// 保存日志
				tdDiySiteInventoryLogService.saveChangeLog(diySiteInventory, quantitiy, order.getOrderNumber(), req,
						changeName);
			}

		}

		// 赠品列表
		List<TdOrderGoods> giftGoodsList = order.getGiftGoodsList();
		for (TdOrderGoods giftGoods : giftGoodsList) {
			if (giftGoods != null) {
				TdDiySiteInventory diySiteInventory = null;
				if (orderType == 2L) {
					// 门店库存
					diySiteInventory = repository.findByGoodsCodeAndDiySiteId(giftGoods.getSku(), order.getDiySiteId());
				} else {
					// 城市库存
					diySiteInventory = repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(giftGoods.getSku(),
							city.getSobIdCity());
				}
				// 要修改库存数量
				Long quantitiy = giftGoods.getQuantity();
				if (type == 2L) { // type为2时减库存
					quantitiy = -quantitiy;
				}
				// 修改库存
				if (diySiteInventory != null) {
					diySiteInventory.setInventory(diySiteInventory.getInventory() + quantitiy);
				} else {
					diySiteInventory = saveInventoryByGoods(order, giftGoods, city, orderType);
					diySiteInventory.setInventory(quantitiy);
				}
				repository.save(diySiteInventory);
				// 保存日志
				tdDiySiteInventoryLogService.saveChangeLog(diySiteInventory, quantitiy, order.getOrderNumber(), req,
						changeName);
			}

		}
		// 小辅料列表
		List<TdOrderGoods> presentedList = order.getPresentedList();
		for (TdOrderGoods presented : presentedList) {
			if (presented != null) {
				TdDiySiteInventory diySiteInventory = null;
				if (orderType == 2L) {
					// 门店库存
					diySiteInventory = repository.findByGoodsCodeAndDiySiteId(presented.getSku(), order.getDiySiteId());
				} else {
					// 城市库存
					diySiteInventory = repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(presented.getSku(),
							city.getSobIdCity());
				}
				// 要修改库存数量
				Long quantitiy = presented.getQuantity();
				if (type == 2L) { // type为2时减库存
					quantitiy = -quantitiy;
				}
				// 修改库存
				if (diySiteInventory != null) {
					diySiteInventory.setInventory(diySiteInventory.getInventory() + quantitiy);
				} else {
					diySiteInventory = saveInventoryByGoods(order, presented, city, orderType);
					diySiteInventory.setInventory(quantitiy);
				}
				repository.save(diySiteInventory);
				// 保存日志
				tdDiySiteInventoryLogService.saveChangeLog(diySiteInventory, quantitiy, order.getOrderNumber(), req,
						changeName);
			}

		}
	}

	/**
	 * 新增库存
	 * 
	 * @param tdGoods
	 *            订单商品
	 * @param city
	 *            城市
	 * @param type
	 *            1城市库存 2门店库存
	 */
	public TdDiySiteInventory saveInventoryByGoods(TdOrder order, TdOrderGoods orderGoods, TdCity city, Long type) {
		TdDiySiteInventory inventory = new TdDiySiteInventory();
		TdGoods tdGoods = goodsRepo.findOne(orderGoods.getGoodsId());
		if (type == 1L) {// 城市
			inventory.setInventory(0L);
			inventory.setGoodsCode(tdGoods.getCode());
			inventory.setGoodsId(tdGoods.getId());
			inventory.setCategoryId(tdGoods.getCategoryId());
			inventory.setCategoryIdTree(tdGoods.getCategoryIdTree());
			inventory.setCategoryTitle(tdGoods.getCategoryTitle());
			inventory.setGoodsTitle(tdGoods.getTitle());
			inventory.setRegionId(city.getSobIdCity());
			inventory.setRegionName(city.getCityName());
		} else if (type == 2L) {// 门店
			inventory.setInventory(0L);
			inventory.setDiySiteId(order.getDiySiteId());
			inventory.setDiySiteName(order.getDiySiteName());
			inventory.setGoodsCode(tdGoods.getCode());
			inventory.setGoodsId(tdGoods.getId());
			inventory.setCategoryId(tdGoods.getCategoryId());
			inventory.setCategoryIdTree(tdGoods.getCategoryIdTree());
			inventory.setCategoryTitle(tdGoods.getCategoryTitle());
			inventory.setDiyCode(order.getDiySiteCode());
			inventory.setGoodsTitle(tdGoods.getTitle());
			inventory.setRegionId(city.getSobIdCity());
			inventory.setRegionName(city.getCityName());
		}
		return inventory;
	}

	/**
	 * 根据退货单增加库存
	 * 
	 * @param returnNote
	 *            退货单
	 * @param changeName
	 *            修改库存类型名称
	 * @author zp
	 */
	public void changeGoodsInventory(TdReturnNote returnNote, HttpServletRequest req) {
		TdOrder order = orderRepo.findByOrderNumber(returnNote.getOrderNumber());
		if (order != null && null != order.getIsCoupon() && order.getIsCoupon() == true) {
			return;
		}
		TdCity city = tdCityRepo.findByCityName(order.getCity());
		// 订单类型
		Long orderType = 1L;
		String changeName = "配送退货";
		// 判断订单类型
		if ("门店自提".equals(order.getDeliverTypeTitle())) {
			orderType = 2L;
			changeName = "自提单退货";
		}
		List<TdOrderGoods> returnGoodsList = returnNote.getReturnGoodsList();
		for (TdOrderGoods retrunGoods : returnGoodsList) {
			if (retrunGoods != null) {
				TdDiySiteInventory diySiteInventory = null;
				if (orderType == 2L) {
					// 门店库存
					diySiteInventory = repository.findByGoodsCodeAndDiySiteId(retrunGoods.getSku(),
							order.getDiySiteId());
				} else {
					// 城市库存
					diySiteInventory = repository.findByGoodsCodeAndRegionIdAndDiySiteIdIsNull(retrunGoods.getSku(),
							city.getSobIdCity());
				}

				// 要修改库存数量
				Long quantitiy = retrunGoods.getQuantity();

				// 修改库存
				if (diySiteInventory != null) {
					diySiteInventory.setInventory(diySiteInventory.getInventory() + quantitiy);
				} else {
					diySiteInventory = saveInventoryByGoods(order, retrunGoods, city, orderType);
					diySiteInventory.setInventory(+quantitiy);
				}
				// 保存
				repository.save(diySiteInventory);
				// 保存日志
				tdDiySiteInventoryLogService.saveChangeLog(diySiteInventory, quantitiy, returnNote.getReturnNumber(),
						req, changeName);
			}
		}
	}
}
