let fixDup = [];
stringConstructor = "test".constructor;
arrayConstructor = [].constructor;
objectConstructor = {}.constructor;
let fixArrayName = "";
let fixObjectArrayName = "";
let fixItemIndex = -1;
let fixFirstItemIndex = -1;
let fixFoundArray = undefined;
let fixJsonData = undefined;
let fixJsonObj = undefined;
let fixMaxSeq = "";
let fixSearchIdx = "";
fixresultdata = undefined;


//取得localStorage的初始Json
LocalStorageOriFixJson = function () {
    try {
        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_EntireJson });
        return JSON.parse(MainProcess());
    } catch (e) {
        console.log(e);
    }
};


//新增區塊
NewFixBlock = function (initFixid, maxFixid) {
    Spinner.show();
    var newAddJson = NewFixJson(initFixid, maxFixid);
    console.log(`===============NewFixJson===============`);
    console.log(JSON.stringify(newAddJson));
    console.log('重新繪製HTML');
    reloadPara();
    mapperData = newAddJson;
    docVersion = newAddJson.data[0].docver;
    json = $.extend(true, {}, newAddJson);
    renderDom("#divbody", newAddJson, renderScript);
    Spinner.hide();
};


//新增區塊:Json處理
NewFixJson = function (initFixid, maxFixid) {
    fixJsonData = LocalStorageOriFixJson();
    fixDup = [];
    fixItemIndex = -1;
    fixFirstItemIndex = -1;
    fixArrayName = "";
    fixObjectArrayName = "";
    fixFoundArray = undefined;

    getFixBlock(fixJsonData, initFixid);

    if (fixDup.length > 0) {
        var nextid = getFixNextId(initFixid);
		fixBlockid = nextid;
        console.log(`nextid: ${nextid}`);
        for (var item in fixDup) {
            fixDup[item]['fixblock'] = nextid;
            fixMaxSeq = nextid.match(/_fix[\d]+_/g)[0];

            var newid = fixDup[item]['id'];
            newid = newid.replace(/_fix[\d]+_/g, fixMaxSeq);
            fixDup[item]['id'] = newid;

            if (fixDup[item]['data_id'] !== undefined && fixDup[item]['data_id'] !== "") {
                var newDataid = fixDup[item]['data_id'];
                newDataid = newDataid.replace(/_fix[\d]+_/g, fixMaxSeq);
                fixDup[item]['data_id'] = newDataid;
            }
        }
        for (var idx = 0; idx < fixDup.length; idx++)
            fixFoundArray.splice(fixFirstItemIndex + fixDup.length + idx, 0, fixDup[idx]);
    }
    try {
        if (fixJsonData.data[0].docdetail[fixJsonData.data[0].docdetail.length - 1]["resultdata"] === undefined)
            fixJsonData.data[0].docdetail.push(fixresultdata);

        fixJsonObj = fixJsonData;
        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(fixJsonObj) });
        MainProcess();
    } catch (e) {
        console.log(e);
    }

    return fixJsonObj;
};


//移除區塊
RemoveFixBlock = function (fixid) {
    Spinner.show();
    var newAddJson = RemoveFixJson(fixid);
    console.log(`===============RemoveFixJson===============`);
    console.log(JSON.stringify(newAddJson));
    console.log('重新繪製HTML');
    reloadPara();
    mapperData = newAddJson;
    docVersion = newAddJson.data[0].docver;
    json = $.extend(true, {}, newAddJson);
    renderDom("#divbody", newAddJson, renderScript);
    Spinner.hide();
};


//移除區塊:Json處理
RemoveFixJson = function (fixid) {
    fixJsonData = LocalStorageOriFixJson();
    fixDup = [];
    fixItemIndex = -1;
    fixFirstItemIndex = -1;
    fixArrayName = "";
    fixObjectArrayName = "";
    fixFoundArray = undefined;

    getFixBlock(fixJsonData, fixid);

    if (fixDup.length > 0) {
        console.log("Before  Total Length = " + fixFoundArray.length);
        console.log(JSON.stringify(fixDup));
        console.log("======================================");
        fixFoundArray.splice(fixFirstItemIndex, fixDup.length);
        console.log("After Total Length = " + fixFoundArray.length);
    }
    else
        console.log(`${fixid} did not found.`);

    try {
        fixJsonData.data[dataIdx].docdetail = fixFoundArray;
        fixJsonObj = fixJsonData;
        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(fixJsonObj) });
        MainProcess();
    } catch (e) {
        console.log(e);
    }

    return fixJsonObj;
};


