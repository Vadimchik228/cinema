<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Add new movie</title>
    <style>
        <%@include file="/WEB-INF/css/entryStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="buttons">
        <form action="${pageContext.request.contextPath}/movies" method="get">
            <button class="backButton" type="submit">Back to all movies</button>
        </form>
    </div>
    <form action="${pageContext.request.contextPath}" method="post" enctype="multipart/form-data">
        <div class="entry">
            <div class="form-container">
                <label> Title:
                    <input type="text" name="title" id="titleId" required>
                </label><br>
                <label> Description: <br>
                    <textarea class="description" name="description" id="descriptionId" rows="15" cols="50"
                              required></textarea>
                </label><br>
                <label> Genre:<br>
                    <c:forEach var="genre" items="${requestScope.genres}">
                        <c:if test="${genre != 'ALL'}">
                            <input type="radio" name="genre" value="${genre}"> ${genre}<br>
                        </c:if>
                    </c:forEach>
                </label><br>
                <label> Minimum age:
                    <input type="number" id="minimumAgeId" name="minimumAge" min="0" max="21" required/>
                </label><br>
                <label> Duration in minutes:
                    <input type="number" id="durationMinId" name="durationMin" min="1" required/>
                </label><br>
                <label> Image:
                    <input type="file" name="image" id="imageId" required>
                </label><br>
                <c:if test="${requestScope.errors!=null}">
                    <div class="errors">
                        <c:forEach var="error" items="${requestScope.errors}">
                            <p>${error.message}</p>
                        </c:forEach>
                    </div>
                </c:if>
                <button type="submit">Add</button>
            </div>
        </div>
    </form>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>