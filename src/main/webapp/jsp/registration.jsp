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
<h1>Registration Page</h1>
<form action="registration" method="post">
  <input type="hidden" name="actionName" value="registration" />
    Name:
    <input type="text" name="name"/>
    <br>
  E-mail:
  <input type="text" name="email" required/>
  <br>
  Login:
  <input type="text" name="login" required/>
  <br>
  Password:
  <input type="password" name="password"/>
  <br>
  <input type="submit" value="Submit">
    <br>

  <c:if test="${not empty reg_error}">
      <div style="color: red; font-weight: bold">
    Registration failed: ${reg_error}
      </div>
  </c:if>

  <h4><a href=<c:url value="/authentication"/>>Log in</a></h4> if you are already registered.
</form>
</body>
</html>
