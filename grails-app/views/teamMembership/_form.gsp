<%@ page import="com.traderapist.models.TeamMembership" %>



<div class="fieldcontain ${hasErrors(bean: teamMembershipInstance, field: 'season', 'error')} required">
	<label for="season">
		<g:message code="teamMembership.season.label" default="Season" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="season" type="number" min="2001" value="${teamMembershipInstance.season}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: teamMembershipInstance, field: 'player', 'error')} required">
	<label for="player">
		<g:message code="teamMembership.player.label" default="Player" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="player" name="player.id" from="${com.traderapist.models.Player.list()}" optionKey="id" required="" value="${teamMembershipInstance?.player?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: teamMembershipInstance, field: 'team', 'error')} required">
	<label for="team">
		<g:message code="teamMembership.team.label" default="Team" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="team" name="team.id" from="${com.traderapist.models.Team.list()}" optionKey="id" required="" value="${teamMembershipInstance?.team?.id}" class="many-to-one"/>
</div>

