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
									<li><span>수수료 예약 수정</span></li>
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
												<i class="fa fa-reorder"></i> 수수료 변경 예약 수정
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/rate/" method="post">
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
														<label class="control-label col-sm-4 req-label">가맹점수수료율</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm rate2" data-reg="false" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
															<input type="hidden" class="form-control input-sm rate" maxlength="7" name="rate" value="${DATAMAP.rate}">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">선정산수수료</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm loanRate2" data-reg="false" maxlength="9" name="loanRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
															<input type="hidden" class="form-control input-sm loanRate" maxlength="7" name="loanRate" value="${DATAMAP.loanRate}">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사수수료율</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm distRate2" data-reg="false" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
															<input type="hidden" class="form-control input-sm distRate" maxlength="7" name="distRate" value="${DATAMAP.distRate}">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시수수료율</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm agencyRate2" data-reg="false" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
															<input type="hidden" class="form-control input-sm agencyRate" maxlength="7" name="agencyRate" value="${DATAMAP.agencyRate}">
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
		
		$(".loading-btn").click(function(){
			$('.rate').val(($('.rate2').val()/100).toFixed(5));
			$('.loanRate').val(($('.loanRate2').val()/100).toFixed(5));
			$('.distRate').val(($('.distRate2').val()/100).toFixed(5));
			$('.agencyRate').val(($('.agencyRate2').val()/100).toFixed(5));
		});
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>