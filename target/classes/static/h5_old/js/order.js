//初始引入FastClick
$(function() {
	FastClick.attach(document.body);
});

// language 选取语言参数
var language = {
	text: '中文简体',
	url: 'img/China.svg'
}
var languageObj = ""; //全局语言包

// +-------------------
//	店铺相关参数
// +-------------------
var shopInfo = {
	shopName: '', //店铺名
	shopId: '', //店铺id
	language: '' //店铺语言
};
// +-------------------
//	店铺菜单菜品信息
// +-------------------
var menuInfo = {
	menuCatalogList: '', //菜单信息
	dataList: '', //菜品信息
	buffetList: '', // 自助餐菜单
	garnishList: '' // 配菜菜单
};

// 追加食品参数
var addMoreConfig = {
	isShownOrder: false,
	isShowFinish: false,
	totalPrice: 0,
	totalTax: 0,
	cartHtml: '',
	orderHtml: '',
	orderStatus: ''
};

// 第一次付款前购物车参数
var shopConfig = {
	isShowCartPanel: false,
	isShownOrder: false, // 当前是否显示下单页面
	isShowFinish: false, // 当前是否显示付款完成页面
	totalPrice: 0,
	totalTax: 0,
	hasFinishCheckout: false, // 是否完成付款（第一次购买的状态）
	hasAddMore: '', // 追加订单完成
	cartHtml: '', // 购物车内容参数
	orderHtml: '', // 下单页面参数
	orderStatus: '' // 订单状态

};

// 第一次购买状态 购物车已加入食品
var cartList = {};

// 追加状态 购物车已加入的食品
var addMoreList = {};

var currentFood = {} // 进入食品详情面板时选中的当前商品相关参数

var currentSpec = {} // 进入规格面板时选中的当前商品相关参数

var finishList = {}; // 点击结算时推送的数据：finishList = {cartList:{} , addMoreList：{}},包括追加后的和第一次的

var statusConfig = {}; // 订单数据

var languagePack = {}; //语言包对象

var buffetConfig = { // 自助餐配置
	hasChoiceBuffet: false, // 是否已经下单了自助餐，（会清除，只是为了选择能不能买配菜）
	hasFinishBuffet: false, // 是否下单套餐（一旦点了自助餐就永久在，除非退出重来）
	garnishChoicedNum: 0, // 配菜已选数量
	garnishCartHtml: '', // 配菜购物车
	garnishOrderHtml: '', // 配菜订单信息
	orderHtml: '', // 自助餐订单信息
	specDataList: [], // 配餐信息单
	cartHtml: '' // 自助餐加入购物车
};
var buffetList = {}; // 自助餐加购参数
var garnishList = {}; // 自助餐配菜加购参数
// +=======================================================================

var urlData = getUrlPathData(); // 初始得到店铺信息
var langArr = [1, 2, 3, 4, 5];
var langStr = getLang();
var $language = $(".language");

// 根据请求数据显示语言选项
function showLanguage(language) {
	var $languageSel = $('#language-select');
	$languageSel.find("li").hide();
	language = language.split(",");
	for (var i = 0; i < language.length; i++) {
		$languageSel.find("[data-type=" + language[i] + "]").show();
	}
}
setCache("toolbarData","menu");
// 初始化店铺信息
function initMenu(languageType) {
	ajaxRequest(
		'queryShopInfo?shopId=' + urlData.shopId + '&languageType=' + languageType+'&accessToken='+getAccessToken(), '', '',
		function(res) {
			if (res.appCode == 1) {
				var shopData = res.data;
				shopInfo.shopName = shopData.shopName; //店铺名
				shopInfo.shopId = shopData.shopId; //店铺id
				shopInfo.language = shopData.language; //语言
				$(".home-nav-name").text(shopData.shopName);
				let imgSrc = isEmpty(shopData.shopFirstImage)?noneImage:(picPrefix+shopData.shopFirstImage);
				$(".header .bgImg").css({
					"background-image": "url(" + imgSrc + ")"
				});
				$('.store-info').text(shopData.shopName);
				$('.store-info-content').text(isEmpty(shopData.shopIntroduce)?'':shopData.shopIntroduce);
				showLanguage(shopData.language);
				// 判断whetherShowBuffet 0不显示自助餐 1显示   （二期修改）
				// if(isEmpty(getAccessToken())){
				// 	$(".toolbar>ul>li:gt(0)").hide();
				// 	// 增加class控制li标签宽度为1/2
				// 	$('.toolbar').addClass("buffet-order-hide");
				// }else{
					// 0显示全部  1只显示菜单   2 只显示自助餐
				let displayType = parseInt(res.displayType);
				setCache("displayType",displayType);
				if(isEmpty(getAccessToken())){
					$(".toolbar>ul>li:eq(1)").hide();
					$('.toolbar').addClass("buffet-order-hide");
				}else{
					if(displayType === 0){
						$(".toolbar>ul>li").show();
						$('.toolbar').removeClass("buffet-order-hide");
					}else if(displayType === 1){
						// $(".toolbar>ul>li:gt(0)").hide();
						$(".toolbar>ul>li:eq(1)").hide();
						$('.toolbar').addClass("buffet-order-hide");
					}else{
						$(".toolbar>ul>li:eq(0)").hide();
						$(".toolbar>ul>li:eq(1)").addClass("active");
						$('.toolbar').addClass("buffet-order-hide");
						let firstLoadBuffet = setTimeout(function(){
							$(".toolbar>ul>li:eq(1)").click();
							clearTimeout(firstLoadBuffet);
						},200);
					}
				}
				
				// }
				// 是否显示价格---三期修改
				setCache("isShowPrice",res.whetherShowPrice === "1" ? true:false);
				// 请求菜单数据  ----三期修改
				initMenuCata(languageType);
				// +-----------------------
				// 三期修改 根据isShowPrice判断是否隐藏价格
				// +-----------------------
				isShowPrice();

			} else {
				//请求失败处理
			}
		},
		// 网络故障的处理
		function() {},
		false
	);
}

// 初始化菜单菜品信息
function initMenuCata(languageType) {
	ajaxRequest(
		'loadShopMenu?shopId=' + urlData.shopId + '&languageType=' + languageType, '', '',
		function(res) {
			if (res.appCode == 1) {
				//给菜品加上区分普通菜品和自助餐菜品的标示
				var dataList = res.dataList;
				for (var i = 0; i < dataList.length; i++) {
					for (var x = 0; x < dataList[i]['menuDictList'].length; x++) {
						dataList[i]['menuDictList'][x]['isSpec'] = false;
						// false代表不显示按钮   true代表根据正常逻辑显示
						dataList[i]['menuDictList'][x]['zizhucan'] = true;
						dataList[i]['menuDictList'][x]['iszizhucan'] = false;
						dataList[i]['menuDictList'][x]['iconAdd'] = 'img/add.svg';
						dataList[i]['menuDictList'][x]['iconReduce'] = 'img/reduce.svg';
						dataList[i]['menuDictList'][x]['catalogType'] = dataList[i]['catalogType'];
						// 是否显示价格 增加参数 -----三期修改
						dataList[i]['menuDictList'][x]['isShowPrice'] = getCache("isShowPrice");
					}
				}

				menuInfo.dataList = dataList; //菜品数据集合
				menuInfo.menuCatalogList = res.menuCatalogList; //菜单数据集合
				$('.footer').css({"display": "block"});
				// 如果有登录过
				if(!isEmpty(getCache("loginInfo"))){
					//修改名称
					$("#to-login").text("进入");
					$(".score-box>.to-title").text("查看积分");
				}
			} else {
				//请求失败处理

			}
		},
		// 网络故障的处理
		function() {},
		false
	);
}
var languageType = 1;
if(getCache('languageType')!=undefined){
	languageType = getCache('languageType');
}else{
	if (langStr == "zh") {
		// $language.attr("class", "language China");
		// $language.find("span").text("中文简体");
		languageType = 2;
	} else if (langStr == "en") {
		// $language.attr("class", "language UK");
		// $language.find("span").text("English");
		languageType = 5;
	} else if(langStr == 'ja'){
		// $language.attr("class", "language Japan");
		// $language.find("span").text("日本語");
		languageType = 1;
	}else if(langStr == 'ko'){
		languageType = 4;
	}else{
		languageType = 3;
	}	
}

setCache('languageType', languageType);
setLanguage(languageType);
initMenu(languageType);
// requestShopBuffetMenu(languageType, getBuffetList); // 每次点击toolbar里的buffet （自助餐）标题，就会重新请求
// queryBufferMenuList(languageType, getGarnishList); // 测试用
// +-------------------
// 格式化价格
// +-------------------
Vue.filter('priceFormat', function(value) {
	var value = (value || 0).toString(),
		result = '';
	while (value.length > 3) {
		result = ',' + value.slice(-3) + result;
		value = value.slice(0, value.length - 3);
	}
	if (value) {
		result = value + result;
	}
	return result;
})
// vue 加载左右两边食品分类和 对应的食品详细参数
var vueFood = new Vue({
	el: '#container',
	created: function() {},
	mounted: function() {},
	data: {
		toolbarData: 'menu', // toolbar当前选择的标题，默认是菜单
		totalHeight: 0, // 设置页面总高度
		lastPadding: 0, // 如果最后一页内容和页面宽差不多高，加一点底部距离增加滑动空间
		windowWidth: 0, // 页面宽
		windowHeight: 0, // 页面高
		buffetWidth: 0,
		hasChoiceBuffet: false,
		hasChoiceGarnish: false,
		languageOption: languageObj.sold_out,
		garnishCount: 10, // 自助餐下单后可选配菜数量
		picPrefix: picPrefix, //图片前缀
		noneImage:noneImage,//无图片默认图片地址
		foodTypes: menuInfo.menuCatalogList, // 菜单参数
		foodItems: menuInfo.dataList, // 菜品参数，里面每个分类对应一个若干菜品
		buffetList: menuInfo.buffetList, // 自助餐参数
		// garnishList: menuInfo.garnishList // 测试专用
		garnishList: [] ,// 自助餐配菜参数
		buffetDataType:false,    //（三期修改）套餐数据类型false 默认为显示为data-name='buffet' 的li标签   true显示为data-name='menu'的li标签
		isShowPrice:false //是否显示价格 ----三期修改
	},
	methods: {
		reduceHandClick: function(food, type, index) { // 主页上食品减按钮，减操作
			reduceCartFood(food, type, index)
		},
		addHandClick: function(e, food, typeIndex, index) { //小球运动动画
			if ((this.hasChoiceBuffet || $("#tab-buffet").parent().attr("class") == "active")  && buffetConfig.garnishChoicedNum >= this.garnishCount) {
				showToast(languageObj.buffet_over_full);
				return;
			}
			vueFood.$set(food, 'typeIndex', typeIndex);
			vueFood.$set(food, 'index', index);
			addFoodToCart(food) // 主页上加按钮，点击加食品操作
			let start = {
				x: e.x - e.offsetX - 10,
				y: e.y - 10,
				scale: 1
			}
			let end = {
				x: 30 - start.x,
				y: this.windowHeight - start.y,
				scale: 0.5
			}
			let ball = $("<div class='ball'><div class='ball-enter'></div></div>")
			ball.css({
				left: start.x + "px",
				top: start.y + "px",
				transform: 'scale(1)'
			})
			$("#container").append(ball)
			setTimeout(() => {
				ball.one({
					'transitionend': () => {
						ball.remove()
					},
					'webkitTransitionEnd': () => {
						ball.remove()
					}
				})
				ball.css({
					transform: `translate3d(0px,${end.y}px,0)`
				})
				ball.find('.ball-enter').css({
					transform: `translate3d(${end.x}px,0px,0) scale(${end.scale})`
				})
			}, 1)
		},
		showFoodPanel: function(food, typeIndex, index) { //显示食品详情页
			
			// 如果等于空，说明是自助餐详情
			if(typeIndex === "" || !food.zizhucan){
				setCache("btn_show_detail",true);
			}else{
				setCache("btn_show_detail",false);
			}
			vueFood.$set(food, 'typeIndex', typeIndex);
			vueFood.$set(food, 'index', index);
			if (this.toolbarData === 'buffet' || this.hasChoiceBuffet  || $("#tab-buffet").parent().attr("class") == "active") {
				// showHomeButton();
				$('#next').html(languageObj.buy);
				$("#buffet-panel-buy>.food-add").attr("src","img/add-l.png");
				$("#buffet-panel-buy>.food-reduce").attr("src","img/reduce-l.png");
				$("#buffet-panel-specifications").attr("src","img/guige-l.png");
				showFoodPanel(food,true);
			}else{
				$("#buffet-panel-buy>.food-add").attr("src","img/add.svg");
				$("#buffet-panel-buy>.food-reduce").attr("src","img/reduce.svg");
				$("#buffet-panel-specifications").attr("src","img/guige.png");
				showFoodPanel(food);
			}
		},
		specificationsEvent: function(food, typeIndex, index, e) { // 食品规格按钮
			vueFood.$set(food, 'typeIndex', typeIndex)
			vueFood.$set(food, 'index', index)
			// showSpecifications(food) 2019-0824-12:00临时注销
			stepPage(food); //步骤规格页面
		},
		leftHandClick: function(index) { // 主菜单或者自助餐配菜菜单，左边食品类名点击滑动到右边对应的位置，
			// 点击左侧食品导航加active，同时右侧滑动到对应分类
			$('body, html').animate({
				scrollTop: $('body .right ul li').eq(index).offset().top - extendTop
			}, 500);
		},
		resetHeight: function() {
			this.totalHeight = 0
			this.windowHeight = $(window).height()
			this.windowWidth = $(window).width()
			this.buffetWidth = this.windowWidth * 0.44
			if (this.hasChoiceBuffet) var list = this.garnishList
			else var list = this.foodItems
			for (let i = 0; i < list.length; i++) {
				
				if(isEmpty(list[i].menuDictList)) //三期判断  menuDictList属性是否存在/是否为空
				list[i].menuDictList = [];//三期判断  不存在给空
				
				let count = list[i].menuDictList.length
				let height = count * 100 + 38
				this.totalHeight += height;
				if (i === list.length - 1) {
					if (height < this.windowHeight) {
						this.lastPadding = this.windowHeight - height + 50
					} else {
						this.lastPadding += 200
					}
				}
			}
		},
	},
	computed: {
		getFoodItems: function() {
			// +-------------------------------------
			//	三期增加buffetDataType的判断  为true则是首页的套餐已下过单了
			// +-------------------------------------
			if (this.hasChoiceBuffet || this.buffetDataType) { // 如果自助餐已下单，就返回配餐garnishList
				return this.garnishList //配菜菜单
			} else { // 否则就返回主菜单foodItems
				return this.foodItems
			}
		},
		getFoodTypes: function() {
			// +-------------------------------------
			//	三期增加buffetDataType的判断 为true则是首页的套餐已下过单了
			// +-------------------------------------
			if (this.hasChoiceBuffet || this.buffetDataType) { // 如果自助餐已下单，就返回配餐garnishList
				return this.garnishList
			} else { // 否则就返回主菜单类名foodTypes
				return this.foodTypes
			}
		},
		getContainerHeight: function() {
			this.resetHeight()
			return this.totalHeight
		},
		getGarnishMax: function() {
			return languageObj.order_choose.replace('0', this.garnishCount)
		}
	}
});
// +=======================================================================

/*vue和jquery分割线==========================================================*/
// **初始化加载

// 初始化语言 
// changeLanguage();

// 初始设置下一次样式
setNext(canOrder(),true);

var windowHeight = $(window).height(); // 页面宽
var windowWidth = $(window).width(); // 页面高

