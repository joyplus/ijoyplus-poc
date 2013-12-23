(function( $ ) {
$.floatScroll = {
    defaults: {
    	direction: "top",
        distance: 0,
        css: 'fixed', 
    }
}
$.fn.floatScroll = function(map){
    var opts = $.extend({},
        $.floatScroll.defaults,
        map || {}
    );
    return this.each(function() {
    	var $this = $(this),
    		$window = $(window),
	    	windowHeight = $window.height(),
	    	objTop = $this.offset().top,
	    	objHeight = $this.height();
    	$window.scroll(function() {
    		scollTop = $(document).scrollTop();
    		console.log("scollTop:"+scollTop);
    		console.log("objTop  :"+objTop);
    		console.log("distance:"+opts.distance);
    		console.log("wheight :"+windowHeight);
    		if(opts.direction=="top"){
            	var scoll = opts.distance + scollTop - objTop > 0;
            	console.log("top:"+scoll);
        		if(scoll) {
        			$this.addClass(opts.css);
        		}else {
        			$this.removeClass(opts.css);
        			objTop = $this.offset().top;
        		}
    		}else{
	        	var scoll = windowHeight + scollTop - objHeight;
	    		if(scoll < objTop) {
	    			$this.addClass(opts.css);
	    		}else {
	    			$this.removeClass(opts.css);
	    			windowHeight = $window.height();
	    			objTop = $this.offset().top;
	    		}
    		}
    	});
    });
};
})( jQuery );