//[FIX Block]-取代區塊
ReplaceFixBlock = function (fromFixid, toFixid) {
    Spinner.show();
    var newAddJson = ReplaceFixBlockValue(fromFixid, toFixid);
    console.log(`===============ReplaceFixBlockValue===============`);
    console.log(JSON.stringify(newAddJson));
    console.log('重新繪製HTML');
    reloadPara();
    mapperData = newAddJson;
    docVersion = newAddJson.data[0].docver;
    json = $.extend(true, {}, newAddJson);
    renderDom("#divbody", newAddJson, renderScript);
    Spinner.hide();
};


//取代區塊:Json處理
ReplaceFixBlockValue = function (blockidFrom, blockidTo) {
    fixJsonData = LocalStorageOriFixJson();
    fixDup = [];
    fixItemIndex = -1;
    fixFirstItemIndex = -1;
    fixArrayName = "";
    fixObjectArrayName = "";
    fixFoundArray = undefined;

    getFixBlock(fixJsonData, blockidFrom);

    if (fixDup.length > 0) {
        var fromArray = fixDup.slice(0);
        fixDup = [];
        fixItemIndex = -1;
        fixFirstItemIndex = -1;
        fixArrayName = "";
        fixObjectArrayName = "";
        CurrentFixArray = undefined;
        ObjContainer = undefined;
        fixFoundArray = undefined;

        getFixBlock(fixJsonData, blockidTo);

        if (fixDup.length > 0) {
            var fileName = "";
            for (var idx = 0; idx < fixDup.length; idx++) {
                if (fromArray.length > idx) {
                    
                    fromArray[idx]['id'] = fixDup[idx]['id'];
                    fromArray[idx]['data_id'] = fixDup[idx]['data_id'];
                    fromArray[idx]['fixblock'] = fixDup[idx]['fixblock'];
                    //if (fromArray[idx]['displayname'] === "會議記錄:" && fromArray[idx]['value'] === "") 
                    //    fromArray[idx]['value'] = fixDup[idx]['value'];
                    //else if (fromArray[idx]['displayname'] === "會議記錄:" && fromArray[idx]['value'] !== "") 
                    //    fileName = fromArray[idx]['value'];
                    //if (fromArray[idx]['displayname'] === "上傳會議記錄")
                    //    fromArray[idx + 1]['value'] = fileName;
                    fixFoundArray[fixFirstItemIndex + idx] = fromArray[idx];
                }
            }
        }
    }

    try {
        fixJsonData.data[dataIdx].docdetail = fixFoundArray;
        fixJsonObj = fixJsonData;
        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(fixJsonObj) });
        MainProcess();
    } catch (e) {
        console.log(e);
    }

    return fixJsonObj;
};


//新版無遞迴getFixBlock
getFixBlock = function (json, blockid) {
    var path = $.JSONPath({ data: json, keepHistory: false, onFound: function (path, value) { if (fixFirstItemIndex === -1) fixFirstItemIndex = parseInt(path.split(';').slice(-1)); } });
    var qryRS = path.query(`$.data[?(@.datatype == '${dataType}')].docdetail[?(@.fixblock == '${blockid}')]`);
    fixresultdata = path.query(`$.data[0].docdetail[?(@.resultdata)]`);
    fixDup = JSON.parse(JSON.stringify(qryRS));
    if (fixDup.length > 0) fixFoundArray = path.query(`$.data[?(@.datatype=="${dataType}")].docdetail`)[0];
};


//取得當前[FIX Block]的id下一個序號
getFixNextId = function (initFixid) {
    var path = $.JSONPath({ data: json, keepHistory: false });
    var count = 0;
    var blockid = initFixid;
    while (path.query(`$.data[?(@.datatype == '${dataType}')].docdetail[?(@.fixblock == '${blockid}')]`)) {
        blockid = blockid.replace(/_fix[\d]+_/g, `_fix${++count}_`);
    }
    
    return blockid;
};


//取得當前[FIX Block]的最大block序號
getFixMaxId = function (oriFixId) {
    var searchJson = json.data[dataIdx].docdetail;
    var searchIdx = fixSearchIdx;
    var searchBlockId = oriFixId;

    while (searchJson[searchIdx]) {
        if (searchJson[searchIdx]["fixblock"] && searchJson[searchIdx]["fixblock"] === searchBlockId)
            searchIdx++;
        else {
            if (searchJson[searchIdx]["fixblock"] === undefined)
                break;
            else if (searchJson[searchIdx]["fixblock"] !== searchBlockId) {
                var oldNum = searchBlockId.match(/_fix[\d]+/g)[0].replace("_fix", "");
                var searchNum = searchJson[searchIdx]["fixblock"].match(/_fix[\d]+/g)[0].replace("_fix", "");
                if (parseInt(searchNum) >= parseInt(oldNum)) {
                    searchBlockId = searchJson[searchIdx]["fixblock"];
                }
                searchIdx++;
            }
        }
    }
    return searchBlockId;
};