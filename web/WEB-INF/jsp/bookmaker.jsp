<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Bookmaker ${requestScope.reservationDto.firstName} ${requestScope.reservationDto.lastName}</title>
    <style>
        <%@include file="/WEB-INF/css/entryStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="entry">
        <div class="info">
            <h1>Reservation info</h1>
            <p>Name: ${requestScope.reservationDto.firstName} ${requestScope.reservationDto.lastName}</p>
            <p>Email: ${requestScope.reservationDto.email}</p>
            <p>Movie: ${requestScope.reservationDto.title}</p>
            <p>Minimum age: ${requestScope.reservationDto.minimumAge}+</p>
            <p>Duration in minutes: ${requestScope.reservationDto.durationMin}</p>
            <p>Start time: ${requestScope.reservationDto.startTime}</p>
            <p>Hall: ${requestScope.reservationDto.hallName}</p>
            <p>Row: ${requestScope.reservationDto.rowNumber}</p>
            <p>Seat: ${requestScope.reservationDto.seatNumber}</p>
            <p>Price: ${requestScope.reservationDto.price}$</p>
        </div>
    </div>

    <div class="buttons">
        <button onclick="window.location.href='${requestScope.prevPage}'" class="backButton" type="submit">
            Back
        </button>
        <form action="${pageContext.request.contextPath}/remove_bookmaker" method="get">
            <button class="removeButton" type="submit" name="reservationId" value="${requestScope.reservationDto.id}">Remove</button>
        </form>
        <form action="${pageContext.request.contextPath}/download-pdf" method="get">
            <button class="downloadButton" type="submit" name="reservationId" value="${requestScope.reservationDto.id}">Download ticket</button>
        </form>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>