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
								<li><span>거래관리</span><i class="fa fa-circle"></i></li>
								<li><span>가상계좌 거래내역 조회</span></li>
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
									<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/vact/trx/list"
									 method="post">
										<input type="hidden" data-reg="false" name="reason" value="가상계좌 거래내역">
										<c:if test="${CP_SESSION.grade eq '본사'}">
											<input type="hidden" data-reg="false" name="thead" value="vactId:거래번호,issueId:발행번호,mchtId:가맹점ID,mchtName:가맹점,trackId:거래추적번호,amount:금액,issuerBank:가상계좌발행은행,account:가상계좌번호,seqNo:전문번호,vactType:발행타입,sender:보낸사람,trxType:거래타입,rootVactId:원거래번호,settleTarget:정산대상,stlType:정산주기,stlDay:정산예정일,stlFee:가맹점수수료,stlFeeVat:가맹점수수료VAT,stlDistFee:대행사수수료,stlDistFeeVat:대행사수수료VAT,stlAgencyFee:에이전시수수료,stlAgencyFeeVat:에이전시수수료VAT,stlSalesFee:지사수수료,stlSalesFeeVat:지사수수료VAT,vanFee:은행수수료,trxDay:거래일자,trxTime:거래시간">
										</c:if>
										<c:if test="${CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사' || CP_SESSION.grade eq '가맹점' || CP_SESSION.grade eq '하위가맹점'}">
											<input type="hidden" data-reg="false" name="thead" value="vactId:거래번호,issueId:발행번호,mchtId:가맹점ID,mchtName:가맹점,trackId:거래추적번호,amount:금액,issuerBank:가상계좌발행은행,account:가상계좌번호,seqNo:전문번호,vactType:발행타입,sender:보낸사람,trxType:거래타입,trxDay:거래일자,trxTime:거래시간,stlDay:정산예정일,stlFee:가맹점수수료,stlFeeVat:가맹점수수료VAT">
										</c:if>
										<div class="form-body">
											<div class="row">
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">거래번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="vactId" data-oper="eq" placeholder="거래번호">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">계좌번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="account" data-oper="eq" placeholder="계좌번호">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">발행번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="issueId" data-oper="eq" placeholder="발행번호">
													</div>
												</div>
												<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사'}">
													<div class="form-group pg-form-group">
														<label class="control-label col-lg-4">가맹점 ID</label>
														<div class="col-lg-8">
															<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="eq" placeholder="가맹점 ID">
														</div>
													</div>
													
													<div class="form-group pg-form-group">
														<label class="control-label col-lg-4">가맹점명</label>
														<div class="col-lg-8">
															<input type="text" class="form-control input-sm mchtName" name="mchtName" data-oper="lk" placeholder="가맹점명">
														</div>
													</div>
												</c:if>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">거래추적번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="trackId" data-oper="eq" placeholder="거래추적번호">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">금액</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="amount" data-oper="eq" placeholder="승인금액">
													</div>
												</div>

												<div class="form-group pg-form-group">
													<div class="col-lg-4" style="padding:0;">
													<select class="selectpicker col-lg-12" name="" id="date-selector" data-reg="false">
														<option value="trxDay" selected>거래일자</option>
														<option value="stlDay">정산예정일</option>
													</select>
													</div>
													<div class="col-lg-8">
														<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
															<input type="text" class="form-control now-date date-selector-target" name="trxDay" value="" data-oper="ge">
															<span class="input-group-addon">~</span>
															<input type="text" class="form-control now-date date-selector-target" name="trxDay" value="" data-oper="le">
														</div>
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">보낸사람</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="sender" data-oper="lk" placeholder="보낸사람">
													</div>
												</div>
												<c:if test="${CP_SESSION.grade eq '본사'}">
													<div class="form-group pg-form-group">
														<label class="control-label col-lg-4">정산대상</label>
															<select class="selectpicker col-lg-8" name="settleTarget" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="Y">Y</option>
																<option value="N">N</option>
															</select>
													</div>
													<div class="form-group pg-form-group">
														<label class="control-label col-lg-4">정산주기</label>
														<select class="selectpicker col-lg-8" name="stlType" data-oper="eq">
															<option value="">-- 전체 -- </option>
															<option value="D+0">D+0</option>
															<option value="A+1">A+1</option>
															<option value="D+1">D+1</option>
															<option value="D+2">D+2</option>
															<option value="D+3">D+3</option>
															<option value="D+4">D+4</option>
															<option value="D+5">D+5</option>
															<option value="D+6">D+6</option>
															<option value="D+7">D+7</option>
															<option value="C+0">C+0</option>
															<option value="C+1">C+1</option>
															<option value="C+2">C+2</option>
															<option value="C+3">C+3</option>
															<option value="C+4">C+4</option>
															<option value="C+5">C+5</option>
															<option value="C+6">C+6</option>
															<option value="C+7">C+7</option>
															<option value="B+1">B+1</option>
														</select>
													</div>
												</c:if>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">거래타입</label>
													<select class="selectpicker col-lg-8" name="trxType" data-oper="eq">
														<option value="">-- 전체 -- </option>
														<option value="입금">입금</option>
														<option value="취소">취소</option>
													</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">가상계좌발행은행</label>
													<select class="selectpicker col-lg-8" name="issuerBank" data-oper="eq">
														<option value="">-- 전체 -- </option>
														<option value="경남은행">경남은행</option>
														<option value="수협은행">수협은행</option>
														<option value="케이뱅크">케이뱅크</option>
														<option value="부산은행">부산은행</option>
														<option value="우리은행">우리은행</option>
													</select>
												</div>
											</div>
											<c:if test="${CP_SESSION.grade eq '본사'}">
												<div class="row search-opt">
													<c:import url="/common/selectGrade.jsp" />
													<input type="hidden" id="grade_search" name="parentId" data-oper="eq" value="" />
												</div>
											</c:if>
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
		
		$('.date-selector-target').datepicker({
			format: 'yyyy-mm-dd',			// 날짜 포맷
			startDate: new Date(nowYear.toString())		// 5년 이전 년도 선택 불가
		});
	
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function () {
			searchForList();
		}, 100); //검색 실행
		$('#nav-trx').addClass('active');
		
		$('#date-selector').on('change', function() {
			$('.date-selector-target').attr('name', $(this).val());
		})
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>