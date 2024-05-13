<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Edit screening ${requestScope.id}</title>
    <style>
        <%@include file="/WEB-INF/css/entryStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="buttons">
        <form action="${pageContext.request.contextPath}/screenings" method="get">
            <button class="backButton" type="submit">Back to all screenings</button>
        </form>
    </div>
    <form action="${pageContext.request.contextPath}" method="post" enctype="multipart/form-data">
        <div class="entry">
            <div class="form-container">
                <label> MovieId: <a href="${pageContext.request.contextPath}/choose-movie">Choose movie</a>
                    <input type="number" name="movieId" id="movieId" value="${requestScope.movieId}" readonly>
                </label><br>
                <label> HallId: <a href="${pageContext.request.contextPath}/choose-hall">Choose hall</a>
                    <input type="number" name="hallId" id="hallId" value="${requestScope.hallId}" readonly>
                </label><br>
                <label> Price:
                    <input type="number" name="price" id="priceId" min="0" value="${requestScope.price}" required>
                </label><br>
                <label> Start time:
                    <input type="datetime-local" name="startTime" id="startTimeId" value="${requestScope.startTime}"
                           required>
                </label><br>
                <button type="submit">Edit</button>
            </div>
        </div>
    </form>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>