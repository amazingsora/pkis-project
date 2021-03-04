var $fileUpload = $.Deferred();
var $cr = $.Deferred();
var $s = $.Deferred();
//BTN類型連結
function linkURL(datatype, confirmFlag, cancerCode) {
    var tmpDataid = "", tmpDoccode = "";
    //個案相關BTN eg. 基本資料、診斷、治療、復發...
    $.each(json.data, function (idx, obj) {
        if (idx === 0) tmpDataid = obj.dataid;
        if (obj.datatype === datatype) {
            tmpDoccode = obj.doccode;
            return false;
        }
    });
    if (tmpDataid === "" || tmpDataid === undefined || tmpDoccode === "" || tmpDoccode === undefined) {
        //[多專科團隊會議]for 新增成功後，取得dataid,doccode(當流程跳過查詢步驟時使用)
        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_RtnJson });
        var tmpJsonStr = MainProcess();
        if (tmpJsonStr) {
            var tmpJsonObj = JSON.parse(tmpJsonStr);
            if (tmpJsonObj) {
                tmpJsonObj = JSON.parse(tmpJsonObj.data);
                if (tmpJsonObj && tmpJsonObj.hasOwnProperty("_id")) {
                    if (tmpDataid === "" || tmpDataid === undefined) tmpDataid = tmpJsonObj._id;
                }
                if (tmpJsonObj && tmpJsonObj.hasOwnProperty("_doccode")) {
                    if (tmpDoccode === "" || tmpDoccode === undefined) tmpDoccode = tmpJsonObj._doccode;
                }
            }
        }
    }
    console.log(`${tmpDataid},${tmpDoccode}`);

    var execResult;
    if (dataType.match(Reg_Datatype)) {
        //[個管]當dataType符合Reg_Datatype內的字串時，直接啟動儲存按鈕
        execResult = eval($("input:button[value='儲存']").attr("onclick"));
    } else
        execResult = CheckAndSave();
    //console.log(execResult);
    setTimeout(function () {
        if (execResult !== false) {
            //移除[多專科團隊會議]判斷式
            SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_RtnJson });
            var tmpJsonStr = MainProcess();
            if (tmpJsonStr) {
                var tmpJsonObj = JSON.parse(tmpJsonStr);
                if (tmpJsonObj) {
                    tmpJsonObj = JSON.parse(tmpJsonObj.data);
                    if (tmpJsonObj && tmpJsonObj.hasOwnProperty("_id")) {
                        if (tmpDataid === "" || tmpDataid === undefined) tmpDataid = tmpJsonObj._id;
                    }
                    if (tmpJsonObj && tmpJsonObj.hasOwnProperty("_doccode")) {
                        if (tmpDoccode === "" || tmpDoccode === undefined) tmpDoccode = tmpJsonObj._doccode;
                    }
                }
            }
            
            setTimeout(function () { linkTo(tmpDataid, tmpDoccode, datatype); }, 4000);
        }
    }, timeOutTime);
}

/**
 * [查詢] 顯示ResultList資料table的HTML
 * @param {any} para1      {cluster}_{doccode[0]}_{doccode[1]}
 * @param {any} para2      table欄位名稱(使用逗號串接)
 * @param {any} targetTbId jqDataTable ID
 * @param {any} docCode    查詢頁版次碼doccode
 * @param {any} phraseStr  預設空字串
 */
