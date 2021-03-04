function render_child(element, parentname, changeKey = null, disabledAttr = "", preImplementJs = "") {
    var obj = $("<div></div>");//合併後刪除var
    var block = $("<blockquote></blockquote>");
    jQuery.each(element.child, function (c, val) {
        //block.append(`<blockquote>${Json2HTML(val, parentname + ".child[" + c + "]")}</blockquote>`);
        if (preImplementJs !== "")
            block.append(Json2HTML(val, `${parentname}.child[${c}]`, null, disabledAttr, preImplementJs));
        else 
            block.append(Json2HTML(val, `${parentname}.child[${c}]`));
    });
    //obj.append(`<label>${element.segmentation ? element.segmentation : element.item}</label>`);

    if (element.segmentation)
        obj.append(`<h3 class="panel-title">${element.segmentation}</h3>`);
    else
        obj.append(`<label ${get_element_css("child", "label")}>${getAddressedColon(element.item)}</label>`);


    //obj.append(block);
    //obj.append(block.html());

    if (element.child.length > 0 && element.child[0].method === "dropdownlist") {
        obj.append(`<div ${get_element_css()}>${block.html()}</div>`);
        obj = $(`<div><div class="form-group row">${obj.html()}</div></div>`);
    } else
        obj.append(block.html());

    return obj;//合併後刪除
}