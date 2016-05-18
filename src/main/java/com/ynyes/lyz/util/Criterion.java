package com.ynyes.lyz.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 条件接口
 * 用户提供条件表达式接口
 * @Class Name Criterion
 * @Author zp
 * @Create In 2012-2-8
 */
public interface Criterion {
	
	
		public enum Operator {
			EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR,NULL,NOTNULL
		}
		public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
				CriteriaBuilder builder);

}
