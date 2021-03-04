<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	$(function () {
	
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '<spring:message code="xauth.field.menu.id"/>', name: 'menuId', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.menu.url"/>', name: 'menuUrl', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.menu.name"/>', name: 'menuCname', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.menu.parent"/>', name: 'parentId', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.menu.order"/>', name: 'seqNo', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.menu.parent.order"/>', name: 'parentSeq', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.menu.memo"/>', name: 'memo', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.create.user"/>', name: 'creUser', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.create.date"/>', name: 'creDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.update.user"/>', name: 'updUser', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.update.date"/>', name: 'updDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds});
		grid.setController("<c:url value='/xauth/menu/query' />");	
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {				
			grid.putParams("menuId", $('#menuId').val());
			grid.putParams("menuCname", $('#menuCname').val());
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
		$('#keyData').val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
	function toDelete(rid) {
		$.fn.confirm('<spring:message code="common.confirm.delete"/>', function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/menu/delete"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(result) {
				if (result) {
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
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/><spring:message code="xauth.function.bread.query.suffix"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/menu/data' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.menu.id"/></label>
								<div class="col-sm-9">
									<input type="text" id="menuId" name="menuId" class="form-control" placeholder="">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.menu.name"/></label>
								<div class="col-sm-9">
									<input type="text" id="menuCname" name="menuCname" class="form-control" placeholder="">
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
				<div >
					<table id="dataGrid"></table>
	   				<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>
