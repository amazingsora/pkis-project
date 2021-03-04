function render_checkbox(element, parentname, changeKey = null, disabledAttr = "", preImplementJs = "", hiddenAttr = "", addedParentName = "") {
    var obj;//合併須刪除
    var tmpId;
    obj = $(`<div></div>`);
    var selItemDisabled = disabledAttr;
    var element_js = "", preJs = "";

    //checkbox 控制項
    if (element.segmentation) {
        //第一層是checkbox
        //obj.append(`<label>${getAddressedColon(element.segmentation)}</label><br />`);
        //var block = $("<blockquote></blockquote>");
        obj.append(`<label ${get_element_css("checkbox", "label")}>${getAddressedColon(element.segmentation)}</label>`);
        var block = $(`<div ${get_element_css()}></div>`);
        jQuery.each(element.option, function (j, val) {
            var tmpObj = getColumnName();
            //block.append(`<label><input type="checkbox" name="" value="" data-bind="checked:${tmpObj}">${val.item}</label>`);

            tmpId = getCheckId();
            //formShow 查詢(腫瘤心理)報錯
            //block.append(`<div ${get_element_css("checkbox", "div", "v")} ${formShow}><input type="checkbox" name="${val.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled}/><label for="${tmpId}">${val.item}</label></div>`);
            block.append(`<div ${get_element_css("checkbox", "div", "v")}><input type="checkbox" name="${val.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled}/><label for="${tmpId}">${val.item}</label></div>`);

            arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.option[${j}].checked);`);
            // 腫瘤心理 - 查詢 - checkbox 排除
            if (cluster !== "TM" && ajccType !== "QRY" && masterSpec !== "REC") {
                // [JSV] 設定KO function：若父項不選，則全子項應反選
                element_js = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` +
                             `self.callFun_${tmpObj} = function(value) { if (typeof setChildrenChecked === "function") setChildrenChecked("${tmpObj}", value, false); `;
                if (val.follow && val.follow[0].js) {
                    var tmpFuncArr = val.follow[0].js.indexOf(",") > -1 ? val.follow[0].js.split(",") : [val.follow[0].js];
                    $.each(tmpFuncArr, function (idx, func) {
                        // render時需執行的JS
                        if (func === "FormShow" || func === "OpenDetail") {
                            element_js += `${func}("${tmpObj}", value, 'checked');`;
                            if (func === "FormShow") {
                                preJs = ` ${func}("${tmpObj}", null, 'checked');`;
                            }
                        }
                    });
                }
                element_js += " }";
            }

            arr1.push(element_js);
            if (preJs !== "") arr1.push(preJs);
            arr2.push(`${knockoutPath(parentname)}.option[${j}].checked = viewModel.${tmpObj}();`);

            if (val.follow) {
                //後面還有控制項
                validDisplay.push(val.item);
                validCnt++;
                var blockSec = $(`<div ${get_element_css()} data-vaildation="abc"></div>`); //abc
                jQuery.each(val.follow, function (f, fobject) {
                    if (fobject.method === "radio" || fobject.method === "checkbox") {
                        if (typeof setJsvValidationForDom === "function" &&
                            element.segmentation.indexOf("★") === 0)
                            setJsvValidationForDom(element, null, "", element.segmentation);

                        blockSec.append(Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]", null, "", "", "", addedParentName));
                    } else if (fobject.method === "text") {
                        if (fobject.item) {
                            validDisplay.push(fobject.item);
                            validCnt++;
                        }
                        blockSec.append(Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]", null, "", "", "", addedParentName));
                    } else {
                        blockSec.append(Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]", null, "", "", "", addedParentName));
                    }
                });
                block.append(blockSec);
            } 
			else {
                if (typeof setJsvValidationForDom === "function" &&
                    element.segmentation.indexOf("★") === 0)
                    setJsvValidationForDom(element, null, "", element.segmentation);
            }
            //block.append("<br />");
        });
        obj.append(block);
        obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
    }
    else {
        //非第一層的checkbox
        var block = $(`<div ${get_element_css()}></div>`);
        //if (element.item) obj.append(`<label>${element.item}</label>`);

        if (element.option) {
            jQuery.each(element.option, function (j, val) {
                var tmpObj = getColumnName();
                tmpId = getCheckId();
                //block.append(`<label><input type="checkbox" value="${val.item}" data-bind="checked:${tmpObj}" />${val.item}</label>`);
                //block.append(`<div ${get_element_css("checkbox", "div", "h")}><input type="checkbox" name="${element.segmentation || element.id || val.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label></div>`);


                //MANTIS Case: 0025221 -> Leo 20180712 S
                //html = `<input type="checkbox" name="${element.segmentation || element.id || val.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label>`;
                html = `<input type="checkbox" onclick="fCheckboxYN(this , '${addedParentName}${element.segmentation || element.id || val.id}')"  name="${addedParentName}${element.segmentation || element.id || val.id}" id="${tmpId}" value="${val.item}" data-bind="checked:${tmpObj}" ${selItemDisabled} /><label for="${tmpId}">${val.item}</label>`;
                //MANTIS Case: 0025221 -> Leo 20180712 E


                var tmpBlockObj = $(`<div ${get_element_css("checkbox", "div", "h")}${val.follow ? ` rule-validation="rvcheckbox"` : ``} style="white-space:nowrap;">#BlockContent</div>`).removeClass("checkbox-inline");
                block.append(tmpBlockObj[0].outerHTML.replace("#BlockContent", html));

                if (val.follow) {
                    //加入一行空的DIV才會對齊
                    if (val.follow[0].method === "checkbox") {
                        //block.find(`div > input[value="${val.item}"]`).parent().append(`<div ${get_element_css("checkbox", "div", "h")}></div>`);
                        block.find(`div > label[for="${tmpId}"]`).parent().append(`<div ${get_element_css("checkbox", "div", "h")}></div>`);
                    }

                    //validDisplay.push(val.item);
                    validDisplay.push((element.item ? element.item + " -> " : "") + val.item);
                    validCnt++;
                    jQuery.each(val.follow, function (f, fobject) {
                        var tmpFollowType = (fobject.method === "checkbox") ? "checkbox" : "radio";

                        if (fobject.method === "radio" || fobject.method === "checkbox") {
                            //block.append("<blockquote>" + Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]") + "</blockquote>");

                            //block.append(`<blockquote name="check2nd-list">${Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]")}</blockquote>`);
                            //block.find("blockquote > div.col-md-9").removeClass("col-md-9").addClass("checkbox-custom checkbox-primary checkbox-inline");
                            html = Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]", null, "", "", "", addedParentName);
                            // 使checkbox-custom會不受到radio-custom的CSS影響
                            if ($(html).find("input:checkbox").length > 0) tmpFollowType = "checkbox";

                            //MANTIS Case: 0025221 -> Leo 20180706 S
                            //var tmpRdoObj = $(`<div ${get_element_css(tmpFollowType, "div", "h")}>#CheckFollows</div>`).removeClass("checkbox-inline").removeClass("radio-inline");
                            var tmpRdoObj = $(`<div ${get_element_css("", "", "h")}>#CheckFollows</div>`).removeClass("checkbox-inline").removeClass("radio-inline");
                            //MANTIS Case: 0025221 -> Leo 20180706 E

                            var _html = tmpRdoObj[0].outerHTML.replace("#CheckFollows", html);
                            block.append(_html);
                            //block.append(Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]"));
                            if (fobject.method === "checkbox") block.find("div.col-md-9").removeClass("col-md-9").addClass(`checkbox-custom checkbox-primary ${formShow}`);
                        }
                        else {
                            if (val.follow.length == 1) {
                                //block.append(Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]"));
                                //block.find(`div > input[value="${val.item}"]`).parent().append(`<div ${get_element_css("checkbox", "div", "h")}>${Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]")}</div>`);
                                html = `<tmp>${Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]", null, "", "", "", addedParentName)}</tmp>`;
                                var _tmpObj = $(html).find("div:first").css("display", "inline");
                                html = _tmpObj.html();
                                block.find(`div > label[for="${tmpId}"]`).parent().append(`<div ${get_element_css("checkbox", "div", "h")}>${html}</div>`);
                                // 對AJCC Stage的<label>作處理：替換成<span>
                                $.each(block.find(`label[class*='col-md-3']`), function (iLb, objLb) {
                                    var textLb = $(objLb).text();
                                    $(objLb).replaceWith(`<span style="padding-right:5%">${textLb}</span>`);
                                });
                            } else {
                                tmp = $(Json2HTML(fobject, `${parentname}.option[${j}].follow[${f}]`, null, "", "", "", addedParentName));
                                if (fobject.remark && fobject.remark === "disable" && fobject.item.toUpperCase().indexOf("STAGE") > -1) {
                                    tmp = tmp.removeClass("form-group row").addClass("checkbox-custom checkbox-primary checkbox-inline");
                                    block.append(tmp).find(`div > label[for="${tmpId}"]`).parent().addClass("radio-inline").append(`<div ${get_element_css("radio", "div", "h")}></div>`);

                                    // 對AJCC Stage的<label>作處理：替換成<span>
                                    $.each(block.find(`label[class*='col-md-3']`), function (iLb, objLb) {
                                        var textLb = $(objLb).text();
                                        $(objLb).replaceWith(`<span style="padding-right:5%">${textLb}</span>`);
                                    });
                                } else {
                                    //block.append("<blockquote>" + Json2HTML(fobject, parentname + ".option[" + j + "].follow[" + f + "]") + "</blockquote>");
                                    //block.append(`<div for="checkbox.follows" style="white-space:nowrap;">${$(tmp).html()}</div>`);
                                    block.append(`<div for="checkbox.follows" >${$(tmp).html()}</div>`);
                                }
                            }
                        }
						//加入一行空的DIV才會對齊
						block.find(`div > input[value="${val.item}"]`).parent().append(`<div ${get_element_css("checkbox", "div", "h")}></div>`);
                    });
                }
                else {
                    //加入一行空的DIV才會對齊
                    block.find(`div > input[value="${val.item}"]`).parent().append(`<div ${get_element_css("checkbox", "div", "h")}></div>`);
                    //if (block.find(`div > label[for="${tmpId}"]`).html().length < 15)
                    //    block.find(`div > label[for="${tmpId}"]`).css("padding-right", "90px").parent().append(`<div ${get_element_css("checkbox", "div", "h")}></div>`);
                }

                //if (j != element.option.length - 1) block.append('<br>');
                obj.append(block);
                arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.option[${j}].checked);`);
                // [JSV] 設定KO function：若有項目選取，則其父項應一併選取；若全子項未選，則父項不選

                element_js = `
                    self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);
                    self.callFun_${tmpObj} = function(value) { if (typeof setJsvParentChecked === "function") setJsvParentChecked("${tmpObj}", value); 
                `;
                if (element.js) {
                    var tmpFuncArr = element.js.indexOf(",") > -1 ? element.js.split(",") : [element.js];
                    $.each(tmpFuncArr, function (idx, func) {
                        if (func === "DisplayItem") {
                            // [治療]首次治療類別 DisplayItem
                            element_js += ` DisplayItem('${tmpObj}', value, 'checked', '${displayItemArr[0]}');`;
                        }
                        else {
                            // eg. [診斷-尚未治療] js: unTreatment
                            if (func !== "FormShow") element_js += ` ${func}('${tmpObj}', value, 'checked');`;
                        }

                        // [治療]預設：若未勾選，將該項目的內容隱藏
                        if (func === "OpenDetail" && !element.checked)
                            arr1.push(`${func}('${tmpObj}', null, 'checked');`);
                    });
                }
                else if (val.follow && val.follow[0].js) {
                    if (val.follow && val.follow[0].js) {
                        var tmpFuncArr = val.follow[0].js.indexOf(",") > -1 ? val.follow[0].js.split(",") : [val.follow[0].js];
                        $.each(tmpFuncArr, function (idx, func) {
                            // render時需執行的JS
                            if (func === "FormShow" || func === "OpenDetail") {
                                element_js += `${func}("${tmpObj}", value, 'checked');`;
                            }
							if (func === "FormShow")
                                arr1.push(`${func}('${tmpObj}', null, 'checked');`);
                        });
                    }
                }
                element_js += "}";

                arr1.push(element_js);
                arr2.push(`${knockoutPath(parentname)}.option[${j}].checked = viewModel.${tmpObj}();`);
            });

            if (element.item)
                //obj = $(`<div><div class="form-group row"><label ${get_element_css("checkbox", "label")}>${element.item}</label>${obj.html()}</div></div>`);
                obj = $(`<div><div class="form-group row">${getAddressedColon(element.item)}${obj.html()}</div></div>`);
        }
    }

    return obj;//合併後刪除
}

function getCheckId() {
    checkIdx++;
    return "checkbox" + checkIdx;
}