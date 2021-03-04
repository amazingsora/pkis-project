var fieldsetClassMatchName = "收案基本資料|個管服務|追蹤|首次治療類別|預約資訊|核心成員|團隊會議|診療計畫書|個案管理|癌篩新收"; //設定fieldset寬度需滿版
var hasPreItemEmptyDisplay = false; //判斷前面控制項是否displayname為空


function render_Labelfor(processCode, obj, item) {
    var lbFItem = undefined;

    if (processCode === "render_Labelfor") {
        if (obj !== undefined && item !== undefined) {
            if (obj.uitype === "checkbox" || obj.uitype === "radio") {
                if (containerDiv !== undefined && obj.id) {
                    lbFItem = document.createElement("label");
                    lbFItem.setAttribute("for", obj.id);
                    //lbFItem.appendChild(item);
                    if (item.getAttribute('displayname') !== undefined) {
                        if (item.getAttribute('displayname') === "")
                            hasPreItemEmptyDisplay = true;
                        else
                            lbFItem.innerHTML += item.getAttribute('displayname');
                    } else {
                        hasPreItemEmptyDisplay = true;
                    }
                    //設定隱藏屬性
                    if (obj.remark.indexOf("hide") > -1 ||
                        obj.block && isHide1stAddBtnBlock && $(item).is(":hidden"))
                        $(lbFItem).css("display", "none");
                }
            }
        }
    }

    return lbFItem;
}


function render_Fieldset(processCode, obj, item, legendName, root) {
    var isPack = undefined;

    if (processCode === "render_Fieldset") {
        var lCol = item.getAttribute('col');
        var lRow = item.getAttribute('row');
        isPack = false;

        if (lCol === "0") {
            //產生fieldset
            if (fieldsetCnt === 0) {
                if (containerFieldset !== undefined) {
                    //fieldsetr結尾
                    root.append(containerFieldset);
                    containerFieldset = undefined;
                    isPack = true;
                } else {
                    //開始包fieldset
                    CreateFieldset(legendName, obj, lCol, lRow);
                    flag_SelectOtherOption = 0;
                }
            }
            else {
                if (containerFieldset !== undefined) {
                    //fieldsetr結尾
                    fieldsetCnt = 0; //歸零，表示fieldset即將結尾
                    if ($(containerFieldset).find("legend").text() === "首次治療類別") {
                        //調整[診斷][首次治療類別] label欄位文字內容可自動換行
                        //$(containerFieldset).find("div:last").find("label:last").css(lblCss_AutoLineBreak);
                        $(containerFieldset).find("div:last").find("label:last").addClass("label_auto_line_break");
                    }
                    root.append(containerFieldset);
                    containerFieldset = undefined;
                    isPack = true;

                    //開始包fieldset
                    CreateFieldset(legendName, obj, lCol, lRow);
                    flag_SelectOtherOption = 0;
                } else {
                    root.append(containerDiv);
                    flag_SelectOtherOption = 0;
                    containerDiv = undefined;
                    isPack = true;
                }
            }
        }
        else {
            if (fieldsetCnt > 0 && containerFieldset !== undefined) {
                if (obj.ResultListItems) {
                    //fieldsetr結尾
                    fieldsetCnt = 0; //歸零，表示fieldset即將結尾
                    root.append(containerFieldset);
                    containerFieldset = undefined;
                    isPack = true;

                    //開始包fieldset
                    containerFieldset = document.createElement("fieldset");
                    containerFieldset.className = "skh-fls-border skh-order cqs-fls-width";
                }

                containerFieldset.appendChild(containerDiv);
                flag_SelectOtherOption = 0;
                fieldsetCnt++;
            } else {
                root.append(containerDiv);
                flag_SelectOtherOption = 0;
                containerDiv = undefined;
                isPack = true;
            }
        }
    }

    return isPack;
}


function render_Holder(processCode, obj, holderId) {
    var isAppendToHolder = undefined;

    if (processCode === "render_Holder") {
        //TopButton處理
        if (obj._ref && obj._ref.indexOf("TopButton") > -1) {
            var holderBlock = $(`#${holderId}`);
            if (holderBlock.length > 0) {
                holderBlock.append(containerDiv);
                isAppendToHolder = true;
            }
        }
    }

    return isAppendToHolder;
}


function render_TabNav(processCode, obj, tabholderId) {
    var isAppendToTabHolder = undefined;

    if (processCode === "render_TabNav") {
        //TopButton處理
        var tabholderBlock = $(`#${tabholderId}`);
        if (tabholderBlock.length > 0 && tabholderBlock.html().indexOf(obj.displayname) === -1) {
            tabholderBlock.append($(containerDiv).html());
            isAppendToTabHolder = true;
        }
    }

    return isAppendToTabHolder;
}


