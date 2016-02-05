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
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="css/main.css">
    <fmt:message key="order.form.edit.title" var="order_edit_title"/>
    <title>${order_edit_title}</title>
</head>
<body>
<div class="main-content">
    <div class="head-content">

        <tags:logout userLogin="${user.login}" userName="${user.name}" curr_lang="${locale}"/>
        <tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}"/>
        ${order_edit_title}
    </div>
    <fmt:message key="order.form.places" var="number_places"/>
    <fmt:message key="order.form.duration" var="duration"/>
    <fmt:message key="order.form.from" var="from"/>
    <fmt:message key="order.form.to" var="to"/>
    <fmt:message key="order.form.class" var="room_class"/>
    <fmt:message key="order.form.comment" var="comment"/>
    <div class="form">
    <form action="edit_order" method="post">
        <input type="hidden" name="actionName" value="submit_edit"/>
        ${number_places}:
        <input type="number" name="places" value="${order.places}" min="1" max="4" required/>
        <br>
        ${duration}:
        <br>
        ${from}: <input type="date" name="date_in" value="${order.dateIn}" required/>
        <br>
        ${to}: <input type="date" name="date_out" value="${order.dateOut}" required/>

        <br>
        ${room_class}:
        <fmt:message key="user.order.class.lux" var="lux"/>
        <fmt:message key="user.order.class.economy" var="economy"/>
        <select name="class">
            <option value="1" <c:if test="${order.classOfComfort == 1}">selected</c:if>>1: ${lux}</option>
            <option value="2" <c:if test="${order.classOfComfort == 2}">selected</c:if>>2: ${economy}</option>
        </select>
        <br>
        ${comment}:<br>
        <textarea name="comment"></textarea>
        <br>
        <fmt:message key="order.form.edit.submit" var="submit_edit"/>
        <input type="submit" value="${submit_edit}">


        <c:if test="${not empty order_error}">
            <fmt:message key="order.form.edit.fail" var="fail"/>
            <fmt:message key="order.form.error_msg" var="error_msg"/>
            <div class="error">
                    ${fail}: ${error_msg}
            </div>
        </c:if>

    </form>
    <c:if test="${not empty error}">
        <fmt:message key="orders.error" var="error"/>
        ${error}
    </c:if>
    <fmt:message key="ref.my_page" var="my_page"/>
    <h4><a href=<c:url value="/user"/>>${my_page}</a></h4>
    </div>
</div>
</body>
</html>
