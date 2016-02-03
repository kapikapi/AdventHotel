<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 02.02.16
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Room Search Results</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty isAdmin}">
        <c:choose>
            <c:when test="${not empty result_list}">
                <table>
                    <tr><th>Room id</th>
                        <th>Room number</th>
                        <th>Places</th>
                        <th>Class</th>
                        <th>Cost</th>
                    </tr>
                    <c:forEach items="${result_list}" var="order">
                        <tr>
                            <td>
                                <a href=<c:url value="room/${order.aptId}"/>>${order.number}</a>
                            </td>
                            <td>${order.places}</td>
                            <td><c:choose>
                                <c:when test="${order.classOfComfort == 1}">
                                    Lux
                                </c:when>
                                <c:otherwise>
                                    Economy
                                </c:otherwise>
                            </c:choose></td>
                            <td>${order.cost}$</td>
                            <td>
                                <form action="admin_order" method="get">
                                        <%--<input type="hidden" name="actionName" value="offer"/>--%>
                                    <input type="hidden" name="room_id" value="${order.aptId}">
                                    <input type="hidden" name="order_id" value="${order_id}">
                                    <input type="submit" value="Offer">
                                </form>

                            </td>

                        </tr>

                    </c:forEach>
                </table>
                <c:choose>
                    <c:when test="${empty error}">
                        <c:if test="${currentPage != 1}">
                            <td><a href="search_room?page=${currentPage - 1}">Previous</a></td>
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
                                            <td><a href="search_room?page=${i}">${i}</a></td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </tr>
                        </table>

                        <%--For displaying Next link --%>
                        <c:if test="${currentPage lt noOfPages}">
                            <td><a href="search_room?page=${currentPage + 1}">Next</a></td>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        ${error}
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                ${no_result}
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        Sorry, you can not visit this page.
        <h4><a href=<c:url value="/user"/>>My page</a></h4>
    </c:otherwise>
</c:choose>
</body>
</html>
