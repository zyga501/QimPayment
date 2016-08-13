<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>Bestpay</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript">
      function barcodePay() {
        $.ajax({
          type: 'post',
          url: '<%=request.getContextPath()%>/bestpay/Pay!barcodePay',
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
          <td>子商户员工号:</td>
          <td>
            <input type="text" id="id" name="id" value="1596144387655680"/>
          </td>
        </tr>
        <tr><td>总金额:</td>
          <td>
            <input type="text" id="total_amount" name="productAmt" value="1"/>
          </td>
        </tr>
        <tr><td>二维码字符串:</td>
          <td>
            <input type="text" id="barcode" name="barcode"/>
          </td>
        </tr>
        <tr>
          <td>
            <input type="button" onclick="barcodePay()" value="条码提交"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
