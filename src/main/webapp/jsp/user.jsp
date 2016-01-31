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
            <c:if test="${not empty old_orders_list}">
                <tr>
                    <td>Old orders</td>
                </tr>
            </c:if>
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


        <c:if test="${currentPage != 1}">
            <td><a href="user?page=${currentPage - 1}">Previous</a></td>
        </c:if>

        <%--For displaying Page numbers.
        The when condition does not display a link for the current page--%>
        <table border="1" cellpadding="5" cellspacing="5">
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

        <%--<c:set var="totalCount" scope="session" value="${new_list_size+old_list_size}"/>--%>
        <%--<c:set var="perPage" scope="session" value="${perPage}"/>--%>
        <%--<c:set var="totalPages" scope="session" value="${totalCount/perPage}"/>--%>
        <%--<c:set var="pageIndex" scope="session" value="${param.start/perPage+1}"/>--%>

        <%--<c:if test="${!empty param.start && param.start >(perPage-1) && param.start !=0 }">--%>
            <%--<a href="user?start=<c:out value="${param.start - perPage}"/>">Previous</a>--%>
        <%--</c:if>--%>

        <%--<c:forEach var="boundaryStart" varStatus="status" begin="0" end="${totalCount - 1}" step="${perPage}">--%>
            <%--<c:choose>--%>
                <%--<c:when test="${status.count>0 && status.count != pageIndex}">--%>
                    <%--<a href="?start=<c:out value='${boundaryStart}'/>">--%>
                        <%--<c:out value="${status.count}"/> |--%>
                    <%--</a>--%>
                <%--</c:when>--%>
                <%--<c:otherwise>--%>
                    <%--<c:out value="${status.count}"/> |--%>
                <%--</c:otherwise>--%>

            <%--</c:choose>--%>
        <%--</c:forEach>--%>

        <%--<c:if test="${empty param.start || param.start<(totalCount-perPage)}">--%>
            <%--<a href="user?start=<c:out value="${param.start + perPage}"/>">Next </a>--%>
        <%--</c:if>--%>
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
