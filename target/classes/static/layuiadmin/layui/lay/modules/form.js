/** layui-v2.4.3 MIT License By https://www.layui.com */ ;
layui.define("layer", function (e) {
    "use strict";
    var i = layui.$,
        t = layui.layer,
        a = layui.hint(),
        n = layui.device(),
        l = "form",
        r = ".layui-form",
        s = "layui-this",
        o = "layui-hide",
        c = "layui-disabled",
        u = function () {
    	this.config = {
                verify: {
                    required: [/[\S]+/, "未記入の項目があります"],
                    phone: [/^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/, "请输入正确手机号码"],
                    tel: [/^(0\d{2,3}-\d{7,8})(-\d{1,4})?$/, "请输入正确的座机号码"],
                    phoneAndTel:function(e,r){
                    	if(!(/^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/.test(e)||/^(0\d{2,3}-\d{7,8})(-\d{1,4})?$/.test(e))){
                    		
                    		return "请输入正确手机号码或座机号码"
                    	}
                    },
                    fax:[/^(\d{3,4}-)?\d{7,8}$/,"请输入正确传真号码"],
                    email: [/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/, "邮箱格式不正确"],
                    url: [/(^#)|(^http(s*):\/\/[^\s]+\.[^\s]+)/, "链接格式不正确"],
                    date: [/^(\d{4})[-\/](\d{1}|0\d{1}|1[0-2])([-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/, "日期格式不正确"],
                    identity: [/(^\d{15}$)|(^\d{17}(\d|X|x)$)/, "请输入正确的身份证号"],
                    uname: [/^[\u4E00-\u9FA5a-zA-Z][\u4E00-\u9FA5a-zA-Z0-9_]*$/, "只能为汉字和字母数字组合"],
                    intOrFloat:[/^\d+(\.\d+)?$/i,"请输入数字，并确保格式正确"],
                    integer: [/^[0-9]\d*$/, "正の整数"],
                    qq: [/^[1-9][0-9]{4,10}$/, "请输入正确的QQ号码"],
                    keyname:[/^[a-zA-Z][a-zA-Z0-9_]{1,29}$/i,"输入不合法（字母开头，允许2-30字符，允许字母数字下划线）"],
                    number: function (e) {
                        if (isNaN(e)) return "数字のみを入力してください"
                    }, min: function (e, r) {
                        if (e.length < r) return "输入不能小于长度" + r
                    }, max: function (e, r) {
                        if (e.length > r) return "制限文字数を超えました" + r
                    }, between: function (e, r) {
                        var a = parseInt(r.split("-")[0]),
                            t = parseInt(r.split("-")[1]);
                        if (e.length < a || e.length > t) {
                            return "输入长度只能介于" + a + "和" + t + "之间"
                        }
                    },
                    minInt:function(e,r){
                    	if(isNaN(e)){
                    		return "数字のみを入力してください"
                    	}
                    	var a=parseInt(r);
                    	if(e<a){
                    		 return "输入的值不能小于" + r
                    	}
                    	
                    },
                    maxInt:function(e,r){
                    	if(isNaN(e)){
                    		return "只能填写数字"
                    	}
                    	var a=parseInt(r);
                    	if(e>a){
                    		 return "输入的值不能大于" + r
                    	}
                    	
                    },
                    accepted: function (e) {}, alpha: [/^[a-zA-Z]+$/, "只能为纯字母"],
                    wechat:[/^[a-zA-Z\d_]{5,}$/,"微信号格式不正确"],
                    alphaNum: [/^[A-Za-z0-9]+$/, "只能为字母或数字"],
                    alphaDash: [/^[a-zA-Z0-9_-]+$/, "只能为字母或数字_-"],
                    chs: [/^[\u4e00-\u9fa5]+$/, "只能为汉字"],
                    chsAlpha: [/^[A-Za-z\u4e00-\u9fa5]+$/, "只能为汉字和字母"],
                    chsAlphaNum: [/^[A-Za-z0-9\u4e00-\u9fa5]+$/, "只能为汉字、字母、数字"],
                    chsAlphaNumDash: [/^[A-Za-z0-9_\u4e00-\u9fa5]+$/, "只能为汉字、字母、数字、下划线"],
                    xdigit: [/([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$/, "不是正确的十六进制格式"],
                    httpUrl: [/^(?=^.{3,255}$)(http(s)?:\/\/)?(www\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\d+)*(\/\w+\.\w+)*([\?&]\w+=\w*)*$/, "请输入正确的网址url"],
                    domain: [/^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/, "请输入正确的域名"],
                    ip: [/^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/, "请输入正确的ip地址"],
                    disabled: [/[^~\x22]+/, "禁止输入~特殊符号"],
                    macAddr: [/^[A-F0-9]{2}(-[A-F0-9]{2}){5}$/, "请输入正确的mac地址，默认'-'分割"],
                    part: [/^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/, "请输入正确的端口号"],
                    longitude: [/^[\-\+]?(0?\d{1,2}|0?\d{1,2}\.\d{1,15}|1[0-7]?\d{1}|1[0-7]?\d{1}\.\d{1,15}|180|180\.0{1,15})$/, "请输入正确的经度"],
                    latitude: [/^[\-\+]?([0-8]?\d{1}|[0-8]?\d{1}\.\d{1,15}|90|90\.0{1,15})$/, "请输入正确的纬度"],
                    car: [/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[a-hj-zA-HJ-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[a-hj-np-zA-HJ-NP-Z0-9]{4,5}[a-hj-np-zA-HJ-NP-Z0-9挂学警港澳]{1}$/, "请输入正确的车牌号码"],
                    level: function (e, r) {
                        var a = this.pwdStrong(e);
                        if (a < r) {
                        }
                    }, pwd: [/((?=.*[a-z])(?=.*\d)|(?=[a-z])(?=.*[#@!~%^&*])|(?=.*\d)(?=.*[#@!~%^&*]))[a-z\d#@!~%^&*]{5,32}/i, "密码只能为6-32位的非纯数字、非纯字母、非纯特殊字符所组成"],
                    repeatPwd: function (e, r) {
                        if (e !== i("#" + r).val()) {
                            return "パスワードが一致してません"
                        }
                    }, creditCode: function (e) {
                        var r = /^[0-9A-Z]+$/;
                        if (e.length != 18 || r.test(e) == false) {
                            return "社会统一信用代码格式不正确"
                        } else {
                            var a;
                            var t;
                            var i = 0;
                            var n = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];
                            var s = "0123456789ABCDEFGHJKLMNPQRTUWXY";
                            for (var l = 0; l < e.length - 1; l++) {
                                a = e.substring(l, l + 1);
                                t = s.indexOf(a);
                                i = i + t * n[l]
                            }
                            var f = 31 - i % 31;
                            if (f == 31) {
                                f = 0
                            }
                            var o = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
                            var u = o.split(",");
                            f = u[f];
                            var d = e.substring(17, 18);
                            if (f != d) {
                                return "社会统一信用代码格式不正确"
                            }
                            return true
                        }
                    }, bankCard: function (e) {
                        e = e || String(this);
                        if ("" == e.trim() || undefined == e) {
                            return false
                        }
                        var r = e.substr(e.length - 1, 1);
                        var a = e.substr(0, e.length - 1);
                        var t = new Array;
                        for (var i = a.length - 1; i > -1; i--) {
                            t.push(a.substr(i, 1))
                        }
                        var n = new Array;
                        var s = new Array;
                        var l = new Array;
                        for (var f = 0; f < t.length; f++) {
                            if ((f + 1) % 2 == 1) {
                                if (parseInt(t[f]) * 2 < 9) n.push(parseInt(t[f]) * 2);
                                else s.push(parseInt(t[f]) * 2)
                            } else l.push(t[f])
                        }
                        var o = new Array;
                        var u = new Array;
                        for (var d = 0; d < s.length; d++) {
                            o.push(parseInt(s[d]) % 10);
                            u.push(parseInt(s[d]) / 10)
                        }
                        var c = 0;
                        var p = 0;
                        var v = 0;
                        var h = 0;
                        var y = 0;
                        for (var m = 0; m < n.length; m++) {
                            c = c + parseInt(n[m])
                        }
                        for (var g = 0; g < l.length; g++) {
                            p = p + parseInt(l[g])
                        }
                        for (var b = 0; b < o.length; b++) {
                            v = v + parseInt(o[b]);
                            h = h + parseInt(u[b])
                        }
                        y = parseInt(c) + parseInt(p) + parseInt(v) + parseInt(h);
                        var x = parseInt(y) % 10 == 0 ? 10 : parseInt(y) % 10;
                        var C = 10 - x;
                        if (r == C) {
                            return true
                        } else {
                            return "请输入正确的银行卡号"
                        }
                    }, idCard: function (e) {
                        var r = {
                            11: "北京",
                            12: "天津",
                            13: "河北",
                            14: "山西",
                            15: "内蒙古",
                            21: "辽宁",
                            22: "吉林",
                            23: "黑龙江 ",
                            31: "上海",
                            32: "江苏",
                            33: "浙江",
                            34: "安徽",
                            35: "福建",
                            36: "江西",
                            37: "山东",
                            41: "河南",
                            42: "湖北 ",
                            43: "湖南",
                            44: "广东",
                            45: "广西",
                            46: "海南",
                            50: "重庆",
                            51: "四川",
                            52: "贵州",
                            53: "云南",
                            54: "西藏 ",
                            61: "陕西",
                            62: "甘肃",
                            63: "青海",
                            64: "宁夏",
                            65: "新疆",
                            71: "台湾",
                            81: "香港",
                            82: "澳门",
                            91: "国外 "
                        };
                        var a = "";
                        var t = true;
                        if (!e || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(e)) {
                            a = "身份证号格式错误";
                            t = false
                        } else if (!r[e.substr(0, 2)]) {
                            a = "身份证号地址编码错误";
                            t = false
                        } else {
                            if (e.length == 18) {
                                e = e.split("");
                                var i = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                                var n = [1, 0, "X", 9, 8, 7, 6, 5, 4, 3, 2];
                                var s = 0;
                                var l = 0;
                                var f = 0;
                                for (var o = 0; o < 17; o++) {
                                    l = e[o];
                                    f = i[o];
                                    s += l * f
                                }
                                var u = n[s % 11];
                                if (n[s % 11] != e[17]) {
                                    a = "身份证号校验位错误";
                                    t = false
                                }
                            }
                        } if (!t) {
                            return a
                        }
                    }, pwdStrong: function (e) {
                        var r = 0;
                        if (e.match(/[a-z]/g)) {
                            r++
                        }
                        if (e.match(/[A-Z]/g)) {
                            r++
                        }
                        if (e.match(/[0-9]/g)) {
                            r++
                        }
                        if (e.match(/(.[^a-z0-9A-Z])/g)) {
                            r++
                        }
                        if (r > 4) {
                            r = 4
                        }
                        if (r === 0) return false;
                        return r
                    }
                },
                func: {}
            }
        };
    
    u.prototype.set = function (e) {
        var t = this;
        return i.extend(!0, t.config, e), t
    }, u.prototype.verify = function (e) {
        var t = this;
        return i.extend(!0, t.config.verify, e), t
    }, u.prototype.on = function (e, i) {
        return layui.onevent.call(this, l, e, i)
    }, u.prototype.val = function (e, t) {
        var a = i(r + '[lay-filter="' + e + '"]');
        a.each(function (e, a) {
            var n = i(this);
            layui.each(t, function (e, i) {
                var t, a = n.find('[name="' + e + '"]');
                a[0] && (t = a[0].type, "checkbox" === t ? a[0].checked = i : "radio" === t ? a.each(function () {
                    this.value === i && (this.checked = !0)
                }) : a.val(i))
            })
        }), f.render(null, e)
    }, u.prototype.render = function (e, t) {
        var n = this,
            u = i(r + function () {
                return t ? '[lay-filter="' + t + '"]' : ""
            }()),
            d = {
                select: function () {
                    var e, t = "请选择",
                        a = "layui-form-select",
                        n = "layui-select-title",
                        r = "layui-select-none",
                        d = "",
                        f = u.find("select"),
                        v = function (t, l) {
                            i(t.target).parent().hasClass(n) && !l || (i("." + a).removeClass(a + "ed " + a + "up"), e && d && e.val(d)), e = null
                        },
                        y = function (t, u, f) {
                            var y, p = i(this),
                                m = t.find("." + n),
                                k = m.find("input"),
                                x = t.find("dl"),
                                g = x.children("dd"),
                                b = this.selectedIndex;
                            if (!u) {
                                var C = function () {
                                        var e = t.offset().top + t.outerHeight() + 5 - h.scrollTop(),
                                            i = x.outerHeight();
                                        b = p[0].selectedIndex, t.addClass(a + "ed"), g.removeClass(o), y = null, g.eq(b).addClass(s).siblings().removeClass(s), e + i > h.height() && e >= i && t.addClass(a + "up"), $()
                                    },
                                    w = function (e) {
                                        t.removeClass(a + "ed " + a + "up"), k.blur(), y = null, e || T(k.val(), function (e) {
                                            e && (d = x.find("." + s).html(), k && k.val(d))
                                        })
                                    },
                                    $ = function () {
                                        var e = x.children("dd." + s);
                                        if (e[0]) {
                                            var i = e.position().top,
                                                t = x.height(),
                                                a = e.height();
                                            i > t && x.scrollTop(i + x.scrollTop() - t + a - 5), i < 0 && x.scrollTop(i + x.scrollTop() - 5)
                                        }
                                    };
                                m.on("click", function (e) {
                                    t.hasClass(a + "ed") ? w() : (v(e, !0), C()), x.find("." + r).remove()
                                }), m.find(".layui-edge").on("click", function () {
                                    k.focus()
                                }), k.on("keyup", function (e) {
                                    var i = e.keyCode;
                                    9 === i && C()
                                }).on("keydown", function (e) {
                                    var i = e.keyCode;
                                    9 === i && w();
                                    var t = function (i, a) {
                                        var n, l;
                                        e.preventDefault();
                                        var r = function () {
                                            var e = x.children("dd." + s);
                                            if (x.children("dd." + o)[0] && "next" === i) {
                                                var t = x.children("dd:not(." + o + ",." + c + ")"),
                                                    n = t.eq(0).index();
                                                if (n >= 0 && n < e.index() && !t.hasClass(s)) return t.eq(0).prev()[0] ? t.eq(0).prev() : x.children(":last")
                                            }
                                            return a && a[0] ? a : y && y[0] ? y : e
                                        }();
                                        return l = r[i](), n = r[i]("dd:not(." + o + ")"), l[0] ? (y = r[i](), n[0] && !n.hasClass(c) || !y[0] ? (n.addClass(s).siblings().removeClass(s), void $()) : t(i, y)) : y = null
                                    };
                                    38 === i && t("prev"), 40 === i && t("next"), 13 === i && (e.preventDefault(), x.children("dd." + s).trigger("click"))
                                });
                                var T = function (e, t, a) {
                                        var n = 0;
                                        layui.each(g, function () {
                                            var t = i(this),
                                                l = t.text(),
                                                r = l.indexOf(e) === -1;
                                            ("" === e || "blur" === a ? e !== l : r) && n++, "keyup" === a && t[r ? "addClass" : "removeClass"](o)
                                        });
                                        var l = n === g.length;
                                        return t(l), l
                                    },
                                    j = function (e) {
                                        var i = this.value,
                                            t = e.keyCode;
                                        return 9 !== t && 13 !== t && 37 !== t && 38 !== t && 39 !== t && 40 !== t && (T(i, function (e) {
                                            e ? x.find("." + r)[0] || x.append('<p class="' + r + '">无匹配项</p>') : x.find("." + r).remove()
                                        }, "keyup"), "" === i && x.find("." + r).remove(), void $())
                                    };
                                f && k.on("keyup", j).on("blur", function (t) {
                                    var a = p[0].selectedIndex;
                                    e = k, d = i(p[0].options[a]).html(), setTimeout(function () {
                                        T(k.val(), function (e) {
                                            d || k.val("")
                                        }, "blur")
                                    }, 200)
                                }), g.on("click", function () {
                                    var e = i(this),
                                        a = e.attr("lay-value"),
                                        n = p.attr("lay-filter");
                                    return !e.hasClass(c) && (e.hasClass("layui-select-tips") ? k.val("") : (k.val(e.text()), e.addClass(s)), e.siblings().removeClass(s), p.val(a).removeClass("layui-form-danger"), layui.event.call(this, l, "select(" + n + ")", {
                                        elem: p[0],
                                        value: a,
                                        othis: t
                                    }), w(!0), !1)
                                }), t.find("dl>dt").on("click", function (e) {
                                    return !1
                                }), i(document).off("click", v).on("click", v)
                            }
                        };
                    f.each(function (e, l) {
                        var r = i(this),
                            o = r.next("." + a),
                            u = this.disabled,
                            d = l.value,
                            f = i(l.options[l.selectedIndex]),
                            v = l.options[0];
                        if ("string" == typeof r.attr("lay-ignore")) return r.show();
                        var h = "string" == typeof r.attr("lay-search"),
                            p = v ? v.value ? t : v.innerHTML || t : t,
                            m = i(['<div class="' + (h ? "" : "layui-unselect ") + a, (u ? " layui-select-disabled" : "") + '">', '<div class="' + n + '">', '<input type="text" placeholder="' + p + '" ' + ('value="' + (d ? f.html() : "") + '"') + (h ? "" : " readonly") + ' class="layui-input' + (h ? "" : " layui-unselect") + (u ? " " + c : "") + '">', '<i class="layui-edge"></i></div>', '<dl class="layui-anim layui-anim-upbit' + (r.find("optgroup")[0] ? " layui-select-group" : "") + '">',
                                function (e) {
                                    var i = [];
                                    return layui.each(e, function (e, a) {
                                        0 !== e || a.value ? "optgroup" === a.tagName.toLowerCase() ? i.push("<dt>" + a.label + "</dt>") : i.push('<dd lay-value="' + a.value + '" class="' + (d === a.value ? s : "") + (a.disabled ? " " + c : "") + '">' + a.innerHTML + "</dd>") : i.push('<dd lay-value="" class="layui-select-tips">' + (a.innerHTML || t) + "</dd>")
                                    }), 0 === i.length && i.push('<dd lay-value="" class="' + c + '">没有选项</dd>'), i.join("")
                                }(r.find("*")) + "</dl>", "</div>"
                            ].join(""));
                        o[0] && o.remove(), r.after(m), y.call(this, m, u, h)
                    })
                }, checkbox: function () {
                    var e = {
                            checkbox: ["layui-form-checkbox", "layui-form-checked", "checkbox"],
                            _switch: ["layui-form-switch", "layui-form-onswitch", "switch"]
                        },
                        t = u.find("input[type=checkbox]"),
                        a = function (e, t) {
                            var a = i(this);
                            e.on("click", function () {
                                var i = a.attr("lay-filter"),
                                    n = (a.attr("lay-text") || "").split("|");
                                a[0].disabled || (a[0].checked ? (a[0].checked = !1, e.removeClass(t[1]).find("em").text(n[1])) : (a[0].checked = !0, e.addClass(t[1]).find("em").text(n[0])), layui.event.call(a[0], l, t[2] + "(" + i + ")", {
                                    elem: a[0],
                                    value: a[0].value,
                                    othis: e
                                }))
                            })
                        };
                    t.each(function (t, n) {
                        var l = i(this),
                            r = l.attr("lay-skin"),
                            s = (l.attr("lay-text") || "").split("|"),
                            o = this.disabled;
                        "switch" === r && (r = "_" + r);
                        var u = e[r] || e.checkbox;
                        if ("string" == typeof l.attr("lay-ignore")) return l.show();
                        var d = l.next("." + u[0]),
                            f = i(['<div class="layui-unselect ' + u[0], n.checked ? " " + u[1] : "", o ? " layui-checkbox-disbaled " + c : "", '"', r ? ' lay-skin="' + r + '"' : "", ">",
                                function () {
                                    var e = n.title.replace(/\s/g, ""),
                                        i = {
                                            checkbox: [e ? "<span>" + n.title + "</span>" : "", '<i class="layui-icon layui-icon-ok"></i>'].join(""),
                                            _switch: "<em>" + ((n.checked ? s[0] : s[1]) || "") + "</em><i></i>"
                                        };
                                    return i[r] || i.checkbox
                                }(), "</div>"
                            ].join(""));
                        d[0] && d.remove(), l.after(f), a.call(this, f, u)
                    })
                }, radio: function () {
                    var e = "layui-form-radio",
                        t = ["&#xe643;", "&#xe63f;"],
                        a = u.find("input[type=radio]"),
                        n = function (a) {
                            var n = i(this),
                                s = "layui-anim-scaleSpring";
                            a.on("click", function () {
                                var o = n[0].name,
                                    c = n.parents(r),
                                    u = n.attr("lay-filter"),
                                    d = c.find("input[name=" + o.replace(/(\.|#|\[|\])/g, "\\$1") + "]");
                                n[0].disabled || (layui.each(d, function () {
                                    var a = i(this).next("." + e);
                                    this.checked = !1, a.removeClass(e + "ed"), a.find(".layui-icon").removeClass(s).html(t[1])
                                }), n[0].checked = !0, a.addClass(e + "ed"), a.find(".layui-icon").addClass(s).html(t[0]), layui.event.call(n[0], l, "radio(" + u + ")", {
                                    elem: n[0],
                                    value: n[0].value,
                                    othis: a
                                }))
                            })
                        };
                    a.each(function (a, l) {
                        var r = i(this),
                            s = r.next("." + e),
                            o = this.disabled;
                        if ("string" == typeof r.attr("lay-ignore")) return r.show();
                        s[0] && s.remove();
                        var u = i(['<div class="layui-unselect ' + e, l.checked ? " " + e + "ed" : "", (o ? " layui-radio-disbaled " + c : "") + '">', '<i class="layui-anim layui-icon">' + t[l.checked ? 0 : 1] + "</i>", "<div>" + function () {
                            var e = l.title || "";
                            return "string" == typeof r.next().attr("lay-radio") && (e = r.next().html(), r.next().remove()), e
                        }() + "</div>", "</div>"].join(""));
                        r.after(u), n.call(this, u)
                    })
                }
            };
        return e ? d[e] ? d[e]() : a.error("不支持的" + e + "表单渲染") : layui.each(d, function (e, i) {
            i()
        }), n
    };
    var d = function () {
            var e = i(this),
                a = f.config.verify,
                s = null,
                o = "layui-form-danger",
                c = {},
                u = e.parents(r),
                d = u.find("*[lay-verify]"),
                v = e.parents("form")[0],
                h = u.find("input,select,textarea"),
                y = e.attr("lay-filter");
            if (layui.each(d, function (e, l) {
                var r = i(this),
                   
                    c = r.attr("lay-verify").split("|"),
                    u = r.attr("lay-verType"),
                    d = r.val();
                
                if (r.removeClass(o), layui.each(c, function (e, i) {
                	
                	
                	var p = "",
                       g= "";
                	if(i.indexOf(":")){
                		p = i.split(":");
                         g = p[1];
                         i = p[0]
                	}
                	if(d.length>0||i=='required'){
                		 var c, f = "",
                         v = "function" == typeof a[i];
                         
                         if (a[i]) {
                             var c = v ? f = a[i](d, g,1) : !a[i][0].test(d);
                             if (f = f || a[i][1], c) return "tips" === u ? t.tips(f, function () {
                                 return "string" == typeof r.attr("lay-ignore") || "select" !== l.tagName.toLowerCase() && !/^checkbox|radio$/.test(l.type) ? r : r.next()
                             }(), {
                                 tips: 1
                             }) : "alert" === u ? t.alert(f, {
                                 title: "提示",
                                 shadeClose: !0
                             }) : t.msg(f, {
                                 icon: 5,
                                 shift: 6
                             }), n.android || n.ios || l.focus(), r.addClass(o), s = !0
                         }
                	}
                   
                }), s) return s
            }), s) return !1;
            var p = {};
            return layui.each(h, function (e, i) {
                if (i.name = (i.name || "").replace(/^\s*|\s*&/, ""), i.name) {
                    if (/^.*\[\]$/.test(i.name)) {
                        var t = i.name.match(/^(.*)\[\]$/g)[0];
                        p[t] = 0 | p[t], i.name = i.name.replace(/^(.*)\[\]$/, "$1[" + p[t]+++"]")
                    }
                    /^checkbox|radio$/.test(i.type) && !i.checked || (c[i.name] = i.value)
                }
            }), layui.event.call(this, l, "submit(" + y + ")", {
                elem: this,
                form: v,
                field: c
            })
        },
        f = new u,
        v = i(document),
        h = i(window);
    f.render(), v.on("reset", r, function () {
        var e = i(this).attr("lay-filter");
        setTimeout(function () {
            f.render(null, e)
        }, 50)
    }), v.on("submit", r, d).on("click", "*[lay-submit]", d), e(l, f)
});