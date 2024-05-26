<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:if test="${not empty requestScope.error}">
    <c:out value="${error}"></c:out>
</c:if>
