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

		$("#dataForm").validate();
		
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
				ajax.setController("<c:url value="/manage/sysCode/insert"/>");
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
				ajax.setController("<c:url value="/manage/sysCode/update"/>");
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
					ajax.setController("<c:url value="/manage/sysCode/delete"/>");
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
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/manage/sysCode/' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.idenId}">
											<label class="col-form-label"><c:out value="${data.idenId}"/> - <c:out value="${data.idenName}"/></label>
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
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.gp"/></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.gp}">
											<label class="col-form-label"><c:out value="${data.gp}"/></label>
											<input type="hidden" id="gp" name="gp" value="<c:out value="${data.gp}"/>" >
										</c:when>
										<c:otherwise>
											<input type="text" id="gp" name="gp" class="form-control" value="<c:out value="${data.gp}"/>" required>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.code"/></label>
		                    	<div class="col-sm-9">
		                      		<c:choose>
										<c:when test="${not empty data.code}">
											<label class="col-form-label"><c:out value="${data.code}"/></label>
											<input type="hidden" id="code" name="code" value="<c:out value="${data.code}"/>" >
										</c:when>
										<c:otherwise>
											<input type="text" id="code" name="code" class="form-control" value="<c:out value="${data.code}"/>" required>
										</c:otherwise>
									</c:choose>
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.cname"/></label>
								<div class="col-sm-9">
									<input type="text" id="cname" name="cname" class="form-control" value="<c:out value="${data.cname}"/>" required>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.ename"/></label>
		                    	<div class="col-sm-9">
		                      		<input type="text" id="ename" name="ename" class="form-control" value="<c:out value="${data.ename}"/>" >
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.order"/></label>
		                    	<div class="col-sm-9">
		                      		<input type="text" id="orderSeq" name="orderSeq" class="form-control" value="<c:out value="${data.orderSeq}"/>" >
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
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.memo"/></label>
								<div class="col-sm-9">
									<input type="text" id="memo" name="memo" class="form-control" value="<c:out value="${data.memo}"/>">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.c01"/></label>
								<div class="col-sm-9">									
									<input type="text" id="c01" name="c01" class="form-control" value="<c:out value="${data.c01}"/>" >
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.c02"/></label>
		                    	<div class="col-sm-9">		                      		
		                      		<input type="text" id="c02" name="c02" class="form-control" value="<c:out value="${data.c02}"/>" >
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.c03"/></label>
								<div class="col-sm-9">									
									<input type="text" id="c03" name="c03" class="form-control" value="<c:out value="${data.c03}"/>" >
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.c04"/></label>
		                    	<div class="col-sm-9">		                      		
		                      		<input type="text" id="c04" name="c04" class="form-control" value="<c:out value="${data.c04}"/>" >
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.c05"/></label>
								<div class="col-sm-9">									
									<input type="text" id="c05" name="c05" class="form-control" value="<c:out value="${data.c05}"/>" >
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
						onclick="window.location.href='<c:url value='/manage/sysCode/' />'">
						<spring:message code="common.back"/>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
