<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 15.01.16
  Time: 16:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration Page</title>
</head>
<body>
<form action="registration" method="post">
  <input type="hidden" name="actionName" value="registration" />
  E-mail:
  <input type="text" name="email"/>
  <br>
  Login:
  <input type="text" name="login"/>
  <br>
  Password:
  <input type="password" name="password"/>
  <br>
  <input type="submit" value="Submit">
  <c:if test="${reg_error}">
    Registration failed!
  </c:if>
</form>
</body>
</html>
