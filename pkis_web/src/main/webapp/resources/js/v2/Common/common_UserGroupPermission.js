//使用者資料儲存
function fUDataSave(sUCode) {
    var sMsg = "", sEECode = "", sDEPName = "", sECName = "", sEJobTitleNo = "", sEJobTitle = "", sEONBDate = "", sELVDate = "", sEState = "", sELVDate1 = "", sELVDate2 = "";
    sEECode = $('input[name="txEECode"]').val().trim();//員工編號
    sDEPName = $('input[name="txDEPName"]').val().trim();//部門名稱(科別)
    sECName = $('input[name="txECName"]').val().trim();//中文姓名
    sEJobTitleNo = $('input[name="txEJobTitleNo"]').val().trim();//職稱序號
    sEJobTitle = $('input[name="txEJobTitle"]').val().trim();//職稱名稱
    sEONBDate = $('input[name="txEONBDate"]').val().trim();//到職日期
    sELVDate = $('input[name="txELVDate"]').val().trim();//離職日期
    //sEState = $('input[name="taEState"]:checked').val().trim();//是否在職
    sELVDate1 = $('input[name="txELVDate1"]').val().trim();//密碼
    sELVDate2 = $('input[name="txELVDate2"]').val().trim();//確認密碼

    if (sEECode === "") {
        sMsg += "[員工編號]為必填欄位!\r\n";
    } else {
        if (sEECode.toLowerCase !== "admin" &&
            new RegExp("^([A-Z]{1})([0-9]{6,6})$").exec(sEECode) === null)
                sMsg += "[員工編號]長度需7字元，第1字元需大寫英文，第2字元後為數字!\r\n";
    }

    if (sDEPName === "") sMsg += "[部門名稱(科別)]為必填欄位!\r\n";
    if (sECName === "") sMsg += "[中文姓名]為必填欄位!\r\n";
    if (sEJobTitleNo === "") sMsg += "[職稱序號]為必填欄位!\r\n";
    if (sEJobTitle === "") sMsg += "[職稱名稱]為必填欄位!\r\n";
    if (sEONBDate === "") {
        sMsg += "[到職日期]為必填欄位!\r\n";
    } else {
        sMsg += fDateValidationCheck("[到職日期]", sEONBDate);
    }
    //if (sEState === undefined) sMsg += "[是否在職]為必填欄位!\r\n";

    if ((sELVDate1 !== "") || (sELVDate2 !== "")) {
        sELVDate1 = $.md5(`${sEECode}${sELVDate1}`);
        sELVDate2 = $.md5(`${sEECode}${sELVDate2}`);
    }

    if (sUCode === "") {//新增
        if (sELVDate1 === "") sMsg += "[密碼]為必填欄位!\r\n";
        if (sELVDate1 !== sELVDate2) sMsg += "[密碼和確認密碼]不相同!\r\n";
    } else {//修改
        if (sELVDate === "") {
            //[MANTIS:0025160] 新增時，離職日非必填 ! 但編輯時，離職日為必填，請改一致為非必填 !
            //sMsg += "[離職日期]為必填欄位!\r\n";
        } else {
            sMsg += fDateValidationCheck("[離職日期]", sELVDate);
        }

        if (sELVDate1 !== "" || sELVDate2 !== "") {
            if (sELVDate1 !== sELVDate2) sMsg += "[密碼和確認密碼]不相同!\r\n";
        }
    }

    if (sMsg !== "") {
        FixSysMsg.danger(sMsg);
        return;
    }

    Spinner.show();
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "data": {
            "EIP_PWD": sELVDate1,
            "EIP_CODE": sEECode,
            "EIP_DEPT_NAME": sDEPName,
            "EIP_NAME": sECName,
            "EIP_JOB_NO": sEJobTitleNo,
            "EIP_JOB_TITLE": sEJobTitle,
            "EIP_OBDATE": sEONBDate
        }
    };

    //修改
    if (sUCode !== "") obj.data["USER_LEAVE"] = sELVDate;
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL(`UserGroupPermission/${sUCode === "" ? "AddNew" : "Update"}User`),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                FixSysMsg.success(`${sUCode === "" ? "新增" : "修改"}成功！`);
                window.location.replace("UserManagement");
            } else {
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        },
        complete: function (xhr, status) {
            Spinner.hide();
        }
    });
}

