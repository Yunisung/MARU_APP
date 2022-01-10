<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
	<c:import url="/include/head.jsp" />
</head>
<!-- END HEAD -->

<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
	<div class="page-wrapper">
		<c:import url="/include/header.jsp" />
		<!-- BEGIN CONTAINER -->
		<div class="page-container">
			<c:import url="/include/nav.jsp" />
			<!-- BEGIN CONTENT -->
			<div class="page-content-wrapper">
				<div class="page-content">
					<div class="mtouch-container">
						<!-- BEGIN PAGE BAR -->
						<div class="page-bar">
							<ul class="page-breadcrumb">
								<li><a href="/">Home</a><i class="fa fa-circle"></i></li>
								<li><span>가맹점 관리</span><i class="fa fa-circle"></i></li>
								<li><span>가상계좌 등록</span></li>
							</ul>
							<div class="page-toolbar">
								<div class="btn-group btn-theme-panel">
									<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
									<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
										<i class="icon-settings"></i>
									</a>
									<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
										<div class="panel-heading">도움말</div>
										<div class="panel-body">TEXT</div>
									</div>
								</div>
							</div>
						</div>
						<!-- END PAGE BAR -->
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
						<div class="page-content-inner">
							<div class="portlet light">
								<div class="portlet-title">
									<div class="caption">
										<i class="fa fa-reorder"></i>
										<span class="caption-title"> ${MCHT_MAP.name} 가상계좌 정보 수정 </span>
									</div>
								</div>
								<div class="portlet-body form light">
									<div class="row">
										<div class="col-md-6">
											<div style="font-size:15px">
												발급받은 총 가상계좌: <span class="font-blue">${ISSUED_ACCNT}</span>
											</div>
											<br>
											<form class="form-inline" role="form">
												<div class="form-group">
													<select class="selectpicker" id="cnt-ex-bank" style="width:80px;">
														<c:forEach var="entry" items="${UNUSED_ACCNT_MAP}">
															<option value="${entry.bankCd}">${entry.issuerBank}</option>
														</c:forEach>
													</select>
												</div>
												<div class="form-group">
													<input type="text" class="form-control input-sm" style="width:180px;" value="${MCHT_MAP.name}" id="cnt-ex-holder">
												</div>
												<div class="form-group">
													<input type="text" class="form-control input-sm numberOnly" maxlength="20" style="width:80px;" value="1"
													 id="cnt-ex-accnt">
												</div>
												<a class="btn btn-sm btn-default" id="btn-ex-accnt" data-org-cnt="${ISSUED_ACCNT}">계좌 추가발행</a>
											</form>
										</div>
										<div class="col-md-6" style="font-size:15px">
											추가 발급 가능 가상계좌
											<c:forEach var="entry" items="${UNUSED_ACCNT_MAP}" varStatus="status">
												<div>${entry.issuerBank} : <span class="font-blue digits">${entry.cnt}</span></div>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							<div class="portlet light">
								<div class="portlet-title">
									<div class="caption">
										<i class="fa fa-reorder"></i>
										<span class="caption-title"> 발급 가상계좌 내역 </span>
									</div>
								</div>
								<div class="portlet-body form light">
									<div class="table-scrollable">
										<!-- 리스트 본문 시작 -->
										<table class="pg-table table table-striped table-hover flip-content">
											<!-- table-bordered -->
											<thead>
												<tr>
													<th>No</th>
													<th>발행번호</th>
													<th>계좌번호</th>
													<th>예금주명</th>
													<th>거래추적번호</th>
													<th>사용자 정의1</th>
													<th>사용자 정의2</th>
													<th>상태</th>
													<th>Action</th>
													<th>최종일자</th>
												</tr>
											</thead>
											<tbody id="list">
												<c:if test="${fn:length(DATAMAP) < 1}">
													<tr>
														<td colspan="9">조회 가능한 서류목록이 없습니다.</td>
													</tr>
												</c:if>
												<c:if test="${fn:length(DATAMAP) > 0}">
													<c:forEach var="entry" items="${DATAMAP}" varStatus="status">
														<tr>
															<td>${status.count}</td>
															<td>${entry.issueId}</td>
															<td>${entry.account}</td>

															<td>${entry.holderName}</td>
															<td><input type="text" name="trackId" value="${entry.trackId}"></td>
															<td><input type="text" name="udf1" value="${entry.udf1}"></td>
															<td><input type="text" name="udf2" value="${entry.udf2}"></td>

															<td>
																<c:choose>
																	<c:when test="${entry.status eq '대기'}">
																		<select name="status" class=" input-xsmall">
																			<option value="대기" selected>대기</option>
																			<option value="발행">발행</option>
																		</select>
																	</c:when>
																	<c:when test="${entry.status eq '발행'}">
																		<select name="status" class=" input-xsmall">
																			<option value="대기">대기</option>
																			<option value="발행" selected>발행</option>
																		</select>
																	</c:when>
																	<c:otherwise>
																		<span class="font-red">${entry.status}</span>
																	</c:otherwise>
																</c:choose>
															</td>
															<td>
																<c:if test="${entry.status ne '만료'}">
																	<button onClick="setRowInfo(this, '${entry.issueId}');" class="btn btn-sm btn-default">Set</button>
																	<button onClick="setRowInfo(this, '${entry.issueId}', 'Y');" class="btn btn-sm yellow">expiry</button>
																</c:if>
															</td>
															<td class="date">${entry.regDate}</td>
														</tr>
													</c:forEach>
												</c:if>
											</tbody>
										</table>
									</div>
								</div>
								<!-- END ADD FORM TABLE-->
							</div>
						</div>
						<!-- END PAGE CONTENT INNER -->
					</div>
				</div>
			</div>
			<!-- BEGIN CONTAINER -->
		</div>
		<c:import url="/include/footer.jsp" />
		<c:import url="/include/javascript.jsp" />
		<!-- BEGIN FORM JAVASCRIPT -->
		<script type="text/javascript">
			$('#btn-ex-accnt').click(function () {
				var bankCd = $('#cnt-ex-bank').val();
				var holderName = $('#cnt-ex-holder').val();
				var bankName = $(document).find('[data-id="cnt-ex-bank"]').text();
				var cnt = Number($('#cnt-ex-accnt').val());
				var orgCnt = $(this).attr('data-org-cnt');

				if (cnt <= 0 || !bankCd) {
					bootbox.alert('발행하려는 총 계좌수가 올바르지 않습니다.');
				} else {
					bootbox.confirm(bankName + '은행 계좌를 ' + cnt + '개 추가발급 하시겠습니까?', function (res) {
						if (res) {
							$.ajax({
								type: "get",
								url: "/mcht/vact/exissue/${MCHT_MAP.mchtId}/" + bankCd + "/" + holderName + "/" + cnt,
								success: function (res) {
									console.log(res);
									if (res.result == 'OK') {
										bootbox.alert('가상계좌 추가발급에 성공했습니다.', function () {
											window.location.reload();
										});
									} else {
										bootbox.alert(res.msg, function () {
											window.location.reload();
										});
									}
								},
								error: function (res, stats) {
									console.log(res, status);
									bootbox.alert('가상계좌 추가발급에 실패했습니다.' + status);
								}
							});
						}
					});
				}
			});

			function setRowInfo(t, issueId, expiry) {
				var trEle = $(t).closest('tr');
				var status = trEle.find('[name="status"]').val();
				var trackId = trEle.find('[name="trackId"]').val();
				var udf1 = trEle.find('[name="udf1"]').val();
				var udf2 = trEle.find('[name="udf2"]').val();
				console.log(trackId, status, udf1, udf2, issueId, expiry);

				bootbox.confirm('가상계좌 정보를 수정하시겠습니까?', function (res) {
					var sendObj = {
						issueId: issueId,
						status: status,
						trackId: trackId,
						udf1: udf1,
						udf2: udf2
					};
					if (expiry == 'Y') sendObj.status = '만료';

					if (res) {
						$.ajax({
							type: "post",
							url: "/mcht/vact/row/update",
							beforeSend: function (xhr) {
								xhr.setRequestHeader("Content-type", "application/json;charset=utf-8");
							},
							data: JSON.stringify(sendObj),
							success: function (res) {
								if (res.result == 'OK') {
									bootbox.alert('가상계좌 정보 변경에 성공했습니다.', function () {
										window.location.reload();
									});
								} else {
									bootbox.alert(res.msg, function () {
										window.location.reload();
									});
								}
							},
							error: function (xhr, status, error) {
								console.log("변경에 실패했습니다. 관리자에게 문의하세요.");
							}
						});
					}
				});
			}

			$('#nav-mcht').addClass('active');
		</script>
		<!-- END FORM JAVASCRIPT -->
		<!-- 모달 생성을 위한 베이스 -->
		<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>