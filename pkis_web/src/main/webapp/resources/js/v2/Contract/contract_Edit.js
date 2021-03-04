$(function () {
	contractor = "";
    Spinner.show();
    dataType = $('#datatype').val();
    modeltype = $('#modeltype').val();
    btntype = $('#btntype').val();
    dataid = $('#dataid').val();
    indexName = $('#indexname').val();
    IsSignature = $('#IsSignature').val();
    currentUserCname = $('#currentUserCname').val();
    idenId = $('#idenId').val();
    modal = $('#modal').val();
    modal_url = $('#modal_url').val();
    isSuper = $('#isSuper').val();
    templateDataId = $('#templateDataId').val();
    isPerConTemplate = $('#isPerConTemplate').val();
    isConTemplate = $('#isConTemplate').val();
	isDowonload = "";
	docver = $("#docver").val();
	taskName = $("#taskName").val();
    getData("#divbody", renderDom);
    currentUserid=$("#currentUserid").val();
    //比對承辦代理人
    contractorAgentUserId=$('#contractorAgentUserId').val();
    //階層判斷
    dispord = $("#dispord").val();
    $(".page-container-top").show();
    $(".page-container-bottom").addClass("page-space");
    //顯示TAB NAV
    $("#cqs-tabnav-holder_Master, #cqs-tabnav-holder_Detail").show();

    //reload頁面時，初始化暫存物件
    if (TMP_CONST_KEY_UPLOADFILE !== "") delete selectOptionsObj[TMP_CONST_KEY_UPLOADFILE];

    $(window).bind("beforeunload", function (e) {
        //離開當前畫面前，清除localStorage
        ClearLocalStorageForContract(e.type);
    });
});

//取回表單明細
getData = function (dom, callback) {
    var obj = {
    		"data": {
        	"dataid": dataid,
            "dataType": dataType,
            "indexName": indexName,
            "templateDataId": templateDataId,
            "docver": docver
        }
    };
    obj = JSON.stringify(obj);
    $.ajax({
        url: getAPIURL(modal_url + "/detail"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                var result = JSON.parse(response.jsonData.data);

                if (LocalStorage_Key_EntireJson !== "" && LocalStorage_Key_EntireJson !== result.data[0].doccode) {
                    //清除localStorage已毋須使用的item
                    SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_EntireJson, "isClearAll": "false" });
                    MainProcess();
                }
                LocalStorage_Key_EntireJson = result.data[0].doccode;
                SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(result) });

                if (MainProcess()) {
                    SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_EntireJson });

                    result = JSON.parse(MainProcess());
                    mapperData = result;
                    json = result;
                    test=result
                    console.log(JSON.stringify(result));

                    //[MANTIS:0025964] 當"案號"不同時，需清除localStorage
                    SetMainProcessPara("modifyLocalStoragePara", {
                        "key": "",
                        "value": { "comparedKey": LocalStorage_Key_Dataid, "updVal": result.data[0].dataid, "toBeClearedKeys": ["初診斷日期"] }
                    });
                    MainProcess();

                    callback(dom, json, renderScript);
                }
            } else {
                $.fn.alert(response.message, function(){
					if(response.rtnCode == -1){
						location.href = getAPIURL(modal_url+'/');
					}
				});
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        },
        complete: function (xhr, status) {
            Spinner.hide();
        }
    });
};


//處理jsonlert

