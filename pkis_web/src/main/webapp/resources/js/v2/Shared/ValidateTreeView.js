﻿var $preObj, preCol, preRow;
var $currObj, currId, currCol, currRow, currVal;
var $nextObj, nextCol, nextRow, nextVal;
var $currDiv, $preDiv, $nextDiv;
var $selObj, selCol, selRow;
var grpCompObj, grpCompCol, grpCompVal;

//for 必填檢核(Json物件)
var Required_Fields = [];
var Required_Titles = [];
var Required_Title = ""; //必填檢核字串
var Required_Id = ""; //必填檢核控制項Id
var Required_Remark = false;

//for 格式檢核(Json物件)
var ValidFormat_Fields = [];
var ValidFormat_Titles = [];
var ValidFormat_Title = ""; //格式檢核字串
var ValidFormat_Id = ""; //格式檢核控制項的"Id"
var ValidFormat_Fmt = ""; //格式檢核控制項的"格式"
var ValidFormat_Remark = false;

//for 起、迄日檢核(Json物件)
var Valid_S_E_Dates = [];
var ValidSEDate_Remark = 0;

//開始Tree行為連動
//nowId: 當為radio時，nowId為群組的第1個(注意：可能非當下選中的項目)
function GearingBehavior(nowId, bindName, nowCol, nowRow, nowVal, uitype) {
    if (nowVal !== undefined) {
        //console.log(`=============GearingBehavior[s]=============`);
        $currObj = $(`#${nowId}`);
        if (nowId === "radio_2_184_s2f187")
            console.log($currObj);
        currId = nowId;
        currCol = nowCol;
        currRow = nowRow;
        currVal = nowVal;
        //if (uitype === "radio") {
        //    var tmpGroupName = $currObj.attr("name");
        //    var $tmpRdoObj = $(`[name='${tmpGroupName}'][value='${nowVal}']`);
        //    if ($tmpRdoObj.length > 0) {
        //        $currObj = $tmpRdoObj;
        //        currId = $currObj.attr("id");
        //        currCol = $currObj.attr("col");
        //        currRow = $currObj.attr("row");
        //    }
        //}
        $currDiv = $currObj.parent("div");
        $preDiv = $currDiv.prev("div");
        $nextDiv = $currDiv.next("div");
        nextCol = $nextDiv.attr("col");
        nextRow = $nextDiv.attr("row");
        //console.log(`${currId},${bindName},Col:${currCol},Row:${currRow},Value:${nowVal}(${typeof nowVal}),${uitype}`);

        var currGroupName, tmpNextObj, tmpNowVal;

        if ("text|date|textarray|dropdownlist".indexOf(uitype) > -1) {
            if (nowVal !== "") {
                GoPreLevelSetting($currObj, currCol);
            }
        }
        else if (uitype === "radio") {
            if (currVal !== "") SetGearingPreLevel($preDiv);
            SetGearingEqLevel($(`#${currId}`).next("label[for]"));
            if ($nextDiv.length > 0 && nextCol > "1") {
                if (nowVal === "") {
                    currCol = $currDiv.attr("col");
                    currRow = $currDiv.attr("row");
                    ClearGearingNextLevel($nextDiv, currCol);
                }
                else {
                    currGroupName = $currObj.attr("name");
                    tmpNowVal = eval(`viewModel.${bindName}()`);
                    $(`input:radio[data-bind='checked:${bindName}']`).each(function (i, rdoObj) {
                        tmpNextObj = $(rdoObj);
                        if (tmpNowVal !== tmpNextObj.val()) grpCompVal = false;
                        //若項目屬於當前radio群組，且非當前選擇的值(nowVal)，則繼續往下一層找
                        if (currGroupName === tmpNextObj.attr("name") && tmpNowVal !== tmpNextObj.val() && nextCol >= currCol) {
                            var tmpNextDiv = tmpNextObj.parent("div");
                            if (currGroupName !== tmpNextDiv.next("div").find("input:radio:first").attr("name")) {
                                if (tmpNextDiv.next("div").length > 0) {
                                    SetGearingEqLevel(tmpNextObj.next("label[for]"));
                                    currCol = tmpNextDiv.attr("col");
                                    currRow = tmpNextDiv.attr("row");
                                    $nextDiv = tmpNextDiv.next("div");
                                    nextCol = $nextDiv.attr("col");
                                    nextRow = $nextDiv.attr("row");
                                    ClearGearingNextLevel($nextDiv, currCol, grpCompVal);
                                }
                            }
                        }
                    });
                }
            } else {
                currGroupName = $currObj.attr("name");
                tmpNowVal = eval(`viewModel.${bindName}()`);
                $(`input:radio[data-bind='checked:${bindName}']`).each(function (i, rdoObj) {
                    tmpNextObj = $(rdoObj);
                    //若項目屬於當前radio群組，且非當前選擇的值(nowVal)，則繼續往下一層找
                    if (currGroupName === tmpNextObj.attr("name") && tmpNowVal !== tmpNextObj.val()) {
                        var tmpNextDiv = tmpNextObj.parent("div");
                        if (currGroupName !== tmpNextDiv.next("div").find("input:radio:first").attr("name")) {
                            if (tmpNextDiv.next("div").length > 0) {
                                currCol = tmpNextDiv.attr("col");
                                currRow = tmpNextDiv.attr("row");
                                $nextDiv = tmpNextDiv.next("div");
                                nextCol = $nextDiv.attr("col");
                                nextRow = $nextDiv.attr("row");
                                ClearGearingNextLevel($nextDiv, currCol);
                            }
                        }
                    }
                });
            }
        }
        else if (uitype === "checkbox") {
            if (nowVal === true) {
                SetGearingPreLevel($preDiv);
            }
            else {
                SetGearingEqLevel($(`#${currId}`).next("label[for]"));
                if ($nextDiv.length > 0) {
                    currCol = $currDiv.attr("col");
                    currRow = $currDiv.attr("row");
                    if (currCol !== nextCol) {
                        ClearGearingNextLevel($nextDiv, currCol);
                    }
                }
            }
        }
    }
}

