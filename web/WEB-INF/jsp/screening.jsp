<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>A ${requestScope.screeningDto.title} movie screening</title>
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
                     src="${requestScope.request.contextPath}/images/${requestScope.screeningDto.imageUrl}"
                     alt="No Image"
                     title="${requestScope.screeningDto.description}">
            </div>
            <h1>${requestScope.screeningDto.title}</h1>
            <p>Genre: ${requestScope.screeningDto.genre}</p>
            <p>Description: ${requestScope.screeningDto.description}</p>
            <p>Minimum age: ${requestScope.screeningDto.minimumAge}+</p>
            <p>Duration in minutes: ${requestScope.screeningDto.durationMin}</p>
            <p>Hall: ${requestScope.screeningDto.hallName}</p>
            <p>Price: ${requestScope.screeningDto.price}$</p>
            <p>Start time: ${requestScope.screeningDto.startTime}</p>
            <h3>Income: ${requestScope.income}$</h3>
        </div>
    </div>

    <div class="buttons">
        <form action="${pageContext.request.contextPath}/screenings" method="get">
            <button class="backButton" type="submit">Back to all screenings</button>
        </form>
        <form action="${pageContext.request.contextPath}/edit-screening" method="get">
            <button class="editButton" type="submit" name="screeningId" value="${requestScope.screeningDto.id}">Edit</button>
        </form>
        <form action="${pageContext.request.contextPath}/bookmakers" method="get">
            <button class="bookmakersButton" type="submit" name="screeningId" value="${requestScope.screeningDto.id}">Bookmakers</button>
        </form>
        <form action="${pageContext.request.contextPath}/remove-screening" method="get">
            <button class="removeButton" type="submit" name="screeningId" value="${requestScope.screeningDto.id}">Remove</button>
        </form>
    </div>

</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>