<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='sec'
	uri='http://www.springframework.org/security/tags'%>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">
	
	$(function() {
		getFlowCheckbox();
		
		$('#prereviewDiv').hide();
		$('#orgreviewDiv').hide();
		$('#conditionDiv').hide();
		
		$("#btnInsert").on("click", function() {
			var ajax = new $.tvAjax();
			$("#conditionName").val($("#condition").val() == "" ? "" : $("#condition").find(":selected").text());
			
			if ($("#dataForm").valid()) {
				ajax.setController("<c:url value="/xauth/contractReview/insertData" />");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					console.log("result == ", result);
					if(result){
						$.fn.alert(result.messages, function(){
							if(result.status == "ok"){
								location.href='<c:url value='/xauth/contractReview/' />';
							}
						});
					}
				});
			}
		});
		
		$("#btnUpdate").on("click", function() {
			var ajax = new $.tvAjax();
			
			if ($("#dataForm").valid()) {
				ajax.setController("<c:url value="/xauth/contractReview/updateData" />");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					console.log("result == ", result);
					if(result){
						$.fn.alert(result.messages, function(){
							if(result.status == "ok"){
								location.href='<c:url value='/xauth/contractReview/'/>';
							}
						});
					}
				});
			}
		});
		
		var status = "<c:out value="${data.status}"/>";
		if (status == 'Y') {
			$('#cbStatus').prop('checked', true);
			$('#status').val('Y');
		} else if (status == 'N') {
			$('#cbStatus').prop('checked', false);
			$('#status').val('N');
		} else {
			$('#cbStatus').prop('checked', false);
			$('#status').val('N');
		}
		
		$("input[name='cbStatus']").click(function() {
			if ($(this).prop('checked')) {
				$('#status').val('Y');
			} else {
				$('#status').val('N');
			}
		});
		
		var isprereview = "<c:out value="${data.isprereview}"/>";
		if (isprereview == 'Y') {
			$('#cbIsprereview').prop('checked', true);
			$('#isprereview').val('Y');
		} else if (isprereview == 'N') {
			$('#cbIsprereview').prop('checked', false);
			$('#isprereview').val('N');
		} else {
			$('#cbIsprereview').prop('checked', false);
			$('#isprereview').val('N');
		}
		
		$("input[name='cbIsprereview']").click(function() {
			if ($(this).prop('checked')) {
				$('#isprereview').val('Y');
			} else {
				$('#isprereview').val('N');
			}
		});
		
		var isdeptreview = "<c:out value="${data.isdeptreview}"/>";
		if (isdeptreview == 'Y') {
			$('#cbIsdeptreview').prop('checked', true);
			$('#isdeptreview').val('Y');
		} else if (isdeptreview == 'N') {
			$('#cbIsdeptreview').prop('checked', false);
			$('#isdeptreview').val('N');
		} else {
			$('#cbIsdeptreview').prop('checked', false);
			$('#isdeptreview').val('N');
		}
		
		$("input[name='cbIsdeptreview']").click(function() {
			if ($(this).prop('checked')) {
				$('#isdeptreview').val('Y');
			} else {
				$('#isdeptreview').val('N');
			}
		});
		
		var isorgreview = "<c:out value="${data.isorgreview}"/>";
		if (isorgreview == 'Y') {
			$('#cbIsorgreview').prop('checked', true);
			$('#isorgreview').val('Y');
		} else if (isorgreview == 'N') {
			$('#cbIsorgreview').prop('checked', false);
			$('#isorgreview').val('N');
		} else {
			$('#cbIsorgreview').prop('checked', false);
			$('#isorgreview').val('N');
		}
		
		$("input[name='cbIsorgreview']").click(function() {
			if ($(this).prop('checked')) {
				$('#isorgreview').val('Y');
			} else {
				$('#isorgreview').val('N');
			}
		});
		
		console.log("<c:out value="${data}"/>");
		
		var condition = "<c:out value="${data.condition}"/>";
		if (condition != '') {
			$('#conditionDiv').show();
		} else {
			$('#conditionDiv').hide();
		}
		
		$('#contractmodel').change(function(){
			var flowname = "<c:out value="${data.flowname}"/>";
			
			if(flowname != null && flowname != ''){
				$('#flowname').val(flowname);
			}else{
				$('#flowname').val('');
			}
			
			$('#condition').val('');
			$('#condition').trigger("change");
			
			if($(this).val() == 'NSC'){
				$('#conditionDiv').show();
				$('#prereviewDiv').show();
				$('#orgreviewDiv').show();
				$("div[id^='checkboxDiv_']").show();
				
				
			}else if($(this).val() == 'SC'){
				$('#conditionDiv').hide();
				$('#prereviewDiv').show();
				$('#orgreviewDiv').show();
				$("div[id^='checkboxDiv_']").hide();
			}
			else
			{
				$('#conditionDiv').hide();
				$('#prereviewDiv').hide();
				$('#orgreviewDiv').hide();
				$("div[id^='checkboxDiv_']").hide();
			}
			
			getFlowList();
		});
		
		$('#flowid').change(function(){
			var flowname = $('#flowcname').val($(this).val()).find(':selected').text();
			if($(this).val() == ''){
				$('#flowname').val('');
			}else{
				$('#flowname').val(flowname);
			}
		});
		
		// 顯示下拉選單內容
		showSelectVal();
		$('#flowcnameDiv').hide();
	});
	
	function showSelectVal(){
		
		var contractmodel;
		if('<c:out value="${data.contractmodel}"/>'!=''){
			contractmodel = '<c:out value="${data.contractmodel}"/>';
			$("#contractmodel option[value=" + contractmodel +"]").prop('selected' , 'selected');
			$("#contractmodel").trigger("change");
		}
		
		var condition;
		if('<c:out value="${data.condition}"/>'!=''){
			condition = '<c:out value="${data.condition}"/>';
			$("#condition option[value=" + condition +"]").prop('selected' , 'selected');
			$("#condition").trigger("change");
		}
	}
	
	function getFlowList() {
		$('#flowid').empty();
		$("#flowid").append($("<option>").prop({"value":""}).append("請選擇"));
		var contractmodel = $('#contractmodel').val();
		if (contractmodel != null && contractmodel != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/contractReview/getFlowList"/>");
			ajax.put("contractmodel", contractmodel);
			ajax.call(function(json) {
				if (json.result.dataList != null) {
					$.each(json.result.dataList,function() {
						var data = this;
						$("#flowid").append($("<option>").prop({"value":data.code}).append(data.code + "-" + data.cname));
						$("#flowcname").append($("<option>").prop({"value":data.code}).append(data.cname));
					});
				}
			});
		}		
	}
	
	// 動態取得checkbox
	function getFlowCheckbox() {
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/xauth/contractReview/getReviewsetdata"/>");
		ajax.call(function(json) {
			if(json.result.dataList != null) {
				
				var content = '';
				var data = json.result.dataList;
				for(var i = 0 ; i < data.length ; i ++) {
					
					content = content + '<div class="row" id="checkboxDiv_' + data[i].serno + '">';
					content = content + '<div class="col-md-6">';
					content = content + '<div class="form-group row">';
					content = content + '<label class="col-sm-4 col-form-label">' + data[i].reviewname + '</label>';
					content = content + '<div class="col-sm-1">';
					content = content + '<div class="form-check">';
					content = content + '<input type="checkbox" id="cbReviewName_' + data[i].serno + '" name="cbReviewName_' + data[i].serno + '">';
					content = content + '<input type="hidden" id="reviewName_' + data[i].serno + '" name="reviewName_' + data[i].serno + '">';
					content = content + '</div>';
					content = content + '</div>';
					if(i < data.length - 1) {
						i = i + 1;
						content = content + '<label class="col-sm-4 col-form-label">' + data[i].reviewname + '</label>';
	 					content = content + '<div class="col-sm-1">';
	 					content = content + '<div class="form-check">';
	 					content = content + '<input type="checkbox" id="cbReviewName_' + data[i].serno + '" name="cbReviewName_' + data[i].serno + '">';
	 					content = content + '<input type="hidden" id="reviewName_' + data[i].serno + '" name="reviewName_' + data[i].serno + '">';
	 					content = content + '</div>';
	 					content = content + '</div>';
					}
					content = content + '</div>';
					content = content + '</div>';
					content = content + '</div>';
				}
				$("#cardDiv").append(content);
			}
			$("div[id^='checkboxDiv_']").hide();
			
			var reviewsetdataconfList = '<c:out value="${reviewsetdataconfList}"/>';
			if('<c:out value="${data}"/>' != null) {
				if('<c:out value="${data.contractmodel}"/>' == "NSC") {
					$("div[id^='checkboxDiv_']").show();
					
			        <c:forEach items="${reviewsetdataconfList}" var="resultData">
			        	$('#cbReviewName_' + '<c:out value="${resultData.SERNO}"/>').prop('checked', true);
			        </c:forEach>
				        
				}
			}
		});
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:choose>
						<c:when test="${not empty data}">
							<c:out value="${menuName}"/>-編輯
						</c:when>
						<c:otherwise>
							<c:out value="${menuName}"/>-新增
						</c:otherwise>
					</c:choose>
				</header>
				<form id="dataForm" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contractReview.field.contractmodel" /></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.contractmodel}">
											<combox:xauth id="contractmodel" name="contractmodel" disabled="true"
												class="form-control select2-single" xauthType="x-sys-code"
												listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required" />
										</c:when>
										<c:otherwise>
											<combox:xauth id="contractmodel" name="contractmodel"
												class="form-control select2-single" xauthType="x-sys-code"
												listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required" />
										</c:otherwise>
									</c:choose>
									<input type="hidden" id="contractmodelName" name="contractmodelName" />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contractReview.field.status"/></label>
								<div class="col-sm-1">
									<div class="form-check">
										<input type="checkbox" id="cbStatus" name="cbStatus">
										<input type="hidden" id="status" name="status">
									</div>
								</div>
								<c:choose>
									<c:when test="${not empty data.flowversion}">
										<label  class="col-sm-2 col-form-label"><spring:message code="contractReview.field.flowversion" /></label>
										<label class="col-sm-1 col-form-label">${data.flowversion}</label>
										<input type="hidden" id="flowversion" name="flowversion" class="form-control" value="<c:out value="${data.flowversion}"/>">
									</c:when>
								</c:choose>		
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<c:choose>
									<c:when test="${not empty data.flowid}">
										<label class="col-sm-2 col-form-label"><spring:message code="contractReview.field.flowid" /></label>
										<label class="col-sm-2 col-form-label">${data.flowid}</label>
										<input type="hidden" id="flowid" name="flowid" class="form-control" value="<c:out value="${data.flowid}"/>">
									</c:when>
									<c:otherwise>
										<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contractReview.field.flowid" /></label>
										<div class="col-sm-9">
											<select id="flowid" name="flowid" class="form-control select2-single" required>
												<option value="">請選擇</option>
											</select>
										</div>	
										<div class="col-sm-9" id='flowcnameDiv'>
											<select id="flowcname" name="flowcname" class="form-control select2-single">
												<option value="">請選擇</option>
											</select>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<c:choose>
									<c:when test="${not empty data.flowname}">
										<label class="col-sm-2 col-form-label"><spring:message code="contractReview.field.flowname" /></label>
										<label class="col-sm-2 col-form-label">${data.flowname}</label>
										<input type="hidden" id="flowname" name="flowname" class="form-control" value="<c:out value="${data.flowname}"/>">
									</c:when>
									<c:otherwise>
										<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contractReview.field.flowname" /></label>
										<div class="col-sm-9">
											<input type="text" id="flowname" name="flowname" class="form-control" readonly="readonly" required>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6" id='conditionDiv'>
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="contractReview.field.condition" /></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.condition}">
											<combox:xauth id="condition" name="condition" class="form-control select2-single" disabled="true"
												xauthType="x-sys-code" headerKey="請選擇" headerValue="" listKey="code" listValue="cname"
												gp="NSC_FLOW_CONDITION" required="required" />
										</c:when>
										<c:otherwise>
											<combox:xauth id="condition" name="condition" class="form-control select2-single" 
												xauthType="x-sys-code" headerKey="請選擇" headerValue="" listKey="code" listValue="cname"
												gp="NSC_FLOW_CONDITION" required="required" />
										</c:otherwise>
									</c:choose>
									<input type="hidden" id="conditionName" name="conditionName" />
								</div>
							</div>
						</div>
					</div>
					<div class="col-12 stretch-card">
						<div class="card" id="cardDiv">
						<label class="col-sm-4 col-form-label">流程是否需要：</label>
							<div class="row" id = "prereviewDiv">
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-4 col-form-label"><spring:message code="contractReview.field.isprereview"/></label>
										<div class="col-sm-1">
											<div class="form-check">
												<input type="checkbox" id="cbIsprereview" name="cbIsprereview">
												<input type="hidden" id="isprereview" name="isprereview">
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row" id="orgreviewDiv">
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-4 col-form-label"><spring:message code="contractReview.field.isdeptreview"/></label>
										<div class="col-sm-1">
											<div class="form-check">
												<input type="checkbox" id="cbIsdeptreview" name="cbIsdeptreview">
												<input type="hidden" id="isdeptreview" name="isdeptreview">
											</div>
										</div>
										<label class="col-sm-4 col-form-label"><spring:message code="contractReview.field.isorgreview"/></label>
										<div class="col-sm-1">
											<div class="form-check">
												<input type="checkbox" id="cbIsorgreview" name="cbIsorgreview">
												<input type="hidden" id="isorgreview" name="isorgreview">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<br/>
					<c:choose>
						<c:when test="${empty data}">
							<button id="btnInsert" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.insert"/>
							</button>
						</c:when>
						<c:otherwise>
							<button id="btnUpdate" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.update"/>
							</button>
						</c:otherwise>
					</c:choose>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw" onclick="window.location.href='<c:url value='/xauth/contractReview/'/>'">
						<spring:message code="common.back" />
					</button>
				</form>
			</div>
		</div>
	</div>
</div>
