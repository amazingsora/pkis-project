var mainProcessPara = {
    processCode: "", jsonPara: {}
};

function SetMainProcessPara(processCode, jsonPara) {
    mainProcessPara.processCode = processCode;
    mainProcessPara.jsonPara = jsonPara;
}

function MainProcess() {
    var rtnObj = undefined;
    var processCode = "", jsonData;

    if (mainProcessPara !== undefined) {
        processCode = mainProcessPara.processCode;

        if (!rtnObj) rtnObj = GenDiv(processCode, mainProcessPara.jsonPara["data"], mainProcessPara.jsonPara["body"]);
        if (!rtnObj) rtnObj = LocalStorageProcess(processCode, mainProcessPara.jsonPara);
    }

    return rtnObj;
}