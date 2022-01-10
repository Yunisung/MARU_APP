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
		<h4>${DATAMAP.name} 승인실패내역 상세정보</h4>
	</div>
	<div class="modal-body">
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
						<label class="control-label col-md-3">가맹점명</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.name}</p>
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
						<label class='control-label col-md-3'>카드 4자리</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.last4}</p>
						</div>
					</div>
				</div>
				
				<!--/span-->
				<%-- <div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>prodId</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.prodId}</p>
						</div>
					</div>
				</div> --%>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>요청일시</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.reqDay} ${DATAMAP.reqTime}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>결과 정보</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.resultCd} : ${DATAMAP.resultMsg}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>거래처리사</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.van}</p>
						</div>
					</div>
				</div>
			<c:if test="${CP_SESSION.grade ne '가맹점' }">
				<c:if test="${CP_SESSION.grade ne '지사' }">
					<!--/span-->
					<div class='col-md-6'>
						<div class='form-group pg-view-group'>
							<label class='control-label col-md-3'>소속 에이전시</label>
							<div class='col-md-9'>
								<p class='form-control-static'>${DATAMAP.agencyName}</p>
							</div>
						</div>
					</div>
				</c:if>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>소속 지사</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.salesName}</p>
						</div>
					</div>
				</div>
			</c:if>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>기록 일시</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.regDate}</p>
						</div>
					</div>
				</div>
			</div>
		</form>
		<!-- END FORM-->
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>
</html>