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
									<li><span>기타거래</span><i class="fa fa-circle"></i></li>
                  					<li><span>VAN거래 조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/othTrns/van/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="VAN거래내역">
												<input type="hidden" data-reg="false" name="thead" value="reqDay:거래일,reqTime:거래시간,authCd:승인번호,trxId:거래번호,name:가맹점명,ceoName:가맹점 대표,trackId:주문번호,tmnId:단말기ID,cancelTrxId:취소거래번호,amount:금액,bin:BIN,last4:LAST4,issuer:발급사,acquirer:매입사,installment:할부,van:VAN">
												<c:if test="${FIX_SEARCH ne null}">
												<div class="form-body">	
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="mchtId" data-oper="eq" value="${SEARCH_MCHT_ID }">
															</div>
														</div>
													</div>
												</div>
												</c:if>
												<c:if test="${FIX_SEARCH eq null}">
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="authCd" data-oper="lk" placeholder="승인번호" data-search="test">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm typeahead mchtName" name="name" data-oper="lk" placeholder="가맹점명" data-search="mchtName">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래승인구분</label>
															<select class="selectpicker col-lg-8" name="trxType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="0">승인</option>
																	<option value="1">취소</option>
															</select>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<div class="col-lg-4" style="padding:0;">
															<!-- <select class="selectpicker col-lg-12" name="" id="date-selector" data-reg="false">
																<option value="reqDay" selected>거래일자</option>
															</select> -->
															<label class="control-label col-lg-12">거래일자</label>
															</div>
															<div class="col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date date-selector-target" name="reqDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date date-selector-target" name="reqDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인금액</label>
															<div class="col-lg-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm" name="amount" data-oper="ge" onkeydown="OnlyInput(event)">
																	<span class="input-group-addon" style="border-left: 0;border-right: 0;">~</span>
																	<input type="text" class="form-control input-sm" name="amount" data-oper="le" onkeydown="OnlyInput(event)">
																</div>
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
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">단말기 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="tmnId" data-oper="lk" placeholder="단말기 ID">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">카드 BIN</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" maxlength="6" name="bin" data-oper="eq" placeholder="6자리">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">카드뒤4자리</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" maxlength="4" name="last4" data-oper="eq" placeholder="4자리">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">매입사</label>
															<select class="selectpicker col-lg-8" name="acquirer" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="국민">국민</option>
																<option value="농협">농협</option>
																<option value="롯데">롯데</option>
																<option value="비씨">비씨</option>
																<option value="삼성">삼성</option>
																<option value="신한">신한</option>
																<option value="하나">하나</option>
																<option value="현대">현대</option>
																<option value="기타">기타</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">주문번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="trackId" data-oper="lk" placeholder="주문번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">VAN</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="van" data-oper="eq" placeholder="VAN">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">취소거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="cancelTrxId" data-oper="eq" placeholder="취소거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="trxId" data-oper="eq" placeholder="거래번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="mchtId" data-oper="eq" value=""  placeholder="가맹점 ID">
															</div>
														</div>
													</div>
													</div>
												</c:if>
												
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
		
		function OnlyInput(event){
			if((event.keyCode>=48) && (event.keyCode<=57)){ // 숫자
				true;
			} else if((event.keyCode>=96) && (event.keyCode<=105)){ // 숫자
				true;
			} else if((event.keyCode==8) || (event.keyCode==9)){ // 백스페이스 및 tab 키
				true;
			} else if((event.keyCode>=37) && (event.keyCode<=40)){ // 커서 키
				true;
			} else if(event.keyCode==46){ // delete 키
				true;
			} else if(event.keyCode==13){ // Enter 키
				true;
			} else {
				event.preventDefault();
			}
		}
		
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-oth').addClass('active');
	
		
		$('#date-selector').on('change', function() {
			$('.date-selector-target').attr('name', $(this).val());
		})
		
		

		
	</script>
	
</body>

</html>