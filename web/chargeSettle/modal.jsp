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
		<h4>${DATAMAP.trxId} 거래 내역 상세정보</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">거래정보</a>
			</li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="tab1">	
				<!-- BEGIN FORM-->
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
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
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">가맹점명</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.name}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">가맹점아이디</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.mchtId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">거래구분</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.trxType}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">거래유형</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.trxUnit}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">입출금 원금</label>
								<div class="col-md-9">
									<p class="form-control-static digits">${DATAMAP.amount}</p>
								</div>
							</div>
						</div>
						<c:if test="${DATAMAP.trxType eq '출금'}">
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">수수료</label>
								<div class="col-md-9">
									<p class="form-control-static digits">${DATAMAP.fee}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">수수료부가세</label>
								<div class="col-md-9">
									<p class="form-control-static digits">${DATAMAP.feeVat}</p>
								</div>
							</div>
						</div>
						</c:if>
						<c:if test="${CP_SESSION.grade eq '본사' && DATAMAP.trxType eq '출금'}">
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">은행수수료</label>
								<div class="col-md-9">
									<p class="form-control-static digits">${DATAMAP.bankFee}</p>
								</div>
							</div>
						</div>
						</c:if>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">계정 실출금액</label>
								<div class="col-md-9">
									<p class="form-control-static digits">${DATAMAP.netAmount}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">거래 후 잔액</label>
								<div class="col-md-9">
									<p class="form-control-static digits">${DATAMAP.balance}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">주문번호</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.trackId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">참조번호</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.refId}</p>
								</div>
							</div>
						</div>
						<c:if test="${DATAMAP.trxType eq '출금'}">
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">은행코드</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.bankCd}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">은행이름</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.bankName}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">계좌번호</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.account}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">받는계좌 예금주명</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.holder}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">적요</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.recordInfo}</p>
								</div>
							</div>
						</div>
						</c:if>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">등록자</label>
								<div class="col-md-9">
									<p class="form-control-static">${DATAMAP.regId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-6">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-3">거래일시</label>
								<div class="col-md-9">
									<p class="form-control-static date">${DATAMAP.trxDay}${DATAMAP.trxTime}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>기재내용</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.summary}</p>
								</div>
							</div>
						</div>
					</div>
				</form>
				<!-- END FORM-->
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
	<script>
	
	
	</script>
</body>
</html>