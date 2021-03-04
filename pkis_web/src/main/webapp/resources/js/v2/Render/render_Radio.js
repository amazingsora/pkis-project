function NewRadioButton(obj, itemType) {
    var rdobtn = undefined;

    if (itemType === "radio") {
        rdobtn = document.createElement("input");
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        //if (lCol > 0) rdobtn.className = GetClassName(lCol, itemType);
        if (lCol > 0) rdobtn.className = GenCss(lCol, itemType);
        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        rdobtn.setAttribute("col", lCol);
        rdobtn.setAttribute('row', lRow);
        rdobtn.setAttribute("type", "radio");
        rdobtn.setAttribute("id", obj.id);
        if (obj.displayname !== "") rdobtn.setAttribute("value", obj.displayname);
        else rdobtn.setAttribute("value", "其他");
        rdobtn.setAttribute('requirenewline', 1);
        //rdobtn.setAttribute('shouldIndent', true);
        rdobtn.setAttribute("displayname", obj.displayname);

        //設定radio的群組name
        if (obj.data_id && nameGroupID !== `group_${obj.data_id}`) {
            nameGroupID = `group_${obj.data_id}`;
        }
        if (nameGroupID !== undefined) rdobtn.setAttribute('name', nameGroupID);

        if (obj._js && obj._js !== "") {
            rdobtn = SetJsAttr(rdobtn, obj._js);
        }
        if (obj.data_id !== undefined) rdobtn.setAttribute("data-bind", `checked:${obj.data_id}`);
    }
    return rdobtn;
}