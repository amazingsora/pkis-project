<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<sec:authentication var="principal" property="principal" />

<script>
	$(function () {
		var msg = '<c:out value="${msg}"/>';
		if (msg) {
			$.fn.alert(msg);
		}
	});
</script>

<div class="row">
    <div class="col-md-12 grid-margin">
      	<div class="d-flex justify-content-between flex-wrap">
        	<div class="d-flex align-items-end flex-wrap">
          		<div class="mr-md-3 mr-xl-5">
            		<h2>${principal.userCname}，歡迎</h2>
            		<p class="mb-md-0">請直接點擊左側選單開始使用</p>
          		</div>
        	</div>
      	</div>
	</div>
</div>