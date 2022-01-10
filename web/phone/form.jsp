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
									<li><span>휴대폰 결제 조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/phone/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="휴대폰결제내역">
												<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,trackId:주문번호,tmnId:터미널,prodType:상품유형,stlType:정산유형,status:승인구분,capType:매입/취소,rfdType:취소구분,rfdTrxId:취소 원거래번호,rfdRegDay:취소 원거래일자,stlStatus:정산구분,stlDay:정산예정일,van:van사,vanId:vanID,amount:결제금액,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT,stlAmount:지급예정액,stlVanRate:휴대폰결제 수수료율,stlVanFee:휴대폰결제 수수료,vanTrxId:VAN거래번호">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm trxId" name="trxId" data-oper="lk" placeholder="거래번호" data-search="trxId">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="가맹점ID" data-search="mchtId">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="가맹점명" data-search="name">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date" name="regDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="regDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">터미널ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm tmnId" name="tmnId" data-oper="lk" placeholder="터미널ID" data-search="tmnId">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인금액</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm amount" name="amount" data-oper="lk" placeholder="승인금액" data-search="amount">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인구분</label>
															<select class="selectpicker col-lg-8 status" name="status" data-oper="eq">
																<option value="">-- 승인 구분 --</option>
																<option value="승인">승인거래</option>
																<option value="취소">취소거래</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">주문자명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm payerName" name="payerName" data-oper="eq" placeholder="주문자명" data-search="payerName">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">유형</label>
															<select class="selectpicker col-lg-8 prodType" name="prodType" data-oper="eq">
																<option value="">-- 유형 선택 --</option>
																<option value="1">실물</option>
																<option value="2">컨텐츠</option>
															</select>
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
		$('#nav-phone-settle').addClass('active');
		
		function failPop(message) {
			bootbox.alert(message);
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>
</html>