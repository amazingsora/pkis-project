function parseHtmlPosi(tagName1, tagName2) {
    var divElem1 = document.body.getElementsByTagName(tagName1);
    for (var i = 0, divCnt1 = divElem1.length; i < divCnt1; i++) {
        var divElem2 = divElem1[i].childNodes;
        var curRow = "0";
        var latClsName = "";
        var colCnt = 0, divCnt2 = divElem2.length;
        for (var j = 0; divCnt2 > j; j++) {
            //console.log(`i:${i}, j:${j}, ${divElem1[i].childNodes[j].nodeName}`);
            if (divElem2[j].nodeName !== tagName2 || divElem2[j] === "undefined") {
                continue;
            }
            var colNum = divElem2[j].getAttribute("col");
            var rowNum = divElem2[j].getAttribute("row");
            if ((typeof (colNum) === "undefined") || (typeof (rowNum) === "undefined") ||
                (isNaN(colNum)) || (isNaN(rowNum))) {
                continue;
            }
            if (rowNum !== curRow) {
                //alert("rowNum="+rowNum);
                var colCnt = 0;
                //if (typeof(divElem2[j].id) === "undefined") {                    
                divElem2[j].id = "colId" + rowNum + "_" + colNum + "_temp";
                //}   
                var colId = divElem2[j].id;
                var grpCls = "grpCls" + rowNum + "_temp";
                var idSelector = "#" + colId;
                $(idSelector).addClass(grpCls);
                if (parseInt(colNum) > 0) {
                    for (var h = 0; h < colNum; h++) {
                        $(idSelector).before("<div class='col-1 " + grpCls + "'></div>");
                    }
                }
                curRow = rowNum;
            } else {
                //if (typeof(divElem2[j].id) === "undefined") {
                divElem2[j].id = "colId" + rowNum + "_" + colNum + "_temp";
                //}
                var colId = divElem2[j].id;
                var grpCls = "grpCls" + rowNum + "_temp";
                var idSelector = "#" + colId;
                $(idSelector).addClass(grpCls);
                //$(idSelector).before("<div class='col-1'></div>");                                                                
                curRow = rowNum;
            }
            var clsSelector = "." + grpCls;
            $(clsSelector).wrapAll("<div class='row'></div>");
        }
    }
}