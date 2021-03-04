let dup = [];
stringConstructor = "test".constructor;
arrayConstructor = [].constructor;
objectConstructor = {}.constructor;
let ObjContainer = undefined;
let CurrentArray = undefined;
let ArrayName = "";
let ObjectArrayName = "";
let ItemIndex = -1;
let FirstItemIndex = -1;
let FoundArray = undefined;
let jsonData = undefined;
let jsonObj = undefined;
let addBtnSearchIdx = "";
let addBtnMaxSeq = "";


//取得localStorage的初始Json
LocalStorageOriJson = function () {
    try {
        if (typeof getvalue === "function") getvalue();
        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(json) });
        MainProcess();

        SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_EntireJson });
        return JSON.parse(MainProcess());
    } catch (e) {
        console.log(e);
    }
};


//新增區塊
NewAddButtonBlock = function (btnid, inPageIdx, inRowIdx) {
    Spinner.show();
    setTimeout(function () {
        var newAddJson = NewAddButtonJson(btnid, inPageIdx, inRowIdx);
        console.log(`===============newAddJson===============`);
        console.log(JSON.stringify(newAddJson));
        console.log('重新繪製HTML');
        reloadPara();
        mapperData = newAddJson;
        docVersion = newAddJson.data[0].docver;
        json = $.extend(true, {}, newAddJson);

        renderDom("#divbody", newAddJson, renderScript);
        resetBindingItems($(`[block='${btnid}']`)); //清空編輯區
        Spinner.hide();
    }, timeOutTime);
};


//新增區塊:Json處理
NewAddButtonJson = function (btnid, inPageIdx, inRowIdx) {
    jsonData = LocalStorageOriJson();
    console.log(`***************original json`);
    console.log(`${JSON.stringify(jsonData)}`);
    dup = [];
    ItemIndex = -1;
    FirstItemIndex = -1;
    ArrayName = "";
    ObjectArrayName = "";
    CurrentArray = undefined;
    ObjContainer = undefined;
    FoundArray = undefined;

    getBlock(jsonData, btnid);

    if (dup.length > 0) {
        var maxBtnid = getAddButtonMaxId(btnid, inPageIdx, inRowIdx);
        var nextid = getNextId(maxBtnid);
        console.log(`nextid: ${nextid}`);

        for (var item in dup) {
            dup[item]['block'] = nextid;
            addBtnMaxSeq = nextid.match(/_btn[\d]+_/g)[0];

            var newid = getNextId(dup[item]['id']);
            newid = newid.replace(/_btn[\d]+_/g, addBtnMaxSeq);
            dup[item]['id'] = newid;

            if (dup[item]['data_id'] !== undefined && dup[item]['data_id'] !== "") {
                //當前對應data_id的控制項是屬於此[增加BTN]block的，才會有新data_id
                var newDataid = dup[item]['data_id'];
                if (newDataid !== undefined) {
                    newDataid = newDataid.replace(/_btn[\d]+_/g, addBtnMaxSeq);
                    dup[item]['data_id'] = newDataid;
                }
            }
        }

        for (var idx = 0; idx < dup.length; idx++) {
            FoundArray.splice(FirstItemIndex + dup.length + idx, 0, dup[idx]);
        }
    }
    try {
        jsonData.data[inPageIdx].docdetail = FoundArray;
        jsonObj = jsonData;
        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(jsonObj) });
        MainProcess();
    } catch (e) {
        console.log(e);
    }
    return jsonObj;
	/*
	var str = JSON.stringify(dup);
	console.log(str);
	console.log("=====================================================");
	console.log('Item Index = ' + FirstItemIndex);		
	console.log("=====================================================");
	var str = JSON.stringify(jsonObj, null, 2);
	console.log(str);
    */
};


//移除區塊
RemoveAddButtonBlock = function (btnid, inPageIdx) {
    Spinner.show();
    var triggerItem = $(`input:text[onchange*='DisplayItem'][block='${btnid}']`);
    if (triggerItem.length > 0) {
        //[個管-治療] 若有"首次治療類別"的項目，需執行其function
        eval(triggerItem.attr("onchange").replace("this", "triggerItem[0]").replace(")", ", true)"));
    }
    var newAddJson = RemoveAddButtonJson(btnid, inPageIdx);
    console.log(`===============removeAddJson===============`);
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
RemoveAddButtonJson = function (btnid, inPageIdx) {
    jsonData = LocalStorageOriJson();
    dup = [];
    ItemIndex = -1;
    FirstItemIndex = -1;
    ArrayName = "";
    ObjectArrayName = "";
    CurrentArray = undefined;
    ObjContainer = undefined;
    FoundArray = undefined;

    console.log(`btnid: ${btnid} jsonData: `);
    console.log(`${JSON.stringify(jsonData)}`);
    getBlock(jsonData, btnid);

    if (dup.length > 0) {
        console.log("Before  Total Length = " + FoundArray.length);
        console.log(JSON.stringify(dup));
        console.log("======================================");
        FoundArray.splice(FirstItemIndex, dup.length);
        console.log("After Total Length = " + FoundArray.length);
    }
    else console.log("did not found.");
    try {
        jsonData.data[inPageIdx].docdetail = FoundArray;
        jsonObj = jsonData;
        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_EntireJson, "value": JSON.stringify(jsonObj) });
        MainProcess();
    } catch (e) {
        console.log(e);
    }
    return jsonObj;
};


