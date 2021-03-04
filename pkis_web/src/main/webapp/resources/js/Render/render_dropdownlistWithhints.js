function render_dropdownlistWithhints(element, parentname, changeKey = null) {
    var obj;//合併須刪除

    //下拉可覆選,且有數量限制
    var noOption;
    var optionStr;
    var tmpObj = getColumnName();//合併須刪除var
    var option = "options" + tmpObj;
    obj = $("<div></div>");

    if (element.api) {
        //有api
        if (element.api === "onSelectHints" && element.parameters === "醫師姓名片語") {
            nowOption = getOptionStr(eval(`selectOptionsObj.${element.parameters}`), element.api, element.parameters, "姓名");

            var eventScriptStr;
            if (element.checkdtotal) {
                eventScriptStr = `                        
                        var nowSelfHintsLength = viewModel.${tmpObj}().length;
                        if(nowSelfHintsLength > ${element.checkdtotal}) { 
                            var removeNumber = nowSelfHintsLength - ${element.checkdtotal};
                            viewModel.${tmpObj}.splice(${element.checkdtotal},removeNumber);
                        }`;
            }

            if (!eventScriptStr) eventScriptStr = " ";
            var option = "options" + tmpObj;
            var select = `<br/><select data-bind="options: ${option}, selectedOptions: ${tmpObj}, optionsText: '姓名',optionsValue: '姓名' "  multiple="true" onclick="${eventScriptStr}" onchange="${eventScriptStr}" onkeyup="${eventScriptStr}"></select>`;
            var hints = `<br/>
                        <span data-bind="foreach: ${tmpObj}" >
                            <span class="select_hints"> 
                                <span data-bind="text: $index()+1"></span>.
                                <a class="select_hints_close" href="javascript:void(0);" data-bind="text: $data, attr: { title: '取消選擇'+$data }" onclick="viewModel.${tmpObj}.remove(this.text);"></a>
                            </span>
                            <span data-bind="if: ($index()+1)%${element.colnumber}=== 0"><br/></span>
                        </span>
                        `;
            obj.append(select);
            obj.append(hints);
            obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
            arr1.push(`self.${option} = ko.observableArray([${nowOption}]);`);
            //arr1.push("self." + tmpObj + " = ko.observableArray(items[" + rowIdx + "].data)");
            arr1.push(`self.${tmpObj} = ko.observable(${knockoutPath(parentname)}.data);`);
            arr2.push(parentname + ".data = viewModel." + tmpObj + "();");
        }
    }

    return obj;//合併須刪除
}