function render_dropdownlist(element, parentname, changeKey = null, disabledAttr) {
    var obj = $(`<div></div>`);//合併須刪除var
    tmpObj = getColumnName();
    var tmpId;
    var option = "options" + tmpObj;
    var nowOption = "", apiOption, optionText, optionValue, optionJson, element_js = "";
    var preChangeValue = ""; //for 預設帶入連動的value [MANTIS:0025137]
    var classAttr = "col-md-2 input-group";
    var cssAttr = `style="width:30%;"`; //強制設定寬度[150px]
    var tmp, select, mappingTbName, koJsStr = "", nowOptions = [];

    //下拉選單
    if (element.segmentation) {
        //第一層是select
        obj.append(`<label ${get_element_css("dropdown", "label")}>${getAddressedColon(element.segmentation)}</label>`);

        if (element.api) {
            var _val, _txt, hintItem = "片語";
            if (element.dropdownitem) hintItem = element.dropdownitem;

            //有API並已先行呼叫(癌別、醫師姓名片語)API)
            if (element.api === "onSelectCodeList") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
                if (element.parameters === "癌別")
                    _val = _txt = "中文";
                else
                    _val = _txt = hintItem;
            }
            //20191130 雲端
            else if (element.api === "onSelectCommonList") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
                _val = _txt = hintItem;
                hintItem = obj.displayname;
            }
            else if (element.api === "onSelectHints") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
                if (element.parameters === "醫師姓名片語")
                    _val = _txt = "科別";
            }
            else if (element.api === "onSelectHints2") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, hintItem);
                _val = _txt = hintItem;
            }
            else if (element.api === "onSelectHintsUsages") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
                if (element.parameters === "片語用途設定")
                    _val = _txt = "用途";
                else
                    _val = _txt = hintItem;
            }
            else if (element.api === "getMainDoctorList") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, hintItem);
                _val = _txt = hintItem;
            }
            //20191130 雲端
            else if (element.api === "onSelectParentCodeList") {
                if (element.parameters !== undefined) {
                    nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
                    _val = _txt = hintItem;
                }
            }

            optionJson = eval(`[${nowOption}]`);
            optionValue = getOptionKeySettings(optionJson[0], _val);
            optionText = getOptionKeySettings(optionJson[0], _txt);

            if (nowOption) {
                arr1.push(`self.${option} = ko.observableArray([${nowOption}]);`);
            } else {
                arr1.push(`self.${option} = ko.observableArray();`);
            }

            if (ajccType === "METNEW" && cluster === "MGM" && masterSpec === "REC") {
                //多專科團隊會議 - 新增專用
                tmp = $(`<div class="col-md-9 btn-group bootstrap-select show-tick"><select id="${element.id}" ${get_element_css("dropdown", "")} data-bind="options:${option},value:${tmpObj},optionsText:'${optionText}',optionsValue:'${optionValue}',optionsCaption:'請選擇'"${disabledAttr} ${cssAttr}></select></div>`);
                obj.append(tmp);

                if (typeof setJsvValidationForDom === "function" &&
                    element.segmentation.indexOf("★") === 0)
                    setJsvValidationForDom(element, tmp, "", element.segmentation);
            }
            else {
                tmpId = getDropDownId();
                tmp = $(`<div class="col-md-9 btn-group bootstrap-select show-tick"><select id="${element.id}" ${get_element_css("dropdown", "")} data-bind="options:${option},value:${tmpObj},optionsText:'${optionText}',optionsValue:'${optionValue}',optionsCaption:'請選擇'"${disabledAttr} ${cssAttr}></select></div>`);
                obj.append(tmp);
            }


            //20190509
            if (element.dropdownitem === "科別" && !getGroupName().match(/管理者/)) {
                tmp = getBranch(eval(`selectOptionsObj.${element.parameters}`));
                if (tmp !== "") arr1.push(`viewModel.${tmpObj}("${tmp}");`);
            }
        }
        else {
            //EXCEL已經有option 不用call API rende option
            if (element.option) {
                nowOptions = element.option;

                if (element.js && element.js.match(/onchange/))
                    element_js = [element.js.slice(0, 8), `="`, element.js.slice(8), `"`].join('');
            } else {
                // 取得JSON中Ref.Tables對應資料集
                if (element.js.indexOf('|') > -1) {
                    mappingTbName = element.js.split('|')[0].replace(/on|get|set|do|go/, "");
                    if (element.js.split('|')[1].match(/onchange/)) {
                        element_js = element.js.split('|')[1];
                        element_js = [element_js.slice(0, 8), `="`, element_js.slice(8), `"`].join('');
                        element_js = element_js.replace("(this,", `('${mappingTbName}',this,`);
                        element.js = element.js.split('|')[0];
                    }
                }
                else
                    mappingTbName = element.js.replace(/on|get|set|do|go/, "");

                nowOptions = getJsonMappingElement("datatype", mappingTbName, mappingTbName, false);
                console.log(nowOptions);
            }

            tmpId = element.id;
            tmp = $(`<div class="col-md-9 btn-group bootstrap-select show-tick"><select id="${tmpId}" name="${tmpObj}" ${get_element_css("dropdown", "")} data-bind="value:${tmpObj},optionsCaption:'請選擇'"${disabledAttr} ${cssAttr} ${element_js}></select></div>`);
            if (cluster.match(/(TP|CEM|REV|RPT)/) && ajccType.match(/(QRY|REC|CEM)/) && masterSpec.match(/(UNFILLEDLSIT|REC|(ASSIGN|DIVIDE)DOCTOR|REGQRY|REGSEND|INQUIRE|AUDITLIST|DOWNLOAD)/) &&
                tmp.find("select").children().length === 0)
                tmp.find("select").append(`<option value="">請選擇</option>`);
            jQuery.each(nowOptions, function (i, val) {
                var tmpMinorKey = "";
                var tmpKey = Object.keys(val).map(function (keyStr, idk) {
                    if (keyStr.toUpperCase() === "ITEM") return keyStr;
                    else if (keyStr.toUpperCase() === "ADD VALUE") {
                        tmpMinorKey = keyStr; return false;
                    }
                    else return false;
                }).filter(Boolean)[0];

                if (cluster.match(/(CEM|REV|RPT)/) && ajccType.match(/(QRY|CEM)/) && masterSpec.match(/(REC|(ASSIGN|DIVIDE)DOCTOR|REGQRY|REGSEND|INQUIRE|RPTDOCTOR|RPTLONGSHORT|AUDITLIST)/))
                    tmp.find("select").append(`<option value="${val[val[tmpMinorKey] === "" ? tmpKey : tmpMinorKey]}">${val[tmpKey]}</option>`);
                else
                    tmp.find("select").append(`<option value="${val[tmpKey]}${tmpMinorKey === "" ? "" : `(${val[tmpMinorKey]})`}">${val[tmpKey]}</option>`);
            });
            obj.append(tmp);


            //個案審查-選案
            if (element.intcode && element.intcode.match(/BF2S|BF4S/i)) {
                tmp = getToDay(0);
                tmp = getFormatDateStr(0, tmp, element.intcode).substr(4, 2);
                tmp = `Q${parseInt(tmp) / 3}`;

                tmp = nowOptions.find(function (obj) { return obj.Item.indexOf(tmp) > -1; });
                if (tmp !== undefined) arr1.push(`setBindingItemValue('${tmpId}', '${tmp["Add Value"]}');`);
            }
        }

        arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
        arr2.push(`${knockoutPath(parentname)}.data = viewModel.${tmpObj}();`);
        
        if (element.ref && element.js) {
            koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);`;

            if (element.js === "LocalStorageProcess")
                koJsStr += `self.callFun_${tmpObj} = function(value) { ${element.js}('${element.ref.split(",")[0]}', '${element.ref.split(",")[1]},'+value+',${element.ref.split(",")[2]}'); }`;
            else
                koJsStr += `self.callFun_${tmpObj} = function(value) { if (typeof ${element.js} === "function") ${element.js}("${tmpObj}", value, "${element.ref}"); }`;
        }
        else if (element.js && element.js.indexOf("SelectDetail") === -1) {
            if (element.js.match(/^[a-zA-Z]+/)) {
                tmp = element.js.split("(")[0];
                if (element.js.match(/\(/)) select = element.js.replace(/\)/, "");

                koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` +
                          `self.callFun_${tmpObj} = function(value) { if (typeof ${tmp} === "function") ${select !== undefined ? select : tmp + "(\"" + tmpObj + "\""}, value); }`;
            }
            else {
                koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` +
                          `self.callFun_${tmpObj} = function(value) { if (typeof ${element.js} === "function") ${element.js}("${tmpObj}", value); }`;
            }
        }
        else if (element.ref && element.ref === "SelectDetail") {
            koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` +
                      `self.callFun_${tmpObj} = function(value) { if (typeof set${element.ref} === "function") set${element.ref}("${tmpObj}", value, "${element.ref}"); }`;
        }
        arr1.push(`${koJsStr}`);

        if (element.follow) {
            //後面還有控制項
            jQuery.each(element.follow, function (f, fobject) {
                if (fobject.method === "dropdownlist") {
                    obj.find("div").append("&nbsp;" + Json2HTML(fobject, parentname + ".follow[" + f + "]", element.dropdownitem));
                } else {
                    var tmpHtml = Json2HTML(fobject, parentname + ".follow[" + f + "]");

                    if (cluster === "TM" && ajccType.match(/(NEW|QRY)/) && masterSpec === "REC")
                        tmpHtml = `<div class="col-md-6" style="float:initial;display:inline-block;">${$(tmpHtml).find("input[type='text']").hide()[0].outerHTML}</div>`;

                    obj.find("div").append("&nbsp;" + tmpHtml);
                }
            });
        }

        obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
    }
    else {
        //非第一層dropdownlist
        if (element.api) {
            //有API並已先行呼叫(癌別、醫師姓名片語)API)
            if (element.api === "onSelectCodeList") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);

                if (changeKey === "癌別中文") changeKey = "";//MANTIS Case: 0025143 -> Leo 20180620
            }
            else if (element.api === "onSelectHints") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
                // [MANTIS:0025137] eg. "ref":"#科別 胃腸肝膽科"
                if (element.ref && element.ref.indexOf("#") === 0) {
                    changeKey = element.ref.split(" ")[0].replace("#", "");
                    preChangeValue = element.ref.split(" ")[1];
                }
            }
            //20191130 雲端
            else if (element.api === "onSelectCommonList") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);

                if (element.ref && element.ref.indexOf("#") === 0) {
                    changeKey = element.ref.split(" ")[0].replace("#", "");
                    preChangeValue = element.ref.split(" ")[1];
                }
            }

            if (changeKey) {
                // 須連動的上層值
                var tmpNumber = getStringAsNumber(tmpObj) - 1;
                var selectM = "column" + tmpNumber;
                arr1.push(`self.tmp${tmpObj} = ko.observableArray([${nowOption}]);`);
                var selectScriptStr =
                    `self.${option} = ko.computed(function() {
                         var typeA = self.${selectM}();
                         return self.tmp${tmpObj}().filter(el =>{ return el['${changeKey}'] === typeA;})});
                    `;

                // [MANTIS:0025137] eg. "ref":"#科別 胃腸肝膽科"
                if (preChangeValue !== "") {
                    selectScriptStr =
                        `self.${option} = ko.computed(function() {
                         return self.tmp${tmpObj}().filter(el =>{ return el['${changeKey}'] === "${preChangeValue}";})});
                    `;
                }
                arr1.push(selectScriptStr);
            }
            else //不用連動的上層值
                arr1.push(`self.${option}=ko.observableArray([${nowOption}]);`);
            

            arr1.push(`self.${tmpObj}=ko.observable(${knockoutPath(parentname)}.data);`);
            arr2.push(`${knockoutPath(parentname)}.data=viewModel.${tmpObj}();`);
            
            if (element.ref && element.js) {
                koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` + 
                          `self.callFun_${tmpObj}=function(value) { if (typeof ${element.js} === "function") ${element.js}("${tmpObj}", value, "${element.ref}"); }`;
            }
            else if (element.js && element.js.indexOf("SelectDetail") === -1) {
                koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` + 
                          `self.callFun_${tmpObj}=function(value) { if (typeof ${element.js} === "function") ${element.js}("${tmpObj}", value); }`;
            }
            else if (element.ref && element.ref === "SelectDetail") {
                koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);` + 
                          `self.callFun_${tmpObj}=function(value) { if (typeof set${element.ref} === "function") set${element.ref}("${tmpObj}", value, "${element.ref}"); }`;
            }
            arr1.push(`${koJsStr}`);

            select = (element.item ? `<label ${get_element_css("label", parentname.indexOf("implement") > -1 ? "implement" : parentname.indexOf("child") > -1 ? "child" : "")}>${getAddressedColon(element.item)}</label>` : "") + `<select data-bind="options:${option},value:${tmpObj},optionsText:'${element.dropdownitem}',optionsValue:'${element.dropdownitem}',optionsCaption:'請選擇'"${disabledAttr} ${cssAttr}></select>`;
            if (element.dropdownitem === "癌別中文")
                select = (element.item ? `<label ${get_element_css("label", parentname.indexOf("implement") > -1 ? "implement" : parentname.indexOf("child") > -1 ? "child" : "")}>${getAddressedColon(element.item)}</label>` : "") + `<select id="${element.id}" data-bind="options:${option},value:${tmpObj},optionsText:'${element.dropdownitem}',optionsValue:'${element.dropdownitem}',optionsCaption:'請選擇'"${disabledAttr} ${cssAttr}></select>`;

            obj.append(select);

            //20190509
            if (element.dropdownitem === "員編(姓名)") {
                tmp = `${getUserId()}(${getUserName()})`;
                arr1.push(`viewModel.${tmpObj}("${tmp}");`);
            }
        }
        else if (changeKey && changeKey.indexOf("options") > -1) {
            tmpObj = changeKey.replace("options", "");
            select = (element.item ? `<label>${getAddressedColon(element.item)}</label>` : "") + `<select data-bind="options:${changeKey},value:${tmpObj},optionsText:'${element.dropdownitem}',optionsValue: '${element.dropdownitem}',optionsCaption:'請選擇'"${disabledAttr} ${cssAttr}></select>`;
            obj.append(select);

            arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
            arr2.push(`${knockoutPath(parentname)}.data = viewModel.${tmpObj}();`);
        }
        else {
            //EXCEL已經有option 不用call API rende option *暫時只有call API 取得option 的有做連動*
            if (element.js) {
                // 取得JSON中Ref.Tables對應資料集
                mappingTbName = element.js.replace(/on|get|set|do|go/, "");
                console.log(mappingTbName);
                nowOptions = getJsonMappingElement("datatype", mappingTbName, mappingTbName, false);
                console.log(nowOptions);
            }
            else if (element.dropdownitem && (element.dropdownitem === "Hour" || element.dropdownitem === "Minute")) {
                if (element.dropdownitem === "Hour") {
                    for (var i = 0; i <= 23; i++)
                        nowOptions.push({ item: i });
                }
                else if (element.dropdownitem === "Minute") {
                    nowOptions.push({ item: "00" });
                    nowOptions.push({ item: "30" });
                }
            }
            else if (element.option) {
                nowOptions = element.option;
            }

            if (element.item && element.item.indexOf("稱謂") > -1)
                tmp = $(`<div ${get_element_css()}><div class="btn-group bootstrap-select show-tick"><select onchange="fTitleOther(this, '${tmpObj}')" data-bind="value:${tmpObj},optionsCaption:'請選擇'"${disabledAttr} ${cssAttr}></select></div></div>`);
            else
                tmp = $(`<div ${get_element_css()}><div class="btn-group bootstrap-select show-tick"><select data-bind="value:${tmpObj},optionsCaption:'請選擇'"${disabledAttr} ${cssAttr}></select></div></div>`);

            jQuery.each(nowOptions, function (i, val) {
                var tmpKey = Object.keys(val).map(function (keyStr, idk) {
                    if (keyStr.toUpperCase() === "ITEM") return keyStr;
                    else return false;
                }).filter(Boolean)[0];

                if (i === 0) tmp.find("select").append(`<option value="">請選擇</option>`);

                var tmpOptionVal = val[tmpKey] || val["Description"];
                if (tmpOptionVal !== undefined) {
                    if (val["add value"])
                        tmp.find("select").append(`<option value="${tmpOptionVal}(${val["add value"]})">${tmpOptionVal}</option>`);
                    else
                        tmp.find("select").append(`<option value="${tmpOptionVal}">${tmpOptionVal}</option>`);
                }

                if (i === nowOptions.length - 1 && val.follow) {
                    var jtmp;
                    jQuery.each(val.follow, function (j, jval) {
                        jtmp = Json2HTML(jval, `${parentname}.${tmpKey ? `option[${i}].` : ``}follow[${j}]`);
                        tmp.find("select").parent().append(`&nbsp;${$(jtmp).html()}`).find("input.form-control").removeClass("form-control");
                    });
                }
            });

            if (element.item) {
                if (element.dropdownitem !== "Minute" || element.item !== "時") 
                    obj.append(`<label ${get_element_css("dropdown", "label")}>${getAddressedColon(element.item)}</label>`);
            }

            if (tmp.html().indexOf("&nbsp;") > -1)
                obj.append(tmp.find("select").parent().html());
            else
                obj.append(tmp);
            obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);

            //處理[個案-基本資料-稱謂]
            if (obj.find(`div > input[id*="${element.id}"]`).length > 0) {
                obj.find(`div > input[id*="${element.id}"]`).parent().addClass("input-group");
            }

            arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
            arr2.push(`${knockoutPath(parentname)}.data = viewModel.${tmpObj}();`);

            if (element.js && element.js.indexOf("on") === 0) {
                koJsStr = `self.${tmpObj}.subscribe(function(newValue){ self.callFun_${tmpObj}(newValue); }, this);
                       self.callFun_${tmpObj} = function(value) { if (typeof ${element.js} === "function") ${element.js}("${tmpObj}", value); }`;
            }
            arr1.push(`${koJsStr}`);
        }

        if (element.follow) {
            //後面還有控制項
            jQuery.each(element.follow, function (f, fobject) {
                if (fobject.method === "dropdownlist") {
                    obj.append(Json2HTML(fobject, parentname + ".follow[" + f + "]", element.dropdownitem));
                } else {
                    obj.append(Json2HTML(fobject, parentname + ".follow[" + f + "]"));
                }
            });
        }
    }

    if (obj.find("select").parent("[clss*='col-md-9']").length > 0)
        obj.contents().find("select").addClass(classAttr);

    return obj;//合併須刪除
}

function getDropDownId() {
    dropdownIdx++;
    return "dropdown" + dropdownIdx;
}
