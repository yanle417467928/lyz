<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/mag/style/idialog.css" rel="stylesheet" id="lhgdialoglink">
<title>编辑类别</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js"></script>
<script type="text/javascript" charset="utf-8" src="/mag/js/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/mag/js/zh_CN.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.js"></script>
<script type="text/javascript" src="/mag/js/swfupload.handlers.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
<link href="/mag/style/default.css" rel="stylesheet"></head>
<script type="text/javascript">
    $(function () {
        //初始化表单验证
        $("#form1").initValidform();

        //初始化上传控件
        $(".upload-img").each(function () {
            $(this).InitSWFUpload({ 
                sendurl: "/Verwalter/upload", 
                flashurl: "/mag/js/swfupload.swf"
            });
        });
        
        //初始化编辑器
        var editorMini = KindEditor.create('.editor-mini', {
            width: '98%',
            height: '250px',
            resizeType: 1,
            uploadJson: '/Verwalter/editor/upload?action=EditorFile',
            items: [
				'source', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'image', 'link', 'fullscreen']
        });
        
    });

    function change2cn(en, cninput) {
        var channel_name = "news_";
        cninput.value = channel_name + getSpell(en, "");
    }
   
</script>

<body class="mainbody">
<form method="post" action="/Verwalter/product/category/save" id="form1">
<div>
<input type="hidden" name="__EVENTTARGET" id="__EVENTTARGET" value="${__EVENTTARGET!""}" />
<input type="hidden" name="__EVENTARGUMENT" id="__EVENTARGUMENT" value="${__EVENTARGUMENT!""}" />
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="${__VIEWSTATE!""}" />
</div>
<input name="menuId" type="text" value='${mid!""}' style="display:none;">
<input name="channelId" type="text" value='${cid!""}' style="display:none">
<input name="id" type="text" value='<#if cat??>${cat.id!""}</#if>' style="display:none">
<!--导航栏-->
<div class="location" style="position: static; top: 0px;">
  <a href="/Verwalter/product/category/list" class="back"><i></i><span>返回列表页</span></a>
  <a href="/Verwalter/center" class="home"><i></i><span>首页</span></a>
  <i class="arrow"></i>
  <span>编辑分类</span>
</div>
<div class="line10"></div>
<!--/导航栏-->

<!--内容-->
<div class="content-tab-wrap">
  <div id="floatHead" class="content-tab" style="position: static; top: 52px;">
    <div class="content-tab-ul-wrap">
      <ul>
        <li><a href="javascript:;" onclick="tabs(this);" class="selected">基本信息</a></li>
        <li><a href="javascript:;" onclick="tabs(this);">SEO选项</a></li>
        <li><a href="javascript:;" onclick="tabs(this);">扩展选项</a></li>
      </ul>
    </div>
  </div>
</div>

