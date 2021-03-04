<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<%@include file="../xauth/xauth_transfer_Dialog.jsp" %>
<sec:authentication var="principal" property="principal" />
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/v2/Shared/ValidateTreeView.js"/>"></script>
<link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">
<script type="text/javascript">
	var datalist=null;
	var grid;
	$(function() {
	
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '勾選', width:60, exportcol:false,sortable: false, formatter:girdChcekBoxs});
		grid.addColumn({ label: '<spring:message code="contract.field.year"/>', name: '<spring:message code="contract.field.year"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contractReview.field.contractmodel"/>', name: '<spring:message code="contractReview.field.contractmodel"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="contract.field.dispname"/>', name: '<spring:message code="contract.field.dispname"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.transfer.section"/>', name: '<spring:message code="xauth.field.transfer.section"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="suppliter.field.suppliergui"/>', name: '<spring:message code="suppliter.field.suppliergui"/>', width: 150, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.transfer.suppliercode"/>', name: '<spring:message code="xauth.field.transfer.suppliercode"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.transfer.suppliercname"/>', name: '<spring:message code="xauth.field.transfer.suppliercname"/>', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.transfer.contractcount"/>', name: 'Count', width: 90, sortable: false });
		grid.addColumn({ label: 'Contracts', name: 'Contracts', width: 100, sortable: false, hidden: true });
		//原角色資訊

		grid.setController("<c:url value='/xauth/transfer/query' />");		
// 		grid.showExportXls('N');
		grid.showExportPdf('N');
		
		grid.init();
		getSection();
		
		$("#idenId").on("change", function() {
			getRoleList();
		});
		
		$("#btntransfer").on("click", function() { 
			var datasize = 0;
			if(datalist != null && datalist.length > 0){
				var resWaitRows =  $("#dataGrid").find("tbody > tr");
	    		$.each(resWaitRows, function (i, rowObj) {
					var $rowChkObj = $(rowObj).find("td:eq(0) > input[type='checkbox']");
					if($rowChkObj.prop("checked")){
						datasize++;
					}
			     });	 
    			$("input:radio[name='slectscope']").eq(1).attr("checked",false);
    			$("input:radio[name='slectscope']").eq(0).attr("checked",false);

	    		if(datasize == 0){
	    			$("input:radio[name='slectscope']").eq(1).attr("checked",true);
	    		}
	    		else{
	    			$("input:radio[name='slectscope']").eq(0).attr("checked",true);

	    		}
	    		$('#TransferDialog').modal('show');
			}
			else{
				$.fn.alert("尚無合約轉移");
			}
		});	
		
		$("#btnQuery").on("click", function() {	
			if($("#dataForm").valid()) {
				var sections=$("#sections").val();
				$("#section").val(sections);
				grid.putParams("userId", $('#userId').val());
				grid.putParams("idenId", $('#idenId').val());
				grid.putParams("roleId", $("#roleId").val());
				grid.putParams("years", $("#years").val());
				grid.putParams("section", $("#section").val());
				grid.putParams("suppliergui", $("#suppliergui").val());
				grid.putParams("suppliercode", $("#suppliercode").val());
				grid.putParams("module", $("#module").val());
				grid.load(function(data){
					datalist= data.rows;
					console.log(datalist);
					setmodule=$('#module').val();
				});
			}
		});	
		
		
		$('#module').change(function(){
			if($('#module').val()=='NSC'){
				$('#sections').empty();
			}
			getSection();
			var module = $("#module").val();
			$("#sectionDiv").show();
			if(module == 'NSC'){
				$('#suppliercode').empty();
				$('#suppliergui').empty();
				$("#sectionDiv").hide();
			} else {
				$('#suppliercode').empty();
				$('#suppliergui').empty();
				$("#sectionDiv").show();
			} 
		});
		
		$("#years").datepicker({
			multipleDates: true
		});
		
	});	
	//取得課別
	function getSection(){
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/xauth/transfer/getAllSection"/>");
		ajax.call(function(dataList){
			if (dataList!= null) {
				$.each(dataList,function() {
					var data = this;
					$("#sections").append($("<option>").prop({"value":data.CODE}).append(data.CODE+"-"+data.CNAME));
				});
			}
		});
	}
	//取得角色資訊
	function getRoleList(idenIdtag) {
		//用於modal用於查詢跳窗頁面的選單
		if(idenIdtag!="modal"){
			$('#roleId').empty();
			$("#roleId").append($("<option>").prop({"value":""}).append("請選擇"));
			var idenId = $('#idenId').val();
			if (idenId != null && idenId != '') {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/roleAgentUser/getRoleList"/>");
				ajax.put("idenId", idenId);	
				ajax.call(function(json) {
					if (json.result.dataList != null) {
						$.each(json.result.dataList,function() {
							var data = this;
							
							$("#roleId").append($("<option>").prop({"value":data.roleIdOri}).append(data.roleId + "-" + data.roleCname));
							<!--待回傳後入RoleId-->
						});
					}
				});
			}
		}
		else{
			$("#transferRoleid").empty();
			$("#transferRoleid").append($("<option>").prop({"value":""}).append("請選擇"));
			$("#transferUserid").empty();
			$("#transferUserid").append($("<option>").prop({"value":""}).append("請選擇"));
			var idenId = $('#transferidenId').val();
			if (idenId != null && idenId != '') {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/roleAgentUser/getRoleList"/>");
				ajax.put("idenId", idenId);	
				ajax.call(function(json) {
					if (json.result.dataList != null) {
						$.each(json.result.dataList,function() {
							var data = this;
							$("#transferRoleid").append($("<option>").prop({"value":data.roleIdOri}).append(data.roleId + "-" + data.roleCname));
							<!--待回傳後入RoleId-->
						});
					}
				});
			}
		}
	}
	//勾選項目
	function girdChcekBoxs(cellvalue, options, rowObject) {
        var cmd = '<input type="checkbox" id="checkbox_ '+ options.rowId +'" name="checkbox_ '+ options.rowId +'">' ;
        			
        return cmd;
	}

