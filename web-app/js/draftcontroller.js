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
        $http.get("draft/players?year=" + $scope.draftYear).success(function(data) {
            $scope.players = data;

            var index = [0,0,0,0,0,0];
            for(p in $scope.players) {
                if($scope.players[p].position == $scope.QUARTERBACK) {
                    console.log("Adding quarterback " + $scope.players[p].name + " to list of available quarterbacks.");
                    $scope.available_qbs[index[0]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.RUNNING_BACK) {
                    console.log("Adding running back " + $scope.players[p].name + " to list of available running backs.");
                    $scope.available_rbs[index[1]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.WIDE_RECEIVER) {
                    console.log("Adding wide receiver " + $scope.players[p].name + " to list of available wide receiver.");
                    $scope.available_wrs[index[2]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.TIGHT_END) {
                    console.log("Adding tight end " + $scope.players[p].name + " to list of available tight end.");
                    $scope.available_tes[index[3]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.DEFENSE) {
                    console.log("Adding defense " + $scope.players[p].name + " to list of available defenses.");
                    $scope.available_ds[index[4]++] = $scope.players[p];
                }
                else if($scope.players[p].position == $scope.KICKER) {
                    console.log("Adding kicker " + $scope.players[p].name + " to list of available kickers.");
                    $scope.available_ks[index[5]++] = $scope.players[p];
                }
            }
        });
    }

    $scope.calculateValue = function(adp, vorp, need) {
        var adpFactor = 1;

        // ADP is less than current pick --> Getting a bargain
        if(adp < $scope.currentPick) {
            adpFactor = 1 + (($scope.currentPick - adp)/adp);
        }
        // ADP is greater than current pick --> reaching
        else if(adp > $scope.currentPick) {
            adpFactor = 1 - (Math.abs($scope.currentPick - adp)/adp);
        }

        return adpFactor * vorp * need;
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

        if(playerType == $scope.QUARTERBACK) {
            for(var i=0; i<$scope.available_qbs.length; i++) {
                if($scope.available_qbs[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_qbs.splice(i,1);
        }
        else if(playerType == $scope.RUNNING_BACK) {
            for(var i=0; i<$scope.available_rbs.length; i++) {
                if($scope.available_rbs[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_rbs.splice(i,1);
        }
        else if(playerType == $scope.WIDE_RECEIVER) {
            for(var i=0; i<$scope.available_wrs.length; i++) {
                if($scope.available_wrs[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_wrs.splice(i,1);
        }
        else if(playerType == $scope.TIGHT_END) {
            for(var i=0; i<$scope.available_tes.length; i++) {
                if($scope.available_tes[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_tes.splice(i,1);
        }
        else if(playerType == $scope.DEFENSE) {
            for(var i=0; i<$scope.available_ds.length; i++) {
                if($scope.available_ds[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_ds.splice(i,1);
        }
        else if(playerType == $scope.KICKER) {
            for(var i=0; i<$scope.available_ks.length; i++) {
                if($scope.available_ks[i].id == playerId) {
                    break;
                }
            }
            draftedPlayer = $scope.available_ks.splice(i,1);
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
}