<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 24.01.16
  Time: 19:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Result</title>
</head>
<>
<c:if test="${not empty result_list}">

  <table>
  <tr>
  <th>Room number</th>
  <th>Places</th>
  <th>Class</th>
  <th>Cost</th>
  </tr>
  <c:forEach items="${result_list}" var="room">
    <tr>

      <td><a href=<c:url value="room/${room.id}"/>>${room.number}</a>
        <input type="hidden" name="id" value="${room.id}">
      </td>
      <td>${room.places}</td>
      <td>${room.classOfComfort}</td>
      <td>${room.cost}</td>

      <td>
        <form action="bill" method="post">

          <input type="hidden" name="actionName" value="chooseRoom" />
          <input type="hidden" name="room_id" value="${room.id}">
          <input type="submit" value="Order"></form></a>
      </td>

    </tr>
  </c:forEach>
  </table>
  </c:if>

</body>
</html>