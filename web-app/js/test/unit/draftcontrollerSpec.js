/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 4/2/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */

describe('DraftController spec', function() {
    var scope, ctrl, $httpBackend;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
        $httpBackend = _$httpBackend_;
        $httpBackend.whenGET('login/whoami').respond("dmaclean");
        $httpBackend.whenGET('fantasyTeam/list?json=true').respond("[" +
            "{\"class\":\"com.traderapist.models.FantasyTeam\",\"id\":1,\"fantasyLeagueType\":{\"class\":\"FantasyLeagueType\",\"id\":1},\"leagueId\":\"106647\",\"name\":\"Team Dan Mac\",\"season\":2013,\"user\":{\"class\":\"User\",\"id\":3}}]");
        scope = $rootScope.$new();
        ctrl = $controller(DraftController, {$scope: scope});
    }));

    it('should default current pick to 1', function(){
        expect(scope.currentPick).toEqual(1);
    });

    it('should default owners to empty list', function() {
        expect(scope.owners).toEqual({});
    });

    it('should default numOwners to 0', function() {
        expect(scope.numOwners).toEqual(0);
    });

    it('should default myPick to 1', function() {
        expect(scope.myPick).toEqual(1);
    });

    it('should default initNumOwners to false', function() {
        expect(scope.initNumOwners).toEqual(false);
    });

    it('should default initialized to false', function() {
        expect(scope.initialized).toEqual(false);
    });

    it('should default currentPick to 1', function() {
        expect(scope.currentPick).toEqual(1);
    });

    it('should default maxOwnersPerRow to 6', function() {
        expect(scope.maxOwnersPerRow).toEqual(6);
    });

    it('should default draftType to SNAKE', function() {
        expect(scope.draftType).toEqual(scope.DRAFT_TYPE_SNAKE);
    });

    it('should default draftYear to current year', function() {
        expect(scope.draftYear).toEqual(new Date().getFullYear());
    });

    it('should default replacements to empty hash', function() {
        expect(scope.replacements).toEqual({});
    });

    it('should default username to empty hash', function() {
        expect(scope.username).toEqual(undefined);
    });

    it('should default leagues list to undefined', function() {
        expect(scope.leagues).toEqual(undefined);
    });

    it('should default selectedLeague to undefined', function() {
        expect(scope.selectedLeague).toEqual(undefined);
    });

    describe('Loading players from HTTP request', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            $httpBackend = _$httpBackend_;
            $httpBackend.whenGET('draft/players?year=' + new Date().getFullYear()).
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
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
                    {id: 12, name: 'K 2', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}]);

            scope = $rootScope.$new();
            ctrl = $controller(DraftController, {$scope: scope});

            scope.numOwners = 2;
            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 1;
            scope.startablePositions[scope.WIDE_RECEIVER] = 1;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;
        }));

        it ('should create players model with two players', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.players).toEqual([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
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
                {id: 12, name: 'K 2', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}]);
        });
    });

    describe('replacement players', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 3;
            scope.draftYear = 2001;

            $httpBackend = _$httpBackend_;

            // Use this to test with 10 players at each position.
            $httpBackend.whenGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'RB 1', position: 'RUNNING_BACK', points: 250.0, adp: 2.5, vorp: 30.0},
                    {id: 12, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 15, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'WR 1', position: 'WIDE_RECEIVER', points: 200.0, adp: 2.5, vorp: 20.0},
                    {id: 22, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 23, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 24, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 25, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 27, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 32, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 33, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 34, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 35, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 36, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 37, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 39, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 42, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 43, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 44, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 45, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 46, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 47, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 48, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 49, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 50, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 52, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 54, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 55, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 56, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 57, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 58, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 59, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 60, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0}]);
        }));

        it('should have replacement QB at #3 for 3 owners and 1 starting QB', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.replacements[scope.QUARTERBACK]).toEqual(scope.available_qbs[2]);
        });

        it('should have replacement RB at #6 for 3 owners and 2 starting RBs', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.replacements[scope.RUNNING_BACK]).toEqual(scope.available_rbs[5]);
        });

        it('should have replacement WR at #9 for 3 owners and 3 starting WRs', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.replacements[scope.WIDE_RECEIVER]).toEqual(scope.available_wrs[8]);
        });

        it('should have replacement TE at #3 for 3 owners and 1 starting TE', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.replacements[scope.TIGHT_END]).toEqual(scope.available_tes[2]);
        });

        it('should have replacement DEF at #3 for 3 owners and 1 starting DEF', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.replacements[scope.DEFENSE]).toEqual(scope.available_ds[2]);
        });

        it('should have replacement K at #3 for 3 owners and 1 starting K', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.replacements[scope.KICKER]).toEqual(scope.available_ks[2]);
        });

        /*
         * Check VORP values for Quarterbacks.
         */
        it('should have QB1 with VORP of 30', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_qbs[0].vorp).toEqual(30);
        });

        it('should have QB2 with VORP of 10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_qbs[1].vorp).toEqual(10);
        });

        it('should have QB3 with VORP of 0', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_qbs[2].vorp).toEqual(0);
        });

        it('should have QB4 with VORP of -10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_qbs[3].vorp).toEqual(-10);
        });

        /*
         * Check VORP values for Running backs.
         */
        it('should have RB1 with VORP of 70', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[0].vorp).toEqual(70);
        });

        it('should have RB2 with VORP of 40', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[1].vorp).toEqual(40);
        });

        it('should have RB3 with VORP of 30', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[2].vorp).toEqual(30);
        });

        it('should have RB4 with VORP of 20', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[3].vorp).toEqual(20);
        });

        it('should have RB5 with VORP of 10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[4].vorp).toEqual(10);
        });

        it('should have RB6 with VORP of 0', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[5].vorp).toEqual(0);
        });

        it('should have RB7 with VORP of -10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[6].vorp).toEqual(-10);
        });


        /*
         * Check VORP values for Wide Receivers
         */
        it('should have WR1 with VORP of 80', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[0].vorp).toEqual(80);
        });

        it('should have WR2 with VORP of 70', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[1].vorp).toEqual(70);
        });

        it('should have WR3 with VORP of 60', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[2].vorp).toEqual(60);
        });

        it('should have WR4 with VORP of 50', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[3].vorp).toEqual(50);
        });

        it('should have WR5 with VORP of 40', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[4].vorp).toEqual(40);
        });

        it('should have WR6 with VORP of 30', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[5].vorp).toEqual(30);
        });

        it('should have WR7 with VORP of 20', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[6].vorp).toEqual(20);
        });

        it('should have WR8 with VORP of 10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[7].vorp).toEqual(10);
        });

        it('should have WR9 with VORP of 0', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[8].vorp).toEqual(0);
        });

        it('should have WR10 with VORP of -10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[9].vorp).toEqual(-10);
        });


        /*
         * Check VORP values for Tight Ends
         */
        it('should have TE1 with VORP of 20', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_tes[0].vorp).toEqual(20);
        });

        it('should have TE2 with VORP of 10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_tes[1].vorp).toEqual(10);
        });

        it('should have TE3 with VORP of 0', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_tes[2].vorp).toEqual(0);
        });

        it('should have TE4 with VORP of -10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_tes[3].vorp).toEqual(-10);
        });


        /*
         * Check VORP values for Defenses
         */
        it('should have D1 with VORP of 30', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ds[0].vorp).toEqual(30);
        });

        it('should have D2 with VORP of 10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ds[1].vorp).toEqual(10);
        });

        it('should have D3 with VORP of 0', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ds[2].vorp).toEqual(0);
        });

        it('should have D4 with VORP of -10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ds[3].vorp).toEqual(-10);
        });

        /*
         * Check VORP values for Kickers
         */
        it('should have K1 with VORP of 20', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ks[0].vorp).toEqual(20);
        });

        it('should have K2 with VORP of 10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ks[1].vorp).toEqual(10);
        });

        it('should have K3 with VORP of 0', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ks[2].vorp).toEqual(0);
        });

        it('should have K4 with VORP of -10', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ks[3].vorp).toEqual(-10);
        });
    });

    describe('Replacement players - drafting after replacement player', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 3;
            scope.draftYear = 2001;

            $httpBackend = _$httpBackend_;

            // Use this to test with 10 players at each position.
            $httpBackend.whenGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'RB 1', position: 'RUNNING_BACK', points: 250.0, adp: 2.5, vorp: 30.0},
                    {id: 12, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 15, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'WR 1', position: 'WIDE_RECEIVER', points: 200.0, adp: 2.5, vorp: 20.0},
                    {id: 22, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 23, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 24, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 25, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 27, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 32, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 33, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 34, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 35, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 36, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 37, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 39, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 42, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 43, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 44, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 45, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 46, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 47, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 48, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 49, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 50, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 52, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 54, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 55, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 56, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 57, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 58, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 59, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 60, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0}]);
        }));

        /**
         * One startable QB per team, so #3 should be the replacement.
         * Someone will draft #4 so #2 should become the replacement.
         */
        it('should move replacement player (QB) up one index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd quarterback is the replacement
            expect(scope.replacements[scope.QUARTERBACK]).toEqual(scope.available_qbs[2]);

            // Draft the 4th quarterback.
            scope.draftPlayer(scope.QUARTERBACK, 4);

            // Replacement quarterback should have moved to be the 2nd.
            expect(scope.replacements[scope.QUARTERBACK]).toEqual(scope.available_qbs[1]);
        });

        /**
         * Two startable RB per team, so #6 should be the replacement.
         * Someone will draft #7 so #5 should become the replacement.
         */
        it('should move replacement player (RB) up one index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 6th running back is the replacement
            expect(scope.replacements[scope.RUNNING_BACK]).toEqual(scope.available_rbs[5]);

            // Draft the 7th running back.
            scope.draftPlayer(scope.RUNNING_BACK, 17);

            // Replacement running back should have moved to be the 5th.
            expect(scope.replacements[scope.RUNNING_BACK]).toEqual(scope.available_rbs[4]);
        });

        /**
         * Three startable WR per team, so #9 should be the replacement.
         * Someone will draft #10 so #8 should become the replacement.
         */
        it('should move replacement player (WR) up one index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 9th wide receiver is the replacement
            expect(scope.replacements[scope.WIDE_RECEIVER]).toEqual(scope.available_wrs[8]);

            // Draft the 10th receiver.
            scope.draftPlayer(scope.WIDE_RECEIVER, 30);

            // Replacement receiver should have moved to be the 8th.
            expect(scope.replacements[scope.WIDE_RECEIVER]).toEqual(scope.available_wrs[7]);
        });

        /**
         * One startable TE per team, so #3 should be the replacement.
         * Someone will draft #4 so #2 should become the replacement.
         */
        it('should move replacement player (TE) up one index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd tight end is the replacement
            expect(scope.replacements[scope.TIGHT_END]).toEqual(scope.available_tes[2]);

            // Draft the 4th tight end.
            scope.draftPlayer(scope.TIGHT_END, 34);

            // Replacement tight end should have moved to be the 2nd.
            expect(scope.replacements[scope.TIGHT_END]).toEqual(scope.available_tes[1]);
        });

        /**
         * One startable DEF per team, so #3 should be the replacement.
         * Someone will draft #4 so #2 should become the replacement.
         */
        it('should move replacement player (DEF) up one index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd defense is the replacement
            expect(scope.replacements[scope.DEFENSE]).toEqual(scope.available_ds[2]);

            // Draft the 4th defense.
            scope.draftPlayer(scope.DEFENSE, 44);

            // Replacement defense should have moved to be the 2nd.
            expect(scope.replacements[scope.DEFENSE]).toEqual(scope.available_ds[1]);
        });

        /**
         * One startable K per team, so #3 should be the replacement.
         * Someone will draft #4 so #2 should become the replacement.
         */
        it('should move replacement player (K) up one index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd kicker is the replacement
            expect(scope.replacements[scope.KICKER]).toEqual(scope.available_ks[2]);

            // Draft the 4th kicker.
            scope.draftPlayer(scope.KICKER, 54);

            // Replacement kicker should have moved to be the 2nd.
            expect(scope.replacements[scope.KICKER]).toEqual(scope.available_ks[1]);
        });
    });

    describe('Replacement players - drafting before replacement player', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 3;
            scope.draftYear = 2001;

            $httpBackend = _$httpBackend_;

            // Use this to test with 10 players at each position.
            $httpBackend.whenGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'RB 1', position: 'RUNNING_BACK', points: 250.0, adp: 2.5, vorp: 30.0},
                    {id: 12, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 15, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'WR 1', position: 'WIDE_RECEIVER', points: 200.0, adp: 2.5, vorp: 20.0},
                    {id: 22, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 23, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 24, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 25, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 27, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 32, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 33, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 34, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 35, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 36, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 37, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 39, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 42, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 43, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 44, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 45, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 46, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 47, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 48, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 49, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 50, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 52, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 54, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 55, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 56, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 57, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 58, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 59, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 60, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0}]);
        }));

        /**
         * One startable QB per team, so #3 should be the replacement.
         * Someone will draft #1 so #3 should stay the replacement.
         */
        it('should keep replacement player (QB) at same index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd quarterback is the replacement
            expect(scope.replacements[scope.QUARTERBACK]).toEqual(scope.available_qbs[2]);

            // Draft the 1st quarterback.
            scope.draftPlayer(scope.QUARTERBACK, 1);

            // Replacement quarterback should be the same.
            expect(scope.replacements[scope.QUARTERBACK]).toEqual(scope.available_qbs[1]);
        });

        /**
         * Two startable RB per team, so #6 should be the replacement.
         * Someone will draft #1 so #6 should stay the replacement.
         */
        it('should keep replacement player (RB) at same index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 6th running back is the replacement
            expect(scope.replacements[scope.RUNNING_BACK]).toEqual(scope.available_rbs[5]);

            // Draft the 1st running back.
            scope.draftPlayer(scope.RUNNING_BACK, 11);

            // Replacement running back should stay put.
            expect(scope.replacements[scope.RUNNING_BACK]).toEqual(scope.available_rbs[4]);
        });

        /**
         * Three startable WR per team, so #9 should be the replacement.
         * Someone will draft #1 so #9 should stay the replacement.
         */
        it('should keep replacement player (WR) at same index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 9th wide receiver is the replacement
            expect(scope.replacements[scope.WIDE_RECEIVER]).toEqual(scope.available_wrs[8]);

            // Draft the 1st receiver.
            scope.draftPlayer(scope.WIDE_RECEIVER, 21);

            // Replacement receiver should stay put.
            expect(scope.replacements[scope.WIDE_RECEIVER]).toEqual(scope.available_wrs[7]);
        });

        /**
         * One startable TE per team, so #3 should be the replacement.
         * Someone will draft #1 so #3 should stay the replacement.
         */
        it('should keep replacement player (TE) up one index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd tight end is the replacement
            expect(scope.replacements[scope.TIGHT_END]).toEqual(scope.available_tes[2]);

            // Draft the 1st tight end.
            scope.draftPlayer(scope.TIGHT_END, 31);

            // Replacement tight end should stay put.
            expect(scope.replacements[scope.TIGHT_END]).toEqual(scope.available_tes[1]);
        });

        /**
         * One startable DEF per team, so #3 should be the replacement.
         * Someone will draft #1 so #3 should stay the replacement.
         */
        it('should keep replacement player (DEF) at same index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd defense is the replacement
            expect(scope.replacements[scope.DEFENSE]).toEqual(scope.available_ds[2]);

            // Draft the 1st defense.
            scope.draftPlayer(scope.DEFENSE, 41);

            // Replacement defense should stay put.
            expect(scope.replacements[scope.DEFENSE]).toEqual(scope.available_ds[1]);
        });

        /**
         * One startable K per team, so #3 should be the replacement.
         * Someone will draft #1 so #3 should stay the replacement.
         */
        it('should keep replacement player (K) at same index', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Sanity check - make sure the 3rd kicker is the replacement
            expect(scope.replacements[scope.KICKER]).toEqual(scope.available_ks[2]);

            // Draft the 1st kicker.
            scope.draftPlayer(scope.KICKER, 54);

            // Replacement kicker should stay put.
            expect(scope.replacements[scope.KICKER]).toEqual(scope.available_ks[1]);
        });
    });

    describe('isOwnersPick for 10 owners', function() {
        beforeEach(function() {
            scope.numOwners = 10;
        });

        it('should be true for first pick', function() {
            scope.myPick = 1;
            scope.currentPick = 1;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be false for 9th pick', function() {
            scope.myPick = 1;
            scope.currentPick = 9;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be false for 19th pick', function() {
            scope.myPick = 1;
            scope.currentPick = 19;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 20th pick', function() {
            scope.myPick = 1;
            scope.currentPick = 20;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be false for 1st pick', function() {
            scope.myPick = 2;
            scope.currentPick = 1;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 2nd pick', function() {
            scope.myPick = 2;
            scope.currentPick = 2;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be false for 11th pick', function() {
            scope.myPick = 2;
            scope.currentPick = 11;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 19th pick', function() {
            scope.myPick = 2;
            scope.currentPick = 19;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be false for 20th pick', function() {
            scope.myPick = 2;
            scope.currentPick = 20;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 22nd pick', function() {
            scope.myPick = 2;
            scope.currentPick = 22;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });




        it('should be false for 1st pick', function() {
            scope.myPick = 10;
            scope.currentPick = 1;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 1st pick', function() {
            scope.myPick = 10;
            scope.currentPick = 10;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be true for 11th pick', function() {
            scope.myPick = 10;
            scope.currentPick = 11;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be false for 20th pick', function() {
            scope.myPick = 10;
            scope.currentPick = 20;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 30th pick', function() {
            scope.myPick = 10;
            scope.currentPick = 30;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });
    });

    describe('isOwnersPick for 12 owners', function() {
        beforeEach(function() {
            scope.numOwners = 12;
        });

        it('should be true for 1st pick', function() {
            scope.myPick = 1;
            scope.currentPick = 1;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be false for 12th pick', function() {
            scope.myPick = 1;
            scope.currentPick = 12;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 24th pick', function() {
            scope.myPick = 1;
            scope.currentPick = 24;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be true for 25th pick', function() {
            scope.myPick = 1;
            scope.currentPick = 25;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });



        it('should be false for 1st pick', function() {
            scope.myPick = 2;
            scope.currentPick = 1;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 2nd pick', function() {
            scope.myPick = 2
            scope.currentPick = 2;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be false for 13th pick', function() {
            scope.myPick = 2
            scope.currentPick = 13;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be false for 14th pick', function() {
            scope.myPick = 2
            scope.currentPick = 14;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 23rd pick', function() {
            scope.myPick = 2
            scope.currentPick = 23;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be true for 26th pick', function() {
            scope.myPick = 2
            scope.currentPick = 26;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });



        it('should be false for 1st pick', function() {
            scope.myPick = 12;
            scope.currentPick = 1;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(false);
        });

        it('should be true for 12th pick', function() {
            scope.myPick = 12
            scope.currentPick = 12;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be true for 13th pick', function() {
            scope.myPick = 12
            scope.currentPick = 13;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });

        it('should be true for 36th pick', function() {
            scope.myPick = 12
            scope.currentPick = 36;
            expect(scope.isOwnersPick(scope.myPick)).toEqual(true);
        });
    });

    describe('getOwnersPick for 10 owners', function() {
        beforeEach(function() {
            scope.numOwners = 10;
        });

        it('should be 0 for first pick', function() {
            scope.currentPick = 1;
            expect(scope.getOwnerPick()).toEqual(0);
        });

        it('should be 9 for tenth pick', function() {
            scope.currentPick = 10;
            expect(scope.getOwnerPick()).toEqual(9);
        });

        it('should be 9 for eleventh pick', function() {
            scope.currentPick = 11;
            expect(scope.getOwnerPick()).toEqual(9);
        });

        it('should be 8 for twelfth pick', function() {
            scope.currentPick = 12;
            expect(scope.getOwnerPick()).toEqual(8);
        });
    });

    describe('Available players lists', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            $httpBackend = _$httpBackend_;

            $httpBackend.expectGET('draft/players?year=' + new Date().getFullYear()).
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
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
                    {id: 12, name: 'K 2', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}]);

            scope = $rootScope.$new();
            ctrl = $controller(DraftController, {$scope: scope});

            scope.numOwners = 2;
            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 1;
            scope.startablePositions[scope.WIDE_RECEIVER] = 1;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;
        }));

        it('available_qbs should have two players, QB 1 first', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_qbs[0].name).toEqual("QB 1");
            expect(scope.available_qbs[1].name).toEqual("QB 2");
        });

        it('available_rbs should have two players, RB 1 first', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_rbs[0].name).toEqual("RB 1");
            expect(scope.available_rbs[1].name).toEqual("RB 2");
        });

        it('available_wrs should have two players, WR 2 first', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_wrs[0].name).toEqual("WR 2");
            expect(scope.available_wrs[1].name).toEqual("WR 1");
        });

        it('available_tes should have two players, TE 2 first', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_tes[0].name).toEqual("TE 2");
            expect(scope.available_tes[1].name).toEqual("TE 1");
        });

        it('available_ds should have two players, DEF 1 first', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ds[0].name).toEqual("DEF 1");
            expect(scope.available_ds[1].name).toEqual("DEF 2");
        });

        it('available_ks should have two players, K 1 first', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.available_ks[0].name).toEqual("K 1");
            expect(scope.available_ks[1].name).toEqual("K 2");
        });
    });

    describe('Num Owners validation', function() {
        it('should be true when numOwners is a positive whole number', function() {
            expect(scope.isNumOwnersValid(1)).toEqual(true);
            expect(scope.isNumOwnersValid(10)).toEqual(true);
        });

        it('should be false when numOwners is a negative whole number', function() {
            expect(scope.isNumOwnersValid(-1)).toEqual(false);
            expect(scope.isNumOwnersValid(-10)).toEqual(false);
        });

        it('should be false when numOwners is a fractional number', function() {
            expect(scope.isNumOwnersValid(1.5)).toEqual(false);
        });

        it('should be false when numOwners is not a number', function() {
            expect(scope.isNumOwnersValid("-")).toEqual(false);
            expect(scope.isNumOwnersValid("abc")).toEqual(false);
        });
    });

    describe('Player value calculations', function(){
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            $httpBackend = _$httpBackend_;

            $httpBackend.expectGET('draft/players').
                respond([{name: 'Dan MacLean', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
                    {name: 'Bill Smith', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0}]);

            scope = $rootScope.$new();
            ctrl = $controller(DraftController, {$scope: scope});
        }));

        it('should be low when adp is less than current draft pick', function() {
            scope.currentPick = 1;
            var adp = 10;
            var vorp = 5;
            var need = 1;

            expect(scope.calculateValue(adp, vorp, need)).toBeGreaterThan(0.59);
            expect(scope.calculateValue(adp, vorp, need)).toBeLessThan(0.6);
//            expect(scope.calculateValue(adp, vorp, need, scope.QUARTERBACK)).toEqual(0.359);
//            expect(scope.calculateValue(adp, vorp, need, scope.RUNNING_BACK)).toEqual(0.287);
//            expect(scope.calculateValue(adp, vorp, need, scope.WIDE_RECEIVER)).toEqual(0.251);
//            expect(scope.calculateValue(adp, vorp, need, scope.TIGHT_END)).toEqual(0.371);
//            expect(scope.calculateValue(adp, vorp, need, scope.DEFENSE)).toEqual(0.059);
//            expect(scope.calculateValue(adp, vorp, need, scope.KICKER)).toEqual(0.059);
        });

        it('should be average when adp is equal to current draft pick', function() {
            scope.currentPick = 10;
            var adp = 10;
            var vorp = 5;
            var need = 1;

            expect(scope.calculateValue(adp, vorp, need)).toEqual(6);
//            expect(scope.calculateValue(adp, vorp, need, scope.QUARTERBACK)).toEqual(3.599);
//            expect(scope.calculateValue(adp, vorp, need, scope.RUNNING_BACK)).toEqual(2.88);
//            expect(scope.calculateValue(adp, vorp, need, scope.WIDE_RECEIVER)).toEqual(2.52);
//            expect(scope.calculateValue(adp, vorp, need, scope.TIGHT_END)).toEqual(3.719);
//            expect(scope.calculateValue(adp, vorp, need, scope.DEFENSE)).toEqual(0.6);
//            expect(scope.calculateValue(adp, vorp, need, scope.KICKER)).toEqual(0.6);
        });

        it('should be high when adp is greater than current draft pick', function() {
            scope.currentPick = 20;
            var adp = 10;
            var vorp = 5;
            var need = 1;

            expect(scope.calculateValue(adp, vorp, need)).toEqual(12);
//            expect(scope.calculateValue(adp, vorp, need, scope.QUARTERBACK)).toEqual(7.199);
//            expect(scope.calculateValue(adp, vorp, need, scope.RUNNING_BACK)).toEqual(5.76);
//            expect(scope.calculateValue(adp, vorp, need, scope.WIDE_RECEIVER)).toEqual(5.04);
//            expect(scope.calculateValue(adp, vorp, need, scope.TIGHT_END)).toEqual(7.439);
//            expect(scope.calculateValue(adp, vorp, need, scope.DEFENSE)).toEqual(1.2);
//            expect(scope.calculateValue(adp, vorp, need, scope.KICKER)).toEqual(1.2);
        });

        it('should be half of average when adp is equal to current draft pick and have 1 of 2 players at position', function() {
            scope.currentPick = 10;
            var adp = 10;
            var vorp = 5;
            var need = 0.5;

            expect(scope.calculateValue(adp, vorp, need)).toEqual(3);
//            expect(scope.calculateValue(adp, vorp, need, scope.QUARTERBACK)).toEqual(1.799);
//            expect(scope.calculateValue(adp, vorp, need, scope.RUNNING_BACK)).toEqual(1.44);
//            expect(scope.calculateValue(adp, vorp, need, scope.WIDE_RECEIVER)).toEqual(1.26);
//            expect(scope.calculateValue(adp, vorp, need, scope.TIGHT_END)).toEqual(1.859);
//            expect(scope.calculateValue(adp, vorp, need, scope.DEFENSE)).toEqual(0.3);
//            expect(scope.calculateValue(adp, vorp, need, scope.KICKER)).toEqual(0.3);
        });
    });

    describe('Player drafting', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('draft/players?year=' + new Date().getFullYear()).
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
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
                    {id: 12, name: 'K 2', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}]);

            scope = $rootScope.$new();
            ctrl = $controller(DraftController, {$scope: scope});

            scope.numOwners = 2;
            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 1;
            scope.startablePositions[scope.WIDE_RECEIVER] = 1;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;
        }));

        it('should decrement owner need when player drafted', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.ownerNeed[0][scope.QUARTERBACK]).toEqual(2);
            expect(scope.ownerNeed[0][scope.RUNNING_BACK]).toEqual(7);
            expect(scope.ownerNeed[0][scope.WIDE_RECEIVER]).toEqual(7);
            expect(scope.ownerNeed[0][scope.TIGHT_END]).toEqual(2);
            expect(scope.ownerNeed[0][scope.DEFENSE]).toEqual(1);
            expect(scope.ownerNeed[0][scope.KICKER]).toEqual(1);

            expect(scope.ownerNeed[1][scope.QUARTERBACK]).toEqual(2);
            expect(scope.ownerNeed[1][scope.RUNNING_BACK]).toEqual(7);
            expect(scope.ownerNeed[1][scope.WIDE_RECEIVER]).toEqual(7);
            expect(scope.ownerNeed[1][scope.TIGHT_END]).toEqual(2);
            expect(scope.ownerNeed[1][scope.DEFENSE]).toEqual(1);
            expect(scope.ownerNeed[1][scope.KICKER]).toEqual(1);

            scope.numOwners = 10;
            scope.myPick = 1;

            scope.currentPick = 1;
            scope.draftPlayer(scope.QUARTERBACK, scope.available_qbs[0].id);
            expect(scope.ownerNeed[0][scope.QUARTERBACK]).toEqual(1);

            scope.currentPick = 1;
            scope.draftPlayer(scope.RUNNING_BACK, scope.available_rbs[0].id);
            expect(scope.ownerNeed[0][scope.RUNNING_BACK]).toEqual(6);

            scope.currentPick = 1;
            scope.draftPlayer(scope.WIDE_RECEIVER, scope.available_wrs[0].id);
            expect(scope.ownerNeed[0][scope.WIDE_RECEIVER]).toEqual(6);

            scope.currentPick = 1;
            scope.draftPlayer(scope.TIGHT_END, scope.available_tes[0].id);
            expect(scope.ownerNeed[0][scope.TIGHT_END]).toEqual(1);

            scope.currentPick = 1;
            scope.draftPlayer(scope.DEFENSE, scope.available_ds[0].id);
            expect(scope.ownerNeed[0][scope.DEFENSE]).toEqual(0);

            scope.currentPick = 1;
            scope.draftPlayer(scope.KICKER, scope.available_ks[0].id);
            expect(scope.ownerNeed[0][scope.KICKER]).toEqual(0);
        });

        it('should not decrement owner need when player drafted', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.ownerNeed[0][scope.QUARTERBACK]).toEqual(2);
            expect(scope.ownerNeed[0][scope.RUNNING_BACK]).toEqual(7);
            expect(scope.ownerNeed[0][scope.WIDE_RECEIVER]).toEqual(7);
            expect(scope.ownerNeed[0][scope.TIGHT_END]).toEqual(2);
            expect(scope.ownerNeed[0][scope.DEFENSE]).toEqual(1);
            expect(scope.ownerNeed[0][scope.KICKER]).toEqual(1);

            expect(scope.ownerNeed[1][scope.QUARTERBACK]).toEqual(2);
            expect(scope.ownerNeed[1][scope.RUNNING_BACK]).toEqual(7);
            expect(scope.ownerNeed[1][scope.WIDE_RECEIVER]).toEqual(7);
            expect(scope.ownerNeed[1][scope.TIGHT_END]).toEqual(2);
            expect(scope.ownerNeed[1][scope.DEFENSE]).toEqual(1);
            expect(scope.ownerNeed[1][scope.KICKER]).toEqual(1);

            scope.numOwners = 10;
            scope.myPick = 1;

            /*
             * Draft two quarterbacks, need should only decrement once.
             */
            scope.currentPick = 1;
            scope.draftPlayer(scope.QUARTERBACK, scope.available_qbs[0].id);
            expect(scope.ownerNeed[0][scope.QUARTERBACK]).toEqual(1);

            scope.draftPlayer(scope.QUARTERBACK, scope.available_qbs[0].id);
            expect(scope.ownerNeed[0][scope.QUARTERBACK]).toEqual(1);

            /*
             * Draft two running backs, need should only decrement once.
             */
            scope.currentPick = 1;
            scope.draftPlayer(scope.RUNNING_BACK, scope.available_rbs[0].id);
            expect(scope.ownerNeed[0][scope.RUNNING_BACK]).toEqual(6);

            scope.draftPlayer(scope.RUNNING_BACK, scope.available_rbs[0].id);
            expect(scope.ownerNeed[0][scope.RUNNING_BACK]).toEqual(6);

            /*
             * Draft two wide receivers, need should only decrement once.
             */
            scope.currentPick = 1;
            scope.draftPlayer(scope.WIDE_RECEIVER, scope.available_wrs[0].id);
            expect(scope.ownerNeed[0][scope.WIDE_RECEIVER]).toEqual(6);

            scope.draftPlayer(scope.WIDE_RECEIVER, scope.available_wrs[0].id);
            expect(scope.ownerNeed[0][scope.WIDE_RECEIVER]).toEqual(6);

            /*
             * Draft two tight ends, need should only decrement once.
             */
            scope.currentPick = 1;
            scope.draftPlayer(scope.TIGHT_END, scope.available_tes[0].id);
            expect(scope.ownerNeed[0][scope.TIGHT_END]).toEqual(1);

            scope.draftPlayer(scope.TIGHT_END, scope.available_tes[0].id);
            expect(scope.ownerNeed[0][scope.TIGHT_END]).toEqual(1);

            /*
             * Draft two defenses, need should only decrement once.
             */
            scope.currentPick = 1;
            scope.draftPlayer(scope.DEFENSE, scope.available_ds[0].id);
            expect(scope.ownerNeed[0][scope.DEFENSE]).toEqual(0);

            scope.draftPlayer(scope.DEFENSE, scope.available_ds[0].id);
            expect(scope.ownerNeed[0][scope.DEFENSE]).toEqual(0);

            /*
             * Draft two kickers, need should only decrement once.
             */
            scope.currentPick = 1;
            scope.draftPlayer(scope.KICKER, scope.available_ks[0].id);
            expect(scope.ownerNeed[0][scope.KICKER]).toEqual(0);

            scope.draftPlayer(scope.KICKER, scope.available_ks[0].id);
            expect(scope.ownerNeed[0][scope.KICKER]).toEqual(0);
        });

        it('should draft all players for one owner', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            scope.numOwners = 1;

            expect(scope.currentPick).toEqual(1);

            // First player drafted - QB 1
            var selectedPlayer = scope.available_qbs[0];

            scope.draftPlayer(scope.QUARTERBACK, selectedPlayer.id);
            expect(scope.owners[0][0]).toEqual(selectedPlayer);
            expect(scope.available_qbs.length).toEqual(1);
            expect(scope.currentPick).toEqual(2);

            // Second player drafted - QB 2
            selectedPlayer = scope.available_qbs[0];

            scope.draftPlayer(scope.QUARTERBACK, selectedPlayer.id);
            expect(scope.owners[0][1]).toEqual(selectedPlayer);
            expect(scope.available_qbs.length).toEqual(0);
            expect(scope.currentPick).toEqual(3);

            // Third player drafted - RB 1
            selectedPlayer = scope.available_rbs[0];

            scope.draftPlayer(scope.RUNNING_BACK, selectedPlayer.id);
            expect(scope.owners[0][2]).toEqual(selectedPlayer);
            expect(scope.available_rbs.length).toEqual(1);
            expect(scope.currentPick).toEqual(4);

            // Fourth player drafted - RB 2
            selectedPlayer = scope.available_rbs[0];

            scope.draftPlayer(scope.RUNNING_BACK, selectedPlayer.id);
            expect(scope.owners[0][3]).toEqual(selectedPlayer);
            expect(scope.available_rbs.length).toEqual(0);
            expect(scope.currentPick).toEqual(5);

            // Fifth player drafted - WR 1
            selectedPlayer = scope.available_wrs[0];

            scope.draftPlayer(scope.WIDE_RECEIVER, selectedPlayer.id);
            expect(scope.owners[0][4]).toEqual(selectedPlayer);
            expect(scope.available_wrs.length).toEqual(1);
            expect(scope.currentPick).toEqual(6);

            // Sixth player drafted - WR 2
            selectedPlayer = scope.available_wrs[0];

            scope.draftPlayer(scope.WIDE_RECEIVER, selectedPlayer.id);
            expect(scope.owners[0][5]).toEqual(selectedPlayer);
            expect(scope.available_wrs.length).toEqual(0);
            expect(scope.currentPick).toEqual(7);

            // Seventh player drafted - TE 1
            selectedPlayer = scope.available_tes[0];

            scope.draftPlayer(scope.TIGHT_END, selectedPlayer.id);
            expect(scope.owners[0][6]).toEqual(selectedPlayer);
            expect(scope.available_tes.length).toEqual(1);
            expect(scope.currentPick).toEqual(8);

            // Eighth player drafted - TE 2
            selectedPlayer = scope.available_tes[0];

            scope.draftPlayer(scope.TIGHT_END, selectedPlayer.id);
            expect(scope.owners[0][7]).toEqual(selectedPlayer);
            expect(scope.available_tes.length).toEqual(0);
            expect(scope.currentPick).toEqual(9);

            // Ninth player drafted - DEF 1
            selectedPlayer = scope.available_ds[0];

            scope.draftPlayer(scope.DEFENSE, selectedPlayer.id);
            expect(scope.owners[0][8]).toEqual(selectedPlayer);
            expect(scope.available_ds.length).toEqual(1);
            expect(scope.currentPick).toEqual(10);

            // Tenth player drafted - DEF 2
            selectedPlayer = scope.available_ds[0];

            scope.draftPlayer(scope.DEFENSE, selectedPlayer.id);
            expect(scope.owners[0][9]).toEqual(selectedPlayer);
            expect(scope.available_ds.length).toEqual(0);
            expect(scope.currentPick).toEqual(11);

            // Eleventh player drafted - K 1
            selectedPlayer = scope.available_ks[0];

            scope.draftPlayer(scope.KICKER, selectedPlayer.id);
            expect(scope.owners[0][10]).toEqual(selectedPlayer);
            expect(scope.available_ks.length).toEqual(1);
            expect(scope.currentPick).toEqual(12);

            // Twelfth player drafted - K 2
            selectedPlayer = scope.available_ks[0];

            scope.draftPlayer(scope.KICKER, selectedPlayer.id);
            expect(scope.owners[0][11]).toEqual(selectedPlayer);
            expect(scope.available_ks.length).toEqual(0);
            expect(scope.currentPick).toEqual(13);
        });
    });

    describe('Player drafting - 12 owners', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 12;
            scope.draftYear = 2001;

            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 1;
            scope.startablePositions[scope.WIDE_RECEIVER] = 1;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;

            $httpBackend = _$httpBackend_;
//            $httpBackend.expectGET('login/whoami').respond("dmaclean");

            // Use this to test with 10 players at each position.
            $httpBackend.expectGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'QB 11', position: 'QUARTERBACK', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 12, name: 'QB 12', position: 'QUARTERBACK', points: 180.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'RB 1', position: 'RUNNING_BACK', points: 250.0, adp: 2.5, vorp: 30.0},
                    {id: 12, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 15, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 11', position: 'RUNNING_BACK', points: 130.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 12', position: 'RUNNING_BACK', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'WR 1', position: 'WIDE_RECEIVER', points: 200.0, adp: 2.5, vorp: 20.0},
                    {id: 22, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 23, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 24, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 25, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 27, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 11', position: 'WIDE_RECEIVER', points: 100.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 12', position: 'WIDE_RECEIVER', points: 90.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 32, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 33, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 34, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 35, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 36, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 37, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 39, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 11', position: 'TIGHT_END', points: 80.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 12', position: 'TIGHT_END', points: 70.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 42, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 43, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 44, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 45, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 46, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 47, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 48, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 49, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 50, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 50, name: 'DEF 11', position: 'DEFENSE', points: 10.0, adp: 5.0, vorp: 0},
                    {id: 50, name: 'DEF 12', position: 'DEFENSE', points: 5.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 52, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 54, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 55, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 56, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 57, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 58, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 59, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 60, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0},
                    {id: 60, name: 'K 11', position: 'KICKER', points: 20.0, adp: 2.5, vorp: 30.0},
                    {id: 60, name: 'K 12', position: 'KICKER', points: 10.0, adp: 2.5, vorp: 30.0}]);
        }));

        it('should draft one player for each of 12 owners', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            expect(scope.currentPick).toEqual(1);

            // First player drafted - QB 1
            var selectedPlayer = scope.available_qbs[0];

            scope.draftPlayer(scope.QUARTERBACK, selectedPlayer.id);
            expect(scope.owners[0][0]).toEqual(selectedPlayer);
            expect(scope.available_qbs.length).toEqual(11);
            expect(scope.currentPick).toEqual(2);

            // Second player drafted - QB 2
            selectedPlayer = scope.available_qbs[0];

            scope.draftPlayer(scope.QUARTERBACK, selectedPlayer.id);
            expect(scope.owners[1][0]).toEqual(selectedPlayer);
            expect(scope.available_qbs.length).toEqual(10);
            expect(scope.currentPick).toEqual(3);

            // Third player drafted - RB 1
            selectedPlayer = scope.available_rbs[0];

            scope.draftPlayer(scope.RUNNING_BACK, selectedPlayer.id);
            expect(scope.owners[2][0]).toEqual(selectedPlayer);
            expect(scope.available_rbs.length).toEqual(11);
            expect(scope.currentPick).toEqual(4);

            // Fourth player drafted - RB 2
            selectedPlayer = scope.available_rbs[0];

            scope.draftPlayer(scope.RUNNING_BACK, selectedPlayer.id);
            expect(scope.owners[3][0]).toEqual(selectedPlayer);
            expect(scope.available_rbs.length).toEqual(10);
            expect(scope.currentPick).toEqual(5);

            // Fifth player drafted - WR 1
            selectedPlayer = scope.available_wrs[0];

            scope.draftPlayer(scope.WIDE_RECEIVER, selectedPlayer.id);
            expect(scope.owners[4][0]).toEqual(selectedPlayer);
            expect(scope.available_wrs.length).toEqual(11);
            expect(scope.currentPick).toEqual(6);

            // Sixth player drafted - WR 2
            selectedPlayer = scope.available_wrs[0];

            scope.draftPlayer(scope.WIDE_RECEIVER, selectedPlayer.id);
            expect(scope.owners[5][0]).toEqual(selectedPlayer);
            expect(scope.available_wrs.length).toEqual(10);
            expect(scope.currentPick).toEqual(7);

            // Seventh player drafted - TE 1
            selectedPlayer = scope.available_tes[0];

            scope.draftPlayer(scope.TIGHT_END, selectedPlayer.id);
            expect(scope.owners[6][0]).toEqual(selectedPlayer);
            expect(scope.available_tes.length).toEqual(11);
            expect(scope.currentPick).toEqual(8);

            // Eighth player drafted - TE 2
            selectedPlayer = scope.available_tes[0];

            scope.draftPlayer(scope.TIGHT_END, selectedPlayer.id);
            expect(scope.owners[7][0]).toEqual(selectedPlayer);
            expect(scope.available_tes.length).toEqual(10);
            expect(scope.currentPick).toEqual(9);

            // Ninth player drafted - DEF 1
            selectedPlayer = scope.available_ds[0];

            scope.draftPlayer(scope.DEFENSE, selectedPlayer.id);
            expect(scope.owners[8][0]).toEqual(selectedPlayer);
            expect(scope.available_ds.length).toEqual(11);
            expect(scope.currentPick).toEqual(10);

            // Tenth player drafted - DEF 2
            selectedPlayer = scope.available_ds[0];

            scope.draftPlayer(scope.DEFENSE, selectedPlayer.id);
            expect(scope.owners[9][0]).toEqual(selectedPlayer);
            expect(scope.available_ds.length).toEqual(10);
            expect(scope.currentPick).toEqual(11);

            // Eleventh player drafted - K 1
            selectedPlayer = scope.available_ks[0];

            scope.draftPlayer(scope.KICKER, selectedPlayer.id);
            expect(scope.owners[10][0]).toEqual(selectedPlayer);
            expect(scope.available_ks.length).toEqual(11);
            expect(scope.currentPick).toEqual(12);

            // Twelfth player drafted - K 2
            selectedPlayer = scope.available_ks[0];

            scope.draftPlayer(scope.KICKER, selectedPlayer.id);
            expect(scope.owners[11][0]).toEqual(selectedPlayer);
            expect(scope.available_ks.length).toEqual(10);
            expect(scope.currentPick).toEqual(13);
        });
    });

    describe('ownersPerRow function', function() {
        it('should have an array of [0] for only one owner', function() {
            var numOwners = 1;
            var rowNum = 0;

            expect(scope.ownersPerRow(rowNum, numOwners)).toEqual([0]);
        });

        it('should have an array of [0,1,2,3,4,5] for 1st row and 12 owners', function() {
            var numOwners = 12;
            var rowNum = 0;

            expect(scope.ownersPerRow(rowNum, numOwners)).toEqual([0,1,2,3,4,5]);
        });

        it('should have an array of [6,7,8,9,10,11] for 2nd row and 12 owners', function() {
            var numOwners = 12;
            var rowNum = 1;

            expect(scope.ownersPerRow(rowNum, numOwners)).toEqual([6,7,8,9,10,11]);
        });

        it('should have an array of [6] for 2nd row and 7 owners', function() {
            var numOwners = 7;
            var rowNum = 1;

            expect(scope.ownersPerRow(rowNum, numOwners)).toEqual([6]);
        });
    });

    describe('getOwnerRows function', function() {
        it('should have 0 rows for 0 owners', function() {
            scope.numOwners = 0;
            expect(scope.getOwnerRows()).toEqual(0);
        });

        it('should have 1 rows for 1 owners', function() {
            scope.numOwners = 1;
            expect(scope.getOwnerRows()).toEqual(1);
        });

        it('should have 1 rows for 6 owners', function() {
            scope.numOwners = 6;
            expect(scope.getOwnerRows()).toEqual(1);
        });

        it('should have 2 rows for 7 owners', function() {
            scope.numOwners = 7;
            expect(scope.getOwnerRows()).toEqual(2);
        });

        it('should have 2 rows for 12 owners', function() {
            scope.numOwners = 12;
            expect(scope.getOwnerRows()).toEqual(2);
        });

        it('should have 3 rows for 13 owners', function() {
            scope.numOwners = 13;
            expect(scope.getOwnerRows()).toEqual(3);
        });
    });

    describe('Calculate total points', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('draft/players?year=' + new Date().getFullYear()).
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 20.0},
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
                    {id: 12, name: 'K 2', position: 'KICKER', points: 90.0, adp: 5.0, vorp: 0}]);

            scope = $rootScope.$new();
            ctrl = $controller(DraftController, {$scope: scope});

            scope.numOwners = 2;
            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 1;
            scope.startablePositions[scope.WIDE_RECEIVER] = 1;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;
            scope.initialized = true;
        }));

        it('should have 520 points for owner 1 (QB1, RB2) and 530 for owner 2 (QB2, RB1)', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            scope.draftPlayer(scope.QUARTERBACK, 1);
            scope.draftPlayer(scope.QUARTERBACK, 2);
            scope.draftPlayer(scope.RUNNING_BACK, 3);
            scope.draftPlayer(scope.RUNNING_BACK, 4);

            expect(scope.calculateTotalPoints(0)).toEqual(520);
            expect(scope.calculateTotalPoints(1)).toEqual(530);
        });
    });

    describe('Simulate draft', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 2;
            scope.draftYear = 2001;
            scope.initialized = true;

            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 2;
            scope.startablePositions[scope.WIDE_RECEIVER] = 3;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;

            $httpBackend = _$httpBackend_;

            // Use this to test with 10 players at each position.
            $httpBackend.expectGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'QB 11', position: 'QUARTERBACK', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 12, name: 'QB 12', position: 'QUARTERBACK', points: 180.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 1', position: 'RUNNING_BACK', points: 299.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 15, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 22, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 23, name: 'RB 11', position: 'RUNNING_BACK', points: 130.0, adp: 2.5, vorp: 30.0},
                    {id: 24, name: 'RB 12', position: 'RUNNING_BACK', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 25, name: 'WR 1', position: 'WIDE_RECEIVER', points: 275.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 27, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 32, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 33, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 34, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 35, name: 'WR 11', position: 'WIDE_RECEIVER', points: 100.0, adp: 2.5, vorp: 20.0},
                    {id: 36, name: 'WR 12', position: 'WIDE_RECEIVER', points: 90.0, adp: 2.5, vorp: 20.0},
                    {id: 37, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 39, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 42, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 43, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 44, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 45, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 46, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 47, name: 'TE 11', position: 'TIGHT_END', points: 80.0, adp: 2.5, vorp: 5.0},
                    {id: 48, name: 'TE 12', position: 'TIGHT_END', points: 70.0, adp: 2.5, vorp: 5.0},
                    {id: 49, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 50, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 52, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 54, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 55, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 56, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 57, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 58, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 59, name: 'DEF 11', position: 'DEFENSE', points: 10.0, adp: 5.0, vorp: 0},
                    {id: 60, name: 'DEF 12', position: 'DEFENSE', points: 5.0, adp: 5.0, vorp: 0},
                    {id: 61, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 62, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 63, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 64, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 65, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 66, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 67, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 68, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 69, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 70, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0},
                    {id: 71, name: 'K 11', position: 'KICKER', points: 20.0, adp: 2.5, vorp: 30.0},
                    {id: 72, name: 'K 12', position: 'KICKER', points: 10.0, adp: 2.5, vorp: 30.0}]);
        }));

        it('should take QB1, RB1, QB2, WR1 for two owners and two rounds', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Get QB1
            var player = scope.getBPA();
            expect(player.id).toEqual(1);
            scope.draftPlayer(scope.QUARTERBACK, player.id);

            // Get RB1
            player = scope.getBPA();
            expect(player.id).toEqual(13);
            scope.draftPlayer(scope.RUNNING_BACK, player.id);

            // Get QB2
            player = scope.getBPA();
            expect(player.id).toEqual(2);
            scope.draftPlayer(scope.QUARTERBACK, player.id);

            // Get WR1
            player = scope.getBPA();
            expect(player.id).toEqual(25);
            scope.draftPlayer(scope.WIDE_RECEIVER, player.id);

            // Check owner 1 sum
            var sum = 0;
            for(var i=0; i<scope.owners[0].length; i++) {
                sum += scope.owners[0][i].points;
            }
            expect(sum).toEqual(575);

            // Check owner 2 sum
            sum = 0;
            for(var i=0; i<scope.owners[1].length; i++) {
                sum += scope.owners[1][i].points;
            }
            expect(sum).toEqual(579);
        });
    });

    describe('VORP Drafting Strategy', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 2;
            scope.draftYear = 2001;
            scope.initialized = true;

            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 2;
            scope.startablePositions[scope.WIDE_RECEIVER] = 3;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;

            $httpBackend = _$httpBackend_;

            // Use this to test with 10 players at each position.
            $httpBackend.expectGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'QB 11', position: 'QUARTERBACK', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 12, name: 'QB 12', position: 'QUARTERBACK', points: 180.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 1', position: 'RUNNING_BACK', points: 299.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 15, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 22, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 23, name: 'RB 11', position: 'RUNNING_BACK', points: 130.0, adp: 2.5, vorp: 30.0},
                    {id: 24, name: 'RB 12', position: 'RUNNING_BACK', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 25, name: 'WR 1', position: 'WIDE_RECEIVER', points: 275.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 27, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 32, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 33, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 34, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 35, name: 'WR 11', position: 'WIDE_RECEIVER', points: 100.0, adp: 2.5, vorp: 20.0},
                    {id: 36, name: 'WR 12', position: 'WIDE_RECEIVER', points: 90.0, adp: 2.5, vorp: 20.0},
                    {id: 37, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 39, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 42, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 43, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 44, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 45, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 46, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 47, name: 'TE 11', position: 'TIGHT_END', points: 80.0, adp: 2.5, vorp: 5.0},
                    {id: 48, name: 'TE 12', position: 'TIGHT_END', points: 70.0, adp: 2.5, vorp: 5.0},
                    {id: 49, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 50, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 52, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 54, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 55, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 56, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 57, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 58, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 59, name: 'DEF 11', position: 'DEFENSE', points: 10.0, adp: 5.0, vorp: 0},
                    {id: 60, name: 'DEF 12', position: 'DEFENSE', points: 5.0, adp: 5.0, vorp: 0},
                    {id: 61, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 62, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 63, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 64, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 65, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 66, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 67, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 68, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 69, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 70, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0},
                    {id: 71, name: 'K 11', position: 'KICKER', points: 20.0, adp: 2.5, vorp: 30.0},
                    {id: 72, name: 'K 12', position: 'KICKER', points: 10.0, adp: 2.5, vorp: 30.0}]);
        }));

        it('should get WR1, RB1, QB1, WR2 for first four drafted players', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Should get WR
            expect(scope.getVORP().id).toEqual(25);
            scope.draftPlayer(scope.WIDE_RECEIVER, 25);

            // Should get RB
            expect(scope.getVORP().id).toEqual(13);
            scope.draftPlayer(scope.RUNNING_BACK, 13);

            // Should get QB
            expect(scope.getVORP().id).toEqual(1);
            scope.draftPlayer(scope.QUARTERBACK, 1);

            // Should get WR
            expect(scope.getVORP().id).toEqual(26);
            scope.draftPlayer(scope.QUARTERBACK, 26);
        });

        it('should fill roster in first n picks (where n = sum of starting roster spots)', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            var num_avail_qbs = scope.available_qbs.length;
            var num_avail_rbs = scope.available_rbs.length;
            var num_avail_wrs = scope.available_wrs.length;
            var num_avail_tes = scope.available_tes.length;
            var num_avail_ds = scope.available_ds.length;
            var num_avail_ks = scope.available_ks.length;

            // Make first 7 picks for each owner.  This should cover starting spots for QBs, RBs, WRs, and TEs
            // Remember, we're saving DEFs and Ks for last two rounds.
            for(var i=0; i<14; i++) {
                var player = undefined;

                if(scope.getOwnerPick() == 0) {
                    player = scope.getVORP();
                }
                else {
                    player = scope.getBPAWithStartersFirst();
                }

                scope.draftPlayer(player.position, player.id);
            }

            expect(num_avail_qbs - scope.available_qbs.length).toEqual(2);
            expect(num_avail_rbs - scope.available_rbs.length).toEqual(4);
            expect(num_avail_wrs - scope.available_wrs.length).toEqual(6);
            expect(num_avail_tes - scope.available_tes.length).toEqual(2);
            expect(num_avail_ds - scope.available_ds.length).toEqual(0);
            expect(num_avail_ks - scope.available_ks.length).toEqual(0);

            // Fill out the rest of the rosters
            while(scope.owners[0].length < scope.totalRosterSpots || scope.owners[1].length < scope.totalRosterSpots) {
                if(scope.getOwnerPick() == 0) {
                    player = scope.getVORP();
                }
                else {
                    player = scope.getBPAWithStartersFirst();
                }

                scope.draftPlayer(player.position, player.id);
            }

            expect(num_avail_qbs - scope.available_qbs.length).toEqual(4);
//            expect(num_avail_rbs - scope.available_rbs.length).toEqual(8);
//            expect(num_avail_wrs - scope.available_wrs.length).toEqual(12);
            expect(num_avail_tes - scope.available_tes.length).toEqual(4);
            expect(num_avail_ds - scope.available_ds.length).toEqual(2);
            expect(num_avail_ks - scope.available_ks.length).toEqual(2);
        });
    });

    describe('BPA Drafting Strategy', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 2;
            scope.draftYear = 2001;
            scope.initialized = true;

            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 2;
            scope.startablePositions[scope.WIDE_RECEIVER] = 3;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;

            $httpBackend = _$httpBackend_;

            // Use this to test with 10 players at each position.
            $httpBackend.expectGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'QB 11', position: 'QUARTERBACK', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 12, name: 'QB 12', position: 'QUARTERBACK', points: 180.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 1', position: 'RUNNING_BACK', points: 299.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 15, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 22, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 23, name: 'RB 11', position: 'RUNNING_BACK', points: 130.0, adp: 2.5, vorp: 30.0},
                    {id: 24, name: 'RB 12', position: 'RUNNING_BACK', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 25, name: 'WR 1', position: 'WIDE_RECEIVER', points: 275.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 27, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 32, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 33, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 34, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 35, name: 'WR 11', position: 'WIDE_RECEIVER', points: 100.0, adp: 2.5, vorp: 20.0},
                    {id: 36, name: 'WR 12', position: 'WIDE_RECEIVER', points: 90.0, adp: 2.5, vorp: 20.0},
                    {id: 37, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 39, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 42, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 43, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 44, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 45, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 46, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 47, name: 'TE 11', position: 'TIGHT_END', points: 80.0, adp: 2.5, vorp: 5.0},
                    {id: 48, name: 'TE 12', position: 'TIGHT_END', points: 70.0, adp: 2.5, vorp: 5.0},
                    {id: 49, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 50, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 52, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 54, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 55, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 56, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 57, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 58, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 59, name: 'DEF 11', position: 'DEFENSE', points: 10.0, adp: 5.0, vorp: 0},
                    {id: 60, name: 'DEF 12', position: 'DEFENSE', points: 5.0, adp: 5.0, vorp: 0},
                    {id: 61, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 62, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 63, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 64, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 65, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 66, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 67, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 68, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 69, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 70, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0},
                    {id: 71, name: 'K 11', position: 'KICKER', points: 20.0, adp: 2.5, vorp: 30.0},
                    {id: 72, name: 'K 12', position: 'KICKER', points: 10.0, adp: 2.5, vorp: 30.0}]);
        }));

        it('should return with undefined if not initialized', function() {
            scope.initialized = false;

            expect(scope.getBPA()).toEqual(undefined);
        });

        it('should take QB1, RB1, QB2, WR1 for two owners and two rounds', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            // Get QB1
            var player = scope.getBPA();
            expect(player.id).toEqual(1);
            scope.draftPlayer(scope.QUARTERBACK, player.id);

            // Get RB1
            player = scope.getBPA();
            expect(player.id).toEqual(13);
            scope.draftPlayer(scope.RUNNING_BACK, player.id);

            // Get QB2
            player = scope.getBPA();
            expect(player.id).toEqual(2);
            scope.draftPlayer(scope.QUARTERBACK, player.id);

            // Get WR1
            player = scope.getBPA();
            expect(player.id).toEqual(25);
            scope.draftPlayer(scope.WIDE_RECEIVER, player.id);

            // Check owner 1 sum
            var sum = 0;
            for(var i=0; i<scope.owners[0].length; i++) {
                sum += scope.owners[0][i].points;
            }
            expect(sum).toEqual(575);

            // Check owner 2 sum
            sum = 0;
            for(var i=0; i<scope.owners[1].length; i++) {
                sum += scope.owners[1][i].points;
            }
            expect(sum).toEqual(579);
        });
    });

    describe('calculateStarterPoints', function() {
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            scope.numOwners = 2;
            scope.draftYear = 2001;
            scope.initialized = true;

            scope.startablePositions[scope.QUARTERBACK] = 1;
            scope.startablePositions[scope.RUNNING_BACK] = 2;
            scope.startablePositions[scope.WIDE_RECEIVER] = 3;
            scope.startablePositions[scope.TIGHT_END] = 1;
            scope.startablePositions[scope.DEFENSE] = 1;
            scope.startablePositions[scope.KICKER] = 1;

            $httpBackend = _$httpBackend_;

            // Use this to test with 10 players at each position.
            $httpBackend.expectGET('draft/players?year=2001').
                respond([{id: 1, name: 'QB 1', position: 'QUARTERBACK', points: 300.0, adp: 2.5, vorp: 0},
                    {id: 2, name: 'QB 2', position: 'QUARTERBACK', points: 280.0, adp: 5.0, vorp: 0},
                    {id: 3, name: 'QB 3', position: 'QUARTERBACK', points: 270.0, adp: 5.0, vorp: 0},
                    {id: 4, name: 'QB 4', position: 'QUARTERBACK', points: 260.0, adp: 5.0, vorp: 0},
                    {id: 5, name: 'QB 5', position: 'QUARTERBACK', points: 250.0, adp: 5.0, vorp: 0},
                    {id: 6, name: 'QB 6', position: 'QUARTERBACK', points: 240.0, adp: 5.0, vorp: 0},
                    {id: 7, name: 'QB 7', position: 'QUARTERBACK', points: 230.0, adp: 5.0, vorp: 0},
                    {id: 8, name: 'QB 8', position: 'QUARTERBACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 9, name: 'QB 9', position: 'QUARTERBACK', points: 210.0, adp: 5.0, vorp: 0},
                    {id: 10, name: 'QB 10', position: 'QUARTERBACK', points: 200.0, adp: 5.0, vorp: 0},
                    {id: 11, name: 'QB 11', position: 'QUARTERBACK', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 12, name: 'QB 12', position: 'QUARTERBACK', points: 180.0, adp: 5.0, vorp: 0},
                    {id: 13, name: 'RB 1', position: 'RUNNING_BACK', points: 299.0, adp: 2.5, vorp: 30.0},
                    {id: 14, name: 'RB 2', position: 'RUNNING_BACK', points: 220.0, adp: 5.0, vorp: 0},
                    {id: 15, name: 'RB 3', position: 'RUNNING_BACK', points: 210.0, adp: 2.5, vorp: 30.0},
                    {id: 16, name: 'RB 4', position: 'RUNNING_BACK', points: 200.0, adp: 2.5, vorp: 30.0},
                    {id: 17, name: 'RB 5', position: 'RUNNING_BACK', points: 190.0, adp: 2.5, vorp: 30.0},
                    {id: 18, name: 'RB 6', position: 'RUNNING_BACK', points: 180.0, adp: 2.5, vorp: 30.0},
                    {id: 19, name: 'RB 7', position: 'RUNNING_BACK', points: 170.0, adp: 2.5, vorp: 30.0},
                    {id: 20, name: 'RB 8', position: 'RUNNING_BACK', points: 160.0, adp: 2.5, vorp: 30.0},
                    {id: 21, name: 'RB 9', position: 'RUNNING_BACK', points: 150.0, adp: 2.5, vorp: 30.0},
                    {id: 22, name: 'RB 10', position: 'RUNNING_BACK', points: 140.0, adp: 2.5, vorp: 30.0},
                    {id: 23, name: 'RB 11', position: 'RUNNING_BACK', points: 130.0, adp: 2.5, vorp: 30.0},
                    {id: 24, name: 'RB 12', position: 'RUNNING_BACK', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 25, name: 'WR 1', position: 'WIDE_RECEIVER', points: 275.0, adp: 2.5, vorp: 20.0},
                    {id: 26, name: 'WR 2', position: 'WIDE_RECEIVER', points: 190.0, adp: 5.0, vorp: 0},
                    {id: 27, name: 'WR 3', position: 'WIDE_RECEIVER', points: 180.0, adp: 2.5, vorp: 20.0},
                    {id: 28, name: 'WR 4', position: 'WIDE_RECEIVER', points: 170.0, adp: 2.5, vorp: 20.0},
                    {id: 29, name: 'WR 5', position: 'WIDE_RECEIVER', points: 160.0, adp: 2.5, vorp: 20.0},
                    {id: 30, name: 'WR 6', position: 'WIDE_RECEIVER', points: 150.0, adp: 2.5, vorp: 20.0},
                    {id: 31, name: 'WR 7', position: 'WIDE_RECEIVER', points: 140.0, adp: 2.5, vorp: 20.0},
                    {id: 32, name: 'WR 8', position: 'WIDE_RECEIVER', points: 130.0, adp: 2.5, vorp: 20.0},
                    {id: 33, name: 'WR 9', position: 'WIDE_RECEIVER', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 34, name: 'WR 10', position: 'WIDE_RECEIVER', points: 110.0, adp: 2.5, vorp: 20.0},
                    {id: 35, name: 'WR 11', position: 'WIDE_RECEIVER', points: 100.0, adp: 2.5, vorp: 20.0},
                    {id: 36, name: 'WR 12', position: 'WIDE_RECEIVER', points: 90.0, adp: 2.5, vorp: 20.0},
                    {id: 37, name: 'TE 1', position: 'TIGHT_END', points: 180.0, adp: 2.5, vorp: 5.0},
                    {id: 38, name: 'TE 2', position: 'TIGHT_END', points: 170.0, adp: 5.0, vorp: 0},
                    {id: 39, name: 'TE 3', position: 'TIGHT_END', points: 160.0, adp: 2.5, vorp: 5.0},
                    {id: 40, name: 'TE 4', position: 'TIGHT_END', points: 150.0, adp: 2.5, vorp: 5.0},
                    {id: 41, name: 'TE 5', position: 'TIGHT_END', points: 140.0, adp: 2.5, vorp: 5.0},
                    {id: 42, name: 'TE 6', position: 'TIGHT_END', points: 130.0, adp: 2.5, vorp: 5.0},
                    {id: 43, name: 'TE 7', position: 'TIGHT_END', points: 120.0, adp: 2.5, vorp: 5.0},
                    {id: 44, name: 'TE 8', position: 'TIGHT_END', points: 110.0, adp: 2.5, vorp: 5.0},
                    {id: 45, name: 'TE 9', position: 'TIGHT_END', points: 100.0, adp: 2.5, vorp: 5.0},
                    {id: 46, name: 'TE 10', position: 'TIGHT_END', points: 90.0, adp: 2.5, vorp: 5.0},
                    {id: 47, name: 'TE 11', position: 'TIGHT_END', points: 80.0, adp: 2.5, vorp: 5.0},
                    {id: 48, name: 'TE 12', position: 'TIGHT_END', points: 70.0, adp: 2.5, vorp: 5.0},
                    {id: 49, name: 'DEF 1', position: 'DEFENSE', points: 120.0, adp: 2.5, vorp: 20.0},
                    {id: 50, name: 'DEF 2', position: 'DEFENSE', points: 100.0, adp: 5.0, vorp: 0},
                    {id: 51, name: 'DEF 3', position: 'DEFENSE', points: 90.0, adp: 5.0, vorp: 0},
                    {id: 52, name: 'DEF 4', position: 'DEFENSE', points: 80.0, adp: 5.0, vorp: 0},
                    {id: 53, name: 'DEF 5', position: 'DEFENSE', points: 70.0, adp: 5.0, vorp: 0},
                    {id: 54, name: 'DEF 6', position: 'DEFENSE', points: 60.0, adp: 5.0, vorp: 0},
                    {id: 55, name: 'DEF 7', position: 'DEFENSE', points: 50.0, adp: 5.0, vorp: 0},
                    {id: 56, name: 'DEF 8', position: 'DEFENSE', points: 40.0, adp: 5.0, vorp: 0},
                    {id: 57, name: 'DEF 9', position: 'DEFENSE', points: 30.0, adp: 5.0, vorp: 0},
                    {id: 58, name: 'DEF 10', position: 'DEFENSE', points: 20.0, adp: 5.0, vorp: 0},
                    {id: 59, name: 'DEF 11', position: 'DEFENSE', points: 10.0, adp: 5.0, vorp: 0},
                    {id: 60, name: 'DEF 12', position: 'DEFENSE', points: 5.0, adp: 5.0, vorp: 0},
                    {id: 61, name: 'K 1', position: 'KICKER', points: 120.0, adp: 2.5, vorp: 30.0},
                    {id: 62, name: 'K 2', position: 'KICKER', points: 110.0, adp: 5.0, vorp: 0},
                    {id: 63, name: 'K 3', position: 'KICKER', points: 100.0, adp: 2.5, vorp: 30.0},
                    {id: 64, name: 'K 4', position: 'KICKER', points: 90.0, adp: 2.5, vorp: 30.0},
                    {id: 65, name: 'K 5', position: 'KICKER', points: 80.0, adp: 2.5, vorp: 30.0},
                    {id: 66, name: 'K 6', position: 'KICKER', points: 70.0, adp: 2.5, vorp: 30.0},
                    {id: 67, name: 'K 7', position: 'KICKER', points: 60.0, adp: 2.5, vorp: 30.0},
                    {id: 68, name: 'K 8', position: 'KICKER', points: 50.0, adp: 2.5, vorp: 30.0},
                    {id: 69, name: 'K 9', position: 'KICKER', points: 40.0, adp: 2.5, vorp: 30.0},
                    {id: 70, name: 'K 10', position: 'KICKER', points: 30.0, adp: 2.5, vorp: 30.0},
                    {id: 71, name: 'K 11', position: 'KICKER', points: 20.0, adp: 2.5, vorp: 30.0},
                    {id: 72, name: 'K 12', position: 'KICKER', points: 10.0, adp: 2.5, vorp: 30.0}]);
        }));

        it('should get 1884 points', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            scope.myPick = 1;

            scope.currentPick = 1;
            scope.draftPlayer(scope.QUARTERBACK, 1);
            scope.currentPick = 1;
            scope.draftPlayer(scope.QUARTERBACK, 2);
            scope.currentPick = 1;
            scope.draftPlayer(scope.RUNNING_BACK, 13);
            scope.currentPick = 1;
            scope.draftPlayer(scope.RUNNING_BACK, 14);
            scope.currentPick = 1;
            scope.draftPlayer(scope.RUNNING_BACK, 15);
            scope.currentPick = 1;
            scope.draftPlayer(scope.RUNNING_BACK, 16);
            scope.currentPick = 1;
            scope.draftPlayer(scope.WIDE_RECEIVER, 25);
            scope.currentPick = 1;
            scope.draftPlayer(scope.WIDE_RECEIVER, 26);
            scope.currentPick = 1;
            scope.draftPlayer(scope.WIDE_RECEIVER, 27);
            scope.currentPick = 1;
            scope.draftPlayer(scope.WIDE_RECEIVER, 28);
            scope.currentPick = 1;
            scope.draftPlayer(scope.TIGHT_END, 37);
            scope.currentPick = 1;
            scope.draftPlayer(scope.TIGHT_END, 38);
            scope.currentPick = 1;
            scope.draftPlayer(scope.DEFENSE, 49);
            scope.currentPick = 1;
            scope.draftPlayer(scope.DEFENSE, 50);
            scope.currentPick = 1;
            scope.draftPlayer(scope.KICKER, 61);
            scope.currentPick = 1;
            scope.draftPlayer(scope.KICKER, 62);

            expect(scope.calculateStarterPoints(0)).toEqual(1884);

        });
    });
});