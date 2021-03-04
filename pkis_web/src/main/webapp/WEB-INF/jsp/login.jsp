<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">
		<meta name="_csrf" content="${_csrf.token}"/>
		<meta name="_csrf_header" content="${_csrf.headerName}"/>		
		<title><spring:message code="xauth.field.system.name"/></title>
		
		<link href="<c:url value="/resources/maj-layout/vendors/mdi/css/materialdesignicons.min.css"/>" rel="stylesheet">
		<link href="<c:url value="/resources/maj-layout/vendors/base/vendor.bundle.base.css"/>" rel="stylesheet">
		<link href="<c:url value="/resources/maj-layout/css/style.css"/>" rel="stylesheet">		
		<link rel="shortcut icon" href="<c:url value="/resources/maj-layout/images/favicon.png"/>" />
		<link href='<c:url value="/resources/common/maj.css"/>' rel="stylesheet">

		<script type="text/javascript" src="<c:url value="/resources/maj-layout/vendors/base/vendor.bundle.base.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/off-canvas.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/hoverable-collapse.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/template.js"/>"></script>
		
		<script type="text/javascript">
			$(function() {
				var status = '<c:out value="${status}"/>';
				var msg = '<c:out value="${msg}"/>';
				if (status == 'ok') {
					alert(msg);
				}
			});
		</script>
		
	</head>
	
	<body>
		<div class="container-scroller">
	    	<div class="container-fluid page-body-wrapper full-page-wrapper">
	      		<div class="content-wrapper d-flex align-items-center auth px-0">
	        		<div class="row w-100 mx-0">
	          			<div class="col-lg-4 mx-auto">
	            			<div class="auth-form-light text-left py-5 px-4 px-sm-5">
	              				<div class="brand-logo">
	              					<img alt="" src="<c:url value='/resources/images/CarrefourLogo.png' />" style="display:block; margin:auto;">
									<h4 align="center"><spring:message code="xauth.field.system.name"/></h4>
									<h4 align="center"><spring:message code="xauth.field.system.ename"/></h4>
	              				</div>	              				
	              				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
	              					<h6 class="text-danger">${SPRING_SECURITY_LAST_EXCEPTION}</h6>
	              				</c:if>	              				
	              				<form id="dataForm" name='login' class="pt-3" action="<c:url value='/login' />" method='POST'>
<!-- 	                				<div class="form-group">	                  					 -->
<%-- 	                  					<input type="text" class="form-control form-control-lg" id="idenId" name="idenId" placeholder="<spring:message code="xauth.field.iden.id"/>" > --%>
<!-- 	                				</div> -->
	                				<div class="form-group">	                  						                  					
	                  					<input type="text" class="form-control form-control-lg" id="username" name="username" placeholder="<spring:message code="xauth.field.user.id"/>" >
	                				</div>
	                				<div class="form-group">	                  					
	                  					<input type="password" class="form-control form-control-lg" id="password" name="password" placeholder="<spring:message code="xauth.field.user.pwd"/>" >
	                				</div>
	                				<div class="mt-3">	                  					
	                  					<button class="btn btn-block btn-primary btn-lg font-weight-medium auth-form-btn" type="submit"><spring:message code="xauth.field.login"/></button>
	                				</div>
	                				<div class="my-2 d-flex justify-content-between align-items-center">
	                  					<div class="form-check">
	                    					<label class="form-check-label text-muted"></label>
	                  					</div>
<%-- 	                  					<a href="<c:url value="/forgetPwd" />" class="auth-link text-danger"><spring:message code="xauth.field.forget.pwd"/></a> --%>
	                  					<a href="<c:url value="/changeForgetPwd/" />" class="auth-link text-danger"><spring:message code="xauth.field.forget.pwd"/></a>
	                				</div>
	                				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	              				</form>
	            			</div>
	          			</div>
	        		</div>
	      		</div>
	    	</div>
	  	</div>
	</body>
	
</html>