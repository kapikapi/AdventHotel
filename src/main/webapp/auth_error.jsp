<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 31.01.16
  Time: 17:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Authentication Error</title>
</head>
<body>
Please log in or register to make an order:
<br>
<h4><a href=<c:url value="/authentication"/>>Log in</a></h4>
<h4><a href=<c:url value="/registration"/>>Register</a></h4>
</body>
</html>
