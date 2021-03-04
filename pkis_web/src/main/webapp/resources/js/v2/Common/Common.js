/*=== 全域變數 ===*/
const path_API_Version = ""; //v1/
const timeOutTime = 2000;
const oneDayInterval = 1000 * 60 * 60 * 24;
var FIX_QUERY_KEY_setToBackResult = "|附件資料|審核意見|查詢|";
var Reg_SetToBackResult_Key = "|COMM個案查詢|病歷查詢|";
var fixBlockid = "";

//顯示系統訊息
FixSysMsg = (function () {
    return {
        success: function (msg) {
            $('#alertBlock1').html(
                `<div align="center" class="myAlert-top-middle alert alert-success alert-dismissable fade in">` +
                `    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>` +
                `    <strong>${msg.replace(/(\r\n|\r|\n)/g, "<br />")}</strong>` +
                `</div >`
            );

            $(".alert-dismissable").fadeTo(2000, 500).slideUp(500, function () {
//                $(".alert-dismissable").alert('close');
            });
        },
        danger: function (msg) {
            var _pct = msg.match(/\-\>/g) ? "-middle" : "";

            $('#alertBlock1').html(
                `<div align="center" class="myAlert-top${msg.match(/【/gi) ? _pct : "-middle"} alert alert-danger alert-dismissable fade in">` +
                `    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>` +
                `    <strong>${msg.replace(/(\r\n|\r|\n)/g, "<br />")}</strong>` +
                `</div >`
            );
        },
        clear: function () {
            $('#alertBlock1').html("");
        },
        status: function () {
            return $('#alertBlock1').html() === "";
        }
    };
})();


//取儲存在用戶端的Token
function getToken() {
    var token = localStorage.getItem("token");
    if (token)
        return token;
    else {
        //會重複刷新(Todo)
        //alert('登入時效逾期，請重新登入');
        //window.location.href = '/';
        console.log("Token取得失敗！");
    }
}


//取儲存在用戶端的ID
function getUserId() {
    var userId = localStorage.getItem("userId");
    return userId ? userId : "";
}


//取儲存在用戶端的姓名
function getUserName() {
    var userName = localStorage.getItem("userName");
    return userName ? userName : "";
}


//取儲存在用戶端的群組名稱
function getGroupName() {
    var groupName = localStorage.getItem("groupName");
    return groupName ? groupName : "";
}


//取儲存在用戶端的虛擬目錄路徑
function getCQSVDN() {
    var cqsVDN = localStorage.getItem("cqsVDN");
    return cqsVDN ? cqsVDN : "";
}


//取診間串入的科別
function getBranch(data) {
    var _branch = "";
    data.forEach(function (res) {
        if (res.員編 === getUserId()) { _branch = res.科別; return false; }
    });

    return _branch;
}


//取診間串入的病歷號
function getChrtNo() {
    var chrtNo = localStorage.getItem("chrtNum");
    return chrtNo ? chrtNo : "";
}


//取得api位置
function getApiUrl() {
    return $('#skhcq_api_url').val();
}


//取得實際API連結
/**
 *
 * @param {string} val 預設為[main-access]，可依不同需求將參數帶入
 * @returns {string}
 **/
function getAPIURL(val) {
    var url = `${getApiUrl()}${path_API_Version}main-access`;
    return val !== undefined ? url.replace(/main-access/gi, val) : url;
}


//回傳knockout的modal位置
//parentname: eg. `json.data[${dataIdx}].docdetail[${rowIdx - 1}]`
function getKnockoutPath(parentname) {
    var kpath = "";
    var parr = parentname.split(".");
    kpath = parr[2].replace("docdetail", "items");
    for (var i = 3; i < parr.length; i++) {
        kpath = kpath + "." + parr[i];
    }
    return kpath;
}


//為Html元素賦值，並寫回json
function setBindingItemValue(itemId, newValue, parentName) {
    if (itemId !== undefined) {
        var newBindAttr = $(`#${itemId}`).attr("data-bind");
        var newBindName = "";
        
        if (newBindAttr !== undefined && newBindAttr !== "") {

            //有binding時，使用binding code賦值(有寫回json)
            if ($(`#${itemId}`).is("select")) {
                var tmpBindNameArr = newBindAttr.split(",");
                for (var x = 0; x < tmpBindNameArr.length; x++) {
                    if (tmpBindNameArr[x].indexOf("value") === 0) {
                        newBindName = tmpBindNameArr[x].split(":")[1].trim();
                        break;
                    }
                }
            } else {
                newBindName = newBindAttr.replace(/(checked|value|text)\:/gi, "").trim();

            }

            //console.log(`viewModel.${newBindName}(${newValue})`);
            if (viewModel !== undefined) {
                if (typeof newValue === "string")
                    eval(`viewModel.${newBindName}('${newValue.replace(/\n/g, "\\n").replace("'", "’")}');`);
                else
                    eval(`viewModel.${newBindName}(${newValue});`);
            }
        } else {
            //無binding時，僅使用jQuery賦值(未寫回json)
            //$(`#${itemId}`).val(newValue);
            console.log(`${itemId} => ${newValue}`);
            if (viewModel !== undefined && eval(`viewModel.${itemId}`) !== undefined) {
                if (typeof newValue === "string")
                	eval(`viewModel.${itemId}('${newValue.replace(/\n/g, "\\n")}');`);
            
                else
                    eval(`viewModel.${itemId}(${newValue});`);
            }
            else
                $(`#${itemId}`).val(newValue);
        }
    }
}


//回傳binding的json物件
//inputDom: Html的element object
//rtnObj: {"bindName":"column...", "bindType":"value"}
function getBindingObj(inputDom) {
    var rtnObj = {};

    if (inputDom !== undefined) {
        var $obj = $(inputDom);
        var newBindAttr = $obj.attr("data-bind");
        if (newBindAttr !== undefined && newBindAttr !== "") {
            var bindName = "", bindType = "";

            if ($obj.is("input:text") || $obj.is("input:radio") || $obj.is("input:checkbox") ||
                $obj.is("textarea") || $obj.is("label")) {
                bindName = newBindAttr.replace(/(checked|value|text)\:/gi, "").trim();
                bindType = newBindAttr.match(/(checked|value|text)\:/gi)[0].replace(":", "");

                rtnObj["bindName"] = bindName;
                rtnObj["bindType"] = bindType;
                if (viewModel && typeof eval(`viewModel.${bindName}`) === "function") {
                    rtnObj["bindVal"] = eval(`viewModel.${bindName}()`);
                }
            }
            else if ($obj.is("select")) {
                var tmpBindNameArr = newBindAttr.split(",");
                for (var x = 0; x < tmpBindNameArr.length; x++) {
                    if (tmpBindNameArr[x].indexOf("value") === 0) {
                        bindName = tmpBindNameArr[x].split(":")[1].trim();
                        bindType = "value";
                        break;
                    }
                }
                rtnObj["bindName"] = bindName;
                rtnObj["bindType"] = bindType;
                if (viewModel && typeof eval(`viewModel.${bindName}`) === "function") {
                    rtnObj["bindVal"] = eval(`viewModel.${bindName}()`);
                }
            }
        }
    }
    return rtnObj;
}


//將表單內容轉成json
function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}


//對傳入的Html元素集合，逐一還原預設Binding值
function resetBindingItems(inBindingItems) {
    $.each(inBindingItems, function (i, item) {
        var tmpEmptyVal = "";
        var tmpBindName = $(item).attr("data-bind");
        if (tmpBindName !== undefined) {
            if (tmpBindName.indexOf("optionsCaption") > -1) {
                //select
                tmpEmptyVal = "";
                var tmpBindNameArr = tmpBindName.split(",");
                for (var x = 0; x < tmpBindNameArr.length; x++) {
                    if (tmpBindNameArr[x].indexOf("value") === 0) {
                        tmpBindName = tmpBindNameArr[x].split(":")[1].trim();
                        break;
                    }
                }
            }
            else {
                tmpEmptyVal = "";
                //input:text,checkbox,radio
                tmpBindName = tmpBindName.replace(/(checked|value|text)\:/gi, "").trim();
            }

            if ($(item).attr("type") === "checkbox")
                eval(`viewModel.${tmpBindName}(false);`);
            else
                eval(`viewModel.${tmpBindName}("${tmpEmptyVal}");`);
        }
        else {
            $.each($(item).find("input"), function (j, obj) {
                tmpBindName = $(obj).attr("data-bind");
                if (tmpBindName !== undefined && tmpBindName.indexOf("optionsCaption") === -1) {
                    //input:text,checkbox,radio
                    tmpBindName = tmpBindName.replace(/(checked|value|text)\:/gi, "").trim();
                    if ($(obj).attr("type") === "checkbox")
                        eval(`viewModel.${tmpBindName}(false);`);
                    else
                        eval(`viewModel.${tmpBindName}("");`);
                }
            });
            $.each($(item).find("select"), function (j, obj) {
                tmpBindName = $(obj).attr("data-bind");
                if (tmpBindName !== undefined) {
                    tmpEmptyVal = "";
                    var tmpBindNameArr = tmpBindName.split(",");
                    for (var x = 0; x < tmpBindNameArr.length; x++) {
                        if (tmpBindNameArr[x].indexOf("value") === 0) {
                            tmpBindName = tmpBindNameArr[x].split(":")[1].trim();
                            break;
                        }
                    }
                    eval(`viewModel.${tmpBindName}("${tmpEmptyVal}");`);
                }
            });
            $.each($(item).find("textarea"), function (j, obj) {
                tmpBindName = $(obj).attr("data-bind");
                if (tmpBindName !== undefined) {
                    //input:text,checkbox,radio
                    tmpBindName = tmpBindName.replace(/(checked|value|text)\:/gi, "").trim();
                    eval(`viewModel.${tmpBindName}("");`);
                }
            });
        }
    });
}


//依"字串(lblText)"取得對應label後的Html控制項值
function getItemByMappingLabel(lblText, isNeedHandle) {
    var rtnVal = $(`#${lblText}`);
    if (rtnVal.length === 0) {
        rtnVal = $(`label:contains('${lblText}')`).next("input,select");
        if (rtnVal.length === 0) {
            rtnVal = $(`label:contains('${lblText}')`).next("div").find("input,select");
        }
    }
    if (rtnVal.length > 0) {
        var tmpValue = "";
        if (lblText.indexOf("收案類別") > -1 && isNeedHandle === true) {
            tmpValue = rtnVal.val();
            if (tmpValue.match(/[\u4e00-\u9fa5]+/gi) !== null)
                tmpValue = tmpValue.match(/[\u4e00-\u9fa5]+/gi)[0];
            if (tmpValue !== undefined && selectOptionsObj.癌別) {
                $.each(selectOptionsObj.癌別, function (i, obj) {
                    if (obj.癌別中文 && obj.癌別中文 === tmpValue) {
                        tmpValue = obj.癌別代碼;
                        return false;
                    }
                });
            }
            if (tmpValue) rtnVal = tmpValue;
        }
        else if (lblText.indexOf("主治醫師") > -1 && rtnVal.val() === "") {
            SetMainProcessPara("getLocalStorage", { "key": "userName" });
            rtnVal = MainProcess();
            if (rtnVal.indexOf("(") > -1) rtnVal = rtnVal.replace(/\(.+\)/g, "");
        }
        else
            rtnVal = rtnVal.val();
    }

    return rtnVal;
}


//登出
function logout() {
    Spinner.show();
    var eipUrl = $('#EIP_URL').val();
    var obj = {
        "token": getToken(),
        "accessCode": "AUTH002"
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Auth/Logout"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            console.log(response);

            if ("|0|90|".indexOf(`|${response.rtnCode}|`) > -1) { //90:登入無效，請重新登入
                if (response.rtnCode === 90) console.log(response.message);
                localStorage.clear();
                sessionStorage.clear();
                window.location.href = eipUrl === "" ? "about:blank" : eipUrl;
                //window.close();
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


//將傳入參數判斷小於10則補0
function getCurrVal(val) {
    return `${(val < 10 ? '0' : '')}${val}`;
}


//取當天日期
function getToDay(type) {
    var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var day = d.getDate();
    var hour = d.getHours();
    var minute = d.getMinutes();
    var second = d.getSeconds();

    return getCurrDateTime(type, year, month, day, hour, minute, second);
}


//取得正確日期(加時間)
function getCurrDateTime(type, year, month, day, hour, minute, second) {
    var result;

    switch (type) {
        case -2:
            result = `${year}`;
            break;
        case -1:
            result = `${year}${getCurrVal(month)}`;
            break;
        case 0:
            result = `${year}${getCurrVal(month)}${getCurrVal(day)}`;
            break;
        case 1:
            result = `${year}-${getCurrVal(month)}-${getCurrVal(day)}`;
            break;
        case 2:
            result = `${year}/${getCurrVal(month)}/${getCurrVal(day)}`;
            break;
        case 3:
            result = `${year}-${getCurrVal(month)}-${getCurrVal(day)}T${getCurrVal(hour)}:${getCurrVal(minute)}`;
            break;
        case 4:
            result = `${year}-${getCurrVal(month)}-${getCurrVal(day)}${getCurrVal(hour)}${getCurrVal(minute)}${second}`;
            break;
        case 5: //民國年(YYYMMDD)
            var chYYY = parseInt(year) - 1911;
            result = `${padLeft(chYYY.toString(), "0", 3)}${getCurrVal(month)}${getCurrVal(day)}`;
            break;
    }

    return result;
}


//設定當天日期
function setSysDate(targetId, type) {
    if (targetId !== undefined) {
        var tmpSysdate = getToDay(type);
        setBindingItemValue(targetId, tmpSysdate);
    }
}


//依傳入日期，取得對應格式的日期字串
function getFormatDateStr(type, dateStr, intcode) {
    var currDate;
    if (dateStr) currDate = dateStr.length === 8 ? [dateStr.slice(0, 4), "-", dateStr.slice(4, 6), "-", dateStr.slice(6, 8)].join('') : dateStr;

    var d = currDate && !isNaN(new Date(currDate)) ? new Date(currDate) : new Date();
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var day = d.getDate();
    var hour = d.getHours();
    var minute = d.getMinutes();
    var second = d.getSeconds();

    if (intcode !== undefined) {
        var mod, nums = intcode.match(/\d+/)[0];
        if (intcode.match(/^BF/)) {
            switch (intcode.substr(-1)) {
                case "S": //Season
                    mod = month % 3;
                    if (mod > 0) month += 3 - mod; //算出目前為第幾季(3、6、9、12)
                    month -= nums * 3;             //算出正確的季份與是否年份要減1
                    if (month < 1) {
                        month = 12 - Math.abs(month);
                        year -= 1;
                    }

                    break;
            }
        }
    }

    return getCurrDateTime(type, year, month, day, hour, minute, second);
}


//設定日曆元件格式
function setDatePickerFormat(inId, inFormat, dVersion, baseId) {
    var $dItem, $bItem;
    if (typeof inId === "string") $dItem = $(`#${inId}`);
    else if (typeof inId === "object") $dItem = $(inId);

    if (typeof baseId === "string") $bItem = $(`#${baseId}`);
    else if (typeof baseId === "object") $bItem = $(baseId);

    var dFormat = "yymmdd", dView = "days", dViewMode = "days";
    if (inFormat && inFormat.match(/#[0-9]{1,2}D/g)) {
        var tmpDF = inFormat.match(/[0-9]{1,2}/g)[0];

        switch (tmpDF) {
            case "4":
                dFormat = "yyyy";
                dView = "years";
                dViewMode = "years";
                break;
            case "6":
                dFormat = "yyyymm";
                dView = "months";
                dViewMode = "months";
                break;
            case "8":
                dFormat = "yyyymmdd";
                break;
			case "10":
                dFormat = "yyyy/mm/dd";
                break;
        }
    }

    if ($dItem.length > 0) {
        var _afterMay01 = false, _notfutrue = false;


        $dItem.datepicker({
            format: dFormat,
            startView: dView,
            minViewMode: dViewMode,
            container: "body",
            autoclose: true,
            clearBtn: true,
            todayBtn: 'linked',
            todayHighlight: true,
            forceParse: false,
            language: 'zh-TW',
        });
    }
}


function setMultiSelectFormat(inId) {
    if ($(`"#${inId}"`).length > 0) {
        $(`"#${inId}"`).multiselect({
            allSelectedText: "已全選",
            nonSelectedText: '尚未選擇',
            onChange: function (element, checked) {
                var brands = $(`#${inId} option:selected`);
                var selected = [];
                $(brands).each(function (index, brand) {
                    selected.push([$(this).val()]);
                });

                console.log(selected);
            }
        });
    }
}


function setMultiSelectValue(obj, inId) {
    console.log($(obj).val());
    console.log($(obj).val().join('、'));
    setBindingItemValue(inId, $(obj).val().join('、'));
}


//取AJCC版本
function getAJCC_Version(dateStr) {
    var sAJCC = "";
    if (dateStr === undefined || dateStr === "") {
        SetMainProcessPara("getLocalStorage", { "key": "初診斷日期" });
        dateStr = MainProcess();

        //若是無[初診斷日期]時透過[#SOURCE]來取得(個管使用)
        if (dateStr === undefined) {
            SetMainProcessPara("getLocalStorage", { "key": "#SOURCE" });
            dateStr = MainProcess();

            if (dateStr !== undefined) {
                dateStr = JSON.parse(dateStr).初診斷日期;
                sDiagnosisDate = dateStr;
            }

            //使用jsonPath取得初診斷日期
            //$.data[?(@.datatype == '基本資料')].docdetail[?(/初診斷日/.test(@.displayname) && @.uitype == 'label')].value
            //var path = $.JSONPath({ data: json, keepHistory: false });
            //var qryRS = path.query(`$.data[?(@.datatype == '基本資料')].docdetail[?(/初診斷日/.test(@.displayname) && @.uitype == 'label')].value`)[0];
            //if (qryRS !== null) dateStr = qryRS;
        }
    }
    if (dateStr !== "") {
        var sVal = parseInt(getFormatDateStr(0, dateStr));
        if (sVal <= 20091231) sAJCC = "AJCC6";
        else if (sVal >= 20180101) sAJCC = "AJCC8";
        else sAJCC = "AJCC7";
    }
    else sAJCC = "AJCC8";
    return sAJCC;
}


//判斷是否為數字
function getStringAsNumber(text) {
    var num_g = text.match(/\d+/);
    if (num_g !== null) {
        return num_g[0];
    } else {
        return;
    }
}

function padLeft(str, padStr, lenght) {
    if (str.length >= lenght)
        return str;
    else
        return padLeft(padStr + str, lenght);
}

function padRight(str, padStr, lenght) {
    if (str.length >= lenght)
        return str;
    else
        return padRight(str + padStr, lenght);
}


/**
 * 回傳指定物件內是否有傳入的屬性(true/false)
 * @param {any} obj   物件
 * @param {any} prop  屬性名稱
 * @returns {boolean} true/false
 */
function chkObjectProperty(obj, prop) {
    return obj.hasOwnProperty(prop);
}


/**
 * 判斷bindVal是否為不存在、空值或請選擇
 * @param {any} obj   檢核物件
 * @returns {boolean} true/falsetrue/false
 */
function checkBindValIsEmpty(obj) {
    return obj["bindVal"] === undefined || obj["bindVal"] === "" || obj["bindVal"] === "請選擇" || obj["bindVal"] === "請選擇(請選擇)" || obj["bindType"] === "checked" && !obj["bindVal"] ? true : false;
}


/**
 * 遞迴方式取得物件最終bind Id
 * @param {any} obj  檢核物件
 * @returns {string} 物件ID
 */
function getFinalObjectID(obj) {
    var $obj = $(obj);
    var $id = $obj.is("label[for]") ?
        $obj.attr("for") :
        $obj.attr("data-bind").replace(/(checked|value|text)\:/gi, "").trim();

    if ($obj.is("label[for]"))
        $id = getFinalObjectID(`#${$id}`);
    else if ($obj.attr("id") !== $id)
        $id = getFinalObjectID(`#${$id}`);

    return $id;
}


//20191122 雲端
function CloseWin() {
    closeWin();
}

//關閉視窗
function closeWin(obj) {
    if (obj === undefined) {
        obj = $(window.parent.document).find("[id*='iframe_']");
    }
    try {
        if (obj.length > 0) {
            obj.each(function (i, item) {
                $(item).html('');
                $(item).parent().fadeOut();
            });

            //將設定清空
            $(window.parent.document).find("#PatCluster").val("");
            $(window.parent.document).find("#PatAjcctype").val("");
            $(window.parent.document).find("#PatSpec").val("");

            //個管審查-確認派案處理
            if (ajccType === "QRY" && masterSpec === "ASSIGNDOCTOR") {
                SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_Dataid, "isClearAll": "false" });
                MainProcess();
                SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_IFrameParent, "isClearAll": "false" });
                MainProcess();
                SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_RtnJson, "isClearAll": "false" });
                MainProcess();
            }
        }
    } catch (e) {
        console.log(e);
    }
}


