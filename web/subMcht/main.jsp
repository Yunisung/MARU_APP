<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
<c:import url="./include/head.jsp" />
<link href="/assets/global/plugins/fullcalendar/fullcalendar.min.css" rel="stylesheet" type="text/css" />
<link href="/assets/apps/css/todo.min.css" rel="stylesheet" type="text/css">
<style type="text/css">
</style>
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
	<div class="page-wrapper">
		<c:import url="./include/nav.jsp" />
		<div class="page-wrapper-row full-height">
			<div class="page-wrapper-middle">
				<!-- BEGIN CONTAINER -->
				<div class="page-container">
					<div class="page-content-wrapper">
						<div class="page-head">
							<div class="container">
								<div class="page-title">
									<h1>
										Dashboard &nbsp;&nbsp; <small>* Home &gt; Dashboard </small>
									</h1>
								</div>
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
						</div>
						<div class="page-content">
							<div class="container">
								<!-- BEGIN PAGE CONTENT INNER -->
								<div class="page-content-inner">
									<div class="row">
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<a class="dashboard-stat dashboard-stat-v2 blue" href="#">
												<div class="visual">
													<i class="fa fa-comments"></i>
												</div>
												<div class="details">
													<div class="number">
														<span data-counter="counterup" data-value="${PAYMAP.dayPay }">${PAYMAP.dayPay }</span>
														￦
													</div>
													<div class="desc">오늘 승인내역 / ${PAYMAP.dayPayCnt } 건</div>
												</div>
											</a>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<a class="dashboard-stat dashboard-stat-v2 red" href="#">
												<div class="visual">
													<i class="fa fa-bar-chart-o"></i>
												</div>
												<div class="details">
													<div class="number">
														<span data-counter="counterup" data-value="${PAYMAP.dayRef }">${PAYMAP.dayRef }</span>
														￦
													</div>
													<div class="desc">오늘 취소내역 / ${PAYMAP.dayRefCnt } 건</div>
												</div>
											</a>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<a class="dashboard-stat dashboard-stat-v2 green" href="#">
												<div class="visual">
													<i class="fa fa-shopping-cart"></i>
												</div>
												<div class="details">
													<div class="number">
														<span data-counter="counterup" data-value="${PAYMAP.monthPay }">${PAYMAP.monthPay }</span>
														￦
													</div>
													<div class="desc">이번달 승인내역 / ${PAYMAP.monthPayCnt } 건</div>
												</div>
											</a>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<a class="dashboard-stat dashboard-stat-v2 purple" href="#">
												<div class="visual">
													<i class="fa fa-area-chart"></i>
												</div>
												<div class="details">
													<div class="number">
														<span data-counter="counterup" data-value="${PAYMAP.monthRef }">${PAYMAP.monthRef }</span>
														￦
													</div>
													<div class="desc">이번달 취소내역 / ${PAYMAP.monthRefCnt } 건</div>
												</div>
											</a>
										</div>
									</div>
									<div class="row">
										<div class="col-md-5">
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
										<div class="col-md-7">
											<div class="portlet light portlet-fit bordered calendar">
												<div class="portlet-title">
													<div class="caption">
														<i class=" icon-layers font-green"></i>
														<span class="caption-subject font-green sbold uppercase">정산 일정</span>
													</div>
												</div>
												<div class="portlet-body">
													<div class="row">
														<div class="col-md-12">
															<div id="settle_calendar" class="has-toolbar"></div>
														</div>
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
				</div>
				<!-- BEGIN CONTAINER -->
			</div>
		</div>
		<c:import url="./include/footer.jsp" />
	</div>
	<c:import url="./include/javascript.jsp" />
	<script src="/assets/global/plugins/moment.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
	<script src='/assets/global/plugins/fullcalendar/lang/ko.js'></script>
	<script src="/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
	<script src="/assets/apps/scripts/calendar.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/counterup/jquery.waypoints.min.js" type="text/javascript"></script>
	<script src="/assets/global/plugins/counterup/jquery.counterup.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
/* 			$('#settle_calendar').fullCalendar({
				locale : 'ko',
				events: ${SETTLEJSON}
			})
			 */
			$('#nav-board').addClass('active');
		});
	</script>
</body>
</html>