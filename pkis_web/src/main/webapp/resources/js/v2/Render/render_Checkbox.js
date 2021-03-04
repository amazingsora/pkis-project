function NewCheckBox(obj, itemType) {
    var cb = undefined;

    if (itemType === "checkbox") {
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        cb = document.createElement("input");
        //if (lCol > 0) cb.className = GetClassName(lCol, itemType);
        if (lCol > 0) cb.className = GenCss(lCol, itemType);
        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        cb.setAttribute('col', lCol);
        cb.setAttribute('row', lRow);
        cb.setAttribute("type", "checkbox");
        cb.setAttribute("id", obj.id);
        cb.setAttribute("value", obj.value);
        cb.setAttribute('displayname', obj.displayname);
        cb.setAttribute('requirenewline', 1);
        //cb.setAttribute('shouldIndent', true);
        if (obj._js && obj._js !== "") {
            cb = SetJsAttr(cb, obj._js);
        }

        if (obj.id) cb.setAttribute("data-bind", `checked:${obj.id}`);
    }
    return cb;
}