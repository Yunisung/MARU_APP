<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<title>MTOUCH</title>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta content="width=device-width, initial-scale=1" name="viewport"/>
	<meta content="PG" name="description"/>
	<meta content=MTOUCH name="author"/>
	<!-- <meta name="MobileOptimized" content="640"/> -->
	<!-- BEGIN GLOBAL MANDATORY STYLES -->
	<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-toastr/toastr.css" rel="stylesheet"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN THEME GLOBAL STYLES -->
	<link href="/assets/global/css/components-rounded.min.css" rel="stylesheet" id="style_components" type="text/css" />
	<link href="/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
	<!-- END THEME GLOBAL STYLES -->
	
	<!-- BEGIN THEME LAYOUT STYLES -->
	<link href="/assets/layouts/layout3/css/layout.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/layouts/layout3/css/themes/blue-hoki.min.css" rel="stylesheet" type="text/css" id="style_color" />
	<link href="/assets/global/css/pgmate.css" rel="stylesheet" type="text/css" />
	<!-- END THEME LAYOUT STYLES -->
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
	<link rel="icon" href="favicon.ico" type="image/x-icon">
	
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-container-bg-solid page-header-menu-fixed">
	<div class="page-wrapper">
		<div class="page-wrapper-row full-height">
			<div class="page-wrapper-middle">
				<!-- BEGIN CONTAINER -->
				<div class="page-container">
					<div class="page-content-wrapper">
						<div class="page-content">
							<div class="mtouch-container">
								<!-- BEGIN PAGE CONTENT INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form portlet-fit full-height-content full-height-content-scrollable bordered" id="searchResult">
										<div class="portlet-title">
											<div class="caption font-red-sunglo">
												<i class="icon-share font-red-sunglo"></i>
												<span class="caption-subject bold uppercase"> Result </span>
											</div>
											<div class="actions">
												<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
												<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '일마감-승인거래기준상세');">
													<i class="fa fa-file-excel-o" aria-hidden="true"></i>
												</a>
												<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
											</div>
										</div>
										<div class="portlet-body form light">
											<div class="table-scrollable">
												<!-- 리스트 본문 시작 -->
												<table class="pg-table table table-bordered table-hover flip-content" id="sortTable">
													<!-- table-bordered -->
													<thead>
														<tr>
															<th rowspan="3">No</th>
															<th rowspan="3" data-sort="string">가맹점</th>
															<th colspan="6">매출</th>
															<th colspan="2">입금예정액</th>
															<th colspan="2">지급예정액</th>
															<th rowspan="3">예상 매출이익</th>
															<th colspan="4">예상 영업수수료</th>
															<th rowspan="3">차액정산입금액</th>
															<th rowspan="3">예상 영업이익</th>
														</tr>
														<tr>
															<th colspan="2">승인</th>
															<th colspan="2">취소</th>
															<th colspan="2">합계</th>
															<th rowspan="2">입금수수료</th>
															<th rowspan="2">입금액</th>
															<th rowspan="2">가맹점수수료</th>
															<th rowspan="2">금액</th>
															<th rowspan="2">대행사</th>
															<th rowspan="2">에이전시</th>
															<th rowspan="2">지사</th>
															<th rowspan="2">총합계</th>
														</tr>
														<tr>
															<th>건수</th>
															<th>금액</th>
															<th>건수</th>
															<th>금액</th>
															<th>건수</th>
															<th>금액</th>
														</tr>
													</thead>
													<tbody id="list">
														<c:set var="payCnt" value="0"/>
														<c:set var="payAmt" value="0"/>
														<c:set var="rfdCnt" value="0"/>
														<c:set var="rfdAmt" value="0"/>
														<c:set var="totalCnt" value="0"/>
														<c:set var="totalAmt" value="0"/>
														<c:set var="stlVanFee" value="0"/>
														<c:set var="stlVanAmt" value="0"/>
														<c:set var="stlFee" value="0"/>
														<c:set var="stlAmt" value="0"/>
														<c:set var="profit" value="0"/>
														<c:set var="stlDistFee" value="0"/>
														<c:set var="stlAgencyFee" value="0"/>
														<c:set var="stlSalesFee" value="0"/>
														<c:set var="totSalesFee" value="0"/>
														<c:set var="stlDiffAmt" value="0"/>
														<c:set var="benefit" value="0"/>

														<c:forEach var="entry" items="${DATAMAP}" varStatus="status">
															<tr>
																<td class="digits right">${status.count}</td>
																<td class="digits right">${entry.name}</td>
																<td class="digits right">${entry.payCnt}</td>
																<td class="digits right">${entry.payAmt}</td>
																<td class="digits right">${entry.rfdCnt}</td>
																<td class="digits right">${entry.rfdAmt}</td>
																<td class="digits right">${entry.totalCnt}</td>
																<td class="digits right">${entry.totalAmt}</td>
																<td class="digits right">${entry.stlVanFee}</td>
																<td class="digits right">${entry.stlVanAmt}</td>
																<td class="digits right">${entry.stlFee}</td>
																<td class="digits right">${entry.stlAmt}</td>
																<td class="digits right">${entry.profit}</td>
																<td class="digits right">${entry.stlDistFee}</td>
																<td class="digits right">${entry.stlAgencyFee}</td>
																<td class="digits right">${entry.stlSalesFee}</td>
																<td class="digits right">${entry.totSalesFee}</td>
																<td class="digits right">${entry.stlDiffAmt}</td>
																<td class="digits right">${entry.benefit}</td>

																<c:set var="payCnt" value="${payCnt + entry.payCnt}"/>
																<c:set var="payAmt" value="${payAmt + entry.payAmt}"/>
																<c:set var="rfdCnt" value="${rfdCnt + entry.rfdCnt}"/>
																<c:set var="rfdAmt" value="${rfdAmt + entry.rfdAmt}"/>
																<c:set var="totalCnt" value="${totalCnt + entry.totalCnt}"/>
																<c:set var="totalAmt" value="${totalAmt + entry.totalAmt}"/>
																<c:set var="stlVanFee" value="${stlVanFee + entry.stlVanFee}"/>
																<c:set var="stlVanAmt" value="${stlVanAmt + entry.stlVanAmt}"/>
																<c:set var="stlFee" value="${stlFee + entry.stlFee}"/>
																<c:set var="stlAmt" value="${stlAmt + entry.stlAmt}"/>
																<c:set var="profit" value="${profit + entry.profit}"/>
																<c:set var="stlDistFee" value="${stlDistFee + entry.stlDistFee}"/>
																<c:set var="stlAgencyFee" value="${stlAgencyFee + entry.stlAgencyFee}"/>
																<c:set var="stlSalesFee" value="${stlSalesFee + entry.stlSalesFee}"/>
																<c:set var="totSalesFee" value="${totSalesFee + entry.totSalesFee}"/>
																<c:set var="stlDiffAmt" value="${stlDiffAmt + entry.stlDiffAmt}"/>
																<c:set var="benefit" value="${benefit + entry.benefit}"/>
															</tr>
														</c:forEach>
														<tr  class="warning">
															<td ></td>
															<td ></td>
															<td class="digits right"><c:out value="${payCnt}"/></td>
															<td class="digits right"><c:out value="${payAmt}"/></td>
															<td class="digits right"><c:out value="${rfdCnt}"/></td>
															<td class="digits right"><c:out value="${rfdAmt}"/></td>
															<td class="digits right"><c:out value="${totalCnt}"/></td>
															<td class="digits right"><c:out value="${totalAmt}"/></td>
															<td class="digits right"><c:out value="${stlVanFee}"/></td>
															<td class="digits right"><c:out value="${stlVanAmt}"/></td>
															<td class="digits right"><c:out value="${stlFee}"/></td>
															<td class="digits right"><c:out value="${stlAmt}"/></td>
															<td class="digits right"><c:out value="${profit}"/></td>
															<td class="digits right"><c:out value="${stlDistFee}"/></td>
															<td class="digits right"><c:out value="${stlAgencyFee}"/></td>
															<td class="digits right"><c:out value="${stlSalesFee}"/></td>
															<td class="digits right"><c:out value="${totSalesFee}"/></td>
															<td class="digits right"><c:out value="${stlDiffAmt}"/></td>
															<td class="digits right"><c:out value="${benefit}"/></td>
														</tr>
													</tbody>
												</table>
											</div>
											<div class="row">
												
											</div>
										</div>
									<iframe id="txtArea1" style="display:none"></iframe>
									</div>
									<!-- 내용 폼 종료 -->
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
				<!-- BEGIN INNER FOOTER -->
				<div class="page-footer">
					<div class="page-footer-inner"></div>
				</div>
				<div class="scroll-to-top">
					<i class="icon-arrow-up"></i>
				</div>
				<!-- END INNER FOOTER -->
		
	</div>