//往上層找父項
//isGoUpstair: 是否有往上一階div找
function GoPreLevelSetting($obj, inCurrCol, isGoUpstair) {
    var $preObj, tmpCol = "", tmpPreCol = "", tmpGrpName = "", tmpPreGrpName = "";

    if ($obj.length > 0) {
        tmpCol = $obj.attr("col");
        $preObj = $obj.prev("label[for]");

        if ($preObj.length > 0) {
            tmpPreCol = $preObj.prev().attr("col");
            if (isGoUpstair === true) {
                if (tmpPreCol && tmpPreCol < tmpCol && tmpPreCol < inCurrCol) {
                    SetGearingPreLevel($preObj);
                } else {
                    if (tmpPreCol !== "1") GoPreLevelSetting($preObj.prev(), inCurrCol, isGoUpstair);
                }
            } else {
                if (tmpPreCol && tmpPreCol <= tmpCol && tmpPreCol <= inCurrCol) {
                    SetGearingPreLevel($preObj);
                } else {
                    if (tmpPreCol !== "1") GoPreLevelSetting($preObj.prev(), inCurrCol);
                }
            }
        } else {
            $preObj = $obj.prev("label");

            if ($preObj.length === 0) {
                if (tmpCol !== "1") {
                    $preObj = $obj.prevAll("label[for]:last");

                    if ($preObj.length > 0) {
                        //有找到同階DIV的最後一個待勾選label[for]
                        tmpPreCol = $preObj.prev().attr("col");
                        if (tmpPreCol !== "1") {
                            if (tmpPreCol < tmpCol && tmpPreCol < inCurrCol) {
                                SetGearingPreLevel($preObj);
                            } else {
                                GoPreLevelSetting($preObj.prev(), inCurrCol);
                            }
                        } else
                            SetGearingPreLevel($preObj);
                    } else {
                        $preObj = $obj.parent("div").prev("div").find("label[for]:last");

                        if ($preObj.length > 0) {
                            //有找到上一階DIV的最後一個待勾選label[for]
                            tmpPreCol = $preObj.prev().attr("col");
                            if (tmpPreCol !== "1") {
                                if (tmpPreCol < tmpCol && tmpPreCol < inCurrCol) {
                                    SetGearingPreLevel($preObj);
                                } else {
                                    GoPreLevelSetting($preObj.prev(), inCurrCol, true);
                                }
                            } else
                                SetGearingPreLevel($preObj);
                        } else {
                            //未找到上一階DIV的最後一個待勾選label[for]
                            $preObj = $obj.parent("div").prev("div").find("[col]:first");
                            tmpPreCol = $preObj.attr("col");
                            if (tmpPreCol !== "1") GoPreLevelSetting($preObj, inCurrCol);
                        }
                    }
                }
            } else {
                //前一個控制項($preObj)是label或field(隱藏欄位)
                tmpPreCol = $preObj.attr("col");
                GoPreLevelSetting($preObj, inCurrCol);
            }
        }
    }
}

