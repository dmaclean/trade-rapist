<%@ page import="com.traderapist.models.FantasyTeamPlayer" %>



<div class="fieldcontain ${hasErrors(bean: fantasyTeamPlayerInstance, field: 'player', 'error')} required">
	<label for="player">
		<g:message code="fantasyTeamPlayer.player.label" default="Player" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="player" name="player.id" from="${com.traderapist.models.Player.list()}" optionKey="id" required="" value="${fantasyTeamPlayerInstance?.player?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamPlayerInstance, field: 'fantasyTeam', 'error')} required">
	<label for="fantasyTeam">
		<g:message code="fantasyTeamPlayer.fantasyTeam.label" default="Fantasy Team" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="fantasyTeam" name="fantasyTeam.id" from="${com.traderapist.models.FantasyTeam.list()}" optionKey="id" required="" value="${fantasyTeamPlayerInstance?.fantasyTeam?.id}" class="many-to-one"/>
</div>

