/**
 * 回前頁
 * @param {any} url URL位址
 */
function GoPrevUrl(url) {
    window.location.replace(url);
}


/**
 * 取得群組清單
 * @param {any} gCode 群組代碼
 * @returns {any}     回傳Deferred物件
 */
function GetGroupList(gCode) {
    var $gp = $.Deferred();

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG01",
        "data": { "dataid": gCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Permission/GroupList"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            $gp.resolve(response);
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            $gp.resolve({ "rtnCode": 500, "message": "取得群組清單發生錯誤!", "jsonData": {} });
        }
    });

    return $gp;
}


/**
 * 群組查詢
 * @param {any} $obj 群組名稱的HTML Object
 */
function GroupQuery($obj) {
    Spinner.show();

    var $gp = GetGroupList($('#GroupList').val());
    $.when($gp).done(function (_gp) {
        if (_gp.rtnCode === 0) {
            var result = JSON.parse(_gp.jsonData.data);
            console.log(result);

            if (result.length > 0) {
                //選取=>□選取
                result.forEach(function (item) {
                    console.log(`${item.群組代碼}=>${item.群組名稱}`);
                });

                var oTable = $obj.dataTable();
                oTable.fnClearTable();
                oTable.fnAddData(result);
            }
        } else
            FixSysMsg.danger(`${response.rtnCode}-${response.message}`);

        Spinner.hide();
    });
}


/**
 * 群組新增
  */
