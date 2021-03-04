var resultInfo = {};
var mcInfo = {};
var lcInfo = {};
var sInfo = {};
var imageInfo = {};
var checkFlag = "1";
var checkFlagMessage = "";

$(function () {
	TvCryptoPlugin.List(this.value);
    $('#type').on("change", function() { 
		 	$.when(TvCryptoPlugin.List(this.value)).done(function(ListResult) {
                    console.log(ListResult);
                }).fail(function(checkRAResult) {
                    kendo.ui.progress($("body"), false);
                    $("hr").after('<div class="alert alert-error"><p><strong>&#10008;</strong><s:text name="download.latest.websocket"/></p></div>');
                    alert("請下載最新版元件");
            });
	});
    
	$('#partyASignature').on("click", function() { 
		if(!checkData()){
			return false;
		}
		
		if($("#dataForm").valid()) { 
			$('#contractId').val(dataid);
			checkConnection("A");
		} 
	}); 
	 
	$('#partyBSignature').on("click", function() { 
		if(!checkData()){
			return false;
		}
		
		if($("#dataForm").valid()) { 
			$('#contractId').val(dataid);
			if($('#fileUpload1').val() !== ""){
				checkConnection("B");
			}
		} 
	}); 
    
	$('#fileUpload_brn1').on("click", function() { 
		$('#fileUpload1').click();
	}); 
	
	
	$('#fileUpload1').on("change", function() { 
		var filename = $('#fileUpload1').val();
		var filenames=filename.split("\\");
		filename=filenames[filenames.length-1];
		$('#fileUpload_lb1').text(filename);
	}); 
	
	$('#fileUpload_brn2').on("click", function() { 
		$('#fileUpload2').click();
	}); 
	
	
	$('#fileUpload2').on("change", function() { 
		var filename = $('#fileUpload2').val();
		var filenames=filename.split("\\");
		filename=filenames[filenames.length-1];
		$('#fileUpload_lb2').text(filename);
	}); 
	
});

function checkConnection(type){
	var obj;
	if(type === "B"){
		if(resultdata.供應商統編 === "22662550"){
			obj = {
					"PostData": "OK"
			};
		}else{
			obj = {
				"PostData": "OK",
				"hc": "Hicos"
			};
		}
	}else{
		obj = {
				"PostData": "OK"
		};
	}
	$.ajax({
		url: "https://localhost:32767/api/CryptoService/ConnnectionServiceTest",
		data: obj,
		type: "GET",
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		async: false,
		error: function(xhr, textStatus, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            alert("請先下載元件");
		},
		success: function(data, textStatus, xhr) {
			runSign();
		},
		complete: function (XMLHttpRequest, textStatus) {
//          objElement.handleElementDisplay("UnLock");
      }
	});
}

function runSign() {
	checkFlag = true;
	$.when(TvCryptoPlugin.CertInfo()).done(function(CertResult) {
	    console.log("CertResult == ", CertResult);
		if(checkFlag === "2"){
			alert("工商憑證統編與該合約供應商統編不符");
			return false;
		}else if(checkFlag === "0"){
			alert("軟體憑證統編與該合約供應商統編不符");
			return false;
		}else if(checkFlag === "3"){
			alert(checkFlagMessage);
			return false;
		}
	    $.when(TvCryptoPlugin.Sign()).done(function(res1) {
			// resultInfo
	    	getSignJson(resultInfo);
	    }).fail(function(res1) {
	        alert("請下載最新版元件");
	    })
	
	}).fail(function(checkRAResult) {
	    alert("請下載最新版元件");
	});
}

function isSignature(signature){ 
	$('#partyASignature').hide(); 
	$('#partyBSignature').hide(); 
					 
	if(signature == 1){ 
		$('#partyASignature').show(); 
	} 
	if(signature == 2){ 

		$('#partyBSignature').show(); 
	} 
} 
	
