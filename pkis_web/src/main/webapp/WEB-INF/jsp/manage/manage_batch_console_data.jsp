<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="tv-combox" prefix="combox" %>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">

	$(function() {
		
		var cbStatus = "<c:out value="${data.status}"/>";
		if(cbStatus == '1') {
			$("#cbStatus").prop("checked", true);
			$("#status").val("1");
		} else if(cbStatus == '0') {
			$("cbStatus").prop("checked", false);
			$("status").val("0");
		} else {
			$("#cbStatus").prop("checked", true);
			$("#status").val("1");
		}
		
		$("input[name='cbStatus']").click(function() {
			if($(this).prop("checked")) {
				$("#status").val("1");
			} else {
				$("#status").val("0");
			}
		});
		
		$("#btnInsert").on("click", function() {
			if($("#dataForm").valid()) {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/manage/batchConsole/insert"/>");
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
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/manage/batchConsole/update"/>");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					if(result) {
						$.fn.alert(result.messages);
					}
				});
			}
		});
		
		$("#btnDel").on("click", function() {
			$.fn.confirm('<spring:message code="common.confirm.delete"/>', function() {
				if($("#dataForm").valid()) {
					var ajax = new $.tvAjax();
					ajax.setController("<c:url value="/manage/batchConsole/delete"/>");
					ajax.put("paramsData", $("#dataForm").serializeObject());
					ajax.call(function(result) {
						if(result) {
							$.fn.alert(result.messages, function() {
								if(result.status == 'ok') {
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
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/manage/batchConsole/' />" method="POST">
				<input type="hidden" id="serno" name="serno" value="<c:out value="${data.serno}"/>" />
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.batch.name"/></label>
								<div class="col-sm-9">
									<input type="text" id="batchname" name="batchname" class="form-control" value="<c:out value="${data.batchname}" />" required />
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.batch.sleepsec"/> (秒) </label>
								<div class="col-sm-9">
									<input type="text" id="sleepsec" name="sleepsec" class="form-control" value="<c:out value="${data.sleepsec}" />" required />
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
										<input type="checkbox" id="cbStatus" name="cbStatus" />
										<input type="hidden" id="status" name="status" />
									</div>
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
						onclick="window.location.href='<c:url value='/manage/batchConsole/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
