var patternText = /^#(\d+(.[0-9]{1,2})?)(A|N|D|ID|TA|HID|DOCTORID|PATIENTID)/; //驗證使用

function NewTextarea(obj, itemType) {
    var ta = undefined;
    var sizeNum = obj.format && patternText.test(obj.format) ? obj.format.match(/(\d+(.[0-9]{1,2})?)/g)[0] : "0";
    if (sizeNum.indexOf(".") > -1) sizeNum = (parseInt(sizeNum.split(".")[0]) + 1 + parseInt(sizeNum.split(".")[1])).toString();

    if (itemType === "textarray") {
        var indent = "";
        var fmt = obj.format;
        var pattern = "";
        var lCol = obj.col;
        var lRow = obj.row;
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        ta = document.createElement("textarea");
        if (sizeNum > 0) {
            ta.setAttribute('maxlength', sizeNum); //html maxlength屬性
            if (parseFloat(sizeNum) > 40) sizeNum = 40; //限制不超過40
            ta.setAttribute('cols', sizeNum); //html size屬性

            //$(ta).width(`${parseInt(document.body.clientWidth / (obj.col + 1))}px`);
        } else {
            ta.setAttribute('cols', '40');
        }

        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        ta.setAttribute('id', obj.id);
        ta.setAttribute('type', "text");
        ta.setAttribute('rows', 3);
        //[MANTIS:0025668]移除CSS
        //$(ta).css("overflow-y", "scroll");
        ta.setAttribute('col', lCol);
        ta.setAttribute('row', lRow);
        //if (lCol > 0) tb.className = GetClassName(lCol, itemType);
        if (lCol > 0) ta.className = GenCss(lCol, itemType);
        ta.setAttribute('requirenewline', 0);
        //tb.setAttribute('shouldIndent', false);
        if (fmt === null || fmt === undefined || fmt === "") {
            //$('#'+pid).append("<input type='text' size='10%' />");
        }
        else {
            var charType = fmt.substr(fmt.length - 1, 1);
            var len = fmt.match(/\d+(\.\d+)*/g)[0];
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
                ta.setAttribute("pattern", pattern);
                ta.setAttribute("maxLength", len);
                if (charType === "A") { // && GetGearing_TextBox($(this))
                    //ta.setAttribute("onkeypress", "return (isAlphaKey(event) || isNumberKey(event))");
                    //vTitle = "最長 " + len + " 個字元";
                }
                else if (charType === "N") {
                    //ta.setAttribute("onkeypress", "return isNumberKey(event)");
                    //vTitle = "最長 " + len + " 個數字";
                }
                else if (charType === "D") {
                    //ta.setAttribute("onkeypress", "return isNumberKey(event)");
                    //vTitle = "最長 " + len + " 個數字";
                }

                if (vTitle !== "") ta.setAttribute("title", vTitle);
            }

        }
        if (obj.value && obj.value !== "") {
            ta.setAttribute("value", obj.value);
        }
        if (obj._js && obj._js !== "") {
            ta = SetJsAttr(ta, obj._js);
        }
        if (obj.id) ta.setAttribute("data-bind", `value:${obj.id}`);

        //if (flag_SelectOtherOption == 1) {
        //    ta.setAttribute("disabled", true);
        //    flag_SelectOtherOption = 0;
        //}
    }
    return ta;
}
