var patternText = /^#(\d+(.[0-9]{1,2})?)(A|N|D|ID|TA|HID|DOCTORID|PATIENTID)/; //驗證使用

function NewTextBox(obj, itemType) {
    var tb = undefined;
    var sizeNum = obj.format && patternText.test(obj.format) ? obj.format.match(/(\d+(.[0-9]{1,2})?)/g)[0] : "0";
    if (sizeNum.indexOf(".") > -1) {
        sizeNum = (parseInt(sizeNum.split(".")[0]) + 1 + parseInt(sizeNum.split(".")[1])).toString();
    }

    if (itemType === "text" || itemType === "date") {
        var indent = "";
        var fmt = obj.format;
        var pattern = "";
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        tb = document.createElement("input");
        if (sizeNum > 0) {
            tb.setAttribute('maxlength', sizeNum); //html maxlength屬性
            if (parseFloat(sizeNum) > 40) sizeNum = 40;
			if (parseFloat(sizeNum) === 8) {
				sizeNum = 10;
				tb.setAttribute('maxlength', 10); 
			}
			
            tb.setAttribute('size', sizeNum*1.1); //html size屬性
        } else {
            tb.setAttribute('size', '10%');
        }

        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        tb.setAttribute('id', obj.id);
        tb.setAttribute('type', 'text');
        tb.setAttribute('col', lCol);
        tb.setAttribute('row', lRow);
        tb.setAttribute('autocomplete', 'off');
        //if (lCol > 0) tb.className = GetClassName(lCol, itemType);
        if (lCol > 0) tb.className = GenCss(lCol, itemType);
        tb.setAttribute('requirenewline', 0);
        //tb.setAttribute('shouldIndent', false);
        if (fmt === null || fmt === undefined || fmt === "") {
            //$('#'+pid).append("<input type='text' size='10%' />");
        }
        else {
            var charType = fmt.substr(fmt.length - 1, 1);
            var len = fmt.substr(1, fmt.length - 2);
            var vTitle = "";
            if (charType === 'A') pattern = '[\\w\\W]';
            else if (charType === 'N') pattern = '[0-9]';
            else if (charType === 'D' && len === 8) pattern = Ev8DReg;
            if (pattern !== "") {
                if (len.indexOf(".") === -1) pattern += "{1," + len + "}";
                else {
                    var tmpLen1 = len.split(".")[0];
                    var tmpLen2 = len.split(".")[1];
                    pattern += `{0,${parseInt(tmpLen1 - tmpLen2)}}`;
                    pattern += `([.][0-9]{${tmpLen2}})?`;
                }
                //var tb = document.createElement("input");
                tb.setAttribute("pattern", pattern);
                tb.setAttribute("maxLength", len);
				if(fmt === "#3.2N"){
					tb.setAttribute('maxlength', 6); 
				}
                if (charType === "A") { // && GetGearing_TextBox($(this))
                    //移除純文字格式的判斷(因"."無法輸入)
                    //tb.setAttribute("onkeypress", "return (isAlphaKey(event) || isNumberKey(event))");
                    //vTitle = "最長 " + len + " 個字元";
                }
                else if (charType === "N") {
                    //移除格式判斷(因"."無法輸入)
                    //tb.setAttribute("onkeypress", "return isNumberKey(event)");
                    //vTitle = "最長 " + len + " 個數字";
                }
                else if (charType === "D") {
                    //移除格式判斷(因"."無法輸入)
                    //tb.setAttribute("onkeypress", "return isNumberKey(event)");
                    //vTitle = "最長 " + len + " 個數字";
                }

                if (vTitle !== "") tb.setAttribute("title", vTitle);
            }
        }

        if (obj.value && obj.value !== "") {
            tb.setAttribute("value", obj.value);
        } else {
            var itemParentName = getItemParentName(obj);
            var itemParentVal = eval(`${itemParentName}.value`);
            if (itemParentVal && itemParentVal !== "") {
                tb.setAttribute("value", itemParentVal);
            }
        }

        //是否顯示不同UI
        var isShowDiffUIType = false;
        if (obj._js && obj._js !== "") {
            if (obj._js.match(/ShowHistologicGrade\(*\)/g)) {
                tb = ShowHistologicGrade(tb); isShowDiffUIType = true;
            } else if (obj._js.match(/ShowInvasion\(*\)/g)) {
                tb = ShowInvasion(tb); isShowDiffUIType = true;
            } else {
                tb = SetJsAttr(tb, obj._js);
            }
        }
        if (!isShowDiffUIType) {
            if (obj._parameters) tb.setAttribute("parameters", obj._parameters); //eg.身高、體重
            if (obj.id) tb.setAttribute("data-bind", `value:${obj.id}`);
        }

        //前面選項為"其他"時，才需顯示輸入框
        if (flag_SelectOtherOption === 1) {
            tb.setAttribute("disabled", true);
            flag_SelectOtherOption = 0;
        }
    }
    return tb;
}

//[治療] Histologic Grade: 若不能帶回時，則顯示選項〇G1、 〇G2、 〇G3、 〇不明
function ShowHistologicGrade(inElement) {
    var tmpDom = $(`<div ${get_element_css("radio", "div")}></div>`).addClass("col-md-9");
    var oriHtmlDom = $(inElement);
    var disabledAttr = oriHtmlDom.attr("disabled");

    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_G1" name="group_${inElement.id}" value="G1" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_G1">G1</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_G2" name="group_${inElement.id}" value="G2" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_G2">G2</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_G3" name="group_${inElement.id}" value="G3" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_G3">G3</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_NAN" name="group_${inElement.id}" value="不明" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_NAN">不明</label>`);

    tmpDom.append(oriHtmlDom);
    tmpDom.contents().removeClass();
    tmpDom.contents().find("input:text").hide(); //將原textbox隱藏
    //var rtnHtml = tmpDom[0].outerHTML;

    return tmpDom[0];
}

//[治療] Visceral Pleura Invasion:若不能帶回時，則顯示選項〇Not identified、 〇Present, PL1、 〇Present, PL2、 〇cannot be determined
function ShowInvasion(inElement) {
    var tmpDom = $(`<div ${get_element_css("radio", "div")}></div>`).addClass("col-md-9");
    var oriHtmlDom = $(inElement);
    var disabledAttr = oriHtmlDom.attr("disabled");

    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_NI" name="group_${inElement.id}" value="Not identified" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_NI">Not identified</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_PL1" name="group_${inElement.id}" value="Present, PL1" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_PL1">Present, PL1</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_PL2" name="group_${inElement.id}" value="Present, PL2" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_PL2">Present, PL2</label>`);
    oriHtmlDom.append(`<input type="radio" id="${inElement.id}_ND" name="group_${inElement.id}" value="cannot be determined" data-bind="checked:${inElement.id}" ${disabledAttr}><label style="padding-right:5%;" for="${inElement.id}_ND">cannot be determined</label>`);

    tmpDom.append(oriHtmlDom);
    tmpDom.contents().removeClass();
    tmpDom.contents().find("input:text").hide(); //將原textbox隱藏
    //var rtnHtml = tmpDom[0].outerHTML;

    return tmpDom[0];
}