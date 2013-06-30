
<%@ page import="com.traderapist.models.FantasyTeam" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fantasyTeam.label', default: 'FantasyTeam')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-fantasyTeam" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-fantasyTeam" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="leagueId" title="${message(code: 'fantasyTeam.leagueId.label', default: 'League Id')}" />
					
						<g:sortableColumn property="name" title="${message(code: 'fantasyTeam.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="season" title="${message(code: 'fantasyTeam.season.label', default: 'Season')}" />
					
						<g:sortableColumn property="type" title="${message(code: 'fantasyTeam.type.label', default: 'Type')}" />
					
						<th><g:message code="fantasyTeam.user.label" default="User" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${fantasyTeamInstanceList}" status="i" var="fantasyTeamInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${fantasyTeamInstance.id}">${fieldValue(bean: fantasyTeamInstance, field: "leagueId")}</g:link></td>
					
						<td>${fieldValue(bean: fantasyTeamInstance, field: "name")}</td>
					
						<td>${fieldValue(bean: fantasyTeamInstance, field: "season")}</td>
					
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
	</body>
</html>
