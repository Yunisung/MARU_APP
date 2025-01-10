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
									<li><span>가맹점 지불 및 정산정보 수정</span></li>
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
												<i class="fa fa-reorder"></i> <span class="caption-title">
													${DATAMAP.name} 가맹점 지불 및 정산정보 수정 </span>
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
											
											<form class="form-horizontal form-bordered" role="form"
												data-form="true" id="writeFrm" name="form"
												action="/mcht/mng/" method="post">
												<input type="hidden" name="action_type" value="update"
													data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
													</div>
													<input type="hidden" name="mchtId" data-key="true"
														value="${DATAMAP.mchtId}" />
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">지불
															사용여부</label> <select name="payStatus"
															class="selectpicker col-sm-6">
															<option value="사용" selected>사용</option>
															<option value="중지">중지</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.payStatus.value = '${DATAMAP.payStatus}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">터미널상점 정산 사용여부</label> 
														<select name="settleTmnStatus" class="selectpicker col-sm-6 settleTmnStatus">
															<option value="사용">사용</option>
															<option value="중지">중지</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.settleTmnStatus.value = '${DATAMAP.settleTmnStatus}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">영중소구분</label> 
														<select name="diffType" class="selectpicker col-sm-6">
															<option value="영세">영세</option>
															<option value="중소1">중소1</option>
															<option value="중소2">중소2</option>
															<option value="중소3">중소3</option>
															<option value="일반">일반</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.diffType.value = '${DATAMAP.diffType}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">영업라인차액정산여부</label>
														<select name="memDiffSettleStatus" class="selectpicker col-sm-6">
															<option value="미사용" selected>미사용</option>
															<option value="사용">사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.memDiffSettleStatus.value = '${DATAMAP.memDiffSettleStatus}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label ">정산유형</label>
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
														<script type="text/javascript"> document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}' </script>
													</div>
													
													<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 수수료</label> 
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm distRate2" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm distRate" maxlength="9" name="distRate" value="${DATAMAP.distRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
													</c:if>
													<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사' || CP_SESSION.grade == '에이전시'}">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시
																수수료</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm agencyRate2" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" <c:if test="${CP_SESSION.grade == '에이전시' }">readonly</c:if> data-reg="false">
																	<input type="hidden" class="form-control input-sm agencyRate" maxlength="9" name="agencyRate" value="${DATAMAP.agencyRate}" <c:if test="${CP_SESSION.grade == '에이전시' }">readonly</c:if>>
																	<span class="input-group-addon"> % (VAT 별도) </span>
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
																	<input type="text" class="form-control input-sm salesRate2" maxlength="9" name="salesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" <c:if test="${CP_SESSION.grade == '지사' }">readonly</c:if> data-reg="false">
																	<input type="hidden" class="form-control input-sm salesRate" maxlength="9" name="salesRate" value="${DATAMAP.salesRate}" <c:if test="${CP_SESSION.grade == '지사' }">readonly</c:if>>
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
													</c:if>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점 수수료</label>
														<div class="col-sm-8">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control rate2" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																<input type="hidden" class="form-control rate" maxlength="9" name="rate" value="${DATAMAP.rate}">
																<span class="input-group-addon"> % (VAT 별도) </span>
															</div>
														</div>
													</div>
													<c:if test="${CP_SESSION.grade == '본사'}">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">선정산 수수료</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control loanRate2" maxlength="9" name="loanRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control loanRate" maxlength="9" name="loanRate" value="${DATAMAP.loanRate}"> 
																	<span class="input-group-addon"> % </span>
																</div>
															</div>
														</div>
													</c:if>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이체
															건당 수수료 <br>(즉시결제시만 사용)
														</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm wireFee currency" maxlength="15" name="wireFee" placeholder="" value="${DATAMAP.wireFee}"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1회한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm limitOnce currency" maxlength="15" name="limitOnce" placeholder="" value="${DATAMAP.limitOnce}"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1일한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm limitDay currency" maxlength="15" name="limitDay" placeholder="" value="${DATAMAP.limitDay}"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1개월한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm limitMonth currency" maxlength="15" name="limitMonth" placeholder="" value="${DATAMAP.limitMonth}"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">연한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm limitYear currency" maxlength="15" name="limitYear" placeholder="" value="${DATAMAP.limitYear}"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">고액거래 기준</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency largeAmount" name="largeAmount" placeholder="" value="${DATAMAP.largeAmount}"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">선정산 한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency maxLoan" name="maxLoan" placeholder="" value="${DATAMAP.maxLoan}"> 
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
																		<input type="text" class="form-control currency payOutFee" maxlength="9" name="payOutFee" placeholder="" value="${DATAMAP.payOutFee}">
																		<span class="input-group-addon"><i class="fa fa-krw"></i></span>
																	</div>
																</div>
															</div>
															<div class="form-group col-sm-6">
																<label class="control-label col-sm-4 req-label">실시간정산<br>전송간격</label>
																<div class="col-sm-6">
																	<div class="input-group input-group-sm">
																		<input type="text" class="form-control currency transferInterval" maxlength="3" name="transferInterval" placeholder="" value="${DATAMAP.transferInterval}"> 
																		<span class="input-group-addon">분</span>
																	</div>
																</div>
															</div>
														</c:if>
													</c:if>								
													<c:if test="${CP_SESSION.grade eq '본사' }">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">상점부담무이자</label>
															<select name="interType" class="selectpicker col-sm-6">
																<option value="사용" >사용</option>
																<option value="중지" selected>중지</option>
															</select>
														</div>
														<script type="text/javascript"> document.forms.writeFrm.interType.value = '${DATAMAP.interType}' </script>
													</c:if>
													<c:if test="${CP_SESSION.grade eq '본사' }">
														<div class="form-group col-sm-12 form-subtitle">
															<label><i class="fa fa-reorder"></i> 영중소 차액정산</label>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff0DistRate2" maxlength="9" name="diff0DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0DistRate" maxlength="9" name="diff0DistRate" placeholder="" value="${DATAMAP.diff0DistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff0CheckDistRate2" maxlength="9" name="diff0CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckDistRate" maxlength="9" name="diff0CheckDistRate" placeholder="" value="${DATAMAP.diff0CheckDistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff1DistRate2" maxlength="9" name="diff1DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1DistRate" maxlength="9" name="diff1DistRate" placeholder="" value="${DATAMAP.diff1DistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff1CheckDistRate2" maxlength="9" name="diff1CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckDistRate" maxlength="9" name="diff1CheckDistRate" placeholder="" value="${DATAMAP.diff1CheckDistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff2DistRate2" maxlength="9" name="diff2DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2DistRate" maxlength="9" name="diff2DistRate" placeholder="" value="${DATAMAP.diff2DistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff2CheckDistRate2" maxlength="9" name="diff2CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckDistRate" maxlength="9" name="diff2CheckDistRate" placeholder="" value="${DATAMAP.diff2CheckDistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff3DistRate2" maxlength="9" name="diff3DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3DistRate" maxlength="9" name="diff3DistRate" placeholder="" value="${DATAMAP.diff3DistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff3CheckDistRate2" maxlength="9" name="diff3CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckDistRate" maxlength="9" name="diff3CheckDistRate" placeholder="" value="${DATAMAP.diff3CheckDistRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff0AgencyRate2" maxlength="9" name="diff0AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0AgencyRate" maxlength="9" name="diff0AgencyRate" placeholder="" value="${DATAMAP.diff0AgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff0CheckAgencyRate2" maxlength="9" name="diff0CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckAgencyRate" maxlength="9" name="diff0CheckAgencyRate" placeholder="" value="${DATAMAP.diff0CheckAgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff1AgencyRate2" maxlength="9" name="diff1AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1AgencyRate" maxlength="9" name="diff1AgencyRate" placeholder="" value="${DATAMAP.diff1AgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff1CheckAgencyRate2" maxlength="9" name="diff1CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckAgencyRate" maxlength="9" name="diff1CheckAgencyRate" placeholder="" value="${DATAMAP.diff1CheckAgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff2AgencyRate2" maxlength="9" name="diff2AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2AgencyRate" maxlength="9" name="diff2AgencyRate" placeholder="" value="${DATAMAP.diff2AgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff2CheckAgencyRate2" maxlength="9" name="diff2CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckAgencyRate" maxlength="9" name="diff2CheckAgencyRate" placeholder="" value="${DATAMAP.diff2CheckAgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff3AgencyRate2" maxlength="9" name="diff3AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3AgencyRate" maxlength="9" name="diff3AgencyRate" placeholder="" value="${DATAMAP.diff3AgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff3CheckAgencyRate2" maxlength="9" name="diff3CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckAgencyRate" maxlength="9" name="diff3CheckAgencyRate" placeholder="" value="${DATAMAP.diff3CheckAgencyRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff0SalesRate2" maxlength="9" name="diff0SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0SalesRate" maxlength="9" name="diff0SalesRate" placeholder="" value="${DATAMAP.diff0SalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff0CheckSalesRate2" maxlength="9" name="diff0CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckSalesRate" maxlength="9" name="diff0CheckSalesRate" placeholder="" value="${DATAMAP.diff0CheckSalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff1SalesRate2" maxlength="9" name="diff1SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1SalesRate" maxlength="9" name="diff1SalesRate" placeholder="" value="${DATAMAP.diff1SalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff1CheckSalesRate2" maxlength="9" name="diff1CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckSalesRate" maxlength="9" name="diff1CheckSalesRate" placeholder="" value="${DATAMAP.diff1CheckSalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff2SalesRate2" maxlength="9" name="diff2SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2SalesRate" maxlength="9" name="diff2SalesRate" placeholder="" value="${DATAMAP.diff2SalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff2CheckSalesRate2" maxlength="9" name="diff2CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckSalesRate" maxlength="9" name="diff2CheckSalesRate" placeholder="" value="${DATAMAP.diff2CheckSalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff3SalesRate2" maxlength="9" name="diff3SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3SalesRate" maxlength="9" name="diff3SalesRate" placeholder="" value="${DATAMAP.diff3SalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control diff3CheckSalesRate2" maxlength="9" name="diff3CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckSalesRate" maxlength="9" name="diff3CheckSalesRate" placeholder="" value="${DATAMAP.diff3CheckSalesRate}">
																	<span class="input-group-addon"> % (VAT 별도) </span>
																</div>
															</div>
														</div>
													</c:if>
													
													
													
												</div>
												<div class="alert alert-danger display-hide"></div>
												<div class="form-actions right">
													<div class="">
														<button type="submit" class="btn green btn-sm loading-btn"
															data-loading-text="Loading...">
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
				largeAmount: {
					required: true
				},
				maxLoan : {
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
							return Number($('input[name="agencyRate"]').val());
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
				}
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
               	var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
               	error1.html(error1Str);
				error1.show();
                App.scrollTo(error1, -200);
            },
            submitHandler: function (form) {
                error1.hide();
				bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 가맹점 지불 및 정산 정보를 수정하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_mng'); //PAGE 이동		
					}
				});
			}
		});
		<%--
		addLimitRules(${DATADISTMNGMAP.limitOnce}, ${DATADISTMNGMAP.limitDay}, ${DATADISTMNGMAP.limitMonth});
		--%>
		
		$('.selectpicker.settleType').on('change', function() {
			var selected = $(this).find("option:selected").val();
			console.log('settleType:', selected);
		  if(selected === 'D+1') {
		    $('input[name="loanRate"]').val('0.00000');
		    $('input[name="loanRate2"]').val('0');
		  } else {
		    $('input[name="loanRate"]').val('0.00000');
		    $('input[name="loanRate2"]').val('0');
		  }
		});
		
		<c:if test="${CP_SESSION.grade eq '본사'}">
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
		</c:if>
		
		 $('.distRate2').val(($('.distRate').val()*100).toFixed(3));
		 $('.agencyRate2').val(($('.agencyRate').val()*100).toFixed(3));
		 $('.salesRate2').val(($('.salesRate').val()*100).toFixed(3));
		 $('.rate2').val(($('.rate').val()*100).toFixed(3));
		 $('.loanRate2').val(($('.loanRate').val()*100).toFixed(3));
		 $('.diff0DistRate2').val(($('.diff0DistRate').val()*100).toFixed(3));
		 $('.diff0CheckDistRate2').val(($('.diff0CheckDistRate').val()*100).toFixed(3));
		 $('.diff1DistRate2').val(($('.diff1DistRate').val()*100).toFixed(3));
		 $('.diff1CheckDistRate2').val(($('.diff1CheckDistRate').val()*100).toFixed(3));
		 $('.diff2DistRate2').val(($('.diff2DistRate').val()*100).toFixed(3));
		 $('.diff2CheckDistRate2').val(($('.diff2CheckDistRate').val()*100).toFixed(3));
		 $('.diff3DistRate2').val(($('.diff3DistRate').val()*100).toFixed(3));
		 $('.diff3CheckDistRate2').val(($('.diff3CheckDistRate').val()*100).toFixed(3));
		 $('.diff0AgencyRate2').val(($('.diff0AgencyRate').val()*100).toFixed(3));
		 $('.diff0CheckAgencyRate2').val(($('.diff0CheckAgencyRate').val()*100).toFixed(3));
		 $('.diff1AgencyRate2').val(($('.diff1AgencyRate').val()*100).toFixed(3));
		 $('.diff1CheckAgencyRate2').val(($('.diff1CheckAgencyRate').val()*100).toFixed(3));
		 $('.diff2AgencyRate2').val(($('.diff2AgencyRate').val()*100).toFixed(3));
		 $('.diff2CheckAgencyRate2').val(($('.diff2CheckAgencyRate').val()*100).toFixed(3));
		 $('.diff3AgencyRate2').val(($('.diff3AgencyRate').val()*100).toFixed(3));
		 $('.diff3CheckAgencyRate2').val(($('.diff3CheckAgencyRate').val()*100).toFixed(3));
		 $('.diff0SalesRate2').val(($('.diff0SalesRate').val()*100).toFixed(3));
		 $('.diff0CheckSalesRate2').val(($('.diff0CheckSalesRate').val()*100).toFixed(3));
		 $('.diff1SalesRate2').val(($('.diff1SalesRate').val()*100).toFixed(3));
		 $('.diff1CheckSalesRate2').val(($('.diff1CheckSalesRate').val()*100).toFixed(3));
		 $('.diff2SalesRate2').val(($('.diff2SalesRate').val()*100).toFixed(3));
		 $('.diff2CheckSalesRate2').val(($('.diff2CheckSalesRate').val()*100).toFixed(3));
		 $('.diff3SalesRate2').val(($('.diff3SalesRate').val()*100).toFixed(3));
		 $('.diff3CheckSalesRate2').val(($('.diff3CheckSalesRate').val()*100).toFixed(3));
		
	

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
		});
		
		$('#nav-mcht').addClass('active');
	</script>
	
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>