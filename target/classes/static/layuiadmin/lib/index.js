/**

 @Name：layuiAdmin iframe版主入口
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License：LPPL
    
 */
 
layui.extend({
  setter: 'config' //配置模块
  ,admin: 'lib/admin' //核心模块 
  ,view: 'lib/view' //视图渲染模块
}).define(['setter', 'admin'], function(exports){
  var setter = layui.setter
  ,element = layui.element
  ,admin = layui.admin
  ,tabsPage = admin.tabsPage
  ,view = layui.view
  //打开标签页
  ,openTabsPage = function(url, text){
	  var pathURL=url;
	var token=getToken();
	 if(url.indexOf("?")>-1){
	    url+="&token="+token;
	  }else{
	    url+="?token="+token;
	  }	
    //遍历页签选项卡
    var matchTo
    ,tabs = $('#LAY_app_tabsheader>li')
    ,path = url.replace(/(^http(s*):)|(\?[\s\S]*$)/g, '');
    
    tabs.each(function(index){
      var li = $(this)
      ,layid = li.attr('lay-id');
      
      if(layid === url){
        matchTo = true;
        tabsPage.index = index;
      }
    });
    
    text = text || '新标签页';
    
    if(setter.pageTabs){
      //如果未在选项卡中匹配到，则追加选项卡
      if(!matchTo){
        $(APP_BODY).append([
          '<div class="layadmin-tabsbody-item layui-show">'
            ,'<iframe src="'+ url +'" frameborder="0" class="layadmin-iframe"></iframe>'
          ,'</div>'
        ].join(''));
        tabsPage.index = tabs.length;
        element.tabAdd(FILTER_TAB_TBAS, {
          title: '<span>'+ text +'</span>'
          ,id: url
          ,attr: path
        });
      }
    } else {
      var iframe = admin.tabsBody(admin.tabsPage.index).find('.layadmin-iframe');
      iframe[0].contentWindow.location.href = url;
    }

    //定位当前tabs
    element.tabChange(FILTER_TAB_TBAS, url);
    admin.tabsBodyChange(tabsPage.index, {
      url: pathURL
      ,text: text
    });
  }
  
  ,APP_BODY = '#LAY_app_body', FILTER_TAB_TBAS = 'layadmin-layout-tabs'
  ,$ = layui.$, $win = $(window);
  
  //初始
  if(admin.screen() < 2) admin.sideFlexible();
  
  //将模块根路径设置为 controller 目录
  layui.config({
    base: setter.base + 'modules/'
  });
  
  //扩展 lib 目录下的其它模块
  layui.each(setter.extend, function(index, item){
    var mods = {};
    mods[item] = '{/}' + setter.base + 'lib/extend/' + item;
    layui.extend(mods);
  });
  
  view().autoRender();
  function getToken() {
	    if(isIE()){
	    	return this.getCookie('token');
	    }else{
	    	return localStorage.getItem("token");
	    }
	};
	
	function isIE(){
	    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
	    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
	    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
	    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
	    if(isIE || isEdge || isIE11) {
	        return true;  
	    }else{
	        return false;//不是ie浏览器
	    }
	};
	
    function getCookie(name){
	    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	    if(arr != null) 
	        return unescape(arr[2]); 
	    return null;
	}
  //加载公共模块
  layui.use('common');

  //对外输出
  exports('index', {
    openTabsPage: openTabsPage
  });
});
