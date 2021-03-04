<#import "/spring.ftl" as spring/> 
<div class="row">
	<div class="col-md-6">
		<div class="form-group row">
			<label class="col-sm-3 col-form-label"><@spring.message "common.query"/></label>
			<div class="col-sm-9">
				<input type="text" id="txt" name="txt" class="form-control" placeholder="" value="${name}">
			</div>
		</div>
	</div>
</div>