<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script language="javascript">
       window.location = "<%=request.getContextPath()%>/merchant/L!wx?code=<%=request.getParameter("code")%>&state=<%=request.getParameter("state")%>";
    </script>
</head>

<body>
</body>
</html>
