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
									<li><span>가맹점 관리</span><i class="fa fa-circle"></i></li>
									<li><span>가맹점 지불 및 정산정보 등록</span></li>
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
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-reorder"></i> 
												<span class="caption-title"> ${DATAMAP.name} 가맹점 지불 및 정산정보 등록 </span>
											</div>
										</div>
										<c:if test="${CP_SESSION.grade eq '본사' }">
											<div class="form-group col-sm-12">
												<label class="control-label input-sm col-sm-2 req-label">수수료템플릿</label> 
													<select name="feeTemplate" class="selectpicker col-sm-10 feeTemplate">
														<option value="">선택</option>
														<c:forEach items="${FEE_TEMPLATE }" var="list">
															<option value="${list.idx }">${list.name }</option>
														</c:forEach>
													</select>
											</div>
										</c:if>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/mng/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
													</div>
													<input type="hidden" name="mchtId"
														value="${DATAMAP.mchtId}" />
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">지불
															사용여부</label> <select name="payStatus"
															class="selectpicker col-sm-6">
															<option value="사용" selected>사용</option>
															<option value="중지">중지</option>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">영중소구분</label> 
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" name="diffType" value="${DIFFTYPE }" readonly="readonly">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">정산유형</label>
														<select name="settleType" class="selectpicker col-sm-6 settleType">
															<c:if test="${DATASVCMAP.settle == '일반'}">
																<option value="D+1">1일 후 정산</option>
																<option value="D+2">2일 후 정산</option>
																<option value="D+3">3일 후 정산</option>
																<option value="D+4">4일 후 정산</option>
																<option value="D+5">5일 후 정산</option>
																<option value="D+6">6일 후 정산</option>
																<option value="D+7">7일 후 정산</option>
															</c:if>
															<c:if test="${DATASVCMAP.settle == '충전정산'}">
																<option value="C+0">실시간 충전정산</option>
																<option value="B+1">1일 후 자동충전정산</option>
																<option value="C+1">1일 후 충전정산</option>
																<option value="C+2">2일 후 충전정산</option>
																<option value="C+3">3일 후 충전정산</option>
																<option value="C+4">4일 후 충전정산</option>
																<option value="C+5">5일 후 충전정산</option>
																<option value="C+6">6일 후 충전정산</option>
																<option value="C+7">7일 후 충전정산</option>
															</c:if>
															<c:if test="${CP_SESSION.grade eq '본사' && DATASVCMAP.settle == '실시간정산' }">
																<option value="D+0">실시간정산</option>
															</c:if>
															<c:if test="${CP_SESSION.grade eq '본사' && DATASVCMAP.settle == '자동정산' }">
																<option value="A+1">1일 후 자동정산</option>
																<option value="A+0">당일정산</option>
															</c:if>
														</select>
													</div>
													<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 수수료</label> 
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm distRate2" data-reg="false" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																	<input type="hidden" class="form-control input-sm distRate" maxlength="9" name="distRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
															<%--
															<select name="distRate"
																class="selectpicker col-sm-8 distRate">
																<option value="">수수료 선택</option>
																<c:forEach var="entryMap" items="${DISTRATE_OPTION}">
																	<option value="${entryMap['rate']}">${entryMap['ratePer']} %</option>
																</c:forEach>
															</select> --%>
														</div>
													</c:if>
													<c:if test="${CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 수수료</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm agencyRate2" data-reg="false" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" <c:if test="${CP_SESSION.grade == '에이전시' }">readonly</c:if>>
																	<input type="hidden" class="form-control input-sm agencyRate" maxlength="9" name="agencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
													</c:if>
													<c:if test="${CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사' || CP_SESSION.grade == '지사'}">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사
																수수료</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm salesRate2" data-reg="false" maxlength="9" name="salesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" <c:if test="${CP_SESSION.grade == '지사' }">readonly</c:if>>
																	<input type="hidden" class="form-control input-sm salesRate" maxlength="9" name="salesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
													</c:if>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점 수수료</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control rate2" data-reg="false" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control rate" maxlength="9" name="rate" value="0.00000"> 
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
														</div>
													</div>
													<c:if test="${CP_SESSION.grade == '본사'}">
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">선정산 수수료</label>
														<div class="col-sm-8">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control loanRate2" data-reg="false" maxlength="9" name="loanRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0"> 
																<input type="hidden" class="form-control input-sm loanRate" maxlength="9" name="loanRate" value="0.00000">
																<span class="input-group-addon"> % </span>
															</div>
														</div>
													</div>
													</c:if>
													
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이체 건당 수수료<br>(즉시결제시만 사용)
														</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency wireFee" maxlength="6" name="wireFee" placeholder="" value="0">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1회한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency limitOnce" maxlength="12" name="limitOnce" placeholder="" value="500000"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1일한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency limitDay" maxlength="14" name="limitDay" placeholder="" value="0"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1개월한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency limitMonth" maxlength="14" name="limitMonth" placeholder="" value="0"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">연한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency limitYear" maxlength="14" name="limitYear" placeholder="" value="0"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">고액거래 기준</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency largeAmount" name="largeAmount" placeholder="" value="3000000"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">선정산 한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency maxLoan" name="maxLoan" placeholder="" value="10000000"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													
													<c:if test="${CP_SESSION.grade eq '본사'}">
														<c:if test="${DATASVCMAP.settle == '실시간정산' || DATASVCMAP.settle == '자동정산'}">
															<div class="form-group col-sm-6">
																<label class="control-label col-sm-4 req-label">실시간정산<br>출금 수수료</label>
																<div class="col-sm-6">
																	<div class="input-group input-group-sm">
																		<input type="text" class="form-control currency payOutFee" maxlength="9" name="payOutFee" placeholder="" value="0">
																		<span class="input-group-addon"><i class="fa fa-krw"></i></span>
																	</div>
																</div>
															</div>
															<div class="form-group col-sm-6">
																<label class="control-label col-sm-4 req-label">실시간정산<br>전송간격</label>
																<div class="col-sm-6">
																	<div class="input-group input-group-sm">
																		<input type="text" class="form-control currency transferInterval" maxlength="3" name="transferInterval" placeholder="" value="5"> 
																		<span class="input-group-addon">분</span>
																	</div>
																</div>
															</div>
														</c:if>
													</c:if>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이메일</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm email"
																maxlength="100" name="email" placeholder="이메일을 입력하세요."
																value="">
														</div>
													</div>
													
													<c:if test="${CP_SESSION.grade eq '본사' }">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">상점부담무이자</label>
															<select name="interType" class="selectpicker col-sm-6">
																<option value="사용" >사용</option>
																<option value="중지" selected>중지</option>
															</select>
														</div>
													</c:if>
													
													
													<c:if test="${CP_SESSION.grade eq '본사' }">
														<div class="form-group col-sm-12 form-subtitle">
															<label><i class="fa fa-reorder"></i> 영중소 차액정산</label>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0DistRate2" maxlength="9" name="diff0DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0DistRate" maxlength="9" name="diff0DistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0CheckDistRate2" maxlength="9" name="diff0CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckDistRate" maxlength="9" name="diff0CheckDistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1DistRate2" maxlength="9" name="diff1DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1DistRate" maxlength="9" name="diff1DistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1CheckDistRate2" maxlength="9" name="diff1CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckDistRate" maxlength="9" name="diff1CheckDistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2DistRate2" maxlength="9" name="diff2DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2DistRate" maxlength="9" name="diff2DistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2CheckDistRate2" maxlength="9" name="diff2CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckDistRate" maxlength="9" name="diff2CheckDistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3DistRate2" maxlength="9" name="diff3DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3DistRate" maxlength="9" name="diff3DistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3CheckDistRate2" maxlength="9" name="diff3CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckDistRate" maxlength="9" name="diff3CheckDistRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0AgencyRate2" maxlength="9" name="diff0AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0AgencyRate" maxlength="9" name="diff0AgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0CheckAgencyRate2" maxlength="9" name="diff0CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckAgencyRate" maxlength="9" name="diff0CheckAgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1AgencyRate2" maxlength="9" name="diff1AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1AgencyRate" maxlength="9" name="diff1AgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1CheckAgencyRate2" maxlength="9" name="diff1CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckAgencyRate" maxlength="9" name="diff1CheckAgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2AgencyRate2" maxlength="9" name="diff2AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2AgencyRate" maxlength="9" name="diff2AgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2CheckAgencyRate2" maxlength="9" name="diff2CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckAgencyRate" maxlength="9" name="diff2CheckAgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3AgencyRate2" maxlength="9" name="diff3AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3AgencyRate" maxlength="9" name="diff3AgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3CheckAgencyRate2" maxlength="9" name="diff3CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckAgencyRate" maxlength="9" name="diff3CheckAgencyRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0SalesRate2" maxlength="9" name="diff0SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0SalesRate" maxlength="9" name="diff0SalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0CheckSalesRate2" maxlength="9" name="diff0CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckSalesRate" maxlength="9" name="diff0CheckSalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1SalesRate2" maxlength="9" name="diff1SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1SalesRate" maxlength="9" name="diff1SalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1CheckSalesRate2" maxlength="9" name="diff1CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckSalesRate" maxlength="9" name="diff1CheckSalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2SalesRate2" maxlength="9" name="diff2SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2SalesRate" maxlength="9" name="diff2SalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2CheckSalesRate2" maxlength="9" name="diff2CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckSalesRate" maxlength="9" name="diff2CheckSalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3SalesRate2" maxlength="9" name="diff3SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3SalesRate" maxlength="9" name="diff3SalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3CheckSalesRate2" maxlength="9" name="diff3CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckSalesRate" maxlength="9" name="diff3CheckSalesRate" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
													</c:if>
													
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-bank"></i> 은행 정보 입력</label>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">은행이름</label>
														<select name="bankCd" class="selectpicker col-sm-6 bankCd">
															<option value="">은행 선택</option>
															<c:forEach var="entryMap" items="${BANK_OPTION}">
																<option value="${entryMap['code']}">${entryMap['codeName']}</option>
															</c:forEach>
														</select> <input type="hidden" name="bankName" value="">
													</div> 
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">계좌번호</label>
														<div class="col-sm-6">
															<input type="text"
																class="form-control input-sm ceoPhone account"
																maxlength="20" name="account" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">예금주</label>
														<div class="col-sm-6">
															<input type="text"
																class="form-control input-sm ceoTel accntHolder"
																maxlength="30" name="accntHolder" placeholder=""
																value="">
														</div>
													</div>
												</div>
												<div class="alert alert-danger display-hide"></div>
												<div class="form-actions right">
													<div class="">
														<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
															<i class="fa fa-search"></i>&nbsp;Submit
														</button>
													</div>
												</div>
											</form>
										</div>
										<!-- END ADD FORM TABLE-->
									</div>
								</div>
							</div>
						</div>
			</div>
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				limitOnce : {
					money: true
				},
				limitDay : {
					money: true
				},
				limitMonth : {
					money: true
				},
				limitYear : {
					money: true
				},
				settleType : {
					required: true
				},
				rate2 : {
					required: true
					<%--<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
					,max: function(element) {
						return Number($('input[name="distRate"]').val() * 5);
					}
					</c:if>--%>
				},
				<%--
				<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
					agencyRate2 : {
						required: true,
						min: function(element) {
							return Number($('input[name="distRate2"]').val());
						}
					},
					distRate2 : {
						required: true,
						max: function(element) {
							return Number($('input[name="agencyRate2"]').val());
						}
					},
				</c:if>
				--%>
				wireFee : {
					required: true,
					money: true
				},
				bankCd : {
					required : true
				},
				bankName : {
					required: true
				},
				account : {
					required:true
				},
				accntHolder: {
					required:true
				},
				tmnId : {
                	required: true,
					remote : {
						url : "/mcht/tmn/idCheck", //make sure to return true or false with a 200 status code
						type : "post",
						data : {
							id : function() {
								return form1.find('input[name="tmnId"]').val();
							}
						}
					}
				},
				van : {
					required: true
				},
				vanIdx : {
					required: true
				},
				email: {
					required: true,
					email:true
				}
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
               	var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
               	error1.html(error1Str);
				error1.show();
                App.scrollTo(error1, -200);
            },
            submitHandler: function (form) {
                error1.hide();
				bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 가맹점 지불 및 정산 정보를 생성하시겠습니까?", function(result) {
					if (result) {
						var str = "";
						
						$.ajax({
				            type: "POST",
				            url: "/mcht/mng/check/${DATAMAP.mchtId}",
				            data: "mchtId=${DATAMAP.mchtId}",
				            success: function(res){
				    			if(res.distRes == "OK" && res.agencyRes == "OK"){
				    				ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_mng'); //PAGE 이동		
				    			} else {
				    				if(res.distRes == "NOK" && res.agencyRes == "NOK") {
				    					str = res.agencyMsg + "와 " + res.distMsg;
				    				} else if (res.distRes == "NOK") {
				    					str = res.distMsg; 
				    				} else {
				    					str = res.agencyMsg;
				    				}
				    				
				    				bootbox.alert(str + "'정산정보'를 입력하세요.");
				    				return false;
				    			}
				    		},
				    		error: function(xhr, textStatus, errorThrown){
				    			bootbox.alert('Error ' + errorThrown);
				    		}
				        });
						
					
					}
					
					
				});
			}
		});
		
		<%--
		var limitOnce = 0;
		var limitDay = 0;
		var limitMonth = 0;
		var limitYear = 0;
		if('${DATADISTMNGMAP.limitOnce}' != '' || '${DATADISTMNGMAP.limitOnce}' != null){
			limitOnce = '${DATADISTMNGMAP.limitOnce}';
		}
		if('${DATADISTMNGMAP.limitDay}' != '' || '${DATADISTMNGMAP.limitDay}' != null){
			limitDay = '${DATADISTMNGMAP.limitDay}';
		}
		if('${DATADISTMNGMAP.limitMonth}' != '' || '${DATADISTMNGMAP.limitMonth}' != null){
			limitMonth = '${DATADISTMNGMAP.limitMonth}';
		}
		if('${DATADISTMNGMAP.limitYear}' != '' || '${DATADISTMNGMAP.limitYear}' != null){
			limitYear = '${DATADISTMNGMAP.limitYear}';
		}
		
		addLimitRules(limitOnce, limitDay, limitMonth);
		--%>
		
		$('.selectpicker.settleType').on('change', function() {
			var selected = $(this).find("option:selected").val();
		  if(selected === 'D+1') {
		    $('input[name="loanRate"]').val('0.00000');
		    $('input[name="loanRate2"]').val('0');
		  } else {
		    $('input[name="loanRate"]').val('0.00000');
		    $('input[name="loanRate2"]').val('0');
		  }
		});

		$('.selectpicker.feeTemplate').on('change', function() {
			var selected = $(this).find("option:selected").val();
			if(selected == '') return false;
		  	$.ajax({
		  		url:'/mcht/feeTemplate/get/'+selected,
		  		method: 'GET',
		  		dataType: 'json',
		  		success: function(data){
		  			 $('input[name="rate"]').val(data.rate);
		  			 $('input[name="distRate"]').val(data.distRate);
		  			 $('input[name="agencyRate"]').val(data.agencyRate);
		  			 $('input[name="salesRate"]').val(data.salesRate);
		  			 $('input[name="diff0DistRate"]').val(data.diff0DistRate);
		  			 $('input[name="diff0CheckDistRate"]').val(data.diff0CheckDistRate);
		  			 $('input[name="diff1DistRate"]').val(data.diff1DistRate);
		  			 $('input[name="diff1CheckDistRate"]').val(data.diff1CheckDistRate);
		  			 $('input[name="diff2DistRate"]').val(data.diff2DistRate);
		  			 $('input[name="diff2CheckDistRate"]').val(data.diff2CheckDistRate);
		  			 $('input[name="diff3DistRate"]').val(data.diff3DistRate);
		  			 $('input[name="diff3CheckDistRate"]').val(data.diff3CheckDistRate);
		  			 $('input[name="diff0AgencyRate"]').val(data.diff0AgencyRate);
		  			 $('input[name="diff0CheckAgencyRate"]').val(data.diff0CheckAgencyRate);
		  			 $('input[name="diff1AgencyRate"]').val(data.diff1AgencyRate);
		  			 $('input[name="diff1CheckAgencyRate"]').val(data.diff1CheckAgencyRate);
		  			 $('input[name="diff2AgencyRate"]').val(data.diff2AgencyRate);
		  			 $('input[name="diff2CheckAgencyRate"]').val(data.diff2CheckAgencyRate);
		  			 $('input[name="diff3AgencyRate"]').val(data.diff3AgencyRate);
		  			 $('input[name="diff3CheckAgencyRate"]').val(data.diff3CheckAgencyRate);
		  			 $('input[name="diff0SalesRate"]').val(data.diff0SalesRate);
		  			 $('input[name="diff0CheckSalesRate"]').val(data.diff0CheckSalesRate);
		  			 $('input[name="diff1SalesRate"]').val(data.diff1SalesRate);
		  			 $('input[name="diff1CheckSalesRate"]').val(data.diff1CheckSalesRate);
		  			 $('input[name="diff2SalesRate"]').val(data.diff2SalesRate);
		  			 $('input[name="diff2CheckSalesRate"]').val(data.diff2CheckSalesRate);
		  			 $('input[name="diff3SalesRate"]').val(data.diff3SalesRate);
		  			 $('input[name="diff3CheckSalesRate"]').val(data.diff3CheckSalesRate);
		  			 
		  			 $('input[name="rate2"]').val((data.rate*100).toFixed(3));
		  			 $('input[name="distRate2"]').val((data.distRate*100).toFixed(3));
		  			 $('input[name="agencyRate2"]').val((data.agencyRate*100).toFixed(3));
		  			 $('input[name="salesRate2"]').val((data.salesRate*100).toFixed(3));
		  			 $('input[name="diff0DistRate2"]').val((data.diff0DistRate*100).toFixed(3));
		  			 $('input[name="diff0CheckDistRate2"]').val((data.diff0CheckDistRate*100).toFixed(3));
		  			 $('input[name="diff1DistRate2"]').val((data.diff1DistRate*100).toFixed(3));
		  			 $('input[name="diff1CheckDistRate2"]').val((data.diff1CheckDistRate*100).toFixed(3));
		  			 $('input[name="diff2DistRate2"]').val((data.diff2DistRate*100).toFixed(3));
		  			 $('input[name="diff2CheckDistRate2"]').val((data.diff2CheckDistRate*100).toFixed(3));
		  			 $('input[name="diff3DistRate2"]').val((data.diff3DistRate*100).toFixed(3));
		  			 $('input[name="diff3CheckDistRate2"]').val((data.diff3CheckDistRate*100).toFixed(3));
		  			 $('input[name="diff0AgencyRate2"]').val((data.diff0AgencyRate*100).toFixed(3));
		  			 $('input[name="diff0CheckAgencyRate2"]').val((data.diff0CheckAgencyRate*100).toFixed(3));
		  			 $('input[name="diff1AgencyRate2"]').val((data.diff1AgencyRate*100).toFixed(3));
		  			 $('input[name="diff1CheckAgencyRate2"]').val((data.diff1CheckAgencyRate*100).toFixed(3));
		  			 $('input[name="diff2AgencyRate2"]').val((data.diff2AgencyRate*100).toFixed(3));
		  			 $('input[name="diff2CheckAgencyRate2"]').val((data.diff2CheckAgencyRate*100).toFixed(3));
		  			 $('input[name="diff3AgencyRate2"]').val((data.diff3AgencyRate*100).toFixed(3));
		  			 $('input[name="diff3CheckAgencyRate2"]').val((data.diff3CheckAgencyRate*100).toFixed(3));
		  			 $('input[name="diff0SalesRate2"]').val((data.diff0SalesRate*100).toFixed(3));
		  			 $('input[name="diff0CheckSalesRate2"]').val((data.diff0CheckSalesRate*100).toFixed(3));
		  			 $('input[name="diff1SalesRate2"]').val((data.diff1SalesRate*100).toFixed(3));
		  			 $('input[name="diff1CheckSalesRate2"]').val((data.diff1CheckSalesRate*100).toFixed(3));
		  			 $('input[name="diff2SalesRate2"]').val((data.diff2SalesRate*100).toFixed(3));
		  			 $('input[name="diff2CheckSalesRate2"]').val((data.diff2CheckSalesRate*100).toFixed(3));
		  			 $('input[name="diff3SalesRate2"]').val((data.diff3SalesRate*100).toFixed(3));
		  			 $('input[name="diff3CheckSalesRate2"]').val((data.diff3CheckSalesRate*100).toFixed(3));
		  			 
		  		}
		  	});
		});
		
   		$('.wireFee').val(addComma(String($('.wireFee').val())));
   		$('.limitOnce').val(addComma(String($('.limitOnce').val())));
   		$('.limitDay').val(addComma(String($('.limitDay').val())));
   		$('.limitMonth').val(addComma(String($('.limitMonth').val())));
   		$('.limitYear').val(addComma(String($('.limitYear').val())));
   		$('.largeAmount').val(addComma(String($('.largeAmount').val())));
   		$('.maxLoan').val(addComma(String($('.maxLoan').val())));
		
		$('.wireFee').keyup(function(){
			$('.wireFee').val(addComma(String($('.wireFee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		$('.limitOnce').keyup(function(){
			$('.limitOnce').val(addComma(String($('.limitOnce').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		$('.limitDay').keyup(function(){
			$('.limitDay').val(addComma(String($('.limitDay').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		$('.limitMonth').keyup(function(){
			$('.limitMonth').val(addComma(String($('.limitMonth').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		$('.limitYear').keyup(function(){
			$('.limitYear').val(addComma(String($('.limitYear').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		$('.largeAmount').keyup(function(){
			$('.largeAmount').val(addComma(String($('.largeAmount').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		$('.maxLoan').keyup(function(){
			$('.maxLoan').val(addComma(String($('.maxLoan').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		$(".loading-btn").click(function(){
	    	$(".wireFee").val($(".wireFee").val().replace(/,/g, ''));
	    	$(".limitOnce").val($(".limitOnce").val().replace(/,/g, ''));
	    	$(".limitDay").val($(".limitDay").val().replace(/,/g, ''));
	    	$(".limitMonth").val($(".limitMonth").val().replace(/,/g, ''));
	    	$(".limitYear").val($(".limitYear").val().replace(/,/g, ''));
	    	$(".largeAmount").val($(".largeAmount").val().replace(/,/g, ''));
	    	$(".maxLoan").val($(".maxLoan").val().replace(/,/g, ''));
	    	
			$('.distRate').val(($('.distRate2').val()/100).toFixed(5));
			$('.agencyRate').val(($('.agencyRate2').val()/100).toFixed(5));
			$('.salesRate').val(($('.salesRate2').val()/100).toFixed(5));
			$('.rate').val(($('.rate2').val()/100).toFixed(5));
			$('.loanRate').val(($('.loanRate2').val()/100).toFixed(5));
			$('.diff0DistRate').val(($('.diff0DistRate2').val()/100).toFixed(5));
			$('.diff0CheckDistRate').val(($('.diff0CheckDistRate2').val()/100).toFixed(5));
			$('.diff1DistRate').val(($('.diff1DistRate2').val()/100).toFixed(5));
			$('.diff1CheckDistRate').val(( $('.diff1CheckDistRate2').val()/100).toFixed(5));
			$('.diff2DistRate').val(($('.diff2DistRate2').val()/100).toFixed(5));
			$('.diff2CheckDistRate').val(($('.diff2CheckDistRate2').val()/100).toFixed(5));
			$('.diff3DistRate').val(($('.diff3DistRate2').val()/100).toFixed(5));
			$('.diff3CheckDistRate').val(($('.diff3CheckDistRate2').val()/100).toFixed(5));
			$('.diff0AgencyRate').val(($('.diff0AgencyRate2').val()/100).toFixed(5));
			$('.diff0CheckAgencyRate').val(($('.diff0CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff1AgencyRate').val(($('.diff1AgencyRate2').val()/100).toFixed(5));
			$('.diff1CheckAgencyRate').val(($('.diff1CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff2AgencyRate').val(($('.diff2AgencyRate2').val()/100).toFixed(5));
			$('.diff2CheckAgencyRate').val(($('.diff2CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff3AgencyRate').val(($('.diff3AgencyRate2').val()/100).toFixed(5));
			$('.diff3CheckAgencyRate').val(($('.diff3CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff0SalesRate').val(($('.diff0SalesRate2').val()/100).toFixed(5));
			$('.diff0CheckSalesRate').val(($('.diff0CheckSalesRate2').val()/100).toFixed(5));
			$('.diff1SalesRate').val(($('.diff1SalesRate2').val()/100).toFixed(5));
			$('.diff1CheckSalesRate').val(($('.diff1CheckSalesRate2').val()/100).toFixed(5));
			$('.diff2SalesRate').val(($('.diff2SalesRate2').val()/100).toFixed(5));
			$('.diff2CheckSalesRate').val(($('.diff2CheckSalesRate2').val()/100).toFixed(5));
			$('.diff3SalesRate').val(($('.diff3SalesRate2').val()/100).toFixed(5));
			$('.diff3CheckSalesRate').val(($('.diff3CheckSalesRate2').val()/100).toFixed(5));
		});
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>