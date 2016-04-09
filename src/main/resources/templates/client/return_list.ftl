<!DOCTYPE html>
<html lang="zh-CN" class="bgc-f3f4f6">
<head>
<meta charset="UTF-8">
<meta name="keywords" content="">
<meta name="copyright" content="" />
<meta name="description" content="">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>乐易装-退货列表</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/my_base.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/x_common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/x_gu_sales.css"/>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=bdd6b0736678f88ed49be498bff86754"></script>
<script type="text/javascript">

var map, geolocation;

//加载地图，调用浏览器定位服务
map = new AMap.Map('container');

setInterval("timer()", 1000 * 60 * 5);
    
function timer() {
    map.plugin('AMap.Geolocation', function() {
        geolocation = new AMap.Geolocation({
            enableHighAccuracy: true,//是否使用高精度定位，默认:true
            timeout: 2000          //超过10秒后停止定位，默认：无穷大
        });
        map.addControl(geolocation);
        geolocation.getCurrentPosition();
        AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
    });
    
    var geocoder;
    
    //解析定位结果
    function onComplete(data) {
    	AMap.service('AMap.Geocoder',function(){//回调函数
	        //实例化Geocoder
	        geocoder = new AMap.Geocoder({
	            city: "010" //城市，默认：“全国”
	        });
	        
			var lnglatXY=[data.position.getLng(), data.position.getLat()];//地图上所标点的坐标
			
	        geocoder.getAddress(lnglatXY, function(status, result) {
	            if (status === 'complete' && result.info === 'OK') {
	               //获得了有效的地址信息:
	               warning(result.regeocode.formattedAddress);
	               
	               $.ajax({ 
						url: "/delivery/geo/submit", 
						type: "post",
						dataType: "json",
						data: 
						{
							"longitude": data.position.getLng(), 
							"latitude": data.position.getLat(),
							"accuracy": data.accuracy,
							"isConverted": data.isConverted,
							"formattedAddress" : result.regeocode.formattedAddress
						},
						success: function(data)
						{
				        	if (data.code != 0)
				        	{
				        		warning(data.message);
				        	}
				  		}
					});
	            }else{
	               //获取地址失败
	            }
	        });
	    })
    }
}
</script>
</head>
<body class="bgc-f3f4f6">
<#include "/client/common_warn.ftl" />
<div id='container'></div>
<div id="tip"></div>
  <!--弹窗-->
  <div id="bg"></div>
  <div id="popbox">
    <div class="time-select">
      <div>开始时间：<input type="date" id="start" min="2015-12-04" value="<#if startDate??>${startDate?string("yyyy-MM-dd")}</#if>"></div>
      <div>结束时间：<input type="date" id="end" min="2015-12-04"  value="<#if endDate??>${endDate?string("yyyy-MM-dd")}</#if>"></div>
      <a class="btn-sure-time" href="javascript:;" onclick="pupclose()">确定</a>
    </div>    
  </div>
  <script type="text/javascript">
    $("#bg").height($(window).height());
    function pupopen(){
      document.getElementById("bg").style.display="block";
      document.getElementById("popbox").style.display="block" ;
    }
    function pupclose(){
      document.getElementById("bg").style.display="none";
      document.getElementById("popbox").style.display="none" ;
      window.location.href="/delivery/return?start=" + document.getElementById("start").value + "&end=" + document.getElementById("end").value;
    }
  </script>
  <!--弹窗 END-->
  <!-- 头部 -->
  <header>
    <a class="back" style="/client/images/esc.png" href="/delivery"></a>
    <div class="date-group">
      <a <#if days?? && days!=7>class="active"</#if> href="/delivery/return?days=3">三天内</a>
      <a <#if days?? && days==7>class="active"</#if> href="/delivery/return?days=7">一周内</a>
      <a <#if startDate?? || endDate??>class="active"</#if> class="btn-filter" href="javascript:;" onclick="pupopen()">筛选</a>
    </div>
  </header>
  <!-- 头部 END -->

  <!-- 详情列表 -->
  <article class="look-details-list">
    <ul>
      <li <#if type?? && type==1>class="active"</#if>><a href="/delivery/return?type=1<#if days??>&days=${days}</#if><#if startDate??>&start=${startDate?string("yyyy-MM-dd")}</#if><#if endDate??>&end=${endDate?string("yyyy-MM-dd")}</#if>">待取货（${count_type_1!'0'}）</a></li>
      <li <#if type?? && type==2>class="active"</#if>><a href="/delivery/return?type=2<#if days??>&days=${days}</#if><#if startDate??>&start=${startDate?string("yyyy-MM-dd")}</#if><#if endDate??>&end=${endDate?string("yyyy-MM-dd")}</#if>">已取货（${count_type_2!'0'}）</a></li>
      <li <#if type?? && type==3>class="active"</#if>><a href="/delivery/return?type=3<#if days??>&days=${days}</#if><#if startDate??>&start=${startDate?string("yyyy-MM-dd")}</#if><#if endDate??>&end=${endDate?string("yyyy-MM-dd")}</#if>">已返仓（${count_type_3!'0'}）</a></li>
    </ul>
    <!-- 详情列表 -->
    
    <#if return_list??>
    	<#list return_list as item>
    		<section>
		      <a href="/delivery/return/detail/${item.id?c}">
		      	
		        	<div class="time">【退货时间 ${item.orderTime!''}】</div>
	        	
		        <div class="address">退货单号：${item.returnNumber!''}</div>
		        <div class="address">定单号：${item.orderNumber!''}</div>
		      </a>
		    </section>
    	</#list>
    </#if>
  </article>
  <!-- 详情列表 END -->

  <div class="clear h66"></div>


</body>
</html>