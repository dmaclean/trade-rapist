<%@ page import="com.traderapist.models.FantasyTeam" %>



<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'leagueId', 'error')} ">
	<label for="leagueId">
		<g:message code="fantasyTeam.leagueId.label" default="League Id" />
		
	</label>
	<g:textField name="leagueId" value="${fantasyTeamInstance?.leagueId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="fantasyTeam.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${fantasyTeamInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'season', 'error')} required">
	<label for="season">
		<g:message code="fantasyTeam.season.label" default="Season" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="season" type="number" value="${fantasyTeamInstance.season}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'fantasyLeagueType', 'error')} required">
	<label for="fantasyLeagueType">
		<g:message code="fantasyTeam.fantasyLeagueType.label" default="FantasyLeagueType" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="fantasyLeagueType" name="fantasyLeagueType.id" from="${com.traderapist.models.FantasyLeagueType.list()}" optionKey="id" optionValue="code" value="${fantasyTeamInstance.fantasyLeagueType}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'user', 'error')} required">
	<label for="user">
		<g:message code="fantasyTeam.user.label" default="User" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="user" name="user.id" from="${com.traderapist.security.User.list()}" optionKey="id" optionValue="username" required="" value="${fantasyTeamInstance?.user?.id}" class="many-to-one"/>
</div>

