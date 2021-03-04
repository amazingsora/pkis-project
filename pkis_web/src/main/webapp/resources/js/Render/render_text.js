function render_text(element, parentname, changeKey = null, inDisabledAttr = "", preImplementJs = "", hiddenAttr = "", addedParentName = "") {
    var obj;//合併須刪除
    //文字框
    //合併須刪除var
    var tmpObj = getColumnName();
    // html disabled屬性
    var disabledAttr = element.remark && element.remark === "disable" ? "disabled" : "";
    if (inDisabledAttr === "disabled") disabledAttr = inDisabledAttr;
    // html readonly屬性
    var readonlyAttr = (element.remark && element.remark === "readonly") ? "readonly" : "";
    // html size屬性
    var sizeAttr = "";
    var sizeNum = element.format && pattern.test(element.format) ? element.format.match(/(\d+(.[0-9]{1,2})?)/g)[0] : "0";

    if (sizeNum.indexOf(".") > -1) {
        sizeNum = (parseInt(sizeNum.split(".")[0]) + 1 + parseInt(sizeNum.split(".")[1])).toString();
    }
    sizeAttr = `size="${sizeNum}"`;
    if (parseFloat(sizeNum) > 40) sizeAttr = `size="40"`; //限制不超過40

    // html maxlength屬性
    var maxlengthAttr = "";
    if (sizeNum > 0) {
        maxlengthAttr = `maxlength="${sizeNum}"`;
    }
    // html hide屬性
    var hideAttr = hiddenAttr !== "" ? hiddenAttr : (element.remark && element.remark === "hide" ? "hide" : "");

    var html = `<div ${get_element_css()}><input type="text" id="${addedParentName}${element.id}" name="${tmpObj}" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${get_element_css("text", "")} ${sizeAttr} ${maxlengthAttr} autocomplete="off" /></div>`;
    var preExecuteJsArr = [], element_js = "", tmpFuncArr, tmpFunc, tmpPara;

    obj = $(`<div></div>`);
    if (element.segmentation) {
        //第一層元件
        obj.append(`<label ${get_element_css("text", "label")}>${getAddressedColon(element.segmentation)}</label>`);

        if (element.segmentation.indexOf("病歷號") > -1) {
            if (ajccType !== "COMM")
                arr1.push(`viewModel.${tmpObj}("${getChrtNo()}");`);
        }
        else if (element.segmentation.indexOf("身分證號") > -1) {
            html = $(`<div>${html}</div>`).find(`input:text[id='${element.id}']`).attr("name", "idno").parent().parent().html();
        }
        else if (element.segmentation.indexOf("出生日期") > -1) {
            html = $(`<div>${html}</div>`).find(`input:text[id='${element.id}']`).attr("name", "birth").parent().parent().html();
        }
        else if (ajccType === "QRYREC" && dataType === "治療" && element.segmentation.indexOf("首次治療類別") === 0) {
            html = $(`<div>${html}</div>`).find(`input:text[id='${element.id}']`).parent().parent().html();
        }
        else if (ajccType === "METNEW" && cluster === "MGM" && masterSpec === "REC" && element.segmentation.indexOf("日期") > -1) {
            //多專科團隊會議 - 新增專用
            html = `<div>${html}</div>`;
        }
        else {
            if (element.follow) {
                html = `<div ${get_element_css()}><div class="input-group">${$(html).html()}</div></div>`;
            }
        }

        if (typeof getJsvHtmlText === "function")
            obj.append(getJsvHtmlText(element, html, setJsvElementDisplay(validDisplay)));
        else
            obj.append(html);

        obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
    }
    else if (element.label) {
        //cell中包含文字和文字框  ex:預計完成日 #7D
        obj.append(`<label>${getAddressedColon(element.label)}</label>`);

        if (element.js) {
            if (element.js.indexOf("BSA") > -1 && element.parameters) {
                obj.append(`<input type="text" parameters="${element.parameters}" onchange="${element.js}()" id="${element.id}" name="" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`);
            } else {
                obj.append(`<input type="text" onchange="${element.js}()" id="${element.id}" name="" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`);
            }
        } else {
            obj.append(`<input type="text" id="${element.id}" name="" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`);
        }
        if (element.ref && element.ref.indexOf("BSA") > -1) {
            obj.append(`<input type="text" id="${element.id}" name="${element.ref}" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`);
        }
    }
    else if (element.item) {
        //非第一層元件
        if (changeKey !== null && pattern.test(element.format)) {
            obj.append(element.item);
        }
        else {
            if (element.item !== "&nbsp")
                obj.append(`<label ${get_element_css("text", "label")} ${hiddenAttr}>${getAddressedColon(element.item)}</label>`);
        }

        if (element.js) {
            if (element.js.indexOf("BSA") > -1 && element.parameters) {
                // 計算BSA
                html = `<input type="text" id="${element.id}" parameters="${element.parameters}" onchange="${element.js}()" name="" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${get_element_css("text", "")} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`;
            }
            else if (element.js.indexOf("clearclose") > -1) {
                // 復案
                //20190808 - ATTIE
                html = `<input type="text" id="${element.id}" onchange="${element.js}('${tmpObj}', this.value, 'value')" name="" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`;
                if (element.format && element.format.trim().match(/^\#\dD/)) {
                    html = `<div class="col-md-9">${html}</div>`;
                    if (typeof getJsvHtmlText === "function") html = getJsvHtmlText(element, html, setJsvElementDisplay(validDisplay));
                }
            }
            else {
                tmpFuncArr = element.js.indexOf(";") > -1 ? element.js.split(";") :
                    element.js.indexOf(",") > -1 ? element.js.split(",") : [element.js];
                $.each(tmpFuncArr, function (idx, func) {
                    tmpFunc = func.indexOf(",") > -1 ? func.split(",")[0] : func;
                    tmpPara = func.indexOf(",") > 0 ? func.split(",")[1] : "";
                    // [治療]首次治療類別 eg. DisplayItem
                    if (tmpFunc.indexOf("DisplayItem") > -1) {
                        element_js += `DisplayItem('${tmpObj}', this.value, 'value', '${displayItemArr[0]}'); `;
                    }
                    else if (tmpFunc.match(/sum[\w]*Score|sum[\w]+|setTotalNumber/g)) {
                        // 需加總值，並寫入目標控制項 eg. sumPieces
                        element_js += `sumTargetNum('${tmpObj}', this.value, 'value', '${tmpFunc}'); `;
                    }
                    else if (tmpFunc.match(/set[\w]*Group/g)) {
                        // 需判斷後，設定目標控制項 eg. setTRUSBxRiskGroup(disabled時沒作用)
                        if (disabledAttr === "") element_js += `setTargetGroup('${tmpObj}', this.value, 'value', '${tmpFunc}', '${tmpPara}'); `;
                    }
                    else if (tmpFunc.match(/calDiffdays|setDiffDays/g)) {
                        // 計算日期區間幾日之結果，並寫入目標控制項
                        if (disabledAttr === "") element_js += `calDiffdays('${tmpObj}', this.value, 'value', '${tmpFunc}', '${displayItemArr[0]}'); `;
                    }
                    else if (tmpFunc.match(/calInterval/g)) {
                        // 計算日期區間幾日之結果，並寫入目標控制項
                        if (disabledAttr === "") {
                            if (element.item.indexOf("起日") > -1) {
                                element_js += `${tmpFunc}('SDate', '${tmpObj}'); `;
                            } else if (element.item.indexOf("迄日") > -1) {
                                element_js += `${tmpFunc}('EDate', '${tmpObj}'); `;
                            }
                        }
                    }
                    else if (tmpFunc === "setDiagnosisDate") {
                        // 設定"初診斷日期"
                        element_js += `${tmpFunc}('${tmpObj}', this.value, 'value'); `;

                        // 若resultdata內"初診斷日期"有值，則設定此值
                        var targetJItem = getJsonMappingElement("resultdata", "初診斷日期", "個案查詢", false);
                        if (targetJItem["初診斷日期"] !== undefined && targetJItem["初診斷日期"] !== "")
                            element.data = targetJItem["初診斷日期"];
                    }
                    else if (tmpFunc === "setRootTreatDays") {
                        // 計算日期區間幾日之結果，並寫入目標控制項
                        if (disabledAttr === "") element_js += `${tmpFunc}('${tmpObj}', this.value, 'value', '${element.item}'); `;
                    }
                    else element_js += `${tmpFunc}(); `;
                });

                //20190808 - ATTIE
                html = `<input type="text" onchange="${element_js}" id="${element.id}" name="" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`;
                if (element.format && element.format.trim().match(/^\#\dD/)) {
                    html = `<div class="col-md-9">${html}</div>`;
                    if (typeof getJsvHtmlText === "function") html = getJsvHtmlText(element, html, setJsvElementDisplay(validDisplay));
                }
            }
        }
        else {
            element_js = "";
            //20190808 - ATTIE
            html = `<input type="text" ${element_js} id="${element.id}" #name format="${element.format}" data-bind="value:${tmpObj}" ${get_element_css("text", "")} ${disabledAttr} ${readonlyAttr} ${hiddenAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" />`;
            if (element.format && element.format.trim().match(/^\#\dD/)) {
                html = `<div class="col-md-9">${html}</div>`;
                if (typeof getJsvHtmlText === "function") html = getJsvHtmlText(element, html, setJsvElementDisplay(validDisplay));
            }

            //Leo
            var _name = "";
            if (element.item === "初診斷日期:") _name = ` name="txDiagnosisDate" `;

            html = html.replace("#name", _name);
        }

        // eg. setBSA
        if (element.ref && element.ref.indexOf("BSA") > -1) {
            html = `<input type="text" id="${element.id}" name="${element.ref}" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off"/>`;
        }

        // 有提前需render的JS
        if (preImplementJs !== "") {
            preExecuteJsArr = preImplementJs.indexOf(",") > -1 ? preImplementJs.split(",") : [preImplementJs];
            preExecuteJsArr.forEach(function (jsVal, idx) {
                if (jsVal === "ShowHistologicGrade" && (!element.data || element.data === "")) {
                    // Histologic Grade: 若不能帶回時，則顯示選項〇G1、 〇G2、 〇G3、 〇不明
                    html = ShowHistologicGrade(html, element, tmpObj, disabledAttr);
                }
                else {
                    // [治療]首次治療類別 DisplayItem
                    if (jsVal === "DisplayItem") {
                        var sDisplayItemJS = `DisplayItem('${tmpObj}', this.value, 'value', '${displayItemArr[0]}'); `;
                        if (element_js.indexOf(sDisplayItemJS) === -1) {
                            element_js += sDisplayItemJS;
                        }

                        if ($(html).is("input:text"))
                            html = $(html).attr("onchange", element_js);
                        else
                            html = $(html).find("input:text").attr("onchange", element_js);
                    }
                }
            });
        }

        obj.append(html);
        if (changeKey !== null && pattern.test(element.format) && element.remark === undefined) {
            obj = $(`<div>${obj.html()}</div>`);
        }
        else {
            obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
        }
    }
    else {
        if (element.format) {
            if (element.format) {
                if (element.format.trim() === "#8D") html = `<div ${get_element_css()}><input type="text" id="${element.id}" name="${tmpObj}" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${get_element_css("text", "")} ${sizeAttr} ${maxlengthAttr} autocomplete="off" /></div>`;
            }

            var word = element.format.replace(pattern, "");
            element.format = element.format.replace(word, "");

            if (word && word.indexOf("%") > -1) html = `<div ${get_element_css()}><input type="text" id="${element.id}" name="" data-bind="value:${tmpObj}" ${disabledAttr} ${readonlyAttr} ${sizeAttr} ${maxlengthAttr} autocomplete="off" /> %</div>`;

            if (pattern.test(element.format)) {
                if (typeof getJsvHtmlText === "function")
                    html = getJsvHtmlText(element, html, setJsvElementDisplay(validDisplay));

                if (changeKey !== null) {
                    obj.append($(html).html());
                } else {
                    obj.append(html);
                }
            }

            if (word && word.indexOf("%") === -1) obj.append(`<span ${get_element_css("text", "span")}>${word}</span>`);
        } else {
            //無label
            if (typeof getJsvHtmlText === "function")
                obj.append(getJsvHtmlText(element, html, setJsvElementDisplay(validDisplay)));
            else
                obj.append(html);
        }
    }

    if (element.format && element.format.trim().match(/^\#\dD/)) {
        arr1.push(`setDatePickerFormat('${element.id}', '${element.format}');`);
    }

    if (ajccType === "QRYREC" && dataType === "治療" &&
        element.segmentation && element.segmentation.indexOf("首次治療類別") === 0) {
        arr1.push(`mouseoverShowText('${element.id}');`);
    }

    // [JSV] validCnt > 0 表示有深入Json子階層，檢核提示字串array需pop至父階層
    if (validCnt > 0) {
        validDisplay.length = validDisplay.length - validCnt;
        validCnt = 0;
    }

    if (resultdata && resultdata !== "") {
        matchColName = `${element.segmentation || element.label || element.item}`;
        if (LocalStorage_Key_ReadonlyJson.indexOf("READONLY") > -1)
            matchColVal = setReadonlySrcData(matchColName, resultdata);
        else if (element.controller && element.controller.indexOf("#") === 0)
            matchColVal = setSourceData(matchColName, element.controller.replace(/\..+/g, ""));
        else
            matchColVal = setSourceData(matchColName, resultdata);

        // 將json內的resultdata填入基本資料
        if (matchColVal) {
            if (element.format) {
                if (element.item && element.item.indexOf("收案日期") === -1) {
                    matchColVal = `${knockoutPath(parentname)}.data`;
                } else {
                    if (element.data !== "") matchColVal = element.data;
                }
            } else {
                matchColVal = `'${matchColVal}'`;
            }
        }
        else {
            matchColVal = `${knockoutPath(parentname)}.data`;
            if (element.js && element.ref) {
                // eg. setAuditCaseMaxNum("Ref.應派案最大值(病歷數)")
                matchColVal = eval(`${element.js}("${element.ref}")`);
            }
        }
    } else {
        if (element.remark &&
            (element.remark.indexOf("default") > -1 || element.remark.indexOf("系統日期") > -1)) {
            matchColVal = getToDay(0);

            //個案審查-選案
            if (element.intcode && element.intcode.match(/BF2S|BF4S/i)) {
                matchColVal = getFormatDateStr(0, matchColVal, element.intcode).substr(0, 4);
            }
            else if (element.format === "#7D") {
                matchColVal = parseInt(matchColVal.substring(0, 4)) - 1911 + matchColVal.substring(4, 8);
            }
        }
        else {
            matchColVal = `${knockoutPath(parentname)}.data`;
            if (element.js && element.ref) {
                // eg. setAuditCaseMaxNum("Ref.應派案最大值(病歷數)")
                matchColVal = eval(`${element.js}("${element.ref}")`);
            }
        }
    }

    //====================================[Leo S]====================================
    var sVal = "";

    if (ajccType === "QRYREC" && dataType === "基本資料" && element.item) {
        if (element.item.indexOf("臨床") > -1) {
            if (element.item.indexOf("首次復發或轉移") === 0)
                sVal = setreclinicalTNM(element, sAJCC);
            else
                sVal = setinitclinicalTNM(element, sAJCC);
            arr1.push(`self.${tmpObj} = ko.observable("${sVal}");`);
        }
        if (element.item.indexOf("病理") > -1) {
            if (element.item.indexOf("首次復發或轉移") === 0)
                sVal = setrepathologyTNM(element, sAJCC);
            else
                sVal = setinitpathologyTNM(element, sAJCC);
            arr1.push(`self.${tmpObj} = ko.observable("${sVal}");`);
        }
        if (element.item === "最終期別Stage:") {
            sVal = setfinalStage(element, sAJCC);
            arr1.push(`self.${tmpObj} = ko.observable("${sVal}");`);
        }
    }
    //====================================[Leo E]====================================

    if (sVal === "") {
        arr1.push("self." + tmpObj + " = ko.observable(" + matchColVal + ");");
    }

    element_js = "";
    // [JSV] 設定KO function：若textbox有值，則其父項應選取
    arr1.push(`self.${tmpObj}.subscribe(function(newValue) { self.callFun_${tmpObj}(newValue); }, this);` +
        `self.callFun_${tmpObj} = function(value) { if (typeof setJsvParentChecked === "function") setJsvParentChecked("${tmpObj}", value); `);
    if (element.js === "LocalStorageProcess" && element.ref) {
        element_js += `${element.js}('${element.ref.split(",")[0]}', '${element.ref.split(",")[1]},'+value+',${element.ref.split(",")[2]}'); `;
    }
    if (element.item && element.js) {
        tmpFuncArr = element.js.indexOf(";") > -1 ? element.js.split(";") : [element.js];
        $.each(tmpFuncArr, function (idx, func) {
            tmpFunc = func.indexOf(",") > -1 ? func.split(",")[0] : func;
            tmpPara = func.indexOf(",") > 0 ? func.split(",")[1] : "";
            if (tmpFunc.match(/set[\w]*Group/g)) {
                // 需判斷後，設定目標控制項 eg. setTRUSBxRiskGroup(disabled時沒作用)
                element_js += `setTargetGroup('${tmpObj}', value, 'value', '${tmpFunc}', '${tmpPara}'); `;
            }
        });
    }
    if (element_js !== "") arr1.push(element_js);
    arr1.push("}");
    arr2.push(`${knockoutPath(parentname)}.data = viewModel.${tmpObj}();`);
    if (element.format === "#1000A") arr1.push(` fDisplayInline('${element.id}'); `);//MANTIS Case:0025247 -> Leo 20180724

    if (element.follow) {
        var tmp;
        jQuery.each(element.follow, function (f, fobject) {
            tmp = Json2HTML(fobject, parentname + ".follow[" + f + "]", element.dropdownitem);
            if (obj.find("div.input-group").html() === undefined) {
                var tmpObjHtml;
                if (pattern.test(fobject.format)) {
                    var tmpBlockStyle = $(tmp).find("div").attr("class");
                    if (tmpBlockStyle && !$(tmp).children(":first").is("div"))
                        obj.append(`<div class="${tmpBlockStyle}">${$(tmp).html().replace("<div ", "</div><div ")}`);
                    else {
                        //處理follow之後控制項不換行
                        tmpObjHtml = obj.html();
                        obj.html(tmpObjHtml.replace("</div>", `${tmp}</div>`));
                    }
                }
                else {
                    if (obj.find(`div > input[id="${element.id}"]`).parent().length > 0)
                        obj.find(`div > input[id="${element.id}"]`).parent().addClass("input-group").append(tmp);
                    else if (obj.find("div.form-group").length > 0) {
                        //處理follow之後控制項不換行
                        tmpObjHtml = obj.html();
                        obj.html(tmpObjHtml.replace("</div>", `${tmp}</div>`));
                    }
                    else {
                        obj.append(tmp);
                    }
                }
            } else {
                obj.find("div.input-group").append(tmp);
            }
        });
    }

    // 調整受到bootstrap css影響的textbox父項、子項不對齊 eg. [診斷] Child-Pugh Score
    if (obj.find(".form-group.row.input-group").length > 0 &&
        element.follow && element.follow[0].method === "text") {
        $.each(obj.find(".form-group.row.input-group"), function (idx, domObj) {
            $(domObj).removeClass("form-group row");
        });
    }

    // 隱藏
    if (hideAttr !== "") $(obj).find("div:first").css("display", "none");

    return obj;//合併後刪除
}

// 設定：初診斷 臨床AJCC Stage 及 臨床TNM 資訊
function setinitclinicalTNM(paraElement, sAJCC) {
    var sVal = "";
    if (paraElement.item.indexOf("臨床AJCC Stage") > -1) {
        sVal = fJasonData(json, "診斷", "臨床期別", "臨床Stage：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床T") > -1) {
        sVal = fJasonData(json, "診斷", "臨床期別", "臨床 T：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床N") > -1) {
        sVal = fJasonData(json, "診斷", "臨床期別", "臨床 N：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床M") > -1) {
        sVal = fJasonData(json, "診斷", "臨床期別", "臨床 M：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床BCLC分期") > -1) {
        sVal = fJasonData(json, "診斷", "臨床期別", "臨床BCLC分期：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床DUKES'分期") > -1) {
        sVal = fJasonData(json, "診斷", "臨床期別", "臨床Dukes'分期：▼", sAJCC);
    }
    return sVal;
}

// 設定：初診斷 病理AJCC Stage 及 病理TNM 資訊
function setinitpathologyTNM(paraElement, sAJCC) {
    var sVal = "";
    if (paraElement.item.indexOf("病理AJCC Stage") > -1) {
        sVal = fJasonData(json, "治療", "病理期別", "病理Stage：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理T") > -1) {
        sVal = fJasonData(json, "治療", "病理期別", "病理 T：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理N") > -1) {
        sVal = fJasonData(json, "治療", "病理期別", "病理 N：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理M") > -1) {
        sVal = fJasonData(json, "治療", "病理期別", "病理 M：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理BCLC分期") > -1) {
        sVal = fJasonData(json, "治療", "病理期別", "病理BCLC分期：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理DUKES'分期") > -1) {
        sVal = fJasonData(json, "治療", "病理期別", "病理Dukes'分期：▼", sAJCC);
    }
    return sVal;
}

// 設定：首次復發或轉移臨床AJCC Stage 及 首次復發或轉移臨床TNM 資訊
function setreclinicalTNM(paraElement, sAJCC) {
    var sVal = "";
    if (paraElement.item.indexOf("臨床AJCC Stage") > -1) {
        sVal = fJasonData(json, "復發", "臨床期別", "臨床Stage：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床T") > -1) {
        sVal = fJasonData(json, "復發", "臨床期別", "臨床 T：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床N") > -1) {
        sVal = fJasonData(json, "復發", "臨床期別", "臨床 N：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床M") > -1) {
        sVal = fJasonData(json, "復發", "臨床期別", "臨床 M：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床BCLC分期") > -1) {
        sVal = fJasonData(json, "復發", "臨床期別", "臨床BCLC分期：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("臨床DUKES'分期") > -1) {
        sVal = fJasonData(json, "復發", "臨床期別", "臨床Dukes'分期：▼", sAJCC);
    }
    return sVal;
}

// 設定：首次復發或轉移病理AJCC Stage 及 首次復發或轉移病理TNM 資訊
function setrepathologyTNM(paraElement, sAJCC) {
    var sVal = "";
    if (paraElement.item.indexOf("病理AJCC Stage") > -1) {
        sVal = fJasonData(json, "復發", "病理期別", "病理Stage：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理T") > -1) {
        sVal = fJasonData(json, "復發", "病理期別", "病理 T：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理N") > -1) {
        sVal = fJasonData(json, "復發", "病理期別", "病理 N：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理M") > -1) {
        sVal = fJasonData(json, "復發", "病理期別", "病理 M：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理BCLC分期") > -1) {
        sVal = fJasonData(json, "復發", "病理期別", "病理BCLC分期：▼", sAJCC);
    }
    if (TrimAllBlank(paraElement.item, "g").toUpperCase().indexOf("病理DUKES'分期") > -1) {
        sVal = fJasonData(json, "復發", "病理期別", "病理Dukes'分期：▼", sAJCC);
    }
    return sVal;
}

// 設定：最終期別Stage
// 由"初診斷 病理AJCC Stage" 寫入
// 但是若無病理分期時，則由"初診斷 臨床AJCC Stage"寫入
function setfinalStage(paraElement, sAJCC) {
    var sVal = fJasonData(json, "治療", "病理期別", "病理Stage：▼", sAJCC);
    if (sVal === "") sVal = fJasonData(json, "診斷", "臨床期別", "臨床Stage：▼", sAJCC);
    return sVal;
}

// [治療] Histologic Grade: 若不能帶回時，則顯示選項〇G1、 〇G2、 〇G3、 〇不明
function ShowHistologicGrade(inHtmlStr, inElement, inTmpObj, disabledAttr) {
    var tmpDom = $(`<div ${get_element_css("radio", "div")}></div>`).addClass("col-md-9");
    var oriHtmlDom = $(inHtmlStr);
    var rtnHtml = "";
    oriHtmlDom.append(`<input type="radio" id="${inElement.segmentation || inElement.id}_G1" name="${inElement.segmentation || inElement.id}" value="G1" data-bind="checked:${inTmpObj}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.segmentation || inElement.id}_G1">G1</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.segmentation || inElement.id}_G2" name="${inElement.segmentation || inElement.id}" value="G2" data-bind="checked:${inTmpObj}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.segmentation || inElement.id}_G2">G2</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.segmentation || inElement.id}_G3" name="${inElement.segmentation || inElement.id}" value="G3" data-bind="checked:${inTmpObj}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.segmentation || inElement.id}_G3">G3</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.segmentation || inElement.id}_NAN" name="${inElement.segmentation || inElement.id}" value="不明" data-bind="checked:${inTmpObj}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.segmentation || inElement.id}_NAN">不明</label>`);

    tmpDom.append(oriHtmlDom);
    tmpDom.contents().removeClass();
    tmpDom.contents().find("input:text").hide(); //將原textbox隱藏
    rtnHtml = tmpDom[0].outerHTML;

    return rtnHtml;
}