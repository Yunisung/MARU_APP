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
		<h4>${DATAMAP.name} Tax 정보</h4>
	</div>
	<div class="modal-body">
		<!-- BEGIN FORM-->
		<form class="form-horizontal form" role="form">
			<div class="form-body row">
				<div class="col-md-12">
					<p> <i class="fa fa-check"></i> 기본 정보</p>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">Tax 아이디</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.taxId}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">이름</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.name}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">사용 상태</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.taxStatus}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">TAX 결제 한도</label>
						<div class="col-md-9">
							<p class="form-control-static digits"><c:if test="${DATAMAP.taxLimit > 0}">${DATAMAP.taxLimit}</c:if>
								<c:if test="${DATAMAP.taxLimit == 0}">
									제한 없음
								</c:if>
							</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">회사명</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.compName}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">대표자 성명</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.ceoName}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">${DATAMAP.idType}</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.identity}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">이메일</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.email}</p>
						</div>
					</div>
				</div>
				<div class="col-md-12">
					<p> <i class="fa fa-check"></i> 은행 정보</p>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">은행코드</label>
						<div class="col-md-9">
							<p class="form-control-static date">${DATAMAP.bankCd}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">은행명</label>
						<div class="col-md-9">
							<p class="form-control-static date">${DATAMAP.bankName}</p>
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
						<label class="control-label col-md-3">예금주</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.accntHolder}</p>
						</div>
					</div>
				</div>
				<%-- <!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">계좌 확인</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.accntCheck}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">계좌 확인일</label>
						<div class="col-md-9">
							<p class="form-control-static date">${DATAMAP.accntDate}</p>
						</div>
					</div>
				</div> --%>
				<div class="col-md-12">
					<p> <i class="fa fa-check"></i> 주소 정보</p>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">우편번호</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.zip}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">주소</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.addr1}</p>
						</div>
					</div>
				</div>
				<!--/span-->
				<div class="col-md-6">
					<div class="form-group pg-view-group">
						<label class="control-label col-md-3">상세주소</label>
						<div class="col-md-9">
							<p class="form-control-static">${DATAMAP.addr2}</p>
						</div>
					</div>
				</div>
			</div>
		</form>
		<!-- END FORM-->
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
		<c:if test="${CP_SESSION.grade == '본사'}">
		<button type="button" class="btn btn-sm green" onclick="location.href='/mcht/tax/modify/${DATAMAP.taxId}';">
			<i class="fa fa-pencil"></i> 정보 수정
		</button>
		</c:if>
	</div>
</body>
</html>