<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Choose seat</title>
    <style>
        <%@include file="/WEB-INF/css/chooseSeatStyle.css" %>
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
    <div class="hall">
        <h2>${requestScope.hallDto.name}</h2>
        <div class="lines">
            <c:forEach var="line" items="${requestScope.hallDto.lines}">
                <div class="line">
                    <c:forEach var="seat" items="${line.seats}">
                        <c:choose>
                            <c:when test="${seat.isReserved}">
                                <div class="reserved-seat">${seat.number}</div>
                            </c:when>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/choose-seat" method="post">
                                    <input type="hidden" name="seatId" value="${seat.id}">
                                    <input type="hidden" name="screeningId" value="${requestScope.screeningId}">
                                    <div class="free-seat" onclick="this.parentNode.submit()">
                                            ${seat.number}
                                    </div>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div><br>
            </c:forEach>
        </div>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>