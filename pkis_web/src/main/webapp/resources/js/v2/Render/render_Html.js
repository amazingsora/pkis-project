function NewHtml(obj, itemType) {
    var html = undefined;

    if (itemType === "html") {
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);

        switch (obj.displayname.replace(/\<|\>/g, "")) {
            case "br":
                html = document.createElement("div");
                html.setAttribute('col', lCol);
                html.setAttribute('row', lRow);
                html.setAttribute('requirenewline', 0);
                break;
            default:
                break;
        }
    }
    else if (itemType === "title")
        $('#cqs-title-h1').html(obj.data).show();

    return html;
}