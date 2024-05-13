<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <title>Profile</title>
    <style>
        <%@include file="/WEB-INF/css/entryStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="entry">
        <div class="info">
            <h1>Profile info</h1>
            <p>First name: ${requestScope.user.firstName}</p>
            <p>Last name: ${requestScope.user.lastName}</p>
            <p>Email: ${requestScope.user.email}</p>
            <p>Password: **********</p>
        </div>
    </div>
    <div class="buttons">
        <button onclick="window.location.href='${requestScope.prevPage}'" class="backButton" type="submit">
            Back
        </button>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>