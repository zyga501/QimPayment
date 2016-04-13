<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><%=request.getSession().getAttribute("storename")%>
    </title>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <style type="text/css">
<!--
input[type="submit"],input[type="reset"],input[type="button"],button {
	-webkit-appearance: none;
}

body {
	-webkit-user-select: none;
	user-select: none;
	background-color: #EFEFEF
}

.Layer1 {
	width: 100%;
	z-index: 1;
	top: 28px;
}

.STYLE3 {
	color: #06af3f;
	font-size: 22px;
}

.STYLE5 {
	color: #06af3f;
	font-size: 16px;
}

.STYLE7 {
	font-size: 22px;
}

.STYLE8 {
	color: #06af3f;
	font-size: 16px;
	text-align: left;
}

.dv1 {
	margin: 12px 1px 12px 1px;
	border: 0px none #d7d7d7;
	padding: 11px 1px;
	font-size: 18px;
	background: #fff;
	display: -webkit-box;
	display: -moz-box;
	display: -ms-flexbox;
	display: -webkit-flex;
	display: flex;
	-webkit-box-orient: horizontal;
	-moz-box-orient: horizontal;
	-webkit-flex-direction: row;
	-ms-flex-direction: row;
	flex-direction: row;
	position: relative;
	z-index: 0
}

.dv2 {
	padding: 11px 1px;
	text-align: center
}

a,input,label {
	outline: 0;
	white-space: nowrap;
}

.amount {
	display: block;
	-webkit-box-flex: 1;
	-moz-box-flex: 1;
	-webkit-flex: 1 1 auto;
	-ms-flex: 1 1 auto;
	flex: 1 1 auto;
	z-index: 1;
	padding: 0;
	line-height: 1;
	color: #000;
	text-align: right;
	font-size: 22px;
	white-space: nowrap;
	border-left: 0px;
	border-top: 0px;
	border-right: 0px;
	border-bottom: 0px;
}

.amount::before {
	content: '\a5';
	margin-right: .1em
}

.but {
	width: 100%;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	background-color: #c8c8c8;
	color: #FEFEFE;
	border: none;
	font-size: 18px;
	padding: 10px 6px;
}

.paynum {
	color: #FF0000;
	font-size: 20px;
	font-weight: bold;
}
-->
</style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script language="javascript">
        function amount(th) {
            var regStrs = [
                ['^0(\\d+)$', '$1'],
                ['[^\\d\\.]+$', ''],
                ['\\.(\\d?)\\.+', '.$1'],
                ['^(\\d+\\.\\d{2}).+', '$1']
            ];
            for (i = 0; i < regStrs.length; i++) {
                var reg = new RegExp(regStrs[i][0]);
                th.value = th.value.replace(reg, regStrs[i][1]);
            }
            $("#total_fee").val(parseFloat(th.value)*100);
            if (th.value == "") {
                $("#paynum").text("");
            }
            else {
                $("#paynum").text("￥" + th.value);
            }
            if (th.value != "") {
                $("#butpaynum").css("background-color", "#06af3f");
                $("#butpaynum").removeAttr("disabled");
            }
            else {
                $("#butpaynum").css("background-color", "#c8c8c8");
                $("#butpaynum").attr("disabled", "disabled");
            }
        }
        function brandWCPay() {
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!brandWCPay',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    if (typeof (WeixinJSBridge) == "undefined") {
                        alert("请在微信客户端打开该网页");
                    }
                    else {
                        var json = eval("(" + data + ")");
                        WeixinJSBridge.invoke(
                                'getBrandWCPayRequest',
                                {
                                    "appId" : json.appId,           //公众号名称，由商户传入
                                    "timeStamp" : json.timeStamp,  //时间戳，自1970年以来的秒数
                                    "nonceStr" : json.nonceStr,    //随机串
                                    "package" : json.package,      //统一下单返回
                                    "signType" : json.signType,    //微信签名方式
                                    "paySign" : json.paySign       //微信签名
                                }
                                , function(result) {
                                    if (result.err_msg == "get_brand_wcpay_request:ok") {
                                        WeixinJSBridge.call('closeWindow');
                                    }
                                });
                    }
                }
            })
        }
    </script>
</head>

<body>
<input type="hidden" name="hideparam" id="hideparam" value=""/>

<form >
    <input type="hidden" value="<%=request.getParameter("state")%>" name="state"/>
    <input type="hidden" value="<%=request.getParameter("code")%>" name="code"/>
    <input type="hidden" value="<%=request.getParameter("storename")%>" name="body"/>
    <input type="hidden" value="" name="total_fee" id="total_fee"/>

    <div class="Layer122">
        <div align="center" class="STYLE3"></div>
        <br>

        <div align="center" class="STYLE3"><img style="width:90px;height:90px;border-radius:8px"
                                                src="<%=request.getContextPath()%>/merchant/Merchant!FetchLogo?id=<s:property value="subMerchantId" escape="false" />"></div>
        <br>

        <div align="center" class="STYLE5"><%=request.getSession().getAttribute("storename")%>
            &nbsp;&nbsp;<%="收银员：" + request.getSession().getAttribute("ucode")%>
        </div>
        <br>
    </div>
    <div class="Layer1">
        <div class="dv1">
	<span class="STYLE7">
  	  <label>消费总额: </label>
  	</span>
            <input type="text" name="paynum" class="amount" maxlength=10 onkeyup="amount(this)" onpaste="return false;"
                   autocomplete="off" placeholder="询问服务员后输入"/>
        </div>
    </div>
    <div class="Layer1">
        <div class="dv2">
	<span class="STYLE7">
  	  <label>实付金额: </label>
  	</span> <label id="paynum" class="paynum"></label><br><br>
            <input type="button" class="but" id="butpaynum"  disabled="disabled" value="微信支付" onclick="brandWCPay()"/>
        </div>
    </div>
</form>
</body>
</html>
