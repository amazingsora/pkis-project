<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	$(function(){
		
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '流程編號', name: 'flowid', width: 110, sortable: false, hidden: true });
		grid.addColumn({ label: '合約模式', name: 'contractmodel', width: 90, sortable: false });
		grid.addColumn({ label: '條件', name: 'condition', width: 90, sortable: false, hidden: true });
		grid.addColumn({ label: '流程名稱', name: 'flowname', width: 140, sortable: false });
		grid.addColumn({ label: '前置流程', name: 'isprereview', width: 90, sortable: false });
		grid.addColumn({ label: '單位主管', name: 'isdeptreview', width: 90, sortable: false });
		grid.addColumn({ label: '組織主管', name: 'isorgreview', width: 90, sortable: false });
		grid.addColumn({ label: '流程版本', name: 'flowversion', width: 90, sortable: false });
		grid.addColumn({ label: '是否停用', name: 'status', width: 90, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="common.grid.action.edit"/>', width: 80, exportcol:false, formatter:girdCmds });
		grid.setController("<c:url value='/xauth/contractReview/query' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {
			grid.putParams("flowname", $('#flowname').val());
			grid.putParams("contractmodel", $('#contractmodel').val());
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
		if(rowObject.status == 'N'){
        	cmd += '<button id="btnDelete" type="button" class="btn btn-sm btn-danger" ' +
		   		 		'onclick="toBgnAndStop(\'' + options.rowId +'\', \'N\' );"><spring:message code="suppliter.field.stop"/></button>';	
        }else{
        	cmd += '<button id="btnDelete" type="button" class="btn btn-sm btn-success" ' +
	   		 			'onclick="toBgnAndStop(\'' + options.rowId +'\', \'Y\' );"><spring:message code="suppliter.field.begin"/></button>';
        }

		return cmd;
	}

	//編輯
	function toDetail(rid) {
		$('#keyData').val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
	//停用 && 啟用
	function toBgnAndStop(rid, status) {
		//Alert 訊息
		var msg = '';
		if(status == 'N'){
			msg = '<spring:message code="suppliter.field.stop"/>';
		}else{
			msg = '<spring:message code="suppliter.field.begin"/>';
		}
		
		$.fn.confirm(msg, function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/contractReview/bgnAndStop"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(result) {
				$.fn.alert(result.messages, function() {
					if (result.status == "ok") {
						$("#btnQuery").click();
					}
				});
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
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/contractReview/data' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"> 合約模式:</label>
								<div class="col-sm-9">
									<combox:xauth id="contractmodel" name="contractmodel"
										class="form-control select2-single" xauthType="x-sys-code"
										listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required" />
									<input type="hidden" id="contractmodelName" name="contractmodelName" />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"> 流程名稱:</label>
								<div class="col-sm-9">
									<input type="text" id="flowname" name="flowname" class="form-control" <%-- value="<c:out value="${data.menuId}"/>" --%> required>
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
