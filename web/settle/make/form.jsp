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
									<li><span>정산관리</span><i class="fa fa-circle"></i></li>
                  <li><span>가맹점 정산</span></li>
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
												<code>매입</code> : 전체 매입된 원금 (지급보류 포함)<p/>
												<code>매입취소</code> : 전체 매입 취소된 원금<p/>
												<code>지급보류</code> : 고액,중복,최소 등 RISK로 추가된 원금<p/>
												<code>수수료</code> : 매입에 따른 수수료(지급보류 포함) - 매입 취소 수수료<p/>
												<code>입금수수료</code> : VAN 또는 PG 사 예상 수수료<p/>
												<code>대행사수수료</code> : 대행사 및 에이전시 지급 예정 금액<p/>
												<code>수익</code> : 수수료 - 입금수수료 - 대행사수수료<p/>
												<code>예수금</code> : 지급 보류된 원금<p/>
												<code>지급 예정금액</code> : 매입금액 - 매입취소금액 - 수수료 + 예수금 - 차감<p/>
												<code>차감</code> : 기타 보류 금액<p/>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/settle/mcht/make/list" method="post">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="아이디">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm typeahead mchtName" name="mchtName" data-oper="lk" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 대표자</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="가맹점 대표자">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산예정일</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<!-- <input class="form-control form-control-inline input-medium date-picker now-date" size="16" type="text" name="stlDay" value="" /> -->
																	<input type="text" class="form-control now-date" name="stlStartDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="stlEndDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산주기</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="stlType" data-oper="eq">
																<option value="">전체</option>
																<option value="D+1">D+1</option>
																<option value="D+2">D+2</option>
																<option value="D+3">D+3</option>
																<option value="D+4">D+4</option>
																<option value="D+5">D+5</option>
																<option value="D+6">D+6</option>
																<option value="D+7">D+7</option>
																<option value="C+1">C+1</option>
																<option value="C+2">C+2</option>
																<option value="C+3">C+3</option>
																<option value="C+4">C+4</option>
																<option value="C+5">C+5</option>
																<option value="C+6">C+6</option>
																<option value="C+7">C+7</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산구분</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="settleSvc" data-oper="eq">
																<option value="">전체</option>
																<option value="일반">일반</option>
																<option value="충전정산">충전정산</option>
															</select>
														</div>
														<%-- <c:import url="/common/selectGrade.jsp" /> --%>
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
															<i class="fa fa-search" aria-hidden="true"></i> 정산 내역 생성
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult"></div>
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
	<iframe id="txtArea1" style="display:none"></iframe>
	<script type="text/javascript">
		/* gradeSelector('searchForm', '${CP_SESSION.grade}'); */
		$('#nav-settle-mcht').addClass('active');

	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>