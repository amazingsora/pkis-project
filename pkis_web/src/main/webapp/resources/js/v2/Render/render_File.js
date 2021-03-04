function NewInputFile(obj, itemType) {
    var iFl = undefined, iForm = undefined;
    var sizeNum = 40;

    if (itemType === "file") {
        var indent = "";
        var fmt = obj.format;
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        iFl = document.createElement("input");
        iFl.setAttribute('size', sizeNum); //html size屬性

        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        iFl.setAttribute('id', obj.id);
        iFl.setAttribute('name', 'fUplFile');
        iFl.setAttribute('type', "file");
        iFl.setAttribute('col', lCol);
        iFl.setAttribute('row', lRow);
        //if (lCol > 0) tb.className = GetClassName(lCol, itemType);
        if (lCol > 0) iFl.className = GenCss(lCol, itemType);
        iFl.setAttribute('requirenewline', 0);
        //tb.setAttribute('shouldIndent', false);
        iFl.setAttribute('multiple', 'multiple');
        
        if (obj.value && obj.value !== "") {
            iFl.setAttribute("value", obj.value);
            iFl.setAttribute("title", obj.value);
        } else {
            var itemParentName = getItemParentName(obj);
            var itemParentVal = eval(`${itemParentName}.value`);
            if (itemParentVal && itemParentVal !== "") {
                iFl.setAttribute("value", itemParentVal);
                iFl.setAttribute("title", itemParentVal);
            }
        }

        if (obj._parameters) iFl.setAttribute("parameters", obj._parameters); //eg.身高、體重
        if (obj.id) iFl.setAttribute("data-bind", `value:${obj.id}`);

        //外包form元素
        iForm = document.createElement("form");
        iForm.setAttribute("id", `form_${obj.id}`);
        iForm.setAttribute("method", "post");
        iForm.setAttribute("enctype", "multipart/form-data");
        iForm.setAttribute('col', lCol);
        iForm.setAttribute('row', lRow);
        iForm.setAttribute('requirenewline', 0);
        iForm.appendChild(iFl);
    }
    return iForm;
}