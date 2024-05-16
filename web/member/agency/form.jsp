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
									<li><span>멤버관리</span><i class="fa fa-circle"></i></li>
									<li><span>에이전시조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/member/agency/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="에이전시 리스트">
												<input type="hidden" data-reg="false" name="thead" value="agencyId:에이전시ID,name:에이전시,status:상태,bizType:업종,bizCategory:업태,distId:대행사ID,idType:사업자(주민)번호 구분,identity:사업자(주민)번호,email:이메일,tel1:연락처,tel2:연락처2,fax:팩스,zip:우편번호,addr1:주소,addr2:상세주소,ceoName:대표자,ceoIdentity:대표자 주민번호,ceoPhone:대표자 핸드폰,ceoTel:대표자 연락처,ceoZip:대표자 우편번호,ceoAddr1:대표자 주소,ceoAddr2:대표자 상세주소,managerName:관리자,managerPhone:관리자 연락처,bankName:은행명,account:계좌번호,accntHolder:예금주명,regDate:생성일시">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">아이디</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm agencyId" name="agencyId" data-oper="eq" placeholder="아이디">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">에이전시명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">사업자(주민)번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm identity" name="identity" data-oper="eq" placeholder="사업자(주민)번호">
															</div>
														</div>
													</div>
													<div class="row search-opt">
														
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">전화번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="tel1" data-oper="lk" placeholder="대표전화번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">대표자이름</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="대표자이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">담당자이름</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="managerName" data-oper="lk" placeholder="담당자이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">업종</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm bizType" name="bizType" data-oper="lk" placeholder="업종">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">업태</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm bizCategory" name="bizCategory" data-oper="lk" placeholder="업태">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">상태</label>
															<select class="selectpicker col-lg-8 col-xs-12 btn-sm" name="status" data-oper="eq">
																<option value="">상태</option>
																<option value="예비">예비</option>
																<option value="사용">사용</option>
																<option value="중지">중지</option>
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
		searchForList();
		$('#nav-member').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>