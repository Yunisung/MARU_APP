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
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">거래정보</a>
			</li>
			<c:if test="${CP_SESSION.grade == '본사'}">
				<li>
					<a href="#tab_hook" data-toggle="tab">통지 정보</a>
				</li>
			</c:if>
		</ul>
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
			<div class="tab-pane" id="tab_hook">
				<!-- BEGIN FORM-->
				<div class="form-body row">
					<!--/span-->
					<div class='col-md-4'>
						<div class='form-group pg-view-group'>
							<label class='control-label col-md-4'>Status</label>
							<div class='col-md-8'>
								<p class='form-control-static'>${DATANOTIMAP.status}</p>
							</div>
						</div>
					</div>
					<!--/span-->
					<div class='col-md-4'>
						<div class='form-group pg-view-group'>
							<label class='control-label col-md-4'>Retry</label>
							<div class='col-md-8'>
								<p class='form-control-static'>${DATANOTIMAP.retry}</p>
							</div>
						</div>
					</div>
					<!--/span-->
					<div class='col-md-4'>
						<div class='form-group pg-view-group'>
							<label class='control-label col-md-4'>Type</label>
							<div class='col-md-8'>
								<p class='form-control-static'>${DATANOTIMAP.hookType}</p>
							</div>
						</div>
					</div>
					<!--/span-->
					<div class='col-md-4'>
						<div class='form-group pg-view-group'>
							<label class='control-label col-md-4'>Address</label>
							<div class='col-md-8'>
								<p class='form-control-static'>${DATANOTIMAP.webHookUrl}</p>
							</div>
						</div>
					</div>
					<!--/span-->
					<div class='col-md-4'>
						<div class='form-group pg-view-group'>
							<label class='control-label col-md-4'>Sent Date</label>
							<div class='col-md-8'>
								<p class='form-control-static'>${DATANOTIMAP.sentDate}</p>
							</div>
						</div>
					</div>
					<!--/span-->
					<div class='col-md-4'>
						<div class='form-group pg-view-group'>
							<label class='control-label col-md-4'></label>
							<div class='col-md-8'>
								<c:if test="${!empty DATANOTIMAP }">
									<button class="btn btn-sm blue" onClick="retryHook();">재전송</button>
								</c:if>
								<c:if test="${empty DATANOTIMAP }">
									<p class='form-control-static'>전송중</p>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>

	<script>
		function retryHook() {
			bootbox.confirm('해당 거래를 재전송 하시겠습니까?', function(result) {
				if (result) {
					$.ajax({
						type: "GET",
						url: "/rebill/retry/${DATAMAP.trxId}",
						success: function (res) {
							if(res.result == 'OK') {
								bootbox.alert('재전송 요청에 성공했습니다.');
							} else {
								bootbox.alert(res.msg);
							}
						},
						error: function (res, status) {
							bootbox.alert('재전송 요청에 실패했습니다.' + status);
						}
					});
				}
			});
		};
	</script>
</body>
</html>