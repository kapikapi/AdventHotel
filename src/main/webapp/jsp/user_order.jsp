<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/usernameDescriptor.tld" prefix="username"%>
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
    <title>Order Page</title>
</head>
<body>
Order details: <br>
Order id: ${order.orderId} <br>
Required places: ${order.places} <br>
Required class:
<c:choose>
    <c:when test="${order.classOfComfort == 1}">
        Lux
    </c:when>
    <c:otherwise>
        Economy
    </c:otherwise>
</c:choose>
<br>
Dates: from ${order.dateIn} to ${order.dateOut} <br>
Status: ${order.status}<br>

<c:choose>
    <c:when test="${order.status == 'REQUESTED'}">
        Please, wait until administrator offers you the best room.
    </c:when>
    <c:when test="${order.status == 'IN_DISCUSSION'}">
        Offered apartments id: ${apt.number}
        <h4><a href=<c:url value="/room/${apt.aptId}"/>>View room details</a></h4>

        <form action="user_order" method="get">
            <input type="submit" value="Approve">
            <input type="hidden" name="order_id" value="${order.orderId}">
            <input type="hidden" name="actionName" value="approve">
        </form>
    </c:when>
    <c:when test="${order.status == 'APPROVED'}">
        Bill for this order is sent to you.
        Final cost for room №${apt.number} from ${order.dateIn} to ${order.dateOut} is ${order.cost}$<br>
        You can pay for it now:
        <form action="bill" method="get">
            <input type="submit" value="Pay">
        </form>
    </c:when>
    <c:when test="${order.status == 'PAID'}">
        This order is paid. Thank you for choosing Advent Hotel.
    </c:when>
    <c:when test="${order.status == 'REJECTED'}">
        This order is rejected.
    </c:when>

</c:choose>
${error}
<c:if test="${order.status != 'REJECTED' and order.status != 'PAID' and order.status != 'APPROVED'}">
    <form action="edit_order" method="post">
        <%--<input type="hidden" name="order_id" value="${order.orderId}">--%>
        <input type="submit" value="Edit order">
        <input type="hidden" name="actionName" value="editOrder">
    </form>
</c:if>

<c:if test="${order.status != 'REJECTED'}">
    <form action="reject_warning" method="post">
            <%--<input type="hidden" name="order_id" value="${order.orderId}">--%>
        <input type="submit" value="Reject Order">
        <input type="hidden" name="actionName" value="reject">
    </form>
</c:if>
<h4><a href=<c:url value="/user"/>>My page</a></h4>


<form action="user_order" method="post">
    Send comment to this order: <br>
    <textarea name="comment"></textarea> <br>
    <input type="hidden" name="actionName" value="send_comment">
    <input type="submit" value="Leave comment">
</form>
<br>
By leaving comments to this order you can communicate with administrator.
<br>
Comments to this order:
<br>
<c:if test="${not empty no_comments}">
No comments to this order.
</c:if>
<c:forEach  items="${comment_list}" var="comment">
<username:usernameTagHandler userId="${comment.userId}"/> ${comment.text} <br>
</c:forEach>

<c:if test="${currentPage != 1}">
    <td><a href="user_order?page=${currentPage - 1}">Previous</a></td>
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
                    <td><a href="user_order?page=${i}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </tr>
</table>

<%--For displaying Next link --%>
<c:if test="${currentPage lt noOfPages}">
    <td><a href="user_order?page=${currentPage + 1}">Next</a></td>
</c:if>
<form action="<c:url value="authentication"/>" method="POST">
    <br>
    <input type="submit" value="Log out">
    <input type="hidden" name="actionName" value="logout">
</form>
</body>
</html>
