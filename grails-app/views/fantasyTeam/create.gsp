<%@ page import="com.traderapist.constants.FantasyConstants; com.traderapist.models.FantasyTeam" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fantasyTeam.label', default: 'FantasyTeam')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-fantasyTeam" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-fantasyTeam" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${fantasyTeamInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${fantasyTeamInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>

        <!-- Div for filling out ESPN scoring systems.  This is initially hidden until the user wants to fill it out. -->
            <div id="scoring" class="modal hide fade">
                Scoring System name: <g:textField id="ss_name" name="ss_name"/>

                <!--
                   Passing

                   Every 5 passing yards = 0.2
                   40+ yard TD pass bonus = 2
                   2pt passing conversion = 2
                   TD pass = 4
                   Interception = -2
                -->
                Passing Yards: <g:textField id="stat_multiplier_${ FantasyConstants.STAT_PASSING_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_PASSING_YARDS }" value="0.04"/><br/>
                Passing TDs: <g:textField id="stat_multiplier_${ FantasyConstants.STAT_PASSING_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_PASSING_TOUCHDOWNS }" value="4"/>

                <button id="ss_submit" name="submit">Save Scoring System</button>

                            <!--
                   Rushing

                   Rushing yards = 0.1
                   40+ yard TD rush bonus = 2
                   TD rush = 6
                   2pt rushing conversion = 2


                   Receiving

                   Receiving yards = 0.1
                   40+ yard TD rec bonus = 2
                   TD reception = 6
                   2pt receiving conversion = 2


                   Miscellaneous

                   Kickoff return TD = 6
                   Fumble recovered for TD = 6
                   Fumble return td = 6
                   Punt return TD = 6
                   Total Fumbles Lost = -2


                   Kicking

                   Each PAT made = 1
                   FG made 0-39 yards = 3
                   FG made 40-49 yards = 4
                   FG made 50+ yards = 5
                   FG missed 0-39 yards = -2
                   FG missed 40-49 yards = -1


                   Team Defense/Special Teams

                   Sack = 1
                   Interception return TD = 3
                   Fumble return TD = 3
                   Kickoff return TD = 3
                   Punt return TD = 3
                   Blocked punt or FG returned for TD = 4
                   Blocked punt, PAT, or FG = 2
                   Interception = 2
                   Fumble recovered = 2
                   Safety = 2
                   0 points allowed = 10
                   1-6 points allowed = 7
                   7-13 points allowed = 4
                   14-17 points allowed = 1
                   22-27 points allowed = -1
                   28-34 points allowed = -4
                   35-45 points allowed = -7
                   46+ points allowed = -10
                -->
            </div>

			<g:form action="save" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
