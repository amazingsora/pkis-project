<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">
	var grid;
	$(function() {
		grid = $("#dataGrid").tvGrid();
		grid.addColumn({ label: '', name: '', width: 50, exportcol: false,sortable: false, formatter:gridCmds});
		grid.addColumn({ label: '<spring:message code="contract.field.templateType"/>', name: 'module', width: 90, sortable: false});
		grid.addColumn({ label: '<spring:message code="contract.field.templatename"/>', name: 'disp', width: 90, sortable: false});
		grid.addColumn({ label: '<spring:message code="contract.field.version"/>', name: 'version', width: 90, sortable: false});
		grid.addColumn({ label: '<spring:message code="contract.field.docstatus"/>', name: 'docstatusname', width: 90, sortable: false});
		grid.addColumn({ label: '<spring:message code="contract.field.updtime"/>', name: 'updtime', width: 90, sortable: false});
		grid.setController("<c:url value='/contractTemplate/manage/queryPdfVerCompare' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		grid.putParams("modulecode", "<c:out value="${data.MODULECODE}"/>");
		grid.putParams("disp", "<c:out value="${data.DISP}"/>");
		grid.putParams("version", "<c:out value="${data.VERSION}"/>");
		grid.load();
		console.log("data == ", "<c:out value="${data}"/>");
		
		$("#btnVerCompare").on("click", function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value='/contractTemplate/manage/pdfVerCompare' />");
			ajax.put("docverSource", "<c:out value="${data.DOCVER}"/>");
			ajax.put("docver", $("#docver").val());
			ajax.call(function(response) {
				console.log("result === ", response);
				if(response.status === 'ok') {
					window.location.href = '<c:url value="/contractTemplate/manage/downloadPdf?fileName=' + response.result.fileName + '" />';
				} else {
					$.fn.alert(response.messages);
				}
			});
		});
		
		<!--checkbox單選 -->
		$("#dataGrid tbody tr td input").on("click", function() {
			alert();
			if($(this).prop("checked")) {
				alert("2");
				$("#dataGrid tbody tr td input:checkbox").prop("checked", false);
				$(this).prop("checked", true);
			}
		});
	
	});
	
	function gridCmds(cellvalue, options, rowObject) {
		var cmd = '';
		cmd += '<input type="checkbox" id="cbCompared_' + options.rowId + '" name="cbCompared_' + options.rowId + '" onclick="setCheckbox(\'' + options.rowId + '\', \'' + rowObject.docver + '\');">';
		
		return cmd;
	}
	
	function setCheckbox(rowId, docver) {
		if($("#cbCompared_" + rowId).prop("checked")) {
			$("#dataGrid tbody tr td input:checkbox").prop("checked", false);
			$("#cbCompared_" + rowId).prop("checked", true);
			$("#docver").val(docver);
		}
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:out value="${menuName}"/> - <spring:message code="common.compare.version"/>
				</header>
				<input type="hidden" id="docver" name="docver">
				<form id="dataForm" name="dataForm" class="forms-sample" action="" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.templateType"/></label>
								<div class="col-sm-9">
									<label class="col-form-label"><c:out value="${data.MODULE}"/></label>
								</div>
							</div>						
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.templatename"/></label>
								<div class="col-sm-9">
									<label class="col-form-label"><c:out value="${data.DISP}"/></label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.version"/></label>
								<div class="col-sm-9">
									<label class="col-form-label"><c:out value="${data.VERSION}"/></label>
								</div>
							</div>						
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.docstatus"/></label>
								<div class="col-sm-9">
									<label class="col-form-label"><c:out value="${data.DOCSTATUSNAME}"/></label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.updtime"/></label>
								<div class="col-sm-9">
									<label class="col-form-label"><c:out value="${data.UPDTIME}"/></label>
								</div>
							</div>						
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.prdtime"/></label>
								<div class="col-sm-9">
									<label class="col-form-label"><c:out value="${data.PRDTIME}"/></label>
								</div>
							</div>
						</div>
					</div>
					<button id="btnVerCompare" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.compare.version"/>
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw" onclick="window.location.href='<c:url value='/contractTemplate/manage/'/>'">
						<spring:message code="common.back" />
					</button>
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
	<!-- 查詢結果Grid -->
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
