<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">
	var grid;
	$(function() {
		getTemplatemoudle();
		grid = $("#dataGrid").tvGrid();
		grid.addColumn({ label: '<spring:message code="contract.field.action"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds});
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'modelname', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.type"/>', name: 'flowname', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.templatename"/>', name: 'templatename', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="statisticalreport.field.CreateTime"/>', name: 'createdate', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.updateTime"/>', name: 'updatedate', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.droptime"/>', name: 'droptime', width: 100, sortable: false });

		grid.addColumn({ name: 'templateDataId', hidden: true }); 
		grid.addColumn({ name: 'dataType', hidden: true }); 
		grid.addColumn({ name: 'prdtime', hidden: true }); 
		grid.addColumn({ name: 'droptime', hidden: true }); 
		grid.setController("<c:url value='/percontractTemplate/list/query'/>");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {
			grid.putParams("module", $("#module").val());
			grid.putParams("disp", $("#disp").val());
			grid.load();
		});
		
		$("#module").on("change", function() {
			getFlowList();
		});
		
	});
	
	function girdCmds(cellvalue, options, rowObject) {
        var cmd = '';
		if(rowObject.droptime === '' || rowObject.droptime === null) {
			cmd += '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
			'onclick="toDetail(\'' + options.rowId +'\');"><spring:message code="common.grid.action.edit"/></button>';
			// 狀態為尚未起用，需要啟用按鈕
			if(rowObject.prdtime === '' || rowObject.prdtime === null) {
				cmd += '<button id="begin" type="button" class="btn btn-sm btn-success" ' +
	 			'onclick="toBegin(\'' + options.rowId +'\' );"><spring:message code="suppliter.field.begin"/></button>';
			} else {
				cmd += '<button id="stop" type="button" class="btn btn-sm btn-danger" ' +
		 		'onclick="toStop(\'' + options.rowId +'\' );"><spring:message code="suppliter.field.stop"/></button>';
			}
		}
		
		return cmd;
	}
	
	function toDetail(rid) {
		$("#keyData").val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
	function toBegin(rid) {
		var msg = '<spring:message code="contract.confirm.begin"/>';
		
		$.fn.confirm(msg, function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/percontractTemplate/list/setBeginVal"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(response){ 
				if (response) {
					$.fn.alert(response.messages, function() {
						$("#btnQuery").click();
					});
				}
			});
		});
	}
	
	function toStop(rid) {
		var msg = '<spring:message code="contract.confirm.stop"/>';
		
		$.fn.confirm(msg, function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/percontractTemplate/list/setStopVal"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(response){ 
				if (response) {
					$.fn.alert(response.messages, function() {
						$("#btnQuery").click();
					});
				}
			});
		});
	}
	
	function toDelete(rid) {
		$.fn.confirm('<spring:message code="common.confirm.delete"/>', function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/percontractTemplate/list/delete"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(result){ 
				if (result) {
					$.fn.alert(result.messages, function() {
						$("#btnQuery").click();
					});
				}
			});
		});
	}
	
	function getFlowList() {
		$("#disp").empty();
		$("#disp").append($("<option>").prop({"value":""}).append("請選擇"));
		var module = $("#module").val();
		if(module != null && module != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/contract/list/getFlowList"/>");
			ajax.put("module", module);
			ajax.call(function(response) {
				if(response.result.dataList != null) {
					$.each(response.result.dataList, function() {
						var data = this;
						$("#disp").append($("<option>").prop({"value":data.flowid}).append(data.flowname));
					});
				}
			});
		}
	}
	function getTemplatemoudle(){
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/percontractTemplate/list/getTemplatemoudle"/>");
		ajax.call(function(response) {
			console.log(response)
			if(response.result.dataList != null) {
				$.each(response.result.dataList, function() {
					var data = this;
					console.log(data);
					$("#module").append($("<option>").prop({"value":data.code}).append(data.cname));
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
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/perContractTemplate/insert/editView'/>" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.module" /></label>
								<div class="col-sm-9">
<%-- 									<combox:xauth id="module" name="module" class="form-control select2-single" xauthType="x-sys-code" --%>
<%-- 										listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required" /> --%>
									<select id="module" name="module" class="form-control select2-single" required>
										<option value="">請選擇</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.dispname" /></label>
								<div class="col-sm-9">
									<select id="disp" name="disp" class="form-control select2-single" >
										<option value="">請選擇</option>
									</select>
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
				<div>
					<table id="dataGrid"></table>
					<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>