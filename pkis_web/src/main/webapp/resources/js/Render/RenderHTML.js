/*=== 全域變數 ===*/
var isGetTemplateJson;
var errorMsgStatus = 0;
var module = "", cluster = "", ajccType = "", masterSpec = "", dataType = "", doccode = "", patSpec = "", seq_id = "";
var json = { "data": [{ "docdetail": [] }] }, dataIdx = 0, rowIdx = 0;
var valueIdx = 0, dropdownIdx = 0, radioIdx = 0, checkIdx = 0, mscheckIdx = 0;
var pattern = /^#(\d+(.[0-9]{1,2})?)([A,N,D,ID,HID,DOCTORID,PATIENTID])/; //驗證使用
var arr1 = [];
var arr2 = [];
var arr3;
var ajccStageMapTable;
var funAjccArr = [];
var queryTabArr = [];
var mapperData; // 接收到的json資料
var docVersion;
var firstVisitDate;
var validDisplay = []; // [JSV] 檢核提示訊息
var validCnt = 0; // [JSV] 迴圈次數控制
var resultListItems; // 接收json內的ResultListItems
var resultdata; // 接收json內的resultdata(用於填入基本資料)
var resultMaxSeq = 0; // resultList目前已顯示的最後一筆序號
var matchColName; // for ResultData的key
var matchColVal; // for 帶入ResultData的值
var sDiagnosisDate = ""; // for 帶入[初診斷日期]
var sAJCC = ""; // for AJCC期別(eg. AJCC6、AJCC7、AJCC8)
var displayItemArr = []; // for [治療]首次治療類別 DisplayItem
var currRecordKey = ""; // for [個管服務|追蹤] ResultList目前選擇的PKey
var addBtnBlockArr = []; // for [基本資料...] 暫存 增加BTN(+) 已存在的block名稱；當array非空、表示需先更新該block內既有的值

