trade-rapist
============

Web application to utilize data scraped from Yahoo! and provide recommendations with drafting and trading in Fantasy Football.


Regexes
--------
Yahoo! player projections
<tr.*?<a href=\"http:\/\/sports\.yahoo\.com\/nfl\/players\/(\d+)\".*?>([ A-Za-z\-\.\']+)<\/a>.*?<td.*?class=\"Alt Fw-b Ta-end Nowrap Selected\".*?>(<a.*?>)?([\d\.]+)(<\/a>)?<\/td>.*?<\/tr>

Yahoo! team projections
<tr.*?<a href=\"http:\/\/sports\.yahoo\.com\/nfl\/teams\/(\w+)\".*?>([ A-Za-z\-\.\']+)<\/a>.*?<td.*?class=\"Alt Fw-b Ta-end Nowrap Selected\".*?>(<a.*?>)?([\d\.]+)(<\/a>)?<\/td>.*?<\/tr>
