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
									<li><span>시스템관리</span><i class="fa fa-circle"></i></li>
                  <li><span>상담이력 관리</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body">
											</div>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/system/iqr/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="상담이력 관리">
												<input type="hidden" data-reg="false" name="thead" value="capId:매입번호,mchtId:가맹점ID,mchtName:가맹점명,summary:리스크 원인,status:리스크 반영상태,regDate:생성일시">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">매입번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="trxId" data-oper="eq" placeholder="거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtName" name="mchtName" data-oper="lk" placeholder="가맹점명">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="가맹점 ID">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange">
																	<input type="text" class="form-control now-date" name="regDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="regDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">터미널 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm tmnId" name="tmnId" data-oper="lk" placeholder="터미널 ID">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm authCd" name="authCd" data-oper="lk" placeholder="승인번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">내용</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="summary" data-oper="lk" placeholder="">
															</div>
														</div>
													</div>
													<div class="row search-opt">
														<c:import url="/common/selectGrade.jsp" />
														<input type="hidden" id="grade_search" name="parentId" data-oper="eq" value=""/>
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
		$('#nav-system').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>