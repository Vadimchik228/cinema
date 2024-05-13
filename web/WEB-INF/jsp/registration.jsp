<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
    <style>
        <%@include file="/WEB-INF/css/authorizationStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/registration" method="post">
            <div class = "title">Registration</div>
            <label>First name:
                <input type="text" name="firstName" id="firstNameId" required>
            </label><br>
            <label>Last name:
                <input type="text" name="lastName" id="lastNameId" required>
            </label><br>
            <label>Email:
                <input type="text" name="email" id="emailId" required>
            </label><br>
            <label>Password:
                <input type="password" name="password" id="passwordId" required>
            </label><br>
            <label>Role:
                <select name="role">
                    <c:forEach var="role" items="${requestScope.roles}">
                        <option value="${role}">${role}</option>
                    </c:forEach>
                </select>
            </label><br>
            <c:if test="${not empty requestScope.errors}">
                <div class="errors">
                    <c:forEach var="error" items="${requestScope.errors}">
                        <p>${error.message}</p>
                    </c:forEach>
                </div>
            </c:if>
            <button type="submit">Register</button>
        </form>
    </div>
    <div class="auxiliary-link">
        <p>Already registered?</p>
        <form action="${pageContext.request.contextPath}/login" method="get">
            <button type="submit">Login</button>
        </form>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>