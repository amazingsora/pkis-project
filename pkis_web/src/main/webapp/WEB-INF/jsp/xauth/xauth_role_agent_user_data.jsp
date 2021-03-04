<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>


<sec:authentication var="principal" property="principal" />
<script type="text/javascript">
	$(function () {
		var principal="${principal.idenId}";
		if(principal=='00000000'){
			$('#colroleId').show();
			
		}
		if('<c:out value="${principal.idenId}"/>'!=00000000){
			$("#idenId").attr("disabled",true);
			$("#roleId").attr("disabled",true);
			$("#userId").attr("disabled",true);
			<!--部門ID-->
			$("#idenId").val("${principal.idenId}");
			$("#hideidenId").val("${principal.idenId}");
	        $("#agentIdenId").val("${principal.idenId}");
	        $("#hideagentIdenId").val("${principal.idenId}");
		}
		$("#dataForm").validate({
			errorPlacement: function(error, element) {				
				if (element.attr("name") == 'idenId') {
					$('#divIdenId').append(error);
				}	
				else {
					error.appendTo(element.parent());
				}			
			}
		});
		$("#idenId").on("change", function() {
			getRoleList();
		});
		$("#roleId").on("change", function() {
			getSourceUserList();
		});
		$("#agentIdenId").on("change", function() {
			getUserList();
		});
		$("#idenId").trigger( "change" );
		
		$("#agentIdenId").trigger( "change" );
		
		var cbEnabled = "<c:out value="${data.enabled}"/>";
		if (cbEnabled == '1') {
			$('#cbEnabled').prop('checked', true);
			$('#enabled').val('1');
		}
		else if (cbEnabled == '0') {
			$('#cbEnabled').prop('checked', false);
			$('#enabled').val('0');
		}
		else {
			$('#cbEnabled').prop('checked', true);
			$('#enabled').val('1');
		}
		
		$("input[name='cbEnabled']").click(function() {
			if ($(this).prop('checked')) {
				$('#enabled').val('1');
			}
			else {
				$('#enabled').val('0');
			}
		});

		$("#btnInsert").on("click", function() {
			$('#hideuserId').val($('#userId').val());
			$('#hideagentIdenId').val($('#agentIdenId').val());

			if ($("#dataForm").valid()) {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/roleAgentUser/insert"/>");
				ajax.put("paramsData", $('#dataForm').serializeObject());
				ajax.call(function(result) {
					if (result) {
						$.fn.alert(result.messages);
					}
				});
			}
		});
		
		$("#btnUpdate").on("click", function() {
			if ($("#dataForm").valid()) {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/roleAgentUser/update"/>");
				ajax.put("paramsData", $('#dataForm').serializeObject());
				ajax.call(function(result) {
					if (result) {
						$.fn.alert(result.messages);
					}
				});
			}
		});

		$("#btnDel").on("click", function() {
			$.fn.confirm('<spring:message code="common.confirm.delete"/>', function() {
				if ($("#dataForm").valid()) {
					var ajax = new $.tvAjax();
					ajax.setController("<c:url value="/xauth/roleAgentUser/delete"/>");
					ajax.put("paramsData", $('#dataForm').serializeObject());
					ajax.call(function(result) {
						if (result) {
							$.fn.alert(result.messages, function() {
								if (result.status == 'ok') {
									$("#dataForm").submit();
								}
							});	
						}
					});
				}	
			});				
		});

	}); 

	function getRoleList() {
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
						
						$("#roleId").append($("<option>").prop({"value":data.roleId}).append(data.roleId + "-" + data.roleCname));
						<!--待回傳後入RoleId-->
						if('<c:out value="${principal.idenId}"/>'!=00000000){
							$("#roleId").val("${userRoleId}");
							$("#hideroleId").val("${userRoleId}");

							getSourceUserList();
			
						}else{
							$("#hideidenId").val($("#idenId").val());

							
							
						}
					});
				}
			});
		}		
	}
	
	function getSourceUserList() {
		$('#userId').empty();
		$("#userId").append($("<option>").prop({"value":""}).append("請選擇"));
		 idenId = $('#idenId').val();
		 roleId = $('#roleId').val();
		
		if (idenId != null && idenId != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/roleAgentUser/getUserListByIdenAndRole"/>");
			ajax.put("idenId", idenId);
			ajax.put("roleId", roleId);
			ajax.call(function(json) {
				if (json.result.dataList != null) {
					$.each(json.result.dataList,function() {
						var data = this;
						$("#userId").append($("<option>").prop({"value":data.userId}).append(data.userId + "-" + data.userCname));
						<!--設定回傳-->
						if('<c:out value="${principal.idenId}"/>'!=00000000){
							$("#userId").val('${principal.userId}');
							$("#hideuserId").val('${principal.userId}');

						}else{
							$("#hideroleId").val($("#roleId").val());
							$("#hideroleId").val("${userRoleId}");

						}

					});
				}
			});
		}	
	}
	
	function getUserList() {
		$('#agentUserId').empty();
		$("#agentUserId").append($("<option>").prop({"value":""}).append("請選擇"));
		var agentIdenId = $('#agentIdenId').val();
		if (agentIdenId != null && agentIdenId != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/roleAgentUser/getUserList"/>");
			ajax.put("idenId", agentIdenId);
			ajax.put("action", "insert");
			ajax.call(function(json) {
				if (json.result.dataList != null) {
					$.each(json.result.dataList,function() {
						var data = this;
						
						$("#agentUserId").append($("<option>").prop({"value":data.userId}).append(data.userId + "-" + data.userCname));
					});
				}
			});
		}	
	}
	
	</script>