function getResultList(para1, para2, targetTbId, docCode, phraseStr) {
    if (checkReqFieldEitherOr(para2)) {
        Spinner.show();
        getvalue();

        var queryObj = {
            "token": getToken(),
            "userId": getUserId(),
            "accessCode": "AJCC001",
            "data": {
                "module": module,
                "cluster": cluster,
                "class": para1.split("_").length > 1 ? para1.split("_")[1] : para1,
                "spec": para1.split("_").length > 2 ? para1.split("_")[2] : para2,
                "docdetail": json.data[0].docdetail
            }
        };
        queryObj = JSON.stringify(queryObj);
        console.log(queryObj);

        $.ajax({
            url: getAPIURL("COL/Query"),
            type: "POST",
            contentType: "application/json",
            data: queryObj,
            success: function (response) {
                if (response.rtnCode === 0) {
                    console.log(response.jsonData.data);

                    var resultArr = JSON.parse(response.jsonData.data);
                    if (resultArr === null) resultArr = [];

                    var oTable = $(`${(targetTbId.indexOf("#") > -1 ? "" : "#") + targetTbId}`),
                        isExistTgTb = false;

                    if (oTable.length !== 0) {
                        oTable = oTable.dataTable();
                        isExistTgTb = true;
                    }

                    if (resultArr && resultArr.length > 0) {
                        //查詢的欄位列出儲存於localStirage
                        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_BoxItems, "value": JSON.stringify(resultListItems) });
                        MainProcess();
                        //查詢的結果集合儲存於localStirage
                        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_BoxResult, "value": JSON.stringify(resultArr) });
                        MainProcess();

                        if (isExistTgTb) {
                            oTable.fnClearTable();
                            oTable.fnAddData(resultArr);

                            //重新繪製排序
                            var orderList = [];
                            oTable.find("thead > tr > th").each(function (i, item) {
                                if ($(this).is("[aria-label^='病歷號:']")) {
                                    orderList.push([i, "asc"]); return false;
                                }
                            });
                            oTable.find("thead > tr > th").each(function (i, item) {
                                if ($(this).is("[aria-label^='收案類別:']")) {
                                    orderList.push([i, "asc"]); return false;
                                }
                            });
                            oTable.find("thead > tr > th").each(function (i, item) {
                                if ($(this).is("[aria-label^='初診斷日期:']")) {
                                    orderList.push([i, "asc"]); return false;
                                }
                            });
                            //重新繪製排序
                            oTable.DataTable().order(orderList).draw();
                        }

                        FixSysMsg.success(`${ajccType}查詢成功`);
                    } else {
                        FixSysMsg.success("查無資料");
                        if (isExistTgTb) oTable.fnClearTable(); //Talas 20180607 修正查詢前清除顯示資料
                        //清除localStorage的查詢結果
                        SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_BoxItems, "isClearAll": "false" });
                        MainProcess();
                        SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_BoxResult, "isClearAll": "false" });
                        MainProcess();
                    }
                } else
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
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
}

function addRecord(searchType, targetId, refId, clickType) {
    Spinner.show(); var altMsg = "", $body = "#divbody", fileSize = 0, extension = "";
	var resultdata = getJsonMappingElement("resultdata", "合約編碼", "基本資料", false);
    if (clickType !== undefined) {
        var jqFilter = `:contains('${searchType}')`;

        var arrChk = $($body).find(`fieldset legend${jqFilter}`).nextAll().children().filter(function (i, obj) {
            return !checkBindValIsEmpty(getBindingObj(obj));
        });

        if (arrChk.length === 0) { Spinner.hide(); return false; }
    }

	var file_data = $(`#${targetId}`).parent('div').next().children('form').children('input').prop('files')[0];
	if($('label:contains(檔案類型)').next().val() === ""){
		altMsg = altMsg + "請選擇檔案類型\n";
		if(file_data === undefined)
			altMsg = altMsg + "請選擇檔案\n";
	}else{
		if(file_data === undefined){
			altMsg = altMsg + "請選擇檔案\n";
		}else{
			extension = file_data.name;
			extension = extension.split('.')[1].toLowerCase();
			fileSize = file_data.size;
			if(fileSize > (1024 *1024 *10)){ 
				altMsg = "檔案超過限制 10M!";
			}
			if($('label:contains(檔案類型)').next().val() === "合約主檔"){
				if(resultdata.合約模式 === "非制式合約"){
					var fileTypeText = $("#json_query tr").find("td:eq(2)").text();
					if(fileTypeText.indexOf("合約主檔") >= 0){
						altMsg = "請先刪除舊合約主檔，再重新上傳";
					}
				}
				if(extension.indexOf('pdf') === -1){
					altMsg = "檔案格式不正確(合約主檔只允許 PDF格式)";
				}
			}
			
			if("bmp, tif, png, jpg, jpge, zip, doc, docx, xlsx, xls, pdf".indexOf(extension.toLowerCase()) === -1){
				altMsg = "檔案格式不正確!";
			}
		}
	}

    if (altMsg !== "") {
		cleanRecord(searchType);
		//非制式客製化-EN
		setBindingItemValue( $(`label:contains(供應商可否下載)`).attr('id'), isDowonload);
        Spinner.hide();
        FixSysMsg.danger(altMsg);
        return false;
    }
    else {
        if (typeof getvalue === "function") getvalue();
        //取得當前序號(以"_序號_"為key)
        var currPKey = selectOptionsObj[currRecordKey] ? selectOptionsObj[currRecordKey] : "";

        var targetItem = $(`#${targetId}`);
        var currMaxFixId = "", initBlockid = "";
        if (targetItem.length > 0) {
            initBlockid = targetItem.attr("fixblock");
            currMaxFixId = getFixMaxId(initBlockid);
        }

        console.log(`"Cluster": ${cluster},"datatype": ${dataType},"fixid": ${currPKey}`);
        console.log(`initBlockid: ${initBlockid}, currMaxFixId: ${currMaxFixId}`);

        try {
            if (currPKey === "")
                NewFixBlock(initBlockid, currMaxFixId); //新增
            else {
                ReplaceFixBlock(initBlockid, currPKey); //取代
            }
         	if(file_data !== undefined){
         		upload(file_data, clickType, searchType)
         	}

			if (clickType === undefined) {
		        setToBackResult("", dataType, "ResultList"); //查詢ResultList
		
		        console.log('重新繪製HTML');
				reloadPara();
		        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_EntireJson });
		        json = JSON.parse(MainProcess());
		        renderDom($body, json, renderScript);
		    }
			
        } catch (e) {
            FixSysMsg.danger("新增發生錯誤!");
            console.log(e);
        } finally {
            Spinner.hide();
        }
    }
}

