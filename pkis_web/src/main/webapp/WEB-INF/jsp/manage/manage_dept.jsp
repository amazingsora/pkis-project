<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">
	var grid;
	$(function () {
	
		grid = $("#dataGrid").tvGrid();		
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.id"/>', name: 'idenId', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.ban"/>', name: 'ban', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.name"/>', name: 'cname', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.type"/>', name: 'idenTypeName', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.enabled"/>', name: 'enabledName', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.create.user"/>', name: 'creUser', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.create.date"/>', name: 'creDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.update.user"/>', name: 'updUser', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.update.date"/>', name: 'updDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds});
		grid.setController("<c:url value='/manage/dept/query'/>");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		<%-- 查詢 --%>
		$("#btnQuery").on("click", function() {	
			if ($("#dataForm").valid()) {
				grid.putParams("appId", $('#appId').val());
				grid.putParams("idenId", $('#idenId').val());
				grid.putParams("ban", $('#ban').val());
				grid.putParams("idenType", $('#idenType').val());	
				grid.putParams("cname", $('#cname').val());	
				grid.load();
			}			
		});

		<%-- 新增 --%>
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
			ajax.setController("<c:url value="/manage/dept/delete"/>");
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
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/manage/dept/data' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
								<div class="col-sm-9">
									<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
										xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden.ban"/></label>
								<div class="col-sm-9">
									<input type="text" id="ban" name="ban" class="form-control" placeholder="">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden.type"/></label>
								<div class="col-sm-9">
									<combox:xauth id="idenType" name="idenType" class="form-control select2-single" 
										xauthType="x-iden-type" listKey="key" listValue="value" />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden.name"/></label>
								<div class="col-sm-9">
									<input type="text" id="cname" name="cname" class="form-control" placeholder="">
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
