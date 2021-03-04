<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="tv-combox" prefix="combox"%>
<META HTTP-EQUIV="Content-Type" content="text/html; charset=utf-8"/> 
<script type="text/javascript">
	$(function() {
		$("#transferbutton").on("click", function() {
			var radio=$('input:radio:checked[name="slectscope"]').val();
			//選取部分
			if(!radio){
				$.fn.alert("請勾選後轉移");
				return
			}
			else{
				var datas="";
				//選取合約
				if(radio == "select"){
					var resWaitRows =  $("#dataGrid").find("tbody > tr");
					var datasize=0;
					 $.each(resWaitRows, function (i, rowObj) {
						var $rowChkObj = $(rowObj).find("td:eq(0) > input[type='checkbox']");
						if($rowChkObj.prop("checked")){
							datasize++;
							if(datas==""){
								datas=$(rowObj).find("td:eq(9)").text();
							}
							else{
								datas=datas+"#"+$(rowObj).find("td:eq(9)").text();
							}
						}
				     });
					if(datasize<1){
						$.fn.alert("無勾選批次項目");
						return;
					}
				}
				//全部合約
				else if(radio == "all"){
					$.each(datalist,function(i,data){
						if(datas==""){
							datas=data.Contracts
						}
						else{
							datas=datas+"#"+data.Contracts
						}
					});	
				}
				var ajax = new $.tvAjax();
				$.blockUI({
	                message: '<h3>讀取中...</h3>',
	                baseZ: 9999
	            });
				ajax.setController("<c:url value="/xauth/transfer/setUndertaker"/>");
				//原部門人腳色資訊
				console.log("datas ==="+datas)
				ajax.put("dataids",datas)
				ajax.setBlockUIDisabled(true);

				//移轉人資訊
				ajax.put("transferidenId",$("#transferidenId").val());
				ajax.put("transferUserid",$("#transferUserid").val());
				ajax.put("transferRoleid",$("#transferRoleid").val());
				
				ajax.call(function(response) {	
					if(response){
						$.unblockUI();  
						$.fn.alert(response.messages, function(){
							if(response.status == "ok"){
								//產生讀取畫面 並移轉至原頁面
								$.blockUI({
					                message: '<h3>讀取中...</h3>',
					                baseZ: 9999
					            });
								$("#dataFormToView").submit();
							}
						});
					}
				});
			}
		});
		$("#transferidenId").on("change", function() {
			//modal用於查詢跳窗頁面
			getRoleList("modal");
		});
		$("#transferRoleid").on("change", function() {
			getuserid();
		});
		$("#close").on("click",function(){
			$("#TransferDialog").modal('hide'); 
		});
		$("#TransferDialog").on('show.bs.modal',function(){
			modalResize();
		})
		$(window).resize(function(){
			modalResize();
		})
		//跳窗設定
		function modalResize(){
			var winWidth = $(document.body).width(); 
			var modalWidth = $("#docModalContent").width();
			var width = (winWidth-modalWidth)/2 + "px"

			$("#TransferDialog").find(".modal-dialog").css({
				'margin-left':width
		    });
		}
		
	});
	//取得當前角色ID資訊
	function getuserid(){
		$("#transferUserid").empty();
		$("#transferUserid").append($("<option>").prop({"value":""}).append("請選擇"));
		var idenId = $('#transferidenId').val();
		var roleId = $('#transferRoleid').val();
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/xauth/transfer/getUserid"/>");
		ajax.put("idenId", idenId);	
		ajax.put("roleId", roleId);	
		ajax.call(function(dataList){
			if (dataList!= null) {
				$.each(dataList,function() {
					var data = this;
					$("#transferUserid").append($("<option>").prop({"value":data.USER_ID}).append(data.USER_ID+"-"+data.USER_CNAME));
				});
			}
		});
	}

</script>
<div class="modal" id="TransferDialog"  role="dialog"  aria-hidden="true">
	<div class="modal-dialog"  >
		<div class="modal-content " id="docModalContent" style="width: 850px;margin-right: 100px" >
			<div class="modal-body ">
				<h4 class='modal-title'>選擇合約要移轉的對象(承接人)</h4><br>
				<div class="box-body">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<form id="transferdataForm" name="dataForm" class="forms-sample" method="POST">
									<div class="row">
										<div class="col-md-6">
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">部門職稱 :</label>
												<div id="divIdenId" class="col-sm-9">
													<combox:xauth id="transferidenId"  class="form-control select2-single" xauthType="x-iden-qry" listKey="idenId" listValue="struct" headerKey="請選擇" headerValue="" /> 
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group row">
												<div class="col-sm-9">
													<select id="transferRoleid" name="type" class="form-control select2-single" required>
														<option value="">請選擇</option>
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">承接人員編號 :</label>
												<div class="col-sm-9">
													<select id="transferUserid" name="transferUserid" class="form-control select2-single" required>
														<option value="">請選擇</option>
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">轉移資料範圍 :</label>
												<div class="col-sm-9">
													<input type="radio" class="slectscope" name="slectscope" value="select">勾選的合約資料<br>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group row">
												<div class="col-sm-9">
													<input type="radio"class="slectscope" name="slectscope" value="all">查詢結果全部的合約資料<br>
												</div>
											</div>
										</div>
									</div>
									<!-- 返回頁面-->
								</form>
								<form id="dataFormToView"  method="POST" action="<c:url value='/xauth/transfer/' />" >
									<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />									
								</form>
								<button id="transferbutton" type="button" class="btn  btn-outline-danger btn-fw">
									資料移轉
								</button>
								<button id="close" type="button" class="btn btn-outline-dark btn-fw">
									返回
								</button>
							</div>
						</div>		
					</div>
				</div>		
			</div>
		</div>
	</div>
</div>