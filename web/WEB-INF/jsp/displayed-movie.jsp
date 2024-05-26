<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${requestScope.movie.title}</title>
    <style>
        <%@include file="/WEB-INF/css/entryStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="buttons">
        <form action="${pageContext.request.contextPath}/displayed-movies" method="get">
            <button class="backButton" type="submit">Back to all movies</button>
        </form>
    </div>
    <div class="entry">
        <div class="info">
            <div class="image-container">
                <img width="150" height="100"
                     src="${requestScope.request.contextPath}/images/${requestScope.movie.imageUrl}"
                     alt="No Image"
                     title="${requestScope.movie.description}">
            </div>
            <h1>${requestScope.movie.title}</h1>
            <p>Genre: ${requestScope.movie.genre}</p>
            <p>Description: ${requestScope.movie.description}</p>
            <p>Minimum age: ${requestScope.movie.minimumAge}+</p>
            <p>Duration in minutes: ${requestScope.movie.durationMin}</p>

            <div class="screenings">
                <c:if test="${not empty requestScope.screenings}">
                    <c:forEach var="screening" items="${requestScope.screenings}">
                        <div class="screening" onclick="window.location.href='${pageContext.request.contextPath}/choose-seat?screeningId=${screening.id}&hallId=${screening.hallId}'">
                            <p>${screening.startTime}</p>
                            <p>${screening.price}$</p>
                            <p>${screening.hallName}</p>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${empty requestScope.screenings}">
                    <div style="font-weight: bold">Not found any available screenings!</div>
                </c:if>
            </div>

        </div>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>