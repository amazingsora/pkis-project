<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	$(function(){
		
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '部門代碼', name: 'idenid', width: 110, sortable: false, hidden: true });
		grid.addColumn({ label: '供應商代碼', name: 'supplierid', width: 110, sortable: false });
		grid.addColumn({ label: '供應商廠編', name: 'suppliercode', width: 95, sortable: false, hidden: true });
		grid.addColumn({ label: '供應商統編', name: 'suppliergui', width: 95, sortable: false });
		grid.addColumn({ label: '供應商種類', name: 'suppliertype', width: 95, sortable: false });		
		grid.addColumn({ label: '供應商中文名稱', name: 'suppliercname', width: 120, sortable: false });
		grid.addColumn({ label: '供應商英文名稱', name: 'supplierename', width: 120, sortable: false });
		grid.addColumn({ label: '供應商中文地址', name: 'suppliercaddr', width: 120, sortable: false , hidden: true });
		grid.addColumn({ label: '供應商英文地址', name: 'suppliereaddr', width: 120, sortable: false, hidden: true });
		grid.addColumn({ label: '負責人', name: 'picuser', width: 75, sortable: false });
		grid.addColumn({ label: '供應商電話', name: 'supplieretel', width: 100, sortable: false, hidden: true });
		grid.addColumn({ label: '連絡人', name: 'contacruser', width: 75, sortable: false });
		grid.addColumn({ label: '供應商電子郵', name: 'supplieremail', width: 150, sortable: false, hidden: true });
		grid.addColumn({ label: '是否停用', name: 'enabled', width: 150, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="common.grid.action.edit"/>', width: 80, exportcol:false,sortable: false, formatter:girdCmds });
		grid.setController("<c:url value='/supplier/master/query' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {			
			grid.putParams("suppliergui", $('#supplierguiInput').val());
			grid.putParams("suppliercname", $('#suppliercnameInput').val());
			grid.putParams("suppliercode", $('#suppliercodeInput').val());
			grid.load();			
		});

		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
	});
	
	//Grid Btn
	function girdCmds(cellvalue, options, rowObject) {
        //編輯
		var cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
		 				'onclick="toDetail(\'' + options.rowId +'\');"><spring:message code="common.grid.action.edit"/></button>';
		
		//停用 ＆＆ 啟用 Btn 判斷
		if(rowObject.enabled == 'Y'){
        	cmd += '<button id="btnDelete" type="button" class="btn btn-sm btn-danger" ' +
		   		 		'onclick="toBgnAndStop(\'' + options.rowId +'\', \'Y\' );"><spring:message code="suppliter.field.stop"/></button>';	
        }else{
        	cmd += '<button id="btnDelete" type="button" class="btn btn-sm btn-success" ' +
	   		 			'onclick="toBgnAndStop(\'' + options.rowId +'\', \'N\' );"><spring:message code="suppliter.field.begin"/></button>';
        }

		return cmd;
	}

	//編輯
	function toDetail(rid) {
		$('#keyData').val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
	//停用 && 啟用
	function toBgnAndStop(rid, enabled) {
		//Alert 訊息
		var msg = '';
		if(enabled == 'Y'){
			msg = '<spring:message code="suppliter.field.stop"/>';
		}else{
			msg = '<spring:message code="suppliter.field.begin"/>';
		}
		
		$.fn.confirm(msg, function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/supplier/master/bgnAndStop"/>");
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
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/supplier/master/data' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label">供應商統編:</label>
								<div class="col-sm-9">
									<input type="text" id="supplierguiInput" name="supplierguiInput" class="form-control" value="<c:out value="${data.menuId}"/>" required>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">供應商中文名稱:</label>
								<div class="col-sm-9">
									<input type="text" id="suppliercnameInput" name="suppliercnameInput" class="form-control" <%-- value="<c:out value="${data.menuId}"/>" --%> required>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label">供應商廠編:</label>
								<div class="col-sm-9">
									<input type="text" id="suppliercodeInput" name="suppliercodeInput" class="form-control" <%-- value="<c:out value="${data.menuId}"/>" --%> required>
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
				<div >
					<table id="dataGrid"></table>
	   				<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>
