<%--
  Created by IntelliJ IDEA.
  User: dmaclean
  Date: 7/8/13
  Time: 5:20 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <meta name="layout" content="main"/>
    <title>Trade Rapist - Sign-up</title>
</head>
<body>
    <g:set var="user" value="${sec.username()}" />
    <div class="container">
        <g:if test="${ user }">
            <div class="row">
                <div class="span12">
                    Welcome back, ${ user }
                </div>
            </div>
        </g:if>
        <g:else>
            <div class="hero-unit">
                <h1>Trade Rapist</h1>
                <p>Kick-ass fantasy football analysis</p>
                <p><a href="user/create" class="btn btn-primary btn-large">Sign up!</a></p>
            </div>
        </g:else>
    </div>
</body>