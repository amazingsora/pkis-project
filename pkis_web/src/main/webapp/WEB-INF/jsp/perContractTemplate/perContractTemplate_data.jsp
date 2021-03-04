<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>
<link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">
<script type="text/javascript">
	$(function() {
		getTemplatemoudle();
		$("#module").on("change", function() {
			getFlowList();
		});
		
		$("#btnInsert").on("click", function() {
			if($("#dataForm").valid()) { 
				var ajax = new $.tvAjax();
				$("#dispName").val($("#disp").val() == "" ? "" : $("#disp").find(":selected").text());
				ajax.setController("<c:url value="/perContractTemplate/insert/insertData"/>");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(response) {
					$.fn.alert(response.messages, function() {
						console.log("response == ", response);
						if(response.status === 'ok') {
							$("#templateDataId").val(response.result.templateDataId);
							$("#dataType").val(response.result.dataType)
							$("#dataFormToView").submit();
						}
					});
				});
			}
		});
		
		$("#cbEnabled").prop('checked', true);
		$("#enabled").val("1");
		$("input[name='cbEnabled']").on("click", function() {
			if($(this).prop('checked')){
				$("#enabled").val("1");
			} else {
				$("#enabled").val("0");
			}
		});
		
	});
	
	function getFlowList(){
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
				<form id="dataForm" name="dataForm" class="forms-sample" method="POST">
					<input type="hidden" id="dispName" name="dispName"/>
					<input type="hidden" id="dataid" name="dataid"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.module" /></label>
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
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.dispname" /></label>
								<div class="col-sm-9">
									<select id="disp" name="disp" class="form-control select2-single" required>
										<option value="">請選擇</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contract.field.templatename" /></label>
								<div class="col-sm-9">
									<input type="text" id="templatename" name="templatename" class="form-control" required />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="xauth.field.enabled" /></label>
								<div class="form-check">
									<input type="checkbox" id="cbEnabled" name="cbEnabled" />
									<input type="hidden" id="enabled" name="enabled" />
								</div>
							</div>
						</div>
					</div>
					<button id="btnInsert" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.insert" />
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"
						onclick="window.location.href='<c:url value='/percontractTemplate/list/' />'">
						<spring:message code="common.back" />
					</button>
				</form>
				<form id="dataFormToView" method="POST" action="<c:url value='/perContractTemplate/insert/editView'/>">
					<input type="hidden" id="templateDataId" name="templateDataId"/>
					<input type="hidden" id="dataType" name="dataType" />
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</form>
			</div>
		</div>
	</div>
</div>