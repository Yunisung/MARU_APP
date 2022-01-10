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
									<li><span>가맹점 수수료 템플릿 등록</span></li>
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
													가맹점 수수료 템플릿 수정 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/feeTemplate/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 일반 수수료 입력</label>
													</div>
													<div class="form-group col-sm-12">
														<label class="control-label col-sm-2 req-label">템플릿명</label> 
														<div class="col-sm-10">
																<input type="text" class="form-control input-sm name" name="name" maxlength="200" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 수수료</label> 
														<div class="col-sm-8">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm distRate2" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																<input type="hidden" class="form-control input-sm distRate" name="distRate" maxlength="9" value="0.00000">
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 수수료</label>
														<div class="col-sm-8">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm agencyRate2" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																<input type="hidden" class="form-control input-sm agencyRate" maxlength="9" name="agencyRate" placeholder="" value="0.00000">
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
															</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 수수료</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm salesRate2" maxlength="9" name="salesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm salesRate" maxlength="9" name="salesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점 수수료</label>
														<div class="col-sm-8">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm rate2" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																<input type="hidden" class="form-control rate" maxlength="9" name="rate" placeholder="" value="0.00000"> 
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
														</div>
													</div>
													
													
														<div class="form-group col-sm-12 form-subtitle">
															<label><i class="fa fa-reorder"></i> 영중소 차액정산</label>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0DistRate2" maxlength="9" name="diff0DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0DistRate" maxlength="9" name="diff0DistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0CheckDistRate2" maxlength="9" name="diff0CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckDistRate" maxlength="9" name="diff0CheckDistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1DistRate2" maxlength="9" name="diff1DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1DistRate" maxlength="9" name="diff1DistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1CheckDistRate2" maxlength="9" name="diff1CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckDistRate" maxlength="9" name="diff1CheckDistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2DistRate2" maxlength="9" name="diff2DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2DistRate" maxlength="9" name="diff2DistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2CheckDistRate2" maxlength="9" name="diff2CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckDistRate" maxlength="9" name="diff2CheckDistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3DistRate2" maxlength="9" name="diff3DistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3DistRate" maxlength="9" name="diff3DistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">대행사 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3CheckDistRate2" maxlength="9" name="diff3CheckDistRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckDistRate" maxlength="9" name="diff3CheckDistRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0AgencyRate2" maxlength="9" name="diff0AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0AgencyRate" maxlength="9" name="diff0AgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0CheckAgencyRate2" maxlength="9" name="diff0CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckAgencyRate" maxlength="9" name="diff0CheckAgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1AgencyRate2" maxlength="9" name="diff1AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1AgencyRate" maxlength="9" name="diff1AgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1CheckAgencyRate2" maxlength="9" name="diff1CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckAgencyRate" maxlength="9" name="diff1CheckAgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2AgencyRate2" maxlength="9" name="diff2AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2AgencyRate" maxlength="9" name="diff2AgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2CheckAgencyRate2" maxlength="9" name="diff2CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckAgencyRate" maxlength="9" name="diff2CheckAgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3AgencyRate2" maxlength="9" name="diff3AgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3AgencyRate" maxlength="9" name="diff3AgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3CheckAgencyRate2" maxlength="9" name="diff3CheckAgencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckAgencyRate" maxlength="9" name="diff3CheckAgencyRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 영세 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0SalesRate2" maxlength="9" name="diff0SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0SalesRate" maxlength="9" name="diff0SalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 영세 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff0CheckSalesRate2" maxlength="9" name="diff0CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff0CheckSalesRate" maxlength="9" name="diff0CheckSalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1SalesRate2" maxlength="9" name="diff1SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1SalesRate" maxlength="9" name="diff1SalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소1 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff1CheckSalesRate2" maxlength="9" name="diff1CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff1CheckSalesRate" maxlength="9" name="diff1CheckSalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2SalesRate2" maxlength="9" name="diff2SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2SalesRate" maxlength="9" name="diff2SalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소2 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff2CheckSalesRate2" maxlength="9" name="diff2CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff2CheckSalesRate" maxlength="9" name="diff2CheckSalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(신용)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3SalesRate2" maxlength="9" name="diff3SalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3SalesRate" maxlength="9" name="diff3SalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">지사 중소3 가맹점(체크)</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text" class="form-control input-sm diff3CheckSalesRate2" maxlength="9" name="diff3CheckSalesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0" data-reg="false">
																	<input type="hidden" class="form-control input-sm diff3CheckSalesRate" maxlength="9" name="diff3CheckSalesRate" placeholder="" value="0.00000">
																	<span class="input-group-addon"> % (VAT 별도)</span>
																</div>
															</div>
														</div>
													
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
				name : {
					required: true
				},
				rate2 : {
					required: true,
				},
				agencyRate2 : {
					required: true,
				},
				distRate2 : {
					required: true,
				},
				salesRate2 : {
					required: true,
				},
				diff0DistRate2 : {
					required: true,
				},
				diff1DistRate2 : {
					required: true,
				},
				diff2DistRate2 : {
					required: true,
				},
				diff3DistRate2 : {
					required: true,
				},
				diff0CheckDistRate2 : {
					required: true,
				},
				diff1CheckDistRate2 : {
					required: true,
				},
				diff2CheckDistRate2 : {
					required: true,
				},
				diff3CheckDistRate2 : {
					required: true,
				},
				diff0AgencyRate2 : {
					required: true,
				},
				diff1AgencyRate2 : {
					required: true,
				},
				diff2AgencyRate2 : {
					required: true,
				},
				diff3AgencyRate2 : {
					required: true,
				},
				diff0CheckAgencyRate2 : {
					required: true,
				},
				diff1CheckAgencyRate2 : {
					required: true,
				},
				diff2CheckAgencyRate2 : {
					required: true,
				},
				diff3CheckAgencyRate2 : {
					required: true,
				},
				diff0SalesRate2 : {
					required: true,
				},
				diff1SalesRate2 : {
					required: true,
				},
				diff2SalesRate2 : {
					required: true,
				},
				diff3SalesRate2 : {
					required: true,
				},
				diff0CheckSalesRate2 : {
					required: true,
				},
				diff1CheckSalesRate2 : {
					required: true,
				},
				diff2CheckSalesRate2 : {
					required: true,
				},
				diff3CheckSalesRate2 : {
					required: true,
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
				bootbox.confirm("입력하신 정보로 가맹점 수수료 템플릿을 등록하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/feeTemplate/form.jsp'); //PAGE 이동		
					}
				});
			}
		});
		
	
		$(".loading-btn").click(function(){
			$('.distRate').val(($('.distRate2').val()/100).toFixed(5));
			$('.agencyRate').val(($('.agencyRate2').val()/100).toFixed(5));
			$('.salesRate').val(($('.salesRate2').val()/100).toFixed(5));
			$('.rate').val(($('.rate2').val()/100).toFixed(5));
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
	3
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>