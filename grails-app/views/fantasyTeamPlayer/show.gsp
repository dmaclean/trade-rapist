
<%@ page import="com.traderapist.models.FantasyTeamPlayer" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-fantasyTeamPlayer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-fantasyTeamPlayer" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list fantasyTeamPlayer">
			
				<g:if test="${fantasyTeamPlayerInstance?.player}">
				<li class="fieldcontain">
					<span id="player-label" class="property-label"><g:message code="fantasyTeamPlayer.player.label" default="Player" /></span>
					
						<span class="property-value" aria-labelledby="player-label"><g:link controller="player" action="show" id="${fantasyTeamPlayerInstance?.player?.id}">${fantasyTeamPlayerInstance?.player?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${fantasyTeamPlayerInstance?.fantasyTeam}">
				<li class="fieldcontain">
					<span id="fantasyTeam-label" class="property-label"><g:message code="fantasyTeamPlayer.fantasyTeam.label" default="Fantasy Team" /></span>
					
						<span class="property-value" aria-labelledby="fantasyTeam-label"><g:link controller="fantasyTeam" action="show" id="${fantasyTeamPlayerInstance?.fantasyTeam?.id}">${fantasyTeamPlayerInstance?.fantasyTeam?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${fantasyTeamPlayerInstance?.id}" />
					<g:link class="edit" action="edit" id="${fantasyTeamPlayerInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
