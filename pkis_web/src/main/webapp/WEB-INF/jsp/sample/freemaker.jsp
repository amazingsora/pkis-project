<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">

	$(function () {
		
		$("#btnTemp1").on("click", function(event) {
			var ajax = new $.tvAjax();			
			ajax.setController("<c:url value="/sample/freemaker/tmp"/>");			
			ajax.put("tmp", "01");
			ajax.put("name", "pp");
			ajax.call(function(result) {
				if (result) {
					$('#template').empty();
					$('#template').html(result.object);
				}
			});
		});
		
		$("#btnTemp2").on("click", function(event) {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/sample/freemaker/tmp"/>");
			ajax.put("tmp", "02");
			ajax.put("name", "gg");
			ajax.call(function(result) {
				if (result) {
					$('#template').empty();
					$('#template').html(result.object);
				}
			});
		});
		
		$("#btnValue").on("click", function(event) {
			$.fn.alert($('#txt').val());
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
					<div id="template"></div>					
					<button id="btnTemp1" type="button" class="btn btn-outline-primary btn-fw">tmp1</button>
					<button id="btnTemp2" type="button" class="btn btn-outline-primary btn-fw">tmp2</button>
					<button id="btnValue" type="button" class="btn btn-outline-primary btn-fw">get value</button>
				</form>
			</div>
		</div>
	</div>
</div>
