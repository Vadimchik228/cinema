<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>All screenings</title>
    <style>
        <%@include file="/WEB-INF/css/entriesStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<form action="${pageContext.request.contextPath}/admin-panel" method="get">
    <button class="return-button" type="submit">Back to admin panel</button>
</form>
<div class="container">
    <div class="filters">
        <h1>Filters</h1>
        <form action="${pageContext.request.contextPath}/screenings" method="post">
            <label>Enter title:
                <input type="text" name="title" id="titleId" value="${requestScope.selectedTitle}">
            </label><br>
            <label>Select genre:<br>
                <c:forEach var="genre" items="${requestScope.genres}">
                    <input type="radio" name="genre" value="${genre}" ${genre == requestScope.selectedGenre ? 'checked' : ''}>${genre}<br>
                </c:forEach>
            </label><br>
            <label>Select date:
                <input type="date" name="date" id="dateId" value="${requestScope.selectedDate}">
            </label><br>
            <button type="submit">Find</button>
        </form>
    </div>

    <div class="entries">
        <c:if test="${not empty requestScope.screenings}">
            <c:forEach var="screening" items="${requestScope.screenings}">
                <div class="entry" onclick="window.location.href='${pageContext.request.contextPath}/screening?screeningId=${screening.id}'">
                    <img width="150" height="100" src="${pageContext.request.contextPath}/images/${screening.imageUrl}" alt="No Image"><br>
                    <a href="${pageContext.request.contextPath}/screening?screeningId=${screening.id}">${screening.title}</a>
                    <div class="info">
                        <span class="genre">${screening.genre}</span>
                        <span class="minimum-age">${screening.minimumAge}+</span>
                    </div>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty requestScope.screenings}">
            <div>Not found!</div>
        </c:if>
    </div>
</div>
<form action="${pageContext.request.contextPath}/add-screening" method="get" class="nomadic-button">
    <button type="submit">Add screening</button>
</form>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>