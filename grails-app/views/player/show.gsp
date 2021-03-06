
<%@ page import="com.traderapist.models.Player" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'player.label', default: 'Player')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-player" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-player" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list player">
			
				<g:if test="${playerInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="player.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${playerInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${playerInstance?.position}">
				<li class="fieldcontain">
					<span id="position-label" class="property-label"><g:message code="player.position.label" default="Position" /></span>
					
						<span class="property-value" aria-labelledby="position-label"><g:fieldValue bean="${playerInstance}" field="position"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${playerInstance?.stats}">
				<li class="fieldcontain">
					<span id="stats-label" class="property-label"><g:message code="player.stats.label" default="Stats" /></span>
					
						<g:each in="${playerInstance.stats}" var="s">
						    <span class="property-value" aria-labelledby="stats-label"><g:link controller="stat" action="show" id="${s.id}">
                                <g:if test="${s.week == -1}">${s.season} - ${s.translateStatKey()} - ${s.statValue}</g:if>
                                <g:if test="${s.week != -1}">Week ${s.week} (${s.season}) - ${s.translateStatKey()} - ${s.statValue}</g:if>
                            </g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>

            <h3>Season Standard Deviations</h3>
            <ol>
                <g:each in="${stdDevYears}" var="val">
                    <li>${val.key} - ${Double.valueOf(val.value).trunc(1)} (${Double.valueOf(scoringAverages[val.key] - val.value).trunc(1)} - ${Double.valueOf(scoringAverages[val.key] + val.value).trunc(1)})</li>
                </g:each>
            </ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${playerInstance?.id}" />
					<g:link class="edit" action="edit" id="${playerInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
