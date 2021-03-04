<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>
<link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">
<script type="text/javascript">
	var grid;
	$(function () {
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '<spring:message code="contract.field.action"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds});
		grid.addColumn({ label: '<spring:message code="contract.field.no"/>', name: '<spring:message code="contract.field.no"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: '<spring:message code="contract.field.module"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.name"/>', name: '<spring:message code="contract.field.name"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.suppliergui"/>', name: '<spring:message code="suppliter.field.suppliergui"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.suppliercname"/>', name: '<spring:message code="suppliter.field.suppliercname"/>', width: 150, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.submit"/>', name: '<spring:message code="contract.field.updtime"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.docstatus"/>', name: '<spring:message code="contract.field.docstatus"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.contractor"/>', name: '<spring:message code="contract.field.contractor"/>', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'deptCName', width: 150, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'isNowUser', width: 100, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'indexName', width: 100, sortable: false, hidden: true });
		grid.addColumn({ label: 'deptNo', name: 'deptNo', width: 150, sortable: false , hidden: true });
		grid.addColumn({ label: 'flowId', name: 'flowId', width: 150, sortable: false , hidden: true });
		grid.addColumn({ label: 'todo', name: 'todo', width: 100, sortable: false , hidden: true}); 
		grid.setController("<c:url value='/supplier/contract/query' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {	
			if($("#dataForm").valid()) {
				$("#moduleName").val($("#module").val() == "" ? "" : $("#module").find(":selected").text());
				grid.putParams("year", $('#year').val());
				grid.putParams("module", $('#module').val());
				grid.putParams("moduleName", $("#moduleName").val());
				grid.putParams("supplierid", $('#supplierid').val());
				grid.load();
			}
		});
	});

	//Grid Btn
	function girdCmds(cellvalue, options, rowObject) {
		var userCname = '<c:out value="${principal.userCname}"/>';
		var cmd = '';
		
		if(rowObject.isNowUser == 'Y'){
			 if(rowObject.承辦人員 == userCname){
				 //編輯
				 cmd += '<button id="btnEdit" type="button" class="btn btn-sm btn-success" ' +
		 			'onclick="toView(\'' + options.rowId +'\', \'2\');">編輯</button>';
			 }else{
				 //簽核
				 cmd += '<button id="btnFlow" type="button" class="btn btn-sm btn-success" ' +
		 			'onclick="toView(\'' + options.rowId +'\', \'1\');">簽核</button>';
			 }
        }else {
        	if(rowObject.承辦人員 == userCname){
        		if(rowObject.todo == '暫存' || rowObject.todo == '新建'){
        			//編輯
        			cmd += '<button id="btnEdit" type="button" class="btn btn-sm btn-success" ' +
		 			'onclick="toView(\'' + options.rowId +'\', \'2\');">編輯</button>';
        		} else {
        			//檢視
       	    		cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
    	    		'onclick="toView(\'' + options.rowId +'\', \'0\');">檢視</button>';
        		}
        	} else {
        		//檢視
           		cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
           		'onclick="toView(\'' + options.rowId +'\', \'0\');">檢視</button>';
        	}
        }
		return cmd;
	}
	
	//檢視 && 簽核 && 編輯
	function toView(rid, btnType) {
		$("#btnType").val(btnType);
		$('#keyData').val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/supplier/contract/view' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<input type="hidden" id="btnType" name="btnType"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.year" /></label>
								<div class="col-sm-9">
									<input type="text" id="year" name="year" class="dateY form-control" required="required">
								</div>
								<label class="col-form-label">年</label>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.module" /></label>
								<div class="col-sm-9">
									<combox:xauth id="module" name="module" class="form-control select2-single" xauthType="x-sys-code"
										listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required" />
									<input type="hidden" id="moduleName" name="moduleName" />
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
<!-- 					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"   -->
<%-- 						onclick="window.location.href='<c:url value='/xauth/menu/' />'">匯出清單 --%>
<!-- 					</button>					 -->
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<div >
					<table id="dataGrid"></table>
	   				<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>