// addedParentName: 用來為[增加BTN]下的各個新block控制項取新html name
Json2HTML = function (element, parentname, changeKey = null, disabledAttr = "", preImplementJs = "", hiddenAttr = "", addedParentName = "", clearChilds = "") {
    var tmpObj, html = "", disabledHtml = "", obj = $(`<div></div>`), childsCleared = "", tmpJsArr;

    //動態產出[醫師姓名片語]、[癌別]等陣列
    getCodeListOrHints(element);

    // 取得[初診斷日期]
    var tmpDiagnosisDateItem;
    if (dataType === "基本資料" && element.item && element.item.indexOf("初診斷日期") === 0) {
        if (element.data && element.data !== "") sDiagnosisDate = element.data;

        // 取得[個案新收]新增之資料(若resultdata已有值，以此為主:2018.07.05 modify)
        tmpDiagnosisDateItem = getJsonMappingElement("resultdata", "初診斷日期", "個案查詢", false);
        if (tmpDiagnosisDateItem) sDiagnosisDate = tmpDiagnosisDateItem["初診斷日期"];
    }
    else {
        tmpDiagnosisDateItem = getJsonMappingElement("item", "初診斷日期", "基本資料", false);
        if (tmpDiagnosisDateItem) sDiagnosisDate = tmpDiagnosisDateItem.data;
        if (sDiagnosisDate === "") {
            // 取得[個案新收]新增之資料
            tmpDiagnosisDateItem = getJsonMappingElement("resultdata", "初診斷日期", "個案查詢", false);
            if (tmpDiagnosisDateItem) sDiagnosisDate = tmpDiagnosisDateItem["初診斷日期"];
        }
    }

    // 取得[AJCC期別]
    if (sAJCC === "") sAJCC = getAJCC_Version(sDiagnosisDate);

    // 依JS取得的權限，判斷是否disabled
    if (element.js && element.js.indexOf("getRights") > -1) {
        if (element.js.indexOf(";") > -1) {
            tmpJsArr = element.js.split(";");
            for (var i = 0; i < tmpJsArr.length; i++) {
                if (tmpJsArr[i].indexOf("getRights") === 0) {
                    var tmpFunc = tmpJsArr[i].indexOf(",") > -1 ? tmpJsArr[i].split(",")[0] : tmpJsArr[i];
                    if (eval(`!${tmpFunc}()`)) disabledHtml = " disabled";
                    break;
                }
            }
        }
        else if (eval(`!${element.js}()`)) disabledHtml = " disabled";
        console.log(`[${element.js}] disabledHtml:${disabledHtml}`);
    }

    // 若目前element(checkbox,radio)為"未勾選"，則以下子項均清空
    childsCleared = clearChilds;
    if (childsCleared === "") {
        if (element.method === "checkbox") {
            if (element.option) {
                $.each(element.option, function (iOpt, vOpt) {
                    if (vOpt.checked && vOpt.checked === false && vOpt.follow && vOpt.follow.length > 0) {
                        childsCleared = "cleared";
                    }
                });
            }
        }
        else if (element.method === "radio" && element.data !== "") {
            if (element.option) {
                //
            }
        }
    }

    // [JSV]
    if (element.segmentation) {
        validDisplay = [element.segmentation];
        validCnt = 0;
        displayItemArr = [element.segmentation];
    }

    // start rendering
    if (element.child) {
        if (preImplementJs !== "") {
            obj = render_child(element, parentname, null, null, preImplementJs);
        }
        else {
            obj = render_child(element, parentname);
        }
    }
    else if (element.segmentation === "title") {
        //文件Title
        if (element.parameters)
            obj.append(`<div class="title_fixed"><h1>${element.data}</h1></div>`);
        else
            obj.append(`<h1>${element.data}</h1>`);
    }
    else if (element.method === "label") {
        //控制項末端label: 改成<span>避免受到radio-custom或checkbox-custom的CSS影響

        //MANTIS Case: 0025236 -> Leo 20180705 S
        if (element.data === "迄日") {
            obj.append(`<label ${get_element_css("text", "label")}>${getAddressedColon(element.data)}</label>`);
        } else {
            obj.append(`<span ${get_element_css("text", "span")}>${element.data}</span>`);
        }
        //MANTIS Case: 0025236 -> Leo 20180705 E

        if (element.follow) {
            jQuery.each(element.follow, function (f, fobject) {
                obj.append(Json2HTML(fobject, parentname + ".follow[" + f + "]"));
            });
        }
    }
    else if (element.method === "text" || element.controller) {
        //MANTIS Case: 0025221 -> Leo 20180712 S
        obj = render_text(element, parentname, changeKey, disabledAttr, preImplementJs, hiddenAttr, addedParentName);
        //MANTIS Case: 0025221 -> Leo 20180712 E
    }
    else if (element.method === "radio") {
        obj = render_radio(element, parentname, changeKey, disabledHtml, preImplementJs, "", addedParentName);
    }
    else if (element.method === "checkbox") {
        obj = render_checkbox(element, parentname, changeKey, disabledHtml, preImplementJs, "", addedParentName);
    }
    else if (element.method === "dropdownlist") {
        obj = render_dropdownlist(element, parentname, changeKey, disabledHtml);
    }
    else if (element.method === "dropdownlist with checkbox") {
        obj = render_dropdownlistWithcheckbox(element, parentname, changeKey);
    }
    else if (element.method === "dropdownlist with hints") {
        obj = render_dropdownlistWithhints(element, parentname, changeKey);
    }
    else if (element.method === "展開") {//Leo
        var tmpHeader = element.segmentation || element.item;
        if (tmpHeader) {
            obj.append(`<button id="btUnfold" class="btn btn-primary" onclick="ToggleBlock(this)">+</button>&nbsp;<label>${tmpHeader}</label>`);
            var childObj = $(`<blockquote style="display: none;"></blockquote><hr>`);

            if (element.js) {
                preImplementJs = "";
                tmpJsArr = element.js.indexOf(",") > -1 ? element.js.split(",") : [element.js];
                $.each(tmpJsArr, function (i, jsVal) {
                    // 需提前於render時執行的js eg.[診斷] ShowHistologicGrade
                    preImplementJs += "," + jsVal;
                });
            }
            if (element.follow) {
                jQuery.each(element.follow, function (f, fobject) {
                    if (preImplementJs !== "") {
                        preImplementJs = preImplementJs.substr(1);
                        html = Json2HTML(fobject, parentname + ".follow[" + f + "]", element.dropdownitem, null, preImplementJs);
                    } else
                        html = Json2HTML(fobject, parentname + ".follow[" + f + "]", element.dropdownitem);

                    // (Todo)遇到展開時，對子項呈顯所做的CSS變更
                    // 目前先不判斷標題文字前是否有"Html空格"符號
                    if ($(`<div>${html}</div>`).find("div.form-group.row").length > 0) {
                        //若第一層子項DIV非".form-group.row"樣式
                        if (!$(`<div>${html}</div>`).children().is("div.form-group.row"))
                            html = $(`<div>${html}</div>`).addClass("form-group row")[0].outerHTML;

                        //將展開div[form-group.row]內子項右移
                        if ($(`<div>${html}</div>`).find("label.col-form-label").length === 0) {
                            //當找無label，表示只有文字，需右移更多
                            html = $(`<div>${html}</div>`).find("div.form-group.row:first").css("padding-left", "66px").parent().html();
                        } else
                            html = $(`<div>${html}</div>`).find("div.form-group.row:first").css("padding-left", "50px").parent().html();

                        var _tmpObj = $(`<div>${html}</div>`);
                        if (_tmpObj.find("div.col-md-9").length > 0) {
                            if (_tmpObj.find("div.checkbox-custom").length === 0 && _tmpObj.find("div.radio-custom").length === 0) {
                                //移除col-md-9所造成的排版不一致
                                _tmpObj.find("div.col-md-9").remove();
                                _tmpObj.find("div.form-group.row").append($(`<div>${html}</div>`).find("div.col-md-9").html());
                                html = _tmpObj.html();
                            }
                            else if (_tmpObj.find("div.checkbox-custom").length > 0 || _tmpObj.find("div.radio-custom").length > 0) {
                                html = _tmpObj.find("div.col-md-9").addClass("text-left").parents("div.form-group.row:last").parent().html();
                            }
                        }

                        //移除form-control造成的textbox樣式不一致
                        if (_tmpObj.find("input:text").length > 0) {
                            if (_tmpObj.find("input:text[maxlength='8']").length > 0) //日期欄位一般為長度8
                                _tmpObj.find("input:text[maxlength='8']").addClass("form-control").css("width", "30%");

                            _tmpObj.find("input:text[maxlength!='8']").addClass("form-control").css("width", "50%");
                            //控制textbox所屬div block內元素排列不折行
                            _tmpObj.find("input:text").css("display", "inline-block");
                            _tmpObj.find("span").css("display", "inline-block");
                            _tmpObj.find("input:text").prev("label").css("white-space", "normal"); //但是label內text要自動換行
                            _tmpObj.find("input:text").parent().css({ "white-space": "nowrap", "word-wrap": "break-word" });

                            html = _tmpObj.html();
                        }
                    }

                    childObj.append(html);
                });
            }

            obj.append(childObj);
        }
    }
    else if (element.method === "button") {
        obj = render_button(element, parentname, changeKey);

        if (parentname.split(".").length === 3 && obj.prop("outerHTML").indexOf("linkURL") > -1) {
            $("div.btn_group_list_fixed").append(`<div>${obj.html()}</div>`);
            obj = $(`<div></div>`);
        }
    }
    else if (element.remark === "controller") {
        //自動帶出的欄位
        tmpObj = getColumnName();
        if (element.ref && element.ref === "FixMessage") {
            var fixMsgHtml = "";
            //提供需長駐置頂顯示的訊息區塊
            if (element.parameters) {
                var msgArr = element.parameters.split(" ");
                if (msgArr.length > 0) {
                    msgArr.forEach(function (valMsg, iMsg) {
                        var tmpTitle = valMsg.indexOf(":") > -1 ? valMsg.split(":")[0] : valMsg;
                        var tmpValPar = valMsg.indexOf(":") > -1 ? valMsg.split(":")[1] : valMsg;
                        var tmpValue = "";
                        if (tmpValPar.indexOf("#SOURCE.") > -1) {
                            tmpValue = setSourceData(tmpValPar.replace("#SOURCE.", ""), resultdata);
                        }
                        fixMsgHtml += `<span style="display:inline-block;">${tmpTitle}：${tmpValue}<span>&nbsp;&nbsp;&nbsp;&nbsp;`;
                    });
                }
            }
            if (fixMsgHtml !== "") {
                $("#msgBlock1").css("background-color", "antiquewhite");
                $("#msgBlock1").html(fixMsgHtml);
            }
        } else {
            //動態產出[醫師姓名片語]、[癌別]等陣列
            getCodeListOrHints(element);
        }
    }
    else if (element.ref === "ResultList") {
        resultListItems = getValidResultListItems(element.ResultListItems);
        //render table class 需render scirpt
        tmpObj = getColumnName();
        if (LocalStorage_Key_ReadonlyJson.indexOf("READONLY") > -1) {
            obj.append(`<table class="table table-bordered table-striped" id="json_query" width="100%"></table>`);

            //obj.append(`<table class="table table-bordered table-striped" id="ctrl_qry_readonlylist" width="100%"></table>`);
        } else
            obj.append(`<table class="table table-bordered table-striped" id="json_query" width="100%"></table>`);

        // 如果有js，表示ResultList需自動查詢、並帶入資料
//        if (element.js && element.js === "setToBackResult" && element.parameters) {
//            if (element.parameters.indexOf("選案") > -1)
//                selectOptionsObj.baseResultListFuncStr = `setToBackResult('選案', '${element.parameters}', '${element.ref}')`;
//            else if (element.parameters.indexOf(",") > 0) {
//                if (element.parameters.split(",")[0] === "TPREADONLY")
//                    selectOptionsObj.baseResultListFuncStr = `getResultList('TP_TPQRYREC_DOC', '', 'json_query', '${element.parameters.split(",")[1]}', '')`;
//                else if (element.parameters.split(",")[0] === "COMMREADONLY") {
//                    //[唯讀列表]-個管
//                    if (cluster === "COL") selectOptionsObj.baseResultListFuncStr = `setToBackResult('', '${element.parameters.split(",")[1]}', 'COL_QRYREC_DOC')`;
//                }
//            } else
//                selectOptionsObj.baseResultListFuncStr = `setToBackResult('', '${element.parameters}', '${element.ref}')`;
//        }
//		else 
		if (dataType === "附件資料")
			selectOptionsObj.baseResultListFuncStr = `setToBackResult('', '${dataType}', '${element.ref}')`;
//        else if (ajccType === "QRYREC" && (dataType === "個管服務" || dataType === "追蹤"))
//            selectOptionsObj.baseResultListFuncStr = `setToBackResult('', '${dataType}', '${element.ref}')`;
//        else if (ajccType === "QRY" && dataType === "團隊會議")
//            selectOptionsObj.baseResultListFuncStr = `setToBackResult('', '${dataType}', '${element.ref}')`;
    }
    else if (element.method === "calculate") {
        tmpObj = getColumnName();
        obj.append(`<label>${element.item}</label>`);

        if (element.item === "BSA:") {
            obj.append(`<input type="text" id="${element.id}" name="txBSA" disabled="disabled" data-bind="value:${tmpObj}" />`);
        } else {
            obj.append(`<input type="text" id="${element.id}" name="" data-bind="value:${tmpObj}" />`);
        }

        arr1.push(`self.${tmpObj} = ko.observable(items[${rowIdx}].data);`);
        arr2.push(`${parentname}.data = viewModel.${tmpObj}();`);
    }
    else if (element.method === "+" && element.follow) {
        //增加BTN
        obj = render_addbutton(element, parentname, changeKey, disabledAttr, preImplementJs, "", addedParentName);
    }
    else if (element.implement) {
        obj = render_implement(element, parentname, changeKey, disabledAttr, preImplementJs, "", addedParentName);
        if (element.api) obj = $(`<div>${obj.html()}</div>`);
    }
    else if (element.method === "html" && element.format) {
        //經過XSS filter會轉成全形
        obj.append(element.format.replace("#", "").replace("＜", "<").replace("＞", ">"));
    }
    else {
        //其他情況 暫當textbox處理
        tmpObj = getColumnName();
        obj.append(`<label>${getAddressedColon(element.segmentation)}</label>`);
        if (element.segmentation === '初診斷日') {
            if (isGetTemplateJson) {
                obj.append(`<input type="date" onchange="checkVersion(this)" name="" data-bind="value:${tmpObj}" />`);
            } else {
                obj.append(`<input type="date" name="" data-bind="value:${tmpObj}" />`);
            }

        } else if (element.segmentation === '填寫日期') {
            obj.append(`<input type="date" name="" data-bind="value:${tmpObj}" />`);
        } else {
            html = `<input type="text" id="${element.id}" name="" data-bind="value:${tmpObj}" />`;
            if (typeof getJsvHtmlText === "function")
                obj.append(getJsvHtmlText(element, html, setJsvElementDisplay(validDisplay)));
            else
                obj.append(html);
        }

        arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
        arr2.push(`${parentname}.data = viewModel.${tmpObj}();`);
    }

    //設定隱藏
    if (element.remark && element.remark.indexOf("hide") > -1)
        obj.find("div:first").hide();

    html = obj.html();
    return html;
};

