<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="<c:url value="/resources/jstree/jstree.js"/>"></script>
<link href='<c:url value="/resources/jstree/jstree.css"/>' rel="stylesheet">

<script type="text/javascript">

	$(function () {

		$(document).bind("dnd_stop.vakata", function(e, data) {
		    process();
		});

		getTree();
		
	});

	function getTree() {
		$("#tree").jstree("destroy");
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/xauth/tree/getMenuTree"/>");
		ajax.call(function(json){
			if (json.result) {
				$('#tree').show();
				$('#tree')
				.on('changed.jstree', function (e, data) {
				    var i, j, r = [];
				    for(i = 0, j = data.selected.length; i < j; i++) {
				      r.push(data.instance.get_node(data.selected[i]).original.url);
				    }
				})
				.jstree({ 
					'core' : {
					    'data' : json.result.data,
					    'check_callback' : true,
					    'check_callback' : function (operation, node, parent, position, more) {
							if(operation === "copy_node" || operation === "move_node") {
								cb_node_obj = node;
								cb_parent_obj = parent;
								cb_position = position;
								cb_more_obj = more;
					        }
					        return true;
					    }
					},
					"plugins" : ["dnd","contextmenu"],
				}).bind("move_node.jstree", function(e, data) {
					mv_data_obj = data;
				}).on('loaded.jstree', function () {
				     $(".jstree").jstree('open_all');					     
				});
				
			}
			else {
				$("#tree").jstree("destroy");
			}
		});
	}

	function process() {
		var ajax = new $.tvAjax();
		ajax.setController("<c:url value="/xauth/tree/saveMenuTree"/>");
		ajax.put("parentId", mv_data_obj.parent);
		ajax.put("itemList", cb_parent_obj.children);
		ajax.put("parentSeq", mv_data_obj.node.original.parentseq);
		ajax.call(function(result) {
			if (result) {
				if (result.status == 'ng') {
					$.fn.alert(result.messages);
				}
				getTree();
			}
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
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">							
							<div class="col-sm-9">
								<div id="tree" class="demo"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
