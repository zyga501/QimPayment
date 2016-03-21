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
            alert(json.code_url);
            var qr = qrcode(10, 'Q');
            qr.addData(json.code_url);
            qr.make();
            alert($("#scanPayCode"));
            $("#scanPayCode").src = qr.toSource();
          }
        })
      }
    </script>
  </head>
  <body>
    <form class="form form-horizontal" >
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
          <td>APPSECRET:</td>
          <td>
            <input type="text" id="appsecret" name="appsecret" value="1307537901DING1307537901DING1234"/>
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
        <tr><td>价格:</td>
          <td>
            <input type="text" id="productFee" name="productFee"/>
          </td>
        </tr>
        <tr><td>二维码:</td>
          <td>
            <input type="text" id="auth_code" name="auth_code"/>
            <img id="scanPayCode" />
          </td>
        </tr>
        <tr>
          <td>
            <input type="button" onclick="microPay()" value="刷卡提交"/>
          </td>
          <td>
            <input type="button" onclick="scanPay()" value="扫码支付"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