//產生控制項唯一ID
function getColumnName() {
    valueIdx++;
    return "column" + valueIdx;
}

//回傳knockout的modal位置
function knockoutPath(parentname) {
    var kpath = "";
    var parr = parentname.split(".");
    kpath = parr[2].replace("docdetail", "items");
    for (var i = 3; i < parr.length; i++) {
        kpath = kpath + "." + parr[i];
    }
    return kpath;
}

//render 動態產出的javascript
function renderScript() {
    //再重新產出側邊選單
    render_PageSlide();
    renderScript1();
    renderScript2();

    //render_Layout.js
    SetAddButtonInitialBlockItemHidden(json.data[dataIdx], $('#divbody'));
    SetTextAreaAutoHeight($('#divbody')); //將所有textarea能依內容自動展開(寬高度)
    SetCustomLayout($('#divbody'));       //執行自訂畫面樣式

    renderJsonQueryTable();
    renderScriptAjccStage();
    if ($('.myselect_multiple').length > 0) {
        $('.myselect_multiple').multiselect({
            allSelectedText: "",
            nonSelectedText: '尚未選擇',
            numberDisplayed: 7
        });
    }
    Spinner.hide();
}

//knockout binding
function renderScript1() {
    if (selectOptionsObj.AJCCFormShow === undefined) selectOptionsObj["AJCCFormShow"] = [];
    //須額外獨立執行的function
    var chkOutFmt = ["SetDiagnosisDate", "GetAJCCDetail", "setDatePickerFormat", "SetProcCSS", 
        "setBindingItemValue", "viewModel", "setMultiSelectFormat",
        "SetDoctorList", "SetDoctorLicense"],
        arrOutFmt = [];
    var scriptObj = $(`<script class='reload'></script>`);
    scriptObj.append(`var SimpleListModel = function (items) {\n`);
    scriptObj.append(`var self = this;\n`);

    //癌篩使用
    if (module === "CS" && cluster === "PS") chkOutFmt.push("DiagnosticCodeList"); //診斷碼下拉選單處理

    arr1.forEach(function (v) {
        chkOutFmt.forEach(function (val) { if (v.indexOf(`${val}(`) > -1 || v.indexOf(`${val}.`) > -1) arrOutFmt.push(v); });
        if (!arrOutFmt.find(function (item) { return item === v; })) {
            //加入處理[AJCC - FromShow]
            if (v.indexOf("FormShow(") === 0 && v.match(/AJCC/g)) selectOptionsObj["AJCCFormShow"].push(v);
            scriptObj.append(`${v}\n`);
        }
    });

    scriptObj.append(`};\n`);
    scriptObj.append(`var viewModel = new SimpleListModel(json.data[${dataIdx}].docdetail);\n`);

    //knockout binding 欄位時先清除
    scriptObj.append(`ko.cleanNode(document.getElementById('divbody'));\n`);
    scriptObj.append(`ko.applyBindings(viewModel, document.getElementById('divbody'));\n`);

    //額外獨立執行的function binding拉出來
    arrOutFmt.forEach(function (v) { scriptObj.append(`${v}${v.lastIndexOf(";") !== v.length -1 ? ";" : ""}\n`); });

    $('body').append(scriptObj);
}