<div class="tab-content">
  <dl>
    <dt>所属父类别</dt>
    <dd>
      <div class="rule-single-select single-select">
        <select name="parentId">
            <option value="" <#if cat?? && cat.parentId?? && cat.parentId==0>selected="selected"</#if>>无父级分类</option>
        	<#if category_list??>
        	   <#list category_list as c>
        	   		<#if c.layerCount?? && c.layerCount lt 3 && (!cat?? || cat?? && cat.id != c.id)>
        	       		<option value="${c.id!""}" <#if cat?? && cat.parentId?? && cat.parentId==c.id || fatherCat?? && fatherCat.id==c.id>selected="selected"</#if>><#if c.layerCount?? && c.layerCount gt 1><#list 1..(c.layerCount-1) as a>　</#list>├ </#if>${c.title!""}</option>
        	   		</#if>
        	   </#list>
        	</#if>
        </select>
      </div>
    </dd>
  </dl>
  <dl>
    <dt>排序数字</dt>
    <dd>
      <input name="sortId" type="text" value="<#if cat??>${cat.sortId!"99"}<#else>99</#if>" id="txtSortId" class="input small" datatype="/^(([1-9]\d{0,1})|0)((\.\d{2})|(\.\d{1}))?$/" sucmsg=" " errormsg="请输入不超过100的2位小数">
      <span class="Validform_checktip">*数字，越小越向前</span>
    </dd>
  </dl>
  <dl>
    <dt>类别名称</dt>
    <dd>
        <input name="title" type="text" value="<#if cat??>${cat.title!""}</#if>" class="input normal" datatype="*1-100" sucmsg=" " ajaxurl="/Verwalter/product/category/check<#if cat??>?id=${cat.id}</#if>" >
        <span class="Validform_checktip">*类别中文名称，100字符内</span>
    </dd>
  </dl>
  <dl>
    <dt>调用别名</dt>
    <dd>
      <input name="callIndex" type="text" id="txtCallIndex" value="<#if cat??>${cat.callIndex!""}</#if>" class="input normal" datatype="/^\s*$|^[a-zA-Z0-9\-\_]{2,50}$/" errormsg="请填写正确的别名" sucmsg=" ">
      <span class="Validform_checktip">类别的调用别名，只允许字母、数字、下划线</span>
    </dd>
  </dl>
  <dl>
    <dt>EBS物料类别ID</dt>
    <dd>
      <input style="width:300px;" name="invCategoryId" type="text" value="<#if cat?? && cat.invCategoryId??>${cat.invCategoryId?c}</#if>" class="input small" datatype="n" sucmsg=" " errormsg="请输入数字">
      <span class="Validform_checktip">*请一定参考EBS传下来的分类id，只有第二级才填</span>
    </dd>
  </dl>
  <dl>
    <dt>关联参数</dt>
    <dd>
      <div class="rule-single-select">
        <select id="categoryParamType" name="paramCategoryId" datatype="n0-100">
            <option value="0">无关联参数</option>
            <#if param_category_list??>
                <#list param_category_list as c>
                    <option value="${c.id!""}" <#if cat?? && cat.paramCategoryId?? && cat.paramCategoryId==c.id>selected="selected"</#if>><#if c.layerCount?? && c.layerCount gt 1><#list 1..(c.layerCount-1) as a>　</#list>├ </#if>${c.title!""}</option>
                </#list>
            </#if>
        </select>
      </div>
    </dd>
  </dl>
</div>

<div class="tab-content" style="display:none">
  <dl>
    <dt>SEO标题</dt>
    <dd>
      <input name="seoTitle" type="text" maxlength="255" value="<#if cat??>${cat.seoTitle!""}</#if>" id="txtSeoTitle" class="input normal" datatype="*0-100" sucmsg=" ">
      <span class="Validform_checktip">255个字符以内</span>
    </dd>
  </dl>
  <dl>
    <dt>SEO关健字</dt>
    <dd>
      <textarea name="seoKeywords" rows="2" cols="20" id="txtSeoKeywords" class="input"><#if cat??>${cat.seoKeywords!""}</#if></textarea>
      <span class="Validform_checktip">以“,”逗号区分开，255个字符以内</span>
    </dd>
  </dl>
  <dl>
    <dt>SEO描述</dt>
    <dd>
      <textarea name="seoDescription" rows="2" cols="20" id="txtSeoDescription" class="input"><#if cat??>${cat.seoDescription!""}</#if></textarea>
      <span class="Validform_checktip">255个字符以内</span>
    </dd>
  </dl>
</div>

<div class="tab-content" style="display:none">
  <dl>
    <dt>URL链接</dt>
    <dd>
      <input name="linkUrl" type="text" maxlength="255" id="txtLinkUrl" value="<#if cat??>${cat.linkUrl!""}</#if>" class="input normal">
      <span class="Validform_checktip">填写后直接跳转到该网址</span>
    </dd>
  </dl>
  <dl>
    <dt>显示图片</dt>
    <dd>
      <input name="imgUrl" type="text" value="<#if cat??>${cat.imgUrl!""}</#if>" id="txtImgUrl" class="input normal upload-path">
      <div class="upload-box upload-img"></div>
    </dd>
  </dl>
  <dl>
    <dt>类别介绍</dt>
    <dd>
        <textarea name="content" class="editor-mini" style="visibility:hidden;" ><#if cat??>${cat.content!""}</#if></textarea>
    </dd>
  </dl>
</div>




<!--/内容-->


<!--工具栏-->
<div class="page-footer">
  <div class="btn-list">
    <input type="submit" name="btnSubmit" value="提交保存" id="btnSubmit" class="btn">
    <input name="btnReturn" type="button" value="返回上一页" class="btn yellow" onclick="javascript:history.back(-1);">
  </div>
  <div class="clear"></div>
</div>
<!--/工具栏-->

</form>


</body></html>