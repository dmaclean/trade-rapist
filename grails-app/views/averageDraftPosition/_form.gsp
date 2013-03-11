<%@ page import="com.traderapist.models.AverageDraftPosition" %>



<div class="fieldcontain ${hasErrors(bean: averageDraftPositionInstance, field: 'season', 'error')} required">
	<label for="season">
		<g:message code="averageDraftPosition.season.label" default="Season" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="season" type="number" min="2001" value="${averageDraftPositionInstance.season}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: averageDraftPositionInstance, field: 'adp', 'error')} required">
	<label for="adp">
		<g:message code="averageDraftPosition.adp.label" default="Adp" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="adp" value="${fieldValue(bean: averageDraftPositionInstance, field: 'adp')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: averageDraftPositionInstance, field: 'player', 'error')} required">
	<label for="player">
		<g:message code="averageDraftPosition.player.label" default="Player" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="player" name="player.id" from="${com.traderapist.models.Player.list()}" optionKey="id" required="" value="${averageDraftPositionInstance?.player?.id}" class="many-to-one"/>
</div>

