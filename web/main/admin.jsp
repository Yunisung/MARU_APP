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
<link href="/assets/global/plugins/fullcalendar/fullcalendar.min.css"
	rel="stylesheet" type="text/css" />
<link href="/assets/apps/css/todo.min.css" rel="stylesheet"
	type="text/css">
<style type="text/css">
.dashboard-stat .details .number { font-size: 17px;padding-top:4px; }
.dashboard-stat .details .number-title {
	font-size: 22px;
	padding-top:0;
	font-weight: bold;
}
.fc-right {
	display: none;
}
.portlet.calendar .fc-event .fc-title {
	font-size: 12px;
}
</style>
</head>
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white page-sidebar-fixed">
	<div class="page-wrapper">
		<c:import url="/include/header.jsp" />
		<!-- BEGIN HEADER & CONTENT DIVIDER -->
    <div class="clearfix"> </div>
    <!-- END HEADER & CONTENT DIVIDER -->
		<!-- BEGIN CONTAINER -->
		<div class="page-container">
			<!-- BEGIN SIDEBAR -->
    	<div class="page-sidebar-wrapper">
				<c:import url="/include/nav.jsp" />
    	</div>
			<!-- END SIDEBAR -->
			<!-- BEGIN CONTENT -->
			<div class="page-content-wrapper">
				<div class="page-content">
					<div class="mtouch-container">
						 <!-- BEGIN PAGE BAR -->
						<div class="page-bar">
							<ul class="page-breadcrumb">
									<li>
											<a href="/">Home</a>
											<i class="fa fa-circle"></i>
									</li>
									<li>
											<span>Dashboard</span>
									</li>
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
							<br>
							<div class="row">
								<div class="col-md-8">
									<div class="portlet light portlet-fit bordered calendar">
										<div class="portlet-title">
											<div class="caption">
												<i class=" icon-speech font-green"></i>
												<span class="caption-subject font-green sbold uppercase">공지사항</span>
											</div>
										</div>
										<div class="portlet-body todo-container">
											<div class="row">
												<div class="todo-tasks-container">
													<ul class="todo-tasks-content">
														<c:forEach var="entry" items="${CP_SESSION.newNoticeList}" varStatus="status">
															<li class="todo-tasks-item">
																<h4 class="todo-inline">
																	<a href="/system/notice/view/${entry.idx }">${entry.title}</a>
																</h4>
																<p class="todo-inline todo-float-r">
																	${entry.regId}
																	<span class="todo-red">${entry.pubDay}</span>
																</p>
															</li>
														</c:forEach>
													</ul>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="portlet light portlet-fit bordered">
										<div class="portlet-title">
											<div class="caption">
												<i class=" icon-layers font-green"></i>
												<span class="caption-subject font-green bold uppercase">등록현황</span>
											</div>
										</div>
										<div class="portlet-body">
											<div class="row">
												<div class="col-md-12">
													<table class="table table-hover table-bordered">
														<thead>
															<tr>
																<th>구분</th>
																<th>가맹점</th>
																<th>대행사</th>
																<th>에이전시</th>
																<th>터미널</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td>기존</td>
																<td>${REG_STATS_MAP.mchtCnt - REG_STATS_MAP.mchtMonthCnt}</td>
																<td>${REG_STATS_MAP.distCnt - REG_STATS_MAP.distMonthCnt}</td>
																<td>${REG_STATS_MAP.agencyCnt - REG_STATS_MAP.agencyMonthCnt}</td>
																<td>${REG_STATS_MAP.tmnCnt - REG_STATS_MAP.tmnMonthCnt}</td>
															</tr>
															<tr>
																<td>이번 달</td>
																<td>${REG_STATS_MAP.mchtMonthCnt}</td>
																<td>${REG_STATS_MAP.distMonthCnt}</td>
																<td>${REG_STATS_MAP.agencyMonthCnt}</td>
																<td>${REG_STATS_MAP.tmnMonthCnt}</td>
															</tr>
															<tr>
																<td>합계</td>
																<td>${REG_STATS_MAP.mchtCnt}</td>
																<td>${REG_STATS_MAP.distCnt}</td>
																<td>${REG_STATS_MAP.agencyCnt}</td>
																<td>${REG_STATS_MAP.tmnCnt}</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- END CONTENT -->
		</div>
		 <!-- END CONTAINER -->
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script src="/assets/global/plugins/moment.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/fullcalendar/fullcalendar.min.js"	type="text/javascript"></script>
	<script src='/assets/global/plugins/fullcalendar/lang/ko.js'></script>
	<script src="/assets/global/plugins/jquery-ui/jquery-ui.min.js"	type="text/javascript"></script>
	<script src="/assets/apps/scripts/calendar.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/counterup/jquery.waypoints.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/counterup/jquery.counterup.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#nav-board').addClass('active open');
			/*
			$('#settle_calendar').fullCalendar({
				locale : 'ko',
				events: ${SETTLEJSON}
			})*/

			function numberWithCommas(x) {
				return x.toString().replace(
						/\B(?=(\d{3})+(?!\d))/g, ",");
			}
		});
	</script>
</body>
</html>