<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS-->
<!--[if lt IE 9]>
		<script src="/assets/global/plugins/respond.min.js"></script>
		<script src="/assets/global/plugins/excanvas.min.js"></script> 
		<script src="/assets/global/plugins/ie8.fix.min.js"></script> 
		<![endif]-->
<!-- BEGIN CORE PLUGINS -->
<script src="/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-toastr/toastr.js" type="text/javascript"></script>

<!-- 데이터 피커 -->
<script src="/assets/global/plugins/moment.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.ko.min.js" type="text/javascript"></script>

<!-- 폼 체크 -->
<script src="/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery-validation/js/localization/messages_ko.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery-validation/js/jquery-validate.bootstrap-tooltip.min.js" type="text/javascript"></script>

<script src="/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>

<script src="/assets/global/scripts/pg-common.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/app.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/pgmate.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/pg-search.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/pg-form.js?v=9" type="text/javascript"></script>

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="/assets/pages/scripts/components-bootstrap-select.min.js" type="text/javascript"></script>
<script src="/assets/layouts/layout3/scripts/layout.min.js" type="text/javascript"></script>

<script>
<%--
$( document ).ready(function() {
	$('.fullscreen').trigger( "click" );
});
--%>
</script>
</body>
<!-- END BODY -->	

</html>
					