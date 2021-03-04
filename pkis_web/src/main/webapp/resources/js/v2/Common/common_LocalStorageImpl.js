//取得儲存於LocalStorage的資料
function getItemFromLocalStorage(processCode, key) {
    var rtnItem = undefined;
    if (processCode === "getLocalStorage") {
        try {
            rtnItem = window.localStorage.getItem(key);
            if (rtnItem === null) rtnItem = undefined;
        } catch (e) {
            console.log(e);
            rtnItem = false;
        }
    }
    return rtnItem;
}

//將資料儲存於LocalStorage
function setItemToLocalStorage(processCode, key, value) {
    if (processCode === "setLocalStorage") {
        try {
            window.localStorage.setItem(key, value);
            return true;
        } catch (e) {
            console.log(e);
            return false;
        }
    }
}

//清除儲存於LocalStorage的資料
//isClearAll: 是否全部清除(true/false)
function clearLocalStorage(processCode, key, isClearAll) {
    if (processCode === "clearLocalStorage") {
        try {
            if (key)
                window.localStorage.removeItem(key);
            else if (isClearAll === "true")
                window.localStorage.clear();
            return true;
        } catch (e) {
            console.log(e);
            return false;
        }
    }
}

function modifyLocalStoragePara(processCode, comparedKey, updVal, toBeClearedKeys) {
    var oriVal, newVal;

    if (processCode === "modifyLocalStoragePara" && updVal) {
        SetMainProcessPara("getLocalStorage", { "key": comparedKey });
        oriVal = MainProcess();
        if (oriVal !== updVal) {
            //當比對與原localStorage值不同，需清除localStorage不必要的資料
            if (Array.isArray(toBeClearedKeys)) {
                toBeClearedKeys.forEach(function (val, i) {
                    SetMainProcessPara("clearLocalStorage", { "key": val, "isClearAll": "false" });
                    MainProcess();
                });
            } else if (typeof toBeClearedKeys === "string") {
                SetMainProcessPara("clearLocalStorage", { "key": toBeClearedKeys, "isClearAll": "false" });
                MainProcess();
            }
        }

        SetMainProcessPara("setLocalStorage", { "key": comparedKey, "value": updVal });
        MainProcess();

        return true;
    } else {
        return false;
    }
}