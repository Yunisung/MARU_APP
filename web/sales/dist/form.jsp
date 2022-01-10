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
									<li><span>매출관리</span><i class="fa fa-circle"></i></li>
                  <li><span>대행사 매출조회</span></li>
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
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/sales/dist/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="대행사 매출현황">
												<input type="hidden" data-reg="false" name="thead" value="capMonth:매입월,distName:대행사,payAmt:매입 금액,payCnt:매입 건수,rfdAmt:매입취소 금액,rfdCnt:매입취소 건수,totalAmt:합계금액,totalCnt:합계건수,stlAmt:정산금액,stlFee:가맹점수수료,stlDistFee:대행사수익">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">대행사명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm distName" name="distName" data-oper="lk" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">조회 시작월</label>
															<div class=" col-lg-8">
																<select class="selectpicker form-control" name="capMonth" data-oper="ge">
																	<option value="">-----&nbsp;</option>
																	<c:forEach var="entry" items="${CP_SESSION.salesMonthList}" varStatus="status">
																	<option value="${entry}">${ entry}</option>
																	</c:forEach>
																</select>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">조회 종료 월</label>
															<div class=" col-lg-8">
																<select class="selectpicker form-control" name="capMonth" data-oper="le">
																	<option value="">-----&nbsp;</option>
																	<c:forEach var="entry" items="${CP_SESSION.salesMonthList}" varStatus="status">
																	<option value="${entry}">${ entry}</option>
																	</c:forEach>
																</select>
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
														<button type="button" class="btn btn-sm green" id="search_submit" onClick="searchForList()">
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
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-sales').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>