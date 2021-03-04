var SubscribeBindingName = "";
var InitVar_Radio_Funcs = [];

function render_bindingscript(obj, itemParentName) {
    if (itemParentName === undefined)
        itemParentName = getItemParentName(obj);

    if (itemParentName && eval(`${itemParentName}.value`) !== undefined) {
        //產生binding script
        renderBindingscript(obj, itemParentName);
    }
}

function getItemParentName(obj) {
    var data_id = obj.data_id;
    var itemParentName = "";
    var n, prevId;

    if (data_id) {
        //Binding對象為前數第n個json物件
        for (n = rowIdx; n > 0; n--) {
            itemParentName = `json.data[${dataIdx}].docdetail[${n}]`;

            prevId = eval(`${itemParentName}.id`);
            if (prevId && prevId === data_id &&
                itemParentName && eval(`${itemParentName}.value`) !== undefined) {
                break;
            }
        }
    } else {
        if (obj.value !== undefined) {
            //Binding對象為自己
            itemParentName = `json.data[${dataIdx}].docdetail[${rowIdx}]`;
        }
        else {
            //Binding對象為前1個json物件
            itemParentName = `json.data[${dataIdx}].docdetail[${rowIdx - 1}]`;
        }
    }

    return itemParentName;
}

//產生binding script
function renderBindingscript(obj, parentname) {
    var id = obj.id;
    var uitype = obj.uitype;
    var displayName = obj.displayname;
    var currKoName = getKnockoutPath(parentname);

    if (id) {
        var tmpBindingName = `${id}`, dispArr, dispVal, tmpArr, tmpJs;

        //設定onload頁面時，即需執行的function
        if (obj._js && obj._js.match(/^[A-Z].+\(.+(,'.+')*\)/g)) {
            var tmpfuncStr = "";
            if (obj._js.indexOf("FormShow") > -1) { //eg. FormShow(this, 'AJCC6')
                if (obj._js.match(/FormShow\(.+,'.+'\)/g)) {
                    tmpfuncStr = obj._js.match(/FormShow\(.+,'.+'\)/g)[0].replace("this", `'${obj.id}'`);
                    arr1.push(`${tmpfuncStr};`);
                } else
                    arr1.push(`FormShow('${obj.id}');`);
            }
            /*
            else {
                var tmpfuncArr = obj._js.indexOf(";") > -1 ? obj._js.split(";") : [obj._js];
                for (var i = 0; i < tmpfuncArr.length; i++) {
                    tmpfuncStr = tmpfuncArr[i];
                    if (tmpfuncStr.match(/(on[a-z]+[A-Z][a-z]*([A-Z]*[a-z]*)*)|ShowHistologicGrade|ShowInvasion/g)) continue;
                    arr1.push(tmpfuncStr);
                }
            }
            */
            //將SetTNM排除(因為下方會處理)
            else if (!obj._js.match(/SetTNM\(.+\)/g)) {
                var tmpfuncArr = obj._js.indexOf(";") > -1 ? obj._js.split(";") : [obj._js];
                for (var i = 0; i < tmpfuncArr.length; i++) {
                    tmpfuncStr = tmpfuncArr[i];
                    if (tmpfuncStr.match(/(on[a-z]+[A-Z][a-z]*([A-Z]*[a-z]*)*)|ShowHistologicGrade|ShowInvasion/g)) continue;
                    arr1.push(tmpfuncStr);
                }
            }
        }

        if ("text|date|field|textarray|file".indexOf(uitype) > -1 ||
            uitype === "label" && displayName && displayName.indexOf("SOURCE.") > -1) {

            if (uitype === "label" && displayName && displayName.indexOf("SOURCE.") > -1) {
                //個管
                dispArr = displayName.split(".");
                dispVal = SetSourceData(dispArr[1], dispArr[0]);
                if (dispVal !== undefined && dispVal !== "") {
                    arr1.push(`self.${tmpBindingName} = ko.observable('${dispVal}');`);
                } else {
                    if (obj._js) {
                        if (obj._js.match(/SetTNM\(.+\)/g)) {
                            tmpArr = obj.format.split(".");
                            tmpJs = obj._js.replace("#TNM", tmpArr[tmpArr.length - 1]);
                            if (tmpJs.split(',').length === 4)
                                dispVal = eval(tmpJs.replace(")", `, undefined, '${sAJCC}')`));
                            else
                                dispVal = eval(tmpJs.replace(")", `,'${sAJCC}')`));
                        }
                        else if (obj._js.match(/Get2LevelData\(.+\)/g)) {
                            dispVal = eval(obj._js);
                        }
                        else if (obj._js.match(/SetfinalStage\(.*\)/g)) {
                            dispVal = eval(obj._js.replace(")", `'${sAJCC}')`));
                        }
                    }
                    if (dispVal !== undefined && dispVal !== "")
                        arr1.push(`self.${tmpBindingName} = ko.observable('${dispVal}');`);
                    else
                        arr1.push(`self.${tmpBindingName} = ko.observable(${currKoName}.value);`);
                }
            }
            else {
                if (uitype === "file") {
                    arr1.push(`self.${tmpBindingName} = ko.observable();`); //避免binding init value出錯
                }
                else if (obj._parameters && obj._parameters.indexOf("SOURCE.") > -1) {
                    dispArr = obj._parameters.split(".");
                    dispVal = SetSourceData(dispArr[1], dispArr[0]);
                    arr1.push(`self.${tmpBindingName} = ko.observable('${dispVal}');`);
                }
                else {
                    dispVal = undefined;
                    if (obj._parameters && obj._parameters.indexOf("localStorage.") > -1) {
                        dispArr = obj._parameters.split(".");
                        SetMainProcessPara("getLocalStorage", { "key": dispArr[1] });
                        dispVal = MainProcess();
                    }
                    else if (obj.remark && obj.remark.match(/系統日期/)) {
                        dispVal = getToDay(0);
                        if (obj.intcode && obj.intcode.match(/BF2S|BF4S/i))
                            dispVal = getFormatDateStr(0, dispVal, obj.intcode).substr(0, 4);
                        else if (obj.format && obj.format === "#7D")
                            dispVal = parseInt(dispVal.substring(0, 4)) - 1911 + dispVal.substring(4, 8);

                        //調整判斷若有填值則使用該值，若無使用系統日期
                        dispVal = `${currKoName}.value !== '' ? ${currKoName}.value : '${dispVal}'`;
                    }
					else if (obj.remark && obj.remark.match(/系統年度/)) {
						dispVal = getToDay(0);
						dispVal = parseInt(dispVal.substring(0, 4));
						dispVal = `${currKoName}.value !== '' ? ${currKoName}.value : '${dispVal}'`;
					}

                    if (dispVal !== undefined && dispVal !== null)
                        arr1.push(`self.${tmpBindingName} = ko.observable(${dispVal});`);
                    else
                        arr1.push(`self.${tmpBindingName} = ko.observable(${currKoName}.value);`);
                }
            }

            //arr2.push(`${parentname}.value = viewModel.${tmpBindingName}();`);
            arr2.push(`${currKoName}.value = viewModel.${tmpBindingName}();`);
            if (uitype === "date") arr1.push(`setDatePickerFormat('${id}', '${obj.format}', 'v2');`); //設定日曆元件
        }
        else if (uitype === "checkbox") {
            arr1.push(`self.${tmpBindingName} = ko.observable(${currKoName}.value);`);
            if (obj._js && obj._js.indexOf("OpenDetail") > -1) {
                //for展開BTN下有"checkbox勾選展開"使用
                //arr1.push(`var tmpAddBtnObj_${obj.id}=$('#${obj.id}')[0]; if (tmpAddBtnObj_${obj.id}) FormShow(tmpAddBtnObj_${obj.id});`);
            }
            arr2.push(`${currKoName}.value = viewModel.${tmpBindingName}();`);
        }
        else if (uitype === "radio") {
            if (obj.data_id !== undefined) tmpBindingName = obj.data_id;
            dispVal = "", isSetInitAjccPara = false;
            if (obj._js) {
                if (obj._js.match(/SetTNM\(.+\)/g)) {
                    tmpArr = obj.format.split(".");
                    tmpJs = obj._js.replace("#TNM", tmpArr[tmpArr.length - 1]);
                    if (tmpJs.split(',').length === 4)
                        dispVal = eval(tmpJs.replace(")", `, undefined, '${sAJCC}')`));
                    else
                        dispVal = eval(tmpJs.replace(")", `,'${sAJCC}')`));
                }
                else if (obj._js.match(/SetAJCCStagePara\(this\,.+\)/g)) {
                    isSetInitAjccPara = true;
                }
            }

            var currRdoJs = "";
            if (dispVal !== undefined && dispVal !== "")
                currRdoJs = `self.${tmpBindingName} = ko.observable('${dispVal}');`;
            else
                currRdoJs = `self.${tmpBindingName} = ko.observable(${currKoName}.value);`;

            if (arr1.indexOf(currRdoJs) === -1) arr1.push(currRdoJs);

            currRdoJs = `${currKoName}.value = viewModel.${tmpBindingName}();`;
            if (arr2.indexOf(currRdoJs) === -1) arr2.push(currRdoJs);

            if (isSetInitAjccPara) {
                tmpJs;
                tmpArr = obj._js.indexOf(";") > 0 ? obj._js.split(";") : [obj._js];
                tmpArr.forEach(function (jsVal, i) {
                    if (obj._js.match(/SetAJCCStagePara\(this\,.+\)/g)) {
                        tmpJs = obj._js.match(/SetAJCCStagePara\(this\,.+\)/g)[0];
                        tmpJs = ToPrefixTransCase(tmpJs, "L");
                        if (dispVal !== undefined && dispVal !== "") {
                            tmpJs = tmpJs.replace("this", `'${tmpBindingName}','${dispVal}','checked'`);
                        } else {
                            tmpJs = tmpJs.replace("this", `'${tmpBindingName}',${currKoName}.value,'checked'`);
                        }
                    }
                });
                if (tmpJs) {
                    if (InitVar_Radio_Funcs.indexOf(tmpJs) === -1) {
                        InitVar_Radio_Funcs.push(tmpJs);
                        arr1.push(tmpJs);
                    }
                }
            }
        }
        else if (uitype === "dropdownlist") {
            render_BindingDropdownList(obj, parentname, tmpBindingName);
        }
        else if (uitype === "dropdownlist_with_checkbox") {
            render_BindingDropdownList(obj, parentname, tmpBindingName, uitype);
        }
        else if (uitype === "button") {
            if (obj.format && obj.format === "#展開BTN") {
                //for多層展開BTN使用
                arr1.push(`var tmpAddBtnObj_${obj.id}=$('#${obj.id}')[0]; if (tmpAddBtnObj_${obj.id}) FormShow(tmpAddBtnObj_${obj.id});`);
            }
        }
        else if (uitype === "label" && displayName && displayName.indexOf("診療計畫書未填寫.") > -1) {
            //診療計畫書
            dispArr = displayName.split(".");
            dispVal = SetSourceData(dispArr[1], dispArr[0]);
            if (dispVal !== undefined && dispVal !== "") {
                arr1.push(`self.${tmpBindingName} = ko.observable('${dispVal}');`);
            }
        }
        else if (uitype === "label" && displayName && displayName.indexOf("PSYCHO.") > -1) {
            //腫瘤心理個案
            dispArr = displayName.split(".");
            dispVal = SetSourceData(dispArr[1], dispArr[0]);
            arr1.push(`self.${tmpBindingName} = ko.observable('${dispVal}');`);
        }

        //設定連動
        var chgJs = obj._js;
        if (chgJs && chgJs.match(/^change[A-Z].+\(/g)) {
            //[MANTIS:20098] 有"change"開頭的js才是連動function
            chgJs = chgJs.replace(/change/g, "").replace(/\)/g, ",value)");
        } else chgJs = undefined;
        //render_GearingScript(id, uitype, tmpBindingName, obj.col, obj.row, false, obj.format, chgJs, obj._ref);
        render_GearingScript(obj, tmpBindingName, false, chgJs);

        //設定大小標
        if (obj._js && obj._js.indexOf("procparam") > -1) {
            var _cusCSS = obj._js.substr(obj._js.indexOf("procparam"));
            _cusCSS = _cusCSS.substring(0, _cusCSS.indexOf("')") + 2);
            arr1.push(`${_cusCSS.replace("procparam", "").replace(/this/g, `'${obj.id}'`)}`);
        }
    }
}

