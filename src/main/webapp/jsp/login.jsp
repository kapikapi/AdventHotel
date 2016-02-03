<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
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


</head>
<body>
<h1>Login Page</h1>
<form action="authentication" method="post">
  <input type="hidden" name="actionName" value="authentication" />
  Login:
  <input type="text" name="login"/>
  <br>
  Password:
  <input type="password" name="password" required/>
  <br>
  <input type="submit" value="Submit" required/>

  <c:if test="${not empty auth_error}">
    <div style="color: red; font-weight: bold">
      Authentication failed: ${auth_error}
    </div>
  </c:if>
  <h4><a href=<c:url value="/registration"/>>Or register now</a></h4>

</form>
</body>
</html>
