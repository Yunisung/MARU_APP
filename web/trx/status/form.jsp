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
                  <li><span>정산상태 관리(리스크)</span></li>
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
												1.	중복 <br/>
													거래처리에서 10분내 거래 중 동일날짜/동일가맹점/BIN/LAST4/금액이 동일한 승인 거래에 대해서 중복 거래로 설정 <br/>
													BIN/LAST4 등 카드 정보가 없는 거래는 제외됩니다.<br/>
													중복 거래 이면서 고액/건한도 등이 해당되는 거래는 “중복: 으로 설정됨을 말씀드립니다. 우선권은 중복입니다.<br/><br/>
												2.	건한도<br/>
													건한도는 가맹점의 1회 한도에 따라 적용됩니다. 단. “0”으로 관리자가 설정하면 이 가맹점은 제외됩니다.<br/><br/>
												3.	고액 <br/>
													가맹점에 설정된 고액 거래 기준 금액에 따라 설정됩니다. 단. “0”으로 관리자가 설정하면 이 가맹점은 제외됩니다.<br/><br/>
												4.	최소금액<br/>
													1005원 미만 . 즉 1004원 이하 거래에 대해서는 최소금액으로 표기됩니다.<br/><br/>
												5.	야간 할부 <br/>
													거래 기준 시간에 의하여 동작하며. 거래 기준시간 00:00:01 ~ 05:59:59 에 해당하는 거래만 야간 할부 기준이 적용됩니다.<br/><br/>
												6.	위험 <br/>
													동일날짜/동일가맹점/동일카드 합계 금액이 100만원 이상인 거래에 대해서 위험 거래로 표기<br/>
													단, BIN/LAST4  등 카드 정보가 없는 거래는 제외됩니다.<br/><br/>
												7.	RISK 에 해당하는 거래에 대한 우선 순위<br/>
													현재 시스템에는 2가지 이상의 RISK 발견 시 아래의 우선순위에 따라 표기됨을 말씀드립니다.<br/>
													중복 > 위험 > 야간할부 > 최소금액 > 건한도 > 고액 <br/><br/>
												8.	취소 시 <br/>
													취소 시에는  RISK 거래가 해지됩니다. 
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/trx/status/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="리스크 관리내역">
												<c:if test="${CP_SESSION.grade == '본사'}">
												<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,van:밴사,stlType:정산유형,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT,stlId:정산ID,stlDistFee:대행사수수료,stlDistRate:대행사수수료율,stlDistId:대행사정산ID,stlAgencyFee:에이전시수수료,stlAgencyRate:에이전시수수료율,stlAgencyId:에이전시정산ID,vanId:CPID,vanTrxId:VAN거래번호,vanStatus:입금상태,stlVanFee:입금수수료,stlVanRate:입금수수료율,stlVanDay:실입급일,benefit:수익,taxId:TAX,distId:대행사ID,agencyId:에이전시ID">
												</c:if>
												<c:if test="${CP_SESSION.grade == '대행사'}">
												<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,van:밴사,stlType:정산유형,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT,stlId:정산ID,taxId:TAX,distId:대행사ID,agencyId:에이전시ID">
												</c:if>
												<c:if test="${CP_SESSION.grade == '에이전시'}">
												<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,van:밴사,stlType:정산유형,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT">
												</c:if>
												<c:if test="${CP_SESSION.grade == '가맹점'}">
												<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,van:밴사,stlType:정산유형,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT">
												</c:if>
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">승인번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm authCd" name="authCd" data-oper="lk" placeholder="승인번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">매입번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="capId" data-oper="eq" placeholder="매입번호">
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
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name" name="name" data-oper="lk" placeholder="가맹점명">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 대표자</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="가맹점 대표자">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="가맹점 ID">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">터미널 ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm tmnId" name="tmnId" data-oper="lk" placeholder="터미널 ID">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산 상태</label>
															<select class="selectpicker col-lg-8" name="stlStatus" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="정산대기">정산대기</option>
																<option value="정산확정">정산확정</option>
																<option value="정산보류">정산보류</option>
																<option value="정산완료">정산완료</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">리스크</label>
															<select class="selectpicker col-lg-8" name="risk" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="건한도">건한도</option>
																<option value="중복">중복</option>
																<option value="고액">고액</option>
																<option value="최소금액">최소금액</option>
																<option value="야간할부">야간할부</option>
																<option value="1일중복">1일중복</option>
																<option value="주간할부">주간할부</option>
																<option value="야간건한도">야간건한도</option>
																<option value="위험">위험</option>
																<option value="관리자 설정">관리자 설정</option>
															</select>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">금액(이상)</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="amount" data-oper="lt" placeholder="금액">
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
													<c:if test="${CP_SESSION.grade == '본사'}">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">PG/선정산</label>
															<select class="selectpicker col-lg-8" name="pgOrFactoring" data-reg="false">
																	<option value="" selected>-- 전체 -- </option>
																	<option value="선정산">선정산</option>
																	<option value="PG">PG</option>
															</select>
															<input id="pgOrFactoring" type="hidden" name="distId" data-oper="eq" data-reg="false" value="00">
														</div>
													</div>
													</c:if>
													<div class="row search-opt">
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
									<div class="portlet light portlet-form" id="searchResult"></div>
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

		
		$('select[name="pgOrFactoring"]').on('change', function() {
			var hidden = $('#pgOrFactoring');
			console.log('pgOrFactoring', $(this).val());
			hidden.data('reg', 'true');
			hidden.attr('data-reg', 'true');
			hidden.val('00');
			if($(this).val() == '선정산') {
				hidden.data('oper', 'eq');
			} else if($(this).val() == 'PG') {
				hidden.data('oper', 'ne');
			} else {
				hidden.data('reg', 'false');
				hidden.attr('data-reg', 'false');
			}
		});
		
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>