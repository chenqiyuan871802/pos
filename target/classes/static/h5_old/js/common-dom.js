
const showCss = {"display":"block","transform": "translate3d(0,0,0)", "opacity": 1 };
const hideCss = { "display":"none","transform": "translate3d(100%,0,0)", "opacity": 0 };
/*
 * 显示panel
 * @elem  标签元素唯一id/class
 */ 
function showPanel(elem,typePanel){
	$(elem).css(showCss);
}
/*
 * 隐藏panel
 * @elem  标签元素唯一id/class
 */ 
function hidePanel(elem){
  $(elem).css(hideCss);
}

/*
 * 显示提示语
 * @title 提示文本
 * @duration  提示框默认显示1.5秒
 */
function showToast(title, duration=1500){
	let html = '<div class="yzk-toast"><div class="yzk-toast-title">'+title+'</div></div>';
	$("body").append(html);
	let showToast = setTimeout(()=>{
		$("body").find(".yzk-toast").remove();
		clearTimeout(showToast);
	},duration);
}

// 跳转提示
function jumpToast(cb,second=3,title,titleContent){
	let html = '<div class="yzk-showing-bg">'+
					'<div class="yzk-showing">'+
						'<p>'+title+'</p>'+
						'<p>'+titleContent+'（<span>'+second+'</span>s）</p>'+
					'</div>'+
				'</div>';
	$("body").append(html);
	let $overStepNum = $("body").find(".yzk-showing-bg>.yzk-showing>p>span");
	let showToast = setInterval(()=>{
		let num = parseInt($overStepNum.text());
		$overStepNum.text(--num);
		if(num==0){
			$("body").find(".yzk-showing-bg").remove();
			clearInterval(showToast);
			cb();
		}
	},1000);
}