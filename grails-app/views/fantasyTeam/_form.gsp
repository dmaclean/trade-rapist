<%@ page import="com.traderapist.models.Player; com.traderapist.constants.FantasyConstants; com.traderapist.models.FantasyTeam; com.traderapist.security.User" %>
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

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'numOwners', 'error')} required">
    <label for="numOwners">
        <g:message code="fantasyTeam.season.label" default="Number of Owners" />
        <span class="required-indicator">*</span>
    </label>

    <g:field name="numOwners" type="number" value="${fantasyTeamInstance?.numOwners}" required="" placeholder="10"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'QB', 'error')} required">
    <label for="QB">
        <g:message code="fantasyTeam.season.label" default="# Startable Quarterbacks" />
        <span class="required-indicator">*</span>
    </label>

    <g:set value="${starters[Player.POSITION_QB]}" var="startableQBs"/>
    <g:field name="QB" type="number" value="${startableQBs}" required="" placeholder="1"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'RB', 'error')} required">
    <label for="RB">
        <g:message code="fantasyTeam.season.label" default="# Startable Running Backs" />
        <span class="required-indicator">*</span>
    </label>

    <g:set value="${starters[Player.POSITION_RB]}" var="startableRBs"/>
    <g:field name="RB" type="number" value="${startableRBs}" required="" placeholder="2"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'WR', 'error')} required">
    <label for="WR">
        <g:message code="fantasyTeam.season.label" default="# Startable Wide Receivers" />
        <span class="required-indicator">*</span>
    </label>

    <g:set value="${starters[Player.POSITION_WR]}" var="startableWRs"/>
    <g:field name="WR" type="number" value="${startableWRs}" required="" placeholder="3"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'TE', 'error')} required">
    <label for="TE">
        <g:message code="fantasyTeam.season.label" default="# Startable Tight Ends" />
        <span class="required-indicator">*</span>
    </label>

    <g:set value="${starters[Player.POSITION_TE]}" var="startableTEs"/>
    <g:field name="TE" type="number" value="${startableTEs}" required="" placeholder="1"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'DEF', 'error')} required">
    <label for="DEF">
        <g:message code="fantasyTeam.season.label" default="# Startable Defenses" />
        <span class="required-indicator">*</span>
    </label>

    <g:set value="${starters[Player.POSITION_DEF]}" var="startableDEFs"/>
    <g:field name="DEF" type="number" value="${startableDEFs}" required="" placeholder="1"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fantasyTeamInstance, field: 'K', 'error')} required">
    <label for="K">
        <g:message code="fantasyTeam.season.label" default="# Startable Kickers" />
        <span class="required-indicator">*</span>
    </label>

    <g:set value="${starters[Player.POSITION_K]}" var="startableKs"/>
    <g:field name="K" type="number" value="${startableKs}" required="" placeholder="1"/>
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
