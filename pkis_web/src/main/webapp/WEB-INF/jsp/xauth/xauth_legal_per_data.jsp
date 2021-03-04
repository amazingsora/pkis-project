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
		
		$("#btnInsert").on("click", function() {
			if($("#dataForm").valid()) {
				var userIds = $("#userIds").val();
				var roleIds = $("#roleIds").val();
				var flowname = $("#flowid").find(":selected").text();
				$("#userId").val(userIds);
				$("#roleId").val(roleIds);
				if(flowname === "請選擇")
					flowname = "";
				$("#flowname").val(flowname);
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/legalPer/insert"/>");
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
				$("#userId").val(userIds);
				$("#roleId").val(roleIds);
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/legalPer/update"/>");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					if(result) {
						$.fn.alert(result.messages);
					}
				});
			}
		});
		
		showSelectMenu();
		
	});
	
	function getSelectMenu() {
		$("#deptnoSelectMenu").hide();
		$("#flowSelectMenu").hide();
		$("#contractmodel").on("change", function() {
			var contractmodel = $("#contractmodel").val();
			if(contractmodel == "SC") {
				$("#deptnoSelectMenu").show();
				$("#flowSelectMenu").hide();
			} else if(contractmodel == "NSC") {
				$("#deptnoSelectMenu").hide();
				$("#flowSelectMenu").show();
			} else {
				$("#deptnoSelectMenu").hide();
				$("#flowSelectMenu").hide();
			}
			getActionTypeList();
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
			ajax.setController("<c:url value="/xauth/legalPer/getActionTypeList"/>");
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
			});
		}
	}

	function getFlowList() {
		$("#flowid").empty();
		$("#flowid").append($("<option>").prop({"value":""}).append("請選擇"));
		var contractmodel = $("#contractmodel").val();
		if(contractmodel != null && contractmodel != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/legalPer/getFlowList"/>");
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
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:out value="${menuName}" /><spring:message code="xauth.function.bread.edit.suffix" />
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/legalPer/' />" method="POST">
					<input type="hidden" id="userId" name="userId"/>
					<input type="hidden" id="roleId" name="roleId"/>
					<input type="hidden" id="flowname" name="flowname"/>
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
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden" /></label>
								<div class="col-sm-9">
									<combox:xauth id="idenId" name="idenId" class="form-control select2-single"
										xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.id" /></label>
								<div class="col-sm-9">
									<select id="userIds" class="form-control select2-single" multiple="multiple">
										<option value="" disabled>請選擇</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.role" /></label>
								<div class="col-sm-9">
									<select id="roleIds" class="form-control select2-single" multiple="multiple">
										<option value="" disabled>請選擇</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.legal.action.type" /></label>
								<div class="col-sm-9">
									<combox:xauth id="actiontype" name="actiontype" class="form-control select2-single"
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="LEGAL_REVIEW" required="required" />
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
						onclick="window.location.href='<c:url value='/xauth/legalPer/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>