</script>

<div class="row">
		<div class="col-12 grid-margin stretch-card">
			<div class="card">
				<div class="card-body">				
					<header class="card-title">
						<spring:message code="xauth.field.update.contract"/>
					</header>
					<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/transfer/query' />" method="POST">
						<input type="hidden" id="keyData" name="keyData"/>
						<input type="hidden" id="section" name="section"/>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
									<label data-require=true class="col-sm-2 col-form-label"><spring:message code="xauth.field.transfer.person" /></label>
									<div class="col-sm-9">
										<input type="text" id="userId" name="userId" class="form-control" required/>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group row">
									
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
									<label class="col-sm-2 col-form-label"><spring:message code="xauth.field.transfer.position" /></label>
									<div id="divIdenId" class="col-sm-9">
										<combox:xauth id="idenId"  class="form-control select2-single" xauthType="x-iden-qry" listKey="idenId" listValue="struct" headerKey="請選擇" headerValue="" /> 
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group row">
									<label class="col-sm-0 col-form-label"></label>
									<div class="col-sm-9">
										<select id="roleId" name="roleId" class="form-control select2-single" >
											<option value="">請選擇</option>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
									<label  class="col-sm-2 col-form-label"><spring:message code="contract.field.year" /></label>
									<div class="col-sm-9">
										<input type="text" id="years" name="years"class="dateY form-control"  multiple="multiple">
											
									</div>
									<label class="col-form-label"></label> 
								</div>
							</div>
							<div class="col-md-6" id="sectionDiv">
								<div class="form-group row">
									<label class="col-sm-2 col-form-label"><spring:message
											code="contract.field.section" /> </label>
									<div class="col-sm-9">
										<select id="sections"  class="form-control select2-single"  multiple="multiple">
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
									<label  class="col-sm-2 col-form-label"><spring:message code="contract.field.module" /></label>
									<div class="col-sm-9">
										<combox:xauth id="module" name="module" class="form-control select2-single" xauthType="x-sys-code"
											listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE"  />
										<input type="hidden" id="moduleName" name="moduleName" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group row">
									<label  class="col-sm-2 col-from-label"><spring:message code="xauth.field.transfer.suppliergui"/></label>
									<div class="col-sm-9">
										<input type="text" id="suppliergui" name="suppliergui" id="suppliercode" name="suppliercode" class="form-control" placeholder="">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
								
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group row">
									<label  class="col-sm-2 col-from-label"><spring:message code="suppliter.field.suppliercode"/></label>
									<div class="col-sm-9">
										<input type="text" id="suppliercode" name="suppliercode" class="form-control" placeholder="">
									</div>
								</div>
							</div>
						</div>
						
						<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
							<spring:message code="common.query"/>
						</button>	
						<button id="btntransfer" type="button" class="btn  btn-outline-danger btn-fw">
							<spring:message code="common.tranfer"/>
						</button>				
						<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
							<spring:message code="common.clear"/>
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
	   					<div id="jqGridPager"></div>
					</div>
				</div>
			</div>
		</div>
</div>
