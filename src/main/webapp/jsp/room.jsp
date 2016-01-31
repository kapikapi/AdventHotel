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
<h1>Room №${room.number}</h1>

<form action="bill" method="post">
    Living places: ${room.places}
    <br>
    Class of comfort: ${room.classOfComfort}
    <br>
    Cost for one day: ${room.cost}
    <br>
    <input type="submit" value="Order this room">
    <input type="hidden" name="room_id" value="${room.id}">
    <input type="hidden" name="actionName" value="search">
</form>
<form action="<c:url value="authentication"/>" method="POST">
    <input type="submit" value="Log out">
    <input type="hidden" name="actionName" value="logout">
</form>

</body>
</html>
