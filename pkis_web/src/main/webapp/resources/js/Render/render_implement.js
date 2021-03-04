
function render_implement(element, parentname, changeKey = null, disabledAttr, preImplementJs, hiddenAttr, addedParentName = "") {
    var tmpObj;//合併須刪除
    var obj = $(`<div></div>`);
    hiddenAttr = "";

    if (element.remark && element.remark === "hide") hiddenAttr = `hidden`;
    if (element.api) {
        tmpObj = changeKey ? changeKey : getColumnName();
        var nowOption, option = "options" + tmpObj;

        if (element.dropdownitem) {
            //有API並已先行呼叫(癌別、醫師姓名片語)API)
            if (element.api === "onSelectCodeList") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
            }
            else if (element.api === "onSelectHints") {
                nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);
            }

            //不用連動的上層值
            arr1.push(`self.${option} = ko.observableArray([${nowOption}]);`);
            //arr1.push("self." + tmpObj + " = ko.observable(items[" + rowIdx + "].data);");
        }
        else option = null;

        jQuery.each(element.implement, function (i, val) {
            tmp = $(`<div>${Json2HTML(val, parentname + ".implement[" + i + "]", option, disabledAttr, "", hiddenAttr, addedParentName)}</div>`);
            //col-md-9
            if (i !== 0 && tmp.find("div.form-group") !== undefined) {
                //obj.append(tmp.find("div.form-group").removeClass("form-group row").addClass("col-md-9"));
                obj.append(tmp.find("div.form-group").html());
            } else
                obj.append(tmp.html());

            //obj.append(Json2HTML(val, parentname + ".implement[" + i + "]", option));

            //移至render_dropdownlist內處理
            //if (val.method === "dropdownlist")
            //    arr2.push(parentname + ".implement[" + i + "].data = viewModel." + tmpObj + "();");
        });

        //html = obj.html();
        //obj = $(`<div></div>`);
        //obj.append(`<blockquote>${html}</blockquote>`);
        //obj.append(html);
        obj = $(`<div>${obj.html()}</div>`);
    }
    else {
        if (element.js) {
            preImplementJs = "";
            var tmpJsArr = element.js.indexOf(",") > -1 ? element.js.split(",") : [element.js];
            $.each(tmpJsArr, function (i, jsVal) {
                // 需提前於render時執行的js eg.[診斷] ShowHistologicGrade
                preImplementJs += "," + jsVal;
            });
        }
        jQuery.each(element.implement, function (i, val) {
            if (preImplementJs != "") {
                preImplementJs = preImplementJs.substr(1);
                obj.append(Json2HTML(val, parentname + ".implement[" + i + "]", null, disabledAttr, preImplementJs, hiddenAttr, addedParentName));
            } else
                obj.append(Json2HTML(val, parentname + ".implement[" + i + "]", null, disabledAttr, "", hiddenAttr, addedParentName));
        });
    }

    return obj;//合併後刪除
}