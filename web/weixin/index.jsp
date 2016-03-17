<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>Weixin</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
    </script>
  </head>
  <body>
    <form class="form form-horizontal" >
      <table>
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
        <tr>
          <td>
            <input type="button" onclick="microPay()" value="刷卡提交"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
