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

    var datalist = null;
	var grid;
	var srt = '${resultinitdata.srt}';
	var module =  '${resultinitdata.module}'
	var inputsrt = '';
	var inputmodule = '';
	var setmodule = "";
	var year = '${resultinitdata.year}'
	var timeframe="";
	
	$(function () {
		$('#year').val()
		$('#statisticalReportType').change(function(){
			inputsrt = $('#statisticalReportType').val();
			if(srt != inputsrt || srt == '' ){
				$("#SRT").val($(this).val())
				$('#hideyear').val($('#year').val());
				$('#srtform').submit();	
			}
		});
		
		$('#module').change(function(){
			inputmodule = $('#module').val();
				if((inputsrt != '' || inputsrt != null) && inputsrt == 'RS' && module != inputmodule ){
				$('#hidemodule').val($('#module').val());
				$("#SRT").val($('#statisticalReportType').val())
				$('#hideyear').val($('#year').val());
				$('#srtform').submit();	
			}
		});
		
		$('#year').val(year)
		initData();
		createGrid();
		DownloadSR();

		$("#btnQuery").on("click", function() {	
			$('#btnDownload').removeAttr("disabled");
			//審核日期區判定
			var _flag = checkDate();
			if(_flag === true){
				if($("#dataForm").valid()) {
					$("#moduleName").val($("#module").val() == "" ? "" : $("#module").find(":selected").text());
					timeframe=$('#contractBgnDate').val() + "~" + $('#contractEndDate').val()
					//CD RS NTS
					grid.putParams("year", $('#year').val());
					grid.putParams("statisticalReportType", $('#statisticalReportType').val());
					grid.putParams("module", $('#module').val());
					grid.putParams("moduleName", $("#moduleName").val());
					grid.putParams("contractBgnDate", $('#contractBgnDate').val()); // 送審日期起
					grid.putParams("contractEndDate", $('#contractEndDate').val()); // 送審日期起
					grid.putParams("statisticalReportType", $("#statisticalReportType").val());

					grid.load(function(data){
						datalist = data.rows;
						setmodule = $('#module').val();
					});
				}
			}
		});
	});
	
	//初始化日期
	function initData() {
		var Bngday = new Date();
		var Today = new Date();
		var Endday = Today.getFullYear() + "/" + ((Today.getMonth()+1) < 10 ? '0' : '') + (Today.getMonth() + 1 ) + "/"+((Today.getMonth() + 1 ) + "/"+Today.getDate() < 10 ? ("0"+Today.getDate()) : Today.getDate()); 
		Bngday.setMonth(Bngday.getMonth() - 12 );
		var Bngday = Bngday.getFullYear() + "/" + ((Bngday.getMonth() + 1) < 10 ? '0' : '') + (Bngday.getMonth() + 1 ) + "/" + (Bngday.getDate() < 10 ? ("0" + Bngday.getDate()) : Bngday.getDate());

		$('#contractEndDate').val(Endday)
		$('#contractBgnDate').val(Bngday)
	}
	//報表紀錄產生
	function DownloadSR(){
		$('#btnDownload').click(function(){
			$('#btnDownload').attr('disabled', "true");
      		var createdate = new Date();
			createdate = createdate.getFullYear() + "/" + ((createdate.getMonth() + 1) < 10 ? '0' : '') + (createdate.getMonth() +1 )+"/"+((createdate.getMonth() +1 ) + "/" + createdate.getDate() < 10 ? ("0"+createdate.getDate()) : createdate.getDate()); 
			if(datalist != null){
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/statisticalReport/list/insertData" />");
				ajax.put("paramsData", datalist);
				ajax.put("createuser", '${principal.userCname}');
				ajax.put("module",setmodule);
				ajax.put("statisticalReportType", $('#statisticalReportType').val());
				ajax.put("timeFrame", timeframe);
				ajax.call(function(response) {
					$.fn.alert('請於下載區查看');
				});
			}
			else			
			$.fn.alert('請查詢後下載');
		});
	}
	//針對報表種類產生grid
	function createGrid(){
		//CD案件資料
		if(srt == "CD"){
			inputsrt = 'CD';
			$("#statisticalReportType option[value='CD']").prop('selected' , 'selected');
			$("#statisticalReportType").trigger("change");
			
			grid = $("#dataGrid").tvGrid();	
			grid.addColumn({ label: '<spring:message code="contract.field.contractor"/>', name: '<spring:message code="contract.field.contractor"/>', width: 90, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: '<spring:message code="contract.field.module"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.no"/>', name: '<spring:message code="contract.field.no"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.name"/>', name: '<spring:message code="contract.field.name"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.submit"/>', name: '<spring:message code="contract.field.submit"/>', width: 100, sortable: false });
			grid.addColumn({ label: '合約狀態', name: 'Flowstatus', width: 100, sortable: false });
			grid.addColumn({ label: '目前關卡', name: '<spring:message code="contract.field.docstatus"/>', width: 100, sortable: false });
			grid.showExportXls('N');
			grid.showExportPdf('N');
			grid.init();
			grid.setController("<c:url value='/statisticalReport/list/query' />");
		}
		//合約類型比例
		else if(srt == "NTS"){
			inputsrt = 'NTS';
			$("#statisticalReportType option[value='NTS']").prop('selected', 'selected');
			$("#statisticalReportType").trigger("change");
			$("#module option[value='NSC']").prop('selected', 'selected');
			$("#module").trigger("change");
			$('#module').attr('disabled',true);
			grid = $("#dataGrid").tvGrid();	
			grid.addColumn({ label: '合約類型', name: 'ContractType', width: 100, sortable: false });
			grid.addColumn({ label: '件數', name: 'count', width: 100, sortable: false });
			grid.showExportXls('N');
			grid.showExportPdf('N');
			grid.init();
			grid.setController("<c:url value='/statisticalReport/list/query' />");
		}
		//審核進度
		else if(srt == "RS" && (module == 'SC' || module == 'NSC') ){
			inputsrt = 'RS';
			grid = $("#dataGrid").tvGrid();	
			grid.addColumn({ label: '<spring:message code="contract.field.contractor"/>', name: '<spring:message code="contract.field.contractor"/>', width: 90, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: '<spring:message code="contract.field.module"/>', width: 100, sortable: false });
			grid.addColumn({ label: '件數', name: 'Count', width: 90, sortable: false });
			grid.addColumn({ label: '製作編輯中', name: 'todoEdit', width: 100, sortable: false });
			if(module == 'SC'){
				inputmodule = 'SC';
				$("#statisticalReportType option[value='RS']").prop('selected', 'selected');
				$("#statisticalReportType").trigger("change");
				$("#module option[value='SC']").prop('selected', 'selected');
				$("#module").trigger("change");
				grid.addColumn({ label: '廠商簽核中', name: 'Supplier', width: 100, sortable: false });
				grid.addColumn({ label: '總檢核數', name: 'TotalReview', width: 100, sortable: false });
				grid.addColumn({ label: '處長審核中', name: 'Director', width: 100, sortable: false });
				grid.addColumn({ label: '後勤處長審核中', name: 'logisticalDirector', width: 100, sortable: false });
				grid.addColumn({ label: '店長審核中', name: 'StoreManager', width: 100, sortable: false });
				grid.addColumn({ label: '總監審核中', name: 'Officer', width: 100, sortable: false });	
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.LegalSignature"/>', name: '<spring:message code="statisticalreport.field.LegalSignature"/>', width: 100, sortable: false });
			}
			else if(srt == "RS" && module == 'NSC'){
				inputmodule = 'NSC';
				$("#statisticalReportType option[value='RS']").prop('selected', 'selected');
				$("#statisticalReportType").trigger("change");
				$("#module option[value='NSC']").prop('selected', 'selected');
				$("#module").trigger("change");
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.Legalreview"/>', name: '<spring:message code="statisticalreport.field.Legalreview"/>', width: 100, sortable: false });
				grid.addColumn({ label: '廠商簽核中', name: 'Supplier', width: 100, sortable: false });
				grid.addColumn({ label: '總檢核數', name: 'TotalReview', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.StoreManager"/>', name: '<spring:message code="statisticalreport.field.StoreManager"/>', width: 90, sortable: false });
				grid.addColumn({ label: '組織最高主管(OSD分區)', name: 'OSD', width: 90, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.ChiefLegalOfficer"/>', name: '<spring:message code="statisticalreport.field.ChiefLegalOfficer"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.Legalcounsel"/>', name: '<spring:message code="statisticalreport.field.Legalcounsel"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.Purchasing"/>', name: '<spring:message code="statisticalreport.field.Purchasing"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.AssetManager"/>', name: '<spring:message code="statisticalreport.field.AssetManager"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.ITmanager"/>', name: '<spring:message code="statisticalreport.field.ITmanager"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.CHO"/>', name: '<spring:message code="statisticalreport.field.CHO"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.DOS"/>', name: '<spring:message code="statisticalreport.field.DOS"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.CMAssetDirector"/>', name: '<spring:message code="statisticalreport.field.CMAssetDirector"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.FinancialDirector"/>', name: '<spring:message code="statisticalreport.field.FinancialDirector"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.GeneralManager"/>', name: '<spring:message code="statisticalreport.field.GeneralManager"/>', width: 100, sortable: false });
				grid.addColumn({ label: '<spring:message code="statisticalreport.field.LegalSignature"/>', name: '<spring:message code="statisticalreport.field.LegalSignature"/>', width: 100, sortable: false });
			}
			grid.addColumn({ label: '退件', name: 'todoreject', width: 100, sortable: false });
			grid.addColumn({ label: '歸檔', name: 'todoArchive', width: 100, sortable: false });
			grid.showExportXls('N');
			grid.showExportPdf('N');
		    grid.init();
			grid.setController("<c:url value='/statisticalReport/list/query' />");
		}
		else if(srt == "ACD"){
			inputsrt = 'ACD';
			$("#statisticalReportType option[value='ACD']").prop('selected' , 'selected');
			$("#statisticalReportType").trigger("change");
			
			grid = $("#dataGrid").tvGrid();	
			grid.addColumn({ label: '<spring:message code="contract.field.no"/>', name: '<spring:message code="contract.field.no"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: '<spring:message code="contract.field.module"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.name"/>', name: '<spring:message code="contract.field.name"/>', width: 100, sortable: false });

			grid.addColumn({ label: '<spring:message code="xauth.field.transfer.suppliergui"/>', name: '<spring:message code="xauth.field.transfer.suppliergui"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="suppliter.field.suppliercname"/>', name: '<spring:message code="suppliter.field.suppliercname"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.submit"/>', name: '<spring:message code="contract.field.submit"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.contractStatus"/>', name: '<spring:message code="contract.field.contractStatus"/>', width: 100, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.contractor"/>', name: '<spring:message code="contract.field.contractor"/>', width: 90, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.principal"/>', name: '<spring:message code="contract.field.principal"/>', width: 90, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.undertakeTask"/>', name: '<spring:message code="contract.field.undertakeTask"/>', width: 90, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.undertakeAction"/>', name: '<spring:message code="contract.field.undertakeAction"/>', width: 90, sortable: false });
			grid.addColumn({ label: '<spring:message code="contract.field.undertakeSenfDate"/>', name: '<spring:message code="contract.field.undertakeSenfDate"/>', width: 90, sortable: false });

			grid.showExportXls('N');
			grid.showExportPdf('N');
			grid.init();
			grid.setController("<c:url value='/statisticalReport/list/query' />");
		}
		else if(srt == "RS" && (module != 'SC')){
			inputsrt = 'RS';
			$("#statisticalReportType option[value='RS']").prop('selected', 'selected');
			$("#statisticalReportType").trigger("change");
		}
	}
	// 日期範圍檢查
	function  checkDate() {
		var BgnDate = $('#contractBgnDate').val().split('/');
		var EndDate = $('#contractEndDate').val().split('/');
		var year = $('#year').val();
		
		if($.trim(BgnDate) == "" && $.trim(EndDate) == "" && $.trim(year) == "") {
			$.fn.alert("請填送審日期或合約年度二選一");
			return false;
		}
		if($.trim(BgnDate) != "" && $.trim(EndDate) == "") {
			$.fn.alert("請填送審日期迄");
			return false;
		}
		if($.trim(BgnDate) == "" && $.trim(EndDate) != "") {
			$.fn.alert("請填送審日期起");
			return false;
		}
			
		BgnDate = new Date(BgnDate[0], BgnDate[1]-1, BgnDate[2]);
		if(Date.parse(BgnDate) - Date.parse(EndDate) > 0 ){
			$.fn.alert("送審日期迄日需大於起日");
			return false;
		}
		BgnDate.setMonth(BgnDate.getMonth() + 12);
		EndDate = new Date(EndDate[0], EndDate[1] - 1, EndDate[2]);
		Date.parse(BgnDate);
		Date.parse(EndDate);
		
		if((BgnDate - EndDate) < 0){
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
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.submit"/>起</label>
								<div class="col-sm-9 ">
									<input type="text" id="contractBgnDate" name="contractBgnDate"
										class="form-control date">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.submit"/>迄</label>
								<div class="col-sm-9">
									<input type="text" id="contractEndDate" name="contractEndDate"
										class="form-control date">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.year" /></label>
								<div class="col-sm-9">
									<input type="text" id="year" name="year"
										class="dateY form-control">
								</div>
								<label class="col-form-label">年</label> 
							</div>
						</div>
					</div>
					
					<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query"/>
					</button>					
					<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
						<spring:message code="common.clear"/>
					</button>
						<button id="btnDownload" type="button" class=" btn btn-outline-info btn-fw">
						下載
					</button>
				</form>
			</div>
		</div>
	</div>			
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<div >
					<table id="dataGrid"></table>
				</div>
			</div>
		</div>
	</div>
	<form id="srtform" action="<c:url value='/statisticalReport/list/' />" method="post">
  		<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
  		<input type="hidden" id="hidemodule" name="hidemodule"/>
  		<input type="hidden" id="hideyear" name="hideyear"/>
        <input type="hidden" id="SRT" name="statisticalReportType" />
    </form>
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