renderDom = function (dom, result, callback2) {
    var checkData = false;
    $.each(result.data, function (i, renderType) {
        //非制式客製化-EN 中英,中判斷計畫書 
    	if (renderType.datatype.indexOf(dataType)>-1) {
            checkData = true;
            Object.values(result.data[i].docdetail).forEach(function (val) {
                //取得json內的resultdata(用於填入基本資料)
                if (val.js && val.js.indexOf("SetSourceData") > -1) {
                    resultdata = GetSourceData(result.data[0].docdetail, val.js.match(/'.+'/g)[0].replace(/'/g, ""));
                    //移轉合約 判斷是否有移轉	
                    contractor = resultdata.承辦人員;
                	if(resultdata.承接人 != ''){
                		contractor = resultdata.承接人
                	}
                    return false;
                }
            });

            dataIdx = i;
            SetMainProcessPara("GenDiv", { "data": result.data[i].docdetail, "body": dom });
            MainProcess();

            //顯示側邊選單區
            $("[class^=page-container-middle]").show();
            //將未使用的Slide頁籤隱藏
            Hide_PageSlide($slide);

            if ($("#divbody > div > *").hasClass("cqs-fls-width") ||
                $("#divbody > div > *").hasClass("cqs-fls-width-fifty"))
                $("#divbody > div").addClass("cqs-doublefield-holder");

            //調整v2 checkbox、radio與label的樣式
            $("div.clr-style").each(function (i, v) {
                //checkbox & radio
                if ($(v).children().find("checkbox, radio") !== undefined)
                    $(v).addClass("dis-flex");

                //label
                if ($(v).children("label.col-md-3").attr("id") !== undefined &&
                    $(v).children("label.col-md-3").attr("id").split("_").length > 4) {
                    $(v).children("label.col-md-3").css({ "margin-top": "7px" });
                }
            });

            if (callback2) callback2();

            // 建構ResultList table
            var $jqTb = $("#json_query");
            if ($jqTb.length > 0) {
                NewResultListColumnDefs($jqTb, resultListItems);
                //非制式客製化-EN 中英,中判斷計畫書
				if(dataType.indexOf("附件資料") > -1 || dataType.indexOf("審核意見")>-1){
					selectOptionsObj.baseResultListFuncStr = selectOptionsObj.baseResultListFuncStr = `setToBackResult('', '${dataType}', 'ResultList')`;
				}
                if (selectOptionsObj.baseResultListFuncStr !== "")
                    eval(selectOptionsObj.baseResultListFuncStr); // eg. setResultList()

                $jqTb.parents("div.dis-flex").css({ "display": "initial", "white-space": "initial" });
            }
        }
    });
   
    //非制式客製化-EN 
    var Approved = ""
    var Rejected = ""
	var widthVal = "";
    if(resultdata) {
    	if(resultdata.合約模式 === "制式合約"){
    		widthVal = "120px";
    		isDowonload = "否";
    		Approved='核准';
    		Rejected='駁回';
    	}
    	else{
    		widthVal = "320px";
    		isDowonload = "NO否";
    		Approved='Approved核准';
    		Rejected='Rejected駁回';

    	}
    }
		
	$(`legend:contains(基本資料)`).parent().find('label').each(function(){
	    var classtemp = String($(this).attr('class'));
	    var reg = RegExp(/label-1/);
	    if(classtemp.match(reg)){
	        $(this)[0].classList.remove("col-md-3");
	        $(this).css({ "width": widthVal,"text-align-last" : "justify"});
	        
	    }
	});
	$(`legend:contains(公司資訊)`).parent().find('label').each(function(){
	    var classtemp = String($(this).attr('class'));
	    var reg = RegExp(/label-1/);
	    if(classtemp.match(reg)){
	        $(this)[0].classList.remove("col-md-3");
	        $(this).css({ "width": widthVal,"text-align-last" : "justify"});
	        
	    }
	});
	$(`legend:contains(供應商資訊)`).parent().find('label').each(function(){
	    var classtemp = String($(this).attr('class'));
	    var reg = RegExp(/label-1/);
	    if(classtemp.match(reg)){
	        $(this)[0].classList.remove("col-md-3");
	        $(this).css({ "width": widthVal,"text-align-last" : "justify"});
	        
	    }
	});
	$(`legend:contains(合約屬性資料)`).parent().find('label').each(function(){
	    var classtemp = String($(this).attr('class'));
	    var reg = RegExp(/label-1/);
	    if(classtemp.match(reg)){
	        $(this)[0].classList.remove("col-md-3");
	        $(this).css({ "width": widthVal,"text-align-last" : "justify"});
	        
	    }
	});
	$(`legend:contains(文檔編輯)`).parent().find('label').each(function(){
	    var classtemp = String($(this).attr('class'));
	    var reg = RegExp(/label-1/);
	    if(classtemp.match(reg)){
	        $(this)[0].classList.remove("col-md-3");
	        $(this).css({ "width": widthVal,"text-align-last" : "justify"});
	        
	    }
	});
    if(isPerConTemplate === "Y") {
		$('#cqs-tabnav-holder_Master').find('li').hide();
		$("#cqs-button-holder").find("input").hide();
		$('input[displayname="暫存"]').show();
    } else if(isConTemplate === "Y") {
    	$('#cqs-tabnav-holder_Master').find('li').hide();
		$("#cqs-button-holder").find("input").hide();
		$('input[displayname="預覽"]').show();
		$("#divbody input[type='file']").attr("disabled", true);
    	$("#divbody input[type='radio']").attr('disabled', true);
    	$("#divbody input[type='checkbox']").attr("disabled", true);
    	$("#divbody").find("select").attr('disabled', true);
    	$('#divbody input[type="text"], textarea').attr('readonly', true);
    } else {
        // 登入者若非承辦人員，不可編輯
        $("#divbody input").attr("readonly", false);
    	$("#divbody input[displayname='上傳']").attr("disabled", false);

		//非制式客製化-EN
		$("#divbody input[displayname='Upload上傳']").attr("disabled", false);
    	$("#divbody input[type='file']").attr("disabled", false);
    	$("#divbody input[type='radio']").attr('disabled', false);
    	$("#divbody input[type='checkbox']").attr("disabled", false);
    	$("#divbody").find("select").attr('disabled', false);
    	$('#divbody input[type="text"], textarea').attr('readonly',false);
    	$("#approvalDiv input").attr("readonly", false);
		if(!taskName){
			taskName = "申請人";
		}
		//判斷承辦人(承接人)是否等於或是等於承辦代理人 且關卡為第一關
        if(((contractor !== currentUserCname) &&  contractorAgentUserId !== currentUserid) || (btntype == 0 || taskName != "申請人")){
        	$("#divbody input[displayname='增加']").attr("disabled", true);
        	$("#divbody input").attr("readonly", true);
        	$("#divbody input[displayname='上傳']").attr("disabled", true);
			//非制式客製化-EN
			$("#divbody input[displayname='Upload上傳']").attr("disabled", true);
        	$("#divbody input[type='file']").attr("disabled", true);
        	$("#divbody input[type='radio']").attr('disabled', true);
    		$("#divbody input[type='checkbox']").attr("disabled", true);
        	$("#divbody").find("select").attr('disabled', true);
        	if(btntype != '' && btntype == 0){
        		$("#divbody").find("textarea").attr('readonly', true);
        	}
            //非制式客製化-EN 中英,中判斷計畫書
        	if(dataType.indexOf("附件資料") > -1){
        		$('#divbody input[type="text"], textarea').attr('readonly',true);
        	}
    		if(dataType.indexOf("其他同意條款") > -1){
    			$('#divbody input[type="text"], textarea').attr('readonly',true);
    		}
    		if(dataType.indexOf("明細資料") > -1){
    			$('#divbody input[type="text"], textarea').attr('readonly',true);
    		}
        	$("#approvalDiv input").attr("readonly", true);
        } 
        //判斷承辦人(承接人)是否等於或是等於承辦代理人 且關卡為第一關 taskName草稿階段為null
        else if((contractor === currentUserCname || contractorAgentUserId === currentUserid) && (taskName == "申請人" || !taskName)) {
        	$("#divbody input").attr("readonly", false);
        	$("#divbody input[displayname='上傳']").attr("disabled", false);
			//非制式客製化-EN
			$("#divbody input[displayname='Upload上傳']").attr("disabled", false);
        	$("#divbody input[type='file']").attr("disabled", false);
        	$("#divbody input[type='radio']").attr('disabled', false);
        	$("#divbody").find("select").attr('disabled', false);
        	$('#divbody input[type="text"], textarea').attr('readonly',false);
        	$("#approvalDiv input").attr("readonly", false);
        }
        
        // 登入者若為供應商，則不顯示
		$('#cqs-tabnav-holder_Master').find('li')[1].style.display ='block';
        $("#divbody").find("fieldset").eq(1).show();
        if(modal == 'SC'){
			$('#cqs-tabnav-holder_Master').find('li')[3].style.display ='block';
        }
        if(idenId == "999999999"){
        	// 隱藏明細資料
			$('#cqs-tabnav-holder_Master').find('li')[1].style.display ='none';
        	// 隱藏審核評估
        	if(modal == 'SC'){
				$('#cqs-tabnav-holder_Master').find('li')[3].style.display ='none';
        	}
            //非制式客製化-EN 中英,中判斷計畫書
    		if(dataType.indexOf("審核意見")>-1){
        		$("#divbody").find("fieldset").eq(1).hide();
    		}
        } else {
			$('#cqs-tabnav-holder_Master').find('li')[1].style.display ='block';
        	$("#divbody").find("fieldset").eq(1).show();
        	if(modal == 'SC'){
				$('#cqs-tabnav-holder_Master').find('li')[3].style.display ='block';
            }
			if($('#cqs-tabnav-holder_Master').find('li > a')[3].text === "審核評估")
				$('#cqs-tabnav-holder_Master').find('li > a')[3].style.background = "red";
        }
        
    	$('input[displayname="'+Approved+'"]').hide();
    	$('input[displayname="'+Rejected+'"]').hide();
    	$('input[displayname="送出"]').hide();
    	$('input[displayname="暫存"]').hide();
    	$('input[displayname="作廢"]').hide();
		$('input[displayname="預覽"]').hide();
		
        if(modeltype == "contract"){
        	if(btntype == 1){
//        		$('input[displayname="審核"]').show();
				if(currentUserCname !== "admin")
					$('input[displayname="'+Approved+'"]').show();
            	$('input[displayname="'+Rejected+'"]').show();
        	}else if(btntype == 2 || !btntype){
            	$('input[displayname="作廢"]').show();
				if(currentUserCname !== "admin"){
					$('input[displayname="送出"]').show();
					$('input[displayname="暫存"]').show();
					$('input[displayname="預覽"]').show();
				}
        	}
        }else{
        	if(btntype != 0){
        		//判斷承辦人(承接人)是否等於或是等於承辦代理人 且關卡為第一關
        		if((contractor == currentUserCname||contractorAgentUserId==currentUserid || currentUserCname == 'admin') && (taskName == "申請人" || !taskName)){
            		$('input[displayname="送出"]').show();
                	$('input[displayname="暫存"]').show();
                	$('input[displayname="作廢"]').show();	
    				$('input[displayname="預覽"]').show();
            	}else{
    				$('input[displayname="'+Approved+'"]').show();
                	$('input[displayname="'+Rejected+'"]').show();

            	}
        	}
        }
        
    	if(dataType === "審核評估"){
    		if($(`#${$('label:contains(檢核狀態)').next().attr('id')}`).val() === "")
    			setBindingItemValue($(`label:contains(檢核狀態)`).next().attr('id'), "通過");	
    	}
    }

    if (!checkData) {
        Spinner.hide();
        alert('查無此資料');
        GetQuery(cluster);
    }
};

//驗證且儲存
function CheckAndSave(clickType) {
	clickType=clickType.replace(/[a-zA-Z]/g,"")
	var jsvValidMsg = "";
    Spinner.show();
    var _result = "";
    if(clickType === "審核" || clickType === "核准"){
    	if(IsSignature == 1 || IsSignature == 2){
    		checkSignConnection();
//    		$('#signatureModal').modal('show');
//        	isSignature(IsSignature);
//    		RunTest('list',{});
    		Spinner.hide();
    	}else{
    		submit(clickType);
    	}
    }
	else if(clickType === "駁回"){
		var approval_Comment="";
	    //非制式客製化-EN 中英,中判斷計畫書
		if(modal == "SC")
			approval_Comment="審核意見";
		else 
			approval_Comment="Approval Comment審核意見";
		
		_result = getJsonMappingElement("orgString", approval_Comment,'審核意見', true).value;
			
		if(_result === "")
			jsvValidMsg = "審查意見尚未填寫，無法駁回。\nRejection can't be excute if approval comment not yet completed.";
		else
			submit(clickType);
	}
	else{
    	submit(clickType);
    }
	if (jsvValidMsg !== "") {
            alert(jsvValidMsg);
            Spinner.hide();
            return false;
	}
}

linkURL = function (clickType, confirmFlag, cancerCode){
	var jsvValidMsg = "";
	//當為個人範本，跳頁不需檢核
	if(isPerConTemplate !== "Y" && isConTemplate !== "Y")
		jsvValidMsg = ValidateRequiredItems();
	if (jsvValidMsg !== "") {
		FixSysMsg.danger(`${jsvValidMsg}`);
	    Spinner.hide();
	    return false;
	}
	
	saveJSONToLocalStorage();
	changeTabToRenderHTML(clickType);
    //非制式客製化-EN 中英,中判斷計畫書
	if(clickType.indexOf('審核意見') > -1){
		getFlowstepList();
	} else if (clickType.indexOf("附件資料") > -1) {
		if(taskName != "申請人"){
			$("input[Type='button'][Value='刪除']").remove();
			$("input[Type='button'][Value='Delete刪除']").remove();
		}else{
			//非制式客製化-EN
			setBindingItemValue( $(`label:contains(供應商可否下載)`).attr('id'), isDowonload);
		}
	} else if(clickType.indexOf("審核評估") > -1){
		getApproval();
	}
	else if(clickType.indexOf("缺貨罰款") > -1 && isPerConTemplate !== "Y"){
		getShortagepenalty();
		//產生TABLE
	}
	else if(clickType.indexOf("商品訂貨與交付") > -1){
		$(`#${getJsonMappingElement("displayname", " E-Order and E-Invoice 電子訂單、電子發票", "2.Order_and_Delivery商品訂貨與交付", false).id}`).prop('disabled', true);
	}
}

//確定儲存,call api
submit = function (clickType) {
    saveJSONToLocalStorage();
    sendData(clickType);
};


//call api to save
sendData = function (clickType) {
	clickType = clickType.replace(/[a-zA-Z]/g,"")
	if(clickType === "送出"){
		if(!checkNegoentry()){
			return;
		}
	}
    var sendObj = {
        	"contractNo": dataid,
            "dataType": dataType,
            "indexName": indexName,
        	"type": clickType,
            "data": json,
            "templateDataId": templateDataId,
            "dispord":dispord
    };
    sendObj = JSON.stringify(sendObj);
    $.ajax({
        url: getAPIURL(modal_url+"/updata"),
        type: "POST",
        contentType: "application/json",
        data: sendObj,
        success: function (response) {
            var result = JSON.stringify(response);

            if (response.rtnCode === 0) {
				if(clickType === "駁回" || clickType === "作廢")
					FixSysMsg.success(`${response.message}`);
				else
					FixSysMsg.success(`${response.message}`);

            	//導頁處理
            	if(response.model === "pending"){
            		if(isSuper === "true"){
            			location.href = getAPIURL('/pending/supervisor/');
            		} else {
            			location.href = getAPIURL('/pending/list/');
            		}
            	}
            	
            	if(response.model === "contract"){
            		location.href = getAPIURL('/contract/list/');
            	}
            	
            	if(response.model === "supplierPending"){
            		location.href = getAPIURL('/supplier/pending/');
            	}
            	
            	if(response.model === "supplierContract"){
            		location.href = getAPIURL('/supplier/contract/');
            	}
            	
            	if(response.model === "perConTemplate") {
            		location.href = getAPIURL('/percontractTemplate/list/');
            	}
            	
            } else {
                //顯示錯誤訊息
                console.log(response);
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        },
        complete: function (xhr, status) {
            Spinner.hide();
            $.unblockUI(); 
        }
    });
};


//清除LocalStorage
ClearLocalStorageForContract = function (type) {
    if (type === "beforeunload") {
        SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_Dataid, "isClearAll": "false" });
        MainProcess();
        SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_EntireJson, "isClearAll": "false" });
        MainProcess();
        SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_RestoreMark, "isClearAll": "false" });
        MainProcess();
    }

    SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_RtnJson, "isClearAll": "false" });
    MainProcess();
    SetMainProcessPara("clearLocalStorage", { "key": "#SOURCE", "isClearAll": "false" });
    MainProcess();
};

