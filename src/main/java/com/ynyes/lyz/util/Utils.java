package com.ynyes.lyz.util;

public class Utils {
	
	/**
	 * 检查是否为null 
	 * @param dou 要检查的值
	 * @return 如果为null,返回0.0其他情况直接返回
	 * @author zp
	 */
	public static Double checkNull(Double dou){
		if(dou==null){
			dou=0.0;
		}
		return dou;
	}

}
