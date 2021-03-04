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
		grid.addColumn({ label: '下載', width:120, exportcol:false,sortable: false, formatter:girdFiles});
		grid.addColumn({ label: '<spring:message code="contract.field.docstatus"/>', name: 'docstatusname', width: 50, sortable: false });
		grid.addColumn({ label: '上傳年度', name: 'year', width: 60, sortable: false});
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: 'module', width: 60, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.version"/>', name: 'version', width: 50, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.disp"/>', name: 'disp', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.updtime"/>', name: 'updtime', width: 80, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.prdtime"/>', name: 'prdtime', width: 80, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.droptime"/>', name: 'droptime', width: 80, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.picuser"/>', name: 'userCname', width:80, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.action"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds});
		grid.setController("<c:url value='/contractTemplate/manage/query'/>");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		<%-- 查詢 --%>
		$("#btnQuery").on("click", function() {
			if ($("#dataForm").valid()) {
				grid.putParams("module", $('#module').val());
				grid.putParams("docstatus", $('#docstatus').val());
				grid.load();
			}
		});

		<%-- 新增 --%>
		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});

		/*
		$("#module").on("change", function() {
			getKindList();
		});

		$("#module").trigger("change");
		*/
	});

	function getKindList() {
		$('#contractKind').empty();
		$("#contractKind").append($("<option>").prop({"value":""}).append("請選擇"));
		
		var module = $('#module').val();
		if (module != null && module != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/contract/manage/getKindList"/>");
			ajax.put("module", module);
			ajax.call(function(json) {
				if (json.result.dataList != null) {
					$.each(json.result.dataList,function() {
						var data = this;
						$("#contractKind").append($("<option>").prop({"value":data.kindKey}).append(data.kindValue));
					});
				}
			});
		}		
	}

	
	<%-- 清單下載按鈕 --%>
	function girdFiles(cellvalue, options, rowObject) {
		console.log("rowObject == ", rowObject);
		var cmd = '', _module = '';
		cmd = '<button id="btnDownloadPdf" type="button" class="btn btn-sm btn-success" ' +
		  'onclick="previewPdf(\'' + rowObject.docver +'\',\''+ _module +'\',\''+ rowObject.year +'\');">預覽</button>' + 
		  '<button id="btnDownloadXls" type="button" class="btn btn-sm btn-info" ' +
		  'onclick="downloadXls(\'' + rowObject.docver + '\');">下載EXCEL</button>' 
		if(rowObject.module === "非制式合約") {
			_module = 'NSC';
			cmd = '<button id="btnDownloadXls" type="button" class="btn btn-sm btn-info" ' +
			  'onclick="downloadXls(\'' + rowObject.docver + '\');">下載EXCEL</button>' 
		} else if(rowObject.module === "制式合約") {
			_module = 'SC';
			if(rowObject.disp == "基本資料"){
				cmd = '<button id="btnDownloadXls" type="button" class="btn btn-sm btn-info" ' +
				  'onclick="downloadXls(\'' + rowObject.docver + '\');">下載EXCEL</button>' 
			} else {
				cmd = '<button id="btnDownloadPdf" type="button" class="btn btn-sm btn-success" ' +
				  'onclick="previewPdf(\'' + rowObject.docver +'\',\''+ _module +'\',\''+ rowObject.year +'\');">預覽</button>' + 
				  '<button id="btnDownloadXls" type="button" class="btn btn-sm btn-info" ' +
				  'onclick="downloadXls(\'' + rowObject.docver + '\');">下載EXCEL</button>' 
			}
		}
			
        return cmd;
	}
	
	
	<%-- 清單停用啟用按鈕 --%>
	function girdCmds(cellvalue, options, rowObject) {
		var cmd = '', _module = '';
		if(rowObject.prdtime == null && rowObject.droptime == null){
			cmd = cmd + '<button id="btnRelease" type="button" class="btn btn-sm btn-primary" ' +
			'onclick="toRelease(\'' + rowObject.docver +'\');"><spring:message code="contract.field.release"/></button>' 
    		   		
		} else if(rowObject.prdtime != null &&  rowObject.droptime == null) {
			if(rowObject.disp === "基本資料") {
				cmd = cmd + '<button id="btnStop" type="button" class="btn btn-sm btn-danger" ' +
		   		'onclick="toStop(\'' + rowObject.docver +'\');"><spring:message code="contract.field.stop"/></button>';
 			} else {
 				cmd = cmd + '<button id="btnStop" type="button" class="btn btn-sm btn-danger" ' +
 		   		'onclick="toStop(\'' + rowObject.docver +'\');"><spring:message code="contract.field.stop"/></button>' + 
 		   		'<button id="btnCompare" type="button" class="btn btn-sm btn-info" ' +
 		   		'onclick="toPdfVerCompare(\'' + rowObject.docver + '\');"><spring:message code="common.compare.version"/></button>';
 			}
 		} 
		
        return cmd;
	}

	function toRelease(docver) {
		$.fn.confirm('<spring:message code="contract.confirm.release"/>', function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/contractTemplate/manage/release"/>");
			ajax.put("docver", docver);
			ajax.call(function(result) {
				if (result) {
					$.fn.alert(result.messages, function() {
						$("#btnQuery").click();
					});
				}
			});
		});
	}
	
	function toStop(docver) {
		$.fn.confirm('<spring:message code="contract.confirm.stop"/>', function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/contractTemplate/manage/stop"/>");
			ajax.put("docver", docver);
			ajax.call(function(result) {
				if (result) {
					$.fn.alert(result.messages, function() {
						$("#btnQuery").click();
					});
				}
			});
		});
	}
	
	function toPdfVerCompare(docver) {
		$("#docver").val(docver);
		$("#compareForm").submit();
	}
	 
	function downloadXls(docver){
		window.location.href = "../../contractTemplate/manage/downloadXls?docver=" + docver;
	}
	
	function previewPdf(docver) {
		$("#dataFormToViewDocver").val(docver);
		$("#dataFormToView").submit();
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contractTemplate/manage/toUpload' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<!-- 定型化契約 -->
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.module"/></label>
								<div class="col-sm-9">
									<combox:xauth id="module" name="module" class="form-control select2-single" 
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.docstatus"/></label>
								<div class="col-sm-9">
									<combox:xauth id="docstatus" name="docstatus" class="form-control select2-single"
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="TEMPLATE_STATUS_CODE" />
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
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
				<form id="compareForm" name="compareForm" class="forms-sample" action="<c:url value='/contractTemplate/manage/toPdfVerCompare' />" method="POST">
					<input type="hidden" id="docver" name="docver">
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</form>
				<form id="dataFormToView" method="POST" action="<c:url value='/contractTemplate/manage/editView'/>">
					<input type="hidden" id="dataFormToViewDocver" name="docver" />
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>
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
