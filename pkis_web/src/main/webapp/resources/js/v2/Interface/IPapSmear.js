var currentBindingObj;
var currentBindName = "";
var currentBindType = "";
var currentBindValue = "";


//新收[取病歷資訊]開啟[病歷資料查詢]子視窗
function SetPatientInfo(api, ...theArgs) {
    setPatientInfo(api);
}


/**
 * 新收[取病歷資訊]按鈕
 * @param {any} parameters  檢核欄位(病歷號,姓名,身分證號)
 * @param {any} callback    執行的函式名稱(GetPatientInfo)
 * @param {any} callbackApi 設計書名稱(QRY-PAT-20180701)
 * @param {any} theArgs     收案日期ID
 */
function SetPatInfo(parameters, callback, callbackApi, theArgs) {
    currentBindingObj = getBindingObj(`#${theArgs}`);
    currentBindValue = currentBindingObj["bindVal"];

    setPatInfo(parameters, callback, callbackApi, currentBindValue);
}


/**
 * 新收[取病歷資訊]基本資料
 * @param {any} parameters  檢核欄位(病歷號,姓名,身分證號)
 * @param {any} target      iframe操作的物件
 * @param {any} requiredStr 需求字串(預設空值)
 * @param {any} isAsync     是否非同步處理(false)
 * @param {any} theArgs     收案日期值
 * @returns {any}           回傳陣列值
 */
function GetPatientInfo(parameters, target, requiredStr, isAsync, theArgs) {
    return getPatientInfo(parameters, target, requiredStr, isAsync, theArgs);
}


/**
 * [新收]儲存時檢核必填項目
 * @param {any} checkItemStr    檢核欄位(收案日(收件日),來源別,病歷號,姓名,身分證號,篩檢機構)
 * @param {any} callback        檢核是否已有篩選個案
 * @param {any} callback_Params undefined
 * @param {any} callback2       呼叫[取病歷資訊]函式
 * @param {any} cb2params       [取病歷資訊]函式檢核欄位(病歷號,姓名,身分證號)
 */
function CheckRequiredItems(checkItemStr, callback, callback_Params, callback2, cb2params) {
    checkRequiredItems(checkItemStr, callback, callback_Params, callback2, cb2params);
}


/**
 * [新收]新收並輸入個案資料
 * @param {any} datatype    預設轉入頁籤
 * @param {any} confirmFlag undefined
 * @param {any} cancerCode  undefined
 */
function CheckAndLinkURL(datatype, confirmFlag, cancerCode) {
    checkandlinkURL(datatype, confirmFlag, cancerCode);
}



//取得基本資料(eg. #SOURCE.分組)，並by key存入localStorage
function GetSourceData(json, lsKey) {
    return getSourceData(json, lsKey);
}


//填入基本資料(eg. #SOURCE.分組)
function SetSourceData(matchName, lsKey) {
    return setSourceData(matchName, lsKey);
}


//BTN類型連結
function LinkURL(datatype, confirmFlag, cancerCode) {
    linkURL(datatype, confirmFlag, cancerCode);
}


//[基本資料 - BMI] 計算公式
function GetBMI(bmiH, bmiW, BMI) {
    getBMI(bmiH, bmiW, BMI);
}


//[基本資料 - 診斷碼] 下拉選單處理(資料由Ref.Tables)
function DiagnosticCodeList(selfDom, targetId) {
    diagnosticCodeList(selfDom, targetId);
}


/**
 * 鄉鎮市區代碼 清空 鄉鎮市區代碼中文欄位
 * @param {any} srcId 來源ID
 * @param {any} tgtId 目標ID
 */
function ClearValue(srcId, tgtId) {
    clearValue(srcId, tgtId);
}


//[追蹤] 清除
//將畫面上區塊內之資料清除
function CleanRecord(searchType, searchKey) {
    cleanRecord(searchType, searchKey);
}


function SetToBackResult(funcType, parametersStr, refStr) {
    setToBackResult(funcType, parametersStr, refStr);
}


function DeleteResult(funcType, currPKey, callback, delFilename, currDataid) {
    deleteResult(funcType, currPKey, callback, delFilename, currDataid);
}


/**
 * 追蹤 - FIX查詢
 * @param {any} currPKey dataid(預設空值)
 */
function ReloadResult(currPKey) {
    reloadResult(currPKey);
}


//[追蹤] 依據點選之片語，將片語由"個管提醒事項"欄位中該游標所在位置插入
function OnRecordPhrase(selfDom, targetId) {
    currentBindValue = selfDom.value;
    currentBindingObj = getBindingObj(selfDom);
    currentBindName = currentBindingObj["bindName"];

    onRecordPhrase(currentBindName, currentBindValue, targetId);
}


/**
 * 將畫面上之資料儲存到個案追蹤資料中(docdetail內增加新物件、並以"_序號_"為key)
 * 且將該個案追蹤資料依"子抹追蹤日期"由大到小排序
 * @param {any} funcType  功能選項名稱
 * @param {any} targetId  需處理的FIX ID
 * @param {any} refId     參考物件ID
 */
function AddRecord(funcType, targetId, refId) {
    addRecord(funcType, targetId, refId);
}


/**
 * 將畫面上之資料儲存到個案追蹤資料中(docdetail內增加新物件、並以"_序號_"為key)
 * 且將該個案追蹤資料依"子抹追蹤日期"由大到小排序
 * @param {any} funcType  功能選項名稱
 * @param {any} targetId  需處理的FIX ID
 * @param {any} refId     參考物件ID
 * @param {any} clickType 目的Tab名稱(Tab點擊時才會有值)
 * @param {any} currTab   目前Tab名稱(Tab點擊時才會有值)
 */
function TabAddRecord(funcType, targetId, refId, clickType, currTab) {
    addRecord(funcType, targetId, refId, clickType);
    if (FixSysMsg.status()) {
        console.log("TabAddRecord changeTabToRenderHTML");
        saveJSONToLocalStorage();
        changeTabToRenderHTML(clickType);
    }
    else {
        //維持Tab在目前的頁籤上
        returnTabToCurrent(currTab);
    }
}

//[追蹤] 依據點選之片語，將片語由"提醒事項"欄位中該游標所在位置插入
function OnReminder(selfDom, targetId) {
    currentBindValue = selfDom.value;
    currentBindingObj = getBindingObj(selfDom);
    currentBindName = currentBindingObj["bindName"];

    onReminder(currentBindName, currentBindValue, targetId);
}