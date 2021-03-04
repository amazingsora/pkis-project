<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox" %>

<script type="text/javascript">
	var grid;
	$(function () {
		
		grid = $("#dataGrid").tvGrid();
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'contractmodelcname', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'contractmodel', width: 110, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="contractReview.field.flowname"/>', name: 'flowname', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'flowid', width: 110, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'deptcname', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'deptno', width: 110, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="xauth.field.iden"/>', name: 'idencname', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'idenid', width: 110, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="xauth.field.user.id"/>', name: 'userids', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.role"/>', name: 'roleids', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.legal.action.type"/>', name: 'actiontypecname', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'actiontype', width: 110, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.setup"/>', name: 'isidenids', width: 90, sortable: false });
		grid.addColumn({ name: 'serno', sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds});
		grid.setController("<c:url value='/xauth/reviewPer/query' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {
			grid.putParams("contractmodel", $("#contractmodel").val());
			grid.putParams("flowid", $("#flowid").val());
			grid.putParams("deptno", $("#deptno").val());
			grid.load();
		});
		
		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
		
		getFlowList();
	});
	
	function girdCmds(cellvalue, options, rowObject) {			  
        var cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
        				'onclick="toDetail(\'' + options.rowId +'\');"><spring:message code="common.grid.action.edit"/></button>' + 
           		   '<button id="btnDelete" type="button" class="btn btn-sm btn-danger" ' +
           		   		'onclick="toDelete(\'' + options.rowId +'\');"><spring:message code="common.grid.action.delete"/></button>';
        return cmd;
	}

	function toDetail(rid) {
		$("#keyData").val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	} 
	
	function toDelete(rid) {
		$.fn.confirm('<spring:message code="common.confirm.delete"/>', function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/reviewPer/delete"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(result) {
				if(result) {
					$.fn.alert(result.messages, function(){
						$("#btnQuery").click();
					});
				}
			});
		});
	}
	
	function getFlowList() {
		$("#flowid").empty();
		$("#flowid").append($("<option>").prop({"value":""}).append("請選擇"));
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/xauth/reviewPer/getFlowList"/>");
		ajax.call(function(response) {
			if(response.result.dataList != null) {
				$.each(response.result.dataList,function() {
					var data = this;
					var flowid;
					$("#flowid").append($("<option>").prop({"value":data.flowid}).append(data.flowname));
					if('<c:out value="${data.flowid}"/>'!=''){
						flowid = '${data.flowid}';
						if(flowid != null){
							$("#flowid option[value=" + flowid + "]").prop('selected', 'selected');
						}
					}
					$("#flowid").trigger("change");
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
					<c:out value="${menuName}"/><spring:message code="xauth.function.bread.query.suffix"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value="/xauth/reviewPer/data"/>" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.module"/></label>
								<div class="col-sm-9">
									<combox:xauth id="contractmodel" name="contractmodel" class="form-control select2-single"
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row" id="flowSelectMenu">
								<label class="col-sm-2 col-form-label"><spring:message code="contractReview.field.flowname" /></label>
								<div class="col-sm-9">
									<select id="flowid" name="flowid" class="form-control select2-single">
										<option value="" disabled>請選擇</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row" id="deptnoSelectMenu">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.section" /></label>
								<div class="col-sm-9">
									<combox:xauth id="deptno" name="deptno" class="form-control select2-single" 
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="DEPT_CODE" />
								</div>
							</div>
						</div>
					</div>
					<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query" />
					</button>
					<button id="btnInsert" type="button" class="btn btn-outline-secondary btn-fw">
						<spring:message code="common.insert" />
					</button>
					<button id="btnClear" type="button" class="btn btn-outline-info btn-fw btnClear">
						<spring:message code="common.clear" />
					</button>
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<div>
					<table id="dataGrid"></table>
					<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>