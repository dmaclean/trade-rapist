<%@ page import="com.traderapist.constants.FantasyConstants; com.traderapist.models.FantasyTeam" %>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'fantasyTeam.label', default: 'Fantasy Team')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
    <div class="row-fluid">
        <div class="span8" role="navigation">
            <g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>
        </div>
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
        <g:form name="fantasy_team_form" action="saveAjax" >
            <fieldset>
                <g:render template="form"/>
            </fieldset>
            <fieldset>
                <g:submitButton id="fantasy_team_submit" name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
            </fieldset>
        </g:form>

        %{--<div class="row-fluid">--}%
            %{--<div class="span8">--}%
                %{----}%
            %{--</div>--}%
        %{--</div>--}%
        %{--<button id="create_system" class="btn btn-primary" type="button" data-toggle="modal" data-target="#scoring">Create Scoring System</button>--}%
    </div>

<!-- Div for filling out ESPN scoring systems.  This is initially hidden until the user wants to fill it out. -->
<div id="scoring" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>Create Scoring System for fantasy team</h3>
    </div>
    <div class="modal-body">
        <div class="alert alert-info">
            You'll need to create a scoring system for your fantasy team.  This lets
            us know how valuable each rushing touchdown, passing yard, etc. is, and
            will help with calculating player values for the draft.<br/><br/>
            For example, if we know that you're in a PPR league then tight ends will instantly
            become more valuable than if you receptions were worth 0 points.<br/><br/>
            <strong>
                You'll have to do this eventually to be able to use the Draft feature.  If you don't
                have time or don't know your league's scoring system then you can create it later.
            </strong>
        </div>
        <fieldset>
            <label>Scoring System name</label>
            <input id="ss_name" name="ss_name" type="text" placeholder="MyLeague scoring system">

            <!--
               Passing

               Every 5 passing yards = 0.2
               40+ yard TD pass bonus = 2
               2pt passing conversion = 2
               TD pass = 4
               Interception = -2
            -->
            <h3>Passing</h3>
            <label>Passing Yards (points per yard)</label>
            <g:textField class="" id="stat_multiplier_${ FantasyConstants.STAT_PASSING_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_PASSING_YARDS }" value="0.04"/>

            <label>40+ Yard TD Pass Bonus</label>
            <g:textField class="" id="stat_multiplier_${ FantasyConstants.STAT_FORTY_PLUS_YARD_PASSING_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_FORTY_PLUS_YARD_PASSING_TOUCHDOWNS }" value="2"/>

            <label>2-point Passing Conversion</label>
            <g:textField class="" id="stat_multiplier_${ FantasyConstants.STAT_TWO_POINT_CONVERSIONS }" name="stat_multiplier_${ FantasyConstants.STAT_TWO_POINT_CONVERSIONS }" value="2"/>

            <label>Passing Touchdowns</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_PASSING_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_PASSING_TOUCHDOWNS }" value="4"/>

            <label>Interception</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_INTERCEPTIONS }" name="stat_multiplier_${ FantasyConstants.STAT_INTERCEPTIONS }" value="-2"/>

            <!--
               Rushing

               Rushing yards = 0.1
               40+ yard TD rush bonus = 2
               TD rush = 6
               2pt rushing conversion = 2
            -->
            <h3>Rushing</h3>
            <label>Rushing Yards (points per yard)</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_RUSHING_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_RUSHING_YARDS }" value="0.1"/>

            <label>40+ yard TD Rush Bonus</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FORTY_PLUS_YARD_RUSHING_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_FORTY_PLUS_YARD_RUSHING_TOUCHDOWNS }" value="2"/>

            <label>Rushing Touchdown</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_RUSHING_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_RUSHING_TOUCHDOWNS }" value="6"/>

            <label>2-point Rushing Conversion</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_TWO_POINT_CONVERSIONS }" name="stat_multiplier_${ FantasyConstants.STAT_TWO_POINT_CONVERSIONS }" value="2"/>

            <!--
                Receiving

               Receiving yards = 0.1
               40+ yard TD rec bonus = 2
               TD reception = 6
               2pt receiving conversion = 2
            -->
            <h3>Receiving</h3>
            <label>Receiving Yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_RECEPTION_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_RECEPTION_YARDS }" value="0.1"/>

            <label>40+ yard TD Reception Bonus</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FORTY_PLUS_YARD_RECEPTION_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_FORTY_PLUS_YARD_RECEPTION_TOUCHDOWNS }" value="2"/>

            <label>Receiving Touchdown</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_RECEPTION_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_RECEPTION_TOUCHDOWNS }" value="6"/>

            <label>2-point Receiving Conversion</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_TWO_POINT_CONVERSIONS }" name="stat_multiplier_${ FantasyConstants.STAT_TWO_POINT_CONVERSIONS }" value="2"/>

            <!--
                Miscellaneous

               Kickoff return TD = 6
               Fumble recovered for TD = 6
               Fumble return td = 6         -- Not sure the difference between this and the one above
               Punt return TD = 6
               Total Fumbles Lost = -2
            -->
            <h3>Miscellaneous</h3>
            <!--<label>Kickoff Return TD</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS }" value="6"/>-->

            <!--<label>Fumble recovered for TD</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_TOUCHDOWN }" name="stat_multiplier_${ FantasyConstants.STAT_TOUCHDOWN }" value="6"/>-->

            <label>Punt Return TD</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS }" value="6"/>

            <label>Fumble Lost</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FUMBLES_LOST }" name="stat_multiplier_${ FantasyConstants.STAT_FUMBLES_LOST }" value="-2"/>


            <!--
                Kicking

               Each PAT made = 1
               FG made 0-39 yards = 3
               FG made 40-49 yards = 4
               FG made 50+ yards = 5
               FG missed 0-39 yards = -2
               FG missed 40-49 yards = -1
            -->
            <h3>Kicking</h3>
            <label>Each PAT made</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINT_AFTER_ATTEMPT_MADE }" name="stat_multiplier_${ FantasyConstants.STAT_POINT_AFTER_ATTEMPT_MADE }" value="1"/>

            <label>FG made 0-19 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_0_19_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_0_19_YARDS }" value="3"/>

            <label>FG made 20-29 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_20_29_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_20_29_YARDS }" value="3"/>

            <label>FG made 30-39 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_30_39_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_30_39_YARDS }" value="3"/>

            <label>FG made 40-49 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_40_49_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_40_49_YARDS }" value="4"/>

            <label>FG made 50+ yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_50_PLUS_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_50_PLUS_YARDS }" value="5"/>

            <label>FG missed 0-19 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_0_19_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_0_19_YARDS }" value="-2"/>

            <label>FG missed 20-29 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_20_29_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_20_29_YARDS }" value="-2"/>

            <label>FG missed 30-39 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_30_39_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_30_39_YARDS }" value="-3"/>

            <label>FG missed 40-49 yards</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_40_49_YARDS }" name="stat_multiplier_${ FantasyConstants.STAT_FIELD_GOALS_MISSED_40_49_YARDS }" value="-4"/>


            <!--
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
            <h3>Team Defense/Special Teams</h3>
            <label>Sack</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_SACK }" name="stat_multiplier_${ FantasyConstants.STAT_SACK }" value="1"/>

            <label>Defensive TD</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_TOUCHDOWN }" name="stat_multiplier_${ FantasyConstants.STAT_TOUCHDOWN }" value="1"/>

            <label>Kickoff/Punt Return TD</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS }" name="stat_multiplier_${ FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS }" value="4"/>

            <label>Blocked punt or FG</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_BLOCK_KICK }" name="stat_multiplier_${ FantasyConstants.STAT_BLOCK_KICK }" value="2"/>

            <label>Interception</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_INTERCEPTION }" name="stat_multiplier_${ FantasyConstants.STAT_INTERCEPTION }" value="2"/>

            <label>Fumble Recovery</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_FUMBLE_RECOVERY }" name="stat_multiplier_${ FantasyConstants.STAT_FUMBLE_RECOVERY }" value="2"/>

            <label>Safety</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_SAFETY }" name="stat_multiplier_${ FantasyConstants.STAT_SAFETY }" value="2"/>

            <label>0 Points Allowed</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_0 }" name="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_0 }" value="10"/>

            <label>1-6 Points Allowed</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_1_6 }" name="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_1_6 }" value="7"/>

            <label>7-13 Points Allowed</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_7_13 }" name="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_7_13 }" value="4"/>

            <label>14-20 Points Allowed</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_14_20 }" name="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_14_20 }" value="1"/>

            <label>21-27 Points Allowed</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_21_27 }" name="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_21_27 }" value="-1"/>

            <label>28-34 Points Allowed</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_28_34 }" name="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_28_34 }" value="-4"/>

            <label>35+ Points Allowed</label>
            <g:textField id="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_35_PLUS }" name="stat_multiplier_${ FantasyConstants.STAT_POINTS_ALLOWED_35_PLUS }" value="-7"/>

            <!-- Reference to the newly-created fantasy team -->
            <g:hiddenField name="fantasy_team_id" id="fantasy_team_id" />
        </fieldset>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Do this later</button>
        <button id="ss_submit" name="submit" class="btn btn-primary">Save Scoring System</button>
    </div>

</div>
</body>
