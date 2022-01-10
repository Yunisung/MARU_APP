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
									<li><span>대출 정산</span><i class="fa fa-circle"></i></li>
                  					<li><span>대출정산내역조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/loanSettle/settleList/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="대출정산내역조회">
												<c:if test="${CP_SESSION.grade == '본사'}">
													<input type="hidden" data-reg="false" name="thead" value="loanId:대출ID,loanStlId:대출정산번호,stlId:가맹점정산번호,name:가맹점명,mchtId:가맹점ID,totAmt:매출합계,loanDeductAmt:대출정산차감금액,stlAmount:가맹점지급액,holdAmt:지급보류금액,payAmt:상환금액,paySession:납입회차,payCnt:납입횟수,delayCnt:연체횟수,trxDay:실지급일,conCnt:약정납입횟수,conAmt:약정상환금액,amount:대출금액,totPayAmt:누적상환금액,prepayAmt:중도상환금액,balance:대출잔액">
												</c:if>
												<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '지사'}">
													<input type="hidden" data-reg="false" name="thead" value="name:가맹점명,mchtId:가맹점ID,totAmt:매출합계,loanDeductAmt:대출정산차감금액,stlAmount:가맹점지급액,holdAmt:지급보류금액,payAmt:상환금액,paySession:납입회차,payCnt:납입횟수,delayCnt:연체횟수,trxDay:실지급일,conCnt:약정납입횟수,conAmt:약정상환금액,amount:대출금액,totPayAmt:누적상환금액,prepayAmt:중도상환금액,balance:대출잔액">
												</c:if>
												<c:if test="${CP_SESSION.grade == '가맹점'}">
													<input type="hidden" data-reg="false" name="thead" value="totAmt:매출합계,loanDeductAmt:대출정산차감금액,stlAmount:가맹점지급액,holdAmt:지급보류금액,payAmt:상환금액,paySession:납입회차,payCnt:납입횟수,delayCnt:연체횟수,trxDay:실지급일,conCnt:약정납입횟수,conAmt:약정상환금액,amount:대출금액,totPayAmt:누적상환금액,prepayAmt:중도상환금액,balance:대출잔액">
												</c:if>
												<div class="form-body">
													<div class="row">
														<c:if test="${CP_SESSION.grade ne '가맹점'}">
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
															<label class="control-label col-lg-4">실지급일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange">
																	<input type="text" class="form-control now-date" name="trxDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="trxDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<c:if test="${CP_SESSION.grade == '본사'}">
															<div class="form-group pg-form-group">
																<label class="control-label col-lg-4">대출ID</label>
																<div class="col-lg-8">
																	<input type="text" class="form-control input-sm mchtId typeahead" name="loanId" data-oper="lk" placeholder="대출ID">
																</div>
															</div>
															<div class="form-group pg-form-group">
																<label class="control-label col-lg-4">대출정산번호</label>
																<div class="col-lg-8">
																	<input type="text" class="form-control input-sm mchtId typeahead" name="loanStlId" data-oper="lk" placeholder="대출정산번호">
																</div>
															</div>
															<div class="form-group pg-form-group">
																<label class="control-label col-lg-4">가맹점정산번호</label>
																<div class="col-lg-8">
																	<input type="text" class="form-control input-sm mchtId typeahead" name="stlId" data-oper="lk" placeholder="가맹점정산번호">
																</div>
															</div>
														</c:if>
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
		$('#nav-loan').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>