<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>
<link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">
<script type="text/javascript">
	$(function () {
		$("#year").val(new Date().getFullYear());
		$("#dataForm").validate({
			errorPlacement: function(error, element) {
				error.appendTo(element.parent());
			}
		});
		
		$("#btnUpload").on("click", function() {
			if ($("#dataForm").valid()) {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/contractTemplate/manage/upload"/>");
				ajax.setFormId("dataForm");
				ajax.upload(function(result){
					if(result){
						var message = '';
						$.each( result.messages, function( key, value ){
							message += value + '\r\n';
						});
						$.fn.alert(message);
					}
				});
			}
		});
	});
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<header class="card-title">
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample">
					<!-- 年度 -->
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">上傳年度</label>
								<div class="col-sm-9">
									<input type="text" id="year" name="year" maxlength="4" class="form-control" required="required" readOnly/>
								</div>
							</div>
						</div>
					</div>
					<!-- 定型化契約 -->
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="contract.field.module"/></label>
								<div class="col-sm-9">
								
									<combox:xauth id="module" name="module" class="form-control select2-single" 
										xauthType="x-sys-code" listKey="code" listValue="cname" gp="CONTRACT_MODE_CODE" required="required"/>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">檔案上傳</label>
								<div class="col-sm-9">
									<input id="fileUpload1" type="file" name="file" class="btn btn-link btn-rounded btn-fw" required>
								</div>
							</div>
						</div>
					</div>
					<button id="btnUpload" type="button" class="btn btn-outline-primary btn-fw">
						檔案上傳
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw" onclick="window.location.href='<c:url value='/contractTemplate/manage/' />'">
						<spring:message code="common.back"/>
					</button>
				</form>
			</div>
		</div>
	</div>
</div>
