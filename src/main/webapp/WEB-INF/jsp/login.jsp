<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 13.01.16
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
  <h1>Login Page</h1>


</head>
<body>
<form action="authentication">
  Login:
  <input type="text" name="login"/>
  <br>
  Password:
  <input type="password" name="password"/>
  <input type="submit" value="submit">
    Or register now
    <h4><a href=<c:url value="/register"/>>Or register now</a></h4>
    <c:if test="${not auth_success}">
        Authentication failed. Wrong login or password.
    </c:if>

</form>
</body>
</html>
