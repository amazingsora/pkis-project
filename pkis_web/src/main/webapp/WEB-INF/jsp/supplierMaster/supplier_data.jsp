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
		$("#btnInsert").on("click", function() {
			var ajax = new $.tvAjax();
			
			if ($("#dataForm").valid()) {
				ajax.setController("<c:url value="/supplier/master/insertData" />");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					console.log("result == ", result);
					if(result){
						$.fn.alert(result.messages, function(){
							if(result.status == "ok"){
								location.href='<c:url value='/supplier/master/' />';
							}
						});
					}
				});
			}
		});
		
		$("#btnUpdate").on("click", function() {
			var ajax = new $.tvAjax();
			
			if ($("#dataForm").valid()) {
				ajax.setController("<c:url value="/supplier/master/updateData" />");
				ajax.put("paramsData", $("#dataForm").serializeObject());
				ajax.call(function(result) {
					console.log("result == ", result);
					if(result){
						$.fn.alert(result.messages, function(){
							if(result.status == "ok"){
								location.href='<c:url value='/supplier/master/'/>';
							}
						});
					}
				});
			}
		});
		
		// 顯示下拉選單內容
		showdeptno();
		
	});
	
	function showdeptno(){
		var deptno;
		  if('<c:out value="${data.deptno}"/>'!=''){
			  deptno = JSON.parse('${data.deptno}');
			  if(deptno != null){
				  $("#deptno option[value=" + deptno +"]").prop('selected' , 'selected');
			  }
			  $("#deptno").trigger("change");
		  }
		  
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
								<c:choose>
									<c:when test="${not empty data.supplierid}">
										<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.supplierid" /></label>
										<label class="col-sm-2 col-form-label">${data.supplierid}</label>
										<input type="hidden" id="supplierid" name="supplierid" class="form-control" value="<c:out value="${data.supplierid}"/>">
									</c:when>
								</c:choose>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<c:choose>
									<c:when test="${not empty data.enabled}">
										<input type="hidden" id="enabled" name="enabled" class="form-control" value="<c:out value="${data.enabled}"/>">
									</c:when>
								</c:choose>
							</div>
						</div>
					</div>		
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<c:choose>
									<c:when test="${not empty data.suppliergui}">
										<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.suppliergui" /></label>
										<label class="col-sm-2 col-form-label">${data.suppliergui}</label>
										<input type="hidden" id="suppliergui" name="suppliergui" class="form-control" value="<c:out value="${data.suppliergui}"/>">
									</c:when>
									<c:otherwise>
										<label data-require=true class="col-sm-2 col-form-label"><spring:message code="suppliter.field.suppliergui" /></label>
										<div class="col-sm-9">
											<input type="text" id="suppliergui" name="suppliergui" class="form-control" value="<c:out value="${data.suppliergui}"/>" required>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.section" /></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data}">
											<combox:xauth id="deptno" name="deptno" class="form-control select2-single" 
												xauthType="x-sys-code" listKey="code" listValue="cname" gp="DEPT_CODE" disabled="true" />
										</c:when>
										<c:otherwise>
											<combox:xauth id="deptno" name="deptno" class="form-control select2-single" 
												xauthType="x-sys-code" listKey="code" listValue="cname" gp="DEPT_CODE" />
										</c:otherwise>
									</c:choose>
									
								</div>
							</div>
						</div>
						<!--<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.suppliertype" /></label>
								<div class="col-sm-9">
									<input type="text" id="suppliertype" name="suppliertype" class="form-control" value="<c:out value="${data.suppliertype}"/>">
								</div>
							</div>
						</div> -->
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<c:choose>
									<c:when test="${not empty data.suppliercode}">
										<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.suppliercode" /></label>
										<label class="col-sm-2 col-form-label">${data.suppliercode}</label>
										<input type="hidden" id="suppliercode" name="suppliercode" class="form-control" value="<c:out value="${data.suppliercode}"/>">
									</c:when>
									<c:otherwise>
										<label data-require=true class="col-sm-2 col-form-label"><spring:message code="suppliter.field.suppliercode" /></label>
										<div class="col-sm-9">
											<input type="text" id="suppliercode" name="suppliercode" class="form-control" required>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="xauth.field.user.pwd" /></label>
								<div class="col-sm-9">
									<input type="password" id="password" name="password" class="form-control">
								</div>
							</div>	
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-2 col-form-label"><spring:message code="suppliter.field.suppliercname" /></label>
								<div class="col-sm-9">
									<input type="text" id="suppliercname" name="suppliercname" class="form-control" value="<c:out value="${data.suppliercname}"/>" required>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.supplierename" /></label>
								<div class="col-sm-9">
									<input type="text" id="supplierename" name="supplierename" class="form-control" value="<c:out value="${data.supplierename}"/>">
								</div>
							</div>
						</div>
					</div>
					<!--  <div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="contract.field.section" /></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data}">
											<combox:xauth id="deptno" name="deptno" class="form-control select2-single" 
												xauthType="x-sys-code" listKey="code" listValue="cname" gp="DEPT_CODE" disabled="true" />
										</c:when>
										<c:otherwise>
											<combox:xauth id="deptno" name="deptno" class="form-control select2-single" 
												xauthType="x-sys-code" listKey="code" listValue="cname" gp="DEPT_CODE" />
										</c:otherwise>
									</c:choose>
									
								</div>
							</div>
						</div>
					</div>-->
					<div class="row">
						<div class="col-md-12">
							<div class="form-group row">
								<label class="col-sm-1 col-from-label"><spring:message code="suppliter.field.suppliercaddr" /></label>
								<div class="col-sm-10">
									<input type="text" id="suppliercaddr" name="suppliercaddr" class="form-control" value="<c:out value="${data.suppliercaddr}"/>">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group row">
								<label class="col-sm-1 col-from-label"><spring:message code="suppliter.field.suppliereaddr" /></label>
								<div class="col-sm-10">
									<input type="text" id="suppliereaddr" name="suppliereaddr" class="form-control" value="<c:out value="${data.suppliereaddr}"/>">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.picuser" /></label>
								<div class="col-sm-9">
									<input type="text" id="picuser" name="picuser" class="form-control" value="<c:out value="${data.picuser}"/>">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.supplieretel" /></label>
								<div class="col-sm-9">
									<input type="text" id="supplieretel" name="supplieretel" class="form-control" value="<c:out value="${data.supplieretel}"/>">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.contacruser" /></label>
								<div class="col-sm-9">
									<input type="text" id="contacruser" name="contacruser" class="form-control" value="<c:out value="${data.contacruser}"/>">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-2 col-form-label"><spring:message code="suppliter.field.supplieremail" /></label>
								<div class="col-sm-9">
									<input type="text" id="supplieremail" name="supplieremail" class="form-control" value="<c:out value="${data.supplieremail}"/>">
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
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw" onclick="window.location.href='<c:url value='/supplier/master/'/>'">
						<spring:message code="common.back" />
					</button>
				</form>
			</div>
		</div>
	</div>
</div>
