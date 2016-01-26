<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 26.01.16
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Editing Order</title>
</head>
<body>
<form action="edit_order" method="post">
  <input type="hidden" name="actionName" value="editOrder"/>
  Number of places in room:
  <input type="number" name="number_people" value="${room_places}"/>
  <br>
  Duration of stay:
  <br>
  From: <input type="date" name="date_in" value="${dateIn}"/>
  <br>
  to: <input type="date" name="date_out" value="${dateOut}"/>

  <br>
  Room class:
  <select name="class">
    <option value="1">1: Lux</option>
    <option value="2">2: Economy</option>
  </select>

  <br>
  <input type="submit" value="Edit">
</body>
</html>
