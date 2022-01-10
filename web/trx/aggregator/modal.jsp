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
		<h4>${DATAMAP.name} 매입내역 상세정보</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active"><a href="#tab1" data-toggle="tab">매입 정보</a></li>
			<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사') && empty DATAREFMAP && DATAMAP.capType eq '매입'}">
			<li><a href="#tab2" data-toggle="tab">정산 취소 요청</a></li>
			</c:if>
			<c:if test="${not empty DATAREFMAP && DATAMAP.capType eq '매입'}">
			<li><a href="#tab3" data-toggle="tab">취소 내역</a></li>
			</c:if>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="tab1">
				<!-- BEGIN FORM-->
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>매입번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.capId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>거래번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.trxId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>가맹점</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.name}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>터미널</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.tmnId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>거래추적번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.trackId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>매입 상태</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.capType}</p>
								</div>
							</div>
						</div>
						<c:if test="${DATAMAP.capType ne '매입'}">
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-3'>취소구분</label>
									<div class='col-md-9'>
										<p class='form-control-static'>${DATAMAP.rfdType}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-3'>승인 거래번호</label>
									<div class='col-md-9'>
										<p class='form-control-static'>${DATAMAP.rootTrxId}</p>
									</div>
								</div>
							</div>
						</c:if>
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
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>VAT</label>
								<div class='col-md-9'>
									<p class='form-control-static digits'>${DATAMAP.vat}</p>
								</div>
							</div>
						</div>
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
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>승인번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.authCd}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>승인일시</label>
								<div class='col-md-9'>
									<p class='form-control-static date'>${DATAMAP.trxDay}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>입력일시</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.regDate}</p>
								</div>
							</div>
						</div>
					</div>
				</form>
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>정산예정금액</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.stlAmount}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>정산예정수수료율</label>
								<div class='col-md-9'>
									<p class='form-control-static rate'>${DATAMAP.stlRate}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>정산예정수수료</label>
								<div class='col-md-9'>
									<p class='form-control-static digits'>${DATAMAP.stlFee}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>정산예정수수료(VAT)</label>
								<div class='col-md-9'>
									<p class='form-control-static digits'>${DATAMAP.stlFeeVat}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>정산유형</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.stlType}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>수익</label>
								<div class='col-md-9'>
									<p class='form-control-static digits'>${DATAMAP.benefit}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>정산예정일</label>
								<div class='col-md-9'>
									<p class='form-control-static date'>${DATAMAP.stlDay}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>정산번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>
										<c:if test="${empty DATAMAP.stlId}">미정산</c:if>
										<c:if test="${not empty DATAMAP.stlId}">${DATAMAP.stlId}</c:if>
									</p>
								</div>
							</div>
						</div>
					</div>
				</form>
				<!-- END FORM-->
			</div>
			<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사') && empty DATAREFMAP && DATAMAP.capType eq '매입'}">
			<div class="tab-pane" id="tab2">
				<!-- BEGIN FORM-->
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>매입번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.capId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>거래번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.trxId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>가맹점</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.name}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>터미널</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.tmnId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>거래추적번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.trackId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>매입 상태</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.capType}</p>
								</div>
							</div>
						</div>
						<c:if test="${DATAMAP.capType ne '매입'}">
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-3'>취소구분</label>
									<div class='col-md-9'>
										<p class='form-control-static'>${DATAMAP.rfdType}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-3'>승인 거래번호</label>
									<div class='col-md-9'>
										<p class='form-control-static'>${DATAMAP.rootTrxId}</p>
									</div>
								</div>
							</div>
						</c:if>
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
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>VAT</label>
								<div class='col-md-9'>
									<p class='form-control-static digits'>${DATAMAP.vat}</p>
								</div>
							</div>
						</div>
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
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>승인번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.authCd}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>승인일시</label>
								<div class='col-md-9'>
									<p class='form-control-static date'>${DATAMAP.trxDay}</p>
								</div>
							</div>
						</div>
					</div>
				</form>
				<form class="form-horizontal form-bordered" role="form" name="form">
					<div class="row" style="padding: 30px;">
						<div class="form-group col-sm-12">
								<div class="input-group col-sm-6 pull-right">
									<div class="input-icon">
										<i class="fa fa-won fa-fw"></i>
										<input id="cap-cancel-amt" class="form-control" type="text" name="amount" data-reg="false" value="${DATAMAP.amount }" readonly>
									</div>
									<span class="input-group-btn">
										<button id="cap-cancel-btn" class="btn btn-success" type="button" data-capid="${DATAMAP.trxId}">
											<i class="fa fa-arrow-left fa-fw"></i> 취소 요청
										</button>
									</span>
								</div>
						</div>
					</div>
				</form>
			</div>
			</c:if>
			<c:if test="${not empty DATAREFMAP && DATAMAP.capType eq '매입'}">
			<div class="tab-pane" id="tab3">
				<!-- BEGIN FORM-->
				<form class="form-horizontal form" role="form">
					<div class="form-body row">
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>취소 매입번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAREFMAP.capId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>취소구분</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAREFMAP.capType}</p>
								</div>
							</div>
						</div>
						<c:if test="${DATAMAP.capType ne '매입취소'}">
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>주문번호</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAMAP.trackId}</p>
								</div>
							</div>
						</div>
						</c:if>
						<!--/span-->
						<div class='col-md-6'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-3'>취소일시</label>
								<div class='col-md-9'>
									<p class='form-control-static'>${DATAREFMAP.regDate}</p>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			</c:if>
		</div>
	</div>
	<script>
		$('#cap-cancel-btn').on('click', function(){
			bootbox.confirm('거래를 전액 취소 하시겠습니까?', function(result) {
				if (result) {
					$.ajax({
						type : "get",
						url : "/trx/cap/cancel/${DATAMAP.trxId}",
						success: function(json, textStatus){
							json = jQuery.parseJSON(json);
							console.log(json);
							if(json.result.resultCd == "0000"){
								bootbox.alert("취소 요청이 정상 처리되었습니다.");
							}else{
								bootbox.alert("취소 요청이 실패 했습니다.<br>" + json.result.resultMsg + " "+json.result.advanceMsg);
							}
						},
						error: function(xhr, textStatus, errorThrown){
							bootbox.alert('Error ' + errorThrown);
						}
					});
				}
			});
		});
	</script>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>
</html>