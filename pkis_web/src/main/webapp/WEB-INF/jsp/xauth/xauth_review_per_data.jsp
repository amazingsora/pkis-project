<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	$(function() {
		
		// 依合約模式取得相對應下拉選單
		getSelectMenu();
		
		$("#idenId").on("change", function() {
			getUserList();
			getRoleList();
		});
		
		$(".select2-single").trigger("change");
		
		$("#dataForm").validate();
		
		$("#idenIdButton").on("click", function(){
			getBeSignDept();
		});
		
		$("#btnInsert").on("click", function() {
			if($("#dataForm").valid()) {
				var userIds = $("#userIds").val();
				var roleIds = $("#roleIds").val();
				var beSignDept = $("#beSignDept").val();
				var flowname = $("#flowid").find(":selected").text();
				$("#userId").val(userIds);
				$("#roleId").val(roleIds);
				$("#idenIds").val(beSignDept);
				if(flowname === "請選擇")
					flowname = "";
				$("#flowname").val(flowname);
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/reviewPer/insert"/>");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					if(result) {
						$.fn.alert(result.messages);
					}
				});
			}
		});
		
		$("#btnUpdate").on("click", function() {
			if($("#dataForm").valid()) {
				var userIds = $("#userIds").val();
				var roleIds = $("#roleIds").val();
				var beSignDept = $("#beSignDept").val();
				var flowname = $("#flowid").find(":selected").text();
				$("#userId").val(userIds);
				$("#roleId").val(roleIds);
				$("#idenIds").val(beSignDept);
				$("#serno").val('<c:out value="${data.serno}"/>');
				if(flowname === "請選擇")
					flowname = "";
				$("#flowname").val(flowname);
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/reviewPer/update"/>");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					if(result) {
						$.fn.alert(result.messages);
					}
				});
			}
		});
		
		$("#flowid").on("change", function() {
			var actiontype = $("#actiontype").val();
			if($("#flowid").val() !== "" && actiontype !== "00"){
				$("#idenIdSelected").attr('disabled', true)
				$("#idenIdButton").attr('disabled', true)
				$("#beSignDept").attr('disabled', true)
			}else{
				$("#idenIdSelected").attr('disabled', false)
				$("#idenIdButton").attr('disabled', false)
				$("#beSignDept").attr('disabled', false)
			}
		});
		
		$("#idenIdSelected").on("change", function() {
			var actiontype = $("#actiontype").val();
			if($("#beSignDept").val() !== "" && actiontype !== "00"){
				$("#flowid").attr('disabled', true)
			}else{
				$("#flowid").attr('disabled', false)
			}
		});
		
		showSelectMenu();
		
		var idenList = '${idenList}';
		if(idenList != ''){
			appendBeSignDept(JSON.parse(idenList));
		}
		
	});
	
	function getSelectMenu() {
		$("#deptnoSelectMenu").hide();
		$("#flowSelectMenu").hide();
		$("#idenIdSelectedDiv").hide();
		$("#beSignDeptDiv").hide();
		
		$("#contractmodel").on("change", function() {
			var contractmodel = $("#contractmodel").val();
			var actiontype = $("#actiontype").val();
			$("#idenIdDiv").show();
			$("#userIdsDiv").show();
			$("#roleIdsDiv").show();
			if(contractmodel == "SC") {
				$("#deptnoSelectMenu").show();
				$("#flowSelectMenu").hide();
				$("#idenIdSelectedDiv").hide();
				$("#beSignDeptDiv").hide();
			} 
			getActionTypeList();
		});
		$("#actiontype").on("change", function() {
			var contractmodel = $("#contractmodel").val();
			var actiontype = $("#actiontype").val();
			$("#idenIdDiv").show();
			$("#userIdsDiv").show();
			$("#roleIdsDiv").show();
			if(contractmodel == "NSC") {
				if("00|01|02".indexOf(actiontype) > -1 && actiontype != ''){
					$("#deptnoSelectMenu").hide();
					$("#flowSelectMenu").show();
					$("#idenIdSelectedDiv").show();
					$("#beSignDeptDiv").show();
					if(actiontype === "00"){
						$("#idenIdDiv").hide();
						$("#userIdsDiv").hide();
						$("#roleIdsDiv").hide();
					}
				}else if(actiontype === '03'){
					$("#deptnoSelectMenu").hide();
					$("#flowSelectMenu").hide();
					$("#idenIdSelectedDiv").show();
					$("#beSignDeptDiv").show();
				}
				else {
					$("#deptnoSelectMenu").hide();
					$("#flowSelectMenu").hide();
					$("#idenIdSelectedDiv").hide();
					$("#beSignDeptDiv").hide();
				}
			} else {
				$("#idenIdDiv").show();
				$("#userIdsDiv").show();
				$("#roleIdsDiv").show();
				$("#deptnoSelectMenu").show();
				$("#flowSelectMenu").hide();
				$("#idenIdSelectedDiv").hide();
				$("#beSignDeptDiv").hide();
				if(actiontype === "00"){
					$("#deptnoSelectMenu").hide();
					$("#flowSelectMenu").show();
					$("#idenIdDiv").hide();
					$("#userIdsDiv").hide();
					$("#roleIdsDiv").hide();
					$("#idenIdSelectedDiv").show();
					$("#beSignDeptDiv").show();
				}
			}
			getFlowList();
		});
	}
	
	function getRoleList() {
		$("#roleIds").empty();
		$("#roleIds").append($("<option>").prop({"value":""}).append("請選擇"));
		var idenId = $("#idenId").val();
		if(idenId != null && idenId != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/roleMenu/getRoleList"/>");
			ajax.put("idenId", idenId);
			ajax.call(function(json) {
				if(json.result.dataList != null) {
					$.each(json.result.dataList, function() {
						var data = this;
						var roleids;
						$("#roleIds").append($("<option>").prop({"value":data.roleIdOri}).append(data.roleIdOri + "-" + data.roleCname));
						  if('<c:out value="${data.roleids}"/>'!=''){
							  roleids =  "${data.roleids}";
				 			  if(roleids != null){
				 				 roleids = roleids.split(",");
				 				  for(var i = 0 ; i < roleids.length ; i ++){
				 					  var roleid = roleids[i];
				 					  $("#roleIds option[value=" + roleid + "]").prop('selected', 'selected');
				 				  }
				 			  }
						  }
						  $("#roleIds").trigger("change");

					})
				}
			});
		}
	}
	
	function getUserList() {
		$("#userIds").empty();
		$("#userIds").append($("<option>").prop({"value":""}).append("請選擇"));
		var idenId = $("#idenId").val();
		if(idenId != null && idenId != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/roleAgentUser/getUserList"/>");
			ajax.put("idenId", idenId);
			ajax.call(function(json) {
				if(json.result.dataList != null) {
					$.each(json.result.dataList, function() {
						var data = this;
						var userids;
						$("#userIds").append($("<option>").prop({"value":data.userId}).append(data.userId));
						  if('<c:out value="${data.userids}"/>'!=''){
				 			  userids =  "${data.userids}";
				 			  if(userids != null){
				 				  userids = userids.split(",");
				 				  for(var i = 0 ; i < userids.length ; i ++){
				 					  var userid = userids[i];
				 					  $("#userIds option[value=" + userid + "]").prop('selected', 'selected');
				 				  }
				 			  }
						  }
						  $("#userIds").trigger("change");
					})
				}
			});
		}
	}
	
	function getActionTypeList() {
		$("#actiontype").empty();
		$("#actiontype").append($("<option>").prop({"value":""}).append("請選擇"));
		var contractmodel = $("#contractmodel").val();
		if(contractmodel != null && contractmodel != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/reviewPer/getActionTypeList"/>");
			ajax.put("contractmodel", contractmodel);
			ajax.call(function(json) {
				if(json.result.dataList != null) {
					$.each(json.result.dataList, function() {
						var data = this;
						var actiontype;
						$("#actiontype").append($("<option>").prop({"value":data.CODE}).append(data.CNAME));
						  if('<c:out value="${data.actiontype}"/>'!=''){
							  actiontype = '${data.actiontype}';
							  if(actiontype != null){
								  $("#actiontype option[value=" + actiontype +"]").prop('selected' , 'selected');
							  }
						  }
						  $("#actionType").trigger("change");
					});
				}
				var contractmodel = $("#contractmodel").val();
				var actiontype = $("#actiontype").val();
				if(contractmodel == "NSC") {
					if("00|01|02".indexOf(actiontype) > -1 && actiontype != ''){
						$("#deptnoSelectMenu").hide();
						$("#flowSelectMenu").show();
						$("#idenIdSelectedDiv").show();
						$("#beSignDeptDiv").show();
						if(actiontype === "00"){
							$("#idenIdDiv").hide();
							$("#userIdsDiv").hide();
							$("#roleIdsDiv").hide();
						}
					}else if(actiontype === '03'){
						$("#deptnoSelectMenu").hide();
						$("#flowSelectMenu").hide();
						$("#idenIdSelectedDiv").show();
						$("#beSignDeptDiv").show();
					}
					else {
						$("#deptnoSelectMenu").hide();
						$("#flowSelectMenu").hide();
						$("#idenIdSelectedDiv").hide();
						$("#beSignDeptDiv").hide();
					}
				} else {
					$("#idenIdDiv").show();
					$("#userIdsDiv").show();
					$("#roleIdsDiv").show();
					$("#deptnoSelectMenu").show();
					$("#flowSelectMenu").hide();
					$("#idenIdSelectedDiv").hide();
					$("#beSignDeptDiv").hide();
					if(actiontype === "00"){
						$("#deptnoSelectMenu").hide();
						$("#flowSelectMenu").show();
						$("#idenIdDiv").hide();
						$("#userIdsDiv").hide();
						$("#roleIdsDiv").hide();
						$("#idenIdSelectedDiv").show();
						$("#beSignDeptDiv").show();
					}
				}
				getFlowList();
			});
		}
	}

	function getFlowList() {
		$("#flowid").empty();
		$("#flowid").append($("<option>").prop({"value":""}).append("請選擇"));
		var contractmodel = $("#contractmodel").val();
		if(contractmodel != null && contractmodel != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/reviewPer/getFlowList"/>");
			ajax.put("contractmodel", contractmodel);
			ajax.call(function(response) {
				if(response.result.dataList != null) {
					$.each(response.result.dataList,function() {
						var data = this;
						var flowid;
						$("#flowid").append($("<option>").prop({"value":data.flowid}).append(data.flowname));
						if('<c:out value="${data.flowid}"/>'!=''){
							flowid = '${data.flowid}';
							if(flowid != null){
								$("#flowid option[value=" + flowid + "]").prop('selected', 'selected');
							}
						}
						$("#flowid").trigger("change");
					});
				}
			});
		}
	}
	
	function showSelectMenu(){
		var contractmodel;
		var deptno;
		var idenid;
		
		  if('<c:out value="${data.contractmodel}"/>'!=''){
			  contractmodel = '${data.contractmodel}';
			  if(contractmodel != null){
				  $("#contractmodel option[value=" + contractmodel +"]").prop('selected' , 'selected');
			  }
			  $("#contractmodel").trigger("change");
		  }
		  if('<c:out value="${data.deptno}"/>'!=''){
			  deptno = "${data.deptno}";
			  if(deptno != null){
				  $("#deptno option[value=" + deptno + "]").prop('selected', 'selected');
			  }
			  $("#deptno").trigger("change");
		  }
		  if('<c:out value="${data.idenid}"/>'!=''){
			  idenid = "${data.idenid}";
			  if(idenid != null){
				  $("#idenId option[value=" + idenid + "]").prop('selected', 'selected');
			  }
			  $("#idenId").trigger("change");
		  }
	}
	
	// 取得被簽審部門
	function getBeSignDept() {
		var idenIdSelected = $("#idenIdSelected").val();
		if(idenIdSelected != null && idenIdSelected != ''){
			if(idenIdSelected != null && idenIdSelected != ''){
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/reviewPer/getBeSignDept"/>");
				ajax.put("idenIdSelected", idenIdSelected);
				ajax.call(function(response){
					console.log("response == ", response);
					if(response.result.deptList != null){
						appendBeSignDept(response.result.deptList);
					}
				});
			}
		} else {
			$.fn.alert("請選擇部門選取");
		}
	}
	
	function appendBeSignDept(deptList) {
		$.each(deptList, function(){
			var data = this;
			console.log("data.IDEN_ID == ", data.IDEN_ID);
			if(!isDuplicateBeSignDept(data.IDEN_ID)){
				$("#beSignDept").append($("<option>").prop({"value":data.IDEN_ID}).append(data.CNAME));
				if(data.IDEN_ID != null && data.IDEN_ID != ''){
					$("#beSignDept option[value=" + data.IDEN_ID + "]").prop('selected', 'selected');
				}
			}
			$("#beSignDept").trigger("change");
		});
	}
	
	function isDuplicateBeSignDept(beSignDept){
		var result = false;
		$("#beSignDept option").each(function() {
			if($(this).val() != ''){
				if(beSignDept == $(this).val()){
					result = true;
				}
			}
		});
		return result;
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:out value="${menuName}" /><spring:message code="xauth.function.bread.edit.suffix" />
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/reviewPer/' />" method="POST">
					<input type="hidden" id="userId" name="userId"/>
					<input type="hidden" id="roleId" name="roleId"/>
					<input type="hidden" id="idenIds" name="idenIds"/>
					<input type="hidden" id="flowname" name="flowname"/>
					<input type="hidden" id="serno" name="serno"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="contract.field.module" /></label>
								<div class="col-sm-9">
									<combox:xauth id="contractmodel" name="contractmodel" class="form-control select2-single"
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required"/>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.legal.action.type" /></label>
								<div class="col-sm-9">
									<combox:xauth id="actiontype" name="actiontype" class="form-control select2-single"
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="REVIEW_PER_CODE" required="required" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row" id="deptnoSelectMenu">
								<label class="col-sm-3 col-form-label"><spring:message code="contract.field.section" /></label>
								<div class="col-sm-9">
									<combox:xauth id="deptno" name="deptno" class="form-control select2-single" 
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="DEPT_CODE" />
								</div>
							</div>
							<div class="form-group row" id="flowSelectMenu">
								<label class="col-sm-3 col-form-label"><spring:message code="contractReview.field.flowname" /></label>
								<div class="col-sm-9">
									<select id="flowid" name="flowid" class="form-control select2-single">
										<option value="" disabled>請選擇</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row" id="idenIdSelectedDiv">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden" />選取</label>
								<div class="col-sm-7">
									<combox:xauth id="idenIdSelected" name="idenIdSelected" class="form-control select2-single"
										xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
								</div>
								<button id="idenIdButton" type="button" style="height:45px" class="btn btn-outline-primary btn-fw">選取</button>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
						</div>
						<div class="col-md-6">
							<div class="form-group row" id="beSignDeptDiv">
								<label class="col-sm-3 col-form-label">被簽審<spring:message code="xauth.field.iden" /></label>
								<div class="col-sm-9">
									<select id="beSignDept" class="form-control select2-single" multiple="multiple">
										<option value="" disabled>請選擇</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row" id="idenIdDiv">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden" /></label>
								<div class="col-sm-9">
									<combox:xauth id="idenId" name="idenId" class="form-control select2-single"
										xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row" id="userIdsDiv">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.id" /></label>
								<div class="col-sm-9">
									<select id="userIds" class="form-control select2-single" multiple="multiple">
										<option value="" disabled>請選擇</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row" id="roleIdsDiv">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.role" /></label>
								<div class="col-sm-9">
									<select id="roleIds" class="form-control select2-single" multiple="multiple">
										<option value="" disabled>請選擇</option>
									</select>
								</div>
							</div>
						</div>
					</div>
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
					<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
						<spring:message code="common.clear"/>
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"  
						onclick="window.location.href='<c:url value='/xauth/reviewPer/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>