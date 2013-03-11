
<%@ page import="com.traderapist.models.TeamMembership" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'teamMembership.label', default: 'TeamMembership')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-teamMembership" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-teamMembership" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="season" title="${message(code: 'teamMembership.season.label', default: 'Season')}" />
					
						<th><g:message code="teamMembership.player.label" default="Player" /></th>
					
						<th><g:message code="teamMembership.team.label" default="Team" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${teamMembershipInstanceList}" status="i" var="teamMembershipInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${teamMembershipInstance.id}">${fieldValue(bean: teamMembershipInstance, field: "season")}</g:link></td>
					
						<td>${fieldValue(bean: teamMembershipInstance, field: "player")}</td>
					
						<td>${fieldValue(bean: teamMembershipInstance, field: "team")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${teamMembershipInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
