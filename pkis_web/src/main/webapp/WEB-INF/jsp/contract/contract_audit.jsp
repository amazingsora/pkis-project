<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	$(function () {
	
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '流程編號', name: 'idenId', width: 90, sortable: false });
		grid.addColumn({ label: '合約型態', name: 'ipAddr', width: 90, sortable: false });
		grid.addColumn({ label: '合約編號', name: 'sysType', width: 90, sortable: false, hidden: true });
		grid.addColumn({ label: '合約名稱', name: 'sysTypeName', width: 90, sortable: false });
		grid.addColumn({ label: '承辦人', name: 'enabledName', width: 100, sortable: false });		
		grid.addColumn({ label: '單位-課別', name: 'memo', width: 100, sortable: false });
		grid.addColumn({ label: '乙方名稱', name: 'creUser', width: 100, sortable: false });
		grid.addColumn({ label: '收到時間', name: 'creDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:100, exportcol:false, formatter:girdCmds});
		grid.setController("<c:url value='/xauth/ipGrant/query' />");		
		grid.init();
		
		$("#btnQuery").on("click", function() {			
			grid.putParams("idenId", $('#idenId').val());
			grid.putParams("ipAddr", $('#ipAddr').val());
			grid.putParams("sysType", $('#sysType').val());
			grid.putParams("enabled", $('#enabled').val());
			grid.load();			
		});

		$("#btnInsert").on("click", function() {
			$("#dataForm").prop('action',"<c:url value='/contract/list/insert' />").submit();;
			//form.action = "<c:url value='/contract/list/insert' />";
			//form.submit();
		});
		
	});

	function girdCmds(cellvalue, options, rowObject) {			  
        var cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
        				'onclick="toView(\'' + options.rowId +'\');">審核</button>' ;
//           		   '<button id="btnDelete" type="button" class="btn btn-sm btn-danger" ' +
//           		   		'onclick="toDelete(\'' + options.rowId +'\');"><spring:message code="common.grid.action.delete"/></button>';
        return cmd;
	}
	
	//檢視
	function toView(rid) {
		$('#keyData').val(JSON.stringify(grid.getRowData(rid)));
		$("#dataForm").submit();
	}
	
	function toDelete(rid) {
		$.fn.confirm('<spring:message code="common.confirm.delete"/>', function() {
			var ajax = new $.tvAjax();
			ajax.setController("<c:url value="/xauth/ipGrant/delete"/>");
			ajax.put("paramsData", JSON.stringify(grid.getRowData(rid)));
			ajax.call(function(result) {
				if (result) {
					$.fn.alert(result.messages, function() {
						$("#btnQuery").click();
					});
				}
			});
		});
	}
	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/><%-- <spring:message code="xauth.function.bread.edit.suffix"/> --%>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/audit/auditSingle' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">合約名稱:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<c:choose>
										<c:when test="${not empty data.menuId}">
											<label class="col-form-label"><c:out value="${data.menuId}"/></label>
											<input type="hidden" id="menuId" name="menuId" value="<c:out value="${data.menuId}"/>" >
										</c:when>
										<c:otherwise>
											<input type="text" id="menuId" name="menuId" class="form-control" value="<c:out value="${data.menuId}"/>" required>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label">組織:<%-- <spring:message code="xauth.field.menu.url"/> --%></label>
