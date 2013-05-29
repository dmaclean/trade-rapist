/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 3/31/13
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
 */
angular.module('TradeRapist', []).
    config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/draft', { templateUrl: 'partials/draft.html', controller: DraftController }).
            otherwise({redirectTo:'/draft'})
}])
    .filter('range', function() {
        return function(input, total) {
            total = parseInt(total);
            for (var i=0; i<total; i++)
                input.push(i);
            return input;
        };
    })
    .directive("owners", function() {
        function link($scope, element, attributes) {
            console.log("Linked", attributes.id);
        }

        return({
            link: link,
            restrict: "A",
            template:   '<div id="owners_row_{{ n }}" ng-repeat="n in [] | range:getOwnerRows()" class="row-fluid">' +
                            '<div id="owner{{ o }}" ng-repeat="o in ownersPerRow(n, numOwners)" class="span2">' +
                                '<h3>Owner {{ o+1 }}</h3>' +
                                '<span id="owner{{ o }}_points">Total Projected Points: {{ calculateTotalPoints(o) }}</span><br/>' +
                                '<span id="owner{{ o }}_starter_points">Starter Projected Points: {{ calculateStarterPoints(o) }}</span>' +
                                '<span class="label label-success" ng-hide="o != myPick-1">My pick</span>' +
                                '<span class="label label-important" ng-hide="!isOwnersPick(o+1)">Current pick</span>' +
                                '<ol><li ng-repeat="player in owners[o]">{{ player.name }} ({{ getPositionDisplayText(player.position) }})</li></ol>' +
                            '</div>' +
                        '</div>'
        });
    });

