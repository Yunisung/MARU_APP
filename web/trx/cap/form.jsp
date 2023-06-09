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
                  <li><span>매입현황조회</span></li>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/trx/cap/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="매입거래내역">
												<%-- <c:if test="${CP_SESSION.grade == '본사'}">
												<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,cardType:신용/체크,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,vanOutDay:입금예정일,van:밴사,stlType:정산유형,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT,stlId:정산ID,stlDistFee:대행사수수료,stlDistRate:대행사수수료율,stlDistId:대행사정산ID,stlAgencyFee:에이전시수수료,stlAgencyRate:에이전시수수료율,stlAgencyId:에이전시정산ID,vanId:CPID,vanTrxId:VAN거래번호,vanStatus:입금상태,stlVanFee:입금수수료,stlVanRate:입금수수료율,stlVanDay:실입급일,stlVanAmount:입금예정액,benefit:수익,taxId:TAX,distId:대행사ID,agencyId:에이전시ID">
												</c:if> --%>
												<c:if test="${CP_SESSION.grade == '본사'}">
													<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,salesName:지사,trackId:주문번호,tmnId:터미널,tmnDesc:취급품목,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,cardType:신용/체크,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,van:밴사,vanId:vanID,stlType:정산유형,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT,stlAmount:지급예정액,stlDistFee:대행사수수료,stlDistRate:대행사수수료율,stlDiffDistFee:대행사차액정산수수료,stlDiffDistRate:대행사차액정산수수료율,stlAgencyFee:에이전시수수료,stlAgencyRate:에이전시수수료율,stlDiffAgencyFee:에이전시차액정산수수료,stlDiffAgencyRate:에이전시차액정산수수료율,stlSalesFee:지사수수료,stlSalesRate:지사수수료율,stlDiffSalesFee:지사차액정산수수료,stlDiffSalesRate:지사차액정산수수료율,vanStatus:입금상태,stlVanFee:입금수수료,stlVanRate:입금수수료율,stlVanDay:실입급일,stlVanAmount:입금예정액,stlDiffVanDay:차액정산입금예정일,stlDiffType:본사영중소구분,stlDiffVanType:카드사영중소구분,stlDiffVanCardType:카드사영중소 신용/체크,stlDiffStatus:차액정산상태,stlDiffResultMsg:차액정산결과,stlDiffAmt:본사차액정산액,stlDiffVanAmt:카드사차액정산액,benefit:수익,vanTrxId:VAN거래번호">
												</c:if>
												<c:if test="${CP_SESSION.grade == '대행사'}">
													<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,salesName:지사,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,cardType:신용/체크,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,vanOutDay:입금예정일,van:밴사,stlDistFee:대행사수수료,stlDistRate:대행사수수료율,stlDiffDistFee:대행사차액정산수수료,stlDiffDistRate:대행사차액정산수수료율,stlAgencyFee:에이전시수수료,stlAgencyRate:에이전시수수료율,stlDiffAgencyFee:에이전시차액정산수수료,stlDiffAgencyRate:에이전시차액정산수수료율,stlSalesFee:지사수수료,stlSalesRate:지사수수료율,stlDiffSalesFee:지사차액정산수수료,stlDiffSalesRate:지사차액정산수수료율,stlType:가맹점정산유형,stlRate:가맹점수수료율,stlFee:가맹점수수료,stlFeeVat:가맹점수수료VAT,stlId:가맹점정산ID,taxId:TAX,distId:대행사ID,agencyId:에이전시ID,vanTrxId:VAN거래번호">
												</c:if>
												<c:if test="${CP_SESSION.grade == '에이전시'}">
													<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,salesName:지사,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,cardType:신용/체크,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,vanOutDay:입금예정일,van:밴사,stlAgencyFee:에이전시수수료,stlAgencyRate:에이전시수수료율,stlDiffAgencyFee:에이전시차액정산수수료,stlDiffAgencyRate:에이전시차액정산수수료율,stlSalesFee:지사수수료,stlSalesRate:지사수수료율,stlDiffSalesFee:지사차액정산수수료,stlDiffSalesRate:지사차액정산수수료율,stlType:가맹점정산유형,stlRate:가맹점수수료율,stlFee:가맹점수수료,stlFeeVat:가맹점수수료VAT,stlId:가맹점정산ID,taxId:TAX,distId:대행사ID,agencyId:에이전시ID,vanTrxId:VAN거래번호">
												</c:if>
												<c:if test="${CP_SESSION.grade == '지사'}">
													<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,distName:대행사,agencyName:에이전시,salesName:지사,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,payOutDay:실지급일,cardType:신용/체크,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,stlDay:정산예정일,vanOutDay:입금예정일,van:밴사,stlSalesFee:지사수수료,stlSalesRate:지사수수료율,stlDiffSalesFee:지사차액정산수수료,stlDiffSalesRate:지사차액정산수수료율,stlType:가맹점정산유형,stlRate:가맹점수수료율,stlFee:가맹점수수료,stlFeeVat:가맹점수수료VAT,stlAmount:지급예정액,stlId:가맹점정산ID,taxId:TAX,distId:대행사ID,agencyId:에이전시ID,vanTrxId:VAN거래번호">
												</c:if>
												<c:if test="${CP_SESSION.grade == '가맹점'}">
													<input type="hidden" data-reg="false" name="thead" value="regDay:거래일,regTime:거래시간,authCd:승인번호,capId:매입번호,trxId:거래번호,mchtId:가맹점ID,name:가맹점명,trackId:주문번호,tmnId:터미널,capType:매입/취소,rfdType:취소구분,rootTrxId:취소 원거래번호,rootTrxDay:취소 원거래일자,amount:매입금액,bin:BIN,last4:LAST4,stlStatus:정산상태,cardType:신용/체크,issuer:발급사,acquirer:매입사,risk:리스크,installment:할부,van:밴사,stlType:정산유형,stlRate:수수료율,stlFee:수수료,stlFeeVat:수수료VAT,taxId:TAX,distId:대행사ID,agencyId:에이전시ID,vanTrxId:VAN거래번호">
												</c:if>
												<c:if test="${FIX_SEARCH ne null}">
												<div class="form-body">	
													<div class="row">
														<c:if test="${SEARCH_STL_VAN_DAY ne null}">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금예정일</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="stlVanDay" data-oper="eq" value="${SEARCH_STL_VAN_DAY }">
															</div>
														</div>
														</c:if>
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
															<label class="control-label col-lg-4">매입번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="capId" data-oper="eq" placeholder="매입번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm typeahead mchtName" name="name" data-oper="lk" placeholder="가맹점명" data-search="mchtName">
															</div>
														</div>
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<div class="col-lg-4" style="padding:0;">
															<select class="selectpicker col-lg-12" name="" id="date-selector" data-reg="false">
																<option value="regDay" selected>거래일자</option>
																<option value="payOutDay">지급일</option>
																<option value="stlDay">정산예정일</option>
																<option value="stlVanDay">입금예정일</option>
															</select>
															</div>
															<div class="col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<%--<c:if test="${CP_SESSION.grade != '본사'}">
																		<input type="text" class="form-control date-selector-target from" name="regDay" value="" data-oper="ge" readonly="readonly" style="background-color:white">
																		<span class="input-group-addon">~</span>
																		<input type="text" class="form-control date-selector-target to" name="regDay" value="" data-oper="le" readonly="readonly" style="background-color:white">
																	</c:if>
																	<c:if test="${CP_SESSION.grade == '본사'}">
																		<input type="text" class="form-control date-selector-target now-date" name="regDay" value="" data-oper="ge" readonly="readonly" style="background-color:white">
																		<span class="input-group-addon">~</span>
																		<input type="text" class="form-control date-selector-target now-date" name="regDay" value="" data-oper="le" readonly="readonly" style="background-color:white">
																	</c:if>--%>
																	<input type="text" class="form-control date-selector-target now-date" name="regDay" value="" data-oper="ge" readonly="readonly" style="background-color:white">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control date-selector-target now-date" name="regDay" value="" data-oper="le" readonly="readonly" style="background-color:white">
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
															<label class="control-label col-lg-4">터미널 번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="tmnId" data-oper="lk" placeholder="터미널번호">
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
															<label class="control-label col-lg-4">매입구분</label>
															<select class="selectpicker col-lg-8" name="capType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="매입">매입</option>
																	<option value="매입취소">매입취소</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산상태</label>
															<select class="selectpicker col-lg-8" name="stlStatus" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="정산대기">정산대기</option>
																	<option value="정산확정">정산확정</option>
																	<option value="정산보류">정산보류</option>
																	<option value="정산완료">정산완료</option>
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
													</div>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">일시불/할부</label>
															<select class="selectpicker col-lg-8" name="installment" data-oper="in">
																<option value="">-- 전체 -- </option>
																<option value="'00'">일시불</option>
																<option value="'01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','36','48','60'">할부</option>
															</select>
														</div>
														<c:if test="${CP_SESSION.grade == '본사'}">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">PG/선정산</label>
															<select class="selectpicker col-lg-8" name="pgOrFactoring" data-reg="false">
																	<option value="" selected>-- 전체 -- </option>
																	<option value="선정산">선정산</option>
																	<option value="PG">PG</option>
															</select>
															<input id="pgOrFactoring" type="hidden" name="distId" data-oper="eq" data-reg="false" value="00">
														</div>
														</c:if>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">신용/체크</label>
															<select class="selectpicker col-lg-8" name="cardType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="신용">신용</option>
																	<option value="체크">체크</option>
																</select>
														</div>
													</div>
													
													<div class="row search-opt">
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
														<c:if test="${CP_SESSION.grade == '본사'}">
														<div class="form-group pg-form-group formOption">
															<label class="control-label col-lg-4">Option</label>
															<select class="selectpicker col-lg-8 formOptionSelect" data-reg="false">
																	<option value="">-- 초기화 -- </option>
																	<option value="risk||eq|true">리스크없는거래</option>
																	<option value="risk||ne|true">리스크있는거래</option>
																	<option value="bin||eq|true">카드번호없은거래</option>
															</select>
														</div>
														</c:if>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점 대표자</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="가맹점 대표자">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">VAN</label>
															<select class="selectpicker col-lg-8 van" name="van" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<c:forEach items="${CP_SESSION.vanList }" var="list">
																	<option value="${list.van}">${list.van}</option>
																</c:forEach>
															</select>
														</div>
														<c:if test="${CP_SESSION.grade == '본사'}">
															<div class="form-group pg-form-group">
																<label class="control-label col-sm-4">VAN ID</label>
																<select name="vanId" class="selectpicker col-lg-8 vanId" data-oper="eq">
																	<option value="">-- 전체 -- </option>
																</select>
															</div>
														</c:if>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">취소구분</label>
															<select class="selectpicker col-lg-8" name="capType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																<option value="전체">전체</option>
																<option value="부분">부분</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">취소원거래번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="rootTrxId" data-oper="eq" placeholder="취소원거래번호">
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
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">입금상태</label>
															<select class="selectpicker col-lg-8" name="vanStatus" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="입금대기">입금대기</option>
																	<option value="입금완료">입금완료</option>
																</select>
														</div>
														
														<c:import url="/common/selectGrade.jsp" />
														<input type="hidden" id="grade_search" name="parentId" data-oper="eq" value=""/>
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
	
		//2022.06.27 현재년도 기준 5년 이전 년도 선택 불가 추가
		var nowYear = new Date().getFullYear() - 5;
		
		$('.date-selector-target').datepicker({
			format: 'yyyy-mm-dd',			// 날짜 포맷
			startDate: new Date(nowYear.toString())		// 5년 이전 년도 선택 불가
		});
	
		var rangeDate = 7; // set limit day
		var sdate, edate;
		
		$('.from').datepicker({
			format: 'yyyy-mm-dd',
			autoclose:true,
			language:'ko',
			ignoreReadonly: true
		}).on('changeDate', function(selected){
			var stxt = $('.from').val().split("-");
            stxt[1] = stxt[1] - 1;
	        sdate = new Date(stxt[0], stxt[1], stxt[2]);
	        edate = new Date(stxt[0], stxt[1], stxt[2]);
	        edate.setDate(sdate.getDate() + rangeDate);
	        $('.to').datepicker('setEndDate', edate);
		});
		
		$('.to').datepicker({
			format: 'yyyy-mm-dd',
			autoclose:true,
			language:'ko',
			ignoreReadonly: true
		});
		
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
		$('#nav-trx').addClass('active');
		
		$('.formOptionSelect').on('change',function(e) {
			var valR = $( ".formOptionSelect option:selected" ).val();
			var wrapper = $(".formOption");
			if(valR !=''){	
				var dtv = valR.split('|');
				$("#formOptionInput").remove();
				$(wrapper).append('<input type="hidden" class="form-control" id="formOptionInput" name="'+dtv[0]+'" value="'+dtv[1]+'" data-oper="'+dtv[2]+'" data-empty="'+dtv[3]+'">');
				
			}else{
				$("#formOptionInput").remove();
			}
			
		});
		
		$('#date-selector').on('change', function() {
			$('.date-selector-target').attr('name', $(this).val());
		})
		
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

		<c:if test="${CP_SESSION.grade == '본사'}">
		$('.selectpicker.van').on('change', function(){
		    var selected = $(this).find("option:selected").val();
		    if(selected) {
		    	$.get("/mcht/van/select/" + selected, function(data, status){
		    		data = jQuery.parseJSON(data);
		    		$(".selectpicker.vanId").html('<option value="">-- 전체 -- </option>').selectpicker('refresh');
		    		if(data.length > 0) {
		    			$.each(data, function(index, val) {
		    				$(".selectpicker.vanId").append('<option value="'+val.vanId+'">('+val.vanId + ') '+ val.name+'</option>');
							});
							$(".selectpicker.vanId").selectpicker('refresh');
			      } else {
			      	bootbox.alert('사용할 수 있는 VAN ID가 없습니다.');
			      }
			    }); 
		    }
		});
		</c:if>
	
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>