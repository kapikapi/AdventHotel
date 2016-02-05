<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 02.02.16
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order Administration Page</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty isAdmin}">
        Order details: <br>
        Order id: ${order.orderId} <br>
        User id: ${order.userId} <br>
        Required: places ${order.places}, class ${order.classOfComfort} <br>
        Dates: from ${order.dateIn} to ${order.dateOut} <br>
        Status: ${order.status}

        <br>
        <c:choose>
            <c:when test="${order.status == 'REQUESTED'}">
                <form action="admin_order" method="post">
                    <input type="submit" value="Find Room">
                    <input type="hidden" name="actionName" value="find_room">
                </form>
            </c:when>
            <c:when test="${order.status == 'IN_DISCUSSION'}">
                Offered apartments id: ${order.orderedAptId}
                <form action="admin_order" method="post">
                    <input type="submit" value="Find Another Room">
                    <input type="hidden" name="actionName" value="find_room">
                </form>
            </c:when>
            <c:when test="${order.status == 'APPROVED'}">
                Apartment id: ${order.orderedAptId}
                Bill for this order is sent. Waiting for payment.
            </c:when>
            <c:when test="${order.status == 'PAID'}">
                This order is paid.
            </c:when>
            <c:when test="${order.status == 'REJECTED'}">
                This order is rejected.
            </c:when>

        </c:choose>
        ${error}
        <c:if test="${order.status != 'REJECTED'}">
            <form action="reject_warning" method="post">
                <input type="hidden" name="order_id" value="${order.orderId}">
                <input type="submit" value="Reject Order">
                <input type="hidden" name="actionName" value="reject">
            </form>
        </c:if>

        <h4><a href=<c:url value="/admin"/>>My page</a></h4>

        <form action="admin_order" method="post">
            Send comment to this order: <br>
            <textarea name="comment"></textarea>
            <input type="hidden" name="actionName" value="send_comment">
            <br>
            <input type="submit" value="Leave comment">
        </form>
        <br>
        Comments to this order:
        <br>
        <c:choose>
            <c:when test="${not empty no_comments}">
                No comments to this order.
            </c:when>
            <c:otherwise>
                <c:forEach items="${comment_list}" var="comment">
                    Username : ${comment.text} <br>
                </c:forEach>

                <c:if test="${currentPage != 1}">
                    <td><a href="admin_order?page=${currentPage - 1}">Previous</a></td>
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
                                    <td><a href="admin_order?page=${i}">${i}</a></td>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </tr>
                </table>

                <%--For displaying Next link --%>
                <c:if test="${currentPage lt noOfPages}">
                    <td><a href="admin_order?page=${currentPage + 1}">Next</a></td>
                </c:if>
            </c:otherwise>
        </c:choose>
        <tags:logout userLogin="${user.login}"/>
    </c:when>
    <c:otherwise>
        Sorry, you can not visit this page.
        <h4><a href=<c:url value="/user"/>>My page</a></h4>
    </c:otherwise>
</c:choose>
</body>
</html>
