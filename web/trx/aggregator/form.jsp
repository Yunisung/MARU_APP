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
									<li><span>대표가맹점 관리</span><i class="fa fa-circle"></i></li>
                  <li><span>매출 조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/trx/aggregator/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="매입거래내역">
												<input type="hidden" data-reg="false" name="thead" value="capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점,tmnId:터미널ID,trackId:거래추적번호,capType:매입구분,rfdType:취소구분,rootTrxId:원거래번호,amount:금액,stlAmount:정산예정금액,stlRate:MDR,stlFee:수수료,stlFeeVat:수수료부가세,issuer:매입사,authCd:승인번호,trxDay:거래일,regTime:거래시간">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">매입번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="capId" data-oper="eq" placeholder="매입번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="trxId" data-oper="eq" placeholder="거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">터미널</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm tmnId" name="tmnId" data-oper="lk" placeholder="터미널명">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">매입구분</label>
															<select class="selectpicker col-lg-8" name="capType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="매입">매입</option>
																	<option value="매입취소">매입취소</option>
																	<option value="정산취소">정산취소</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control" name="trxDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="trxDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">매입일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control" name="regDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="regDay" value="" data-oper="le">
																</div>
															</div>
														</div>
													</div>
													<div class="row search-opt">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">취소구분</label>
															<select class="selectpicker col-lg-8" name="capType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="전체">전체</option>
																<option value="부분">부분</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">취소원거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="rootTrxId" data-oper="eq" placeholder="거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">금액</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="amount" data-oper="eq" placeholder="승인금액">
															</div>
														</div>
														
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">카드뒤4자리</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="last4" data-oper="eq" placeholder="카드번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="authCd" data-oper="lk" placeholder="승인번호">
															</div>
														</div>
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
		
		$('#nav-trx').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>