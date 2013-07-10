
<%@ page import="com.traderapist.models.FantasyTeam" %>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'fantasyTeam.label', default: 'FantasyTeam')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <div class="row-fluid">
        <div class="span8">
            <div id="list-fantasyTeam" class="content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]" /></h1>
                <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
                </g:if>
                <table class="table table-striped">
                    <thead>
                        <tr>

                            <g:sortableColumn property="name" title="${message(code: 'fantasyTeam.name.label', default: 'Name')}" />

                            <g:sortableColumn property="leagueId" title="${message(code: 'fantasyTeam.leagueId.label', default: 'League Id')}" />

                            <g:sortableColumn property="season" title="${message(code: 'fantasyTeam.season.label', default: 'Season')}" />

                            <g:sortableColumn property="type" title="${message(code: 'fantasyTeam.type.label', default: 'Type')}" />

                            <th><g:message code="fantasyTeam.user.label" default="User" /></th>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${fantasyTeamInstanceList}" status="i" var="fantasyTeamInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            <td><g:link action="show" id="${fantasyTeamInstance.id}">${fieldValue(bean: fantasyTeamInstance, field: "name")}</g:link></td>

                            <td>${fieldValue(bean: fantasyTeamInstance, field: "leagueId")}</td>

                            <td>${ fantasyTeamInstance.season }</td>

                            <td>${fieldValue(bean: fantasyTeamInstance, field: "fantasyLeagueType.code")}</td>

                            <td>${fieldValue(bean: fantasyTeamInstance, field: "user.username")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
                <div class="pagination">
                    <g:paginate total="${fantasyTeamInstanceTotal}" />
                </div>
            </div>
        </div>
        <div class="row-fluid" role="navigation">
            <div class="span8">
                <g:link class="create" action="create"><button class="btn btn-primary">Create Fantasy Team</button></g:link>
            </div>
        </div>
    </div>
</body>
