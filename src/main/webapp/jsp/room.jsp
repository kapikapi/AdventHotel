<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
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
<h1>Room №${room.number}</h1>

<form action="/admin_order" method="get">
    Living places: ${room.places}
    <br>
    Class of comfort: ${room.classOfComfort}
    <br>
    Cost for one day: ${room.cost}
    <br>
    Description: ${description}
    <c:if test="${not empty isAdmin}">
        <br>
        <input type="submit" value="Offer this room">
        <input type="hidden" name="room_id" value="${room.aptId}">
        <input type="hidden" name="order_id" value="${order.orderId}">
    </c:if>
</form>

<tags:logout userLogin="${user.login}"/>
</body>
</html>
