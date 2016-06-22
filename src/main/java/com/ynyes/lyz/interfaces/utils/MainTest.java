package com.ynyes.lyz.interfaces.utils;

public class MainTest {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// 测试
//			TdInterfaceService.getCall();
//			String doubleStr = "80.00";
//			Double doubleOfStr = Double.parseDouble(doubleStr);
//			try
//			{
//				Long longOfStr = Long.parseLong(doubleStr);
//			}
//			catch (Exception e)
//			{
//				// TODO: handle exception
//			}
//			
//			Long longOfDou = doubleOfStr.longValue();
//			Double cRecQty = Double.parseDouble(doubleStr);
//			cRecQty = cRecQty * 100;
//			System.out.println(cRecQty);
//			Long recQtyFromDouble = cRecQty.longValue();
//			recQtyFromDouble = recQtyFromDouble / 100;
//			System.out.println("------------" + "======" + recQtyFromDouble);
			
			Double myDouble = 0.0;
			
			if (myDouble == 0)
			{
				System.out.println("误解");
			}
			
			System.err.println(myDouble > 0);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}
