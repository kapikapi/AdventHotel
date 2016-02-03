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

<h2>${user_name} (${user_login})</h2>

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
        <table cellspacing="10">
            <tr>
                <th>Places</th>
                <th>Class</th>
                <th>Date of checking in</th>
                <th>Date of checking out</th>
                <th>Status</th>
                <th>Cost</th>
                <th></th>
                <th></th>

            </tr>
            <c:forEach items="${new_orders_list}" var="order">
                <tr>
                    <td>${order.places}</td>
                    <td><c:choose>
                        <c:when test="${order.classOfComfort == 1}">
                            Lux
                        </c:when>
                        <c:otherwise>
                            Economy
                        </c:otherwise>
                    </c:choose>
                    </td>
                    <td>${order.dateIn}</td>
                    <td>${order.dateOut}</td>

                    <td>${order.status}</td>
                    <td>${order.cost}</td>
                    <td>
                        <form action="user_order" method="get">
                            <input type="hidden" name="order_id" value="${order.orderId}">
                            <input type="submit" value="Order Page">
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${not empty old_orders_list}">
                <tr>
                    <td>Old orders</td>
                </tr>
            </c:if>
            <c:forEach items="${old_orders_list}" var="order">
                <tr>
                    <td>${order.places}</td>
                    <td>
                        <c:choose>
                            <c:when test="${order.classOfComfort == 1}">
                                Lux
                            </c:when>
                            <c:otherwise>
                                Economy
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>${order.dateIn}</td>
                    <td>${order.dateOut}</td>

                    <td>${order.status}</td>
                    <td>${order.cost}</td>
                    <td>
                        <form action="user_order" method="get">
                            <input type="hidden" name="order_id" value="${order.orderId}">
                            <input type="submit" value="Order Page">
                        </form>
                    </td>
                        <td>
                        <form action="remove_warning" method="post">
                                <%--<input type="hidden" name="order_id" value="${order.orderId}">--%>
                            <input type="submit" value="Remove order">
                            <input type="hidden" name="actionName" value="removeOrder">
                        </form>
                        </td>
                </tr>
            </c:forEach>
        </table>


        <c:if test="${currentPage != 1}">
            <td><a href="user?page=${currentPage - 1}">Previous</a></td>
        </c:if>

        <%--For displaying Page numbers.
        The when condition does not display a link for the current page--%>
        <table border="1" cellpadding="3" cellspacing="5">
            <tr>
                <c:forEach begin="1" end="${noOfPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <td>${i}</td>
                        </c:when>
                        <c:otherwise>
                            <td><a href="user?page=${i}">${i}</a></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </table>

        <%--For displaying Next link --%>
        <c:if test="${currentPage lt noOfPages}">
            <td><a href="user?page=${currentPage + 1}">Next</a></td>
        </c:if>

    </c:when>
    <c:otherwise>
        ${no_result}
    </c:otherwise>
</c:choose>

<c:if test="${not empty isAdmin}">
    <h4><a href=<c:url value="/admin"/>>My page as admin</a></h4>
</c:if>
<form action="<c:url value="authentication"/>" method="POST">
    <br>
    <input type="submit" value="Log out">
    <input type="hidden" name="actionName" value="logout">
</form>

</body>
</html>
