<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>
<link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">
<script type="text/javascript">

	$(function () {
		
		$("#userType").val('<c:out value="${data.userType}"/>');
		$(".select2-single").trigger("change");
		
		$("#dataForm").validate({
			errorPlacement: function(error, element) {				
				if (element.attr("name") == 'roleId') {
					$('#divRoleList').append(error);
				}	
				else {
					error.appendTo(element.parent());
				}			
			},
			rules: {
				email : 'validateEmail'
			}
		});

		$("#idenId").on("change", function() {
			getUserRoleList();
		});

		$("#idenId").trigger( "change" );

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
		
		var cbIsFirst = "<c:out value="${data.isFirst}"/>";
		if (cbIsFirst == '1') {
			$('#cbIsFirst').prop('checked', true);
			$('#isFirst').val('1');
		}
		else {
			$('#cbIsFirst').prop('checked', false);
			$('#isFirst').val('0');
		}
		
		$("#btnInsert").on("click", function() {
			if ($("#dataForm").valid()) {
				var dataArray = [];
				$.each($("input[name='roleId']:checked"), function() {      
				    dataArray.push($(this).val());
				});
				
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/manage/user/insert"/>");
				ajax.put("paramsData", $('#dataForm').serializeObject());
				ajax.put("roleId", dataArray.join(","));
				ajax.call(function(result) {
					if (result) {
						$.fn.alert(result.messages);
					}
				});				
			}
		});
		
		$("#btnUpdate").on("click", function() {
			if ($("#dataForm").valid()) {
				var dataArray = [];
				$.each($("input[name='roleId']:checked"), function() {      
				    dataArray.push($(this).val());
				});
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/manage/user/update"/>");				
				ajax.put("paramsData", $('#dataForm').serializeObject());
				ajax.put("roleId", dataArray.join(","));
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
					ajax.setController("<c:url value="/manage/user/delete"/>");
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

		$("input[name='cbEnabled']").click(function() {
			if ($(this).prop('checked')) {
				$('#enabled').val('1');
			}
			else {
				$('#enabled').val('0');
			}
		});
		
		$("input[name='cbIsFirst']").click(function() {
			if ($(this).prop('checked')) {
				$('#isFirst').val('1');
			}
			else {
				$('#isFirst').val('0');
			}
		});
	});		

	function getUserRoleList() {
		$('#divRoleList').empty();
		var idenId = $('#idenId').val();
		if (idenId != null && idenId != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/manage/user/getUserRoleList"/>");			
			ajax.put("idenId", idenId);
			ajax.call(function(json) {
				if (json.result.dataList != null) {
					
					var userRoleList = '${userRoleList}';
					
					$.each(json.result.dataList,function() {
						var data = this;
						var checked = '';
						
						if (userRoleList) {
							$.each(JSON.parse(userRoleList), function() {
								var role = this;
								if (role.roleId == data.roleId) {
									checked = 'checked';
								}
							});							
						}						
						
						var content = '<div class="form-check">';
						content += '<input type="checkbox" name="roleId" value="' + data.roleId + '" ' + checked + ' required>' + data.roleCname;
						content += '</div>';
						$("#divRoleList").append(content);
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
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/manage/user/' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.idenId}">
											<label class="col-form-label"><c:out value="${data.idenId}"/></label>
											<input type="hidden" id="idenId" name="idenId" value="<c:out value="${data.idenId}"/>" >
										</c:when>
										<c:otherwise>
											<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
												xauthType="x-iden-qry" listKey="idenId" listValue="struct" required="required"/>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.role"/></label>
								<div class="col-sm-9">
									<div id="divRoleList" class="col-sm-10"></div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.id"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.userId}">											
											<label class="col-form-label"><c:out value="${data.userId}"/></label>
											<input type="hidden" id="userId" name="userId" value="<c:out value="${data.userId}"/>" >
										</c:when>
										<c:otherwise>
											<input type="text" id="userId" name="userId" class="form-control" value="<c:out value="${data.userId}"/>" required>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.pwd"/></label>
		                    	<div class="col-sm-9">
		                      		<input type="password" id="userPw" name="userPw" class="form-control" >
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.name"/></label>
								<div class="col-sm-9">
									<input type="text" id="userCname" name="userCname" class="form-control" value="<c:out value="${data.userCname}"/>" required>
								</div>
							</div>
						</div>	
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.type"/></label>
								<div class="col-sm-9">
									<combox:xauth id="userType" name="userType" class="form-control select2-single" 
										xauthType="x-user-type" listKey="key" listValue="value" required="required"/>
								</div>
							</div>
						</div>					
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.mail"/></label>
								<div class="col-sm-9">
									<input type="text" id="email" name="email" class="form-control" value="<c:out value="${data.email}"/>" >
								</div>
							</div>
						</div>	
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.directManager"/></label>
								<div class="col-sm-9">
									<input type="text" id="directManager" name="directManager" class="form-control" value="<c:out value="${data.directManager }" />">
								</div>
							</div>
						</div>			
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.enabled"/></label>
								<div class="col-sm-9">
									<div class="form-check">
										<input type="checkbox" id="cbEnabled" name="cbEnabled">
										<input type="hidden" id="enabled" name="enabled">
									</div>
								</div>
							</div>
						</div>		
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.first"/></label>
								<div class="col-sm-9">
									<div class="form-check">
										<input type="checkbox" id="cbIsFirst" name="cbIsFirst">
										<input type="hidden" id="isFirst" name="isFirst">
									</div>
								</div>
							</div>
						</div>					
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.date.begin"/></label>
								<div class="col-sm-9">
									<input type="text" id="bgnDate" name="bgnDate" class="form-control date" value="<c:out value="${data.bgnDateDesc}"/>" required>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.date.end"/></label>
		                    	<div class="col-sm-9">
		                      		<input type="text" id="endDate" name="endDate" class="form-control date" value="<c:out value="${data.endDateDesc}"/>" >
		                    	</div>
		                  	</div>
		                </div>
					</div>
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
						<c:otherwise>
							<button id="btnUpdate" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.update"/>
							</button>
							<button id="btnDel" type="button" class="btn btn-outline-danger btn-fw">
								<spring:message code="common.delete"/>
							</button>
						</c:otherwise>
					</c:choose>
					<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
						<spring:message code="common.clear"/>
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"  
						onclick="window.location.href='<c:url value='/manage/user/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
