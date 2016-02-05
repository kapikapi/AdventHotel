<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<c:choose>
    <c:when test="${locale == 'ru'}">
        <fmt:setLocale value="ru"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en"/>
    </c:otherwise>
</c:choose>

<fmt:setBundle basename="local"/>
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
    <fmt:message key="order.warning_header" var="remove_header"/>
    <title>${remove_header}</title>
    <link type="text/css" rel="stylesheet" href="css/main.css">
</head>
<body>
<div class="main-content">
    <div class="head-content">
        ${remove_header}
        <tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}"/>
        <tags:logout userLogin="${user.login}" userName="${user.name}" curr_lang="${locale}"/>
    </div>
    <div class="warning">
    <form action="remove_warning" method="post">
        <fmt:message key="order.remove.confirm_info" var="confirm_info"/>
        <fmt:message key="order.remove.confirm" var="confirm_submit"/>
        ${confirm_info}:
        <input type="submit" value="${confirm_submit}">
        <input type="hidden" name="actionName" value="confirmed">
        <fmt:message key="order.remove.back" var="back"/>
    </form>
    <c:if test="${not empty error}">
        <fmt:message key="order.remove.error_msg" var="remove_error"/>
        ${remove_error}
    </c:if>
    <h4><a href=<c:url value="/user"/>>${back}</a></h4>
    </div>
</div>
</body>
</html>
