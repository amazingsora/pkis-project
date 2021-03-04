var $slide = $("[class^=page-container-middle]");

$(function () {
    //先隱藏側邊選單
    $slide.hide();

    var $w = $slide.width();
    var $zIdx;
    if ($slide.parent().length > 0 && typeof $slide.parent().zIndex === "function") $zIdx = $slide.parent().zIndex();
    //console.log(`$w: ${$w}, ${$slide.length}, $zIdx: ${$zIdx}`);

    //滑鼠滑入時 & 滑鼠點擊時(行動裝置使用)
    $(".page-slide-tab").bind("mouseover click", function () {
        if ($(this).parent().css("right") === `-${$w}px`) {
            $(this).parent().css("zIndex", parseInt($zIdx) + 2);
            $(this).parent().animate({ right: "0px" });
        }
    });

    //滑鼠離開後
    $(".page-slide-content").mouseleave(function () {
        $slide.animate({ right: `-${$w}px` });
        $(this).parent().css("zIndex", "initial");
    });

    $(document).click(function (e) {
        var $parClsName = $(e.target).closest("div").attr("class");
        //console.log(`$parClsName: ${$parClsName}`);

        //選單區
        if ($parClsName !== "FlatAccordionMenu" && $parClsName !== "navbar-header") {
            if ($("#PatCluster").val() === "undefined" ||
                $("#PatAjcctype").val() === "undefined" ||
                $("#PatSpec").val() === "undefined")
                $(".site-menubar").toggleClass("site-menubar-hide");
        }

        //登入資訊區
        if ($parClsName !== "navbar-collapse navbar-collapse-toolbar collapse in" &&
            $(".navbar-collapse.navbar-collapse-toolbar.collapse").hasClass("in")) {
            $(`button[data-target="#site-navbar-collapse"][data-toggle="collapse"]`).click();
        }

        //側邊選單區
        if ($parClsName !== "page-slide-content") {
            if ($slide.css("right") === "0px") {
                $slide.animate({ right: `-${$w}px` });
                $slide.css("zIndex", "initial");
            }
        }
    });

    render_PageSlide();
});

function render_PageSlide() {
    if (typeof cluster !== "undefined" && typeof ajccType !== "undefined") {
        console.log(`SlideScroll.js => cluster:${cluster}, ajccType:${ajccType}, masterSpec:${masterSpec}`);

        //個管
        if (cluster === "COL" && ajccType === "QRY" &&
            ($("#edit_id").length > 0 || !masterSpec.match(/(WAITLIST|REC)/g))) {
            //將[個案查詢]的Resultlist HTML放入浮動控制DIV區塊
            render_PageSlide_CM("render_PageSlide");
        }

        //診療計畫書
        if (cluster === "TP" && ajccType.match(/(QRY|REC)/g) &&
            ($("#edit_id").length > 0 || !masterSpec.match(/(UNFILLEDLSIT|REC)/g))) {
            //將[診療計畫書]的Resultlist HTML放入浮動控制DIV區塊
            render_PageSlide_TP("render_PageSlide");
        }

        //腫瘤心理
        if (cluster === "TM" && ajccType === "PSYCHOLOGIC") {
            //將[腫瘤心理]的Resultlist HTML放入浮動控制DIV區塊
            render_PageSlide_TM("render_PageSlide");
        }

        //個管審查
        if (cluster === "CEM" && ajccType === "QRY" && masterSpec === "REC" &&
            $("#edit_id").length > 0 && doccode.match(/-R\w+-/)) {
            render_PageSlide_CEM("render_PageSlide");
        }

        //癌登審查
        if (cluster === "REV" && ajccType === "QRY" && masterSpec.match(/(TNMSTAGE|RT|LONG|SHORT)/g) &&
            $("#edit_id").length > 0) {
            render_PageSlide_REV("render_PageSlide");
        }

    }

    //顯示側邊選單
    $slide.show();
    //將未使用的Slide頁籤隱藏
    Hide_PageSlide($slide);
}

