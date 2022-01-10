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
									<li><span>Dashboard</span><i class="fa fa-circle"></i></li>
									<li><span>Dashboard</span></li>
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
									<div class="row">
										<div class="col-md-6">
											<div class="portlet light portlet-form">
												<div class="portlet-title">
													<div class="caption font-red-sunglo">
														<i class="icon-hourglass font-red-sunglo"></i>
														<span class="caption-subject bold uppercase"> 최근 매입 내역 </span>
													</div>
												</div>
												<div class="portlet-body form light">
													<div class="table-scrollable">
														<!-- 리스트 본문 시작 -->
														<table class="pg-table table table-striped table-hover flip-content">
															<!-- table-bordered -->
															<colgroup>
																<col width="25%"/>
																<col width="20%"/>
																<col width="20%"/>
																<col width="15%"/>
															</colgroup>
															<thead>
																<tr>
																	<th>가맹점</th>
																	<!-- <th>거래번호</th> -->
																	<th>터미널</th>
																	<th>매입구분</th>
																	<th>금액</th>
																	<th>거래일자</th>
																</tr>
															</thead>
															<tbody id="list">
																<c:forEach var="entry" items="${DATACAPMAP}" varStatus="status">
																	<c:if test="${status.count < 15 }">
																		<tr>
																			<td>${entry.name}</td>
																			<%-- <td>${entry.trxId}</td> --%>
																			<td>${entry.tmnId}</td>
																			<td>${entry.capType}</td>
																			<td>
																				<fmt:formatNumber type="number" value="${entry.amount}" pattern="#,##0" />
																			</td>
																			<td class="date">${entry.trxDay }</td>
																		</tr>
																	</c:if>
																</c:forEach>
															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="portlet light portlet-form">
												<div class="portlet-title">
													<div class="caption font-red-sunglo">
														<i class="icon-bulb font-red-sunglo"></i>
														<span class="caption-subject bold uppercase"> TAX 한도 관리</span>
													</div>
												</div>
												<div class="portlet-body form light">
													<div class="table-scrollable">
														<!-- 리스트 본문 시작 -->
														<table class="pg-table table table-striped table-hover flip-content">
															<!-- table-bordered -->
															<colgroup>
																<col width="25%"/>
																<col width="20%"/>
																<col width="30%"/>
																<col width="25%"/>
															</colgroup>
															<thead>

																<tr>
																	<th data-sort="string">이름</th>
																	<th data-sort="string">남은 한도</th>
																	<th data-sort="string">TAX 한도</th>
																	<th data-sort="string">최종거래</th>
																</tr>
															</thead>
															<tbody id="list">
																<c:forEach var="entry" items="${DATATAXMAP}" varStatus="status">
																	<c:if test="${status.count < 15 }">
																		<tr>
																			<td>${entry.name}</td>
																			<td><fmt:formatNumber type="number" value="${entry.remainLimit}" pattern="#,##0" /></td>
																			<td><fmt:formatNumber type="number" value="${entry.usedAmt}" pattern="#,##0" />
																				/
																				<fmt:formatNumber type="number" value="${entry.taxLimit}" pattern="#,##0" />
																			</td>
																			<td>${fn:substring(entry.lastDate, 0, 16)}</td>
																		</tr>
																	</c:if>
																</c:forEach>
															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
					</div>
				</div>
				<!-- BEGIN CONTAINER -->
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<script src="/assets/global/plugins/moment.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
	<script src='/assets/global/plugins/fullcalendar/lang/ko.js'></script>
	<script src="/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
	<script src="/assets/apps/scripts/calendar.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/counterup/jquery.waypoints.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/counterup/jquery.counterup.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#nav-board').addClass('active');
		});
	</script>
</body>
</html>