$('body').css({
	height: windowHeight + "px"
}); // 初始给body一个高，如果进入下单页面，就给body overflow:hidden,禁止外层滚动

$('.buffet-container').css({'height':(windowHeight-225)+'px'});

$('.order-content,.cart-panel').on('touchstart touchmove touchend touchcancel', function(e) {
	e.stopPropagation();
})

// 通用渐隐渐显效果面板，点击背景和关闭按钮都会关闭页面
$('.pop-panel').on("click", function(e) {
	e.stopPropagation();
	$(this).fadeOut(300);
});
$('.pop-panel-bg,.close-btn').on("click", () => {
	$('.pop-panel').fadeOut(300);
});

// +=======================================================================
// 隐藏和显示元素函数
function hideElem(elem) { // 隐藏
	elem.css({
		"display": "none"
	});
}

function showElem(elem) { //显示
	elem.css({
		"display": "block"
	});
}
// +=======================================================================

// +=======================================================================
// *滑动食品模块
// 滑动右侧食品对应左侧，滑动后左侧也滑动到对应位置
// $(window).scroll(function() {
// 	if (vueFood.hasChoiceBuffet) {
// 		console.log($(window).scrollTop());
// 		if ($(window).scrollTop() >= 94) {
// 			$('.menu-left').css({
// 				'position': 'fixed',
// 				"height": windowHeight + 'px'
// 			});
// 		} else {
// 			$('.menu-left').css({
// 				'position': '',
// 				"height": ""
// 			});
// 		};
// 	} else {
// 		console.log($(window).scrollTop());
// 		if ($(window).scrollTop() >= 205) {
// 			$('.menu-left').css({
// 				'position': 'fixed',
// 				"height": windowHeight + 'px'
// 			});
// 		} else {
// 			$('.menu-left').css({
// 				'position': '',
// 				"height": ""
// 			});
// 		};
// 	}
var extendTop = 195;
$(window).scroll(function() {
	
	afterScrollFun();
	//滚动到标杆位置,左侧导航加active
	$('.menu-container .menu-right ul li').each(function() {
		var target = parseInt($(this).offset().top - extendTop - $(window).scrollTop());
		var i = $(this).index();
		if (target <= 0) {
			$('.menu-container .menu-left ul li').removeClass('active');
			$('.menu-container .menu-left ul li').eq(i).addClass('active');
			if (i > parseInt(windowHeight / 75) - 1) { // 如果大滚动距离于当前页面高度，左边标题栏随着滚动下移
				$('.menu-container .menu-left').scrollTop((i - parseInt(windowHeight / 75) + 1) * 75);
			} else {
				$('.menu-container .menu-left').scrollTop(0); // 如果大滚动小于当前页面高度，滚动左边滚动距离不变（可自行滚动）
			}
		}
	});
});

function afterScrollFun(){ // 滚动后续，将要指顶的元素变成fixed定位，同时给与top值
	if ($(window).scrollTop() >= 0) {
		fixTopHeader(); // 主菜单界面
		extendTop = 195;
		if(vueFood.hasChoiceBuffet){ // 自助餐配餐界面
			$('.right').css({"top":"94px"});
			$('.menu-left').css({"top":"94px"});
			$('.toolbar.grnish').css({"top":"50px"});
			extendTop = 94;
		} else if(vueFood.buffetDataType){ // 自助餐界面
			$('.right').css({"top":"238px"});
			$('.menu-left').css({"top":"238px"});
			extendTop = 238;
		}
	} else {
		backTopHeader();
	}
}
function fixTopHeader(){
	$('.menu-header').css({'position':'fixed'});
	$('.toolbar').css({'position':'fixed'});
	$('.toolbar.grnish').css({"position":"fixed", "top":""});
	$('.right').css({"position":"absolute", "top" : ""});
	$('.menu-left').css({'position': 'fixed', "top":"", "height": windowHeight + 'px'});
}
function backTopHeader(){
	$('.menu-header').css({'position':''});
	$('.toolbar').css({'position':''});
	$('.toolbar.grnish').css({"position":"", "top":""});
	$('.right').css({"position":"", "top" : ""});
	$('.menu-left').css({'position': '',"height": "", "top":""});
}

// $('body .menu-left ul li').on("click", function () { // 点击左侧食品导航加active，同时右侧滑动到对应分类
//   // $('.left ul li').removeClass('active');
//   // $(this).addClass('active');
//   var i = $(this).index('body .left ul li');
//   $('body, html').animate({ scrollTop: $('body .right ul li').eq(i).offset().top }, 500);
// });

// +=======================================================================

// +=======================================================================
// *目录面板模块
// 首页导航条里的目录按钮，点击显示目录面板
$('.menu').on("click", (e) => {
	// hideElem($(this));
	// $('.menu-panel').fadeIn(300);
});

// 目录面板点击首页时的事件，点击隐藏所有滑出页面
$('#home').on('click', () => {
	shopConfig.isShownOrder = false;
	hideOrder(); //隐藏下单页
	hideFinish(); // 隐藏下单完成页面
	hideFoodPanel(); // 隐藏食品详情页面
})

// 目录模板会触发冒泡，目录页面点击任意按钮会隐藏目录模板（里面的事件会先执行）
$('.menu-panel').on('click', function() {
	$(this).fadeOut(200);
});

// 订单状态按钮，点击显示状态页面
$('#showStatus').on('click', () => {
	showFinish();
	hideElem($('.finish-footer'));
	$('#finish-content').css({
		height: (windowHeight - 70) + "px"
	});
});
// 隐藏状态页面
$('.status-back').on('click', () => {
	hideFinish();
	showElem($('.finish-footer'));
});

// 点击下单页面返回按钮，从下单页面返回
$('.order-back-box').on('click', function(e) {
	e.stopPropagation();
	hideOrder();
});
// 
function showOrderStatus() {
	// 请求订单
	// 展示返回的订单信息
	requestOrder(function(res) {
		setStatusConfig(res);
		$('.status-panel').css({
			"transform": "translate3d(0,0,0)",
			"opacity": 1
		});
	});

}

function hideOrderStatus() {
	$('.status-panel').css({
		"transform": "translate3d(100%,0,0)",
		"opacity": 0
	});
}
// +=======================================================================

// +=======================================================================
// *语言选择面板模块,点击后里面的元素切换语言后，会触发冒泡事件，执行事件后隐藏面板
// 首页导航条里的语言按钮
$('.language').on("click", (e) => {
	$('.language-panel').fadeIn(300);
});

// 选择语言面板
$('.language-panel').on('click', function() {
	$(this).fadeOut(200);
});

// 点击语言选择项，切换语言
$('#language-select li').on('click', function() {
	$(".cu-load").css("visibility","inherit");
	language.url = "img/" + $(this).data('lg') + ".svg";
	language.text = $(this).find("span").text();
	var languageClass = $(this).attr("class");
	var $language = $(".language");
	languageType = $(this).data('type');
	var loadTime = setTimeout(function(){
		
		// $language.attr("class", "language " + languageClass);
		// $language.find("span").text(language.text);
		//设置语言包
		setLanguage(languageType);
		// // 更新左边菜单
		initMenu(languageType);
		// // 更新菜品
		initMenuCata(languageType);
		// 更新自助餐数据
		requestShopBuffetMenu(languageType, getBuffetList);
		// // 更新语言
		setCache("languageType", languageType);
		// // 更新数据
		vueFood.$set(vueFood, "foodItems", menuInfo.dataList);
		vueFood.$set(vueFood, "foodTypes", menuInfo.menuCatalogList);
		// 
		// //
		if(getCache("displayType") == 0){
			$("#tab-menu").click();
		}else{
			$("#tab-buffet").click();
		}
		clearAllList(); // 清除所有
		clearTimeout(loadTime);
	},1000);
});
//
function setLanguage(languageType) {
	requestLanguage(languageType, urlData.shopId, (res) => {
		var data = res.dataList;
		for (var i = 0; i < data.length; i++) {
			languagePack[data[i]['languageCode']] = data[i]['languageValue'];
		}
		// 全局语言包赋值
		languageObj = languagePack;
		if(vueFood) vueFood.$set(vueFood,'languageOption',languageObj.sold_out);
		// 购物车
		$(".cart-title").text(languagePack.cart);
		// 清空
		$(".cart-clear").text(languagePack.clear);
		// 下一步
		$("#next").text(languagePack.next);
		// 店铺名-----
		$(".home-nav-name").text();
		// 店铺信息
		// $("#showStatus").text(languagePack.order);
		// 菜品详情------
		$(".food-panel .nav-title").text(languagePack.detail);
		// 订单状态
		$("#showStatus").text(languagePack.order_status);
		// 订单内容
		$("#order-items").prev("div").text(languagePack.order_list);
		// 下一步顶部----
		$(".order-panel .nav-title").text(languagePack.order);
		// 订单内容提示
		$(".buffet-status-panel .order-content>.no-add").text(languagePack.no_order_info);
		//
		$(".water-buy-text").text(languagePack.is_waiter_buy);
		$(".water-buy").text(languagePack.is_waiter_buy);
		// 订单结算详情------
		$(".finish-panel .nav-title").text(languagePack.order);
		//追加
		$("#service>span").text(languagePack.call);
		//呼叫客服
		$("#dingdan>span").text(languagePack.order);
		// 首页
		$("#addMore>span").text(languagePack.plus);
		//结算
		$("#checkout>span").text(languagePack.pay);
		//规格
		$(".specifications-panel .pop-header").text(languagePack.option + ":");

		$(".shops-service").text(languagePack.callfor);
		$(".shops-service").parent().find(".no").text(languagePack.no);
		$(".shops-service").parent().find(".yes").text(languagePack.yes);
		$(".del-all").text(languagePack.clear_car);
		$(".shop-service").text(languagePack.callfororder);
		$(".shop-service").parent().find(".no").text(languagePack.no);
		$(".shop-service").parent().find(".yes").text(languagePack.yes);
		$(".yes").text(languagePack.yes);
		$(".no").text(languagePack.no);
		$("#buffet-yes").text(languagePack.submit_buy_order);
		// tabbar-语言包
		$("#tab-menu").text(languagePack.menu);
		$("#tab-buffet").text(languagePack.buffet);
		$("#tab-order").text(languagePack.order);
		// 选择菜品标题
		$(".garnish-header .detail-title").text(languagePack.choose_dishes);
		// 详情
		$(".food-panel .detail-title").text(languagePack.menu_detail);
		// 追加套餐
		// $('.add-buffet-menu').text(languagePack.append_package);
		// 自助餐菜单
		// $('.add-buffet-dishes').text(languagePack.buffet_menu);
		//下一步
		$("#sp-next-btn").text(languagePack.next);
		$(".sum-total").text(languagePack.sum_total);
		$(".yzk-panel-title").text(languagePack.order_conform);
		$("#sp-update-order").text(languagePack.order_change);
		$("#sp-buy").text(languagePack.cart_plus);

		// 自助餐订单panel
		$(".buffet-msg").text(languagePack.order_conform_);
		// 内容
		$(".buffet-order-panel .order-content-title").text(languagePack.order_list);
		// 规格
		// vueFood.$set("option", languagePack.option); // 同步vue数量
		//小计
		$("#finish-money").prev("span").html(languagePack.sum_total);
		//税金
		$("#finish-tax").prev("span").html(languagePack.tax);
		//合计----
		$("#finish-total").parent().prev("span").html(languagePack.total);
		
		// 下单提示
		$(".submit-info-title").html('<img src="img/what.svg"/>'+languagePack.order_out_before);
		// 语言切换
		$(".language-content").html('<img src="img/home-p.svg"/>'+languagePack.language_switch);
	});
}

// +=======================================================================

// +=======================================================================
// *购物车模块
// 点击购物车会滑出购物车详情页面，详细显示里面食品
$('#cart').on('click', () => { // 点购物车图标显示，再次点击隐藏
	if (shopConfig.isShowCartPanel) {
		shopConfig.isShowCartPanel = false;
		$('.slide-panel').css({
			"transform": "translateY(100%)",
			"opacity": "0"
		});
	} else {
		shopConfig.isShowCartPanel = true;
		resetCartHtml();

		$('.slide-panel .cart-panel').css({
			'max-height': (windowHeight - 76) + 'px'
		});
		$('.shopping-cart').css({
			"opacity": "1",
			"transform": "translateY(0px)",
			"-webkit-transform": "translateY(0px)"
		});
	}
	setBodyOverflow() // 设置此作用是，防止弹出下单面板或者购物车面板或者下单完成面板时，到最底部后拖着滑动会显示出底下的图片问题
});

function resetCartHtml() { // 重置购物车内容
	if (vueFood.toolbarData == 'buffet') {
		$('#cart-content').html(initCartConfig(buffetConfig.cartHtml));
	} else if (vueFood.hasChoiceBuffet) {
		$('#cart-content').html(initCartConfig(buffetConfig.garnishCartHtml));
	} else if (shopConfig.hasFinishCheckout) {
		$('#cart-content').html(initCartConfig(addMoreConfig.cartHtml));
	} else {
		$('#cart-content').html(initCartConfig(shopConfig.cartHtml));
	}
}

// 购物车和食品详情面板里的加减点击事件（通用，add和reduce为class类名，找到按钮绑定），add为加字符串，reduce为减字符串
function initCartConfig(html) {
	var html = $(`${html}`);
	// 加事件
	html.find('.cart-add').on('click', function() {
		var elem = $(this);
		operateCartPanel(elem, true);
		// cartScale();
	});
	// 减事件
	html.find('.cart-reduce').on('click', function() {
		var elem = $(this);
		operateCartPanel(elem);
	});
	

	return html;
}

// 购物车滑动背景板点击到也会隐藏
$('.slide-panel-bg').on('click', () => {
	shopConfig.isShowCartPanel = false;
	showElem($('.cart-num'));
	setBodyOverflow();
	$('.slide-panel').css({
		"transform": "translateY(100%)",
		"opacity": "0"
	});
});
// 更新购物车内容同时更新主页面食品的数量内容
function operateCartPanel(elem, isAdd) {
	var id = elem.data('id');
	var list = cartList;
	if (vueFood.toolbarData == "buffet") {
		list = buffetList;
	} else if (vueFood.hasChoiceBuffet) {
		list = garnishList;
	} else if(shopConfig.hasFinishCheckout) {
		list = addMoreList;
	}
	if (isAdd) {
		addFoodToCart(list[id]); // 加数据到同时更新购物车
	} else {
		reduceCartFood(list[id]); // 减数据到同时更新购物车
	}
	if (!list[id]) { // 如果当前保存数据里没有这个id对应的类
		// delete(list[id]);
		elem.parent().parent().remove(); // 删除购物车内当前行商品内容
		return;
	}

	elem.siblings('.count').html(list[id].count); // 改变购物车内当前数量
}

// 购物车内清除所有按钮，点击显示确认删除面板
$('#delete-all').on('click', function() {
	if (!canOrder()) return;
	$('.delete-all-panel').fadeIn(200);
});

// 确认清除购物车内所有食品操作
$('#delete-yes').on('click', function() {
	clearCartList();
	$('.slide-panel').css({
		"transform": "translateY(100%)",
		"opacity": "0"
	});
	shopConfig.isShowCartPanel = false;	
	setBodyOverflow();
});

function cartScale() { // 加商品时购物车缩放效果
	$('#cart').addClass('cartScale');
}

$('#cart').on('animationend webkitAnimationEnd', () => {
	$('#cart').removeClass('cartScale');
});
// +=======================================================================

// +=======================================================================
// *主页右边食品面板事件
// 阻止主页上点击加餐冒泡（否则触发显示食品详情页）
$('.food-buy').on('click', function(e) {
	e.stopPropagation();
});