//個管
function render_PageSlide_CM(processCode) {
    var domDivClass = "page-slide-content", resHtml = "", $resDom, orderNum, orderType = "desc";

    if (processCode === "render_PageSlide" && $(`.${domDivClass}`).length > 0) {
        $resDom = $(`<div></div>`);
        //顯示5個按鍵
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="手術紀錄" onclick="GetViewReadonly('CRM_', '', 'SURGREC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="就醫紀錄" onclick="GetViewReadonly('CRM_', '', 'RECEMR_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="收案紀錄" onclick="GetViewReadonly('CRM_', '', 'CRMOTH_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射治療摘要" onclick="GoToRT('RTSummary','RTDOC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射計畫書" onclick="GoToRT('RTPlan','RTDOC_URL');">`);
        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-1" width="100%"></table>`);
        $(`div[class='page-container-middle-1'] > div.page-slide-tab`).text("個案管理");
        $(`div[class='page-container-middle-1'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-2" width="100%"></table>`);
        $(`div[class='page-container-middle-2'] > div.page-slide-tab`).text("診療計劃書");
        $(`div[class='page-container-middle-2'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-3" width="100%"></table>`);
        $(`div[class='page-container-middle-3'] > div.page-slide-tab`).text("多專科團隊");
        $(`div[class='page-container-middle-3'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        //唯讀頁面
        //先取病歷號(從localStorage或是頁面上)
        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_AjccPlanRec });
        $resDom = MainProcess();
        if ($resDom !== undefined) {
            $resDom = JSON.parse($resDom);
            if ($resDom.hasOwnProperty("病歷號")) resHtml = $resDom["病歷號"];
        } else {
            $resDom = $("#msgBlock1").find("div:contains('病歷號')");
            if ($resDom.length > 0) resHtml = $resDom.text().replace(/病歷號[\:|：]/g, "");
        }

        if (resHtml) {
            var resultReadOnlyListItems = ["收案類別", "案號", "#初診斷日期READONLYLINK"];
            orderNum = 1;
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-1", cluster, resHtml, resultReadOnlyListItems, orderNum, orderType);
            resultReadOnlyListItems = ["收案類別", "主治醫師", "#填寫日期READONLYLINK"];
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-2", "TP", resHtml, resultReadOnlyListItems, orderNum, orderType);
            //20191112 多專科側邊選單
            resultReadOnlyListItems = ["團隊名稱", "#會議日期READONLYLINK"];
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-3", "MST", resHtml, resultReadOnlyListItems, orderNum, orderType);
        }
    }
}

//診療計劃書
function render_PageSlide_TP(processCode) {
    var domDivClass = "page-slide-content", resHtml = "", $resDom, orderNum, orderType = "desc";

    if (processCode === "render_PageSlide" && $(`.${domDivClass}`).length > 0) {
        $resDom = $(`<div></div>`);
        //顯示6個按鍵
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="報告查詢" onclick="GetViewReadonly('CRM_', '', 'EMRQRY_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="手術紀錄" onclick="GetViewReadonly('CRM_', '', 'SURGREC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="就醫紀錄" onclick="GetViewReadonly('CRM_', '', 'RECEMR_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="收案紀錄" onclick="GetViewReadonly('CRM_', '', 'CRMOTH_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射治療摘要" onclick="GoToRT('RTSummary','RTDOC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射計畫書" onclick="GoToRT('RTPlan','RTDOC_URL');">`);
        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-1" width="100%"></table>`);
        $(`div[class='page-container-middle-1'] > div.page-slide-tab`).text("病歷查詢");
        $(`div[class='page-container-middle-1'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-2" width="100%"></table>`);
        $(`div[class='page-container-middle-2'] > div.page-slide-tab`).text("多專科團隊");
        $(`div[class='page-container-middle-2'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        //唯讀頁面
        //先取病歷號(從localStorage或是頁面上)
        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_AjccPlanRec });
        $resDom = MainProcess();
        if ($resDom !== undefined) {
            $resDom = JSON.parse($resDom);
            if ($resDom.hasOwnProperty("病歷號")) resHtml = $resDom.病歷號;
        } else {
            $resDom = $("#msgBlock1").find("div:contains('病歷號')");
            if ($resDom.length > 0) resHtml = $resDom.text().replace(/病歷號[\:|：]/g, "");
        }

        if (resHtml) {
            var resultReadOnlyListItems = ["收案類別", "主治醫師", "#填寫日期READONLYLINK"];
            orderNum = 1;
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-1", cluster, resHtml, resultReadOnlyListItems, orderNum, orderType);
            resultReadOnlyListItems = ["團隊名稱", "#會議日期READONLYLINK"];
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-2", "MST", resHtml, resultReadOnlyListItems, orderNum, orderType);
        }
    }
}

//腫瘤心理
function render_PageSlide_TM(processCode) {
    var domDivClass = "page-slide-content", resHtml = "", $resDom;

    if (processCode === "render_PageSlide" && $(`.${domDivClass}`).length > 0) {
        $resDom = $(`<div></div>`);
        //顯示3個按鍵
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="手術紀錄" onclick="GetViewReadonly('CRM_', '', 'SURGREC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="就醫紀錄" onclick="GetViewReadonly('CRM_', '', 'RECEMR_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="收案紀錄" onclick="GetViewReadonly('CRM_', '', 'CRMOTH_URL');">`);
        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-1" width="100%"></table>`);
        $(`div[class='page-container-middle-1'] > div.page-slide-tab`).text("腫瘤心理");
        $(`div[class='page-container-middle-1'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        //唯讀頁面
        //先取病歷號(從localStorage或是頁面上)
        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_AjccPlanRec });
        $resDom = MainProcess();
        if ($resDom !== undefined) {
            $resDom = JSON.parse($resDom);
            if ($resDom && !$resDom.hasOwnProperty("病歷號"))
                resHtml = $resDom["病歷號"];
        } else {
            $resDom = $("#msgBlock1").find("div:contains('病歷號')");
            if ($resDom.length > 0) resHtml = $resDom.text().replace(/病歷號[\:|：]/g, "");
        }

        if (resHtml) {
            var resultReadOnlyListItems = ["收案類別", "#案號READONLYLINK", "初診斷日期"];
            orderNum = 2;
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-1", cluster, resHtml, resultReadOnlyListItems, orderNum, orderType);
        }
    }
}


//個管審查
function render_PageSlide_CEM(processCode) {
    var domDivClass = "page-slide-content", resHtml = "", $resDom, orderNum, orderType = "desc";

    if (processCode === "render_PageSlide" && $(`.${domDivClass}`).length > 0) {
        $resDom = $(`<div></div>`);
        //顯示8個按鍵
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="報告查詢" onclick="GetViewReadonly('CRM_', '', 'EMRQRY_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="手術紀錄" onclick="GetViewReadonly('CRM_', '', 'SURGREC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="就醫紀錄" onclick="GetViewReadonly('CRM_', '', 'RECEMR_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="收案紀錄" onclick="GetViewReadonly('CRM_', '', 'CRMOTH_URL');"><br />`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="診療指引" onclick="GetViewReadonly('CRM_', '', 'KMMTG_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射治療摘要" onclick="GoToRT('RTSummary', 'RTDOC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射計畫書" onclick="GoToRT('RTPlan', 'RTDOC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="TNM期別" onclick="GetViewReadonly('CRM_', '', 'TNMSTAGE_URL');">`);
        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-1" width="100%"></table>`);
        $(`div[class='page-container-middle-1'] > div.page-slide-tab`).text("病歷查詢");
        $(`div[class='page-container-middle-1'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-2" width="100%"></table>`);
        $(`div[class='page-container-middle-2'] > div.page-slide-tab`).text("多專科團隊");
        $(`div[class='page-container-middle-2'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        //唯讀頁面
        //先取病歷號(從localStorage或是頁面上)
        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_AjccPlanRec });
        $resDom = MainProcess();
        if ($resDom !== undefined) {
            $resDom = JSON.parse($resDom);
            if ($resDom.hasOwnProperty("病歷號")) resHtml = $resDom.病歷號;
        } else {
            $resDom = $("#msgBlock1").find("div:contains('病歷號')");
            if ($resDom.length > 0) resHtml = $resDom.text().replace(/病歷號[\:|：]/g, "");
        }

        if (resHtml) {
            orderNum = 1;
            var resultReadOnlyListItems = ["收案類別", "主治醫師", "#填寫日期READONLYLINK"];
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-1", cluster, resHtml, resultReadOnlyListItems, orderNum, orderType);
            resultReadOnlyListItems = ["團隊名稱", "#會議日期READONLYLINK"];
            render_PageSlide_ReadOnly("qry_pageslide_readonlylist-2", "MST", resHtml, resultReadOnlyListItems, orderNum, orderType);
        }
    }
}

//癌登審查
function render_PageSlide_REV(processCode) {
    var domDivClass = "page-slide-content", resHtml = "", $resDom, orderNum, orderType = "desc";

    if (processCode === "render_PageSlide" && $(`.${domDivClass}`).length > 0) {
        $resDom = $(`<div></div>`);
        //顯示6個按鍵
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="報告查詢" onclick="GetViewReadonly('CRM_', '', 'EMRQRY_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="手術紀錄" onclick="GetViewReadonly('CRM_', '', 'SURGREC_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="就醫紀錄" onclick="GetViewReadonly('CRM_', '', 'RECEMR_URL');">`);
        $resDom.append(`<input class="button-0 btn skh-btn" type="button" value="收案紀錄" onclick="GetViewReadonly('CRM_', '', 'CRMOTH_URL');">`);
        //$resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射治療摘要" onclick="GoToRT('RTSummary','RTDOC_URL');">`);
        //$resDom.append(`<input class="button-0 btn skh-btn" type="button" value="放射計畫書" onclick="GoToRT('RTPlan','RTDOC_URL');">`);
        $resDom.append(`<br /><br /><table class="table table-bordered table-striped" id="qry_pageslide_readonlylist-1" width="100%"></table>`);
        $(`div[class='page-container-middle-1'] > div.page-slide-tab`).text("病歷查詢");
        $(`div[class='page-container-middle-1'] > div.${domDivClass}`).html($resDom.html());
        $resDom.html("");

        SetMainProcessPara("getLocalStorage", { "key": "#SOURCE" });
        $resDom = JSON.parse(MainProcess());

        if ($resDom["6-1摘錄者"].split('(')[0] === getUserId()) {
            $(`div[class='page-container-middle-1'] > div.page-slide-tab`).text("病歷查詢與諮詢單檢視");
            if ($resDom["資料編號"]) resHtml = $resDom["1-2病歷號"] + "/" + $resDom["資料編號"];

            if (resHtml) {
                var resultReadOnlyListItems = ["諮詢日期", "諮詢人員", "醫師回覆", "病歷不一致諮詢"];
                orderNum = 1;
                render_PageSlide_ReadOnly("qry_pageslide_readonlylist-1", cluster, resHtml, resultReadOnlyListItems, orderNum, orderType);
            }
        }
    }
}


/**
 * 側邊選單的唯讀頁
 * @param {any} target                  目標jquery datatableID
 * @param {any} cluster                 傳入指定的cluster
 * @param {any} CHRT                    病歷號(先從localStorage內取，若無再從頁面上取得)
 * @param {any} resultReadOnlyListItems 指定唯讀頁所需呈現的欄位(使用陣列表示)
 * @param {any} orderNum                排序的欄位Index
 * @param {any} orderType               排序方式
 */
function render_PageSlide_ReadOnly(target, cluster, CHRT, resultReadOnlyListItems, orderNum, orderType) {
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PSRO",
        "data": {
            "module": module,
            "cluster": cluster,
            "class": ajccType,
            "spec": masterSpec,
            "docdetail": [
                { "segmentation": "病歷號", "data": CHRT },
                { "segmentation": "dataid", "data": $("#edit_id").val() },
                { "segmentation": "resultListItems", "data": resultReadOnlyListItems }
            ]
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("pageslide/ReadOnly"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0 && response.jsonData.data) {
                var result = JSON.parse(response.jsonData.data);
                console.log(result);

                if (result !== undefined && result.length > 0 && $(`#${target}`).length > 0) {
                    var nowTbOptions = {
                        info: false,  //共幾筆
                        lengthChange: false,
                        order: [[orderNum, orderType]],
                        paging: false //分頁和顯示幾筆
                    };

                    //繪製DataTable
                    NewResultListColumnDefs($(`#${target}`), resultReadOnlyListItems, nowTbOptions);

                    var oTable = $(`#${target}`).dataTable();
                    oTable.fnAddData(result);
                }
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("側邊唯讀歷程查詢發生錯誤!");
        }
    });
}


/**
 * 將[查詢]後的結果從localStorage內讀出後重繪至浮動控制DIV區塊
 * @param {any} target    目標jquery datatableID
 * @param {any} divHolder 重繪至的側邊DIV區塊ClassName
 * @param {any} orderNum  排序的欄位Index
 * @param {any} orderType 排序方式
 */
function render_PageSlide_ResultList(target, divHolder, orderNum, orderType) {
    var qryRS_ListItems, qryRS_List;
    SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_BoxItems });
    qryRS_ListItems = MainProcess();
    SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_BoxResult });
    qryRS_List = MainProcess();

    if (qryRS_ListItems && qryRS_List) {
        $resDom = $(`<div><br /><table class="table table-bordered table-striped" id="${target}" width="100%"></table></div>`);
        $(`div[class='page-container-middle-2'] > div.page-slide-tab`).text("查詢管理");
        $(`div[class='page-container-middle-2'] > div.${divHolder}`).html($resDom.html());

        var nowTbOptions = {
            info: false,  //共幾筆
            lengthChange: false,
            order: [[orderNum, orderType]],
            paging: true  //分頁和顯示幾筆
        };

        //繪製DataTable
        NewResultListColumnDefs($(`#${target}`), JSON.parse(qryRS_ListItems), nowTbOptions);

        var oTable = $(`#${target}`).dataTable();
        oTable.fnAddData(JSON.parse(qryRS_List));
    }
}


/**
 * 將[查詢]後的結果重繪至浮動控制DIV區塊
 * @param {any} target 目標jquery datatableID
 * @param {any} CHRT   病歷號
 */
function render_PageSlide_TNMStage(target, CHRT) {
    $.ajax({
        url: getAPIURL(`v1/EMR/TNMStage?CHRT=${CHRT}`),
        type: "GET",
        success: function (response) {
            if (response.match(/^\<table/))
                $(`#${target}`)[0].innerHTML = `<tr><td>${response}</td></tr>`;
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("側邊TNM期別歷程查詢發生錯誤!");
        }
    });
}


/**
 * 將未使用的Slide頁籤隱藏
 * @param {any} obj 指定側邊選單的區塊
 */
function Hide_PageSlide(obj) {
    obj.find("div.page-slide-tab").each(function (i, item) {
        if ($(item).text() === "側邊選單區") $(item).parent().hide();
    });
}