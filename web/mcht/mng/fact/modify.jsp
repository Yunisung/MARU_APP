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
									<li><span>가맹점 지불 및 정산정보 수정(선정산 가맹점용)</span></li>
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
													${DATAMAP.name} 가맹점 지불 및 정산정보 수정(선정산 가맹점용) </span>
											</div>
										</div>

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
														</select>
														<script type="text/javascript"> document.forms.writeFrm.payStatus.value = '${DATAMAP.payStatus}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label ">입금정산유형</label>
														<select name="settleType" class="selectpicker col-sm-6 settleType">
															<option value="D+1">1일 후 정산</option>
															<option value="D+2">2일 후 정산</option>
															<option value="D+3">3일 후 정산</option>
															<option value="D+4">4일 후 정산</option>
															<option value="D+5">5일 후 정산</option>
															<option value="D+6">6일 후 정산</option>
															<option value="D+7">7일 후 정산</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}' </script>
													</div>
													<c:if test="${CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시
																수수료</label>
															<div class="col-sm-8">
																<div class="input-group input-group-sm">
																	<input type="text"
																		class="form-control input-sm agencyRate" maxlength="9"
																		name="agencyRate" placeholder=""
																		value="${DATAMAP.agencyRate}"
																		<c:if test="${CP_SESSION.grade == '에이전시' }">readonly</c:if>>
																	<span class="input-group-addon">VAT 별도(<span
																		class="display-rate">0.1 = 10</span> %)
																	</span>
																</div>
															</div>
														</div>
													</c:if>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">입금정산 수수료</label>
														<div class="col-sm-8">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control rate"
																	maxlength="9" name="rate" placeholder=""
																	value="${DATAMAP.rate}"> <span
																	class="input-group-addon">VAT 별도(<span
																	class="display-rate">0.1 = 10</span> %)
																</span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">선정산 수수료</label>
														<div class="col-sm-8">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control loanRate" maxlength="9" name="loanRate" placeholder="" value="${DATAMAP.loanRate}"> <span class="input-group-addon"> % </span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이체
															건당 수수료 <br>(즉시결제시만 사용)
														</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text"
																	class="form-control input-sm wireFee currency"
																	maxlength="15" name="wireFee" placeholder=""
																	value="${DATAMAP.wireFee}"> <span
																	class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1회한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text"
																	class="form-control input-sm limitOnce currency"
																	maxlength="15" name="limitOnce" placeholder=""
																	value="${DATAMAP.limitOnce}"> <span
																	class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1일한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text"
																	class="form-control input-sm limitDay currency"
																	maxlength="15" name="limitDay" placeholder=""
																	value="${DATAMAP.limitDay}"> <span
																	class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1개월한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text"
																	class="form-control input-sm limitMonth currency"
																	maxlength="15" name="limitMonth" placeholder=""
																	value="${DATAMAP.limitMonth}"> <span
																	class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">고액거래 기준</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text"
																	class="form-control currency largeAmount"
																	name="largeAmount" placeholder="" value="${DATAMAP.largeAmount}"> <span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">선정산 한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text"
																	class="form-control currency maxLoan"
																	name="maxLoan" placeholder="" value="${DATAMAP.maxLoan}"> <span class="input-group-addon"><i class="fa fa-krw"></i></span>
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
				limitOnce : {
					money: true
				},
				limitDay : {
					money: true
				},
				limitMonth : {
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
				rate : {
					required: true,
					rate: true
				},
				<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
					agencyRate : {
						required: true,
						rate: true
					},
				</c:if>
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
		
		addLimitRules(${DATADISTMNGMAP.limitOnce}, ${DATADISTMNGMAP.limitDay}, ${DATADISTMNGMAP.limitMonth});
		
		$('input[name="rate"], input[name="agencyRate"]').focusout(function(e) {
			$(this).parent().find('.display-rate').text(($(this).val() * 100).toFixed(2));
		});
		
		$(document).ready(function() {
			$('input[name="rate"], input[name="agencyRate"]').each(function() {
				$(this).parent().find('.display-rate').text(($(this).val() * 100).toFixed(2));
			});
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