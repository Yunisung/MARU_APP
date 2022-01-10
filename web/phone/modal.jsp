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
		<h4>${DATAMAP.trxId} 결제 내역 상세정보 (${DATAMAP.name})</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">결제 정보</a>
			</li>
			<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.webPay != '') && empty DATAREFMAP && DATAMAP.status eq '승인'}">
				<li>
					<a href="#tab2" data-toggle="tab">취소 요청</a>
				</li>
			</c:if>
			<c:if test="${not empty DATAREFMAP && DATAMAP.status eq '취소'}">
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
								<i class="fa fa-reorder"></i>거래 기본 정보</label>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>금액</label>
								<div class='col-md-8'>
									<p class='form-control-static digits'>${DATAMAP.amount}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>VAT</label>
								<div class='col-md-8'>
									<p class='form-control-static digits'>${DATAMAP.vat}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>거래번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.trxId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>가맹점</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.name}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>터미널</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.tmnId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>거래추적번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.trackId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>승인일시</label>
								<div class='col-md-8'>
									<p class='form-control-static date'>${DATAMAP.regDay}${DATAMAP.regTime}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>전산등록일시</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.regDate}</p>
								</div>
							</div>
						</div>
						<c:if test="${DATAMAP.status ne '승인'}">
							<div class="form-group col-sm-12 form-subtitle">
								<label>
									<i class="fa fa-reorder"></i>취소정보 상세</label>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>취소구분</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.status}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>승인 거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.rootTrxId}</p>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</form> 
				<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사')}">
			 		<form class="form-horizontal form" role="form">
						<div class="form-body row">
							<div class="form-group col-sm-12 form-subtitle">
								<label>
									<i class="fa fa-reorder"></i>정산 정보 상세</label>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-6'>정산예정금액</label>
									<div class='col-md-6'>
										<p class='form-control-static digits'>${DATAMAP.stlAmount}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-6'>정산예정수수료율</label>
									<div class='col-md-6'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-6'>정산예정수수료</label>
									<div class='col-md-6'>
										<p class='form-control-static'>
											<span class="digits">${DATAMAP.stlFee}</span> (VAT:
											<span class="digits">${DATAMAP.stlFeeVat})</span>
										</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-6'>정산유형</label>
									<div class='col-md-6'>
										<p class='form-control-static date'>
										<c:if test="${DATAMAP.stlType == '0'}">주정산</c:if>
										<c:if test="${DATAMAP.stlType == '1'}">수납정산</c:if>
										</p>
									</div>
								</div>
							</div>
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-6'>정산번호</label>
									<div class='col-md-6'>
										<p class='form-control-static'>
											<c:if test="${empty DATAMAP.stlId}">미정산</c:if>
											<c:if test="${not empty DATAMAP.stlId}">${DATAMAP.stlId}</c:if>
										</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-6'>정산예정일</label>
									<div class='col-md-6'>
										<p class='form-control-static date'>${DATAMAP.stlDay}</p>
									</div>
								</div>
							</div>
							<c:if test="${CP_SESSION.grade == '본사'}">
								<!--/span-->
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-6'>수익</label>
										<div class='col-md-6'>
											<p class='form-control-static digits'>${DATAMAP.benefit}</p>
										</div>
									</div>
								</div>
								
							</c:if>
						</div>
					</form>
					<!-- END FORM-->
				</c:if>
			</div>
			<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.webPay != '') && empty DATAREFMAP && DATAMAP.status eq '승인'}">
				<div class="tab-pane" id="tab2">
					<!-- BEGIN FORM-->
					<form class="form-horizontal form" role="form">
						<div class="form-body row">
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.trxId}</p>
									</div>
								</div>
							</div>
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
									<label class='control-label col-md-4'>터미널</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.tmnId}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>거래추적번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.trackId}</p>
									</div>
								</div>
							</div>
							<c:if test="${DATAMAP.status ne '승인'}">
								<!--/span-->
								<div class='col-md-6'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-4'>취소구분</label>
										<div class='col-md-8'>
											<p class='form-control-static'>${DATAMAP.status}</p>
										</div>
									</div>
								</div>
								<!--/span-->
								<div class='col-md-6'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-4'>승인 거래번호</label>
										<div class='col-md-8'>
											<p class='form-control-static'>${DATAMAP.rootTrxId}</p>
										</div>
									</div>
								</div>
							</c:if>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>금액</label>
									<div class='col-md-8'>
										<p class='form-control-static digits'>${DATAMAP.amount}</p>
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
						</div>
					</form>
					<form class="form-horizontal form-bordered" role="form" name="form">
						<div class="row" style="padding: 30px;">
							<div class="form-group col-sm-12">
								<div class="input-group col-sm-6 pull-right">
									<div class="input-icon">
										<i class="fa fa-won fa-fw"></i>
										<c:if test="${CP_SESSION.grade eq '본사' }">
										<input id="cap-cancel-amt" class="form-control" type="text" name="amount" data-reg="false" value="${DATAMAP.amount }">
										</c:if>
										<c:if test="${CP_SESSION.grade ne '본사' }">
										<input id="cap-cancel-amt" class="form-control" type="text" name="amount" data-reg="false" value="${DATAMAP.amount }" readonly>
										</c:if>
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
			<c:if test="${not empty DATAREFMAP && DATAMAP.status eq '취소'}">
				<div class="tab-pane" id="tab3">
					<!-- BEGIN FORM-->
					<form class="form-horizontal form" role="form">
						<div class="form-body row">
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.trxId}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>주문번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.trackId}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>원거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.rootTrxId}</p>
									</div>
								</div>
							</div>
								<!--/span-->
								<div class='col-md-6'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-4'>원거래주문번호</label>
										<div class='col-md-8'>
											<p class='form-control-static'>${DATAREFMAP.rootTrackId}</p>
										</div>
									</div>
								</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>취소일시</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.regDate}</p>
									</div>
								</div>
							</div>
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>금액</label>
									<div class='col-md-8'>
										<p class='form-control-static digits'>${DATAREFMAP.rfdAmount}</p>
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
	
		$('#cap-cancel-amt').val(addComma(String($('#cap-cancel-amt').val())));
		
		$("#cap-cancel-amt").keyup(function(){
			$("#cap-cancel-amt").val(addComma(String($("#cap-cancel-amt").val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
	
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
	
		$('#cap-cancel-btn').on(
			'click',
			function () {
				bootbox.confirm('거래를 취소 하시겠습니까?', function (result) {
					if (result) {
						var amount = $('#cap-cancel-amt').val().replace(/,/g, '');
						$.ajax({
							type: "get",
							url: "/phone/cancel/${DATAMAP.trxId}/"+amount,
							success: function (json, textStatus) {
								json = jQuery.parseJSON(json);
								console.log(json);
								if (json.result.resultCd == "0000") {
									bootbox.alert("취소 요청이 정상 처리되었습니다.");
								} else {
									bootbox.alert("취소 요청이 실패 했습니다.<br>" +
										json.result.resultMsg + " " +
										json.result.advanceMsg);
								}
							},
							error: function (xhr, textStatus, errorThrown) {
								bootbox.alert('Error ' + errorThrown);
							}
						});
					}
				});
			});

		$('#dayButton').on(
			'click',
			function () {
				if ($('#lstlDay').val().length != 8) {
					bootbox.alert('가맹점 정산일자를 입력하여 주시기 바랍니다. ');
				} else if ($('#lstlVanDay').val().length != 8) {
					bootbox.alert('카드사 입금 예정일자를 입력하여 주시기 바랍니다.');
				} else if ($("textarea[name='daysummary']").val().length < 2) {
					bootbox.alert('변경이력을 입력하여 주시기 바랍니다.');
				} else {
					$.ajax({
						type: "post",
						url: "/trx/cap/daychange/${DATAMAP.capId}",
						data: $("#dayForm").serialize(),
						success: function (json, textStatus) {
							bootbox.alert(json);

						},
						error: function (xhr, textStatus, errorThrown) {
							bootbox.alert('Error ' + errorThrown);
						}
					});
				}
			});
	</script>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>

</html>