<#include "molgenis-header.ftl">
<#include "molgenis-footer.ftl">
<#include "ontology-match-view-result.ftl">
<#include "ontology-match-new-task.ftl">
<#include "ontology-match-list-tasks.ftl">
<#include "ontology-match-roc-modal.ftl">
<<<<<<< HEAD
<#assign css=["bootstrap-fileupload.min.css", "ui.fancytree.min.css", "ontology-service.css", "biobank-connect.css"]>
<#assign js=["bootstrap-fileupload.min.js", "jquery.fancytree.min.js", "common-component.js", "ontology-tree-view.js", "ontology.tree.plugin.js", "jquery.bootstrap.pager.js", "ontology-service-result.js", "simple_statistics.js"]>
=======
<#assign css=["bootstrap.fileupload.min.css", "ui.fancytree.min.css", "ontology-service.css", "biobank-connect.css"]>
<#assign js=["bootstrap-fileupload.min.js", "jquery.fancytree.min.js", "common-component.js", "ontology-tree-view.js", "ontology.tree.plugin.js", "ontology-service-result.js", "jquery.bootstrap.pager.js", "simple_statistics.js"]>
>>>>>>> 4d1886aae65812ad5f86a15e43f651975953727f
<@header css js/>
<form id="ontology-match" class="form-horizontal" enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-12">
			<br>
			<div class="row">
				<div class="col-md-offset-3 col-md-6">
					<legend><center><strong>SORTA</strong> - <strong>S</strong>ystem for <strong>O</strong>ntology-based <strong>R</strong>e-coding and <strong>T</strong>echnical <strong>A</strong>nnotation</center></legend>
				</div>
			</div>
			<#if existingTasks??>
				<@listTasks />
				<#if rocfilePath??>
					<@ShowROCModal />
				</#if>
			<#else>
			<div class="row">
				<div class="col-md-offset-2 col-md-2">
					<button id="back-button" type="button" class="btn btn-primary">Restart</button>
				</div>
			</div>
			<script>
				$(document).ready(function(){
					$('#back-button').click(function(){
						$('#ontology-match').attr({
							'action' : molgenis.getContextUrl(),
							'method' : 'GET'
						}).submit();
					});
				});
			</script>
				<#if ontologies??>
					<@ontologyMatchNewTask />
				<#else>
					<@ontologyMatchResult />
				</#if>
			</#if>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
		<#if message??>
			var molgenis = window.top.molgenis;
			molgenis.createAlert([ {
				'message' : '${message?js_string}'
			}], 'error');
		</#if>
		});
	</script>
</form>
<@footer/>	