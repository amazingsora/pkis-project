<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
		<link href="<c:url value="/resources/common/maj.css"/>" rel="stylesheet">

		<script type="text/javascript" src="<c:url value="/resources/maj-layout/vendors/base/vendor.bundle.base.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/off-canvas.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/hoverable-collapse.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/template.js"/>"></script>
		
	</head>
	
	<body>
		<div class="container-scroller">
	    	<div class="container-fluid page-body-wrapper full-page-wrapper">
	      		<div class="content-wrapper d-flex align-items-center auth px-0">
	        		<div class="row w-100 mx-0">
	          			<div class="col-lg-8 mx-auto">
	            			<div class="auth-form-light text-left py-5 px-4 px-sm-5">
	              				<div class="brand-logo">
									<h1><label class="">系統訊息</label></h1>
									<h4>
							        	<span class="text-danger"><c:out value="${msg}"/></span>
							        </h4>	   
							        <sec:authorize access="!isAuthenticated()">
										<p>點擊 <a href="<c:url value="/login" />">這裡</a> 返回首頁</p>
									</sec:authorize>
									<sec:authorize access="isAuthenticated()">
										<p>點擊 <a href="<c:url value="/main/index" />">這裡</a> 返回首頁</p>
									</sec:authorize>
	              				</div>            					              				
	            			</div>
	          			</div>
	        		</div>
	      		</div>
	    	</div>
	  	</div>
	</body>
	
</html>