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
		<h4>${DATAMAP.capId}매입내역 상세정보 (${DATAMAP.name})</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#tab1" data-toggle="tab">매입 정보</a>
			</li>
			<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사') && empty DATAREFMAP && DATAMAP.capType eq '매입'}">
				<li>
					<a href="#tab2" data-toggle="tab">취소 요청</a>
				</li>
			</c:if>
			<c:if test="${not empty DATAREFMAP && DATAMAP.capType eq '매입'}">
				<li>
					<a href="#tab3" data-toggle="tab">취소 내역</a>
				</li>
			</c:if>
			<c:if test="${CP_SESSION.grade == '본사'}">
				<li>
					<a href="#tab4" data-toggle="tab">정산 정보 상세</a>
				</li>
				<c:if test="${CP_SESSION.role =='마스터' || CP_SESSION.role =='관리자' }">
					<li>
						<a href="#tab6" data-toggle="tab">정산일자 변경</a>
					</li>
				</c:if>
				<c:if test="${empty DATAMAP.bin}">
					<li>
						<a href="#tab7" data-toggle="tab">카드번호등록</a>
					</li>
				</c:if>
				
				<c:if test="${DATAMAP.van eq 'DAOU' && DATAMAP.vanId ne 'FACTORING' && CP_SESSION.grade eq '본사' && CP_SESSION.role eq '마스터'}">
					<li>
						<a href="#tab8" data-toggle="tab">카드타입변경</a>
					</li>
				</c:if>
			<li>
				<a href="#tab5" data-toggle="tab">상담 이력</a>
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
								<label class='control-label col-md-4'>매입 상태</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.capType}
										<c:if test="${not empty DATAREFMAP && DATAMAP.capType eq '매입'}">
											<span class="font-red">(취소된 거래)</span>
										</c:if>
									</p>
								</div>
							</div>
						</div>
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>매입번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.capId}</p>
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
								<label class='control-label col-md-4'>리스크</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.risk}</p>
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
								<label class='control-label col-md-4'>승인번호</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.authCd}</p>
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
						<c:if test="${DATAMAP.capType ne '매입'}">
							<div class="form-group col-sm-12 form-subtitle">
								<label>
									<i class="fa fa-reorder"></i>취소정보 상세</label>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>취소구분</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.rfdType}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>승인 거래번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.rootTrxId}</p>
									</div>
								</div>
							</div>
						</c:if>
						<div class="form-group col-sm-12 form-subtitle">
							<label>
								<i class="fa fa-reorder"></i>카드 정보</label>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>매입사</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.issuer}</p>
								</div>
							</div>
						</div>
						<!--/span-->
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>카드 BIN</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.bin}</p>
								</div>
							</div>
						</div>
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>카드 4자리</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.last4}</p>
								</div>
							</div>
						</div>
						<div class='col-md-4'>
							<div class='form-group pg-view-group'>
								<label class='control-label col-md-4'>신용/체크</label>
								<div class='col-md-8'>
									<p class='form-control-static'>${DATAMAP.cardType}</p>
								</div>
							</div>
						</div>
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
										<p class='form-control-static date'>${DATAMAP.stlDay} (${DATAMAP.stlType})
										</p>
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
							
							
							<div class="form-group col-sm-12 form-subtitle">
								<label>
									<i class="fa fa-reorder"></i>가맹 정보</label>
							</div>
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-2'>취급품목</label>
									<div class='col-md-8'>
										<p class='form-control-static digits'>${DATATMNMAP.description}</p>
									</div>
								</div>
							</div>

						</div>
					</form>
					<!-- END FORM-->
				</c:if>
			</div>

			<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.webPay != '') && empty DATAREFMAP && DATAMAP.capType eq '매입'}">
				<div class="tab-pane" id="tab2">
					<!-- BEGIN FORM-->
					<form class="form-horizontal form" role="form">
						<div class="form-body row">
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>매입번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.capId}</p>
									</div>
								</div>
							</div>
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
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>매입 상태</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.capType}</p>
									</div>
								</div>
							</div>
							<c:if test="${DATAMAP.capType ne '매입'}">
								<!--/span-->
								<div class='col-md-6'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-4'>취소구분</label>
										<div class='col-md-8'>
											<p class='form-control-static'>${DATAMAP.rfdType}</p>
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
									<label class='control-label col-md-4'>VAT</label>
									<div class='col-md-8'>
										<p class='form-control-static digits'>${DATAMAP.vat}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>매입사</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.issuer}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>카드 4자리</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.last4}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>승인번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAMAP.authCd}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>승인일시</label>
									<div class='col-md-8'>
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
			<c:if test="${not empty DATAREFMAP && DATAMAP.capType eq '매입'}">
				<div class="tab-pane" id="tab3">
					<!-- BEGIN FORM-->
					<form class="form-horizontal form" role="form">
						<div class="form-body row">
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>취소 매입번호</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.capId}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>취소구분</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.capType}</p>
									</div>
								</div>
							</div>
							<c:if test="${DATAMAP.capType ne '매입취소'}">
								<!--/span-->
								<div class='col-md-6'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-4'>주문번호</label>
										<div class='col-md-8'>
											<p class='form-control-static'>${DATAMAP.trackId}</p>
										</div>
									</div>
								</div>
							</c:if>
							<!--/span-->
							<div class='col-md-6'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-4'>취소일시</label>
									<div class='col-md-8'>
										<p class='form-control-static'>${DATAREFMAP.regDate}</p>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</c:if>
			<c:if test="${CP_SESSION.grade eq '본사'}">
				<div class="tab-pane" id="tab4">
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
									<label class='control-label col-md-5'>정산예정금액</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.stlAmount}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산예정수수료율</label>
									<div class='col-md-7'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산예정수수료</label>
									<div class='col-md-7'>
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
									<label class='control-label col-md-5'>정산번호</label>
									<div class='col-md-7'>
										<p class='form-control-static'>
											<c:if test="${empty DATAMAP.stlId}">정산대기</c:if>
											<c:if test="${not empty DATAMAP.stlId}">${DATAMAP.stlId}</c:if>
										</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산예정일</label>
									<div class='col-md-7'>
										<p class='form-control-static date'>${DATAMAP.stlDay} (${DATAMAP.stlType})
										</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>본사 수익</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.benefit}</p>
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
									<label class='control-label col-md-5'>정산예정수수료율</label>
									<div class='col-md-7'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlDistRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산예정수수료</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.stlDistFee}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>차액정산예정수수료율</label>
									<div class='col-md-7'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlDiffDistRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>차액정산예정수수료</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.stlDiffDistFee}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산번호</label>
									<div class='col-md-7'>
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
									<label class='control-label col-md-5'>정산예정일</label>
									<div class='col-md-7'>
										<p class='form-control-static date'>${DATAMAP.stlDistDay}</p>
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
									<label class='control-label col-md-5'>정산예정수수료율</label>
									<div class='col-md-7'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlAgencyRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산예정수수료</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.stlAgencyFee}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>차액정산예정수수료율</label>
									<div class='col-md-7'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlDiffAgencyRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>차액정산예정수수료</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.stlDiffAgencyFee}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산번호</label>
									<div class='col-md-7'>
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
									<label class='control-label col-md-5'>정산예정일</label>
									<div class='col-md-7'>
										<p class='form-control-static date'>${DATAMAP.stlAgencyDay}</p>
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
									<label class='control-label col-md-5'>정산예정수수료율</label>
									<div class='col-md-7'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlSalesRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산예정수수료</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.stlSalesFee}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>차액정산예정수수료율</label>
									<div class='col-md-7'>
										<p class='form-control-static'><fmt:formatNumber value="${DATAMAP.stlDiffSalesRate * 100}" pattern="0.000"/> %</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>차액정산예정수수료</label>
									<div class='col-md-7'>
										<p class='form-control-static digits'>${DATAMAP.stlDiffSalesFee}</p>
									</div>
								</div>
							</div>
							<!--/span-->
							<div class='col-md-4'>
								<div class='form-group pg-view-group'>
									<label class='control-label col-md-5'>정산번호</label>
									<div class='col-md-7'>
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
									<label class='control-label col-md-5'>정산예정일</label>
									<div class='col-md-7'>
										<p class='form-control-static date'>${DATAMAP.stlSalesDay}</p>
									</div>
								</div>
							</div>

							<c:if test="${DATAMAP.stlDiffType ne '일반' }">
								<div class="form-group col-sm-12 form-subtitle">
									<label> <i class="fa fa-reorder"></i>영중소 차액정산 상세
									</label>
								</div>
								<!--/span-->
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-5'>차액정산상태</label>
										<div class='col-md-7'>
											<p class='form-control-static'>${DATAMAP.stlDiffStatus}</p>
										</div>
									</div>
								</div>
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-5'>영중소 구분</label>
										<div class='col-md-7'>
											<p class='form-control-static'>${DATAMAP.stlDiffType}</p>
										</div>
									</div>
								</div>
								<!--/span-->
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-5'>차액정산액</label>
										<div class='col-md-7'>
											<p class='form-control-static digits'>${DATAMAP.stlDiffAmt}</p>
										</div>
									</div>
								</div>
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-5'>카드사 영중소구분</label>
										<div class='col-md-7'>
											<p class='form-control-static'>${DATAMAP.stlDiffVanType}</p>
										</div>
									</div>
								</div>
								<!--/span-->
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-5'>카드사 차액정산액</label>
										<div class='col-md-7'>
											<p class='form-control-static digits'>${DATAMAP.stlDiffVanAmt}</p>
										</div>
									</div>
								</div>
								<!--/span-->
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-5'>입금예정일</label>
										<div class='col-md-7'>
											<p class='form-control-static date'>${DATAMAP.stlDiffVanDay}</p>
										</div>
									</div>
								</div>
								<!--/span-->
								<div class='col-md-4'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-5'>카드사 신용/체크</label>
										<div class='col-md-7'>
											<p class='form-control-static date'>${DATAMAP.stlDiffVanCardType}</p>
										</div>
									</div>
								</div>
								<!--/span-->
								<div class='col-md-8'>
									<div class='form-group pg-view-group'>
										<label class='control-label col-md-2'>차액정산결과</label>
										<div class='col-md-7'>
											<p class='form-control-static date'>${DATAMAP.stlDiffResultMsg}</p>
										</div>
									</div>
								</div>
							</c:if>
						</div>
					</form>
				</div>
				<div class="tab-pane" id="tab6">
					<div class="portlet light portlet-form">
						<div class="portlet-body form light">
							<h4>정산일자 변경 거래 기준 일 : ${DATAMAP.trxDay}</h4>
							<br/>
							<form class="form-horizontal" role="form" id="dayForm" action="#">
								<div class="form-group">
									<label for="lstlDay" class="col-md-2 control-label">가맹점 정산 예정일자 (${DATAMAP.stlType})</label>
									<div class="col-md-4">
										<input type="text" name="stlDay" class="form-control" id="lstlDay" value="${DATAMAP.stlDay}" maxlength="8"> </div>
									<input type="hidden" name="oldStlDay" value="${DATAMAP.stlDay}">
								</div>
								<div class="form-group">
									<label for="lstlVanDay" class="col-md-2 control-label">카드사/PG 입금 예정일자 </label>
									<div class="col-md-4">
										<input type="text" name="stlVanDay" class="form-control" id="lstlVanDay" value="${DATAMAP.stlVanDay}" maxlength="8"> </div>
									<input type="hidden" name="oldStlVanDay" value="${DATAMAP.stlVanDay}">
								</div>
								<div class="form-group">
									<label for="lsummary" class="col-md-2 control-label">변경이력 </label>
									<div class="col-md-8">
										<textarea rows="4" name="daysummary" id="lsummary" placeholder="Write comment here ..." class="form-control input-md"></textarea>
									</div>
								</div>

								<div class="form-group">
									<div class="col-md-offset-2 col-md-10">
										<button class="btn purple" type="button" id="dayButton">정산일자변경 </button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				
				
				<div class="tab-pane" id="tab7">
					<div class="portlet light portlet-form">
						<div class="portlet-body form light">
							<h4>카드 번호 등록 </h4>
							<br/>
							<form class="form-horizontal" role="form" id="cardNumForm" action="#">
								<div class="form-group">
									<label for="lstlDay" class="col-md-2 control-label">BIN (카드번호 앞6자리)</label>
									<div class="col-md-4">
										<input type="text" name="bin" class="form-control" id="bin" value="" maxlength="6"> </div>
								</div>
								<div class="form-group">
									<label for="lstlVanDay" class="col-md-2 control-label">LAST4 (마지막 4자리) <br/> 3자리는 앞에 * </label>
									<div class="col-md-4">
										<input type="text" name="last4" class="form-control" id="last4" value="" maxlength="4"> </div>
									
								</div>
								<div class="form-group">
									<label for="lsummary" class="col-md-2 control-label">변경이력 </label>
									<div class="col-md-8">
										<textarea rows="4" name="cardsummary" id="cardsummary" placeholder="Write comment here ..." class="form-control input-md"></textarea>
									</div>
								</div>

								<div class="form-group">
									<div class="col-md-offset-2 col-md-10">
										<button class="btn purple" type="button" id="cardButton">카드번호 등록 </button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				
				<div class="tab-pane" id="tab8">
					<div class="portlet light portlet-form">
						<div class="portlet-body form light">
							<h4>카드 타입 변경 체크 <-> 신용 </h4>
							<br/>
							<form class="form-horizontal" role="form" id="cardTypeForm" action="#">
								<div class="form-group">
									<label for="cardType" class="col-md-2 control-label">기존카드유형</label>
									<div class="col-md-4">
										<input type="text" name="cardType" class="form-control" id="cardType" value="${DATAMAP.cardType}"> </div>
								</div>
								<div class="form-group">
									<label for="newCardType" class="col-md-2 control-label">변경 카드유형<br/></label>
									<div class="col-md-8">
										<select name="newCardType" style="width:100px;height:30px;">
											<option value="신용">신용</option>
											<option value="체크">체크</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="lsummary" class="col-md-2 control-label">변경이력 </label>
									<div class="col-md-8">
										<textarea rows="4" name="cardTypeSummary" id="cardTypeSummary" placeholder="Write comment here ..." class="form-control input-md"></textarea>
									</div>
								</div>

								<div class="form-group">
									<div class="col-md-offset-2 col-md-10">
										<button class="btn purple" type="button" id="cardTypeButton">카드타입변경  </button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</c:if>
			<div class="tab-pane" id="tab5">
				<div class="portlet light portlet-form">
					<div class="portlet-body form light">
						<div class="c-contact">
							<form action="#" id="iqrForm">
								<p>상담이력</p>
								<c:if test="${CP_SESSION.grade == '본사'}">
									<div class="form-group">
										<textarea rows="4" name="summary" placeholder="Write comment here ..." class="form-control input-md"></textarea>
									</div>
									<div class="input-group input-group-md c-square col-md-4">
										<input type="text" name="telNo" placeholder="Contact Phone" class="form-control input-sm">
										<span class="input-group-btn">
											<button class="btn btn-sm blue" type="button" id="iqrButton">등록</button>
										</span>
									</div>
								</c:if>
							</form>
						</div>

						<div class="table-scrollable">
							<!-- 리스트 본문 시작 -->
							<table class="pg-table table table-striped table-hover flip-content">
								<!-- table-bordered -->
								<thead>
									<tr>
										<th data-sort="string">등록일자</th>
										<th data-sort="string">유형</th>
										<th data-sort="string">설명</th>
										<th data-sort="string">전화번호</th>
										<th data-sort="string">등록자</th>

									</tr>
								</thead>
								<tbody id="edit-list">
									<c:forEach var="entry" items="${IQR_MAP}" varStatus="status">
										<tr>
											<td style="width:80px;" class="date">${entry.regDate}</td>
											<td style="width:80px;">${entry.iqrType}</td>
											<td class="left">${entry.summary}</td>
											<td style="width:80px;">${entry.telNo}</td>
											<td style="width:80px;">${entry.regName}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
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
							url: "/trx/cap/cancel2/${DATAMAP.trxId}/"+amount,
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


		$('#iqrButton').on(
			'click',
			function () {
				if ($("textarea[name='summary']").val().length < 2) {
					bootbox.alert('변경이력을 입력하여 주시기 바랍니다.');
				} else {
					$.ajax({
						type: "post",
						url: "/trx/cap/iqr/${DATAMAP.capId}",
						data: $("#iqrForm").serialize(),
						success: function (json, textStatus) {
							if (json == "OK") {
								$('#edit-list').prepend('<tr><td>now</td><td>일반</td><td>' + $("textarea[name='summary']").val() +
									'</td><td>' + $("input[name='telNo']").val() + '</td><td>${CP_SESSION.name}</td></tr>');
							} else {
								bootbox.alert("등록실패");
							}
						},
						error: function (xhr, textStatus, errorThrown) {
							bootbox.alert('Error ' + errorThrown);
						}
					});
				}
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
		
		$('#cardButton').on(
				'click',
				function () {
					if ($('#bin').val().length != 6) {
						bootbox.alert('카드번호 앞 6자리를 입력하여 주세요. ');
					} else if ($('#last4').val().length != 4) {
						bootbox.alert('카드번호 뒷 4자리르 입력하여 주세요. 3자리인 경우 앞에 *을 추가하여 주시기 바랍니다.');
					} else if ($("textarea[name='cardsummary']").val().length < 2) {
						bootbox.alert('변경이력을 입력하여 주시기 바랍니다.');
					} else {
						$.ajax({
							type: "post",
							url: "/trx/cap/binupdate/${DATAMAP.capId}",
							data: $("#cardNumForm").serialize(),
							success: function (json, textStatus) {
								bootbox.alert(json);

							},
							error: function (xhr, textStatus, errorThrown) {
								bootbox.alert('Error ' + errorThrown);
							}
						});
					}
				});
		
		$('#cardTypeButton').on(
				'click',
				function () {
					if ($("textarea[name='cardTypeSummary']").val().length < 2) {
						bootbox.alert('변경이력을 입력하여 주시기 바랍니다.');
					} else {
						$.ajax({
							type: "post",
							url: "/trx/cap/cardTypeUpdate/${DATAMAP.capId}",
							data: $("#cardTypeForm").serialize(),
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
		<c:if test = "${fn:startsWith(DATAMAP.van, 'DAOU')}">
			<c:choose>
				<c:when test="${DATAMAP.vanId eq 'CSF27016'}">
					<c:if test = "${DATAMAP.capType eq '매입'}">
						<button class="btn btn-sm btn-default" onClick="window.open('https://agent.daoupay.com/common/PayInfoPrintCreditCard.jsp?DAOUTRX=${DATAMAP.vanTrxId}&INSTATUS=11','KSPAY','width=450,height=750');">
							다우페이 영수증 조회
						</button>
					</c:if>
					<c:if test = "${DATAMAP.capType eq '매입취소'}">
						<button class="btn btn-sm btn-default" onClick="window.open('https://agent.daoupay.com/common/PayInfoPrintCreditCard.jsp?DAOUTRX=${DATAMAP.vanTrxId}&INSTATUS=12','KSPAY','width=450,height=750');">
							다우페이 취소 영수증 조회
						</button>
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test = "${DATAMAP.capType eq '매입'}">
						<button class="btn btn-sm btn-default" onClick="window.open('https://agent.daoupay.com/common/PayInfoPrintDirectCard.jsp?DAOUTRX=${DATAMAP.vanTrxId}&STATUS=11','KSPAY','width=420,height=720');">
							다우페이 영수증 조회
						</button>
					</c:if>
					<c:if test = "${DATAMAP.capType eq '매입취소'}">
						<button class="btn btn-sm btn-default" onClick="window.open('https://agent.daoupay.com/common/PayInfoPrintDirectCard.jsp?DAOUTRX=${DATAMAP.vanTrxId}&STATUS=12','KSPAY','width=450,height=750');">
							다우페이 취소 영수증 조회
						</button>
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'KSPAY')}">
			<c:if test = "${!fn:startsWith(DATAMAP.vanTrxId, 'TX')}">
				<button class="btn btn-sm btn-default" onClick="window.open('https://pgims.ksnet.co.kr/pg_infoc/src/bill/new_credit_view.jsp?tr_no=${DATAMAP.vanTrxId}','KSPAY','width=460,height=750');">
						KSPAY 영수증 조회
				</button>
			</c:if>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'DANAL')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://www.danalpay.com/receipt/creditcard/view.aspx?dataType=cp&param=${DATAMAP.danalParam}','KSPAY','width=460,height=750');">
						DANAL 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'ALLAT')}">
			<button class="btn btn-sm btn-default" onClick="window.open('http://www.allatpay.com/servlet/AllatBizPop/member/pop_card_receipt.jsp?${DATAMAP.allatParam}','app','width=410,height=650');">
						ALLAT 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'NICE')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://pg.nicepay.co.kr/issue/IssueLoader.jsp?TID=${DATAMAP.vanTrxId}&type=0','popupIssue','width=420,height=540');">
						나이스페이 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'WELCOMEO')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://payapi.welcomepayments.co.kr/api/receipt/print?tid=${DATAMAP.vanTrxId}&hash_value=${DATAMAP.hash_value}','popupIssue','width=603,height=884');">
						웰컴페이먼츠 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'KICC')}">
			<form name="rec" id="rec" method="post">
				<input type="hidden" name="controlNo" value="${DATAMAP.vanTrxId}">
			</form>
			<button class="btn btn-sm btn-default" onClick="kiccReceipt();">
						KICC 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'SPC')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://cp.mainpay.co.kr/card/cardReceipt_popup.do?ref_no=${DATAMAP.vanTrxId}&tran_date=${fn:substring(DATAMAP.trxDay,2,8) }','popupIssue','width=603,height=884');">
						SPC 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'GALAXIA')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://cpadmin.billgate.net/billgate/common/authCardReceipt.jsp?mid=${DATAMAP.GalaxiaMID}&transNm=${DATAMAP.vanTrxId}&currTp=0000','popupIssue','width=440,height=790');">
						Billgate 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:endsWith(DATAMAP.van, 'WELCOME')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://wbiz.paywelcome.co.kr/mCmReceipt_head.jsp?noTid=${DATAMAP.vanTrxId}&noMethod=1','popupIssue','width=520,height=700');">
						웰컴페이먼츠 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:endsWith(DATAMAP.van, 'WELCOMESUB')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://payapi.welcomepayments.co.kr/api/receipt/print?tid=${DATAMAP.vanTrxId}&hash_value=${DATAMAP.hash_value}','popupIssue','width=603,height=884');">
						웰컴페이먼츠 영수증 조회
			</button>
		</c:if>
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>

<script>

//KICC 영수증 조회
function kiccReceipt(){
	var pop = "popupIssue";
	window.open("",pop,'width=603,height=884');
	
	var rec=document.rec;
	rec.target=pop;
	rec.action="https://office.easypay.co.kr/mcht/receipt/CardReceiptAction.do";
	
	rec.submit();
}
//KICC 영수증 조회
</script>
</html>