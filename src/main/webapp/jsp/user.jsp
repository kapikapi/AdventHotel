<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 25.01.16
  Time: 21:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Page</title>
</head>
<body>

<h2>${user_login}</h2>

<form action="user" method="post">
    <br>
    New order:
    <input type="hidden" name="actionName" value="newOrder">
    <input type="submit" value="Order room">
    <br>
</form>
My orders:
<c:choose>
    <c:when test="${empty no_result}">
        <br>
        <table>
            <tr>
                <th>Room number</th>
                <th>Places</th>
                <th>Class</th>
                <th>Cost</th>
                <th>Date of checking in</th>
                <th>Date of checking out</th>
                <th></th>
            </tr>
            <c:forEach items="${new_orders_list}" var="room">
                <tr>
                    <td>${room.number}</td>
                    <td>${room.places}</td>
                    <td><c:choose>
                        <c:when test="${room.classOfComfort == 1}">
                            Lux
                        </c:when>
                        <c:otherwise>
                            Economy
                        </c:otherwise>
                    </c:choose>
                    </td>
                    <td>${room.cost}</td>
                    <td>${room.dateIn}</td>
                    <td>${room.dateOut}</td>
                    <td>
                        <form action="edit_order" method="post">
                            <input type="hidden" name="order_id" value="${room.orderId}">
                            <input type="submit" value="Edit order">
                            <input type="hidden" name="actionName" value="editOrder">
                        </form>
                    </td>
                    <td>
                        <form action="remove_warning" method="post">
                            <input type="hidden" name="order_id" value="${room.orderId}">
                            <input type="submit" value="Remove order">
                            <input type="hidden" name="actionName" value="removeOrder">

                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br>
        Old orders:
        <table>
            <tr>
                <th>Room number</th>
                <th>Places</th>
                <th>Class</th>
                <th>Cost</th>
                <th>Date of checking in</th>
                <th>Date of checking out</th>
                <th></th>
            </tr>
            <c:forEach items="${old_orders_list}" var="room">
                <tr>
                    <td>${room.number}</td>
                    <td>${room.places}</td>
                    <td>
                        <c:choose>
                            <c:when test="${room.classOfComfort == 1}">
                                Lux
                            </c:when>
                            <c:otherwise>
                                Economy
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${room.cost}$</td>
                    <td>${room.dateIn}</td>
                    <td>${room.dateOut}</td>
                    <td>
                        <form action="remove_warning" method="post">
                            <input type="hidden" name="order_id" value="${room.orderId}">
                            <input type="submit" value="Remove order">
                            <input type="hidden" name="actionName" value="removeOrder">

                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </c:when>
    <c:otherwise>
        ${no_result}
    </c:otherwise>
</c:choose>

<form action="<c:url value="authentication"/>" method="POST">
    <br>
    <input type="submit" value="Log out">
    <input type="hidden" name="actionName" value="logout">
</form>

</body>
</html>
