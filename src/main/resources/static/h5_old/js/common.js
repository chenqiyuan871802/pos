//	清除字符串所有的空格
function trim (basicStr){
	return basicStr.replace(/\s+/g,'');;
}
//  判断是否为空
function isEmpty (v){
	switch (typeof v) {
		case 'undefined':
			return true;
		case 'string':
			if (trim(v).length == 0) return true;
			break;
		case 'boolean':
			if (!v) return true;
			break;
		case 'number':
			if (0 === v) return true;
			break;
		case 'object':
			if (null === v) return true;
			if (undefined !== v.length && v.length == 0) return true;
			for ( var k in v) {
				return false;
			}
			return true;
			break;
	}
	return false;
}
// 访问路径配置
    const projectName = getHttpPrefix();
// const projectName = "http://14.116.205.228:8886/websys/";
//图片展示配置前缀路径
const picPrefix =  projectName+'file/showImage?whetherImage=1&fileName=';
const noneImage =  'img/default_dishes.svg';
// 测试默认的accessToken
function getAccessToken(){
	return getUrlPathData().accessToken;
}
// 测试默认的获取店铺Id
function getShopId(){
	return getUrlPathData().shopId;
}

// 数据请求
function ajaxRequest(url,data,type,suc,err,flag,cpt){
	data = data || {};
	type = type || "POST";
	cpt = isEmpty(cpt)?'application/x-www-form-urlencoded':'application/json;charset=utf-8',
	$.ajax({
		type:type,
		url:projectName + "h5/"+url,
		data:data,
		async:flag,
		success: function(data) {
			suc(data);
		},
		error: function() {
			err();
		},
		contentType:cpt
	});
}
// 获取浏览器语言
function getLang(){
	var lang = navigator.language||navigator.userLanguage;//常规浏览器语言和IE浏览器
    lang = lang.substr(0, 2);//截取lang前2位字符
    return lang;
}
// 获取跳转链接url携带的参数
function getUrlPathData(){
	var str = location.search;
	if(!isEmpty(str) && str.indexOf("?")>=0){
		str = str.split("?")[1];
	}else{
		return false;
	}
	var obj = {};
	if(str.indexOf("&")>0){
		str = str.split("&");
		for(i in str){
			var res = str[i].split("=");
			obj[res[0]] = res[1]; 
		}
	}else{
		var res = str.split("=");
		obj[res[0]] = res[1]; 
	}
	return obj;
}
// 获取请求前缀
function getHttpPrefix(){
	var fullUrl= window.location.href;
	var httpPrefix=window.location.protocol+"//"+window.location.host+"/";
	var urlTmp=fullUrl.replace(httpPrefix,"");
	var len=urlTmp.indexOf("/");
	if(len>-1){
		urlTmp=urlTmp.substring(0,len+1);
	}else{
		urlTmp=urlTmp+"/";
	}
	return httpPrefix+urlTmp;
}
// 格式化price(每3位加逗号)
function toThousands(value) {
	if(!value) return '0.00';
	/*原来用的是Number(value).toFixed(0)，这样取整时有问题，例如0.51取整之后为1 */
	var intPart =  Number(value)|0; //获取整数部分
	var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,'); //将整数部分逢三一断
	
	var floatPart = ".00"; //预定义小数部分
	var value2Array = value.split(".");
	
	//=2表示数据有小数位
	if(value2Array.length == 2) {
		floatPart = value2Array[1].toString(); //拿到小数部分
	
		if(floatPart.length == 1) { //补0,实际上用不着
			return intPartFormat + "." + floatPart + '0';
		} else {
			return intPartFormat + "." + floatPart;
		}
	
	} else {
		return intPartFormat + floatPart;
	}
}
// 格式化price
function priceFormat(value){
	var value = (value || 0).toString(), result = '';
	while (value.length > 3) {
		result = ',' + value.slice(-3) + result;
		value = value.slice(0, value.length - 3);
	}
	if (value) { result = value + result; }
	return result;
}
// 设置缓存
function setCache(name,value) {
	return setLocalStorge(name,value);
}
// 获取缓存
function getCache(name) {
	return getLocalStorge(name);
}
// 清除缓存
function clearCache(name){
	setLocalStorge(name,'');
}
// 设置本地缓存
function setLocalStorge(key, value, hour){
	var hour = hour || 3; // 默认保留3小时
	var exp = new Date();
	localStorage[key] = JSON.stringify({
		value:value,
		expires: exp.getTime() + hour * 60 * 60 * 1000
	})
}
// 获取本地缓存
function getLocalStorge(name){
	try {
		var o = JSON.parse(localStorage[name])
		if (!o || o.expires < Date.now()) {
			return null
		} else {
			return o.value
		}
	} catch (e) {
		return localStorage[name]
	}
}

