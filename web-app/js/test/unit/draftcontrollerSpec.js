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

    describe('Available players lists', function() {
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
            expect(scope.calculateValue(adp, vorp, need)).toBeLessThan(0.51);
            expect(scope.calculateValue(adp, vorp, need)).toBeGreaterThan(0.49);
        });

        it('should be average when adp is equal to current draft pick', function() {
            scope.currentPick = 10;
            var adp = 10;
            var vorp = 5;
            var need = 1;
            expect(scope.calculateValue(adp, vorp, need)).toEqual(5.0);
        });

        it('should be high when adp is greater than current draft pick', function() {
            scope.currentPick = 20;
            var adp = 10;
            var vorp = 5;
            var need = 1;
            expect(scope.calculateValue(adp, vorp, need)).toEqual(10.0);
        });
    });

    describe('Player drafting', function() {
        it('should draft one player for each of 12 owners', function() {
            expect(scope.players).toBeUndefined();
            scope.fetchPlayers();
            $httpBackend.flush();

            scope.numOwners = 12;

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
            expect(scope.owners[1][0]).toEqual(selectedPlayer);
            expect(scope.available_qbs.length).toEqual(0);
            expect(scope.currentPick).toEqual(3);

            // Third player drafted - RB 1
            selectedPlayer = scope.available_rbs[0];

            scope.draftPlayer(scope.RUNNING_BACK, selectedPlayer.id);
            expect(scope.owners[2][0]).toEqual(selectedPlayer);
            expect(scope.available_rbs.length).toEqual(1);
            expect(scope.currentPick).toEqual(4);

            // Fourth player drafted - RB 2
            selectedPlayer = scope.available_rbs[0];

            scope.draftPlayer(scope.RUNNING_BACK, selectedPlayer.id);
            expect(scope.owners[3][0]).toEqual(selectedPlayer);
            expect(scope.available_rbs.length).toEqual(0);
            expect(scope.currentPick).toEqual(5);

            // Fifth player drafted - WR 1
            selectedPlayer = scope.available_wrs[0];

            scope.draftPlayer(scope.WIDE_RECEIVER, selectedPlayer.id);
            expect(scope.owners[4][0]).toEqual(selectedPlayer);
            expect(scope.available_wrs.length).toEqual(1);
            expect(scope.currentPick).toEqual(6);

            // Sixth player drafted - WR 2
            selectedPlayer = scope.available_wrs[0];

            scope.draftPlayer(scope.WIDE_RECEIVER, selectedPlayer.id);
            expect(scope.owners[5][0]).toEqual(selectedPlayer);
            expect(scope.available_wrs.length).toEqual(0);
            expect(scope.currentPick).toEqual(7);

            // Seventh player drafted - TE 1
            selectedPlayer = scope.available_tes[0];

            scope.draftPlayer(scope.TIGHT_END, selectedPlayer.id);
            expect(scope.owners[6][0]).toEqual(selectedPlayer);
            expect(scope.available_tes.length).toEqual(1);
            expect(scope.currentPick).toEqual(8);

            // Eighth player drafted - TE 2
            selectedPlayer = scope.available_tes[0];

            scope.draftPlayer(scope.TIGHT_END, selectedPlayer.id);
            expect(scope.owners[7][0]).toEqual(selectedPlayer);
            expect(scope.available_tes.length).toEqual(0);
            expect(scope.currentPick).toEqual(9);

            // Ninth player drafted - DEF 1
            selectedPlayer = scope.available_ds[0];

            scope.draftPlayer(scope.DEFENSE, selectedPlayer.id);
            expect(scope.owners[8][0]).toEqual(selectedPlayer);
            expect(scope.available_ds.length).toEqual(1);
            expect(scope.currentPick).toEqual(10);

            // Tenth player drafted - DEF 2
            selectedPlayer = scope.available_ds[0];

            scope.draftPlayer(scope.DEFENSE, selectedPlayer.id);
            expect(scope.owners[9][0]).toEqual(selectedPlayer);
            expect(scope.available_ds.length).toEqual(0);
            expect(scope.currentPick).toEqual(11);

            // Eleventh player drafted - K 1
            selectedPlayer = scope.available_ks[0];

            scope.draftPlayer(scope.KICKER, selectedPlayer.id);
            expect(scope.owners[10][0]).toEqual(selectedPlayer);
            expect(scope.available_ks.length).toEqual(1);
            expect(scope.currentPick).toEqual(12);

            // Twelfth player drafted - K 2
            selectedPlayer = scope.available_ks[0];

            scope.draftPlayer(scope.KICKER, selectedPlayer.id);
            expect(scope.owners[11][0]).toEqual(selectedPlayer);
            expect(scope.available_ks.length).toEqual(0);
            expect(scope.currentPick).toEqual(13);
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
});