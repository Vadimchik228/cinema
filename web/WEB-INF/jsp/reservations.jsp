<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>All reservations</title>
    <style>
        <%@include file="/WEB-INF/css/entriesStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<form action="${pageContext.request.contextPath}/displayed-movies" method="get">
    <button class="return-button" type="submit">Back to all movies</button>
</form>
<div class="container">
    <div class="entries">
        <c:if test="${not empty requestScope.reservations}">
            <c:forEach var="reservation" items="${requestScope.reservations}">
                <div class="entry" onclick="window.location.href='${pageContext.request.contextPath}/reservation?reservationId=${reservation.id}'">
                    <p>Movie: ${reservation.title}</p>
                    <p>Start time: ${reservation.startTime}</p>
                    <p>Hall: ${reservation.hallName}</p>
                    <p>Seat: ${reservation.seatNumber}</p>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty requestScope.reservations}">
            <div>Not found!</div>
        </c:if>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>