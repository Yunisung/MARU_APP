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
		<h4>${DATAMAP.vactId} 가상계좌 거래 상세정보 (${DATAMAP.mchtName})</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">거래 정보</a>
			</li>
			<c:if test="${CP_SESSION.grade == '본사'}">
				<li>
					<a href="#tab_stl" data-toggle="tab">정산 정보</a>
				</li>
			</c:if>
			<c:if test="${CP_SESSION.grade == '본사'}">
				<li>
					<a href="#tab_hook" data-toggle="tab">통지 정보</a>
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
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>거래번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.vactId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-4">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-4">발행번호</label>
								<div class="col-md-8">
									<p class="form-control-static">${DATAMAP.issueId}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>거래 타입</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.trxType}</p>
								</div>
							</div>
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
								<label class='control-label col-md-4'>가맹점</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.mchtName}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>가맹점ID</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.mchtId}</p>
								</div>
							</div>
						</div>
						
						<!--/span-->
						<div class="col-md-4">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-4">은행</label>
								<div class="col-md-8">
									<p class="form-control-static">${DATAMAP.issuerBank} (${DATAMAP.bankCd})</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-4">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-4">계좌번호</label>
								<div class="col-md-8">
									<p class="form-control-static">${DATAMAP.account}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class="col-md-4">
							<div class="form-group pg-view-group">
								<label class="control-label col-md-4">전문번호</label>
								<div class="col-md-8">
									<p class="form-control-static">${DATAMAP.seqNo}</p>
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
								<label class='control-label col-md-4'>발행유형</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.vactType}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>보낸사람</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.sender}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>거래일시</label>
								<div class='col-md-8'>
									<p class='form-control-static date'>${DATAMAP.trxDay}${DATAMAP.trxTime}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>전산등록일시</label>
								<div class='col-md-8'>
									<p class='form-control-static date'>${DATAMAP.regDate}</p>
								</div>
							</div>
						</div>
						<c:if test="${DATAMAP.trxType eq '취소'}">
							<div class="form-group col-sm-12 form-subtitle">
								<label>
									<i class="fa fa-reorder"></i>취소정보 상세</label>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>원거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.rootVactId}</p>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</form>
			</div>
			<c:if test="${CP_SESSION.grade eq '본사'}">
				<div class="tab-pane" id="tab_stl">
						<!-- BEGIN FORM-->
						<form class="form-horizontal form" role="form">
								<div class="form-body row">
										<div class="form-group col-sm-12 form-subtitle">
												<label>
														<i class="fa fa-reorder"></i>가맹점 상세</label>
										</div>
										<!--/span-->
										<div class='col-md-4'>
												<div class='form-group pg-view-group'>
														<label class='control-label col-md-4'>정산예정금액</label>
														<div class='col-md-8'>
																<p class='form-control-static digits'>${DATAMAP.stlAmount}</p>
														</div>
												</div>
										</div>
										<!--/span-->
										<div class='col-md-4'>
												<div class='form-group pg-view-group'>
														<label class='control-label col-md-4'>정산예정수수료</label>
														<div class='col-md-8'>
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
														<label class='control-label col-md-4'>정산번호</label>
														<div class='col-md-8'>
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
														<label class='control-label col-md-4'>정산예정일</label>
														<div class='col-md-8'>
																<p class='form-control-static'><span class="date">${DATAMAP.stlDay}</span> (${DATAMAP.stlType})
																</p>
														</div>
												</div>
										</div>
										<div class="form-group col-sm-12 form-subtitle">
											<label>
												<i class="fa fa-reorder"></i>대행사 상세</label>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산예정수수료</label>
												<div class='col-md-8'>
													<p class='form-control-static'>
														<span class="digits">${DATAMAP.stlDistFee}</span> (VAT:
														<span class="digits">${DATAMAP.stlDistFeeVat})</span>
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산번호</label>
												<div class='col-md-8'>
													<p class='form-control-static'>
														<c:if test="${empty DATAMAP.stlDistId}">미정산</c:if>
														<c:if test="${not empty DATAMAP.stlDistId}">${DATAMAP.stlDistId}</c:if>
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산예정일</label>
												<div class='col-md-8'>
													<p class='form-control-static'><span class="date">${DATAMAP.stlDistDay}</span> (${DATAMAP.stlDistType})
													</p>
												</div>
											</div>
										</div>
										<div class="form-group col-sm-12 form-subtitle">
											<label>
												<i class="fa fa-reorder"></i>에이전시 상세</label>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산예정수수료</label>
												<div class='col-md-8'>
													<p class='form-control-static'>
														<span class="digits">${DATAMAP.stlAgencyFee}</span> (VAT:
														<span class="digits">${DATAMAP.stlAgencyFeeVat})</span>
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산번호</label>
												<div class='col-md-8'>
													<p class='form-control-static'>
														<c:if test="${empty DATAMAP.stlAgencyId}">미정산</c:if>
														<c:if test="${not empty DATAMAP.stlAgencyId}">${DATAMAP.stlAgencyId}</c:if>
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산예정일</label>
												<div class='col-md-8'>
													<p class='form-control-static'><span class="date">${DATAMAP.stlAgencyDay}</span> (${DATAMAP.stlAgencyType})
													</p>
												</div>
											</div>
										</div>
										<div class="form-group col-sm-12 form-subtitle">
											<label>
												<i class="fa fa-reorder"></i>지사 상세</label>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산예정수수료</label>
												<div class='col-md-8'>
													<p class='form-control-static'>
														<span class="digits">${DATAMAP.stlSalesFee}</span> (VAT:
														<span class="digits">${DATAMAP.stlSalesFeeVat})</span>
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산번호</label>
												<div class='col-md-8'>
													<p class='form-control-static'>
														<c:if test="${empty DATAMAP.stlSalesId}">미정산</c:if>
														<c:if test="${not empty DATAMAP.stlSalesId}">${DATAMAP.stlSalesId}</c:if>
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>정산예정일</label>
												<div class='col-md-8'>
														<p class='form-control-static'><span class="date">${DATAMAP.stlSalesDay}</span> (${DATAMAP.stlSalesType})
														</p>
												</div>
											</div>
										</div>
										<div class="form-group col-sm-12 form-subtitle">
											<label>
													<i class="fa fa-reorder"></i>은행 정산 상세</label>
										</div>
										<!--/span-->
										<div class='col-md-4'>
											<div class='form-group pg-view-group'>
												<label class='control-label col-md-4'>은행 정산</label>
													<div class='col-md-8'>
														<p class='form-control-static digits'>${DATAMAP.vanAmount}</p>
													</div>
											</div>
										</div>
												<!--/span-->
												<div class='col-md-4'>
														<div class='form-group pg-view-group'>
																<label class='control-label col-md-4'>은행 수수료</label>
																<div class='col-md-8'>
																		<p class='form-control-static digits'>${DATAMAP.vanFee}</p>
																</div>
														</div>
												</div>
												<!--/span-->
												<div class='col-md-4'>
														<div class='form-group pg-view-group'>
																<label class='control-label col-md-4'>은행 정산일자</label>
																<div class='col-md-8'>
																		<p class='form-control-static date'>${DATAMAP.vanDay}</p>
																</div>
														</div>
												</div>
										
								</div>
						</form>
				</div>
			</c:if>
			<div class="tab-pane" id="tab_hook">
				<!-- BEGIN FORM-->
					<div class="form-body row">
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>Status</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.hookStatus}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>Retry</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.hookRetry}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>Type</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.hookType}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>Address</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.hookAddr}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>Sent Date</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.hookSentDate}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'></label>
								<div class='col-md-8'>
									<button class="btn btn-sm blue" onClick="retryHook();">재전송</button>
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
			bootbox.confirm('해당 거래를 재전송 하시겠습니까?', function() {
				$.ajax({
					type: "GET",
					url: "/vact/retry/${DATAMAP.vactId}",
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
			});
		};
	</script>
</body>

</html>