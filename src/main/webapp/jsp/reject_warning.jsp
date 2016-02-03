<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 02.02.16
  Time: 13:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rejection Order Warning</title>
</head>
<body>
<form action="reject_warning" method="post">
  Please, confirm rejecting this order:
  <input type="submit" value="I confirm">
  <input type="hidden" name="actionName" value="confirmed">
  <%--<input type="hidden" name="order_id" value="${order_id}">--%>
  <h4><a href=<c:url value="/user"/>>Back</a></h4>
</form>
</body>
</html>
