<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- <%@include file="../contract/contract_signature.jsp" %> --%>

<link href="../../resources/css/site/contract_edit.css" rel="stylesheet" />
<link href="../../resources/css/site/layout.css" rel="stylesheet" />
<link href="../../resources/css/site/cqs.css" rel="stylesheet" />
<link href="../../resources/css/site/Spinner.css" rel="stylesheet" />
<link href="../../resources/dataTable/jquery.dataTables.min.css" rel="stylesheet" />
<link href="../../resources/datepicker/bootstrap-datepicker3.min.css" rel="stylesheet" />
<script type="text/javascript" src="../../resources/dataTable/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="../../resources/js/lib/jsonpath.jquery.js"></script>
<script type="text/javascript" src="../../resources/js/Shared/Spinner.js"></script>
<script type="text/javascript" src="../../resources/datepicker/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="../../resources/datepicker/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="../../resources/datepicker/bootstrap-datepicker.zh-TW.min.js"></script>
<script src="http://knockoutjs.com/downloads/knockout-3.2.0.js"></script>

<input type="hidden" id="modal_url" value="/perContractTemplate/insert"/>
<input type="hidden" id="modeltype" value="contract" />
<input type="hidden" id="skhcq_api_url" value="<%=request.getContextPath()%>" />
<input type="hidden" id="datatype" value="<c:out value="${data.dataType}"/>" />
<input type="hidden" id="currentUserCname" value="<c:out value="${data.currentUserCname}"/>" />
<input type="hidden" id="modal" value="<c:out value="${data.module}"/>" />
<input type="hidden" id="templateDataId" value="<c:out value="${data.templateDataId}"/>" />
<input type="hidden" id="isPerConTemplate" value="Y"/>

 <div class="row">			
	<div class="col-12 grid-margin stretch-card">
		<div class="card">
			<div class="card-body"> 
				<div class="page"> 
					<div class="  container-fluid page-container-top">
						<!-- 固定訊息與置頂按鈕 -->
						<div class="page-title-header">
							<div id="cqs-button-holder" class="cqs-button-holder"></div>
							<div id="msgBlock1" class="cqs-fixmsg-holder"></div>
						</div>
					</div>
					<div class="page-content container-fluid page-container-bottom">
						<div id="alertBlock1" class="sys_msg_top_fixed"></div>
						<div id="mycontainer_body">
							<script>
								$(".page-container-top").hide();
							</script>
							<h1 id="cqs-title-h1" style="display: none"></h1>
<!-- 							<ul id="cqs-tabnav-holder" class="nav nav-tabs" style="display: none"></ul> -->
							<ul id="cqs-tabnav-holder_Master" class="nav nav-tabs" style="display: none"></ul>
                            <ul id="cqs-tabnav-holder_Detail" class="nav nav-tabs" style="display: none"></ul>
							<div class="btn_group_list_fixed"></div>
							<div id="divbody"></div>
							<div id="circle_spinner" class="backdrop">
								<div class="sk-double-bounce">
									<div class="sk-child sk-double-bounce1"></div>
									<div class="sk-child sk-double-bounce2"></div>
									<span>Loading...</span>
								</div>
							</div>
						</div>
					</div>
				</div>
		 	</div>
		</div>
	</div>
</div> 

<script type="text/javascript" src="../../resources/js/Render/RenderHTML.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_button.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_addbutton.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_checkbox.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_child.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_dropdownlist.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_dropdownlistWithcheckbox.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_dropdownlistWithhints.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_implement.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_radio.js"></script>
<script type="text/javascript" src="../../resources/js/Render/render_text.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Common/common_LocalStorageImpl.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Common/Common.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Common/common_ApiOption.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Common/common_Contract.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Contract/contract_Edit.js"></script>

<script type="text/javascript" src="../../resources/js/v2/Shared/SlideScroll.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Shared/Spinner.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Shared/ValidateTreeView.js"></script>

<script type="text/javascript" src="../../resources/js/v2/Render/render_AddButton.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_BindingScript.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Button.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Checkbox.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Controller.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_DropdownList.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_DropdownListWithCheckbox.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_File.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_FixBlock.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Html.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Label.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Layout.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Li.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Radio.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Resultlist.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Textbox.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Textarea.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/Render.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Render/render_Css.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/IGenCss.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/ILocalStorage.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/IGenElements.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/IGenBinding.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/IGenLayout.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/IRender.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/IMain.js"></script>
<script type="text/javascript" src="../../resources/js/v2/Interface/IContract.js"></script>
