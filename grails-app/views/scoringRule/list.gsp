<%@ page import="com.traderapist.constants.FantasyConstants; com.traderapist.models.ScoringRule" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'scoringRule.label', default: 'ScoringRule')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-scoringRule" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                  default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-scoringRule" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="statKey"
                              title="${message(code: 'scoringRule.statKey.label', default: 'Stat Key')}"/>

            <g:sortableColumn property="multiplier"
                              title="${message(code: 'scoringRule.multiplier.label', default: 'Multiplier')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${scoringRuleInstanceList}" status="i" var="scoringRuleInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${scoringRuleInstance.id}">${ FantasyConstants.statTranslation.get(scoringRuleInstance.statKey) }</g:link></td>

                <td>${fieldValue(bean: scoringRuleInstance, field: "multiplier")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${scoringRuleInstanceTotal}"/>
    </div>
</div>
</body>
</html>
