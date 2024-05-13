<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${requestScope.bookmakersNumber} bookmakers</title>
    <style>
        <%@include file="/WEB-INF/css/entriesStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<button onclick="window.location.href='${requestScope.prevPage}'" class="return-button" type="submit">
    Back
</button>
<div class="container">
    <div class="entries">
        <c:if test="${not empty requestScope.bookmakers}">
            <c:forEach var="bookmaker" items="${requestScope.bookmakers}">
                <div class="entry" onclick="window.location.href='${pageContext.request.contextPath}/bookmaker?reservationId=${bookmaker.reservationId}'">
                    <p>First name: ${bookmaker.firstName}</p>
                    <p>Last name: ${bookmaker.lastName}</p>
                    <p>Email: ${bookmaker.email}</p>
                    <p>Seat: ${bookmaker.seatNumber}</p>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty requestScope.bookmakers}">
            <div>Not found!</div>
        </c:if>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>