function upload(file_data, clickType, searchType){
	var obj = {
	        "fixid": fixBlockid,
	        "dataid": dataid,
	        "fileType": $("#divbody").find("fieldset").eq(0).find("select").val(),
			"isDownload": $(`input[name=${$(`label:contains(供應商可否下載)`).parent().next().find('input')[0].name}]:checked`).val()
	    };
	var formData = new FormData();
	formData.append("data", JSON.stringify(obj));
	formData.append("file", file_data);
	$.ajax({
	      type: "POST",
	      url: getAPIURL(modal_url+`/upload`),
	      data:formData,
	      cache:false, // 不需要cache
	      processData: false, // jQuery預設會把data轉為query String, 所以要停用
	      contentType: false, // jQuery預設contentType為'application/x-www-form-urlencoded; charset=UTF-8', 且不用自己設定為'multipart/form-data'
	      success: function (response){
	    	  cleanRecord(searchType);
	    	  if (clickType === undefined) {
	    		  	CheckAndSave('暫存');
	    		  	// 非制式客製化-EN 上傳
					setBindingItemValue( $(`label:contains(供應商可否下載)`).attr('id'),isDowonload );
			  }
	    	  return true;
	      }      
	    });
}

function reloadResult(currPKey = "") {
	var obj = {
        "func": dataType,
		"idenId" : idenId,
        "data": json
    };
    obj = JSON.stringify(obj);
    console.log(obj);
    console.log("============reloadResult=============");
    $.ajax({
        url: getAPIURL(modal_url+`/fixQuery`),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
	        var passJVal = "";
            if (response.rtnCode === 0) {
                passJVal = response.jsonData.data;
                eval(`selectOptionsObj["${dataType}"]=${passJVal}`);
            }
			else if(response.rtnCode === 99) {
				if(dataType.indexOf("附件資料")>-1){
					selectOptionsObj[`${dataType}`] = [];
					eval(`selectOptionsObj["${dataType}"]`);
				}else{
					
					eval(`selectOptionsObj["${dataType}"]="${passJVal}"`);
				}
			}
			else{
             	FixSysMsg.danger(`${response.rtnCode}-${response.message}`);	
			}
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
}

function deleteResult(searchType, currPKey, callback) {
	if (!confirm('是否刪除')) return;
    if (typeof getvalue === "function") getvalue();
    try {
//        setTimeout(function () {
            Spinner.show();
            if (currPKey !== "") RemoveFixBlock(currPKey); //刪除
            else return;
            // 刪除DB資料及路徑檔案
            deleteFile(dataType, currPKey);

            //清空編輯區
            cleanRecord(searchType);
			//存檔
            CheckAndSave('暫存');
            //查詢ResultList
            callback("", dataType, "ResultList");
			

	        console.log('重新繪製HTML');
	        reloadPara();
	        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_EntireJson });
	        json = JSON.parse(MainProcess());
	        renderDom("#divbody", json, renderScript);
	        // 非制式客製化-EN
			setBindingItemValue( $(`label:contains(供應商可否下載)`).attr('id'), isDowonload);
	        Spinner.hide();
//       
    } catch (e) {
        FixSysMsg.danger("刪除發生錯誤!");
        console.log(e);
        Spinner.hide();
    }
}

