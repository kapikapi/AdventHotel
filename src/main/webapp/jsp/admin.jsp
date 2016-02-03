<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 01.02.16
  Time: 21:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Administrators Page</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty isAdmin}">
        <h2>Welcome home, lovely admin!</h2>
        <table>
            <tr>
                <td>
                    <form action="admin" method="post">
                        <input type="hidden" name="actionName" value="all">
                        <input type="submit" value="All orders">
                    </form>
                </td>
                <td>
                    <form action="admin" method="post">
                        <input type="hidden" name="actionName" value="requested">
                        <input type="submit" value="Requested orders">
                    </form>
                </td>
                <td>
                    <form action="admin" method="post">
                        <input type="hidden" name="actionName" value="in_discussion">
                        <input type="submit" value="Orders in discussion">
                    </form>
                </td>
                <td>
                    <form action="admin" method="post">
                        <input type="hidden" name="actionName" value="approved">
                        <input type="submit" value="Approved orders">
                    </form>
                </td>
                <td>
                    <form action="admin" method="post">
                        <input type="hidden" name="actionName" value="paid">
                        <input type="submit" value="Paid orders">
                    </form>
                </td>
                <td>
                    <form action="admin" method="post">
                        <input type="hidden" name="actionName" value="rejected">
                        <input type="submit" value="Rejected orders">
                    </form>
                </td>
            </tr>
        </table>
        <h4>${heading}</h4>
        <c:choose>
            <c:when test="${empty no_result}">
                <br>
                <table cellspacing="10">
                    <tr>
                        <th>User Id</th>
                        <th>Places</th>
                        <th>Class</th>
                        <th>Date of checking in</th>
                        <th>Date of checking out</th>
                        <th>Apartment id to order</th>
                        <th>Additional information</th>
                        <th>Status</th>
                        <th>Cost</th>

                    </tr>
                    <c:forEach items="${orders_list}" var="order">
                        <tr>
                            <td>${order.userId}</td>
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
                            <td>${order.orderedAptId}</td>
                            <td>${order.additionalInfo}</td>
                            <td>${order.status}</td>
                            <td>${order.cost}</td>
                            <td>
                                <form action="admin_order" method="get">
                                    <input type="hidden" name="order_id" value="${order.orderId}">
                                    <input type="submit" value="Order Page">
                                </form>
                            </td>

                            <c:if test="${order.status=='REJECTED'}">
                                <td>
                                    <form action="remove_warning" method="post">
                                            <%--<input type="hidden" name="order_id" value="${order.orderId}">--%>
                                        <input type="submit" value="Remove order">
                                        <input type="hidden" name="actionName" value="removeOrder">
                                    </form>
                                </td>
                            </c:if>

                        </tr>
                    </c:forEach>
                </table>


                <c:if test="${currentPage != 1}">
                    <td><a href="admin?page=${currentPage - 1}">Previous</a></td>
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
                                    <td><a href="admin?page=${i}">${i}</a></td>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </tr>
                </table>

                <%--For displaying Next link --%>
                <c:if test="${currentPage lt noOfPages}">
                    <td><a href="admin?page=${currentPage + 1}">Next</a></td>
                </c:if>

            </c:when>
            <c:otherwise>
                ${no_result}
            </c:otherwise>
        </c:choose>
        <h4><a href=<c:url value="/user"/>>My page as user</a></h4>
        <c:if test="${not empty error}">${error}</c:if>
        <form action="<c:url value="authentication"/>" method="POST">
            <br>
            <input type="submit" value="Log out">
            <input type="hidden" name="actionName" value="logout">
        </form>
    </c:when>

    <c:otherwise>
        Sorry, you can not visit this page.
        <h4><a href=<c:url value="/user"/>>My page</a></h4>
    </c:otherwise>
</c:choose>
</body>
</html>
