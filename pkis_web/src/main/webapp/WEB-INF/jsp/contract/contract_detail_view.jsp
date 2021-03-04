<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	var grid_RE;
	var grid_FD;
	$(function () {
	
 		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '流程關卡', name: 'idenId', width: 90, sortable: false });
		grid.addColumn({ label: '處理人', name: 'ipAddr', width: 90, sortable: false });
		grid.addColumn({ label: '處理時間', name: 'sysType', width: 90, sortable: false, hidden: true });
		grid.addColumn({ label: '流程狀態', name: 'sysTypeName', width: 90, sortable: false });
		grid.addColumn({ label: '版次', name: 'enabledName', width: 100, sortable: false });		
		grid.addColumn({ label: '備註', name: 'memo', width: 100, sortable: false });
/* 		grid.addColumn({ label: '合約版次', name: 'creUser', width: 100, sortable: false });
		grid.addColumn({ label: '合約狀態', name: 'creDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '承辦人', name: 'updUser', width: 100, sortable: false });
		grid.addColumn({ label: '部門', name: 'updDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="common.grid.action"/>', width:100, exportcol:false, formatter:girdCmds});
		grid.setController("<c:url value='/xauth/ipGrant/query' />");	 */	
		/* grid.setPager('jqGridPager'); */
		grid.init();
		
		grid_RE = $("#dataGrid_RE").tvGrid();	
		grid_RE.addColumn({ label: '', name: 'idenId', width: 90, sortable: false });
		grid_RE.addColumn({ label: '', name: 'ipAddr', width: 90, sortable: false });
		grid_RE.addColumn({ label: '', name: 'sysType', width: 90, sortable: false, hidden: true });
		grid_RE.addColumn({ label: '', name: 'sysTypeName', width: 90, sortable: false });
		grid_RE.addColumn({ label: 'Direct Cost_Simulation', name: 'enabledName', width: 100, sortable: false });		
		grid_RE.addColumn({ label: 'With Margin_Simulation', name: 'memo', width: 100, sortable: false });
		grid_RE.addColumn({ label: 'With Margin_Actual', name: 'creUser', width: 100, sortable: false });
		grid_RE.addColumn({ label: 'Remark', name: 'creDateDesc', width: 100, sortable: false });
		/* grid_RE.setPager('jqGridPager_RE'); */
		grid_RE.init();
		
		grid_FD = $("#dataGrid_FD").tvGrid();	
		grid_FD.addColumn({ label: '檔名', name: 'idenId', width: 90, sortable: false });
		grid_FD.addColumn({ label: '說明', name: 'ipAddr', width: 90, sortable: false });
		grid_FD.addColumn({ label: '下載', name: 'sysType', width: 90, sortable: false });
		/* grid_FD.setPager('jqGridPager_FD'); */
		grid_FD.init();
		
		$("#btnQuery").on("click", function() {			
			grid.putParams("idenId", $('#idenId').val());
			grid.putParams("ipAddr", $('#ipAddr').val());
			grid.putParams("sysType", $('#sysType').val());
			grid.putParams("enabled", $('#enabled').val());
			grid.load();			
		});
		$('#basic_information').show();
		$('#grid_info').show();		
		$('#Review_Evaluation').hide();
		$('#file_download').hide();
		$('#btnBasicInformation').on("click",function(){
			$('#basic_information').show();
			$('#grid_info').show();	
			$('#Review_Evaluation').hide();
			$('#file_download').hide();
		});
		
		$('#btnReviewEvaluation').on("click",function(){
			$('#basic_information').hide();
			$('#Review_Evaluation').show();
			$('#grid_info').hide();	
			$('#file_download').hide();			
		});
		
		$('#btnDownload').on("click",function(){
			$('#basic_information').hide();
			$('#Review_Evaluation').hide();
			$('#grid_info').hide();	
			$('#file_download').show();
		});

		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
		
	});

	function girdCmds(cellvalue, options, rowObject) {			  
        var cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
        				'onclick="toView(\'' + options.rowId +'\');">檢視</button>' ;
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
					<c:out value="${menuName}"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample">				
					<button id="btnBasicInformation" type="button" class="btn btn-outline-primary btn-fw">基本資料</button>
					<button id="btnReviewEvaluation" type="button" class="btn btn-outline-primary btn-fw">審核評估</button>
					<button id="btnDownload" type="button" class="btn btn-outline-primary btn-fw">文檔下載</button>
				</form>
			</div>
		</div>
	</div>
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body" id='basic_information'>				
				<header class="card-title">
					供應商資料<%-- <c:out value="${menuName}"/>明細<spring:message code="xauth.function.bread.edit.suffix"/> --%>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/list/view' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">供應商統一編號:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">123456<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">供應商廠編:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">供應商全名:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
		                  	</div>
		                </div>
		                <div class="col-md-6">
		                	<div class="form-group row">
								<label class="col-sm-3 col-form-label">單位:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div>               
		                </div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">供應商地址:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
		                  	</div>
		                </div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">部門:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
						</div>
	   			--------------------------------------------------------------------------------------------------------------------------------------------------------
					</div>
					<header class="card-title">
					合約資料<%-- <c:out value="${menuName}"/>明細<spring:message code="xauth.function.bread.edit.suffix"/> --%>
					</header>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約編號:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">123456<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約簽約日:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約名稱:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
		                  	</div>
		                </div>
		                <div class="col-md-6">
		                	<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約效期:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div>               
		                </div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約類別:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
		                  	</div>
		                </div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約年度:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
						</div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">合約版次:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
		                  	</div>
		                </div>
						<div class="col-md-6">
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">承辦人:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
						</div>
	   			--------------------------------------------------------------------------------------------------------------------------------------------------------
					</div>
<%-- 					<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query"/>
					</button>					
					<button id="btnClear" type="button" class="btnClear btn btn-outline-info btn-fw">
						<spring:message code="common.clear"/>
					</button>
					<button id="btnInsert" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.insert"/>
					</button>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"  
						onclick="window.location.href='<c:url value='/xauth/menu/' />'">匯出清單
						<spring:message code="common.back"/>
					</button> --%>
					<button id="btnBack" type="button" class="btn btn-outline-dark btn-fw"  
						onclick="window.location.href='<c:url value='/contract/list/' />'">
						<spring:message code="common.back"/>
					</button>						
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="card-body" id='Review_Evaluation'>				
				<header class="card-title">
					數據匯入檔名: dkfjasldkfj<%-- <c:out value="${menuName}"/>明細<spring:message code="xauth.function.bread.edit.suffix"/> --%>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/list/view' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Dpno:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">123456<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier Code:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier CName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
		                  	</div>
		                	<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
						</div>
						<div class="card-body">
							<div >
								<table id="dataGrid_RE"></table>
	   							<!-- <div id="jqGridPager_RE"></div> -->
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">1、"Year"值為 <%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">123456<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">2、"Flow"值為<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>
		                  	<div class="form-group row">
								<label class="col-sm-3 col-form-label">3、"2019" 預估成本 "Hyper" =<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
		                  	</div>
		                	<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
						    <div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">Supplier EName:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>	 
							</div> 
						</div>
					</div>										
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div id='file_download'>	
				<div class="card-body">		
				<header class="card-title">
					文檔下載<%-- <c:out value="${menuName}"/>明細<spring:message code="xauth.function.bread.edit.suffix"/> --%>
				</header>
				</div>
				<div  class="col-12 grid-margin stretch-card">
					<div class="card-body">
						<div >
							<table id="dataGrid_FD"></table>
			   				<!-- <div id="jqGridPager_FD"></div> -->
						</div>
					</div>	
				</div>			
				<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</div>
		</div>
	</div>
	<div class="col-12 grid-margin stretch-card" id='grid_info'>
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
