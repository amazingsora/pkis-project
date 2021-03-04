<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	$(function () {
		$("#btnAllCancel").hide();
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '勾選', width:60, exportcol:false,sortable: false, formatter:girdChcekBoxs});
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:70, exportcol:false,sortable: false, formatter:girdCmds});
		grid.addColumn({ label: '<spring:message code="contract.field.check_static"/>', name: 'checkStatic', sortable: false, hidden: true});
		grid.addColumn({ label: '<spring:message code="contract.field.check_result"/>', name: 'checkResult', width: 110, sortable: false});
		grid.addColumn({ label: '<spring:message code="contract.field.no"/>', name: 'applyNo', width: 110, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'contractmodel', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.name"/>', name: 'contracttype', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.suppliergui"/>', name: 'suppliergui', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.suppliercname"/>', name: 'supplierCname', width: 150, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.submit"/>', name: 'startDay', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.docstatus"/>', name: 'flowStatus', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.contractor"/>', name: 'contractor', width: 70, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'deptNo', width: 50, sortable: false });
		
		grid.addColumn({ label: '文件序號', name: 'id', width: 90, sortable: false , hidden: true});
		grid.addColumn({ label: '表單編號', name: 'formId', width: 90, sortable: false , hidden: true});
		grid.addColumn({ label: '表單名稱', name: 'formName', width: 90, sortable: false, hidden: true });
		grid.addColumn({ label: '流程編號', name: 'flowId', width: 100, sortable: false , hidden: true});		
		grid.addColumn({ label: '流程版本', name: 'flowVersion', width: 100, sortable: false , hidden: true});
		grid.addColumn({ label: '流程關卡', name: 'nowTaskId', width: 100, sortable: false , hidden: true});
		grid.addColumn({ label: '流程狀態', name: 'flowStatus', width: 100, sortable: false , hidden: true});
		grid.addColumn({ label: '流程名稱', name: 'taskName', width: 100, sortable: false , hidden: true});
		grid.addColumn({ label: '流程說明', name: 'taskDesc', width: 100, sortable: false , hidden: true});
		grid.setController("<c:url value='/pending/supervisor/toDoList' />");		
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		grid.load(function(data){
			console.log(data);
			$("#totalCnt").text(data.records);
		});//一開始就去後台撈資料
        $(`label:contains(檢核結果)`).css("font-size", "16px");
		//更改字體大小
		$("#btnQuery").on("click", function() {	
			
// 			var checkResults = $('#checkResults').val();
			var checkResults = "";

			$.each($('#checkResultsDiv').find('input:checked'), function (i, rowObj) {
		            var $rowChkObj = $(rowObj);
					if($rowChkObj.prop("checked")){
						checkResults = checkResults + $rowChkObj.val() +",";
					}
		    });
			
			if(checkResults !== "")
				checkResults = checkResults.substr(0, checkResults.length -1);
			
			$('#checkResult').val(checkResults);
			grid.putParams("checkResult", $('#checkResult').val());
			grid.putParams("applyNo", $('#applyNo').val());
			grid.putParams("contractmodel",$("#contractmodel").val() == "" ? "" : $("#contractmodel").val());
			grid.load();			
		});
		
		getCheckResultList();
		
		$("#btnSign").on("click", function() {			
			var resWaitArr = [],resWaitRows =  $("#dataGrid").find("tbody > tr");
			
			 $.each(resWaitRows, function (i, rowObj) {
		            var $rowChkObj = $(rowObj).find("td:eq(0) > input[type='checkbox']");
					if($rowChkObj.prop("checked")){
					   var sendObj = {
						        	"contractNo": $(rowObj).find("td:eq(4)").text(),
					   };
						var $dbObj = JSON.stringify(sendObj);
						resWaitArr.push($dbObj);
					}
		     });	
		
			 console.log(JSON.stringify(resWaitArr));
			 if(resWaitArr.length === 0) {
				 $.fn.alert("無勾選批次簽核項目");
				 return;
			 }
			 
			 $.ajax({
			        url: "<c:url value='/pending/supervisor/batchSign' />",
			        type: "POST",
			        dataType : 'text',
			        contentType: "application/json",
			        data: JSON.stringify(resWaitArr),
			        success: function (response) {
			        	$.fn.alert("批次簽核通過，共" + resWaitArr.length + "筆");
			        	grid.load();
			        }
			    });
		});
		
		
		$("#btnReject").on("click", function() {	
			var resWaitArr = [],resWaitRows =  $("#dataGrid").find("tbody > tr");
			
			 $.each(resWaitRows, function (i, rowObj) {
		            var $rowChkObj = $(rowObj).find("td:eq(0) > input[type='checkbox']");
					if($rowChkObj.prop("checked")){
					   var sendObj = {
						        	"contractNo": $(rowObj).find("td:eq(4)").text(),
					   };
						var $dbObj = JSON.stringify(sendObj);
						resWaitArr.push($dbObj);
					}
		     });	
		
			 console.log(JSON.stringify(resWaitArr));
			 if(resWaitArr.length === 0) {
				 $.fn.alert("無勾選批次駁回項目");
				 return;
			 }
			 
			 $.ajax({
			        url: "<c:url value='/pending/supervisor/btnReject' />",
			        type: "POST",
			        dataType : 'text',
			        contentType: "application/json",
			        data: JSON.stringify(resWaitArr),
			        success: function (response) {
			        	$.fn.alert("批次駁回通過，共" + resWaitArr.length + "筆");
			        	grid.load();
			        }
			    });
		});
		
		$("#btnAll").on("click", function() {			
			var resWaitRows =  $("#dataGrid").find("tbody > tr");
			 $.each(resWaitRows, function (i, rowObj) {
				 	var $rowChkObj = $(rowObj).find("td:eq(0) > input[type='checkbox']");
				    var _checkStatus = $(rowObj).find("td:eq(2)").text();
// 				    if(_checkStatus === "不通過"){
				    	
// 				    }
// 				    else{
				    $rowChkObj.prop("checked", true);
				    $("#btnAll").hide();
				    $("#btnAllCancel").show();
// 				    }
		     });	
		});
		
		$("#btnAllCancel").on("click", function() {			
			var resWaitRows =  $("#dataGrid").find("tbody > tr");
			 $.each(resWaitRows, function (i, rowObj) {
				 	var $rowChkObj = $(rowObj).find("td:eq(0) > input[type='checkbox']");
				    var _checkStatus = $(rowObj).find("td:eq(2)").text();
				    $rowChkObj.prop("checked", false);
				    $("#btnAll").show();
				    $("#btnAllCancel").hide();
		     });	
		});
	});
	//勾選列表
	function girdChcekBoxs(cellvalue, options, rowObject) {
        var cmd = '<input type="checkbox" id="checkbox_ '+ options.rowId +'" name="checkbox_ '+ options.rowId +'">' ;
        			
        return cmd;
	}
	
	function girdCmds(cellvalue, options, rowObject) {
		var userCname = '<c:out value="${principal.userCname}"/>';
        var cmd = '' ;
        				
        if(rowObject.承辦人員 == userCname){
			 cmd += '<button id="btnEdit" type="button" class="btn btn-sm btn-success" ' +
	 			'onclick="toDetailView(\'' + options.rowId +'\');">編輯</button>';
		 }else{
			 cmd += '<button id="btnFlow" type="button" class="btn btn-sm btn-success" ' +
	 			'onclick="toDetailView(\'' + options.rowId +'\');">簽核</button>';
		 }
        
        return cmd;
	}
	
	//檢視
	function toDetailView(rid) {
		$('#keyData').val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
	function getCheckResultList() {
		var i = 0;
		$("#checkResults").empty();
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/pending/supervisor/getCheckResultList"/>");
		ajax.call(function(data) {
			if(data.result.checkResultList != null){
				$.each(data.result.checkResultList, function() {
					var data = this;
					i++;
// 					$("#checkResultsDiv").append("<input id='checkResults" + i + "' name='checkResults" + i + "' type='checkbox' value='" + data.code + "' /> <label for='checkResults" + i + "'>" + data.cname + "</label><br>")
					$("#checkResultsDiv").append("<input id='checkResults" + i + "' name='checkResults" + i + "' type='checkbox' value='" + data.code + "' style='zoom:112.5%;' /> <label for='checkResults" + i + "'style='font-size:16px;''>" + data.cname + "</label><br>")
				});
			}
		});
	}
	
