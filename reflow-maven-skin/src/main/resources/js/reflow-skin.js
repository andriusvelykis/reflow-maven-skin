// Additional skin Javascript
// ++++++++++++++++++++++++++++++++++++++++++

!function ($) {

	$(function(){

		var $window = $(window)
	
		// Start carousel
		$(function() {
			$('.carousel').carousel();
		});
		
		// Support for smooth scrolling and back button while scrolling
		// Note: only run if smoothScroll is enabled
		if (typeof($.smoothScroll) == typeof(Function)) {
			$('a[href^="#"]:not([href^="#carousel"])').live('click', function() {
				var slashedHash = '#/' + this.hash.slice(1);
				if ( this.hash ) {

					if ( slashedHash === location.hash ) {
						$.smoothScroll({scrollTarget: this.hash});
					} else {
						$.bbq.pushState(slashedHash);
					}

					return false;
				}
			});

			$(window).bind('hashchange', function(event) {
				var tgt = location.hash.replace(/^#\/?/,'');
				if ( document.getElementById(tgt) ) {
					$.smoothScroll({scrollTarget: '#' + tgt});
				}
			});

			$(window).trigger('hashchange');
		}
		
		// activate syntax higlighting with highlight.js
		// Note: only run if `hljs` exists
		if (typeof hljs != 'undefined')
		{
			// classic encoding with <div class="source"><pre></pre></div>
			// and HTML5 version with <pre><code></code></pre>
			$('div.source pre, pre code').each(function(i, e) {hljs.highlightBlock(e)});
		}

	})

}(window.jQuery)
