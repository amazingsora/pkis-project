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
        url: getAPIURL("CODELIST/Query"),
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
                            case "CODE":     //代碼類別
                            case "CODELIST": //代碼表
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='代碼類別']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='代碼']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                break;
                            case "CANCERCODE": //癌別代碼
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='癌別代碼']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='癌別團隊代碼']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                break;
                            case "S3CMASTER":
                            case "S3CDETAIL":
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='主檔編號']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='父編號']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='代碼']")) {
                                        orderList.push([i, "asc"]); return false;
                                    }
                                });
                                break;
                            default:
                                oTable.find("thead > tr > th").each(function (i, item) {
                                    if ($(this).is("[aria-label^='代碼']")) {
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