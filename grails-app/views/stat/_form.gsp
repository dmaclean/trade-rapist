<%@ page import="com.traderapist.models.Stat" %>



<div class="fieldcontain ${hasErrors(bean: statInstance, field: 'season', 'error')} required">
	<label for="season">
		<g:message code="stat.season.label" default="Season" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="season" type="number" value="${statInstance.season}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: statInstance, field: 'week', 'error')} required">
	<label for="week">
		<g:message code="stat.week.label" default="Week" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="week" from="${-1..17}" class="range" required="" value="${fieldValue(bean: statInstance, field: 'week')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: statInstance, field: 'statKey', 'error')} required">
	<label for="statKey">
		<g:message code="stat.statKey.label" default="Stat Key" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="statKey" type="number" value="${statInstance.statKey}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: statInstance, field: 'statValue', 'error')} required">
	<label for="statValue">
		<g:message code="stat.statValue.label" default="Stat Value" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="statValue" type="number" value="${statInstance.statValue}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: statInstance, field: 'player', 'error')} required">
	<label for="player">
		<g:message code="stat.player.label" default="Player" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="player" name="player.id" from="${com.traderapist.models.Player.list()}" optionKey="id" required="" value="${statInstance?.player?.id}" class="many-to-one"/>
</div>

