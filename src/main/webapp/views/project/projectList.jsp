<%--
  Created by IntelliJ IDEA.
  User: yasaman
  Date: 3/2/19
  Time: 11:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Users</title>
    <style>
        table {
            text-align: center;
            margin: 0 auto;
        }
        td {
            min-width: 300px;
            margin: 5px 5px 5px 5px;
            text-align: center;
        }
    </style>
</head>
<body>
<div style="direction: rtl">
    <table>
        <c:forEach var="project" items="${projectList}">
            <tr>
                <td><c:out value="${project.ID}"/></td>
                <td><c:out value="${project.title}"/></td>
                <td>${project.budget}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>