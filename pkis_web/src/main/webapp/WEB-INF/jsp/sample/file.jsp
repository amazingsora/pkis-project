<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">

	$(function () {
		$("#btnUpload").on("click", function() {	
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/sample/file/upload"/>");
			ajax.setFormId("dataForm");			
			<%-- 設置檔案上限  MB為單位--%>
			ajax.setLimit(1);
			ajax.upload(function(result) {
				if (result) {
					var message = '';
					$.each( result.messages, function( key, value ) {
						message += value + '\r\n';
					});
					$.fn.alert(message);
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
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">輸入欄位</label>
								<div class="col-sm-9">
									<input type="text" id="txt" name="txt" class="form-control" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">檔案上傳1</label>
								<div class="col-sm-9">
									<input id="fileUpload1" type="file" name="file" class="btn btn-link btn-rounded btn-fw" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">檔案上傳2</label>
								<div class="col-sm-9">
									<input id="fileUpload2" type="file" name="file" class="btn btn-link btn-rounded btn-fw" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"></label>
								<div class="col-sm-9">
									<button id="btnUpload" type="button" class="btn btn-primary btn-sm">
										檔案上傳
									</button>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">檔案下載</label>
								<div class="col-sm-9">
									<a id="fd" href="<c:url value='/sample/file/download'/>"  class="btn btn-link btn-rounded btn-fw">檔案下載</a> 
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
