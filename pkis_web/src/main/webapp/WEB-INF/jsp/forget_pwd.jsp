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

		<script type="text/javascript" src="<c:url value="/resources/maj-layout/vendors/base/vendor.bundle.base.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/off-canvas.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/hoverable-collapse.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/template.js"/>"></script>
		
	</head>
	
	<script type="text/javascript">
  		$(function() { 
  			var message = "<c:out value="${result.messages}"/>";
  			var status = "<c:out value="${result.status}"/>";
  			if(message){
  				alert(message);
  				if(status === 'ok'){
  					window.location.href='<c:url value='/login' />'
  				}
  			}
 		});
	
	</script>
	
	<body>
		<div class="container-scroller">
	    	<div class="container-fluid page-body-wrapper full-page-wrapper">
	      		<div class="content-wrapper d-flex align-items-center auth px-0">
	        		<div class="row w-100 mx-0">
	          			<div class="col-lg-4 mx-auto">
	            			<div class="auth-form-light text-left py-5 px-4 px-sm-5">
	              				<div class="brand-logo">
									<h4><spring:message code="xauth.field.forget.pwd"/></h4>
	              				</div>            				
	              				<form id="dataForm" name='login' class="pt-3" action="<c:url value='/changeForgetPwd/update' />" method='POST'>
<!-- 	                				<div class="form-group">	                  					 -->
<%-- 	                  					<input type="text" id="idenId" name="idenId" class="form-control" placeholder="<spring:message code="xauth.field.iden.id"/>" required> --%>
<!-- 	                				</div> -->
	                				<div class="form-group">	                  						                  					
	                  					<input type="text" id="userId" name="userId" class="form-control" placeholder="<spring:message code="xauth.field.user.id"/>" required>
	                				</div>
	                				<div class="form-group">	                  						                  					
	                  					<input type="text" id="email" name="email" class="form-control" placeholder="<spring:message code="xauth.field.user.mail"/>" required>
	                				</div>
	                				<button id="btnUpdate" type="submit" class="btn btn-primary btn-sm"><spring:message code="common.ok"/></button>
	                				<button id="btnCancel" type="button" class="btn btn-primary btn-sm" onclick="window.location.href='<c:url value='/login' />'"><spring:message code="common.cancel"/></button>
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