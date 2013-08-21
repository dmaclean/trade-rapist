<%--
  Created by IntelliJ IDEA.
  User: dmaclean
  Date: 8/19/13
  Time: 2:49 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.traderapist.models.FantasyPointsJob" contentType="text/html;charset=UTF-8" %>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'fantasyPoints.label', default: 'FantasyPoints')}" />
    <title>Trade Rapist - Update Projections</title>
</head>
<body>
<div class="row">
    <div class="span12">
        <g:form method="post" >
            <fieldset>
                <legend>Update Projections</legend>
                <label>Projection Type</label>
                <select name="projection_type" id="projection_type">
                    <option value="">Select One</option>
                    <option value="${ FantasyPointsJob.YAHOO_STANDARD_PROJECTION }">Yahoo! Standard</option>
                    <option value="${ FantasyPointsJob.YAHOO_PPR_PROJECTION }">Yahoo! PPR</option>
                </select>

                <label>Season</label>
                <g:textField name="season" value="${ params.season }"/>

                <label>Data</label>
                <g:textArea name="data" id="data"></g:textArea>

                <g:actionSubmit class="btn btn-primary" action="updateProjections" value="${message(code: 'default.button.update.label', default: 'Update Projections!')}" />
            </fieldset>
        </g:form>
    </div>
</div>
</body>