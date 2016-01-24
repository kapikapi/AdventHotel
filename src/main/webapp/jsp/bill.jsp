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
<h1>Final Cost</h1>
<c:choose>
  <c:when test="${not empty user}">
    <form action="bill" method="post">
    Final cost for room №${room.number} ${room.classOfComfort} class from ${room.dateIn} to ${room.dateOut} for
    ${room.places} people is ${room.cost}$
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