//將knockout modal回填至json
function renderScript2() {
    var scriptObj = $(`<script class='reload'></script>`);
    scriptObj.append(`function getvalue(){\n`);
    scriptObj.append(`var items = json.data[${dataIdx}].docdetail;\n`);

    arr2.forEach(function (v) {
        // 增加判斷undefined，避免json的data因此被吃掉
        if (v.indexOf("=") > 0 && v) { // && !v.match(/不可小於/)
            var currScriptLVar = v.split("=")[0].trim();
            var currScriptRVal = v.split("=")[1].replace(";", "").trim();
            var tmpScript = v;
            if (currScriptRVal.match(/\.file_.+/g)) tmpScript = `${currScriptLVar} = ${currScriptRVal}.match(/[^\\\\]*$/)[0]; console.log(${currScriptRVal}.match(/[^\\\\]*$/)[0]);`;
            var newScript = `if (${currScriptRVal} !== undefined) {${tmpScript}} else {${currScriptLVar.replace(".data", '["data"]')}="";}`;
            scriptObj.append(`${newScript}\n`);
        } else {
            scriptObj.append(`${v}\n`);
        }
    });

    //Json處理完binding後，應儲存於localStorage
    scriptObj.append(`if (LocalStorage_Key_EntireJson === "") LocalStorage_Key_EntireJson = json.data[0].doccode;`);
    scriptObj.append(`SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(json) });`);
    scriptObj.append(`MainProcess();`);

    scriptObj.append(`}`);
    $('body').append(scriptObj);
}


function renderJsonQueryTable() {

}

function renderScriptAjccStage() {
    if (funAjccArr.length !== 0) {
        var scriptObj = $(`<script class="reload"></script>`);
        jQuery.each(funAjccArr, function (i, obj) {
            if (obj.name.match('getAjccStage') !== null && obj.remark === "setAjccStage") {
                scriptObj.append(`function ${obj.name}(){`);
                jQuery.each(obj.bindName, function (idx, val) {
                    scriptObj.append(`funAjccArr[${i}].parameters.para[${idx}].colvalue = viewModel.${val}();`);
                });
                scriptObj.append(`var stage = setAjccStage(funAjccArr[${i}].parameters.tabName, funAjccArr[${i}].parameters.para);`);
                scriptObj.append(`viewModel.${obj.changeName}(stage);`);
                scriptObj.append("}");
            }
        });
        $('body').append(scriptObj);
    }
}


