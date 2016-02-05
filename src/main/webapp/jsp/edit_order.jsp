<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
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

<h1>Edit Order</h1>

<form action="edit_order" method="post">
    <input type="hidden" name="actionName" value="submit_edit"/>
    Number of places in room:
    <input type="number" name="places" value="${order.places}" min="1" max="4" required/>
    <br>
    Duration of stay:
    <br>
    From: <input type="date" name="date_in" value="${order.dateIn}" required/>
    <br>
    to: <input type="date" name="date_out" value="${order.dateOut}" required/>

    <br>
    Room class:
    <select name="class">
        <option value="1" <c:if test="${order.classOfComfort == 1}">selected</c:if>>1: Lux</option>
        <option value="2" <c:if test="${order.classOfComfort == 2}">selected</c:if>>2: Economy</option>
    </select>
    <br>
    Comment:
    <input type="text" name="comment" style="width:100px; height:40px;"/>
    <br>
    <input type="submit" value="Edit">


    <c:if test="${not empty order_error}">
        <div style="color: red; font-weight: bold">
            Making order failed: ${order_error}
        </div>
    </c:if>

</form>
Back to <h4><a href=<c:url value="/user"/>>My page</a></h4>

<tags:logout userLogin="${user.login}"/>
</body>
</html>
