(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["history"],{"0a0d":function(t,i,e){},2994:function(t,i,e){"use strict";e("68ef"),e("e3b3"),e("c0c2")},"2bdd":function(t,i,e){"use strict";var n=e("d282");function o(t){var i=window.getComputedStyle(t),e="none"===i.display,n=null===t.offsetParent&&"fixed"!==i.position;return e||n}var r=e("a8c1"),s=e("5fbe"),a=e("543e"),c=Object(n["a"])("list"),d=c[0],l=c[1],h=c[2];i["a"]=d({mixins:[Object(s["a"])((function(t){this.scroller||(this.scroller=Object(r["a"])(this.$el)),t(this.scroller,"scroll",this.check)}))],model:{prop:"loading"},props:{error:Boolean,loading:Boolean,finished:Boolean,errorText:String,loadingText:String,finishedText:String,immediateCheck:{type:Boolean,default:!0},offset:{type:[Number,String],default:300},direction:{type:String,default:"down"}},data:function(){return{innerLoading:this.loading}},updated:function(){this.innerLoading=this.loading},mounted:function(){this.immediateCheck&&this.check()},watch:{loading:"check",finished:"check"},methods:{check:function(){var t=this;this.$nextTick((function(){if(!(t.innerLoading||t.finished||t.error)){var i,e=t.$el,n=t.scroller,r=t.offset,s=t.direction;i=n.getBoundingClientRect?n.getBoundingClientRect():{top:0,bottom:n.innerHeight};var a=i.bottom-i.top;if(!a||o(e))return!1;var c=!1,d=t.$refs.placeholder.getBoundingClientRect();c="up"===s?i.top-d.top<=r:d.bottom-i.bottom<=r,c&&(t.innerLoading=!0,t.$emit("input",!0),t.$emit("load"))}}))},clickErrorText:function(){this.$emit("update:error",!1),this.check()},genLoading:function(){var t=this.$createElement;if(this.innerLoading&&!this.finished)return t("div",{class:l("loading"),key:"loading"},[this.slots("loading")||t(a["a"],{attrs:{size:"16"}},[this.loadingText||h("loading")])])},genFinishedText:function(){var t=this.$createElement;if(this.finished){var i=this.slots("finished")||this.finishedText;if(i)return t("div",{class:l("finished-text")},[i])}},genErrorText:function(){var t=this.$createElement;if(this.error){var i=this.slots("error")||this.errorText;if(i)return t("div",{on:{click:this.clickErrorText},class:l("error-text")},[i])}}},render:function(){var t=arguments[0],i=t("div",{ref:"placeholder",class:l("placeholder")});return t("div",{class:l(),attrs:{role:"feed","aria-busy":this.innerLoading}},["down"===this.direction?this.slots():i,this.genLoading(),this.genFinishedText(),this.genErrorText(),"up"===this.direction?this.slots():i])}})},"43c4":function(t,i,e){"use strict";e.r(i);var n=function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"container"},[e("headerBox"),e("div",{staticClass:"mainBox"},[e("div",{staticClass:"historyBox"},[e("van-list",{attrs:{finished:t.finished,"finished-text":t.finishedText,"loading-text":t.loadingText,"immediate-check":!1},on:{load:t.onLoad},model:{value:t.loading,callback:function(i){t.loading=i},expression:"loading"}},[e("ul",t._l(t.list,(function(i,n){return e("li",{key:n},[e("div",{staticClass:"top"},[t._v(" "+t._s(i.outletName)+" ")]),e("div",{staticClass:"bottom"},[e("div",{staticClass:"tips"},[t._v(t._s(i.transDate)+" 会計金額"+t._s(i.total))]),e("div",{staticClass:"money",class:{red:i.point<=0}},[t._v(t._s(i.point)+"P")])])])})),0)])],1)]),e("footerBox",{attrs:{navIndex:1}})],1)},o=[],r=(e("99af"),e("2994"),e("2bdd")),s=e("6efa"),a=e("0f80"),c=e("5530"),d=e("751a"),l=function(t){return d["a"].post("/point/listPoint",Object(c["a"])({},t))},h=e("f6b5"),f=e.n(h);f.a.use(r["a"]);var u={name:"history",components:{headerBox:s["a"],footerBox:a["a"]},data:function(){return{list:[],loading:!1,finished:!1,page:1,size:10,total:0,finishedText:"",loadingText:"loading..."}},created:function(){this.getListPoint()},mounted:function(){},methods:{onLoad:function(){var t=this;setTimeout((function(){t.page++,t.getListPoint()}),500)},getListPoint:function(){var t=this,i={currentPage:this.page,pageSize:this.size};l(i).then((function(i){if(1===i.appCode){var e=i.dataList||[];e.length>0?t.list=t.list.concat(e):t.list=[],t.total=i.page.count}t.loading=!1,i.dataList.length<t.size&&(t.finished=!0)})).catch((function(i){t.loading=!1}))}}},g=u,p=(e("7034"),e("2877")),v=Object(p["a"])(g,n,o,!1,null,"72d8d61e",null);i["default"]=v.exports},7034:function(t,i,e){"use strict";var n=e("0a0d"),o=e.n(n);o.a},"99af":function(t,i,e){"use strict";var n=e("23e7"),o=e("d039"),r=e("e8b5"),s=e("861d"),a=e("7b0b"),c=e("50c4"),d=e("8418"),l=e("65f0"),h=e("1dde"),f=e("b622"),u=e("2d00"),g=f("isConcatSpreadable"),p=9007199254740991,v="Maximum allowed index exceeded",m=u>=51||!o((function(){var t=[];return t[g]=!1,t.concat()[0]!==t})),x=h("concat"),b=function(t){if(!s(t))return!1;var i=t[g];return void 0!==i?!!i:r(t)},T=!m||!x;n({target:"Array",proto:!0,forced:T},{concat:function(t){var i,e,n,o,r,s=a(this),h=l(s,0),f=0;for(i=-1,n=arguments.length;i<n;i++)if(r=-1===i?s:arguments[i],b(r)){if(o=c(r.length),f+o>p)throw TypeError(v);for(e=0;e<o;e++,f++)e in r&&d(h,f,r[e])}else{if(f>=p)throw TypeError(v);d(h,f++,r)}return h.length=f,h}})},c0c2:function(t,i,e){}}]);