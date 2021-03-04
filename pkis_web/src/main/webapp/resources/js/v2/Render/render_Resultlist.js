var Resultlist_ColName_AutoLineBreak_Flag = "收案類別|個管提醒事項|簡訊提醒事項|簡訊提醒對象|簡訊發送日期|電訪結果|代掛診說明|看診紀錄說明|備註";
var Resultlist_ColType_Link_Flag = "TODO|QRY|REC|MST";
var TODO_EnableOptionFlag = "轉本院他科或其他醫師";

function NewResultList(obj, itemType) {
    var resTable = undefined;

    if (itemType === "resultlist") {
        //取得欲顯示的ResultList欄位名稱
        resultListItems = getValidResultListItems(obj.ResultListItems);
        resTable = document.createElement("table");
        resTable.setAttribute("id", "json_query");
        resTable.setAttribute("class", "table table-bordered table-striped");
        resTable.className = GetClassName("", itemType);
        // 如果有js，表示ResultList需自動查詢、並帶入資料
        if (obj._js && obj._js.indexOf("setToBackResult") > -1 && obj.parameters) {
            if (element.parameters.indexOf("選案") > -1)
                selectOptionsObj.baseResultListFuncStr = `setToBackResult("選案", "${obj.parameters}", "${obj._ref || obj.ref}")`;
        }
        else if (ajccType === "QRY" && FIX_QUERY_KEY_setToBackResult.indexOf(`|${dataType}|`) > -1){
            selectOptionsObj.baseResultListFuncStr = `setToBackResult("", "${odataType}", "${obj._ref || obj.ref}")`;
        }
        
    }

    return resTable;
}

/**
 * 初始化ResultList的table元件
 * @param {any} jqTb               目標jquery datatable元件
 * @param {any} resultListItems    需render的column名稱array
 * @param {any} dtOptions          目標jquery datatable的相關參數
 * @param {any} isReadOnly         將連結改為唯讀顯示
 */
