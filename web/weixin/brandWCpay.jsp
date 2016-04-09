<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>Weixin</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript">
      function brandWCPay() {
        $.ajax({
          type: 'post',
          url: '<%=request.getContextPath()%>/weixin/Pay!brandWCPay',
          dataType:"json",
          data:{code:"<%=request.getSession().getAttribute("code")%>",state:"<%=request.getSession().getAttribute("state")%>"},
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
      $().ready(function () {
         brandWCPay();
      });
    </script>
  </head>
  <body>
  </body>
</html>
