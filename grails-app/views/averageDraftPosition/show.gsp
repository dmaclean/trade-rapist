
<%@ page import="com.traderapist.models.AverageDraftPosition" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-averageDraftPosition" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-averageDraftPosition" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list averageDraftPosition">
			
				<g:if test="${averageDraftPositionInstance?.season}">
				<li class="fieldcontain">
					<span id="season-label" class="property-label"><g:message code="averageDraftPosition.season.label" default="Season" /></span>
					
						<span class="property-value" aria-labelledby="season-label"><g:fieldValue bean="${averageDraftPositionInstance}" field="season"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${averageDraftPositionInstance?.adp}">
				<li class="fieldcontain">
					<span id="adp-label" class="property-label"><g:message code="averageDraftPosition.adp.label" default="Adp" /></span>
					
						<span class="property-value" aria-labelledby="adp-label"><g:fieldValue bean="${averageDraftPositionInstance}" field="adp"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${averageDraftPositionInstance?.player}">
				<li class="fieldcontain">
					<span id="player-label" class="property-label"><g:message code="averageDraftPosition.player.label" default="Player" /></span>
					
						<span class="property-value" aria-labelledby="player-label"><g:link controller="player" action="show" id="${averageDraftPositionInstance?.player?.id}">${averageDraftPositionInstance?.player?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${averageDraftPositionInstance?.id}" />
					<g:link class="edit" action="edit" id="${averageDraftPositionInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