/**
 * 將所傳入的byte[]轉成檔案下載(window.atob 在 IE 瀏覽器不支援)
 * @param {any} base64    base64編碼
 * @returns {ArrayBuffer} 檔案的Uint8Array
 */
function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
        var ascii = binaryString.charCodeAt(i);
        bytes[i] = ascii;
    }
    return bytes;
}


//展開TODO
function showTodo() {
    $('#skhcq_header_left_todo').css('display', 'block');
    $('#todo_btn').css('display', 'none');
}


//隱藏TODO
function hideTodo() {
    $('#skhcq_header_left_todo').css('display', 'none');
    $('#todo_btn').css('display', 'block');
}


/**
 * binding資料並儲存至LocalStorage
 */
function saveJSONToLocalStorage() {
    getvalue();
    SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(json) });
    MainProcess();
}


/**
 * Tab切換重新繪製HTML
 * @param {any} clickType 目的Tab名稱(Tab點擊時才會有值)
 */
function changeTabToRenderHTML(clickType) {
    dataType = clickType;
    SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_EntireJson });
    var newJson = JSON.parse(MainProcess());

    reloadPara(); //清空所有參數
    json = $.extend(true, {}, newJson);
    docVersion = json.data[0].docver;
    console.log(`=============== 重新繪製[${clickType}]HTML ===============`);
    renderDom("#divbody", json, renderScript);
}


/**
 * 呈現回原Tab頁籤
 * @param {any} currTab   目前Tab名稱(Tab點擊時才會有值)
 */
function returnTabToCurrent(currTab) {
    setTimeout(function () {
        Spinner.hide();
        $(`ul.nav.nav-tabs li:contains('${currTab}') a`).click();
    }, timeOutTime);
}

/**
 * 判斷所需輸入的欄位均不可為空值
 * @param {any} fieldList 欄位列表
 * @returns {string}      未輸入值的欄位清單
 */
function checkReqField(fieldList) {
    var alrMsg = "", reqParas;
    if (typeof fieldList === "string") {
        fieldList = fieldList.replace(/required./gi, "");

        if (fieldList.indexOf(",") > -1)
            reqParas = fieldList.split(",");
        else if (fieldList.indexOf("||") > -1)
            reqParas = fieldList.split("||");
        else
            reqParas = [fieldList];
    }
    else
        reqParas = fieldList;

    reqParas.forEach(function (item) {
        var chkItem = $(`#${item.replace(/\(/gi, "\\(").replace(/\)/gi, "\\)")}`), tmpBindObj;

        if (item.indexOf('初診斷日') > -1) {
            var _msg = "";
            if (chkItem.length === 0) chkItem = $(`label:contains('${item}')`).parent().find(":input");

            $.each(chkItem, function (i, val) {
                tmpBindObj = getBindingObj(val);
                if (tmpBindObj && checkBindValIsEmpty(tmpBindObj))
                    _msg += `、${item}_${i === 0 ? "起" : "迄"}`;
            });

            if (_msg !== "") alrMsg += _msg;
        }
        else if (item.match(/收案來源|收案職類/)) {
            if (chkItem.length === 0) chkItem = $(`label:contains('${item}')`).next();

            tmpBindObj = getBindingObj(chkItem[0]);
            if (tmpBindObj) {
                if (checkBindValIsEmpty(tmpBindObj))
                    alrMsg += `、${item}`;
                else if (tmpBindObj.bindVal === "其他") {
                    //腫瘤心理-新收時判斷
                    chkItem = $(`label:contains('${item}')`).parent().next("div").find(":input");

                    tmpBindObj = getBindingObj(chkItem[0]);
                    if (tmpBindObj && checkBindValIsEmpty(tmpBindObj))
                        alrMsg += `、${item}：其他`;
                }
            }
        }
        else {
            if (chkItem.length === 0) {
                chkItem = $(`label:contains('${item}')`);

                if (chkItem.attr("fixblock") === undefined) {
                    if (cluster.match(/CEM|HINTS|RPT|REV|DM|MAINT/)) {
                        if (masterSpec.match(/AUDITLIST|AGESTATISTICS|CANCERYEAR|ANNUALSTATISTICS|POINTER|SERVICECOUNT/))
                            chkItem = $(`label:contains('${item}')`).next();
                        else if (cluster === "MAINT" && ajccType === "NEW" && masterSpec === "REC")
                            chkItem = $(`label:contains('${item}')`).next();
                        else
                            chkItem = $(`label:contains('${item}')`).next("div").find("[data-bind]:first");
                    }
                    else if (cluster.match(/PS|ORAL|MMG|COLON/)) { //子抹、口腔、腸篩 => 篩檢機構檢核使用
                        chkItem = $(`label:contains('${item}')`).next();
                        if (chkItem.length === 0)
                            chkItem = $(`label:contains('${item}')`).parent().next("div").find("[data-bind]:first");
                    }
                    else
                        chkItem = $(`label:contains('${item}')`).next();
                }
                else {
                    chkItem = chkItem.next();
                    if (chkItem.length === 0 || chkItem.is("label"))
                        chkItem = $(`label:contains('${item}')`).parent().next("div").children(":first");
                }
            }

            tmpBindObj = getBindingObj(chkItem[0]);
            if (tmpBindObj && checkBindValIsEmpty(tmpBindObj))
                alrMsg += `、${item}`;
        }
    });

    if (alrMsg !== "" && alrMsg.split('、').join().length > 1) {
        FixSysMsg.danger(`請輸入：${alrMsg.substr(1)}！`);
        return false;
    }

    return true;
}

/**
 * 20191122 雲端
 * @param {any} idVal QQ
 * @returns {any}     XXXX
 */
function CheckIdNumber(idVal) {
    var ret = false;
    var r = /^[0-9]*$/;
    //第二個字母是否為數字
    var checkChar = idVal.substr(1, 1);
    var r2 = /^[A-Za-z][12]\d{8}$/;

    //數字  身分證
    if (r.test(checkChar)) {
        if (r2.test(idVal))
            ret = true;
    }
    else {
        if (5 <= idVal.length && idVal.length <= 10)
            ret = true;
    }
    return ret;
}

/**
 * 判斷所需輸入的欄位擇一輸入即可
 * @param {any} fieldList 欄位列表
 * @returns {boolean} 真/假
 */
function checkReqFieldEitherOr(fieldList) {
    var alrMsg = "", reqParas, isChk = false;
    if (typeof fieldList === "string") {
        fieldList = fieldList.replace(/required./gi, "");

        if (fieldList.indexOf(",") > -1)
            reqParas = fieldList.split(",");
        else if (fieldList.indexOf("||") > -1)
            reqParas = fieldList.split("||");
        else
            reqParas = [fieldList];
    }
    else
        reqParas = fieldList;

    reqParas.forEach(function (item) {
        if (!isChk) {
            var chkItem = $(`#${item.replace(/\(/gi, "\\(").replace(/\)/gi, "\\)")}`), tmpBindObj;

            if (item.indexOf('醫師') > -1) {
                if (chkItem.length === 0) chkItem = $(`label:contains('${item}')`).next("div").find("select:last");

                tmpBindObj = getBindingObj(chkItem[0]);
                if (tmpBindObj && checkBindValIsEmpty(tmpBindObj))
                    alrMsg += `、${item}`;
                else {
                    isChk = true;
                    return false;
                }
            }
            else if (item.indexOf('日期') > -1) {
                var _msg = "";
                if (chkItem.length === 0) chkItem = $(`label:contains('${item}')`).next("div").find(":input");

                $.each(chkItem, function (i, val) {
                    tmpBindObj = getBindingObj(val);
                    if (tmpBindObj && checkBindValIsEmpty(tmpBindObj))
                        _msg += `、${item}_${i === 0 ? "起" : "迄"}`;
                });

                if (_msg === "") {
                    isChk = true;
                    return false;
                }
                else
                    alrMsg += _msg;
            }
            else {
                var _flag = false;
                if (chkItem.length === 0) chkItem = $(`label:contains('${item}')`).next("div").find("[data-bind]:first");

                if (module === "CS" && ajccType === "QRY" && chkItem.is("input:checkbox")) {
                    chkItem = $(`label:contains('${item}')`).next("div");

                    tmpBindObj = $(chkItem[0]).children().find("input").filter(function (i, item) {
                        return !checkBindValIsEmpty(getBindingObj(item));
                    });

                    if (tmpBindObj.length === 0)
                        alrMsg += `、${item}`;
                    else
                        _flag = true;
                }
                else {
                    tmpBindObj = getBindingObj(chkItem[0]);
                    if (tmpBindObj && checkBindValIsEmpty(tmpBindObj))
                        alrMsg += `、${item}`;
                    else
                        _flag = true;
                }

                if (_flag) {
                    isChk = true;
                    return false;
                }
            }
        }
    });

    if (isChk)
        return true;
    else if (alrMsg !== "") {
        alert(`${alrMsg.substr(1)} 請擇一輸入！`);
        return false;
    }
}


/**
 * 判斷無STAGE時顯示警告訊息
 * @param {any} stageTbName AJCC-TNM(ARR_TNM)等儲存的參數名稱
 * @param {any} target      Stage所要存入的ID
 * @param {any} targetName  目前Stage處理的名稱
 */
//function ShowMsgforSTAGEUnKnow(stageTbName, target, targetName) {
//    var _chkCnt = 0, _flag = true;
//    ARR_TNM.forEach(function (val) {
//        var $obj = selectOptionsObj[`${stageTbName}__${val}`];
//        if ($obj && $obj !== "") _chkCnt++;
//    });
//
//    if (stageTbName.indexOf("AfterTrt") > -1)
//        targetName += "預後";
//    else if (stageTbName.indexOf("AfterCure") > -1)
//        targetName += "治療後";
//
//    if ((stageTbName.match(/^AJCC8AfterTrt(Pathologic|Clinical)Stage/g) && _chkCnt === 7 ||
//        stageTbName.match(/^AJCC[\d]+(Pathologic|Clinical)Stage/g) && _chkCnt === 3) &&
//        selectOptionsObj[`${stageTbName}__Stage`] === "") {
//        setBindingItemValue(target, "");
//        FixSysMsg.danger(`AJCC對照表，無[${targetName}]相對應的Stage設定!`);
//        _flag = false;
//    }
//
//    if (_flag && !FixSysMsg.status()) FixSysMsg.clear();
//}


/**
 * 將字串由obj欄位中游標所在位置插入
 * @param {any} obj 目標物件
 * @param {any} str 所要插入的文字
 * @returns {any} 回傳插入後的整段文字
 */
function InsertStrToMousePos(obj, str) {
    if (document.selection) {
        obj.focus();
        var sel = document.selection.createRange();
        document.selection.empty();
        sel.text = str;
    } else {
        var prefix, main, suffix;
        prefix = obj.value.substring(0, obj.selectionStart);
        main = obj.value.substring(obj.selectionStart, obj.selectionEnd);
        suffix = obj.value.substring(obj.selectionEnd);
        obj.value = prefix + str + suffix;
    }
    obj.focus();

    return obj.value;
}


/**
 * 20191122 雲端 開啟視窗 原版是開啟[病歷資料查詢]子視窗 20190221-Ben
 * @param {any} title        A
 * @param {any} url          B
 * @param {any} parameters   C
 * @param {any} initFunction D
 * @param {any} height       E
 */
function OpenDialog(title, url, parameters, initFunction, height) {
    BoxDialog.show(title, url, parameters, initFunction, height);
}



/**
 * 取鄉鎮市區代碼API
 * @param {any} tgtCodeId   鄉鎮市區代碼ID
 * @param {any} target      Table ID
 * @param {any} requiredStr 需求字串
 * @param {any} isAsync     是否同步
 * @returns {Array}         回傳陣列
 */
