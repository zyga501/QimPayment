<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><s:property value="storeName" />
    </title>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <style type="text/css">
        <!--
        input[type="submit"], input[type="reset"], input[type="button"], button {
            -webkit-appearance: none;
        }

        body {
            -webkit-user-select: none;
            user-select: none;
            background-color: #EFEFEF
        }

        .Layer1 {
            width: 100%;
            z-index: 1;
            border-style: solid;
            border-width: 0px;
            border-color: #fff;
            border-bottom-color: #06af3f;
            top: 28px;
            text-align: center;
        }

        .STYLE3 {
            color: #06af3f;
            font-size: 22px;
        }

        .STYLE5 {
            color: #06af3f;
            font-size: 16px;
            padding: 0.5em 2.5em
        }

        .STYLE7 {
            font-size: 22px;
        }

        .STYLE8 {
            color: #06af3f;
            font-size: 16px;
        }

        .dv1  {
            margin: 10px 1px 12px 1px;
            padding: 11px 10px;
            border: 0px none #d7d7d7;
            font-size: 18px;
            background: #fff;
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            -webkit-box-orient: horizontal;
            -moz-box-orient: horizontal;
            -webkit-flex-direction: row;
            -ms-flex-direction: row;
            flex-direction: row;
            position: relative;
            z-index: 0
        }

        .dv2 {
            padding: 11px 10px;
            text-align: center
        }

        a, input, label {
            outline: 0;
            white-space: nowrap;
        }

        .amount {
            display: block;
            -webkit-box-flex: 1;
            -moz-box-flex: 1;
            -webkit-flex: 1 1 auto;
            -ms-flex: 1 1 auto;
            flex: 1 1 auto;
            z-index: 1;
            padding: 0;
            line-height: 1;
            color: #000;
            text-align: right;
            font-size: 22px;
            white-space: nowrap;
            border-left: 0px;
            border-top: 0px;
            border-right: 0px;
            border-bottom: 0px;
        }

        .amount::before {
            content: '\a5';
            margin-right: .1em
        }

        .but {
            -webkit-border-radius: 5px;
            border-radius: 5px;
            background-color: #06af3f;
            color: #FEFEFE;
            border: none;
            font-size: 18px;
            width: 100%;
            padding: 10px 6px;
        }

        .paynum {
            color: #FF0000;
            font-size: 20px;
            font-weight: bold;
        }

        -->
    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script>
        function getQcode() {
            document.getElementById("imgid").src = "./image/loading.gif";
            $.ajax({
                type: 'post',
                url: 'weixin/Pay!scanPay',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    var qr = qrcode(10, 'Q');
                    qr.addData(json.code_url);
                    qr.make();
                    var dom=document.createElement('DIV');
                    dom.innerHTML = qr.createImgTag();
                    clearimg();
                    $("#QRCode")[0].appendChild(dom);
                }
            })
        }
        function clearimg() {
            document.getElementById("imgid").src = "./image/nopic.png";
            $("#QRCode").html("");
        }

        function amount(th) {
            var regStrs = [
                ['^0(\\d+)$', '$1'],
                ['[^\\d\\.]+$', ''],
                ['\\.(\\d?)\\.+', '.$1'],
                ['^(\\d+\\.\\d{2}).+', '$1']
            ];
            for (i = 0; i < regStrs.length; i++) {
                var reg = new RegExp(regStrs[i][0]);
                th.value = th.value.replace(reg, regStrs[i][1]);
            }
            if (th.value == "") {
                $("#paynum").text("");
            }
            else {
                $("#paynum").text("￥" + th.value);
            }
            $("#total_fee").val(parseInt(th.value)*100);
        }  </script>
</head>
<body>
<form>
    <input type="hidden" id="id" name="id" value="<%=request.getSession().getAttribute("id")%>"/>
    <input type="hidden" id="ucode" name="ucode" value="<s:property value="userName" escape="false" />"/>
    <input type="hidden" id="body" name="body" value="<s:property value="storeName" />"/>
    <input type="hidden" id="product_id" name="product_id" value="<s:property value="storeName" />"/>
    <input type="hidden" id="total_fee" name="total_fee" value=""/>

    <div class="Layer1">
        <div align="center" class="STYLE3"></div>
        <div align="center" class="STYLE3">
            <img style="width:90px;height:90px;border-radius:8px"
                                                src="./merchant/Merchant!FetchLogo?id=<s:property value="subMerchantId" escape="false" />">
        </div>
        <div align="center" class="STYLE5"><s:property value="storeName" escape="false" />
            &nbsp;&nbsp;收银员：<s:property value="userName" escape="false" />
        </div>
    </div>
    <div class="Layer1">
        <div class="dv1">
	<span class="STYLE7">
  	  <label>消费总额: </label>
  	</span>
            <input type="text" name="productprice" id="productprice" class="amount" onkeyup="amount(this)"
                   onpaste="return false;" autocomplete="off" onchange="clearimg()" placeholder="单位：￥（元）"/>
        </div>
    </div>
    <div class="Layer1">
        <div class="dv2">
	<span class="STYLE7">
  	  <label>实付金额: </label>
  	</span> <label id="paynum" class="paynum"></label><br>
            <input type="button" class="but" id="butpaynum" onclick="getQcode()" value="提交生成二维码"/>
        </div>
        <img id="imgid" src="./image/nopic.png">
        <div id="QRCode"></div>
    </div>
</form>
</body>
</html>
