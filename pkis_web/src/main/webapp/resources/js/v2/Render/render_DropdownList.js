var flag_SelectOtherOption = 0; //判斷是否下拉有選擇"其他": 0:預設 | 1:非「其他」選項 | 2:其他

function NewDropdownList(obj, itemType) {
    var ddl = undefined;

    if (itemType === "dropdownlist") {
        //動態產出[醫師姓名片語]、[癌別]等陣列
        getCodeListOrHints(obj);

        ddl = document.createElement("select");
        var opts = obj.options;
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        //if (lCol > 0) ddl.className = GetClassName(lCol, itemType);
        if (lCol > 0) ddl.className = GenCss(lCol, itemType);
        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        ddl.setAttribute('requirenewline', 0);
        //ddl.setAttribute('shouldIndent', false);
        $.each(opts, function (key, value) {
            if (value['id'] === undefined) {
                return true;
            }
            else {
                var opt = document.createElement("option");
                opt.setAttribute("id", value['id']);
                opt.setAttribute("value", value['value']);
                opt.setAttribute("displayname", value['displayname']);
                opt.text = value['displayname'];
                ddl.appendChild(opt);
            }
        });
        ddl.setAttribute('col', lCol);
        ddl.setAttribute('row', lRow);
        if (obj._js && obj._js !== "") {
            ddl = SetJsAttr(ddl, obj._js);
        }
        ddl.setAttribute("id", obj.id);

        if (obj.id && lFmt) {
            var optionText = lFmt.replace(/#|SELECT/g, "");
            var optionValue = lFmt.replace(/#|SELECT/g, "");

            if (obj._api) {
                ddl.setAttribute("data-bind", `options:options${obj.id},value:${obj.id},optionsText:'${optionText}',optionsValue:'${optionValue}',optionsCaption:'請選擇'`);
            } else {
                //個管報表
                if (module === "CM" && cluster === "RPT") {
                    //匯出審查病歷名單
                    if (ajccType === "CEM" && masterSpec === "AUDITLIST") {
                        optionText = "item";
                        optionValue = "value";
                        ddl.setAttribute("data-bind", `options:options${obj.id},value:${obj.id},optionsText:'${optionText}',optionsValue:'${optionValue}',optionsCaption:'請選擇'`);
                    }
                    //多專科團隊年度統計表
                    else if (ajccType === "MST" && obj._js.match(/ChangeTeamToggle/)) {
                        optionText = "item";
                        optionValue = "item";
                        ddl.setAttribute("data-bind", `options:options${obj.id},value:${obj.id},optionsText:'${optionText}',optionsValue:'${optionValue}'`);
                    }
                }
                else if (module === "CS" || module === "CM" && cluster === "COL") {
                    optionText = "item";
                    optionValue = "item";
                    ddl.setAttribute("data-bind", `options:options${obj.id},value:${obj.id},optionsText:'${optionText}',optionsValue:'${optionValue}',optionsCaption:'請選擇'`);

                }

                if (optionText === "" && optionValue === "") {
                    optionText = "item";
                    optionValue = "item";
                    ddl.setAttribute("data-bind", `options:options${obj.id},value:${obj.id},optionsText:'${optionText}',optionsValue:'${optionValue}',optionsCaption:'請選擇'`);
                }
            }
        }

        $(ddl).css("width", "30%"); //150px
    }
    return ddl;
}

function NewOptions(processCode, obj) {
    var nowOption = undefined, findOptCnt, findOption;
    var _val, _txt, hintItem = "片語";

    if (processCode === "NewOptions") {
        if (obj._api) {
            //有API並已先行呼叫(癌別、醫師姓名片語)API
            if (obj._api === "onSelectCodeList") {
                if (obj._parameters === "癌別") _val = _txt = "中文";
                else _val = _txt = hintItem;
                hintItem = obj.displayname;
            }
            //20191127 雲端
            else if (obj._api === "onSelectCommonList") {
                _val = _txt = hintItem;
                hintItem = obj.displayname;
            }
            else if (obj._api === "onSelectHints") {
                if (obj._parameters.match(/醫師姓名片語|CQS相關人員/)  ) _val = _txt = "科別";
                hintItem = obj.displayname;
            }
            else if (obj._api === "onSelectHints2") {
                _val = _txt = hintItem;
            }
            else if (obj._api === "onSelectHintsUsages") {
                if (obj._parameters === "片語用途設定") _val = _txt = "用途";
                else _val = _txt = hintItem;
                hintItem = obj.displayname;
            }
            else if (obj._api === "getMainDoctorList") {
                _val = _txt = hintItem;
            }
            nowOption = getOptionStr(eval(`selectOptionsObj.${obj._parameters}`), obj._api, obj._parameters, hintItem);
        }
        else {
            nowOption = [];
            //EXCEL已經有option 不用call API rende option
            if (obj.format === "#SELECT" && obj.note && obj.note === "#SELECT-start") {
                findOptCnt = rowIdx + 1;
                findOption = json.data[dataIdx].docdetail[findOptCnt];
                while (findOption.note && findOption.note === "#SELECT-option") {
                    nowOption.push(`{ "item": "${findOption.displayname}" }`);
                    findOptCnt++;
                    findOption = json.data[dataIdx].docdetail[findOptCnt];
                }
            }
            else if (obj.format === "#□SELECT" && obj.note && obj.note === "#□SELECT-start") {
                findOptCnt = rowIdx + 1;
                findOption = json.data[dataIdx].docdetail[findOptCnt];
                while (findOption.note && findOption.note === "#□SELECT-option") {
                    nowOption.push(`{ "item": "${findOption.displayname}" }`);
                    findOptCnt++;
                    findOption = json.data[dataIdx].docdetail[findOptCnt];
                }
            }
            else {
                // 取得JSON中Ref.Tables對應資料集
                if (obj._js) {
                    var mappingTbName = obj._js.replace(/^(on(change|click)|On)/gi, "");
                    if (mappingTbName.indexOf('(') > -1) mappingTbName = mappingTbName.substr(0, mappingTbName.indexOf('('));
                    nowOption = getJsonMappingElement("datatype", obj.value, mappingTbName, false);
                    hintItem = "item";

                    //個案報表-審查名單使用
                    if (module === "CM" && cluster === "RPT" &&
                        ajccType === "CEM" && masterSpec === "AUDITLIST") {
                        nowOption = $.map(nowOption, function (item, idx) {
                            return `{"item":"${item.Item}","value":"${item["Add Value"]}"}`;
                        });

                        if (obj.intcode && obj.intcode.match(/BF2S|BF4S/i)) {
                            _val = getToDay(0);
                            _val = getFormatDateStr(0, _val, obj.intcode).substr(4, 2);
                            _val = `Q${parseInt(_val) / 3}`;

                            _val = nowOption.find(function (obj) { return obj.indexOf(_val) > -1; });
                            if (_val !== undefined) arr1.push(`setBindingItemValue('${obj.id}', '${JSON.parse(_val).value}');`);
                        }
                    }
                    //癌篩使用
                    else if (module === "CS" || module === "CM" && cluster === "COL") {
                        nowOption = $.map(nowOption, function (item, idx) {
                            return `{"item":"${item.Value}-${item.Item}"}`;
                        });

                        arr1.push(obj._js.replace(/on(change|click)/gi, "").replace(/this/, `'#${obj.id}'`));
                    }
                }
                else if (obj.format && (obj.format.replace(/#|SELECT/g, "") === "Hour" || obj.format.replace(/#|SELECT/g, "") === "Minute")) {
                    if (obj.format.replace(/#|SELECT/g, "") === "Hour") {
                        for (var i = 0; i <= 23; i++)
                            nowOption.push(`{ "item": "${i}" }`);
                        hintItem = "item";
                    }
                    else if (obj.format.replace(/#|SELECT/g, "") === "Minute") {
                        nowOption.push(`{ "item": "00" }`);
                        nowOption.push(`{ "item": "30" }`);
                        hintItem = "item";
                    }
                }
            }
        }

        //排序
        if (nowOption && Array.isArray(nowOption) && hintItem !== "item" && obj._parameters !== "醫師姓名片語" && masterSpec !== "REG") {
            nowOption.sort(function (a, b) {
                var comparedA = typeof a === "string" ? JSON.parse(a)[hintItem] : a[hintItem] ? a[hintItem] : a;
                var comparedB = typeof b === "string" ? JSON.parse(b)[hintItem] : b[hintItem] ? b[hintItem] : b;
                if (comparedA && comparedB) {
                    if (comparedA.match(/[\d]+/g) !== null && comparedB.match(/[\d]+/g) !== null) {
                        comparedA = comparedA.match(/\([\d]+\)/g) !== null ? comparedA.match(/\([\d]+\)/g)[0].match(/[\d]+/g)[0] : comparedA.match(/[\d]+/g) !== null ? comparedA.match(/[\d]+/g)[0] : "0";
                        comparedB = comparedB.match(/\([\d]+\)/g) !== null ? comparedB.match(/\([\d]+\)/g)[0].match(/[\d]+/g)[0] : comparedB.match(/[\d]+/g) !== null ? comparedB.match(/[\d]+/g)[0] : "0";
                        return parseInt(comparedA) - parseInt(comparedB);
                    }
                    else {
                        if (hintItem.indexOf("癌症既往史代碼") === -1) return comparedA.localeCompare(comparedB);
                        else return 0;
                    }
                }
                else return 0;
            });
        }
    }

    return nowOption;
}

//下拉連動下一階
function ResetNextSelectOptions(selfDom, callback, apiStr, apiType, itemStr) {
    if (selfDom !== undefined && selfDom.value !== undefined) {
        var selectedKey = "";
        if (apiType === "醫師姓名片語") selectedKey = "科別";

        var changedJsonOption = eval(`selectOptionsObj.${apiType}`);
        if (selfDom.value !== "") changedJsonOption = eval(`selectOptionsObj.${apiType}`).filter(para => para[selectedKey] === selfDom.value);

        var nowOption = callback(changedJsonOption, apiStr, apiType, itemStr);
        var $target = $(selfDom).next("select");
        if ($target.length > 0 && nowOption !== undefined) {
            $target.find("option").remove().end().append(`<option value="">請選擇</option>`);
            var optionData = JSON.parse(`[${nowOption}]`);
            $.each(optionData, function (k, val) {
                $target.append(`<option value="${val[itemStr] === "空白" ? "" : val[itemStr]}">${val[itemStr]}</option>`);
            });
        }
    }
}