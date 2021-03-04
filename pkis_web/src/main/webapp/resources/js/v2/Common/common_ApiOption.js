/**
 * 取得[代碼類別表]相關說明
 * @param {any} phraseStr 片語名稱
 */
function onSelectHintsUsages(phraseStr) {
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "HintsUsages",
        "data": {
            "module": module,
            "cluster": cluster,
            "class": ajccType,
            "spec": masterSpec,
            "func": phraseStr
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("HINTS/SelectHintsUsages"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
            if (response.rtnCode === 0) {
                console.log(JSON.parse(response.jsonData.data));

                selectOptionsObj[phraseStr] = JSON.parse(response.jsonData.data);
            } else
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
}

/**
 * 取[片語維護]頁籤的資料
 * @param {any} phraseStr 簡訊提醒片語、個管紀錄片語、醫師姓名片語、追蹤日期預設區間片語等
 */
function onSelectHints(phraseStr) {
//	delete selectOptionsObj[phraseStr] 
//    if (!selectOptionsObj[phraseStr]) {
//        var selectOptObj = sessionStorage.getItem(`selectOptionsObj.${phraseStr}`);
//        if (selectOptObj){
//        	selectOptionsObj[phraseStr] = JSON.parse(selectOptObj);
//        	console.log(selectOptionsObj[phraseStr]);
//        }
//        else {
            var obj = {
                "data": {
                    "module": "BDS",
                    "cluster": "HINTS",
                    "class": "SETUP",
                    "spec": "PHRASE",
                    "gp": phraseStr,
					"model": dataid.indexOf('LOGR') > -1 ? "SC" : "NSC"
                }
            };
            obj = JSON.stringify(obj);
            console.log(obj);

            $.ajax({
                url: getAPIURL(modal_url+"/getCode"),
                type: "POST",
                contentType: "application/json",
                data: obj,
                async: false,
                success: function (response) {
                	if (response.rtnCode === 0) {
                        console.log(JSON.parse(response.jsonData.data));

                        selectOptionsObj[phraseStr] = JSON.parse(response.jsonData.data); 
                        sessionStorage.setItem(`selectOptionsObj.${phraseStr}`, response.jsonData.data);
                    } else
                        FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
                },
                error: function (xhr, status, errorThrown) {
                    console.log(`Status:${status} ${errorThrown}`);
                    FixSysMsg.danger("Ajax發生錯誤!");
                }
            });
//        }
//    }
}


//20191122 雲端
function setSelectAjcc7TemplateList(code, phraseStr) {
    var getDataStatus = 0;
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "",
        "data": {
            "module": "CR",
            "cluster": "REC",
            "class": "MAINT",
            "spec": "REG",
            "code": code
        }
    };
    obj = JSON.stringify(obj);

    $.ajax({
        url: getAPIURL("MAINT/GetAjcc7Template"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
            if (response.rtnCode === 0) {
                console.log(response.jsonData.data);

                eval(`selectOptionsObj["${phraseStr}"]=${response.jsonData.data}`);
                selectOptionsObj[phraseStr] = response.jsonData.data;
                sessionStorage.setItem(`selectOptionsObj.${phraseStr}`, response.jsonData.data);
            } else {
                getDataStatus = response.rtnCode;
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
    return getDataStatus;
}

//20191122 雲端
function onSelectTNMList7(templateCode, templateSubCode) {
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "",
        "data": {
            "module": "CR",
            "cluster": "REC",
            "class": "MAINT",
            "spec": "REG",
            "code2": templateCode,
            "subCode": templateSubCode
        }
    };
    obj = JSON.stringify(obj);

    $.ajax({
        url: getAPIURL("MAINT/GetTNMList7"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
            if (response.rtnCode === 0) {
                console.log(response.jsonData.data);
                var rtnJson = JSON.parse(response.jsonData.data);
                selectOptionsObj['array_C_T'] = rtnJson['array_C_T'];
                selectOptionsObj['array_C_N'] = rtnJson['array_C_N'];
                selectOptionsObj['array_C_M'] = rtnJson['array_C_M'];
                selectOptionsObj['array_C_Code'] = rtnJson['array_C_Code'];
                selectOptionsObj['array_P_T'] = rtnJson['array_P_T'];
                selectOptionsObj['array_P_N'] = rtnJson['array_P_N'];
                selectOptionsObj['array_P_M'] = rtnJson['array_P_M'];
                selectOptionsObj['array_P_Code'] = rtnJson['array_P_Code'];
                selectOptionsObj['allArray'] = rtnJson['allArray'];
                //eval(`selectOptionsObj["${phraseStr}"]=${response.jsonData.data}`);
                //selectOptionsObj[phraseStr] = response.jsonData.data;
                //sessionStorage.setItem(`selectOptionsObj.${phraseStr}`, response.jsonData.data);
            } else
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
}

//20191122 雲端
function onSelectTNMList8(chapNo) {
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "",
        "data": {
            "module": "CR",
            "cluster": "REC",
            "class": "MAINT",
            "spec": "REG",
            "chapNo": chapNo
        }
    };
    obj = JSON.stringify(obj);

    $.ajax({
        url: getAPIURL("MAINT/GetTNMList8"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
            if (response.rtnCode === 0) {
                console.log(response.jsonData.data);
                var rtnJson = JSON.parse(response.jsonData.data);
                selectOptionsObj['array_C_T'] = rtnJson['array_C_T'];
                selectOptionsObj['array_C_N'] = rtnJson['array_C_N'];
                selectOptionsObj['array_C_M'] = rtnJson['array_C_M'];
                selectOptionsObj['array_C_Code'] = rtnJson['array_C_Code'];
                selectOptionsObj['array_P_T'] = rtnJson['array_P_T'];
                selectOptionsObj['array_P_N'] = rtnJson['array_P_N'];
                selectOptionsObj['array_P_M'] = rtnJson['array_P_M'];
                selectOptionsObj['array_P_Code'] = rtnJson['array_P_Code'];
                selectOptionsObj['allArray'] = rtnJson['allArray'];
            } else
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
}

//20191122 雲端
function onSelectCommonList(phraseStr) {
    var data = [];
    var selectOptObj = sessionStorage.getItem(`selectOptionsObj.${phraseStr}`);
    if (selectOptObj !== "[]" && selectOptObj !== null) {
        data = JSON.parse(selectOptObj);
    }
    else if (!selectOptionsObj[phraseStr]) {
        var obj = {
            "token": getToken(),
            "userId": getUserId(),
            "accessCode": "CodeList",
            "data": { "func": phraseStr }
        };
        obj = JSON.stringify(obj);
        console.log(obj);

        $.ajax({
            url: getAPIURL("CODELIST/GetCodeList"),
            type: "POST",
            contentType: "application/json",
            data: obj,
            async: false,
            success: function (response) {
                if (response.rtnCode === 0) {
                    var datas = JSON.parse(response.jsonData.data);
                    data = [];

                    datas.forEach(function (element) {
                        if (phraseStr === element["codeClass"]) {
                            if (phraseStr === '其他分期臨床Stage' || phraseStr === '其他分期病理Stage' || phraseStr === 'AJCC8分級' || phraseStr === '其他放射治療技術') {
                                var changeStr = phraseStr;
                                data.push({
                                    [changeStr]: element[phraseStr.toLowerCase() + '代碼'], '中文': element[phraseStr.toLowerCase()],
                                    '父代碼類別': element['父代碼類別'], '父代碼子項': element['父代碼子項'], '備註': element['備註']
                                });
                            } else if (phraseStr === '首次復發型式_子項') {
                                data.push({
                                    [phraseStr]: element[phraseStr.toLowerCase() + '代碼'], '中文': element[phraseStr.toLowerCase()],
                                    '父代碼': element['父代碼']
                                });
                            }
                            else {
                                data.push({
                                    [phraseStr]: element[phraseStr.toLowerCase() + '代碼'], '中文': element[phraseStr.toLowerCase()]
                                });
                            }

                        }
                    });
                } else
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            },
            error: function (xhr, status, errorThrown) {
                console.log(`Status:${status} ${errorThrown}`);
                FixSysMsg.danger("Ajax發生錯誤!");
            }
        });
    }

    data = data.sort(function (a, b) {
        return parseInt(a[phraseStr]) > parseInt(b[phraseStr]) ? 1 : -1;

    });
    selectOptionsObj[phraseStr] = data;
    sessionStorage.setItem(`selectOptionsObj.${phraseStr}`, JSON.stringify(data));

    return data;
}

/**
 * 癌登連動選單依父項目取值
 * @param {any} phraseStr   sessionStorage參數名稱
 * @param {any} parentCode  父代碼類別
 * @param {any} parentCode2 父代碼子項
 * @returns {Array}         回傳陣列
 */
function onSelectCommonListParentCode(phraseStr, parentCode, parentCode2) {
    var data = [];

    var selectOptObj = sessionStorage.getItem(`selectOptionsObj.${phraseStr}`);
    if (selectOptObj !== "[]" && selectOptObj !== null) {
        data = JSON.parse(selectOptObj);
    } else data = onSelectCommonList(phraseStr);

    if (phraseStr === '其他分期臨床Stage' || phraseStr === '其他分期病理Stage' || phraseStr === 'AJCC8分級' || phraseStr === '其他放射治療技術')
        data = data.filter(p => p['父代碼類別'] === parentCode && p['父代碼子項'] === parentCode2);
    else if (phraseStr === '首次復發型式_子項') data = data.filter(p => p['父代碼'] === parentCode);
    data = data.sort(function (a, b) {
        return parseInt(a[phraseStr]) > parseInt(b[phraseStr]) ? 1 : -1;
    });

    return data;
}

//癌別
function onSelectCodeList(phraseStr) {
    if (!selectOptionsObj[phraseStr]) {
        var selectOptObj = sessionStorage.getItem(`selectOptionsObj.${phraseStr}`);

        if (selectOptObj)
            selectOptionsObj[phraseStr] = JSON.parse(selectOptObj);
        else {
            var obj = {
                "token": getToken(),
                "userId": getUserId(),
                "accessCode": "CodeList",
                "data": { "func": phraseStr }
            };
            obj = JSON.stringify(obj);
            console.log(obj);

            $.ajax({
                url: getAPIURL("CODELIST/GetCodeList"),
                type: "POST",
                contentType: "application/json",
                data: obj,
                async: false,
                success: function (response) {
                    if (response.rtnCode === 0) {
                        console.log(JSON.parse(response.jsonData.data));

                        selectOptionsObj[phraseStr] = JSON.parse(response.jsonData.data);
                        sessionStorage.setItem(`selectOptionsObj.${phraseStr}`, response.jsonData.data);
                    } else
                        FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
                },
                error: function (xhr, status, errorThrown) {
                    console.log(`Status:${status} ${errorThrown}`);
                    FixSysMsg.danger("Ajax發生錯誤!");
                }
            });
        }
    }
}


//通知主治醫師(POST)
function getMainDoctorList(phraseStr) {
    var tmpConditionVal = ""; //eg.個案號

    if (selectOptionsObj.baseControlers && selectOptionsObj.baseControlers.length > 0) {
        //找baseControlers內是否有對應的條件參數
        selectOptionsObj.baseControlers.forEach(function (item) {
            if (item.controller && item.controller.indexOf(phraseStr) > -1) {
                tmpConditionVal = item.data; return false;
            }
        });
    }

    if (tmpConditionVal !== "") {
        var obj = {
            "token": getToken(),
            "userId": getUserId(),
            "data": { "caseid": tmpConditionVal }
        };
        obj = JSON.stringify(obj);
        console.log(obj);

        $.ajax({
            url: getAPIURL("HINTS/MainDoctorList"),
            type: "POST",
            contentType: "application/json",
            data: obj,
            async: false,
            success: function (response) {
                if (response.rtnCode === 0) {
                    console.log(JSON.parse(response.jsonData.data));
                    //eval(`selectOptionsObj["通知主治醫師"]=${response.jsonData.data}`);
                    selectOptionsObj["通知主治醫師"] = JSON.parse(response.jsonData.data);
                } else
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            },
            error: function (xhr, status, errorThrown) {
                console.log(`Status:${status} ${errorThrown}`);
                FixSysMsg.danger("Ajax發生錯誤!");
            }
        });
    }
}

/**
 * 取[代碼維護 - 審查醫師]的資料
 * @param {any} phraseStr 審查醫師
 */
function getAuditDoctor(phraseStr) {
    if (!selectOptionsObj[phraseStr]) {
        var selectOptObj = sessionStorage.getItem(`selectOptionsObj.${phraseStr}`);

        if (selectOptObj)
            selectOptionsObj[phraseStr] = JSON.parse(selectOptObj);
        else {
            var obj = {
                "token": getToken(),
                "userId": getUserId(),
                "accessCode": "Hints",
                "data": {
                    "module": "BDS",
                    "cluster": "HINTS",
                    "class": "SETUP",
                    "spec": "EXAMDOCTORS",
                    "func": phraseStr
                }
            };
            obj = JSON.stringify(obj);
            console.log(obj);

            $.ajax({
                url: getAPIURL("HINTS/SelectHints"),
                type: "POST",
                contentType: "application/json",
                data: obj,
                async: false,
                success: function (response) {
                    if (response.rtnCode === 0) {
                        console.log(JSON.parse(response.jsonData.data));

                        selectOptionsObj[phraseStr] = JSON.parse(response.jsonData.data);
                        sessionStorage.setItem(`selectOptionsObj.${phraseStr}`, response.jsonData.data);
                    } else
                        FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
                },
                error: function (xhr, status, errorThrown) {
                    console.log(`Status:${status} ${errorThrown}`);
                    FixSysMsg.danger("Ajax發生錯誤!");
                }
            });
        }
    }
}

//20191122 雲端 
//癌登代碼維護
function onSelectParentCodeList(phraseStr) {
    if (!selectOptionsObj[phraseStr]) {
        var obj = {
            "token": getToken(),
            "userId": getUserId(),
            "accessCode": "CodeList",
            "data": {
                "func": phraseStr,
                "ajcctype": ajccType,
                "spec": masterSpec
            }
        };
        obj = JSON.stringify(obj);
        console.log(obj);

        $.ajax({
            url: getAPIURL("CODELIST/GetCodeList"),
            type: "POST",
            contentType: "application/json",
            data: obj,
            async: false,
            success: function (response) {
                if (response.rtnCode === 0) {
                    console.log(JSON.parse(response.jsonData.data));

                    selectOptionsObj[phraseStr] = JSON.parse(response.jsonData.data);
                    sessionStorage.setItem(`selectOptionsObj.${phraseStr}`, response.jsonData.data);
                } else
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            },
            error: function (xhr, status, errorThrown) {
                console.log(`Status:${status} ${errorThrown}`);
                FixSysMsg.danger("Ajax發生錯誤!");
            }
        });
    }
}