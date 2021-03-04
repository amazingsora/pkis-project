<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<h1><label class="error">系統訊息</label></h1>
				<h4>
		        	<c:out value="${msg}"/>
		        </h4>
			</div>
		</div>
	</div>
</div>