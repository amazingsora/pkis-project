var token = $("meta[name='_csrf']").attr("content");  
var header = $("meta[name='_csrf_header']").attr("content");  
$.ajaxSetup({  
    beforeSend: function (xhr) {  
        if(header && token ){  
            xhr.setRequestHeader(header, token);  
        }  
    }}  
); 

$.fn.alert = function(msg, callback) {
	if(parent.messagebox) {
		parent.messagebox(msg, callback);
	} else {
		$.alert(msg, function() {
			if(callback) callback.call(this);
		});
	}
}

$.fn.confirm = function(msg, callbackTrue, callbackFalse) {
	$.confirm(msg, function(bool) {
		if (bool) {
			if(callbackTrue) callbackTrue.call(this);
		} else {
			if(callbackFalse) callbackFalse.call(this);
		}		
	});
}

$.tvAjax = function(config) {
	var selectedObjects = this;	
	selectedObjects.config = (config != undefined ? config : {}) ;
	if (selectedObjects.config.data == undefined) selectedObjects.config.data = {};	
	return {
		setController : function(controller) {
			selectedObjects.config.url = controller;
		},	
		put : function(key, obj) {
			if (typeof obj == 'object') {
				selectedObjects.config["data"][key == '' || key == undefined ? "paramsData" : key] = JSON.stringify(obj) ;
			} 
			else {
				selectedObjects.config["data"][key == '' || key == undefined ? "paramsData" : key] = obj ;
			}
		},
		setFormId : function(formId) {
			selectedObjects.config.form = formId;
		},
		setLimit : function(limit) {
			selectedObjects.config.limit = limit;
		},	
		setBlockUIDisabled : function(val) {
			selectedObjects.config.blockUIDisabled = val;
		},
		call : function(callback) {
			if (selectedObjects.config.type == undefined) selectedObjects.config.type = "post";
			if (selectedObjects.config.dataType == undefined) selectedObjects.config.dataType = "json";
			if (selectedObjects.config.blockUIDisabled == undefined) {
				$.blockUI({
	                message: '<h3>讀取中...</h3>',
	                baseZ: 9999
	            });
			}
			$.ajax(selectedObjects.config).done(function(data, textStatus, request) {
				if (callback) {
					callback.call(this, data);
				}
				if (selectedObjects.config.blockUIDisabled == undefined) {
					$.unblockUI();
				}
			}).fail(function(xhr, textStatus, errorThrown) {					
				$.unblockUI();
				if (xhr.status == 403) {
					$.fn.alert("連線逾時，請重新登入", function() {
		    			window.location.href = getContextPath() + '/login';
					});
			    }
				else {
					$.fn.alert(xhr.responseText);
				}					
			});	
		},
		upload : function(callback) {
			if (selectedObjects.config.blockUIDisabled == undefined) {
				$.blockUI({
	                message: '<h3>讀取中...</h3>',
	                baseZ: 9999
	            });
			}
			var $dataForm = $("#" + selectedObjects.config.form);									
			$dataForm.attr("enctype", "multipart/form-data");
			selectedObjects.config.type = "post";
			selectedObjects.config.dataType = "json";
			selectedObjects.config.success = function(responseText, statusText) {
				if (callback) {
					callback.call(this, responseText);
				}
				if (selectedObjects.config.blockUIDisabled == undefined) {
					$.unblockUI();
				}
			};
			selectedObjects.config.error = function() {
				$.fn.alert('檔案超過上限');
				$.unblockUI();
			},
			$dataForm.ajaxSubmit(selectedObjects.config);					
		}
	}
};

$.fn.serializeObject = function() {
	return $(this).serializeJSON({useIntKeysAsArrayIndex: true});
};

function getContextPath() {
	return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

$(function() {
	
	datePluginInit();
	
	$(".btnClear").click(function() {
		$(this).parents('form')[0].reset();
		
		$(".select2-single").select2({
	        val: ''
	    });
	});		
	
	$(".upperCase").bind("keyup change", function(e) {
        var str = $(this).val();
        $(this).val(str.toUpperCase());
    });
	
	$.fn.select2.defaults.set( "width", "100%" );
    $('.select2-single').on('select2:open', function (e) {
	    var maxHeight = window.innerHeight/2 - 50;
	    $('ul.select2-results__options').css('max-height', maxHeight + 'px');
	});
    $(".select2-single").select2({
        //placeholder: '請選擇',
        allowClear: true,
        val: ''
    });
	
});

