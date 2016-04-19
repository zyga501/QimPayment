<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>PaymentFramework</title>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
  <script type="text/javascript">
    function microPay() {
      $.ajax({
        type: 'post',
        url: 'api/TestPay!microPay',
        dataType:"json",
        data:$("form").serialize() + '<xml>234243234243234</xml>',
        success: function (data) {
        }
      })
    }

    function scanPay() {
      $.ajax({
        type: 'post',
        url: 'api/TestPay!scanPay',
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

    function brandWCPay() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/api/TestPay!brandWCPay',
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
                        alert("购买成功！");
                      }
                    });
          }
        }
      })
    }

    function refund() {
      $.ajax({
        type: 'post',
        url: 'Pay!refund',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
        }
      })
    }

    function getInfoBySubMerchantId() {
      $.ajax({
        type: 'post',
        url: 'merchant/SubMerchantUser!getInfoByWeixinSubMerchantId',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          alert(json);
        }
      })
    }

    function regsiterSubMerchantInfo() {
      $.ajax({
        type: 'post',
        url: 'merchant/SubMerchant!regsiterSubMerchantInfo',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          alert(json);
        }
      })
    }

    function updateUserWeixinIdByUId() {
      $.ajax({
        type: 'post',
        url: 'merchant/SubMerchantUser!updateWeixinIdById',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          alert(json);
        }
      })
    }

    function updateSubMerchantWeixinIdByUId() {
      $.ajax({
        type: 'post',
        url: 'merchant/SubMerchant!updateWeixinIdById',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          alert(json);
        }
      })
    }

    function updateStoreNameById() {
      $.ajax({
        type: 'post',
        url: 'merchant/SubMerchantUser!updateStoreNameById',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          alert(json);
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
      <td>支付方式:</td>
      <td>
        <input type="text" id="mode" name="mode" value="weixin"/>
      </td>
    </tr>
    <tr>
      <td>子商户员工号:</td>
      <td>
        <input type="text" id="id" name="id" value="1596144387655680"/>
      </td>
    </tr>
    <tr>
      <td>商品描述:</td>
      <td>
        <input type="text" id="body" name="body" value="商品"/>
      </td>
    </tr>
    <tr><td>总金额:</td>
      <td>
        <input type="text" id="total_fee" name="total_fee" value="1"/>
      </td>
    </tr>
    <tr><td>二维码字符串:</td>
      <td>
        <input type="text" id="auth_code" name="auth_code"/>
      </td>
    </tr>
    <tr><td>产品序列号:</td>
      <td>
        <input type="text" id="product_id" name="product_id"/>
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
    <tr><td>微信子商户ID:</td>
      <td>
        <input type="text" id="sub_mch_id" name="sub_mch_id"/>
      </td>
    </tr>
    <tr><td>店名:</td>
      <td>
        <input type="text" id="storeName" name="storeName" value="狗皮牌狗皮膏药"/>
      </td>
    </tr>
    <tr><td>地址:</td>
      <td>
        <input type="text" id="address" name="address" value="没地址"/>
      </td>
    </tr>
    <tr><td>商户内部ID:</td>
      <td>
        <input type="text" id="merchantId" name="merchantId" value="1596082254858240"/>
      </td>
    </tr>
    <tr><td>微信Id:</td>
      <td>
        <input type="text" id="weixinId" name="weixinId"/>
      </td>
    </tr>
    <tr>
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
      <td>
        <input type="button" onclick="refund()" value="申请退款"/>
      </td>
      <td>
        <input type="button" onclick="getInfoBySubMerchantId()" value="获取子商户信息"/>
      </td>
      <td>
        <input type="button" onclick="regsiterSubMerchantInfo()" value="注册子商户"/>
      </td>
    <td>
      <input type="button" onclick="updateUserWeixinIdById()" value="更新员工微信号"/>
    </td>
    <td>
      <input type="button" onclick="updateSubMerchantWeixinIdByUId()" value="更新商户微信号"/>
    </td>
    <td>
      <input type="button" onclick="updateStoreNameById()" value="更新店名"/>
    </td>
  </tr>
  </table>
</form>
</body>
</html>