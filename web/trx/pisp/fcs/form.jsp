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

<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white page-sidebar-fixed">
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
								<li><span>거래 관리</span><i class="fa fa-circle"></i></li>
								<li><span>지급대행 관리</span><i class="fa fa-circle"></i></li>
								<li><span>인증내역 조회</span></li>
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
									<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/trx/pisp/fcs/list"
									 method="post">
										<input type="hidden" data-reg="false" name="reason" value="지급대행 인증내역">
										<c:if test="${CP_SESSION.grade eq '본사'}">
										<input type="hidden" data-reg="false" name="thead" value="trxId:거래번호,mchtId:가맹점아이디,bankName:은행명,trackId:주문번호,fee:수수료,vanFee:원가,name:입금인,status:상태,resultCd:결과코드,resultMsg:결과메세지,resDay:수신일자,resTime:수신시간,regDate:기록일시">
										</c:if>
										<c:if test="${CP_SESSION.grade eq '가맹점'}">
										<input type="hidden" data-reg="false" name="thead" value="trxId:거래번호,mchtId:가맹점아이디,bankName:은행명,trackId:주문번호,fee:수수료,name:입금인,status:상태,resultCd:결과코드,resultMsg:결과메세지,resDay:수신일자,resTime:수신시간,regDate:기록일시">
										</c:if>
										<div class="form-body"> 
											<div class="row">
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">거래번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="trxId" data-oper="eq" placeholder="거래번호">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">가맹점아이디</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="eq" placeholder="가맹점아이디">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">가맹점명</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm mchtName" name="mchtName" data-oper="lk" placeholder="가맹점명">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">은행명</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm bankName" name="bankName" data-oper="lk" placeholder="은행명">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">주문번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="trackId" data-oper="eq" placeholder="주문번호">
													</div>
												</div>
												
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">상태</label>
													<select class="selectpicker col-lg-8" name="status" data-oper="eq">
														<option value="">---</option>
															<option value="성공">성공</option>
															<option value="실패">실패</option> 
															<option value="처리중">처리중</option>
														</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">기록일시</label>
													<div class=" col-lg-8">  
														<div class="input-group input-group-sm input-daterange">
															<input type="text" class="form-control now-date" name="regDay" value="" data-oper="ge">
															<span class="input-group-addon">~</span>
															<input type="text" class="form-control now-date" name="regDay" value="" data-oper="le">
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="form-actions nobg right">
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
	
		//2022.06.27 현재년도 기준 5년 이전 년도 선택 불가 추가
		var nowYear = new Date().getFullYear() - 5;
		
		$('.now-date').datepicker({
			format: 'yyyy-mm-dd',			// 날짜 포맷
			startDate: new Date(nowYear.toString())		// 5년 이전 년도 선택 불가
		});
	
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function () {
			searchForList();
		}, 100); //검색 실행
		$('#nav-trx').addClass('active');
	</script>
</body>

</html>