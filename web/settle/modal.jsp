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
		<h4>${DATAMAP.memberName} ${DATAMAP.grade} 정산내역 상세정보</h4>
	</div>
	<div class="modal-body">
		<!-- BEGIN FORM-->
		<form class="form-horizontal form" role="form">
			<div class="form-body row">
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>정산번호</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.stlId}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<%-- <div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>정산 대상 구분</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.grade}</p>
						</div>
					</div>
				</div> --%>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>${DATAMAP.grade} ID</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.memberId}<c:if test="${empty DATAMAP.memberId}">${DATAMAP.tmnId}</c:if></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>지급 총액</label>
						<div class='col-md-9'>
							<p class='form-control-static'><fmt:formatNumber type="number" value="${DATAMAP.stlAmt}" pattern="#,##0" /></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>확정 상태</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.status}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>지급 상태</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.payStatus}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>대상 거래 시작일</label>
						<div class='col-md-9'>
							<p class='form-control-static date'>${DATAMAP.startDay}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>대상 거래 종료일</label>
						<div class='col-md-9'>
							<p class='form-control-static date'>${DATAMAP.endDay}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>매입 총액</label>
						<div class='col-md-9'>
							<p class='form-control-static'><fmt:formatNumber type="number" value="${DATAMAP.payAmt}" pattern="#,##0" /></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>매입 수수료 총액</label>
						<div class='col-md-9'>
							<p class='form-control-static'><fmt:formatNumber type="number" value="${DATAMAP.payFee}" pattern="#,##0" /></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>승인 거래 VAT</label>
						<div class='col-md-9'>
							<p class='form-control-static'><fmt:formatNumber type="number" value="${DATAMAP.payVat}" pattern="#,##0" /></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>승인 거래량</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.payCnt}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>취소거래 금액 </label>
						<div class='col-md-9'>
							<p class='form-control-static'><fmt:formatNumber type="number" value="${DATAMAP.rfdAmt}" pattern="#,##0" /></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>취소거래 수수료</label>
						<div class='col-md-9'>
							<p class='form-control-static'><fmt:formatNumber type="number" value="${DATAMAP.rfdFee}" pattern="#,##0" /></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>취소 거래 VAT</label>
						<div class='col-md-9'>
							<p class='form-control-static'><fmt:formatNumber type="number" value="${DATAMAP.rfdVat}" pattern="#,##0" /></p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>취소 거래량</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.rfdCnt}</p>
						</div>
					</div>
				</div>
				<c:if test="${empty DATAMAP.payOutDay}">
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>정산 예정일</label>
						<div class='col-md-9'>
							<p class='form-control-static date'>${DATAMAP.stlDay}</p>
						</div>
					</div>
				</div>
				</c:if>
				<!--/span-->
				<c:if test="${not empty DATAMAP.payOutDay}">
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>지급일</label>
						<div class='col-md-9'>
							<p class='form-control-static date'>${DATAMAP.payOutDay}</p>
						</div>
					</div>
				</div>
				</c:if>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>정산 은행</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.bankName} (${DATAMAP.bankCd})</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>정산 계좌</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.account}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>정산 예금주</label>
						<div class='col-md-9'>
							<p class='form-control-static'>${DATAMAP.accntHolder}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class='col-md-6'>
					<div class='form-group pg-view-group'>
						<label class='control-label col-md-3'>등록일</label>
						<div class='col-md-9'>
							<p class='form-control-static date'>${DATAMAP.regDate}</p>
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