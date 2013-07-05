/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 4/18/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
angular.module('TradeRapistE2E', ['TradeRapist', 'ngMockE2E'])
    .run(function($httpBackend) {
        var players =   [{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
                        {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
            {id: 3, name: 'RB 1', position: 'RUNNING_BACK', points: 250.0, adp: 2.5, vorp: 30.0},
            {id: 4, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
            {id: 5, name: 'WR 2', position: 'WIDE_RECEIVER', points: 200.0, adp: 2.5, vorp: 20.0},
            {id: 6, name: 'WR 1', position: 'WIDE_RECEIVER', points: 180.0, adp: 5.0, vorp: 0},
            {id: 7, name: 'TE 2', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
            {id: 8, name: 'TE 1', position: 'TIGHT_END', points: 175.0, adp: 5.0, vorp: 0},
            {id: 9, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
            {id: 10, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
            {id: 11, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
            {id: 12, name: 'K 2', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}];

        var players_need_test =   [{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
            {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 5.0},
            {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 275.0, adp: 9.0, vorp: 0},
            {id: 4, name: 'RB 1', position: 'RUNNING_BACK', points: 250.0, adp: 2.5, vorp: 30.0},
            {id: 5, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
            {id: 6, name: 'WR 2', position: 'WIDE_RECEIVER', points: 200.0, adp: 2.5, vorp: 20.0},
            {id: 7, name: 'WR 1', position: 'WIDE_RECEIVER', points: 180.0, adp: 5.0, vorp: 0},
            {id: 8, name: 'TE 2', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
            {id: 9, name: 'TE 1', position: 'TIGHT_END', points: 175.0, adp: 5.0, vorp: 0},
            {id: 10, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
            {id: 11, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
            {id: 12, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
            {id: 13, name: 'K 2', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}];

        var players_2001 =   [{id: 1, name: 'QB 1_2001', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
            {id: 2, name: 'QB 2_2001', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
            {id: 3, name: 'RB 1_2001', position: 'RUNNING_BACK', points: 250.0, adp: 2.5, vorp: 30.0},
            {id: 4, name: 'RB 2_2001', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
            {id: 5, name: 'WR 2_2001', position: 'WIDE_RECEIVER', points: 200.0, adp: 2.5, vorp: 20.0},
            {id: 6, name: 'WR 1_2001', position: 'WIDE_RECEIVER', points: 180.0, adp: 5.0, vorp: 0},
            {id: 7, name: 'TE 2_2001', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
            {id: 8, name: 'TE 1_2001', position: 'TIGHT_END', points: 175.0, adp: 5.0, vorp: 0},
            {id: 9, name: 'DEF 1_2001', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
            {id: 10, name: 'DEF 2_2001', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
            {id: 11, name: 'K 1_2001', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
            {id: 12, name: 'K 2_2001', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}];

        $httpBackend.whenGET('draft/players?year=2013').respond(players);
        $httpBackend.whenGET('draft/players?year=2012').passThrough();
        $httpBackend.whenGET('draft/players?year=2002').respond(players);
        $httpBackend.whenGET('draft/players?year=2001').respond(players_2001);
        $httpBackend.whenGET('draft/players?year=2000').respond(players_need_test);

        $httpBackend.whenGET('partials/draft.html').passThrough();

        $httpBackend.whenGET('login/whoami').respond("dmaclean");

        $httpBackend.whenGET('fantasyTeam/list?json=true').respond("[" +
            "{\"class\":\"com.traderapist.models.FantasyTeam\",\"id\":1,\"fantasyLeagueType\":{\"class\":\"FantasyLeagueType\",\"id\":1},\"leagueId\":\"106647\",\"name\":\"Team Dan Mac\",\"season\":2013,\"user\":{\"class\":\"User\",\"id\":3}}," +
            "{\"class\":\"com.traderapist.models.FantasyTeam\",\"id\":2,\"fantasyLeagueType\":{\"class\":\"FantasyLeagueType\",\"id\":2},\"leagueId\":\"1111\",\"name\":\"Terror Squid\",\"season\":2013,\"user\":{\"class\":\"User\",\"id\":3}}" +
        "]");
    });