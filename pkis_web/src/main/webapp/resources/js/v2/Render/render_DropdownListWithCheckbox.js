function NewDropdownListWithCheckbox(obj, itemType) {
    var ddlc = undefined;

    if (itemType === "dropdownlist_with_checkbox") {

        ddlc = document.createElement("select");
        var opts = obj.options;
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        //if (lCol > 0) ddl.className = GetClassName(lCol, itemType);
        if (lCol > 0) ddlc.className = GenCss(lCol, itemType);
        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        ddlc.setAttribute('requirenewline', 0);
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
                ddlc.appendChild(opt);
            }
        });
        ddlc.setAttribute('col', lCol);
        ddlc.setAttribute('row', lRow);
        if (obj._js && obj._js !== "") {
            ddlc = SetJsAttr(ddlc, obj._js);
        }
        ddlc.setAttribute("id", obj.id);
        ddlc.setAttribute("class", "myselect_multiple");
        ddlc.setAttribute("multiple", "multiple");

        if (obj.id && lFmt) {
            var optionText = lFmt.replace(/#□|SELECT/g, "");
            var optionValue = lFmt.replace(/#□|SELECT/g, "");

            if (obj._api === undefined || obj._api === "") {
                optionText = "item";
                optionValue = "item";
            }
            ddlc.setAttribute("data-bind", `options:options${obj.id},value:${obj.id},optionsText:'${optionText}',optionsValue:'${optionValue}'`);
        }

        $(ddlc).css("width", "150px");
    }

    return ddlc;
}