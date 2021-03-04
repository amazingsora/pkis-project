<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="tv-combox" prefix="combox"%>

<META HTTP-EQUIV="Content-Type" content="text/html; charset=utf-8"/> 
<script type="text/javascript">
	var fileCount = 0, contractPath = ""; 
	$(function () { 
		signatureSubmit = function(){ 
		} 
		 
		$('#partyASignature').on("click", function() { 
			if($('#pin').val() === ""){
				$.fn.alert("請輸入憑證密碼"); 
				return false;
			}
			
			if($("#dataForm").valid()) { 
				$('#contractId').val(dataid);
				RunTest('cert', {});
			} 
		}); 
		 
		$('#partyBSignature').on("click", function() { 
			if($('#pin').val() === ""){
				$.fn.alert("請輸入憑證密碼"); 
				return false;
			}
			
			if($("#dataForm").valid()) { 
				$('#contractId').val(dataid);
				if($('#fileUpload1').val() !== ""){
					RunTest('cert', {});
				}
			} 
		}); 
		
		$('#type').on("change", function() { 
			RunTest('list',{});
		}); 
		
		$('#fileUpload_brn1').on("click", function() { 
			$('#fileUpload1').click();
		}); 
		
		
		$('#fileUpload1').on("change", function() { 
			var filename = $('#fileUpload1').val();
			var filenames=filename.split("\\");
			filename=filenames[filenames.length-1];
			$('#fileUpload_lb1').text(filename);
		}); 
		
		$('#fileUpload_brn2').on("click", function() { 
			$('#fileUpload2').click();
		}); 
		
		
		$('#fileUpload2').on("change", function() { 
			var filename = $('#fileUpload2').val();
			var filenames=filename.split("\\");
			filename=filenames[filenames.length-1];
			$('#fileUpload_lb2').text(filename);
		}); 
	}); 
	 
	function isSignature(signature){ 
		$('#partyASignature').hide(); 
		$('#partyBSignature').hide(); 
					 
		if(signature == 1){ 
			$('#partyASignature').show(); 
		} 
		if(signature == 2){ 
			$('#partyBSignature').show(); 
		} 
	} 
	 
	 
	function prepareParam(url,params) { 
	    if(! params) params={}; 
	    var e;
	    if(url === "cert")
	    	params["slot"] = $('#solt').val();
	    else
	    	params["slot"]="0"; 
	 
	    params["type"]=$('#type').val(); 
	 
	    if(url.indexOf("XML") > -1) { 
	 
	    } else { 
	      params["data"]= ""; 
	    } 
	 
	    e = document.getElementById("pin"); 
	    str = e.value; 
	    params["pin"]=str; 
	 
	    params["algo"]="default"; 

	    params["authCert"]= ""; 
	 
	  } 
	
	
	function prepareParamData(url,params, hash) { 
	    if(! params) params={}; 
	    var e; 
	    params["slot"]=  $('#solt').val(); 
	 
	    params["type"]=  $('#type').val();//"0"; 
	 
	    if(url.indexOf("XML") > -1) { 
	 
	    } else { 
	      params["data"]= hash; 
	    } 
	 
	    e = document.getElementById("pin"); 
	    str = e.value; 
	    params["pin"]=str; 
	 
	    params["algo"]="default"; 

	    params["authCert"]= ""; 
	 
	  } 
	 
	  function postProcess(evt , obj) { 
	     if(obj.success == false){ 
	    	 alert( obj.error + ":" + obj.messages);
	     } else if(obj.data.signature) { 
	    	 signature = obj.data.signature
	     } else if(obj.data.cipherText) { 
	       document.getElementById('signResult').innerHTML = obj.data.cipherText; 
	     } else if(obj.data.data) { 
	       document.getElementById('signResult').innerHTML = obj.data.data; 
	     } else if(obj.data.deviceList) { 
	      var optionSlot = document.getElementById('slot'); 
	      var j; 
	       
	      $("#solt").empty(); 
	      $("#solt").append($("<option>").prop({"value":""}).append("請選擇")); 
	      for(var i=0 ;i < obj.data.deviceList.length ;i++) { 
	        if(obj.data.deviceList[i].readerName) { 
	          var tmpValue=obj.data.deviceList[i].readerName; 
	          var new_option = new Option(tmpValue,i); 
	          $("#solt").append($("<option>").prop({"value":i}).append(tmpValue)); 
	        } else{ 
	            var tmpValue=obj.data.deviceList[i].filename; 
	            var new_option = new Option(tmpValue,tmpValue); 
	            $("#solt").append($("<option>").prop({"value":tmpValue}).append(tmpValue)); 
	        } 
// 	        $("#solt").append($("<option>").prop({"value":tmpValue}).append(tmpValue)); 
	      } 
	    } else if(obj.data.certList) { 
	      document.getElementById('signResult').innerHTML = JSON.stringify(obj.data.certList); 
	 
	      var j; 
	      for(j=optionSlot.options.length-1;j>=0;j--) { 
	        optionSlot.remove(j); 
	      } 
	 
	      for(var i=0 ;i < obj.data.certList.length ;i++)    { 
	          var tmpValue=obj.data.certList[i].serialNo; 
	          var tmpKey=obj.data.certList[i].subject + ", " + obj.data.certList[i].notBefore + ", " + obj.data.certList[i].notAfter; 
	          var new_option = new Option(tmpKey,tmpValue); 
	          optionSlot.options.add(new_option); 
	      } 
	    } else if(obj.data) { 
	    	WebSignature('signb64',{}, obj.data)
	    } 
	  } 
	   
	/////////////////////////////////////////////////////////////////////// 
	  function WebSocketTest(url,params) { 
	    if(! params) params={}; 
	    prepareParam(url,params); 
	 
	    if ("WebSocket" in window)  { 
	      protocolPrefix = (window.location.protocol === 'https:') ? 'wss:' : 'ws:'; 
	      protocolPrefix = protocolPrefix + '//' + "localhost:9991" + "/ws/"; 
	 
	      var ws = new WebSocket(protocolPrefix+url); 
	      ws.onopen = function() { 
	        ws.send(JSON.stringify(params)); 
	      }; 
	      ws.onmessage = function(evt) { 
	        var obj = JSON.parse(evt.data); 
	        postProcess(evt, obj); 
	        ws.close(); 
	      }; 
	      ws.onclose = function() { 
	        document.getElementById('status').value = 'websocket closed'; 
	      }; 
	      ws.onerror = function (error) { 
	        alert("Error code=" +ws.readyState+","+error); 
	      }; 
	    } else  { 
	      alert("This browser does not support WebSockets."); 
	    } 
	  } 
	   
	//////////////////////////////////////////////////////////////////////// 
	  function WebRestTest(url,params) { 
	    if(! params) params={}; 
	    if(url === "signb64")
	    	prepareParamData(url,params); 
	    else
	    	prepareParam(url,params); 
	    cor = new XMLHttpRequest(); 
	 
	    cor.onreadystatechange = function () { 
	     if (cor.readyState == 4 && this.status ==  200) {  
	        var obj = JSON.parse(this.responseText); 
	        var evt={}; 
	        evt["data"]=this.responseText; 
	        postProcess(evt, obj); 
	      } 
	    }; 
	 
	    protocolPrefix = (window.location.protocol === 'https:') ? 'https:' : 'https:'; 
	    protocolPrefix = protocolPrefix + '//' + "localhost:9991" + "/ws/"; 
	    cor.open("POST",protocolPrefix+url, true); 
	    cor.send(JSON.stringify(params)); 
	  } 
	

	 function RunTest(url,param) { 
	    WebRestTest(url,param); 
	  } 
	 
	 
	 function WebSignature(url,params, cert) { 
		    if(! params) params={}; 
		    
		    var ajax = new $.tvAjax(); 
    	 	if(IsSignature === "1" ) 
    	 		ajax.setController("<c:url value="/contract/list/partyASignatureGetHash"/>"); 
    	 	else 
    	 		ajax.setController("<c:url value="/supplier/contract/partyBSignatureGetHash"/>"); 
			ajax.setFormId("dataForm");		
// 			ajax.put("hex", hex);
			ajax.put("fileCount", fileCount);
			if(fileCount === 1){
				ajax.put("contractPath", contractPath);
			}
			ajax.put("dataid", dataid);
			ajax.put("cert", JSON.stringify(cert));
			ajax.upload(function(result) { 
				console.log(JSON.stringify(result));
				if(result.status == "ok"){ 
					prepareParamData(url,params, result.result.hash); 
				    cor = new XMLHttpRequest(); 
				 
				    cor.onreadystatechange = function () { 
				     if (cor.readyState == 4 && this.status ==  200) {  
				        var obj = JSON.parse(this.responseText); 
				        var evt={}; 
				        evt["data"]=this.responseText; 
				        postProcessSignature(evt, obj, cert, result.result); 
				      } 
				    }; 
				 
				    protocolPrefix = (window.location.protocol === 'https:') ? 'https:' : 'https:'; 
				    protocolPrefix = protocolPrefix + '//' + "localhost:9991" + "/ws/"; 
				    cor.open("POST",protocolPrefix+url, true); 
				    cor.send(JSON.stringify(params)); 
				}else{ 
					$.fn.alert(result.messages); 
				} 
			}); 
		  } 
	 
	 
	 function postProcessSignature(evt , obj, cert, result) { 
		     if(obj.success == false){ 
		    	 alert( obj.error + ":" + obj.messages);
		     } else if(obj.data.signature) { 
					var ajax = new $.tvAjax(); 
		    	 	if(IsSignature === "1" )
		    	 		ajax.setController("<c:url value="/contract/list/partyASignature"/>"); 
		    	 	else 
		    	 		ajax.setController("<c:url value="/supplier/contract/partyBSignature"/>"); 
		    	 	console.log(JSON.stringify(evt.data));
		    	 	ajax.put("hex", obj.data.signature);
		    	 	ajax.put("dataid", dataid);
					ajax.put("testPath", result.testPath);
					ajax.put("newContractPath", result.newContractPath);
					ajax.put("cert", JSON.stringify(cert));
					ajax.put("fileCount", fileCount);
					ajax.call(function(result) { 
						console.log(JSON.stringify(result));
						if(result.status == "ok"){ 
							contractPath = result.result.contractPath;
						
							fileCount += 1;
							if($('#fileUpload2').val() !== "" && fileCount === 1){
								RunTest('cert', {});
							}
							if($('#fileUpload2').val() !== "" && (fileCount === 2 && IsSignature === "1" )){
								RunTest('cert', {});
							}
							if(fileCount === 2 && IsSignature != "1" ){
								submit('核准');
							}
							if(fileCount === 3){
								submit('核准');
							}
						}else{ 
							$.fn.alert(result.messages); 
						} 
					}); 
		     } else if(obj.data.cipherText) { 
		       document.getElementById('signResult').innerHTML = obj.data.cipherText; 
		     } else if(obj.data.data) { 
		       document.getElementById('signResult').innerHTML = obj.data.data; 
		     } else if(obj.data.deviceList) { 
		      var optionSlot = document.getElementById('slot'); 
		      var j; 
		       
		      $("#solt").empty(); 
		      $("#solt").append($("<option>").prop({"value":""}).append("請選擇")); 
		      for(var i=0 ;i < obj.data.deviceList.length ;i++) { 
		        if(obj.data.deviceList[i].readerName) { 
		          var tmpValue=obj.data.deviceList[i].readerName; 
		          var new_option = new Option(tmpValue,i); 
		          $("#solt").append($("<option>").prop({"value":i}).append(tmpValue)); 
		        } else{ 
		            var tmpValue=obj.data.deviceList[i].filename; 
		            var new_option = new Option(tmpValue,tmpValue); 
		            $("#solt").append($("<option>").prop({"value":tmpValue}).append(tmpValue)); 
		        } 
// 		        $("#solt").append($("<option>").prop({"value":tmpValue}).append(tmpValue)); 
		      } 
		    } else if(obj.data.certList) { 
		      document.getElementById('signResult').innerHTML = JSON.stringify(obj.data.certList); 
		 
		      var j; 
		      for(j=optionSlot.options.length-1;j>=0;j--) { 
		        optionSlot.remove(j); 
		      } 
		 
		      for(var i=0 ;i < obj.data.certList.length ;i++)    { 
		          var tmpValue=obj.data.certList[i].serialNo; 
		          var tmpKey=obj.data.certList[i].subject + ", " + obj.data.certList[i].notBefore + ", " + obj.data.certList[i].notAfter; 
		          var new_option = new Option(tmpKey,tmpValue); 
		          optionSlot.options.add(new_option); 
		      } 
		    } else if(obj.data) { 
		    	cert = JSON.stringify(obj.data);
		    } 
		  } 
</script>
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