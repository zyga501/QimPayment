<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
  <title>Weixin</title>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/jdpay/h5pay.js"></script>
  <script type="text/javascript">
    function orderquery() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/jdpay/Pay!queryPay',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
        }
      })
    }
    function microPay() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/jdpay/Pay!microPay',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
        }
      })
    }
    function h5Pay() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/jdpay/Pay!h5Pay',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          alert(data);
          if ('Y'== json.is_success){
            $("#paystr").val(JSON.stringify(json.paystr))
            alert( $("#paystr").val() );
            pay(eval("(" + $("#paystr").val() + ")"));
          }
        }
      })
    }
    function h5Pay22() {
      var sss =eval("(" + $("#paystr").val() + ")")
      alert(sss.merchantTradeAmount);
      pay(sss);
    }
    function calback() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/jdpay/Callback!codePay',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
        }
      })
    }
    function SpecialInteface() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/api/SpecialInteface!OrderInert',
        dataType: "json",
        data: $("form").serialize(),
        success: function (data) {
        }
      })
    }
    function scanPay() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/jdpay/Pay!tokenPay',
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
  </script>
</head>
<body>
<form class="form form-horizontal" >
  <input id="code" name="code" type="hidden" value="<%=request.getParameter("code")%>" />
  <input id="state" name="state" type="hidden" value="<%=request.getParameter("state")%>" />
  <table>
    <tr>
      <td>子商户员工号:</td>
      <td>
        <input type="text" id="id" name="id" value="1629719047000007"/>
      </td>
    </tr>
    <tr>
      <td>orderno:</td>
      <td>
        <input type="text" id="orderno" name="orderno" value="a25"/>
      </td>
    </tr>
    <tr>
      <td>商品描述:</td>
      <td>
        <input type="text" id="goodsname" name="goodsname" value="交易标题"/>
      </td>
    </tr>
    <tr><td>总金额:</td>
      <td>
        <input type="text" id="price" name="price" value="0.01"/>
      </td>
    </tr>
    <tr><td>二维码字符串:</td>
      <td>
        <input type="text" id="auth_code" name="auth_code" value="1629719047000007"/>
      </td>
      <td><textarea id="paystr" rows="6"></textarea></td>
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
        <input type="text" id="data" name="data" value=""/>
        <input name="sign" value="524d24e4f148df5cbef4c2e2c8fd84fb">
        <input type="hidden" name="roleid" value="bojin">
      </td>
    </tr>
    <tr>
      <td>
        <input type="button" onclick="microPay()" value="刷卡提交"/>
      </td>
      <td>
        <!--input type="button" onclick="scanPay()" value="扫码支付"/>
        <input type="button" onclick="calback()" value="calback"/>
        <input type="button" onclick="SpecialInteface()" value="SpecialInteface"/>
        <input type="button" onclick="orderquery()" value="orderquery"/>
        <input type="button" onclick="h5Pay22()" value="h5Pay22"/-->
        <input type="button" onclick="h5Pay()" value="h5Pay"/>
      </td>
    </tr>
  </table>
</form>
</body>
</html>