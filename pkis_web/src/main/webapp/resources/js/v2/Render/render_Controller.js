function NewController(obj, itemType) {
    var rtnItem = undefined, tmpfuncArr;
    if (itemType === "controller") {
        if (obj._ref && obj._ref === "FixMessage") { //置頂訊息
            var fixMsgHtml = "";
            //提供需長駐置頂顯示的訊息區塊
            if (obj._parameters) {
                var msgArr = obj._parameters.split(" ");
                if (msgArr.length > 0) {
                    msgArr.forEach(function (valMsg, iMsg) {
                        var tmpTitle = valMsg.indexOf(":") > -1 ? valMsg.split(":")[0] : valMsg;
                        var tmpValPar = valMsg.indexOf(":") > -1 ? valMsg.split(":")[1] : valMsg;
                        var tmpValue = "";
                        if (tmpValPar.match(/#.+\./g)) {
                            tmpValue = SetSourceData(tmpValPar.replace(/#.+\./g, ""), tmpValPar.split(".")[0].replace("#", ""));
                            if (tmpValPar.replace(/#.+\./g, "").trim() === "性別") {
                                tmpValue = tmpValue.replace(/\(.+\)/g, "");
                            }
                        }
                        if (tmpValue && tmpValue !== "") fixMsgHtml += `<div><b>${tmpTitle}：</b>${tmpValue}</div>`;
                    });
                }
            }
            if (fixMsgHtml !== "") {
//                $("#msgBlock1").css("background-color", "antiquewhite");
				$("#msgBlock1").css("background-color", "#ffa84c");
                $("#msgBlock1").html(fixMsgHtml);
            }
        }
        else if (obj._js && !obj._js.match(/(on[a-z]+[A-Z][a-z]*([A-Z]*[a-z]*)*)|ShowHistologicGrade|ShowInvasion|SetBeforeBaseDateIsDisabled/g)) {
            //個管才會使用到[切頁籤時，須判斷"初診斷日"來顯示AJCC]
            if (!sDiagnosisDate) {  //sDiagnosisDate === null
                tmpfuncArr = obj._js.indexOf(";") > -1 ? obj._js.split(";") : [obj._js];
                var tmpArr;
                for (var i = 0; i < tmpfuncArr.length; i++) {
                    tmpArr = tmpfuncArr[i];
                    if (tmpArr.match(/^GetAJCCDetail\(.+\)$/g))
                        arr1.push(`${tmpArr.replace(/\('.+'\);?/g, "")}($('#${tmpArr.match(/'.+'/g)[0].replace(/'/g, "")}')[0]);`);
                    else
                        arr1.push(`${tmpArr};`);
                }
            }
        }
        else if (obj._js && obj._js.match(/^SetBeforeBaseDateIsDisabled\(/)) {
            //設定[日期]須大於[基準日期]
            var tmpObjArr = TrimAllBlank(obj._js.match(/'.+'/g)[0].replace(/'/g, ""), "g").split(',');
            var _baseDate = tmpObjArr[0]; tmpObjArr.splice(0, 1);
            var _chkDate = tmpObjArr.toString().replace(/,/, "");
            tmpObjArr.forEach(function (item) {
                arr1.find(function (v, i) {
                    if (v.indexOf(`setDatePickerFormat('${item}'`) > -1) arr1[i] = v.replace(")", `, '${_baseDate}')`);
                    if (v.indexOf(`self.callFun_${item} = function (value) {`) > -1) arr1[i] = v.replace("};", `if (value) $("#${_baseDate}").removeAttr("onchange"); else if ($("#${_chkDate.replace(item, "")}").val() === "") { $("#${_baseDate}").attr("onchange", $("#${item}").attr("onchange")); var _baseFunc = $("#${_baseDate}").attr("onchange").replace(/this/g, "$('#${_baseDate}')[0]"); eval(_baseFunc); } };`);
                });
            });
        }
    }

    return rtnItem;
}