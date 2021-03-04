$.jgrid.defaults.styleUI = 'Bootstrap';
$.jgrid.regional['tw'].formatter.integer.thousandsSeparator = ',';
$.jgrid.regional['tw'].formatter.number.thousandsSeparator = ',';
$.jgrid.regional['tw'].formatter.currency.thousandsSeparator = ',';

$.fn.tvGrid = function(config) {
	var gridObj = this;
	gridObj.config = (config != undefined ? config:{}) ;
	gridObj.params = {};
	gridObj.cols = new Array();
	gridObj.config.pager = 'jqGridPager';
	gridObj.config.rows = '10';
	gridObj.config.showMultiselect = 'N';
	gridObj.config.showAll = 'N';
	gridObj.config.showExportXls = 'Y';
	gridObj.config.showExportPdf = 'Y';
	gridObj.resizeGrid = function() {
		if (gridObj) {
			var newWidth = document.getElementsByClassName("card-body")[0].clientWidth - 65;
			gridObj.jqGrid("setGridWidth", newWidth, true);
			if (window.frameElement) {
				var iframeWidth = window.frameElement.offsetWidth;
				gridObj.jqGrid("setGridWidth", iframeWidth - 115, true);
			}
		}
	};
	gridObj.getRowData = function(rid) {		
    	return gridObj.jqGrid('getRowData', rid);
	};
	gridObj.getRowInfo = function(rid) {
		var row = gridObj.jqGrid('getRowData', rid);
		row.rowid = rid;
		for (var item in row) {
		    var v = row[item];
		    var obj = $($.parseHTML(v));
		    if (obj) {
		    	var type = $(obj).attr('type');			    	
		    	if (type == 'text') {
		    		row[item] = $('input[id="' + obj.prop('id') + '"]').val();
		    	}
		    	else if (type == 'checkbox') {
		    		row[item] = $('input:checkbox:checked[name="' + obj.prop('name') + '"]').map(function() { return $(this).val(); }).get(); 
		    	}
		    	else if (type == 'radio') {
		    		row[item] = $('input[name="' + obj.prop('name') + '"]:checked').val();   
		    	}
		    	else if (obj.is('select')) {
		    		row[item] = $('#' + obj.prop('id') + ' option:selected').val(); 
		    	}
		    	else if (obj.is('button')) {
		    		delete row[item];
		    	}
		    }
		}
    	return row;
	};
	gridObj.exportFile = function(type) {
		gridObj.config.rows = 100000;
		var columns = new Array();
    	var cm = gridObj.jqGrid('getGridParam', 'colModel');
    	for (var i = 0 ; i < cm.length ; i ++ ) {
    		if ((cm[i].hidden == undefined || cm[i].hidden == false) && (cm[i].name)) {
    			var column = {} ;
    			column.title = cm[i].label;
    			column.width = cm[i].width;
    			column.dataIndx = cm[i].name;
    			columns[columns.length] = column ;
    		}
    	}
    	var outputParams = {};
    	outputParams.controller = gridObj.controller;
    	outputParams.postData = gridObj.params;
    	outputParams.columns = columns;
    	outputParams.type = type ;
    	try {
        	var form = $(document.createElement('form'));
        	$(form).attr("action", getContextPath() + "/grid/export/");
        	$(form).attr("method", "POST");        	
        	var input = $("<input>").attr("type", "hidden").attr("name", "data").val(JSON.stringify(outputParams));
        	var inputCsrf = $("<input>").attr("type", "hidden").attr("name", $('#csrf').attr('name')).val($('#csrf').val());
        	$(form).append($(input));
        	$(form).append($(inputCsrf));
        	$("body").append(form);
        	$(form).submit();	        	
    	}
    	catch(e) {
    		$.fn.alert(e);
    	}
	};
	return {
		addColumn : function(config) {
			gridObj.cols[gridObj.cols.length] = config ;
		},
		setController : function(controller) {
			gridObj.controller = controller;
		},
		putParams : function(key, value) {
			gridObj.params[key] = value;
		},
		setPager: function(value) {
			if (value) {
				gridObj.config.pager = value;
			}
		},
		setRows : function(rows) {
			if (rows > 0) {
				gridObj.config.rows = rows;
			}			
		},
		getRowData: function(rid) {
			return gridObj.getRowData(rid);
		},
		getSelect: function() {
			var rowArray = [];
			var rowKey = gridObj.getGridParam("selrow");
			if (!rowKey) {
				//$.fn.alert('請選擇資料列');
				return null;
			}
			var selectedIDs = gridObj.getGridParam("selarrrow");
			var result = "";
            for (var i = 0; i < selectedIDs.length; i++) {
            	var rid = selectedIDs[i];
            	if (!$('#' + rid + ' > td > input.cbox').is(':disabled')) {		    		
		    		var row = gridObj.getRowInfo(rid);
		    		if (row) {
		    			rowArray[rowArray.length] = row;
		    		}	            	
		    	}
            }
			return rowArray;
		},
		getGridSelect: function(isAlert) {
			var rowArray = [];
			var trArray = $('#' + gridId + ' .ui-row-ltr');
			if (trArray.length > 0) {
				for (var i=0 ; i<trArray.length ; i++) {
					var tr = trArray[i];
					var row = gridObj.getRowInfo(tr.id);
					rowArray[rowArray.length] = row;
				}
			}
			else {
				if (isAlert && isAlert == true) {
					$.fn.alert('查無資料');
				}							
			}
			return rowArray;
		},
		showMultiselect: function(value) {
			gridObj.config.showMultiselect = value;
		},
		showAll: function(value) {
			gridObj.config.showAll = value;
		},
		showExportXls: function(value) {
			gridObj.config.showExportXls = value;
		},
		showExportPdf: function(value) {
			gridObj.config.showExportPdf = value;
		},
		resize: function() {
			gridObj.resizeGrid();
		},
		genText: function(cellvalue, options, rowObject) {
			var value = ((cellvalue) ? cellvalue : '');
			var classes = '';
			if (options.colModel.fmtType == 'money') {				
				if (value != '') {
					value = gridObj.toMoney(value);
					classes = options.colModel.fmtType;
				}				
			}
			var v = '<input type="text" id="' + options.rowId + '_' + options.colModel.name + '" name="' + options.rowId + '_' + options.colModel.name + '" role="textbox" class="editable inline-edit-cell form-control" style="width: 96%;" value="' + value + '">';
			return v;
		},
		getSelectNonCb: function() {
			var rowArray = [];
			var rowKey = gridObj.getGridParam("selrow");
			var row = gridObj.getRowInfo(rowKey);
			return row;
		},
		getSelectNonCb: function(rid) {
			var row = gridObj.getRowInfo(rid);
			return row;
		},
		init: function() {
			if (gridObj.config.showMultiselect == 'Y') {	
				gridObj.cols[gridObj.cols.length] = { label: 'seq', width:50, exportcol:false, formatter:gridObj.getSeq, key: true, hidden: true };
			}
			
			gridObj.jqGrid('setGridParam',{ postData: gridObj.params });
			gridObj.jqGrid({
				datatype: "local",
				colModel: gridObj.cols,
				viewrecords: true,				
				height: 350,
				rowNum: gridObj.config.rows,
				multiselect: (gridObj.config.showMultiselect == 'Y' ? true : false),
				scroll: (gridObj.config.showAll == 'Y' ? 1 : ''),
				shrinkToFit:false,
				autowidth:true,
				pager: "#" + gridObj.config.pager
			});
			
			gridObj.navGrid('#' + gridObj.config.pager, { 
				edit: false, 
				add: false, 
				del: false, 
				search: false, 
				refresh: true, 
				view: true,
				position: "left", 
				cloneToTop: false 
			});
			
			if (gridObj.config.showExportXls == 'Y') {
				gridObj.navButtonAdd("#" + gridObj.config.pager, {
	                buttonicon: "ui-icon-pencil",
	                title: "匯出EXCEL",
	                caption: "匯出EXCEL",
	                position: "last",
	                onClickButton: exportXLS
	            });
			}
			
			if (gridObj.config.showExportPdf == 'Y') {
				gridObj.navButtonAdd("#" + gridObj.config.pager, {
	                buttonicon: "ui-icon-pencil",
	                title: "匯出PDF",
	                caption: "匯出PDF",
	                position: "last",
	                onClickButton: exportPDF
	            });
			}
			
			$(window).on("resize", gridObj.resizeGrid);
			gridObj.resizeGrid();	
			
			function exportXLS() {
				gridObj.exportFile('XLS');
			}

			function exportPDF() {
				gridObj.exportFile('PDF');
			}
		},
		load: function(callback) {		
			var token = $("meta[name='_csrf']").attr("content");  
			var header = $("meta[name='_csrf_header']").attr("content");
			gridObj.setGridParam({
				datatype: 'json', 
				url: gridObj.controller,
				loadBeforeSend: function( xhr, settings ) {
					if (header && token ){  
		                xhr.setRequestHeader(header, token);  
		            }
				},
				loadComplete: function(data) {
					$(window).on("resize", gridObj.resizeGrid);
					if(callback){
						callback(data);
					}
			    },
			    loadError: function(xhr, status, error) {
			    	if (xhr.status == 403) {
			    		$.fn.alert("連線逾時，請重新登入", function() {
			    			window.location.href = getContextPath() + '/login';
						});
				    }
			    	else {
			    		$.fn.alert(xhr.responseText);
			    	}
			    },
				mtype: "POST", 
				loadonce: false,
				page: 1,
				postData: gridObj.params
			}).trigger('reloadGrid');
						
		}
	};
}
