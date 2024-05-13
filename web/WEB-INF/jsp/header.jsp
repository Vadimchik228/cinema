<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <style>
        <%@include file="/WEB-INF/css/headerStyle.css" %>
    </style>
</head>
<body>
<div class="header">
    <h1>CINEMA</h1>
    <c:if test="${not empty sessionScope.user}">
        <form action="${pageContext.request.contextPath}/profile" method="get">
            <button class="profile-button" type="submit">Profile</button>
        </form>
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <button class="logout-button" type="submit">Logout</button>
        </form>
    </c:if>
</div>
</body>
</html>