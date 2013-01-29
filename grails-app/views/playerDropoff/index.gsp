<%--
  Created by IntelliJ IDEA.
  User: dmaclean
  Date: 1/27/13
  Time: 2:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:javascript src="jquery.min.js" />
    <g:javascript src="charts.js" />

    <script type="text/javascript">
        google.load("visualization", "1", {packages:["corechart"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var arr = [
                ['Player Rank', 'Quarterbacks', 'Runningbacks', 'Wide Receivers', 'Tight Ends', 'Kickers', 'Defenses']
            ]
            <g:each in="${chartData}" var="c">
                arr.push([${c[0]}, ${c[1]}, ${c[2]}, ${c[3]}, ${c[4]}, ${c[5]}, ${c[6]}])
            </g:each>

            var data = google.visualization.arrayToDataTable(arr);
            var options = {
                title: 'Player dropoff'
            };

            var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
            chart.draw(data, options);
        }
    </script>
  <title></title>
</head>
<body>
    <p>
        <g:form action="index" method="POST">
            <g:select name="seasons" from="${seasons}"/>
            <g:submitButton name="submit"/>
        </g:form>
    </p>

    <div id="chart_div" style="width: 900px; height: 500px;"></div>

    <h1>Quarterbacks</h1>
    <g:set var="tierNum" value="${1}"/>
    <g:each in="${quarterbackTiers}" var="qbTier">
        <h2>Tier ${tierNum}</h2>
        <g:each in="${qbTier}" var="player">
            <p>${player[0].name} - ${player[1].points}</p>
        </g:each>

        <g:set var="tierNum" value="${tierNum + 1}"/>
    </g:each>

    <h1>Running Backs</h1>
    <g:set var="tierNum" value="${1}"/>
    <g:each in="${runningbackTiers}" var="rbTier">
        <h2>Tier ${tierNum}</h2>
        <g:each in="${rbTier}" var="player">
            <p>${player[0].name} - ${player[1].points}</p>
        </g:each>

        <g:set var="tierNum" value="${tierNum + 1}"/>
    </g:each>

    <h1>Wide Receivers</h1>
    <g:set var="tierNum" value="${1}"/>
    <g:each in="${receiverTiers}" var="wrTier">
        <h2>Tier ${tierNum}</h2>
        <g:each in="${wrTier}" var="player">
            <p>${player[0].name} - ${player[1].points}</p>
        </g:each>

        <g:set var="tierNum" value="${tierNum + 1}"/>
    </g:each>

    <h1>Tight Ends</h1>
    <g:set var="tierNum" value="${1}"/>
    <g:each in="${tightEndTiers}" var="teTier">
        <h2>Tier ${tierNum}</h2>
        <g:each in="${teTier}" var="player">
            <p>${player[0].name} - ${player[1].points}</p>
        </g:each>

        <g:set var="tierNum" value="${tierNum + 1}"/>
    </g:each>

    <h1>Kickers</h1>
    <g:set var="tierNum" value="${1}"/>
    <g:each in="${kickerTiers}" var="kTier">
        <h2>Tier ${tierNum}</h2>
        <g:each in="${kTier}" var="player">
            <p>${player[0].name} - ${player[1].points}</p>
        </g:each>

        <g:set var="tierNum" value="${tierNum + 1}"/>
    </g:each>

    <h1>Defenses</h1>
    <g:set var="tierNum" value="${1}"/>
    <g:each in="${teamTiers}" var="teamTier">
        <h2>Tier ${tierNum}</h2>
        <g:each in="${teamTier}" var="player">
            <p>${player[0].name} - ${player[1].points}</p>
        </g:each>

        <g:set var="tierNum" value="${tierNum + 1}"/>
    </g:each>
</body>
</html>