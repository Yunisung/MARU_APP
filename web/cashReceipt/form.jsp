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
									<li><span>현금영수증</span><i class="fa fa-circle"></i></li>
									<li><span>현금영수증 내역 조회</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body">도움말 내용</div>
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
									<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/cashReceipt/list" method="post">
										<div class="form-body">
											<div class="row">
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">현금영수증 아이디</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm mid" name="mid" data-oper="lk" placeholder="현금영수증 아이디" data-search="mid">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">현금영수증 승인 구분</label>
													<select class="selectpicker col-lg-8 assort" name="assort" data-oper="eq">
														<option value="">-- 승인 구분 --</option>
														<option value="0">승인</option>
														<option value="1">취소</option>
													</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">현금영수증 등록 구분</label>
													<select class="selectpicker col-lg-8 identityGb" name="identityGb" data-oper="eq">
														<option value="">-- 등록 구분 --</option>
														<option value="4">휴대폰 번호</option>
														<option value="1">국세청 현금영수증 카드</option>
														<option value="3">사업자 번호</option>
													</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">거래일시</label>
													<div class=" col-lg-8">
														<div class="input-group input-group-sm input-daterange" data-date-format="yyyyMMddhh24miss">
															<input type="text" class="form-control now-date" name="trDt" value="" data-oper="ge">
															<span class="input-group-addon">~</span>
															<input type="text" class="form-control now-date" name="trDt" value="" data-oper="le">
														</div>
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">주문번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm transNo" name="transNo" data-oper="lk" placeholder="주문번호" data-search="transNo">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">현금영수증 용도</label>
													<select class="selectpicker col-lg-8 purpose" name="purpose" data-oper="eq">
														<option value="">-- 용도 구분 --</option>
														<option value="0">소득공제</option>
														<option value="1">지출증빙</option>
													</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">승인번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm authNo" name="authNo" data-oper="lk" placeholder="승인번호" data-search="authNo">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">가맹점명</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="가맹점명" data-search="mchtId">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">주문자명</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm ordNm" name="ordNm" data-oper="lk" placeholder="주문자명" data-search="ordNm">
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
	<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-cash-receipt').addClass('active');
		
		function failPop(message) {
			bootbox.alert(message);
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>