function cleanRecord(targetId, searchKey) {
    //畫面 & Json 資料清除
    var cleanBlock = $(`#${targetId}`).parents("fieldset");
    if (cleanBlock.length === 0 && targetId === dataType) cleanBlock = $("#divbody");
    resetBindingItems(cleanBlock.children());

    //清除後，更新Json
    if (typeof getvalue === "function") getvalue();
}

function deleteFile(dataType, currPKey, callback){
//	if (!confirm('是否刪除')) return;
	
    var obj = {
    		"id": currPKey,
    		"dataid": dataid
    };
    
    obj = JSON.stringify(obj);
    console.log(obj);
    
    $.ajax({
    	url: getAPIURL(modal_url+"/deleteFile"),
    	type: "POST",
    	contentType: "application/json",
    	data: obj,
    	success: function(response){
			if(!currPKey.match(/fix/g))
				callback("", dataType, "ResultList");
    		console.log(response);
    	},
    	error: function(xhr, status, errorThrown){
    		console.log(`Status:${status} ${errorThrown}`);
    		FixSysMsg.danger("Ajax發生錯誤!");
    	},
    	complete: function(xhr, status){
    		Spinner.hide();
    	}
    });
	
}

function downloadFile(dataType, currPKey){
	var obj = {
		"id": currPKey,
		"dataid": dataid
	};
	
	obj = JSON.stringify(obj);
	console.log(obj);
	
	$.ajax({
		url: getAPIURL(modal_url+"/preDownloadGetFileSerNo"),
		type: "POST",
		dataType : 'text',
		contentType: "application/json;charset=UTF-8",
		data: obj,
		success: function(response){
			console.log(response);
			window.location.href = getAPIURL(modal_url+"/download?serno=" + response);
//			window.location.href = getAPIURL(modal_url+"/download?fileName=" + response);
		},
		error: function(xhr, status, errorThrown){
			console.log(`Status:${status} ${errorThrown}`);
			FixSysMsg.danger("Ajax發生錯誤!");
		},
		complete: function(xhr, status){
			Spinner.hide();
		}
	});
}

function checkRule(resultId){
	var extra1Actual1 = "", extra1Actual2 = "", extra1Actual3 = "";
	var natMargin = "";
	var extra1 = "", extra2 = "", extra3 = "";
	var _result = "", resultData = "";
	var flag =  false;
	
	$.each($(`label:contains(NAT_Margin)`), function (j, childItem) {
		if($(`#${childItem.id}`).text() === "NAT_Margin"){
			if(j===4)
				natMargin = $(`#${childItem.id}`).next().val();
		}
 	});
	
	$.each($(`label:contains(Extra白單)`), function (j, childItem) {
		if($(`#${childItem.id}`).text() === "Extra白單")
		{
			switch(j){
			case 0:
				extra1 = $(`#${childItem.id}`).next().val();
				if(extra1 === "")
					extra1 = 0;
				break;
			case 3:
				extra2 = $(`#${childItem.id}`).next().val();
				if(extra2 === "")
					extra2 = 0;
				break;
			case 6:
				extra3 = $(`#${childItem.id}`).next().val();
				if(extra3 === "")
					extra4 = 0;
				break;	
				}
		}
		else if($(`#${childItem.id}`).text() === "Extra白單%Actual"){
			switch(j){
			case 2:
				extra1Actual1 = $(`#${childItem.id}`).next().val();
				if(extra1Actual1 === "")
					extra1Actual1 = 0;
				break;
			case 5:
				extra1Actual2 = $(`#${childItem.id}`).next().val();
				if(extra1Actual2 === "")
					extra1Actual2 = 0;
				break;
			case 8:
				extra1Actual3 = $(`#${childItem.id}`).next().val();
				if(extra1Actual3 === "")
					extra1Actual3 = 0;
				break;	
				}
		}
 	  });


	 setBindingItemValue($(`label:contains(檢核規則1)`).next().attr('id'), _result);
	 setBindingItemValue($(`label:contains(檢核規則2)`).next().attr('id'), _result);
	 setBindingItemValue($(`label:contains(檢核規則3)`).next().attr('id'), _result);
	 setBindingItemValue($(`label:contains(檢核規則4)`).next().attr('id'), _result);
	 setBindingItemValue($(`label:contains(檢核規則5)`).next().attr('id'), _result);
	 setBindingItemValue($(`label:contains(檢核規則6)`).next().attr('id'), _result);
	 setBindingItemValue($(`label:contains(檢核規則7)`).next().attr('id'), _result);
	 setBindingItemValue($(`label:contains(檢核規則8)`).next().attr('id'), _result);
	 if(extra1Actual1 === extra1Actual2 && extra1Actual1 === extra1Actual3)
		_result = "連續三年未調整";
		
	 if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則1)`).next().attr('id'), _result);
	   	flag =  true;
		resultData = resultData + _result + "/"; 
		_result = "";
	 }

	 if(extra1Actual3 > extra1Actual2 &&  extra1Actual3 > natMargin)
 		_result = `有調整 且cover margin`;
//        _result = `有調整 ${parseFloat(extra1Actual3)-parseFloat(extra1Actual2)}且cover margin`;

	 if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則2)`).next().attr('id'), _result);
		flag =  true;
		resultData = resultData + _result + "/"; 
		_result = "";
	 }

	 if(extra1Actual3 > extra1Actual2 &&  extra1Actual3 < natMargin)
		_result = `有調整但未cover margin`;
