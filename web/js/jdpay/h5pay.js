function a(d) {
    if (window.WebViewJavascriptBridge) {
        d(WebViewJavascriptBridge)
    } else {
        document.addEventListener("WebViewJavascriptBridgeReady",
            function() {
                d(WebViewJavascriptBridge)
            },
            false)
    }
};

function b() {
    this.a = navigator.userAgent;
    this.b = this.a.match(/(Android)\s+([\d.]+)/);
};
b.prototype = {
    constructor: b,
    getInfo: function(d) {
        var e = ("globalCallback" + Math.random()).replace(/\./, "");
        this.fnCallback(e, d);
        if (this.b) {
            android.getInfo(e)
        } else {
            this.openIframe("native://getInfo?callback=" + e)
        }
    },
    setTitle: function(d) {
        if (this.b) {
            android.setTitleName(d)
        } else {
            this.openIframe("native://setTitleName?titleName=" + d)
        }
    },
    alert: function(d) {
        var e = ("globalCallback" + Math.random()).replace(/\./, "");
        if (this.b) {
            android.alert(d)
        } else {
            this.fnCallback(e, null);
            this.openIframe("native://alert?text=" + d + "&callback=" + e)
        }
    },
    openIframe: function(d) {
        var e = document.createElement("iframe");
        e.src = d;
        e.style.visibility = "hidden";
        document.body.appendChild(e);
        setTimeout(function() {
                document.body.removeChild(e)
            },
            2000)
    },
    fnCallback: function(d, e) {
        window[d] = function(f) {
            e && e(f);
            delete window[d]
        }
    },
    pay: function(d) {
        var e = ("globalCallback" + Math.random()).replace(/\./, "");
        this.fnCallback(e, d.callback);
        delete d.callback;
        if (this.b) {
            android.pay(JSON.stringify(d.params), e)
        } else {
            this.openIframe("native://commonPay?params=" + JSON.stringify(d.params) + "&callback=" + e)
        }
    }
};
window.b = b;
var jdp = new b();
function pay(data){
    alert(JSON.stringify(data.merchantNotifyUrl));
    jdp.getInfo(function(json) {
        var datasjson =data;// eval("(" + data + ")");
        var json = JSON.parse(json);
        var data = {
            "merchantToken": datasjson.merchantToken,
            "merchantOuterOrderNum": datasjson.merchantOuterOrderNum,
            "merchantUserId": datasjson.merchantUserId,
            "merchantMobile": datasjson.merchantMobile,
            "merchantNum": datasjson.merchantNum,
            "merchantRemark":datasjson.merchantRemark,
            "merchantTradeNum":datasjson.merchantTradeNum,
            "merchantTradeName": datasjson.merchantTradeName,
            "merchantTradeDescription": datasjson.merchantTradeDescription,
            "merchantTradeTime": datasjson.merchantTradeTime,
            "merchantTradeAmount": datasjson.merchantTradeAmount,
            "merchantCurrency": "CNY",
            "merchantNotifyUrl": datasjson.merchantNotifyUrl,
            "merchantSign": datasjson.merchantSign,
            "data": datasjson.data,
            "cpTradeNum": datasjson.cpTradeNum
        };

        if (json.isLogin) {
            jdp.pay({
                params: data,
                callback: function(data) {
                    // 回调处理
                    jdp.alert(data);
                }
            })
        } else {
            jdp.alert('请先登录');
        }

    })

}