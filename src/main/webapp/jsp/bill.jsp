<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 22.01.16
  Time: 19:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Final Cost Page</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty user}">
        <h1>Final Cost</h1>
        ${res_str}
        <form action="bill" method="post">
            <input type="hidden" name="room_id" value="${room_id}">
            <input type="hidden" name="actionName" value="bill">

            <input type="submit" value="Submit room order">

            <br>
            <c:if test="${not empty setError}">
                ${setError}
            </c:if>
        You can view all your orders on your user page:
        <br>
        <a href=<c:url value="user"/>>My page</a>
        </form>
        <form action="<c:url value="authentication"/>" method="POST">
            <input type="submit" value="Log out">
            <input type="hidden" name="actionName" value="logout">
        </form>
    </c:when>
    <c:otherwise>
        Please log in or register to make an order:
        <br>
        <h4><a href=<c:url value="/authentication"/>>Log in</a></h4>
        <h4><a href=<c:url value="/registration"/>>Register</a></h4>
    </c:otherwise>
</c:choose>
</body>
</html>
