<%--
  Created by IntelliJ IDEA.
  User: dmaclean
  Date: 2/25/13
  Time: 6:04 AM
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html ng-app="TradeRapist">
<head>
  <title>Trade Rapist Draft Central</title>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.5/angular.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="${createLinkTo(dir: 'js', file: 'draftcontroller.js')}"></script>
    <link href="${createLinkTo(dir: 'css', file: 'bootstrap.min.css')}" rel="stylesheet" media="screen"/>
    <style>
    body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
    }
    </style>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container-fluid" ng-controller="DraftController">
        <div id="draft_init" class="row-fluid">
            <input type="text" value="" id="num_owners" name="num_owners"/>
            <input type="text" value="1" id="my_pick" name="my_pick"/>
            <button id="start_draft_button">Start Draft!</button>
        </div>
        <div class="row-fluid">
            <div class="span2">
                <h3>Quarterbacks</h3>
                <ul>
                    <!--<li ng-repeat="player in players | filter:'QUARTERBACK'">
                        {{player.name}} - {{ calculateValue(player.adp, player.vorp, 1) }}
                    </li>-->
                    <g:each in="players[0]" var="p">
                        <li>
                             ${p.name} -
                        </li>
                    </g:each>
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
</body>
</html>