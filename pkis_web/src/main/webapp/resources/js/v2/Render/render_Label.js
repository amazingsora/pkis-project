function NewLabel(obj, itemType) {
    var label = undefined;
    var isEnabled = false; //使之變成可編輯

    if (itemType === "label") {
        var id = obj.id;
        var lCol = obj.col;
        var lRow = obj.row;
        var display = "";
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        if (obj._ref && obj._ref.indexOf("enable") > -1) {
            isEnabled = true;
        }
        if (isEnabled) {
            label = document.createElement("input");
            label.setAttribute('type', "text");
        } else
            label = document.createElement("label");
        //if (lCol > 0) label.className = GetClassName(lCol, itemType);
        if (lCol > 0) label.className = GenCss(lCol, isEnabled ? "text" : itemType);
        
        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        if (id !== null || id !== undefined) {
            try {
                label.setAttribute('id', obj.id);
                label.setAttribute('col', lCol);
                label.setAttribute('row', lRow);
                label.setAttribute('requirenewline', 0);
                //label.setAttribute('shouldIndent', false);
                //label.innerHTML = obj.displayname; //GetFixedTextLen(obj.displayname);
                if (obj.displayname) {
                    if (obj.displayname.indexOf("診療計畫書未填寫.") > -1) {
                        label.setAttribute("data-bind", `text:${obj.id}`);
                    }
                    else if (obj.displayname.indexOf("PSYCHO.") > -1) {
                        label.setAttribute("data-bind", `text:${obj.id}`);
                    }
                    else if (obj.displayname.indexOf("SOURCE.") === -1) {
                        if (obj.displayname.match(/\:|\：/g)) {
                            display = getAddressedColon(obj.displayname);
                        } else {
                            if (obj.displayname.indexOf("FIX") === 0) display = obj.displayname.replace("FIX", "");
                            else display = obj.displayname;
                        }
                        label.innerHTML = display;

                        //若前面控制項displayname為空(eg. radio)，則將文字顯示於前面控制項的label for(for 畫面顯示需求)
                        if (hasPreItemEmptyDisplay) {
                            $(label).css("display", "none");
                            $(containerDiv).find("label[for]:last").text(display);
                            hasPreItemEmptyDisplay = false;
                        }
                    }
                    else {
                        if (obj.id) {
                            if (isEnabled) label.setAttribute("data-bind", `value:${obj.id}`);
                            else label.setAttribute("data-bind", `text:${obj.id}`);
                        }
                    }
                } else {
                    label.innerHTML = "";
                }
                //for [FIX Block]
                if (obj.fixblock && obj.fixblock.match(/label_.+_fix0_.+/g)) {
                    label.setAttribute("fixblock", obj.fixblock);
                    fixSearchIdx = rowIdx;
                }
                //label.setAttribute('shouldIndent', GetShouldIndent(obj.displayname));

                //for CSS
                if (obj._js) {
                    label = SetJsAttr(label, obj._js);
                }
            }
            catch (ex) { console.log(ex); }
        }
    }
    return label;
}

function NewBindingField(obj, itemType) {
    var bField = undefined;
    if (itemType === "field") {
        var id = obj.id;
        var lCol = obj.col;
        var lRow = obj.row;
        var display = "";
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        bField = document.createElement("label");
        if (lCol > 0) bField.className = GetClassName(lCol, itemType);
        if (id !== null || id !== undefined) {
            try {
                bField.setAttribute('id', obj.id);
                bField.setAttribute('col', lCol);
                bField.setAttribute('row', lRow);
                bField.setAttribute('requirenewline', 0);
                bField.setAttribute("data-bind", `text:${obj.id}`);
                if (obj.displayname) {
                    if (obj.displayname.match(/\:|\：/g)) {
                        display = getAddressedColon(obj.displayname);
                    } else {
                        if (obj.displayname.indexOf("FIX") === 0) display = obj.displayname.replace("FIX", "");
                        else display = obj.displayname;
                    }
                    bField.innerHTML = display;
                } else {
                    bField.innerHTML = "";
                }
                $(bField).css("display", "none");
            }
            catch (ex) { console.log(ex); }
        }
    }
    return bField;
}