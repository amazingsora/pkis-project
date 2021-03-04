<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="tv-combox" prefix="combox" %>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">
	var grid;
	$(function() {
		grid = $("#dataGrid").tvGrid();
		grid.addColumn({ name: 'serno', sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="xauth.field.batch.name"/>', name: 'batchname', width: 30 , sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.docstatus"/>', name: 'status', width: 20 , sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.batch.sleepsec"/>', name: 'sleepsec', width: 25 , sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.batch.lasterrordate"/>', name: 'lasterrordate', width: 35 , sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.batch.lasterrormsg"/>', name: 'lasterrormsg', sortable: false });
// 		grid.addColumn({ label: '<spring:message code="xauth.field.batch.laststartdate"/>', name: 'updatedate', width: 35 , sortable: false });
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:25, exportcol:false,sortable: false, formatter:girdCmds});
		grid.setController("<c:url value='/manage/batchConsole/query' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {
			grid.putParams("serno", $("#serno").val());
			grid.load();
		});
		
		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
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
			ajax.setController("<c:url value="/manage/batchConsole/delete"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(result) {
				if(result) {
					$.fn.alert(result.messages, function() {
						$("#btnQuery").click();
					});
				}
			});
		});
	}

</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class=card-body>
				<header class="card-title">
					<c:out value="${menuName}"/><spring:message code="xauth.function.bread.query.suffix"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/manage/batchConsole/data' />" method="POST">
					<input type="hidden" id="keyData" name="keyData" />
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.batch.name"/></label>
								<div class="col-sm-9">
									<combox:xauth id="serno" name="serno" class="form-control select2-single" 
										xauthType="x-batch-param" listKey="serno" listValue="batchname" />
								</div>
							</div>
						</div>
					</div>
					<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query"/>
					</button>
					<button id="btnInsert" type="button" class="btn btn-outline-secondary btn-fw">
		            	<spring:message code="common.insert"/>
		            </button>
		            <button id="btnClear" type="button" class="btn btn-outline-info btn-fw btnClear">
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
				<div>
					<table id="dataGrid"></table>
					<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>