//產生[DropdownList] 的 binding script
function render_BindingDropdownList(obj, parentname, tmpBindingName, uitype) {
    var dynamic_key; //下拉選單是否需連動
    var preChangeValue = ""; //for 預設帶入連動的value [MANTIS:0025137]
    var option = "options" + tmpBindingName;
    var selectedOption = "selected" + tmpBindingName;
    var nowOption = [], tmpSelectedVal;

    if (obj._api) {
        //有API並已先行呼叫(癌別、醫師姓名片語)API
        if (obj._api === "onSelectHints" && obj._ref && obj._ref.indexOf("#") === 0) {
            var refOpt;
            if (obj._ref.indexOf(",") > -1) {
                refOpt = obj._ref.split(",").filter(element => element.indexOf("#") === 0)[0];
            } else {
                refOpt = obj._ref;
            }
            dynamic_key = refOpt.indexOf(" ") > -1 ? refOpt.split(" ")[0].replace("#", "") : refOpt.replace("#", "");
            preChangeValue = refOpt.indexOf(" ") > -1 ? refOpt.split(" ")[1] : "";
        }
    }

    nowOption = NewOptions("NewOptions", obj);

    if (dynamic_key) {
        //須連動的上層值
        var dynamicNumber = rowIdx - 1;
        var dynamicBindingName = eval(`json.data[${dataIdx}].docdetail[${dynamicNumber}].id`);
        arr1.push(`self.tmp${tmpBindingName} = ko.observableArray([${nowOption}]);`);
        var selectScriptStr =
            `self.${option} = ko.computed(function() {
                var dynamicVal = self.${dynamicBindingName}();
                return self.tmp${tmpBindingName}().filter(el =>{ return el['${dynamic_key}'] === dynamicVal;})});
            `;
        //[MANTIS:0025137] eg. "ref":"#科別 胃腸肝膽科"
        if (preChangeValue !== "") {
            selectScriptStr =
                `self.${option} = ko.computed(function() {
                    return self.tmp${tmpBindingName}().filter(el =>{ return el['${dynamic_key}'] === "${preChangeValue}";})});
                `;
        }
        arr1.push(selectScriptStr);
    } else {
        if (nowOption) {
            arr1.push(`self.${option} = ko.observableArray([${nowOption}]);`);
        } else {
            arr1.push(`self.${option} = ko.observableArray();`);
        }
    }

    //設定下拉初始值
    var _defaultVal;
    if (obj._js && obj._js.indexOf("default") > -1) {
        var _defaultJs = obj._js.substr(obj._js.indexOf("default"));
        _defaultJs = _defaultJs.substring(0, _defaultJs.indexOf("')") + 2);
        _defaultVal = eval(_defaultJs.replace("default", ""));
    }

    if (_defaultVal) {
        arr1.push(`self.${tmpBindingName}=ko.observable('${_defaultVal}');`);
    }
    else {
        if (uitype !== undefined && uitype.indexOf("checkbox") > -1) {
            arr1.push(`var tmp${selectedOption}Val = ${getKnockoutPath(parentname)}.value;
                   if (tmp${selectedOption}Val && tmp${selectedOption}Val !== "") {
                        self.${selectedOption}=ko.observableArray(tmp${selectedOption}Val);
                   } else
                        self.${selectedOption}=ko.observableArray();`);
        }
        else
            arr1.push(`self.${tmpBindingName}=ko.observable(${getKnockoutPath(parentname)}.value);`);
    }

    //選項有"其他"時，判斷輸入後面之輸入框欄位
    var hasOptionOther = false;
    if (Array.isArray(nowOption)) {
        for (var i = 0; i < nowOption.length; i++) {
            //console.log(JSON.stringify(nowOption[i]));
            if (JSON.stringify(nowOption[i]).indexOf("其他") > -1) {
                hasOptionOther = true; break;
            }
        }
    } else if (nowOption.indexOf("其他") > -1) {
        hasOptionOther = true;
    }
    if (hasOptionOther) {
        //render_GearingScript(obj.id, obj.uitype, tmpBindingName, obj.col, obj.row, hasOptionOther);
        render_GearingScript(obj, tmpBindingName, hasOptionOther);

        var initOptionObj = eval(parentname);
        if (initOptionObj && initOptionObj.value && initOptionObj.value === "其他") {
            flag_SelectOtherOption = 2;
        } else {
            flag_SelectOtherOption = 1;
        }
    }

    if (uitype !== undefined && uitype.indexOf("checkbox") > -1) {
        arr2.push(`${getKnockoutPath(parentname)}.value = viewModel.${selectedOption}();`);
    } else
        arr2.push(`${getKnockoutPath(parentname)}.value = viewModel.${tmpBindingName}();`);
}

