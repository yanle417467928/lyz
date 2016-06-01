<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>查看订单信息</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
    $(function () {
        $("#auditYes").click(function () { auditYes(1); });   //通过审核
        $("#auditNo").click(function () { auditYes(0); });   //不通过审核
        $("#signPhoto").click(function () { imgChange(); }); //修改签收图片大小
    });
  //通过审核
    function auditYes(type) {
	    var msg='将允许欠款，是否继续？';
	  	if(type==0){
	  		msg='将不允许欠款，是否继续？';
	  	}
        var dialog = $.dialog.confirm(msg, function () {
            var ownId = $("#ownId").val();
            var postData = { "id": ownId, "type": type };
            //发送AJAX请求
            sendAjaxUrl(dialog, postData, "/Verwalter/order/own/save");
            return false;
        });

    }

  //图片点击放大缩小
    var isopen = false; 
    function imgChange(){
    	if (!isopen){ 
        	isopen = true; 
        	$("#signPhoto").width("230px"); 
        	$("#signPhoto").height("320px");
    	} 
    	else{ 
        	isopen = false; 
        	$("#signPhoto").width("100px");
        	$("#signPhoto").height("100px");
    	} 
    }
  //发送AJAX请求
    function sendAjaxUrl(winObj, postData, sendUrl) {
        $.ajax({
            type: "post",
            url: sendUrl,
            data: postData,
            dataType: "json",
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $.dialog.alert('尝试发送失败，错误信息：' + errorThrown, function () { }, winObj);
            },
            success: function (data) {
                 if (data.code == 0) {
                    winObj.close();
                    $.dialog.tips(data.msg, 2, '32X32/succ.png', function () { location.reload(); }); //刷新页面
                } else {
                    $.dialog.alert('错误提示：' + data.message, function () { }, winObj);
                }
             }
        });
    }
    </script>
