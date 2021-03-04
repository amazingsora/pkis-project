<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-frame" prefix="frame"%>
<sec:authentication var="principal" property="principal" />

<!DOCTYPE html>
<html lang="en">
	<head>	
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">		
		<meta name="description" content="">
		<meta name="author" content="">
		<meta name="_csrf" content="${_csrf.token}"/>
		<meta name="_csrf_header" content="${_csrf.headerName}"/>
	
	    <title><spring:message code="xauth.field.system.name"/></title>
	    
	    <link href="<c:url value="/resources/maj-layout/vendors/mdi/css/materialdesignicons.min.css"/>" rel="stylesheet">
		<link href="<c:url value="/resources/maj-layout/vendors/base/vendor.bundle.base.css"/>" rel="stylesheet">
		<link href="<c:url value="/resources/maj-layout/css/style.css"/>" rel="stylesheet">		
		<link rel="shortcut icon" href=<c:url value="/resources/maj-layout/images/favicon.png"/> />
		
	    <link href='<c:url value="/resources/jqGrid/css/ui.jqgrid-bootstrap.css"/>' rel="stylesheet">
		<link href='<c:url value="/resources/jqGrid/css/ui.jqgrid-bootstrap-ui.css"/>' rel="stylesheet">
	    <link href='<c:url value="/resources/jstree/jstree.css"/>' rel="stylesheet">	    
<%-- 	    <link href='<c:url value="/resources/air-datepicker/datepicker.css"/>' rel="stylesheet">  	    	     --%>
	    
	    <link href='<c:url value="/resources/select2/css/select2.min.css"/>' rel="stylesheet">
	    
	    <link href='<c:url value="/resources/common/common.css"/>' rel="stylesheet">
	    <link href='<c:url value="/resources/common/maj.css"/>' rel="stylesheet">
	    <link href='<c:url value="/resources/common/glyphicon.css"/>' rel="stylesheet">

	    
	    <script type="text/javascript" src="<c:url value="/resources/maj-layout/vendors/base/vendor.bundle.base.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/off-canvas.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/hoverable-collapse.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/maj-layout/js/template.js"/>"></script>
		
	    <script type="text/javascript" src="<c:url value="/resources/jquery/jquery.form.js"/>"></script>
	    <script type="text/javascript" src="<c:url value="/resources/jquery/jquery.blockUI.js"/>"></script>			
		
		<script type="text/javascript" src="<c:url value="/resources/jqGrid/js/i18n/grid.locale-tw.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/jqGrid/js/jquery.jqGrid.js"/>"></script>
		
		<script type="text/javascript" src="<c:url value="/resources/jstree/jstree.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/msgbox/jquery.msgbox.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery-validation/jquery.validate.min.js"/>"></script>	
		<script type="text/javascript" src="<c:url value="/resources/jquery-validation/validatorAddMethod.js"/>"></script>
			
		<script type="text/javascript" src="<c:url value="/resources/jquery-validation/messages_zh_TW.js"/>"></script>				