//父項、同階選取
//gearSelCol: 前次被選中項目的col值
function SetGearingPreLevel($obj, gearSelCol) {
    if ($obj.length > 0) {
        //console.log(`=============SetGearingPreLevel=============`);
        //console.log($obj);
        var $id, tmpChkItem;

        if ($obj.is("div")) {
            preCol = $obj.attr("col");
            preRow = $obj.attr("row");
            //console.log(`currCol:${currCol},PreCol:${preCol},currRow:${currRow},PreRow:${preRow}`);
            var formerDiv = $obj.next("div");
            var formerCol;
            if (formerDiv.length > 0) formerCol = formerDiv.attr("col");

            if (preCol < formerCol && currCol >= preCol && preCol >= "1" && parseInt(currRow) >= parseInt(preRow)) {
                //父項DIV內最後的checkbox or radio
                $preObj = $obj.find("label[for]:last");
                if ($preObj.length > 0) {
                    $id = $preObj.attr("for");
                    $preObj = $preObj.prev();
                    preCol = $preObj.attr("col");
                    preRow = $preObj.attr("row");

                    if (currCol > preCol && preCol >= "1" && parseInt(currRow) >= parseInt(preRow)) {
                        if (preCol !== selCol && (gearSelCol === undefined || preCol < gearSelCol)) {
                            switch ($id.split("_")[0]) {
                                case "checkbox":
                                    tmpChkItem = $(`#${$id}`);
                                    if (tmpChkItem.attr("onclick") !== undefined && tmpChkItem.attr("onclick") !== "") {
                                        if (tmpChkItem[0].checked === false) tmpChkItem.click();
                                    } else {
                                        setBindingItemValue($id, true);
                                        gearSelCol = preCol;
                                    }
                                    break;
                                case "radio":
                                    setBindingItemValue($id, $(`#${$id}`).attr("value"));
                                    gearSelCol = preCol;
                                    break;
                            }
                            $selObj = $(`#${$id}`);
                            selCol = preCol;
                            selRow = preRow;
                        }
                    }
                } else if (preCol > "1") {
                    $preDiv = $obj.prev("div");
                    SetGearingPreLevel($preDiv, gearSelCol);
                }
            }

            if (preCol > "1") {
                $preDiv = $obj.prev("div");
                SetGearingPreLevel($preDiv, gearSelCol);
            } else {
                $selObj = undefined;
                selCol = undefined;
                selRow = undefined;
            }
        }
        else if ($obj.is("label[for]")) {
            $id = $obj.attr("for");
            $preObj = $obj.prev();
            if ($preObj.length > 0) {
                preCol = $preObj.attr("col");
                preRow = $preObj.attr("row");

                if (currCol >= preCol && preCol >= "1" && currRow >= preRow) {
                    if (preCol !== selCol && (gearSelCol === undefined || preCol < gearSelCol)) {
                        switch ($id.split("_")[0]) {
                            case "checkbox":
                                tmpChkItem = $(`#${$id}`);
                                if (tmpChkItem.attr("onclick") !== undefined && tmpChkItem.attr("onclick") !== "") {
                                    if (tmpChkItem[0].checked === false) tmpChkItem.click();
                                } else {
                                    setBindingItemValue($id, true);
                                    gearSelCol = preCol;
                                }
                                break;
                            case "radio":
                                setBindingItemValue($id, $(`#${$id}`).attr("value"));
                                gearSelCol = preCol;
                                break;
                        }
                        $selObj = $(`#${$id}`);
                        selCol = preCol;
                        selRow = preRow;
                    }
                }

                if (preCol > "1") {
                    var $tmpParentDiv = $obj.parent("div");
                    $preDiv = $tmpParentDiv.prev("div");
                    SetGearingPreLevel($preDiv, gearSelCol);
                } else {
                    $selObj = undefined;
                    selCol = undefined;
                    selRow = undefined;
                }
            }
        } else {
            $selObj = undefined;
            selCol = undefined;
            selRow = undefined;
        }
    }
}

