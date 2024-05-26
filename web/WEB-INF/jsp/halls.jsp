<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>All halls</title>
    <style>
        <%@include file="/WEB-INF/css/hallsStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<form action="${pageContext.request.contextPath}/admin-panel" method="get">
    <button class="return-button" type="submit">Back to admin panel</button>
</form>
<div class="container">
    <c:if test="${not empty requestScope.halls}">
        <c:forEach var="hall" items="${requestScope.halls}">
            <div class="hall">
                <h2>${hall.name}</h2>
                <div class="lines">
                    <c:forEach var="line" items="${hall.lines}">
                        <div class="line">
                            <c:forEach var="seat" items="${line.seats}">
                                <div class="seat">${seat.number}</div>
                            </c:forEach>
                        </div><br>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </c:if>
    <c:if test="${empty requestScope.halls}">
        <div>No halls found!</div>
    </c:if>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>