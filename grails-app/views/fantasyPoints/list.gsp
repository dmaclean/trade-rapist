
<%@ page import="com.traderapist.models.FantasyPoints" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fantasyPoints.label', default: 'FantasyPoints')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-fantasyPoints" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-fantasyPoints" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="season" title="${message(code: 'fantasyPoints.season.label', default: 'Season')}" />
					
						<g:sortableColumn property="week" title="${message(code: 'fantasyPoints.week.label', default: 'Week')}" />
					
						<g:sortableColumn property="points" title="${message(code: 'fantasyPoints.points.label', default: 'Points')}" />
					
						<th><g:message code="fantasyPoints.player.label" default="Player" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${fantasyPointsInstanceList}" status="i" var="fantasyPointsInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${fantasyPointsInstance.id}">${fieldValue(bean: fantasyPointsInstance, field: "season")}</g:link></td>
					
						<td>${fieldValue(bean: fantasyPointsInstance, field: "week")}</td>
					
						<td>${fieldValue(bean: fantasyPointsInstance, field: "points")}</td>
					
						<td>${fieldValue(bean: fantasyPointsInstance, field: "player")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${fantasyPointsInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
