<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
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
<h1>Final Cost</h1>
<c:choose>
    <c:when test="${empty error}">
        Final cost for room
        №${room.number},
        <c:choose>
            <c:when test="${order.classOfComfort == 1}">
                Lux
            </c:when>
            <c:otherwise>
                Economy
            </c:otherwise>
        </c:choose> class
        from ${order.dateIn} to ${order.dateOut}
        for ${order.places} people
        is ${order.cost}$.
        <br>
        Please, submit payment:
        <br>

        <form action="bill" method="post">
            <input type="hidden" name="actionName" value="bill">
            <input type="submit" value="Submit payment for room">
        </form>
        <br>
    </c:when>
    <c:otherwise>
        ${error}
    </c:otherwise>
</c:choose>
<br>
You can view all your orders on your user page:
<br>
<a href=<c:url value="user"/>>My page</a>

<tags:logout userLogin="${user.login}"/>
</body>
</html>