//子項清空
function ClearGearingNextLevel($obj, currentCol, comparedVal) {
    var $tmpNextDivObj = $obj, tmpNextCol, tmpNextRow;
    if ($tmpNextDivObj.length > 0) {
        //console.log(`=============ClearGearingNextLevel=============`);
        //console.log($tmpNextDivObj);

        if ($tmpNextDivObj.is("div")) {
            nextCol = $tmpNextDivObj.attr("col");
            nextRow = $tmpNextDivObj.attr("row");
            //console.log(`currentCol:${currentCol},NextCol:${nextCol},currRow:${currRow},NextRow:${nextRow}`);

            if (currentCol < nextCol) {
                if (comparedVal === undefined || comparedVal === false) {
                    //子項DIV內所有元素清空
                    $.each($tmpNextDivObj.children(), function (i, item) {
                        var $tmpItem = $(item);
                        var $tmpBindObj, tmpBindingName = "", tmpBindVal, tmpCancelVal;
                        var $id = "";
                        //console.log(`${$tmpItem.attr("displayname")}, ${currVal}`);

                        if ($tmpItem.is("label[for]")) {
                            $id = $tmpItem.attr("for");
                            $tmpBindObj = $(`#${$id}`);

                            switch ($id.split("_")[0]) {
                                case "checkbox":
                                    tmpCancelVal = false;
                                    break;
                                case "radio":
                                    //子項才需清空，同群組選項不用
                                    if (currentCol < $tmpBindObj.attr("col")) tmpCancelVal = "";
                                    break;
                            }
                        }
                        else if ($tmpItem.is("input:text") || $tmpItem.is("textarea") || $tmpItem.is("select")) {
                            $id = $tmpItem.attr("id");
                            tmpCancelVal = "";
                        }

                        if (tmpCancelVal !== undefined) {
                            setBindingItemValue($id, tmpCancelVal);
                        }
                    });
                }

                var tmpBindObj = getBindingObj($tmpNextDivObj.find("input:radio, input:checkbox")[0]);
                if (tmpBindObj && tmpBindObj.bindVal === currVal) grpCompVal = currVal;
                if (grpCompVal === "") grpCompVal = undefined; //undefined或false時，需繼續往下清空
                $tmpNextDivObj = $tmpNextDivObj.next("div");
                ClearGearingNextLevel($tmpNextDivObj, currentCol, grpCompVal);
            }
            else {
                $tmpNextDivObj = $tmpNextDivObj.next("div");
                if ($tmpNextDivObj.length > 0) {
                    tmpNextCol = $tmpNextDivObj.attr("col");
                    tmpNextRow = $tmpNextDivObj.attr("row");
                    if (nextCol === tmpNextCol && nextRow < tmpNextRow) {
                        ClearGearingNextLevel($tmpNextDivObj, currentCol);
                    }
                }
            }
        }
    }
}