//详情面板返回按钮点击隐藏
$('.food-back-detail').on("click", () => {
	hideFoodPanel();
	// 清除已选配菜数量
	buffetConfig.garnishChoicedNum = 0;
});

function showSpecifications(food) { // 显示规格面板,同时设置选中参数currentFood(当前面板的食品参数)
	currentSpec = Object.assign({}, food); // 保存当前显示的商品信息
	// 请求数据
	ajaxRequest(
		'queryMenuSpecList?menuIndexId=' + food.menuIndexId + "&languageType=" + languageType, '', '',
		function(res) {
			if (res.appCode == 1) {
				var data = res.dataList;
				var specsHtml = '';
				for (var i = 0; i < data.length; i++) {
					var specsData = data[i];
					var active = i == 0 ? "active" : '';
					specsHtml += '<li class="' + active + '" data-sid="' + specsData.specIndexId + '" data-mid="' + specsData.menuIndexId +
						'" data-price="' + specsData.specPrice + '">' + specsData.specName + '</li>';
				}
				$("#specs-box").html(specsHtml);
				// 规格面板内容相关
				$('.pop-header.title').html(languageObj.option);
				$('.specifications-title').html(food.menuName);
				$('.specifications-count').html(food.count ? food.count : 0);
				$('.specifications-price').html(priceFormat(data[0]['specPrice']));
				$('.specifications-panel').fadeIn();
			} else {
				//请求失败处理

			}
		},
		// 网络故障的处理
		function() {},
		true
	)
}

$("#specs-box").on('click', 'li', function() {
	var _this = $(this);
	var className = _this.attr("class");
	if (className == "active") return false;
	_this.addClass("active").siblings("li").removeClass("active");
	$('.specifications-price').html(priceFormat(_this.data('price')));
});
// 添加规格
$('.specifications-add').on('click', function(e) {
	e.stopPropagation();
	var $spec = $("#specs-box>li.active");
	currentSpec.menuPrice = $spec.data('price'); //选中的规格价格
	currentSpec.menuIndexId = $spec.data('mid'); //menuIndexId
	currentSpec.specIndexId = $spec.data('sid'); //specIndexId
	if (currentSpec.menuName.indexOf("(") > 0) {
		currentSpec.menuName = currentSpec.menuName.split("(")[0];
	}
	currentSpec.menuName = currentSpec.menuName + "(" + $spec.text() + ")";
	var newSpec = Object.assign({}, currentSpec);
	addFoodToCart(newSpec);
	// $('.specifications-panel').fadeOut();
	// cartScale();
});
// 删除规格
$('.specifications-reduce').on('click', function(e) {
	e.stopPropagation();
	reduceCartFood(currentFood)
	// $('.specifications-panel').fadeOut();
	// cartScale();
});

$('#close-specifications').on('click', (e) => {
	e.stopPropagation();
	$('.specifications-panel').fadeOut();
});
//食品规格面板里面，点击选中当前
$('.center-ul li').each(function() {
	$(this).on('click', function() {
		$('.center-ul li').removeClass('active');
		$(this).addClass('active');
	})
});

// 食品详情面板里的加减事件
// 加操作
$('.food-add').on('click', function() {
	operateFoodPanel(true);
	// cartScale();
});
// 自助餐详情按钮追加套餐
$('.add-buffet-menu').on('click', function() {
	// +--------------------------------------------
	// 三期修改 强制设置成buffet 走自助餐提交流程
	// +--------------------------------------------
	vueFood.$set(vueFood,"toolbarData", "buffet"); 
	$('.footer').css({"display": "none"});
	// operateFoodPanel(true,true); //加数量
	showBuffetOrderPanel();
	// cartScale();
});

// 减操作
$('.food-reduce').on('click', function() {
	operateFoodPanel();
});

function operateFoodPanel(isAdd,buffet) { // 更新食品详情面板参数，刷新页面内容
	// +--------------------------------------------
	// 三期修改 id值需要从menuIndexId 或者catalogIndexId取值
	// +--------------------------------------------
	var id = currentFood.menuIndexId || currentFood.catalogIndexId;
	if (vueFood.toolbarData === "buffet") {
		// if(refreshBuyBuffet(currentFood)) return; // 如果已下单，就只能和已下单自助餐保持一致(弹出提示)，否则购物车已有自助餐，再加入不一样的自助餐也会清除,同时无法下单会返回true
	}
	if (isAdd) {
		addFoodToCart(currentFood);
	} else {
		reduceCartFood(currentFood);
	}
	var list = cartList;
	if (vueFood.toolbarData == 'buffet') {
		list = buffetList;
	} else if (vueFood.hasChoiceBuffet) {
		list = garnishList;
	} else if (shopConfig.hasFinishCheckout) {
		list = addMoreList;
	}
	if (!list[id]) {
		// delete(list[id]);
		resetPanelCount(list[id]);
		return;
	}
	resetPanelCount(list[id]);
	//如果buffet不为空,说明是自助餐详情页面的购买按钮,则直跳转到是否购买自助餐的panel页面
	if(!isEmpty(buffet)){
		$("#next").click();
	}
}

function showFoodPanel(food,buffet) { // 显示食品详情面板,同时设置选中参数currentFood(当前面板的食品参数)
	// food.typeIndex = type; // 显示商品详情时，先保存类型索引和商品索引
	// food.index = index;
	currentFood = food; // 保存当前显示的商品信息
	resetPanelCount(food); // 判断数量相关操作
	//如果buffet不为空,说明是自助餐详情页面的购买按钮,则直跳转到是否购买自助餐的panel页面
	if(!isEmpty(buffet)){
		// hideElem($('.footer'));
	}
	// languageObj.option;
	$('.food-content .food-specifications').html('<img src="img/guige.png" style="width: 30px;">');
	// 商品详情内容相关
	if (food.whetherSpec === '1') {
		$('#buffet-panel-specifications').css({
			"display": "block"
		});
		$('#buffet-panel-buy').css({
			"display": "none"
		});
	} else {
		$('#buffet-panel-buy').css({
			"display": "block"
		});
		$('#buffet-panel-specifications').css({
			"display": "none"
		});
	}
	// 如果为true，则显示详情页面按钮
	if(getCache("btn_show_detail")){
		$(".buffet-btn-box").show();
		$("#buffet-panel-buy").hide();
	}else{
		$(".buffet-btn-box").hide();
		$("#buffet-panel-buy").show();
		if (food.whetherSpec === '1') {
			$("#buffet-panel-buy").hide();
		}
	}
	if (vueFood.toolbarData === 'buffet'  || $("#tab-buffet").parent().attr("class") == "active") {
		// let imgSrc = isEmpty(food.buffetImage)?noneImage:(picPrefix+food.buffetImage);
		// // 二期修改详情页面默认显示图片
		// if(isEmpty(imgSrc) ||  imgSrc.indexOf("default_dishes")>0){
		// 	imgSrc = "./img/detail_dishes.svg";
		// }
		// $(".bgImg-detail").css({
		// 	"background-image": "url(" + imgSrc + ")"
		// });
		// +---------------------------
		// 三期修改 图片
		// +---------------------------
		if(isEmpty(food.menuImage) && isEmpty(food.buffetImage)){
			$(".bgImg-detail>div").css({"background-image": "url(" + noneImage + ")"});
			$(".bgImg-detail").css({"background-image": "url()"});
		}else{
			var img1 = food.menuImage;
			var img2 = food.buffetImage;
			var imgDetail = isEmpty(img1)?img2:img1;
			$(".bgImg-detail").css({"background-image": "url(" + picPrefix+imgDetail + ")"});
			$(".bgImg-detail>div").css({"background-image": "url()"});
		}
		var menuIntroduce = food.menuIntroduce != undefined ? food.menuIntroduce:food.buffetIntroduce
		$('.food-panel-info').html(menuIntroduce);
		var menuPrice = food.menuName != undefined ? food.menuName:food.catalogName;
		$('.food-panel-title').html(menuPrice);
		$('.food-count').html(food.count ? food.count : 0);
		var menuPrice = food.menuPrice != undefined ? food.menuPrice:food.buffetPrice;
		$('.food-panel-price').html(priceFormat(menuPrice));
		if(!food.zizhucan && getCache("isShowPrice")){
			$('.food-panel-price').parent().css({"visibility":"inherit"});
		}else{
			$('.food-panel-price').parent().css({"visibility":"hidden"});
		}
		
		var whetherBuffetStr= $("#whetherBuffet").val();  //判断是否下载个自助餐
		// if(whetherBuffetStr=='0') $('.add-buffet-dishes').css({"display":"block"});
		// else $('.add-buffet-dishes').css({"display":"none"});
	} else {
		console.log(food,456);
		// let imgSrc = isEmpty(food.menuImage)?noneImage:(picPrefix+food.menuImage);
		// // 二期修改详情页面默认显示图片
		// if(isEmpty(imgSrc) ||  imgSrc.indexOf("default_dishes")>0){
		// 	imgSrc = "./img/detail_dishes.svg";
		// }
		// $(".bgImg-detail").css({
		// 	"background-image": "url(" + imgSrc+ ")"
		// });
		
		// +---------------------------
		// 三期修改  图片
		// +---------------------------
		if(isEmpty(food.menuImage) && isEmpty(food.buffetImage)){
			$(".bgImg-detail>div").css({"background-image": "url(" + noneImage + ")"});
			$(".bgImg-detail").css({"background-image": "url()"});
		}else{
			var img1 = food.menuImage;
			var img2 = food.buffetImage;
			var imgDetail = isEmpty(img1)?img2:img1;
			$(".bgImg-detail").css({"background-image": "url(" + picPrefix+imgDetail + ")"});
			$(".bgImg-detail>div").css({"background-image": "url()"});
		}
		$('.food-panel-info').html(food.menuIntroduce);
		$('.food-panel-title').html(food.menuName);
		$('.food-count').html(food.count ? food.count : 0);
		$('.food-panel-price').html(priceFormat(food.menuPrice));
		// +-----------------------
		// 三期修改 根据isShowPrice判断是否隐藏价格
		// +-----------------------
		if(getCache("isShowPrice")){
			$('.food-panel-price').parent().css({"visibility":"inherit"});
		}else{
			$('.food-panel-price').parent().css({"visibility":"hidden"});
		}
	}
	
	
	$('.food-panel').fadeIn();
	// +---------------------------
	// 三期修改  如果是自助餐
	// +---------------------------
	if(food.iszizhucan){
		setCache('iszizhucan',true);
	}else{
		setCache('iszizhucan',false);
	}
	
}

function resetPanelCount(food) { // 判断食品详情面板里的当前数量，没有数量隐藏减和数量栏
	if (!food || !food.count) {
		// console.log(!food.count);//解决详情面板减号和数字不显示的问题20190914
		$('.food-count').css({
			"display": "none"
		});
		$('.food-reduce').css({
			"display": "none"
		});
	} else {
		$('.food-count').html(food.count).css({
			"display": "block"
		});
		$('.food-reduce').css({
			"display": "block"
		});
	}
}

function hideFoodPanel() { // 隐藏食品详情面板
	$('.food-panel').fadeOut(300);
	$('#next').html(languageObj.next);
	hideHomeButton();
	if (vueFood.toolbarData === 'buffet' && !vueFood.hasChoiceBuffet) {
		hideElem($('.footer'));
	}else{
		showElem($('.footer'));
	}
}

$('#buffet-panel-specifications').on('click', function() { // 食品详情面板里的规格按钮
	stepPage(currentFood);
});
//+================================================
//*这里操作为加减食品时的操作，会更新购物车内容和总数量价格参数，下单页面参数
function addFoodToCart(food) { // 添加食品
	if (vueFood.toolbarData == 'buffet') {
		operateCart(food, buffetList, true);
	} else if (vueFood.hasChoiceBuffet) {
		operateCart(food, garnishList, true);
	} else if (shopConfig.hasFinishCheckout) { // 追加食品状态
		operateCart(food, addMoreList, true);
	} else {
		operateCart(food, cartList, true);
	}
	setNext(canOrder(),false); // 设置下一页按钮状态
}

function reduceCartFood(food) { // 减少食品
	if (vueFood.toolbarData == 'buffet') {
		operateCart(food, buffetList,false);
	} else if (vueFood.hasChoiceBuffet) {
		operateCart(food, garnishList,false);
	} else if (shopConfig.hasFinishCheckout) {
		operateCart(food, addMoreList,false);
	} else{
		operateCart(food, cartList,false);
	}
	setNext(canOrder(),false);
}

function operateCart(food, list, isAdd) { // 操作加减时的数据函数
	var id = '';
	if (vueFood.toolbarData === "buffet") { // 现在自助餐和普通菜单区分开，里面的名字不同
		id = food.catalogIndexId;
		
	}else if(!isEmpty(food.buffetListData) && isEmpty(food.buffetListUuid)){// |--------如果是规格菜品并且buffetListUuid为空，说明是第一次加入 @1212 9
		id = food.menuIndexId+getUuid();
		food.buffetListUuid = id;
	}else if(!isEmpty(food.buffetListData) && !isEmpty(food.buffetListUuid)){// |--------如果是规格菜品并且buffetListUuid为不为空，说明是操作购物车增加规格  @1212 10
		id =food.buffetListUuid;
	}else {
		var str = "";
		var specIndexId = isEmpty(food.specIndexId) ? '' : food.specIndexId;
		if (!isEmpty(specIndexId)) {
			str = "_" + specIndexId;
		}
		id = food.menuIndexId + str;
	}
	var num = parseInt(food.count ? food.count : 0); // 商品数量
	if (specIndexId && list[id]) { // 如果有规格ID，就用当前购物数据（cartList或者AddMoreList）里的数量值
		num = list[id].count ? list[id].count : 0;
	}
	
	if (isAdd) {
		// +--------------
		//	三期判断可选数量
		// +---------------
		if (vueFood.hasChoiceBuffet || $("#tab-buffet").parent().attr("class") == "active") { // 已经下单自助餐的情况下
			if (buffetConfig.garnishChoicedNum >= vueFood.garnishCount) { // 自助餐可选配菜数量大于可选的最大值
				showToast(languageObj.buffet_over_full);
				return;
			} else { // 自助餐可选配菜加1
				buffetConfig.garnishChoicedNum += 1;
			}
		}
		cartScale(); // 购物车那里数量缩放动画，放在这里可以只写一次(把面板、主页、规格、购物车的都加到了这里)
		num += 1;
	} else {
		num = num - 1 < 0 ? 0 : num - 1;
		if (vueFood.hasChoiceBuffet || $("#tab-buffet").parent().attr("class") == "active") buffetConfig.garnishChoicedNum -= 1;
	
	}

	if (num == 0) { //如果商品数量归零，删除这条保存的购物车数据
		delete(list[id]);
		// +------------------
		// |减少时筛选匹配规格数据(当count为0时删除匹配到的规格，不为0时更新规格数据buyNum值 == food.count)
		// +------------------
		// for(var i=0;i<buffetConfig.specDataList.length;i++){
		// 	if(food.menuIndexId == buffetConfig.specDataList[i].menuIndexId){
		// 		buffetConfig.specDataList.splice(i--,1);
		// 	}
		// }
	} else { //否则，保存更新后的数据
		list[id] = food;
		// +------------------
		// |增加时筛选匹配规格数据(更新规格数据buyNum值 == food.count)三期修复
		// +------------------
		// buffetConfig.specDataList.forEach((item,index)=>{
		// 	if(food.menuIndexId == item.menuIndexId){
		// 		item.buyNum = num;
		// 	}
		// });
	}
	vueFood.$set(food, 'count', num); // 同步vue数量
	setPriceFun(); // 更新下方购物车价格和数量
	resetPanelCount(food); // 更新购物车面板里的数量
}

