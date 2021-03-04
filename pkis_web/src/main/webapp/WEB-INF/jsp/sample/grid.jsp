<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	var gridDept;
	var girdUser;
	$(function () {
	
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '<spring:message code="xauth.field.iden.id"/>', name: 'idenId', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.sys.code.gp"/>', name: 'gp', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.sys.code.code"/>', name: 'code', width: 100, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.sys.code.cname"/>', name: 'cname', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.enabled"/>', name: 'enabledName', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.date.begin"/>', name: 'bgnDateDesc', width: 90, sortable: false });
		grid.addColumn({ label: '<spring:message code="xauth.field.date.end"/>', name: 'endDateDesc', width: 90, sortable: false });		
		grid.setController("<c:url value='/manage/sysCode/query' />");	
		
		<%-- 設置分頁標籤 --%>
		grid.setPager('jqGridPager');
		
		<%-- 設置每頁顯示筆數 --%>
		grid.setRows(10);
		
		<%-- 
			是否顯示多選控制項
			Y:是
			N:否
		--%>
		grid.showMultiselect('Y');
		
		<%-- 
			分頁顯示方式  
			Y:顯示全部資料，滾動捲軸時向後端取資料
			N:顯示分頁
		--%>
		grid.showAll('Y');
		
		<%-- 
			是否顯示匯出EXCEL按鈕
			Y:是
			N:否
		--%>
		grid.showExportXls('N');
		
		<%-- 
			是否顯示匯出PDF按鈕
			Y:是
			N:否
		--%>
		grid.showExportPdf('N');
		grid.init();
		
		$("#btnQuery").on("click", function() {				
			grid.putParams("idenId", $('#idenId').val());
			grid.putParams("gp", $('#gp').val());
			grid.putParams("code", $('#code').val());
			grid.load();			
		});
		
		<%-- 選擇列 --%>
		$("#btnRow").on("click", function() {	
			var dataArray = grid.getSelect();			
			if (dataArray) {
				console.log(dataArray.length);
				for (var i=0 ; i<dataArray.length ; i++) {
					console.log(dataArray[i]);
				}
			}
			else {
				$.fn.alert('請選擇資料列');
			}
		});				
		
		gridDept = $("#dataGridDept").tvGrid();		
		gridDept.addColumn({ label: '<spring:message code="xauth.field.iden.id"/>', name: 'idenId', width: 90, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.iden.ban"/>', name: 'ban', width: 90, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.iden.name"/>', name: 'cname', width: 100, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.iden.type"/>', name: 'idenTypeName', width: 100, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.enabled"/>', name: 'enabledName', width: 90, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.create.user"/>', name: 'creUser', width: 100, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.create.date"/>', name: 'creDateDesc', width: 100, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.update.user"/>', name: 'updUser', width: 100, sortable: false });
		gridDept.addColumn({ label: '<spring:message code="xauth.field.update.date"/>', name: 'updDateDesc', width: 100, sortable: false });		
		gridDept.setController("<c:url value='/manage/dept/query'/>");
		gridDept.setPager('jqGridPagerDept');
		gridDept.init();
		
		$("#btnQueryDept").on("click", function() {	
			gridDept.putParams("appId", $('#appId').val());
			gridDept.putParams("idenId", $('#idenId').val());
			gridDept.putParams("ban", $('#ban').val());
			gridDept.putParams("idenType", $('#idenType').val());	
			gridDept.putParams("cname", $('#cname').val());	
			gridDept.load();			
		});
		
		
		girdUser = $("#dataGridUser").tvGrid();	
		girdUser.addColumn({ label: '<spring:message code="xauth.field.iden.id"/>', name: 'idenId', width: 90, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.user.id"/>', name: 'userId', width: 90, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.user.name"/>', name: 'userCname', width: 100, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.enabled"/>', name: 'enabledName', width: 100, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.date.begin"/>', name: 'bgnDateDesc', width: 90, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.date.end"/>', name: 'endDateDesc', width: 90, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.create.user"/>', name: 'creUser', width: 100, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.create.date"/>', name: 'creDateDesc', width: 100, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.update.user"/>', name: 'updUser', width: 100, sortable: false });
		girdUser.addColumn({ label: '<spring:message code="xauth.field.update.date"/>', name: 'updDateDesc', width: 100, sortable: false });		
		girdUser.setController("<c:url value='/manage/user/query' />");	
		girdUser.setPager('jqGridPagerUser');
		girdUser.init();
			
		$("#btnQueryUser").on("click", function() {	
			girdUser.putParams("idenId", $('#idenId').val());
			girdUser.putParams("userId", $('#userId').val());		
			girdUser.putParams("userCname", $('#userCname').val());
			girdUser.load();
		});
		
		<%-- tabs --%>
		$('#myTab a').click(function (e) {
			$(this).tab('show');
			var tabId = $(this).prop('id');
			if (tabId == 'tab1') {
				gridDept.resize();
			}
			else if (tabId == 'tab2') {
				girdUser.resize();
			}			
		});
	});

	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/><spring:message code="xauth.function.bread.query.suffix"/>
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/manage/sysCode/data' />" method="POST">
					<input type="hidden" id="keyData" name="keyData"/>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
								<div class="col-sm-9">
									<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
										xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.gp"/></label>
								<div class="col-sm-9">
									<input type="text" id="gp" name="gp" class="form-control" placeholder="" value="SAMPLE" readonly="readonly">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.sys.code.code"/></label>
								<div class="col-sm-9">
									<input type="text" id="code" name="code" class="form-control" placeholder="">
								</div>
							</div>
						</div>
					</div>
					<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query"/>
					</button>
		            <button id="btnRow" type="button" class="btn btn-outline-primary btn-fw">
						選擇列(checkbox)
					</button>
		            <button id="btnClear" type="button" class="btn btn-outline-info btn-fw btnClear">
		            	<spring:message code="common.clear"/>
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
	   				<div id="jqGridPager"></div>
				</div>
			</div>
		</div>
	</div>
</div>


<div class="row">
	<div class="col-md-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body dashboard-tabs p-0">
				<ul id="myTab" class="nav nav-tabs px-4" role="tablist">
					<li class="nav-item">
						<a class="nav-link active" id="tab1" data-toggle="tab" href="#company" role="tab" aria-controls="company" aria-selected="true">部門資料</a>
                    </li>
                    <li class="nav-item">
						<a class="nav-link" id="tab2" data-toggle="tab" href="#user" role="tab" aria-controls="user" aria-selected="false">使用者資料</a>
                    </li>
				</ul>
				<div class="tab-content py-0 px-0">
					<div class="tab-pane fade show active" id="company" role="tabpanel" aria-labelledby="tab1">
						<div class="card-body">
							<header class="card-title">
								部門資料查詢
							</header>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
										<div class="col-sm-9">
											<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
												xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden.ban"/></label>
										<div class="col-sm-9">
											<input type="text" id="ban" name="ban" class="form-control" placeholder="">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden.type"/></label>
										<div class="col-sm-9">
											<combox:xauth id="idenType" name="idenType" class="form-control select2-single" 
												xauthType="x-iden-type" listKey="key" listValue="value" />
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden.name"/></label>
										<div class="col-sm-9">
											<input type="text" id="cname" name="cname" class="form-control" placeholder="">
										</div>
									</div>
								</div>
							</div>
							<button id="btnQueryDept" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.query"/>
							</button>
						</div>
						<div class="card-body">
							<div>
								<table id="dataGridDept"></table>
				   				<div id="jqGridPagerDept"></div>
							</div>
						</div>	
					</div>
					<div class="tab-pane fade" id="user" role="tabpanel" aria-labelledby="tab2">
						<div class="card-body">
							<header class="card-title">
								使用者資料查詢
							</header>															
							<div class="row">
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.iden"/></label>
										<div class="col-sm-9">
											<combox:xauth id="idenId" name="idenId" class="form-control select2-single" 
												xauthType="x-iden-qry" listKey="idenId" listValue="struct" />
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.id"/></label>
										<div class="col-sm-9">
											<input type="text" id="userId" name="userId" class="form-control" placeholder="">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-sm-3 col-form-label"><spring:message code="xauth.field.user.name"/></label>
										<div class="col-sm-9">
											<input type="text" id="userCname" name="userCname" class="form-control" placeholder="">
										</div>
									</div>
								</div>
							</div>
							<button id="btnQueryUser" type="button" class="btn btn-outline-primary btn-fw">
								<spring:message code="common.query"/>
							</button>	
						</div>
						<div class="card-body">
							<div>
								<table id="dataGridUser"></table>
				   				<div id="jqGridPagerUser"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
