<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 23.01.16
  Time: 16:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Room Details</title>
</head>
<body>
<c:choose>
  <c:when test="${not empty user}">
<h1>Room №${room.number}</h1>
<form method="post">
  Living places: ${room.places}
  <br>
  Class of comfort: ${room.classOfComfort}
  <br>
  Cost for one day: ${room.cost}
  <input type="submit" value="Confirm">
  <input type="hidden" name="room_id" value="${room.id}">
  </c:when>
  <c:otherwise>
    Please log in or register to see room details and make an order:
    <br>
    <h4><a href=<c:url value="/authentication"/>>Log in</a></h4>
    <h4><a href=<c:url value="/registration"/>>Register</a></h4>
  </c:otherwise>
  </c:choose>
</form>
</body>
</html>