function DraftController($scope, $http) {
    $scope.QUARTERBACK = "QUARTERBACK";
    $scope.RUNNING_BACK = "RUNNING_BACK";
    $scope.WIDE_RECEIVER = "WIDE_RECEIVER";
    $scope.TIGHT_END = "TIGHT_END";
    $scope.DEFENSE = "DEFENSE";
    $scope.KICKER = "KICKER";

    $scope.ownerMaxNeed = {};
    $scope.ownerMaxNeed[$scope.QUARTERBACK] = 2;
    $scope.ownerMaxNeed[$scope.RUNNING_BACK] = 7;
    $scope.ownerMaxNeed[$scope.WIDE_RECEIVER] = 7;
    $scope.ownerMaxNeed[$scope.TIGHT_END] = 2;
    $scope.ownerMaxNeed[$scope.DEFENSE] = 1;
    $scope.ownerMaxNeed[$scope.KICKER] = 1;

    /*
     * Initialize the needs of each owner.
     */
    $scope.ownerNeed = [];

    /**
     * Keeps track of how many players at each position an owner is allowed to start.
     *
     * @type {{}}
     */
    $scope.startablePositions = {};
    $scope.startablePositions[$scope.QUARTERBACK] = 1;
    $scope.startablePositions[$scope.RUNNING_BACK] = 2;
    $scope.startablePositions[$scope.WIDE_RECEIVER] = 3;
    $scope.startablePositions[$scope.TIGHT_END] = 1;
    $scope.startablePositions[$scope.DEFENSE] = 1;
    $scope.startablePositions[$scope.KICKER] = 1;

    /**
     * The total number of spots on each owner's roster.
     *
     * @type {number}
     */
    $scope.totalRosterSpots = 16;


    /**
     * Set up replacement players.
     *
     * @type {{}}
     */
    $scope.replacements = {};

    /**
     * Defines the draft type that we're using.  Defaults to snake draft.
     *
     * @type {string}
     */
    $scope.DRAFT_TYPE_SNAKE = "SNAKE";

    /**
     * Flag to keep track of whether the draft has been initialized.
     *
     * @type {boolean}
     */
    $scope.initialized = false;

    /**
     * Initialize the current pick to 1.
     *
     * @type {number}
     */
    $scope.currentPick = 1;

    /**
     *
     *
     * @type {Array}
     */
    $scope.owners = new Array();

    $scope.available_qbs = new Array();
    $scope.available_rbs = new Array();
    $scope.available_wrs = new Array();
    $scope.available_tes = new Array();
    $scope.available_ds = new Array();
    $scope.available_ks = new Array();

    /**
     * The number of owners participating in this draft.
     *
     * @type {number}
     */
    $scope.numOwners = 0;

    /**
     * The # pick that I have in the draft.
     *
     * @type {number}
     */
    $scope.myPick = 1;

    /**
     * The current pick.
     *
     * @type {number}
     */
    $scope.currentPick = 1;

    /**
     * In the section containing the owners, allow up to 6 owners in each row.
     *
     * @type {number}
     */
    $scope.maxOwnersPerRow = 6;

    /**
     * The style of draft we're using.  Defaults to snake.
     *
     * @type {string}
     */
    $scope.draftType = $scope.DRAFT_TYPE_SNAKE;

    /**
     * The year that the draft is taking place.  Defaults to the current year.
     *
     * @type {number}
     */
    $scope.draftYear = new Date().getFullYear();

    /**
     * Flag to let us know if we're running the draft as a simulation.
     *
     * @type {boolean}
     */
    $scope.simulation = false;

    $scope.fetchPlayers = function() {
        for(var i=0; i<$scope.numOwners; i++) {
            $scope.owners[i] = new Array();
        }

        for(var i=0; i<$scope.numOwners; i++) {
            $scope.ownerNeed[i] = {};
            $scope.ownerNeed[i][$scope.QUARTERBACK] = $scope.ownerMaxNeed[$scope.QUARTERBACK];
            $scope.ownerNeed[i][$scope.RUNNING_BACK] = $scope.ownerMaxNeed[$scope.RUNNING_BACK];
            $scope.ownerNeed[i][$scope.WIDE_RECEIVER] = $scope.ownerMaxNeed[$scope.WIDE_RECEIVER];
            $scope.ownerNeed[i][$scope.TIGHT_END] = $scope.ownerMaxNeed[$scope.TIGHT_END];
            $scope.ownerNeed[i][$scope.DEFENSE] = $scope.ownerMaxNeed[$scope.DEFENSE];
            $scope.ownerNeed[i][$scope.KICKER] = $scope.ownerMaxNeed[$scope.KICKER];
        }

        $http.get("draft/players?year=" + $scope.draftYear).success(function(data) {
            $scope.players = data;

            var index = [0,0,0,0,0,0];
            for(p in $scope.players) {
                if($scope.players[p].position == $scope.QUARTERBACK) {
                    $scope.available_qbs[index[0]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.RUNNING_BACK) {
                    $scope.available_rbs[index[1]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.WIDE_RECEIVER) {
                    $scope.available_wrs[index[2]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.TIGHT_END) {
                    $scope.available_tes[index[3]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.DEFENSE) {
                    $scope.available_ds[index[4]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.KICKER) {
                    $scope.available_ks[index[5]++] = $scope.players[p];
                }
            }

            /*
             * Determine replacement players and calculate VORP.
             */
            var qb_idx = ($scope.startablePositions[$scope.QUARTERBACK] * $scope.numOwners)-1;
            var rb_idx = ($scope.startablePositions[$scope.RUNNING_BACK] * $scope.numOwners)-1;
            var wr_idx = ($scope.startablePositions[$scope.WIDE_RECEIVER] * $scope.numOwners)-1;
            var te_idx = ($scope.startablePositions[$scope.TIGHT_END] * $scope.numOwners)-1;
            var d_idx = ($scope.startablePositions[$scope.DEFENSE] * $scope.numOwners)-1;
            var k_idx = ($scope.startablePositions[$scope.KICKER] * $scope.numOwners)-1;

            $scope.replacements[$scope.QUARTERBACK] = $scope.available_qbs[ qb_idx ];
            $scope.replacements[$scope.RUNNING_BACK] = $scope.available_rbs[ rb_idx ];
            $scope.replacements[$scope.WIDE_RECEIVER] = $scope.available_wrs[ wr_idx ];
            $scope.replacements[$scope.TIGHT_END] = $scope.available_tes[ te_idx ];
            $scope.replacements[$scope.DEFENSE] = $scope.available_ds[d_idx];
            $scope.replacements[$scope.KICKER] = $scope.available_ks[k_idx];

            $scope.calculateVORP($scope.QUARTERBACK);
            $scope.calculateVORP($scope.RUNNING_BACK);
            $scope.calculateVORP($scope.WIDE_RECEIVER);
            $scope.calculateVORP($scope.TIGHT_END);
            $scope.calculateVORP($scope.DEFENSE);
            $scope.calculateVORP($scope.KICKER);

            if($scope.simulation) {
                $scope.simulateDraft();
            }
        });
    }

    $scope.calculateVORP = function(position) {
        if(position == $scope.QUARTERBACK) {
            for(p in $scope.available_qbs) {
                $scope.available_qbs[p].vorp = $scope.available_qbs[p].points - $scope.replacements[$scope.QUARTERBACK].points;
            }
        }
        else if(position == $scope.RUNNING_BACK) {
            for(p in $scope.available_rbs) {
                $scope.available_rbs[p].vorp = $scope.available_rbs[p].points - $scope.replacements[$scope.RUNNING_BACK].points;
            }
        }
        else if(position == $scope.WIDE_RECEIVER) {
            for(p in $scope.available_wrs) {
                $scope.available_wrs[p].vorp = $scope.available_wrs[p].points - $scope.replacements[$scope.WIDE_RECEIVER].points;
            }
        }
        else if(position == $scope.TIGHT_END) {
            for(p in $scope.available_tes) {
                $scope.available_tes[p].vorp = $scope.available_tes[p].points - $scope.replacements[$scope.TIGHT_END].points;
            }
        }
        else if(position == $scope.DEFENSE) {
            for(p in $scope.available_ds) {
                $scope.available_ds[p].vorp = $scope.available_ds[p].points - $scope.replacements[$scope.DEFENSE].points;
            }
        }
        else if(position == $scope.KICKER) {
            for(p in $scope.available_ks) {
                $scope.available_ks[p].vorp = $scope.available_ks[p].points - $scope.replacements[$scope.KICKER].points;
            }
        }
    }

    $scope.calculateValue = function(adp, vorp, need) {
        /*
         * Tight end - 0.62
         * Quarterback - 0.60
         * Running back - 0.48
         * Wide receivers - 0.42
         * Defense - 0.1
         * Kickers - 0.1
         */
//        var consistency = 0;
//        if(position == $scope.QUARTERBACK) {
//            consistency = 0.60;
//        }
//        else if(position == $scope.RUNNING_BACK) {
//            consistency = 0.48;
//        }
//        else if(position == $scope.WIDE_RECEIVER) {
//            consistency = 0.42;
//        }
//        else if(position == $scope.TIGHT_END) {
//            consistency = 0.62;
//        }
//        else if(position == $scope.DEFENSE || position == $scope.KICKER) {
//            consistency = 0.1;
//        }

        var adpFactor = 1;

        // ADP is less than current pick --> Getting a bargain
        if(adp < $scope.currentPick) {
            adpFactor = 1 + (($scope.currentPick - adp)/adp);
        }
        // ADP is greater than current pick --> reaching
        else if(adp > $scope.currentPick) {
            adpFactor = 1 - (Math.abs($scope.currentPick - adp)/adp);
        }

        // Truncate the value to three decimal spots.
        return Math.floor( adpFactor * (vorp+1) * need * /*consistency */ 1000) / 1000;
    }

    /**
     * Translate the encoded position name into a shorter, more familiar one.
     *
     * @param position
     * @returns {string}
     */
    $scope.getPositionDisplayText = function(position) {
        if(position == $scope.QUARTERBACK) {
            return "QB";
        }
        else if(position == $scope.RUNNING_BACK) {
            return "RB";
        }
        else if(position == $scope.WIDE_RECEIVER) {
            return "WR";
        }
        else if(position == $scope.TIGHT_END) {
            return "TE";
        }
        else if(position == $scope.DEFENSE) {
            return "DEF";
        }
        else if(position == $scope.KICKER) {
            return "K";
        }
    }

    /**
     * Performs a check to make sure the value entered for the
     * number of owners is a positive integer.
     *
     * @param numOwners
     * @returns {boolean}
     */
    $scope.isNumOwnersValid = function(numOwners) {
        var n = Number(numOwners);
        if(n <= 0) {
            return false;
        }

        if(/^[0-9]+$/.test(numOwners)) {
            return true;
        }

        return false;
    }

    /**
     * Calculate which owners belong in a given row div in the section where owners are displayed.
     *
     * @param rowNum
     * @param maxOwnersPerRow
     * @param numOwners
     * @returns {Array}
     */
    $scope.ownersPerRow = function(rowNum, numOwners) {
        var input = [];
        var start = rowNum * $scope.maxOwnersPerRow;
        var end = ( (start + $scope.maxOwnersPerRow) > numOwners) ? numOwners : start + $scope.maxOwnersPerRow;

        for(var i=start; i<end; i++) {
            input.push(i);
        }

        return input;
    }

    $scope.getOwnerRows = function() {
        return Math.ceil($scope.numOwners/$scope.maxOwnersPerRow);
    }

    /**
     * Draft the specified player.  This involves finding the playerId from the
     * list of available players of the associated playerType.  As a post-condition,
     * the player should be absent from the list of available players and present
     * in the appropriate owner's player list.
     *
     * @param playerType
     * @param playerId
     */
    $scope.draftPlayer = function(playerType, playerId) {
        var draftedPlayer = undefined;
        var replacementIndex = -1;

        var ownerIndex = $scope.getOwnerPick();

        if(playerType == $scope.QUARTERBACK) {
            for(var i=0; i<$scope.available_qbs.length; i++) {
                /*
                 * Check to see if we've reached the replacement player.  If we've
                 * gotten here then regardless of who gets drafted, we need to change
                 * who the replacement player is.
                 */
                if($scope.available_qbs[i] == $scope.replacements[$scope.QUARTERBACK]) {
                    replacementIndex = i;
                }

                if($scope.available_qbs[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_qbs.splice(i,1);

            // Did we reach the replacement player?  If so, adjust.
            if(replacementIndex != -1) {
                $scope.replacements[$scope.QUARTERBACK] = $scope.available_qbs[replacementIndex-1];
            }

            // Decrement our owner's needed quarterback count
            $scope.ownerNeed[ownerIndex][$scope.QUARTERBACK]--;
        }
        else if(playerType == $scope.RUNNING_BACK) {
            for(var i=0; i<$scope.available_rbs.length; i++) {
                /*
                 * Check to see if we've reached the replacement player.  If we've
                 * gotten here then regardless of who gets drafted, we need to change
                 * who the replacement player is.
                 */
                if($scope.available_rbs[i] == $scope.replacements[$scope.RUNNING_BACK]) {
                    replacementIndex = i;
                }

                if($scope.available_rbs[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_rbs.splice(i,1);

            // Did we reach the replacement player?  If so, adjust.
            if(replacementIndex != -1) {
                $scope.replacements[$scope.RUNNING_BACK] = $scope.available_rbs[replacementIndex-1];
            }

            // Decrement our owner's needed running back count
            $scope.ownerNeed[ownerIndex][$scope.RUNNING_BACK]--;
        }
        else if(playerType == $scope.WIDE_RECEIVER) {
            for(var i=0; i<$scope.available_wrs.length; i++) {
                /*
                 * Check to see if we've reached the replacement player.  If we've
                 * gotten here then regardless of who gets drafted, we need to change
                 * who the replacement player is.
                 */
                if($scope.available_wrs[i] == $scope.replacements[$scope.WIDE_RECEIVER]) {
                    replacementIndex = i;
                }

                if($scope.available_wrs[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_wrs.splice(i,1);

            // Did we reach the replacement player?  If so, adjust.
            if(replacementIndex != -1) {
                $scope.replacements[$scope.WIDE_RECEIVER] = $scope.available_wrs[replacementIndex-1];
            }

            // Decrement our owner's needed wide receiver count
            $scope.ownerNeed[ownerIndex][$scope.WIDE_RECEIVER]--;
        }
        else if(playerType == $scope.TIGHT_END) {
            for(var i=0; i<$scope.available_tes.length; i++) {
                /*
                 * Check to see if we've reached the replacement player.  If we've
                 * gotten here then regardless of who gets drafted, we need to change
                 * who the replacement player is.
                 */
                if($scope.available_tes[i] == $scope.replacements[$scope.TIGHT_END]) {
                    replacementIndex = i;
                }

                if($scope.available_tes[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_tes.splice(i,1);

            // Did we reach the replacement player?  If so, adjust.
            if(replacementIndex != -1) {
                $scope.replacements[$scope.TIGHT_END] = $scope.available_tes[replacementIndex-1];
            }

            // Decrement our owner's needed tight end count
            $scope.ownerNeed[ownerIndex][$scope.TIGHT_END]--;
        }
        else if(playerType == $scope.DEFENSE) {
            for(var i=0; i<$scope.available_ds.length; i++) {
                /*
                 * Check to see if we've reached the replacement player.  If we've
                 * gotten here then regardless of who gets drafted, we need to change
                 * who the replacement player is.
                 */
                if($scope.available_ds[i] == $scope.replacements[$scope.DEFENSE]) {
                    replacementIndex = i;
                }

                if($scope.available_ds[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_ds.splice(i,1);

            // Did we reach the replacement player?  If so, adjust.
            if(replacementIndex != -1) {
                $scope.replacements[$scope.DEFENSE] = $scope.available_ds[replacementIndex-1];
            }

            // Decrement our owner's needed defense count
            $scope.ownerNeed[ownerIndex][$scope.DEFENSE]--;
        }
        else if(playerType == $scope.KICKER) {
            for(var i=0; i<$scope.available_ks.length; i++) {
                /*
                 * Check to see if we've reached the replacement player.  If we've
                 * gotten here then regardless of who gets drafted, we need to change
                 * who the replacement player is.
                 */
                if($scope.available_ks[i] == $scope.replacements[$scope.KICKER]) {
                    replacementIndex = i;
                }

                if($scope.available_ks[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_ks.splice(i,1);

            // Did we reach the replacement player?  If so, adjust.
            if(replacementIndex != -1) {
                $scope.replacements[$scope.KICKER] = $scope.available_ks[replacementIndex-1];
            }

            // Decrement our owner's needed kicker count
            $scope.ownerNeed[ownerIndex][$scope.KICKER]--;
        }

        // If this is the first player's pick, we need to create a new array
        // for that owner.
        if($scope.owners[ownerIndex] == undefined) {
            $scope.owners[ownerIndex] = new Array();
        }

        $scope.owners[ownerIndex].push(draftedPlayer[0]);

        $scope.currentPick++;
    }

    /**
     * Determine whether it is my pick based on the depth of the tree that we're analyzing.
     *
     * Due to the math, it's easier to use 0-based currentPick and myPick.
     *
     * @param draftPosition      The number pick of the owner in question (1-based).
     */
    $scope.isOwnersPick = function(draftPosition) {
        // If it's a snake draft, are we going forwards or backwards through the owners?
        if ($scope.draftType == $scope.DRAFT_TYPE_SNAKE) {
            /*
             * Forward
             *
             * Assume 10 owners
             * Picks p={0-9} (first round) will yield p/#owners = 0
             * Picks p={20-29} (third round) will yield p/#owners = 2
             */
            if ( Math.floor(($scope.currentPick-1)/$scope.numOwners) % 2 == 0) {
                return Math.ceil( ($scope.currentPick-1) % $scope.numOwners) == draftPosition-1;
            }
            /*
             * Backwards
             *
             * Picks p={10-19} (second round) will yield p/#owners = 1
             * Picks p={30-39} (fourth round) will yield p/#owners = 3
             */
            else {
                var reversePick = $scope.numOwners - draftPosition;
                return Math.ceil(($scope.currentPick-1) % $scope.numOwners) == reversePick;
            }
        }
    }

    /**
     * Determine which owner has the current pick.
     *
     * @param pick          The current pick (1-based).
     * @returns {boolean}
     */
    $scope.getOwnerPick = function() {
        // If it's a snake draft, are we going forwards or backwards through the owners?
        if ($scope.draftType == $scope.DRAFT_TYPE_SNAKE) {
            var forwardPick = Math.ceil( ($scope.currentPick-1) % $scope.numOwners);

            /*
             * Forward
             *
             * Assume 10 owners
             * Picks p={0-9} (first round) will yield p/#owners = 0
             * Picks p={20-29} (third round) will yield p/#owners = 2
             */
            if ( Math.floor(($scope.currentPick-1)/$scope.numOwners) % 2 == 0) {
                return forwardPick;
            }
            /*
             * Backwards
             *
             * Picks p={10-19} (second round) will yield p/#owners = 1
             * Picks p={30-39} (fourth round) will yield p/#owners = 3
             */
            else {
                return ($scope.numOwners - forwardPick)-1;
            }
        }
    }



    /**
     * Determine the total projected points so far for an owner.
     *
     * @param owner     The number (0-based) of the owner that we want to calculate points for.
     */
    $scope.calculateTotalPoints = function(owner) {
        if(!$scope.initialized) {
            return;
        }

        var sum = 0;
        for(var i=0; i<$scope.owners[owner].length; i++) {
            sum += $scope.owners[owner][i].points;
        }

        return sum;
    }

    /**
     * Determine the total projected points for your starters, who are defined as the top n players
     * at each position, where n is the number of startable players at that position.
     *
     * @param owner         Index of the owner (0-based)
     * @returns {Object}    The sum of the points projected by your starters.
     */
    $scope.calculateStarterPoints = function(owner) {
        if(!$scope.initialized || $scope.owners[owner].length < $scope.totalRosterSpots) {
            return;
        }

        var sortFunc = function(p1,p2) {
            return p2.points - p1.points;
        }

        var arrSum = function(prev,curr) {
            return prev + curr.points;
        }

        var qbs = [];
        var rbs = [];
        var wrs = [];
        var tes = [];
        var ds = [];
        var ks = [];
        for(var i=0; i<$scope.owners[owner].length; i++) {
            if($scope.owners[owner][i].position == $scope.QUARTERBACK) {
                qbs.push($scope.owners[owner][i]);
            }
            else if($scope.owners[owner][i].position == $scope.RUNNING_BACK) {
                rbs.push($scope.owners[owner][i]);
            }
            if($scope.owners[owner][i].position == $scope.WIDE_RECEIVER) {
                wrs.push($scope.owners[owner][i]);
            }
            if($scope.owners[owner][i].position == $scope.TIGHT_END) {
                tes.push($scope.owners[owner][i]);
            }
            if($scope.owners[owner][i].position == $scope.DEFENSE) {
                ds.push($scope.owners[owner][i]);
            }
            if($scope.owners[owner][i].position == $scope.KICKER) {
                ks.push($scope.owners[owner][i]);
            }
        }

        qbs.sort(sortFunc);
        rbs.sort(sortFunc);
        wrs.sort(sortFunc);
        tes.sort(sortFunc);
        ds.sort(sortFunc);
        ks.sort(sortFunc);


        try {
            var qbSum = qbs.slice(0, $scope.startablePositions[$scope.QUARTERBACK]).reduce(arrSum, 0);
        }
        catch(e) {
            console.log(e);
        }

        try {
            var rbSum = rbs.slice(0, $scope.startablePositions[$scope.RUNNING_BACK]).reduce(arrSum, 0);
        }
        catch(e) {
            console.log(e);
        }

        try {
            var wrSum = wrs.slice(0, $scope.startablePositions[$scope.WIDE_RECEIVER]).reduce(arrSum, 0);
        }
        catch(e) {
            console.log(e);
        }

        try {
            var teSum = tes.slice(0, $scope.startablePositions[$scope.TIGHT_END]).reduce(arrSum, 0);
        }
        catch(e) {
            console.log(e);
        }

        try {
            var dSum = ds.slice(0, $scope.startablePositions[$scope.DEFENSE]).reduce(arrSum, 0);
        }
        catch(e) {
            console.log(e);
        }

        try {
            var kSum = ks.slice(0, $scope.startablePositions[$scope.KICKER]).reduce(arrSum, 0);
        }
        catch(e) {
            console.log(e);
        }

        return qbSum + rbSum + wrSum + teSum + dSum + kSum;
    }

    $scope.getPlayerPosition = function(playerId) {
        for(var i=0; i<$scope.players; i++) {
            if($scope.players[i].id == playerId) {
                return $scope.players[i].position;
            }
        }
    }

    $scope.simulateDraft = function() {
        console.log("Simulating draft...");

        var player = undefined;

        while($scope.owners[0].length < $scope.totalRosterSpots || $scope.owners[$scope.numOwners-1].length < $scope.totalRosterSpots) {
            // Is this my pick?  If so, use VORP.
            if($scope.isOwnersPick($scope.myPick)) {
                player = $scope.getVORP();
                if(player == undefined) {
                    console.log("Found undefined player");
                    player = $scope.getVORP();
                }
            }
            // Not my pick.  Use BPA.
            else {
                player = $scope.getBPAWithStartersFirst();
                if(player == undefined) {
                    console.log("Found undefined player");
                }
            }

            $scope.draftPlayer(player.position, player.id);
        }
    }

    /**
     * Chooses the best player for the current owner based on their need and
     * the best player available (by points) at the those positions.
     *
     * @returns Id of the available player with the highest projected points.
     */
    $scope.getBPA = function() {
        if(!$scope.initialized) {
            return;
        }

        var owner = $scope.getOwnerPick();
        var round = Math.floor( ($scope.currentPick-1)/$scope.numOwners) + 1;

        var list = [];

        // Only pick non-kickers and defenses in all but last two rounds.
        if(round < $scope.totalRosterSpots-1) {
            if($scope.ownerNeed[owner][$scope.QUARTERBACK] > 0 && $scope.available_qbs.length > 0) {
                list.splice(0,0,$scope.available_qbs);
            }
            if($scope.ownerNeed[owner][$scope.RUNNING_BACK] > 0 && $scope.available_rbs.length > 0) {
                list.splice(0,0,$scope.available_rbs);
            }
            if($scope.ownerNeed[owner][$scope.WIDE_RECEIVER] > 0 && $scope.available_wrs.length > 0) {
                list.splice(0,0,$scope.available_wrs);
            }
            if($scope.ownerNeed[owner][$scope.TIGHT_END] > 0 && $scope.available_tes.length > 0) {
                list.splice(0,0,$scope.available_tes);
            }
        }
        else {
            if($scope.ownerNeed[owner][$scope.DEFENSE] > 0 && $scope.available_ds.length > 0) {
                list.splice(0,0,$scope.available_ds);
            }
            if($scope.ownerNeed[owner][$scope.KICKER] > 0 && $scope.available_ks.length > 0) {
                list.splice(0,0,$scope.available_ks);
            }
        }

        var max = -9999;
        var player = undefined;
        for(var i=0; i<list.length; i++) {
            if(list[i][0].points > max) {
                max = list[i][0].points;
                player = list[i][0];
            }
        }

        return player;
    }

    /**
     * Chooses the best player for the current owner based on their need and
     * the best player available (by points) at the those positions.
     *
     * Unlike getBPA, we're going to satisfy all the starter positions first.
     *
     * @returns Id of the available player with the highest projected points.
     */
    $scope.getBPAWithStartersFirst = function() {
        if(!$scope.initialized) {
            return;
        }

        var owner = $scope.getOwnerPick();
        var round = Math.floor( ($scope.currentPick-1)/$scope.numOwners) + 1;

        var list = [];

        if( ($scope.ownerMaxNeed[$scope.QUARTERBACK] - $scope.ownerNeed[owner][$scope.QUARTERBACK]) < $scope.startablePositions[$scope.QUARTERBACK] && $scope.available_qbs.length > 0) {
            list.splice(0,0,$scope.available_qbs);
        }
        if( ($scope.ownerMaxNeed[$scope.RUNNING_BACK] - $scope.ownerNeed[owner][$scope.RUNNING_BACK]) < $scope.startablePositions[$scope.RUNNING_BACK] && $scope.available_rbs.length > 0) {
            list.splice(0,0,$scope.available_rbs);
        }
        if( ($scope.ownerMaxNeed[$scope.WIDE_RECEIVER] - $scope.ownerNeed[owner][$scope.WIDE_RECEIVER]) < $scope.startablePositions[$scope.WIDE_RECEIVER] && $scope.available_wrs.length > 0) {
            list.splice(0,0,$scope.available_wrs);
        }
        if( ($scope.ownerMaxNeed[$scope.TIGHT_END] - $scope.ownerNeed[owner][$scope.TIGHT_END]) < $scope.startablePositions[$scope.TIGHT_END] && $scope.available_tes.length > 0) {
            list.splice(0,0,$scope.available_tes);
        }

        /*
         * Looks like we've drafted all our starters.
         */
        if(list.length == 0) {
            // Only pick non-kickers and defenses in all but last two rounds.
            if(round < $scope.totalRosterSpots-1) {
                if($scope.ownerNeed[owner][$scope.QUARTERBACK] > 0 && $scope.available_qbs.length > 0) {
                    list.splice(0,0,$scope.available_qbs);
                }
                if($scope.ownerNeed[owner][$scope.RUNNING_BACK] > 0 && $scope.available_rbs.length > 0) {
                    list.splice(0,0,$scope.available_rbs);
                }
                if($scope.ownerNeed[owner][$scope.WIDE_RECEIVER] > 0 && $scope.available_wrs.length > 0) {
                    list.splice(0,0,$scope.available_wrs);
                }
                if($scope.ownerNeed[owner][$scope.TIGHT_END] > 0 && $scope.available_tes.length > 0) {
                    list.splice(0,0,$scope.available_tes);
                }
            }
            else {
                if($scope.ownerNeed[owner][$scope.DEFENSE] > 0 && $scope.available_ds.length > 0) {
                    list.splice(0,0,$scope.available_ds);
                }
                if($scope.ownerNeed[owner][$scope.KICKER] > 0 && $scope.available_ks.length > 0) {
                    list.splice(0,0,$scope.available_ks);
                }
            }
        }

        var max = -9999;
        var player = undefined;
        for(var i=0; i<list.length; i++) {
            if(list[i][0].points > max) {
                max = list[i][0].points;
                player = list[i][0];
            }
        }

        return player;
    }

    /**
     * Chooses the player remaining with the highest VORP.
     *
     * @returns Id of the available player with the highest VORP.
     */
    $scope.getVORP = function() {
        if(!$scope.initialized) {
            return;
        }

        var round = Math.floor( ($scope.currentPick-1)/$scope.numOwners) + 1;
        var list = [];

        /*
         * Only pick non-kickers and defenses in all but last two rounds.
         *
         * We're going to evaluate the starter situation and make sure that the first n picks get starters.
         * This is to prevent situations where the owner may never be in a situation where a certain position,
         * like quarterback, is the highest VORP, and they don't pick any quarterbacks.
         */
        if(round < $scope.totalRosterSpots-1) {
            // Check starter situation for QB, RB, WR, and TE.  If there are still starting spots available,
            // qualify the list of players at that position for being picked.
            if($scope.ownerMaxNeed[$scope.QUARTERBACK] - $scope.ownerNeed[$scope.myPick-1][$scope.QUARTERBACK] < $scope.startablePositions[$scope.QUARTERBACK]) {
//                console.log("Still have " + $scope.ownerNeed[$scope.myPick-1][$scope.QUARTERBACK] + " starting spot at QB");
                list.push($scope.available_qbs);
            }
            if($scope.ownerMaxNeed[$scope.RUNNING_BACK] - $scope.ownerNeed[$scope.myPick-1][$scope.RUNNING_BACK] < $scope.startablePositions[$scope.RUNNING_BACK]) {
//                console.log("Still have " + $scope.ownerNeed[$scope.myPick-1][$scope.RUNNING_BACK] + " starting spot at RB");
                list.push($scope.available_rbs);
            }
            if($scope.ownerMaxNeed[$scope.WIDE_RECEIVER] - $scope.ownerNeed[$scope.myPick-1][$scope.WIDE_RECEIVER] < $scope.startablePositions[$scope.WIDE_RECEIVER]) {
//                console.log("Still have " + $scope.ownerNeed[$scope.myPick-1][$scope.WIDE_RECEIVER] + " starting spot at WR");
                list.push($scope.available_wrs);
            }
            if($scope.ownerMaxNeed[$scope.TIGHT_END] - $scope.ownerNeed[$scope.myPick-1][$scope.TIGHT_END] < $scope.startablePositions[$scope.TIGHT_END]) {
//                console.log("Still have " + $scope.ownerNeed[$scope.myPick-1][$scope.TIGHT_END] + " starting spot at TE");
                list.push($scope.available_tes);
            }

            // list length is 0.  We've filled all our starting positions.
            if(list.length == 0) {
                list = [$scope.available_qbs, $scope.available_rbs, $scope.available_wrs, $scope.available_tes];
            }
        }
        else {
            list = [$scope.available_ds, $scope.available_ks];
        }

        var max = -9999;
        var player = undefined;
//        console.log("Current choices for round " + round);
        for(var i=0; i<list.length; i++) {
            // Only draft players we need.
            var need = $scope.ownerNeed[$scope.myPick-1][list[i][0].position]/$scope.ownerMaxNeed[list[i][0].position];
            if(need == 0) {
                continue;
            }

            var value = $scope.calculateValue(list[i][0].adp, list[i][0].vorp, need, list[i][0].position);
//            console.log("\t" + list[i][0].name + " (" + $scope.getPositionDisplayText(list[i][0].position) + ") - " + value);
            if(value > max) {
                max = value;
                player = list[i][0];
            }
        }

        return player;
    }
}