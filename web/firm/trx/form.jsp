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
									<li><span>펌뱅킹 관리</span><i class="fa fa-circle"></i></li>
									<li><span>출금현황조회</span></li>
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
								<!-- BEGIN PAGE CONTENT INNER -->
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/firm/trx/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="출금현황조회">
												<input type="hidden" data-reg="false" name="thead" value="sendDate:송금일자,sendTime:송금시간,seqNo:전문번호,amount:송금금액,recvBankName:입금은행명,recvAccount:입금계좌,recvHolder:고객적요,recordInfo:통장적요,filler:거래번호,balance:잔액,recvDate:응답일자,recvTime:응답시간,resultMsg:결과메세지,procGb:처리상태,procType:프로세스,procId:프로세스ID">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">처리일자</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																<input type="text" class="form-control now-date" name="sendDate" value="" data-oper="ge">
																<span class="input-group-addon">~</span>
																<input type="text" class="form-control now-date" name="sendDate" value="${endDate}" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">프로세스ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="procId" data-oper="eq" placeholder="프로세스ID">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금금액</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="amount" data-oper="eq" placeholder="금액">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금계좌번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="recvAccount" data-oper="eq" placeholder="입금계좌번호">
															</div>
														</div>
														
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금은행</label>
															<select class="selectpicker col-lg-8" name="bankCd" data-oper="eq">
																<option value=""> -----&nbsp; </option>
																<c:forEach var="entry" items="${recvBank}" varStatus="status">
																	<option value="${entry.code }">${ entry.codeName}&nbsp;&nbsp;[${entry.code }]</option>
																</c:forEach>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">처리결과</label>
															<select class="selectpicker col-lg-8" name="procGb" data-oper="eq">
																<option value="">-----&nbsp;</option>
																	<option value="Y">지급완료</option>
																	<option value="N">지급실패</option>
																	<option value="X">통신장애</option>
																	<option value="I">지급처리중</option>
																	<option value="R">지급대기</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">통장적요</label>
															<select class="selectpicker col-lg-8" name="recordInfo" data-oper="eq">
																<option value="">-----&nbsp;</option>
																<option value="실시간">실시간</option>
																<option value="자동">자동</option>
																<option value="대행">대행</option>
																<option value="충전">충전</option>
																<option value="월렛">월렛</option>
																<option value="모계좌">모계좌</option>
																<option value="잔액이체">잔액이체</option>
																<option value="1원인증">1원인증</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">전문번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="seqNo" data-oper="eq" placeholder="전문번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="filler" data-oper="eq" placeholder="거래번호">
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
		$('#nav-firm').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>