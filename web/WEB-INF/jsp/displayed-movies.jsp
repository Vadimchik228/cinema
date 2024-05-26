<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>All movies</title>
    <style>
        <%@include file="/WEB-INF/css/entriesStyle.css" %>

    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="filters">
        <h1>Filters</h1>
        <form action="${pageContext.request.contextPath}/displayed-movies" method="post">
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
        <c:if test="${not empty requestScope.movies}">
            <c:forEach var="movie" items="${requestScope.movies}">
                <div class="entry" onclick="window.location.href='${pageContext.request.contextPath}/displayed-movie?movieId=${movie.id}'">
                    <img width="150" height="100" src="${pageContext.request.contextPath}/images/${movie.imageUrl}" alt="No Image"><br>
                    <a href="${pageContext.request.contextPath}/displayed-movie?movieId=${movie.id}">${movie.title}</a>
                    <div class="info">
                        <span class="genre">${movie.genre}</span>
                        <span class="minimum-age">${movie.minimumAge}+</span>
                    </div>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty requestScope.movies}">
            <div>Not found!</div>
        </c:if>
    </div>
</div>
<form action="${pageContext.request.contextPath}/reservations" method="get" class="nomadic-button">
    <button type="submit">All reservations</button>
</form>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>