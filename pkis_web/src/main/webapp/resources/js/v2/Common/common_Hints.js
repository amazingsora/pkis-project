/**
 * 儲存時檢核必填項目
 * @param {any} checkItemStr 檢核項目字串
 * @param {any} callback     檢核通過後執行的函式名稱
 * @returns {boolean}        執行callback或是回傳false
 */
function checkRequiredItems(checkItemStr, callback) {
    if (checkReqField(checkItemStr))
        callback();
    else
        return false;
}


/**
 * 存檔前依條件檢核是否有已存在資料
 */
function checkExistDataAndSave() {
    Spinner.show();
    var conditionKey = "CheckExistData";

    var jObjDetail = selectOptionsObj[conditionKey];

    if (jObjDetail && Array.isArray(jObjDetail)) {
        jObjDetail = jObjDetail.map(function (jVal, i) {
            var jData = $(`label:contains('${jVal}'):first`);
            if (jData.next("div").length > 0) {
                if (ajccType === "SETUP" && masterSpec === "EXAMDOCTORS" && jVal === "審查醫師")
                    jData = jData.next("div").children(":last").val();
                else
                    jData = jData.next("div").children(":first").val();
            }
            else
                jData = jData.next().val();

            return { "segmentation": jVal, "data": jData };
        });
    }

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "CHECK_EXIST",
        "data": {
            "module": module,
            "cluster": cluster,
            "class": ajccType,
            "spec": masterSpec,
            "docdetail": jObjDetail
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL(`${cluster}/CheckExistForm`),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0)
                CheckAndSave();
            else {
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
        }
    });
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
        url: getAPIURL("HINTS/Query"),
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
                        switch (masterSpec) {
                            case "EXAMDOCTORS":
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='審查年度']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='分組']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                break;
                            default:
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='用途:']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                break;
                        }
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

function auditType(selfDom) {
    if (selfDom.value === "審查醫師") {
        $("label:contains('分組')").parent().find("select").attr('disabled', false)
        $("label:contains('審查醫師')").parent().find("select:first").attr('disabled', false)
        setBindingItemValue($("label:contains('審查醫師')").parent().find("select:first").attr("id"), '');
    }
    else {
        if (selfDom.value === "RT醫師") {
            setBindingItemValue($("label:contains('分組')").parent().find("select").attr("id"), '998(RT醫師)');
            setBindingItemValue($("label:contains('審查醫師')").parent().find("select:first").attr("id"), '腫瘤治療科');
        } else if (selfDom.value === "其他審查人員") {
            setBindingItemValue($("label:contains('分組')").parent().find("select").attr("id"), '999(其他審查人員)');
            setBindingItemValue($("label:contains('審查醫師')").parent().find("select:first").attr("id"), '其他審查人員');
        }
        $("label:contains('分組')").parent().find("select").attr('disabled', true)
        $("label:contains('審查醫師')").parent().find("select:first").attr('disabled', true)
    }
}
