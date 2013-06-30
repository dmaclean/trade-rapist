
<%@ page import="com.traderapist.models.FantasyTeam" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fantasyTeam.label', default: 'FantasyTeam')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-fantasyTeam" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-fantasyTeam" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list fantasyTeam">
			
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
			
				<g:if test="${fantasyTeamInstance?.user}">
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
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
