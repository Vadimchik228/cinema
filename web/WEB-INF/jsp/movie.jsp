<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>${requestScope.movieDto.title}</title>
    <style>
        <%@include file="/WEB-INF/css/entryStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="entry">
        <div class="info">
            <div class="image-container">
                <img width="150" height="100"
                     src="${requestScope.request.contextPath}/images/${requestScope.movieDto.imageUrl}"
                     alt="No Image"
                     title="${requestScope.movieDto.description}">
            </div>
            <h1>${requestScope.movieDto.title}</h1>
            <p>Genre: ${requestScope.movieDto.genre}</p>
            <p>Description: ${requestScope.movieDto.description}</p>
            <p>Minimum age: ${requestScope.movieDto.minimumAge}+</p>
            <p>Duration in minutes: ${requestScope.movieDto.durationMin}</p>
        </div>
    </div>

    <div class="buttons">
        <form action="${pageContext.request.contextPath}/movies" method="get">
            <button class="backButton" type="submit">Back to all movies</button>
        </form>
        <form action="${pageContext.request.contextPath}/edit-movie" method="get">
            <button class="editButton" type="submit" name="movieId" value="${requestScope.movieDto.id}">Edit</button>
        </form>
        <form action="${pageContext.request.contextPath}/remove-movie" method="get">
            <button class="removeButton" type="submit" name="movieId" value="${requestScope.movieDto.id}">Remove</button>
        </form>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>