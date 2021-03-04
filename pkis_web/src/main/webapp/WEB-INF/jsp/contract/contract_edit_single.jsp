<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
    var grid_RE;
    var grid_FE;
	$(function () {	
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

		grid_FE = $("#dataGrid_fileEdit").tvGrid();	
		grid_FE.addColumn({ label: '檔名', name: 'enabledName', width: 100, sortable: false });		
		grid_FE.addColumn({ label: '說明', name: 'memo', width: 100, sortable: false });
		grid_FE.addColumn({ label: '下載', name: 'creUser', width: 100, sortable: false });
		grid_FE.addColumn({ label: '刪除', name: 'creDateDesc', width: 100, sortable: false });
		/* grid_FE.setPager('jqGridPager_fileEdit') */;
		grid_FE.init();
		
		$('#contract_0').show();
		$('#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
		  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
		  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		
		$('#supplier').hide();
		$('#Review_Evaluation').hide();
		$('#supplier_info').show();
		$('#contract_terms').hide();
		$('#Doc_Edit').hide();
		
		$('#btnQuery').on('click',function(){
			$('#supplier').show();
			$('#contract_base_info').hide();
		});
		
		$('#btnReviewEvaluation').on("click",function(){
			$('#supplier_info').hide();
			$('#Review_Evaluation').show();
			$('#contract_terms').hide();
			$('#supplier').hide();
			$('#Doc_Edit').hide();
		});
		
		$('#btnSupplierInfo').on('click',function(){
			$('#supplier_info').show();
			$('#supplier').hide();
			$('#contract_terms').hide();
			$('#Review_Evaluation').hide();
			$('#Doc_Edit').hide();
		});
		
		$('#btnContractTerms').on('click',function(){
			$('#supplier_info').hide();
			$('#supplier').hide();
			$('#contract_terms').show();
			$('#Review_Evaluation').hide();
			$('#Doc_Edit').hide();
		});
		
		$('#btnDocFileEidt').on('click',function(){
			$('#supplier_info').hide();
			$('#supplier').hide();
			$('#contract_terms').hide();
			$('#Review_Evaluation').hide();
			$('#Doc_Edit').show();
		});
		
		$("#btnInsert").on("click", function() {
			$("#dataForm").submit();
		});
		
		$('#contract0').on('click',function(){
			$('#contract_0').show();
			$('#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
			
		});
		$('#contract1').on('click',function(){
			$('#contract_1').show();
			$('#contract_0,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract2').on('click',function(){
			$('#contract_2').show();
			$('#contract_0,#contract_1,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract3').on('click',function(){
			$('#contract_3').show();
			$('#contract_0,#contract_1,#contract_2,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract4').on('click',function(){
			$('#contract_4').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract5').on('click',function(){
			$('#contract_5').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract6').on('click',function(){
			$('#contract_6').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract7').on('click',function(){
			$('#contract_7').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract8').on('click',function(){
			$('#contract_8').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract9').on('click',function(){
			$('#contract_9').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract10').on('click',function(){
			$('#contract_10').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract11').on('click',function(){
			$('#contract_11').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,'+
			  '#contract_12,#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract12').on('click',function(){
			$('#contract_12').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_13,#contract_14,#contract_15').hide();
		});
		$('#contract13').on('click',function(){
			$('#contract_13').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_14,#contract_15').hide();
		});
		$('#contract14').on('click',function(){
			$('#contract_14').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_15').hide();
		});
		$('#contract15').on('click',function(){
			$('#contract_15').show();
			$('#contract_0,#contract_1,#contract_2,#contract_3,#contract_4,#contract_5,'+
			  '#contract_6,#contract_7,#contract_8,#contract_9,#contract_10,#contract_11,'+
			  '#contract_12,#contract_13,#contract_14').hide();
		});
	});	
</script>

<div class="row">
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body">				
				<header class="card-title">
					<c:out value="${menuName}"/>-新增
				</header>
				<form id="dataForm" name="dataForm" class="forms-sample">					
					<button id="btnSupplierInfo" type="button" class="btn btn-outline-primary btn-fw">供應商資料</button>
					<button id="btnContractTerms" type="button" class="btn btn-outline-primary btn-fw">合約條款</button>
					<button id="btnReviewEvaluation" type="button" class="btn btn-outline-primary btn-fw">審核評估</button>
					<button id="btnDocFileEidt" type="button" class="btn btn-outline-primary btn-fw">文檔編輯</button>
				</form>
			</div>
		</div>
	</div>
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body" id='supplier_info'>				
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/list/view' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label data-require=true class="col-sm-3 col-form-label">供應商統編:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<input type="text" id="idenId" name="idenId" class="form-control" value="<c:out value="${data.idenId}"/>" >
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group row">
								<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
									<spring:message code="common.query"/>
								</button>
							</div>
						</div>	
					</div>
					<div  id="supplier">
						<div class="col-12 grid-margin stretch-card">
							<div class="card">
								<div class="card-body" id='supplier_info'>	
									<div class="row">
										<div class="col-md-6">
											<div class="form-group row">
												<label class="col-sm-3 col-form-label">供應商統一編號:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												<div class="col-sm-9">
													<label class="col-sm-3 col-form-label">123456<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												</div>
											</div>
											<div class="form-group row">
												<label class="col-sm-3 col-form-label">供應商全名:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												<div class="col-sm-9">
													<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												</div>
											</div>
			                  				<div class="form-group row">
												<label class="col-sm-3 col-form-label">供應商地址:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												<div class="col-sm-9">
													<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												</div>
			                  				</div>
			                			</div>
		                			</div>								                
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
									<label data-require=true class="col-sm-3 col-form-label">供應商廠編號:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>	
						</div>
						<div class="col-12 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">	
									<div class="row">
										<div class="col-md-6">
											<div class="form-group row">
												<input type="radio" id="male" name="gender" value="male">
												<label for="male">Male</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" id="female" name="gender" value="female">
												<label for="female">Female</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" id="other" name="gender" value="other">
												<label for="other">Other</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" id="male" name="gender" value="male">
												<label for="male">Male</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" id="female" name="gender" value="female">
												<label for="female">Female</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" id="other" name="gender" value="other">
												<label for="other">Other</label>
											</div>
			                			</div>
		                			</div>								                
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
									<label data-require=true class="col-sm-3 col-form-label">供應商聯絡備註:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								</div>
							</div>	
						</div>
						<div class="col-12 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">	
									<div class="row">
										<div class="col-md-6">
											<div class="form-group row">
												<label class="col-sm-3 col-form-label">聯絡人:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												<div class="col-sm-9">
													<label class="col-sm-3 col-form-label">123456<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												</div>
											</div>
											<div class="form-group row">
												<label class="col-sm-3 col-form-label">電話:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												<div class="col-sm-9">
													<label class="col-sm-3 col-form-label">D696<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
												</div>
											</div>
			                			</div>
		                			</div>								                
								</div>
							</div>
						</div>
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
							<button id="contract7" type="button" class="btn btn-outline-primary btn-fw">7、Receiving Location 收貨地點</button>
							<button id="contract8" type="button" class="btn btn-outline-primary btn-fw">8、Standard Pallets 標準棧板規格</button>
							<button id="contract9" type="button" class="btn btn-outline-primary btn-fw">9、Payment 付款</button>
							<button id="contract10" type="button" class="btn btn-outline-primary btn-fw">10、Distribution Center Fees 物流費用</button>
							<button id="contract11" type="button" class="btn btn-outline-primary btn-fw">11、Delivery Optimization 最佳化配送</button>
							<button id="contract12" type="button" class="btn btn-outline-primary btn-fw">12、Pick Up Service 到倉取貨</button>
							<button id="contract13" type="button" class="btn btn-outline-primary btn-fw">13、Supplier Service Scorecard 供應商服務評估表</button>
							<button id="contract14" type="button" class="btn btn-outline-primary btn-fw">14、Other Conditions 其他條款</button>
							<button id="contract15" type="button" class="btn btn-outline-primary btn-fw">15、Other Agreements 其他同意條款</button>					
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
					<div class="col-md-11" id = "contract_7">
							7. 前言
					</div>
					<div class="col-md-11" id = "contract_8">
							8. 前言
					</div>
					<div class="col-md-11" id = "contract_9">
							9. 前言
					</div>
					<div class="col-md-11" id = "contract_10">
							10. 前言
					</div>
					<div class="col-md-11" id = "contract_11">
							11. 前言
					</div>
					<div class="col-md-11" id = "contract_12">
							12. 前言
					</div>
					<div class="col-md-11" id = "contract_13">
							13. 前言
					</div>
					<div class="col-md-11" id = "contract_14">
							14. 前言
					</div>
					<div class="col-md-11" id = "contract_15">
							15. 前言
					</div>
				</div>	
			<!--  -->
			</div>
			<div class="card-body" id='Review_Evaluation'>	
				<div class="row">
					<div class="col-md-5">
						<header class="card-title">
							數據匯入檔名: dkfjasldkfj<%-- <c:out value="${menuName}"/>明細<spring:message code="xauth.function.bread.edit.suffix"/> --%>
						</header>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<div class="col-sm-9">
								<input id="fileUpload1" type="file" name="file" class="btn btn-link btn-rounded btn-fw" />
							</div>
							<button id="btnUpload" type="button" class="btn btn-primary btn-sm">
								檔案上傳
							</button>
						</div>
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
			<div class="card-body" id='Doc_Edit'>	
				<div class="row">
					<header class="card-title">
						文檔編輯
					</header>
				</div>			
				<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/list/view' />" method="POST">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">檔案路徑:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<input id="fileUpload1" type="file" name="file" class="btn btn-link btn-rounded btn-fw" />
								</div>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label">說明:<%-- <spring:message code="xauth.field.menu.id"/> --%></label>
								<div class="col-sm-9">
									<textarea id="testarea"  name="file" class="form-control" ></textarea>
								</div>
							</div>
						</div>
					</div>		
					<div class="row">
						<div class="col-md-6">
							<div class="form-group row">
								<label class="col-sm-3 col-form-label"> </label>
								<button id="btnUpload" type="button" class="btn btn-primary btn-sm">
									檔案上傳
								</button>
							</div>
						</div>
					</div>		
					<div class="row">
						<div class="col-md-6">
						<label class="col-sm-2 col-form-label">清單:</label>
							<div class="form-group row">							
								<table id="dataGrid_fileEdit"></table>
				   				<!-- <div id="jqGridPager_fileEdit"></div> -->
							</div>
						</div>
					</div>								
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</div>
