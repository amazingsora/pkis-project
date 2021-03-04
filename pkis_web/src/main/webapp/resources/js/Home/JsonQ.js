
var json = { "data": [{ "docdetail": [] }] };
var dataIdx = 0;
var rowIdx = 0;
var valueIdx = 0;

var arr1 = []; //delete
var arr2 = []; //delete

$(function () {
    //get json from web service
    getData("#divbody", "http://localhost:21283/jsonQ.html", renderDom);
});

getData = function (dom, url, callback) {
    $.ajax({
        url: url,
        type: 'GET',
        data: {
            //user_name: $('#user_name').val()
        },
        error: function (xhr) {
            alert('Ajax request 發生錯誤');
        },
        success: function (response) {
            var result = JSON.parse(response);
            json = result;
            callback(dom, result, renderScript1);
        }
    });
};

renderDom = function (dom, result, callback2) {
    var body = $(dom);
    //body.append("<header><h1>" + result.data[0].docname + "</h1></header><hr>");

    jQuery.each(result.data[dataIdx].docdetail, function (idx, val) {
        rowIdx = idx;
        body.append(Json2HTML(val, "json.data[" + dataIdx + "].docdetail[" + rowIdx + "]") + "<hr>");

        if (idx === result.data[0].docdetail.length - 1)
            callback2();
    });
}


Json2HTML = function (element, parentname) {
    var html = "";
    var obj;
    var tmpObj; //delete
    if (element.segmentation === "title") {
        obj = $("<header></header>");
        obj.append("<h1>" + element.data + "</h1>");
    }
    else if (element.method === "text") {
        obj = $("<div>");
        tmpObj = getColumnName();
        obj.append(' <span data-bind="text:' + tmpObj + '" ></span>');
        arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".data);");
    }
    else if (element.method === "radio") {
        obj = $("<div></div>");
        if (element.segmentation) {
            obj.append('<label>' + element.segmentation + ':</label><br />');
            tmpObj = getColumnName();
            var block = $("<blockquote></blockquote>");
            jQuery.each(element.option, function (i, val) {
                if (val.item == element.data) {
                    block.append('<span data-bind="text: ' + tmpObj + '">' + val.item + '</span>');
                    if (val.follow) {
                        jQuery.each(val.follow, function (f, fobject) {
                            if (fobject.method === "text") {
                                block.append(Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]"));
                            }
                            else {
                                block.append("<blockquote>" + Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]") + "</blockquote>");
                            }
                        });
                    }
                    block.append("<br />");
                }
            });
            obj.append(block);

            arr1.push("self." + tmpObj + " = ko.observable(items[" + rowIdx + "].data);");
        }
        else {
            if (element.option[0].item) {
                //直向
                tmpObj = getColumnName();
                var block = $("<blockquote></blockquote>");
                jQuery.each(element.option, function (i, val) {
                    if (val.item == element.data) {
                        block.append('<span data-bind="text: ' + tmpObj + '">' + val.item + '</span>');
                        if (val.follow) {
                            jQuery.each(val.follow, function (f, fobject) {
                                if (fobject.method === "text") {
                                    block.append(Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]"));
                                }
                                else {
                                    block.append("<blockquote>" + Json2HTML(fobject, parentname + ".option[" + i + "].follow[" + f + "]") + "</blockquote>");
                                }
                            });
                        }
                        block.append("<br />");
                    }
                });
                obj.append(block);

                arr1.push("self." + tmpObj + " = ko.observable(items[" + rowIdx + "].data);");
            }
            else {
                //橫向
                //if (element.data) {
                obj.append('<label>' + element.item + ' </label>');
                tmpObj = getColumnName();
                obj.append('<span data-bind="text: ' + tmpObj + '"></span> ');
                arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".data);");
                //}
            }
        }
    }
    else if (element.method === "checkbox") {
        if (element.segmentation) {
            obj = $("<div></div>");
            obj.append('<label>' + element.segmentation + ':</label><br />');
            if (element.option) {
                jQuery.each(element.option, function (i1, optionItem) {
                    if (optionItem.checked) {
                        var tmpObj = getColumnName();
                        obj.append('<span data-bind="text:' + tmpObj + '"></span>');
                        if (optionItem.follow) {
                            jQuery.each(optionItem.follow, function (f, fobject) {
                                if (fobject.method === "radio") {
                                    obj.append("<blockquote>" + Json2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]") + "</blockquote>");
                                }
                                else if (fobject.method === "checkbox") {
                                    obj.append("<blockquote>" + Json2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]") + "</blockquote>");
                                }
                            });

                            //if (optionItem.follow.method === "text") {
                            //    obj.append(Json2HTML(optionItem.follow, parentname + ".option[" + i1 + "].follow"));
                            //}
                        }
                        obj.append('<br>');
                        arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".option[" + i1 + "].item);");
                    }
                });
            }
        }
        else {
            obj = $("<div></div>");
            if (element.option) {
                jQuery.each(element.option, function (i1, optionItem) {
                    if (optionItem.checked) {
                        var tmpObj = getColumnName();
                        obj.append('<span data-bind="text:' + tmpObj + '"></span>');
                        if (optionItem.follow) {
                            jQuery.each(optionItem.follow, function (f, fobject) {
                                jQuery.each(optionItem.follow, function (f, fobject) {
                                    if (fobject.method === "text") {
                                        obj.append(Json2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]"));
                                    }
                                });
                            });
                        }
                        obj.append('<br>');
                        arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".option[" + i1 + "].item);");
                    }
                });
            }
        }
    }
    else {
        //textbox
        tmpObj = getColumnName();

        obj = $("<div></div>");
        obj.append('<label>' + element.segmentation + ':</label>');
        obj.append(' <span data-bind="text:' + tmpObj + '"></span>');

        arr1.push("self." + tmpObj + " = ko.observable(items[" + rowIdx + "].data);");
    }

    html = obj.html();
    return html;
}

function getColumnName() {
    valueIdx++;
    return "column" + valueIdx;
}

function knockoutPath(parentname) {
    var kpath = "";
    var parr = parentname.split(".");
    kpath = parr[2].replace("docdetail", "items");
    for (var i = 3; i < parr.length; i++) {
        kpath = kpath + "." + parr[i];
    }
    return kpath;
}

function renderScript1() {
    //alert('111');

    var scriptObj = $("<script><\/script>");
    scriptObj.append("var SimpleListModel = function (items) {" + "\n");
    scriptObj.append("var self = this;" + "\n");
    scriptObj.append("self.items = ko.observableArray(items);" + "\n");

    jQuery.each(arr1, function (i, val) {
        scriptObj.append(val + "\n");
    })

    scriptObj.append("};" + "\n");
    scriptObj.append("var viewModel = new SimpleListModel(json.data[" + dataIdx + "].docdetail);" + "\n");

    scriptObj.append("ko.applyBindings(viewModel);" + "\n");

    $('body').append(scriptObj);
}