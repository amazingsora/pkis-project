function render_addbutton(element, parentname, changeKey = null, disabledAttr, preImplementJs, hiddenAttr = "", addedParentName = "") {
    var tmpObj;//合併須刪除
    var obj = $(`<div></div>`);

    //按鈕控制項
    var element_api = "";
    var element_js = "";
    var tmpTitle = element.segmentation || element.item;
    var _html = "";

    tmpObj = getColumnName();

    if (element.js) {
        preImplementJs = "";
        var tmpJsArr = element.js.indexOf(",") > -1 ? element.js.split(",") : [element.js];
        $.each(tmpJsArr, function (i, jsVal) {
            // 需提前於render時執行的js eg.[診斷] ShowHistologicGrade
            preImplementJs += "," + jsVal;
        });
    }

    if (element.segmentation && element.segmentation.indexOf("FIX") === 0) {
        jQuery.each(element.follow, function (i, val) {
            var tmpHeadKey = Object.keys(val)[0];
            if (tmpHeadKey && tmpHeadKey.split("_").length === 3) {
                // 當KEY為"_序號_"的格式時，不render出HTML eg. [個管服務 or 追蹤] "_1_":[{...}]
                return false;
            }
            if (preImplementJs != "") {
                preImplementJs = preImplementJs.substr(1);
                _html = Json2HTML(val, parentname + ".follow[" + i + "]", null, null, preImplementJs);
            } else {
                _html = Json2HTML(val, parentname + ".follow[" + i + "]");
            }

            // 調整CSS版面：避免有的有包DIV、有的沒包
            var _tmpObj = $(`<div>${_html}</div>`);
            if (_tmpObj.find("div").length === 0) _html = `<div class="form-group row input-group">${_html}</div>`;
            obj.append(_html);
        });
    }
    else {
        var tmpRemoveBtnContent = `<button class="btn btn-danger" style="position:absolute;right:0;top:5px;" onclick="removeBtnFinite('${tmpTitle}', renderDom#blockid#)"><span class="glyphicon glyphicon-trash">-</span></button>`;
        var tmpContent = `<div class="panel-body" style="padding:10px; position:relative;">#follow##removeBtn#</div>`;
        var currFollows = [];
        var iOP = 0;//MANTIS Case: 0025221 -> Leo 20180712

        // 增加BTN(已存在key、value的block)
        Object.keys(element).forEach(function (keyVal, iKey) {
            if (keyVal.indexOf("follow_") === 0) {
                currFollows.push(keyVal);
            }
        });
        currFollows.forEach(function (keyVal, iKey) {
            //MANTIS Case: 0025221 -> Leo 20180712 S
            //addedParentName = `add${iKey}_`;
            addedParentName = `add${iOP}_`;
            iOP += 1;
            //MANTIS Case: 0025221 -> Leo 20180712 E

            jQuery.each(element[keyVal], function (i, val) {
                addBtnBlockArr.push({ "segmentation": tmpTitle, "blockid": keyVal });

                //MANTIS Case: 0025141 -> Leo 20180622 S
                //if (preImplementJs != "") {
                //    preImplementJs = preImplementJs.substr(1);
                //    obj.append(tmpContent.replace("#removeBtn#", tmpRemoveBtnContent).replace("#blockid#", `, '${keyVal}'`).replace("#follow#", Json2HTML(val, parentname + `.${keyVal}[${i}]`, null, null, preImplementJs)));
                //} else
                //    obj.append(tmpContent.replace("#removeBtn#", tmpRemoveBtnContent).replace("#blockid#", `, '${keyVal}'`).replace("#follow#", Json2HTML(val, parentname + `.${keyVal}[${i}]`)));

                if (i == 0) {
                    tmpRemoveBtnContent = `<button class="btn btn-danger" style="position:absolute;right:0;top:5px;" onclick="removeBtnFinite('${tmpTitle}', renderDom#blockid#)"><span class="glyphicon glyphicon-trash">-</span></button>`;
                } else {
                    tmpRemoveBtnContent = "";
                }

                if (preImplementJs != "") {
                    preImplementJs = preImplementJs.substr(1);
                    _html = Json2HTML(val, parentname + `.${keyVal}[${i}]`, null, null, preImplementJs, "", addedParentName).replace(/<div/g, `<div style="width:auto;" `);
                } else {
                    _html = Json2HTML(val, parentname + `.${keyVal}[${i}]`, null, null, "", "", addedParentName).replace(/<div/g, `<div style="width:auto;" `);
                }

                _html = tmpContent.replace("#removeBtn#", tmpRemoveBtnContent).replace("#blockid#", `, '${keyVal}'`).replace("#follow#", _html);
                var _tmpObj = $(_html);
                _tmpObj.find("label[class*=col-md-3]").toggleClass("col-md-3 col-md-2");
                obj.append(_tmpObj[0].outerHTML);
                //MANTIS Case: 0025141 -> Leo 20180622 E
            });
        });

        //MANTIS Case: 0025141 -> Leo 20180705 S
        // 增加BTN(空白block)  
        jQuery.each(element.follow, function (i, val) {
            //MANTIS Case: 0025221 -> Leo 20180712 S
            addedParentName = `add${iOP}_`;
            iOP += 1;
            //MANTIS Case: 0025221 -> Leo 20180712 E

            tmpRemoveBtnContent = `<button class="btn btn-success" style="position:absolute;right:0;top:5px;" onclick="addBtnFinite('${tmpTitle}', renderDom)"><span class="glyphicon glyphicon-trash">+</span></button>`;

            if (preImplementJs != "") {
                preImplementJs = preImplementJs.substr(1);
                _html = tmpContent.replace("#removeBtn#", "").replace("#blockid#", "").replace("#follow#", Json2HTML(val, parentname + ".follow[" + i + "]", null, null, preImplementJs));
            } else {
                if (i != 0) tmpRemoveBtnContent = "";

                //MANTIS Case: 0025221 -> Leo 20180712 S
                //_html = tmpContent.replace("#removeBtn#", tmpRemoveBtnContent).replace("#blockid#", "").replace("#follow#", Json2HTML(val, parentname + ".follow[" + i + "]"));
                _html = tmpContent.replace("#removeBtn#", tmpRemoveBtnContent).replace("#blockid#", "").replace("#follow#", Json2HTML(val, parentname + ".follow[" + i + "]", null, "", "", "", addedParentName));
                //MANTIS Case: 0025221 -> Leo 20180712 E

            }

            if (preImplementJs != "") {
                preImplementJs = preImplementJs.substr(1);
                _html = tmpContent.replace("#removeBtn#", tmpRemoveBtnContent).replace("#blockid#", "").replace("#follow#", Json2HTML(val, parentname + ".follow[" + i + "]", null, null, preImplementJs));
            }

            var _tmpObj = $(_html);
            _tmpObj.find("label[class*=col-md-3]").toggleClass("col-md-3 col-md-2");
            obj.append(_tmpObj[0].outerHTML);
        });
        //MANTIS Case: 0025141 -> Leo 20180705 E

    }

    html = obj.html();
    obj = $(`<div></div>`);
    //if (element.segmentation) obj.append(`<label>${element.segmentation}</label>`);
    //obj.append(`<blockquote>${html}</blockquote>`);
    if (element.segmentation || element.item) {
        if (tmpTitle.indexOf("FIX") === 0) {
            tmpTitle = tmpTitle.replace("FIX", "");
            obj.append(`<h3 class="panel-title">${tmpTitle}</h3>`);
            //obj.append(`<div class="form-group row">${html}</div>`);
            //Talas 20180606 調整頁面跑版未換行初步處理
            obj.append(`<blockquote>${html}</blockquote>`);
        }
        else {
            //var tmpAddBtnBlock = `<button data-toggle="collapse" href=".${tmpObj}_c1" class="btn btn-primary la_btn_pink"><span class="glyphicon glyphicon-option-vertical">${tmpTitle}</span></button>`;
            if ((tmpTitle == "ALK") || (tmpTitle == "EGFR")) tmpTitle = "";//MANTIS Case: 0025221 -> Leo 20180719
            var tmpAddBtnBlock = `<h3 class="panel-title"><span class="glyphicon glyphicon-option-vertical">${tmpTitle}</span></h3>`;

            //MANTIS Case: 0025141 -> Leo 20180629 S
            //var tmpAddBtnContent = `<button class="btn btn-success" onclick="addBtnFinite('${tmpTitle}', renderDom)"><span class="glyphicon glyphicon-plus">+</span></button><br/>`;
            var tmpAddBtnContent = "";
            //MANTIS Case: 0025141 -> Leo 20180629 E

            obj.append(`${tmpAddBtnBlock}`);
            obj.append(`<blockquote class="panel-collapse ${tmpObj}_c1" style="padding:10px;">${tmpAddBtnContent}${html}</blockquote>`);
        }
    }
    else {
        //obj.append(`<div class="form-group row">${html}</div>`);
        obj.append(html);
    }

    //obj.append(`<blockquote>${block.html().replace("#obj", html)}</blockquote>`);
    //arr1.push(`
    //    self.obj_${tmpObj} = ko.observableArray([{ test: '' }]);
    //    self.add_${tmpObj} = function () { self.obj_${tmpObj}.push({ test: "" }); };
    //    self.del_${tmpObj} = function () { self.obj_${tmpObj}.remove(this); };
    //`);

    return obj;//合併後刪除
}