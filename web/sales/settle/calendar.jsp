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
									<li><a href="/">Home</a><i class="fa fa-circle"></i></li>
									<li><span>매출관리</span><i class="fa fa-circle"></i></li>
                  					<li><span>정산일정</span></li>
							</ul>
<!-- 							<div class="page-toolbar"> -->
<!-- 									<div class="btn-group btn-theme-panel"> -->
<!-- 										<a class="btn float-window"> -->
<!-- 										<i class="icon-size-fullscreen"></i> -->
<!-- 										</a> -->
<!-- 										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown"> -->
<!-- 											<i class="icon-settings"></i> -->
<!-- 										</a> -->
<!-- 										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning"> -->
<!-- 											<div class="panel-heading">도움말</div> -->
<!-- 											<div class="panel-body">TEXT</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 							</div> -->
						</div>
						<!-- END PAGE BAR -->
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
						<div class="page-content-inner">
							<div class="row">
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<a class="dashboard-stat dashboard-stat-v2 blue-steel" href="#">
										<div class="visual">
											<%-- <i class="fa fa-comments"></i> --%>
										</div>
										<div class="details">
											<div class="number">
												<span id="day-pay" class="counterup"></span> ￦
											</div>
											<div class="desc">
												오늘 승인내역 / <span id="day-pay-cnt"></span> 건
											</div>
										</div>
									</a>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<a class="dashboard-stat dashboard-stat-v2 blue-steel" href="#">
										<div class="visual">
											<%-- <i class="fa fa-bar-chart-o"></i> --%>
										</div>
										<div class="details">
											<div class="number">
												<span id="day-ref" class="counterup"></span> ￦
											</div>
											<div class="desc">
												오늘 취소내역 / <span id="day-rfd-cnt"></span> 건
											</div>
										</div>
									</a>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<a class="dashboard-stat dashboard-stat-v2 blue-steel" href="#">
										<div class="visual">
											<%-- <i class="fa fa-shopping-cart"></i> --%>
										</div>
										<div class="details">
											<div class="number">
												<span id="month-pay" class="counterup"></span> ￦
											</div>
											<div class="desc">
												이번달 승인내역 / <span id="month-pay-cnt"></span> 건
											</div>
										</div>
									</a>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<a class="dashboard-stat dashboard-stat-v2 blue-steel" href="#">
										<div class="visual">
											<%-- <i class="fa fa-area-chart"></i> --%>
										</div>
										<div class="details">
											<div class="number">
												<span id="month-ref" class="counterup" ></span> ￦
											</div>
											<div class="desc">
												이번달 취소내역 / <span id="month-ref-cnt"></span> 건
											</div>
										</div>
									</a>
								</div>
							</div>

							<div class="row">
								<div class="col-md-12">
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
			$('#nav-sales').addClass('active');
			
			
			function numberWithCommas(x) {
				return x.toString().replace(
						/\B(?=(\d{3})+(?!\d))/g, ",");
			}
			
			var sendCal  = "";
			var sendSettle = "";

			$.ajax({
				url: '/sales/settle/getCalendar',
				type:'get',
				cache : false,
				success: function(res) {
					
					sendCal = "OK";
					
					dataProc(res,"calendar");
				}, error:function(e){  
		        	bootbox.alert("관리자에게 문의하세요.");  
		        }  
			
			});
				
			
			$.ajax({
				url: '/sales/settle/getSettleData',
				type:'get',
				cache : false,
				success: function(res) {

					var jsonParse = JSON.parse(res.PAYMAP);
					
					sendSettle = "OK";
					
					dataProc(jsonParse,"settle");
					
				}, error:function(e){  
		        	bootbox.alert("관리자에게 문의하세요.");  
		        }  
				
			});
			
			function calendarData(res) {
				
				$('#settle_calendar').fullCalendar({
					locale : 'ko',
					events: res
				})
			}
			
			function settleData(jsonParse) {
				
				$("#day-pay").attr("data-value",jsonParse.dayPay);
				$("#day-ref").attr("data-value",jsonParse.dayRef);
				$("#month-pay").attr("data-value",jsonParse.monthPay);					
				$("#month-ref").attr("data-value",jsonParse.monthRef);
				
				$("#day-pay").text(jsonParse.dayPay);
				$("#day-ref").text(jsonParse.dayRef);
				$("#month-pay").text(jsonParse.monthPay);
				$("#month-ref").text(jsonParse.monthRef);
				
				$("#day-pay-cnt").text(jsonParse.dayPayCnt);
				$("#day-rfd-cnt").text(jsonParse.dayRefCnt);
				$("#month-pay-cnt").text(jsonParse.monthPayCnt);
				$("#month-ref-cnt").text(jsonParse.monthRefCnt);
				
				$('.counterup').counterUp({
					delay : 10,
					time : 1000
				});
				
			}
			
			var cal_A = "";
			var cal_B = "";
			var set_A = "";
			var set_B = "";
			
			function dataProc(data, temp) {
				
				if(temp =="calendar") {
					cal_A = data;
					cal_B = "calendar"
				} else if(temp =="settle") {
					set_A = data;
					set_B = "settle"
				}
				
				if(sendCal == sendSettle) {
					if(cal_B == "calendar") {
						calendarData(cal_A); 
					} 
					
					if(set_B == "settle") {
						settleData(set_A);
					}
				}
			}
			
		});
		
	</script>
</body>
</html>