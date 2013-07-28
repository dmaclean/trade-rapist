if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});

        context_root = $('#context_root').text();

        function createScoringSystem() {
            var pairs = { 'ss_name' : $('#ss_name').val(), 'fantasy_team_id' : $('#fantasy_team_id').val() };

            $(':input[name^="stat_multiplier"]').each(function(index) {
                var statKey = $(this).attr('name'); //$(this).attr('name').split("_")[2];
                var multiplier = $(this).val();
                console.log(multiplier + " - " + statKey);

                pairs[statKey] = multiplier;
            });

            var url = "/scoringSystem/createSystemAndRules";
            if(context_root != "/") {
                url = context_root + "/scoringSystem/createSystemAndRules";
            }

            $.ajax({
                type: 'POST',
                url: url,
                data: pairs
            }).done(function() {
                    window.location.href = "..";     // Send user back to home page.
            }).fail(function(e) {
                    alert(e);
            });
        }

        var fantasyTeamForm = $($('form')[0]);
        fantasyTeamForm.submit(function() {
            $.ajax({
                type: fantasyTeamForm.attr('method'),
                url: fantasyTeamForm.attr('action'),
                data: fantasyTeamForm.serialize()
            }).done(function(data) {
                    $('#fantasy_team_id').val(data);
                    $('#scoring').modal();
            }).fail(function() {
                    alert("Something went wrong.");
            });

            event.preventDefault();
        });

        $('#ss_submit').click(function() {
            createScoringSystem();
        });
	})(jQuery);
}