var TvCryptoPlugin = TvCryptoPlugin || function() {

    var timeout;
    var host = "localhost:9991"; //有憑證 一定要用 localhost
    var timeoutSec = 60 * 1000;

    return {

        tvCryptoModal: "", //密碼視窗
        tvCryptoTPModal: "", //交易夥伴視窗
        tvCertType: "0",
        tvResult: {}, //confirm click 後的結果要回應的物件

        checkTimeout: function() {
            funCheckTimeout();
        },

        Sign: function() {
            return funWebSocket("signb64", {});
        },

        List: function(type) {
            return funWebSocket("list", {});
        },

        Echo: function() {
            funWebSocket("echo", {});
        },

        Time: function() {
            funWebSocket("time", {});
        },

        CertInfo: function() {
            return funWebSocket("cert", {});
        },

        Version: function() {
            funWebSocket("version", {});
        },

        ListTpCert: function() {
            funWebSocket("listTpCert", {});
        }
    }
} ();                 //*z*  /.  var TvCryptoPlugin = TvCryptoPlugin || function()

//==========================================================

	function funWebSocket(func, params) {
        var dfd = $.Deferred();

        if (!params) params = {};
        funPrepareParam(func, params);

        if ("WebSocket" in window) {
            var protocolPrefix = (window.location.protocol === 'https:') ? 'wss:' : 'wss:';
            protocolPrefix = protocolPrefix + '//' + "localhost:9991" + "/ws/";

            $("#errorMsg").val("");
            var ws = new WebSocket(protocolPrefix + func); //websocket

            ws.onopen = function() {
                ws.send(JSON.stringify(params));
            };
            
            ws.onerror = function() {
            	$("#errorMsg").val("請先下載元件");
            };

            ws.onmessage = function(evt) {
                var obj = JSON.parse(evt.data);
				//供應商 簽章 驗證統編
				if(obj.data.func === 'cert'){
					if(IsSignature === "2"){
						if(obj.messages){
							checkFlagMessage = obj.messages;
							checkFlag = "3";
							return false;
						}else{
							if(params.type === "2" && obj.data.certInfo.companyID !== resultdata.供應商統編){
								checkFlag = params.type;
								return false;
							}else if(params.type === "0" && obj.data.certInfo.subjectCN.indexOf(resultdata.供應商統編) === -1){
								checkFlag = params.type;
								return false;
							}
						}
					}else if(IsSignature === "1"){
						if(obj.messages){
							checkFlagMessage = obj.messages;
							checkFlag = "3";
							return false;
						}
					}
				}
                funPostProcess(evt, obj, func);
                ws.close();
            };

            ws.onclose = function() {
                dfd.resolve();
            };
        } else {
            handleNotSupportWs();
        }

        return dfd.promise();
	}  //*z*  /. funWebSocket


//==========================================================

    function funPrepareParam(func, params) {
        if (!params) params = {};
        var str = $('#solt').val();
		if(str === "")
			str = 0;
        params["slot"] = str;

        str = $('#type').val();
        params["type"] = str;

        if (func.indexOf("XML") > -1) {

        } else {
            params["data"] = "AA拉拉拉AAAAAA";
        }

        str = $('#pin').val();
        params["pin"] = str;

        params["algo"] = "default";

        params["authCert"] = "AA拉拉拉AAAAAA";

    }  //*z*  /. funPrepareParam

