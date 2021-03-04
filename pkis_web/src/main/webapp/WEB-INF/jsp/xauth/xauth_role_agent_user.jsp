<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">

	var grid;
	$(function () {
		
		grid = $("#dataGrid").tvGrid();		
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.id"/>', name: 'idenId', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.role.id"/>', name: 'roleId', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.id"/>', name: 'agentIdenId', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.agent.user.id"/>', name: 'agentUserId', width: 90, sortable: false });		
		grid.addColumn({ label: '<spring:message code="xauth.field.agent.user.name"/>', name: 'agentUserCname', width: 100, sortable: false });
// 		grid.addColumn({ label: '<spring:message code="xauth.field.enabled"/>', name: 'enabledName', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.create.user"/>', name: 'creUser', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.create.date"/>', name: 'creDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.update.user"/>', name: 'updUser', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.update.date"/>', name: 'updDateDesc', width: 100, sortable: false });
		grid.addColumn({ name: 'userId', hidden: true });
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds});
		grid.setController("<c:url value='/xauth/roleAgentUser/query' />");		
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		if('<c:out value="${principal}"/>'!="admin"){
			$("#idenId").attr('disabled', true);
			$("#roleId").attr('disabled', true);
		}
		$("#btnQuery").on("click", function() {				
			grid.putParams("idenId", $('#idenId').val());
			grid.putParams("roleId", $('#roleId').val());
			grid.load();			
		});
	
		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
		
		$("#idenId").on("change", function() {
			getRoleList();
		});
		$("#idenId").trigger( "change" );

		$("#btnClear").on("click", function() {	
			$('#roleId').empty();
			$("#roleId").append($("<option>").prop({"value":""}).append("請選擇"));
		});
		
	});
	
	function girdCmds(cellvalue, options, rowObject) {			  
        var cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
        				'onclick="toDetail(\'' + options.rowId +'\');"><spring:message code="common.grid.action.view"/></button>' + 
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
			ajax.setController("<c:url value="/xauth/roleAgentUser/delete"/>");
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
	
	function getRoleList() {
		$('#roleId').empty();
		if('<c:out value="${principal}"/>' == "admin"){
			$("#roleId").append($("<option>").prop({"value":""}).append("請選擇"));
		}
		var idenId = $('#idenId').val();
		if (idenId != null && idenId != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/roleAgentUser/getRoleList"/>");
			ajax.put("idenId", idenId);
			ajax.call(function(json) {
				if (json.result.dataList != null) {
					$.each(json.result.dataList,function() {
						var data = this;
						$("#roleId").append($("<option>").prop({"value":data.roleId}).append(data.roleId + "-" + data.roleCname));
						
					});
				}
			});
		}		
	}	

</script>
<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/><spring:message code="xauth.function.bread.query.suffix"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/roleAgentUser/data' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					
					
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
								<div class="col-sm-9">
								
									<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
										xauthType="x-iden-qry" listKey="idenId" listValue="struct"  headerValue="" />
							
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.role"/></label>
								<div class="col-sm-9">
									<select id="roleId" name="roleId" class="form-control select2-single">
									</select>
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
<!-- 		            <button id="btnClear" type="button" class="btn btn-outline-info btn-fw btnClear"> -->
<%-- 		            	<spring:message code="common.clear"/> --%>
<!-- 		            </button> -->
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
