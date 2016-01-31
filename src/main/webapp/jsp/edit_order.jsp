<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <input type="hidden" name="order_id" value="${order_id}">
    Number of places in room:
    <input type="number" name="number_people" value="${room_places}" required/>
    <br>
    Duration of stay:
    <br>
    From: <input type="date" name="date_in" value="${dateIn}" required/>
    <br>
    to: <input type="date" name="date_out" value="${dateOut}" required/>

    <br>
    Room class:
    <select name="class">
        <option value="1" <c:if test="${classOfComfort == 1}">selected</c:if>>1: Lux</option>
        <option value="2" <c:if test="${classOfComfort == 2}">selected</c:if>>2: Economy</option>
    </select>

    <br>
    <input type="submit" value="Edit">

    <c:if test="${not empty search_error}">
        <div style="color: red; font-weight: bold">
            Search failed: ${search_error}
        </div>
    </c:if>
    <c:if test="${not empty no_result}">
        <div style="font-weight: bold">
                ${no_result}
        </div>

    </c:if>
</form>
Back to <h4><a href=<c:url value="/user"/>>My page</a></h4>

<form action="<c:url value="authentication"/>" method="POST">
    <input type="submit" value="Log out">
    <input type="hidden" name="actionName" value="logout">
</form>

</body>
</html>
