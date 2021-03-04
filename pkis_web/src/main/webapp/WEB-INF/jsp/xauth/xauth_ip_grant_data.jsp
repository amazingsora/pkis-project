<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">

	$(function () {

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
			if ($("#dataForm").valid()) {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/ipGrant/insert"/>");
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
				ajax.setController("<c:url value="/xauth/ipGrant/update"/>");
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
					ajax.setController("<c:url value="/xauth/ipGrant/delete"/>");
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

</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/><spring:message code="xauth.function.bread.edit.suffix"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/ipGrant/' />" method="POST">
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
											<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
												xauthType="x-iden-root-ip" listKey="idenId" listValue="struct" required="required"/>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.ip.grant.address"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.ipAddr}">
											<label class="col-form-label"><c:out value="${data.ipAddr}"/></label>
											<input type="hidden" id="ipAddr" name="ipAddr" value="<c:out value="${data.ipAddr}"/>" >
										</c:when>
										<c:otherwise>
											<input type="text" id="ipAddr" name="ipAddr" class="form-control" value="<c:out value="${data.ipAddr}"/>" required>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.ip.grant.sys.type"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.sysType}">
											<label class="col-form-label"><c:out value="${data.sysTypeName}"/></label>
											<input type="hidden" id="sysType" name="sysType" value="<c:out value="${data.sysType}"/>" >
										</c:when>
										<c:otherwise>
											<combox:xauth id="sysType" name="sysType" class="form-control" 
												xauthType="x-sys-code" idenId="00000000" gp="SYS_TYPE"
												listKey="code" listValue="cname" required="required"/>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
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
					</div>		
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.ip.grant.memo"/></label>
								<div class="col-sm-9">									
									<input type="text" id="memo" name="memo" class="form-control" value="<c:out value="${data.memo}"/>">
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
						onclick="window.location.href='<c:url value='/xauth/ipGrant/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