<%-- 		<script type="text/javascript" src="<c:url value="/resources/air-datepicker/datepicker.js"/>"></script> --%>
<%-- 		<script type="text/javascript" src="<c:url value="/resources/air-datepicker/i18n/datepicker.zh.js"/>"></script>		 --%>
		<script type="text/javascript" src="<c:url value="/resources/common/air-date.js"/>"></script> 
		<script type="text/javascript" src="<c:url value="/resources/common/common.js"/>"></script>		
		<script type="text/javascript" src="<c:url value="/resources/common/grid_jqGrid.js"/>"></script>		
		<script type="text/javascript" src="<c:url value="/resources/jqGrid/plugins/jszip.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/common/date-validation.js"/>"></script>
		
		<script type="text/javascript" src="<c:url value="/resources/select2/js/select2.full.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery/jquery.serializejson.js"/>"></script>			
		
		<frame:option policy="SAMEORIGIN"/>
		
		<script>
			$(function () {				
				var sso = '<c:out value="${principal.sso}" />';		
				var idenId = '<c:out value="${principal.idenId}"/>';
				
				if (sso == 'true') {
					$(".page-body-wrapper").css("padding-top", "0px");
					$(".main-panel").css("width", "100%");
					$(".card-title").css("display", "none");
				}
				else {
					$(".page-body-wrapper").css("padding-top", "60px");
					$(".main-panel").css("width", "calc(100% - 257px)");
				}
				
				if(idenId !== '999999999') {
					$("#manualName").css("display", "none");
				}
			});
			
			function getLiContent() {
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/main/layout/getFiles"/>");
				ajax.call(function(response) {
					console.log(response);
					if(response.status === "ok") {
						var fileNames = response.result.fileData;
						var fileName = "";
						$("#manualUl").empty();
						$.each(fileNames, function(key, value) {
							fileName = value.substr(0, value.lastIndexOf("."));
							if(fileName.indexOf("手冊") > -1 && '<c:out value="${principal.idenId}"/>' !== '999999999')
								return;
							$("#manualUl").append('<li id=' + key + '><a href="<c:url value="/main/layout/download?fileNameKey=' + key +'" />">' + fileName + '</li>');
						});
						$('#manualModal').modal('show');
					} else {
						$.fn.alert("取得檔案發生錯誤");
					}
				});
			}
		</script>		
	</head>
	
	<body>
		<div class="container-scroller">			
			<c:if test="${principal.sso ne true}">			
				<nav class="navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
		            <div class="navbar-brand-wrapper d-flex justify-content-center">
		        		<div class="navbar-brand-inner-wrapper d-flex justify-content-between align-items-center w-100">
		          			<a href="<c:url value='/main/index' />" class="btn btn-outline-light btn-lg btn-block" style="font-size:18px"><spring:message code="xauth.field.system.name"/></a>
		        		</div>  
		      		</div>
		      		<div class="navbar-menu-wrapper d-flex align-items-center justify-content-end">
		      			<p><a id="manualName" class="btn btn-link btn-fw" style="color:#ffffff" href="javascript:getLiContent()"><spring:message code="xauth.field.manual.name"/></a>
		      				歡迎 ${principal.userCname}<a class="btn btn-link btn-fw" style="color:#ffffff" href="javascript:document.getElementById('logout').submit()" ><spring:message code="xauth.field.logout"/></a>
		      			</p>	      			
		      		</div>
		    	</nav>	  
	    	</c:if>  	
	    	<div class="container-fluid page-body-wrapper">	    		
	    		<c:if test="${principal.sso ne true}">
	    			<jsp:include page="/WEB-INF/jsp/layout/menu.jsp"></jsp:include>
	    		</c:if>
				
				<div class="main-panel"> 
					<div class="content-wrapper">
						<div class="row">
							<div class="col-12 grid-margin stretch-card">
								<c:if test="${not empty bread}">
									<p class="card-description">
										<div class="d-flex">
											<i class="mdi mdi-home text-muted"></i>
  											<p class="text-muted mb-0"><c:out value="${bread}"/></p>
										</div>
									</p>							
								</c:if>	
							</div>
						</div>
						<!-- page start-->
						<sitemesh:write property='body'/>
						<!-- page end-->
					</div>
					<footer class="footer">
		          		<div class="d-sm-flex justify-content-center justify-content-sm-between">
		            		<span class="text-muted text-center text-sm-left d-block d-sm-inline-block">Copyright © 2018 關貿網路股份有限公司. All rights reserved. V1.000|20201130</span>
		            		<span class="float-none float-sm-right d-block mt-1 mt-sm-0 text-center">版權所有，請勿任意轉載使用</span>
		          		</div>
		        	</footer>
				</div>
			</div>
		</div>
		<div class="modal" id="manualModal" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title"><spring:message code="xauth.field.manual.name"/></h4>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
					</div>
					<div class="modal-body">
						<div class="box-body">
							<div class="row">
								<div class="col-md-12">
									<ul id="manualUl"></ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>	
	</body>	
</html>