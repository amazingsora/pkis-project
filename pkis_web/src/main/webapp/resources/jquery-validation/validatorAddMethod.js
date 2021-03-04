//檢查Email
$.validator.addMethod("validateEmail",function(value){
	if (value != "") {
		var expression = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
		if (expression.test(value)) {
			return true;
		} else {
			return false;
		};
	} else {return true;}
},"請輸入有效的電子郵件，且前後請勿含有空格");