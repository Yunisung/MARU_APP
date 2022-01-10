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
                  <li><span>지급보류</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/settle/mcht/hold/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="지급보류내역">
												<input type="hidden" data-reg="false" name="thead" value="holdId:보류번호,holdStlId:보류정산번호,stlDay:정산예정일,status:상태,capId:매입번호,lastStatus:보류원인,authCd:승인번호,stlId:정산번호,amount:매입금액,stlAmount:정산금액,mchtId:가맹점ID,mchtName:가맹점,trxDate:거래일자,regDate:생성일자">
												
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
																<input type="text" class="form-control input-sm mchtName" name="mchtName" data-oper="lk" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 대표자</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="가맹점 대표자">
															</div>
														</div>
													</div>
													<div class="row">	
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산예정일</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date" name="stlDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="stlDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">상태</label>
															<select class="selectpicker col-lg-8 status" name="status" data-oper="eq">
																<option value="">-- 전체 --</option>
																<option value="보류">보류</option>
																<option value="반환요청">반환요청</option>
																<option value="반환완료">반환완료</option>
															</select>
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
	<script type="text/javascript">
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		$('#nav-settle-mcht').addClass('active');
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>