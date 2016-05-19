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
          url: 'Pay!tradePay',
          dataType:"json",
          data:$("form").serialize(),
          success: function (data) {
          }
        })
      }

      function scanPay() {
        $.ajax({
          type: 'post',
          url: 'Pay!tradePreCreate',
          dataType:"json",
          data:$("form").serialize(),
          success: function (data) {
            var json = eval("(" + data + ")");
            var qr = qrcode(10, 'Q');
            qr.addData(json.qr_code);
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
            <input type="text" id="id" name="id" value="1596144387655680"/>
          </td>
        </tr>
        <tr>
          <td>商品描述:</td>
          <td>
            <input type="text" id="subject" name="subject" value="测试商品"/>
          </td>
        </tr>
        <tr><td>总金额:</td>
          <td>
            <input type="text" id="total_amount" name="total_amount" value="0.01"/>
          </td>
        </tr>
        <tr><td>二维码字符串:</td>
          <td>
            <input type="text" id="auth_code" name="auth_code"/>
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
        </tr>
      </table>
    </form>
  </body>
</html>
