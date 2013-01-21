
<%@ page import="com.traderapist.models.Stat" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'stat.label', default: 'Stat')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-stat" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-stat" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list stat">
			
				<g:if test="${statInstance?.season}">
				<li class="fieldcontain">
					<span id="season-label" class="property-label"><g:message code="stat.season.label" default="Season" /></span>
					
						<span class="property-value" aria-labelledby="season-label"><g:fieldValue bean="${statInstance}" field="season"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${statInstance?.week}">
				<li class="fieldcontain">
					<span id="week-label" class="property-label"><g:message code="stat.week.label" default="Week" /></span>
					
						<span class="property-value" aria-labelledby="week-label"><g:fieldValue bean="${statInstance}" field="week"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${statInstance?.statKey}">
				<li class="fieldcontain">
					<span id="statKey-label" class="property-label"><g:message code="stat.statKey.label" default="Stat Key" /></span>
					
						<span class="property-value" aria-labelledby="statKey-label">${statInstance.translateStatKey()}</span>
					
				</li>
				</g:if>
			
				<g:if test="${statInstance?.statValue}">
				<li class="fieldcontain">
					<span id="statValue-label" class="property-label"><g:message code="stat.statValue.label" default="Stat Value" /></span>
					
						<span class="property-value" aria-labelledby="statValue-label"><g:fieldValue bean="${statInstance}" field="statValue"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${statInstance?.player}">
				<li class="fieldcontain">
					<span id="player-label" class="property-label"><g:message code="stat.player.label" default="Player" /></span>
					
						<span class="property-value" aria-labelledby="player-label"><g:link controller="player" action="show" id="${statInstance?.player?.id}">${statInstance.player.name}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${statInstance?.id}" />
					<g:link class="edit" action="edit" id="${statInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
