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
									<li><span>서비스 정보 수정</span></li>
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
												<span class="caption-title"> 서비스 정보 수정 </span>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/svc/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 가맹점 ${MCHT.name } &nbsp; ID : ${MCHT.mchtId }</label>
														<input type="hidden" class="form-control input-sm" name="mchtId" data-key="true" placeholder="" value="${MCHT.mchtId}" readonly>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">카드일반</label>
														<select name="cardRegular" class="selectpicker col-sm-6 col-xs-12" >
															<option value="사용">사용</option>
														</select>
														
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">카드3D</label>
														<select name="card3D" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>
															<option value="미사용">미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.card3D.value = '${DATAMAP.card3D}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">가상계좌</label>
														<select name="virAccount" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>
															<option value="미사용">미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.virAccount.value = '${DATAMAP.virAccount}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">실시간계좌이체</label>
														<select name="bankCollect" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>
															<option value="미사용">미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.bankCollect.value = '${DATAMAP.bankCollect}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">월렛서비스</label>
														<select name="walletSvc" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>
															<option value="미사용">미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.walletSvc.value = '${DATAMAP.walletSvc}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">휴대폰소액결제</label>
														<select name="phoneBill" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>
															<option value="미사용">미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.phoneBill.value = '${DATAMAP.phoneBill}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">KSNET 빠른현장결제</label>
														<select name="recurring" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>
															<option value="미사용">미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.recurring.value = '${DATAMAP.recurring}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">지급대행</label>
														<select name="pisp" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>     
															<option value="미사용">미사용</option> 
														</select>
														<script type="text/javascript"> document.forms.writeFrm.pisp.value = '${DATAMAP.pisp}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">정산구분</label>
														<select name="settle" class="selectpicker col-sm-6 col-xs-12">
															<option value="일반">일반</option> 
															<option value="실시간정산">실시간정산</option> 
															<option value="자동정산">자동정산</option> 
															<option value="충전정산">충전정산</option> 
														</select>
														<script type="text/javascript"> document.forms.writeFrm.settle.value = '${DATAMAP.settle}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">현금영수증</label>
														<select name="cashReceipt" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option> 
															<option value="미사용">미사용</option> 
														</select>
														<script type="text/javascript"> document.forms.writeFrm.cashReceipt.value = '${DATAMAP.cashReceipt}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">정기결제</label>
														<select name="rebill" class="selectpicker col-sm-6 col-xs-12">
															<option value="사용">사용</option>
															<option value="미사용">미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.rebill.value = '${DATAMAP.rebill}'</script>
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
				bootbox.confirm("입력하신 정보로 ${MCHT.name} 서비스 정보를 수정 하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${MCHT.mchtId}/tab_svc'); //PAGE 이동		
					}
				});
			}
		});
		
		
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>