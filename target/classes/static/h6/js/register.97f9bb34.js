(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["register"],{"0cea":function(e,t,a){"use strict";var r=a("d22f"),s=a.n(r);s.a},"466d":function(e,t,a){"use strict";var r=a("d784"),s=a("825a"),i=a("50c4"),o=a("1d80"),n=a("8aa5"),l=a("14c3");r("match",1,(function(e,t,a){return[function(t){var a=o(this),r=void 0==t?void 0:t[e];return void 0!==r?r.call(t,a):new RegExp(t)[e](String(a))},function(e){var r=a(t,e,this);if(r.done)return r.value;var o=s(e),c=String(this);if(!o.global)return l(o,c);var m=o.unicode;o.lastIndex=0;var u,d=[],h=0;while(null!==(u=l(o,c))){var v=String(u[0]);d[h]=v,""===v&&(o.lastIndex=n(c,i(o.lastIndex),m)),h++}return 0===h?null:d}]}))},7803:function(e,t,a){"use strict";a.r(t);var r=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"container"},[a("headerBox",{attrs:{iconToggleShow:!1}}),a("div",{staticClass:"mainBox"},[a("div",{staticClass:"formBox"},[a("div",{staticClass:"formItem"},[a("div",{staticClass:"name"},[e._v("名前を入力してください（必須）")]),a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.firstName,expression:"ruleForm.firstName"}],staticClass:"input",attrs:{type:"text",placeholder:"姓"},domProps:{value:e.ruleForm.firstName},on:{input:function(t){t.target.composing||e.$set(e.ruleForm,"firstName",t.target.value)}}})]),a("div",{staticClass:"formItem"},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.lastName,expression:"ruleForm.lastName"}],staticClass:"input",attrs:{type:"text",placeholder:"名"},domProps:{value:e.ruleForm.lastName},on:{input:function(t){t.target.composing||e.$set(e.ruleForm,"lastName",t.target.value)}}})]),a("div",{staticClass:"formItem"},[a("div",{staticClass:"name"},[e._v("携帯番号を入力してください（必須）")]),a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.mobile,expression:"ruleForm.mobile"}],staticClass:"input",attrs:{type:"text",placeholder:"携帯番号"},domProps:{value:e.ruleForm.mobile},on:{input:function(t){t.target.composing||e.$set(e.ruleForm,"mobile",t.target.value)}}})]),a("button",{staticClass:"sendBtn",attrs:{disabled:e.isDisabled},on:{click:function(t){return e.getAuthCode()}}},[e._v(e._s(e.buttonName))]),e._m(0),a("div",{staticClass:"sendTips"},[e._v(" 本人確認のため、携帯電話のSMS(ショートメッセジサービス)を利用して認証を行います。送られてきた認証番号(4桁)を次へ入力してください。 ")]),a("div",{staticClass:"formItem mt0"},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.authCode,expression:"ruleForm.authCode"}],staticClass:"input",attrs:{type:"text",placeholder:"認証番号(4桁)",maxlength:"4"},domProps:{value:e.ruleForm.authCode},on:{input:function(t){t.target.composing||e.$set(e.ruleForm,"authCode",t.target.value)}}})]),a("div",{staticClass:"formItem"},[a("div",{staticClass:"name"},[e._v("生年月日")]),a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.birthDate,expression:"ruleForm.birthDate"}],staticClass:"input",attrs:{type:"text",placeholder:"生年月日",readonly:"readonly"},domProps:{value:e.ruleForm.birthDate},on:{click:function(t){e.birthdayShow=!0},input:function(t){t.target.composing||e.$set(e.ruleForm,"birthDate",t.target.value)}}}),e._m(1)]),a("div",{staticClass:"formItem"},[a("div",{staticClass:"name"},[e._v("性別を選択（必須）")]),a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.sexName,expression:"ruleForm.sexName"}],staticClass:"input",attrs:{type:"text",placeholder:"性別",readonly:"readonly"},domProps:{value:e.ruleForm.sexName},on:{click:function(t){return e.chooseSex()},input:function(t){t.target.composing||e.$set(e.ruleForm,"sexName",t.target.value)}}}),e._m(2)]),a("div",{staticClass:"formItem"},[a("div",{staticClass:"name"},[e._v("入力間違いました")]),a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.email,expression:"ruleForm.email"}],staticClass:"input",attrs:{type:"text",placeholder:"メールアドレス"},domProps:{value:e.ruleForm.email},on:{input:function(t){t.target.composing||e.$set(e.ruleForm,"email",t.target.value)}}})]),a("div",{staticClass:"formItem"},[a("div",{staticClass:"name"},[e._v("パスワードを入力してください")]),a("input",{directives:[{name:"model",rawName:"v-model",value:e.ruleForm.password,expression:"ruleForm.password"}],staticClass:"input",attrs:{type:"password",placeholder:"パスワード（半角英数6桁以上）"},domProps:{value:e.ruleForm.password},on:{input:function(t){t.target.composing||e.$set(e.ruleForm,"password",t.target.value)}}})]),a("van-checkbox",{staticClass:"checkBox",attrs:{"checked-color":"#27ABE1",shape:"square","label-disabled":""},model:{value:e.checked,callback:function(t){e.checked=t},expression:"checked"}},[e._v(" 利用規約に同意 "),a("span",{staticClass:"rule",on:{click:function(t){return e.$router.push("/rule/2")}}},[e._v(" 利用規約を読んで同意 ")])]),a("button",{staticClass:"registerBtn",on:{click:function(t){return e.submit()}}},[e._v("新規会員登録")])],1)]),a("van-action-sheet",{attrs:{actions:e.actions},on:{select:e.onSelect},model:{value:e.show,callback:function(t){e.show=t},expression:"show"}}),a("van-popup",{style:{height:"50%"},attrs:{position:"bottom"},model:{value:e.birthdayShow,callback:function(t){e.birthdayShow=t},expression:"birthdayShow"}},[a("van-datetime-picker",{attrs:{"min-date":e.minDate,"max-date":e.maxDate,type:"date","confirm-button-text":"完了","cancel-button-text":"キャンセル"},on:{change:function(t){return e.changeFn()},confirm:e.confirmBrithday,cancel:e.closeBrithday},model:{value:e.currentDate,callback:function(t){e.currentDate=t},expression:"currentDate"}})],1)],1)},s=[function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("i",{staticClass:"iconDown"},[r("img",{attrs:{src:a("c74d"),alt:""}})])},function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("i",{staticClass:"iconRight"},[r("img",{attrs:{src:a("b81e"),alt:""}})])},function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("i",{staticClass:"iconRight"},[r("img",{attrs:{src:a("b81e"),alt:""}})])}],i=(a("b0c0"),a("ac1f"),a("466d"),a("8a58"),a("e41f")),o=(a("d1cf"),a("ee83")),n=(a("2cbd"),a("ab2c")),l=(a("e7e5"),a("d399")),c=(a("a909"),a("3acc")),m=(a("3c32"),a("417e")),u=a("6efa"),d=a("3de1"),h=a("3191"),v=a("f6b5"),p=a.n(v);p.a.use(m["a"]),p.a.use(c["a"]),p.a.use(l["a"]),p.a.use(n["a"]),p.a.use(o["a"]),p.a.use(i["a"]);var f={name:"Home",components:{headerBox:u["a"]},data:function(){return{show:!1,birthdayShow:!1,currentDate:new Date,changeDate:new Date,minDate:new Date(1920,0,1),maxDate:new Date(2100,0,1),actions:[{name:"男性",val:"M"},{name:"女性",val:"F"},{name:"不明",val:"N"}],cancelText:"キャンセルする",checked:!1,buttonName:"携帯番号のSMS認証を送る",time:60,isDisabled:!1,ruleForm:{mobile:"",password:"",firstName:"",lastName:"",email:"",sex:"",sexName:"",authCode:"",birthDate:""},flag:!1}},created:function(){},mounted:function(){},methods:{onCancel:function(){this.show=!1},chooseSex:function(){this.show=!0},onSelect:function(e){this.ruleForm.sex=e.val,this.ruleForm.sexName=e.name,this.onCancel()},closeBrithday:function(){this.birthdayShow=!1},changeFn:function(){this.changeDate=this.currentDate},confirmBrithday:function(){this.ruleForm.birthDate=this.timeFormat(this.currentDate),console.log(this.ruleForm.birthDate),this.closeBrithday()},timeFormat:function(e){var t=e.getFullYear(),a=e.getMonth()+1,r=e.getDate();return t+"-"+a+"-"+r},getAuthCode:function(){if(Object(d["a"])(this.ruleForm.mobile))Object(l["a"])("正しい携帯電話番号を入力してください");else{var e=this;e.isDisabled=!0;var t=window.setInterval((function(){e.buttonName=e.time+"s",--e.time,e.time<0&&(e.buttonName="再送",e.time=60,e.isDisabled=!1,window.clearInterval(t))}),1e3);Object(h["v"])({mobile:e.ruleForm.mobile}).then((function(e){1===e.appCode&&Object(l["a"])("正常に送信されました")})).catch((function(e){}))}},submit:function(){var e=this;this.ruleForm.firstName&&this.ruleForm.lastName?Object(d["a"])(this.ruleForm.mobile)?Object(l["a"])("正しい携帯電話番号を入力してください"):this.ruleForm.authCode?this.ruleForm.sexName?/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(this.ruleForm.email)?this.ruleForm.password.match(/^(\w){6,20}$/)?this.checked?this.flag||(this.flag=!0,this.$loading.open(),Object(h["h"])({mobile:this.ruleForm.mobile,password:this.ruleForm.password,firstName:this.ruleForm.firstName,lastName:this.ruleForm.lastName,email:this.ruleForm.email,sex:this.ruleForm.sex,authCode:this.ruleForm.authCode,birthDate:this.ruleForm.birthDate}).then((function(t){1===t.appCode?(Object(l["a"])("登録成功"),setTimeout((function(){e.$router.push("/login")}),1e3)):Object(l["a"])(t.appMsg),e.flag=!1,e.$loading.close()})).catch((function(t){e.flag=!1,e.$loading.close()}))):Object(l["a"])("利用規約に同意してください"):Object(l["a"])("パスワードを入力してください"):Object(l["a"])("メールアドレスを入力してください"):Object(l["a"])("性別を選択してください"):Object(l["a"])("送られてきた認証番号(4桁)を次へ入力してください"):Object(l["a"])("名前を入力してください（必須）")}}},b=f,g=(a("0cea"),a("2877")),F=Object(g["a"])(b,r,s,!1,null,"6f5f4e57",null);t["default"]=F.exports},c74d:function(e,t,a){e.exports=a.p+"img/icon_down.68d6856b.svg"},d22f:function(e,t,a){}}]);