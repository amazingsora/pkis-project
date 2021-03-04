
var json = { "data": [{ "docdetail": [] }] };
var dataIdx = 0;
var rowIdx = 0;
var valueIdx = 0;
var arr1 = [];
var arr2 = [];
$(function () {
    //get json from web service
    getData("#divbody", "http://localhost:21283/json.html", renderDom);

    //hide all text when readio not checked
    $("input[type=radio]:not(:checked) + input[type=text]").hide();
    $("#sendBtn").click(function () {
        submit();
    });
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
            callback(dom, result, renderScript);
        }
    });
};

sendData = function (url) {
    $.ajax({
        url: url,
        type: "POST",
        //contentType: "application/json; charset=utf-8",
        //dataType: "json",
        data: 'str=' + JSON.stringify(json),
        //data: '{ str: \'' + JSON.stringify(json) + '}',
        error: function(xhr, status, errorThrown) {
            var err = "Status: " + status + " " + errorThrown;
            console.log(err);
        },
        success: function (response) {
            var result = JSON.parse(response);
            //json = result;
            alert('save success');
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
    var tmpObj;
    if (element.segmentation === "title") {
        obj = $("<header></header>");
        obj.append("<h1>" + element.data + "</h1>");
    }
    else if (element.method === "text") {
        obj = $("<div>");
        tmpObj = getColumnName();
        obj.append(' <input type="Text" name="" data-bind="value:' + tmpObj + '" />');

        arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".data);");
        arr2.push(parentname + ".data = viewModel." + tmpObj + "();");
    }
    else if (element.method === "radio") {
        obj = $("<div></div>");
        if (element.segmentation) {
            //第一層是radio
            obj.append('<label>' + element.segmentation + ':</label><br />');
            tmpObj = getColumnName();
            var block = $("<blockquote></blockquote>");
            jQuery.each(element.option, function (i, val) {
                block.append('<label><input type="radio" name="' + element.segmentation + '" value="' + val.item + '" data-bind="checked: ' + tmpObj + '">' + val.item + '</label>');
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
            });
            obj.append(block);

            arr1.push("self." + tmpObj + " = ko.observable(items[" + rowIdx + "].data);"); //self.column1 = ko.observable(items[1].data);
            arr2.push(parentname + ".data = viewModel." + tmpObj + "();"); //json.data[0].docdetail[1].data = viewModel.column1();
        }
        else {
            //非第一層的radio
            if (element.option[0].item) {
                //直向
                tmpObj = getColumnName();
                var block = $("<blockquote></blockquote>");
                jQuery.each(element.option, function (i, val) {
                    block.append('<label><input type="radio" name="' + element.segmentation + '" value="' + val.item + '" data-bind="checked: ' + tmpObj + '">' + val.item + '</label>');
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
                });
                obj.append(block);

                arr1.push("self." + tmpObj + " = ko.observable(items[" + rowIdx + "].data);"); //self.column1 = ko.observable(items[1].data);
                arr2.push(parentname + ".data = viewModel." + tmpObj + "();"); //json.data[0].docdetail[1].data = viewModel.column1();
            }
            else {
                //橫向
                obj.append('<label>' + element.item + ' </label>');
                tmpObj = getColumnName();
                jQuery.each(element.option[0].hitem, function (i, optionItem) {
                    obj.append('<label><input type="radio" name="' + parentname + element.item + '" value="' + optionItem + '" data-bind="checked: ' + tmpObj + '">' + optionItem + '</label> ');
                });

                arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".data);");
                arr2.push(parentname + ".data = viewModel." + tmpObj + "();");
            }
        }
    }
    else if (element.method === "checkbox") {
        if (element.segmentation) {
            //第一層是checkbox
            obj = $("<div></div>");
            obj.append('<label>' + element.segmentation + ':</label><br />');
            var block2 = $("<blockquote></blockquote>");
            jQuery.each(element.option, function (i1, val) {
                var tmpObj = getColumnName();
                block2.append('<label><input type="checkbox" name="" value="" data-bind="checked: ' + tmpObj + '">' + val.item + '</label><br />');
                arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".option[" + i1 + "].checked);");
                arr2.push(parentname + ".option[" + i1 + "].checked = viewModel." + tmpObj + "();");

                if (val.follow) {
                    var blockSec = $("<blockquote></blockquote>");;
                    jQuery.each(val.follow, function (f, fobject) {
                        if (f != 0) {
                            blockSec.append("<br />");
                        }
                        if (fobject.method === "radio") {
                            blockSec.append(Json2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]"));
                        }
                        else if (fobject.method === "checkbox") {
                            blockSec.append(Json2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]"));
                        }
                    });
                    block2.append(blockSec);
                }
                block2.append("<br />");
            });
            obj.append(block2);
        }
        else {
            //非第一層的checkbox
            obj = $("<div></div>");
            var block2 = $("<blockquote></blockquote>");
            if (element.option) {
                jQuery.each(element.option, function (i1, optionItem) {
                    var tmpObj = getColumnName();
                    obj.append('<label><input type="checkbox" value="' + optionItem.item + '" data-bind="checked: ' + tmpObj + '">' + optionItem.item + '</label>');
                    if (optionItem.follow) {
                        jQuery.each(optionItem.follow, function (f, fobject) {
                            if (fobject.method === "text") {
                                obj.append(Json2HTML(fobject, parentname + ".option[" + i1 + "].follow[" + f + "]"));
                            }
                        });
                    }
                    obj.append('<br>');
                    arr1.push("self." + tmpObj + " = ko.observable(" + knockoutPath(parentname) + ".option[" + i1 + "].checked);");
                    arr2.push(parentname + ".option[" + i1 + "].checked = viewModel." + tmpObj + "();");
                });
            }
        }
    }
    else {
        //textbox
        tmpObj = getColumnName();

        obj = $("<div></div>");
        obj.append('<label>' + element.segmentation + ':</label>');
        obj.append('<input type="Text" name="" data-bind="value:' + tmpObj + '" />');

        arr1.push("self." + tmpObj + " = ko.observable(items[" + rowIdx + "].data);");
        arr2.push(parentname + ".data = viewModel." + tmpObj + "();");
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

function renderScript() {
    renderScript1();
    renderScript2();
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


function renderScript2() {
    //alert('2');
    var scriptObj = $("<script><\/script>");
    scriptObj.append("function getvalue(){" + "\n");

    jQuery.each(arr2, function (i, val) {
        scriptObj.append(val + "\n");
    })

    console.log(JSON.stringify(json));

    scriptObj.append("}");
    $('body').append(scriptObj);
}

submit = function () {
    getvalue();
    //sendData("/Home/SaveData");
    sendData("http://localhost:21283/Home/SaveData");
}