function GetFieldsetLegendName(obj) {
    var rtnLegnedName = "";
    if (obj.displayname && obj.displayname !== "") {
        rtnLegnedName = obj.displayname;
        if (rtnLegnedName.indexOf("FIX") === 0) {
            rtnLegnedName = rtnLegnedName.replace("FIX", "");
        }
        else if (obj.uitype === "button" && (rtnLegnedName === "展開" || rtnLegnedName === "增加")) {
            var tmpDisplayItem = eval(`json.data[${dataIdx}].docdetail[${rowIdx + 1}]`); 
            if (tmpDisplayItem) rtnLegnedName = tmpDisplayItem["displayname"];
        }
    }
    rtnLegnedName = rtnLegnedName.replace(/\:|\：/g, "");
    return rtnLegnedName;
}


function CreateFieldset(legendName, obj, lCol, lRow) {
    //開始包fieldset
    containerFieldset = document.createElement("fieldset");

    //判斷若是有remark為hide時，將fieldset隱藏
    //if (obj.remark.match(/hide/gi)) containerFieldset.setAttribute("style", "display:none");

    //if (legendName && fieldsetClassMatchName.indexOf(legendName) > -1)
    //    containerFieldset.className = "skh-fls-border skh-order cqs-fls-width";
    //else
    //    containerFieldset.className = "skh-fls-border skh-order cqs-fls-width-fifty";
 	if (obj._ref) {
		if (obj._ref.indexOf('半版') > -1) {
			containerFieldset.className = "skh-fls-border skh-order cqs-fls-width-fifty";
		}
		else if (obj._ref.indexOf('三欄') > -1) {
			containerFieldset.className = "skh-fls-border skh-order cqs-fls-width-thirty";
		}
		else {
			containerFieldset.className = "skh-fls-border skh-order cqs-fls-width";
		}
	}
	else
		containerFieldset.className = "skh-fls-border skh-order cqs-fls-width";
    //if (legendName && fieldsetClassMatchName.indexOf(legendName) > -1)
    //    containerFieldset.className = "skh-fls-border skh-order cqs-fls-width";
    //else {
    //    if (obj._ref) {
    //        if (obj._ref.indexOf('滿版') > -1) {
    //            containerFieldset.className = "skh-fls-border skh-order cqs-fls-width";
    //        } else {
    //            containerFieldset.className = "skh-fls-border skh-order cqs-fls-width-fifty";
    //        }
    //    } else {
    //        containerFieldset.className = "skh-fls-border skh-order cqs-fls-width-fifty";
    //    }
    //}

    if (obj.uitype !== "button" ||
        obj.uitype === "button" && obj.format.match(EvButtonBlockReg)) {
        containerLegend = document.createElement("legend");
        containerLegend.setAttribute('col', lCol);
        containerLegend.setAttribute('row', lRow);
        containerLegend.innerHTML = legendName;
        containerFieldset.appendChild(containerLegend);
    }
    containerFieldset.appendChild(containerDiv);
    fieldsetCnt++;
}


/**
 * render_Layout.js
 * 設計書指定各項目的前台CSS樣式
 * @param {any} obj   物件ID
 * @param {any} style CSS樣式(可帶#className或style)
 */
