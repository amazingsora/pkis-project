/**
 * 回前頁
 * @param {any} url URL位址
 */
function GoPrevUrl(url) {
    window.location.replace(url);
}


/**
 * 取得群組清單
 * @param {any} sGCode 群組代碼
 * @returns {any} 回傳Deferred物件
 */
function GetGroupList(sGCode) {
    var $gp = $.Deferred();

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG01",
        "data": { "dataid": sGCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/GroupList"),
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

            if (result.length !== 0) {
                //選取=>□選取
                $.each(result, function (idx, item) {
                    console.log(`${idx},${item.群組代碼},${item.群組名稱}`);
                });

                var oTable = $obj.dataTable();
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
            url: getAPIURL("UserGroupPermission/AddGroup"),
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
 * @param {any} sGCode 群組代碼
 * @returns {any} 回傳空值/NULL
 */
function DeleteGroup(sGCode) {
    if (!confirm("是否刪除群組?")) return "";

    Spinner.show();
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG03",
        "data": { "dataid": sGCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/DeleteGroup"),
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
 * @param {any} sGCode 群組代碼
 * @param {any} $obj   群組名稱的HTML Object
 */
function GetGroupName(sGCode, $obj) {
    Spinner.show();

    var $gp = GetGroupList();
    $.when($gp).done(function (_gp) {
        if (_gp.rtnCode === 0) {
            var result = JSON.parse(_gp.jsonData.data);
            console.log(result);

            var val = result.find(function (item) { return sGCode === item.群組代碼; });
            if (val) $obj.val(val.群組名稱);
        } else
            FixSysMsg.danger(`${_gp.rtnCode}-${_gp.message}`);

        Spinner.hide();
    });
}


/**
 * 群組權限列表
 * @param {any} sGCode 群組代碼
 * @param {any} $obj   要產出的jquery dataTable object
 * @returns {any} HTML
 */
function GroupPowerQuery(sGCode, $obj) {
    if (sGCode === "") {
        FixSysMsg.danger("發生錯誤");
        return "";
    }

    Spinner.show();
    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG04",
        "data": { "dataid": sGCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/GroupPermissionList"),
        type: "POST",
        contentType: "application/json",
        data: obj,
        success: function (response) {
            if (response.rtnCode === 0) {
                var result = JSON.parse(response.jsonData.data);
                console.log(result);

                if (result.length !== 0) {
                    $("#txGPowerNO").val(result.length);

                    //權限=>□管理、□黑名單、□新增、□讀取、□更新、□刪除
                    $.each(result, function (idx, item) {
                        console.log(`${idx},${item.模組}:${item.module.trim()},${item.集合}:${item.cluster.trim()}=>${item.權限}`);
                    });

                    var oTable = $obj.dataTable();
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
 * @param {any} sGCode 群組代碼
 */
function SaveGroupPermission(sGCode) {
    Spinner.show();
    var _module = "", _cluster = "", _permissions = "", PermArr = [];

    for (var idx = 0; idx < $("#txGPowerNO").val(); idx++) {
        _module = $(`#module${idx}`).val();
        _cluster = $(`#cluster${idx}`).val();
        _permissions = "";

        if ($(`input[type=checkbox][id=${_module}_${_cluster}_S]`).checked) _permissions += "S";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_B]`).checked) _permissions += "B";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_C]`).checked) _permissions += "C";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_R]`).checked) _permissions += "R";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_U]`).checked) _permissions += "U";
        if ($(`input[type=checkbox][id=${_module}_${_cluster}_D]`).checked) _permissions += "D";

        PermArr.push({ "module": _module, "cluster": _cluster, "permissions": _permissions });
    }

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG05",
        "data": {
            "dataid": sGCode,
            "docdetail": PermArr
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/SaveGroupPermission"),
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
 * @param {any} sGCode 群組代碼
 * @returns {any}      回傳Deferred物件
 */
function GetGroupUserList(sGCode) {
    var $gp = $.Deferred();

    var obj = {
        "token": getToken(),
        "userId": getUserId(),
        "accessCode": "PG06",
        "data": { "dataid": sGCode }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/GroupUserList"),
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
 * @param {any} sGCode 群組代碼
 * @param {any} $obj   要產出的jquery dataTable object
 */
function GroupUserQuery(sGCode, $obj) {
    Spinner.show();

    var $gp = GetGroupUserList(sGCode);
    $.when($gp).done(function (_gp) {
        if (_gp.rtnCode === 0) {
            var result = JSON.parse(_gp.jsonData.data);
            console.log(result);

            if (result.length !== 0) {
                //選取=>□選取
                $.each(result, function (idx, item) {
                    console.log(`${idx},${item.員編},${item.姓名},${item["部門(科別)"]},${item.職務}=>${item.選取}`);
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
 * @param {any} sGCode 群組代碼
 */
function SaveGroupUserPermission(sGCode) {
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
            "dataid": sGCode,
            "docdetail": PermArr
        }
    };
    obj = JSON.stringify(obj);
    console.log(obj);

    $.ajax({
        url: getAPIURL("UserGroupPermission/SaveGroupUserPermission"),
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