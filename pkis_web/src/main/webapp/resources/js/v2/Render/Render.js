var stringConstructor = "test".constructor;
var arrayConstructor = [].constructor;
var objectConstructor = {}.constructor;
var rootContent = "content";
var contentList = [];
var containerDiv = undefined;
var nameGroupID = "";
var rootDiv = undefined;
var rootFieldset = undefined;
var containerFieldset = undefined;
var containerLegend = undefined;
var fieldsetCnt = 0;
var addBtnFieldset = undefined;
var currentRow = -999;
var currentCol = -999;
var Ev8DReg = '(?:19|20)[0-9]{2}(?:(?:0[1-9]|1[0-2])(?:0[1-9]|1[0-9]|2[0-9])|(?:(?!02)(?:0[1-9]|1[0-2])-(?:30))|(?:(?:0[13578]|1[02])-31))';
var EvButtonBlockReg = /#(增加|\+|展開)BTN/g;
var isReload = false;
var isHide1stAddBtnBlock = false; //是否隱藏[增加BTN]初始編輯區
var Cnt_tabIndex = 0; //設定TAB鍵導航控制次序(PS:Safari不支援)

function init() {
    contentList = [];
    contentList.push(rootContent);
    containerFieldset = undefined;
    containerDiv = undefined;
    nameGroupID = "";
    currentRow = -999;
    currentCol = -999;
    fieldsetCnt = 0;
    rootDiv = document.createElement('div');
}

function genDiv(json, dom) {
    var body = $(dom);
    init();
    try {
        $.each(json, function (key, value) {
            rowIdx = key;
            Show(key, value);
        });
        if (containerFieldset !== undefined) {
            rootDiv.appendChild(containerFieldset);
        }
        else if (containerDiv !== undefined) {
            rootDiv.appendChild(containerDiv);
        }
        body.append(rootDiv);

        return true;
    } catch (e) {
        console.log(e);
        return false;
    }
}

function Show(key, value) {
    var type = GetType(value);
    if (type === "Object") {
        ShowObject(value, type);
    }
    else if (type === "Array") {
        ShowArray(key, value, type);
    }
}

function ShowArray(key, obj, type) {
    if (type === "Array") {
        for (var item in obj)
            ShowObject(obj[item]);
    }
    else {
        console.log("[ShowArray]: Got Type = " + type);
    }
}

function ShowObject(obj, type) {
    if (type === "Object") {
        //[基本資料] 取得[初診斷日期]
        if (sDiagnosisDate === "" && obj.displayname && obj.displayname === "初診斷日期") GetDiagnosisDate(obj);
        if (sAJCC === "") sAJCC = getAJCC_Version(sDiagnosisDate);

        if (IsUIElement(obj)) {
            var item = undefined, itemType = obj.uitype;

            if (itemType === undefined) {
                if (obj.segmentation && obj.segmentation === "title")
                    itemType = "title";
                if (obj.ResultListItems && obj.ResultListItems.length > 0)
                    itemType = "resultlist";
                else if (obj.remark && obj.remark === "controller")
                    itemType = "controller";
            }

            //render畫面時，排除"#SELECT-option" 和 [FIX Block]非編輯區的控制項(另外處理)
            if (obj.note !== "#SELECT-option" && obj.note !== "#□SELECT-option" &&
                (!obj.fixblock || !obj.fixblock.match(/.+_fix[1-9][\d]*_.+/g)) &&
                (!obj._ref || (itemType !== "button" || obj._ref.indexOf("Readonly") === -1))) {
                //設定Tab
                if (obj._ref && obj._ref.indexOf("TopButton") > -1 && obj._js && obj._js.indexOf("LinkURL") > -1)
                	item = GenHtmlElements("GenHtmlElements", obj, "li");
                else
                	item = GenHtmlElements("GenHtmlElements", obj, itemType);
                
                if (item !== undefined) {
                    if (obj.remark) {
                        if (obj.remark.indexOf("disable") > -1) item.setAttribute("disabled", true); //設定disabled
                        if (obj.remark.indexOf("hide") > -1) $(item).css("display", "none"); //設定隱藏
                        if (obj.remark.indexOf("important") > -1) item.setAttribute("important", true); //設定為"重要"，需有明顯標示
                    }
                    if (obj.block && obj.block.match(/.+_btn[\d]+_.+/g)) {
                        //[增加BTN] 判別屬性
                        item.setAttribute("block", obj.block);
                        if (addBtnSearchIdx !== "") item.setAttribute("jsonRowIdx", addBtnSearchIdx);
                        //[增加BTN] 隱藏初始編輯區
                        SetAddBtnBlockItemHidden(obj, item);
                    } else {
                        isHide1stAddBtnBlock = false;
                    }

                    //判斷是否需加入 必填檢核
                    AddValidReqField(obj, item, itemType);
                    //判斷是否需加入 格式檢核
                    AddValidFormatField(obj, item, itemType);
                    //判斷是否需加入 起、迄日檢核
                    AddValidSEDateField(obj, item, itemType);

                    AppendItem(rootDiv, item, obj);
                    if (itemType !== "title" && itemType !== "resultlist" && !selectOptionsObj["IsReadonlyPersonalList"]) {
                        GenBindingScripts("GenBindingScripts", obj);
                    }
                }
            }
        }
    }
    else if (type === "Array") {
        ShowArray("", obj, type);
    }
}

