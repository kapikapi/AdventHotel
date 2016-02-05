<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: kapikapi
  Date: 26.01.16
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Warning before remove</title>
</head>
<body>
<form action="remove_warning" method="post">
    Please, confirm deleting this order:
    <input type="submit" value="I confirm">
    <input type="hidden" name="actionName" value="confirmed">
    <%--<input type="hidden" name="order_id" value="${order_id}">--%>
    <h4><a href=<c:url value="/user"/>>Back</a></h4>
</form>
<c:if test="${not empty error}">
    ${error}
</c:if>
<tags:logout userLogin="${user.login}"/>
</body>
</html>