<%-- 		                    	<div class="col-sm-9">
		                      		<input type="text" id="menuUrl" name="menuUrl" class="form-control" value="<c:out value="${data.menuUrl}"/>">
		                    	</div> --%>
		                    	<div class="col-sm-9">
		                      		<combox:xauth id="parentId" name="parentId" class="form-control select2-single" 
										xauthType="x-menu" listKey="selDdl" listValue="struct" headerKey="請選擇" headerValue=""/>
		                    	</div>
		                  	</div>
		                </div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">合約編號:<%-- <spring:message code="xauth.field.menu.name"/> --%></label>
								<div class="col-sm-9">
									<input type="text" id="menuCname" name="menuCname" class="form-control" value="<c:out value="${data.menuCname}"/>" required>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label">單位:<%-- <spring:message code="xauth.field.menu.parent"/> --%></label>
		                    	<div class="col-sm-9">
		                      		<combox:xauth id="parentId" name="parentId" class="form-control select2-single" 
										xauthType="x-menu" listKey="selDdl" listValue="struct" headerKey="請選擇" headerValue=""/>
		                    	</div>
		                  	</div>
		                </div>
					</div>	
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">乙方統編:<%-- <spring:message code="xauth.field.menu.memo"/> --%></label>
								<div class="col-sm-9">
									<input type="text" id="memo" name="memo" class="form-control" value="<c:out value="${data.memo}"/>">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">部門:<%-- <spring:message code="xauth.field.create.user"/> --%></label>
<%-- 								<div class="col-sm-9">									
									<label class="col-form-label"><c:out value="${data.creUser}"/></label>
								</div> --%>
		                    	<div class="col-sm-9">
		                      		<combox:xauth id="parentId" name="parentId" class="form-control select2-single" 
										xauthType="x-menu" listKey="selDdl" listValue="struct" headerKey="請選擇" headerValue=""/>
		                    	</div>
							</div>
						</div>
					</div>					
					<div class="row">
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label">合約類別:<%-- <spring:message code="xauth.field.create.date"/> --%></label>
<%-- 		                    	<div class="col-sm-9">		                      		
		                      		<label class="col-form-label"><c:out value="${data.creDateDesc}"/></label>
		                    	</div> --%>
		                    	<div class="col-sm-9">
		                      		<combox:xauth id="parentId" name="parentId" class="form-control select2-single" 
										xauthType="x-menu" listKey="selDdl" listValue="struct" headerKey="請選擇" headerValue=""/>
		                    	</div>
		                  	</div>
		                </div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約狀態:<%-- <spring:message code="xauth.field.update.user"/> --%></label>
<%-- 								<div class="col-sm-9">									
									<label class="col-form-label"><c:out value="${data.updUser}"/></label>
								</div> --%>
		                    	<div class="col-sm-9">
		                      		<combox:xauth id="parentId" name="parentId" class="form-control select2-single" 
										xauthType="x-menu" listKey="selDdl" listValue="struct" headerKey="請選擇" headerValue=""/>
		                    	</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
		                  	<div class="form-group row">
		                    	<label class="col-sm-3 col-form-label">承辦人員:<%-- <spring:message code="xauth.field.update.date"/> --%></label>
								<div class="col-sm-9">
									<input type="text" id="memo" name="memo" class="form-control" value="<c:out value="${data.memo}"/>">
								</div>
		                  	</div>
		                </div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約期間:<%-- <spring:message code="xauth.field.update.user"/> --%></label>
								<div class="col-sm-4">
									<input type="text" id="bgnDate" name="bgnDate" class="form-control date" value="<c:out value="${data.bgnDateDesc}"/>" required>
								</div>~
								<div class="col-sm-4">
		                      		<input type="text" id="endDate" name="endDate" class="form-control date" value="<c:out value="${data.endDateDesc}"/>" >
		                    	</div>
							</div>
						</div>
					</div>
<%-- 					<c:choose>
						<c:when test="${empty data}">
							<button id="btnInsert" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.insert"/>
							</button>
						</c:when>
						<c:otherwise>
							<button id="btnUpdate" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.update"/>
							</button>
							<button id="btnDel" type="button" class="btn btn-outline-danger btn-fw">
								<spring:message code="common.delete"/>
							</button>
						</c:otherwise>
					</c:choose> --%>
					<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query"/>
					</button>					
					<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
						<spring:message code="common.clear"/>
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"  
						onclick="window.location.href='<c:url value='/xauth/menu/' />'">匯出清單
						<%-- <spring:message code="common.back"/> --%>
					</button>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">
				<div >
					<table id="dataGrid"></table>
	   				<!-- <div id="jqGridPager"></div> -->
				</div>
			</div>
		</div>
	</div>
</div>
