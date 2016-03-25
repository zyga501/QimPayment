<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>Weixin</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript">
      function microPay() {
        $.ajax({
          type: 'post',
          url: 'Pay!microPay',
          dataType:"json",
          data:$("form").serialize(),
          success: function (data) {
          }
        })
      }
      function scanPay() {
        $.ajax({
          type: 'post',
          url: 'Pay!scanPay',
          dataType:"json",
          data:$("form").serialize(),
          success: function (data) {
            var json = eval("(" + data + ")");
            var qr = qrcode(10, 'Q');
            qr.addData(json.code_url);
            qr.make();
            var dom=document.createElement('DIV');
            dom.innerHTML = qr.createImgTag();
            $("#QRCode")[0].appendChild(dom);
          }
        })
      }

      function brandWCPay(json) {
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
                          alert("购买成功！");
                        }
                      });
            }
          }
        })
      }
    </script>
  </head>
  <body>
    <form class="form form-horizontal" >
      <input id="code" name="code" type="hidden" value="<%=request.getParameter("code")%>" />
      <input id="state" name="state" type="hidden" value="<%=request.getParameter("state")%>" />
      <table>
        <tr>
          <td>公众账号ID:</td>
          <td>
            <input type="text" id="appid" name="appid" value="wx0bfa8f7ec59b1f33"/>
          </td>
        </tr>
        <tr>
          <td>商户号:</td>
          <td>
            <input type="text" id="mch_id" name="mch_id" value="1307537901"/>
          </td>
        </tr>
        <tr>
          <td>子商户号:</td>
          <td>
            <input type="text" id="sub_mch_id" name="sub_mch_id" value="1319548401"/>
          </td>
        </tr>
        <tr>
          <td>APIKEY:</td>
          <td>
            <input type="text" id="apikey" name="apikey" value="1307537901DING1307537901DING1234"/>
          </td>
        </tr>
        <tr>
          <td>APPSECRET:</td>
          <td>
            <input type="text" id="appsecret" name="appsecret" value="9386215269d6eb50c14199089890050f"/>
          </td>
        </tr>
        <tr>
          <td>商品描述:</td>
          <td>
            <input type="text" id="productBody" name="productBody"/>
          </td>
        </tr>
        <tr>
          <td>商品详情:</td>
          <td><input type="text" id="productDetail" name="productDetail"/>
          </td>
        </tr>
        <tr><td>总金额:</td>
          <td>
            <input type="text" id="productFee" name="productFee"/>
          </td>
        </tr>
        <tr><td>二维码字符串:</td>
          <td>
            <input type="text" id="auth_code" name="auth_code"/>
          </td>
        </tr>
        <tr>
          <td>微信订单号:</td>
          <td>
            <input type="text" id="transaction_id" name="transaction_id"/>
          </td>
        </tr>
        <tr>
          <td>商户订单号:</td>
          <td>
            <input type="text" id="out_trade_no" name="out_trade_no"/>
          </td>
        </tr>
        <tr><td>退款金额:</td>
          <td>
            <input type="text" id="refund_fee" name="refund_fee"/>
          </td>
        </tr>
        <tr>
          <td>
          <div  id="QRCode">
          二维码
          </div>
          </td>
        </tr>
        <tr>
          <td>
            <input type="button" onclick="microPay()" value="刷卡提交"/>
          </td>
          <td>
            <input type="button" onclick="scanPay()" value="扫码支付"/>
          </td>
          <td>
            <input type="button" onclick="brandWCPay()" value="公众号支付"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
