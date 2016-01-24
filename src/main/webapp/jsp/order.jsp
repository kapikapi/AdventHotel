<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
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
<h1>Order Page</h1>
<c:choose>
<c:when test="${not empty user}">
<c:if test="${empty result_list}">
<form action="order" method="get">
  <input type="hidden" name="actionName" value="order" />
  Number of places in room:
  <input type="number" name="number_people"/>
  <br>
  Duration of stay:
  <br>
  From: <input type="date" name="date_in" />
  <br>
  to: <input type="date" name="date_out" />

  <br>
  Room class:
  <select name = "class">
    <option value = "1">1: Lux</option>
    <option value = "2">2: Economy</option>
  </select>

  <br>
  <input type="submit" value="Submit">

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
  </c:if></form>
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