function renderScriptWithoutJson() {
    //須額外獨立執行的function
    var chkOutFmt = ["setDatePickerFormat"],
        arrOutFmt = [];
    var scriptObj = $(`<script class='reload'></script>`);
    scriptObj.append(`var SimpleListModel = function () {\n`);
    scriptObj.append(`var self = this;\n`);

    arr1.forEach(function (v) {
        chkOutFmt.forEach(function (val) { if (v.indexOf(`${val}(`) > -1 || v.indexOf(`${val}.`) > -1) arrOutFmt.push(v); });

        if (!arrOutFmt.find(function (item) { return item === v; })) { scriptObj.append(`${v}\n`); }
    });

    scriptObj.append(`};\n`);
    scriptObj.append(`var viewModel = new SimpleListModel();\n`);

    //knockout binding 欄位時先清除
    scriptObj.append(`ko.cleanNode(document.getElementById('divbody'));\n`);
    scriptObj.append(`ko.applyBindings(viewModel, document.getElementById('divbody'));\n`);

    //額外獨立執行的function binding拉出來
    arrOutFmt.forEach(function (v) { scriptObj.append(`${v}${v.lastIndexOf(";") !== v.length - 1 ? ";" : ""}\n`); });

    $('body').append(scriptObj);
}

/* - ATTIE - 20190209 block
// 將[json]內[datatype: "ajccStage"]的code，存入ajccStageMapTable
// tabName: [ex: "ajccStage"]
function getAjccStageMap(tabName) {
    jQuery.each(mapperData.data, function (i, val) {
        if (val.datatype === tabName) {
            jQuery.each(val.docdetail, function (idx, val2) {
                ajccStageMapTable = val2.code;
            });
        }
    });

}

// tabName: [ex: "ajccStage"]
// groupVal: [ex: {★T,★N,★M}]
function setAjccStage(tabName, groupVal) {
    getAjccStageMap(tabName);
    var map = ajccStageMapTable;
    console.log(map);
    var result;
    var tmp = groupVal.length;
    result = checkAjccParaLength(tmp, map, groupVal);
    return result;
}
*/