//同階清空
function SetGearingEqLevel($obj) {
    var $id = "", $groupName = "";
    var $tmpObj, tmpBindObj, tmpBindVal, tmpValue, tmpKey;

    if ($obj.length > 0) {
        //console.log(`=============SetGearingEqLevel=============`);
        //console.log($obj);

        if ($obj.is("label[for]")) {
            $id = $obj.attr("for");
            $tmpObj = $(`#${$id}`);
            tmpBindObj = getBindingObj($tmpObj[0]);
            tmpBindVal = tmpBindObj["bindVal"];

            switch ($id.split("_")[0]) {
                case "checkbox":
                    if (currVal === false) {
                        $nextObj = $obj.nextAll();

                        if ($nextObj.length > 0) {
                            $.each($nextObj, function (j, txtItem) {
                                var $tmpItem = $(txtItem);

                                if ($tmpItem.is("input:text") || $tmpItem.is("textarea") || $tmpItem.is("select")) {
                                    //若遇到自己，則跳出next迴圈，進行下一群組選項檢核
                                    if (txtItem.id === currId) return false;

                                    nextCol = $tmpItem.attr("col");
                                    nextRow = $tmpItem.attr("row");
                                    if (nextCol >= currCol) {
                                        $id = $tmpItem.attr("id");
                                        setBindingItemValue($id, "");
                                    }
                                }
                                else {
                                    //若遇到同群組項目，則跳出next迴圈，進行下一群組選項檢核
                                    if ($tmpItem.attr("name") === $groupName) return false;
                                }
                            });
                        }
                    }
                    break;
                case "radio":
                    $groupName = $(`#${$id}`).attr("name");
                    //radio群組選項
                    $.each($(`[name='${$groupName}']`), function (i, rdoItem) {
                        tmpValue = rdoItem.value;
                        //console.log(`childVal:${tmpValue},currVal:${currVal},tmpBindVal:${tmpBindVal}`);
                        if (tmpValue !== tmpBindVal) {
                            $nextObj = $(rdoItem).next("label[for]");
                            $nextObj = $nextObj.nextAll();

                            if ($nextObj.length > 0) {
                                $.each($nextObj, function (j, txtItem) {
                                    var $tmpItem = $(txtItem);

                                    if ($tmpItem.is("input:text") || $tmpItem.is("textarea") || $tmpItem.is("select")) {
                                        //若遇到自己，則跳出next迴圈，進行下一群組選項檢核
                                        if (txtItem.id === currId) return false;

                                        nextCol = $tmpItem.attr("col");
                                        nextRow = $tmpItem.attr("row");
                                        if (nextCol >= currCol) {
                                            $id = $tmpItem.attr("id");
                                            setBindingItemValue($id, "");
                                        }
                                    }
                                    else {
                                        //若遇到同群組項目，則跳出next迴圈，進行下一群組選項檢核
                                        if ($tmpItem.attr("name") === $groupName) return false;
                                    }
                                });
                            }
                        }
                    });
                    break;
            }
        }
    }
}

//必填驗證 - 加入必填檢核欄位
function AddValidReqField(obj, item, itemType) {
    var lCol = obj["col"];
    var lDisplayname = obj["displayname"];

    if (Required_Remark && $(item).attr("data-bind")) {
        Required_Id = obj["id"];
        Required_Title = Required_Titles.toString().replace(",", " -> ");
        var Required_Field_JObj = {
            "REQ_TITLE": Required_Title,  //檢核標題字串
            "REQ_ID": Required_Id      //檢核控制項Id
        };
        var isDuplicate = false; //去除重複
        Required_Fields.forEach(function (rqObj, i) {
            if (rqObj["REQ_TITLE"] === Required_Field_JObj["REQ_TITLE"]) isDuplicate = true;
        });
        if (!isDuplicate) Required_Fields.push(Required_Field_JObj);
        Required_Remark = false;
    }

    if (lDisplayname && lDisplayname !== "" && lDisplayname.indexOf("SOURCE.") === -1) {
        lDisplayname = lDisplayname.replace(/\:|：/g, "").replace(/FIX|增加/g, "").trim();
        if (lCol === 0) {
            Required_Title = lDisplayname;
            Required_Titles = [lDisplayname];
        } else {
            if (itemType === "label") {
                Required_Titles = Required_Titles.slice(0, lCol);
                Required_Titles.push(lDisplayname);
            }
        }
    }

    if (obj["remark"] && obj.remark.match(/\*|required/g) || obj["_ref"] && obj._ref.match(/required/g))
        Required_Remark = true;
    else
        Required_Remark = false;
}

//必填驗證 - 開始驗證
function ValidateRequiredItems() {
    var checkedMsg = "";

    if (Required_Fields.length > 0) {
        Required_Fields.forEach(function (validObj, i) {
            var checkedItem = getBindingObj($(`#${validObj.REQ_ID}`)[0]);
            if (checkedItem["bindVal"] === undefined || checkedItem["bindVal"] === "")
                checkedMsg += `【${validObj.REQ_TITLE}】為必填！\n`;
        });
    }
    return checkedMsg;
}

