<%@ page import="com.traderapist.models.Player" %>



<div class="fieldcontain ${hasErrors(bean: playerInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="player.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${playerInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: playerInstance, field: 'position', 'error')} ">
	<label for="position">
		<g:message code="player.position.label" default="Position" />
		
	</label>
	<g:textField name="position" value="${playerInstance?.position}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: playerInstance, field: 'stats', 'error')} ">
	<label for="stats">
		<g:message code="player.stats.label" default="Stats" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${playerInstance?.stats?}" var="s">
    <li><g:link controller="stat" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="stat" action="create" params="['player.id': playerInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'stat.label', default: 'Stat')])}</g:link>
</li>
</ul>

</div>

