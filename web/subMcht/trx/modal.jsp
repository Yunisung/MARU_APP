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
				<%--<form class="form-horizontal form" role="form">
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
				</form>--%>
				<!-- END FORM-->
			</div>
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
		<c:if test = "${fn:startsWith(DATAMAP.van, 'KCP')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://admin8.kcp.co.kr/assist/bill.BillActionNew.do?cmd=card_bill&tno=${DATAMAP.vanTrxId}&order_no=${DATAMAP.trackId}&trade_mony=${DATAMAP.amount}','popupIssue','width=470,height=815');">
						KCP 영수증 조회
			</button>
		</c:if>
		<c:if test = "${fn:startsWith(DATAMAP.van, 'GALAXIA')}">
			<button class="btn btn-sm btn-default" onClick="window.open('https://cpadmin.billgate.net/billgate/common/authCardReceipt.jsp?mid=${DATAMAP.GalaxiaMID}&transNm=${DATAMAP.vanTrxId}&currTp=0000','popupIssue','width=440,height=790');">
						Billgate 영수증 조회
			</button>
		</c:if>
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
	</div>
</body>
</html>