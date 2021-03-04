var LocalStorage_Key_MENURESIZE = "MENU_RESIZE";
var LocalStorage_Key_EntireJson = ""; //for 表單doccode
var LocalStorage_Key_ReadonlyJson = ""; //for Readonly表單(doccode + UUID)
var LocalStorage_Key_ReadonlyRecord = "Readonly_Resultdata"; //for Readonly表單各筆病歷 + UUID
var LocalStorage_Key_RtnJson = "RTN_JsonData"; //for 儲存成功後、回傳的json資料
var LocalStorage_Key_RestoreMark = ""; //是否需於新增時，取"最新填寫日期、且相同"癌別"與"病歷號"的資料" 的註記(UUID)

var LocalStorage_Key_QryResultlistHtml = "HTML_QryResultlist"; //for 查詢清單
var LocalStorage_Key_ReadonlylistHtml = "HTML_ReadonlyPersonalList"; //for [診療計劃書] 個人病歷唯讀清單

var LocalStorage_Key_AjccUnReceivePlan = "AJCC_UnReceivePlan"; //for 各癌別[診療計劃書] 資料
var LocalStorage_Key_AjccPlanRec = "#診療計畫書未填寫";  //for 各癌別[診療計劃書] 未填寫資料
var LocalStorage_Key_Dataid = "MAIN_DATAID";          //for [案號 - ES DATAID]

var LocalStorage_Key_BoxItems = "#BOX_ITEMS";         //for 查詢的欄位列出
var LocalStorage_Key_BoxResult = "#BOX_RESULT";       //for 查詢的結果集合

var LocalStorage_Key_EMRResult = "CLINIC_AJCC_EMR";   //醫師待辦時使用(儲存由診間連結從後台所取得的JSON資料)

var LocalStorage_Key_CEMForm = "AUDIT_REC";           //醫師待辦清單轉個案審查使用

var LocalStorage_Key_IFrameParent = "#IFrame_Parent"; //子視窗內讀取父視窗物件使用
var LocalStorage_Key_ReviewForm = "#Review_Form";     //設計書預覽使用

function LocalStorageProcess(processCode, jsonData) {
    var rtnItem = undefined;
    var key, value, isClearAll, isAddJsonProperty;

    if (jsonData !== undefined) {
        if (typeof jsonData === "string") {
            var tmpJsonArr = jsonData.indexOf(",") > -1 ? jsonData.split(",") : [jsonData];
            key = tmpJsonArr[0].indexOf(".") > -1 ? tmpJsonArr[0].split(".")[0] : tmpJsonArr[0]; //eg. #SOURCE.病歷號
            if (tmpJsonArr.length >= 2) {
                if (tmpJsonArr[1].match(/isClearAll|isNotClearAll/))
                    isClearAll = tmpJsonArr[1].match(/isClearAll/) ? "true" : "false";
                else {
                    if (tmpJsonArr.length > 2) {
                        isAddJsonProperty = tmpJsonArr[2].match(/isAddJsonProperty/) ? true : false;
                    }
                    if (tmpJsonArr[0].indexOf(".") > -1) {
                        if (isAddJsonProperty) {
                            //需於已存在localStorage內的JSON字串，新修一筆property，而非直接蓋掉
                            rtnItem = getItemFromLocalStorage("getLocalStorage", key);
                            if (rtnItem) {
                                rtnItem = JSON.parse(rtnItem);
                                rtnItem[tmpJsonArr[0].split(".")[1]] = tmpJsonArr[1];
                                value = JSON.stringify(rtnItem);
                            } else
                                value = `{"${tmpJsonArr[0].split(".")[1]}":"${tmpJsonArr[1]}"}`;
                            rtnItem = undefined;
                        }
                        else 
                            value = `{"${tmpJsonArr[0].split(".")[1]}":"${tmpJsonArr[1]}"}`;
                    } else
                        value = tmpJsonArr[1];
                }
            }
        } else {
            key = jsonData["key"];
            value = jsonData["value"];
            isClearAll = jsonData["isClearAll"];
        }

        if (isClearAll !== undefined) {
            if (rtnItem === undefined) rtnItem = clearLocalStorage(processCode, key, isClearAll);
        } else {
            if (rtnItem === undefined) rtnItem = getItemFromLocalStorage(processCode, key);
            if (rtnItem === undefined) rtnItem = setItemToLocalStorage(processCode, key, value);
            if (rtnItem === undefined) {
                if (key === "" || key === undefined) {
                    if (value !== undefined) {
                        var comparedKey = value["comparedKey"];
                        var updVal = value["updVal"];
                        var toBeClearedKeys = value["toBeClearedKeys"];
                        rtnItem = modifyLocalStoragePara(processCode, comparedKey, updVal, toBeClearedKeys);
                    }
                }
            }
        }
    }

    return rtnItem;
}