<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/><spring:message code="xauth.function.bread.edit.suffix"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/roleAgentUser/' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
								<div id="divIdenId" class="col-sm-9">
																		
									<c:choose>
										<c:when test="${not empty data.idenId}">											
											<label class="col-form-label"><c:out value="${data.idenId}"/> - <c:out value="${data.idenName}"/></label>
											<input type="hidden" id="idenId" name="idenId" value="<c:out value="${data.idenId}"/>" >
										</c:when>
										
										
										<c:otherwise>
											<combox:xauth id="idenId"  class="form-control select2-single" 
												xauthType="x-iden-qry" listKey="idenId" listValue="struct" headerKey="請選擇" headerValue="" required="required"/>
										    	<input type="hidden" id="hideidenId" name="idenId" value=""/>
										    	
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="col-md-6">	
							<div id="colroleId" style="display:none">
								<div class="form-group row">
									<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.role"/></label>
									<div class="col-sm-9">
										<c:choose>
											<c:when test="${not empty data.roleId}">											
												<label class="col-form-label"><c:out value="${data.roleId}"/> - <c:out value="${data.roleCname}"/></label>
												<input type="hidden" id="roleId" name="roleId" value="<c:out value="${data.roleId}"/>" >
											</c:when>
											<c:otherwise>
												<select id="roleId" name="roleId" class="form-control select2-single" required>
													<option value="">請選擇</option>
												</select>
											</c:otherwise>
										</c:choose>
										
									</div>
								</div>
							</div>
					         <input type="hidden" id="hideroleId" name="roleId" value="" >
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"> <spring:message code="xauth.field.user"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.userId}">											
											<label class="col-form-label"><c:out value="${data.userId}"/> - <c:out value="${data.userCname}"/></label>
											<input type="hidden" id="userId" name="userId" value="<c:out value="${data.userId}"/>" >
										</c:when>
										<c:otherwise>
											<select id="userId" name=userId class="form-control select2-single" required>
												<option value="">請選擇</option>
											</select>
										</c:otherwise>
									</c:choose>
									
									<input type="hidden" id="hideuserId" name="userId" value="" >
									
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.agent.iden"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.agentIdenId}">											
											<label class="col-form-label"><c:out value="${data.agentIdenId}"/> - <c:out value="${data.agentIdenName}"/></label>
											<input type="hidden" id="agentIdenId" name="agentIdenId" value="<c:out value="${data.agentIdenId}"/>" >
										</c:when>
										<c:otherwise>
											<combox:xauth id="agentIdenId"  class="form-control select2-single" 
												xauthType="ALL-x-iden-qry" listKey="idenId" listValue="struct" headerKey="請選擇" headerValue="" required="required"/>
												<input type="hidden" id="hideagentIdenId" name="agentIdenId" value="" >
										
										</c:otherwise>
									</c:choose>
									
								</div>
							</div>
						</div>


						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">代理<spring:message code="xauth.field.user"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.agentUserId}">											
											<label class="col-form-label"><c:out value="${data.agentUserId}"/> - <c:out value="${data.agentUserCname}"/></label>
											<input type="hidden" id="agentUserId" name="agentUserId" value="<c:out value="${data.agentUserId}"/>" >
										</c:when>
										<c:otherwise>
											<select id="agentUserId" name="agentUserId" class="form-control select2-single" required>
												<option value="">請選擇</option>
											</select>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
<!-- 					<div class="row">						 -->
<!-- 						<div class="col-md-6"> -->
<!-- 							<div class="form-group row"> -->
<%-- 								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.enabled"/></label> --%>
<!-- 								<div class="col-sm-9"> -->
<!-- 									<div class="form-check"> -->
<!-- 										<input type="checkbox" id="cbEnabled" name="cbEnabled"> -->
<!-- 										<input type="hidden" id="enabled" name="enabled"> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div>	 -->
<!-- 					</div>							 -->
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.create.user"/></label>
								<div class="col-sm-9">									
									<label class="col-form-label"><c:out value="${data.creUser}"/></label>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.create.date"/></label>
		                    	<div class="col-sm-9">		                      		
		                      		<label class="col-form-label"><c:out value="${data.creDateDesc}"/></label>
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.update.user"/></label>
								<div class="col-sm-9">									
									<label class="col-form-label"><c:out value="${data.updUser}"/></label>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.update.date"/></label>
		                    	<div class="col-sm-9">		                      		
		                      		<label class="col-form-label"><c:out value="${data.updDateDesc}"/></label>
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
<%--						<c:otherwise>
							<button id="btnUpdate" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.update"/>
							</button>
							<button id="btnDel" type="button" class="btn btn-outline-danger btn-fw">
								<spring:message code="common.delete"/>
							</button>
						</c:otherwise>  --%>
					</c:choose>
<%--					<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
						<spring:message code="common.clear"/>
					</button>  --%>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"  
						onclick="window.location.href='<c:url value='/xauth/roleAgentUser/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<!-- 預設值1-啟動 待日後調整 -->
					<input type="hidden" id="enabled" name="enabled" value="1">					
				</form>
			</div>
		</div>
	</div>
</div>