function setPriceFun() { // 更新下方购物车价格和数量的函数，要重复使用  就提出来了
	// if(shopConfig.hasFinishCheckout) { setTotalPrice(list,addMoreConfig); } // 购物车下方相关
	// else if(vueFood.toolbarData=='buffet'){ setTotalPrice(list,buffetConfig); }
	// else {setTotalPrice(list,shopConfig);}
	if (vueFood.toolbarData == 'buffet') {
		setTotalPrice(buffetList, buffetConfig);
	} else if (vueFood.hasChoiceBuffet) {
		setTotalPrice(garnishList, buffetConfig);
	} else if (shopConfig.hasFinishCheckout) { // 购物车下方相关
		setTotalPrice(addMoreList, addMoreConfig);
	} else {
		setTotalPrice(cartList, shopConfig);
	}
}
// +---------------------------------
// 三期订单页面增加小计总计税金
// +---------------------------------
var priceInfo = {};
function setTotalPrice(list, config) { // 设置下方购物车的参数，并且计算当前购物车面板和下单面板参数
	var totalPrice = 0; // 总价
	var totalCount = 0; // 总数量

	if (vueFood.hasChoiceBuffet) { // 如果是自助餐配菜
		config.garnishCartHtml = ''; // 购物车面板里的内容
		config.garnishOrderHtml = ''; // 下单面板里的内容
	} else {
		config.cartHtml = ''; // 购物车面板里的内容
		config.orderHtml = ''; // 下单面板里的内容
	}
	// +---------------------------------
	// 三期订单页面增加小计总计税金
	// +---------------------------------
	var totalTax10=0,//税前
		totalTax8=0,//税前
		totalTaxB10=0,//税后
		totalTaxB8=0;//税后
	for (var key in list) {
		var food = list[key];
		if (vueFood.toolbarData === 'buffet') { // 自助餐的属性
			var price = food.buffetPrice * food.count;
			var foodName = food.catalogName || food.menuName;
			var menuPrice = priceFormat(food.buffetPrice);
			var id = food.catalogIndexId;
			
			// +---------------------------------
			// 三期订单页面增加小计总计税金
			// +---------------------------------
			// 如果是catalogType==0   10%税
			if(isEmpty(parseInt(food.catalogType))){
				if(!isEmpty(taxType) && taxType === 1){
					totalTax10 += parseInt(price);
				}else{
					totalTaxB10 += parseInt(price);
				}
			}else{
				if(!isEmpty(taxType) && taxType === 1){
					totalTax8 += parseInt(price);
				}else{
					totalTaxB8 += parseInt(price);
				}
			}
			
		} else { // 主菜单里的属性
			var price = food.menuPrice * food.count;
			var foodName = food.menuName;
			var menuPrice = priceFormat(food.menuPrice);
			var id = food.menuIndexId;
			if (food.specIndexId) { // 如果存在规格ID，就用拼接ID 
				id += '_' + food.specIndexId;
			}
			
			// 三期修改用于从此列表中匹配taxType参数 
			let objData = vueFood.foodTypes.filter(function(item){
				 return food.catalogIndexId == item.catalogIndexId
			});
			let taxType = '';
			if(!isEmpty(objData[0]) &&  !isEmpty(objData[0].taxType)){
				taxType = parseInt(objData[0].taxType);
			}
			// +---------------------------------
			// 三期订单页面增加小计总计税金
			// +---------------------------------
			// 如果是catalogType==0   10%税
			if(isEmpty(parseInt(food.catalogType))){
				// +---------------------------------
				// 三期订单页面增加taxType判断 1税前 2税后
				// +---------------------------------
				if(!isEmpty(taxType) && taxType === 1){
					totalTax10 += parseInt(price);
				}else{
					totalTaxB10 += parseInt(price);
				}
				
			}else{
				// +---------------------------------
				// 三期订单页面增加taxType判断 1税前 2税后
				// +---------------------------------
				if(!isEmpty(taxType) && taxType === 1){
					totalTax8 += parseInt(price);
				}else{
					totalTaxB8 += parseInt(price);
				}
			}
		}
		// +---------------------------
		// |如果存在规格菜品，说明是规格，那么id等于menuindexid+uuid  @1212 15
		// +---------------------------
		if(!isEmpty(food.buffetListData)){
			var id = key;
		}
		
		totalCount += parseInt(food.count);
		totalPrice += parseInt(price);
		// +-----------------------
		// 三期修改 根据isShowPrice判断是否隐藏价格
		// +-----------------------
		var firstOrderPrice = "";
		// 如果是自助餐 并且是套餐  并且显示价格
		if(food.iszizhucan && !food.zizhucan && getCache("isShowPrice")){
			firstOrderPrice = menuPrice+'円';
		}
		
		let buffetListDataHtml = "";
		// 如果不是自助餐 并且显示价格
		if(!food.iszizhucan && getCache("isShowPrice") ){
			firstOrderPrice = menuPrice+'円';
		}
		// +-----------------
		// |如果存在规格则 @1212  11
		// +-----------------
		if(!isEmpty(food.buffetListData)){
			let buffetListHtml = "";
			// 规格配菜名字
			let buffetNameArr =  food.buffetListName.split(",");
			buffetNameArr.forEach((item)=>{
				buffetListHtml += '<div class="steps-panel-list order-box"><span class="item-title">'+item+'</span><span class="item-price price-color"></span></div>';
			});
			buffetListDataHtml = '<div class="steps-panel-box mt-2">'+buffetListHtml+'</div>';
			// 要加上规格配菜的价格
			if(!food.iszizhucan && getCache("isShowPrice")){
				firstOrderPrice = priceFormat(price)+'円';
			}
		}
		// var firstOrderPrice = !food.zizhucan || getCache("isShowPrice")?menuPrice+'円':'';
		// 二期修改 增加税的判断（1外带显示*号 or 0不外带店里不显示*号）
		let suffixString = food['catalogType'] === '1'?'*':'';
		
		
		// foodName = isEmpty(food.buffetListName)?foodName:foodName+"("+food.buffetListName+")";
		var orderItem =
			`<div class='order-item'><span class='item-title'>${foodName}${suffixString}</span><span class='item-count'>${food.count}</span><span class='item-price price-color'>${firstOrderPrice}</span></div>`+buffetListDataHtml;
		if (vueFood.hasChoiceBuffet) { // 如果是自助餐配菜
			foodName = isEmpty(food.buffetListName)?foodName:foodName+"("+food.buffetListName+")";
			config.garnishCartHtml +=
				`<li><div class='food-title'>${foodName}</div><div class='food-buy'><img src="img/add.svg" data-id='${id}' data-type='${food.typeIndex}' data-type='${food.index}' class='cart-add'><div class='count cart-count'>${food.count}</div><img src="img/reduce.svg" data-id='${id}' data-type='${food.typeIndex}' data-type='${food.index}' class='cart-reduce'></div></li>`;
			config.garnishOrderHtml += orderItem;
		} else {
			foodName = isEmpty(food.buffetListName)?foodName:foodName+"("+food.buffetListName+")";
			config.cartHtml +=
				`<li><div class='food-title'>${foodName}</div><div class='food-buy'><img src="img/add.svg" data-id='${id}' data-type='${food.typeIndex}' data-type='${food.index}' class='cart-add'><div class='count cart-count'>${food.count}</div><img src="img/reduce.svg" data-id='${id}' data-type='${food.typeIndex}' data-type='${food.index}' class='cart-reduce'></div></li>`;
			config.orderHtml += orderItem;
		}
	}
	
	// +---------------------------------
	// 三期订单页面增加小计总计税金
	// +---------------------------------
	//税前10%
	var sq10 = parseInt(totalTax10)>0?Math.round(totalTax10*0.1):0;
	//税后10%
	var sh10 = parseInt(totalTaxB10)>0?(totalTaxB10-parseInt(Math.round(totalTaxB10/1.1))):0;
	//合计10%
	priceInfo.taxes10 = parseInt(sq10) + parseInt(sh10);
	//税前8%
	var sq8 = parseInt(totalTax8)>0?Math.round(totalTax8*0.08):0;
	//税后8%
	var sh8 = parseInt(totalTaxB8)>0?(totalTaxB10-parseInt(Math.round(totalTaxB8/1.08))):0;
	//合计8%
	priceInfo.taxes8 = parseInt(sq8) + parseInt(sh8);
	
	// priceInfo.total = parseInt(totalTax8)+parseInt(totalTax10);//小计
	// 总价加税前的利息
	priceInfo.subtotal = parseInt(totalPrice) +  parseInt(sq10) + parseInt(sq8);//总价
	
	config.totalPrice = priceFormat(parseInt(totalPrice) + parseInt(sq10) + parseInt(sq8)); // 保存的总价，用于下单完成页面
	// config.totalTax = priceFormat(totalPrice * 0.1); // 保存的税值，先取10%做测试，用于下单完成页面
	$('#cart-num').html(totalCount); // 更新购物车总数量

	$('#cart-total').html(priceFormat(totalPrice)); // 更新购物车总价
	// +-----------------------
	// 三期修改根据isShowPrice判断是否隐藏价格
	// +-----------------------
	isShowPrice();
	
	// 如果是在订单内容页面，那么操作购物车就加载刷新订单内容
	if (shopConfig.isShownOrder) {
		if (vueFood.hasChoiceBuffet){
			$('#order-items').html(config.garnishOrderHtml); // 如果是配菜订单，不一样
		}else{
			$('#order-items').html(config.orderHtml);
		}
	}
	//20190905 02:00
	// if(vueFood.hasChoiceBuffet) $('#cart-total').css({"display":"none"});
	// else $('#cart-total').css({"display":"block"});
}

// +=======================================================================

// +=======================================================================
// +---------------------------------
// 三期订单页面增加小计总计税金 组装html
// +---------------------------------
function setOrderToalHtml(){
	var taxHtml10='',taxHtml8='';
	
	if(priceInfo.taxes10>0){
		taxHtml10 = `<p>
					<span class="title-left">${languagePack.taxes}:</span>
					<span id="finish-tax">${priceFormat(priceInfo.taxes10)}</span>円
					<span class="suffix-info">${languagePack.tax10}</span>
					</p>`;
	}
	if(priceInfo.taxes8>0){
		taxHtml8 = `<p>
					<span class="title-left">${languagePack.taxes}:</span>
					<span id="finish-tax">${priceFormat(priceInfo.taxes8)}</span>円
					<span class="suffix-info">${languagePack.tax8}</span>
					</p>`;
	}
	//  `<p>
	// 				<span class="title-left">${languagePack.sum_total}:</span>
	// 				<span id="finish-money">${priceFormat(priceInfo.total)}</span>円 
	// 				<span class="suffix-info">&nbsp;(${languagePack.tax_info})</span>
	// 			</p>
	var html =`<p class="bold">
					<span class="title-left">${languagePack.total}:</span> 
					<span id="finish-total">${priceFormat(priceInfo.subtotal)}</span>円
					<span class="suffix-info">(${languagePack.tax_included})</span>
				</p>${taxHtml10}${taxHtml8}
				<p><span class="suffix-info">(${languagePack.tax_info})</span></p>
				`;
	if($("#tab-buffet").parent("li").attr("class")=='active'){
		$("#order-total-box").removeClass("item").html('');
		
	}else{
		// +-----------------------
		// 三期修改 根据isShowPrice判断是否隐藏价格
		// +-----------------------
		if(getCache("isShowPrice")){
			$("#order-total-box").addClass("item").html('<div class="item total-money"  style="line-height: 30px;padding: .2rem 0;">'+html+'</div>');
		}else{
			$("#order-total-box").removeClass("item").html('');
		}
	}
	
}

// 下方购物车行的下一次按钮，点击触发下单或者完成下单页面，具体看到哪一步，第一次先到下单页面，再点击到下单详情页面
$('#next').on('click', () => {
	if (!canOrder()) {
		return;
	}
	if (vueFood.toolbarData === 'buffet') {
		showBuffetOrderPanel();
	} else {
		if (!shopConfig.isShownOrder) { // 第一次点下一次按钮到下单预览内容页面
			// 如果当前显示的是订单内容页面,给个标识，用于订单内容页面购物车操作
			showOrder();
			// +---------------------------------
			// 三期订单页面增加小计总计税金 设置组装html
			// +---------------------------------
			setOrderToalHtml();
			
			$('#next').text(languageObj.order_before);
		} else { // 再次点击下一次按钮到完成订单页面（反正是第二个下单页面）
			var param = {};
			var list = {}; // 设置临时变量list,方便cartList或者addMoreList赋值给它
			param.accessToken = getAccessToken();

			// 下单
			if (vueFood.hasChoiceBuffet) {
				list = garnishList;
			} else {
				 if (vueFood.toolbarData == 'buffet') {
					list = buffetList;
				} else if (!shopConfig.hasFinishCheckout) { // 如果是第一次下单
					list = cartList;
				} else {
					// 追加订单
					list = addMoreList;
				}
			}
			// param.orderLines = mergerData(list, buffetConfig.specDataList);
			//已经将规格存入list
			param.orderLines = mergerData(list, buffetConfig.specDataList);
			if (isEmpty(urlData.accessToken)) { // 如果是店铺扫码入，显示完成订单页不清除购物车信息
				showFinish(); // 显示订单完成页时会请求，然后解析商品数据，同时生成HTML
				setTimeout(hideOrder, 300);

			} else { // 如果是桌号扫码入，请求保存订单信息，以及相关后续操作settSuccess
				ajaxRequest(
					'saveOrder', param, '',
					function(res) {
						if (res.appCode == 1) {
							// 结算成功后处理数据
							settSuccess();
						} else {
							//请求失败处理
						}
					},
					// 网络故障的处理
					function() {},
					false
				)

				clearCartList(); // 从桌面扫入，订单完成后清除购物车数据和主页上的数量
			}
			shopConfig.isShowCartPanel = false;
			$('.slide-panel').css({
				"transform": "translateY(100%)",
				"opacity": "0"
			});

		}
	}
});
//  扫桌号码下单成功后操作
function settSuccess() {
	afterFinish(); // 下好单后保存要结算的数据

	showFinish(); // 显示完成订单页面
	var list = cartList;
	if (vueFood.hasChoiceBuffet) {		
		queryBufferMenuList(languageType, getGarnishList);		
		list = garnishList;	
	}else {
		if (vueFood.toolbarData == 'buffet') {
			list = buffetList;
		} else if (shopConfig.hasFinishCheckout) {
			list = addMoreList;
		}
	}

	resetFoodConfig(list);

	if (!shopConfig.hasFinishCheckout) {
		if(!vueFood.hasChoiceBuffet) shopConfig.hasFinishCheckout = true;
	}
	buffetConfig.specDataList = [];
	setTimeout(hideOrder, 300);
}

// 判断下单时是否有商品
function canOrder() {
	var hasFood = false;
	if (vueFood.toolbarData == 'buffet') {
		if (Object.keys(buffetList).length) hasFood = true // 判断食品参数是否有食品
	} else if (vueFood.hasChoiceBuffet) {
		if (Object.keys(garnishList).length) hasFood = true // 判断食品参数是否有食品
	} else if (shopConfig.hasFinishCheckout) { // 已经完成呼叫买单选项一次，就是追加操作
		if (Object.keys(addMoreList).length) hasFood = true // 判断追加的食品参数是否有食品
	} else { // 完成呼叫买单选项
		if (Object.keys(cartList).length) hasFood = true // 判断食品参数是否有食品
	}
	return hasFood;
}

