function GenHtmlElements(processCode, obj, itemType) {
    var rtnElement = undefined;

    if (processCode === "GenHtmlElements") {
        if (rtnElement === undefined)
            if (typeof NewController === "function") rtnElement = NewController(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewLabel === "function") rtnElement = NewLabel(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewLi === "function") rtnElement = NewLi(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewBindingField === "function") rtnElement = NewBindingField(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewTextBox === "function") rtnElement = NewTextBox(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewDropdownList === "function") rtnElement = NewDropdownList(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewDropdownListWithCheckbox === "function") rtnElement = NewDropdownListWithCheckbox(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewCheckBox === "function") rtnElement = NewCheckBox(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewRadioButton === "function") rtnElement = NewRadioButton(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewButton === "function") rtnElement = NewButton(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewResultList === "function") rtnElement = NewResultList(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewTextarea === "function") rtnElement = NewTextarea(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewInputFile === "function") rtnElement = NewInputFile(obj, itemType);
        if (rtnElement === undefined)
            if (typeof NewHtml === "function") rtnElement = NewHtml(obj, itemType);
    }

    return rtnElement;
}