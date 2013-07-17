<%@ page import="com.traderapist.constants.FantasyConstants; com.traderapist.models.ScoringRule" %>



<div class="fieldcontain ${hasErrors(bean: scoringRuleInstance, field: 'statKey', 'error')} required">
    <label for="statKey">
        <g:message code="scoringRule.statKey.label" default="Stat Key"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select name="statKey" from="${ FantasyConstants.statTranslation.entrySet() }" optionKey="key" optionValue="value"/>
</div>

<div class="fieldcontain ${hasErrors(bean: scoringRuleInstance, field: 'multiplier', 'error')} required">
    <label for="multiplier">
        <g:message code="scoringRule.multiplier.label" default="Multiplier"/>
        <span class="required-indicator">*</span>
    </label>
    <g:field name="multiplier" value="${fieldValue(bean: scoringRuleInstance, field: 'multiplier')}" required=""/>
</div>

