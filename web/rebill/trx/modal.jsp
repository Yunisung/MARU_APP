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
		<h4>${DATAMAP.name} 정기결제 거래 내역 상세정보</h4>
	</div>
	<div class="modal-body">
		<div class="tab-content">
			<!-- BEGIN FORM-->
			<div class="tab-pane active" id="tab1">
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
						<div class="form-group col-sm-12 form-subtitle">
							<label><i class="fa fa-reorder"></i>거래 기본 정보</label>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>상태</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.status}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">거래번호</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.trxId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>금액</label>
								<div class='col-md-9'>
									<p class='form-control-static digits'>${DATAMAP.amount}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">터미널</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.tmnId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">거래 추적번호</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.trackId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>구매자</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.payerName}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>구매자 Email</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.payerEmail}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>구매자 연락처</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.payerTel}</p>
								</div>
							</div>
						</div>

						<!--/span-->
						<%-- <div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>cardId</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.cardId}</p>
								</div>
							</div>
						</div> --%>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>매입사</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.issuer}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>카드 번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.bin}******${DATAMAP.last4}</p>
								</div>
							</div>
						</div>
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>승인일시</label>
								<div class='col-md-9'>
									<p class='form-control-static date'>${DATAMAP.reqDay}${DATAMAP.reqTime}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<%-- <div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>vanId</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.vanId}</p>
								</div>
							</div>
						</div> --%>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>처리사 거래번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.vanTrxId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>기록 일시</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.regDate}</p>
								</div>
							</div>
						</div>
						<c:if test="${not empty DATAMAP.rfdTrxId }">
							<div class="form-group col-sm-12 form-subtitle">
								<label><i class="fa fa-reorder"></i>취소 내역 정보</label>
							</div>
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-3'>취소 거래번호</label>
									<div class='col-md-9'>
										<p class='form-control-static'>${DATAMAP.rfdTrxId}</p>
									</div>
								</div>
							</div>
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-3'>취소일시</label>
									<div class='col-md-9'>
										<p class='form-control-static'>${DATAMAP.rfdRegDate}</p>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</form>
			</div>

			<!-- END FORM-->
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>
</html>