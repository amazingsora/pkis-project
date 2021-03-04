<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">

	$(function () {

		var pId = '<c:out value="${data.selParentDdl}"/>';
		if (pId == '#') {
			pId = '';
		}
		$("#parentId").val(pId);
		$(".select2-single").trigger("change");

		$("#dataForm").validate();

		$("#btnInsert").on("click", function() {
			if ($("#dataForm").valid()) {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/menu/insert"/>");				
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
				ajax.setController("<c:url value="/xauth/menu/update"/>");				
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
					ajax.setController("<c:url value="/xauth/menu/delete"/>");										
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
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/xauth/menu/' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.menu.id"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.menuId}">
											<label class="col-form-label"><c:out value="${data.menuId}"/></label>
											<input type="hidden" id="menuId" name="menuId" value="<c:out value="${data.menuId}"/>" >
										</c:when>
										<c:otherwise>
											<input type="text" id="menuId" name="menuId" class="form-control" value="<c:out value="${data.menuId}"/>" required>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.menu.url"/></label>
		                    	<div class="col-sm-9">
		                      		<input type="text" id="menuUrl" name="menuUrl" class="form-control" value="<c:out value="${data.menuUrl}"/>">
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.menu.name"/></label>
								<div class="col-sm-9">
									<input type="text" id="menuCname" name="menuCname" class="form-control" value="<c:out value="${data.menuCname}"/>" required>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.menu.parent"/></label>
		                    	<div class="col-sm-9">
		                      		<combox:xauth id="parentId" name="parentId" class="form-control select2-single" 
										xauthType="x-menu" listKey="selDdl" listValue="struct" headerKey="請選擇" headerValue=""/>
		                    	</div>
		                  	</div>
		                </div>
					</div>	
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.menu.memo"/></label>
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
						onclick="window.location.href='<c:url value='/xauth/menu/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
