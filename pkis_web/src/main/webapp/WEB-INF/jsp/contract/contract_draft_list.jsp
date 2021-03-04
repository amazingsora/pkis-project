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
	var contractor = '';
	var grid;
	$(function () {
		initData();
		getSuppliercode();		
		getSuppliername();		
		getSupplierGUI();		
		
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '<spring:message code="contract.field.action"/>', width:100, exportcol:false, sortable: false, formatter:girdCmds});
		grid.addColumn({ label: '<spring:message code="contract.field.no"/>', name: '<spring:message code="contract.field.no"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.module"/>', name: '<spring:message code="contract.field.module"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.name"/>', name: '<spring:message code="contract.field.name"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.suppliergui"/>', name: '<spring:message code="suppliter.field.suppliergui"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.suppliercname"/>', name: '<spring:message code="suppliter.field.suppliercname"/>', width: 150, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.updtime"/>', name: '<spring:message code="contract.field.updtime"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.docstatus"/>', name: '<spring:message code="contract.field.docstatus"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.contractor"/>', name: '<spring:message code="contract.field.contractor"/>', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'deptCName', width: 150, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'isNowUser', width: 100, sortable: false, hidden: true });
		grid.addColumn({ label: '<spring:message code="contract.field.section"/>', name: 'indexName', width: 100, sortable: false, hidden: true });
		grid.addColumn({ label: 'deptNo', name: 'deptNo', width: 150, sortable: false , hidden: true });
		grid.addColumn({ label: 'flowId', name: 'flowId', width: 150, sortable: false , hidden: true });
		grid.addColumn({ label: 'todo', name: 'todo', width: 100, sortable: false , hidden: true }); 
		grid.setController("<c:url value='/contract/draft/query' />");
		grid.showExportXls('N');
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {	
			//審核日期區判定
			var _flag = checkDate();
			if(_flag === true){
				if($("#dataForm").valid()) {
					$("#moduleName").val($("#module").val() == "" ? "" : $("#module").find(":selected").text());
					grid.putParams("year", $('#year').val());
					grid.putParams("module", $('#module').val());
					grid.putParams("moduleName", $("#moduleName").val());
					grid.putParams("suppliercode", $('#suppliercode').val());
					grid.putParams("suppliername", $('#suppliername').val());
					grid.putParams("suppliergui", $('#GUIId').val());
					grid.putParams("deptno", $('#section').val()); // 課別
					grid.putParams("contractBgnDate", $('#contractBgnDate').val()); // 送審日期起
					grid.putParams("contractEndDate", $('#contractEndDate').val()); // 送審日期起
					grid.load();
				}
			}
		});
	});
	function initData() {
		var Bngday=new Date();
		var Today=new Date();
		var Endday=Today.getFullYear()+"/"+((Today.getMonth()+1)<10 ? '0' : '')+(Today.getMonth()+1)+"/"+((Today.getMonth()+1)+"/"+Today.getDate() < 10 ? ("0"+Today.getDate()) : Today.getDate()); 
		Bngday.setMonth(Bngday.getMonth()-12);
		var Bngday=Bngday.getFullYear()+"/"+((Bngday.getMonth()+1)<10 ? '0' : '')+(Bngday.getMonth()+1)+"/"+
		(Bngday.getDate() < 10 ? ("0"+Bngday.getDate()) : Bngday.getDate());

		$('#contractEndDate').val(Endday)
		$('#contractBgnDate').val(Bngday)
	}
	
	// 日期
		function  checkDate() {
			var BgnDate=$('#contractBgnDate').val().split('/');
			var EndDate=$('#contractEndDate').val().split('/');
			BgnDate = new Date(BgnDate[0], BgnDate[1]-1, BgnDate[2]);
			if(Date.parse(BgnDate) - Date.parse(EndDate) > 0 ){
				$.fn.alert("建立日期迄日需大於起日");
				return false;
			}
			BgnDate.setMonth(BgnDate.getMonth() + 12);
			EndDate = new Date(EndDate[0], EndDate[1]-1, EndDate[2]);
			Date.parse(BgnDate);
			Date.parse(EndDate);
			
			if((BgnDate-EndDate)<0){
				$.fn.alert("建立日期區間不可超過半年");
				return false;
			}	
			return true;
	}

	//Grid Btn
	function girdCmds(cellvalue, options, rowObject) {
		var userCname = '<c:out value="${principal.userCname}"/>';
		var cmd = '';
		contractor = rowObject.承辦人員
		//草稿階段當前人員為承接人或是承辦人
		if(rowObject.承接人 !=  undefined & rowObject.承接人 !=  ""){
			contractor=rowObject.承接人
		}
		if(rowObject.isNowUser == 'Y'){
			//簽核 && 編輯
			 if(contractor == userCname){
				 cmd += '<button id="btnEdit" type="button" class="btn btn-sm btn-success" ' +
		 			'onclick="toView(\'' + options.rowId +'\', \'2\');">編輯</button>';
			 }else{
				 cmd += '<button id="btnFlow" type="button" class="btn btn-sm btn-success" ' +
		 			'onclick="toView(\'' + options.rowId +'\', \'1\');">簽核</button>';
			 }
        }
		else if(userCname=="admin"){
			if(rowObject.狀態=="END"){
				cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
	    		'onclick="toView(\'' + options.rowId +'\', \'0\');">檢視</button>';
		}
		else{
			 cmd += '<button id="btnFlow" type="button" class="btn btn-sm btn-success" ' +
	 			'onclick="toView(\'' + options.rowId +'\', \'1\');">簽核</button>';	
		}}
			else {
            if(contractor == userCname){
            	if(rowObject.todo == '暫存' || rowObject.todo == '新建'){
        			cmd += '<button id="btnEdit" type="button" class="btn btn-sm btn-success" ' +
		 			'onclick="toView(\'' + options.rowId +'\', \'2\');">編輯</button>';
        		} else {
       	    		cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
    	    		'onclick="toView(\'' + options.rowId +'\', \'0\');">檢視</button>';
        		}
        	} else {
        		//檢視
           		cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
           		'onclick="toView(\'' + options.rowId +'\', \'0\');">檢視</button>';
        	}
        }
		
		return cmd;
	}
	
	//檢視 && 簽核 && 編輯
	function toView(rid, btnType) {
		$("#btnType").val(btnType);
		$('#keyData').val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
	<!--供應商廠編輸入並生成下拉式選單-->
	function getSuppliercode(){
		var keyinTime;//此為需要事先定義要記錄的定時器
		$("#suppliercode").on('keyup',function (e) {  
		  var v = $(this).val();
		  e.preventDefault();
		  clearTimeout(keyinTime);//清除計時的事件 待回傳時間為1.3秒
		  keyinTime = setTimeout(function(){ 
		      if(v.length > 1){
		    	  $("#suppliercode-browsers").empty();
		    		var ajax = new $.tvAjax();
		    		ajax.setController("<c:url value="/contract/list/getSupplieridList"/>");
		    		ajax.put("suppliercode", $('#suppliercode').val());
		    		ajax.call(function(dataList) {
		    			if (dataList != null) {
		    				for(i=0;i<dataList.result.supplierCodeList.length;i++){
		    					$("#suppliercode-browsers")
		    					.append($("<option>").prop({"value":dataList.result.supplierCodeList[i].suppliercode}).append(dataList.result.supplierCodeList[i].suppliercode));
		    				}			
		    			}
		    		});	
		      }
		  },1300);
		});
	}
	
	<!--供應商名稱輸入並生成下拉式選單-->
	function getSuppliername(){
		var keyinTime;
		$("#suppliername").on('keyup',function (e) {  
		  var v = $(this).val();
		  e.preventDefault();
		  clearTimeout(keyinTime);//清除計時的事件 待回傳時間為1.3秒
		  keyinTime = setTimeout(function(){ 
		      if(v.length > 1){
		    	  $("#suppliername-browsers").empty();
		    		var ajax = new $.tvAjax();
		    		ajax.setController("<c:url value="/contract/list/getSuppliernameList"/>");
		    		ajax.put("suppliername", $('#suppliername').val());
		    		ajax.call(function(dataList) {

		    			if (dataList != null) {
		    				for(i=0;i<dataList.result.supplierCodeList.length;i++){
		    					$("#suppliername-browsers")
		    					.append($("<option>").prop({"value":dataList.result.supplierCodeList[i].suppliercname}).append(dataList.result.supplierCodeList[i].suppliercname));
		    				}			
		    			}
		    		});	
		      }	
		  },1300);
		});
	}
	
	<!--供應商名稱輸入並生成下拉式選單-->
	function getSupplierGUI(){
		var keyinTime;
		$("#GUIId").on('keyup',function (e) {  
		  var v = $(this).val();
		  e.preventDefault();
		  clearTimeout(keyinTime);//清除計時的事件 待回傳時間為1.3秒
		  keyinTime = setTimeout(function(){ 
		      if(v.length > 1){
		    	  $("#GUIId-browsers").empty();
		    		var ajax = new $.tvAjax();
		    		ajax.setController("<c:url value="/contract/list/getSupplierGUIList"/>");
		    		ajax.put("GUIId", $('#GUIId').val());
		    		ajax.call(function(dataList) {
		    			console.log(dataList);
		    			if (dataList != null) {
		    				for(i=0;i<dataList.result.supplierGUIList.length;i++){
		    					$("#GUIId-browsers")
		    					.append($("<option>").prop({"value":dataList.result.supplierGUIList[i].suppliergui}).append(dataList.result.supplierGUIList[i].suppliergui));
		    				}			
		    			}
		    		});	
		      }		
		  },1300);
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
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/list/view' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<input type="hidden" id="btnType" name="btnType"/>
					<div class="row">
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
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.updtime"/>起</label>
								<div class="col-sm-9 ">
									<input type="text" id="contractBgnDate" name="contractBgnDate"
										class="form-control date" required="required">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.updtime"/>迄</label>
								<div class="col-sm-9">
									<input type="text" id="contractEndDate" name="contractEndDate"
										class="form-control date" required="required">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.suppliercode" /></label>
								<div class="col-sm-9">
									<input list="suppliercode-browsers" id="suppliercode" class="form-control">
									<datalist id="suppliercode-browsers">
									</datalist>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label">供應商名稱</label>
								<div class="col-sm-9">
									<input list="suppliername-browsers" id="suppliername" class="form-control" >
									<datalist id="suppliername-browsers">
									</datalist>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message
										code="contract.field.section" /> </label>
								<div class="col-sm-9">
									<combox:xauth id="section" name="section"
										class="form-control select2-single" xauthType="x-sys-code"
										listKey="code" listValue="cname" gp="DEPT_CODE"
										 />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message
										code="suppliter.field.suppliergui" /></label>
								<div class="col-sm-9">
									<input list="GUIId-browsers" id="GUIId" class="form-control"> 
									<datalist id="GUIId-browsers">
									</datalist>
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