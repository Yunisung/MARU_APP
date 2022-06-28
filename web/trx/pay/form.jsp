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
									<li><span>거래관리</span><i class="fa fa-circle"></i></li>
                  <li><span>승인내역조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/trx/pay/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="승인내역">
												<c:if test="${CP_SESSION.grade eq '본사'}">
												<input type="hidden" data-reg="false" name="thead" value="trxId:거래번호,mchtId:가맹점ID,name:가맹점,tmnId:터미널ID,trackId:거래추적번호,payerName:구매자,payerEmail:구매자Email,payerTel:구매자전화번호,amount:금액,bin:BIN,last4:카드뒷자리4,status:거래상태,issuer:매입사,installment:할부,reqDay:요청일,reqTime:요청시간,authCd:승인번호,resultCd:결과코드,resultMsg:결과메시지,van:VAN,vanId:VANID,vanTrxId:VAN추적번호,prodName:상품명,qty:수량,description:상품설명/배달주소">
												</c:if>
												<c:if test="${CP_SESSION.grade ne '본사'}">
												<input type="hidden" data-reg="false" name="thead" value="trxId:거래번호,mchtId:가맹점ID,name:가맹점,tmnId:터미널ID,trackId:거래추적번호,payerName:구매자,payerEmail:구매자Email,payerTel:구매자전화번호,amount:금액,bin:BIN,last4:카드뒷자리4,status:거래상태,issuer:매입사,installment:할부,reqDay:요청일,reqTime:요청시간,authCd:승인번호,resultCd:결과코드,resultMsg:결과메시지,van:VAN,vanId:VANID,vanTrxId:VAN추적번호,prodName:상품명">
												</c:if>
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="authCd" data-oper="eq" placeholder="승인번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtName" name="name" data-oper="lk" placeholder="가맹점명">
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
															<label class="control-label col-lg-4">거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="trxId" data-oper="eq" placeholder="거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인금액</label>
															<div class="col-lg-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm" name="amount" data-oper="ge">
																	<span class="input-group-addon" style="border-left: 0;border-right: 0;">~</span>
																	<input type="text" class="form-control input-sm" name="amount" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">카드뒤4자리</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="last4" data-oper="eq" placeholder="카드번호">
															</div>
														</div>
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
													</div>
													<div class="row search-opt">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">이름</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="payerName" data-oper="lk" placeholder="지불자이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">이메일</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="payerEmail" data-oper="lk" placeholder="이메일">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">전화번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="payerTel" data-oper="lk" placeholder="전화번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">PG 거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="vanTrxId" data-oper="lk" placeholder="PG 거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">일시불/할부</label>
															<select class="selectpicker col-lg-8" name="installment" data-oper="in">
																<option value="">-- 전체 -- </option>
																<option value="'00'">일시불</option>
																<option value="'01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','36','48','60'">할부</option>
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
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-trx').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>