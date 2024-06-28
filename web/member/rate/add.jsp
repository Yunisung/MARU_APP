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
									<li><span>수수료 변경 예약 등록</span></li>
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
												<i class="fa fa-reorder"></i> 수수료 변경 예약 등록
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/rate/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">소속
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm grade" name="grade" value="${GRADE }" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">멤버
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm parentId" name="parentId" value="${PARENTID}" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">제목
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm title" maxlength="25" name="title" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">적용일</label>
														<div class="col-sm-6">
															<input class="form-control form-control-inline input-sm datepicker tomorrow-date pubDay" data-date-format="yyyy-mm-dd" name="pubDay" maxlength="10" type="text" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점수수료율</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm rate2" data-reg="false" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm rate" maxlength="7" name="rate" value="${MCHTMAP.rate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm rate2" data-reg="false" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm rate" maxlength="7" name="rate" value="">
															</c:if>
														</div>
													</div>
													<c:if test="${!empty MCHTMAP }">
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">선정산수수료</label>
														<div class="col-sm-6">
														<input type="text" class="form-control input-sm loanRate2" data-reg="false" maxlength="9" name="loanRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
														<input type="hidden" class="form-control input-sm loanRate" maxlength="7" name="loanRate" value="${MCHTMAP.loanRate}">
														</div>
													</div>
													</c:if>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사수수료율</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm distRate2" data-reg="false" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm distRate" maxlength="7" name="distRate" value="${MCHTMAP.distRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm distRate2" data-reg="false" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm distRate" maxlength="7" name="distRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시수수료율</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm agencyRate2" data-reg="false" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm agencyRate" maxlength="7" name="agencyRate" value="${MCHTMAP.agencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm agencyRate2" data-reg="false" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm agencyRate" maxlength="7" name="agencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">예약여부</label>
														<select name="status" class="selectpicker col-sm-6">
															<option value="사용">사용</option>
														</select>
													</div>

													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 영중소 차액정산</label>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0DistRate2" data-reg="false" maxlength="9" name="diff0DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0DistRate" maxlength="7" name="diff0DistRate" value="${MCHTMAP.diff0DistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0DistRate2" data-reg="false" maxlength="9" name="diff0DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0DistRate" maxlength="7" name="diff0DistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0CheckDistRate2" data-reg="false" maxlength="9" name="diff0CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0CheckDistRate" maxlength="7" name="diff0CheckDistRate" value="${MCHTMAP.diff0CheckDistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0CheckDistRate2" data-reg="false" maxlength="9" name="diff0CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0CheckDistRate" maxlength="7" name="diff0CheckDistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1DistRate2" data-reg="false" maxlength="9" name="diff1DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1DistRate" maxlength="7" name="diff1DistRate" value="${MCHTMAP.diff1DistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1DistRate2" data-reg="false" maxlength="9" name="diff1DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1DistRate" maxlength="7" name="diff1DistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1CheckDistRate2" data-reg="false" maxlength="9" name="diff1CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1CheckDistRate" maxlength="7" name="diff1CheckDistRate" value="${MCHTMAP.diff1CheckDistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1CheckDistRate2" data-reg="false" maxlength="9" name="diff1CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1CheckDistRate" maxlength="7" name="diff1CheckDistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2DistRate2" data-reg="false" maxlength="9" name="diff2DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2DistRate" maxlength="7" name="diff2DistRate" value="${MCHTMAP.diff2DistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2DistRate2" data-reg="false" maxlength="9" name="diff2DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2DistRate" maxlength="7" name="diff2DistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2CheckDistRate2" data-reg="false" maxlength="9" name="diff2CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2CheckDistRate" maxlength="7" name="diff2CheckDistRate" value="${MCHTMAP.diff2CheckDistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2CheckDistRate2" data-reg="false" maxlength="9" name="diff2CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2CheckDistRate" maxlength="7" name="diff2CheckDistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3DistRate2" data-reg="false" maxlength="9" name="diff3DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3DistRate" maxlength="7" name="diff3DistRate" value="${MCHTMAP.diff3DistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3DistRate2" data-reg="false" maxlength="9" name="diff3DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3DistRate" maxlength="7" name="diff3DistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3CheckDistRate2" data-reg="false" maxlength="9" name="diff3CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3CheckDistRate" maxlength="7" name="diff3CheckDistRate" value="${MCHTMAP.diff3CheckDistRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3CheckDistRate2" data-reg="false" maxlength="9" name="diff3CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3CheckDistRate" maxlength="7" name="diff3CheckDistRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0AgencyRate2" data-reg="false" maxlength="9" name="diff0AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0AgencyRate" maxlength="7" name="diff0AgencyRate" value="${MCHTMAP.diff0AgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0AgencyRate2" data-reg="false" maxlength="9" name="diff0AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0AgencyRate" maxlength="7" name="diff0AgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0CheckAgencyRate2" data-reg="false" maxlength="9" name="diff0CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0CheckAgencyRate" maxlength="7" name="diff0CheckAgencyRate" value="${MCHTMAP.diff0CheckAgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0CheckAgencyRate2" data-reg="false" maxlength="9" name="diff0CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0CheckAgencyRate" maxlength="7" name="diff0CheckAgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1AgencyRate2" data-reg="false" maxlength="9" name="diff1AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1AgencyRate" maxlength="7" name="diff1AgencyRate" value="${MCHTMAP.diff1AgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1AgencyRate2" data-reg="false" maxlength="9" name="diff1AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1AgencyRate" maxlength="7" name="diff1AgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1CheckAgencyRate2" data-reg="false" maxlength="9" name="diff1CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1CheckAgencyRate" maxlength="7" name="diff1CheckAgencyRate" value="${MCHTMAP.diff1CheckAgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1CheckAgencyRate2" data-reg="false" maxlength="9" name="diff1CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1CheckAgencyRate" maxlength="7" name="diff1CheckAgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2AgencyRate2" data-reg="false" maxlength="9" name="diff2AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2AgencyRate" maxlength="7" name="diff2AgencyRate" value="${MCHTMAP.diff2AgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2AgencyRate2" data-reg="false" maxlength="9" name="diff2AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2AgencyRate" maxlength="7" name="diff2AgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2CheckAgencyRate2" data-reg="false" maxlength="9" name="diff2CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2CheckAgencyRate" maxlength="7" name="diff2CheckAgencyRate" value="${MCHTMAP.diff2CheckAgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2CheckAgencyRate2" data-reg="false" maxlength="9" name="diff2CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2CheckAgencyRate" maxlength="7" name="diff2CheckAgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3AgencyRate2" data-reg="false" maxlength="9" name="diff3AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3AgencyRate" maxlength="7" name="diff3AgencyRate" value="${MCHTMAP.diff3AgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3AgencyRate2" data-reg="false" maxlength="9" name="diff3AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3AgencyRate" maxlength="7" name="diff3AgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3CheckAgencyRate2" data-reg="false" maxlength="9" name="diff3CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3CheckAgencyRate" maxlength="7" name="diff3CheckAgencyRate" value="${MCHTMAP.diff3CheckAgencyRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3CheckAgencyRate2" data-reg="false" maxlength="9" name="diff3CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3CheckAgencyRate" maxlength="7" name="diff3CheckAgencyRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 영세 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0SalesRate2" data-reg="false" maxlength="9" name="diff0SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0SalesRate" maxlength="7" name="diff0SalesRate" value="${MCHTMAP.diff0SalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0SalesRate2" data-reg="false" maxlength="9" name="diff0SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0SalesRate" maxlength="7" name="diff0SalesRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 영세 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0CheckSalesRate2" data-reg="false" maxlength="9" name="diff0CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0CheckSalesRate" maxlength="7" name="diff0CheckSalesRate" value="${MCHTMAP.diff0CheckSalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff0CheckSalesRate2" data-reg="false" maxlength="9" name="diff0CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff0CheckSalesRate" maxlength="7" name="diff0CheckSalesRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1SalesRate2" data-reg="false" maxlength="9" name="diff1SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1SalesRate" maxlength="7" name="diff1SalesRate" value="${MCHTMAP.diff1SalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1SalesRate2" data-reg="false" maxlength="9" name="diff1SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1SalesRate" maxlength="7" name="diff1SalesRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1CheckSalesRate2" data-reg="false" maxlength="9" name="diff1CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1CheckSalesRate" maxlength="7" name="diff1CheckSalesRate" value="${MCHTMAP.diff1CheckSalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff1CheckSalesRate2" data-reg="false" maxlength="9" name="diff1CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff1CheckSalesRate" maxlength="7" name="diff1CheckSalesRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2SalesRate2" data-reg="false" maxlength="9" name="diff2SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2SalesRate" maxlength="7" name="diff2SalesRate" value="${MCHTMAP.diff2SalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2SalesRate2" data-reg="false" maxlength="9" name="diff2SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2SalesRate" maxlength="7" name="diff2SalesRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2CheckSalesRate2" data-reg="false" maxlength="9" name="diff2CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2CheckSalesRate" maxlength="7" name="diff2CheckSalesRate" value="${MCHTMAP.diff2CheckSalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff2CheckSalesRate2" data-reg="false" maxlength="9" name="diff2CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff2CheckSalesRate" maxlength="7" name="diff2CheckSalesRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(신용)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3SalesRate2" data-reg="false" maxlength="9" name="diff3SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3SalesRate" maxlength="7" name="diff3SalesRate" value="${MCHTMAP.diff3SalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3SalesRate2" data-reg="false" maxlength="9" name="diff3SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3SalesRate" maxlength="7" name="diff3SalesRate" value="">
															</c:if>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(체크)</label>
														<div class="col-sm-6">
															<c:if test="${!empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3CheckSalesRate2" data-reg="false" maxlength="9" name="diff3CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3CheckSalesRate" maxlength="7" name="diff3CheckSalesRate" value="${MCHTMAP.diff3CheckSalesRate}">
															</c:if>
															<c:if test="${empty MCHTMAP }">
																<input type="text" class="form-control input-sm diff3CheckSalesRate2" data-reg="false" maxlength="9" name="diff3CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
																<input type="hidden" class="form-control input-sm diff3CheckSalesRate" maxlength="7" name="diff3CheckSalesRate" value="">
															</c:if>
														</div>
													</div>
												</div>
												<div class="alert alert-danger display-hide">
													<button class="close" data-close="alert"></button>
													등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.
												</div>
												<div class="form-actions right">
													<div class="">
														<button type="submit" class="btn btn-sm green loading-btn" data-loading-text="Loading...">
															<i class="fa fa-search"></i>&nbsp;Submit
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- END ADD FORM TABLE-->
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
		form1.validate({
			rules : {
				title : {
					required : true
				},
				rate2 : {
					required: true,
					max: function(element) {
						return Number($('input[name="distRate2"]').val() * 5);
					}
				},
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
				pubDay : {
					minlength:10,
					required: true
				}
			},
			submitHandler : function(form) {
				ajaxFormSubmit(form1, '/member/rate/form.jsp'); //PAGE 이동		
			}
		});
		
		
		$('.rate2').val(($('.rate').val()*100).toFixed(3));
		$('.loanRate2').val(($('.loanRate').val()*100).toFixed(3));
		$('.distRate2').val(($('.distRate').val()*100).toFixed(3));
		$('.agencyRate2').val(($('.agencyRate').val()*100).toFixed(3));
		$('.diff0DistRate2').val(($('.diff0DistRate').val()*100).toFixed(3));
		$('.diff1DistRate2').val(($('.diff1DistRate').val()*100).toFixed(3));
		$('.diff2DistRate2').val(($('.diff2DistRate').val()*100).toFixed(3));
		$('.diff3DistRate2').val(($('.diff3DistRate').val()*100).toFixed(3));
		$('.diff0CheckDistRate2').val(($('.diff0CheckDistRate').val()*100).toFixed(3));
		$('.diff1CheckDistRate2').val(($('.diff1CheckDistRate').val()*100).toFixed(3));
		$('.diff2CheckDistRate2').val(($('.diff2CheckDistRate').val()*100).toFixed(3));
		$('.diff3CheckDistRate2').val(($('.diff3CheckDistRate').val()*100).toFixed(3));
		$('.diff0AgencyRate2').val(($('.diff0AgencyRate').val()*100).toFixed(3));
		$('.diff1AgencyRate2').val(($('.diff1AgencyRate').val()*100).toFixed(3));
		$('.diff2AgencyRate2').val(($('.diff2AgencyRate').val()*100).toFixed(3));
		$('.diff3AgencyRate2').val(($('.diff3AgencyRate').val()*100).toFixed(3));
		$('.diff0CheckAgencyRate2').val(($('.diff0CheckAgencyRate').val()*100).toFixed(3));
		$('.diff1CheckAgencyRate2').val(($('.diff1CheckAgencyRate').val()*100).toFixed(3));
		$('.diff2CheckAgencyRate2').val(($('.diff2CheckAgencyRate').val()*100).toFixed(3));
		$('.diff3CheckAgencyRate2').val(($('.diff3CheckAgencyRate').val()*100).toFixed(3));
		$('.diff0SalesRate2').val(($('.diff0SalesRate').val()*100).toFixed(3));
		$('.diff1SalesRate2').val(($('.diff1SalesRate').val()*100).toFixed(3));
		$('.diff2SalesRate2').val(($('.diff2SalesRate').val()*100).toFixed(3));
		$('.diff3SalesRate2').val(($('.diff3SalesRate').val()*100).toFixed(3));
		$('.diff0CheckSalesRate2').val(($('.diff0CheckSalesRate').val()*100).toFixed(3));
		$('.diff1CheckSalesRate2').val(($('.diff1CheckSalesRate').val()*100).toFixed(3));
		$('.diff2CheckSalesRate2').val(($('.diff2CheckSalesRate').val()*100).toFixed(3));
		$('.diff3CheckSalesRate2').val(($('.diff3CheckSalesRate').val()*100).toFixed(3));

		$(".loading-btn").click(function(){
			$('.rate').val(($('.rate2').val()/100).toFixed(5));
			$('.loanRate').val(($('.loanRate2').val()/100).toFixed(5));
			$('.distRate').val(($('.distRate2').val()/100).toFixed(5));
			$('.agencyRate').val(($('.agencyRate2').val()/100).toFixed(5));
			$('.diff0DistRate').val(($('.diff0DistRate2').val()/100).toFixed(5));
			$('.diff1DistRate').val(($('.diff1DistRate2').val()/100).toFixed(5));
			$('.diff2DistRate').val(($('.diff2DistRate2').val()/100).toFixed(5));
			$('.diff3DistRate').val(($('.diff3DistRate2').val()/100).toFixed(5));
			$('.diff0CheckDistRate').val(($('.diff0CheckDistRate2').val()/100).toFixed(5));
			$('.diff1CheckDistRate').val(($('.diff1CheckDistRate2').val()/100).toFixed(5));
			$('.diff2CheckDistRate').val(($('.diff2CheckDistRate2').val()/100).toFixed(5));
			$('.diff3CheckDistRate').val(($('.diff3CheckDistRate2').val()/100).toFixed(5));
			$('.diff0AgencyRate').val(($('.diff0AgencyRate2').val()/100).toFixed(5));
			$('.diff1AgencyRate').val(($('.diff1AgencyRate2').val()/100).toFixed(5));
			$('.diff2AgencyRate').val(($('.diff2AgencyRate2').val()/100).toFixed(5));
			$('.diff3AgencyRate').val(($('.diff3AgencyRate2').val()/100).toFixed(5));
			$('.diff0CheckAgencyRate').val(($('.diff0CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff1CheckAgencyRate').val(($('.diff1CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff2CheckAgencyRate').val(($('.diff2CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff3CheckAgencyRate').val(($('.diff3CheckAgencyRate2').val()/100).toFixed(5));
			$('.diff0SalesRate').val(($('.diff0SalesRate2').val()/100).toFixed(5));
			$('.diff1SalesRate').val(($('.diff1SalesRate2').val()/100).toFixed(5));
			$('.diff2SalesRate').val(($('.diff2SalesRate2').val()/100).toFixed(5));
			$('.diff3SalesRate').val(($('.diff3SalesRate2').val()/100).toFixed(5));
			$('.diff0CheckSalesRate').val(($('.diff0CheckSalesRate2').val()/100).toFixed(5));
			$('.diff1CheckSalesRate').val(($('.diff1CheckSalesRate2').val()/100).toFixed(5));
			$('.diff2CheckSalesRate').val(($('.diff2CheckSalesRate2').val()/100).toFixed(5));
			$('.diff3CheckSalesRate').val(($('.diff3CheckSalesRate2').val()/100).toFixed(5));
		});
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>