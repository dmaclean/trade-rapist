<head>
    <meta name="layout" content="main">
</head>
<body>
    <table>
        <thead>
        <th>Player</th>
        <th>Injury Date</th>
        <th>Return Date</th>
        <th>Details</th>
        </thead>
        <tbody>
        <g:each in="${injuries}" var="injury">
            <tr>
                <td>${injury.player.name}</td>
                <td><g:formatDate format="MM-dd-yyyy" date="${injury.injuryDate}"/></td>
                <td><g:formatDate format="MM-dd-yyyy" date="${injury.returnDate}"/></td>
                <td>${injury.details}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
</body>