'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('Trade Rapist Draft Setup', function() {
    var scope, ctrl, $httpBackend;

    beforeEach(function() {
        browser().navigateTo('../../../index-test.html');
    });

    it('should redirect index.html to /draft', function() {
        expect(browser().location().url()).toBe('/draft');
    });

    it('should default numOwners to 0', function() {
        expect(element('#numOwners').val()).toMatch("0");
    });

    it('should not see my pick by default', function() {
        expect(element('#myPick').css('display')).toBe("none");
    });

    it('should not see Start Draft button by default', function() {
        expect(element('#draft_init button').css('display')).toBe("none");
    });

    it('should not see owners div by default', function() {
        expect(element('#owners').css('display')).toBe("none");
    });

    it('should not see available_players div by default', function() {
        expect(element('#available_players').css('display')).toBe("none");
    });

    it('should see My Pick field and Start Draft button as soon as any positive, non-zero value is entered', function() {
        // Positive, non-zero
        input('numOwners').enter("12");
        expect(element('#myPick').css('display')).not().toBe("none");
        expect(element('#draft_init button').css('display')).not().toBe("none");

        // Negative
        input('numOwners').enter("-1");
        expect(element('#myPick').css('display')).toBe("none");
        expect(element('#draft_init button').css('display')).toBe("none");

        // Non-numeric
        input('numOwners').enter("abc");
        expect(element('#myPick').css('display')).toBe("none");
        expect(element('#draft_init button').css('display')).toBe("none");
    });

    it('should show draft_action and owners divs when "Start Draft" button is clicked', function() {
        input('numOwners').enter("12");
        input('myPick').enter("1");
        element('#draft_init button').click();

        expect(element('#available_players').css('display')).not().toBe("none");
        expect(element('#owners').css('display')).not().toBe("none");
    });

    describe('Owner div creation', function() {
        it('should create 1 row and 6 owner divs when 6 owners entered', function() {
            input('draftYear').enter("2002");
            input('numOwners').enter("6");
            input('myPick').enter("1");
            element('#draft_init button').click();

            // Only 1 row
            expect(element('#owners_row_0').count()).toBe(1);
            expect(element('#owners_row_1').count()).toBe(0);

            expect(repeater('#owners_row_0 div').count()).toBe(6);
            for(var i=0; i<6; i++) {
                var id = "#owner" + i;
                expect(element(id + " h3").text()).toBe("Owner " + (i+1));
                expect(element(id + " ol li").count()).toBe(0);
            }

            expect(element('#owner0 span.label-success').text()).toBe("My pick");
            expect(element('#owner0 span.label-important').text()).toBe("Current pick");
        });

        it('should create 2 row and 7 owner divs when 7 owners entered', function() {
            input('numOwners').enter("7");
            input('myPick').enter("1");
            element('#draft_init button').click();

            // Only 1 row
            expect(element('#owners_row_0').count()).toBe(1);
            expect(element('#owners_row_1').count()).toBe(1);
            expect(element('#owners_row_1').count()).toBe(1);

            expect(repeater('#owners_row_0 div').count()).toBe(6);
            for(var i=0; i<6; i++) {
                var id = "#owner" + i;
                expect(element(id + " h3").text()).toBe("Owner " + (i+1));
                expect(element(id + " ol li").count()).toBe(0);
            }

            expect(repeater('#owners_row_1 div').count()).toBe(1);
            var id = "#owner6";
            expect(element(id + " h3").text()).toBe("Owner 7");
            expect(element(id + " ol li").count()).toBe(0);

            expect(element('#owner0 span.label-success').text()).toBe("My pick");
            expect(element('#owner0 span.label-important').text()).toBe("Current pick");
        });

        it('should create 3 rows and 18 owner divs when 18 owners entered', function() {
            input('numOwners').enter("18");
            input('myPick').enter("1");
            element('#draft_init button').click();

            // Only 1 row
            expect(element('#owners_row_0').count()).toBe(1);
            expect(element('#owners_row_1').count()).toBe(1);
            expect(element('#owners_row_2').count()).toBe(1);
            expect(element('#owners_row_3').count()).toBe(0);

            expect(repeater('#owners_row_0 div').count()).toBe(6);
            for(var i=0; i<6; i++) {
                var id = "#owner" + i;
                expect(element(id + " h3").text()).toBe("Owner " + (i+1));
                expect(element(id + " ol li").count()).toBe(0);
            }

            expect(repeater('#owners_row_1 div').count()).toBe(6);
            for(var i=7; i<12; i++) {
                var id = "#owner" + i;
                expect(element(id + " h3").text()).toBe("Owner " + (i+1));
                expect(element(id + " ol li").count()).toBe(0);
            }

            expect(repeater('#owners_row_2 div').count()).toBe(6);
            for(var i=12; i<18; i++) {
                var id = "#owner" + i;
                expect(element(id + " h3").text()).toBe("Owner " + (i+1));
                expect(element(id + " ol li").count()).toBe(0);
            }

            expect(element('#owner0 span.label-success').text()).toBe("My pick");
            expect(element('#owner0 span.label-important').text()).toBe("Current pick");
        });
    });

    describe('Players available', function() {
        it('should have RB 1_2001 as first RB in 2001', function() {
            input('draftYear').enter("2001");
            input('numOwners').enter("18");
            input('myPick').enter("1");
            element('#draft_init button').click();

            expect(element('li:contains("RB 1_2001")').count()).toEqual(1);
        });

        it('should have RB 1 as first RB in 2002', function() {
            input('draftYear').enter("2002");
            input('numOwners').enter("18");
            input('myPick').enter("1");
            element('#draft_init button').click();

            expect(element('li:contains("RB 1")').count()).toEqual(1);
        });
    });

    describe('Player drafting', function() {
        it('should adjust value to reflect decremented player need', function() {
            input('draftYear').enter("2000");
            input('numOwners').enter("6");
            input('myPick').enter("1");
            element('#draft_init button').click();

            expect(element('li:contains("QB 1 - 8")').count()).toEqual(1);
            expect(element('li:contains("QB 2 - 0.9")').count()).toEqual(1);
            element('#qb_1').click();

            expect(element('li:contains("QB 2 - 1")').count()).toEqual(1);
        });

        it('should move current pick badge to next owner', function() {
            input('draftYear').enter("2002");
            input('numOwners').enter("18");
            input('myPick').enter("1");
            element('#draft_init button').click();

            expect(element('#owner0 span.label-success').text()).toBe("My pick");
            expect(element('#owner0 span.label-important').text()).toBe("Current pick");

            // First pick, get QB 1
            element('#qb_1').click();

            // We should still have "My Pick", but not "Current Pick".
            expect(element('#owner0 span.label-success').css('display')).not().toBe("none");
            expect(element('#owner0 span.label-important').css('display')).toBe("none");

            // Owner 2 should have "Current Pick"
            expect(element('#owner1 span.label-important').css('display')).not().toBe("none");
        });

        it('should add drafted player in current owner list when clicking draft', function() {
            input('draftYear').enter("2002");
            input('numOwners').enter("5");
            input('myPick').enter("1");
            element('#draft_init button').click();

            // First pick, get QB 1
            element('#qb_1').click();

            // Make sure QB 1 is in the firsts owner's list, and no one else's.
            expect(element('#qb_1').count()).toEqual(0);
            expect(element('#owner0 ol li').text()).toBe("QB 1");
            expect(element('#owner1 ol li').count()).toBe(0);
            expect(element('#owner2 ol li').count()).toBe(0);
            expect(element('#owner3 ol li').count()).toBe(0);
            expect(element('#owner4 ol li').count()).toBe(0);
        });

        it('should move Current Pick badge forward, backward, and forward in snake draft', function() {
            input('draftYear').enter("2002");
            input('numOwners').enter("3");
            input('myPick').enter("1");
            element('#draft_init button').click();

            expect(element('#owner0 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // First pick
            element('#qb_1').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // Second pick
            element('#qb_2').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).not().toBe("none");

            // Third pick
            element('#rb_3').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).not().toBe("none");

            // Fourth pick
            element('#rb_4').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // Fifth pick
            element('#wr_5').click();
            expect(element('#owner0 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // Sixth pick
            element('#wr_6').click();
            expect(element('#owner0 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // Seventh pick
            element('#te_7').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // Eighth pick
            element('#te_8').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).not().toBe("none");

            // Ninth pick
            element('#d_9').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).not().toBe("none");

            // Tenth pick
            element('#d_10').click();
            expect(element('#owner0 span.label-important').css('display')).toBe("none");
            expect(element('#owner1 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // Eleventh pick
            element('#k_11').click();
            expect(element('#owner0 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");

            // Twelfth pick
            element('#k_12').click();
            expect(element('#owner0 span.label-important').css('display')).not().toBe("none");
            expect(element('#owner1 span.label-important').css('display')).toBe("none");
            expect(element('#owner2 span.label-important').css('display')).toBe("none");
        });
    });
});
