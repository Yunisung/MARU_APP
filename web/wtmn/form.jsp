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
                  <li><span>웹결제</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body"></div>
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
												<span class="caption-title"> 웹 카드 결제 시스템 </span>
											</div>
										</div>
									
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/" method="post">
												<div class="form-body row">
													<div class="form-group">
														<label class="col-md-3 control-label req-label">구매 상품명</label>
														<div class="col-md-6">
															<div class="input-group">
																<span class="input-group-addon"><i class="fa fa-barcode"></i></span>
																<input type="text" name="product" id="product" class="form-control" placeholder="구매 상품명 상세 입력" >
																<input type="hidden" name="publicKey" id="publicKey" value="${CP_SESSION.webPay}">
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-3 control-label req-label">결제금액</label>
														<div class="col-md-6">
															<div class="input-group">
																<span class="input-group-addon"><i class="fa fa-won"></i></span>
																<input type="text" name="amount" id="amount" class="form-control" placeholder="숫자만 입력" >
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-3 control-label req-label">고객 이름</label>
														<div class="col-md-6">
															<div class="input-group">
																<span class="input-group-addon"><i class="fa fa-user"></i></span>
																<input type="text" name="payerName" id="payerName" class="form-control" placeholder="" >
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-3 control-label req-label">고객전화번호</label>
														<div class="col-md-6">
															<div class="input-group">
																<span class="input-group-addon"><i class="fa fa-phone"></i></span>
																<input type="text" name="payerTel" id="payerTel" class="form-control" placeholder="" >
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-3 control-label">고객 이메일</label>
														<div class="col-md-6">
															<div class="input-group">
																<span class="input-group-addon"><i class="fa fa-envelope"></i></span>
																<input type="email" name="payerEmail" id="payerEmail" class="form-control" placeholder="" >
															</div>
														</div>
													</div>
												</div>
												<div class="alert alert-danger display-hide"></div>
												<div class="form-actions right">
													<div class="">
														<button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
															<i class="fa fa-search"></i>&nbsp;결제하기
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
	<script type="text/javascript" src="https://api.bkwinners.kr/js/clientside.js"></script>
	
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules : {
				product : {
					minlength : 2,
					required : true
				},
				amount : {
					money: true,
					required : true
				},
				payerName : {
					minlength : 2,
					required : true
				},
				payerTel : {
					required : true,
					minlength : 8
				}
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
               	var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "결제 중  잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
               	error1.html(error1Str);
				error1.show();
                App.scrollTo(error1, -200);
            },
            submitHandler: function (form) {
                error1.hide();
				
				var products = [];
			      var product = new Object();
			      product.name=$('#product').val();
			      product.price=$('#amount').val();
			      product.qty =1;
			      product.desc ='WEBPAY';
			      
			      products.push(product);
			      MARU.pay({
			        amount: $('#amount').val(),
			        publicKey: $('#publicKey').val(),
			        products: products,
			        responseFunction: eventFnc,
			        redirectUrl: '',
			        webhookUrl: '',
			        udf1: '',
			        udf2: '',
			        payerName: $('#payerName').val(),
			        payerEmail: $('#payerEmail').val(),
			        payerTel: $('#payerTel').val(),
			        mode: 'layer'
			      });
			
			}
		});
		$('#nav-mcht').addClass('active');
	</script>
	<script type="text/javascript">
		MARU.debug(false);
		function eventFnc(data) {
			location.href="/trx/pay/form.jsp";

    	}
	</script>
</body>

</html>