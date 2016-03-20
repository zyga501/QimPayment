<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>Weixin</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
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
    </script>
  </head>
  <body>
    <form class="form form-horizontal" >
      <table>
        <tr>
          <td>公众账号ID:</td>
          <td>
            <input type="text" id="appid" name="appid" value="wxbc6cb3102836e118"/>
          </td>
        </tr>
        <tr>
          <td>商户号:</td>
          <td>
            <input type="text" id="mch_id" name="mch_id" value="1241847602"/>
          </td>
        </tr>
        <tr>
          <td>子商户号:</td>
          <td>
            <input type="text" id="sub_mch_id" name="sub_mch_id" value="1294045001"/>
          </td>
        </tr>
        <tr>
          <td>APPSECRET:</td>
          <td>
            <input type="text" id="appsecret" name="appsecret" value="9778c54c04ff5d14f7f9c491badbc60b"/>
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
          </td>
        </tr>
        <tr>
          <td>
            <input type="button" onclick="microPay()" value="刷卡提交"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