//        _result = `有調整 ${parseFloat(extra1Actual3)-parseFloat(extra1Actual2)}但未cover margin`;

	 if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則3)`).next().attr('id'), _result);
		flag =  true;
		resultData = resultData + _result + "/"; 
		_result = "";
	 }

	 if(extra1Actual3 === extra1Actual2 &&  extra1Actual2 > natMargin)
        _result = `未調整 但有cover margin`;

	 if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則4)`).next().attr('id'), _result);
		flag =  true;
		resultData = resultData + _result + "/"; 
		_result = "";
	 }

	 if(extra1Actual3 === extra1Actual2 &&  extra1Actual2 < natMargin)
        _result = `未調整且沒有cover margin`;

	 if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則5)`).next().attr('id'), _result);
		flag =  true;
		resultData = resultData + _result + "/"; 
		_result = "";
	 }

	 if(parseFloat(extra3) - parseFloat(extra2) > 0)
        _result = `增加數值`;

 	if(_result !== ""){
 		setBindingItemValue($(`label:contains(檢核規則6)`).next().attr('id'), _result);
		flag =  true;
		resultData = resultData + _result + "/"; 
		_result = "";
	 }

	if(flag === true){
		resultData = resultData.substring(0, resultData.length -1);
		setBindingItemValue(resultId, `不通過(` + resultData +')');
	}
	else{
		setBindingItemValue(resultId, "通過");
	}
		
}

function calculation(natMarginActualId, extraId, actualId, resultId){
	var natMarginActual = 0, extra = 0, _result = "";
	if($(`#${natMarginActualId}`).val() !== "")
		natMarginActual = $(`#${natMarginActualId}`).val();
	if($(`#${extraId}`).val() !== "")
		extra = $(`#${extraId}`).val();
	
	_result = (parseFloat(natMarginActual) + parseFloat(extra)).toString();
		
	setBindingItemValue(actualId, _result)
	CheckRule(resultId);
}

//function getApproval(detpNo, suppcode){
//	var sendObj = {
//        	"section": detpNo,
//			"suppliercode": suppcode
//    };
//    sendObj = JSON.stringify(sendObj);
//    console.log(sendObj);
//	
//    $.ajax({
//        url: getAPIURL(modal_url + "/getNegoentry"),
//        type: "POST",
//        contentType: "application/json",
//        data: sendObj,
//        success: function (response) {
//            var result = JSON.stringify(response);
//            var content = '';
//            console.log(result);
//            if(response.result.negoentryList != null){
//            	var negoentryData = response.result.negoentryList
//            	content = "<br/><div class='label-1 clr-style dis-flex' id='flowstepDiv'><table border='1' width='300px' height='500px'>";
//            	for(var i = 0 ; i < flowstepData.length ; i ++){
//            		//content += '<BR>';
//					if(i === 0)
//						content = content + flowstepData[i].stepname + "</td></tr>"; 
//					else
//						content = content + '<tr><td>--> ' + flowstepData[i].stepname + "</td></tr>";
//            	}
//                $(`legend:contains(審核意見)`).parent().append(content);
//            	$("#flowstepDiv").show();
//                if(idenId == "999999999"){
//                	$("#flowstepDiv").hide();
//                } else {
//                	$("#flowstepDiv").show();
//                }
//            	content = content + "</table></div>";
//            }
//
//        },
//        error: function (xhr, status, errorThrown) {
//            console.log(`Status:${status} ${errorThrown}`);
//            FixSysMsg.danger("Ajax發生錯誤!");
//        },
//        complete: function (xhr, status) {
//            Spinner.hide();
//        }
//    });
//    
//}
