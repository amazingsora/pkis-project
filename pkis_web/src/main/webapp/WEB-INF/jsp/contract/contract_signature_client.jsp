<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="tv-combox" prefix="combox"%>

<META HTTP-EQUIV="Content-Type" content="text/html; charset=utf-8"/> 
<script type="text/javascript" src="../../resources/js/v2/Contract/contract_signature_client.js"></script>
<input type="hidden" id="errorMsg" />
<div class="modal" id="signatureModal" tabindex="-1" role="dialog" aria-labelledby="signatureModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class='modal-title'>合約簽審</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
			</div>
			<div class="modal-body ">
				<div class="box-body">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<form id="dataForm" name="dataForm" class="forms-sample" method="POST">
									<input type="hidden" id="contractId" name="contractId" value="">
									<div class="row">
										<div class="col-md-12">
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">憑證種類 :</label>
												<div class="col-sm-9">
													<select id="type" name="type" class="form-control select2-single" required>
														<option value="0" selected="true">TWCA</option>
														<option value="2">工商憑證</option>
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12">
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">憑證 :</label>
												<div class="col-sm-9">
													<select id="solt" name="solt" class="form-control select2-single" required>
														<option value="">請選擇</option>
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12">
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">PIN :</label>
												<div class="col-sm-9">
													<input type="password" id="pin" name="pin" class="form-control" required>
												</div>				
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12">
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">圖片上傳(公司大章)</label>
												<div class="col-sm-9">
													<label id="fileUpload_lb1"></label><br>
      												<input id="fileUpload_brn1" type="button" value="選取簽章" class="btn btn-outline-info btn-fw">
													<input id="fileUpload1" type="file" name="file" class="btn btn-link btn-rounded btn-fw" accept=".jpge,.jpg,.png" style="display: none" required>
												</div>
											</div>
											<div class="form-group row">
												<label data-require=true class="control-label col-md-3">圖片上傳(公司小章)</label>
												<div class="col-sm-9">
													<label id="fileUpload_lb2"></label><br>
      												<input id="fileUpload_brn2" type="button" value="選取簽章" class="btn btn-outline-info btn-fw">
													<input id="fileUpload2" type="file" name="file" class="btn btn-link btn-rounded btn-fw" accept=".jpge,.jpg,.png" style="display: none" required>
												</div>
											</div>
										</div>
									</div>
								</form>
								<button id="partyASignature" type="button" class="btn btn-outline-dark btn-fw">
									甲方簽章
								</button>
								<button id="partyBSignature" type="button" class="btn btn-outline-dark btn-fw">
									乙方簽章
								</button>
							</div>
						</div>		
					</div>
				</div>		
			</div>
		</div>
	</div>
</div>