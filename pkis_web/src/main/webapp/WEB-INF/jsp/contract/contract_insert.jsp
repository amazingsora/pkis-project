<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/v2/Shared/ValidateTreeView.js"/>"></script>
<link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">
<script type="text/javascript">
var contractPrice=0
	$(function() {
		dispchangeevent();
		selectYear();
		convertContractAmount();
		
		$("#btnInsert").on("click", function() {
			if(!isContractPrice(contractPrice) || !checknumber ()){
				$.fn.alert("請填入正確合約金額");
				return;
			}
			if($("#dataForm").valid()) {
				var module = $("#module").val()
				if(module == 'NSC'){
					$("#section").val('');
				}
				var ajax = new $.tvAjax();
				$("#moduleName").val($("#module").val() == "" ? "" : $("#module").find(":selected").text());
				$("#dispName").val($("#disp").val() == "" ? "" : $("#disp").find(":selected").text());
				$("#sectionName").val($("#section").val() == "" ? "" : $("#section").find(":selected").text());
				$("#depName").val($("#depId").val() == "" ? "" : $("#depId").find(":selected").text());
				$("#supplieridName").val($("#supplierid").val() == "" ? "" : $("#supplierid").find(":selected").text());
				$("#idenId").val($("#depId").val());
				console.log($("#dataForm").serializeObject());
				if($("#templateType").val() === 'LT') {
					$.blockUI({
		                message: '<h3>讀取中...</h3>',
		                baseZ: 9999
		            });
					ajax.setController("<c:url value="/contract/list/checkBeforeContract" />");
					ajax.put("paramsData", $("#dataForm").serializeObject());
					ajax.setBlockUIDisabled(true);
					ajax.call(function(response) {
						console.log("response == ", response);
						if(response.status === "ok") {
							if(response.result.isJson == "Y") {
								$.unblockUI(); 
								$.fn.confirm(response.messages, function() {
									$("#isUseLastCon").val("Y");
									insertData();
								}, function() {
									insertData();
								});
							} else {
								insertData('Y');
							}
						} else {
							$.unblockUI();  
							$.fn.alert(response.messages);
						}
					});
				} else {
					insertData();
				}
			}
		});
		
		$('#module').change(function(){
			$("#contractName").val("");
			var module = $("#module").val();
			$("#sectionDiv").show();
			$("#contractName").attr("readonly", false);
			if(module == 'NSC'){
				$("#sectionDiv").hide();
				$("#contractName").attr("readonly", false);
			} else {
				$("#sectionDiv").show();
				$("#contractName").attr("readonly", true);
				$('#supplierid').val("");
				getContractName();
			} 
			getFlowList();
		});
		
		$('#section').change(function(){
			getSuppliermaster();
		});
		
		$("#disp").change(function() {
			getTemplateTypeList();
		});
		
		$('#supplierid').change(function() {
			getContractName();
		});
		
		$("#perTemplateDiv").hide();
		$("#templateType").change(function() {
			var module = $("#module").val();
			var disp = $("#disp").val();
			var templateType = $("#templateType").val();
			if(templateType === "PT" && module && disp) {
				$("#perTemplateDiv").show();
				getPerTemplateList();
			} else {
				$("#perTemplateDiv").hide();
			}
		});
		
		showSelectVal()
		getDeptList()
	});
	  
	function insertData(controlBlockUI) {
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/contract/list/insertData" />");
		ajax.put("paramsData", $("#dataForm").serializeObject());
		if(controlBlockUI === 'Y') ajax.setBlockUIDisabled(true);
		ajax.call(function(response) {
			console.log("response == ", response);
			if(response){
				$.fn.alert(response.messages, function(){
					if(response.status == "ok"){
						$("#btnType").val(response.result.btntype);
						$("#indexName").val(response.result.indexName);
						$("#dataId").val(response.result.dataId);
						$("#dataType").val(response.result.dataType)
						$("#dataFormToView").submit();
					}
				});
			}
			if(controlBlockUI === 'Y') $.unblockUI(); 
		});
	}
	//產生合約範本
	function getFlowList() {
		$('#disp').empty();
		$("#disp").append($("<option>").prop({"value":""}).append("請選擇"));
		var module = $('#module').val();
		if (module != null && module != '') {
			var ajax = new $.tvAjax();
			$.blockUI({
                message: '<h3>讀取中...</h3>',
                baseZ: 9999
            });
			ajax.setController("<c:url value="/contract/list/getFlowList"/>");
			ajax.put("module", module);
			ajax.setBlockUIDisabled(true);
			ajax.call(function(response) {
			
				if (response.result.dataList != null) {
					$.each(response.result.dataList,function() {
						var data = this;
						$("#disp").append($("<option>").prop({"value":data.flowid}).append(data.flowname));
					});
				}
				getSuppliermaster();
			});
		}		
	}
	//合約金額上限
	function dispchangeevent(){
		$('#disp').change(function(){
			if($('#disp').val() != null || $('#disp') != ""){
				//去掉前5碼
				contractPrice = $('#disp').val().slice(5);
				contractPrice = '0' + contractPrice;
				contractPrice = parseInt(contractPrice);
				contractPrice += "";
			}
			if(contractPrice.length > 1){
				contractPrice = parseInt(contractPrice);
			}
			else{
				contractPrice = 0;
			}
		});		
	}
	//合約金額檢核
	function isContractPrice(contractPrice){
		contractPrice += '';
		
		if(contractPrice.substr(contractPrice.length - 1) == 1){
			contractPrice = contractPrice.substr(0, contractPrice.length - 1);
			contractPrice += "0";
			contractPrice = parseInt(contractPrice);
			if($('#contractAmount').val() > contractPrice){
				return true;
			}
			return false;
		}
		else if(contractPrice.substr(contractPrice.length-1) == 0 && contractPrice > 0){
			contractPrice = parseInt(contractPrice);
			if($('#contractAmount').val() <= contractPrice){ 
				return true;
			}
			return false;
		}
		else {
			contractPrice = parseInt(contractPrice);
			if($('#contractAmount').val()>contractPrice){
				return true;
			}
			return false;
		}
	}
	//產生供應商資訊
	function getSuppliermaster(){
		$('#supplierid').empty();
		$("#supplierid").append($("<option>").prop({"value":""}).append("請選擇"));
		var section = $('#section').val();
		var module = $('#module').val();
		if(module == 'NSC'){
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/contract/list/getSuppliermaster"/>");
			ajax.put("module", module);
			ajax.call(function(response) {
				if (response.result.dataList != null) {
					$.each(response.result.dataList,function() {
						var data = this;
						$("#supplierid").append($("<option>").prop({"value":data.supplierid}).append(data.suppliergui +'-'+ data.suppliercode +'-'+ data.suppliercname));
					});
					$("#supplierid").trigger("change");
				}
				$.unblockUI(); 
			});
		} else {
			if (section != null && section != '') {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/contract/list/getSuppliermaster"/>");
				ajax.put("section", section);
				ajax.put("module", module);
				ajax.call(function(response) {
					if (response.result.dataList != null) {
						$.each(response.result.dataList,function() {
							var data = this;
							$("#supplierid").append($("<option>").prop({"value":data.supplierid}).data("name", data.suppliercname).append(data.suppliergui +'-'+ data.suppliercode +'-'+ data.suppliercname));
						});
						$("#supplierid").trigger("change");
					}
					$.unblockUI(); 
				});
			} else {
				$.unblockUI(); 
			}
		}
	}
	//產生部門資訊
	function getDeptList(){
		var $HTML = '';
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/contract/list/getDept"/>");
		ajax.call(function(response) {
			if (response.result.data != null) {
				var data = response.result.data;
				var size = data.length - 1;
				for(var i = size; i >= 0 ; i--){
					var val = data[i];
					$HTML += '<label>';
					if(i === size)
						$HTML += '</br><label> ' + val;
					else
						$HTML += '</br><label>|____ ' + val;
				}
			}
			$('#deptList').append($HTML);
		});
	}
	
	function showSelectVal(){
		var idenId;
		if('<c:out value="${principal.idenId}"/>'!=''){
			idenId = '<c:out value="${principal.idenId}"/>';
			$("#depId option[value=" + idenId +"]").prop('selected' , 'selected');
			$("#depId").trigger("change");
		}
	}
	
	function getTemplateTypeList() {
		var module = $("#module").val();
		var disp = $("#disp").val();
		var ajax = new $.tvAjax();
		$("#templateType").empty();
		$("#templateType").append($("<option>").prop({"value":""}).append("請選擇"));
		ajax.setController("<c:url value="/contract/list/getTemplateTypeList" />");
		ajax.put("module", module);
		ajax.put("disp", disp);
		ajax.call(function(response){
			$.each(response.result.dataList, function() {
				var data = this;
				$("#templateType").append($("<option>").prop({"value":data.code}).append(data.cname));
				if(module === "NSC"){
					 $("#templateType").val('LT');
					}
			});
			$("#templateType").trigger("change");
		});
	}
	
	function getPerTemplateList() {
		var module = $("#module").val();
		var disp = $("#disp").val();
		var ajax = new $.tvAjax();
		$("#perTemplateDataid").empty();
		$("#perTemplateDataid").append($("<option>").prop({"value":""}).append("請選擇"));
		ajax.setController("<c:url value="/contract/list/getPerConTemplateList" />");
		ajax.put("module", module);
		ajax.put("disp", disp);
		ajax.call(function(response){
			$.each(response.result.dataList, function() {
				var data = this;
				$("#perTemplateDataid").append($("<option>").prop({"value":data.dataid}).append(data.templatename));
			});
			$("#perTemplateDataid").trigger("change");
		});
	}
	//合約選取年度後自動設定好預設區間
	function selectYear(){
		$("#year").datepicker({
			autoClose: true,
		    onSelect: function(dateText) {
		    	if($("#year").val()==''){
		    		$('#contractBgnDate').val('');
					$('#contractEndDate').val('');
		    	}
		    	else{
		    		$('#contractBgnDate').val(dateText+"/01/01");
					$('#contractEndDate').val(dateText+"/12/31");
					getContractName();
				}
		    }
		});
	}
	//轉換千分位
	function convertContractAmount(){
		$("#displatcontractAmount").change(function(){
			var value = $("#displatcontractAmount").val();
			value = convertPerMill(value);
			$("#displatcontractAmount").val(value);
			//純數字化存入contractAmount
			value = convertnum(value);
			$("#contractAmount").val(parseInt(value));
			console.log("合約金 ==="+$("#contractAmount").val());
		});
	}
	
	function getContractName() {
		var year = $("#year").val();
		var module = $("#module").val();
		var supplierid = $("#supplierid").val();
		var suppliername = "";
	
		if(year != "" && module != "" && supplierid != ""){
			if(module == "SC"){
				suppliername = $("#supplierid").find('option:selected').data('name');
				$("#contractName").val(year + suppliername);
			}
		}
	}
	function checknumber () {
		var disp = $("#displatcontractAmount").val();
		disp = convertnum(disp);
		var reg = /^[0-9]+.?[0-9]*$/
			if (reg.test(disp)) {
				return true
			}
		return false
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:out value="${menuName}" />-新增
				</header>
				<form id="dataForm"  method="POST">
				<input type="hidden" id="isUseLastCon" name="isUseLastCon" />
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.year" /></label>
								<div class="col-sm-9">
									<input type="text" id="year" name="year"
										class="dateY form-control" required="required">
								</div>
								<label class="col-form-label">年</label> 
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.date" /></label>
								<div class="col-sm-4">
									<input type="text" id="contractBgnDate" name="contractBgnDate"
										class="form-control date" required="required">
								</div>
								<label class="col-form-label">~</label>
								<div class="col-sm-4">
									<input type="text" id="contractEndDate" name="contractEndDate"
										class="form-control date" required="required">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.module" /></label>
								<div class="col-sm-9">
									<combox:xauth id="module" name="module"
										class="form-control select2-single" xauthType="x-sys-code"
										listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required" />
									<input type="hidden" id="moduleName" name="moduleName" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.dispname" /></label>
								<div class="col-sm-9">
									<select id="disp" name="disp" class="form-control select2-single" required>
										<option value="">請選擇</option>
									</select>
									<input type="hidden" id="dispName" name="dispName" />
								</div>
							</div>
						</div>
						<div class="col-md-6" id="sectionDiv">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.section" /></label>
								<div class="col-sm-9">
									<combox:xauth id="section" name="section"
										class="form-control select2-single" xauthType="x-sys-code"
										listKey="code" listValue="cname" gp="DEPT_CODE" required="required"/>
									<input type="hidden" id="sectionName" name="sectionName" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.templateType" /></label>
								<div class="col-sm-9">
									<select id="templateType" name="templateType" class="form-control select2-single">
										<option value="">請選擇</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-from-label"><spring:message code="suppliter.field.suppliercode"/></label>
								<div class="col-sm-9">
									<select id="supplierid" name="supplierid" class="form-control select2-single" required>
										<option value="">請選擇</option>
									</select>
									<input type="hidden" id="supplieridName" name="supplieridName" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.name" /></label>
								<div class="col-sm-9">
									<input type="text" id="contractName" name="contractName"
										class="form-control" required="required">
<!-- 									<input type="hidden" id="contractName" name="contractName" -->
<!-- 										class="form-control" required="required"> -->
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6" id="perTemplateDiv">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.perTemplate" /></label>
								<div class="col-sm-9">
									<select id="perTemplateDataid" name="perTemplateDataid" class="form-control select2-single">
										<option value="">請選擇</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-from-label"><spring:message code="contract.field.amount"/></label>
								<div class="col-sm-9">
									<input type="text" id="displatcontractAmount" name="displatcontractAmount" class="form-control" required="required"/>
									<input type="hidden" id="contractAmount" name="contractAmount"/>
								
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-from-label"><spring:message code="xauth.field.iden"/></label>
								<div class="col-sm-9">
									<combox:xauth id="depId" name="depId" class="form-control select2-single" 
										disabled="true" xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
									<input type="hidden" id="depName" name="depName" />
									<input type="hidden" id="idenId" name="idenId" />
								</div>
							</div>
						</div>
					</div>
					<div class="col-12 stretch-card">
						<div class="card">
							<div class="form-group row">
								<label class="col-sm-1 col-from-label">部門階層 ：</label>
								<div class="row" id='deptList'></div>
							</div>
						</div>	
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.contractor" /></label>
								<div class="col-sm-9">
									<label class="col-form-label"><c:out value="${principal.userCname}" /></label>
									<input type="hidden" name="userCname" value="<c:out value="${principal.userCname}"/>" />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.updtime" /></label>
								<div class="col-sm-9">
									<label class="col-form-label" id="creDateDesc"><c:out value="${creDateDesc }" /></label> 
									<input type="hidden" name="creDateDesc" value="<c:out value="${creDateDesc }" />" />
								</div>
							</div>
						</div>
					</div>
					<button id="btnInsert" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.insert" />
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"
						onclick="window.location.href='<c:url value='/contract/list/' />'">
						<spring:message code="common.back" />
					</button>
				</form>
				<form id="dataFormToView"  method="POST" action="<c:url value='/contract/list/editView' />" >
					<input type="hidden" id="indexName" name="indexName" />
					<input type="hidden" id="dataId" name="dataId" />
					<input type="hidden" id="btnType" name="btnType" value="2"/>
					<input type="hidden" id="dataType" name="dataType" />
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
