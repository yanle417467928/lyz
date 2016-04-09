架构:SpringMvc+MYSQL5.0+freemark
包命名：
src/main/java:核心业务处理
	controller:--业务处理
		management:代表后台处理相关：
		front：前台业务处理相关;
		
src/main/resources：所有页面相关，
		static:js/css/images。。。存放目录
		templates:所有页面存放目录
			management:代表后台页面相关：
			front：前台页面处理相关;
				index.ftl:代表首页页面
				login.ftl:代表登录页面
				news.ftl:代表新闻资讯页面
				ProductContrast.ftl:代表具体商品详情页面
				type_list_accessories.ftl:代表手机配件页面
				type_list_content.ftl:代表商品对比页面
				type_list_mobile.ftl:代表手机产品页面
				type_list_number.ftl：代表靓号选择页面
				type_list_star.ftl：代表明星产品
				afshelp:代表存放售后服务页面相关
				cart：	代表存放购物车，下单。。。页面相关
				comment：引入文件相关
				distribution：代表存放支付配送页面相关
				news：代表存放新闻资讯具体页面页面相关
				order：代表存放订单页面页面相关
				question：代表存放新手入门页面相关
				user：代表存放用户中心页面相关
前台url:
		/ ---代表访问主页
		商品

		/list/0 --代表访问相关商品类型typeid=1(1代表进入明星产品,2代表进入手机产品，3代表搜集配件，4代表靓号选择,5代表新闻资讯)
		/product/0  --代表具体商品详情typeid=1	
		/ProductContrast --代表访问商品对比                 ？？未找到
		
		登录注册
		/login ----代表跳转登录页面
		/reg --代表跳转注册页面
		
		帮助中心
		/help/question/1	--代表跳转帮助中心 新手入门模块 -(1代表购物指南)，(2代表常见问题)，(3代表用户协议)															
		/help/distribution/1	--代表跳转帮助中心 支付配送模块 -(1代表支付方式)，(2代表配送方式)，(3代表配送费用)															
		/help/afshelp/1     --代表跳转帮助中心 售后服务模块  -(1代表退货政策)，(2代表退货流程)(3,代表三包服务)
		
		用户
		/user/info  ---跳转用户个人信息
		/user/updatePassword  ---跳转修改密码
		/user/address  ---跳转收货地址
		/user/collect  ---跳转我的收藏
		/user/point  ---跳转我的积分
		
		订单
		/order/list --跳转所有订单页面
		/order/obligation --跳转待付款订单页面
		/order/startorder --跳转待收货订单页面
		/order/orderok --跳转已完成订单页面
		/order/orderno --跳转已关闭订单页面
		
		新闻资讯
		/news/1     --代表跳转帮助中心  -(1代表手机资讯)，(2代表手机发烧友),(3，代表新闻视频)
		
		下单，购物车：
			/cart   --代表跳转我的购物车
			/cartStep   --代表跳转订单信息填写
			/cartFinish  --代表跳转银行卡选择支付页面（目前没有页面）    ？？未做
			/paysuccess  --支付成功页面     ？？未做
			
			
			----解说人-:郭正洋
			
			//placeholder     autofocus="autofocus"   maxlength