// 比對dataList，找出stage對應的設定值 [ex: {★T: "Tis", ★N: "N0", ★M: "M0", Stage: "0"}]
// level: keyList array的長度 [ex: {★T,★N,★M} => 3]
// dataList: ajccStageMapTable (即[json]內[datatype: "ajccStage"]的code)
// keyList: key array[ex: {★T,★N,★M}]
function checkAjccParaLength(level, dataList, keyList) {
    var refData = [], refData2 = [];
    var colKey, colKey2, colKey3, colKey4, colKey5, colKey6;
    var colVal, colVal2, colVal3, colVal4, colVal5, colVal6;
    var nowStatus = true, nowVal;
    if (level === 3) {
        colKey = keyList[0].colname;
        colKey2 = keyList[1].colname;
        colKey3 = keyList[2].colname;

        colVal = keyList[0].colvalue;
        colVal2 = keyList[1].colvalue;
        colVal3 = keyList[2].colvalue;

        jQuery.each(dataList, function (i, val) {
            if (val[colKey] !== "*" && val[colKey2] !== "*" && val[colKey3] !== "*") {
                refData.push(val);
            } else {
                refData2.push(val);
            }
        });

        jQuery.each(refData, function (i, val) {
            if (val[colKey] === colVal && val[colKey2] === colVal2 && val[colKey3] === colVal3) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowVal) return nowVal;
        //has *
        jQuery.each(refData2, function (i, val) {
            var tmpKey = [];// not * 
            if (val[colKey] !== "*") tmpKey.push(colKey);
            if (val[colKey2] !== "*") tmpKey.push(colKey2);
            if (val[colKey3] !== "*") tmpKey.push(colKey3);
            if (tmpKey.length === 0) {
                nowVal = val.Stage;
                nowStatus = true;
                return false;
            }
            jQuery.each(tmpKey, function (j, val2) {
                jQuery.each(keyList, function (k, val3) {
                    if (val3.colname === val2) {
                        if (val3.colvalue === val[val2]) {
                            nowStatus = true;
                        } else {
                            nowStatus = false;
                            return false;
                        }
                    }
                });
                if (!nowStatus) return false;
            });
            if (nowStatus) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowStatus) {
            return nowVal;
        }
    }
    else if (level === 4) {
        colKey = keyList[0].colname;
        colKey2 = keyList[1].colname;
        colKey3 = keyList[2].colname;
        colKey4 = keyList[3].colname;

        colVal = keyList[0].colvalue;
        colVal2 = keyList[1].colvalue;
        colVal3 = keyList[2].colvalue;
        colVal4 = keyList[3].colvalue;

        jQuery.each(dataList, function (i, val) {
            if (val[colKey] !== "*" && val[colKey2] !== "*" && val[colKey3] !== "*" && val[colKey4] !== "*") {
                refData.push(val);
            } else {
                refData2.push(val);
            }
        });

        jQuery.each(refData, function (i, val) {
            if (val[colKey] === colVal && val[colKey2] === colVal2 && val[colKey3] === colVal3 && val[colKey4] === colVal4) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowVal) return nowVal;
        //has *
        jQuery.each(refData2, function (i, val) {
            var tmpKey = [];// not * 
            if (val[colKey] !== "*") tmpKey.push(colKey);
            if (val[colKey2] !== "*") tmpKey.push(colKey2);
            if (val[colKey3] !== "*") tmpKey.push(colKey3);
            if (val[colKey4] !== "*") tmpKey.push(colKey4);

            if (tmpKey.length === 0) {
                nowVal = val.Stage;
                nowStatus = true;
                return false;
            }
            jQuery.each(tmpKey, function (j, val2) {
                jQuery.each(keyList, function (k, val3) {
                    if (val3.colname === val2) {
                        if (val3.colvalue === val[val2]) {
                            nowStatus = true;
                        } else {
                            nowStatus = false;
                            return false;
                        }
                    }
                });
                if (!nowStatus) return false;
            });
            if (nowStatus) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowStatus) {
            return nowVal;
        }
    }
    else if (level === 5) {
        colKey = keyList[0].colname;
        colKey2 = keyList[1].colname;
        colKey3 = keyList[2].colname;
        colKey4 = keyList[3].colname;
        colKey5 = keyList[4].colname;

        colVal = keyList[0].colvalue;
        colVal2 = keyList[1].colvalue;
        colVal3 = keyList[2].colvalue;
        colVal4 = keyList[3].colvalue;
        colVal5 = keyList[4].colvalue;

        jQuery.each(dataList, function (i, val) {
            if (val[colKey] !== "*" && val[colKey2] !== "*" && val[colKey3] !== "*" && val[colKey4] !== "*" && val[colKey5] !== "*") {
                refData.push(val);
            } else {
                refData2.push(val);
            }
        });

        jQuery.each(refData, function (i, val) {
            if (val[colKey] === colVal && val[colKey2] === colVal2 && val[colKey3] === colVal3 && val[colKey4] === colVal4 && val[colKey5] === colVal5) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowVal) return nowVal;
        //has *
        jQuery.each(refData2, function (i, val) {
            var tmpKey = [];// not * 
            if (val[colKey] !== "*") tmpKey.push(colKey);
            if (val[colKey2] !== "*") tmpKey.push(colKey2);
            if (val[colKey3] !== "*") tmpKey.push(colKey3);
            if (val[colKey4] !== "*") tmpKey.push(colKey4);
            if (val[colKey5] !== "*") tmpKey.push(colKey5);

            if (tmpKey.length === 0) {
                nowVal = val.Stage;
                nowStatus = true;
                return false;
            }
            jQuery.each(tmpKey, function (j, val2) {
                jQuery.each(keyList, function (k, val3) {
                    if (val3.colname === val2) {
                        if (val3.colvalue === val[val2]) {
                            nowStatus = true;
                        } else {
                            nowStatus = false;
                            return false;
                        }
                    }
                });
                if (!nowStatus) return false;
            });
            if (nowStatus) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowStatus) {
            return nowVal;
        }

    }
    else if (level === 6) {
        colKey = keyList[0].colname;
        colKey2 = keyList[1].colname;
        colKey3 = keyList[2].colname;
        colKey4 = keyList[3].colname;
        colKey5 = keyList[4].colname;
        colKey6 = keyList[5].colname;


        colVal = keyList[0].colvalue;
        colVal2 = keyList[1].colvalue;
        colVal3 = keyList[2].colvalue;
        colVal4 = keyList[3].colvalue;
        colVal5 = keyList[4].colvalue;
        colVal6 = keyList[5].colvalue;

        jQuery.each(dataList, function (i, val) {
            if (val[colKey] !== "*" && val[colKey2] !== "*" && val[colKey3] !== "*" && val[colKey4] !== "*" && val[colKey6] !== "*" && val[colKey6] !== "*") {
                refData.push(val);
            } else {
                refData2.push(val);
            }
        });

        jQuery.each(refData, function (i, val) {
            if (val[colKey] === colVal && val[colKey2] === colVal2 && val[colKey3] === colVal3 && val[colKey4] === colVal4 && val[colKey5] === colVal5 && val[colKey6] === colVal6) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowVal) return nowVal;
        //has *
        jQuery.each(refData2, function (i, val) {
            var tmpKey = [];// not * 
            if (val[colKey] !== "*") tmpKey.push(colKey);
            if (val[colKey2] !== "*") tmpKey.push(colKey2);
            if (val[colKey3] !== "*") tmpKey.push(colKey3);
            if (val[colKey4] !== "*") tmpKey.push(colKey4);
            if (val[colKey5] !== "*") tmpKey.push(colKey5);
            if (val[colKey6] !== "*") tmpKey.push(colKey6);


            if (tmpKey.length === 0) {
                nowVal = val.Stage;
                nowStatus = true;
                return false;
            }
            jQuery.each(tmpKey, function (j, val2) {
                jQuery.each(keyList, function (k, val3) {
                    if (val3.colname === val2) {
                        if (val3.colvalue === val[val2]) {
                            nowStatus = true;
                        } else {
                            nowStatus = false;
                            return false;
                        }
                    }
                });
                if (!nowStatus) return false;
            });
            if (nowStatus) {
                nowVal = val.Stage;
                return false;
            }
        });
        if (nowStatus) {
            return nowVal;
        }
    }

}

// 初始化funAjccArr
// funName: [json] js
// reName: [json] ref
// column: 傳入當前biding的columnName
// para: [json] parameters
function findAjccArrScript(funName, reName, column, para) {
    let ajccResult = -1;
    let checkPush = -1;
    jQuery.each(funAjccArr, function (i, obj) {
        if (obj.name === funName) {
            ajccResult = 0;
            jQuery.each(obj.bindName, function (idx, ajccArr) {
                if (ajccArr === column) {
                    checkPush = 0;
                    return false;
                }
            });
            if (checkPush === -1) {
                obj.bindName.push(column);
            }

        } else if (obj.name === reName) {
            ajccResult = 0;
            obj.changeName = column;
        }
    });

    if (ajccResult === -1) {
        if (funName !== "setAjccStage") {
            let para2 = para.split(','); // para: "ajccStage,★T,★N,★M"
            var paraObj = { "tabName": null, "para": [] };
            jQuery.each(para2, function (pidx, pval) {
                if (pidx === 0) {
                    paraObj.tabName = pval; // pval: ajccStage
                } else {
                    var pObj = { "colname": pval, "colvalue": null }; // pval: ★T or ★N or ★M
                    paraObj.para.push(pObj);
                }
            });
            var funObj = {
                "name": funName,
                "remark": reName,
                "bindName": [column],
                "changeName": "",
                "parameters": paraObj
            };
            funAjccArr.push(funObj);
        }
    }
}

//新增時需要fun

function checkVersion(el) {
    var elDate = el.value;
    //var reg = /^[1-2]\d{3}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/;

    //var regExp = new RegExp(reg);
    //if (!regExp.test(elDate)) {
    //    alert('初診斷日期不符合格式,範例格式20180101');
    //    errorMsgStatus = 1;
    //    return;
    //} else {
    //    errorMsgStatus = 0;
    getvalue();


    firstVisitDate = elDate;
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "AJCC005",
        "data": {
            "docfun": ajccType,
            "firstDiaDate": elDate,
            "docver": docVersion,
            "Cluster": cluster,
            "spec": $("#spec").val()
        }
    };

    //call api
    getTemplateJson('#divbody', `${getApiUrl()}${path_API_Version}main-access`, JSON.stringify(obj), renderDom);

    //}

}

function getTemplateJson(dom, url, verObj, cb) {
    Spinner.show();
    console.log(verObj);
    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: verObj,
        error: function (xhr, status, errorThrown) {
            var err = "Status: " + status + " " + errorThrown;
            console.log(err);
            setSysMsg(err, 'danger');
            Spinner.hide();
        },
        success: function (response) {

            var result = JSON.stringify(response);
            if (response.rtnCode === 0) {//不改變 繼續填寫表單
                console.log('不改變 繼續填寫表單');
                Spinner.hide();
            } else if (response.rtnCode === 1) {//重新繪製HTML
                if (response.message === "查無此資料") {
                    alert(response.message);
                    window.location.href = `../AJCC/${cluster}?cluster=${cluster}&ajccType=${ajccType}&spec=${masterSpec}`;
                }
                console.log('重新繪製HTML');

                reloadPara();
                result = JSON.parse(response.jsonData.data);
                mapperData = result;
                docVersion = result.data[0].docver;
                json = result;
                console.log(json);
                json.data[0].docdetail[1].data = firstVisitDate;
                cb(dom, result, renderScript);
            } else {
                //顯示錯誤訊息
                console.log('顯示錯誤訊息');

                FixSysMsg.danger(response.message);
                Spinner.hide();
            }

        }
    });
}

function reloadPara() {
    $('#divbody').html('');
    if ($(".btn_group_list_fixed").length > 0) $(".btn_group_list_fixed").empty(); //清除FIXED BUTTON
    if ($("#cqs-button-holder").length > 0) $("#cqs-button-holder").empty();       //清除TopButton
    //清除Tab
    if ($("#cqs-tabnav-holder_Master").length > 0) $("#cqs-tabnav-holder_Master").empty();
    if ($("#cqs-tabnav-holder_Detail").length > 0) $("#cqs-tabnav-holder_Detail").empty();
    if ($(".page-slide-content").length > 0) $(".page-slide-content").empty();     //清除Slide內容
    $("script.reload").each(function () { $(this).remove(); });
    json = { "data": [{ "docdetail": [] }] };
    dataIdx = 0;
    rowIdx = 0;
    valueIdx = 0;
    arr1 = [];
    arr2 = [];
    docVersion = null;
    funAjccArr = [];

//    selectOptionsObj["AJCCFormShow"] = [];
//    var $tnm;
//    "Clinical|Pathologic|AfterTrtClinical|AfterTrtPathologic".split("|").forEach(function (val) {
//        ARR_TNM.forEach(function (tnm) {
//            $tnm = selectOptionsObj[`${sAJCC}${val}Stage__${tnm}`];
//            if ($tnm && $tnm !== "") $tnm = "";
//        });
//    });
    sAJCC = "";

    //清除[增加BTN(+)]參數
    addBtnBlockArr = [];

    //清除檢核
    Required_Fields = [];
    Required_Titles = [];
    ValidFormat_Fields = [];
    ValidFormat_Titles = [];

    //清除knockout綁定物件
    ko.cleanNode(document.getElementById("divbody"));

    //診療計畫書修改時因讀取[初診斷日]來判斷TNMStage時，需先清除，不然會錯誤
    if (sDiagnosisDate !== null) delete selectOptionsObj["IsReadonlyPersonalList"];
    if (typeof formShow !== "function") formShow = "formShow";
}

function dateVali(el) {
    var elDate = el.value;
    var reg = /^[1-2]\d{3}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/;

    var regExp = new RegExp(reg);
    if (!regExp.test(elDate)) {
        alert('填寫日期不符合格式,範例格式20180101');
        errorMsgStatus = 1;
        return;
    } else {
        errorMsgStatus = 0;
    }
}

//render querdetail 顯示部分資料
var queryDetailJson = { "data": [{ "docdetail": [] }] };
var queryDetailDataIdx = 0;
var queryDetailRowIdx = 0;
var queryDetailValueIdx = 0;
var queryDetailArr1 = []; //delete
var queryDetailArr2 = []; //delete

queryDetailRenderDom = function (dom, result, callback2) {
    var body = $(dom);

    jQuery.each(result.data, function (i, renderType) {
        if (renderType.datatype === "master") {
            jQuery.each(result.data[i].docdetail, function (idx, val) {
                queryDetailRowIdx = idx;
                body.append(queryDetailJson2HTML(val, "json.data[" + queryDetailDataIdx + "].docdetail[" + queryDetailRowIdx + "]") + "<hr>");

                if (idx === result.data[0].docdetail.length - 1)
                    callback2();
            });

        }
    });
};

queryDetailJson2HTML = function (element, parentname) {
    var html = "";
    var obj;
    var tmpObj; //delete
    if (element.segmentation === "title") {
        obj = $("<header></header>");
        obj.append("<h1>" + element.data + "</h1>");
    }
    else if (element.method === "text") {
        obj = $("<div>");
        tmpObj = getQueryDetailColumnName();
        obj.append(' <span data-bind="text:' + tmpObj + '" ></span>');
        arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPathQueryDetail(parentname) + ".data);");
    }
    else if (element.method === "radio") {
        obj = $(`<div></div>`);
        var block = $("<blockquote></blockquote>");

        if (element.segmentation) {
            obj.append('<label>' + element.segmentation + ':</label><br />');
            tmpObj = getQueryDetailColumnName();
            jQuery.each(element.option, function (i, val) {
                if (val.item === element.data) {
                    block.append('<span data-bind="text: ' + tmpObj + '">' + val.item + '</span>');
                    if (val.follow) {
                        jQuery.each(val.follow, function (f, fobject) {
                            if (fobject.method === "text") {
                                block.append(queryDetailJson2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]"));
                            }
                            else {
                                block.append("<blockquote>" + queryDetailJson2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]") + "</blockquote>");
                            }
                        });
                    }
                    block.append("<br />");
                }
            });
            obj.append(block);

            arr1.push("self." + tmpObj + " = ko.observable(items[" + queryDetailRowIdx + "].data);");
        }
        else {
            if (element.option[0].item) {
                //直向
                tmpObj = getQueryDetailColumnName();
                jQuery.each(element.option, function (i, val) {
                    if (val.item === element.data) {
                        block.append('<span data-bind="text: ' + tmpObj + '">' + val.item + '</span>');
                        if (val.follow) {
                            jQuery.each(val.follow, function (f, fobject) {
                                if (fobject.method === "text") {
                                    block.append(Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]"));
                                }
                                else {
                                    block.append("<blockquote>" + queryDetailJson2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]") + "</blockquote>");
                                }
                            });
                        }
                        block.append("<br />");
                    }
                });
                obj.append(block);

                arr1.push(`self.${tmpObj}=ko.observable(items[${queryDetailRowIdx}].data);`);
            }
            else {
                //橫向
                obj.append('<label>' + element.item + ' </label>');
                tmpObj = getQueryDetailColumnName();
                obj.append('<span data-bind="text: ' + tmpObj + '"></span> ');
                arr1.push(`self.${tmpObj}=ko.observable(${knockoutPathQueryDetail(parentname)}.data);`);
            }
        }
    }
    else if (element.method === "checkbox") {
        if (element.segmentation) {
            obj = $(`<div></div>`);
            obj.append(`<label>${element.segmentation}:</label>`);
            if (element.option) {
                jQuery.each(element.option, function (i1, optionItem) {
                    if (optionItem.checked) {
                        var tmpObj = getQueryDetailColumnName();
                        obj.append(`<span data-bind="text:${tmpObj}"></span>`);
                        if (optionItem.follow) {
                            jQuery.each(optionItem.follow, function (f, fobject) {
                                if (fobject.method === "radio") {
                                    obj.append("<blockquote>" + queryDetailJson2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]") + "</blockquote>");
                                }
                                else if (fobject.method === "checkbox") {
                                    obj.append("<blockquote>" + queryDetailJson2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]") + "</blockquote>");
                                }
                            });
                        }

                        obj.append('<br>');
                        arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPathQueryDetail(parentname) + ".option[" + i1 + "].item);");
                    }
                });
            }
        }
        else {
            obj = $(`<div></div>`);
            if (element.option) {
                jQuery.each(element.option, function (i1, optionItem) {
                    if (optionItem.checked) {
                        var tmpObj = getQueryDetailColumnName();
                        obj.append(`<span data-bind="text:${tmpObj}"></span>`);
                        if (optionItem.follow) {
                            jQuery.each(optionItem.follow, function (f, fobject) {
                                jQuery.each(optionItem.follow, function (f, fobject) {
                                    if (fobject.method === "text") {
                                        obj.append(queryDetailJson2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]"));
                                    }
                                });
                            });
                        }
                        obj.append('<br>');
                        arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPathQueryDetail(parentname) + ".option[" + i1 + "].item);");
                    }
                });
            }
        }
    }
    else {
        //textbox
        tmpObj = getQueryDetailColumnName();

        obj = $(`<div></div>`);
        obj.append(`<label>${element.segmentation}:</label>`);
        obj.append(`<span data-bind="text:${tmpObj}"></span>`);

        arr1.push(`self.${tmpObj} = ko.observable(items[${queryDetailRowIdx}].data);`);
    }

    html = obj.html();
    return html;
};

function getQueryDetailColumnName() {
    queryDetailValueIdx++;
    return "qdcolumn" + queryDetailValueIdx;
}

function knockoutPathQueryDetail(parentname) {
    var kpath = "";
    var parr = parentname.split(".");
    kpath = parr[2].replace("docdetail", "items");
    for (var i = 3; i < parr.length; i++) {
        kpath = kpath + "." + parr[i];
    }
    return kpath;
}

function renderScriptQueryDetail1() {
    //alert('111');

    var scriptObj = $("<script><\/script>");
    scriptObj.append("var SimpleListQueryDetailModel = function (items) {" + "\n");
    scriptObj.append("var self = this;" + "\n");
    scriptObj.append("self.items = ko.observableArray(items);" + "\n");

    jQuery.each(arr1, function (i, val) {
        scriptObj.append(val + "\n");
    });

    scriptObj.append("};" + "\n");
    scriptObj.append("var viewModel = new SimpleListQueryDetailModel(queryDetailJson.data[" + dataIdx + "].docdetail);" + "\n");

    scriptObj.append("ko.applyBindings(viewModel,document.getElementById('detailbody'));" + "\n");

    $('#detailbody').append(scriptObj);
    $('#box_form_detail').css('display', 'block');
}

// 去除多出來的冒號
function getValidResultListItems(jsonArr) {
    for (var i = 0; i < jsonArr.length; i++) {
        jsonArr[i] = jsonArr[i].replace(":", "").replace("：", "").trim();
    }
    return jsonArr;
}
