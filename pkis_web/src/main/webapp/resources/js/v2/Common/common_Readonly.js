//[唯讀頁] 進入新增頁
function getAddReadonly(inCancerType, ...theArgs) {
    var readonly_Doccode = "";
    var lsKey = $("#readonly_locStgUUID_Key").val();

    if (lsKey && lsKey !== "") {
        SetMainProcessPara("getLocalStorage", { "key": lsKey });
        var tmpRdJson = MainProcess();

        if (tmpRdJson !== undefined) {
            //取癌別代碼
            tmpRdJson = JSON.parse(tmpRdJson);
            readonly_Doccode = tmpRdJson.data[0].doccode;
            inCancerType = readonly_Doccode.match(/\d{3}\w{3}/g)[0];
            console.log(inCancerType);

            //取此筆Resultdata的Json
            var tmpResKey = $("#readonly_locStgUUID_ResKey").val();
            if (tmpResKey && tmpResKey !== "") {
                SetMainProcessPara("getLocalStorage", { "key": tmpResKey });
                tmpResKey = MainProcess();

                SetMainProcessPara("getLocalStorage", { "key": tmpResKey });
                var tmpResJson = MainProcess();
                if (tmpResJson !== undefined) {
                    console.log(tmpResJson);
                    GetAJCCAdd(inCancerType, tmpResJson);
                }
            }
        }
    }
}

//[唯讀頁] 進入編輯頁
function getEditReadonly() {
    var readonly_Dataid = "", readonly_Doccode = "";
    var lsKey = $("#readonly_locStgUUID_Key").val();

    if (lsKey && lsKey !== "") {
        SetMainProcessPara("getLocalStorage", { "key": lsKey });
        var tmpRdJson = MainProcess();

        if (tmpRdJson !== undefined) {
            tmpRdJson = JSON.parse(tmpRdJson);
            readonly_Dataid = tmpRdJson.data[0].dataid;
            readonly_Doccode = tmpRdJson.data[0].doccode;

            GetEdit(readonly_Dataid, readonly_Doccode);
        }
    }
}