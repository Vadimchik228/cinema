<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Choose hall</title>
    <style>
        <%@include file="/WEB-INF/css/entriesStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="entries">
        <c:if test="${not empty requestScope.halls}">
            <c:forEach var="hall" items="${requestScope.halls}">
                <div class="entry" onclick="window.location.href='${requestScope.prevPage}hallId=${hall.id}'">
                    <a href="${requestScope.prevPage}hallId=${hall.id}">
                        <h1>${hall.name}</h1>
                    </a>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty requestScope.halls}">
            <div>Not found!</div>
        </c:if>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>