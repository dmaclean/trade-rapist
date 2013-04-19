<%--
  Created by IntelliJ IDEA.
  User: dmaclean
  Date: 4/5/13
  Time: 7:31 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<div class="container-fluid" ng-controller="DraftController">
    <div id="draft_init" class="row-fluid">
        <input type="text" value="" id="num_owners" name="num_owners"/>
        <select name="my_pick">

        </select>
    </div>
    <div class="row-fluid">
        <div class="span2">
            <h3>Quarterbacks</h3>
            <ul>
                <li ng-repeat="player in players | filter:'QUARTERBACK'">
                    {{player.name}} - {{ calculateValue(player.adp, player.vorp, 1) }}
                </li>
            </ul>
        </div>
        <div class="span2">
            <h3>Running Backs</h3>
            <ol>
                <li ng-repeat="player in players | filter:'RUNNING_BACK'">
                    {{player.name}} - {{ calculateValue(player.adp, player.vorp, 1) }}
                </li>
            </ol>
        </div>
        <div class="span2">
            <h3>Wide Receivers</h3>
            <ul>
                <li ng-repeat="player in players | filter:'WIDE_RECEIVER'">
                    {{player.name}} - {{ calculateValue(player.adp, player.vorp, 1) }}
                </li>
            </ul>
        </div>
        <div class="span2">
            <h3>Tight Ends</h3>
            <ul>
                <li ng-repeat="player in players | filter:'TIGHT_END'">
                    {{player.name}} - {{ calculateValue(player.adp, player.vorp, 1) }}
                </li>
            </ul>
        </div>
        <div class="span2">
            <h3>Defenses</h3>
            <ul>
                <li ng-repeat="player in players | filter:'DEFENSE'">
                    {{player.name}} - {{ calculateValue(player.adp, player.vorp, 1) }}
                </li>
            </ul>
        </div>
        <div class="span2">
            <h3>Kickers</h3>
            <ul>
                <li ng-repeat="player in players | filter:'KICKER'">
                    {{player.name}} - {{ calculateValue(player.adp, player.vorp, 1) }}
                </li>
            </ul>
        </div>
    </div>
</div>