//設定連動 & 驗證 相關Script
//function render_GearingScript(id, uitype, tmpBindingName, col, row, hasOptionOther, format, js, ref) {
function render_GearingScript(obj, tmpBindingName, hasOptionOther, js) {
    var id = obj.id, uitype = obj.uitype, format = obj.format, temp;
    if (id === "radio_04_236_s2h0239_btn0_29") {
        temp = id;
    }
    if (SubscribeBindingName !== tmpBindingName) { //避免重複
        var _koJS = `if (typeof GearingBehavior === "function") GearingBehavior('${id}', '${tmpBindingName}', ${obj.col}, ${obj.row}, value, '${uitype}');`;

        //腫瘤心理>服務紀錄>_ref有setBSRSNK時，_koJS為空
        if (cluster === "TM" && dataType === "服務紀錄" && obj._ref === "setBSRSNK") _koJS = "";

        //下拉連動 有"其他"選項
        if (hasOptionOther === true) _koJS += ` setOptionOther('${tmpBindingName}', value);`;

        //格式驗證
        if (ValidFormat_Remark === true) _koJS += ` ValidateFormatItem('${id}', value);`;

        //起、迄日檢核
        if (ValidSEDate_Remark > 0 && (uitype === "date" || uitype === "text") &&
            format && format.match(/#\d+D/g)) {
            _koJS += ` ChkValidSEDate('${id}');`;
            ValidSEDate_Remark = 0;
        }

        if (js !== undefined && js !== "") {
            //值有改變時，須連動的function
            var tmpKoJsArr = js.indexOf(";") > -1 ? js.split(";") : [js];
            tmpKoJsArr = tmpKoJsArr.filter(para => !para.match(/^on[A-Z].+\(/g)); //排除非連動function
            tmpKoJsArr.forEach(function (kjVal, i) {
                _koJS += ` ${kjVal};`;
            });
        }

        if ("text|date|textarray|checkbox|radio|dropdownlist".indexOf(uitype) > -1) {
            arr1.push(`self.${tmpBindingName}.subscribe(function (newValue) { self.callFun_${tmpBindingName}(newValue); }, this);`);
            arr1.push(`self.callFun_${tmpBindingName} = function (value) { ${_koJS} };`);
            SubscribeBindingName = tmpBindingName;
        }
    }
}