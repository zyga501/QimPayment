<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>ChanPay</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript">
      function singlePay() {
        $.ajax({
          type: 'post',
          url: '<%=request.getContextPath()%>/chanpay/Pay!singlePay',
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
        <tr><td>总金额:</td>
          <td>
            <input type="text" id="amount" name="amount" value="1"/>
          </td>
        </tr>
        <tr>
          <td>
            <input type="button" onclick="singlePay()" value="单次支付"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
