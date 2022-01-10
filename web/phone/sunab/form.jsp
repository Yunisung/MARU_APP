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
									<li><span>휴대폰 결제</span><i class="fa fa-circle"></i></li>
									<li><span>수납정산 조회</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
										</div>
									</div>
							</div>
						</div>
						<!-- END PAGE BAR -->
						<!-- BEGIN PAGE CONTENT - MARU - INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/settle/phone/sunab/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="수납정산 내역">
												<input type="hidden" data-reg="false" name="thead" value="trxDay:거래월,name:가맹점명,mchtId:가맹점ID,amount:거래금액,m0_sunab:0회차 수납금액,m0_percent:0회차 수납율,m1_sunab:1회차 수납금액,m1_percent:1회차 수납율,m2_sunab:2회차 수납금액,m2_percent:2회차 수납율,m3_sunab:3회차 수납금액,m3_percent:3회차 수납율,m4_sunab:4회차 수납금액,m4_percent:4회차 수납율,m5_sunab:5회차 수납금액,m5_percent:5회차 수납율,m6_sunab:6회차 수납금액,m6_percent:6회차 수납율,m7_sunab:7회차 수납금액,m7_percent:7회차 수납율,m8_sunab:8회차 수납금액,m8_percent:8회차 수납율,m9_sunab:9회차 수납금액,m9_percent:9회차 수납율,m10_sunab:10회차 수납금액,m10_percent:10회차 수납율,minabAmt:미납액,SunabSumAmt:누적 수납액,tot_percent:최종 수납율">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래월</label>
															<div class=" col-lg-8">
																<select class="selectpicker form-control" name="trxDay" data-oper="eq">
																	<option value="">-----&nbsp;</option>
																	<c:forEach var="entry" items="${CP_SESSION.salesMonthList}" varStatus="status">
																		<option value="${entry}" <c:if test="${status.last}">selected</c:if>>${ entry}</option>
																	</c:forEach>
																</select>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="가맹점 ID">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="가맹점명">
															</div>
														</div>
													</div>
												</div>
												<div class="form-actions nobg right">
													<div class="btn folding-search-btn icon-arrow-down"></div>
													<div class="">
														<button type="button" class="btn btn-sm blue-dark" id="SearchClear">
															<i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
														</button>
														<button type="button" class="btn btn-sm green" id="searchSubmit" onClick="searchForList()">
															<i class="fa fa-search" aria-hidden="true"></i> SEARCH
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult">
										
									</div>
									<!-- 내용 폼 종료 -->
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
						</div>
					</div>
				<!-- BEGIN CONTAINER -->
		</div>
		<iframe id="txtArea1" style="display:none"></iframe>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<script type="text/javascript">
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-phone-settle').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>