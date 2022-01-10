<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head></head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h4>${DATAMAP.title}</h4>
		<div class="row">
			<div class="col-md-6 pull-right">
				<div class="form-group">
					<label class="control-label col-md-6" style="text-align: right;">개시자 : ${DATAMAP.regId}</label> 
					<label class="control-label col-md-6" style="text-align: right;">개시일 : <span class="date">${DATAMAP.pubDay}</span></label>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-body">
		<!-- BEGIN FORM-->
		<form class="form-horizontal form" role="form">
			<div class="form-body row">
				<!--/span-->
				<div class="col-md-12">
					<div class="form-group">
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.summary}</p>
						</div>
					</div>
				</div>
			</div>
		</form>
		<!-- END FORM-->
	</div>
	
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
		<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role ne '일반'}">
		<button type="button" class="btn btn-sm green" onclick="location.href='/system/faq/modify/${DATAMAP.idx}';">
			<i class="fa fa-pencil"></i> 수정
		</button>
		</c:if>
	</div>
	
</body>
</html>