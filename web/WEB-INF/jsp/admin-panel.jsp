<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Admin panel</title>
    <style>
        <%@include file="/WEB-INF/css/adminPanel.css" %>
    </style>
</head>
<%@ include file="header.jsp" %>
<body>
<div class="container">
    <div class="card" onclick="window.location.href='${pageContext.request.contextPath}/movies'">
        <label>Movies</label><br>
        <p>Count: ${requestScope.moviesNumber}</p>
    </div>
    <div class="card" onclick="window.location.href='${pageContext.request.contextPath}/screenings'">
        <label>Screenings</label><br>
        <p>Total income: ${requestScope.totalIncome}$</p>
        <p>Count: ${requestScope.screeningsNumber}</p>
    </div>
    <div class="card" onclick="window.location.href='${pageContext.request.contextPath}/halls'">
        <label>Halls</label><br>
        <p>Count: ${requestScope.hallsNumber}</p>
    </div>
</div>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</body>