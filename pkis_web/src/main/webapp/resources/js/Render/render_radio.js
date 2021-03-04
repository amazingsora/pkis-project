var valueIdx = 0;
function render_radio(element, parentname, changeKey = null, disabledAttr = "", preImplementJs = "", hiddenAttr = "", addedParentName = "") {
    var tmpObj;//合併須刪除
    var tmpId;
    var obj = $(`<div></div>`), block;
    var selItemDisabled = disabledAttr;
    var tmpProcArr, i, callFuncArr, callFuncStr, reactItemStr, comparedVal, koJsStr;

    if (disabledAttr === "") {
        //依Json取得的Ref.物件data值，判斷是否disabled
        if (element.js &&
            (element.js.indexOf("onEnable") > -1 || element.js.indexOf("onDisable") > -1)) {
            tmpProcArr = element.js.indexOf(";") > -1 ? element.js.split(";") : [element.js];
            for (i = 0; i < tmpProcArr.length; i++) {
                callFuncArr = tmpProcArr[i].indexOf(",") > -1 ? tmpProcArr[i].split(",") : [tmpProcArr[i]];
                callFuncStr = callFuncArr[0]; // eg. onEnable、onDisable
                reactItemStr = callFuncArr.length > 1 ? callFuncArr[1] : ""; //eg. 再補充已完成
                comparedVal = callFuncArr.length > 2 ? callFuncArr[2] : ""; //eg. Ref.已審待補充

                if (reactItemStr !== "" && comparedVal !== "" && 
                    (callFuncStr === "onEnable" || callFuncStr === "onDisable")) {
                    // 取得Json內對應key、value的element
                    var refJsonItem = getJsonMappingElement("ref", comparedVal, dataType, false);
                    console.log(`[${callFuncStr}] data:${refJsonItem.data}`);
                    if (refJsonItem.data !== comparedVal.replace("Ref.", ""))
                        //eval(`onDisable("${tmpObj}", value, "${reactItemStr}", "${comparedVal}") `);
                        selItemDisabled = " disabled";
                    //else
                        //eval(`onEnable("${tmpObj}", value, "${reactItemStr}", "${comparedVal}") `);
                    break;
                }
            }
        }
    }

    if (element.segmentation) {
        //第一層是radio
        obj.append(`<label>${getAddressedColon(element.segmentation)}</label><br />`);
        tmpObj = getColumnName();

        if (ajccType === "METNEW" && cluster === "MGM" && masterSpec === "REC" && element.segmentation.indexOf("理由") > -1) {
            //多專科團隊會議 - 新增專用
            obj.empty().append(`<label ${get_element_css("radio", "label")}>${getAddressedColon(element.segmentation)}</label>`);
            block = $(`<div ${get_element_css()}></div>`);
            $.each(element.option, function (i, val) {
                tmpId = getRadioId();
                block.append(`<div ${get_element_css("radio", "div")}><input type="radio" name="${addedParentName}${element.id || element.segmentation}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}"${selItemDisabled}><label for="${tmpId}">${val.item}</label></div>`);
                if (val.follow) {
                    //後面還有控制項
                    validDisplay.push(val.item);
                    validCnt++;
                    $.each(val.follow, function (f, fobject) {
                        if (fobject.method === "text") {
                            if (fobject.item) {
                                validDisplay.push(fobject.item);
                                validCnt++;
                            }
                            //block.find(`input[type="radio"][id="${tmpId}"]`).parent().append($(Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]")).html());
                            block.find(`input[type="radio"][id="${tmpId}"]`).parent().append(Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]"));
                            if (block.find(`div[class="col-md-6"]`).length > 0) block.find(`div[class="col-md-6"]`).css({ "float": "none", "display": "inline-block" });
                        }
                    });
                } else {
                    if (typeof setJsvValidationForDom === "function" &&
                        element.segmentation.indexOf("★") === 0)
                        setJsvValidationForDom(element, null, "", element.segmentation);
                }
            });
            obj.append(block);
            obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
        }
        else {
            obj.empty().append(`<label ${get_element_css("radio", "label")}>${getAddressedColon(element.segmentation)}</label>`);
            block = $(`<div class="input-group"></div>`);
            jQuery.each(element.option, function (i, val) {
                tmpId = getRadioId();
                block.append(`<div ${get_element_css("radio", "div", "h")}><input type="radio" name="${addedParentName}${element.id || element.segmentation}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}"${selItemDisabled}><label for="${tmpId}">${val.item}</label>`);
                if (val.follow) {
                    //後面還有控制項
                    validDisplay.push(val.item);
                    validCnt++;
                    jQuery.each(val.follow, function (f, fobject) {
                        if (fobject.method === "text") {
                            if (fobject.item) {
                                validDisplay.push(fobject.item);
                                validCnt++;
                            }
                            block.append(Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]"));
                        }
                        else {
                            block.append("<blockquote>" + Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]") + "</div></blockquote>");
                        }
                    });
                } else {
                    if (typeof setJsvValidationForDom === "function" &&
                        element.segmentation.indexOf("★") === 0)
                        setJsvValidationForDom(element, null, "", element.segmentation);
                }
                block.append("<br />");
            });
            obj.append(block);
            obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
        }

        //arr1.push(`self.${tmpObj} = ko.observable(items[${rowIdx}].data);`);
        arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
        // [JSV] 設定KO function：若父項不選，則全子項應反選
        koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` +
                  `self.callFun_${tmpObj} = function(value) { if (typeof setChildrenChecked === "function") setChildrenChecked("${tmpObj}", value, false);`;
        if (element.js) {
            tmpProcArr = element.js.indexOf(";") > -1 ? element.js.split(";") : [element.js];
            for (i = 0; i < tmpProcArr.length; i++) {
                callFuncArr = tmpProcArr[i].indexOf(",") > -1 ? tmpProcArr[i].split(",") : [tmpProcArr[i]];
                callFuncStr = callFuncArr[0]; // eg. onEnable、onDisable
                reactItemStr = callFuncArr.length > 1 ? callFuncArr[1] : ""; //eg. 再補充已完成
                comparedVal = callFuncArr.length > 2 ? callFuncArr[2] : ""; //eg. Ref.已審待補充

                if (element.ref)
                    koJsStr += `${callFuncStr}("${tmpObj}", value, "${element.ref}"); `;
                else {
                    if (reactItemStr !== "" &&
                        (callFuncStr.indexOf("onEnable") === -1 && callFuncStr.indexOf("onDisable") === -1)) {
                        koJsStr += `${callFuncStr}("${tmpObj}", value, "${reactItemStr}", "${comparedVal}"); `;
                    }
                    else koJsStr += `${callFuncStr}("${tmpObj}", value); `;
                }
            }
        }
        arr1.push(`${koJsStr} }`);
        arr2.push(`${knockoutPath(parentname)}.data = viewModel.${tmpObj}();`);
    }
    else {
        //非第一層的radio
        if (element.option.length > 0 && element.option[0].item) {
            //直向
            tmpObj = getColumnName();
            //var block = $("<blockquote></blockquote>");
            block = $(`<div ${get_element_css()}></div>`); //blockquote
            jQuery.each(element.option, function (i, val) {
                tmpId = getRadioId();


                //MANTIS Case: 0025204 -> Leo 20180704 S
                //block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" name="${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);

                if (element.item === "Lymph dissection" || element.item === "Reconstruction" || element.item === "過去病史:" || element.item === "癌症既往史:" || element.item === "抽菸史:" || element.item === "喝酒:" || element.item === "嚼檳榔:" || element.item === "必要提報事項") {
                    block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" onclick="fDisabledYN('${val.item}' , '${element.id}')" name="${addedParentName}${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                } else if (element.item === "婚姻:" || element.item === "職業:" || element.item === "宗教信仰:") {
                    block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" onclick="fDisabledOne('${val.item}' , '${element.id}')" name="${addedParentName}${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                } else if (element.item === "結果" || element.item === "型態") {//MANTIS Case: 0025248 -> Leo 20180718
                    block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" onclick="fRadioYN('${val.item}' , '${addedParentName}${element.segmentation || element.id}', '${i}')" name="${addedParentName}${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                } else {
                    if (element.item === "治療狀態") {
                        var sOpenID = "";
                        var sCloseID = "";

                        if (val.item === "完成治療") {
                            sOpenID = element.id + "_0";
                            sCloseID = element.id + "_1";
                        } else if (val.item === "中斷治療") {
                            sOpenID = element.id + "_1";
                            sCloseID = element.id + "_0";
                        }

                        block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" onclick="fDisabled_Cure('${val.item}' , '${sOpenID}' , '${sCloseID}')" name="${addedParentName}${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                    }
                    else {
                        //block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" name="${element.segmentation || element.id}${addedParentName}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                        if (val.item === "無人回應") {
                            sOpenID = "";
                            sCloseID = element.id + "_0";
                            block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" onclick="fDisabled_Cure('${val.item}' , '${sOpenID}' , '${sCloseID}')" name="${addedParentName}${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                        } else if (val.item === "回應人員") {
                            sOpenID = element.id + "_0";
                            sCloseID = "";
                            block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" onclick="fDisabled_Cure('${val.item}' , '${sOpenID}' , '${sCloseID}')" name="${addedParentName}${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                        } else {
                            block.append(`<div ${get_element_css("radio", "div", element.option.length > 1 && element.option[1].follow || val.follow ? "v" : "h")}><input type="radio" name="${addedParentName}${element.segmentation || element.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);
                        }
                    }
                }
                //MANTIS Case: 0025204 -> Leo 20180704 E


                if (val.follow) {
                    //後面還有控制項
                    //validDisplay.push(val.item);
                    validDisplay.push((element.item ? element.item + " -> " : "") + val.item);
                    validCnt++;
                    jQuery.each(val.follow, function (f, fobject) {
                        var tmpFollowType = fobject.method === "checkbox" ? "checkbox" : "radio";

                        if (fobject.method === "text") {
                            if (fobject.item) {
                                validDisplay.push(fobject.item);
                                validCnt++;
                            }
                            //block.append(Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]"));
                            //block.find(`div > input[value="${val.item}"]`).parent().addClass("radio-inline").append(`<div ${get_element_css("radio", "div", "h")}>${Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]", val.follow)}</div>`);

                            if (val.follow.length === 1) {
                                //MANTIS Case: 0025221 -> Leo 20180712 S
                                //var tmpFollowHTML = `<div ${get_element_css(tmpFollowType, "div", "h")}>${Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]", val.follow)}</div>`;
                                var tmpFollowHTML = `<div ${get_element_css(tmpFollowType, "div", "h")}>${Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]", val.follow, "", "", "", addedParentName)}</div>`;
                                //MANTIS Case: 0025221 -> Leo 20180712 E

                                if (val.item === "") {
                                    block.find(`div > label[for="${tmpId}"]`).text(fobject.item);
                                    tmpFollowHTML = tmpFollowHTML.replace(fobject.item, "");
                                }
                                block.find(`div > label[for="${tmpId}"]`).parent().addClass("radio-inline").append(tmpFollowHTML);
                            }
                            else {
                                if (fobject.remark && fobject.remark === "disable" && fobject.item.toUpperCase().indexOf("STAGE") > -1) {
                                    tmp = $(Json2HTML(fobject, `${parentname}.option[${i}].follow[${f}]`, val.follow));
                                    //tmp = tmp.attr("for", "radio.follows").removeClass("form-group row").addClass("radio-custom radio-primary radio-inline");
                                    tmp = tmp.removeClass("form-group row").addClass("radio-custom radio-primary radio-inline").attr("rule-validation", "rvtext");
                                    block.append(tmp).find(`div > label[for="${tmpId}"]`).parent().addClass("radio-inline").append(`<div ${get_element_css("radio", "div", "h")}></div>`);
                                } else {
                                    block.find(`div > label[for="${tmpId}"]`).parent().addClass("radio-inline").append(Json2HTML(fobject, `${parentname}.option[${i}].follow[${f}]`, val.follow)).find("input.form-control").removeClass("form-control");
                                }
                            }
                        }
                        else if (fobject.method === "label") {
                            if (f !== val.follow.length - 1)
                                block.find("div > input:last").parent().append(fobject.data).append(`<div ${get_element_css(tmpFollowType, "div", "h")}></div>`);
                            else
                                block.find("div > :last").parent().append(fobject.data);
                        }
                        else {
                            //block.append(`<div for="radio.follows">${Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]")}</div>`);
                            //block.append(`<div for="radio.follows" ${get_element_css("radio", "div", "h")}>${Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]")}</div>`);
                            //block.append(`<div ${get_element_css("radio", "div", "h")} rule-validation="rvradio">${Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]")}</div>`);
                            // [MANTIS:0025145] 使CSS正常顯示
                            //var tmpRdoObj = $(`<div for="radio.follows" ${get_element_css("radio", "div", "h")}>#RadioFollows</div>`).removeClass("radio-inline");

                            //MANTIS Case: 0025221 -> Leo 20180712 S
                            //html = Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]");
                            html = Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]", null, "", "", "", addedParentName);
                            //MANTIS Case: 0025221 -> Leo 20180712 E

                            // 使checkbox-custom會不受到radio-custom的CSS影響
                            if ($(html).find("input:checkbox").length > 0) tmpFollowType = "checkbox";

                            //MANTIS Case: 0025204 -> Leo 20180629 S
                            //var tmpRdoObj = $(`<div ${get_element_css(tmpFollowType, "div", "h")} rule-validation="rvradio">#RadioFollows</div>`).removeClass("radio-inline");

                            var tmpRdoObj = "";
                            if (element.item && element.item.replace(/\:|\：/g, "").indexOf("期別") === element.item.replace(/\:|\：/g, "").length - 2) {
                                tmpRdoObj = $(`<div ${get_element_css(tmpFollowType, "div", "h")} >#RadioFollows</div>`).removeClass("radio-inline");
                            }
                            else if (fobject.method === "dropdownlist") {
                                // (Todo)暫時調整，待改parser
                                if (fobject.dropdownitem && fobject.dropdownitem !== "") {
                                    // eg. [追蹤]自訂->時、分
                                    if (fobject.dropdownitem === "Hour") {
                                        tmpRdoObj = $(`<div class="form-group row">#RadioFollows</div>`);
                                        html = html.replace("</select>", "</select>時");
                                    }
                                    else if (fobject.dropdownitem === "Minute")
                                        tmpRdoObj = $(`<div class="form-group row">#RadioFollows</div>`);
                                    else
                                        tmpRdoObj = $(`<div ${get_element_css(tmpFollowType, "div", "h")} rule-validation="rvradio">#RadioFollows</div>`).removeClass("radio-inline");
                                }
                                else if (fobject.item && fobject.item !== "")
                                    tmpRdoObj = $(`<div class="form-group row">#RadioFollows${fobject.item}</div>`);
                                else if (fobject.item === undefined && fobject.dropdownitem && fobject.dropdownitem !== "")
                                    tmpRdoObj = $(`<div class="form-group row">#RadioFollows${fobject.dropdownitem}</div>`);
                                else
                                    tmpRdoObj = $(`<div ${get_element_css(tmpFollowType, "div", "h")} rule-validation="rvradio">#RadioFollows</div>`).removeClass("radio-inline");
                            }
                            else {
                                tmpRdoObj = $(`<div ${get_element_css(tmpFollowType, "div", "h")} rule-validation="rvradio">#RadioFollows</div>`).removeClass("radio-inline");
                            }
                            //MANTIS Case: 0025204 -> Leo 20180629 E

                            html = tmpRdoObj[0].outerHTML.replace("#RadioFollows", html);
                            block.append(html);
                        }
                    });
                }
                else {
                    //加入一行空的DIV才會對齊
                     block.find(`div > label[for="${tmpId}"]`).parent().append(`<div ${get_element_css("radio", "div", "h")}></div>`);
                }
                //block.append("<br />");
            });

            if (element.item) obj.append(`<label ${get_element_css("radio", "label")}>${getAddressedColon(element.item)}</label>`);
            obj.append(block);
            obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
            // 將radio-inline造成的css位移移除

            //MANTIS Case: 0025221 -> Leo 20180710 S
            //obj.find("[class*='radio-inline'],[class*='checkbox-inline']").css({
            //    "margin-left": "0", "width": "100%"
            //});

            if (element.item !== "檢體") {
                obj.find("[class*='radio-inline'],[class*='checkbox-inline']").css({
                    "margin-left": "0", "width": "100%"
                });
            }
            //MANTIS Case: 0025221 -> Leo 20180710 E

            //Leo
            var sVal = "";
            if (element.item === "初診斷 臨床其他分期:") {
                sVal = fJasonData(json, "診斷", "臨床期別", "小細胞臨床分期：▼");
                arr1.push(`self.${tmpObj} = ko.observable("${sVal}");`);
            }
            //-------------------------------
            if (element.item === "初期診斷 病理其他分期:") {
                sVal = fJasonData(json, "治療", "病理期別", "小細胞病理分期：▼");
                arr1.push(`self.${tmpObj} = ko.observable("${sVal}");`);
            }

            if (sVal === "") {
                //arr1.push(`self.${tmpObj} = ko.observable(items[${rowIdx}].data);`);
                arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
            }

            // [JSV] 設定KO function：若有項目選取，則其父項應一併選取；若全子項未選，則父項不選
            koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` +
                      `self.callFun_${tmpObj} = function(value) { if (typeof setJsvParentChecked === "function") setJsvParentChecked("${tmpObj}", value);`;
            if (element.js) {
                tmpProcArr = element.js.indexOf(";") > -1 ? element.js.split(";") : [element.js];
                for (i = 0; i < tmpProcArr.length; i++) {
                    callFuncArr = tmpProcArr[i].indexOf(",") > -1 ? tmpProcArr[i].split(",") : [tmpProcArr[i]];
                    callFuncStr = callFuncArr[0]; // eg. onEnable、onDisable
                    reactItemStr = callFuncArr.length > 1 ? callFuncArr[1] : ""; //eg. 再補充已完成
                    comparedVal = callFuncArr.length > 2 ? callFuncArr[2] : ""; //eg. Ref.已審待補充

                    if (element.ref) {
                        if (element.js === "setChildPughScore")
                            koJsStr += `${callFuncStr}("${tmpObj}", value, "${element.ref}"); `;
                        else
                            koJsStr += `${callFuncStr}("${tmpObj}", value, "${element.ref}"); `;
                    }
                    else {
                        if (reactItemStr !== "" &&
                            (callFuncStr.indexOf("onEnable") === -1 && callFuncStr.indexOf("onDisable") === -1)) {
                            koJsStr += `${callFuncStr}("${tmpObj}", value, "${reactItemStr}", "${comparedVal}"); `;
                        }
                        else if (callFuncStr.match(/sum[\w]*Score|sum[\w]+/g)) {
                            // 需加總值，並寫入目標控制項
                            koJsStr += ` sumTargetNum("${tmpObj}", value, 'checked', '${callFuncStr}');`;
                        }
                        else {
                            if (callFuncStr === "DisplayItem") {
                                // [治療]首次治療類別 DisplayItem
                                koJsStr += ` DisplayItem('${tmpObj}', value, 'checked', '${displayItemArr[0]}');`;
                            }
                            else koJsStr += `${callFuncStr}("${tmpObj}", value); `;
                        }
                    }
                }
            }
            arr1.push(`${koJsStr} }`);
            //arr2.push(`${parentname}.data = viewModel.${tmpObj}();`);
            arr2.push(`${knockoutPath(parentname)}.data = viewModel.${tmpObj}();`);
        }
        else {
            //橫向
            //${get_element_css("radio", "div", "h")}
            // [MANTIS:0025145] 使CSS正常顯示
            //obj.append(`<label>${getAddressedColon(element.item)}</label>`);
            obj.append(`<span style="padding-right:5%">${getAddressedColon(element.item)}</span>`);
            //obj.append(`<label>${getAddressedColon(element.item)}</label><div ${get_element_css("radio", "div", "h")}></div>`);
            tmpObj = getColumnName();

            if (element.option.length > 0 && element.option[0].hitem) {
                jQuery.each(element.option[0].hitem, function (i, optionItem) {
                    tmpId = getRadioId();
                    //var _change = "", _html = `<label><input type="radio" name="${parentname + element.item}" value="${optionItem}" data-bind="checked:${tmpObj}"${selItemDisabled}#change>${optionItem}</label>`;
                    var _change = "";
                    // [MANTIS:0025145] 使CSS正常顯示
                    //var _html = `<label style="padding-right:8%"><input type="radio" name="${parentname}" value="${optionItem}" data-bind="checked:${tmpObj}"${selItemDisabled}#change>${optionItem}</label>`;
                    var _html = `<div style="display:inline-block; padding-right:5%;"><input type="radio" name="${addedParentName}${parentname}" id="${tmpId}" value="${optionItem}" data-bind="checked:${tmpObj}" ${selItemDisabled}#change /><label for="${tmpId}">${optionItem}</label></div>`;
                    if (element.js && element.ref) {
                        //有js,且是需連動的ajcc stage(unused)
                        if ((element.js.match('getAjccStage') !== null && element.ref === "setAjccStage" && element.parameters) ||
                            (element.ref.match('getAjccStage') !== null && element.js === "setAjccStage" && !element.parameters)) {
                            findAjccArrScript(element.js, element.ref, tmpObj, element.parameters);
                            if (element.js !== "setAjccStage") {
                                _change = ` onchange="${element.js}()"`;
                            }
                        }
                    }

                    obj.append(`${_html.replace("#change", _change)}`);
                });
            }

            arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);

            koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` + 
                      `self.callFun_${tmpObj} = function(value) { if (typeof setJsvParentChecked === "function") setJsvParentChecked("${tmpObj}", value);`;
            var element_js = ``, isNeedOnChangeJs = false;

            // eg. [診斷-臨床期別] js: FormShow,AJCC6;set8ClinicalStage
            if (element.js) {
                var tmpFuncArr = element.js.indexOf(";") > -1 ? element.js.split(";") : [element.js];
                var tmpFunc, tmpPara;
                $.each(tmpFuncArr, function (idx, func) {
                    tmpFunc = func.indexOf(",") > -1 ? func.split(",")[0] : func;
                    tmpPara = func.indexOf(",") > 0 ? func.split(",")[1] : "";
                    // render時需執行的JS
                    if (tmpFunc === "FormShow" || tmpFunc === "OpenDetail") {
                        element_js = `${tmpFunc}("${tmpObj}", "${tmpPara}", 'checked');`;
                        arr1.push(element_js);
                    }
                    // 條件值改變時連動執行的JS
                    else if (tmpFunc.match(/(set[\d]+[\w]*ClinicalStage[\-]?[\w]*)|(set[\d]+[\w]*PathologicStage[\-]?[\w]*)/g)) {
                        // 需設定AJCC Stage eg. set8ClinicalStage / set6PathologicStage ...
                        isNeedOnChangeJs = true;
                        koJsStr += ` setAJCCStagePara("${tmpObj}", value, 'checked', '${tmpFunc}');`;
                        // 若有值需先暫存selectOptionsObj
                        if (element.data && element.data !== "") {
                            element_js = `setAJCCStagePara("${tmpObj}", '${element.data}', 'checked', '${tmpFunc}');`;
                            arr1.push(element_js);
                        }
                    }
                    else if (tmpFunc.match(/set[\w]*Group/g)) {
                        // 需判斷後，設定目標控制項 eg. setTRUSBxRiskGroup
                        isNeedOnChangeJs = true;
                        koJsStr += ` setTargetGroup("${tmpObj}", value, 'checked', '${tmpFunc}', '${tmpPara}');`;
                    }
                });
                if (isNeedOnChangeJs) arr1.push(`${koJsStr} }`);
            }

            arr2.push(`${knockoutPath(parentname)}.data = viewModel.${tmpObj}();`);
        }
    }

    return obj;//合併後刪除
}

function getRadioId() {
    radioIdx++;
    return "radio" + radioIdx;
}