//取得需處理的Json block
this.getBlock = function (json, blockid) {
    //var type = GetType(json);
    if (Array.isArray(json)) {
        ItemIndex = -1;
        for (var x in json) {
            ItemIndex++;
            this.getBlock(json[x], blockid);
            if (dup.length > 0 && FirstItemIndex === -1) {
                FirstItemIndex = ItemIndex;
                FoundArray = json;
            }
        }
        if (dup.length > 0) return;
    }
    else if (typeof json === 'object') {
        var itemid = json['block'];
        if (itemid !== undefined) {
            if (itemid === blockid) {
                if (ObjectArrayName !== ArrayName) ObjectArrayName = ArrayName;
                var item = Object.assign({}, json);
                dup.push(item);
            }
        }
        for (var property in json) {
            if (json.hasOwnProperty(property)) {
                if (typeof json[property] === 'object' || Array.isArray(json[property])) {
                    if (Array.isArray(json[property])) ArrayName = property;
                    this.getBlock(json[property], blockid);
                }
            }
        }
    }
};


//取得當前[增加BTN]的id下一個序號
getNextId = function (btnid) {
    var ids = btnid.split('_');
    var theid = "";
    var count = 0;
    for (var item in ids) {
        count++;
        if (count < ids.length - 1) {
            theid += `${ids[item]}_`;
        }
        else if (count === ids.length - 1) {
            var btnNo = parseInt(ids[item].substring(3, ids[item].length));
            theid += `btn${++btnNo}_`;
        }
        else if (count === ids.length) {
            theid += ids[item];
        }
    }
    return theid;
};


//取得當前[增加BTN]的最大block序號
getAddButtonMaxId = function (oriAddBtnId, inPageIdx, inSearchIdx) {
    var searchJson = json.data[inPageIdx].docdetail;
    var searchIdx = inSearchIdx;
    var searchBlockId = oriAddBtnId;
    var oriBlockPrefix = searchBlockId.replace(/_btn[\d]+_.*/g, "");

    while (searchJson[searchIdx]) {
        if (searchJson[searchIdx]["block"] && searchJson[searchIdx]["block"] === searchBlockId)
            searchIdx++;
        else {
            if (searchJson[searchIdx]["block"] === undefined)
                break;
            else if (searchJson[searchIdx]["block"] !== searchBlockId) {
                if (searchJson[searchIdx]["block"] !== "") {
                    var searchBlockPrefix = searchJson[searchIdx]["block"].replace(/_btn[\d]+_.*/g, "");
                    if (searchBlockPrefix !== oriBlockPrefix) break;

                    var oldNum = searchBlockId.match(/_btn[\d]+/g)[0].replace("_btn", "");
                    var searchNum = searchJson[searchIdx]["block"].match(/_btn[\d]+/g)[0].replace("_btn", "");
                    if (parseInt(searchNum) >= parseInt(oldNum)) {
                        searchBlockId = searchJson[searchIdx]["block"];
                    }
                }
                searchIdx++;
            }
        }
    }
    return searchBlockId;
};


//找尋fieldset範圍內，有增加過的blockId
findAddedBlockids = function ($block) {
    var currAddBtnBlockIds = [];

    if ($block && $block.length > 0) {
        $block.find("input:button[displayname='增加'][value='+']").each(function (i, item) {
            var currBlockId = $(item).attr("block");
            if (currAddBtnBlockIds.indexOf(currBlockId) === -1) currAddBtnBlockIds.push({ "blockid": currBlockId });
        });
        currAddBtnBlockIds.forEach(function (initBlockJson) {
            $block.find("input:button[displayname='增加'][value='-']").each(function (i, item) {
                var initBlockIdPrefix = initBlockJson["blockid"].replace(/_btn[\d]+_.*/g, "");
                var currBlockId = $(item).attr("block");
                if (currBlockId.match(`${initBlockIdPrefix}`)) {
                    initBlockJson["hasAdded"] = true; return false;
                }
            });
        });
    }

    return currAddBtnBlockIds;
};