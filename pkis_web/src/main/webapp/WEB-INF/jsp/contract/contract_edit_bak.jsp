<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="tv-combox" prefix="combox"%>

<script type="text/javascript">
	var grid;
	$(function () {
	
		grid = $("#dataGrid").tvGrid();	
		grid.addColumn({ label: '合約類別', name: 'idenId', width: 90, sortable: false });
		grid.addColumn({ label: '合約編號', name: 'ipAddr', width: 90, sortable: false });
		grid.addColumn({ label: '合約名稱', name: 'sysType', width: 90, sortable: false, hidden: true });
		grid.addColumn({ label: '乙方統編', name: 'sysTypeName', width: 90, sortable: false });
		grid.addColumn({ label: '乙方公司名稱', name: 'enabledName', width: 100, sortable: false });		
		grid.addColumn({ label: '送審日期', name: 'memo', width: 100, sortable: false });
		grid.addColumn({ label: '狀態', name: 'creUser', width: 100, sortable: false });
		grid.addColumn({ label: '承辦人', name: 'creDateDesc', width: 100, sortable: false });
		grid.addColumn({ label: '部門', name: 'updUser', width: 100, sortable: false });
		grid.addColumn({ label: '編輯', width:100, exportcol:false, formatter:girdEdit});
		grid.addColumn({ label: '刪除', width:100, exportcol:false, formatter:girdDelete});
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
			$("#dataForm").submit();
		});
		
	});

	function girdEdit(cellvalue, options, rowObject) {			  
        var cmd = '<button id="btnDetail" type="button" class="btn btn-sm btn-primary" ' +
        				'onclick="toDetailView(\'' + options.rowId +'\');">編輯</button>' ;
        return cmd;
	}
	
	function girdDelete(cellvalue, options, rowObject) {			  
        var cmd = '<button id="btnDelete" type="button" class="btn btn-sm btn-danger" ' +
          		   		'onclick="toDelete(\'' + options.rowId +'\');"><spring:message code="common.grid.action.delete"/></button>';
        return cmd;
	}
	
	//檢視
	function toDetailView(rid) {
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
					<c:out value="${menuName}"/>清單(共 0 筆)<%-- <spring:message code="xauth.function.bread.edit.suffix"/> --%>
				</header>
				<button id="btnQuery" type="button" class="btn btn-outline-primary btn-fw">
						<spring:message code="common.query"/>
				</button>	
			</div>
			<div class="card-body">
				<table id="dataGrid"></table>
   				<!-- <div id="jqGridPager"></div> -->
			</div>
			<form id="dataForm" name="dataForm" class="forms-sample" action="<c:url value='/contract/edit/editSingle' />" method="POST">
				<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>			
		</div>
	</div>
</div>