//預覽
function Preview(clickType){
	Spinner.show();
	getvalue();
	var sendObj = {
		"contractNo": dataid,
	    "dataType": dataType,
	    "indexName": indexName,
	    "type": clickType,
	    "data": json
	};
    sendObj = JSON.stringify(sendObj);
	$.ajax({
		url: getAPIURL(modal_url+"/preDownload"),
		type: "POST",
		contentType: "application/json",
		data: sendObj,
		success: function (response) {
			var message = response.message;
			if(message) {
				$.fn.alert(message);
			} else {
				if(modal_url === "/contractTemplate/manage") {
					window.location.href = getAPIURL(modal_url + "/downloadpreview?fileName=" + response);
				} else {
					window.location.href = getAPIURL(modal_url + "/downloadpreview?dataid=" + dataid);
				}
			}
			
		}, 
		error: function (xhr, status, errorThrown) {
			 console.log(`Status:${status} ${errorThrown}`);
			 FixSysMsg.danger("Ajax發生錯誤!");
		},
		complete: function (xhr, status) {
			Spinner.hide();
		}	 
	});	 
}

function getFlowstepList(){
	var sendObj = {
        	"contractNo": dataid
    };
    sendObj = JSON.stringify(sendObj);
	
    $.ajax({
    	url: getAPIURL(modal_url + "/getFlowstep"),
        type: "POST",
        contentType: "application/json",
        data: sendObj,
        success: function (response) {
            var result = JSON.stringify(response);
            var content = '';
            if(result!= null){
            	var flowstepData = response.result.dataList;
            	content = "<br/><div class='label-1 clr-style dis-flex' id='flowstepDiv'><table border='1' width='300px' height='500px'><tr><td>";
            	if(idenId !=999999999){
            	for(var i = 0 ; i < flowstepData.length ; i ++){
            		//content += '<BR>';
					if(i === 0)
						content = content + flowstepData[i].rolename + "</td></tr>"; 
					else
						content = content + '<tr><td>--> ' + flowstepData[i].rolename + "</td></tr>";
            		}
            	}
                $(`legend:contains(審核意見)`).parent().append(content);
            	$("#flowstepDiv").show();
                if(idenId == "999999999"){
                	$("#flowstepDiv").hide();
                } else {
                	$("#flowstepDiv").show();
                }
            	content = content + "</table></div>";
            }

        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        },
        complete: function (xhr, status) {
            Spinner.hide();
        }
    });
    
}