//格式驗證 - 加入格式檢核欄位
function AddValidFormatField(obj, item, itemType) {
    var lCol = obj["col"];
    var lDisplayname = obj["displayname"];
    var lFormat = obj["format"];

    if (ValidFormat_Remark) {
        ValidFormat_Title = ValidFormat_Titles.toString().replace(/,/g, " -> ");
        var ValidFormat_Field_JObj = {
            "VALF_TITLE": ValidFormat_Title,    //檢核標題字串
            "VALF_ID": ValidFormat_Id,          //檢核控制項Id
            "VALF_FMT": ValidFormat_Fmt
        };
        ValidFormat_Fields.push(ValidFormat_Field_JObj);
        ValidFormat_Remark = false;
    }
    if (lDisplayname !== undefined && lDisplayname !== null && lDisplayname !== "" && lDisplayname.indexOf("SOURCE.") === -1) {
        lDisplayname = lDisplayname.replace(/\:|：/g, "").trim();
        if (lCol === 0) {
            ValidFormat_Title = lDisplayname;
            ValidFormat_Titles = [lDisplayname];
        } else {
            ValidFormat_Titles = ValidFormat_Titles.slice(0, lCol);
            ValidFormat_Titles.push(lDisplayname);
        }
    }
    if (lFormat) {
        //(Todo; 目前先避開尚未有邏輯的元件)
        if (lFormat !== "" && "#PID|#CID|#HID".indexOf(lFormat) > -1) {
            ValidFormat_Remark = false;
        } else if (lFormat !== "" && lFormat.indexOf("#") === 0) {
            if ($(item).attr("data-bind")) {
                ValidFormat_Id = obj["id"];
                ValidFormat_Fmt = lFormat;
                ValidFormat_Remark = true;
            } else {
                ValidFormat_Id = "";
                ValidFormat_Fmt = "";
                ValidFormat_Remark = false;
            }
        }
    }
}

//格式驗證 - 開始驗證
function ValidateFormatItem(nowId, nowVal) {
    var checkedMsg = "";
    var reg = /^#[1-9][0-9]*(A|TA)$/;
    var isMaxLenText = false; //若屬輸入文字長度檢核，則不應清空欄位

    if (ValidFormat_Fields.length > 0) {
        ValidFormat_Fields.forEach(function (validObj, i) {
            if (nowId === validObj.VALF_ID) {
                checkedMsg = ChkValidDateFormat(nowId, nowVal, validObj.VALF_FMT, validObj.VALF_TITLE);
                if (new RegExp(reg).test(validObj.VALF_FMT)) isMaxLenText = true;
            }
        });
    }

    if (checkedMsg !== undefined && checkedMsg !== "") {
        alert(checkedMsg);
        if (!isMaxLenText) setBindingItemValue(nowId, "");
    }
}

