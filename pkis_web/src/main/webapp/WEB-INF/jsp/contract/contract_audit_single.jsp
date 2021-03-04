<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
    var grid_AA;
    var grid_OP;
	$(function () {	
		grid_AA = $("#dataGrid_AA").tvGrid();	
		grid_AA.addColumn({ label: '', name: 'idenId', width: 90, sortable: false });
		grid_AA.addColumn({ label: '', name: 'ipAddr', width: 90, sortable: false });
		grid_AA.addColumn({ label: '', name: 'sysType', width: 90, sortable: false, hidden: true });
		grid_AA.addColumn({ label: '', name: 'sysTypeName', width: 90, sortable: false });
		grid_AA.addColumn({ label: 'Direct Cost_Simulation', name: 'enabledName', width: 100, sortable: false });		
		grid_AA.addColumn({ label: 'With Margin_Simulation', name: 'memo', width: 100, sortable: false });
		grid_AA.addColumn({ label: 'With Margin_Actual', name: 'creUser', width: 100, sortable: false });
		grid_AA.addColumn({ label: 'Remark', name: 'creDateDesc', width: 100, sortable: false });
		/* grid_AA.setPager('jqGridPager_AA'); */
		grid_AA.init();

		grid_OP = $("#dataGrid_opinion").tvGrid();	
		grid_OP.addColumn({ label: '流程關卡', name: 'enabledName', width: 100, sortable: false });		
		grid_OP.addColumn({ label: '狀態 ', name: 'memo', width: 100, sortable: false });
		grid_OP.addColumn({ label: '版次', name: 'creUser', width: 100, sortable: false });
		grid_OP.addColumn({ label: '審核時間', name: 'creDateDesc', width: 100, sortable: false });
		grid_OP.addColumn({ label: '審核者', name: 'creDateDesc', width: 100, sortable: false });
		grid_OP.addColumn({ label: '審核意見', name: 'creDateDesc', width: 100, sortable: false });
		/* grid_OP.setPager('jqGridPager_opinion'); */
		grid_OP.init();
		
		$('#contract_0').show();
		$('#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,#contract_6').hide();
		
		$('#audit_assessment').hide();
		$('#supplier_info').show();
		$('#contract_terms').hide();
		$('#audit_opinion').hide();
		
		$('#btnAuditAssessment').on("click",function(){
			$('#supplier_info').hide();
			$('#audit_assessment').show();
			$('#contract_terms').hide();
			$('#audit_opinion').hide();
		});
		
		$('#btnSupplierInfo').on('click',function(){
			$('#supplier_info').show();
			$('#contract_terms').hide();
			$('#audit_assessment').hide();
			$('#audit_opinion').hide();
		});
		
		$('#btnContractTerms').on('click',function(){
			$('#supplier_info').hide();
			$('#contract_terms').show();
			$('#audit_assessment').hide();
			$('#audit_opinion').hide();
		});
		
		$('#btnAuditOpinion').on('click',function(){
			$('#supplier_info').hide();
			$('#contract_terms').hide();
			$('#audit_assessment').hide();
			$('#audit_opinion').show();
		});
		
		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
		
		$('#contract0').on('click',function(){
			$('#contract_0').show();
			$('#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,#contract_6').hide();			
		});
		
		$('#contract1').on('click',function(){
			$('#contract_1').show();
			$('#contract_0,#contract_2,#contract_3,#contract_4,#contract_5,#contract_6').hide();
		});
		
		$('#contract2').on('click',function(){
			$('#contract_2').show();
			$('#contract_0,#contract_1,#contract_3,#contract_4,#contract_5,#contract_6').hide();
		});
		
		$('#contract3').on('click',function(){
			$('#contract_3').show();
			$('#contract_0,#contract_1,#contract_2,#contract_4,#contract_5,#contract_6').hide();
		});
		
		$('#contract4').on('click',function(){
			$('#contract_4').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_5,#contract_6').hide();
		});
		
		$('#contract5').on('click',function(){
			$('#contract_5').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_6').hide();
		});
		
		$('#contract6').on('click',function(){
			$('#contract_6').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5').hide();
		});

	});	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/>資料表
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample">					
					<button id="btnSupplierInfo" type="button" class="btn btn-outline-primary btn-fw">基本資料</button>
					<button id="btnContractTerms" type="button" class="btn btn-outline-primary btn-fw">合約條款</button>
					<button id="btnAuditAssessment" type="button" class="btn btn-outline-primary btn-fw">審核評估</button>
					<button id="btnAuditOpinion" type="button" class="btn btn-outline-primary btn-fw">審核意見</button>
				</form>
			</div>
		</div>
	</div>
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body" id='supplier_info'>				
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/audit/single' />" method="POST">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">流程編號:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-4 col-form-label">合約名稱:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>	
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-4 col-form-label">合約型態:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
		                      		<combox:xauth id="parentId" name="parentId" class="form-control select2-single" 
										xauthType="x-menu" listKey="selDdl" listValue="struct" headerKey="請選擇" headerValue=""/>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">合約編號:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-4 col-form-label">單位/課別:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-3">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
								<div class="col-sm-2">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
								<div class="col-sm-3">
									<button id="btnInsert" type="button" class="btn btn-primary btn-sm">
										課別
									</button>
								</div>
							</div>
						</div>	
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-4 col-form-label">承辦人:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
		                      		<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">合約效期:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-4 col-form-label">供應商廠編:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>	
						<div class="col-md-4">
							<div class="form-group row">
								<label data-require=true class="col-sm-4 col-form-label">電話:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-8">
		                      		<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>
					</div>					
					<div class="col-12 grid-margin stretch-card">
						<div class="card">
							<div class="card-body" >	
								<div class="row">
									甲方
								</div>
								<div class="row">
									<div class="col-md-4">
										<div class="form-group row">
											<label class="col-sm-4 col-form-label">公司名稱(中):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
										</div>
									</div>
									<div class="col-md-4">
										<div class="form-group row">
											<label class="col-sm-4 col-form-label">公司名稱(英):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
										</div>
									</div>
									<div class="col-md-4">
		                  				<div class="form-group row">
											<label class="col-sm-4 col-form-label">法定代理人:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
		                  				</div>
		                			</div>
	                			</div>	
								<div class="row">
									<div class="col-md-8">
										<div class="form-group row">
											<label class="col-sm-2 col-form-label">公司地址(中):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-10">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
										</div>
									</div>
									<div class="col-md-4">
		                  				<div class="form-group row">
											<label class="col-sm-4 col-form-label">Tel:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<textarea type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" ></textarea>
											</div>
		                  				</div>
		                			</div>
	                			</div>	
								<div class="row">
									<div class="col-md-8">
										<div class="form-group row">
											<label class="col-sm-2 col-form-label">公司地址(英):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-10">
												<textarea type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" ></textarea>
											</div>
										</div>
									</div>
									<div class="col-md-4">
		                  				<div class="form-group row">
											<label class="col-sm-4 col-form-label">Fax:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
		                  				</div>
		                			</div>
	                			</div>					                
							</div>
						</div>
					</div>
					<div class="col-12 grid-margin stretch-card">
						<div class="card">
							<div class="card-body" >	
								<div class="row">
									乙方
								</div>
								<div class="row">
									<div class="col-md-4">
										<div class="form-group row">
											<label class="col-sm-4 col-form-label">公司名稱(中):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<textarea type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" ></textarea>
											</div>
										</div>
									</div>
									<div class="col-md-4">
										<div class="form-group row">
											<label class="col-sm-4 col-form-label">公司名稱(英):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<textarea type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" ></textarea>
											</div>
										</div>
									</div>
									<div class="col-md-4">
		                  				<div class="form-group row">
											<label class="col-sm-4 col-form-label">法定代理人:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
		                  				</div>
		                			</div>
	                			</div>	
								<div class="row">
									<div class="col-md-8">
										<div class="form-group row">
											<label class="col-sm-2 col-form-label">公司地址(中):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-10">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
										</div>
									</div>
									<div class="col-md-4">
		                  				<div class="form-group row">
											<label class="col-sm-4 col-form-label">Tel:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
		                  				</div>
		                			</div>
	                			</div>	
								<div class="row">
									<div class="col-md-8">
										<div class="form-group row">
											<label class="col-sm-2 col-form-label">公司地址(英):<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-10">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
										</div>
									</div>
									<div class="col-md-4">
		                  				<div class="form-group row">
											<label class="col-sm-4 col-form-label">Fax:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
											<div class="col-sm-8">
												<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
											</div>
		                  				</div>
		                			</div>
	                			</div>					                
							</div>
						</div>
					</div>					
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<!--  -->
			<div class="row" id="contract_terms">
				<div class="card-body">
					<div class="col-md-13">
						<div class="form-group row">
							<button id="contract0" type="button" class="btn btn-outline-primary btn-fw">0、前言</button>
							<button id="contract1" type="button" class="btn btn-outline-primary btn-fw">1、Definition 定義 </button>
							<button id="contract2" type="button" class="btn btn-outline-primary btn-fw">2、Order and Delivery 商品訂貨與交付 </button>
							<button id="contract3" type="button" class="btn btn-outline-primary btn-fw">3、Shortage Penalty 缺貨罰款</button>
							<button id="contract4" type="button" class="btn btn-outline-primary btn-fw">4、Return 退貨</button>
							<button id="contract5" type="button" class="btn btn-outline-primary btn-fw">5、Product Bar Code 商品條碼之交付</button>
							<button id="contract6" type="button" class="btn btn-outline-primary btn-fw">6、Approval Standard 驗收標準</button>				
						</div>
					</div>
					<div class="col-md-11" id = "contract_0">
							0. 前言
					</div>
					<div class="col-md-11" id = "contract_1">
							1. 前言
					</div>
					<div class="col-md-11" id = "contract_2">
							2. 前言
					</div>
					<div class="col-md-11" id = "contract_3">
							3. 前言
					</div>
					<div class="col-md-11" id = "contract_4">
							4. 前言
					</div>
					<div class="col-md-11" id = "contract_5">
							5. 前言
					</div>
					<div class="col-md-11" id = "contract_6">
							6. 前言
					</div>
				</div>	
			<!--  -->
			</div>
			<div class="card-body" id='audit_assessment'>	
				<div class="row">
					<div class="col-md-5">
						<header class="card-title">
							數據匯入檔名: dkfjasldkfj<%-- <c:out value="${menuName}"/>明細<spring:message code="xauth.function.bread.edit.suffix"/> --%>
						</header>
					</div>
				</div>			
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
								<table id="dataGrid_AA"></table>
	   							<!-- <div id="jqGridPager_AA"></div> -->
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
			<div class="card-body" id='audit_opinion'>	
				<div class="row">
					<header class="card-title">
						審核資料表
					</header>
				</div>			
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/list/view' />" method="POST">
					<div class="row">
						<label class="col-sm-3 col-form-label">審核意見:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
					</div>	
					<div class="row">
						<div class="col-md-9">
							<div class="form-group row">
									<textarea id="testarea"  name="file" class="form-control" ></textarea>
							</div>
						</div>
					</div>		
					<div class="row">
						<div class="col-md-6">
							<button id="btnUpload" type="button" class="btn btn-primary btn-fw">儲存</button>
							<button id="btnClear" type="button" class="btnClear btn btn-primary btn-fw">清除</button>
							<button id="btnInsert" type="button" class="btn btn-primary btn-fw">審核退回</button>
							<button id="btnInsert" type="button" class="btn btn-primary btn-fw">審核通過</button>
						</div>
					</div>		
					<div class="row">
						<div class="col-md-6">
						<label class="col-sm-2 col-form-label">清單:</label>
							<div class="form-group row">							
								<table id="dataGrid_opinion"></table>
				   				<!-- <div id="jqGridPager_opinion"></div> -->
							</div>
						</div>
					</div>								
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