// 设置下一次按钮的样式
function setNext(hasFood,isInit) {
	if(isInit){
		$('#next').css({'background': "rgb(206, 206, 206)"});
		return false;
	}
	if(!hasFood && !isInit){
		$('#next').css({'background': "rgb(206, 206, 206)"});
		return false;
	}
	if (hasFood && $("#tab-buffet").parent().attr("class") == "active") {
		$('#next').css({'background': "rgb(107, 184, 32)"});
	}else {
			$('#next').css({'background': "rgb(35, 148, 255)"});
	}
}

// 下单完成面板返回按钮，点击从下单完成后的页面返回
$('.finish-back-box').on('click', function(e) {
	e.stopPropagation();
	hideFinish();
});

function showOrder() { // 显示下单面板
	if (vueFood.hasChoiceBuffet) {
		$('#order-items').html(buffetConfig.garnishOrderHtml);
	} else if (shopConfig.hasFinishCheckout) {
		$('#order-items').html(addMoreConfig.orderHtml);
	}else{
		$('#order-items').html(shopConfig.orderHtml);
	}
	// +-----------------------------------
	// 三期修改增加还未下单的提示
	// +-----------------------------------
	shopConfig.isShownOrder = true;
	$('.order-panel').css({
		"transform": "translate3d(0,0,0)",
		"opacity": 1
	});
	$('.order-panel .order-content').css({
		"height": (windowHeight - 121) + 'px'
	});
	setBodyOverflow() // 设置此作用是，防止弹出下单面板或者购物车面板或者下单完成面板时，到最底部后拖着滑动会显示出底下的图片问题
}

function hideOrder() { // 隐藏下单面板
	shopConfig.isShownOrder = false;
	$('.order-panel').css({
		"transform": "translate3d(100%,0,0)",
		"opacity": 0
	});
	// 下一步
	$("#next").text(languagePack.next);
	setBodyOverflow() // 设置此作用是，防止弹出下单面板或者购物车面板或者下单完成面板时，到最底部后拖着滑动会显示出底下的图片问题
}

function showFinish() { // 显示完成下单的面板
	$('#finish-content').css({
		height: (windowHeight) + "px"
	});
	if (vueFood.hasChoiceBuffet) showHomeButton();
	shopConfig.isShowFinish = true;
	if (isEmpty(urlData.accessToken)) {
		$(".finish-footer").hide();
	} else {
		$(".finish-footer").show();
	}
	$('.finish-panel').css({
		"transform": "translate3d(0,0,0)",
		"opacity": 1
	});
	hideElem($('.footer'));
	setFinishPanel(); // 加载完成订单页面内容
	setBodyOverflow() // 设置此作用是，防止弹出下单面板或者购物车面板或者下单完成面板时，到最底部后拖着滑动会显示出底下的图片问题
}

function hideFinish() { // 隐藏完成下单的面板
	shopConfig.isShowFinish = false;
	hideHomeButton();
	$('.finish-panel').css({
		"transform": "translate3d(100%,0,0)",
		"opacity": 0
	});
	$('.toolbar li.active').click();
	// 下一步
	$("#next").text(languagePack.next);
	if (vueFood.toolbarData !== 'buffet' && vueFood.toolbarData !== 'order') showElem($('.footer'));
	setBodyOverflow() // 设置此作用是，防止弹出下单面板或者购物车面板或者下单完成面板时，到最底部后拖着滑动会显示出底下的图片问题
}

// 根据请求返回的数据，设置完成下单页面的内容
function setFinishPanel() {
	// 获取url参数 shopId accessToken (如果是桌位码扫码shopId accessToken 两个参数齐全  如果是店铺二维码扫码进来则只有shopId)
	if (isEmpty(urlData.accessToken)) { // 从店铺二维码扫入
		hideElem($('.total-money')); // 没有加个参数隐藏
		$('.finish-panel').attr('class', 'finish-panel status-panel');
		// 根据请求数据，刷新订单内容
		requestOrder(function(res) { // 扫店铺进入
			// setStatusConfig(res);
			// 不保存数据，也不清空购物车 ，直接发送带订单list的请求，得到数据转换成HTML
			var html = getStatusHtml(res.dataList);
			$('#finish-content').html(html);
		}, cartList, languageType,buffetConfig.specDataList);
	} else {
		showElem($('.total-money')); // 没有加个参数隐藏
		// 根据请求数据，刷新订单内容
		requestOrderLine(languageType, function(res) { // 从桌子二维码扫入
			//组装数据（对数据进行追加和首次下单区分处理）
			setStatusConfig(res);
		});

		var moreHtml = '';
		var html = '',
			buffetDataHtml = ''; //自助餐配餐Html
		for (var key in statusConfig) {
			var list = statusConfig[key];
			if (key != 'data') {
				if (key == 'moreList') {
					if (!Object.keys(list).length) continue;
					moreHtml = getOrderHtml(moreHtml, list, true);
				} else if (key == 'buffetList') {
					if (!Object.keys(list).length) continue;
					html = getOrderHtml(html, list);
				} else if (key == 'garnishList') { // 20190907
					if (!Object.keys(list).length) continue;
					buffetDataHtml = getGarnishHtml(buffetDataHtml, list, statusConfig.data.buffetCatalogName);
				} else {
					if (!Object.keys(list).length) continue;
					html = getOrderHtml(html, list);
				}
			}
		}
		if (!isEmpty(statusConfig)) {
			var deskAmount = priceFormat(statusConfig['data'].deskAmount);
			deskAmount = getCache("isShowPrice")?deskAmount+'円':'';
			var tableHtml =
				`<div class='order-item'><span class='item-title'>${languagePack.desk_amount}</span><span class='item-count'>${statusConfig['data'].eatNum}</span><span class='item-price price-color'>${deskAmount}</span></div>`;
			// 二期修改 增加税的判断（8% or 10%）
			let amountHTml10 =  statusConfig['data'].taxAmount === "0"?'':`<p><span class='title-left'>${languagePack.taxes}:</span><span id='finish-tax'>${priceFormat(statusConfig['data'].taxAmount)}</span>円<span class="suffix-info"> ${languagePack.tax10}</span></p>`;
			let amountHTml8 =  statusConfig['data'].outTaxAmount === "0"?'':`<p><span class='title-left'>${languagePack.taxes}:</span><span id='finish-tax'>${priceFormat(statusConfig['data'].outTaxAmount)}</span>円<span class="suffix-info">  ${languagePack.tax8}</span></p>`;
			
			// <p>
			// 	<span class='title-left'>${languagePack.sum_total}:</span>
			// 	<span id='finish-money'>${priceFormat(statusConfig['data'].smallTotalAmount)}</span>円 
			// 	<span class="suffix-info">&nbsp;(${languagePack.tax_info})</span>
			// </p>
			var moneyHtml =
				`<div class='item total-money'>
				<p class='bold'>
					<span class='title-left'>${languagePack.total}:</span> 
					<span id='finish-total'>${priceFormat(statusConfig['data'].totalAmount)}円</span>
					<span class="suffix-info">&nbsp;(${languagePack.tax_included})</span>
				</p>
					${amountHTml10}${amountHTml8}
				<p><span class="suffix-info">(${languagePack.tax_info})</span></p>
				</div>`;
			
			// +-----------------------
			// 三期修改 根据isShowPrice判断是否隐藏价格
			// +-----------------------
			moneyHtml = getCache("isShowPrice")?moneyHtml:''
			//20190905
			var finishHtml = moreHtml + html + tableHtml + moneyHtml + buffetDataHtml;
			if(!isEmpty(buffetDataHtml)){
				$(".buffet-status-panel>.order-content").html(finishHtml + '</div>'+'<div class="order-item add-cata-box"><span class="add-cata-btn">' + languageObj.append_dishes + '</span></div>').css({
					"marginBottom": "80px"
				}).find(".total-money").css({
					"lineHeight": "30px"
				});
				$('#finish-content').html(finishHtml + '</div>' +
					'<div class="order-item add-cata-box"><span class="add-cata-btn">' + languageObj.append_dishes + '</span></div>');
			}else{
				$(".buffet-status-panel>.order-content").html(finishHtml + '</div>'+'</div>').css({
					"marginBottom": "80px"
				}).find(".total-money").css({
					"lineHeight": "30px"
				});
				$('#finish-content').html(finishHtml + '</div>');
			}
			
			
			// if (buffetConfig.hasFinishBuffet) {
			// 	$('#finish-content').html(finishHtml + '</div>' +
			// 		'<div class="order-item add-cata-box"><span class="add-cata-btn">' + languageObj.append_dishes + '</span></div>');
			// } else {
			// 	$('#finish-content').html(finishHtml + '</div>');
			// }
		} else {
			$('#finish-content').html("<div class='order-content-title item no-add text-center'>" + languageObj.no_order_info +
				"</div>");
			$(".buffet-status-panel>.order-content").html("<div class='order-content-title item no-add text-center'>" +
				languageObj.no_order_info + "</div>");
		}
	}
}

function getSpecItmesHtml(arr, flag,styleFlag) { // 得到返回的规格里面的配菜html,arr是specList
	if (isEmpty(arr)) return '';
	// 如果index等于true则默认展开
	// let styleClass = styleFlag ? 'style="display:block"':'';
	// +-----------------------------------------
	// 三期默认展开规格
	// +-----------------------------------------
	let styleClass = 'style="display:block"';
	var html = '<div class="step-order-collapse steps-panel-box " '+styleClass+'>';
	var className = flag ? "" : 'style="height:1px;"';
	for (var i = 0; i < arr.length; i++) {
		var sp = arr[i];
		var price = flag ? (priceFormat(sp.menuPrice) + '円') : '';
		// +-----------------------
		// 三期修改 根据isShowPrice判断是否隐藏价格
		// +-----------------------
		price = getCache('isShowPrice') ? price : '';
		
		html += '<div class="steps-panel-list"><span class="item-title">' + sp.menuName +
			'</span><span class="item-count"></span><span class="item-price price-color" ></span></div>';//去掉价格
	}
	html += '</div>';
	return html;
}

function getStatusHtml(list) { // 如果是店铺页面扫入，得到含语言的数据，转换成Html(不含价格那些)
	var html = '';
	 
	let isMenu;
	// 三期判断
	if(list.length > 0 && list[0]['orderLineList'].length>0){
	    isMenu = list[0]['orderLineList'].filter(item=>parseInt(item.menuNum) > 1);
	}
	// 如果有menuNum值，说明是编号数据
	orderName = isEmpty(isMenu) ? languagePack.showlist :languagePack.food_ticket;
	// 下单完成页面追加项容标题
	html +=
		`<div class="order-content-title item">${orderName}</div><div class="order-content-items item" id="finish-add-items">`;
	
	for (var j in list) {
		var lang = list[j].languageTypeDict;
		var langClass = getLangClass(list[j].languageType * 1);
		var languageItem = `<p class='lang ${langClass}'><span>${lang}</span></p>`;
		var orderItem = "";
		var orderLineList = list[j].orderLineList;
		for (var index = 0; index < orderLineList.length; index++) {
			var menuPrice = priceFormat(orderLineList[index].menuPrice);
			var menuName = orderLineList[index].menuName;
			var buyNum = orderLineList[index].buyNum;
			
			
				if (!isEmpty(orderLineList[index].specList)) { // 拼接里面的规格附加菜品
				// <i></i>
					orderItem +=
						`<div class='order-content-items' id='step-order-content'><div class='order-item'><div class="order-item-list panel-collapse"><span class='item-title active'>${orderLineList[index].menuName}</span><span class='item-count'>${orderLineList[index].buyNum}</span><span class='item-price price-color'>${priceFormat(orderLineList[index].menuPrice)}円</span></div>`;
					orderItem += getSpecItmesHtml(orderLineList[index].specList, true,true) + '</div></div>';
				}else{
					// 区分是否加序号
					if(isEmpty(orderLineList[index].menuNum)){
						orderItem +=
							`<div class='order-item'><span class='item-title'>${menuName}</span><span class='item-count'>${buyNum}</span><span class='item-price price-color'>${menuPrice}円</span></div>`;
					}else{
						orderItem += `<div class='order-item'><span class='item-radio'>${orderLineList[index].menuNum}</span><span class='item-title item-title-radio'>${menuName} <br/>x ${buyNum}</span><span class='item-price price-color item-price-radio'>${menuPrice}円</span></div>`;
					}
				}
		}
		if (!isEmpty(orderItem)) {
			html += `<div class='order-content-items item'>${orderItem}</div>`;
		}

	}
	
	return html;
}

function getLangClass(num) { // 根据语言类型获得语言类名
	switch (num) {
		case 1:
			{
				return 'Japan';
			}
		case 2:
			{
				return 'China';
			}
		case 3:
			{
				return 'HongKong';
			}
		case 4:
			{
				return 'Korean';
			}
		case 5:
			{
				return 'UK';
			}
	}
}
// 处理订单详情页面的逻辑
function getOrderHtml(html, list, isMore) { // 扫桌号后，根据请求的数据，转换成Html(含价格) // 20190907
	if (isMore) {
		var name = languageObj.plus_over; //语言包
		html +=
			`<div class="order-content-title item">${name}</div><div class="order-content-items item" id="finish-add-items">`; // 下单完成页面追加项容标题
	} else {
		var name = languageObj.order_over; //语言包
		html +=
			`<div class="order-content-title item">${name}</div><div class="order-content-items item" id="finish-add-items">`; // 第一次下单标题
	}

	for (var j in list) {
		var menuPrice = priceFormat(list[j].singleSumPrice);
		// 二期修改 增加税的判断（1外带显示*号 or 0不外带店里不显示*号）
		let suffixString = list[j]['whetherTakeOut'] === '1'?'*':'';
		// +-----------------------
		// 三期修改 根据isShowPrice判断是否隐藏价格
		// +-----------------------
		var guigeSprice = getCache('isShowPrice') ? menuPrice +'円' : '';
		if (list[j].specList) { // 拼接里面的规格附加菜品
		// <i></i>
			html +=
				`<div class='order-content-items' id='step-order-content'><div class='order-item'><div class="order-item-list panel-collapse"><span class='item-title'>${list[j].menuName}${suffixString}</span><span class='item-count'>${list[j].buyNum}</span><span class='item-price price-color'>${guigeSprice}</span></div>`;
			html += getSpecItmesHtml(list[j].specList, true,false) + '</div></div>';
		} else if (list[j].whetherBuffet == '1') { // 拼接自助餐套餐 // 20190907
			html +=
				`<div class='order-item'><span class='item-title'>${list[j].menuName}${suffixString}</span><span class='item-count'>${list[j].buyNum}</span><span class='item-price price-color'>${guigeSprice}</span></div>`;
		} else { // 拼接普通菜品（包括规格菜和主页里菜）
			html +=
				`<div class='order-item'><span class='item-title'>${list[j].menuName}${suffixString}</span><span class='item-count'>${list[j].buyNum}</span><span class='item-price price-color'>${guigeSprice}</span></div>`;
		}
	}
	return html
}

