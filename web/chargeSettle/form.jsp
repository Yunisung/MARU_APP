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
									<li><span>충전 정산</span><i class="fa fa-circle"></i></li>
                  					<li><span>거래내역조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/chargeSettle/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="충전정산 거래내역">
												<c:if test="${CP_SESSION.grade == '본사'}">
													<input type="hidden" data-reg="false" name="thead" value="trxDay:거래일자,trxTime:거래시간,name:가맹점명,mchtId:가맹점아이디,trxType:거래구분,trxUnit:거래유형,amount:입출금원금,fee:수수료,feeVat:수수료부가세,bankFee:은행수수료,netAmount:계정실출금액,balance:거래후잔액,trackId:주문번호,refId:참조번호,bankCd:은행코드,bankName:은행이름,account:계좌번호,holder:받는계좌예금주명,recordInfo:적요,summary:기재내용,regId:등록자">
												</c:if>
												<c:if test="${CP_SESSION.grade == '가맹점'}">
													<input type="hidden" data-reg="false" name="thead" value="trxDay:거래일자,trxTime:거래시간,name:가맹점명,mchtId:가맹점아이디,trxType:거래구분,trxUnit:거래유형,amount:입출금원금,fee:수수료,feeVat:수수료부가세,netAmount:계정실출금액,balance:거래후잔액,trackId:주문번호,refId:참조번호,bankCd:은행코드,bankName:은행이름,account:계좌번호,holder:받는계좌예금주명,recordInfo:적요,summary:기재내용,regId:등록자">
												</c:if>
												<div class="form-body">
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
														<c:if test="${CP_SESSION.grade eq '본사' }">
															<div class="form-group pg-form-group">
																<label class="control-label col-lg-4">가맹점ID</label>
																<div class="col-lg-8">
																	<input type="text" class="form-control input-sm mchtId typeahead" name="mchtId" data-oper="lk" placeholder="가맹점ID">
																</div>
															</div>
															<div class="form-group pg-form-group">
																<label class="control-label col-lg-4">가맹점명</label>
																<div class="col-lg-8">
																	<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="가맹점명">
																</div>
															</div>
														</c:if>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래종류</label>
															<select class="selectpicker col-lg-8" name="trxType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="입금">입금</option>
																	<option value="출금">출금</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래유형</label>
															<select class="selectpicker col-lg-8" name="trxUnit" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="신용카드정산">신용카드정산</option>
																	<option value="가상계좌정산">가상계좌정산</option>
																	<option value="펌뱅킹">펌뱅킹</option>
																	<option value="인증수수료">인증수수료</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="trxId" data-oper="eq" placeholder="거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">참조번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm refId" name="refId" data-oper="lk" placeholder="참조번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">주문번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm trackId" name="trackId" data-oper="lk" placeholder="주문번호">
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
		$('#nav-charge').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>