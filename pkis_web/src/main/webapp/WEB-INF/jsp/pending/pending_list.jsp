<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">
	var grid;
	$(function () {
	
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:80, exportcol:false,sortable: false, formatter:girdCmds});
		grid.addColumn({ label: '<spring:message code="contract.field.check_static"/>', name: 'checkStatic', sortable: false , hidden: true});
		grid.addColumn({ label: '<spring:message code="contract.field.check_result"/>', name: 'checkResult', width: 110, sortable: false});
		grid.addColumn({ label: '<spring:message code="contract.field.no"/>', name: 'applyNo', width: 110, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'contractmodel', width: 70, sortable: false });
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
		grid.setController("<c:url value='/contract/list/toDoList' />");		
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		grid.load(function(data){
			$("#totalCnt").text(data.records);
		});//一開始就去後台撈資料
		$("#btnQuery").on("click", function() {			
			grid.putParams("idenId", $('#idenId').val());
			grid.putParams("ipAddr", $('#ipAddr').val());
			grid.putParams("sysType", $('#sysType').val());
			grid.putParams("enabled", $('#enabled').val());
			grid.load();			
		});

		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
		
	});

	function girdCmds(cellvalue, options, rowObject) {
		var userCname = '<c:out value="${principal.userCname}"/>';
        var cmd = '' ;
        if(rowObject.flowStatus == '編輯中'){
//      if(rowObject.承辦人員 == userCname){
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
	
</script>

<div class="row">			
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
			<header class="card-title">
				<c:out value="${menuName}"/>(共 <span id="totalCnt">0</span> 筆)<%-- <spring:message code="xauth.function.bread.edit.suffix"/> --%>
			</header>
			</div>
			<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/pending/list/view' />" method="POST">
				<input type="hidden" id="keyData" name="keyData"/>
				<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
			<div class="card-body">
				<div >
					<table id="dataGrid"></table>
	   				<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>
