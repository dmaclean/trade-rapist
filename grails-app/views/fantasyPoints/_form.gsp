<%@ page import="com.traderapist.models.FantasyPoints" %>



<div class="fieldcontain ${hasErrors(bean: fantasyPointsInstance, field: 'season', 'error')} required">
	<label for="season">
		<g:message code="fantasyPoints.season.label" default="Season" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="season" type="number" value="${fantasyPointsInstance.season}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyPointsInstance, field: 'week', 'error')} required">
	<label for="week">
		<g:message code="fantasyPoints.week.label" default="Week" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="week" from="${-1..17}" class="range" required="" value="${fieldValue(bean: fantasyPointsInstance, field: 'week')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyPointsInstance, field: 'points', 'error')} required">
	<label for="points">
		<g:message code="fantasyPoints.points.label" default="Points" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="points" type="number" value="${fantasyPointsInstance.points}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyPointsInstance, field: 'system', 'error')} required">
    <label for="system">
        <g:message code="fantasyPoints.system.label" default="System" />
        <span class="required-indicator">*</span>
    </label>
    <g:field name="system" type="string" value="${fantasyPointsInstance.system}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyPointsInstance, field: 'player', 'error')} required">
	<label for="player">
		<g:message code="fantasyPoints.player.label" default="Player" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="player" name="player.id" from="${com.traderapist.models.Player.list()}" optionKey="id" required="" value="${fantasyPointsInstance?.player?.id}" class="many-to-one"/>
</div>

