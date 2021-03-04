function render_dropdownlistWithcheckbox(element, parentname, changeKey = null, disabledAttr = "") {
    var obj;//合併須刪除

    var nowOption = "", optionJson, optionText, optionValue;
    //下拉可覆選
    var tmpObj = getColumnName();//合併須刪除var
    var option = "options" + tmpObj;

    obj = $("<div></div>");
    if (element.segmentation) obj.append(`<label ${get_element_css("dropdown", "label")}>${getAddressedColon(element.segmentation)}</label>`);
    if (element.option) {
        //有下拉選項
        jQuery.each(element.option, function (i, val) {
            nowOption += `{'':'${val.item}'},`;
        });

        optionValue = optionText = "";
        arr1.push(`self.${option} = ko.observableArray([${nowOption.slice(0, -1)}]);`);
    }
    else {
        if (module === "CS" && ajccType === "QRY") {
            nowOption = []
            mappingTbName = element.js.replace(/on|get|set|do|go/, "");
            nowOptions = getJsonMappingElement("datatype", mappingTbName, mappingTbName, false);
            console.log(nowOptions);
            optionValue = optionText = element.segmentation;

            nowOptions.forEach(function (res, i) {
                nowOption.push(`{"${optionText}":"${res.Item}"}`);
            });

            arr1.push(`self.${option} = ko.observableArray([${nowOption}]);`);
        }
        else {
            nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, element.dropdownitem);

            optionJson = eval(`[${nowOption}]`);
            optionValue = getOptionKeySettings(optionJson[0], element.dropdownitem);
            optionText = getOptionKeySettings(optionJson[0], element.dropdownitem);

            arr1.push(`self.${option} = ko.observableArray([${nowOption}]);`);

            if (module === "CM" && cluster === "CEM" && ajccType === "QRY" && masterSpec === "DIVIDEDOCTOR")
                optionValue = optionText = "癌別代碼(癌別中文)";
        }
    }

    arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
    arr2.push(`${knockoutPath(parentname)}.data = $('#${element.id}').val().join(',');`);

    obj.append(`<select id="${element.id}" name="${tmpObj}" class="myselect_multiple" multiple="multiple" data-bind="options:${option},value:${tmpObj},optionsText:'${optionText}',optionsValue:'${optionValue}'"></select>`);
    obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);

    return obj;//合併須刪除
}