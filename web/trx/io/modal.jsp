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
						<div class="caption"><i class="fa fa-gift"></i>요청전문</div>
					</div>
					<div class="portlet-body">
						<div class="row">
							<div class="col-sm-12">
								<pre>${DATAMAP.regData}</pre>
							</div>
						</div>
					</div>
					<div class="portlet-title">
						<div class="caption"><i class="fa fa-gift"></i>응답전문</div>
					</div>
					<div class="portlet-body">
						<div class="row">
							<div class="col-sm-12">
								<pre>${DATAMAP.resData}</pre>
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
<!-- /.modal-dialog -->