<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="userLogin" type="java.lang.String"
              required="true" %>
<%@ attribute name="userName" type="java.lang.String" %>
    <form class="logout" action="<c:url value="/authentication"/>" method="POST">
        ${userName} (${userLogin})
        <input type="submit" value="Log out">
        <input type="hidden" name="actionName" value="logout">
    </form>
