<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript" src="<c:url value="/resources/jstree/jstree.js"/>"></script>
<link href='<c:url value="/resources/jstree/jstree.css"/>' rel="stylesheet">

<script type="text/javascript">

	$(function () {
		$("#dataForm").validate();
		$("#idenId").on("change", function() {
			getRoleList();
			$("#tree").jstree("destroy");
		});

		$("#idenId").trigger( "change" );
		
		$("#roleId").on("change", function() {
			if (this.value == '') {
				$("#tree").jstree("destroy");
			}
			else {
				getTree();
			}				
		});

		$("#btnSave").on("click", function() {
			if ($("#dataForm").valid()) {
				
				var selectedElmsIds = [];
				var selectedElms = $('#tree').jstree("get_selected", true);
				
				if (selectedElms.length > 0) {
					selectedElmsIds.push('ROOT');
					$.each(selectedElms, function() {			    
					    if ($.inArray(this.id, selectedElmsIds) == -1) {
					    	selectedElmsIds.push(this.id);
					    }
					    
					    if ($.inArray(this.parent, selectedElmsIds) == -1) {
					    	selectedElmsIds.push(this.parent);				    	
					    }				    
					});
				}
				var ajax = new $.tvAjax();
				ajax.setController("<c:url value="/xauth/roleMenu/saveRoleMenu"/>");
				ajax.put("idenId", $('#idenId').val());
				ajax.put("roleId", $('#roleId').val());
				ajax.put("menuArray", selectedElmsIds.join(","));
				ajax.call(function(result) {
					if (result) {
						if (result.status == 'ok') {
							getTree();
						}
					}
				});
			}
		});
		$("#btnClear").on("click", function() {
			$("#tree").jstree("destroy");
		});
		
	});

	function getTree() {
		$("#tree").jstree("destroy");
		if ($('#idenId').val() != '' && $('#roleId').val() != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/roleMenu/getRoleMenuTree"/>");
			ajax.put("idenId", $('#idenId').val());
			ajax.put("roleId", $('#roleId').val());
			ajax.call(function(json) {
				if (json) {
					$('#tree').show();
					$('#tree')
					.jstree({ 
						'core' : {
						    'data' : json.result.data,
						    'check_callback' : true
						},
						"plugins" : ["contextmenu", "checkbox"],
					}).on('loaded.jstree', function () {
					     $(".jstree").jstree('open_all');					     
					});
				}
				else {
					$("#tree").jstree("destroy");
				}
			});
		}		
	}

	function getRoleList() {
		$('#roleId').empty();
		$("#roleId").append($("<option>").prop({"value":""}).append("請選擇"));
		var idenId = $('#idenId').val();
		if (idenId != null && idenId != '') {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/roleMenu/getRoleList"/>");
			ajax.put("idenId", idenId);
			ajax.call(function(json) {
				if (json.result.dataList != null) {
					$.each(json.result.dataList,function() {
						var data = this;
						$("#roleId").append($("<option>").prop({"value":data.roleId}).append(data.roleId + "-" + data.roleCname));
					});
				}
			});
		}		
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="form-horizontal">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
								<div class="col-sm-9">
									<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
										xauthType="x-iden-qry" listKey="idenId" listValue="struct" headerKey="請選擇" headerValue="" required="required"/>
								</div>
							</div>
						</div>						
					</div>	
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label"><spring:message code="xauth.field.role"/></label>
								<div class="col-sm-9">
									<select id="roleId" name="roleId" class="form-control select2-single" required>
										<option value="">請選擇</option>
									</select>
								</div>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<div class="col-sm-9">
									<div id="tree" class="demo"></div>
								</div>
							</div>
						</div>
					</div>				
					<c:choose>
						<c:when test="${principal.userType ne '02'}">
							<button id="btnSave" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.save"/>
							</button>
		            		<button id="btnClear" type="button" class="btn btn-outline-info btn-fw btnClear">
		            			<spring:message code="common.clear"/>
		            		</button>
						</c:when>
					</c:choose>
				</form>
			</div>
		</div>
	</div>
</div>