function GetResidenceCode(tgtCodeId, target, requiredStr, isAsync) {
    if (isAsync === undefined) isAsync = true;
    var resPat, resCode = "";

    resCode = $(`#${tgtCodeId}`).val();
    if (resCode !== "") {
        Spinner.show();
        var queryObj = {
            "token": getToken(),
            "userId": getUserId(),
            "data": {
                "codeType": "鄉鎮市區代碼",
                "residenceCode": resCode
            }
        };
        queryObj = JSON.stringify(queryObj);
        console.log(queryObj);

        $.ajax({
            url: getAPIURL("HINTS/GetResidence"),
            data: queryObj,
            contentType: "application/json",
            type: "POST",
            dataType: 'json',
            async: isAsync,
            success: function (response) {
                if (response.rtnCode === 0) {
                    var resultArr = JSON.parse(response.jsonData.data);
                    var oTable;
                    if (target !== undefined) oTable = $(`${target}`).dataTable();
                    else oTable = $(`#json_query`).dataTable();

                    if (resultArr && resultArr.length > 0) {
                        console.log(resultArr);
                        resPat = resultArr;
                        var newJsonData = getHandledData(resultArr);
                        oTable.fnClearTable();
                        oTable.fnAddData(newJsonData);

                        FixSysMsg.success(`${ajccType}查詢成功`);
                    } else {
                        FixSysMsg.danger("查無資料");
                        oTable.fnClearTable(); //Talas 20180607 修正查詢前清除顯示資料
                    }
                } else {
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
    else
        resPat = ["", ""];

    return resPat;
}


/**
 * 開啟[戶籍地代碼與郵遞區號查詢]視窗
 * @param {any} api        設計書名稱(QRY-RESIDENCE-20190101(nodisplay).戶籍地代碼與郵遞區號查詢)
 * @param {any} tgtCodeId  鄉鎮市區代碼ID
 * @param {...any} theArgs 其它參數
 */
function SetResidenceCode(api, tgtCodeId, ...theArgs) {
    var winUrl = "";

    if (api && api !== "" && api.indexOf("-") > -1 && api.split("-").length > 2) {
        var winAjccType = api.split("-")[0];//QRY
        var winSpec = api.split("-")[1];//RESIDENCE
        winUrl = `${getCQSVDN()}/BDS/CODELIST/Query?ajccType=${winAjccType}&spec=${winSpec}`;

        console.log(winUrl);
        BoxPatientInfo.show(true, winUrl);
    }
}


//Render select_option API_Data
var selectOptionsObj = {
    "baseCodes": null,            //癌別,...etc.
    "baseHints": null,            //醫師姓名片語,通知內文,...etc.
    "baseControlers": [],         //預存controller執行api所需的參數
    "baseResultListFuncStr": null //預存ResultList需自動查詢的function字串
};
var TMP_CONST_KEY_UPLOADFILE = ""; //for [上傳檔案BTN]暫存於selectOptionsObj的key


//取得selectOptionsObj內存在的key正確名稱
function getSelectOptionObjSpecificKey(oriKey) {
    var rtnKey = "";
    //若為"審查醫師"，則selectOptionsObj對應的key需做處理
    rtnKey = oriKey === "審查醫師" ? `${oriKey}名單` : oriKey;

    return rtnKey;
}


//組optoinStr
//type: _parameters
function getOptionStr(data, api, type, item) {
    var nowOption = [];

    if (data && data.length > 0) {
        if (api.indexOf("onSelectHints") > -1) {
            if (type.match(/醫師姓名片語|CQS相關人員/)) {
                if (item === "科別") {//需要 distinct
                    //fixed: Array的reduce方法會使結果少一筆
                    //基本資料設定.片語維護.審查醫師名單.增改
                    if (cluster === "HINTS" && ajccType === "SETUP" && masterSpec === "EXAMDOCTORS")
                        data = $.grep(data, function (obj, idx) { return obj.在職 === 'A' && !obj.職代.match(/1(E|F)/); });
                    data.forEach(function (res, i) {
                        var currRes;
                        if (res['科別'] === "其他審查人員") {
                            if (cluster === "HINTS" && ajccType === "SETUP" && masterSpec === "EXAMDOCTORS")
                                currRes = `{"${item}":"${res['科別']}"}`;
                        } else
                            currRes = `{"${item}":"${res['科別']}"}`;
                        if (currRes && nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                    });
                }
                else if (item === "員編(姓名)") {
                    //基本資料設定.片語維護.審查醫師名單.增改
                    if (cluster === "HINTS" && ajccType === "SETUP" && masterSpec === "EXAMDOCTORS")
                        data = $.grep(data, function (obj, idx) { return obj.在職 === 'A' && !obj.職代.match(/1(E|F)/); });
                    data.forEach(function (res, i) {
                        var currRes = `{"${item}":"${res['員編']}(${res['姓名']})","科別":"${res['科別']}"}`;
                        if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                    });
                }
                else if (item === "姓名(員編)") {
                    data.forEach(function (res, i) {
                        var currRes = `{"${item}":"${res['姓名']}(${res['員編']})","科別":"${res['科別']}"}`;
                        if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                    });
                }
                else {
                    data.forEach(function (res, i) {
                        var currRes = JSON.stringify(res);
                        if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                    });
                }
            }
            else if (type === "通知內文") {
                if (item) {//需要 distinct
                    data.forEach(function (res, i) {
                        var currRes = `{"${item}":"${res[item]}"}`;
                        if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                    });
                }
                else {
                    data.forEach(function (res, i) {
                        var currRes = JSON.stringify(res);
                        if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                    });
                }
            }
            else {
                //eg. 片語用途設定、審查醫師意見
                data.forEach(function (res, i) {
                    nowOption.push(`{"${item}":"${res[item]}"}`);
                });
            }
        }
        else if (api === "onSelectCodeList") {//癌別
            var keyPre = "", keyPost = "";

            if (type === "癌別") {
                //癌別團隊中文(癌別團隊代碼)
                if (item.indexOf("癌別") > -1) {//需要 distinct
                    if (item.indexOf("(癌別") > -1) {
                        //[待收]收案類別需可查"空白"
                        keyPre = item.split("(")[0].trim();
                        keyPost = item.split("(")[1].replace(")", "").trim();
                        data.forEach(function (res, i) {
                            if (res[keyPre] !== "") {
                                var currRes = `{"${item}":"${res[keyPre]}(${res[keyPost]})"}`;
                                if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                            }
                        });
                    }
                    else {
                        data.forEach(function (res, i) {
                            var currRes = `{"${item}":"${res[item]}"}`;
                            if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                        });
                    }
                }
                else {
                    data.forEach(function (res, i) {
                        var currRes = JSON.stringify(res);
                        if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                    });
                }
            }
            //20191206 下拉式選單-各式因子
            else if (type.indexOf("AJCC7_") > -1 || type.indexOf("AJCC8_") > -1) {
                keyPre = item;
                if (item.match(/.+\(.+\)/g)) {
                    keyPre = item.split("(")[0].trim();
                    keyPost = item.match(/\(.+/g)[0].replace(/\(|\)/g, "");
                }
                if (keyPost !== "") {
                    //有"(XX代碼)"可排序
                    data.sort(function (a, b) {
                        var compA = a[keyPost];
                        var compB = b[keyPost];
                        return compA - compB;
                    });
                }
                data.forEach(function (res, i) {
                    var currRes = `{"${keyPre}":"${res[keyPre]}"}`;
                    if (keyPost !== "") currRes = `{"${keyPre}(${keyPost})":"${res[keyPre]}(${res[keyPost]})"}`;
                    if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                });
            }
            else {
                keyPre = item;
                if (item.match(/.+\(.+\)/g)) {
                    keyPre = item.split("(")[0].trim();
                    keyPost = item.match(/\(.+/g)[0].replace(/\(|\)/g, "");
                }
                if (keyPost !== "") {
                    //有"(XX代碼)"可排序
                    data.sort(function (a, b) {
                        var compA = a[keyPost];
                        var compB = b[keyPost];
                        return compA - compB;
                    });
                }
                data.forEach(function (res, i) {
                    var currRes = `{"${keyPre}":"${res[keyPre]}"}`;
                    if (keyPost !== "") currRes = `{"${keyPre}(${keyPost})":"${res[keyPre]}"}`;
                    if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                });
            }
        }
        else if (api === "getMainDoctorList") {
            if (type.indexOf("案號") > -1) {
                data.forEach(function (res, i) {
                    var currRes = `{"醫師科別-醫師姓名(醫師員編)":"${res['醫師科別']}-${res['醫師姓名']}(${res['醫師員編']})"}`;
                    if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                });
            }
        }
        else if (api === "onSelectHintsUsages") {//片語用途設定
            if (type === "片語用途設定") {
                data.forEach(function (res, i) {
                    var currRes = JSON.stringify(res);
                    if (nowOption.indexOf(currRes) === -1) nowOption.push(currRes);
                });
            }
            else {
                data.forEach(function (res, i) {
                    nowOption.push(`{"${item}":"${res[item]}"}`);
                });
            }
        }
        //20191122 雲端
        else if (api === "onSelectCommonList") {
            data.forEach(function (res, i) {
                var itemList = item.split(',');
                var displayItem = '';
                if (itemList[0] === "AJCC8分級") {
                    itemList.push("備註");
                }
                if (itemList.length > 1) {
                    itemList.forEach(function (element, idx) {
                        displayItem += res[element];
                        if (idx !== itemList.length - 1) {
                            displayItem += '-';
                        }
                    });
                } else {
                    displayItem = res[itemList[0]];
                }
                nowOption.push(`{"${item}":"${displayItem}"}`);
            });
        }
        //20191122 雲端
        else if (api === "onSelectParentCodeList") {
            //癌登代碼維護下拉選單
            data.forEach(function (res, i) {
                var phrase = "主檔編碼";
                if (masterSpec === "S3CMASTER") phrase = "父編碼";
                else if (masterSpec === "S3CDETAIL" && ajccType === "QRY") phrase = "主檔編碼父編碼";
                if (res[phrase] !== undefined)
                    nowOption.push(`{"${item}":"${res[phrase]}"}`);
            });
            //加入排序
            nowOption.sort();
        }

        //加入排序
        if (type !== "醫師姓名片語") nowOption.sort();
        if (type === "其他放射治療儀器") {
            nowOption = nowOption.sort(function (a, b) {
                var anum = parseInt(a[item]), bnum = parseInt(b[item]);
                var aval = a.split(':')[1], bval = b.split(':')[1];

                if (aval.split('-').length > 2) anum = parseInt("-" + aval.split('-')[1]);
                else anum = parseInt(aval.split('-')[0].split('"')[1]);
                if (bval.split('-').length > 2) bnum = parseInt("-" + bval.split('-')[1]);
                else bnum = parseInt(bval.split('-')[0].split('"')[1]);

                return anum > bnum ? 1 : -1;
            });
        }

        //[待收]收案類別需可查"空白"(排序後新增至第一位)
        if (api === "onSelectCodeList" && type === "癌別" &&
            item.indexOf("(癌別") > -1 && ajccType === "QRY" && masterSpec === "WAITLIST")
            if (nowOption.indexOf("空白") === -1) nowOption.unshift(`{"${item}":"空白"}`);
    }

    return nowOption;
}


//取得相關設定資料，並存至selectOptionsObj
function getCodeListOrHints(jsonElement) {
    var currApi = jsonElement._api || jsonElement.api;
    var currParameters = jsonElement._parameters || jsonElement.parameters;

    if (currApi && currParameters) {
        if (selectOptionsObj && !selectOptionsObj.hasOwnProperty(currParameters)) {
            if (currApi === "onSelectHintsUsages") { //代碼類別表維護
                onSelectHintsUsages(currParameters);
            }
            else if (currApi === "onSelectCodeList") {
                onSelectCodeList(currParameters);
            }
            //20191122 雲端
            else if (currApi === "onSelectParentCodeList") {
                onSelectParentCodeList(currParameters);
            }
            else if (currApi === "onSelectHints") {
                onSelectHints(currParameters);
            }
            else if (currApi === "getMainDoctorList") {
                getMainDoctorList(currParameters);
            }
            else if (currApi === "getAuditDoctor") {
                getAuditDoctor(currParameters);
            }
            else if (currApi === "getOtherResultList") {
                addBaseController(jsonElement);
            }
            else if (currApi === "CaseExecuteBtn" && (jsonElement.controller || jsonElement.remark === "controller")) {
                addBaseController(jsonElement);
            }
            else if (currApi === "onSelectCommonList") {
                onSelectCommonList(currParameters);
            }
        }
    }
    else if (jsonElement.controller || jsonElement.remark === "controller") {
        addBaseController(jsonElement);
    }
}

function addBaseController(jsonElement) {
    if (selectOptionsObj.baseControlers.length > 0) {
        //判斷目前jsonElement是否已存在於selectOptionsObj.baseControlers
        var isExistControler = false;
        $.each(selectOptionsObj.baseControlers, function (i, obj) {
            if (Compare(obj, jsonElement)) {
                isExistControler = true; return false;
            }
        });
        //不存在才加入selectOptionsObj.baseControlers
        if (!isExistControler)
            selectOptionsObj.baseControlers.push(jsonElement);
    } else {
        selectOptionsObj.baseControlers.push(jsonElement);
    }
}

//下拉選單控制
//應只有點選"其他"時，才可以輸入後面之輸入框欄位
function setOptionOther(bindName, nowVal) {
    var selfItem = $(`#${bindName}`);
    var targetItem = undefined, targetBindName = "";

    if (selfItem.length > 0) {
        targetItem = selfItem.next("input:text");
        if (targetItem.length > 0) {
            targetBindName = targetItem.attr("data-bind").replace(/value\:/g, "");
            if (nowVal === "其他") {
                targetItem.attr('disabled', false);
            } else {
                targetItem.attr('disabled', true);
                eval(`viewModel.${targetBindName}('');`);
            }
        }
    }
}


//取得selectOptionsObj內對應key為"應派案最大值(病歷數)"的AuditCaseMaxNum值
//eg. setAuditCaseMaxNum("Ref.應派案最大值(病歷數)")
function setAuditCaseMaxNum(refStr) {
    var refKey = refStr.replace("Ref.", "").trim();
    if (selectOptionsObj.hasOwnProperty(refKey) && selectOptionsObj[refKey].length === 1) {
        return selectOptionsObj[refKey][0]["AuditCaseMaxNum"];
    }
}

//====================================[Leo S]====================================
//MANTIS Case:0025247
//[v1]畫面呈顯：部分子項擺放同一列
function fDisplayInline(sTagID) {
    $("#" + sTagID).parent().parent().find('div').css({
        "display": "inline"
    });
}

//MANTIS Case: 0025222
//radio連動
function fRadioYN(sItemText, sTagID, iOP) {
    var childArrA = document.getElementsByClassName("col-md-9");

    for (var a = 0; a < childArrA.length; a++) {
        var childArrB = childArrA[a].children;
        fRadioYN_Child(childArrB, sItemText, sTagID, iOP);
    }
}

function fRadioYN_Child(oChildArr, sItem, sTagID, iOP) {
    var sId = "", sName = "", sValue = "", sType = "", sText = "";
    var sNewTagID = "";

    $.each(oChildArr, function (i, sValue) {
        sId = this.id;
        sName = this.name;
        sValue = this.value;
        sType = this.type;

        if (sId || sName) {
            if (sTagID !== sId && sTagID !== sName) {
                if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                    //MANTIS Case: 0025248
                    if (sItem === "≧50%" || sItem === "1-49%" || sItem === "Loss" || sItem === "Mutation Type" || sItem === "Postive" || sItem === "Positive" || sItem === "Malignancy") {
                        //$(this).attr('disabled', false);
                    } else if (sItem === "＞14%" || sItem === "＜14%" || sItem === ">14%" || sItem === "<14%") {
                        sNewTagID = sTagID + "_" + iOP;

                        if (sId.indexOf(sNewTagID) > -1 || sName.indexOf(sNewTagID) > -1) {
                            //$(this).attr('disabled', false);
                        } else {
                            fDisabledYN_Clear(sType, sId, sName);
                        }
                    } else {
                        fDisabledYN_Clear(sType, sId, sName);
                    }
                }
            }
        }

        var childArrNext = oChildArr[i].children;
        fRadioYN_Child(childArrNext, sItem, sTagID, iOP);
    });
}

function fDisabledOne(sItem, sTagID) {
    var sVal = "";
    var sId = sTagID + "_0";

    $('input[name=' + sTagID + ']:radio').each(function () {
        sVal = $(this).val();

        if (sItem !== sVal)
            fDisabledYN_Clear("text", sId, sId);
    });
}

//checkbox連動
function fCheckboxYN(oChk, sTagID) {
    var sChk = oChk.checked ? "Y" : "N";
    var sId = "", sName = "", sValue = "", sType = "", sText = "";
    var childArrA = document.getElementsByClassName("col-md-9");

    for (var a = 0; a < childArrA.length; a++) {
        var childArrB = childArrA[a].children;

        $.each(childArrB, function (b, sValueB) {
            sId = this.id;
            sName = this.name;
            sValue = this.value;
            sType = this.type;

            if (sId || sName) {
                if (sTagID !== sId && sTagID !== sName) {
                    if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                        if (sChk === "N") {
                            fDisabledYN_Clear(sType, sId, sName);

                            if (sValue.indexOf("其他") > -1) {
                                sType = "text";
                                sName = sName + "_0";
                                sId = sName;

                                fDisabledYN_Clear(sType, sId, sName);
                            }
                        }
                    }
                }
            }

            var childArrC = childArrB[b].children;
            $.each(childArrC, function (c, sValueC) {
                sId = this.id;
                sName = this.name;
                sValue = this.value;
                sType = this.type;

                if (sId || sName) {
                    if (sTagID !== sId && sTagID !== sName) {
                        if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                            if (sChk === "N") {
                                fDisabledYN_Clear(sType, sId, sName);

                                if (sValue.indexOf("其他") > -1) {
                                    sType = "text";
                                    sName = sName + "_0";
                                    sId = sName;

                                    fDisabledYN_Clear(sType, sId, sName);
                                }
                            }
                        }
                    }
                }

                var childArrD = childArrC[c].children;
                $.each(childArrD, function (d, sValueD) {
                    sId = this.id;
                    sName = this.name;
                    sValue = this.value;
                    sType = this.type;

                    if (sId || sName) {
                        if (sTagID !== sId && sTagID !== sName) {
                            if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                                if (sChk === "N") {
                                    fDisabledYN_Clear(sType, sId, sName);

                                    if (sValue.indexOf("其他") > -1) {
                                        sType = "text";
                                        sName = sName + "_0";
                                        sId = sName;

                                        fDisabledYN_Clear(sType, sId, sName);
                                    }
                                }
                            }
                        }
                    }

                    var childArrE = childArrD[d].children;
                    $.each(childArrE, function (e, sValueE) {
                        sId = this.id;
                        sName = this.name;
                        sValue = this.value;
                        sType = this.type;

                        if (sId || sName) {
                            if (sTagID !== sId && sTagID !== sName) {
                                if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                                    if (sChk === "N") {
                                        fDisabledYN_Clear(sType, sId, sName);

                                        if (sValue.indexOf("其他") > -1) {
                                            sType = "text";
                                            sName = sName + "_0";
                                            sId = sName;

                                            fDisabledYN_Clear(sType, sId, sName);
                                        }
                                    }
                                }
                            } else {
                                if (sValue.indexOf("其他") > -1) fCheckboxYN2(sName, sChk);
                            }
                        }

                        var childArrF = childArrE[e].children;
                        $.each(childArrF, function (f, sValueF) {
                            sId = this.id;
                            sName = this.name;
                            sValue = this.value;
                            sType = this.type;

                            if (sId || sName) {
                                if (sTagID !== sId && sTagID !== sName) {
                                    if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                                        if (sChk === "N") {
                                            fDisabledYN_Clear(sType, sId, sName);

                                            if (sValue.indexOf("其他") > -1) {
                                                sType = "text";
                                                sName = sName + "_0";
                                                sId = sName;

                                                fDisabledYN_Clear(sType, sId, sName);
                                            }
                                        }
                                    }
                                }
                            }

                            var childArrG = childArrF[f].children;
                            fChildArr(childArrG, sTagID, sChk);
                        });
                    });
                });
            });
        });
    }
}

function fCheckboxYN2(sName, sChk) {
    sType = "text";
    sName = sName + "_0";
    sId = sName;

    if (sChk === "N")
        fDisabledYN_Clear(sType, sId, sName);
}

function fChildArr(oChildArr, sTagID, sChk) {
    var sId = "", sName = "", sValue = "", sType = "", sText = "";

    $.each(oChildArr, function (i, sValue) {
        sId = this.id;
        sName = this.name;
        sValue = this.value;
        sType = this.type;

        if (sId || sName) {
            if (sTagID !== sId && sTagID !== sName) {
                if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                    if (sChk === "N")
                        fDisabledYN_Clear(sType, sId, sName);
                }
            }
        }

        var childArrNext = oChildArr[i].children;
        fChildArr(childArrNext, sTagID, sChk);
    });
}

//MANTIS Case: 0025204
function fDisabled_Cure(sItem, sOpenID, sCloseID) {
    var sId = "", sName = "", sValue = "", sType = "", sText = "";
    var childArrA = document.getElementsByClassName("form-group row");

    $.each(childArrA, function (a, sValueA) {
        var childArrB = childArrA[a].children;
        $.each(childArrB, function (b, sValueB) {
            sId = this.id;
            sName = this.name;
            sValue = this.value;
            sType = this.type;

            if (sId || sName) {
                if (sCloseID !== "") {
                    if (sId.indexOf(sCloseID) > -1 || sName.indexOf(sCloseID) > -1)
                        fDisabledYN_Clear(sType, sId, sName);
                }
            }

            var childArrC = childArrB[b].children;
            $.each(childArrC, function (c, sValueC) {
                sId = this.id;
                sName = this.name;
                sValue = this.value;
                sType = this.type;

                if (sId || sName) {
                    if (sCloseID !== "") {
                        if (sId.indexOf(sCloseID) > -1 || sName.indexOf(sCloseID) > -1)
                            fDisabledYN_Clear(sType, sId, sName);
                    }
                }

                var childArrD = childArrC[c].children;
                $.each(childArrD, function (d, sValueD) {
                    sId = this.id;
                    sName = this.name;
                    sValue = this.value;
                    sType = this.type;

                    if (sId || sName) {
                        if (sCloseID !== "") {
                            if (sId.indexOf(sCloseID) > -1 || sName.indexOf(sCloseID) > -1)
                                fDisabledYN_Clear(sType, sId, sName);
                        }
                    }

                    var childArrE = childArrD[d].children;
                    $.each(childArrE, function (e, sValueE) {
                        sId = this.id;
                        sName = this.name;
                        sValue = this.value;
                        sType = this.type;

                        if (sId || sName) {
                            if (sId.indexOf(sCloseID) > -1 || sName.indexOf(sCloseID) > -1)
                                fDisabledYN_Clear(sType, sId, sName);
                        }

                        var childArrF = childArrE[e].children;
                        $.each(childArrF, function (f, sValueF) {
                            sId = this.id;
                            sName = this.name;
                            sValue = this.value;
                            sType = this.type;

                            if (sId || sName) {
                                if (sCloseID !== "") {
                                    if (sId.indexOf(sCloseID) > -1 || sName.indexOf(sCloseID) > -1)
                                        fDisabledYN_Clear(sType, sId, sName);
                                }

                                if (sValue.indexOf("其他") > -1) {
                                    sType = "text";
                                    sName = sName + "_0";
                                    sId = sName;

                                    if (sCloseID !== "")
                                        fDisabledYN_Clear(sType, sId, sName);
                                }
                            }
                        });
                    });
                });
            });
        });
    });
}

//MANTIS Case: 0025142
function fDisabledYN(sItem, sTagID) {
    var sId = "", sName = "", sValue = "", sType = "", sText = "";
    var childArrA = document.getElementsByClassName("col-md-9");

    for (var a = 0; a < childArrA.length; a++) {
        var childArrB = childArrA[a].children;
        $.each(childArrB, function (b, sValueB) {
            sId = this.id;
            sName = this.name;
            sValue = this.value;
            sType = this.type;

            if (sId || sName) {
                if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                    if (sItem === "無" || sItem === "否" || sItem === "未曾吸菸" ||
                        sItem === "未曾喝酒" || sItem === "未曾嚼檳榔")
                        fDisabledYN_Clear(sType, sId, sName);
                }
            }

            var childArrC = childArrB[b].children;
            $.each(childArrC, function (c, sValueC) {
                sId = this.id;
                sName = this.name;
                sValue = this.value;
                sType = this.type;

                if (sId || sName) {
                    if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                        if (sId !== sTagID || sName !== sTagID) {
                            if (sItem === "無" || sItem === "否" || sItem === "未曾吸菸" ||
                                sItem === "未曾喝酒" || sItem === "未曾嚼檳榔")
                                fDisabledYN_Clear(sType, sId, sName);
                        }
                    }
                }

                var childArrD = childArrC[c].children;
                $.each(childArrD, function (d, sValueD) {
                    sId = this.id;
                    sName = this.name;
                    sValue = this.value;
                    sType = this.type;

                    if (sId || sName) {
                        if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                            if (sItem === "無" || sItem === "否" || sItem === "未曾吸菸" ||
                                sItem === "未曾喝酒" || sItem === "未曾嚼檳榔")
                                fDisabledYN_Clear(sType, sId, sName);
                        }
                    }

                    var childArrE = childArrD[d].children;
                    $.each(childArrE, function (e, sValueE) {
                        sId = this.id;
                        sName = this.name;
                        sValue = this.value;
                        sType = this.type;

                        if (sId || sName) {
                            if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                                if (sItem === "無" || sItem === "否" || sItem === "未曾吸菸" ||
                                    sItem === "未曾喝酒" || sItem === "未曾嚼檳榔")
                                    fDisabledYN_Clear(sType, sId, sName);
                            }
                        }

                        var childArrF = childArrE[e].children;
                        $.each(childArrF, function (f, sValueF) {
                            sId = this.id;
                            sName = this.name;
                            sValue = this.value;
                            sType = this.type;

                            if (sId || sName) {
                                if (sId.indexOf(sTagID) > -1 || sName.indexOf(sTagID) > -1) {
                                    if (sItem === "無" || sItem === "否" || sItem === "未曾吸菸" ||
                                        sItem === "未曾喝酒" || sItem === "未曾嚼檳榔")
                                        fDisabledYN_Clear(sType, sId, sName);
                                }
                            }
                        });
                    });
                });
            });
        });
    }
}