function getApproval(){
	SetMainProcessPara("getLocalStorage", {"key": "#SOURCE"});
	var localStorageData = JSON.parse(MainProcess());
	
	var sendObj = {
		"section": localStorageData.課別,
		"suppliercode": localStorageData.供應商廠編,
		"usercname":contractor,
		"taskName":taskName,
		"contractYear": localStorageData.合約年度,
		"contractorAgentUserId":contractorAgentUserId
		
		};
		sendObj = JSON.stringify(sendObj);
		
		$.ajax({
		url: getAPIURL(modal_url + "/getApproval"),
		type: "POST",
		contentType: "application/json",
		data: sendObj,
		success: function (response) {
		    var result = JSON.stringify(response);
		    if(response.result.getNegoentryHtml != null){
		    	var negoentryData = response.result.getNegoentryHtml;
		        $(`legend:contains(檢核)`).parent().prepend(negoentryData);
		        
		    	$("#approvalDiv").show();
		    	$("#approvalDiv input").attr("readonly", false);
		    	//浮點數去除
		    	  $('#EXTRA_0').text(parseFloat($('#EXTRA_0').text()));
		    		$('#EXTRA_1').text(parseFloat($('#EXTRA_1').text()));
		    		var EXTRA_0=$('#EXTRA_0').text();
		    		var EXTRA_1=$('#EXTRA_1').text();
		    		//轉換千分位格式
					$('#EXTRA_0').text(convertPerMill(EXTRA_0));
					$('#EXTRA_1').text(convertPerMill(EXTRA_1));
		    	if((contractor == currentUserCname && btntype != 0) || (contractorAgentUserId == currentUserid && btntype != 0)){
		    		$("#approvalDiv input").attr("readonly", false);
		    	} else {
		    		$("#approvalDiv input").attr("readonly", true);
		    	}
		        if(idenId == "999999999"){
		        	$("#approvalDiv").hide();
		        } else {
		        	$("#approvalDiv").show();
					$('#EXTRAActual_2').attr("readonly", true);
		        }
		    }
		    getApprovalDataValue();
		},
		error: function (xhr, status, errorThrown) {
		    console.log(`Status:${status} ${errorThrown}`);
		    FixSysMsg.danger("Ajax發生錯誤!");
		},
		complete: function (xhr, status) {
		    Spinner.hide();
		}
	});
}

