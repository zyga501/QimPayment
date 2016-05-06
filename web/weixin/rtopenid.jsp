<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script language="javascript">
      // window.location = "< % =request.getContextPath()%>/merchant/Merchant!wx?code=< % =request.getParameter("code")%>&state=< %=request.getParameter("state")%>";
       function onBridgeReady(){
           $.ajax({
               type: 'post',
               url: '<%=request.getContextPath()%>/merchant/Merchant!wx',
               dataType:"json",
               data:$("form").serialize(),
               success: function (data) {
                   WeixinJSBridge.call('closeWindow');
               }
           })
       }
       $(function (){
           onBridgeReady;
       })
    </script>
</head>
<body onload="onBridgeReady()">
<form >
    <input type="hidden" value="<%=request.getParameter("state")%>" name="state"/>
    <input type="hidden" value="<%=request.getParameter("code")%>" name="code"/>
    <input type="button" value="关闭" onclick="WeixinJSBridge.call('closeWindow');"/>
</form>
</body>
</html>
