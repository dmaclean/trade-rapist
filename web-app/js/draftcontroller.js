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
                                '<span id="owner{{ o }}_points">Total Projected Points: {{ calculateTotalPoints(o) }}</span>' +
                                '<span class="label label-success" ng-hide="o != myPick-1">My pick</span>' +
                                '<span class="label label-important" ng-hide="!isOwnersPick(o+1)">Current pick</span>' +
                                '<ol><li ng-repeat="player in owners[o]">{{ player.name }}</li></ol>' +
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
    $scope.ownerMaxNeed[$scope.TIGHT_END] = 3;
    $scope.ownerMaxNeed[$scope.DEFENSE] = 1;
    $scope.ownerMaxNeed[$scope.KICKER] = 1;

    $scope.ownerNeed = {};
    $scope.ownerNeed[$scope.QUARTERBACK] = $scope.ownerMaxNeed[$scope.QUARTERBACK];
    $scope.ownerNeed[$scope.RUNNING_BACK] = $scope.ownerMaxNeed[$scope.RUNNING_BACK];
    $scope.ownerNeed[$scope.WIDE_RECEIVER] = $scope.ownerMaxNeed[$scope.WIDE_RECEIVER];
    $scope.ownerNeed[$scope.TIGHT_END] = $scope.ownerMaxNeed[$scope.TIGHT_END];
    $scope.ownerNeed[$scope.DEFENSE] = $scope.ownerMaxNeed[$scope.DEFENSE];
    $scope.ownerNeed[$scope.KICKER] = $scope.ownerMaxNeed[$scope.KICKER];

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

    $scope.fetchPlayers = function() {
        for(var i=0; i<$scope.numOwners; i++) {
            $scope.owners[i] = new Array();
        }

        $http.get("draft/players?year=" + $scope.draftYear).success(function(data) {
            $scope.players = data;

            var index = [0,0,0,0,0,0];
            for(p in $scope.players) {
                if($scope.players[p].position == $scope.QUARTERBACK) {
//                    console.log("Adding quarterback " + $scope.players[p].name + " to list of available quarterbacks.");
                    $scope.available_qbs[index[0]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.RUNNING_BACK) {
//                    console.log("Adding running back " + $scope.players[p].name + " to list of available running backs.");
                    $scope.available_rbs[index[1]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.WIDE_RECEIVER) {
//                    console.log("Adding wide receiver " + $scope.players[p].name + " to list of available wide receiver.");
                    $scope.available_wrs[index[2]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.TIGHT_END) {
//                    console.log("Adding tight end " + $scope.players[p].name + " to list of available tight end.");
                    $scope.available_tes[index[3]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.DEFENSE) {
//                    console.log("Adding defense " + $scope.players[p].name + " to list of available defenses.");
                    $scope.available_ds[index[4]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.KICKER) {
//                    console.log("Adding kicker " + $scope.players[p].name + " to list of available kickers.");
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

    $scope.calculateValue = function(adp, vorp, need, position) {
        /*
         * Tight end - 0.62
         * Quarterback - 0.60
         * Running back - 0.48
         * Wide receivers - 0.42
         * Defense - 0.1
         * Kickers - 0.1
         */
        var consistency = 0;
        if(position == $scope.QUARTERBACK) {
            consistency = 0.60;
        }
        else if(position == $scope.RUNNING_BACK) {
            consistency = 0.48;
        }
        else if(position == $scope.WIDE_RECEIVER) {
            consistency = 0.42;
        }
        else if(position == $scope.TIGHT_END) {
            consistency = 0.62;
        }
        else if(position == $scope.DEFENSE || position == $scope.KICKER) {
            consistency = 0.1;
        }

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
        return Math.floor( adpFactor * (vorp+1) * need * consistency * 1000) / 1000;
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
            if($scope.isOwnersPick($scope.myPick)) {
                $scope.ownerNeed[$scope.QUARTERBACK]--;
            }
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
            if($scope.isOwnersPick($scope.myPick)) {
                $scope.ownerNeed[$scope.RUNNING_BACK]--;
            }
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
            if($scope.isOwnersPick($scope.myPick)) {
                $scope.ownerNeed[$scope.WIDE_RECEIVER]--;
            }
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
            if($scope.isOwnersPick($scope.myPick)) {
                $scope.ownerNeed[$scope.TIGHT_END]--;
            }
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
            if($scope.isOwnersPick($scope.myPick)) {
                $scope.ownerNeed[$scope.DEFENSE]--;
            }
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
            if($scope.isOwnersPick($scope.myPick)) {
                $scope.ownerNeed[$scope.KICKER]--;
            }
        }

        // Which owner made this pick?
        for(var owner=0; owner<$scope.numOwners; owner++) {
            if($scope.isOwnersPick(owner+1)) {
                break;
            }
        }

        // If this is the first player's pick, we need to create a new array
        // for that owner.
        if($scope.owners[owner] == undefined) {
            $scope.owners[owner] = new Array();
        }

        $scope.owners[owner].push(draftedPlayer[0]);

        $scope.currentPick++;
    }

    /**
     * Determine whether it is my pick based on the depth of the tree that we're analyzing.
     *
     * Due to the math, it's easier to use 0-based currentPick and myPick.
     *
     * @param pick      The number pick of the owner in question.
     */
    $scope.isOwnersPick = function(pick) {
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
                return Math.ceil( ($scope.currentPick-1) % $scope.numOwners) == pick-1;
            }
            /*
             * Backwards
             *
             * Picks p={10-19} (second round) will yield p/#owners = 1
             * Picks p={30-39} (fourth round) will yield p/#owners = 3
             */
            else {
                var reversePick = $scope.numOwners - pick;
                return Math.ceil(($scope.currentPick-1) % $scope.numOwners) == reversePick;
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
     * TEMP!
     */
    $scope.getBPA = function(owner) {
        if(!$scope.initialized) {
            return;
        }

        var list = [$scope.available_qbs, $scope.available_rbs, $scope.available_wrs, $scope.available_tes, $scope.available_ds,
            $scope.available_ks];

        var max = 0;
        var name = "";
        for(var i=0; i<list.length; i++) {
            if(list[i][0].points > max) {
                max = list[i][0].points;
                name = list[i][0].name;
            }
        }

        return name;
    }

    $scope.getVORP = function() {
        if(!$scope.initialized) {
            return;
        }

        var list = [$scope.available_qbs, $scope.available_rbs, $scope.available_wrs, $scope.available_tes, $scope.available_ds,
        $scope.available_ks];

        var max = 0;
        var name = "";
        for(var i=0; i<list.length; i++) {
            if(list[i][0].vorp > max) {
                max = list[i][0].vorp;
                name = list[i][0].name;
            }
        }

        return name;
    }
}