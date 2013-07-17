<%@ page import="com.traderapist.constants.FantasyConstants; com.traderapist.models.ScoringSystem" %>



<div class="fieldcontain ${hasErrors(bean: scoringSystemInstance, field: 'name', 'error')} ">
    <label for="name">
        <g:message code="scoringSystem.name.label" default="Name"/>

    </label>
    <g:textField name="name" value="${scoringSystemInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: scoringSystemInstance, field: 'scoringRules', 'error')} ">
    <label for="scoringRules">
        <g:message code="scoringSystem.scoringRules.label" default="Scoring Rules"/>

    </label>
    <g:select name="scoringRules" from="${com.traderapist.models.ScoringRule.list()}" multiple="multiple" optionKey="id"
              size="5" optionValue="${{FantasyConstants.statTranslation.get(it.statKey) }}" class="many-to-many"/>
</div>