function getGarnishHtml(buffetDataHtml, list, buffetCatalogName) { // 转换自助餐配菜html  // 20190907 还没用 只写出来，传入html 和 statusConfig.granishList就返回配菜html
	for (var j in list) {
		if (list[j].specList) { // 拼接里面的规格附加菜品
		// <i></i>
			buffetDataHtml +=
				`<div class='order-content-items' id='step-order-content'><div class='order-item'><div class="order-item-list panel-collapse"><span class='item-title'>${list[j].menuName}</span><span class='item-count'>${list[j].buyNum}</span><span class='item-price price-color'  style="height:1px;"></span></div>`;
			buffetDataHtml += getSpecItmesHtml(list[j].specList, false,false) + '</div></div>';
		} else {
			buffetDataHtml +=
				`<div class='order-item'><span class='item-title'>${list[j].menuName}</span></span><span class="item-count">${list[j].buyNum}</span><span class="item-price price-color" style="height:1px;"></div>`;
		}
	}
	return '<div class="order-content-title buffet-color item">' + buffetCatalogName + languageObj.ordered_dishes + '</div>' +
		buffetDataHtml;
}
//+=======================================================================

//+=======================================================================
//*追加结算付款面板模块
// 追加按钮，点击后返回首页，隐藏下单和下单完成界面
$('.addMore').on('click', function() {
	// clearCartList();
	vueFood.$set(vueFood, 'hasChoiceBuffet', false);
	if(getCache("displayType") == 2){
		vueFood.$set(vueFood,"toolbarData","buffet");
			requestShopBuffetMenu(languageType, getBuffetList);
		$(".toolbar>ul>li>.buffet").parent("li").click();
	}else{
		vueFood.$set(vueFood,"toolbarData","menu");
		initMenuCata(languageType);
		$(".toolbar>ul>li>.menu").parent("li").click();
	}
	hideFinish();
	hideOrder();
});

// 服务按钮点击显示呼叫服务面板 
$('.service').on('click', () => {
	$('.service-panel').fadeIn(300);
});

// 呼叫服务面板"否"按钮事件
$('#service-no').on('click', () => {
	// showToast('不呼叫服务');
});

// 呼叫服务面板"是"按钮事件
$('#service-yes').on('click', () => {
	// 发起请求
	ajaxRequest('callWaiter', {
			'accessToken': getAccessToken()
		}, '',
		function(res) {
			if (res.appCode == 1) {

			} else {
				//请求失败处理
			}
		},
		// 网络故障的处理
		function() {},
		false
	);
});

// 结算按钮点击是或否 后隐藏
$('.checkout-panel').on('click', function(e) {
	$(this).fadeOut(300);
});

// 结算按钮点击显示结算面板 
$('.checkout').on('click', () => {
	$('.checkout-panel').fadeIn(300);
});

// 结算面板"否"按钮事件
$('#checkout-no').on('click', () => {
	// showToast('不结算');
});

// 结算面板"是"按钮事件
$('#checkout-yes').on('click', () => {
	// 发起请求
	ajaxRequest('callTally', {
			'accessToken': getAccessToken()
		}, '',
		function(res) {
			if (res.appCode == 1) {

			} else {
				//请求失败处理
			}
		},
		// 网络故障的处理
		function() {},
		false
	);
});

function afterFinish() { // 下单完成后把数据保存到finishList,包括第一次买的和追加的，追加的如果已经存在就更新数量
	if (shopConfig.hasFinishCheckout) {
		copyOrUpdateCount(finishList.addMoreList, addMoreList)
	} else {
		copyOrUpdateCount(finishList.cartList, cartList)
	}
}

function copyOrUpdateCount(list, listCopy) { // 保存数据到finishList或更新保存数量的值
	if (!list) list = {};
	for (var key in listCopy) {
		if (list[key]) list[key].count += listCopy[key].count + 0;
		else list[key] = copy(listCopy)
	}
}

function copy(copyList) { //复制新对象
	var list = {};
	for (var key in copyList) {
		list[key] = Object.assign({}, copyList[key]);
	}
	return list
}

function setOrderStatus() { // 订单状态
	var list = finishList.cartList;
	var config = shopConfig;
	var html = '';
	if (shopConfig.hasFinishCheckout) {
		list = finishList.addMoreList;
		config = addMoreConfig;
	}
	var languageItem = `<p class='lang China'><span>${language.text}</span></p>`;
	for (var key in list) {
		var food = list[key];
		var orderItem =
			`<div class='order-item'><span class='item-title'>${food.menuName}</span><span class='item-count'>${food.count}</span><span class='item-price price-color'>${food.menuPrice}円</span></div>`;

		html += `<div class='order-content-items item'>${languageItem}${orderItem}</div>`;
	}
	$('#status-items').html(html);
}

function setStatusConfig(res) { // 得到请求后的订单内容数组 // 20190907
	var data = res.data;
	var dataList = res.dataList;
	statusConfig.data = data; // 订单完成页价格税金桌数之类的数据，可直接用返回的res.data
	statusConfig.statusList = []; // 订单完成第一次下单的对象
	statusConfig.moreList = []; // 订单完成追加下单的对象
	statusConfig.garnishList = res.buffetOrderList; // 订单配菜数组 // 20190907
	for (var key in dataList) {
		var food = dataList[key]; // datalist中的当前循环项
		var id = food.menuIndexId;
		var specIndexId = food.specIndexId;
		var buyNum = food.buyNum;
		var whetherAdd = food.whetherAdd;
		// 如果是规格
		if (specIndexId) {
			id = id + '_' + specIndexId; // 如果是规格，拼接ID设置唯一ID
		}
		//  如果是追加的
		if (whetherAdd === "1") {
			statusConfig.moreList.push(food); // 如果是追加状态，list为追加对象（statusConfig.statusList）
		}
		// else if(buffet==='1'){statusConfig.buffetList.push(food);} // 如果是自助餐，就加到自助餐
		else {
			statusConfig.statusList.push(food);
		}
	}
	// 将返回的自助餐名称加入，用于订单自助餐配餐标题展示
	statusConfig.data.buffetCatalogName = res.buffetCatalogName;
}

function clearAllList() {  // 充值语言清除当前购物车所有数据（包括配菜以及主菜单的菜）
	// 切换语言时清除所有数据
	var allArr = [ // 遍历内容
		{list:cartList, config:shopConfig},
		{list:addMoreList, config:addMoreConfig},
		{list:garnishList, config:buffetConfig},
		{list:buffetList, config:buffetConfig}
	];
	for(var i=0; i<allArr.length;i++){
		var list = allArr[i].list;
		var config = allArr[i].config;
		config.orderHtml = '';
		config.cartHtml = '';
		config.totalPrice = 0;
		config.totalTax = 0;
		if(!isEmpty(config.specDataList)) config.specDataList = [];
		resetFoodConfig(list); // 重置保存的数据（下方有说明）	
	}
	$('#cart-content').html(''); //清空购物车内容
	$('#cart-num').html(0); // 清空购物车总数量
	
	$('#cart-total').html(0); // 更新购物车总价
	
	// +-----------------------
	// 三期修改根据isShowPrice判断是否隐藏价格
	// +-----------------------
	isShowPrice();
	
	buffetConfig.garnishOrderHtml = ''; // 清空配菜订单信息，只有buffetConfig有
	buffetConfig.garnishCartHtml = ''; // 清空配菜购物车信息，只有buffetConfig有
	setNext(false,true); //更新 下一次按钮 的状态样式
	currentFood.count = 0; // 清空详情页数量
	resetPanelCount(currentFood); // 更新食品详情页内容	
	if (buffetConfig.hasChoiceBuffet || $("#tab-buffet").parent().attr("class") == "active") buffetConfig.garnishChoicedNum = 0;	
	$('#order-items').html("");	
	shopConfig.orderHtml = '';
	
	// +--------------
	// 三期修改清除已选配菜数量
	// +---------------------
	buffetConfig.garnishChoicedNum = 0;
	buffetConfig.specDataList = [];
}

