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
									<li><span>가상계좌 수수료 예약 수정</span></li>
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
												<i class="fa fa-reorder"></i> 가상계좌 수수료 변경 예약 수정
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/vactRate/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<div class="form-body row">
													<input type="hidden" name="idx" value="${DATAMAP.idx }" data-key="true">
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">제목
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm title" maxlength="25" name="title" value="${DATAMAP.title}" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">적용일</label>
														<div class="col-sm-6">
															<input class="form-control form-control-inline input-sm datepicker pubDay" data-date-format="yyyy-mm-dd" name="pubDay" maxlength="10" type="text" value="${DATAMAP.pubDay}">
														</div>
													</div>

													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">수수료유형</label>
														<select name="feeType" class="selectpicker col-sm-6">
															<option value="0" selected>정액</option>
															<option value="1">정률</option>
															<option value="2">혼합</option>
														</select>
													</div>
													<script type="text/javascript">
														document.forms.writeFrm.feeType.value = '${DATAMAP.feeType}'
													</script>

													<div class="form-group col-sm-6" id="feeDiv">
														<label class="control-label col-sm-4">가맹점 정산수수료(VAT별도)</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control fee comma" data-oper="comma" maxlength="20" name="fee" placeholder="" value="${DATAMAP.fee }">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6" id="rateDiv">
														<label class="control-label col-sm-4">가맹점 정산수수료율</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control rate percent" data-oper="percent" maxlength="9" name="rate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${DATAMAP.rate }">
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6" id="distFeeDiv">
														<label class="control-label col-sm-4">대행사수수료(VAT별도)</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control distFee comma" data-oper="comma" maxlength="20" name="distFee" placeholder="" value="${DATAMAP.distFee }">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6" id="distRateDiv">
														<label class="control-label col-sm-4">대행사수수료율</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control distRate percent" data-oper="percent" maxlength="9" name="distRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${DATAMAP.distRate }">
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6" id="agencyFeeDiv">
														<label class="control-label col-sm-4">에이전시수수료(VAT별도)</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control agencyFee comma" data-oper="comma" maxlength="20" name="agencyFee" placeholder="" value="${DATAMAP.agencyFee }">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6" id="agencyRateDiv">
														<label class="control-label col-sm-4">에이전시수수료율</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control agencyRate percent" data-oper="percent" maxlength="9" name="agencyRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${DATAMAP.agencyRate }">
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6" id="salesFeeDiv">
														<label class="control-label col-sm-4">지사 수수료(VAT별도)</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control salesFee comma" maxlength="20" data-oper="comma" name="salesFee" placeholder="" value="0">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6" id="salesRateDiv">
														<label class="control-label col-sm-4">지사 수수료율</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control salesRate percent" maxlength="9" data-oper="percent" name="salesRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="0.000">
																<span class="input-group-addon"> % (VAT 별도)</span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">예약여부</label>
														<select name="status" class="selectpicker col-sm-6">
															<option value="사용">사용</option>
															<option value="폐기">폐기</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.status.value = '${DATAMAP.status}' </script>
													</div>
													
												</div>

												<div class="alert alert-danger display-hide"></div>
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
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				title : {
					required : true
				},
				fee : {
					required: true
				},
				pubDay : {
					minlength:10,
					required: true
				}
			},
			submitHandler: function (form) {
				// 수수료 유형이 정액일 때
				if($('select[name="feeType"]').val()=='0'){
					if(Number($('.fee').val()) < Number($('.distFee').val()) + Number($('.agencyFee').val()) + Number($('.salesFee').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사, 에이전시, 지사 수수료의 합이 가맹점 수수료 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.fee').focus();
					} else {
						error1.hide();
						bootbox.confirm("입력하신 정보로 가상계좌를 설정하시겠습니까?", function(result) {
							if (result) {
								ajaxFormSubmit(form, '/member/vactRate/form.jsp'); //PAGE 이동
							}
						});
					}
					// 수수료 유형이 정률일 때
				} else if($('select[name="feeType"]').val()=='1'){
					if(Number($('.distRate').val()) > Number($('.agencyRate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사 수수료율이 에이전시 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.agencyRate').focus();
					} else if(Number($('.agencyRate').val()) > Number($('.rate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "에이전시 수수료율이 가맹점 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.rate').focus();
					} else {
						error1.hide();
						bootbox.confirm("입력하신 정보로 가상계좌를 설정하시겠습니까?", function(result) {
							if (result) {
								ajaxFormSubmit(form, '/member/vactRate/form.jsp'); //PAGE 이동
							}
						});
					}
				} else {
					if(Number($('.distRate').val()) > Number($('.agencyRate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사 수수료율이 에이전시 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.agencyRate').focus();
					} else if(Number($('.agencyRate').val()) > Number($('.rate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "에이전시 수수료율이 가맹점 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.rate').focus();
					} else if(Number($('.fee').val()) < Number($('.distFee').val()) + Number($('.agencyFee').val()) + Number($('.salesFee').val())) {
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사, 에이전시, 지사 수수료의 합이 가맹점 수수료 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.fee').focus();
					} else {
						error1.hide();
						bootbox.confirm("입력하신 정보로 가상계좌를 설정하시겠습니까?", function(result) {
							if (result) {
								ajaxFormSubmit(form, '/member/vactRate/form.jsp'); //PAGE 이동
							}
						});
					}
				}
			}
		});

		$('select[name="feeType"]').on('change', function() {
			var selected = $(this).find("option:selected").val();

			if(selected == '0'){
				$('.rate').val('0');
				$('#rateDiv').hide();
				$('.distRate').val('0');
				$('#distRateDiv').hide();
				$('.agencyRate').val('0');
				$('#agencyRateDiv').hide();
				$('.salesRate').val('0');
				$('#salesRateDiv').hide();

				$('#feeDiv').show();
				$('#distFeeDiv').show();
				$('#agencyFeeDiv').show();
				$('#salesFeeDiv').show();

				$('.noneDiv').hide();
			} else if(selected == '1'){
				$('.fee').val('0');
				$('#feeDiv').hide();
				$('.distFee').val('0');
				$('#distFeeDiv').hide();
				$('.agencyFee').val('0');
				$('#agencyFeeDiv').hide();
				$('.salesFee').val('0');
				$('#salesFeeDiv').hide();

				$('#rateDiv').show();
				$('#distRateDiv').show();
				$('#agencyRateDiv').show();
				$('#salesRateDiv').show();

				$('.noneDiv').hide();
			} else {
				$('#feeDiv').show();
				$('#distFeeDiv').show();
				$('#agencyFeeDiv').show();
				$('#salesFeeDiv').show();
				$('#rateDiv').show();
				$('#distRateDiv').show();
				$('#agencyRateDiv').show();
				$('#salesRateDiv').show();

				$('.noneDiv').show();
			}
		});

		$(document).ready(function(){
			var selected = $('select[name="feeType"]').find("option:selected").val();
			if(selected == '0'){
				$('#rateDiv').hide();
				$('#distRateDiv').hide();
				$('#agencyRateDiv').hide();
				$('#salesRateDiv').hide();
				$('.noneDiv').hide();
			} else if(selected == '1'){
				$('#feeDiv').hide();
				$('#distFeeDiv').hide();
				$('#agencyFeeDiv').hide();
				$('#salesFeeDiv').hide();
				$('.noneDiv').hide();
			}
		})
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>