function SetProcCSS(obj, style) {
    var kvp, $obj, type = obj.substring(0, obj.indexOf("_"));
    //console.log(`SetProcCSS:${type} => ${obj} => ${style}`);
    if ("label,radio,checkbox".indexOf(type) === -1) return;

    if (style.indexOf("#") === 0)
        kvp = style.replace(/#/g, '');
    else {
        style = style.replace(/\s/g, '');
        //console.log(style);
        if (style.lastIndexOf(';') === style.length - 1) style = style.substring(0, style.lastIndexOf(';'));
        //console.log(style);
        kvp = style.split(/;/).map(s => s.split(':'));
        //console.log(kvp);
    }
    //console.log(`${kvp}: ${kvp.length}, ${typeof (kvp)}`);

    switch (type) {
        case "label":
            $obj = $(`#${obj}`);
            break;
        case "radio":
        case "checkbox":
            $obj = $(`label[for="${obj}"`);
            break;
    }

    if (typeof kvp === "string") {
        $obj.addClass(`${kvp}`);
    } else {
        kvp.forEach(function (key, i) {
            for (var j = 0; j < key.length; j += 2)
                $obj.css(key[j], key[j + 1]);
        });
    }

    /*
    switch (type) {
        case "label":
            //console.log(`CQSCSS - label: ${$(`#${obj}`).attr("id")}`);
 
            if (typeof (kvp) === "string")
                $(`#${obj}`).addClass(`${kvp}`);
            else {
                kvp.forEach(function (key, i) {
                    for (var j = 0; j < key.length; j += 2)
                        $(`#${obj}`).css(key[j], key[j + 1]);
                });
            }
            break;
        case "radio":
        case "checkbox":
            if (typeof (kvp) === "string")
                $(`label[for="${obj}"`).addClass(`${kvp}`);
            else {
                kvp.forEach(function (key, i) {
                    for (var j = 0; j < key.length; j += 2)
                        $(`label[for="${obj}"`).css(key[j], key[j + 1]);
                });
            }
            break;
    }*/
}

function SetProcParam(obj, id) {
    console.log(`SetProcParam: ${obj.value}, ${id}`);
}


/**
 * render_Layout.js
 * [設定] 若有增加過，則隱藏初始[增加BTN]編輯區
 * @param {any} obj  json Object
 * @param {any} item Html Element
 */
function SetAddBtnBlockItemHidden(obj, item) {
    if (obj && obj.block && obj.block.match(/button_.+_btn0_.+/g)) {
        var lFmt = obj.format;
        if (lFmt !== "#增加BTN" && lFmt !== "#+BTN" && isHide1stAddBtnBlock && $(item).length > 0)
            $(item).css("display", "none");
    }
}


/**
 * render_Layout.js
 * 隱藏初始[增加BTN]編輯區(總數需超過1才行)
 * @param {any} jsonData 傳入json.data[dataIdx]
 * @param {HTMLElement} $body 目前實際操作上的根節點
 */
function SetAddButtonInitialBlockItemHidden(jsonData, $body) {
    var path = $.JSONPath({ data: jsonData, keepHistory: false });
    var qryRS = path.query(`$.docdetail[?(/#增加BTN|#+BTN/.test(@.format) && /^button_.+_btn0_.+/.test(@.block))]`);
    if (qryRS) {
        qryRS.forEach(function (item) {
            //先計算初始[增加BTN]編輯區內的物件數
            var initCnt = path.query(`$.docdetail[?(@.block == '${item.id}')]`).length;
            //計算所有該block內的物件數
            var allCnt = path.query(`$.docdetail[?(/${item.id.replace(/_btn0_0/i, "")}.+/.test(@.block))]`).length;

            //判斷[增加BTN]編輯區是否總數超過1以上，才須將初始編輯區隱藏
            if (Math.ceil(allCnt / initCnt) - 1 > 0) {
                var $item = $body.children().find(`#${item.id}`).parent().next(), $tmp;

                while (!$item.is(":hidden") && $item.children(":first").is(":hidden")) {
                    $item.hide();
                    $item = $item.next();
                }
            }
        });
    }
}

/**
 * render_Layout.js
 * 隱藏診療計畫書於醫師待辦清單填寫時的多專科團隊會議的TNMStage
 * @param {any} jsonData 傳入json.data[dataIdx]
 * @param {any} $body    目前實際操作上的根節點(#divbody)
 */
function HideTPInsideMST(jsonData, $body) {
    //var path = $.JSONPath({ data: jsonData, keepHistory: false });
    //var $arr = path.query(`$..docdetail[?(/多專科團隊會議/.test(@.displayname))]`);
    //if ($arr) {
    //    $.each($arr, function () {
    //        var $id = $(this).attr("id"), $v;
    //        var $obj = $body.find(`#${$id}`).parent();

    //        $.each($body.find(`#${$id}`).parent().nextAll(), function () {
    //            $v = $(this);
    //            if ($v.attr("col") > $obj.attr("col") &&
    //                $v.attr("row") > $obj.attr("row"))
    //                $v.hide();
    //        });

    //        $obj.hide();
    //    });
    //}
}


/**
 * 將所有textarea能依內容自動展開(寬高度)，寬度為fieldset寬度-25px，高度為8列(若無內容時自動縮回3列)
 * @param {any} $body 目前實際操作上的根節點(#divbody)
 */
function SetTextAreaAutoHeight($body) {
    $body.find("textarea").each(function (i, v) {
        //設定寬度
        $(v).width($(v).parents("fieldset").width() - 25);
        console.log(`SetTextAreaAutoHeight:${$(v).val()}`);
    }).bind("keyup keydown", function () {
        var $obj = $(this);

        if ($obj.val() !== "")
            $obj.attr("rows", "8");
        else if ($obj.val().trim() === "")
            $obj.attr("rows", "3");
    }).keyup();
}


/**
 * 自訂畫面CSS樣式或其他無法於Render內處理的事項
 * @param {any} $body 目前實際操作上的根節點(#divbody)
 */
function SetCustomLayout($body) {
    var $obj;

    //個管-治療處理
    if (cluster === "COL" && ajccType === "QRY" && masterSpec === "REC" && dataType) {
        if (dataType.match(/治療/g)) { // && !dataid.match(/019CHO/g)
            //將上方手術若尚未填值時將AJCC隱藏，若已填值則AJCC開啟(因FormShow開啟的關係)
            //判斷[手術]區段內有填值(含填寫[病理期別])則全開
            var $IsOpen = false;
            $body.find('legend').each(function (i, obj) {
                $obj = $(obj);

                if ($obj.text() === "手術") {
                    //[手術]區段內是否有值，若有則全開
                    $obj.nextAll().children().each(function (i, obj) {
                        if (!checkBindValIsEmpty(getBindingObj(obj))) {
                            $IsOpen = true;
                            return false;
                        }
                    });

                    //再判斷是否有增加空的紀錄，若有則全開
                    if (!$IsOpen) {
                        $obj.nextAll().find(`input[block]`).each(function (j, item) {
                            if ($(item).attr("block").match(/_btn[1-9]\d*_0/)) {
                                $IsOpen = true;
                                return false;
                            }
                        });
                    }

                    //[手術]區段開啟
                    if ($IsOpen) {
                        //執行FormShow呈現正確的AJCC
                        $obj = $obj.parent().find(`input[type='radio'][value='${sAJCC}']`).attr("id");
                        FormShow($obj, `${sAJCC}`);
                    }
                    else {
                        //將[病理期別]關閉
                        $obj.parent().find("div[class^='radio'] input[type='radio'][value*='AJCC']").each(function (i, v) {
                            $obj = $(v);
                            if (!$obj.parent().is(":hidden") && !$obj.prop("checked")) {
                                Object.values($obj.parent().nextAll()).forEach(function (item) {
                                    $(item).hide();
                                });

                                $obj.parent().hide();
                            }
                        });
                    }
                }
            });
        }
        //else if (dataType.match(/個管服務/g)) {
        //    var _msg = "";
        //    $body.find(`input:checked[displayname='其他']`).each(function (i, obj) {
        //        $(obj).nextAll().each(function (j, item) {
        //            if ($(item).is("input:text, textarea") && checkBindValIsEmpty(getBindingObj(item))) {
        //                _msg += $(obj).parents("fieldset").children(":first").text() + " -> ";
        //                $obj = $(obj).parent().prev().children(":first");
        //                if ($obj.is("label"))
        //                    _msg += $obj.text();
        //                else
        //                    _msg += $obj.attr("displayname") ? $obj.attr("displayname") : "";

        //                _msg += $(obj).attr("displayname") ? " - " + $(obj).attr("displayname") : "" + "\n";
        //            }
        //        });
        //    });
        //}
    }

    //判斷當fieldset內最末個div的樣式為position: relative時，將fieldset底部往下延伸
    $body.find("fieldset > div:last-child").each(function (i, obj) {
        if ($(obj).css("position") === "relative") $(obj).parent().css({ "padding-bottom": "75px" });
    });


    //判斷當fieldset內div下的子元素均為[隱藏]時，將div也一併隱藏
    $body.find("fieldset > div > *:first-child").each(function (i, obj) {
        var $parent = $(obj).parent(), $isHide = true;

        if ($parent.children().length === 1 && $(obj).is(":hidden"))
            $parent.hide();
        else {
            $parent.children().each(function (j, item) {
                if (!$(item).is(":hidden")) {
                    $isHide = false;
                    return false;
                }
            });

            if ($isHide) $parent.hide();
            $isHide = true;
        }
    });


    //判斷當fieldset下的div子元素均為[隱藏]時，將fieldset也一併隱藏
    $body.find("fieldset > div").each(function (i, obj) {
        var $parent = $(obj).parent(), $isHide = true;

        if ($parent.children().length === 2 && $(obj).is(":hidden"))
            $parent.hide();
        else {
            $parent.children().each(function (j, item) {
                if ($(item)[0].tagName !== "LEGEND" && !$(item).is(":hidden")) {
                    $isHide = false;
                    return false;
                }
            });

            if ($isHide) $parent.hide();
            $isHide = true;
        }
    });
}