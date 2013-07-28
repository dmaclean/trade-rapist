<%@ page import="com.traderapist.constants.FantasyConstants; com.traderapist.models.FantasyTeam; com.traderapist.security.User" %>
<g:set var="user" value="${sec.username()}" />

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'fantasyLeagueType', 'error')} required">
    <label for="fantasyLeagueType">
        <g:message code="fantasyTeam.fantasyLeagueType.label" default="Fantasy League Type" />
        <span class="required-indicator">*</span>
    </label>
    <g:select id="fantasyLeagueType" name="fantasyLeagueType.id" from="${com.traderapist.models.FantasyLeagueType.list()}" optionKey="id" optionValue="code" value="${fantasyTeamInstance.fantasyLeagueType}" required=""/>
    <span class="help-inline">This is here because in the future we might try to hook into the fantasy provider's services.</span>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'leagueId', 'error')} ">
	<label for="leagueId">
		<g:message code="fantasyTeam.leagueId.label" default="League Id" />
		
	</label>
	<g:textField name="leagueId" value="${fantasyTeamInstance?.leagueId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="fantasyTeam.name.label" default="Team Name" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="name" value="${fantasyTeamInstance?.name}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'season', 'error')} required">
	<label for="season">
		<g:message code="fantasyTeam.season.label" default="Season" />
		<span class="required-indicator">*</span>
	</label>
    <g:if test="${fantasyTeamInstance}">
        <g:set var="season" value="${Calendar.getInstance().get(Calendar.YEAR)}"/>
    </g:if>
    <g:else>
        <g:set var="season" value="${fantasyTeamInstance.season}"/>
    </g:else>
	<g:field name="season" type="number" value="${season}" required=""/>
</div>

<g:if test="${user == "admin"}">
    <div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'user', 'error')} required">
        <label for="user">
            <g:message code="fantasyTeam.user.label" default="User" />
            <span class="required-indicator">*</span>
        </label>
        <g:select id="user" name="user.id" from="${com.traderapist.security.User.list()}" optionKey="id" optionValue="username" required="" value="${fantasyTeamInstance?.user?.id}" class="many-to-one"/>
    </div>
</g:if>
<g:else>
    <g:hiddenField id="user" name="user.id" value="${ sec.loggedInUserInfo(field: "id") }"/>
</g:else>

<g:hiddenField name="scoringSystem"/>