function AppendItem(root, item, obj) {
    if (item === undefined || root === undefined || root === null) return;

    var lCol = item.getAttribute('col');
    var lRow = item.getAttribute('row');
    var lRequireNewLine = item.getAttribute('requirenewline');
    var display = item.getAttribute('displayname');
    var appendDiv = false;
    //取得fieldset的legend名稱
    var legendName = GetFieldsetLegendName(obj);

    appendDiv = ShowContainerDiv(obj, item, legendName, lCol, lRow, lRequireNewLine);

    //產生綁定元素的label for
    var lbFItem = GenLayouts("render_Labelfor", obj, item);

    //設定TAB鍵導航控制次序(PS:Safari不支援)
    item.setAttribute("tabindex", ++Cnt_tabIndex);
    if (lbFItem !== undefined) {
        containerDiv.append(item);
        //設定TAB鍵導航控制次序(PS:Safari不支援)
        lbFItem.setAttribute("tabindex", ++Cnt_tabIndex);
        containerDiv.appendChild(lbFItem);
    } else {
        //與legend名稱相同則不用顯示
        if (lCol === "0" && legendName === $(item).text()) $(item).css("display", "none");

        //[MANTIS:0025843] 避免相鄰textbox太接近以致無法區分
        if ($(containerDiv).find(":last").is("input:text") && $(item).is("input:text"))
            containerDiv.innerHTML += "&nbsp;&nbsp;";

        containerDiv.appendChild(item);
        if (display !== undefined && display !== null && item.type !== "button") containerDiv.innerHTML += display;
    }

    if (appendDiv) {
        var isMoveToHolder = false;
        //設定Tab
        if (obj._ref && obj._ref.indexOf("TopButton") > -1 && obj._js && obj._js.indexOf("LinkURL") > -1)
			isMoveToHolder = GenLayouts("render_TabNav", obj, `cqs-tabnav-holder_${obj.displayname.match(/^\d+\.|[O]+/) ? "Detail" : "Master"}`); //綁入Tab區塊
//        	isMoveToHolder = GenLayouts("render_TabNav", obj, `cqs-tabnav-holder_${obj.displayname.match(/^\d+\.|[a-zA-Z]+/) ? "Detail" : "Master"}`); //綁入Tab區塊
        else
        	isMoveToHolder = GenLayouts("render_Holder", obj, "cqs-button-holder"); //綁入DIV Holder區塊

        //產生fieldset
        if (!isMoveToHolder) GenLayouts("render_Fieldset", obj, item, legendName, root);
    }
    currentRow = lRow;
    currentCol = lCol;
}

