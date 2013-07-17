if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});

        function createScoringSystem() {
            $(':input[name^="stat_multiplier"]').each(function(index) {
                console.log($(this).val());
            });
        }

        $('#ss_submit').click(function() {
            createScoringSystem();
        });
	})(jQuery);
}