//==========================================================
    function funPostProcess(evt, obj, func) {
        if (obj.data.deviceList) {
			var j; 
			$("#solt").empty(); 
			$("#solt").append($("<option>").prop({"value":""}).append("請選擇")); 
			for(var i=0 ;i < obj.data.deviceList.length ;i++) { 
			if(obj.data.deviceList[i].readerName) { 
					var tmpValue=obj.data.deviceList[i].readerName; 
					var new_option = new Option(tmpValue,i); 
					$("#solt").append($("<option>").prop({"value":i}).append(tmpValue)); 
				} else{ 
					var tmpValue=obj.data.deviceList[i].filename; 
					var new_option = new Option(tmpValue,tmpValue); 
					$("#solt").append($("<option>").prop({"value":tmpValue}).append(tmpValue)); 
				} 
			} 
        } else if (obj.data.certList) {
			Console.log(JSON.stringify(obj.data.certList))
			var j; 
			for(j=optionSlot.options.length-1;j>=0;j--) { 
			  optionSlot.remove(j); 
			} 
			
			for(var i=0 ;i < obj.data.certList.length ;i++)    { 
			    var tmpValue=obj.data.certList[i].serialNo; 
			    var tmpKey=obj.data.certList[i].subject + ", " + obj.data.certList[i].notBefore + ", " + obj.data.certList[i].notAfter; 
			    var new_option = new Option(tmpKey,tmpValue); 
			    optionSlot.options.add(new_option); 
			} 
        } 
//		else if(obj.data) { 
//	    } 

		var resultData = { result: obj, signResult: {data : evt}, pin: $('#pin').val(), CAFolder: $('#solt').val(), CAType: $('#type').val()};
		if(func === "list"){
			funReObject(resultData, "", "");
		}else if(func === "cert"){
			funReObject("",resultData, "");
		}else{
			funReObject("", "",resultData);
		}
    }  //*z*  /. funPostProcess



function funReObject(ListData, CertData, SignData) {
	if (!funIsEmpty(ListData)) {
//    	var objList = JSON.parse(ListData);
        lcInfo.deviceList = ListData.result.data.deviceList;
        lcInfo.func = ListData.result.data.func;
        lcInfo.success = ListData.result.success;
        lcInfo.transaction = ListData.result.transaction;
        lcInfo.signResult = ListData.signResult;
        lcInfo.pin = ListData.pin;
	}
	//------------------------------
	if (!funIsEmpty(CertData)) {
//    	var objCert = JSON.parse(CertData);
        mcInfo.certInfo = CertData.result.data.certInfo;
//        mcInfo.certPem = CertData.result.data.certPem;
//        mcInfo.endcer = CertData.result.data.endcer;
//        mcInfo.endintermediateDn = CertData.result.data.endintermediateDn;
//        mcInfo.func = CertData.result.data.func;
//        mcInfo.intermediateDn = CertData.result.data.intermediateDn;
//        mcInfo.intermediatecer = CertData.result.data.intermediatecer;
//        mcInfo.rootDn = CertData.result.data.rootDn;
//        mcInfo.rootcer = CertData.result.data.rootcer;
//        mcInfo.success = CertData.result.success;
//        mcInfo.transaction = CertData.result.transaction;
//        mcInfo.signResult = CertData.signResult;
        mcInfo.pin = CertData.pin;
	}
	//------------------------------
	if (!funIsEmpty(SignData)) {
//	    var objSign = JSON.parse(SignData);
	    sInfo.func = SignData.result.data.func;
	    sInfo.signaturek = SignData.result.data.signaturek;
	    sInfo.signer = SignData.result.data.signer;
	    sInfo.success = SignData.result.success;
	    sInfo.transaction = SignData.result.transaction;
	    sInfo.signResult = SignData.signResult;
	    sInfo.pin = SignData.pin;
	    sInfo.CAType = SignData.CAType
	    sInfo.CAFolder = SignData.CAFolder
	}
	resultInfo = { lcInfo: lcInfo, mcInfo: mcInfo, sInfo: sInfo, byteFile: "", UploadRequestUrl:"", DeviceSignPdfPath :"", fn:"", imageInfo:[]};
	return resultInfo;
}

function funIsEmpty(property) {
	return (property === null || property === "" || typeof property === "undefined");
}  //*z*  /. funIsEmpty