//設定目前DIV的屬性
function ShowContainerDiv(obj, item, legendName, lCol, lRow, lRequireNewLine) {
    var appendDiv = false, clsName = "";

    if (containerDiv === undefined) {
        containerDiv = document.createElement('div');
        containerDiv.setAttribute('col', lCol);
        containerDiv.setAttribute('row', lRow);
        containerDiv.setAttribute('width', '100%');
        if (lCol === "0" && legendName === $(item).text()) clsName = GetClassName(parseInt(lCol) + 1, obj.uitype);
        containerDiv.className = clsName + GenCss(1, "div-clear");
        appendDiv = true;
    }
    else {
        var divCol = containerDiv.getAttribute('col');
        var divRow = containerDiv.getAttribute('row');
        divCol = GetIndentCode(divCol);
        divRow = GetIndentCode(divRow);
        if (lCol === "0" && legendName === $(item).text()) {
            clsName = GetClassName(parseInt(lCol) + 1, obj.uitype);
        }
        else if (obj.uitype && obj.uitype !== "controller") {
            clsName = GetClassName(parseInt(lCol), obj.uitype);
        }
        var lCmp = currentCol < lCol;
        //遇到增加過的[增加BTN]區塊時，需append Div
        if (obj.id && obj.id.match(/button_.+_btn[1-9][\d]*_.+/g) &&
            obj.displayname && obj.displayname.indexOf("增加") > -1) {
            lRequireNewLine = "1"; lCmp = true;
        }
        if (lRequireNewLine === "1" && lCmp) {
            containerDiv = document.createElement('div');
            containerDiv.className = clsName + GenCss(1, "div-clear");
            containerDiv.setAttribute('condition', 1);
            containerDiv.setAttribute('col', lCol);
            containerDiv.setAttribute('row', lRow);
            containerDiv.setAttribute('width', '100%');
            appendDiv = true;
        }
        else if (lRow === divRow) {
            appendDiv = false;
        }
        else {
            containerDiv = document.createElement('div');
            containerDiv.className = clsName + GenCss(2, "div-clear");
            containerDiv.setAttribute('condition', 2);
            containerDiv.setAttribute('col', lCol);
            containerDiv.setAttribute('row', lRow);
            containerDiv.setAttribute('width', '100%');
            appendDiv = true;
        }
    }

    //加入AddButton區塊中移除按鈕的父div樣式
    if (obj.uitype === "button" && obj.value === "-" && $(item).attr("id").match(/_btn[\d]+_/g)) {
        //[AddButton]區塊DIV
        $(containerDiv).css("position", "relative");
        //加入CSS
        $(containerDiv).addClass("dis-flex");
    }

    return appendDiv;
}


