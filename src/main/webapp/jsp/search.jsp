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
<body>

<c:if test="${not empty result_list or not empty curr_room}">

    <table>
        <tr>
            <th>Room number</th>
            <th>Places</th>
            <th>Class</th>
            <th>Cost</th>
        </tr>

        <c:if test="${not empty curr_room}">
            <tr>
                <td>
                    <a href=<c:url value="room/${curr_room.id}"/>>${curr_room.number}</a>
                </td>
                <td>${curr_room.places}</td>
                <td>${curr_room.classOfComfort}</td>
                <td>${curr_room.cost}$</td>

                <td>
                    <form action="bill" method="post">
                        <input type="hidden" name="actionName" value="search"/>
                        <input type="hidden" name="room_id" value="${curr_room.id}">
                        <input type="submit" value="Order">
                    </form>

                </td>
                <td>
                    This is the room from your order under edit. You can also order it now.
                </td>
            </tr>
        </c:if>
        <c:forEach items="${result_list}" var="room">
            <tr>
                <td>
                    <a href=<c:url value="room/${room.id}"/>>${room.number}</a>
                </td>
                <td>${room.places}</td>
                <td>${room.classOfComfort}</td>
                <td>${room.cost}$</td>

                <td>
                    <form action="bill" method="post">
                        <input type="hidden" name="actionName" value="search"/>
                        <input type="hidden" name="room_id" value="${room.id}">
                        <input type="submit" value="Order">
                    </form>

                </td>

            </tr>

        </c:forEach>
    </table>
</c:if>
<form action="<c:url value="authentication"/>" method="POST">
    <input type="submit" value="Log out">
    <input type="hidden" name="actionName" value="logout">
</form>
</body>
</html>