//起、迄日檢核 - 成對加入檢核欄位
function AddValidSEDateField(obj, item, itemType) {
    var lDateId = obj["id"];
    var lDisplayname = obj["displayname"];

    if ((itemType === "date" || itemType === "text") && obj["format"] && obj["format"].match(/#\d+D/g)) {
        if (ValidSEDate_Remark > 0) {
            var Valid_S_E_Date = {};
            if (Valid_S_E_Dates.length === 0) {
                if (ValidSEDate_Remark === 1) Valid_S_E_Date["S_DATE_ID"] = lDateId;
                else if (ValidSEDate_Remark === 2) Valid_S_E_Date["E_DATE_ID"] = lDateId;
                Valid_S_E_Dates.push(Valid_S_E_Date);
            } else {
                if (ValidSEDate_Remark === 1) {
                    Valid_S_E_Date["S_DATE_ID"] = lDateId;
                    Valid_S_E_Dates.push(Valid_S_E_Date);
                }
                else if (ValidSEDate_Remark === 2) {
                    if (Valid_S_E_Dates[Valid_S_E_Dates.length - 1]["E_DATE_ID"] === undefined)
                        Valid_S_E_Dates[Valid_S_E_Dates.length - 1]["E_DATE_ID"] = lDateId;
                }
            }
        }
    }
    else if (itemType === "label" && (lDisplayname === "起日" || lDisplayname === "迄日")) {
        if (lDisplayname === "起日") ValidSEDate_Remark = 1;
        else if (lDisplayname === "迄日") ValidSEDate_Remark = 2;
    }
}

//檢核起、迄日(主流程)
function ChkValidSEDate(currId) {
    for (var i = 0; i < Valid_S_E_Dates.length; i++) {
        var valObj = Valid_S_E_Dates[i];
        if (valObj.S_DATE_ID === currId || valObj.E_DATE_ID === currId) {
            if (valObj.S_DATE_ID !== "" && valObj.E_DATE_ID !== "") {
                ChkValidSEDate_Proc(currId, valObj.S_DATE_ID, valObj.E_DATE_ID);
                break;
            }
        }
    }
}

//檢核起、迄日(子流程)
function ChkValidSEDate_Proc(currId, sDateId, eDateId) {
    var sDateStr = $(`#${sDateId}`).val();
    var eDateStr = $(`#${eDateId}`).val();
    var sDate, eDate;
    var nowVal = getBindingObj($(`#${currId}`))["bindVal"];

    if (nowVal && nowVal !== "") {
        if (sDateStr && sDateStr !== "") {
            sDate = new Date(getFormatDateStr(1, sDateStr));
        }
        if (eDateStr && eDateStr !== "") {
            eDate = new Date(getFormatDateStr(1, eDateStr));
        }
        if (eDateStr.length === 8) {
            if (sDate && eDate && sDate > eDate) {
                alert(`起日不得大於迄日！`);
                setBindingItemValue(currId, "");
            }
        }
    }
}

//檢核欄位日期值需
// - 大於或等於系統日(pattern)："#GE SYSDATE"
// - 小於或等於系統日(pattern)："#LE ..."
// - 大於系統日(pattern)："#GT ..."
// - 小於系統日(pattern)："#LT ..."
function ChkOverSysdate(refId, refName, pattern) {
    var refItm = $(`#${refId}`), msg = "";
    if (refItm.length > 0) {
        var comparedDate = new Date(getFormatDateStr(1, refItm.val()));
        if (comparedDate !== undefined) {
            if (pattern === undefined || pattern.indexOf("#GE") > -1) {
                if (comparedDate < new Date(getToDay(1))) {
                    if ("下次個管追蹤日期|簡訊預約傳送".indexOf(refName) > -1 && refItm.val().length === 8)
                        msg = "需大於或等於系統日！";
                    else if ("下次個管追蹤日期|簡訊預約傳送".indexOf(refName) === -1)
                        msg = "需大於或等於系統日！";
                }
            } else if (pattern && pattern.indexOf("#LE") > -1) {
                if (comparedDate > new Date(getToDay(1))) {
                    msg = "需小於或等於系統日！";
                }
            } else if (pattern && pattern.indexOf("#GT") > -1) {
                if (comparedDate <= new Date(getToDay(1))) {
                    msg = "需大於系統日！";
                }
            } else if (pattern && pattern.indexOf("#LT") > -1) {
                if (comparedDate >= new Date(getToDay(1))) {
                    msg = "需小於系統日！";
                }
            }
            if (msg !== "") {
                alert(`【${refName}】${msg}`);
                setBindingItemValue(refId, "");
            }
        }
    }
}

//檢核格式
function ChkValidDateFormat(currId, currVal, jsvFormat, display) {
   var reg, msg, num, num1, num2; 

    // n位文字：#10A or #10TA
    reg = /^#[1-9][0-9]*(A|TA)$/;
    if (new RegExp(reg).test(jsvFormat)) {
        isMaxLenText = true;
        reg = /[1-9][0-9]*/;
        num = jsvFormat.match(reg).toString();
        reg = `^.{0,${num}}$`;
        if (currVal.length > num) {
            msg = `【${display}】輸入文字不可超過${num}個字！`;
        }
    }

    if (msg === undefined) {
        // n位整數：#3N
        reg = /^#[1-9][0-9]*N$/;
        if (new RegExp(reg).test(jsvFormat)) {
            reg = /0|[1-9][0-9]*/;
            num = parseInt(jsvFormat.match(reg)[0].toString());
            currVal = currVal.toUpperCase();
            reg = `^(0|[1-9][0-9]*|[X])$`;
            msg = `【${display}】格式需為整數，不得大於10位！`;
        }
    }

    if (msg === undefined) {
        // n位整數 m位小數：#3.1N => (n + m <= 3, m = 0~1)
        reg = /^#(0|([1-9][0-9]*))\.(0|([1-9][0-9]*))N$/;
        if (new RegExp(reg).test(jsvFormat)) {
            reg = /(0|([1-9][0-9]*))\.(0|([1-9][0-9]*))/;
             num1 = parseInt(jsvFormat.match(reg)[0].toString().split(".")[0]); 
            num2 = parseInt(jsvFormat.match(reg)[0].toString().split(".")[1]); 
            currVal = currVal.toUpperCase();
			reg = `^([0-9]{1,${num1}}(.[0-9]{1,2})?|[X])$`; 
            msg = `【${display}】格式需為小數，小數點最多2位, 整數最多${num1}位!`; 
        }
    }

    if (msg === undefined) {
        // 身份證：#ID
        if (jsvFormat === "#ID") {
            reg = /^[A-Z]{1}[1-2]{1}[0-9]{8}$/;
            msg = `【${display}】身份證號格式錯誤！`;
        }
    }

    if (msg === undefined) {
        // 民國年月日：#7D
        if (jsvFormat === "#7D") {
            reg = `^[0-1]?[0-9]{1,2}(((0[13578]|10|12)(0[1-9]|[1-2][0-9]|3[01]))|(02(0[1-9]|[1-2][0-9]))|((0[469]|11)(0[1-9]|[1-2][0-9]|30)))$`;
            msg = `【${display}】格式需為7碼民國年月日(ex:0710905)！`;
        }
        // 西元年月日：#8D
        else if (jsvFormat === "#8D") {
            if (currVal.length === 8) {
                //癌登可輸入8個0.8.9
                var regDate = /^[1-9]\d{3}(0[1-9]|1[0-2]|99)99$/;
                if (!(module === "CR" && (currVal === "00000000" || currVal === "88888888" || currVal === "99999999" || new RegExp(regDate).test(currVal)))) {
                    reg = `^[0-9]{4}(((0[13578]|10|12)(0[1-9]|[1-2][0-9]|3[01]))|(02(0[1-9]|[1-2][0-9]))|((0[469]|11)(0[1-9]|[1-2][0-9]|30)))$`;
                    msg = `【${display}】格式需為8碼西元年月日(ex:19850905)！`;
                }
            }
            else {
                $(`#${currId}`).focusout(function () {
                    reg = `^[0-9]{4}(((0[13578]|10|12)(0[1-9]|[1-2][0-9]|3[01]))|(02(0[1-9]|[1-2][0-9]))|((0[469]|11)(0[1-9]|[1-2][0-9]|30)))$`;
                    msg = `【${display}】格式需為8碼西元年月日(ex:19850905)！`;
                    currVal = $(`#${currId}`).val();
                    if (currVal && currVal !== "") {
                        if (currVal.length > 0 && currVal.length < 8) {
                            if (reg !== undefined && !new RegExp(reg).test(currVal)) {
                                alert(msg);
                                setBindingItemValue(currId, "");
                            }
                        }
                    }
                });
            }
        }
		else if(jsvFormat === "#10D") {
			reg = `^([0-9]{4})(/)(((0[13578]|10|12)(/)(0[1-9]|[1-2][0-9]|3[01]))|(02(/)(0[1-9]|[1-2][0-9]))|((0[469]|11)(/)(0[1-9]|[1-2][0-9]|30)))$`;
			msg = `【${display}】格式需為10碼西元年月日(ex:2020/09/21)！`;
		}
    }

    if (currVal && currVal !== "") {
        //console.log(new RegExp(reg).test(currVal));
        //console.log(msg);
        if (reg !== undefined && !new RegExp(reg).test(currVal)) {
				return msg;
        } else {
			if(jsvFormat.indexOf('.') > -1){
				if(isNaN(currVal) === true && currVal !== 'X'){ 
					return msg; 
				}else { 
					var temp = Math.floor(currVal).toString(); 
					if(temp.length > num1){ 
						return msg; 
					}else 
						return ""; 
				} 
					return ""; 
			}else
				return "";
		}
    } else return "";
}
//千分位轉換
function convertPerMill(num){
    var parts = num.replace(/,/g, "");
    parts = parts.replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
    return parts;
}
//千分位轉換純數字
function convertnum(string){
    var parts = string.replace(/,/g, "");
    return parts;
}

