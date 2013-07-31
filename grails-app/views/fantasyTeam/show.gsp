
<%@ page import="com.traderapist.models.FantasyTeam" %>
<g:set var="user" value="${sec.username()}" />

<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'fantasyTeam.label', default: 'FantasyTeam')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="container">
        <div id="show-fantasyTeam" class="span12" role="main">
            <h1>${ fantasyTeamInstance.name }</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <form>
                <fieldset>
                    <label>League Id</label>
                    <span class="property-value" aria-labelledby="leagueId-label"><g:fieldValue bean="${fantasyTeamInstance}" field="leagueId"/></span>

                    <label>Name</label>
                    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${fantasyTeamInstance}" field="name"/></span>
                </fieldset>
            </form>
            <g:if test="${fantasyTeamInstance?.leagueId}">
                    <li class="fieldcontain">
                        <span id="leagueId-label" class="property-label"><g:message code="fantasyTeam.leagueId.label" default="League Id" /></span>

                        <span class="property-value" aria-labelledby="leagueId-label"><g:fieldValue bean="${fantasyTeamInstance}" field="leagueId"/></span>

                    </li>
                </g:if>

                <g:if test="${fantasyTeamInstance?.name}">
                    <li class="fieldcontain">
                        <span id="name-label" class="property-label"><g:message code="fantasyTeam.name.label" default="Name" /></span>

                        <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${fantasyTeamInstance}" field="name"/></span>

                    </li>
                </g:if>

                <g:if test="${fantasyTeamInstance?.season}">
                    <li class="fieldcontain">
                        <span id="season-label" class="property-label"><g:message code="fantasyTeam.season.label" default="Season" /></span>

                        <span class="property-value" aria-labelledby="season-label"><g:fieldValue bean="${fantasyTeamInstance}" field="season"/></span>

                    </li>
                </g:if>

                <g:if test="${fantasyTeamInstance?.fantasyLeagueType}">
                    <li class="fieldcontain">
                        <span id="type-label" class="property-label"><g:message code="fantasyTeam.type.label" default="FantasyLeagueType" /></span>

                        <span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${fantasyTeamInstance}" field="fantasyLeagueType.code"/></span>

                    </li>
                </g:if>

                <g:if test="${fantasyTeamInstance?.user && user == "admin"}">
                    <li class="fieldcontain">
                        <span id="user-label" class="property-label"><g:message code="fantasyTeam.user.label" default="User" /></span>

                        <span class="property-value" aria-labelledby="user-label"><g:link controller="user" action="show" id="${fantasyTeamInstance?.user?.id}">${fantasyTeamInstance?.user?.username}</g:link></span>

                    </li>
                </g:if>

            </ol>
            <g:form>
                <fieldset class="buttons">
                    <g:hiddenField name="id" value="${fantasyTeamInstance?.id}" />
                    <g:link class="edit" action="edit" id="${fantasyTeamInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
        </div>
    </div>
</body>
