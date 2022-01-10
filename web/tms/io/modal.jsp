<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
		<h4 class="modal-title">
			<small>거래번호: ${DATAMAP.trxId} </small>&nbsp; <small class="error"></small>
		</h4>
	</div>
	<div class="modal-body">
		<div class="row">
			<div class="col-sm-12">
				<div class="portlet box yellow">
					<div class="portlet-title">
						<div class="caption"><i class="fa fa-gift"></i>전송전문</div>
					</div>
					<div class="portlet-body">
						<div class="row">
							<div class="col-sm-12 regData">
								${DATAMAP.regData}
							</div>
						</div>
					</div>
					<div class="portlet-title">
						<div class="caption"><i class="fa fa-gift"></i>응답전문</div>
					</div>
					<div class="portlet-body">
						<div class="row">
							<div class="col-sm-12 resDate">
								${DATAMAP.resData}
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn default" data-dismiss="modal">Close</button>
	</div>
</div>
<script>
	$(document).ready(function() {
		$('.resDate').html(syntaxHighlight($('.resDate').text()));
		$('.regData').html(syntaxHighlight($('.regData').text()));
	});
	
	function syntaxHighlight(json) {
	    if (typeof json != 'string') {
	         json = JSON.stringify(json, undefined, 2);
	    }
	    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
	    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
	        var cls = 'number';
	        if (/^"/.test(match)) {
	            if (/:$/.test(match)) {
	                cls = 'key';
	            } else {
	                cls = 'string';
	            }
	        } else if (/true|false/.test(match)) {
	            cls = 'boolean';
	        } else if (/null/.test(match)) {
	            cls = 'null';
	        }
	        
	        if(cls == 'key') {
	        	return'<span class="' + cls + '">' + match + '</span>';
	        } else {
	        	return'<span class="' + cls + '">' + match + '</span><br>';
	        }
	    });
	}
</script>


<!-- /.modal-dialog -->