function AddGroup() {
    var _gCode = $('input[name="txtGroupCode"]').val().trim(),
        _gName = $('input[name="txtGroupName"]').val().trim();

    if (_gCode === "")
        FixSysMsg.danger("[群組代碼]不可為空白！");
    else if (_gName === "")
        FixSysMsg.danger("[群組名稱]不可為空白！");
    else if (confirm("是否新增群組?")) {
        Spinner.show();

        var obj = {
            "token": getToken(),
            "userId": getUserId(),
            "accessCode": "PG02",
            "data": {
                "dataid": _gCode,
                "doccode": _gName
            }
        };
        obj = JSON.stringify(obj);
        console.log(obj);

        $.ajax({
            url: getAPIURL("Permission/AddGroup"),
            type: "POST",
            contentType: "application/json",
            data: obj,
            success: function (response) {
                if (response.rtnCode === 0) {
                    FixSysMsg.success("新增成功！");
                    GroupQuery();
                } else
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
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
}


/**
 * 群組刪除
 * @param {any} gCode 群組代碼
 * @returns {any}     回傳空值/NULL
 */
function DeleteGroup(gCode) {
    if (!confirm("是否刪除群組?")) return "";

    Spinner.show();
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG03",
        "data": { "dataid": gCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Permission/DeleteGroup"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                FixSysMsg.success("刪除成功！");
                GroupQuery();
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


/**
 * 取得群組名稱
 * @param {any} gCode 群組代碼
 * @param {any} $obj  群組名稱的HTML Object
 */
function GetGroupName(gCode, $obj) {
    Spinner.show();

    var $gp = GetGroupList();
    $.when($gp).done(function (_gp) {
        if (_gp.rtnCode === 0) {
            var result = JSON.parse(_gp.jsonData.data);
            console.log(result);

            var val = result.find(function (item) { return gCode === item.群組代碼; });
            if (val) $obj.val(val.群組名稱);
        } else
            FixSysMsg.danger(`${_gp.rtnCode}-${_gp.message}`);

        Spinner.hide();
    });
}


/**
 * 群組權限列表
 * @param {any} gCode 群組代碼
 * @param {any} $obj  要產出的jquery dataTable object
 * @returns {any} HTML
 */
function GroupPermitQuery(gCode, $obj) {
    if (gCode === "") {
        FixSysMsg.danger("發生錯誤");
        return "";
    }

    Spinner.show();
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG04",
        "data": { "dataid": gCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Permission/GroupPermissionList"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                var result = JSON.parse(response.jsonData.data);
                console.log(result);

                if (result.length > 0) {
                    $("#txGPowerNO").val(result.length);

                    //權限=>□管理、□黑名單、□新增、□讀取、□更新、□刪除
                    result.forEach(function (item) {
                        console.log(`${item.模組}:${item.module.trim()},${item.集合}:${item.cluster.trim()}=>${item.權限}`);
                    });

                    var oTable = $obj.dataTable();
                    oTable.fnClearTable();
                    oTable.fnAddData(result);
                }
            } else
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
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


/**
 * 群組權限儲存
 * @param {any} gCode 群組代碼
 */
function SaveGroupPermission(gCode) {
    Spinner.show();
    var _module = "", _cluster = "", _permissions = "", PermArr = [];

    for (var idx = 0; idx < $("#txGPowerNO").val(); idx++) {
        _module = $(`#module${idx}`).val();
        _cluster = $(`#cluster${idx}`).val();
        _permissions = "";

        if ($(`input[type=checkbox][id=${_module}_${_cluster}_S]`)[0].checked) _permissions += "S";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_B]`)[0].checked) _permissions += "B";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_C]`)[0].checked) _permissions += "C";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_R]`)[0].checked) _permissions += "R";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_U]`)[0].checked) _permissions += "U";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_D]`)[0].checked) _permissions += "D";

        PermArr.push({ "module": _module, "cluster": _cluster, "permissions": _permissions });
    }

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG05",
        "data": {
            "dataid": gCode,
            "docdetail": PermArr
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Permission/SaveGroupPermission"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0)
                FixSysMsg.success("儲存成功！");
            else
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
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


/**
 * 取得群組人員清單
 * @param {any} gCode 群組代碼
 * @returns {any}     回傳Deferred物件
 */
function GetGroupUserList(gCode) {
    var $gp = $.Deferred();

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG06",
        "data": { "dataid": gCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Permission/GroupUserList"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            $gp.resolve(response);
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            $gp.resolve({ "rtnCode": 500, "message": "取得群組人員清單發生錯誤!", "jsonData": {} });
        }
    });

    return $gp;
}


/**
 * 群組查詢
 * @param {any} gCode 群組代碼
 * @param {any} $obj  要產出的jquery dataTable object
 */
function GroupUserQuery(gCode, $obj) {
    Spinner.show();

    var $gp = GetGroupUserList(gCode);
    $.when($gp).done(function (_gp) {
        if (_gp.rtnCode === 0) {
            var result = JSON.parse(_gp.jsonData.data);
            console.log(result);

            if (result.length > 0) {
                //選取=>□選取
                result.forEach(function (item) {
                    console.log(`${item.員編},${item.姓名},${item["部門(科別)"]},${item.職務}=>${item.選取}`);
                });

                var oTable = $obj.dataTable();
                oTable.fnClearTable();
                oTable.fnAddData(result);
            }
        } else
            FixSysMsg.danger(`${_gp.rtnCode}-${_gp.message}`);

        Spinner.hide();
    });
}


/**
 * 群組權限儲存
 * @param {any} gCode 群組代碼
 */
function SaveGroupUserPermission(gCode) {
    Spinner.show();
    var PermArr = [];

    $('input[type=checkbox][name=gpULP]').each(function () {
        PermArr.push({ "userId": this.value, "checked": this.checked });
    });

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG07",
        "data": {
            "dataid": gCode,
            "docdetail": PermArr
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Permission/SaveGroupUserPermission"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0)
                FixSysMsg.success("儲存成功！");
            else
                FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
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


/**
 * 取得使用者清單
 * @param {any} _uCode 員編
 * @param {any} _uName 中文姓名
 * @returns {any}      回傳Deferred物件
 */
function GetUserList(_uCode, _uName) {
    var $up = $.Deferred();

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PU01",
        "data": {
            "dataid": _uCode,
            "doccode": _uName
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("Permission/UserList"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            $up.resolve(response);
        },
        error: function (xhr, status, errorThrown) {
            console.log(`Status:${status} ${errorThrown}`);
            $up.resolve({ "rtnCode": 500, "message": "取得群組清單發生錯誤!", "jsonData": {} });
        }
    });

    return $up;
}


/**
 * 使用者列表
 * @param {any} $obj 要產出的jquery dataTable object
 */
function UserQuery($obj) {
    Spinner.show();

    var _uCode = $("#txtEECode").val().trim(),
        _uName = $("#txtEEName").val().trim();

    var $up = GetUserList(_uCode, _uName);
    $.when($up).done(function (_gp) {
        if (_gp.rtnCode === 0) {
            var result = JSON.parse(_gp.jsonData.data);
            console.log(result);

            if (result.length > 0) {
                //選取=>□選取
                result.forEach(function (item) {
                    console.log(`${item.員工編號},${item.中文姓名},${item.職稱名稱}=>${item.是否在職}`);
                });

                var oTable = $obj.dataTable();
                oTable.fnClearTable();
                oTable.fnAddData(result);
            }
        } else
            FixSysMsg.danger(`${response.rtnCode}-${response.message}`);

        Spinner.hide();
    });
}


/**
 * 取得使用者
 * @param {any} _uCode 員編
 */
function UDataQuery(_uCode) {
    Spinner.show();

    var $up = GetUserList(_uCode, "");
    $.when($up).done(function (_gp) {
        if (_gp.rtnCode === 0) {
            var result = JSON.parse(_gp.jsonData.data);
            console.log(result);

            if (result.length > 0) {
                var item = result[0];
                $("#txtEECode").val(item.員工編號);
                $("#txtDEPName").val(item["部門名稱(科別)"]);
                $("#txtECName").val(item.中文姓名);
                $("#txtEJobTitleNo").val(item.職稱序號);
                $("#txtEJobTitle").val(item.職稱名稱);
                $(`input[name="rdoEESex"][value="${item.性別}"]`).attr("checked", true);
                $("#txtEONBDate").val(item.到職日期);
                if (item.離職日期 !== "") $("#txtELVDate").val(item.離職日期);
                $(`input[name="rdoEEState"][value="${item.是否在職}"]`).attr("checked", true);
            }
        } else
            FixSysMsg.danger(`${response.rtnCode}-${response.message}`);

        Spinner.hide();
    });
}


/**
 * 使用者資料儲存
 * @param {any} _uCode 員編
 */
function SaveUser(_uCode) {
    var sMsg = "", _Code = "", _DEPName = "", _ECName = "", _JobTitleNo = "", _JobTitle = "";
    var _Sex = "", _OBDate = "", _LVDate = "", _State = "", _Pwd = "", _Pwd2 = "";
    _Code = $("#txtEECode").val().trim();                        //員工編號
    _DEPName = $("#txtDEPName").val().trim();                    //部門名稱(科別)
    _ECName = $("#txtECName").val().trim();                      //中文姓名
    _JobTitleNo = $("#txtEJobTitleNo").val().trim();             //職稱序號
    _JobTitle = $("#txtEJobTitle").val().trim();                 //職稱名稱
    _Sex = $("input[name='rdoEESex']:checked").val();            //性別
    _OBDate = $("#txtEONBDate").val().trim();                    //到職日期
    _LVDate = $("#txtELVDate").val().trim();                     //離職日期
    _State = $("input[name='rdoEEState']:checked").val().trim(); //是否在職
    _Pwd = $("#txtEEPwd").val().trim();                          //密碼
    _Pwd2 = $("#txtEEPwd2").val().trim();                        //確認密碼

    if (_Code === "") {
        sMsg += "[員工編號]為必填欄位!\r\n";
    } else {
        if (_Code.toLowerCase !== "admin" && !_Code.match(/^[A-Z][0-9]{6}$/))
            sMsg += "[員工編號]長度需7字元，第1字元需大寫英文，第2字元後為數字!\r\n";
    }

    if (_DEPName === "") sMsg += "[部門名稱(科別)]為必填欄位!\r\n";
    if (_ECName === "") sMsg += "[中文姓名]為必填欄位!\r\n";
    if (_JobTitleNo === "") sMsg += "[職稱序號]為必填欄位!\r\n";
    if (_JobTitle === "") sMsg += "[職稱名稱]為必填欄位!\r\n";
    //if (_Sex === "") sMsg += "[性別]為必填欄位!\r\n";
    if (_Sex === undefined) _Sex = "";
    if (_OBDate === "") sMsg += "[到職日期]為必填欄位!\r\n";
    if (_State === undefined) sMsg += "[是否在職]為必填欄位!\r\n";

    if (_Pwd !== "" || _Pwd2 !== "") {
        _Pwd = $.md5(`${_Code}${_Pwd}`);
        _Pwd2 = $.md5(`${_Code}${_Pwd2}`);
    }

    if (_uCode === "") {//新增
        if (_Pwd === "") sMsg += "[密碼]為必填欄位!\r\n";
        if (_Pwd !== _Pwd2) sMsg += "[密碼和確認密碼]不相同!\r\n";
    } else {//修改
        if (_Pwd !== _Pwd2) sMsg += "[密碼和確認密碼]不相同!\r\n";
    }

    if (sMsg !== "") {
        FixSysMsg.danger(sMsg);
        return;
    }

    Spinner.show();
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PU02",
        "data": {
            "docdetail": [{
                "EIP_CODE": _Code,
                "EIP_DEPT_NAME": _DEPName,
                "EIP_NAME": _ECName,
                "EIP_JOB_NO": _JobTitleNo,
                "EIP_JOB_TITLE": _JobTitle,
                "EIP_SEX": _Sex,
                "EIP_OBDATE": _OBDate,
                "EIP_LVDATE": _LVDate,
                "EIP_STATE": _State,
                "EIP_PWD": _Pwd
            }]
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL(`Permission/SaveUser`),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                FixSysMsg.success(`${_uCode === "" ? "新增" : "修改"}成功！`);
                setTimeout(function () { window.location.replace("UserManagement"); }, timeOutTime);
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