//編輯使用者資料
function fUDataEdit(sUCode) {
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "data": { "userEIPId": sUCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/UserList"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        async: false,
        success: function (response) {
            if (response.rtnCode === 0) {
                var result = JSON.parse(response.jsonData.data);
                console.log(result);

                if (result.length === 0) {
                    FixSysMsg.danger("查無此員工編號!");
                }
                else {
                    var sUserID = "", sUserName = "", sDeptName = "", sEJobTitleNo = "", sEJobTitle = "", sEONBDate = "", sELVDate = "";

                    $.each(result, function (idx, item) {
                        sDeptName = item["部門名稱(科別)"].trim();
                        sUserID = item["員工編號"].trim();
                        sUserName = item["中文姓名"].trim();
                        sEJobTitleNo = item["職稱序號"].trim();
                        sEJobTitle = item["職稱名稱"].trim();
                        sEONBDate = item["到職日期"].trim();
                        sELVDate = item["離職日期"].trim();

                        if (sUCode === sUserID) {
                            if (sUserID !== "") $(`input[name='txEECode']`).val(sUserID);//員工編號
                            if (sDeptName !== "") $(`input[name='txDEPName']`).val(sDeptName);//部門名稱(科別)
                            if (sUserName !== "") $(`input[name='txECName']`).val(sUserName);//中文姓名
                            if (sEJobTitleNo !== "") $(`input[name='txEJobTitleNo']`).val(sEJobTitleNo);//職稱序號
                            if (sEJobTitle !== "") $(`input[name='txEJobTitle']`).val(sEJobTitle);//職稱名稱
                            if (sEONBDate !== "") $(`input[name='txEONBDate']`).val(sEONBDate);//到職日期
                            if (sELVDate !== "") $(`input[name='txELVDate']`).val(sELVDate);//離職日期
                        }
                    });
                }
            } else {
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            }
        },
        error: function (xhr, status, errorThrown) {
            Spinner.hide();
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        }
    });
}

//查詢使用者資料
function fUserList(sUCode) {
    Spinner.show();
    var sHtml = "";
    var sEECode = $(`input[name='txEECode']`).val().trim();
    var sECName = $(`input[name='txECName']`).val().trim();

    var obj = {
        "token": getToken(),
        "userId": getUserId()
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/UserList"),
        data: obj,
        contentType: "application/json",
        type: "POST",
        dataType: 'json',
        success: function (response) {
            if (response.rtnCode === 0) {
                var result = JSON.parse(response.jsonData.data);
                console.log(result);

                $("#json_query tbody").html("");

                if (result.length === 0) {
                    $('#json_query > tbody:last-child').append(`<tr class="odd"><td valign="top" colspan="4" class="dataTables_empty"> 無資料 </td></tr>`);
                } else {
                    var sUserID = "", sUserName = "", sDeptName = "";
                    $.each(result, function (idx, item) {
                        sDeptName = item["部門名稱(科別)"].trim();
                        sUserID = item["員工編號"].trim();
                        sUserName = item["中文姓名"].trim();

                        sHtml = "";
                        if (sEECode === "" && sECName === "") {
                            sHtml = fUserListData(sUserID, sDeptName, sUserName);
                        } else {
                            if (sEECode !== "" && sECName === "") {
                                if (sUserID.indexOf(sEECode) !== -1) sHtml = fUserListData(sUserID, sDeptName, sUserName);
                            } else if (sEECode === "" && sECName !== "") {
                                if (sUserName.indexOf(sECName) !== -1) sHtml = fUserListData(sUserID, sDeptName, sUserName);
                            } else {
                                if (sUserID.indexOf(sEECode !== -1) && sUserName.indexOf(sECName !== -1)) sHtml = fUserListData(sUserID, sDeptName, sUserName);
                            }
                        }

                        $('#json_query > tbody:last-child').append(sHtml);
                    });
                }
            } else {
                $("#btADD").attr('disabled', 'disabled');
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
            }
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            FixSysMsg.danger("Ajax發生錯誤!");
        },
        complete: function (xhr, status) {
            Spinner.hide();
        }
    });
}

function fUserListData(sUserID, sDeptName, sUserName) {
    var sHtml = '<tr class="odd">';
    sHtml += `<td class="dataTables_empty"><input type="button" value="編輯" onclick="fUDataLink('${sUserID}')" /></td>`;
    sHtml += `<td class="dataTables_empty">${sUserID}</td>`;
    sHtml += `<td class="dataTables_empty">${sDeptName}</td>`;
    sHtml += `<td class="dataTables_empty">${sUserName}</td>`;
    sHtml += `</tr>`;

    return sHtml;
}

//判斷使用者輸入日期格式是否為 YYYY/MM/DD
function fDateValidationCheck(sTX, sStr) {
    var re = new RegExp("^([0-9]{4})[./]{1}([0-9]{1,2})[./]{1}([0-9]{1,2})$");
    var strDataValue;
    var infoValidation = true;

    if ((strDataValue = re.exec(sStr)) !== null) {
        var i;
        //年
        i = parseFloat(strDataValue[1]);
        if (i <= 0 || i > 9999) infoValidation = false;

        //月
        i = parseFloat(strDataValue[2]);
        if (i <= 0 || i > 12) infoValidation = false;

        //日
        i = parseFloat(strDataValue[3]);
        if (i <= 0 || i > 31) infoValidation = false;
    } else
        infoValidation = false;

    return infoValidation ? "" : sTX + "請輸入[YYYY/MM/DD]日期格式!\r\n";
}