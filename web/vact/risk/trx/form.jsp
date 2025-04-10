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
								<li><span>가상계좌 관리</span><i class="fa fa-circle"></i></li>
								<li><span>리스크 관리</span><i class="fa fa-circle"></i></li>
								<li><span>거래내역</span></li>
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
									<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/vact/risk/trx/list"
									 method="post">
										<input type="hidden" data-reg="false" name="reason" value="가상계좌 거래내역">
										<c:if test="${CP_SESSION.grade eq '본사'}">
											<input type="hidden" data-reg="false" name="thead" value="vactId:거래번호,mchtId:가맹점ID,mchtName:가맹점,amount:금액,codeName:가상계좌발행은행,account:가상계좌번호,sender:보낸사람,trxType:거래타입,trxDay:거래일자,trxTime:거래시간,withdrawBankName:은행이름,decWithdrawAccount:계좌번호,sender:예금주명">
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
													<label class="control-label col-lg-4">가상계좌번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="account" data-oper="eq" placeholder="가상계좌번호">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">가상계좌발행은행</label>
													<select class="selectpicker col-lg-8" name="bankCd" data-oper="eq">
														<option value="">-- 전체 -- </option>
														<option value="048">신협은행</option>
														<option value="039">경남은행</option>
														<option value="034">광주은행</option>
														<option value="007">수협은행</option>
														<option value="089">케이뱅크</option>
														<option value="032">부산은행</option>
														<option value="020">우리은행</option>
													</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">출금계좌번호</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="withdrawAccount" data-oper="eq" placeholder="출금계좌번호">
													</div>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">출금계좌은행</label>
													<div class="col-lg-8">
														<input type="text" class="form-control input-sm" name="withdrawBankName" data-oper="lk" placeholder="출금계좌은행">
													</div>
												</div>
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
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">거래타입</label>
													<select class="selectpicker col-lg-8" name="trxType" data-oper="eq">
														<option value="">-- 전체 -- </option>
														<option value="입금">입금</option>
														<option value="취소">취소</option>
													</select>
												</div>
												<div class="form-group pg-form-group">
													<label class="control-label col-lg-4">리스크</label>
													<select class="selectpicker col-lg-8" name="risk" data-oper="eq">
														<option value="">-- 전체 -- </option>
														<option value="출금계좌 입금 횟수 초과">출금계좌 입금 횟수 초과</option>
														<option value="출금계좌 입금 1회 한도 초과">출금계좌 입금 1회 한도 초과</option>
														<option value="출금계좌 입금 1일 한도 초과">출금계좌 입금 1일 한도 초과</option>
														<option value="가상계좌 입금 횟수, 금액 초과">가상계좌 입금 횟수, 금액 초과</option>
														<option value="출금계좌 입금 횟수, 금액 초과">출금계좌 입금 횟수, 금액 초과</option>
													</select>
												</div>

											</div>
<%--											<c:if test="${CP_SESSION.grade eq '본사'}">--%>
<%--												<div class="row search-opt">--%>
<%--													<c:import url="/common/selectGrade.jsp" />--%>
<%--													<input type="hidden" id="grade_search" name="parentId" data-oper="eq" value="" />--%>
<%--												</div>--%>
<%--											</c:if>--%>
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