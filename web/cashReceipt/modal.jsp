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

<head>
	<style type="text/css">
		.form-subtitle {
			padding-top: 10px;
			padding-left: 38px;
			font-size: 14px;
		}
		.form-subtitle i {
			margin-right: 10px;
		}
	</style>
</head>

<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h4>${DATAMAP.cashId} 현금영수증 내역 상세정보 (${DATAMAP.name})</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">승인 내역</a>
			</li>
			<c:if test="${DATAMAP.assort eq '취소'}">
				<li>
					<a href="#tab3" data-toggle="tab">취소 내역</a>
				</li>
			</c:if>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="tab1">
				<!-- BEGIN FORM-->
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
						<div class="form-group col-sm-12 form-subtitle">
							<label>
								<i class="fa fa-reorder"></i>기본 정보</label>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>금액</label>
								<div class='col-md-8'>
									<p class='form-control-static digits'>${DATAMAP.amt}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>VAT</label>
								<div class='col-md-8'>
									<p class='form-control-static digits'>${DATAMAP.vat}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>주문번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.transNo}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>현금영수증 타입</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.cashType}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>현금영수증 상태</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.stateCd}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>현금영수증 용도구분</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.purpose}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>현금영수증 등록구분</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.identityGb}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>현금영수증 등록번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.identity}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>현금영수증 승인번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.authNo}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<c:if test="${not empty DATAMAP.orgAuthNo}">
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>현금영수증 원거래번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.orgAuthNo}</p>
								</div>
							</div>
						</div>
						</c:if>
						<!--/span-->
						<c:if test="${not empty DATAMAP.trxId}">
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>가상계좌 거래번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.trxId}</p>
								</div>
							</div>
						</div>
						</c:if>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>가맹점</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.name}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>추가공제 구분</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.deductionType}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>거래일시</label>
								<div class='col-md-8'>
									<p class='form-control-static date'>${DATAMAP.trDt}${DATAMAP.trTime}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>승인일시</label>
								<div class='col-md-8'>
									<p class='form-control-static date'>${DATAMAP.regDay}${DATAMAP.regTime}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>오류 결과</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.errCd}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<c:if test="${DATAMAP.assort eq '취소'}">
							<div class="form-group col-sm-12 form-subtitle">
								<label>
									<i class="fa fa-reorder"></i>취소정보 상세</label>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>구분</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.assort}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>승인 거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAORG.cashId}</p>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</form> 
			</div>
			<c:if test="${DATAMAP.assort eq '취소'}">
				<div class="tab-pane" id="tab3">
					<!-- BEGIN FORM-->
					<form class="form-horizontal form" role="form">
						<div class="form-body row">
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>현금영수증 거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.cashId}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>주문번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.transNo}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>원거래 승인번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.orgAuthNo}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>취소일시</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.trDt}</p>
									</div>
								</div>
							</div>
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>공급가액</label>
									<div class='col-md-8'>
										<p class='form-control-static digits'>${DATAMAP.amt}</p>
									</div>
								</div>
							</div>
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>부가세</label>
									<div class='col-md-8'>
										<p class='form-control-static digits'>${DATAMAP.vat}</p>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</c:if>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>

</html>