function fDisabledYN_Clear(sType, sId, sName) {
    var oTag = $("#" + sId);
    var sColumn = "";
    sColumn = oTag.attr("data-bind");

    if (sType === "select-one") {
        var aColumn = sColumn.split(",");
        sColumn = "";

        for (i = 0; i < aColumn.length; i++) {
            sColumn = aColumn[i];
            if (sColumn.indexOf("value") > -1) {
                if (sColumn.indexOf(":") > -1) {
                    sColumn = sColumn.split(":")[1];
                    break;
                } else {
                    return "";
                }
            }
        }

        if (sColumn === "") return "";
    } else {
        if (sColumn.indexOf(":") > -1) {
            sColumn = sColumn.split(":")[1];
        } else {
            return "";
        }
    }

    if (sType === "text") {
        eval(`viewModel.${sColumn}('');`);
    } else if (sType === "checkbox") {
        eval(`viewModel.${sColumn}(false)`);
    } else if (sType === "radio") {
        eval(`viewModel.${sColumn}(false)`);
    } else if (sType === "select-one") {
        var e = document.getElementById(sId);
        if (e.length === 0) {
            eval(`viewModel.${sColumn}('');`);
        } else {
            var sValue = e.options[0].value;
            eval(`viewModel.${sColumn}('${sValue}');`);
        }
    }
}

//MANTIS Case: 0025138
function fTitleOther(oThis, sColumn) {
    var sValue = oThis.value || oThis.options[oThis.selectedIndex].value;

    var sReplace = /column/g;
    sColumn = sColumn.replace(sReplace, "");
    var iColumn = parseInt(sColumn) + 1;
    sColumn = "column" + iColumn;

    if (sValue !== "其他")
        eval(`viewModel.${sColumn}('');`);
}
//---------------------------------------

//MANTIS Case: 0025144
function fCheckDate(sVal) {//西元年格式檢驗
    var sMsg = "";

    if (sVal.length !== 8) {
        sMsg += "日期格式不符合YYYYMMDD";
    } else {
        if (!getHICloudDataIFaceChk2(sVal)) {
            sMsg += "內容為非數字";
        } else {
            y = parseInt(sVal.substr(0, 4), 10);
            m = parseInt(sVal.substr(4, 2), 10) - 1;
            d = parseInt(sVal.substr(6, 2), 10);

            //將日期字串轉成日期物件
            var x = new Date(y, m, d);
            if (isNaN(x.getTime())) {
                sMsg += "若無法轉成日期物件表示輸入資料不合法";
            } else {
                //若輸入內容與轉換成日期格式後的資料一致, 表示日期合法
                if (y === x.getFullYear() && m === x.getMonth() && d === x.getDate()) {
                    sMsg = "";
                } else {
                    sMsg += "日期不合法";
                }
            }
        }
    }

    return sMsg;
}

//展開
function fUnfold() {
    var btUnfold = $("#btUnfold");
    btUnfold.next("blockquote").toggle();
}

//解析URL的GET
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) { return pair[1]; }
    }
    return false;
}
//====================================[Leo E]====================================

