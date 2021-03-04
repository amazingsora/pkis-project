function datePluginInit() {

	$(".date").each(function() {	    
		var $date = $(this);	
		$date.css('background-color', '#FFFFFF');
		$date.attr( 'readOnly' , 'true' );
		$date.datepicker({
			language: 'zh',
			dateFormat: "yyyy/mm/dd",
			clearButton: true,
			onSelect: function onSelect(fd, date) {
		        if (date) {
		        	$date.datepicker().data('datepicker').hide();
		        }
		    }
		});
	});
	
	$(".dateY").each(function() {	    
		var $date = $(this);	
		$date.css('background-color', '#FFFFFF');
		$date.attr( 'readOnly' , 'true' );
		$date.datepicker({
			language: 'zh',
			dateFormat: "yyyy",
			clearButton: true,
			view : "years",
			minView : "years",
			onSelect: function onSelect(fd, date) {
		        if (date) {
		        	$date.datepicker().data('datepicker').hide();
		        }
		    }
		});
	});
		
	$(".dateTime").each(function() {	    		
		var $date =  $(this);	
		$date.css('background-color', '#FFFFFF');
		$date.attr( 'readOnly' , 'true' );
		$date.datepicker({
			language: 'zh',
			timepicker: true,
			dateFormat: "yyyy/mm/dd",
			timeFormat: "hh:ii",
			clearButton: true,
			onSelect: function onSelect(fd, date) {
		        if (date) {
		        	$date.datepicker().data('datepicker').hide();
		        }
		    }
		});
	});		
	
	$(".dateYM").each(function() {	    				
		var $date = $(this);	
		$date.css('background-color', '#FFFFFF');
		$date.attr( 'readOnly' , 'true' );
		$date.datepicker({
			language: 'zh',
			view: 'months',
			minView: 'months',
			dateFormat: "yyyy/mm",
			clearButton: true,
			onSelect: function onSelect(fd, date) {
		        if (date) {
		        	$date.datepicker().data('datepicker').hide();
		        }
		    }
		});
	});		
	
	$(".dateRange").each(function() {	    						
		var $date = $(this);	
		$date.css('background-color', '#FFFFFF');
		$date.attr( 'readOnly' , 'true' );
		var date = new Date();
		$date.datepicker({
			language: 'zh',
			dateFormat: "yyyy/mm/dd",
			clearButton: true,
			minDate: new Date(date.getUTCFullYear(), date.getUTCMonth(), 1),
			maxDate: new Date(date.getUTCFullYear(), date.getUTCMonth() + 1, 0),
			range: true,
			multipleDatesSeparator: '-',
			onSelect: function onSelect(fd, date) {
		        if (date) {
		        	$date.datepicker().data('datepicker').hide();
		        }
		    }
		});
	});
}