function getSignJson(resultInfo) {
    var ajax = new $.tvAjax(); 
    $.blockUI({
        message: '<h3>讀取中...</h3>',
        baseZ: 9999
    });
    if(IsSignature === '2' ) 
    	ajax.setController(getAPIURL("/supplier/contract/getSignFilebyte")); 
    else
    	ajax.setController(getAPIURL("/contract/list/getSignFilebyte")); 
	ajax.setFormId("dataForm");		
	ajax.put("data", JSON.stringify(resultInfo));
	ajax.put("IsSignature", IsSignature);
	ajax.put("dataid", dataid);
	ajax.setBlockUIDisabled(true);
	ajax.upload(function(response) { 
		console.log("result == ", JSON.stringify(response));
		if(response.status === 'ng') {
			$.unblockUI(); 
			$.fn.alert(response.messages == '' ? '請聯絡系統管理員' : response.messages);
		} else {
			 var manageCert = response.result.signJson;//JSON.parse(data);
			 $.ajax({
	                url: "https://localhost:32767/api/CryptoService/X509Certificate2_PDFMultiSign",
	                data: JSON.stringify(manageCert),
	                type: 'POST',
	                contentType: "application/json; charset=utf-8",
	                dataType: "json",
	                async: false,
	                error: function (xhr, textStatus, errorThrown) {
	                	$.unblockUI(); 
	                    console.log(`Status:${status} ${errorThrown}`);
	                    FixSysMsg.danger("Ajax發生錯誤!");
	                },
	                success: function (data, textStatus, xhr) {
//	                    console.log("success: " + JSON.stringify(data));
	                    console.log("textStatus: " + textStatus);
	                    var jsonResponse = JSON.stringify(data);
	                    saveSignFile(jsonResponse);
	                    //*z*  Client端下載
	
	                },
	                complete: function (XMLHttpRequest, textStatus) {
//	                    objElement.handleElementDisplay("UnLock");
	                }
	            });       //*z*  /. ajax
		}
	}); 
  } 

function saveSignFile(signFileData){
	console.log(signFileData);

	var sendObj = {
        	"data": signFileData,
            "dataid": dataid,
            "signName": (IsSignature === '2' ? "供應商" : "家福"),
    };
    sendObj = JSON.stringify(sendObj);
    $.ajax({
        url: (IsSignature === '2' ? getAPIURL("/supplier/contract/saveSignFile"):getAPIURL("/contract/list/saveSignFile")),
        type: "POST",
        contentType: "application/json",
        data: sendObj,
        success: function (response) {
        	console.log("response == ", response);
    		if(response.status === 'ng') {
    			$.unblockUI(); 
    			$.fn.alert(response.messages == '' ? '請聯絡系統管理員' : response.messages);
    		} else {
    			submit('核准');
    		}
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
}

function checkData() {
	if($("type").val() === ""){
		$.fn.alert("請選擇憑證種類");
		return false;
	}
	if($('#solt').val() === ""){
		$.fn.alert("請選擇憑證"); 
		return false;
	}
	if($('#pin').val() === ""){
		$.fn.alert("請輸入憑證密碼"); 
		return false;
	}
	if($('#fileUpload1').val() === ""){
		$.fn.alert("請選取公司大章"); 
		return false;
	}
	if($('#fileUpload2').val() === ""){
		$.fn.alert("請選取公司小章"); 
		return false;
	}
	
	return true;
}

function checkData() {
	 if($("type").val() === ""){
	  $.fn.alert("請選擇憑證種類");
	  return false;
	 }
	 if($('#solt').val() === ""){
	  $.fn.alert("請選擇憑證"); 
	  return false;
	 }
	 if($('#pin').val() === ""){
	  $.fn.alert("請輸入憑證密碼"); 
	  return false;
	 }
	 if($('#fileUpload1').val() === ""){
	  $.fn.alert("請選取公司大章"); 
	  return false;
	 }
	 if($('#fileUpload2').val() === ""){
	  $.fn.alert("請選取公司小章"); 
	  return false;
	 }
	 
	 return true;
}

