function NewButton(obj, itemType) {
    var btn = undefined;

    if (itemType === "button" || obj.uitype === "btn") {
        var lCol = obj.col;
        var lRow = obj.row;
        var lFmt = obj.format;
        if (lFmt === undefined) lFmt = "";
        lCol = GetIndentCode(lCol);
        lRow = GetIndentCode(lRow);
        var id = obj.id;
        btn = document.createElement("input");
        //if (lCol > 0) btn.className = GetClassName(lCol, itemType);
        btn.className = GenCss(lCol, itemType);
        btn.setAttribute("col", lCol);
        btn.setAttribute('row', lRow);
        btn.setAttribute("type", "button");
        if (id !== undefined) btn.setAttribute("id", id);
        btn.setAttribute("displayname", obj.displayname);

        var requireNewLine = 0;
        if (lFmt === "#增加BTN" || lFmt === "#+BTN" || lFmt === "#展開BTN") {
            btn.className = btn.className.replace(/skh-btn/gi, "");

            if (lFmt === "#展開BTN") {
                btn.setAttribute("value", "+");
                btn.className += "btn-primary";
            }
            else if (lFmt === "#增加BTN" || lFmt === "#+BTN") {
                isHide1stAddBtnBlock = false;
                if (id.match(/button_.+_btn0_.+/g)) {
                    btn.setAttribute("value", "+");
                    btn.className += "btn-success";
                    if (addBtnSearchIdx === "") addBtnSearchIdx = rowIdx;

                    var tmpMaxBtnid = getAddButtonMaxId(id, dataIdx, rowIdx);
                    //若有增加過，則隱藏初始編輯區
                    if (tmpMaxBtnid.match(/button_.+_btn[1-9][\d]*_.+/g)) isHide1stAddBtnBlock = true;
                } else {
                    //加入AddButton區塊中移除按鈕的樣式
                    btn.setAttribute("value", "-");
                    btn.className += "btn-danger btn-removeadd";
                    //btn.style += ";position:absolute;right:60px;";
                }
            }
            requireNewLine = 1;
        } else {
            btn.setAttribute("value", obj.displayname);
        }
        btn.setAttribute('requirenewline', requireNewLine);
        //btn.setAttribute('shouldIndent', GetShouldIndent(obj.displayname));
        btn = setButtonJsAttr(btn, obj, lFmt);
    }
    return btn;
}

function setButtonJsAttr(btn, obj, lFmt) {
    var jsAttrStr = obj._js ? `${obj._js}` : "", tmpSaveJsPar = "";
    if (lFmt === "#增加BTN" || lFmt === "#+BTN") {
        var currMaxBlockid = obj.block;
        if (obj.id.match(/button_.+_btn0_.+/g)) {
            if (jsAttrStr !== "") jsAttrStr += `;onclickNewAddButtonBlock('${currMaxBlockid}', ${dataIdx}, ${rowIdx})`;
            else jsAttrStr += `onclickNewAddButtonBlock('${currMaxBlockid}', ${dataIdx}, ${rowIdx})`;
        }
        else {
            if (jsAttrStr !== "") jsAttrStr += `;onclickRemoveAddButtonBlock('${currMaxBlockid}', ${dataIdx})`;
            else jsAttrStr += `onclickRemoveAddButtonBlock('${currMaxBlockid}', ${dataIdx})`;
        }
    }
    else if (lFmt === "#展開BTN") {
        if (jsAttrStr !== "") jsAttrStr += `;onclickToggleBlock(this)`;
        else jsAttrStr += `onclickToggleBlock(this)`;
    }
    else if ("送出,暫存,作廢,駁回,審核,核准,預覽".indexOf(btn.value.replace(/[a-zA-Z]/g,"")) > -1) {
        //指定class
        btn.className = GenCss(0, "btn-submit");
        if("送出,審核,核准".indexOf(btn.value.replace(/[a-zA-Z]/g,"")) > -1){
//        	btn.className += " btn-outline-primary";
			btn.className += " btn-success";
        }else if("駁回".indexOf(btn.value.replace(/[a-zA-Z]/g,"")) > -1){
//        	btn.className += " btn-outline-danger";
			btn.className += " btn-danger";
        }else if("作廢".indexOf(btn.value.replace(/[a-zA-Z]/g,"")) > -1){
			btn.className += " btn-warning";
		}
        //預覽按鈕顏色
//        else if("預覽".indexOf(btn.value) > -1){
////        	btn.className += " btn-outline-primary";
//			btn.className += " btn-primary";
//        }
        else{
//			btn.className += " btn-outline-success";
        	btn.className += " btn-primary";
        }
        tmpSaveJsPar = btn.value;
        if(tmpSaveJsPar == "預覽")
            jsAttrStr += `;onclickPreview('${tmpSaveJsPar}')`;
        else
            jsAttrStr += `;onclickCheckAndSave('${tmpSaveJsPar}')`;

    }

    if (jsAttrStr !== "") {
        var urlBtnController = getJsonMappingElement("ref", lFmt, dataType);
        if (urlBtnController) {
            //儲存時，需設定"案件狀態"
            //jsAttrStr = `onclickSetStatus('${dataType}', 'localStorage.案件狀態');onclickLinkURL('${obj.displayname}')`;
        }

//        if (lFmt.replace(/#|BTN/gi, '') === $("#datatype").val())
//            btn.className += " active";
        
        btn = SetJsAttr(btn, jsAttrStr);
    }

    return btn;
}