function NewResultListColumnDefs(jqTb, resultListItems, dtOptions, isReadOnly) {
    var editor = 0, nowColumns = [];
    var $nowTable = jqTb, nowTbOptions = dtOptions;
    var orderNum = 0, comparedNum = 0, orderType = "asc";

    //動態產生datatable的columns
    $.each(resultListItems, function (i, res) {
        var nowColNameArr, nowProp, rowStr, tbOptMsg, tmpVal;

        if (res.indexOf("BTN") > -1) {
            nowColNameArr = res.toString().split("#").filter(x => x);
            nowProp = getRecomposedColumn(nowColNameArr, "BTN").trim();

            nowColumns.push({
                "title": nowProp,
                "mDataProp": nowProp,
                "render": function (data, type, row) {
                    tbOptMsg = ""; rowStr = JSON.stringify(row).replace(/"/gi, "|").replace(/'/gi, "\\'");
                    nowColNameArr.forEach(function (col) {
                        tmpVal = col.replace("BTN", "").trim();

                        if (nowProp.indexOf("刪除") > -1) {
							if(contractor == currentUserCname||contractorAgentUserId==currentUserid)
								rowStr = `var row = '${rowStr}'; setResultList(row, this);`;
							else
								return false;

                        }
						else if(nowProp.indexOf("下載") > -1){
//							if(row.下載 === "是")
								rowStr = `var row = '${rowStr}'; setResultList(row, this);`;
//							else
//								return false;
						}
                        else
                            rowStr = `var row = '${rowStr}'; setResultList(row, this);`;

                        if (tbOptMsg !== undefined){
                        	
                            tbOptMsg = `<input type="button" class="btn btn-primary la_btn_pink" name="btnSetResultList" onclick="${rowStr}" value="${tmpVal}"${tbOptMsg} />`;
                        }
                    });

                    return tbOptMsg;
                }
            });
        }
        else if (res.indexOf("LINK") > -1) {
            nowColNameArr = res.toString().split("#").filter(x => x);
            nowProp = res.toString();
            if (nowProp.indexOf("READONLYLINK") > -1) nowProp = getRecomposedColumn(nowColNameArr, "READONLYLINK"); //側邊唯讀頁
            if (nowProp.indexOf("DLLINK") > -1) nowProp = getRecomposedColumn(nowColNameArr, "DLLINK");       //下載頁
            if (nowProp.indexOf("LINK") > -1) nowProp = getRecomposedColumn(nowColNameArr, "LINK");         //一般連結頁

            nowColumns.push({
                "title": nowProp,
                "mDataProp": nowProp,
                "render": function (data, type, row) {
                    tbOptMsg = ""; rowStr = JSON.stringify(row).replace(/"/gi, "|");

                    nowColNameArr.forEach(function (col) {
                        if (col.indexOf("READONLYLINK") > -1) {
                            tmpVal = row[nowProp];

                            //個管 多專科團隊側邊選單
                            if (col.indexOf("會議日期") > -1) {
                                if (row.dataid.match(/^CRM_/g) && row.doccode === "")
                                    tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.dataid}', '${row.doccode}', 'QRY', 'REC', '${row.團隊名稱}');" style="cursor:pointer" value="PageSlideReadOnly">${tmpVal}</a>`;
                                else
                                    tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.dataid}', '${row.doccode}', 'QRY', 'REC', '團隊會議');" style="cursor:pointer" value="PageSlideReadOnly">${tmpVal}</a>`;
                            }
                            else {
                                if (row.dataid.match(/^CRM_/g) && row.doccode === "")
                                    tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.dataid}', '${row.doccode}', 'CRM2TP_URL');" style="cursor:pointer" value="PageSlideReadOnly">${tmpVal}</a>`;
                                else
                                    tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.dataid}', '${row.doccode}', 'QRY', 'REC', ${cluster === "COL" ? "'基本資料'" : undefined});" style="cursor:pointer" value="PageSlideReadOnly">${tmpVal}</a>`;
                            }
                        }
                        else if (col.indexOf("DLLINK") > -1) {
                            //CRM多專科團隊 會議記錄-20191108
                            if (cluster === "MST") {
                                if (row.id.match(/^CRM_/))
                                    tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.id}_${row.個案號}_${row.病歷號}', '', 'CRMMSTF_URL', '${data}');" style="cursor:pointer" value="DLLINK">${data}</a>`;
                                else if (row.SEQ_ID)
                                    tbOptMsg = `<a href="#" onclick="getRecordFile('', '${row.SEQ_ID}');" value="DLLINK">${row["#" + col]}</a>`;
                                else
                                    tbOptMsg = `<a href="#" onclick="GetMstFlies('', '${row.病歷號}', '${row.姓名}', '')" style="cursor:pointer" value="DLLINK">${data}</a>`;
                            }
                            else if (cluster === "MAINT") {
                                if (row.SEQ_ID.match(/^Cloud/))
                                    tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="DLLINK">${row[col]}</a>`;
                                else
                                    tbOptMsg = `<a href="#" onclick="getRecordFile('', '${row.SEQ_ID}');" value="DLLINK">${row["#" + col]}</a>`;
                            }
                            else if (cluster === "RPT") {
                                if (module === "CM")
                                    tbOptMsg = `<a href="#" onclick="downloadByte('${row.報表名稱}', '${data}');" value="DLLINK">${col.replace("DLLINK", "")}</a>`;
                            }
                            else
                                tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="DLLINK">${data}</a>`;
                        }
                        else if (col.indexOf("LINK") > -1) {
                            tmpVal = col.replace("LINK", "").trim();
                            if (tmpVal === "個案號") {
                                if (cluster === "COL" && dataType && row.id.match(/^CRM_/g))
                                    tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.id} ', '', 'CDHEBD_URL');" style="cursor:pointer" value="PageSlideReadOnly">${data}</a>`;
                                else
                                    tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="${tmpVal}">${data}</a>`;
                            }
                            else if (tmpVal === "收案號") { //20191122 雲端
                                tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="${tmpVal}">${data}</a>`;
                            }
                            else if (tmpVal === "審查單號") {
                                tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="${tmpVal}">${row["#" + col]}</a>`;
                            }
                            else if (tmpVal === "病歷號" && ajccType !== "" &&
                                (Resultlist_ColType_Link_Flag.indexOf(ajccType) > -1 || LocalStorage_Key_ReadonlyJson.indexOf("READONLY") > -1)) {
                                var tmpText = row["#" + col] || row[tmpVal];
                                var caseData = "";
                                if (row.SEQ_ID) caseData = ` seq="${row.SEQ_ID}"`;
                                //若是舊CRM，由此產出函式
                                if (row.SEQ_ID === undefined) {
                                    if (cluster === "MST" && row.id.match(/^CRM_/))
                                        tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.id}_${row.個案號}_${data}', '', 'CRMMST_URL', '${data}');" style="cursor:pointer" value="${tmpVal}">${data}</a>`;
                                    else if (cluster.match(/ORAL|COLON|MMG|PS/) && ajcctype === "QRY")
                                        tbOptMsg = `<a href="#" onclick="linkTo('${row.dataid}','${row.doccode}','基本資料')" style="cursor:pointer">${tmpText}</a>`;
                                    else
                                        tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="${tmpVal}">${data}</a>`;
                                }
                                else {
                                    if (row.SEQ_ID.match(/^CRM_/) && row.SEQ_ID.split('_').length === 3)
                                        tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.SEQ_ID}', '', 'CRM2TP_URL');" style="cursor:pointer" value="PageSlideReadOnly"${caseData}>${tmpText}</a>`;
                                    else if (row.SEQ_ID.match(/^CRM_/) && row.SEQ_ID.split('_').length === 5 && cluster === "ORAL")
                                        tbOptMsg = `<a href="#" onclick="GetViewReadonly('${row.SEQ_ID}', '', 'CRM2TP_URL');" style="cursor:pointer" value="PageSlideReadOnly"${caseData}>${tmpText}</a>`;
                                    else
                                        tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="${tmpVal}"${caseData}>${tmpText}</a>`;
                                }
                            }
                            else if (tmpVal === "會議日期" && ajccType === "QRY") {
                                tmpVal = row.doccode;
                                if (tmpVal === "" || tmpVal === undefined)
                                    tmpVal = rowStr;
                                //尚無法確認data為何undefined，改用row["#" + col]顯示
                                if (row.不開會理由 !== "")
                                    tbOptMsg = `${row["#" + col]}`;
                                else
                                    tbOptMsg = `<a href="#" onclick="linkTo('${row.dataid}','${tmpVal}','團隊會議', 'MST${row.團隊名稱.split('(')[0]}')" style="cursor:pointer">${row["#" + col]}</a>`;
                            }
                            //20191122 雲端
                            else if (tmpVal === "檔案名稱" & module === 'CR' && cluster === 'MAINT' && (masterSpec === 'IMPORT' || masterSpec === 'TRACELIST')) {
                                var path = row['檔案路徑'];
                                tbOptMsg = `<a href="#" onclick="DownLoadFile('${row['檔案路徑']}', '${row['SEQ_ID']}')" value="${tmpVal}">${row[tmpVal]}</a>`;
                            } else if (tmpVal === "申請文號" && module === 'CR' && cluster === 'MAINT' && masterSpec === 'RES') {
                                var doccode = row['doccode'].split('-');
                                tbOptMsg = `<a href="Edit?cluster=MAINT&ajccType=${doccode[0]}&id=${row['dataid']}&code=${row['doccode']}&spec=${doccode[1]}">${row['申請文號']}</a>`;
                            }
                            //癌篩子抹查詢
                            else if (module === "CS" && cluster === "PS" && ajccType === "QRY") {
                                tbOptMsg = `<a onclick="linkTo('${row.dataid}','${row.doccode}','${tmpVal}')" style="cursor:pointer">${tmpVal}</a>`;
                            }
                            else {
                                var tmpShowText = tmpVal;
                                if (row[`*${tmpVal}`]) tmpShowText = `<div>*</div>${tmpVal}`;
                                else if (row[tmpVal] === "link" || row[tmpVal] === "") tmpShowText = tmpVal;
                                else if (row[tmpVal]) tmpShowText = row[tmpVal];
                                if (tmpVal === "前次填寫日期" && ajccType.match(/(TODO|REC)/g)) {
                                    if (tmpShowText !== "" && tmpShowText !== tmpVal && row.readonly_dataid !== "") {
                                        tbOptMsg = `<a href="#" onclick="var row = '${rowStr}'; setResultList(row, this);" style="cursor:pointer" value="${tmpVal}">${tmpShowText}</a>`;
                                    }
                                    else if (tmpShowText !== "前次填寫日期" && row.readonly_dataid === "") {
                                        tbOptMsg = `${tmpShowText}`;
                                    }
                                }
                                else if (tmpVal === "1-3姓名" && module === "CR" && cluster === 'REV' && masterSpec === 'INQUIRE') {
                                    var DataType = "維護";
                                    if (row["審查案件狀態"] === "已抽回")
                                        tbOptMsg = `<a href="#" onclick="FixSysMsg.danger('此案件已被癌登師抽回');" style="cursor:pointer">${row["#" + col]}</a>`;
                                    else
                                        tbOptMsg = `<a href="#" onclick="linkTo('${row.dataid}','${row.doccode}','${DataType}')" style="cursor:pointer">${row["#" + col]}</a>`;
                                }
                                else
                                    tbOptMsg = `<a onclick="linkTo('${row.dataid}','${row.doccode}','${tmpVal}')" style="cursor:pointer">${tmpShowText}</a>`;
                            }
                        }

                        return false;
                    });

                    return tbOptMsg;
                }
            });
        }
        else if (res.indexOf("□ALL") > -1) {
            nowColNameArr = res.toString().split("#").filter(x => x);
            nowProp = getRecomposedColumn(nowColNameArr, "□");
        }
        else if (res.indexOf("□") > -1) {
            nowColNameArr = res.toString().split("#").filter(x => x);
            nowProp = getRecomposedColumn(nowColNameArr, "□");

            nowColumns.push({
                "title": nowProp,
                "mDataProp": nowProp,
                "data": res,
                "render": function (data, type, row) {
                    tbOptMsg = ""; rowStr = JSON.stringify(row).replace(/"/gi, "|");

                    nowColNameArr.forEach(function (col) {
                        if (ajccType === "QRY" && masterSpec.match(/(DIVIDE|ASSIGN)DOCTOR/)) {
                            tbOptMsg = `<input type="checkbox" class="checkbox-inline" name="chkSetResultList" value="${col.replace("□", "").trim()}" databind="${rowStr}" ${data}`;

                            if (col.replace("□", "").trim() === "化療審查")
                                tbOptMsg += ` ${data.indexOf("disabled") > -1 ? "" : "onclick=\"CMReviewGearing(this);\""}`;

                            tbOptMsg += ` />`;
                        }
                        else if (ajccType === "QRY" && masterSpec.match(/REG(QRY|SEND)/)) //20191127 雲端
                            tbOptMsg = `<input type="checkbox" class="checkbox-inline" name="chkSetResultList" value="${col.replace("□", "").trim()}" ${data.indexOf("disabled") > -1 ? "" : "databind=\"" + rowStr + "\""} ${data} />`;
                        else
                            tbOptMsg = `<input type="checkbox" class="checkbox-inline" name="chkSetResultList" onclick="var row='${rowStr}'; setResultList(row, this);" value="${col.replace("□", "").trim()}" ${data ? "checked disabled" : ""}/>`;
                    });

                    return tbOptMsg;
                }
            });
        }
        else if (res.indexOf("SELECT") > -1) {
            nowColNameArr = res.toString().split("#").filter(x => x);
            nowProp = getRecomposedColumn(nowColNameArr, "SELECT");

            nowColumns.push({
                "title": nowProp,
                "mDataProp": nowProp,
                "render": function (data, type, row) {
                    var resDom = $(`<div></div>`);
                    var detailSelectDom = $(`<select class="show-tick"></select>`);

                    nowColNameArr.forEach(function (col) {
                        //取得已預存在selectOptionsObj內、應顯示的下拉選項清單
                        var optionData = [];
                        var nowOptionDataKey = getSelectOptionObjSpecificKey(nowProp);
                        if (selectOptionsObj && selectOptionsObj.hasOwnProperty(`${nowOptionDataKey}`)) {
                            optionData = eval(`selectOptionsObj.${nowOptionDataKey}`);
                        } else {
                            if (nowProp === "收案類別") {
                                nowOptionDataKey = "癌別代碼(癌別中文)";
                                optionData = JSON.parse(`[${getOptionStr(eval(`selectOptionsObj.癌別`), "onSelectCodeList", "癌別", nowOptionDataKey)}]`);
                            }
                            else if (nowProp === "未填寫理由") {
                                nowOptionDataKey = "ReasonList";
                                optionData = eval(`selectOptionsObj.${nowOptionDataKey}`);

                                //[診療計畫書-醫師待辦清單]連動下一個物件(科別)
                                if (cluster === "TP" &&
                                    ajccType.match(/(TODO|QRY)/) &&
                                    masterSpec.match(/(CHECKLIST|UNFILLEDLSIT)/))
                                    detailSelectDom.attr("onchange", `SetNextDIVVisible(ResetDefaultVal, this, '${row.SEQ_ID}')`);
                            }
                            else if (module === "CM" && cluster === "CEM" && masterSpec === "ASSIGNDOCTOR" && nowProp === "審查醫師") {
                                optionData = eval(`selectOptionsObj.${nowProp}片語`).filter(function (item) {
                                    return item.審查基準 === "基準2.2及3.3同儕互審(內部)" && item.審查年度 === seq_id && item.癌別.indexOf(row.癌別) > -1;
                                });
                            }
                            else if (module === "CR" && cluster === "REV" && masterSpec === "REGSEND" && nowProp === "審查人員") {
                                var _temp = $(`label:contains(審查醫師類型)`).next("div").find("select").val();
                                //取審查醫師 腫瘤治療科
                                if (_temp === "RT醫師") {
                                    optionData = eval(`selectOptionsObj.審查醫師片語`).filter(function (item) {//&& item.癌別.indexOf(row.癌別) > -1
                                        return item.審查基準 === "基準2.2及3.3同儕互審(內部)" && item.審查年度 === seq_id && item.科別 === "腫瘤治療科" && item.分組 === "998(RT醫師)";
                                    });
                                } else if (_temp === "團隊醫師") {
                                    _temp = $(`label:contains(審查表類別)`).next("div").find("select").val();
                                    if (_temp.indexOf("外部") > -1)
                                        _temp = "基準2.2癌品會審查(外部)";
                                    else
                                        _temp = "基準2.2及3.3同儕互審(內部)";
                                    optionData = eval(`selectOptionsObj.審查醫師片語`).filter(function (item) {
                                        return item.審查基準 === _temp && item.審查年度 === seq_id && item.癌別.indexOf(row.癌別) > -1;
                                    });
                                }//取審查醫師 其他審查人員
                                else {
                                    _temp = $(`label:contains(審查表類別)`).next("div").find("select").val();
                                    if (_temp.indexOf("外部") > -1)
                                        _temp = "基準2.2癌品會審查(外部)";
                                    else
                                        _temp = "基準2.2及3.3同儕互審(內部)";
                                    optionData = eval(`selectOptionsObj.審查醫師片語`).filter(function (item) {
                                        return item.審查基準 === _temp && item.審查年度 === seq_id && item.科別 === "其他審查人員" && item.分組 === "999(其他審查人員)";
                                    });
                                }
                            }
                            else if (nowProp === "審查醫師") {
                                optionData = eval(`selectOptionsObj.${nowProp}片語`).filter(function (item) {
                                    return item.癌別.indexOf(row.癌別) > -1;
                                });
                            }
                        }

                        var myOption, isSelected = true;
                        if (nowProp !== "審查醫師") detailSelectDom.append(`<option value="">請選擇</option>`);

                        jQuery.each(optionData, function (k, val) {
                            var _drExam, _selected;

                            if (module === "CM" && cluster === "CEM" && masterSpec === "ASSIGNDOCTOR" && nowProp === "審查醫師") {
                                _drExam = val[nowProp].replace(/\(([\u4e00-\u9fa5]|，|、)+\)/gi, "");
                                _selected = data === _drExam || data === "" && row.主治醫師.indexOf(_drExam) === -1 ? " selected" : "";
                                myOption = `<option value="${val[nowProp]}"${_selected}>${val[nowProp].match(/[\u4e00-\u9fa5]+/gi)}</option>`;

                                //個案審查-派案作業
                                if (row["#勾選 □"] && row["#勾選 □"].indexOf("disabled") > -1 && detailSelectDom.attr("disabled") === undefined) detailSelectDom.attr("disabled", "disabled");
                            }
                            else if (module === "CR" && cluster === "REV" && masterSpec === "REGSEND" && nowProp === "審查人員") {
                                _drExam = val["審查醫師"].replace(/\(([\u4e00-\u9fa5]|，|、)+\)/gi, "");
                                _selected = data === _drExam || data === "" && row.主治醫師.indexOf(_drExam) === -1 ? " selected" : "";
                                myOption = `<option value="${val["審查醫師"]}"${_selected}>${val["審查醫師"].match(/[\u4e00-\u9fa5]+/gi)}</option>`;

                                //個案審查-派案作業
                                if (row["#勾選 □"] && row["#勾選 □"].indexOf("disabled") > -1 && detailSelectDom.attr("disabled") === undefined) detailSelectDom.attr("disabled", "disabled");
                                //myOption = `<option value="${val.resultdata[nowProp]}"${data === val.resultdata[nowProp] ? " selected" : ""}>${val.resultdata[nowProp]}</option>`;
                            }
                            else if (nowProp === "收案類別") {
                                myOption = `<option value="${val[nowOptionDataKey] === "空白" ? "" : val[nowOptionDataKey]}"${data === val[nowOptionDataKey] ? " selected" : ""}>${val[nowOptionDataKey].replace(/(\d{3,5}\w{3,5}|\(|\))/g, '')}</option>`;
                            }
                            else if (val["add value"] === undefined) {
                                myOption = `<option value="${val.item || val.Item}"${data === val.resultdata[nowProp] ? " selected" : ""}>${val.item || val.Item}</option>`;
                            }
                            else {
                                var tmpValue = val.item || val.Item;
                                myOption = `<option value="${tmpValue}(${val["add value"]})"${data === val["add value"] ? " selected" : ""}>${tmpValue}</option>`;
                            }

                            detailSelectDom.append(myOption);
                        });
                        resDom.append(detailSelectDom);

                        if (nowProp === "未填寫理由" && cluster === "TP" &&
                            ajccType.match(/(TODO|QRY)/) &&
                            masterSpec.match(/(CHECKLIST|UNFILLEDLSIT)/)) {
                            isSelected = true;

                            //轉本院他科或其他醫師
                            var $OTH = $(`<div id="div_OTHRD_${row.SEQ_ID}" class="div_holder_OTHREA"></div>`),
                                UnReason = ["科別:科別:員編(姓名)", "醫師:員編(姓名)"];
                            UnReason.forEach(function (item, i) {
                                var $obj = item.split(':');

                                detailSelectDom = $(`<select class="show-tick"></select>`);
                                detailSelectDom.append(`<option value="">請選擇</option>`);
                                if (i === 0) detailSelectDom.attr("onchange", `ResetNextSelectOptions(this, getOptionStr, "onSelectHints", "醫師姓名片語", "${$obj[2]}");`);

                                optionData = JSON.parse(`[${getOptionStr(eval(`selectOptionsObj.醫師姓名片語`), "onSelectHints", "醫師姓名片語", `${$obj[1]}`)}]`);
                                optionData.forEach(function (item) {
                                    myOption = `<option value="${item[`${$obj[1]}`] === "空白" ? "" : item[`${$obj[1]}`]}"${isSelected ? " selected" : ""}>${item[`${$obj[1]}`]}</option>`;
                                    detailSelectDom.append(myOption);
                                });
                                $OTH.append(`${$obj[0]}：`).append(detailSelectDom);
                            });

                            if (row.未填寫理由 !== TODO_EnableOptionFlag) $OTH.hide();
                            resDom.append($OTH);

                            //其他
                            $OTH = $(`<div id="div_OTHRDI_${row.SEQ_ID}" class="div_holder_OTHREA"></div>`);
                            $OTH.append(`請說明：<textarea col="10" row="5" maxlength="500"></textarea>`);
                            if (row.未填寫理由 !== "其他") $OTH.hide();
                            resDom.append($OTH);
                        }
                    });

                    return resDom.html();
                }
            });
        }
        else if (res === "j") {
            nowColumns.push({
                "title": "",
                "mDataProp": res,
                "render": function (data, type, row) {
                    return `<a onclick="linkTo('${row.dataid}','${row.doccode}','')" style="cursor:pointer">連結</a>`;
                }
            });
        }
        else if (res.match(/^\$/)) { //[管理設計書]處理
            nowProp = getRecomposedColumn(res.split("$").filter(x => x), "$").trim();

            nowColumns.push({
                "title": nowProp,
                "mDataProp": nowProp,
                "render": function (data, type, row) {
                    tbOptMsg = ""; rowStr = JSON.stringify(row).replace(/"/gi, "|");

                    if (nowProp === "Excel") { //下載Excel
                        tbOptMsg = `<input type="button" class="btn btn-primary la_btn_pink" name="btnDownload" onclick="downloadExcel('${row.Module}','${row.Cluster}','${row.Class}','${row.Spec}','${row.Extend}')" value="下載" rowval="${rowStr}" />`;
                    }
                    else if (nowProp === "上傳日期") {
                        tbOptMsg = row.UpdTime.replace(/\T/, " ");
                    }
                    else if (nowProp === "上架日期") {
                        if (row.DocStatus === "可上架" && row.PrdTime === "0001-01-01T00:00:00")
                            tbOptMsg = `<input type="button" class="btn btn-primary la_btn_pink" name="btnUpDate" onclick="upDate('${row.DocVer}','${row.Module}','${row.Cluster}','${row.Class}','${row.Spec}')" value="上架" />`;
                        else
                            tbOptMsg = row.PrdTime.replace(/\T/, " ");
                    }
                    else if (nowProp === "下架日期") {
                        if (row.DocStatus === "已上架" && row.DropTime === "0001-01-01T00:00:00")
                            tbOptMsg = `<input type="button"  class="btn btn-primary la_btn_pink" name="btnDownDate" onclick="downDate('${row.DocVer}','${row.Module}','${row.Cluster}','${row.Class}','${row.Spec}')" value="下架" />`;
                        else if (row.DropTime !== "0001-01-01T00:00:00")
                            tbOptMsg = row.DropTime.replace(/\T/, " ");
                        else
                            tbOptMsg = "";
                    }
                    else if (nowProp === "狀態") {
                        tbOptMsg = row.DocStatus;
                    }
                    else if (nowProp === "類別") {
                        tbOptMsg = row.Class;
                    }
                    else if (nowProp === "代碼") {
                        tbOptMsg = row.DocCode;
                    }
                    else if (nowProp === "顯示") {
                        tbOptMsg = row.Disp;
                    }
                    else if (nowProp === "錯誤訊息") {
                        tbOptMsg = row.ErrMessage.replace(/(\"|\')/g, "").replace(/，/g, ",").replace(/(。|、)/g, ".");
                        if (tbOptMsg === "")
                            tbOptMsg = "無";
                        else {
                            tbOptMsg = `<input type="button" class="btn btn-primary" name="btnDisplay" onclick="FixSysMsg.danger('${tbOptMsg}')" value="顯示" /> &nbsp; <input type="button" class="btn btn-primary" name="btnDownLoadError" onclick="dowloadError('${tbOptMsg}')" value="下載" />`;
                        }
                    }
                    else if (nowProp === "下載明細報表") {
                        tbOptMsg = `<input type="button" class="btn btn-primary la_btn_pink" name="btnDownDetail" onclick="downDetail('${row.Module}','${row.Cluster}','${row.Class}','${row.Spec}','${row.Disp}')" value="下載明細報表" />`;
                    }
                    else if (nowProp === "設計書預覽") { // && row.DocCode.match(/nodisplay/gi)
                        tbOptMsg = `<input type="button" class="btn btn-primary la_btn_pink" name="btnDownDetail" onclick="reviewForm('${row.Module}','${row.Cluster}','${row.Class}','${row.Spec}')" value="設計書預覽" />`;
                    }

                    return tbOptMsg;
                }
            });
        }
        else if (res.match(/^%/)) { //群組權限維護
            nowProp = getRecomposedColumn(res.split("%").filter(x => x), "H").trim();
            rowStr = res.match(/H$/) ? false : true;

            nowColumns.push({
                "title": nowProp,
                "mDataProp": nowProp,
                "visible": rowStr, //隱藏該欄位
                "render": function (data, type, row) {
                    tbOptMsg = ""; rowStr = JSON.stringify(row).replace(/"/gi, "|");

                    if (nowProp === "權限") {
                        tbOptMsg += `<input id="module${editor}" type="hidden" value="${row.module}" />`;
                        tbOptMsg += `<input id="cluster${editor}" type="hidden" value="${row.cluster}" />`;
                        tbOptMsg += `<input id="${row.module}_${row.cluster}_S" type="checkbox" value="S" ${row.權限.indexOf('S') !== -1 ? "checked " : ""}/>管理　`;
                        tbOptMsg += `<input id="${row.module}_${row.cluster}_B" type="checkbox" value="B" ${row.權限.indexOf('B') !== -1 ? "checked " : ""}/>黑名單　`;
                        tbOptMsg += `<input id="${row.module}_${row.cluster}_C" type="checkbox" value="C" ${row.權限.indexOf('C') !== -1 ? "checked " : ""}/>新增　`;
                        tbOptMsg += `<input id="${row.module}_${row.cluster}_R" type="checkbox" value="R" ${row.權限.indexOf('R') !== -1 ? "checked " : ""}/>讀取　`;
                        tbOptMsg += `<input id="${row.module}_${row.cluster}_U" type="checkbox" value="U" ${row.權限.indexOf('U') !== -1 ? "checked " : ""}/>更新　`;
                        tbOptMsg += `<input id="${row.module}_${row.cluster}_D" type="checkbox" value="D" ${row.權限.indexOf('D') !== -1 ? "checked " : ""}/>刪除`;
                        editor++;
                    }
                    else if (nowProp === "刪除")
                        tbOptMsg += `<input type="button" class="btn btn-info la_btn_pink" value="刪除" onclick="DeleteGroup('${row.群組代碼.trim()}')" />`;
                    else if (nowProp === "權限維護")
                        tbOptMsg += `<input type="button" class="btn btn-info la_btn_pink" value="權限維護" onclick="funcPermit('${row.群組代碼.trim()}')" />`;
                    else if (nowProp === "人員維護")
                        tbOptMsg += `<input type="button" class="btn btn-info la_btn_pink" value="人員維護" onclick="funcPeople('${row.群組代碼.trim()}')" />`;
                    else if (nowProp === "選取")
                        tbOptMsg += `<input id="${row.員編}" name="gpULP" type="checkbox" value="${row.員編}" ${row.選取 ? "checked " : ""}/>`;
                    else if (nowProp === "編輯")
                        tbOptMsg += `<input type="button" class="btn btn-info la_btn_pink" value="編輯" onclick="funcUser('${row.員工編號.trim()}')" />`;
                    else
                        tbOptMsg = row[nowProp];

                    return tbOptMsg;
                }
            });

            if (nowProp === "權限") nowColumns[nowColumns.length - 1]["sWidth"] = "50%";
        }
        else if (res.match(/(主檔編號)/)) { //20191122 雲端
            if (ajccType === "SETUP" && masterSpec === "S3CDETAIL") {
                nowColumns.push({
                    "title": res,
                    "mDataProp": res,
                    "render": function (data, type, row) { return data; }
                });
            }
        }
        else {
            if (res === "dataid" || res === "doccode" || res === "cluster")
                nowColumns.push({ "title": "", "mDataProp": res, visible: false, searchable: false });
            else if (Resultlist_ColName_AutoLineBreak_Flag.indexOf(res) > -1) {
                nowColumns.push({
                    "title": res,
                    "mDataProp": res,
                    "render": function (data, type, row) {
                        var rtnItem = document.createElement("label");
                        $(rtnItem).addClass("label_auto_line_break").text(data);

                        return rtnItem.outerHTML;
                    }
                });
            }
            else {
                if (new RegExp(/#.+(asc|desc)/g).test(res)) {
                    //需排序的欄位
                    nowColumns.push({ "title": res.replace(/#|asc|desc/g, ""), "mDataProp": res.replace(/#|asc|desc/g, "") });
                } else {
                    rowStr = res.match(/H$/) ? false : true;
                    nowColumns.push({ "title": res, "mDataProp": res, "visible": rowStr, "render": function (data, type, row) { if (data === undefined) data = row[res]; return data ? data : ""; } });
                }
            }
        }

        //設定排序
        if (new RegExp(/#.+(asc|desc)/g).test(res)) {
            orderNum = i;
            orderType = res.match(/asc|desc/g)[0];
        }
    });


 

    //排序index number(Todo: 參ReadonlyPersonalList)
    if (dataType.indexOf("審核意見")>-1) {
    	        orderNum = 4;;
    }else if(dataType.indexOf("附件資料")>-1){
		orderNum = 3; orderType = "desc";
	}

    //Mike:由於上述描述寫死了指定第幾個欄位排序，當欄位數低於這數字將會出現嚴重錯誤，所以在此加入防呆，若超過就改為最後一個欄位排序
    if (orderNum >= nowColumns.length) {
        orderNum = nowColumns.length - 1;
    }

    if (nowTbOptions === undefined) {
        nowTbOptions = {
            info: true,                      //共幾筆(頁腳)
            lengthChange: true,              //是否啟用變更顯示幾筆
            order: [[orderNum, orderType]],  //初始順序
            paging: true,                    //分頁和顯示幾筆
            pagingType: "full_numbers"      //分頁樣式
        };

        if (ajccType === "QRY" && masterSpec.match(/(ASSIGN|DIVIDE)DOCTOR|REGQRY|S3CDETAIL/)) {
            nowTbOptions["order"] = "";
            nowTbOptions["paging"] = false;
        }
    }

    //基本參數
    /*
     * 列初始化、語言配置選項、控制排序功能、響應式擴展
     * 可重複初始化、顯示有限數量的行時允許表格減小高度、搜尋
    */
    nowTbOptions.columns = nowColumns;
    nowTbOptions.language = { url: "../../resources/dataTable/langzh_tw.json" };
    nowTbOptions.ordering = true;
    nowTbOptions.responsive = true;
    nowTbOptions.retrieve = true;
    nowTbOptions.scrollCollaps = true;
    if (nowTbOptions.searching === undefined) nowTbOptions.searching = false;
    //將基本參數設定至DataTable內
    $nowTable.DataTable(nowTbOptions);

    //將datatable的寬度預設至fit外框div寬度
    //若datatable無資料時再至各js內指定css => table-layout: fixed
    $nowTable.css({ "width": "100%" });

    //關閉jquery Datatable自有error檢核提示
    $.fn.dataTable.ext.errMode = 'none';
}