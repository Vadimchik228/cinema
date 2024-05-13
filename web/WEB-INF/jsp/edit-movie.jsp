<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Edit movie ${requestScope.movieDto.id}</title>
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
                <div class="image-container">
                    <img width="150" height="100"
                         src="${requestScope.request.contextPath}/images/${requestScope.movieDto.imageUrl}"
                         alt="No Image">
                </div>
                <br>
                <label for="titleId">Title:
                    <input type="text" name="title" id="titleId" value="${requestScope.movieDto.title}" required>
                </label><br>
                <label> Description: <br>
                    <textarea class="description" name="description" id="descriptionId" rows="15" cols="50"
                              required>${requestScope.movieDto.description}</textarea>
                </label><br>
                <label>Genre:<br>
                    <c:forEach var="genre" items="${requestScope.genres}">
                        <input type="radio" name="genre"
                               value="${genre}" ${requestScope.movieDto.genre == genre ? 'checked' : ''}> ${genre}<br>
                    </c:forEach>
                </label><br>
                <label>Duration in minutes:
                    <input type="number" id="durationMinId" name="durationMin" min="1"
                           value="${requestScope.movieDto.durationMin}" required/>
                </label><br>
                <label>Minimum age:
                    <input type="number" id="minimumAgeId" name="minimumAge" min="0" max="21"
                           value="${requestScope.movieDto.minimumAge}" required/>
                </label><br>
                <label for="currentImageUrlId">Current Image URL:
                    <input type="text" name="currentImage" id="currentImageUrlId" value="${requestScope.movieDto.imageUrl}" readonly>
                </label><br>
                <label for="imageId">Select new image:
                    <input type="file" name="image" id="imageId">
                </label><br>
                <c:if test="${requestScope.errors!=null}">
                    <div class="errors">
                        <c:forEach var="error" items="${requestScope.errors}">
                            <p>${error.message}</p>
                        </c:forEach>
                    </div>
                </c:if>
                <button type="submit">Edit</button>
            </div>
        </div>

    </form>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>