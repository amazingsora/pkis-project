<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />
<link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>
<script type="text/javascript">
	var grid;
	$(function () {
		selectmodule();
		grid = $("#dataGrid").tvGrid();			
		grid.addColumn({ label: '<spring:message code="statisticalreport.field.CName"/>', name: 'RPTCName', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="statisticalreport.field.creater"/>', name: 'CREATEUSER', width: 100, sortable: false});
		grid.addColumn({ label: '<spring:message code="statisticalreport.field.CreateTime"/>', name: 'CREATEDATE', width: 100, sortable: false});
		grid.addColumn({ label: '<spring:message code="statisticalreport.field.DownLoad"/>', width:100, exportcol:false,sortable: false, formatter:girdCmds}); 
		grid.setController("<c:url value='/statisticalReport/downloadList/query' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {	
			if($("#dataForm").valid()) {
				$("#moduleName").val($("#module").val() == "" ? "" : $("#module").find(":selected").text());
				grid.putParams("statisticalReportType", $('#statisticalReportType').val());
				grid.putParams("module", $('#module').val());
				grid.putParams("moduleName", $("#moduleName").val());
				grid.putParams("statisticalReportTypeName", $("#statisticalReportType").find(":selected").text());
				grid.load(function(data){});
			}
		});
	});
	
	function girdCmds(cellvalue, options, rowObject) {
		var cmd = '';
		if(rowObject.DownloadPath != null){
			var rptId = rowObject.RPTID;
			cmd += '<button id="btnDownlod" type="button" class="btn btn-sm btn-success" ' +
				' onClick="downloadExcel(\'' + rptId + '\');">下載</button>';                                                           
		}
		return cmd;
	}
	//下載檔案
	function downloadExcel(rptId){
		window.location.href = "../../statisticalReport/downloadList/download?rptId=" + rptId;
	}
	//選擇合約類型
	function selectmodule(){
		$("#statisticalReportType").change(function(){
			//判斷非制式合約種類 強制選擇非制式
			if($(this).val()=="NTS"){
				$("#module option[value='NSC']").prop('selected' , 'selected');
				$("#module").trigger("change");
				$('#module').attr('disabled',true);
			}
			else{
				$('#module').removeAttr("disabled");
			}
		});
	}
	// 日期檢核
	function  checkDate() {
		var BgnDate=$('#contractBgnDate').val().split('/');
		var EndDate=$('#contractEndDate').val().split('/');
		BgnDate = new Date(BgnDate[0], BgnDate[1]-1, BgnDate[2]);
		if(Date.parse(BgnDate) - Date.parse(EndDate) > 0 ){
			$.fn.alert("送審日期迄日需大於起日");
			return false;
		}
		BgnDate.setMonth(BgnDate.getMonth() + 12);
		EndDate = new Date(EndDate[0], EndDate[1]-1, EndDate[2]);
		Date.parse(BgnDate);
		Date.parse(EndDate);	
		if((BgnDate-EndDate)<0){
			$.fn.alert("送審日期區間不可超過半年");
			return false;
		}	
			return true;
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='Controller' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<input type="hidden" id="btnType" name="btnType"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label">報表類型</label>
								<div class="col-sm-9">
								<combox:xauth id="statisticalReportType" name="statisticalReportType"
									class="form-control select2-single" xauthType="x-statisticalreport-type"
									listKey="code" listValue="cname" gp="STATISTIC_REPORT" required="required"/>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.module" /></label>
								<div class="col-sm-9">
									<combox:xauth id="module" name="module" class="form-control select2-single" xauthType="x-sys-code"
										listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required" />
									<input type="hidden" id="moduleName" name="moduleName" />
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
				<div >
					<table id="dataGrid"></table>
					<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>