//扫码店铺二维码信息请求订单数据
function requestOrder(cb,list,languageType,arr){
	var param = {};
	param.shopId = getShopId();
	param.orderLines = mergerData(list,arr);
	// param.languageType = languageType
	ajaxRequest(
		'showOrderLine',param,'',
		function(res){
			if(res.appCode == 1){
				cb(res);
			}else{
				//请求失败处理
			}
		},
		  // 网络故障的处理
		function(){},
		false
	)
}

//扫码桌位二维码信息请求订单数据（相对扫码店铺二维码返回多了参数--语言，展示的时候要对这个返回的语言做展示）
function requestOrderLine(type,cb){
	ajaxRequest(
		'queryOrderLineList',{'languageType':type,'accessToken':getAccessToken()},'',
		function(res){
			if(res.appCode == 1){
				cb(res);
			}else{
				//请求失败处理
			}
		},
		  // 网络故障的处理
		function(){},
		false
	)
}
// 获取语言包
function requestLanguage(type,shopId,cb){
	ajaxRequest(
		'queryLanguagePack',{'languageType':type,"shopId":shopId},'',
		function(res){
			if(res.appCode == 1){
				cb(res);
			}else{
				//请求失败处理
			}
		},
		  // 网络故障的处理
		function(){},
		false
	)
}

// 转换数据
function mergerData(data,arr){
	var jsonData = [];
	for(var i in data){
		var d = data[i];
		var specIndexId = d['specIndexId'];
		var pushD = {
			menuIndexId : d['menuIndexId'],
			specIndexId : isEmpty(specIndexId)?'':specIndexId,
			buyNum : d['count']
		}
		if(d.lineId) pushD.lineId = d.lineId;
		// pushD.catalogIndexId = getCache('catalogIndexId');
		// 如果是自助餐菜品
		if(d.isSpec) pushD.catalogIndexId = getCache('catalogIndexId');
		jsonData.push(pushD);
		// +------------
		// |如果存在规格，则吧规格加入到list中 @1212
		// +------------
		if(!isEmpty(d.buffetListData)){
			d.buffetListData.forEach((item)=>{
				item.buyNum = d.count;
			});
			jsonData = jsonData.concat(d.buffetListData);
		}
	}
	// if(!isEmpty(arr)) jsonData = jsonData.concat(arr);//规格已经加到data中，所以注释  @1212
	return JSON.stringify(jsonData);
}

//请求自助餐数据
function requestShopBuffetMenu(type,cb){
	ajaxRequest(
		'loadShopBuffetMenu',{'languageType':type,'shopId':getShopId(),'accessToken':getAccessToken()},'',
		function(res,flag){
			if(res.appCode == 1){
				cb(res);
				
				$(".cu-load").css("visibility","hidden");
				if(!isEmpty(res.data)){
					setCache('catalogIndexId', res.data.catalogIndexId); // 缓存自助餐配菜返回的ID 
				}
			}else{
				//请求失败处理
			}
		},
		  // 网络故障的处理
		function(){},
		false
	)
}

// 自助餐购买下单
function saveBufferOrder(id,num,cb){
	ajaxRequest(
		'saveBuffetOrder',{'accessToken':getAccessToken(),'catalogIndexId':id,'buyNum':num},'',
		function(res){
			if(res.appCode == 1){
				cb(res);
			}else{
				//请求失败处理
			}
		},
		  // 网络故障的处理
		function(){},
		false
	)
}

//请求自助餐配菜数据
function queryBufferMenuList(type,cb){
	ajaxRequest(
		'queryBuffetMenuList',{'languageType':type,'accessToken':getAccessToken()},'',
		function(res){
			if(res.appCode == 1){
				// vueFood.$set(vueFood, "garnishList", res.dataList);
				setCache('catalogIndexId', res.data.catalogIndexId); // 缓存自助餐配菜返回的ID 
				cb(res);
			}else{
				//请求失败处理
			}
		},
		  // 网络故障的处理
		function(){},
		false
	)
}
// 请求步骤规格panel
function requestMenuStepSpecList(languageType,menuIndexId,cb){
	ajaxRequest(
		'queryMenuStepSpecList',{'languageType':languageType,"menuIndexId":menuIndexId},'',
		function(res){
			if(res.appCode == "1"){
				cb(res);
			}else{
				//请求失败处理
			}
		},
		  // 网络故障的处理
		function(){},
		false
	)
}
// 生成uuid
function getUuid() {
    function S4() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
}

// 根据isShowPrice判断是否隐藏购物车价格
function isShowPrice(){
	if(getCache("isShowPrice")){
		if(getCache("toolbarData") == "buffet"){
			$(".total-price").hide();
		}else{
			$(".total-price").show();
		}
	}else{
		$(".total-price").hide();
	}
}