function clearCartList() { 
	// 清除所有购物车数据，重置	
	var list = cartList;	
	var config = shopConfig;	
	if (vueFood.hasChoiceBuffet){	
		list = garnishList;		
		config = buffetConfig;	
	} else if (vueFood.toolbarData === 'buffet') {	
		list = buffetList;		
		config = buffetConfig;	
	} else if (shopConfig.hasFinishCheckout) {		
		list = addMoreList;		
		config = addMoreConfig;	
	}	
	resetFoodConfig(list); // 重置保存的数据（下方有说明）	
	setTotalPrice(list, config); //购物车底部的显示部分内容（包括数量和总价格方面）	
	resetPanelCount(currentFood); // 更新食品详情页内容	
	resetCartHtml(); //更新购物车内容	
	setNext(canOrder(),true); //更新 下一次按钮 的状态样式	
	// hideOrder();	
	if (buffetConfig.hasChoiceBuffet || $("#tab-buffet").parent().attr("class") == "active") buffetConfig.garnishChoicedNum = 0;	
	$('#order-items').html("");	
	shopConfig.orderHtml = '';
	buffetConfig.specDataList = [];
}
function resetFoodConfig(list) { // 重置当前购物状态下（追加或第一次买）保存的数据，和重置Vue主页右边食品项的数据	
		for (var key in list) {		
			var type = list[key].typeIndex;		
			var index = list[key].index;
			if ((vueFood.toolbarData === 'buffet'  || $("#tab-buffet").parent().attr("class") == "active") && vueFood.buffetList[index]){
				vueFood.$set(vueFood.buffetList[index], 'count', 0);
			}
			else if(vueFood.hasChoiceBuffet)
			 {
				vueFood.$set(vueFood.garnishList[type].menuDictList[index], 'count', 0); 
			 }
			else {
				// 三期修改切换重置失败
				if(vueFood.garnishList.length>0){
					if(!isEmpty(vueFood.garnishList[type]) && !isEmpty(vueFood.garnishList[type].menuDictList[index])){
						vueFood.$set(vueFood.garnishList[type].menuDictList[index], 'count', 0);
					}
				}
				if((type!==""&&type!==undefined) && vueFood.foodItems[type] && vueFood.foodItems[type].menuDictList[index])
				vueFood.$set(vueFood.foodItems[type].menuDictList[index], 'count', 0);
			}
			delete list[key];	
		}	
}
	//+=======================================================================
	function getIsShowPanel() { // 判断是否有购物车面板或者下单面板或者下单完成面板之中任意一个显示
		return shopConfig.isShowCartPanel || shopConfig.isShowFinish || shopConfig.isShownOrder
	}

	function setBodyOverflow() { // 购物车面板或者下单面板或者下单完成面板之中任意一个显示，设置body溢出隐藏，不允许滚动
		if (getIsShowPanel()) {
			$('body').css({'overflow': 'hidden'});
			$('.menu-right').css({"display":"none"});
		} else { // 否则去掉溢出隐藏，允许滚动（即在主页显示商品时）
			$('body').css({'overflow': ''});
			$('.menu-right').css({"display":"block"});
		}
	}

	//+=======================================================================
	//+------------------------------------
	//  主页菜单 自助餐 订单点击切换tab 3333333
	//+------------------------------------
	$('.toolbar li').on('click', function() {
		$('.toolbar li').removeClass('active');
		$(this).addClass('active');
		var toolbarData = $(this).find("span").data('name'); // 获取toolbar data属性的值（名字是menu,buffet,order）
		//设置切换的状态属性，用于切换时v-show的判断
		vueFood.$set(vueFood, 'toolbarData', toolbarData);
		
		setCache("toolbarData",toolbarData);
		if (toolbarData === 'buffet') { // 如果切换到buffet(自助餐)
			requestShopBuffetMenu(languageType, getBuffetList); // 每次点击toolbar里的buffet （自助餐）标题，就会重新请求
		} else if (toolbarData === 'order') { //20190905
			$('.footer').css({"display": "none"});
			setFinishPanel();
			vueFood.$set(vueFood,"buffetDataType",false);
		} else { // 否则就是主菜单和订单
			$('.footer').css({"display": "block"});
			vueFood.$set(vueFood,"buffetDataType",false);
		}
		resetCartHtml(); // 每次切换toolbar,就会更新购物车内容
		setPriceFun(); // 每次切换toolbar,就会更新购物车数量价格
		clearCartList(); //清空购物车
		setNext(canOrder(),true);// 设置下一次按钮的背景样式（canOrder()判断是否有商品）

		afterScrollFun(); // 界面滚动事件三期修改
		// +-----------------------
		// 三期修改根据isShowPrice判断是否隐藏价格
		// +-----------------------
		isShowPrice();
	});

	function showBuffetOrderPanel() { // 显示自助餐详情页面
		// if(shopConfig.hasFinishCheckout) $('#buffet-order-content').html(addMoreConfig.orderHtml);
		// else $('#buffet-order-content').html(shopConfig.orderHtml);
		var buffetName = $(".food-panel-title").text();
		$('#buffet-order-content').html('<p class="order-content-title">'+buffetName+'</p>');
		var selectHTml = '<select id="buffet-select">';
		for (var i = 1; i < 31; i++) {
			selectHTml += '<option value="' + i + '">' + i + '</option>';
		}
		selectHTml += '</select>';
		$('.buffet-order-panel .food-buy').html(selectHTml);
		$('#buffet-select').selectlist({
			zIndex: 10,
			width: 70,
			height: 30
		});
		$('.buffet-order-panel').fadeIn(); //
	}

	function hideBuffetOrderPanel() { // 隐藏自助餐详情页面
		$('.buffet-order-panel').fadeOut();
	}

	$('#buffet-yes').on('click', function() { // 自助餐确认下单事件(确认下单？ '是')
		buffetConfig.hasChoiceBuffet = true; // 自助餐下单确认
		hideElem($('.total-price')); // 隐藏购物车价格栏
		showElem($('.footer')); // 显示购物车那行（进入自助餐主页时会隐藏）
		
		vueFood.$set(vueFood, "toolbarData", 'menu'); // 更新vue工具条（toolbar）状态，变为选中主菜单状态
		vueFood.$set(vueFood, "hasChoiceBuffet", buffetConfig.hasChoiceBuffet); // 更改vue里保存的变量，和buffetConfig.hasChoiceBuffet同一意思，更改toolbar内容(变成toolbar grnish，即下单好自助餐后，选择配菜状态)
		resetCartHtml(); // 每次切换toolbar,就会更新购物车内容

		var bufferChoiced = getBuffetChoiced();
		clearCartList(); //陈骑元先获取到值在清空购物车
		if (!isEmpty(bufferChoiced)) {
			saveBufferOrder(bufferChoiced.catalogIndexId, bufferChoiced.buyNum, function(res) {
				buffetConfig.hasFinishBuffet = true; // 下单自助餐，用来判断可追加
				// 弹出跳转提示
				jumpToast(function() {
					hideFoodPanel(); // 隐藏自助餐详情面板
					$(".footer").show();
					queryBufferMenuList(languageType, getGarnishList); // 自助餐下单后请求当前配菜参数
				}, 3, languageObj.buy_order_ok, languageObj.jump_order_page);
			}); // 自助餐下单请求
		}
		// setPriceFun(); // 每次切换toolbar,就会更新购物车数量价格
		afterScrollFun();
	});
	
	function getBuffetChoiced() { // 更新选择自助餐后的可买配菜数量
		var garnishCount = 0;
		var catalogIndexId = '';
		var buyNum = parseInt($('.select-button').val());
			// garnishCount += food.count ;
			catalogIndexId = currentFood.catalogIndexId;
		return {
			"catalogIndexId": catalogIndexId,
			"buyNum": buyNum
			// "garnishCount": garnishCount
		}
	}

	function refreshBuyBuffet(food) { // 点击自助餐，如果这次自助餐和已选购的不一致，就清除已选购自助餐
		// var saveId = getCache('catalogIndexId');
		// if (saveId && saveId !== food.catalogIndexId) {
		// 	showToast('不能购买除已下单自助餐的额外套餐');
		// 	return true;
		// }
		for (var i in buffetList) {
			if (food.catalogIndexId !== buffetList[i].catalogIndexId) {
				clearCartList();
				return;
			}
		}
	}
	// +------------------------------
	// 处理自助餐列表返回的数据 （三期修改）**********************
	// +------------------------------
	function getBuffetList(res) { 
		menuInfo.buffetList = res.dataList; //菜品数据集合
		var whetherBuffet = parseInt(res.whetherBuffet);
		// +-----------------------------------------------------
		// 根据whetherBuffet判断是否已经下过单了，如果下过单1 没下过单0  （三期修改）
		// +----------------------------------------------------- 
		//如果为空，说明没下单
		
		if(isEmpty(whetherBuffet)){
			$("#whetherBuffet").val(whetherBuffet); //赋值进去判断是否下载个自助餐
			if(!isEmpty(buffetList)&&!isEmpty(menuInfo.buffetList)){ // 判断购物车里的有没自助餐，有的话就把加购数量更新到刚获取的自助餐菜品（menuInfo.buffetList）里
				for(var i in buffetList){
					var buf = buffetList[i];
					for(var j=0;j< menuInfo.buffetList.length;j++){
						if(buf.catalogIndexId === menuInfo.buffetList[i].catalogIndexId){
							menuInfo.buffetList[i].count = buf.count;
							break;
						}
						// 是否显示价格 增加参数 -----三期修改
						dataList[i]['menuDictList'][x]['isShowPrice'] = getCache("isShowPrice");
					}
				}
			}
			vueFood.$set(vueFood, "buffetList", menuInfo.buffetList);
			vueFood.$set(vueFood, "isShowPrice", getCache("isShowPrice"));
			$('.footer').css({"display": "none"});
		}else{
			// +------------------------
			// 三期修改  套餐下单后
			// +------------------------
			// 组装数据
			getGarnishList(res);
			// 显示menu tab
			vueFood.$set(vueFood, 'toolbarData', 'menu');
			// buffetDataType为true显示还能选多少  false不显示  （配合hasChoiceBuffet）
			vueFood.$set(vueFood,"buffetDataType",true);
			vueFood.$set(vueFood,"hasChoiceBuffet",false);
			// 显示底部购物车
			$('.footer').css({"display": "block"});
			// var bufferChoiced = getBuffetChoiced();// 更新选择自助餐后的可买配菜数量 11.27修改三期
		}
		
		
	}
	// +-------------------------------------------
	// 处理套餐菜单配菜信息 （三期修改）
	// +-------------------------------------------
	function getGarnishList(res) { // 获得自助餐配菜garnishList
		//给菜品加上区分普通菜品和自助餐菜品的标示
		var dataList = res.dataList;
		for (var i = 0; i < dataList.length; i++) {
			if(!isEmpty(dataList[i]['menuDictList'])){  //三期修改 加上判断menuDictList是否为空/存在
				for (var x = 0; x < dataList[i]['menuDictList'].length; x++) {
					dataList[i]['menuDictList'][x]['isSpec'] = true;
					// 如果是第一个
					if(i==0 && x==0){
						dataList[i]['menuDictList'][x]['zizhucan'] = true;
						dataList[i]['menuDictList'][x]['catalogType'] = dataList[i]['catalogType'];
					}else{
						dataList[i]['menuDictList'][x]['zizhucan'] = true;
						dataList[i]['menuDictList'][x]['catalogType'] = dataList[i]['catalogType'];
					}
					dataList[i]['menuDictList'][x]['iszizhucan'] = true;
					dataList[i]['menuDictList'][x]['iconAdd'] = 'img/add-l.png';
					dataList[i]['menuDictList'][x]['iconReduce'] = 'img/reduce-l.png';
					
					// +-----------------------
					// 三期修改 根据isShowPrice判断是否隐藏价格
					// +-----------------------
					dataList[i]['menuDictList'][x]['isShowPrice'] = getCache("isShowPrice")?true:false;
				}
			}
		}
		refreshGarnishList(garnishList, dataList);
		menuInfo.garnishList = dataList; //菜品数据集合
		// if(vueFood) {
		vueFood.$set(vueFood, 'garnishCount', res.data.totalChooseNum); // 设置自助餐可选配餐数量
		vueFood.$set(vueFood, "garnishList", dataList); // 设置自助餐可选配餐参数
		// }
	}

	function refreshGarnishList(list,dataList){ // 转换数量配置，因为每次进入配菜单，都会重新请求数据，所以重新请求后把购物车数量更新
		for(var i in list){
			for(var j in dataList){
				var menuDictList = dataList[j].menuDictList;
				if(!isEmpty(menuDictList)){
					for(var k=0;k<menuDictList.length;k++){
						if(list[i].menuIndexId == menuDictList[k].menuIndexId) {
							menuDictList[k].typeIndex = list[i].typeIndex;
							menuDictList[k].index = list[i].index;
							menuDictList[k].count = list[i].count;
							if(parseInt(i) == Object.keys(list).length-1) return;
							break;
						}
					}
				} 	
			}
		}
	}
	// 自助餐配菜返回按钮事件
	$('.garnish-back').on('click', function() {
		vueFood.$set(vueFood, "hasChoiceBuffet", false);
		vueFood.$set(vueFood, "toolbarData", 'buffet');
		showFoodPanel(currentFood);
		$('.toolbar li').removeClass('active');
		$("#tab-buffet").click();
		// $('.toolbar li span.buffet').addClass('active');
		
		clearCartList();
		// resetCartHtml();
		// setTotalPrice(cartList,shopConfig);
		// setNext(canOrder());
		// vueFood.hasChoiceBuffet = false;
	});
	//----------------Home按钮事件-----------------
	$('.home-button').on('click', function() {
		hidePanel(".step-sure-panel"); // 点击后如果是配菜下单页面，就隐藏
		hideFoodPanel(); // 同样的如果是详情面板也隐藏
		hideFinish();
	});
	// 追加菜品按钮事件（和底部追加按钮不是同一个）
	$("#finish-content").on("click", ".add-cata-btn", function() {
		hideFinish();
		hideOrder();
		// vueFood.$set(vueFood,"hasChoiceBuffet",true); 
		//  vueFood.$set(vueFood,"toolbarData","menu"); 
		//   showElem($('.footer'));
	});
	// 自助餐旁边订单追加按钮
	$(".buffet-status-panel").on("click", ".add-cata-btn", function() {
		vueFood.$set(vueFood,"hasChoiceBuffet",true);
		if(getCache("displayType") == 2){
			vueFood.$set(vueFood,"toolbarData","buffet");
			// if(!vueFood.garnishList.length){
				requestShopBuffetMenu(languageType, getBuffetList);
			// }
			$(".toolbar>ul>li>.buffet").parent("li").click();
		}else{
			vueFood.$set(vueFood,"toolbarData","menu");
			// if(!vueFood.garnishList.length){
				initMenuCata(languageType)
			// }
			$(".toolbar>ul>li>.menu").parent("li").click();
		}
		
		//setTotalPrice(garnishList, buffetConfig);
		showElem($('.footer'));
	});

	function showHomeButton() { // 显示那个浮出的首页按钮
		$('.home-button').css({
			"display": "block"
		});
	}

	function hideHomeButton() { // 隐藏那个浮出的首页按钮
		$('.home-button').css({
			"display": "none"
		});
	}
	//---------------------------------------------

	//----------------------------步骤panel-----------------一---------
	// 步骤进度值
	var $sp_steps = $(".steps-index"),
		// 步骤列表内容区域
		$sp_stepsList = $(".steps-box"),
		// 步骤列表套餐配菜
		$sp_stepsItems = $("#steps-list-items"),
		// 订单按钮区域
		$sp_orderBox = $("#sp-steps-btn-box"),
		// 修改订单按钮
		$sp_updateOrder = $("#sp-update-order"),
		// 下一步按钮
		$sp_next = $("#sp-next-btn"),
		//剩余配菜待选数量
		$surplusNum = $(".surplus-num");
	// 点击规格弹出步骤panel页面
	function stepPage(food) {
		if (buffetConfig.garnishChoicedNum >= vueFood.garnishCount) { // 自助餐可选配菜数量大于可选的最大值
			showToast(languageObj.buffet_over_full);
			return;
		}
		requestMenuStepSpecList(getCache('languageType'), food.menuIndexId, function(res) {
			// 设置规格panel数据
			setStepSpec(res.dataList, food);
			// 调用common-dom.js方法
			showPanel(".step-panel");
		});
	};
	$sp_stepsItems.on('click','label',function(){		
		$(this).find("input").prop("checked",true);	
	});
	// ---s1----
	$stepsInfoBox = $("#steps-check-info-box");
	// 设置规格panel数据
	function setStepSpec(data, food) {
		currentSpec = Object.assign({}, food); // 保存当前显示的菜品规格信息
		var stepHtml = "", // 步骤列表html 
			stepSpecHtml = "", //步骤规格html
			stepTotal = data.length, //步骤总数
			stepsInfoHtml = "";
		for (var i = 0; i < data.length; i++) {
			var stepData = data[i];
			// 区分是单选还是多选
			var type = stepData.chooseType === "1" ? 'radio' : 'checkbox';
			var className = i == 0 ? 'active' : '';
			// 组装步骤
			stepHtml += '<span class="step ' + className + '">' + stepData.stepName + '</span>';
			let specNum = 0;
			// 如果类型是1 说明是单选框
			if (stepData.chooseType == 1) {
				specNum = 1;
			} else {
				// 如是返回值是0 那么计算总数，否则直接赋值
				specNum = stepData.maxChoose == 0 ? stepData.stepSpecList.length : stepData.maxChoose;
			}
			let stepInfoClassName = i == 0 ? 'active' : '';
			var order_choose = languageObj.order_choose.split("0");
			stepsInfoHtml += '<p class="steps-check-info ' + stepInfoClassName + '"><img src="img/what.svg" style="width: 22px;margin-right: 5px;">' + order_choose[0] +
				'<span class="surplus-num">' + specNum +
				'</span>' + order_choose[1] + '</p>';
			// 组装步骤规格
			stepSpecHtml += getStepSpecData(stepData.stepSpecList, className, type, i);
		}
		// 更新步骤进度数
		$sp_steps.text(1);
		// 步骤列表
		$(".steps-list-box").html(stepHtml);
		// 自助餐配菜可以选择的数量值内容区域----s1-------
		$stepsInfoBox.html(stepsInfoHtml);
		// ----e1-------
		// 步骤规格
		$("#steps-list-items").html(stepSpecHtml);
		// 步骤总数
		$(".steps-total").text(stepTotal);
	}
	// 组装步骤规格数据
	function getStepSpecData(stepSpecData, className, type, index) {
		var labelHtml = '';
		for (var i = 0; i < stepSpecData.length; i++) {
			var specData = stepSpecData[i];
			// +-----------------------
			// 三期修改 根据isShowPrice判断是否隐藏价格
			// +-----------------------
			if(getCache('toolbarData') == "buffet"){
				var stepPrice = "";
				
			}else{
				var stepPrice = getCache("isShowPrice")?(specData.specPrice)+'円':'';
			}
			labelHtml += '<label class="yzk-checkbox checkbox-dc">' +
				'<input type="' + type + '" ' +
				'name="ids-' + index + '"' +
				'data-specindexid="' + specData.specIndexId + '"' +
				'data-name="' + specData.specName + '"' +
				'data-price="' + specData.specPrice + '"' +
				'value="' + specData.specId + '">' +
				'<span>' + specData.specName + '</span>' +
				'<p>' + stepPrice + '</p>' +
				'</label>';
		}
		return '<div class="' + className + ' p10">' + labelHtml + '</div>';
	}
	

	// 点击X隐藏(点击页面其他区域不隐藏)
	$('.yzk-panel .yzk-close-btn').click((e) => {
		hideHomeButton();
		// 初始套餐配菜
		$('.yzk-panel').fadeOut(300);

		setTimeout(function() {
			// 初始套餐类型列表样式
			$sp_next.data("info", false);
			// 更新步骤进度数
			$sp_steps.text(1);
			$sp_stepsItems.find("div:eq(0)").addClass("active").siblings("div").removeClass("active");
			$sp_stepsList.find(".steps-list-box>span:gt(0)").removeClass("active");
			// 同步购物车的数量
			buffetConfig.garnishChoicedNum = parseInt($("#cart-num").text());
		}, 300);
		// 关闭的时候将可选数量同步为购物车数量
	});

	
	//修改订单
	$sp_updateOrder.click(function() {
		// 更新步骤完成标识（默认没有完成）
		$sp_next.data("info", false);
		// 更新步骤进度数
		$sp_steps.text(1);
		// 初始套餐类型列表样式
		$sp_stepsList.find(".steps-list-box>span:gt(0)").removeClass("active");
		// 初始套餐配菜
		$sp_stepsItems.find("div:eq(0)").addClass("active").siblings("div").removeClass("active");
		// 隐藏步骤订单页面(调用common-dom.js方法)
		hidePanel(".step-sure-panel");
		// 显示步骤页面(调用common-dom.js方法)
		showPanel(".step-panel");
		$sp_next.data("info",'');
		$(".steps-box-buy>.cart-count").text(1);
	});
	// 添加到购物车
	$("#sp-buy").click(function() {
		// 更新购物车数据(区分菜品购物车和自助餐购物车)
		currentSpec.lineId = getUuid();
		var specList = getStepsItems();
		// 规格数量
		var specMum = $(".steps-box-buy>.cart-count").text();
		buffetConfig.specDataList = [];  //每次不清空累计，在清空按钮时清空
		var specName = [];//规格菜品名字集合
		for (var i = 0; i < specList.length; i++) {
			var spec = {};//规格菜品对象
			spec.menuIndexId = currentSpec.menuIndexId;
			spec.specIndexId = specList[i].specIndexId;
			spec.parentId = currentSpec.lineId;
			spec.buyNum = specList[i].specMum;
			if(getCache('toolbarData') == "buffet"){
				spec.catalogIndexId = getCache('catalogIndexId');
			}
		    // 三期修改区分开来数量，根据规格数量来同步
		    // spec.buyNum = specMum;
			
			// if (vueFood.hasChoiceBuffet) spec.catalogIndexId = getCache('catalogIndexId');
			buffetConfig.specDataList.push(spec);
			// +-----------------
			// |规格菜名  @1212 1
			// +-----------------
			specName.push(specList[i].specName);
			
		}
		//  if(vueFood.hasChoiceBuffet){
		currentSpec.menuPrice = $(".steps-price-box .item-title>.item-title-price").data("price");
		currentSpec.count = parseInt(specMum)-1;
		// +-----------------
		// |将规格菜品挂载到规格下  @1212  3
		// +-----------------
		currentSpec.buffetListData = buffetConfig.specDataList;
		// +-----------------
		// |将规格菜名挂载到规格下  @1212  4
		// +-----------------
		currentSpec.buffetListName = specName.join(",");
		addFoodToCart(currentSpec);
		//  }
		$sp_next.data("info",'');
		$stepSpecNum.text(1);
		hidePanel(".step-sure-panel"); // 隐藏步骤订单页面(调用common-dom.js方法)
	});

	// 下一步
	$sp_next.click(function() {
		// 获取步骤值
		let index = $sp_steps.text();
		// 获取所有选中的套餐配菜信息
		let checkedData = getStepsItems();
		// 步骤总数值
		let $sp_stepsTotal = $(".steps-total").text();
		// 获取当前步骤可选规格数量---s3
		var checkedNum = $stepsInfoBox.find("p.active>.surplus-num").text();
		//小雨规定的值
		if(getStepsCheckedNum()<1){
			showToast(languageObj.least_one);
			return false;
		}
		// 超过固定值提示
		if (getStepsCheckedNum() > parseInt(checkedNum)) {
			showToast(languageObj.choose_over);
			return false;
		}
		if (!isEmpty($sp_next.data("info")) || $sp_stepsTotal == 1) {
			
			// 跳转到配菜订单页面
			jumpOrderPage();
		}
		// ----e3--
		if (!isEmpty($sp_next.data("info"))) {
			// 跳转到配菜订单页面
			jumpOrderPage();
		}
		// 更新套餐类型列表样式
		$sp_stepsList.find(".steps-list-box>span:eq(" + index + ")").addClass("active");
		// 更新套餐配菜
		$sp_stepsItems.find("div:eq(" + index + ")").addClass("active").siblings("div").removeClass("active");
		//切换可选菜品数量列表显示-----s3
		$stepsInfoBox.find("p:eq(" + index + ")").addClass("active").siblings("p").removeClass("active");
		// ----e3--
		// 更新步骤进度数
		$sp_steps.text(++index);
		// 如果当前的步骤进度数和进度总数相等
		if (index == $sp_stepsTotal) {
			$sp_next.data("info", true);
		}
	});
	//获取当前步骤选中的数据数量----s3
	function getStepsCheckedNum() {
		let index = 0;
		$sp_stepsItems.find("div.active input").each(function(i) {
			let _this = $(this);
			if (_this.is(':checked')) {
				index++;
			}
		});
		return parseInt(index);
	}
	// 跳转到菜品规格订单信息页面
	function jumpOrderPage() {
		// 设置选中的步骤规格数据信息
		setStepSpecOrderPanelInfo();
		// 显示步骤订单页面(调用common-dom.js方法)
		showPanel(".step-sure-panel");
		// 隐藏步骤页面(调用common-dom.js方法)
		hidePanel(".step-panel");
		// 显示首页按钮
	}
	// 设置选中的步骤规格数据信息
	function setStepSpecOrderPanelInfo() {
		//获取所有选中的步骤规格信息
		var data = getStepsItems();
		// +-----------------------
		// 三期修改 根据isShowPrice判断是否隐藏价格
		// +-----------------------
		var setStepPrice = getCache("isShowPrice")?priceFormat(currentSpec.menuPrice)+'円':'';
		// 如果是自助餐
		if(getCache('toolbarData') == 'buffet'){
			setStepPrice = '';
		}
		//规格菜品html 
		var menuStepInfo = '<span class="item-title">' + currentSpec.menuName + '</span>' +
			'<span class="item-count">1</span>' +
			'<span class="item-price price-color">' + setStepPrice + '</span>',
			stepListHtml = "", // 菜品规格列表html
			specTotal = currentSpec.menuPrice; //菜品规格小计
		for (var i = 0; i < data.length; i++) {
			data[i].parentId = currentSpec.lineId;
			var specData = data[i];
			// +-----------------------
			// 三期修改 根据isShowPrice判断是否隐藏价格
			// +-----------------------
			var setForStepPrice = getCache("isShowPrice")?priceFormat(specData.specPrice)+'円':'';
			// 如果是自助餐
			if(getCache('toolbarData') == 'buffet'){
				setForStepPrice = '';
			}
			stepListHtml += '<div class="steps-panel-list">' +
				'<span class="item-title">' + specData.specName + '</span>' +
				'<span class="item-price price-color">' + setForStepPrice + '</span>' +
				'</div>';
			specTotal += parseInt(specData.specPrice);
		}
		// 如果是自助餐
		if(getCache('toolbarData') == 'buffet'){
			specTotal = '';
		}
		// 规格菜品
		$("#steps-sure-items>.yzk-content-item").html(menuStepInfo);
		// 菜品规格列表
		$("#steps-panel-box").html(stepListHtml);
		// ----s4
		// if (isEmpty(stepListHtml)) {
		// 	$("#steps-panel-box").css({
		// 		"borderBottom": "none"
		// 	});
		// } else {
		// 	$("#steps-panel-box").css({
		// 		"borderBottom": "1px solid #cecece"
		// 	});
		// }
		// ----s4-----
		// +-----------------------
		// 三期修改 根据isShowPrice判断是否隐藏价格
		// +-----------------------
		//getCache("isShowPrice") 
		if(getCache('toolbarData') == "buffet"){
			$('.steps-price-box .item-title').css({"visibility":"hidden"});
		}else{
			$('.steps-price-box .item-title').css({"visibility":"inherit"});
		}
		
		// 步骤总数
		$(".steps-price-box .item-title>.item-title-price").text(priceFormat(specTotal)).data("price", specTotal);
	}

	//获取所有选中的步骤规格信息
	function getStepsItems() {
		let garnishArr = [];
		$sp_stepsItems.find("input").each(function(i) {
			let _this = $(this);
			if (_this.is(':checked')) {
				let obj = {
					id: _this.val(),
					specIndexId: _this.data('specindexid'),
					specName: _this.data('name'),
					specPrice: _this.data('price'),
					specMum: 1
				};
				garnishArr.push(obj);
			}
		});
		return garnishArr;
	}
	var $stepSpecNum = $(".steps-box-buy>.cart-count");
	// 增加菜品规格数量
	$(".steps-box-buy>.cart-add").click(function() {
		// 增加数量
		buffetConfig.garnishChoicedNum += 1;
		if (getCache('toolbarData') == 'buffet' && buffetConfig.garnishChoicedNum >= vueFood.garnishCount) { // 自助餐可选配菜数量大于可选的最大值
			showToast(languageObj.buffet_over_full);
			return;
		}
		var num = $stepSpecNum.text();
		var $price = $(".steps-price-box .item-title>.item-title-price");
		var price = $price.data("price");
		// 更新数量
		$stepSpecNum.text(++num);
		// 更新价格
		// 更新价格
		if(getCache('toolbarData') == 'buffet'){
			$(".steps-price-box>.steps-panel-list>.item-title").css("visibility","hidden");
		}else{
			$(".steps-price-box>.steps-panel-list>.item-title").css("visibility","inherit");
			$price.text(priceFormat(num * parseInt(price)));
			
		}
		
	});
	// 减少菜品规格数量
	$(".steps-box-buy>.cart-reduce").click(function() {
		// 减少数量
		buffetConfig.garnishChoicedNum -=1;
		var num = $stepSpecNum.text();
		if (num == 1) return false;
		var $price = $(".steps-price-box .item-title>.item-title-price");
		var price = $price.data("price");
		// 更新数量
		$stepSpecNum.text(--num);
		// 更新价格
		$price.text(priceFormat(num * parseInt(price)));
	});
	
	// +-------------------------------------
	// 三期注释点击事件
	// +-------------------------------------
	// 规格点击下拉事件 20190905
	/*$("#finish-content,.buffet-status-panel").on("click", ".order-item>.order-item-list", function() {
		var $_this = $(this);
		var $icon = $_this.find(".item-title>i");
		var className = $icon.attr("class");
		if (className == "active") {
			$icon.removeClass("active");
		} else {
			$icon.addClass("active");
		}
		$_this.next().slideToggle();
	});*/
	// 自助餐菜单跳转到自助餐选择菜品---三期取消次功能
	$(".add-buffet-dishes").click(function() {	
		hideFoodPanel(); // 隐藏自助餐详情面板
		vueFood.$set(vueFood,"hasChoiceBuffet",true);		
		vueFood.$set(vueFood,"toolbarData","menu"); 
		// // 如果已下单 
		// if(!vueFood.garnishList.length){	
			
			// 请求自助餐配菜数据
			queryBufferMenuList(languageType,getGarnishList);		
		// }		
		setTotalPrice(garnishList, buffetConfig);		
		showElem($('.footer'));	
		setNext(canOrder(),true);
	});
	
	// ==========================注册，登录，积分====================================
	// 隐藏panel
	function hideDLPanel(elem){
		$(elem).css({
			"transform": "translate3d(100%,0,0)",
			"opacity": 0
		});
		// 恢复样式
		$("body").css({"position":"static"});
	}
	// 显示panel
	function showDLPanel(elem){
		$(elem).css({
			"transform": "translate3d(0,0,0)",
			"opacity": 1
		});
		// 处理滚动穿透的问题
		$("body").css({"position":"fixed"});
	}
	// 隐藏panel
	$(".dl-box").click(function(){
	    $(this).parents(".dl-panel").css({
			"transform": "translate3d(100%,0,0)",
			"opacity": 0
		});
		// 恢复样式
		$("body").css({"position":"static"});
	});
	
	// +-------------------------------
	// |去登录
	// +-------------------------------
	$("#to-login").click(function(){
		let loginInfo = getCache("loginInfo");
		if(isEmpty(loginInfo)){
			// 显示登录页面
			showDLPanel(".login-panel");
		}else{
			// 发起积分页面请求
			requestScore(loginInfo.token);
		}
	});
	
	// +-------------------------------
	// | 注册页面
	// +-------------------------------
	// -----插件start---------
	function getYMD(flag){
		let date = new Date();
		let y = date.getFullYear();
		if(isEmpty(flag)){
			y = parseInt(y) - 50;
		}
		let m = (date.getMonth()+1).toString().padStart(2,'0');
		let d = date.getDate().toString().padStart(2,'0');
		return y+"/"+m+"/"+d;
	}
	var selectDate = new MobileSelectDate();
	selectDate.init({trigger:'#USER_AGE',min:getYMD(false),max:getYMD(true),position:"bottom"});
	
	// -----时间插件结束------------------
	
	let $codeVal = $("#code-btn");
	// 点击获取验证码
	$("#code-btn").click(function(){
		let _this = this;
		let phone = $("input[name='phone']").val();
		if(isEmpty(phone)){
			showToast("電話番号は空いてはいけません。");
			return false;
		}
		$(".cu-load").css("visibility","inherit");
		// 发起获取验证码请求
		$.ajax({
			type:"post",
			url:projectName + "point/sendAuthCode",
			data:{mobile:phone},
			async:false,
			beforeSend:function(){
				$(_this).prop("disabled",true);//禁用按钮
				let index = 60;
				let sendCodeTime = setInterval(function() {
					--index;
					if(index == 0 ){
						$codeVal.text("検証");
						clearInterval(sendCodeTime);
						$(_this).prop("disabled",false);//启用按钮
					}else{
						$codeVal.text(index+"s");
					}
				}, 1000);
			},
			success: function(data) {
				$(".cu-load").css("visibility","hidden");
				showToast("認証コードはすでに発行されました。ご確認ください!");
			},
			error:function(){
				
			},
			contentType:'application/x-www-form-urlencoded'
		});
	});
	// 发起注册请求
	$("#first-register").click(function(){
		let xm = $("input[name='xm']").val();
		let csrq = $("input[name='csrq']").val();
		let email = $("input[name='email']").val();
		let phone = $("input[name='phone']").val();
		let code = $("input[name='code']").val();
		let pwd = $("input[name='pwd']").val();
		console.log(csrq);
		if(isEmpty(xm)){
			showToast("名前は空ではいけません");
			return false;
		}
		if(isEmpty(csrq)){
			showToast("生年月日は空ではありません。");
			return false;
		}
		if(isEmpty(email)){
			showToast("メールボックスは空にできません");
			return false;
		}
		if(isEmpty(phone)){
			showToast("電話番号は空いてはいけません");
			return false;
		}
		if(isEmpty(code)){
			showToast("認証コードが空ではありません。");
			return false;
		}
		if(isEmpty(pwd)){
			showToast("パスワードが空です");
			return false;
		}
		if(pwd.length<6 || pwd.length>15){
			showToast("パスワードの長さは6桁から15桁の間です。");
			return false;
		}
		let param = {
			username:xm,
			birthDate:csrq,
			email:email,
			mobile:phone,
			authCode:code,
			password:pwd
		}
		$.ajax({
			type:"post",
			url:projectName + "point/enroll",
			data:param,
			beforeSend:function () {
				$(".cu-load").css("visibility","inherit");
			},
			success: function(data) {
				if(data.appCode == 1){
					showToast("登録成功");
					//注册成功后返回登录页面
					hideDLPanel(".register-panel");
				}
				
			},
			complete: function() {
				$(".cu-load").css("visibility","hidden");
			},
			error:function(){},
			contentType:'application/x-www-form-urlencoded'
		});
		
	});
	
	// +-------------------------------
	// | 登录页面
	// +-------------------------------
	// 登录请求
	$("#login-box").click(function(){
		let checked = $(".min-box>input").is(':checked');
		let account = $("input[name='account']").val();
		let password = $("input[name='loginPwd']").val();
		if(isEmpty(account)){
			showToast("アカウントが空です");
			return false;
		}
		if(isEmpty(password)){
			showToast("パスワードが空です");
			return false;
		}
		// 发起登录请求
		$.ajax({
			type:"post",
			url:projectName + "point/login",
			data:{account:account,password:password},
			beforeSend:function () {
				$(".cu-load").css("visibility","inherit");
			},
			success: function(data) {
				if(data.appCode == 1){
					showToast("ログイン成功");
					//隐藏登录页面
					hideDLPanel(".login-panel");
					// 如果是保持登录
					if(checked){
						// 保存一周
						setCache("loginInfo",data.data,168);
					}else{
						// 保存2个小时
						setCache("loginInfo",data.data,2);
					}
					//修改名称
					$("#to-login").text("入ります");
					$(".score-box>.to-title").text("ポイントを表示");
					
				}else{
					showToast(data.appMsg);
				}
				
			},
			complete: function() {
				$(".cu-load").css("visibility","hidden");
			},
			error:function(){},
			contentType:'application/x-www-form-urlencoded'
		});
		// 成功后跳转
	});
	// 跳转到注册页面
	$("#register-box").click(function(){
		showDLPanel(".register-panel");
	});
	// +-------------------------------
	// | 积分页面
	// +-------------------------------
	$("#jifen-box").click(function(){
		let loginInfo = getCache("loginInfo");
		if(isEmpty(loginInfo)){
			// 显示登录页面
			showDLPanel(".login-panel");
		}else{
			// 发起积分页面请求
			requestScore(loginInfo.token);
		}
		
	});
	// 请求积分
	function requestScore(token){
		$(".cu-load").css("visibility","inherit");
		// 设置二维码
		$("#auth-code>img").attr("src",projectName + "point/showQrcodeImage?token="+token);
		// 获取总金额
		$.ajax({
			type:"post",
			url:projectName + "point/getSummary",
			data:{token:token},
			success: function(data) {
				if(data.appCode == 1){
					$(".card-price>p").text(data.data+"P");
				}
				getScoreList(token);
			},
			error:function(){},
			contentType:'application/x-www-form-urlencoded'
		});
	}
	// 获取积分明细
	function getScoreList(token){
		$.ajax({
			type:"post",
			url:projectName + "point/listPoint ",
			data:{currentPage:1,pageSize:60,token:token},
			success: function(data) {
				if(data.appCode == 1){
					let scoreHtml = "";
					data.dataList.forEach((item)=>{
						let point = item.point;
						if(item.point.indexOf("-") < 0){
							point = "+" + item.point;
						}
						scoreHtml += '<div class="jifen-list"><div class="date-name">'+
										'<p class="jifen-name-title">'+item.outletName+'</p>'+
										'<p class="jifen-name-date">'+item.transDate+'</p>'+
									'</div>'+
									'<p>'+point+'</p></div>'
					});
					$(".jifen-panel .order-content").html(scoreHtml);
					// 用户名
					$("#card-title").text(getCache("loginInfo").username);
					showDLPanel(".jifen-panel");
				}else{
					$(".jifen-panel .order-content").html('<div class="none-list">无积分</div>');
				}
			},
			complete:function(){
				$(".cu-load").css("visibility","hidden");
			},
			error:function(){},
			contentType:'application/x-www-form-urlencoded'
		});
	}