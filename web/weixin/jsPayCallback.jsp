<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>微信安全支付中心
    </title>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0">

</head>
<body  >
<input type="hidden" name="hideparam" id="hideparam" value=""/>
<form >
    <input type="hidden" value="<%=request.getParameter("state")%>" name="state"/>
    <input type="hidden" value="<%=request.getParameter("code")%>" name="code"/>
    <input type="hidden" value="${data}" name="data"/>
</form><center><h3 style="color: #06af3f">正在支付,请稍后...</h3>
<img id="imgid" src="<%=request.getContextPath()%>/image/loading.gif"></center>
</body >
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
<script>
    function onBridgeReady(){
        $.ajax({
            type: 'post',
            url: '<%=request.getContextPath()%>/weixin/Pay!brandWCPay',
            dataType:"json",
            data:$("form").serialize(),
            success: function (data) {
                var json=eval("("+data+")");
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
        })
    }
    $(function (){
        if (typeof (WeixinJSBridge) == "undefined") {
            if( document.addEventListener ){
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            }else if (document.attachEvent){
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        }
        else { onBridgeReady();
        }
    })
</script>
</html>