</head>
<body class="mainbody"><div style="position: absolute; left: -9999em; top: 236px; visibility: visible; width: auto; z-index: 1976;"><table class="ui_border ui_state_visible ui_state_focus"><tbody><tr><td class="ui_lt"></td><td class="ui_t"></td><td class="ui_rt"></td></tr><tr><td class="ui_l"></td><td class="ui_c"><div class="ui_inner"><table class="ui_dialog"><tbody><tr><td colspan="2"><div class="ui_title_bar"><div class="ui_title" unselectable="on" style="cursor: move;">视窗 </div><div class="ui_title_buttons"><a class="ui_min" href="javascript:void(0);" title="最小化" style="display: inline-block;"><b class="ui_min_b"></b></a><a class="ui_max" href="javascript:void(0);" title="最大化" style="display: inline-block;"><b class="ui_max_b"></b></a><a class="ui_res" href="javascript:void(0);" title="还原"><b class="ui_res_b"></b><b class="ui_res_t"></b></a><a class="ui_close" href="javascript:void(0);" title="关闭(esc键)" style="display: inline-block;">×</a></div></div></td></tr><tr><td class="ui_icon" style="display: none;"></td><td class="ui_main" style="width: auto; height: auto;"><div class="ui_content" style="padding: 10px;"><div class="ui_loading"><span>loading...</span></div></div></td></tr><tr><td colspan="2"><div class="ui_buttons" style="display: none;"></div></td></tr></tbody></table></div></td><td class="ui_r"></td></tr><tr><td class="ui_lb"></td><td class="ui_b"></td><td class="ui_rb" style="cursor: se-resize;"></td></tr></tbody></table></div>
    <!--导航栏-->
    <div class="location" style="position: fixed; top: 0px;">
        <a href="/Verwalter/order/own/list" class="back"><i></i><span>返回列表页</span></a>
        <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
        <i class="arrow"></i>
        <a href="/Verwalter/order/own/list"><span>审核欠款</span></a>
        <i class="arrow"></i><span>订单详细</span>
    </div>
    <div class="line10">
    </div>
    <!--/导航栏-->
    <!--内容-->
    <div class="content-tab-wrap">
        <div id="floatHead" class="content-tab" style="position: static; top: 52px;">
            <div class="content-tab-ul-wrap">
                <ul>
                    <li>
                        <a href="javascript:;" onclick="tabs(this);" class="selected">订单详细信息</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="tab-content">
        
        <dl>
            <dt>订单号</dt>
            <dd>
            	<input type="hidden" id="ownId" value="${consult.id?c }" />
                <span id="spanOrderNumber">${order.orderNumber!""}</span>
            </dd>
        </dl>
        <#if order.photo??>
            <dl>
                <dt>签收照片</dt>
                <dd>
                    <img id="signPhoto" width="100px" height="100px" src="${order.photo!''}">
                </dd>
            </dl>
        </#if>
        <dl>
            <dt>商品列表</dt>
            <dd>
                <table border="0" cellspacing="0" cellpadding="0" class="border-table" width="98%">
                    <thead>
                        <tr>
                            <th width="12%">
                                商品ID
                            </th>
                            <th>
                                商品名称
                            </th>
                            <th width="12%">
                                成交价
                            </th>
                            <th width="10%">
                                数量
                            </th>
                            <th width="12%">
                                金额合计
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                         <#if orderList??>
        					<#list orderList as orders1>
		                        <#if orders1.orderGoodsList??>
		                            <#list orders1.orderGoodsList as goods>
		                                <tr class="td_c">
		                                    <td>${goods.goodsId?c!""}</td>
		                                    <td style="text-align: left; white-space: normal;">
		                                        ${goods.goodsTitle!""} 
		                                        ${goods.sku!""}
		                                        ${goods.goodsColor!""}
		                                        ${goods.goodsCapacity!""}
		                                        ${goods.goodsVersion!""}
		                                        ${goods.goodsSaleType!""}
		                                    </td>
		                                    <td>${goods.price?string("#.00")}</td>
		                                    <td>${goods.quantity!""}</td>
		                                    <td>${(goods.price*goods.quantity)?string("#.00")}</td>
		                                </tr>
		                            </#list>
		                        </#if>
		                      </#list>
		                    </#if>
                    </tbody> 
                </table> 
            </dd> 
        </dl>
        <#if order.presentedList??>
             <dl>
                <dt>赠品列表</dt>
                <dd>
                    <table border="0" cellspacing="0" cellpadding="0" class="border-table" width="98%">
                        <thead>
                            <tr>
                                <th width="12%">
                                    商品ID
                                </th>
                                <th>
                                    商品名称
                                </th>
                                <th width="12%">
                                    价格
                                </th>
                                <th width="10%">
                                    数量
                                </th>
                                <th width="12%">
                                    金额合计
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                        <#if orderList??>
        					<#list orderList as orders2>
                                <#list orders2.presentedList as goods>
                                    <tr class="td_c">
                                        <td>${goods.goodsId?c!""}</td>
                                        <td style="text-align: left; white-space: normal;">
                                            ${goods.goodsTitle!""} 
                                            ${goods.goodsColor!""}
                                            ${goods.goodsCapacity!""}
                                            ${goods.goodsVersion!""}
                                            ${goods.goodsSaleType!""}
                                        </td>
                                        <td>${goods.price?string("0.00")}</td>
                                        <td>${goods.quantity!""}</td>
                                        <td>${(goods.price*goods.quantity)?string("0.00")}</td>
                                    </tr>
                                </#list>
                              </#list>
		                  </#if>
                        </tbody> 
                    </table> 
                </dd> 
            </dl>
        </#if>
        <#if order.giftGoodsList?? && order.giftGoodsList?size gt 0>
             <dl>
                <dt>小辅料列表</dt>
                <dd>
                    <table border="0" cellspacing="0" cellpadding="0" class="border-table" width="98%">
                        <thead>
                            <tr>
                                <th width="12%">
                                    商品ID
                                </th>
                                <th>
                                    商品名称
                                </th>
                                <th width="12%">
                                    价格
                                </th>
                                <th width="10%">
                                    数量
                                </th>
                                <th width="12%">
                                    金额合计
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                        <#if orderList??>
        					<#list orderList as orders3>
                                <#list orders3.giftGoodsList as goods>
                                    <tr class="td_c">
                                        <td>${goods.goodsId?c!""}</td>
                                        <td style="text-align: left; white-space: normal;">
                                            ${goods.goodsTitle!""} 
                                            ${goods.goodsColor!""}
                                            ${goods.goodsCapacity!""}
                                            ${goods.goodsVersion!""}
                                            ${goods.goodsSaleType!""}
                                        </td>
                                        <td>${goods.price?string("0.00")}</td>
                                        <td>${goods.quantity!""}</td>
                                        <td>${(goods.price*goods.quantity)?string("0.00")}</td>
                                    </tr>
                                </#list>
                                </#list>
		                  </#if>
                        </tbody> 
                    </table> 
                </dd> 
            </dl>
        </#if>
        
        <dl>
            <dt>订单统计</dt>
            <dd>
                <table border="0" cellspacing="0" cellpadding="0" class="border-table" width="98%">
                    <tbody>
                    <tr>
                        <th>
                        导购名字
                        </th>
                        <td>
                            <div class="position">
                            <span><#if order.sellerRealName??>${order.sellerRealName!""}</#if> </span>
                            </div>
                        </td>
                    </tr>
                    <#--
                    <tr>
                        <th width="20%">
                            商品总金额
                        </th>
                        <td>
                            <div class="position">
                                <span id="spanRealAmountValue">
                                    <#if order.totalGoodsPrice??>${order.totalGoodsPrice?string("0.00")}</#if>
                                </span> 元
                            </div>
                        </td>
                    </tr>
                     -->
                    <#--
                    <tr>
                        <th>
                            配送费用
                        </th>
                        <td>
                            <div class="position">
                                <span id="spanExpressFeeValue"><#if order.deliverTypeFee??>${order.deliverTypeFee?string("0.00")}</#if></span> 元
                                <#if order.statusId==1 || order.statusId==2 && order.isOnlinePay>
                                <input type="button" id="btnEditExpressFee" class="ibtn" value="调价">
                                </#if>
                            </div>
                        </td>
                    </tr>
                   
                    <tr>
                        <th>
                            支付手续费
                        </th>
                        <td>
                            <div class="position">
                                <span id="spanPaymentFeeValue"><#if order.payTypeFee??>${order.payTypeFee?string("0.00")}</#if></span> 元
                                <#if order.statusId==1 || order.statusId==2 && order.isOnlinePay>
                                <input type="button" id="btnEditPaymentFee" class="ibtn" value="调价">
                                </#if>
                            </div>
                        </td>
                    </tr>
                     -->
                    <#--
                    <tr>
                        <th>
                            可获取积分总计
                        </th>
                        <td>
                            <div class="position">
                                ${order.points!"0"}分
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            订单使用预存款
                        </th>
                        <td>
                         可用预存款：<#if order.cashBalanceUsed??> ${order.cashBalanceUsed?string("0.00")}<#else>0.00</#if>元 | 
                        不可用预存款：<#if order.unCashBalanceUsed??>${order.unCashBalanceUsed?string("0.00")}<#else>0.00</#if>元
                            </td>
                    </tr>
                     -->
                    <tr>
                        <th>
                            订单总金额
                        </th>
                        <td width="80%">
                            ${order.allTotalPay?string("0.00")}元</td>
                    </tr>
                </tbody>
                </table>
            </dd>
        </dl>
        
        <#if order.deliverTypeTitle!='门店自提'>
        <dl>
            <dt>收货信息</dt>
            <dd>
                <table border="0" cellspacing="0" cellpadding="0" class="border-table" width="98%">
                    <tbody><tr>
                        <th width="20%">
                            收件人
                        </th>
                        <td>
                            <div class="position">
                                <span id="spanAcceptName">${order.shippingName!""}</span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            发货地址
                        </th>
                        <td>
                            <span id="spanArea"></span> 
                            <span id="spanAddress">${order.shippingAddress!""}</span>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            邮政编码
                        </th>
                        <td><span id="spanPostCode">${order.postalCode!""}</span></td>
                    </tr>
                    <tr>
                        <th>
                            电话
                        </th>
                        <td><span id="spanMobile">${order.shippingPhone!""}</span></td>
                    </tr>
                    <tr>
                        <th>
                            是否索要发票
                        </th>
                        
                        <td>
                            <span id="spanInvoice"><#if order.isNeedInvoice?? && order.isNeedInvoice>是<#else>否</#if></span>
                        </td>
                        </tr>
                        <tr>
                        <th>发票抬头</th>
                        <td>
                            <span id="spanInvoice">${order.invoiceTitle!""}</span>
                        </td>
                        
                    </tr>
                </tbody></table>
            </dd>
        </dl>
        </#if>
        
        <dl>
            <dt>支付配送</dt>
            <dd>
                <table border="0" cellspacing="0" cellpadding="0" class="border-table" width="98%">
                    <tbody><tr>
                        <th width="20%">
                            支付方式
                        </th>
                        <td>${order.payTypeTitle!""}</td>
                    </tr>
                    <tr>
                        <th>
                            配送方式
                        </th>
                        <td>${order.deliverTypeTitle!""}</td>
                    </tr>
                    <tr>
                        <th>
                            配送仓库
                        </th>
                        <td><#if tdWareHouse??>${tdWareHouse.whName!''}</#if></td>
                    </tr>
                    <tr>
                        <th>
                            用户留言
                        </th>
                        <td>${order.remark!""}</td>
                    </tr>
                    <tr>
                        <th valign="top">
                            订单备注
                        </th>
                        <td>
                            <div class="position">
                                <div>${order.remarkInfo!""}</div>
                            </div>
                        </td>
                    </tr>
                </tbody></table>
            </dd>
        </dl>
        
    </div>
    <!--/内容-->
    <!--工具栏-->
    <div class="page-footer">
        <div class="btn-list">
        	<#if consult.isEnable?? && consult.isEnable==false>
        	<input type="button" value="通过" class="btn " id="auditYes">
        	<input type="button" value="不通过" class="btn "  id="auditNo">
        	</#if>
            <input type="button" value="返回上一页" class="btn yellow" onclick="javascript:history.back(-1);">
            <span style="margin-left: 33px;"> 收款：<#if consult.payed??>${consult.payed?c}<#else>0</#if><b></b>   |<b></b> 欠款：<#if consult.owned??>${consult.owned?c}<#else>0</#if></span>
        </div>
        <div class="clear">
        </div>
    </div>
    <!--/工具栏-->
</body></html>