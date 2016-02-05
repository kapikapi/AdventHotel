<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 19.01.16
  Time: 0:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order Form</title>
</head>
<body>
<div class="main-content">
    <div class="head-content">
        Orders Page
        <tags:logout userLogin="${user.login}" userName="${user.name}"/>
        </div>
<form action="order" method="post">
    <input type="hidden" name="actionName" value="order"/>
    Number of places in room:
    <input type="number" name="places" min="1" max="4" required/>
    <br>
    Duration of stay:
    <br>
    From: <input type="date" name="date_in" required/>
    <br>
    to: <input type="date" name="date_out" required/>

    <br>
    Room class:
    <select name="class">
        <option value="1">1: Lux</option>
        <option value="2">2: Economy</option>
    </select>
    <br>
    Comment:
    <textarea name="comment"></textarea>
    <br>
    <input type="submit" value="Submit">

    <c:if test="${not empty order_error}">
        <div style="color: red; font-weight: bold">
            Making order failed: ${order_error}
        </div>
    </c:if>
</form>
</div>
</body>
</html>
