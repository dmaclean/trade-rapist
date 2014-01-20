trade-rapist
============
<p>Web application to utilize projections (either scraped from Yahoo! or custom-made) and provide recommendations with drafting and trading in Fantasy Football.</p>
<p>Users will have the opportunity to sign up and configure their fantasy teams.  Upon doing so, they'll have access to "Draft Central", an AngularJS-based tool that helps throughout the user's draft by identifying the most valuable players at any given point in the draft.</p>
<p>We use a unique formula based on projections, ADP, VORP/scarcity, and the user's current roster needs to determine value during each and every pick in the draft.  This helps users make optimal decisions.</p>


Regexes (helpful for me only)
--------
Yahoo! player projections
<tr.*?<a href=\"http:\/\/sports\.yahoo\.com\/nfl\/players\/(\d+)\".*?>([ A-Za-z\-\.\']+)<\/a>.*?<td.*?class=\"Alt Fw-b Ta-end Nowrap Selected\".*?>(<a.*?>)?([\d\.]+)(<\/a>)?<\/td>.*?<\/tr>

Yahoo! team projections
<tr.*?<a href=\"http:\/\/sports\.yahoo\.com\/nfl\/teams\/(\w+)\".*?>([ A-Za-z\-\.\']+)<\/a>.*?<td.*?class=\"Alt Fw-b Ta-end Nowrap Selected\".*?>(<a.*?>)?([\d\.]+)(<\/a>)?<\/td>.*?<\/tr>
