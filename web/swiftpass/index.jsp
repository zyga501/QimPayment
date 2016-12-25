<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>SwiftPass</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript">
      function weixinJsPay() {
        $.ajax({
          type: 'post',
          url: '<%=request.getContextPath()%>/swiftpass/Pay!weixinJsPay',
          dataType:"json",
          data:$("form").serialize(),
          success: function (data) {
          }
        })
      }

      function aliJsPay() {
        $.ajax({
          type: 'post',
          url: '<%=request.getContextPath()%>/swiftpass/Pay!aliJsPay',
          dataType:"json",
          data:$("form").serialize(),
          success: function (data) {
          }
        })
      }

      function weixinNative() {
        $.ajax({
          type: 'post',
          url: '<%=request.getContextPath()%>/swiftpass/Pay!weixinNative?id=1596144387655680',
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
      <input id="code" name="code" type="hidden" value="<%=request.getParameter("code")%>" />
      <input id="state" name="state" type="hidden" value="<%=request.getParameter("state")%>" />
      <table>
        <tr>
          <td>商品描述:</td>
          <td>
            <input type="text" id="body" name="body" value="测试商品"/>
          </td>
        </tr>
        <tr><td>总金额:</td>
          <td>
            <input type="text" id="total_fee" name="total_fee" value="1"/>
          </td>
        </tr>
        <tr>
          <td>
            <input type="button" onclick="weixinJsPay()" value="微信公众号支付"/>
          </td>
          <td>
            <input type="button" onclick="aliJsPay()" value="支付宝统一下单"/>
          </td>
          <td>
            <input type="button" onclick="weixinNative()" value="微信扫码支付"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
