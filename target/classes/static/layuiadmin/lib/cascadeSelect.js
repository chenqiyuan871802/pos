/***
 * 无限极联动选择组件封装模块
 * 
 * @selectValue="310000"  默认选中值
 * @promtion="请选择地区"   默认说明
 * @nextElemUrlPath="znxq/api/getXZQ"  下一级加载数据的地址
 * @child="CityId" 下一级select标签位置
 * @typeCode="HJSHENG"  地址类型
 */
layui.extend({
	webplus: 'lib/webplus'
}).define(['jquery', 'form', 'table', 'webplus'], function(exports) {
	var $ = layui.$,
		form = layui.form,
		webplus = layui.webplus,
		table = layui.table;
	// 监听选中事件
	form.on('select', function(data) {
		let selectValue = data.value;
		let obj = data.elem.name;
		let _this = $("select[name='" + obj + "']");
		// 获取当前标签配置的下一个关联标签的属性值
		let nextElem = _this.attr("nextElem");
		// 当前操作标签上配置的下一个标签请求数据地址
		let nextElemUrl = $("select[name='" + obj + "']").attr("nextElemUrlPath");
		if (nextElemUrl) {
			if(nextElemUrl.indexOf("|") > -1){
				let arrElemUrl = nextElemUrl.split("|");
				let arrElem = nextElem.split("|");
				for(let i=0;i<arrElemUrl.length; i++){
					CascadeSelect.GetListByParent(arrElem[i], selectValue, CascadeSelect.getHttp(arrElemUrl[i]), true);
				}
			}else{
				CascadeSelect.GetListByParent(nextElem, selectValue, CascadeSelect.getHttp(nextElemUrl), true);
			}
		}
	});

	var CascadeSelect = {
		token: "",
		// port1: ":8080/hpyh/", //端口
		alertElem: "address-table-modal",
		searchValue: "",
		suffixHttp: window.location.protocol + "//" + window.location.host,
		getHttp: function(nextElemUrl) {
			// return CascadeSelect.suffixHttp + CascadeSelect.port1 + nextElemUrl;
			return webplus.cxt() + nextElemUrl;
		},
		init: function(initElem, isReset, urlParam) {
			CascadeSelect.token = webplus.getToken();
			
			var typeCode = $("select[name='" + initElem + "']").attr("typeCode");
			webplus.showLoading();
			// ajax请求
			$.ajax({
				type: "POST",
				url: webplus.cxt()+ urlParam,
				dataType: "json",
				data: {
					"token": CascadeSelect.token
				},
				success: function(result) {
					let data = result.dataList;
					// result.itemCode = "440112001";
					let $select = $("select[name='" + initElem + "']");
					$select.prop("disabled", false);
					$.each(data, function(i, d) {
						// 如果有固定参数或者只有一条数据  锁定
						if(result.itemCode == d.itemCode || data.length == 1){
							$select.append("<option selected value='" + d.itemCode + "'>" + d.itemName + "</option>");
							// $select.prop("disabled", true);
						}else{
							$select.append("<option value='" + d.itemCode + "'>" + d.itemName + "</option>");
						}
					});
					// 获取配置的默认选中code
					// var selectValue = $("select[name='" + initElem + "']").attr("selectValue");
					// 获取下一级select的name值
					var nextElem = $("select[name='" + initElem + "']").attr("nextElem");
					// 选中默认值
					// $("select[name='" + initElem + "']").val(selectValue);
					// 获取下一级的url请求地址
					var nextElemUrl = $("select[name='" + initElem + "']").attr("nextElemUrlPath");
					// 如果有配置了下一级url
					if (result.itemCode && nextElemUrl) {
						if(nextElemUrl.indexOf("|") > -1){
							let arrElemUrl = nextElemUrl.split("|");
							let arrElem = nextElem.split("|");
							for(let i=0;i<arrElemUrl.length; i++){
								CascadeSelect.GetListByParent(arrElem[i], result.itemCode, CascadeSelect.getHttp(arrElemUrl[i]), true);
							}
						}else{
							CascadeSelect.GetListByParent(nextElem, result.itemCode, CascadeSelect.getHttp(nextElemUrl), true);
						}
					}else{
						form.render();
					}
					webplus.hideLoading();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					webplus.alertError('系统异常导致操作失败, 请联系管理员。');
				},
				statusCode: {
					404: function() {
						webplus.alertError('未找到指定请求，请检查访问路径！');
					},
					500: function() {
						webplus.alertError('系统错误，请联系管理员。');
					}
				},
				complete: function(XMLHttpRequest, textStatus) {
					webplus.hideLoading();
				}
			});
			
			CascadeSelect.initDroptable();
		},
		initDroptable:function(){
			$("body").append(CascadeSelect.initAddressHtml());
			
			// 输入框焦点监听
			let _this;
			$(".focus-input").on("click", function() {
				_this = $(this);
				console.log(_this,'this')
				// 清空搜索文字
				CascadeSelect.searchValue = "";
				// 清空搜索输入框内容
				$("#tableSelect_btn_search").prev("input").val("");
				// 计算位置
				CascadeSelect.updatePosition(_this);
				// 发起请求
				CascadeSelect.dropTableData(_this);
				// 显示
				CascadeSelect.showAlertElem();
				// 
				event.stopPropagation();
			});
			// 监听视窗改变
			window.addEventListener('resize', function() {
				CascadeSelect.updatePosition(_this);
			}, false);
			
			// 点击其他区域，隐藏弹出层
			$(document).on("click", function() {
				CascadeSelect.hideAlertElem();
			});
			// 给弹出层绑定点击事件，阻止冒泡
			$("#" + CascadeSelect.alertElem).on("click", function() {
				event.stopPropagation();
			});
			// 搜索事件
			$("#tableSelect_btn_search").click(function(){
				let searchName = $(this).prev("input").val();
				// 获取搜索值
				CascadeSelect.searchValue = searchName;
				// 发起请求
				CascadeSelect.dropTableData(_this);
				event.stopPropagation();
			});
			// 表格确定事件
			$("#tableSelect_btn_select").click(function() {
				let data = webplus.getCheckedTableData("addressTable");
				if (data.length < 1) {
					webplus.alertError('请选择要添加的信息');
					return false;
				}
				CascadeSelect.setData(_this, data[0]);
				event.stopPropagation();
			});
			// 表格重置事件
			$("#tableSelect_btn_reset").click(function() {
				_this.val("");
				_this.next().val("");
				let [itemName, itemCode] = _this.data("field").split("|");
				let data = {};
				data[itemName] = "";
				data[itemName] = "";
				CascadeSelect.setData(_this, data);
				event.stopPropagation();
			});
		},
		initAddressHtml:function(){
			return `<div id="address-table-modal" class="p-fixed modal-box left-0 trs2 d-none" style="background-color: rgba(0,0,0,.3);z-index: 999;top:38px;border-top:2px solid #ddd;">
					<div class="alert-box bg-white showdow-full" style="height: 365px;width: 600px;">
						<div class="d-flex j-sb px-20 py-10">
							<div class="d-flex a-start">
								<input style="display:inline-block;width:190px;height:30px;vertical-align:middle;margin-right:-1px;border: 1px solid #C9C9C9;" type="text" placeholder="搜索点什么呢" autocomplete="off" class="layui-input">
								<button class="layui-btn layui-btn-sm layui-btn-primary tableSelect_btn_search" id="tableSelect_btn_search"><i class="layui-icon layui-icon-search"></i></button>
							</div>
							<div>
								<button id="tableSelect_btn_reset" class="layui-btn layui-btn-sm layui-btn-danger">重置</button>
								<button id="tableSelect_btn_select" class="layui-btn layui-btn-sm">选择</button>
							</div>
						</div>
						<div class="alert-box-header-content pt-0">
							<table id="addressTable" lay-filter="addressTable"></table>
						</div>
					</div>
				</div>`;
		},
		// 显示弹出框
		showAlertElem: function() {
			$("#" + CascadeSelect.alertElem).show();
		},
		// 隐藏弹出框
		hideAlertElem: function() {
			$("#" + CascadeSelect.alertElem).hide();
		},
		// 修改位置
		updatePosition: function(_this) {
			if(!webplus.isEmpty(_this)){
				let paramBody = CascadeSelect.getWindowParan();
				let bodyWidth = paramBody.bodyWidth;
				let top = _this.offset().top + 40;
				let left = _this.offset().left;
				
				let couptedLeft = bodyWidth - left - 600;
				// 如果当前的
				if (couptedLeft < 0) {
					left = left + couptedLeft - 15;
				}
				$("#" + CascadeSelect.alertElem).css({
					top: top + "px"
				});
				$("#" + CascadeSelect.alertElem).css({
					left: left + "px"
				});
			}
		},
		// 获取当前位置
		getWindowParan: function() {
			return {
				bodyWidth: document.body.clientWidth,
				bodyHeight: document.body.clientHeight,
				borderBodyWidth: document.body.offsetWidth,
				borderBodyHeight: document.body.offsetHeight,
				scrollTop: document.body.scrollTop,
				scrollLeft: document.body.scrollLeft,
			}
		},
		// 获取表单配置
		getTableConfig: function(name,nameURl='详细地址') {
			
			return addressSearchTable = {
				// 列
				cols: [
					[{
							align: 'center',
							type: 'radio'
						},
						{
							align: 'center',
							field: name,
							title: nameURl
						}
					]
				],
				
				// 高度
				tableHeight: '310',
				// 查询条件默认为空
				where: {}
			};
		},
		// 下拉表格
		dropTableData: function(_this) {
			 //上级标签name
			let sjname = _this.data("sjname");
			let field = _this.data("field");
			let titleName = _this.attr('data-titleName')
			let [fieldName, itemCode] = _this.data("field").split("|");
			let tableConfig = CascadeSelect.getTableConfig(fieldName,titleName);
			// 组装请求参数
			if(sjname){
				// 如果有多个
				if(sjname.indexOf("|")>1){
					let arrKey = sjname.split("|");
					arrKey.forEach((item)=>{
						if($("[name=" + item + "]").is('select')){
							tableConfig.where[item] = $("select[name=" + item + "]").find("option:selected").val();
						}else{
							tableConfig.where[item] = $("input[name=" + item + "]").val();
						}
					});
				}else{
					if($("[name=" + sjname + "]").is('select')){
						tableConfig.where[sjname] = $("select[name=" + sjname + "]").find("option:selected").val();
					}else{
						tableConfig.where[sjname] = $("input[name=" + sjname + "]").val();
					}
				}
			}else{
				return false;
			}
			if(CascadeSelect.searchValue){
				tableConfig.where[_this.data("searchname")] = CascadeSelect.searchValue;
			}
			tableConfig.where.token = webplus.getToken();
			table.render({
				elem: "#addressTable", //表id
				where: Object.assign({}, tableConfig.where),
				url: CascadeSelect.getHttp(_this.data("url")),
				method: 'post', //请求方式
				height: tableConfig.tableHeight, //控制表格高度
				defaultToolbar: [], //不开启工具栏
				cellMinWidth: 80, //每列最小宽度
				loading: true,
				page: true, //不开启分页
				cols: tableConfig.cols,
				limit: 20, //默认一页显示20条信息
				limits: [10, 20, 50, 100, 200], //配置每页显示条数下拉框选项
				response: {
					code: 0//重新规定成功的状态码为 200，table 组件默认为 0
				},
				parseData: function(res) { //将原始数据解析成 table 组件所规定的数据
					if(res.appCode == 1){
						return {
							"code": res.appCode, //解析接口状态
							"count": res.count, //解析数据长度
							"data": res.data //解析数据列表
						};
					}else{
						CascadeSelect.hideAlertElem();
					}
				},
				done: function(res) { //表格加载成功后执行的函数
					CascadeSelect.showAlertElem();
				}
			});
			
			// 地址表格双击事件
			table.on('rowDouble(addressTable)', function(obj) {
				let data = obj.data;
				CascadeSelect.setData(_this, data);
				event.stopPropagation();
			});
			
		},
		// 选中设置数据
		setData: function(_this, data) {
			// 获取当前配置的回显字段
			let [itemName, itemCode] = _this.data("field").split("|");
			_this.val(data[itemName]);
			_this.next().val(data[itemCode]);
			// 获取当前配置的下级关联标签
			let xjname = _this.data("xjname");
			// 如果有多个下级标签
			if(xjname && xjname.indexOf("|")>1){
				let arrKey = xjname.split("|");
				arrKey.forEach((item)=>{
					if($("[name=" + item + "]").is('select')){
						$("select[name=" + item + "]").find("option:selected").val();
						$("select[name=" + item + "]"+ ':gt(0)').remove();
					}else{
						$("input[name=" + item + "]").val('');
						$("input[name=" + item + "]").prev("input").val('');
					}
				});
			}else{
				if($("[name=" + xjname + "]").is('select')){
					$("select[name=" + xjname + "]").find("option:selected").val();
					$("select[name=" + xjname + "]"+ ':gt(0)').remove();
				}else{
					$("input[name=" + xjname + "]").val('');
					$("input[name=" + xjname + "]").prev("input").val('');
				}
			}
			form.render('select');
			// 隐藏
			CascadeSelect.hideAlertElem();
		},
		/**
		 * @param {Object} elem // 当前的select标签
		 * @param {Object} checkedVal  //选中的值
		 * @param {Object} childUrl //当前url地址
		 * @param {Object} isReset  //是否重置
		 */
		GetListByParent: function(elem, checkedVal, childUrl, isReset) {
			webplus.showLoading();
			let $selectNext = $("select[name='" + elem + "']");
			// 清空下拉框
			$selectNext.empty();
			$selectNext.prop("disabled", false);
			
			// 添加默认到第一个节点
			$selectNext.append("<option value=''>" + $selectNext.attr("promtion") + "</option>");
			// 获取下一个标签
			var nextElem = $selectNext.attr("nextElem");
			var _thischild = $("select[name='" + nextElem + "']");
			let selectValue = "";
			if (checkedVal) {
				$.ajax({
					type: "POST",
					url: childUrl,
					dataType: "json",
					data: { jz : checkedVal, token : CascadeSelect.token },
					success: function(result) {
						let data = result.data || result.dataList;
						if (data.length > 0) {
							$.each(data, function(i, d) {
								// 如果有固定参数或者只有一条数据  锁定
								if(result.itemCode == d.itemCode || data.length == 1){
									$selectNext.append("<option selected value='" + d.itemCode + "'>" + d.itemName + "</option>");
									$selectNext.prop("disabled", true);
								}else{
									$selectNext.append("<option value='" + d.itemCode + "'>" + d.itemName + "</option>");
								}
							});
							
							if (!isReset) {
								selectValue = $selectNext.attr("selectValue");
								$selectNext.val(selectValue);
							}
							if (_thischild.length > 0) {
								nextElemUrl = $selectNext.attr("nextElemUrlPath");
								CascadeSelect.GetListByParent(nextElem, selectValue, CascadeSelect.getHttp(nextElemUrl), isReset);
							}
						}
						form.render('select');
						webplus.hideLoading();
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						layer.msg('系统异常导致操作失败, 请联系管理员。', {
							icon: 5,
							shift: 6
						});
					},
					statusCode: {
						404: function() {
							layer.msg('未找到指定请求，请检查访问路径！', {
								icon: 5,
								shift: 6
							});
						},
						500: function() {
							layer.msg('系统错误，请联系管理员。', {
								icon: 5,
								shift: 6
							});
						}
					},
					complete: function(XMLHttpRequest, textStatus) {
						webplus.hideLoading();
					}
				});

			} else {
				while (_thischild.length > 0) {
					_thischild.empty();
					_thischild.append("<option value=''>" + _thischild.attr("promtion") + "</option>");
					_thischild = $("select[name='" + _thischild.attr("child") + "']");
				}
				webplus.hideLoading();
				form.render();
			}
		},

	};

	exports('cascadeSelect', CascadeSelect);
});
