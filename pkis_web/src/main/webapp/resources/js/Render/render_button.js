function render_button(element, parentname, changeKey = null) {
    var tmpObj;//合併須刪除
    var obj = $(`<div></div>`);

    //按鈕控制項
    var element_api = "";
    var element_js = "";
    var element_ref = "";
    var isAsync = true; //判斷是否非同步
    if (element.ref && element.ref.indexOf("SYNC") > -1 && element.ref.indexOf("ASYNC") === -1) isAsync = false;
    var element_hidden = "";
    if (element.remark && element.remark.indexOf("hide") > -1) element_hidden = ` style="display:none;"`;

    if (element.api && element.api === 'getResultList') {
        //get result 按鈕
        var para1 = cluster; // cluster
        var para2 = "";

        var para_doccode = json.data[0].doccode;
        var para_doccode_array = para_doccode.split("-");
        para1 += "_" + para_doccode_array[0] + "_" + para_doccode_array[1];
        para2 = element.parameters;

        if (element.item === "新增") {
            element_api = `onclick="CheckAndSave()"`;
        } else {
            // api: getResultList
            if (element.api === "getResultList") {
                if (para1.indexOf("COL") === 0 &&
                    para1.indexOf("QRYREC") > 0 &&
                    para1.match(/_COMM_/g))
                    para1 = para1.replace(/_COMM/g, "").replace(/_QRYREC/g, "_QRY") + "_COMM";

                element_api = `onclick="${element.api}('${para1}', '${para2}', 'json_query', '${para_doccode}', ''${!isAsync ? "," + isAsync : ""})"`;
            } else
                element_api = `onclick="${element.api}('${para1}', '${para2}', 'json_query', '${para_doccode}')"`;
        }

        // ResultList的button不應刷新頁面
        obj.append(`<input type="button" class="btn btn-primary" ${element_api} value="${element.segmentation || element.item}"${element_hidden}>&nbsp;&nbsp;`);
    }
    else if (element.api && element.api === 'getCardCloudInterface') {
        // 查詢保健紀錄BTN Leo
        element_api = `onclick="getHICloudData()"`;
        obj.append(`&nbsp;&nbsp;&nbsp;<input type="button" class="btn btn-primary" ${element_api} value="${element.segmentation || element.item}">`);
    }
    else if (element.api && element.api === 'getPatientInfo') {
        if (element.ref) {
            if (element.ref.indexOf(";") > -1) {
                element_ref = ",'" + element.ref.split(";")[1] + "'"; //eg. element.ref => "SYNC;required.病歷號||required.姓名"
            } else if (element.ref !== "")
                element_ref = ",'" + element.ref + "'";
        }
        // 取病歷資訊
        element_api = `onclick="getPatientInfo('${element.parameters}','#json_query'${element_ref}${!isAsync ? "," + isAsync : ""})"`;
        obj.append(`&nbsp;&nbsp;&nbsp;<input type="button" class="btn btn-primary" ${element_api} value="${element.segmentation || element.item}">`);
    }
    else if (element.segmentation && element.js) {
        var mustSubmit = false;
        var divDom, blockHtml, _jsReq = "", reqParas;
        //eg. 取病歷資訊
        if (element.js === "setPatientInfo") {
            if (element.api) {
                element_api = element.api;
                if (element.parameters)
                    element_js = `onclick="setPatientInfo('${element_api}', '${element.parameters}')"`;
                else
                    element_js = `onclick="setPatientInfo('${element_api}')"`;
            } else {
                element_js = `onclick="setPatientInfo('')"`;
            }
        }
        else if (element.js === "SetPatInfo" && element.api) {
            //eg. 取病歷資訊(自動查詢)
            if (element.parameters)
                element_js = `onclick="${element.js}('${element.parameters}', GetPatientInfo, '${element.api}')"`;
            else
                element_js = `onclick="${element.js}('', GetPatientInfo, '${element.api}')"`;
        }
        else if (element.js === "SetCancerInfo" && element.api) {
            //eg. 取原發部位(自動查詢)
            if (element.parameters)
                element_js = `onclick="${element.js}('${element.parameters}', GetCancerSiteInfo, '${element.api}')"`;
        }
        else if (element.js === "setToBackResult") {
            //eg. 帶回、選案、查詢
            if ("新增|".indexOf(element.segmentation) === -1 && "新增|".indexOf(element.item) === -1)
                element_js = `onclick="${element.js}('${element.segmentation}','${element.parameters || ""}')"`;
            else {
                if (element.segmentation === "新增" || element.item === "新增") {
                    mustSubmit = true;
                    element_js = `onclick="CheckAndSave()"`;
                }
            }
        }
        else if (element.js === "setStatus") {
            //儲存時，需設定"案件狀態"
            element_js = `onclick="${element.js}('${dataType}'); CheckAndSave();"`;
        }
        else if (element.js === "setSave" || element.js === "save") {
            //儲存按鈕
            //mustSubmit = true;
            if (element.ref && element.ref.indexOf("required") > -1) {
                //必填項目檢核
                reqParas = element.ref.indexOf(",") > -1 ? element.ref.split(",") : [element.ref];
                if (reqParas) {
                    reqParas = reqParas.filter(function (item, i) {
                        return item.toString().indexOf("required.") > -1;
                    });
                    if (reqParas.length > 0) {
                        reqParas.forEach(function (item, j) {
                            _jsReq += `,${item.replace("required.", "")}`;
                        });
                        _jsReq = _jsReq.substring(1);
                    }
                }
            }
            if (_jsReq === "")
                element_js = `onclick="CheckAndSave();"`;
            else {
                if (element.ref.indexOf("團隊名稱") > -1 && element.segmentation.indexOf("儲存") > -1)
                    element_js = `onclick="MstCheckAndSave('${_jsReq}');"`;
                else
                    element_js = `onclick="CheckRequiredItems('${_jsReq}', CheckAndSave);"`;
            }
        }
        else {
            if (element.ref && element.ref.indexOf("required") > -1) {
                //必填項目檢核
                reqParas = element.ref.indexOf(",") > -1 ? element.ref.split(",") : [element.ref];
                if (reqParas) {
                    reqParas = reqParas.filter(function (item, i) {
                        return item.toString().indexOf("required.") > -1;
                    });
                    if (reqParas.length > 0) {
                        reqParas.forEach(function (item, j) {
                            _jsReq += `,${item.replace("required.", "")}`;
                        });
                        _jsReq = _jsReq.substring(1);
                    }
                }
            }
            if (_jsReq === "") {
                //不透過confirm詢問，直接進入編輯頁(LinkURL)
                if (element.ref && element.ref.indexOf("unconfirm") > -1)
                    element_js = `onclick="${element.js}(${element.parameters ? "'" + element.parameters + "'" : "''" + ",'" + element.ref + "'"})"`;
                else
                    element_js = `onclick="${element.js}(${element.parameters ? "'" + element.parameters + "'" : ""})"`;
            } else
                element_js = `onclick="CheckRequiredItems('${_jsReq}', ${element.js}${element.parameters ? ", '" + element.parameters + "'" : ""});"`;
        }

        if (element.api) {
            if (element.api === "CheckExistData") {
                //20190613 因腫瘤心理個案新收問題，將最末參數的取得方式調整
                if (_jsReq.split(",").length > 3) {
                    reqParas = _jsReq.split(",").slice(-4);
                    if (reqParas.length > 0) {
                        _jsReq = "";
                        reqParas.forEach(function (item, j) {
                            _jsReq += `,${item.replace("required.", "")}`;
                        });
                        _jsReq = _jsReq.substring(1);
                    }
                }

                //[MANTIS:0025988] 存檔前，依條件檢核是否有已存在資料
                //20190711
                if (cluster === "HINTS" && ajccType.match(/(SETUP|QRY)/) && masterSpec === "EXAMDOCTORS" ||
                    cluster === "RPTSET" && ajccType.match(/(SETUP|QRY)/))
                    element_js = element_js.replace("CheckAndSave", "CheckExistDataAndSave");
                else
                    element_js = element_js.replace("CheckAndSave", `CheckExistDataAndSave, undefined, GetPatientInfo, '${_jsReq.substring(_jsReq.indexOf(',') + 1)}'`);

                if (element.parameters && element.parameters.indexOf("exist.") > -1) {
                    //帶入檢核條件
                    if (element.parameters.indexOf(",") > -1) {
                        selectOptionsObj[element.api] = element.parameters.split(",")
                            .filter(para => para.indexOf("exist.") === 0)
                            .map(para => para.replace("exist.", ""));
                    } else
                        selectOptionsObj[element.api] = [element.parameters.replace("exist.", "")];
                }
            }
        }

        if (mustSubmit) {
            obj.append(`<button class="btn btn-primary send_submit" ${element_js}>${element.segmentation || element.item}</button>&nbsp;&nbsp;`);
        }
        else {
            //增加BTN
            if (element.segmentation.indexOf("增加 ") === 0) {
                //自動帶出的欄位
                tmpObj = getColumnName();
            }
            else
                obj.append(`&nbsp;&nbsp;<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation}" />&nbsp;&nbsp;`);
        }
    }
    else {
        //儲存按鈕
        if (element.js === "setSave" || element.js === "save" ||
            element.segmentation || element.item === "儲存" || element.segmentation === "派案") {
            element_js = `onclick="`;
            if (element.segmentation === "派案") {
                element_js += `sendCase('選取');"`;
                obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation}" />&nbsp;&nbsp;`);
            }
            else if (element.segmentation === "帶回") {
                if (element.ref) {
                    if (element.ref.indexOf(";") > -1) {
                        element_ref = "'" + element.ref.split(";")[1] + "'"; //eg. element.ref => "SYNC;required.病歷號||required.姓名"
                    }
                }
                element_js = `onclick="setRtnInfoValCR(${element_ref})"`; //eg. setRtnInfoVal
                obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation}" />`);
            }
            else if (element.api === "SaveCase") {
                //[待收案清單資料查詢] 儲存
                element_js += `SaveCase();"`;
                obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation || element.item}" />&nbsp;&nbsp;`);
            }
            else if ((element.segmentation || element.item) && element.js) {
                if (element.js !== "setSave" && element.js !== "save")
                    element_js += `${element.js}('${dataType}'); CheckAndSave();"`;
                else
                    element_js += `CheckAndSave();"`;
                obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation || element.item}" />&nbsp;&nbsp;`);
            }
            else {
                //儲存按鈕
                if (ajccType === "QRYREC" && (dataType === "個管服務" || dataType === "追蹤")) {
                    element_js = `onclick="`;
                    $.each(selectOptionsObj.baseControlers, function (i, obj) {
                        if (obj.remark === "controller" && obj.ref === `#${element.segmentation || element.item}BTN`) {
                            var tmpJsArr = obj.js && obj.js.indexOf(",") > -1 ? obj.js.split(",") : [obj.js];
                            tmpJsArr.forEach(function (vJs, iJs) {
                                if (vJs === "getSysTime")
                                    // [追蹤] 儲存時，取得系統時間並存入指定欄位
                                    element_js += `getSysTime('簡訊發送時間', 'SysTime', '${dataType}'); `;
                                else
                                    element_js += `${vJs}('${dataType}'); `;
                            });
                            return false;
                        }
                    });
                    element_js += `"`;
                    obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation || element.item}" />&nbsp;&nbsp;`);
                }
                else {
                    element_js += `CheckAndSave();"`;
                    obj.append(`<button class="btn btn-primary send_submit" ${element_js}>${element.segmentation || element.item}</button>&nbsp;&nbsp;`);
                }
            }
        }
        else {
            if (element.segmentation && element.segmentation === "選案" && element.ref) {
                element_js = `onclick="${element.ref}()"`; //eg. selectCase
                obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation}" />`);
            }
            else {
                var docdetailItem, tmpDataid = "", tmpDoccode = "";
                if ((element.segmentation || element.item) === "清除") {
                    if (ajccType === "QRYREC" && (dataType === "個管服務" || dataType === "追蹤")) {
                        element_js = `onclick="`;
                        $.each(selectOptionsObj.baseControlers, function (i, obj) {
                            if (obj.remark === "controller" && obj.ref === `#${element.segmentation || element.item}BTN`) {
                                var tmpJsArr = obj.js && obj.js.indexOf(",") > -1 ? obj.js.split(",") : [obj.js];
                                tmpJsArr.forEach(function (vJs, iJs) {
                                    if (vJs === "getSysTime")
                                        // [追蹤] 儲存時，取得系統時間並存入指定欄位
                                        element_js += `getSysTime('簡訊發送時間', 'SysTime', '${dataType}'); `;
                                    else
                                        element_js += `${vJs}('${dataType}'); `;
                                });
                                return false;
                            }
                        });
                        element_js += `"`;
                        obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation || element.item}" />&nbsp;&nbsp;`);
                    }
                }
                else if ((element.segmentation || element.item) && element.js) {
                    element_js = `onclick="`;
                    if (element.js.indexOf("RepBTN") === 0) { // eg. [診斷]複製BTN
                        var tmpFunc = element.js.indexOf(",") > -1 ? element.js.split(",")[0] : element.js;
                        var tmpPara1 = element.js.replace("RepBTN,", "").indexOf(",") > -1 ? element.js.replace("RepBTN,", "").split(",")[0] : element.js.replace("RepBTN,", "");
                        var tmpPara2 = element.js.replace("RepBTN,", "").indexOf(",") > -1 ? element.js.replace("RepBTN,", "").split(",")[1] : "";

                        if (tmpPara1 === "")
                            element_js += `${tmpFunc}(this); `;
                        else {
                            element_js += `${tmpFunc}(this, '${tmpPara1}', '${tmpPara2}'); `;
                        }
                    }
                    else
                        element_js += `${element.js}('${dataType}'); `;
                    if ((element.segmentation || element.item) === "暫存") { //MANTIS Case: 0025146 -> Bob 20180621
                        element_js += ` CheckAndSave('暫存');`;
                    }
                    element_js += `"`;
                    obj.append(`<input type="button" class="btn btn-primary" ${element_js} value="${element.segmentation || element.item}" />&nbsp;&nbsp;`);
                }
                else {
                    if ((element.segmentation || element.item) === "暫存") {
                        element_js = `onclick="CheckAndSave('暫存')"`;
                    }
                    else {
                        // 個案相關BTN eg. 基本資料、診斷、治療、復發...
                        $.each(json.data, function (idx, obj) {
                            if (idx === 0) tmpDataid = obj.dataid;
                            if (obj.datatype === dataType) {
                                tmpDoccode = obj.doccode;
                                docdetailItem = obj.docdetail;
                                return false;
                            }
                        });
                        // 儲存時，需設定"案件狀態"
                        $.each(docdetailItem, function (dtlId, dtlObj) {
                            if (dtlObj.ref &&
                                (dtlObj.ref.indexOf(element.segmentation) > -1 || dtlObj.ref.indexOf(element.item) > -1)) {
                                element_js = `onclick="setStatus('${dataType}'); ${dtlObj.api || dtlObj.js}('${tmpDataid}','${tmpDoccode}','${element.segmentation || element.item}');"`;
                                return false;
                            }
                        });
                    }
                    obj.append(`<button class="btn btn-primary" ${element_js}>${element.segmentation || element.item}</button>&nbsp;&nbsp;`);
                }
            }
        }
    }

    if (element.follow) {
        var emptyStr = "";
        if (element.api && element.api === 'getResultList') emptyStr = "&nbsp;&nbsp;&nbsp;";
        else if (element.segmentation && element.js) emptyStr = "&nbsp;&nbsp;&nbsp;";

        jQuery.each(element.follow, function (f, fobject) {
            if (element.segmentation && element.js) {
                // 增加BTN
                if (element.segmentation.indexOf("增加 ") === 0) {
                    //目前無程式碼
                }
                else obj.append(Json2HTML(fobject, parentname + ".follow[" + f + "]"));
            }
            else {
                obj.append(Json2HTML(fobject, parentname + ".follow[" + f + "]"));
            }
        });

        //console.log(obj.prop("outerHTML"));
        if (element.segmentation === "儲存" && element.method === "button") {
            $("div.btn_group_list_fixed").append(`<div>${obj.html()}<button class="btn btn-primary" onclick="GetQuery('${ajccType}', '${cluster}', '${masterSpec}')">返回查詢</button>&nbsp;&nbsp;</div>`);
            obj = $(`<div></div>`);
        }
    }

    return obj;//合併後刪除
}