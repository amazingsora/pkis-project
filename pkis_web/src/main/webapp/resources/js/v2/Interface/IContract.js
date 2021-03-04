//BTN類型連結
function LinkURL(datatype, confirmFlag, cancerCode) {
    linkURL(datatype, confirmFlag, cancerCode);
}

function SetToBackResult(funcType, parametersStr, refStr) {
    setToBackResult(funcType, parametersStr, refStr);
}

function AddRecord(funcType, targetId, refId) {
    addRecord(funcType, targetId, refId);
}

function GetSourceData(json, lsKey) {
    return getSourceData(json, lsKey);
}

function SetSourceData(matchName, lsKey) {
    return setSourceData(matchName, lsKey);
}

function DeleteResult(funcType, currPKey, callback, delFilename, currDataid) {
    deleteResult(funcType, currPKey, callback, delFilename, currDataid);
}

function ReloadResult(currPKey) {
    reloadResult(currPKey);
}

function ReloadDetail(currPKey) {
    reloadDetail(currPKey);
}

function CleanRecord(searchType, searchKey) {
    cleanRecord(searchType, searchKey);
}

function DeleteResult(dataType, currPKey, callback) {
    deleteResult(dataType, currPKey, callback);
}

function DeleteFile(dataType, currPKey, callback){
	deleteFile(dataType, currPKey, callback);
}

function DownloadFile(dataType, currPKey){
	downloadFile(dataType, currPKey)
}

function SaveData(clickType){
	saveData(clickType);
}

function CheckRule(resultId){
	checkRule(resultId);
}

function Calculation(natMarginActualId, extraId, actualId, resultId){
	calculation(natMarginActualId, extraId, actualId, resultId);
}

//function GetApproval(detpNo, suppcode){
//	getApproval(detpNo, suppcode);
//}