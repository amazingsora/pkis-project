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
		
		<script type="text/javascript">
			$(function() {
				var message = "<c:out value="${msg}"/>";
				var status = "<c:out value="${status}"/>";
				var tokenFail = "<c:out value="${tokenFail}"/>";
				if(status == 'ng'){
					alert(message);
					if(tokenFail == 'Y') {
						window.location.href='<c:url value='/login' />';
					}
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
									<h4><spring:message code="xauth.field.reset.pwd"/></h4>
	              				</div>            				
	              				<form id="dataForm" name='login' class="pt-3" action="<c:url value='/resetPwd/update' />" method='POST'>
	              					<input type="hidden" id="emailToken" name="emailToken" value="<c:out value="${emailToken}"/>" >
	                				<div class="form-group">	                  					
	                  					<input type="password" class="form-control" id="p1" name="p1" value="" placeholder="<spring:message code="xauth.field.pwd.new"/>" required>
	                				</div>
	                				<div class="form-group">	                  						                  					
	                  					<input type="password" class="form-control" id="p2" name="p2" value="" placeholder="<spring:message code="xauth.field.pwd.check"/>" required>
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