function setApprovalData(id){
	var _result = "", extrapc3 = 0, avgMarginActual3=0, resultDataTemp = "", resultDataCheck = "";
	var value = $("#" + id).val();
	var cname = $("#" + id).data("cname");
	var resultdata = getJsonMappingElement("resultdata", "合約編碼", "基本資料", false);

	var num1 = 0;
	var msg = "";

	var reg = `^[0-9]{1,3}(.[0-9]{1,2})?$`; 
	if(cname !== "Extra白單"){
		if(cname !== 'Flow'){
			if(value.toString().split('.')[0].length > 3){
				msg = `【${cname}】格式需為小數，小數點最多2位, 整數最多3位!`;
				$("#" + id).val('');
				resultdata[cname] = '';
			}
			if(!new RegExp(reg).test(value)){
				msg = `【${cname}】格式需為小數，小數點最多2位, 整數最多3位!`;
				$("#" + id).val('');
				resultdata[cname] = '';
			}
			if(isNaN(value) === true){ 
				msg = `【${cname}】格式需為小數，小數點最多2位, 整數最多3位!`;
				$("#" + id).val('');
				resultdata[cname] = '';
			}
		}
	}else{
		reg = `^(0|[1-9][0-9]{0,9})$`;
		//千分位轉換純數字
		value = convertnum(value);
		//檢核
		if(!new RegExp(reg).test(value)){
			msg = `【${cname}】格式為 整數最多10位!`;
			$("#" + id).val('');
			resultdata[cname] = '';
		}
		else  {
			$("#" + id).val(convertPerMill(value));
		}
	}
	
	
	if(msg !== ""){
		if($('#AVG_Margin_Actual_2').val() !== "")
	 		avgMarginActual3 = $('#AVG_Margin_Actual_2').val();
		else 
			avgMarginActual3 = 0;
		if($('#EXTRAPC_2').val() !== "")
		 	extrapc3 = $('#EXTRAPC_2').val();
		
		else 
			extrapc3 = 0;
			
		var extra1Actual3 = 0;
		extra1Actual3 = (parseFloat(avgMarginActual3) + parseFloat(extrapc3)).toFixed(2);
	
	 	$('#EXTRAActual_2').val(parseFloat(extra1Actual3));

		resultdata['AVG_Margin_Actual_with_白單'] = extra1Actual3;
		Spinner.hide();
		FixSysMsg.danger(msg);
		return false;
	}
	
	resultdata[cname] = value;
	
	if($('#AVG_Margin_Actual_2').val() !== ""){
	 	avgMarginActual3 = $('#AVG_Margin_Actual_2').val();
	}
	else 
		avgMarginActual3 = 0;
	if($('#EXTRAPC_2').val() !== "")
	 	extrapc3 = $('#EXTRAPC_2').val();

	else 
		extrapc3 = 0;

	var extra1Actual3 = 0;
	extra1Actual3 = (parseFloat(avgMarginActual3) + parseFloat(extrapc3)).toFixed(2);
	 $('#EXTRAActual_2').val(parseFloat(extra1Actual3));

	resultdata['AVG_Margin_Actual_with_白單'] = extra1Actual3;

	var extra1 = $('#EXTRA_0').text();
	var extra2 = $('#EXTRA_1').text();
	var extra3 = $('#EXTRA_2').val();

	var extra3list = extra3.split(",");
	if(extra3list.length > 1){
		var inputcal = "";
		for(var i in extra3list) {
			inputcal += extra3list[i];
		}
		extra3 = parseInt(inputcal);
	}
	var extra1Actual1 = $('#EXTRAActual_0').text();
	var extra1Actual2 = $('#EXTRAActual_1').text();
	var avgMargin = $('#AVG_Margin_Budget_2').val();
	var aMB1=$('#AVG_Margin_Budget_1').text();
	var aMBresult=avgMargin- $('#AVG_Margin_Budget_1').text()
	var extraactualresult=$('#EXTRAActual_2').val()-$('#EXTRAActual_1').text();
	setBindingItemValue($(`label:contains(檢核規則1)`).next().attr('id'), _result);
	setBindingItemValue($(`label:contains(檢核規則2)`).next().attr('id'), _result);
	setBindingItemValue($(`label:contains(檢核規則3)`).next().attr('id'), _result);
	setBindingItemValue($(`label:contains(檢核規則4)`).next().attr('id'), _result);
	setBindingItemValue($(`label:contains(檢核規則5)`).next().attr('id'), _result);
	setBindingItemValue($(`label:contains(檢核規則6)`).next().attr('id'), _result);
	setBindingItemValue($(`label:contains(檢核規則7)`).next().attr('id'), _result);
	setBindingItemValue($(`label:contains(檢核規則8)`).next().attr('id'), _result);
	
	if(parseFloat(extra1Actual3) > parseFloat(extra1Actual2) &&  parseFloat(extra1Actual3) >= parseFloat(avgMargin)){
		_result = `有調整(${(parseFloat(extra1Actual3) - parseFloat(extra1Actual2)).toFixed(2)})%,且有達 Margin`;
		resultDataCheck = resultDataCheck + `有調整,且有達 Margin/`;
	}

	if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則1)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";
	}
	
	if(parseFloat(extra1Actual3) > parseFloat(extra1Actual2) &&  parseFloat(extra1Actual3) < parseFloat(avgMargin)){
		_result = `有調整(${(parseFloat(extra1Actual3) - parseFloat(extra1Actual2)).toFixed(2)})%,但未達 Margin`;
		resultDataCheck = resultDataCheck + `有調整,但未達 Margin/`;
	}

	if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則2)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";
	}

	if(parseFloat(extra1Actual3) === parseFloat(extra1Actual2) &&  parseFloat(extra1Actual2) >= parseFloat(avgMargin)){
        _result = `未調整但有達Margin`;
	 	resultDataCheck = resultDataCheck + _result + "/";
	}

	if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則3)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";
	}

	if(parseFloat(extra1Actual3) === parseFloat(extra1Actual2) &&  parseFloat(extra1Actual3) < parseFloat(avgMargin)){
        _result = `未調整且未達 Margin`;
		resultDataCheck = resultDataCheck + _result + "/";
	}

    if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則4)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";
	}

	if(parseFloat(extra1Actual3) === parseFloat(extra1Actual2) && parseFloat(extra1Actual3) === parseFloat(extra1Actual1) &&  parseFloat(extra1Actual3) >= parseFloat(avgMargin)){
        _result = `連續 3年未調整但有達Margin`;
		resultDataCheck = resultDataCheck + _result + "/";
	}
	
	if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則5)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";
	}

	if(parseFloat(extra1Actual3) === parseFloat(extra1Actual2) && parseFloat(extra1Actual3) === parseFloat(extra1Actual1) &&  parseFloat(extra1Actual3) < parseFloat(avgMargin)){
        _result = `連續 3年未調整且未達Margin`;
		resultDataCheck = resultDataCheck + _result + "/"; 
	}
	
	if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則6)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";
	}
	
	//規則7,8
	if(parseFloat(aMBresult) > parseFloat(extraactualresult)){
	    _result = `成本調漲大於Margin調漲`;
		resultDataCheck = resultDataCheck + _result + "/"; 
	}
	
	if(_result !== ""){
	 	setBindingItemValue($(`label:contains(檢核規則7)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";		 
	}
	
	if(parseFloat(extraactualresult)<0){
	    _result = `Margin不升反降`;
		resultDataCheck = resultDataCheck + _result + "/"; 
	}
	
	if(_result !== ""){
	 	setBindingItemValue($(`label:contains(檢核規則8)`).next().attr('id'), _result);
		resultDataTemp = resultDataTemp + _result +"/";
		_result = "";
 	}
	
	if(resultDataTemp !== ""){
		resultDataTemp = resultDataTemp.substring(0, resultDataTemp.length -1);
		resultDataCheck = resultDataCheck.substring(0, resultDataCheck.length -1);
	}
	resultdata["檢核結果-文字"] = resultDataCheck;
	resultdata["檢核結果"] = resultDataTemp;
}

function getApprovalDataValue(){
	var resultdata = getJsonMappingElement("resultdata", "合約編碼", "基本資料", false);
	$("#Flow").val(resultdata.Flow);
	$("#approvalDiv input").each(function(){
		var id = $(this).attr('id');
		var cname = $("#" + id).data("cname");
		$("#" + id).val(resultdata[cname]);
	});
	$("#approvalDiv label").each(function(){
		var id = $(this).attr('id');
		if(id !== undefined){
			if(id.substring(id.length-2,id.length) === '_2'){
				var cname = $("#" + id).data("cname");
				$("#" + id).html(resultdata[cname]);
			}
		}
	});
}

//小卡檢核 跨page
function checkNegoentry(){
	resultdata = getJsonMappingElement("resultdata", "合約編碼", "基本資料", false);
	if(resultdata.合約模式 === "非制式合約")
		return true;
	var jsvValidMsg = "";
	var negoentryArray = [
	  "Flow",
	  "HYP_Cost",
	  "SUP_Cost",
	  "KM_Cost",
	  "AVG_Cost",
	  "HYP_Margin",
	  "SUP_Margin",
	  "KM_Margin",
	  "AVG_Margin_Budget",
	  "HYP_Margin_Actual",
	  "SUP_Margin_Actual",
	  "KM_Margin_Actual",
	  "AVG_Margin_Actual",
	  "Extra白單",
	  "Extra白單%",
	  "AVG_Margin_Actual_with_白單"
	];

	var shortagepenaltyMsg = checkShortagepenaltyValue();
	jsvValidMsg = jsvValidMsg + checkValidValue(json);
	if(shortagepenaltyMsg !== ""){
		jsvValidMsg = jsvValidMsg + shortagepenaltyMsg + "\n";
	}
	negoentryArray.forEach(function(item){
  		if(resultdata[item] === undefined || resultdata[item] === ""){
			jsvValidMsg = jsvValidMsg + "【審核評估】-->檢核-->" + item + "欄位資料為必填\n";
		}
	});
	
	if(jsvValidMsg !== ''){
		FixSysMsg.danger(`${jsvValidMsg}`);
		Spinner.hide();
		return false;
	} else {
		return true;
	}
	
}

function checkShortagepenaltyValue(){
	var jsvValidMsg = "";
	if(resultdata !== null){
		var shortagepenaltyArray = Object.keys(resultdata).filter(item => item.indexOf('Shortagepenalty') > -1).sort();
		var shortagepenaltySize = shortagepenaltyArray.length;

		for(i=0; i < shortagepenaltyArray.length; i++){
			var tempArray = resultdata[shortagepenaltyArray[i]].split('|');
			if(tempArray.length === 3){
				$.each(tempArray, function (i, item) {
					if(item === "" || item === undefined){
						jsvValidMsg = "【3.SHORTAGE PENALTY 缺貨罰款】 表格資料必填";
						return jsvValidMsg;
					}
				});
			}else if(tempArray.length === 1){
				if(resultdata[shortagepenaltyArray[i]] === "" || resultdata[shortagepenaltyArray[i]] === undefined){
					jsvValidMsg = "【3.SHORTAGE PENALTY 缺貨罰款】 表格資料必填";
					return jsvValidMsg;
				}
			}
		}
		return jsvValidMsg;
	}
}

//檢核
function checkValidValue(json){
	var jsvValidMsg = "";
	var path = $.JSONPath({ data: json, keepHistory: false, onFound: function (path, value) { dataIdx = parseInt(path.split(';')[1]); } });
	var dataTypeArray = path.query(`$..datatype`);
	
	$.each(dataTypeArray, function (i, item) {
		var checkValidArray = path.query(`$.[?(/${item}/.test(@.datatype))].docdetail[?(@.remark=='*' && (@.uitype=='date' || @.uitype=='text'))].data_id`);
		$.each(checkValidArray, function (i, itemDetail) {
			var tempValue = path.query(`$.[?(/${item}/.test(@.datatype))].docdetail[?(@.id=='${itemDetail}')].value`)
			if(tempValue.toString() === "" && jsvValidMsg.indexOf(item) === -1){
				jsvValidMsg = jsvValidMsg + `【${item}】 必填欄位未填寫\n`;
				return jsvValidMsg;
			}
			
		});
    });
	
	return jsvValidMsg;
}

var shortagepenaltySize;
//  3.2缺貨罰款
function getShortagepenalty(){
	resultdata = getJsonMappingElement("resultdata", "合約編碼", "基本資料", false);
	if(resultdata === undefined){
		resultdata = getJsonMappingElement("resultdata", "Shortagepenalty_0", "基本資料", false);
	}
	var deptno = isConTemplate === "Y" ? "" : resultdata.課別
	var content = '', caseRateResult;
	var shortagepenaltyArray = Object.keys(resultdata).filter(item => item.indexOf('Shortagepenalty') > -1).sort();
	var shortagepenaltySize = shortagepenaltyArray.length;
	content = "<div id='shortagepenaltyDiv' >" +
			  "<table>" +
			  "<tr><td colspan=2><label>Department 課別： "+deptno+"</td></tr>"+
			  "<tr><td><label>當月到貨率%<br>current month arrival rate %</label></td><td><label>懲罰性違約金%<br>shortage penalty%</label></td></tr>";
	for(var i = 0 ; i < shortagepenaltySize ; i ++){
		caseRateResult = resultdata[`Shortagepenalty_${i}`].split('|');
		if(caseRateResult.length === 3){
			//判斷承辦人(承接人)是否等於或是等於承辦代理人 且關卡為第一關 taskName為草稿階段為null
			if((contractor === currentUserCname || contractorAgentUserId === currentUserid) && btntype != 0 && (taskName == "申請人" || !taskName)){ 
				if(isConTemplate === "Y"){
					if(i === 0)
					content = content +'<tr><td> ≧  '+ caseRateResult[0] +"%～ " + caseRateResult[1] + "%</td><td>"+caseRateResult[2]+"%</td></tr>";
				else
					content = content +'<tr><td> ≧  '+ caseRateResult[0] +"%～ < " + caseRateResult[1] + "%</td><td>"+caseRateResult[2]+"%</td></tr>";
				}else{
					if(i === 0){
						content = content +'<tr><td> ≧  <input type="text" size="6" class="ShortagepenaltyTable" onchange="setShortagepenalty(this)"  value="'+caseRateResult[0]+'"  step="0.01" min="0" max="100" style="text-align: right">'
								+"%～ " + '<input type="text" size="6" class="ShortagepenaltyTable"  onchange="setShortagepenalty(this)" value="'+caseRateResult[1]+'"  step="0.01" min="0" max="100" style="text-align: right">%</td><td >'
								+'<input type="text" size="6" class="ShortagepenaltyTable" onchange="setShortagepenalty(this)" value="'+caseRateResult[2]+'"  step="0.01" min="0" max="100" style="text-align: right">'+"%</td></tr>";
					}else{
						content = content +'<tr><td> ≧  <input type="text" size="6" class="ShortagepenaltyTable"  onchange="setShortagepenalty(this)"  value="'+caseRateResult[0]+'"  step="0.01" min="0" max="100" style="text-align: right">'
								+"%～ < " + '<input type="text" size="6" class="ShortagepenaltyTable"  onchange="setShortagepenalty(this)" value="'+caseRateResult[1]+'"  step="0.01" min="0" max="100" style="text-align: right">%</td><td >'
								+'<input type="text" size="6" class="ShortagepenaltyTable" onchange="setShortagepenalty(this)" value="'+caseRateResult[2]+'"  step="0.01" min="0" max="100" style="text-align: right">'+"%</td></tr>";
					}		
				}		
			}
			else{
				if(i === 0)
					content = content +'<tr><td> ≧  '+ caseRateResult[0] +"%～ " + caseRateResult[1] + "%</td><td>"+caseRateResult[2]+"%</td></tr>";
				else
					content = content +'<tr><td> ≧  '+ caseRateResult[0] +"%～ < " + caseRateResult[1] + "%</td><td>"+caseRateResult[2]+"%</td></tr>";
			}
		}else{
			caseRateResult = resultdata[`Shortagepenalty_${i}`].split('|');
			if(isConTemplate === "Y"){
				content = content + '<tr><td colspan=2><label>雙方確認供應商前一年到貨率為	' + caseRateResult[0] + ' %<br>Both parties confirmed	' + caseRateResult[0] + ' % as Supplier’s arrival rate in last year</label></td></tr></table>'
			}else{
				//判斷承辦人(承接人)是否等於或是等於承辦代理人 且關卡為第一關
				if((contractor === currentUserCname || contractorAgentUserId === currentUserid) && btntype != 0 && (taskName == "申請人" || !taskName))
				content = content +'<tr><td colspan=2><label>雙方確認供應商前一年到貨率為	'+'<input type="text" size="6" class="ShortagepenaltyTablelast" onchange="setlastper(value);setShortagepenalty(this)"  value="' + caseRateResult[0] + '" style="text-align: right"/> %<br>Both parties confirmed	' + '<input type="text" size="6" input class="ShortagepenaltyTablelast" onchange="setlastper(value);setShortagepenalty(this)" value="' + caseRateResult[0] + '" style="text-align: right"/> % as Supplier’s arrival rate in last year</label></td></tr></table>'
			else
				content = content +'<tr><td colspan=2><label>雙方確認供應商前一年到貨率為	' + caseRateResult[0] + ' %<br>Both parties confirmed	'+caseRateResult[0] + ' % as Supplier’s arrival rate in last year</label></td></tr></table>'
			}
		}		
	}
	
	if(content !== "")
		$(`#${getJsonMappingElement("_ref", "缺貨罰款", "3.SHORTAGE_PENALTY缺貨罰款", false).id}`).parent().append(content);
}

function setlastper(value){
	//連動表格最後欄位
	$(".ShortagepenaltyTablelast").each(function(){
		this.value = value;
	});
}

function setShortagepenalty(obj){
	
	var value = obj.value;
	var msg = "";
	var result = '';
	var i = 0;
	var reg = `^[0-9]{1,3}(.[0-9]{1,2})?$`; 
	if(value != 'X'){
		if(value.toString().split('.')[0].length > 3){
			msg = `格式需為小數，小數點最多2位, 整數最多3位!`;
		}
		if(!new RegExp(reg).test(value)){
			msg = `格式需為小數，小數點最多2位, 整數最多3位!`;

		}
		if(isNaN(value) === true){ 
			msg = `格式需為小數，小數點最多2位, 整數最多3位!`;
		}
	}
	
	if(msg !== ""){
		FixSysMsg.danger(msg);
		obj.value = "";
//		return false;
	}
	//觸發onchange時,將所有值做成做字串
	$(".ShortagepenaltyTable").each(function(){
		var value=$(this).val()
		if($(this).val() == ''){
			value = "";
		}
		else if(!$(this).val()){
			console.log("無法存取")
			return "";
		}
		//三個數字為一組
		if(i != 2){
	    	result = result+ value+'|';
			i++;
		}
		else{
	    	result = result+value+',';
	    	i = 0;
		}
	});
	//最後表格獨立一個數字
	result += $(".ShortagepenaltyTablelast").val();
	updateShortagepenalty(result);
}
//更新缺貨罰則
function updateShortagepenalty(result){
	var resultdata = getJsonMappingElement("resultdata", "合約編碼", "基本資料", false);
	result=result.split(',');
	var shortagepenaltySize=result.length;
    for(var i = 0 ; i < shortagepenaltySize ; i ++){
    	//當內容不一致時 才替換
    	if(resultdata[`Shortagepenalty_${i}`] != result[i]){	
    		resultdata[`Shortagepenalty_${i}`] = result[i]
    	}
    }
    saveJSONToLocalStorage();
}

function checkSignConnection() {
	var errorMsg = $("#errorMsg").val();
	if(errorMsg !== '') {
		$.fn.alert(errorMsg, function() {
			getLiContent();
		});
		return false;
	}
	checkCrypto();
	
	return true;
}

function checkCrypto() {
	var obj = {
			"PostData": "OK"
	};
	
	$.ajax({
		url: "https://localhost:32767/api/CryptoService/ConnnectionServiceTest",
		data: obj,
		type: "GET",
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		async: false,
		error: function(xhr, textStatus, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            $.fn.alert("請先下載元件", function() {
    			getLiContent();
    		});
		},
		success: function(data, textStatus, xhr) {
			$('#signatureModal').modal('show');
    		isSignature(IsSignature);
//			RunTest('list',{});
		},
		complete: function (XMLHttpRequest, textStatus) {
//          objElement.handleElementDisplay("UnLock");
      }
	});
}