//選案個案資料查詢、審查醫師名單...etc.
function getOtherResultList(phraseStr) {
    var para1 = phraseStr.indexOf(",") > -1 ? phraseStr.split(",")[0] : phraseStr;
    var para2 = [];
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "OtherResultList",
        "data": {
            "func": para1
        }
    };

    if (selectOptionsObj.baseControlers && selectOptionsObj.baseControlers.length > 0) {
        //找baseControlers內是否有對應的條件參數
        $.each(selectOptionsObj.baseControlers, function (i, ctrlObj) {
            var tmpParaArr = ctrlObj.parameters ?
                ctrlObj.parameters.indexOf(",") > -1 ? ctrlObj.parameters.split(",") : [ctrlObj.parameters] : [""];
            if (ctrlObj.api && ctrlObj.api === "getOtherResultList" && tmpParaArr[0] === para1) {
                tmpParaArr.forEach(function (val, i) {
                    if (i !== 0) para2.push(val); //array第2項開始為查詢條件參數
                });

                //json.data建立Condition(查詢條件)
                obj.data["data"] = {};
                para2.forEach(function (val, i) {
                    eval(`obj.data.data["${val}"]="";`);
                    eval(`obj.data.data.${val}='${getJsonMappingVal(val)}';`);
                });
            }
        });
    }

    obj = JSON.stringify(obj);
    console.log(obj);
    $.ajax({
        url: getAPIURL(),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
            if (response.rtnCode === 0) {
                var result = JSON.parse(response.jsonData.data);
                console.log(result);
                eval(`selectOptionsObj["${para1}"]=${response.jsonData.data}`);
            } else {
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

//將已取得的selectOptionsObj的資料，對應funcType做處理，存入ResultList
//funcType: onclick後，需執行的動作類型
//parametersStr: 參數字串 eg. 選案個案資料查詢,癌別代碼,病歷年度,病歷季別,初診斷日期;審查醫師名單
//refStr: 參考字串 eg. ResultList
function setToBackResult(funcType, parametersStr, refStr) {
    var tackledOptionObj, resultObj = {}, isExecute = false;
    var tackledOptionKeys = parametersStr.indexOf(";") > -1 ? parametersStr.split(";") : [parametersStr];

    //建立對應的ResultList
    if (resultListItems && resultListItems.length > 0) {
        tackledOptionKeys.forEach(function (val) {
            //帶入查詢條件進行查詢
            if (funcType === "選案") {
                isExecute = true;
                if (val.indexOf("選案個案資料查詢") === 0)
                    getResultList(ajccType, masterSpec, "json_query", null, val);
                else
                    getOtherResultList(val);
            }
            else {
                if (val !== "") {
                	var oval=val;
                	val = val.replace(/[a-zA-Z]|_/g, "") 
                    .replace(/\s+/g, " "); 
                    if (FIX_QUERY_KEY_setToBackResult.indexOf(`|${val}|`) > -1) {
                        ReloadResult(); isExecute = true;
                    } else if (Reg_SetToBackResult_Key.indexOf(`|${val}|`) > -1) {
                        var reloadBtn = $("input:button[value='查詢']");
                        if (reloadBtn.length > 0) {
                            var reloadJs = reloadBtn.attr("onclick"); //eg. getPatientInfo('病歷號,姓名','#json_query','required.病歷號||required.姓名',false)
                            //自動查詢時不檢核必填
                            if (reloadJs.indexOf("required.") > -1) reloadJs = reloadJs.replace(/'required.+'/g, "undefined");
                            //第一個參數替換成refStr的值(目的為查詢該index)
                            if (refStr !== undefined && refStr !== "" && refStr !== "ResultList") {
                                var funcMapArr;
                                funcMapArr = reloadJs.split(/',/).map(function (val, i) {
                                    if (i === 0) return val.trim().replace(/'.+/, `'${refStr}`);
                                    //else if (val.indexOf("json_query") > -1 && LocalStorage_Key_ReadonlyJson.indexOf("READONLY") > -1) {
                                    //    return val.trim().replace("json_query", "ctrl_qry_readonlylist");
                                    //}
                                    else return val.trim();
                                });
                                if (funcMapArr !== undefined) {
                                    reloadJs = "";
                                    funcMapArr.forEach(function (val, i) { reloadJs += `',${val}`; });
                                    reloadJs = reloadJs.substring(2);
                                }
                            }
                            console.log(reloadJs);
                            eval(reloadJs);
                        }
                    }
                    else if (ajccType === "TODO" && val === "未填寫清單查詢") {

                        getResultList(ajccType, masterSpec, "json_query", null, val);
                        isExecute = true;
                    }
                } else
                    getOtherResultList(val);
                
            }

            if (isExecute) {
                if (refStr === "ResultList") {
                    //建立resultObj模型:for ResultList結果
                    $.each(resultListItems, function (ia, colName) {
                        var tmpColName = "";
                        if (colName.indexOf("DLLINK") > -1) tmpColName = colName.replace(/#|DLLINK/g, "").trim();
                        else tmpColName = colName.replace("#", "").replace(/SELECT|LINK|BTN|□|asc|desc/g, "").trim(); ///[SELECT|LINK|BTN|□|asc|desc]+/g
                        eval(`resultObj["${tmpColName}"]=null`);
                    });
                 
                    //若為[追蹤][個管服務]，則ResultList有多隱藏欄位: "id"
                    if (val !== "" && FIX_QUERY_KEY_setToBackResult.indexOf(`|${val}|`) > -1) {
                    	

                        eval(`resultObj["id"]=null`);
                        //if (val === "團隊會議") eval(`resultObj["dataid"]=null`);
                    }

                    //[final ResultList]
                    var currKey = "";
                	val=oval;
                
                    if (val.indexOf("選案個案資料查詢") === 0) {
                        currKey = val.indexOf(",") > -1 ? val.split(",")[0] : val;
                        setToBackResultSub(currKey, resultObj);
                    }
                    else if (dataType.indexOf(val) >-1) {
                        //eg. 追蹤
                        currKey = val.indexOf(",") > -1 ? val.split(",")[0] : val;
                        
                        setToBackResultSub(currKey, resultObj);
                    }
                    else if (val === `COMM${dataType}`) {
                        //eg. COMM個案查詢
                        currKey = val.indexOf(",") > -1 ? val.split(",")[0] : val;
                        setToBackResultSub(currKey, resultObj);
                    }
                }
            }
        });
    }
}

function setToBackResultSub(currKey, resultObj) {
    var tackledOptionObj, resultListJsonArr = [];

    //eg. selectOptionsObj.追蹤
    tackledOptionObj = eval(`selectOptionsObj.${currKey}`);
//    tackledOptionObj = eval(`selectOptionsObj.${currKey}`);
    //render暫存於selectOptionsObj內的ResultList資料
    $.each(tackledOptionObj, function (ib, rowObj) {
    	
        Object.keys(resultObj).forEach(function (reskey, iRes) { //resultObj內每個key
            resultObj[reskey] = ""; //初始化值

            for (var i = 0; i < Object.keys(rowObj).length; i++) {
                var comparedKey = "", tmpkey = Object.keys(rowObj)[i];

                if (tmpkey.indexOf("DLLINK") > -1)
                    comparedKey = tmpkey.replace(/#|DLLINK/g, "").trim();
                else
                    comparedKey = tmpkey.replace("#", "").replace(/(SELECT|LINK|BTN|□|asc|desc)+/g, "").trim();

                if (reskey.indexOf(comparedKey)>-1) {
                    resultObj[reskey] = rowObj[tmpkey]; break;
                }
            }
        });

        //需要Deep Copy，否則resultListJsonArr每個物件值都只有null
        var tmpResObj = $.extend(true, {}, resultObj);
        resultListJsonArr.push(tmpResObj);
    });

    var oTable = $(`#json_query`).dataTable({ select: true });

    if (resultListJsonArr.length > 0) {

        oTable.fnAddData(resultListJsonArr);
    }
}


//取得JSON內對應key的值
function getJsonMappingVal(key) {
    if (typeof getvalue === "function") getvalue();
    var rtnVal = "";
    jQuery.each(json.data, function (i, renderType) {
        var result = jQuery.map(renderType.docdetail, function (item, idx) {
            if (item.segmentation && item.segmentation.indexOf(key) > -1) {
                if (item.option) {
                    return jQuery.map(item.option, function (itemOption, idOption) {
                        return jQuery.map(itemOption.follow, function (itemf, idf) {
                            if (itemf.data && itemf.data !== "") return itemf.data;
                            else return null;
                        });
                    });
                }
                else {
                    var newData = item.data;
                    if (item.follow) {
                        newData += jQuery.map(item.follow, function (itemf, idf) {
                            if (itemf.data && itemf.data !== "") {
                                if (itemf.item && itemf.item === "~") return itemf.item + itemf.data;
                                else return itemf.data;
                            }
                            else return null;
                        });
                    }
                    return newData;
                }
            } else return null;
        });

        if (result && result.length > 0) {
            rtnVal = result[0];
            return false;
        }
    });

    return rtnVal;
}

//設定有mapping到的data值(Step. I)
//mappingKey: eg. ref、js
//mappingVal: json["ref"]
//value: 需更新data的值
//bindingKey: onchange的binding物件名稱
function setJsonMappingVal(mappingKey, mappingVal, value, bindingKey) {
    var result = false;
    var bindingDom = $(`[data-bind*='${bindingKey}']:first`);
    getvalue();

    //從最外層json開始找起
    jQuery.each(json.data, function (i, renderType) {
        if (renderType.docdetail) {
            jQuery.each(renderType.docdetail, function (idx, item) {
                result = setJsonMappingValSub(mappingKey, mappingVal, value, item);
                if (result) return false;
            });
        }
        if (result) return false;
    });
}

//設定有mapping到的data值(Step. II)(Todo)
function setJsonMappingValSub(mappingKey, mappingVal, value, jsonElement) {
    var result = false;
    var nowVal = eval(`jsonElement.${mappingKey}`);
    var updatedVal = value;

    if (nowVal && nowVal === mappingVal) {
        if (jsonElement.parameters) {
            var tmpKey = jsonElement.parameters.split(",")[0];
            if (selectOptionsObj.hasOwnProperty(tmpKey)) {
                //eg. selectOptionsObj.癌別
                $.each(selectOptionsObj[tmpKey], function (obj) {
                    obj.values().forEach(function (val, idx) {
                        //(Todo)
                        //if (val === value) { }
                    });
                });
            }
        }

        //比對相同，設定data值
        if (jsonElement.data) jsonElement.data = updatedVal;
        else jsonElement["data"] = updatedVal;
        result = true;
    }
    else {
        if (mappingKey === "resultdata" && jsonElement.resultdata) {
            //更新resultdata內mappingVal的值 eg. 案件狀態
            if (eval(`jsonElement.resultdata.${mappingVal}`) !== undefined) {
                jsonElement.resultdata[mappingVal] = updatedVal;
                result = true;
            }
        }
        else {
            var cursorArray = jsonElement.follow || jsonElement.option || jsonElement.child;
            if (cursorArray) {
                $.each(cursorArray, function (idx, item) {
                    result = setJsonMappingValSub(mappingKey, mappingVal, value, item);
                    if (result) return false;
                });
            }
        }
    }

    return result;
}

//取得JSON內對應"key":"value"的element(Step. I)
//searchType: JSON內符合的datatype eg. 維護、基本資料
//isNeedNewJson: 是否需要重新取得新的json
function getJsonMappingElement(key, value, searchType, isNeedNewJson) {
    var result, tmpJIdx = 0, searchArr = [];
    if (isNeedNewJson && typeof getvalue === "function") getvalue();
    if (value && value.indexOf(",") > 0) searchArr = value.split(",");

    //從最外層json開始找起
    jQuery.each(json.data, function (i, renderType) {
        if (renderType.datatype && renderType.datatype.indexOf(searchType)>-1 && renderType.docdetail) {
            if (key === "datatype")
                result = renderType.docdetail[0].code;
            else {
                jQuery.each(renderType.docdetail, function (idx, item) {
                    if (searchArr.length > 0) {
                        for (var j = tmpJIdx; j < searchArr.length; j++) {
                            result = getJsonMappingElementSub(key, searchArr[j], item);
                            if (result)
                                tmpJIdx = j + 1;
                            else
                                break;
                        }
                        if (result && tmpJIdx === searchArr.length) return false;
                    }
                    else {
                    	result = getJsonMappingElementSub(key, value, item);
                        if (result) return false;
                    }
                });
            }
        }
        if (result) return false;
    });

    return result;
}

//取得JSON內對應"key":"value"的element(Step. II)
function getJsonMappingElementSub(key, value, jsonElement) {
    var result;

    if (!Array.isArray(jsonElement)) {
        if (eval(`jsonElement.${key}`)) {
            var currVal = eval(`jsonElement.${key}`);
            if (typeof currVal !== "object" && (currVal === value || currVal.indexOf(value) === 0))
                result = jsonElement;
            else {
                if (key === "resultdata" && eval(`jsonElement.resultdata.${value}`) !== undefined)
                    result = jsonElement.resultdata;
                else if (jsonElement.follow)
                    result = getJsonMappingElementSub(key, value, jsonElement.follow);
                else if (jsonElement.child)
                    result = getJsonMappingElementSub(key, value, jsonElement.child);
                else if (jsonElement.implement)
                    result = getJsonMappingElementSub(key, value, jsonElement.implement);
            }
        }
        else {
            if (jsonElement.follow)
                result = getJsonMappingElementSub(key, value, jsonElement.follow);
            else if (jsonElement.child)
                result = getJsonMappingElementSub(key, value, jsonElement.child);
            else if (jsonElement.implement)
                result = getJsonMappingElementSub(key, value, jsonElement.implement);
            else if (jsonElement.option)
                result = getJsonMappingElementSub(key, value, jsonElement.option);
        }
    }
    else {
        jQuery.each(jsonElement, function (idx, item) {
            if (eval(`item.${key}`)) { //eg. item.ref
                var currVal = eval(`item.${key}`);
                if (typeof currVal !== "object" && (currVal === value || currVal.indexOf(value) === 0))
                    result = item;
                else {
                    if (item.follow)
                        result = getJsonMappingElementSub(key, value, item.follow);
                    else if (item.child)
                        result = getJsonMappingElementSub(key, value, item.child);
                    else if (item.implement)
                        result = getJsonMappingElementSub(key, value, item.implement);
                }
            }
            else {
                if (item.follow)
                    result = getJsonMappingElementSub(key, value, item.follow);
                else if (item.child)
                    result = getJsonMappingElementSub(key, value, item.child);
                else if (item.implement)
                    result = getJsonMappingElementSub(key, value, item.implement);
            }

            if (result) return false;
        });
    }

    return result;
}

/**
 * 從json中取得對應key的value
 * @param {any} json         json
 * @param {any} jdatatype    json內的頁籤(診斷、治療、復發)
 * @param {any} disLabel     頁籤內所指定的的位置說明
 * @param {any} dis2Label    針對特殊需在指定的位置說明
 * @param {any} currDisName  json內要取值的說明("臨床 T：▼" <= displayname)
 * @param {any} currAJCC     目前AJCC的值
 * @returns {any}            回傳取得的值
 */
function fJsonData(json, jdatatype, disLabel, dis2Label, currDisName, currAJCC) {
    var sReturnVal = "";
    if (jdatatype === "") return "";
    var path = $.JSONPath({ data: json, keepHistory: false });
    var qryRS = path.query(`$.data[?(@.datatype == '${jdatatype}')].docdetail[*]`);

    if (qryRS) {
        currDisName = TrimAllBlank(currDisName, "g").toUpperCase();
        var qryObj = path.query(`$.data[?(@.datatype == '${jdatatype}')].docdetail[?(@.displayname == '${disLabel}')]`)[0];
        if (qryObj !== undefined) {
            var qryROW = qryObj.row, qryCOL = qryObj.col, qryVal = qryObj.value,
                isMatchAJCC = false, isMatchOpt = false;

            $.each(qryRS, function () {
                //var obj = $(this);
                //this為目前所指到的object
                if (this["row"] >= qryROW && this["col"] >= qryCOL) {
                    //在"AJCC期別"前還有一階選擇項時，需找到選擇的項目區塊
                    if (!isMatchOpt && this["displayname"] === qryVal) isMatchOpt = true;

                    //符合對應AJCC期別
                    if (!isMatchAJCC && this["displayname"] === currAJCC && isMatchOpt) {
                        isMatchAJCC = true;

                        if (dis2Label !== undefined) {
                            qryObj = path.query(`$.data[?(@.datatype == '${jdatatype}')].docdetail[?(@.displayname == '${dis2Label}')]`)[0];
                            if (qryObj !== undefined) {
                                qryROW = qryObj.row;
                                qryCOL = qryObj.col;
                                disLabel = dis2Label;
                            }
                        }
                    }

                    //加入this["displayname"]判斷已防this["displayname"]是undefined
                    if (isMatchAJCC && this["displayname"] &&
                        TrimAllBlank(this["displayname"], "g").toUpperCase() === currDisName) {
                        sReturnVal = this["value"];
                        return false;
                    }
                }
            });
        }
    }

    return sReturnVal;
}

//取出2階json的值: level1-level2  20191111 Shelley
//display:欄位名稱,jdatatype:區塊名稱
function Get2LevelData(display, jdatatype) {
    var level1 = "", level2 = "", sReturnVal = "";

    level1 = GetJsonData(display, jdatatype);//第1階
    if (level1 !== undefined && level1 !== "") {
        sReturnVal = level1;
        level2 = GetJsonData(level1, jdatatype);//第1階

        if (level2 !== undefined && level2 !== "") {
            sReturnVal = sReturnVal + "-" + level2;
        }
    }
    return sReturnVal;
}

//取出json的值
//display:欄位名稱,jdatatype:區塊名稱
function GetJsonData(display, jdatatype) {
    var sReturnVal = "";
    var path = $.JSONPath({ data: json, keepHistory: false });
    var qryRS = path.query(`$.data[?(@.datatype == '${jdatatype}')].docdetail[?(@.displayname == '${display}')].value`)[0];
    if (qryRS !== undefined) {
        sReturnVal = qryRS;
    }
    return sReturnVal;
}


//取出json的值
//display:欄位名稱,path:json, uitype:欄位型態, ref:參考欄位
function getJsonVal(display, jtoken, uitype, ref) {
    var sReturnVal = "";
    var qryObj;
    if (ref)
        qryObj = jtoken.find(k => k["displayname"] === `${display}` && k["_ref"] === `${ref}`);
    else if (uitype)
        qryObj = jtoken.find(k => k["displayname"] === `${display}` && k["uitype"] === `${uitype}`);
    else
        qryObj = jtoken.find(k => k["displayname"] === `${display}`);

    if (qryObj)
        sReturnVal = qryObj.value;

    if (sReturnVal === false)
        sReturnVal = "";

    return sReturnVal;
}

//取得option的text、value等屬性之key設定
function getOptionKeySettings(jsonObj, mappingKey) {
    for (var opKey in jsonObj) {
        if (opKey.indexOf(mappingKey) !== -1) {
            return opKey;
        }
    }
    return "";
}

function getValueInObject(obj, key, val) {
    var returnData = "";
    var objects = getObjects(obj, key, val);
    if (objects === [] || objects.length === 0) {
        returnData = "";
    } else {
        returnData = objects[0].data;
    }
    return returnData;
}

function getObjects(obj, key, val) {
    //key: segmentation,item
    //val: 年度：
    var objects = [];
    for (var i in obj) {
        //console.log(String(obj[key]));
        if (!obj.hasOwnProperty(i)) continue;
        if (typeof obj[i] === 'object') {
            objects = objects.concat(getObjects(obj[i], key, val));
        } else if (key.indexOf(i) > -1 && obj[i] && obj[i].indexOf(val) > -1) {
            objects.push(obj);
        }
    }
    return objects;
}

//checkbox 勾選展開
function DisplayBlock(t, nowVal) {
    var block = $(t).parents("fieldset");
    var lCol = $(t).attr("col");
    var lRow = $(t).attr("row");
    var currAddBtnBlockId = $(t).attr("block");
    var currAddBtnBlockIds = findAddedBlockids(block);

    for (var i = 0; i < block.children().length; i++) {
        var item = block.children()[i];
        var tmpCol = $(item).attr("col");
        var tmpRow = $(item).attr("row");
        //console.log(`tmpRow:${tmpRow},lRow:${lRow};tmpCol:${tmpCol},lCol:${lCol}`);

        var isIniAddBtn = false;
        $.each($(item).children(), function (j, childItem) {
            var tmpChildItem = $(childItem), tmpBlockId = "", tmpMaxBlockId = "";
            isIniAddBtn = false;
            tmpBlockId = tmpChildItem.attr("block");
            //判斷[增加BTN]初始區塊是否顯示(有增加過、且為初始區塊時，for迴圈continue)
            if (tmpBlockId && tmpBlockId.match(/button_.+_btn0_.+/g) && tmpChildItem.is(":hidden")) {
                var BreakException = {};
                try {
                    currAddBtnBlockIds.forEach(function (initBlockJson) {
                        var initBlockIdPrefix = initBlockJson["blockid"].replace(/_btn[\d]+_.*/g, "");
                        if (tmpBlockId.match(`${initBlockIdPrefix}`) && initBlockJson["hasAdded"] === true) {
                            //表示有增加過
                            isIniAddBtn = true; throw BreakException;
                        }
                    });
                } catch (e) {
                    if (e !== BreakException) console.log(e);
                    else return false;
                }
            }
            //判斷[增加BTN]當為增加過的block、且不屬於當前check block時，for迴圈continue
            if (tmpBlockId && currAddBtnBlockId && currAddBtnBlockId !== tmpBlockId) {
                isIniAddBtn = true; return false;
            }
        });

        if (!isIniAddBtn) {
            if (parseInt(tmpRow) >= parseInt(lRow)) {
                if (parseInt(tmpRow) > parseInt(lRow) && parseInt(tmpCol) === parseInt(lCol)) {
                    break;
                } else {
                    var inOpenBtn = undefined, inTmpCol = "";
                    $.each($(item).children(), function (j, childItem) {
                        var tmpChildItem = $(childItem);
                        if (tmpChildItem.is("input:button") && tmpChildItem.attr("displayname") && tmpChildItem.attr("displayname").match(/展開|增加/g)) {
                            //內部[展開BTN]的處理
                            inOpenBtn = tmpChildItem; inTmpCol = inOpenBtn.attr("col"); return false;
                        }
                        else if (tmpChildItem.is(":checkbox") && tmpChildItem.attr("onclick") && tmpChildItem.attr("onclick").indexOf("OpenDetail") > -1) {
                            //[展開BTN]內部 checkbox的處理
                            inOpenBtn = tmpChildItem; inTmpCol = inOpenBtn.attr("col"); return false;
                        }
                        else if (tmpChildItem.attr("block") && tmpChildItem.attr("block").match(/button_.+_btn0_.+/g) && tmpChildItem.is(":hidden")) {
                            //判斷[增加BTN]初始區塊是否顯示
                            if ($(t).attr("block") && $(t).attr("block").match(/button_.+_btn[1-9][\d]*_.+/g)) { isIniAddBtn = true; return false; }
                        }
                    });
                    if ((inOpenBtn !== undefined && parseInt(inTmpCol) !== parseInt(lCol)) || isIniAddBtn)
                        continue;
                    else if (parseInt(tmpRow) > parseInt(lRow) || (parseInt(tmpRow) === parseInt(lRow) && parseInt(tmpCol) > parseInt(lCol))) {
                        if (nowVal === true) {
                            $(item).css("display", "");
                        } else {
                            $(item).css("display", "none");

                            if ($(item.children[1]).attr("onchange") && $(item.children[1]).attr("onchange").indexOf("DisplayItem") > -1) {
                                setBindingItemValue(item.children[1].id, "");
                                eval($(item.children[1]).attr("onchange").replace(/\(this/g, "(item.children[1]"));
                            }
                            else if ($(item.children[0]).attr("onchange") && $(item.children[0]).attr("onchange").indexOf("DisplayItem") > -1) {
                                setBindingItemValue(item.children[0].id, "");
                                eval($(item.children[0]).attr("onchange").replace(/\(this/g, "(item.children[0]"));
                            }
                        }
                    }
                }
            }
        }
    }
}

//toggle (展開按鈕用)
function ToggleBlock(t, currVal, isFormShow) {
    var block = $(t).parents("fieldset");
    var lCol = $(t).attr("col");
    var lRow = $(t).attr("row");
    var isOpenDetailChecked = false; //是否OpenDetail有勾選
    var isAjccBlock = false; //是否為AJCC期別區塊
    var tmpAjccType = "", ajccShowFlag = false;
    var currAddBtnBlockIds = findAddedBlockids(block);

    for (var i = 0; i < block.children().length; i++) {
        var item = block.children()[i]; //DIV
        var tmpCol = $(item).attr("col");
        var tmpRow = $(item).attr("row");

        var isIniAddBtn = false, isCurrAddedBtn = false, isOtherAddedBtn = false;
        $.each($(item).children(), function (j, childItem) {
            var tmpChildItem = $(childItem), tmpBlockId = "", tmpMaxBlockId = "";
            isIniAddBtn = false;
            tmpBlockId = tmpChildItem.attr("block");
            //判斷[增加BTN]初始區塊是否顯示(有增加過、且為初始區塊時，for迴圈continue)
            if (tmpChildItem.is("input:button[displayname='增加'][value='+']")) {
                return false;
            }
            else if (tmpBlockId && tmpBlockId.match(/button_.+_btn0_.+/g) && tmpChildItem.is(":hidden")) {
                var BreakException = {};
                try {
                    currAddBtnBlockIds.forEach(function (initBlockJson) {
                        var initBlockIdPrefix = initBlockJson["blockid"].replace(/_btn[\d]+_.*/g, "");
                        if (tmpBlockId.match(`${initBlockIdPrefix}`) && initBlockJson["hasAdded"] === true) {
                            //表示有增加過
                            isIniAddBtn = true;
                            throw BreakException;
                        }
                    });
                } catch (e) {
                    if (e !== BreakException) console.log(e);
                    else return false;
                }
            }
            else if (tmpBlockId && tmpBlockId.match(/button_.+_btn[\d]+_.+/g)) {
                isOtherAddedBtn = true;
                var currBlockId = $(t).attr("block");
                if (currBlockId === tmpBlockId) {
                    //判斷[增加BTN]增加過的區塊是否顯示(若非當前序號的[增加BTN]控制區塊時，for迴圈continue)
                    isCurrAddedBtn = true; return false;
                }
            }
        });

        if (isIniAddBtn) {
            if (isFormShow) $(item).css("display", "none");
        }
        else if (parseInt(tmpRow) >= parseInt(lRow) || isCurrAddedBtn) {
            if (parseInt(tmpRow) > parseInt(lRow) && parseInt(tmpCol) === parseInt(lCol)) {
                if (isOtherAddedBtn && !isCurrAddedBtn) continue;
                else break;
            } else {
                var inOpenBtn = undefined, inTmpCol = "";
                $.each($(item).children(), function (j, childItem) {
                    var tmpChildItem = $(childItem), tmpBlockId = "", tmpMaxBlockId = "";
                    if (childItem.innerText.match(/AJCC[\d]/g)) {
                        tmpAjccType = childItem.innerText.match(/AJCC[\d]/g)[0].trim();
                        isAjccBlock = true;
                    }
                    if (tmpChildItem.is("input:button") && tmpChildItem.attr("displayname") && tmpChildItem.attr("displayname").match(/展開|增加/g)) {
                        //內部[展開BTN]的處理
                        inOpenBtn = tmpChildItem; inTmpCol = inOpenBtn.attr("col"); return false;
                    }
                    else if (tmpChildItem.is("input:checkbox") && tmpChildItem.attr("onclick") && tmpChildItem.attr("onclick").indexOf("OpenDetail") > -1) {
                        //[展開BTN]內部 checkbox的處理
                        inOpenBtn = tmpChildItem; inTmpCol = inOpenBtn.attr("col");
                        if (tmpChildItem.val() === "true" || tmpChildItem.val() === true) isOpenDetailChecked = true;
                        else isOpenDetailChecked = false;
                        return false;
                    }
                });

                if (inOpenBtn !== undefined && parseInt(inTmpCol) !== parseInt(lCol))
                    continue;
                else if (parseInt(tmpRow) > parseInt(lRow) && parseInt(tmpCol) >= parseInt(lCol) ||
                    parseInt(tmpRow) === parseInt(lRow) && parseInt(tmpCol) > parseInt(lCol)) {
                    if (isFormShow) {
                        if (isOpenDetailChecked)
                            $(item).css("display", "");
                        else {
                            $(item).css("display", "none");
                        }
                    } else {
                        if (isAjccBlock) {
                            // for AJCC期別區塊
                            if (sAJCC === "") sAJCC = getAJCC_Version(sDiagnosisDate);
                            //判斷相同期別則顯示
                            if (tmpAjccType === sAJCC) $(item).toggle();
                        } else
                            $(item).toggle();
                    }
                }
            }
        }
    }
}

//取得重組過的jquery datatable欄位名稱
function getRecomposedColumn(oriColNameArr, replaceTag) {
    return oriColNameArr[0].trim().replace(replaceTag, "");
}


function getResultList2000() {
    setTimeout($("input[type='button'][onclick*='getResultList']").click(), timeOutTime);
}


//取得處理完成的ResultList資料(將錯誤的row剔除)
function getHandledData(jsonArrData) {
    var newArrData = [];
    var tmpJsonArr = [];
    var tmpResultListItems = resultListItems;

    if (resultListItems && resultListItems.length > 0) {
        //若原jsonArrData有resultdata的key，則需重組array
        if (jsonArrData.length > 0 && jsonArrData[0].hasOwnProperty("resultdata")) {
            $.each(jsonArrData, function (i, obj) {
                tmpJsonArr.push(obj.resultdata);
            });
        } else {
            tmpJsonArr = jsonArrData;
        }

        //resultListItems有值
        $.each(tmpJsonArr, function (i, obj) {
            var isAllMatched = true;
            //判斷是否均符合datatable的key
            $.each(resultListItems, function (j, itemStr) {
                var tmpMatchStr = itemStr.replace("#", "").replace(/[SELECT|LINK|BTN|□|asc|desc]+/gi, "").trim();
                if (!obj.hasOwnProperty(itemStr) && !obj.hasOwnProperty(tmpMatchStr)) {
                    isAllMatched = false;
                    return;
                }
            });

            //若key均符合，則加入欲顯示的array data
            if (isAllMatched) {
                //需要Deep Copy，否則newArrData每個物件值都只有null
                var tmpObj = $.extend(true, {}, obj);
                newArrData.push(tmpObj);
            }
        });
    }

    return newArrData;
}


/**
 * 處理ResultList該筆選取的row資料
 * @param {any} rowItem      自定義參數
 * @param {any} selectDom    this
 * @param {any} $jqTb        指定jQuery Table
 * @returns {any}            NULL
 */
function setResultList(rowItem, selectDom, $jqTb) {
    Spinner.show();

    var currSelectRowStr = rowItem.replace(/\|/gi, '"').replace(/\n/gi, '\\n').replace(/\r/gi, '\\r');
    console.log("currSelectRowStr ==="+currSelectRowStr);
    var currSelectRowObj = JSON.parse(currSelectRowStr);
    var funcType = selectDom.value ? selectDom.value : $(selectDom).attr("value");
    var currPKey, tmpCancerType, tmpLocStgKey, tmpFileName, resWaitRows;
    var path, qryRS, pathRS;
    console.log(dataType);

    switch (funcType) {
  //非制式客製化-EN 刪除下載按鈕

    	case "Delete刪除":
        case "刪除":
			if(dataType.indexOf("附件資料")>-1)
			{
				console.log("currSelectRowObj.id =="+currSelectRowObj.id);
				currPKey = currSelectRowObj.id
				if(currSelectRowObj.id.match(/fix/g)){
					DeleteResult(dataType, currPKey, setToBackResult, tmpFileName, currSelectRowObj.dataid);
				}
				else{
					DeleteFile(dataType, currPKey, setToBackResult);
				}
			}
            break;
    	case "Download下載":
		case "下載":
			if(dataType.indexOf("附件資料")>-1)
			{
				console.log("currSelectRowObj ==="+currSelectRowObj.id);

				currPKey = currSelectRowObj.id
				DownloadFile(dataType, currPKey);
			}
			break;
        case "勾選":
            if (cluster === "REV" && masterSpec.match(/(REGQRY|REGSEND)/)) {
                if (masterSpec.match(/(REGSEND)/)) {
                    //$(".Doctor_Type").parent().next().find('input').each(function () {
                    //[雲端]
                    $('label:contains("審查醫師類型")').parent().find('input').each(function () {
                        if ($(this).is(':checked')) Doctor_Type = $(this).val();
                    });
                }
                var option_value = "";
                if (!selectOptionsObj.hasOwnProperty(funcType)) {
                    selectOptionsObj[funcType] = [];
                    if (masterSpec.match(/(REGSEND)/)) {
                        option_value = currSelectRowObj["4-2-1-3放射治療開始日期"];
                        if (Doctor_Type.indexOf("RT") > -1 && option_value === "") { //RT醫師，[4-2-1-3放射治療開始日期]欄位必須有日期
                            FixSysMsg.danger("病歷號: " + currSelectRowObj["1-2病歷號"] + "，無 4-2-1-3放射治療開始日期");

                            resWaitRows = $("#json_query").find("tbody > tr");
                            if (resWaitRows.length > 0) {//取消勾選
                                $.each(resWaitRows, function (i, rowObj) {
                                    var item = $(rowObj).find(`td:eq(0)`).children()[0];
                                    if (item !== undefined)
                                        if (item.dataset !== "" && item.dataset.rowdata !== undefined)
                                            if (JSON.parse(item.dataset.rowdata.replace(/\|/gi, '"'))["chooseid"].toString() === currSelectRowObj.chooseid.toString()) item.checked = false;
                                });
                            }
                            break;
                        }

                    }
                    selectOptionsObj[funcType].push(currSelectRowObj);
                }
                else {
                    var countIndex = 0;
                    var Index = 0;
                    selectOptionsObj[funcType].filter(function (value) {
                        countIndex++;
                        if (value["dataid"] === currSelectRowObj["dataid"]) Index = countIndex;
                    });
                    if (Index === 0) {
                        if (masterSpec.match(/(REGSEND)/)) {
                            option_value = currSelectRowObj["4-2-1-3放射治療開始日期"];
                            if (Doctor_Type.indexOf("RT") > -1 && option_value === "") { //RT醫師，[4-2-1-3放射治療開始日期]欄位必須有日期
                                FixSysMsg.danger("病歷號: " + currSelectRowObj["1-2病歷號"] + "，無 4-2-1-3放射治療開始日期");

                                resWaitRows = $("#json_query").find("tbody > tr");
                                if (resWaitRows.length > 0) {//取消勾選
                                    $.each(resWaitRows, function (i, rowObj) {
                                        var item = $(rowObj).find(`td:eq(0)`).children()[0];
                                        if (item !== undefined)
                                            if (item.dataset !== "" && item.dataset.rowdata !== undefined)
                                                if (JSON.parse(item.dataset.rowdata.replace(/\|/gi, '"'))["chooseid"].toString() === currSelectRowObj.chooseid.toString()) item.checked = false;
                                    });
                                }

                                break;
                            }
                        }
                        selectOptionsObj[funcType].push(currSelectRowObj);

                    }
                    else selectOptionsObj[funcType].splice(Index - 1, 1);
                }
            }

            break;
        case "選取":
            if (!selectOptionsObj.hasOwnProperty(funcType)) selectOptionsObj[funcType] = [];
            selectOptionsObj[funcType].push(currSelectRowObj);

            break;
    }

    Spinner.hide();
}


//刪除功能
function delData(id, code, callback) {
    if (!confirm('是否刪除')) return;

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "AJCC003",
        "data": {
            "module": module,
            "cluster": cluster,
            "class": ajccType,
            "spec": masterSpec,
            "func": "delete",
            "dataid": id,
            "doccode": code
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    var url = getAPIURL(`${cluster}/Update`);
    //if (cluster === 'MAINT') url = getAPIURL(`CR/Update`);

    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            console.log(response);
            if (response.rtnCode === 0) {
                callback();
                FixSysMsg.success("刪除成功");
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


//判斷[問卷 eg.審查表]所需連動的控制項範圍
function getJump(nowDomName, nowVal, refProcess) {
    //eg. 請跳至第19項
    //eg. Ref.Q14;enableTo,是,Ref.Q19;disableTo,否,Ref.Q19
    var procArr = refProcess.indexOf(";") > -1 ? refProcess.split(";") : [refProcess];
    getvalue();

    for (var i = 0; i < procArr.length; i++) {
        var callFuncArr = procArr[i].indexOf(",") > -1 ? procArr[i].split(",") : [procArr[i]];
        var callFuncStr = callFuncArr[0];
        var comparedVal = callFuncArr.length > 1 ? callFuncArr[1] : "";
        var reactItemStr = callFuncArr.length > 2 ? callFuncArr[2] : "";

        if (comparedVal !== "") {
            if (reactItemStr !== "") {
                if (nowVal === comparedVal || nowVal.indexOf(comparedVal) === 0) {
                    eval(`${callFuncStr}("${nowDomName}", "${reactItemStr}", focusItem)`);
                    break;
                }
            }
            else eval(`${callFuncStr}()`);
        }
        else {
            if (callFuncStr.indexOf("Ref.") === -1) eval(`${callFuncStr}()`);
        }
    }
}


//nowDomName: 目前選擇的控制項
//refTag: 設計書-標記 (Remark) 欄位內容 eg. Ref.Q14
function enableTo(nowDomName, refTag, callback) {
    //取得Json內對應key、value的element
    var refJsonItem = getJsonMappingElement("ref", refTag, dataType, true);
    console.log(refJsonItem);
    if (refJsonItem) {
        var labelText = refJsonItem.segmentation || refJsonItem.item;
        //取得需連動範圍內的所有input控制項
        //$("#divbody > blockquote > blockquote > blockquote > blockquote > label")
        var reactDom = $(`input[data-bind='checked:${nowDomName}']`).parentsUntil("blockquote").parent("blockquote");
        if (reactDom.length > 0) {
            var parentDom = reactDom.parent();
            while (parentDom.length > 0) {
                var searchDoms = parentDom.nextUntil(`label:contains('${labelText}')`);
                if (searchDoms.length > 0) {
                    var isOverRange = false; //超出找尋範圍則跳出while迴圈
                    for (var i = 0; i < searchDoms.length; i++) {
                        if (searchDoms[i].innerText.indexOf(labelText) > -1) isOverRange = true;
                    }
                    if (isOverRange) break;

                    reactDom = searchDoms.find(`input[data-bind!='checked:${nowDomName}']`);
                    reactDom.each(function (i, res) { res.disabled = false; });
                    parentDom = parentDom.parentsUntil("label");
                } else {
                    break;
                }
            }
        }
    }
}


//nowDomName: 目前選擇的控制項
//refTag: 設計書-標記 (Remark) 欄位內容 eg. Ref.Q14
function disableTo(nowDomName, refTag, callback) {
    //取得Json內對應key、value的element
    var refJsonItem = getJsonMappingElement("ref", refTag, dataType, true);
    console.log(refJsonItem);
    if (refJsonItem) {
        var labelText = refJsonItem.segmentation || refJsonItem.item;
        //取得需連動範圍內的所有input控制項
        var reactDom = $(`input[data-bind='checked:${nowDomName}']`).parentsUntil("blockquote").parent("blockquote");
        if (reactDom.length > 0) {
            var parentDom = reactDom.parent();
            while (parentDom.length > 0) {
                var searchDoms = parentDom.nextUntil(`label:contains('${labelText}')`);
                var focusDom;
                if (searchDoms.length > 0) {
                    var isOverRange = false; //超出找尋範圍則跳出while迴圈
                    for (var i = 0; i < searchDoms.length; i++) {
                        if (searchDoms[i].innerText.indexOf(labelText) > -1) {
                            isOverRange = true;
                            for (var j = 0; j < searchDoms[i].children.length; j++) {
                                focusDom = $(searchDoms[i].children[j].outerHTML);
                                if (focusDom.find("input").length > 0) {
                                    callback(focusDom.find("input:first"));
                                }
                            }
                            break;
                        }
                    }
                    if (isOverRange) break;

                    reactDom = searchDoms.find(`input[data-bind!='checked:${nowDomName}']`);
                    reactDom.each(function (i, res) { res.disabled = true; });
                    parentDom = parentDom.parentsUntil("label");
                }
                else {
                    focusDom = $(`label:contains('${labelText}')`).next();
                    if (focusDom.find("input").length > 0) {
                        callback(focusDom.find("input:first"));
                    }
                    break;
                }
            }
        }
    }
}


//游標移動至指定控制項
function focusItem(item) {
    var currName = item[0].name;
    var currId = item[0].id;
    var currBindName = item[0].getAttribute("data-bind");
    console.log(`[focusItem] ${currName}, ${currId}, ${currBindName}`);
    if (currBindName !== "")
        $(`input[data-bind='${currBindName}']`).focus();
    else if (currName !== "")
        $(`input[name='${currName}']`).focus();
    else if (currId !== "")
        $(`#${currId}`).focus();
}


//(Todo)
function onEnable(nowBindName, nowVal, reactText, refText) {
    console.log(`[onEnable] ${nowBindName}, ${nowVal}, ${reactText}, ${refText}`);
    getvalue();
    //取得Json內對應key、value的element
    var refJsonItem = getJsonMappingElement("ref", refText, dataType, true);
}


//(Todo)
function onDisable(nowBindName, nowVal, reactText, refText) {
    console.log(`[onDisable] ${nowBindName}, ${nowVal}, ${reactText}, ${refText}`);
    getvalue();
    //取得Json內對應key、value的element
    var refJsonItem = getJsonMappingElement("ref", refText, dataType, true);
}

function getRightsDoctor(tmpTitleVal = "醫師") {
    var isRightDoctor = false;

    if (tmpTitleVal !== "") {
        //var objStr = `?token=${getToken()}&title=${tmpTitleVal}`;
        var obj = {
            "token": getToken(),
            "userId": getUserId(),
            "data": { "title": tmpTitleVal }
        };
        obj = JSON.stringify(obj);

        $.ajax({
            url: getAPIURL("HINTS/GetRightsCaseRole"),
            type: "POST",
            contentType: "application/json",
            data: obj,
            async: false,
            success: function (response) {
                if (response.rtnCode === 0) {
                    var result = JSON.parse(response.jsonData.data);
                    console.log(result);
                    //eval(`selectOptionsObj["通知主治醫師"]=${response.jsonData.data}`);
                    if (result && result.result === true) isRightDoctor = true;
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

    return isRightDoctor;
}

function getRightsCaseNurse(tmpTitleVal = "護士") {
    var isRightNurse = false;

    if (tmpTitleVal !== "") {
        //var objStr = `?token=${getToken()}&title=${tmpTitleVal}`;
        var obj = {
            "token": getToken(),
            "userId": getUserId(),
            "data": { "title": tmpTitleVal }
        };
        obj = JSON.stringify(obj);
        $.ajax({
            url: getAPIURL("HINTS/GetRightsCaseRole"),
            type: "POST",
            contentType: "application/json",
            data: obj,
            async: false,
            success: function (response) {
                if (response.rtnCode === 0) {
                    var result = JSON.parse(response.jsonData.data);
                    console.log(result);
                    //eval(`selectOptionsObj["通知主治醫師"]=${response.jsonData.data}`);
                    if (result && result.result === true) isRightNurse = true;
                } else {
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
                }
            },
            error: function (xhr, status, errorThrown) {
                Spinner.hide();
                //FixSysMsg.danger(`Status: ${status} ${errorThrown}`);
                console.log(`Status:${status} ${errorThrown}`);
                FixSysMsg.danger("Ajax發生錯誤!");
            }
        });
    }

    return isRightNurse;
}

//取得[基本資料]
function getSourceData(json_data_docdetail, lsKey) {
    var rtnItem;
    var findLsKey = lsKey && lsKey.indexOf("localStorage") === 0 ? lsKey.split(".")[1] : undefined;
    var path = $.JSONPath({ data: json_data_docdetail, keepHsitory: false });
    var qryRS = path.query(`$.[?(@.resultdata)]`)[0];
    if (qryRS !== undefined)
        rtnItem = qryRS.resultdata;
    else {
        var sessionItem = sessionStorage.getItem(`${ajccType}_${masterSpec}_${cluster}`);
        if (sessionItem) rtnItem = JSON.parse(sessionItem);
    }

    //儲存於LocalStorage
    if (findLsKey) {
        if (rtnItem !== undefined) {
            SetMainProcessPara("setLocalStorage", { "key": findLsKey, "value": JSON.stringify(rtnItem) });
            MainProcess();
        } else {
            SetMainProcessPara("getLocalStorage", { "key": findLsKey });
            rtnItem = MainProcess();
            if (rtnItem !== undefined) rtnItem = JSON.parse(rtnItem);
        }
    }

    return rtnItem;
}

//填入[基本資料](eg. #SOURCE.分組)
function setSourceData(matchName, lsKey) {
    var rtnVal = "";
    var findLsKey = lsKey;

    if (findLsKey && typeof findLsKey === "string") {
        //mainProcessPara.jsonPara["key"] = findLsKey.indexOf("SOURCE") > -1 ? findLsKey.indexOf("#") > -1 ? findLsKey : `#${findLsKey}` : findLsKey;
        SetMainProcessPara("getLocalStorage", { "key": findLsKey.indexOf("#") > -1 ? findLsKey : `#${findLsKey}` });
        var sourceJson = MainProcess();
        if (sourceJson === undefined || sourceJson === null) {
            SetMainProcessPara("getLocalStorage", { "key": findLsKey });
            sourceJson = MainProcess();
        }

        if (sourceJson !== undefined && sourceJson !== null) {
            sourceJson = JSON.parse(sourceJson);
            var tmpMatchName = matchName.replace(/\:|\：/g, "").trim();
            Object.keys(sourceJson).forEach(function (resKey, i) {
                if (resKey === tmpMatchName || resKey.indexOf(tmpMatchName) > -1 && module !== "CS") {
                    if (resKey.indexOf("性別") > -1 && sourceJson.hasOwnProperty("性別")) {
                        rtnVal = sourceJson["性別"];
                        if (rtnVal === "1") rtnVal = `男`;
                        else if (rtnVal === "2") rtnVal = `女`;
                    } else
                        rtnVal = sourceJson[resKey];
                } else {
                    //特殊案例處理
                    if (tmpMatchName.indexOf("案號") > -1 && sourceJson.hasOwnProperty("案號"))
                        rtnVal = sourceJson["案號"];
                    else if (tmpMatchName.indexOf("身分證") > -1 && sourceJson.hasOwnProperty("身分證號"))
                        rtnVal = sourceJson["身分證號"];
                    else if (tmpMatchName.indexOf("出生日期") > -1 && sourceJson.hasOwnProperty("出生日期")) {
                        rtnVal = sourceJson["出生日期"];
                        //[MANTIS:0025481]民國年轉西元年表示
                        if (rtnVal.length === 7 || rtnVal.indexOf("0") === 0) {
                            var tmpY = rtnVal.substring(0, 3);
                            if (tmpY !== undefined) rtnVal = (parseInt(tmpY) + 1911).toString() + rtnVal.substring(3, 7);
                        }
                    } else if (tmpMatchName.indexOf("性別") > -1 && sourceJson.hasOwnProperty("性別")) {
                        rtnVal = sourceJson["性別"];
                        if (rtnVal === "1") rtnVal = `男`;
                        else if (rtnVal === "2") rtnVal = `女`;
                    }
                }

                return false;
            });
        }
    }
    return rtnVal;
}

var SelectDetail = {};
//下拉選單onchange時處理
//onchangeKey: 目前下拉選單的data-bind名稱
//value: 目前下拉選單選擇的值
//refName: 需暫存的ref名稱
function setSelectDetail(onchangeKey, value, refName) {
    if (refName && refName === "SelectDetail") {
        //SelectDetail[refName] = value;
        setJsonMappingVal("js", `set${refName}`, value, onchangeKey);
    }
}

//下拉選單onchange時，寫入對應"代碼"至refName目標input控制項
//onchangeKey: 目前下拉選單的data-bind名稱
//value: 目前下拉選單選擇的值
//refName: ref名稱 eg. 代碼類別、癌別代碼
function setSelectCode(onchangeKey, value, refName) {
    var comparedKey = refName.indexOf("代碼") <= 0 ? `${refName}中文` : `${refName.replace("代碼", "")}中文`;
    var targetLbl = $(`label:contains('${refName}')`); //對應refName的目標label
    var targetNewVal = "";

    //針對refName有[癌別]時做特別處理
    if (refName.indexOf("癌別") === 0) {
        if (value && value.indexOf("團隊") > -1) comparedKey = "癌別團隊";
    }

    if (targetLbl.length > 0) {
        var targetItem;
        $.each(targetLbl, function (i, lblObj) {
            if (lblObj.innerText.replace(/\:|\：/g, "") === refName) targetItem = $(lblObj).next("div").find("input");
        });

        if (targetItem && targetItem.length > 0) {
            $.each(selectOptionsObj, function (i, obj) {
                if (obj && obj.length) {
                    for (var j = 0; j < obj.length; j++) {
                        var comparedVal = "";
                        if (value && refName.indexOf("癌別") === 0) {
                            var tmpVal1, tmpVal2;
                            if (value.indexOf("團隊") > -1) {
                                tmpVal1 = eval(`obj[j]["${comparedKey}中文"]`);
                                tmpVal2 = eval(`obj[j]["${comparedKey}代碼"]`);
                            }
                            else {
                                tmpVal1 = eval(`obj[j]["${comparedKey}"]`);
                                tmpVal2 = eval(`obj[j]["${refName}"]`);
                            }
                            comparedVal = `${tmpVal1}(${tmpVal2})`;
                            if (value === comparedVal) {
                                tmpVal1 = eval(`obj[j]["${refName.replace("代碼", "")}中文"]`);
                                tmpVal2 = eval(`obj[j]["${refName}"]`);
                                targetNewVal += "," + `${tmpVal1}(${tmpVal2})`;
                            }
                        }
                        else {
                            comparedVal = eval(`obj[j]["${comparedKey}"]`);
                            if (!comparedVal) {
                                if (refName.indexOf("癌別") === -1) targetNewVal = value;
                            } else {
                                if (value && value === comparedVal) {
                                    targetNewVal = obj[j][refName];
                                    break;
                                }
                            }
                        }
                    }
                    if (targetNewVal !== "") {
                        if (targetNewVal.indexOf(",") > -1) targetNewVal = targetNewVal.substring(1);
                        return;
                    }
                }
            });
            eval(`viewModel.${targetItem.attr("data-bind").replace(/(checked|value)\:/g, "").trim()}("${targetNewVal}");`);
        }
    }
}

//上傳檔案
function uploadRecord(fileId, btnName, patientId) {
    var checkFileName = document.getElementById(`${fileId}`).files[0];
    if (!checkFileName) {
        FixSysMsg.danger("請選擇檔案上傳");
        return false;
    }

    var _fModule = "CM"; //(Todo)目前取不到
    var _fCluster = cluster;
    var _fAjccType = ajccType;
    var _fSpec = masterSpec;
    var _fUplDataid = json["data"][0]["dataid"];
    var _fFormDom = $(`#form_${fileId}`);

    if (_fFormDom.length > 0) {
        //產生表單post後、server處理所需的隱藏欄位
        if (_fFormDom.find(`#token`).length === 0) _fFormDom.append(`<input type="hidden" id="token" name="token" value="${getToken()}">`);
        if (_fFormDom.find(`#userId`).length === 0) _fFormDom.append(`<input type="hidden" id="userId" name="userId" value="${getUserId()}">`);
        if (_fFormDom.find(`#fUplModule`).length === 0) _fFormDom.append(`<input type="hidden" id="fUplModule" name="fUplModule" value="${_fModule}">`);
        if (_fFormDom.find(`#fUplCluster`).length === 0) _fFormDom.append(`<input type="hidden" id="fUplCluster" name="fUplCluster" value="${_fCluster}">`);
        if (_fFormDom.find(`#fUplClass`).length === 0) _fFormDom.append(`<input type="hidden" id="fUplClass" name="fUplClass" value="${_fAjccType}">`);
        if (_fFormDom.find(`#fUplSpec`).length === 0) _fFormDom.append(`<input type="hidden" id="fUplSpec" name="fUplSpec" value="${_fSpec}">`);
        if (_fFormDom.find(`#fUplDataid`).length === 0) _fFormDom.append(`<input type="hidden" id="fUplDataid" name="fUplDataid" value="${_fUplDataid}">`);
        if (_fFormDom.find(`#fUplPatientId`).length === 0) _fFormDom.append(`<input type="hidden" id="fUplPatientId" name="fUplPatientId" value="${patientId}">`);

        var _fFormData = new FormData(_fFormDom[0]);
        $.ajax({
            url: getAPIURL("FILE/UploadRecord"),
            type: "POST",
            data: _fFormData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                console.log(data);
                if (data.rtnCode === 0) {
                    //更新上傳檔案註記
                    TMP_CONST_KEY_UPLOADFILE = btnName;
                    if (TMP_CONST_KEY_UPLOADFILE === undefined) TMP_CONST_KEY_UPLOADFILE = "上傳檔案";
                    selectOptionsObj[TMP_CONST_KEY_UPLOADFILE] = true;
                    FixSysMsg.success("上傳成功");
                } else {
                    FixSysMsg.danger("上傳失敗");
                }
            },
            complete: function (xhr, status) {
                Spinner.hide();
            }
        });
    }
}


//下載檔案
function downloadFileRecord(fModule, fCluster, fClass, fSpec, fExtend, fDataid, fPatientId, filename, callback) {
    var downloadObj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "DL_FILE",
        "data": {
            "Module": fModule,
            "Cluster": fCluster,
            "Class": fClass,
            "Spec": fSpec,
            "Extend": fExtend,
            "Dataid": fDataid,
            "PatientId": fPatientId,
            "Filename": filename
        }
    };
    downloadObj = JSON.stringify(downloadObj);
    console.log(downloadObj);
    $.ajax({
        url: getAPIURL(),
        data: downloadObj,
        contentType: "application/json",
        type: "POST",
        dataType: 'json',
        success: function (response) {
            if (response.rtnCode === 0) {
                if (response.jsonData.data) {
                    response = JSON.parse(response.jsonData.data);
                    console.log(response);
                    callback(response.downloadPath); //callback: getRecordFile
                } else
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


//下載檔案(回傳實體檔案)
function getRecordFile(key, seqId) {
    if (key && key !== "")
        winUrl = `${getApiUrl()}${path_API_Version}FILE/DownloadRecord?file=${encodeURIComponent(key)}`
    else if (seqId && seqId !== "")
        winUrl = `${getApiUrl()}${path_API_Version}FILE/DownloadRecord?seqId=${seqId}`
    window.open(winUrl);
}


//刪除上傳的實體檔案 by dataid
function deleteUploadFile(currDataid, delFilename) {
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "FIX_DELFILE",
        "data": {
            "module": module,
            "cluster": cluster,
            "class": ajccType,
            "spec": masterSpec,
            "func": dataType,
            "dataid": currDataid,
            "extend": delFilename
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);
    console.log("============deleteUploadFile=============");

    $.ajax({
        url: getAPIURL(`${cluster}/FixDelFile`),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
            if (response.rtnCode === 0) {
                var passJVal = response.jsonData.data;
                console.log(passJVal);
                FixSysMsg.success("刪除記錄成功");
            } else
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
}

//進入[新增]頁
function GetAdd(cancerType, inJson, spec) {
    var tmpJsonObj;
    if (cancerType !== undefined) {
        //[診療計畫書]
        if (inJson !== undefined) {
            SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_AjccUnReceivePlan, "value": inJson });
            MainProcess();
        }

        //清除localStorage的"初診斷日期"
        SetMainProcessPara("getLocalStorage", { "key": "初診斷日期" });
        sDiagnosisDate = MainProcess();
        if (sDiagnosisDate !== undefined && sDiagnosisDate !== "") {
            SetMainProcessPara("clearLocalStorage", { "key": "初診斷日期", "isClearAll": "false" });
            MainProcess();
        }

        var currSelectRowStr = inJson.replace(/\|/gi, '"');
        inJson = currSelectRowStr;

        tmpJsonObj = JSON.parse(inJson);
    }

    if (cluster === "MST") {
        //[多專科團隊] 舊CRM
        if (tmpJsonObj.dataid.match(/^CRM_/))
            tmpJsonObj.SEQ_ID = tmpJsonObj.dataid.trim();
        window.location = `Add?ajccType=${cancerType === undefined ? ajccType : cancerType}&spec=${spec === undefined ? masterSpec : spec}` + (tmpJsonObj && tmpJsonObj.SEQ_ID ? `&SEQ_ID=${tmpJsonObj.SEQ_ID}` : "") + (tmpJsonObj && tmpJsonObj["#會議日期LINK"] ? `&mstDate=${tmpJsonObj["#會議日期LINK"]}` : "") + (tmpJsonObj && tmpJsonObj.團隊名稱 ? `&mstName=${tmpJsonObj.團隊名稱}` : "");
    }
    else
        window.location = `Add?ajccType=${cancerType === undefined ? ajccType : cancerType}&spec=${spec === undefined ? masterSpec : spec}` + (tmpJsonObj && tmpJsonObj.SEQ_ID ? `&SEQ_ID=${tmpJsonObj.SEQ_ID}` : "");
}

//進入[編輯]頁
function GetEdit(id, code, cancerType, inJson, datatype, seq_id) {
    //[診療計畫書]
    if (inJson !== undefined) {
        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_AjccUnReceivePlan, "value": inJson });
        MainProcess();
        SetMainProcessPara("clearLocalStorage", { "key": "初診斷日期", "isClearAll": "false" });
        MainProcess();
    }

    if (cluster === "MST" && masterSpec === "REC") masterSpec = code.split("-")[1];
    else if (cluster === "TP" && masterSpec === "CHECKLIST") masterSpec = code.split("-")[0];

    window.location = `Edit?ajccType=${cancerType === undefined ? "QRY" : cancerType}&spec=${masterSpec}&id=${id}&code=${code}` + (datatype === undefined ? "" : `&datatype=${datatype}`) + (seq_id === undefined ? "" : `&SEQ_ID=${seq_id}`);
}

/**
 * 進入[查詢]頁
 * @param {any} cluster cluster
 * @param {any} type    ajccType
 * @param {any} spec    masterSpec
 */
function GetQuery(cluster, type, spec) {
    if (type === undefined) type = ajccType;
    if (spec === undefined) spec = masterSpec;
    window.location.href = `Query?ajccType=${type}&spec=${spec}`;
}


function URLWindowOpen(winUrl, funcType) {
    var win_width = 800;
    var win_height = 680;
    var PosX = screen.width - win_width;
    var config = `left=${PosX},top=0,width=${win_width},height=${win_height},location=no,menubar=no,status=no,toolbar=no,addressbar=no`;
    var winOpen = window.open(winUrl, "_blank", config);

    if (location.href.indexOf("Readonly?") > -1) {
        if (cluster === 'MST' && funcType === 'FileList') {
            winOpen.onload = function () {
                //舊CRM
                // 將系統頁帶入的menu(左選單)、header(標頭)、footer(標頭)隱藏
                $(winOpen.document).find("div[class='navbar-header']").find("button").remove();
                $(winOpen.document).find("div[class*='navbar-brand ']").css("position", "relative");
                $(winOpen.document).find("div[class='site-menubar'], div[class='site-footer']").remove();
            };
        } else
            location.href = winUrl;
    }
    else {
        winOpen.onload = function () {
            //舊CRM
            if (funcType === "CRM2TP_URL") {
                $(winOpen.document).find("td#td_TagHome table tr td:nth-child(8) a").click();
                $(winOpen.document).find("td#td_TagHome").hide();
            }
            //else if (funcType.match(/RQS|RTSummary/)) {
            //}
            else {
                // 將系統頁帶入的menu(左選單)、header(標頭)、footer(標頭)隱藏
                $(winOpen.document).find("div[class='navbar-header']").find("button").remove();
                $(winOpen.document).find("div[class*='navbar-brand ']").css("position", "relative");
                $(winOpen.document).find("div[class='site-menubar'], div[class='site-footer']").remove();
            }
        };
    }
}


//開窗[唯讀]頁
function GetViewReadonly(id, code, funcType, funcSpec, dataType) {
    var winUrl = "", CHRT = "", arrParams;
    //統一使用window.open
    //id內"CRM_"為開啟[舊CRM]
    //其餘為CQS內開啟[唯讀頁面]
    if (id.match(/^CRM_/g) && code === "") {
        CHRT = $("#msgBlock1").find("div:contains('病歷號')");
        if (CHRT.length > 0) CHRT = CHRT.text().replace(/病歷號[\:|：]/g, "");

        if (funcType === "RECEMR_URL") { //就醫紀錄
            var $qry = $.Deferred();

            var obj = {
                "token": getToken(),
                "userId": getUserId(),
                "accessCode": "SmarteVision",
                "data": {
                    "module": module,
                    "cluster": cluster,
                    "class": ajccType,
                    "spec": masterSpec,
                    "docver": "7f7334ea-3f18-4901-8fcd-a8fb4e56ff09", //Folder
                    "func": "55ecb5be-e070-487c-ba88-2d95a1bff20a",   //Page(就醫紀錄)
                    "docdetail": [{ "VIS_CHRT1": CHRT }]
                }
            };
            obj = JSON.stringify(obj);
            console.log(obj);

            $.ajax({
                url: getAPIURL("SEVI/Connect"),
                type: "POST",
                contentType: "application/json",
                data: obj,
                success: function (response) {
                    $qry.resolve(response);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $qry.resolve({ "rtnCode": jqXHR.status, "message": (textStatus === "" ? "" : "[" + textStatus + "]") + errorThrown, "jsonData": {} });
                }
            });

            $.when($qry).done(function (_qry) {
                if (_qry.rtnCode === 0) {
                    var result = _qry.jsonData.data;
                    console.log(result);
                    result = JSON.parse(result);
                    winUrl = result.connURL;
                    URLWindowOpen(winUrl, funcType);
                }
                else {
                    console.log(_qry);
                    FixSysMsg.danger(`${_qry.rtnCode}-${_qry.message}`);
                }
            });
        }
        //else if (funcType === "QRY") {
        //    //20191112 多專科側邊選單
        //    arrParams = id.split('_');
        //    winUrl = `../MST/Readonly?ajccType=REC&spec=MST001&SEQ_ID=${id}&mstDate=${arrParams[2]}&mstName=${dataType}`;
        //    URLWindowOpen(winUrl, funcType);
        //}
        else {
            winUrl = `${$(`#${funcType}`).val()}?`;

            switch (funcType) {
                case "CDHEBD_URL":  //個管服務
                    arrParams = TrimAllBlank(id, "g").split('_');
                    arrParams[2] = getFormatDateStr(5, arrParams[2]);
                    winUrl += `ActP=CRMTSERV&csno=${arrParams[1]}&power=&Dat=${arrParams[2]}&NSEQ=${arrParams[3]}&CHRT=${CHRT}`;
                    break;
                case "EMRQRY_URL":  //病理報告
                    winUrl += `AP=RQS&ID=CRM&CHRT=${CHRT}&DATE=${getToDay(5)}&KIND=`;
                    break;
                case "SURGREC_URL": //手術紀錄
                    winUrl += `chrt=${CHRT}&power=U&ActP=CRM00606&type=OP`;
                    break;
                case "CRMOTH_URL":  //收案紀錄
                    winUrl += `chrt=${CHRT}&power=U&ActP=CRM00619&type=`;
                    break;
                case "CRM2TP_URL":  //舊CRM診療計畫書
                    arrParams = id.split('_');
                    if (cluster === "ORAL")
                        winUrl += `PID=${arrParams[3]}&CHRT=${arrParams[1]}&csno=${arrParams[2]}&CASEKIND=口腔篩檢&NAME=&SEX=${arrParams[4]}&Dept=`;
                    else
                        winUrl += `PID=&CHRT=${arrParams[1]}&csno=${arrParams[2]}&CASEKIND=&NAME=&SEX=`;
                    break;
                case "CRMMSTF_URL": //舊CRM多專科團隊會議記錄
                case "CRMMST_URL":  //舊CRM多專科團隊
                    arrParams = id.split('_');
                    var tmpY = arrParams[2].substring(0, 4);
                    arrParams[2] = (parseInt(tmpY) - 1911).toString() + arrParams[2].substring(4, 8);
                    if (funcType === "CRMMST_URL")
                        winUrl += `ActP=CRMMEET&csno=${arrParams[4]}&power=U&Dat=${arrParams[2]}&NSEQ=${arrParams[3]}&CHRT=${arrParams[5]}`;
                    else if (funcType === "CRMMSTF_URL")
                        winUrl += `csno=${arrParams[4]}&Dat=${arrParams[2]}&type=MEET&team=&CHRT=${arrParams[5]}&acc=&EMID=`;
                    break;
                case "RTDOC_URL":  //放射治療摘,放射計畫書
                    winUrl += `chrt=${CHRT}&csno=${funcSpec}&power=U`;
                    if (dataType === "RTSummary")
                        winUrl += `&ActP=CRM00410&type=IMRT_SUM`;
                    else if (dataType === "RTPlan")
                        winUrl += `&ActP=CRM00423&type=RT_PLAN`;
                    break;
                case "KMMTG_URL": //KM診療指引
                    winUrl += `AccountID=${btoa(getUserId())}&Parameter=Decode&Link=SVNPX1R5cGVfTGlzdDRFSVAuYXNweD9UeXBlX0lEPVdlYklTTw==`;
                    break;
                case "TNMSTAGE_URL": //電子病歷首頁-TNM期別
                    winUrl = getAPIURL(`v1/EMR/TNMStage?CHRT=${CHRT}`);
                    break;
            }

            URLWindowOpen(winUrl, funcType);
        }
    }
    else {
        if (cluster === "COL" && id.match(/^TP/g))
            tmpCluster = "TP";
        else if (cluster.match(/^COL|TP/g) && id.match(/^MST/g))
            tmpCluster = "MST";
        else
            tmpCluster = cluster;

        winUrl = `../${tmpCluster}/Readonly?ajccType=${funcType}&spec=${funcSpec}&id=${id}&code=${code}${dataType === undefined ? "" : "&datatype=" + dataType}`;

        URLWindowOpen(winUrl, funcType);
    }
}


//存檔前，依條件檢核是否有已存在資料
function CheckExistDataAndSave(callback) {
    var conditionKey = "CheckExistData";

    var jObjDetail;
    if (module === "CS" && ajccType === "NEW" && masterSpec === "REC") {
        jObjDetail = [];
        jObjDetail.push({ "segmentation": "篩檢類別", "data": $(`label:contains('篩檢類別')`).next().val() });
        jObjDetail.push({ "segmentation": "病歷號", "data": $(`label:contains('病歷號')`).next().val() });
    }
    else {
        jObjDetail = selectOptionsObj[conditionKey];
        if (jObjDetail && Array.isArray(jObjDetail)) {
            jObjDetail = jObjDetail.map(function (jVal, i) {
                var jData = $(`label:contains('${jVal}'):first`);
                if (jData.next("div").length > 0) {
                    if (cluster === "CEM" && ajccType.match(/(EXAM|QRY)/) && masterSpec === "DOCTORS" && jVal === "審查醫師" ||
                        cluster === "CODELIST" && ajccType === "SETUP" && masterSpec === "EXAMDOCTORS" && jVal === "審查醫師")
                        jData = jData.next("div").children(":last").val();
                    else
                        jData = jData.next("div").children(":first").val();
                }
                else
                    jData = jData.next().val();
                return { "segmentation": jVal, "data": jData };
            });
        }
        else {
            jObjDetail = [];
            jObjDetail.push({ "segmentation": "收案類別", "data": $(`label:contains('收案類別')`).next().val() });
            jObjDetail.push({ "segmentation": "病歷號", "data": $(`label:contains('病歷號')`).next().val() });
            jObjDetail.push({ "segmentation": "姓名", "data": $(`label:contains('姓名')`).next().val() });
            jObjDetail.push({ "segmentation": "身分證號", "data": $(`label:contains('身分證號')`).next().val() });
        }
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

    url = getAPIURL(`${cluster}/CheckExistForm`);

    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                var isGoProcess = false;
                if (response.message !== "") {
                    if (cluster === "CEM" && ajccType.match(/(EXAM|QRY)/) && masterSpec === "DOCTORS")
                        FixSysMsg.danger(`${response.message}`);
                    else if (confirm(`${response.message} 請問是否仍要存檔？`))
                        isGoProcess = true;
                } else
                    isGoProcess = true;

                if (isGoProcess) {
                    if (callback !== undefined && typeof callback === "function") {
                        if (arguments.length > 0) {
                            var str_args = "";
                            arguments.forEach(function (val, i) {
                                if (i !== 0) str_args += `,arguments[${i}]`;
                            });

                            if (str_args !== "")
                                eval(`callback(${str_args.substring(1)})`);
                            else
                                callback();
                        }
                    } else {
                        CheckAndSave(); //存檔
                    }
                }
                else {
                    $d.reject();
                    return false;
                }
            } else {
                console.log(response);
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        },
        complete: function (xhr, status) {
            //目前暫確認均為其他函式呼叫，故不使用Spinner.hide();
        }
    });
}


//回傳處理過冒號的label名稱
function getAddressedColon(currLabelName) {
    return currLabelName && currLabelName.trim() !== "" && !new RegExp(/^~$/).test(currLabelName) ?
        `${currLabelName.replace(/\:|\：/g, "")}：` : currLabelName;
}

//去除字符串中所有空格(包括中間空格,需要設置第2個參數為:g)
function TrimAllBlank(str, is_global) {
    var result = "";
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    if (is_global && is_global.toLowerCase() === "g")
        result = result.replace(/\s/g, "");
    return result;
}

//===========================比較兩個JSON是否相同[S]===========================
function isObj(object) {
    return object && typeof object === 'object' && Object.prototype.toString.call(object).toLowerCase() === "[object object]";
}

function isArray(object) {
    return object && typeof object === 'object' && object.constructor === Array;
}

function getLength(object) {
    var count = 0;
    for (var i in object) count++;
    return count;
}

function Compare(objA, objB) {
    if (!isObj(objA) || !isObj(objB)) return false; //判斷類型是否正確
    if (getLength(objA) !== getLength(objB)) return false; //判斷長度是否一致
    return CompareObj(objA, objB, true); //默認為true
}

function CompareObj(objA, objB, flag) {
    for (var key in objA) {
        if (!flag) //跳出整個循環
            break;
        if (!objB.hasOwnProperty(key)) {
            flag = false;
            break;
        }
        if (!isArray(objA[key])) { //子級不是數組時,比較屬性值
            if (objB[key] !== objA[key]) {
                flag = false;
                break;
            }
        } else {
            if (!isArray(objB[key])) {
                flag = false;
                break;
            }
            var oA = objA[key],
                oB = objB[key];
            if (oA.length !== oB.length) {
                flag = false;
                break;
            }
            for (var k in oA) {
                if (!flag) //這裡跳出循環是為了不讓遞歸繼續
                    break;
                flag = CompareObj(oA[k], oB[k], flag);
            }
        }
    }
    return flag;
}
//===========================比較兩個JSON是否相同[E]===========================

function GenUUID() {
    var d = Date.now();
    if (typeof performance !== undefined && typeof performance.now === "function") {
        d += performance.now(); //use high-precision timer if available
    }
    return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : r & 0x3 | 0x8).toString(16);
    });
}

//字首轉大(U)/小寫(L)
function ToPrefixTransCase(str, format) {
    switch (format) {
        case "L":
            return str.replace(/^\S{1}/g, function (s) { return s.toLowerCase(); });
        case "U":
        default:
            return str.replace(/^\S{1}/g, function (s) { return s.toUpperCase(); });
    }
}

//[設定] 重設目標的屬性值
function ResetItemAttr(targets, attrName, attrVal) {
    if (targets.length && targets.length > 1) {
        $.each(targets, function (i, tgObj) {
            ResetItemAttr($(tgObj), attrName, attrVal);
        });
    } else if (targets.length && targets.length > 0) {
        $(targets).attr(attrName, attrVal);
    }
}

//[設定] 重設目標回初始值
function ResetDefaultVal(targets) {
    if (targets.length && targets.length > 1) {
        $.each(targets, function (i, tgObj) {
            ResetDefaultVal($(tgObj));
        });
    } else if (typeof targets === "object") {
        var target = targets["target"];
        var initVal = targets["value"];
        if (target) {
            if (typeof target === "string") target = $(`#${target}`);
            else target = $(target);

            if (target.length > 0) {
                if (target.is("select")) {
                    if (initVal === undefined) {
                        if (target.find("option:contains('請選擇')").length > 0)
                            initVal = target.find("option:contains('請選擇')").val();
                        else
                            initVal = target.find("option:first").val();
                    }
                } else {
                    if (initVal === undefined) initVal = "";
                }

                if (target.attr("data-bind") === undefined)
                    target.val(initVal);
                else
                    setBindingItemValue(target.attr("id"), initVal);
            }
        } else {
            var tmpObj = { "target": targets };
            ResetDefaultVal(tmpObj);
        }
    }
}

//判斷是否為NodeList
function isNodeList(nodes) {
    var stringRepr = Object.prototype.toString.call(nodes);

    return typeof nodes === 'object' &&
        /^\[object (HTMLCollection|NodeList|Object)\]$/.test(stringRepr) &&
        nodes.hasOwnProperty('length') &&
        (nodes.length === 0 || typeof nodes[0] === "object" && nodes[0].nodeType > 0);
}

//取得URL的參數值
function getURLParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

//設定設計書內指定的靜態連結
function OpenRefData(name) {
    var winUrl = "";
    switch (name) {
        case "TNMStage":
            winUrl = "../../DOC/TNMStage";
            break;
    }

    var win_width = 600;
    var win_height = 580;
    var PosX = screen.width - win_width;
    window.open(winUrl, "_blank", config = `left=${PosX}, top=0, width=${win_width}, height=${win_height}, status=no, toolbar=no, menubar=no, location=no, addressbar=no`);
}

/*===疑似未使用到func===*/
//從json中取得對應key的value
//myObject1: json
//sItem0: eg. "復發"
//sItem1: eg. "臨床期別"
//sItem2: eg. "臨床 T：▼"
//sAJCC: eg. "AJCC8"
function fJasonData(myObject1, sItem0, sItem1, sItem2, sAJCC) {
    var sReturnVal = "";
    if (sItem0 === "") return "";

    for (var sOP1 in myObject1) {
        if (myObject1.hasOwnProperty(sOP1)) {
            if (sOP1 === "data") {
                var myObject2 = myObject1[sOP1];

                for (var sOP2 in myObject2) {
                    if (myObject2.hasOwnProperty(sOP2)) {
                        if (myObject2[sOP2].datatype === sItem0) {
                            var myObject3 = myObject2[sOP2].docdetail, myObject4;

                            for (var sOP3 in myObject3) {
                                if (sItem1 === "癌症期別1") {
                                    //取[基本資料] 癌症期別內對應key的值
                                    if (myObject3[sOP3].segmentation === "癌症期別") {
                                        if (myObject3[sOP3].child) {
                                            myObject4 = myObject3[sOP3].child;

                                            jQuery.each(myObject4, function (f, myObject5) {
                                                if (myObject5.item === sItem2) {
                                                    sReturnVal = myObject5.data;
                                                    return sReturnVal;
                                                }
                                            });
                                        }
                                    }
                                }
                                else if (sItem1 === "癌症期別2") {
                                    //取resultdata的"初診斷日期"
                                    myObject4 = myObject3[sOP3].resultdata;
                                    if (myObject4) {
                                        sReturnVal = myObject4.初診斷日期;
                                        return sReturnVal;
                                    }
                                }
                                else {
                                    if (myObject3[sOP3].follow) {
                                        myObject4 = myObject3[sOP3].follow;

                                        jQuery.each(myObject4, function (f, myObject5) {
                                            if (myObject5.item === sItem1) {
                                                sReturnVal = myObject5.data;

                                                if (sItem2 === "") {
                                                    return sReturnVal;
                                                } else {
                                                    var sVal = "";
                                                    sReturnVal = "";

                                                    if (sAJCC === "") {
                                                        sVal = fJasonData(json, sItem0, sItem1, "");
                                                    } else {
                                                        sVal = sAJCC;
                                                    }

                                                    for (var sOP4 in myObject5.option) {
                                                        var myObject6 = myObject5.option[sOP4];

                                                        if (myObject6.item === sVal) {
                                                            for (var sOP5 in myObject6.follow) {
                                                                var myObject7 = myObject6.follow[sOP5];

                                                                if (myObject7.item === sItem2) {
                                                                    sReturnVal = myObject7.data;
                                                                    return sReturnVal;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    return sReturnVal;
}


//1.依病歷號到webservice取得案號
//2.以取得的案號連結到"放射治療摘要(RTSummary)"/"放射計畫書(RTPlan)"
//GoToRT('RTSummary''RTDOC_URL')
function GoToRT(target, urlCode) {
    //病歷號
    var chrt = $("#msgBlock1").find("div:contains('病歷號')");
    if (chrt.length > 0) chrt = chrt.text().replace(/病歷號[\:|：]/g, "");
    //癌別
    var cancerType = $("#msgBlock1").find("div:contains('收案類別')");
    if (cancerType.length > 0)
        cancerType = cancerType.text().replace(/收案類別[\:|：]/g, "");
    else if (cluster === "CEM") { //個管審查
        cancerType = $("#msgBlock1").find("div:contains('癌別')");
        cancerType = cancerType.text().replace(/癌別[\:|：]/g, "");
    }

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "RTWS",
        "data": {
            "module": module,
            "cluster": cluster,
            "chrt": chrt,
            "cancertype": cancerType
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL(`${cluster}/GetRTCaseNoWS`),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                var result = JSON.parse(response.jsonData.data);
                console.log(result);
                GetViewReadonly('CRM_', "", urlCode, result.caseNo.trimEnd(), target);
            } else {
                console.log(response);
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax request 發生錯誤");
        },
        complete: function (xhr, status) {
            Spinner.hide();
        }
    });
}

/**
 * 下載檔案檔
 * @param {any} fileName    檔案名稱
 * @param {any} fileContent 檔案內容
 */
function downloadByte(fileName, fileContent) {
    var blob = new Blob([base64ToArrayBuffer(fileContent)]);
    var link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = fileName;
    link.click();
}
