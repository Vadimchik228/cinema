<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
    <style>
        <%@include file="/WEB-INF/css/authorizationStyle.css" %>
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class = "title">Login</div>
            <label>Email:
                <input type="text" name="email" id="emailId">
            </label><br>
            <label>Password:
                <input type="password" name="password" id="passwordId">
            </label><br>
            <button type="submit">Login</button>
            <c:if test="${param.error!=null}">
                <div style="color: red">Unfaithful data</div>
            </c:if>
        </form>
    </div>
    <div class="auxiliary-link">
        <p>Not registered yet?</p>
        <form action="${pageContext.request.contextPath}/registration" method="get">
            <button type="submit">Registration</button>
        </form>
    </div>
</div>
</body>
<%@ include file="error.jsp" %>
<%@ include file="footer.jsp" %>
</html>