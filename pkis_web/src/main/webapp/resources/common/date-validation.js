$.fn.dateStrCompare = function(dateBgn, dateEnd, msg) {
	if (dateBgn && dateEnd) {		
		if (Date.parse(dateBgn).valueOf() > Date.parse(dateEnd).valueOf()) {
			if (msg) {
				$.fn.alert(msg);
			}
			return false;
		}
		else {
			return true;
		}
	}
	else {
		return true;
	}
}