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
	<title>CREDITOP</title>
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
												<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '일미감');">
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
															<th colspan="6">매출(매입일 기준)</th>
															<th colspan="8">입금(입금일 기준)</th>
															<th colspan="7">지급(정산일 기준)</th>
														</tr>
														<tr>
															<th colspan="2">건수</th>
															<th colspan="2">금액</th>
															<th colspan="2">합계</th>
															<th colspan="2">건수</th>
															<th colspan="2">매출금액</th>
															<th colspan="3">수수료</th>
															<th rowspan="2">입금액</th>
															<th colspan="2">건수</th>
															<th colspan="2">수수료</th>
															<th rowspan="2">수수료합계</th>
															<th rowspan="2">지급액</th>
															<th rowspan="2">수익</th>
														</tr>
														<tr>
															<th>승인</th>
															<th>취소</th>
															<th>승인</th>
															<th>취소</th>
															<th>건수</th>
															<th>금액</th>
															<th>승인</th>
															<th>취소</th>
															<th>승인</th>
															<th>취소</th>
															<th>승인</th>
															<th>취소</th>
															<th>합계</th>
															<th>승인</th>
															<th>취소</th>
															<th>승인</th>
															<th>취소</th>
														</tr>
													</thead>
													<tbody id="list">
														<c:set var="trxCapAmt" value="0"/>
														<c:set var="trxCapCnt" value="0"/>
														<c:set var="trxRfdAmt" value="0"/>
														<c:set var="trxRfdCnt" value="0"/>
														<c:set var="inCapAmt" value="0"/>
														<c:set var="inCapCnt" value="0"/>
														<c:set var="inRfdAmt" value="0"/>
														<c:set var="inRfdCnt" value="0"/>
														<c:set var="inVanCapFee" value="0"/>
														<c:set var="inVanRfdFee" value="0"/>
														<c:set var="inVanFee" value="0"/>
														<c:set var="inVanInAmt" value="0"/>
														<c:set var="outPayAmt" value="0"/>
														<c:set var="outPayCnt" value="0"/>
														<c:set var="outPayFee" value="0"/>
														<c:set var="outRfdAmt" value="0"/>
														<c:set var="outRfdCnt" value="0"/>
														<c:set var="outRfdFee" value="0"/>
														<c:set var="outAllFee" value="0"/>
														<c:set var="outVanFee" value="0"/>
														<c:set var="outDistFee" value="0"/>
														<c:set var="outAgencyFee" value="0"/>
														<c:set var="outPayOutAmt" value="0"/>
														<c:set var="outBenefit" value="0"/>

														<c:forEach var="entry" items="${DATAMAP}" varStatus="status">
															<tr>
																<td class="digits right">${status.count}</td>
																<td class="digits right">${entry.name}</td>
																<td class="digits right">${entry.trxCapCnt}</td>
																<td class="digits right">${entry.trxRfdCnt}</td>

																<td class="digits right">${entry.trxCapAmt}</td>
																<td class="digits right">${entry.trxRfdAmt}</td>
																
																<td class="digits right">${entry.trxCapCnt + entry.trxRfdCnt}</td>
																<td class="digits right">${entry.trxCapAmt + entry.trxRfdAmt}</td>
																

																<td class="digits right">${entry.inCapCnt}</td>
																<td class="digits right">${entry.inRfdCnt}</td>

																<td class="digits right">${entry.inCapAmt}</td>
																<td class="digits right">${entry.inRfdAmt}</td>

																<td class="digits right">${entry.inVanCapFee}</td>
																<td class="digits right">${entry.inVanRfdFee}</td>
																<td class="digits right">${entry.inVanFee}</td>
																<td class="digits right">${entry.inVanInAmt}</td>

																<td class="digits right">${entry.outPayCnt}</td>
																<td class="digits right">${entry.outRfdCnt}</td>

																<%-- <td class="digits right">${entry.outPayAmt}</td>
																<td class="digits right">${entry.outRfdAmt}</td> --%>

																<td class="digits right">${entry.outPayFee}</td>
																<td class="digits right">${entry.outRfdFee}</td>
																<td class="digits right">${entry.outAllFee}</td>
																<%-- <td class="digits right">${entry.outVanFee}</td>
																<td class="digits right">${entry.outDistFee}</td>
																<td class="digits right">${entry.outAgencyFee}</td> --%>
																<td class="digits right">${entry.outPayOutAmt}</td>
																<td class="digits right">${entry.outBenefit}</td>

																<c:set var="trxCapAmt" value="${trxCapAmt + entry.trxCapAmt}"/>
																<c:set var="trxCapCnt" value="${trxCapCnt + entry.trxCapCnt}"/>
																<c:set var="trxRfdAmt" value="${trxRfdAmt + entry.trxRfdAmt}"/>
																<c:set var="trxRfdCnt" value="${trxRfdCnt + entry.trxRfdCnt}"/>
																<c:set var="inCapAmt" value="${inCapAmt + entry.inCapAmt}"/>
																<c:set var="inCapCnt" value="${inCapCnt + entry.inCapCnt}"/>
																<c:set var="inRfdAmt" value="${inRfdAmt + entry.inRfdAmt}"/>
																<c:set var="inRfdCnt" value="${inRfdCnt + entry.inRfdCnt}"/>
																<c:set var="inVanCapFee" value="${inVanCapFee + entry.inVanCapFee}"/>
																<c:set var="inVanRfdFee" value="${inVanRfdFee + entry.inVanRfdFee}"/>
																<c:set var="inVanFee" value="${inVanFee + entry.inVanFee}"/>
																<c:set var="inVanInAmt" value="${inVanInAmt + entry.inVanInAmt}"/>
																<c:set var="outPayAmt" value="${outPayAmt + entry.outPayAmt}"/>
																<c:set var="outPayCnt" value="${outPayCnt + entry.outPayCnt}"/>
																<c:set var="outPayFee" value="${outPayFee + entry.outPayFee}"/>
																<c:set var="outRfdAmt" value="${outRfdAmt + entry.outRfdAmt}"/>
																<c:set var="outRfdCnt" value="${outRfdCnt + entry.outRfdCnt}"/>
																<c:set var="outRfdFee" value="${outRfdFee + entry.outRfdFee}"/>
																<c:set var="outAllFee" value="${outAllFee + entry.outAllFee}"/>
																<c:set var="outVanFee" value="${outVanFee + entry.outVanFee}"/>
																<c:set var="outDistFee" value="${outDistFee + entry.outDistFee}"/>
																<c:set var="outAgencyFee" value="${outAgencyFee + entry.outAgencyFee}"/>
																<c:set var="outPayOutAmt" value="${outPayOutAmt + entry.outPayOutAmt}"/>
																<c:set var="outBenefit" value="${outBenefit + entry.outBenefit}"/>
															</tr>
														</c:forEach>
														<tr  class="warning">
															<td ></td>
															<td ></td>
															<td class="digits right"><c:out value="${trxCapCnt}"/></td>
															<td class="digits right"><c:out value="${trxRfdCnt}"/></td>
															<td class="digits right"><c:out value="${trxCapAmt}"/></td>
															<td class="digits right"><c:out value="${trxRfdAmt}"/></td>
															<td class="digits right"><c:out value="${trxCapCnt + trxRfdCnt}"/></td>
															<td class="digits right"><c:out value="${trxCapAmt + trxRfdAmt}"/></td>
															
															<td class="digits right"><c:out value="${inCapCnt}"/></td>
															<td class="digits right"><c:out value="${inRfdCnt}"/></td>
															<td class="digits right"><c:out value="${inCapAmt}"/></td>
															<td class="digits right"><c:out value="${inRfdAmt}"/></td>
															<td class="digits right"><c:out value="${inVanCapFee}"/></td>
															<td class="digits right"><c:out value="${inVanRfdFee}"/></td>
															<td class="digits right"><c:out value="${inVanFee}"/></td>
															<td class="digits right"><c:out value="${inVanInAmt}"/></td>
															<td class="digits right"><c:out value="${outPayCnt}"/></td>
															<td class="digits right"><c:out value="${outRfdCnt}"/></td>
															<td class="digits right"><c:out value="${outPayFee}"/></td>
															<td class="digits right"><c:out value="${outRfdFee}"/></td>
															<td class="digits right"><c:out value="${outAllFee}"/></td>
															<td class="digits right"><c:out value="${outPayOutAmt}"/></td>
															<td class="digits right"><c:out value="${outBenefit}"/></td>
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
					