</script>

<div class="row">			
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:out value="${menuName}"/>(共 <span id="totalCnt">0</span> 筆)<%-- <spring:message code="xauth.function.bread.edit.suffix"/> --%>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/pending/supervisor/view' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<input type="hidden" id="checkResult" name="checkResult"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.no" /></label>
								<div class="col-sm-9">
									<input type="text" id="applyNo" name="applyNo" class="form-control">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.module" /></label>
								<div class="col-sm-9">
									<combox:xauth id="contractmodel" name="contractmodel" class="form-control select2-single" xauthType="x-sys-code"
										listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE"/>
									<input type="hidden" id="contractmodelName" name="contractmodelName" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.check_result" /></label>
								<div class="col-sm-9" id="checkResultsDiv">
								</div>
							</div>
						</div>
					</div>
					<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query"/>
					</button>					
					<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
						<spring:message code="common.clear"/>
					</button>
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>	
			<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<button id="btnAll" type="button" class="btn btn-info btn-fw">
					全選
				</button>
				<button id="btnAllCancel" type="button" class="btn btn-info btn-fw">
					全不選
				</button>
				<button id="btnSign" type="button" class="btn btn-success btn-fw">
					批次核准
				</button>
				<button id="btnReject" type="button" class="btn btn-danger btn-fw">
					批次駁回
				</button>
				<div >
					<table id="dataGrid"></table>
	   				<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>