//設定Html的JavaScript屬性
//jsonStr: eg. onclickSetStatus('基本資料',::localStorage)
function SetJsAttr(inputDom, jsonStr) {
    if (inputDom !== undefined) {
        var tmpFuncArr = [], tmpJsMapping = {}, tmpId = inputDom.id, _cusCSS = "";

        if (jsonStr !== undefined && jsonStr !== "") {
            var _idx = jsonStr.indexOf("procparam");
            if (_idx > -1) {
                _cusCSS = jsonStr.substr(_idx);
                _cusCSS = _cusCSS.substring(0, _cusCSS.indexOf("')") + 2);
                if (_cusCSS !== "") {
                    jsonStr = jsonStr.replace(_cusCSS, "");
                    _cusCSS = _cusCSS.replace(/this/g, `'${tmpId}'`);
                }
                jsonStr = jsonStr.replace(/^;|;$/g, "").replace(/;;/g, ";");
            }

            _idx = jsonStr.indexOf("checkRequired");
            if (_idx > -1) jsonStr = jsonStr.replace("checkRequired", "true");

            if (jsonStr.indexOf(";") > -1)
                tmpFuncArr = jsonStr.split(";");
            else if (jsonStr !== "")
                tmpFuncArr = [jsonStr];

            if (_cusCSS !== "") tmpFuncArr.push(_cusCSS);

            //組出Html的JavaScript屬性
            tmpFuncArr.forEach(function (jsVal, i) {
                if (jsVal.indexOf("(") > -1) {
                    var tmpJs = jsVal.match(/\(.+/g)[0];
                    var jsArr = jsVal.replace(tmpJs, "").match(/[a-z]+|[A-Z][a-z]*([A-Z]*[a-z]*)*/g);
                    if (jsArr && jsArr.length > 0) {
                        var funcAttr = jsArr[0]; //Html屬性 eg.onclick,onchange
                        if (!funcAttr.match(/[A-Z]+[a-z]*/g)) {
                            //若funcAttr非以Html的"event trigger attribute"開頭，則不加入render(此部分為BindingScript做)
                            var funcText = `${jsArr[1]}${tmpJs}`;
                            var funcExist = tmpJsMapping[funcAttr];
                            if (funcExist) tmpJsMapping[funcAttr] = `${funcExist};${funcText}`;
                            else tmpJsMapping[funcAttr] = funcText;
                        }
                    }
                }
            });
        }

        Object.keys(tmpJsMapping).forEach(function (key, i) {
            var attrVal = `${tmpJsMapping[key]};`;
            $(inputDom).attr(key, attrVal);
        });
    }
    return inputDom;
}


function IsUIElement(obj) {
    var bRes = IsValidObject(obj.id) && IsValidObject(obj.displayname) && IsValidObject(obj.uitype)
        || Array.isArray(obj.ResultListItems) || IsTitle(obj) || IsController(obj) || IsBindingField(obj);
    return bRes;
}


function IsBindingField(obj) {
    var bRes = IsValidObject(obj.uitype) && obj.uitype === "field";
    return bRes;
}


function IsTitle(obj) {
    var bRes = IsValidObject(obj.segmentation) && obj.segmentation === "title";
    return bRes;
}


function IsController(obj) {
    var bRes = IsValidObject(obj.remark) && obj.remark === "controller";
    return bRes;
}


function IsValidObject(obj) {
    var bRes = false;
    bRes = !(obj === null || obj === undefined);
    return bRes;
}


function IsSegmentation(obj) {
    var bRes = IsValidObject(obj.segmentation) && IsValidObject(obj.data);
    return bRes;
}


function IsIndentObject(displayname) {
    var names = ['radio', 'check box', '#增加BTN', '#+BTN', '#展開BTN'];
    if (names.indexOf(displayname) >= 0) return true;
    else return false;
}


function isNumberKey(evt) {
    evt = window.event || evt;
    var charCode = evt.which ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}


function isAlphaKey(evt) {
    evt = window.event || evt;
    var charCode = evt.which ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 65 || charCode > 122 || charCode > 90 && charCode < 97))
        return false;
    return true;
}


function GetType(object) {
    if (object === null) {
        return "null";
    }
    else if (object === undefined) {
        return "undefined";
    }
    else if (object.constructor === stringConstructor) {
        return "String";
    }
    else if (object.constructor === arrayConstructor) {
        return "Array";
    }
    else if (object.constructor === objectConstructor) {
        return "Object";
    }
    else {
        return "unknown";
    }
}


function GetClassName(indent, uiType) {
    return `${uiType}-${indent}`;
}


function GetIndentCode(val) {
    return val === undefined ? -1 : val;
}


function GetFixedTextLen(text) {
    var startIdx = 0;
    if (text === undefined || text === null) {
        text = "";
        startIdx === 0;
    }
    else startIdx = text.length;
    for (i = startIdx; i < 30; i++) {
        text += "&nbsp;";
    }
    return text;
}


function NewSegmentation(pid, segData) {
    $('#' + pid).append('<h2>' + data + '</h2>');
}

//隱藏空值欄位(增加BTN區塊)
function HideEmptyElement() {
    Object.keys(selectOptionsObj).forEach(function (val, i) {
        if (val["id"] && val["id"].match(/_btn[\d]+_/g)) {
            //[增加BTN]內區塊隱藏
            var isHide = true;
            $.each(val, function (j, element) {
                if (element["emptyFlag"] && element["emptyFlag"] > 0) {
                    isHide = false; return false;
                }
            });
            if (isHide) {
                $.each(val, function (j, element) {
                    $(`#${element["id"]}`).css("display", "none");
                });
            }
        }
    });
}