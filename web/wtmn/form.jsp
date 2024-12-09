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
																<input type="hidden" name="payKey" id="payKey" value="${CP_SESSION.webPay}">
																<input type="hidden" id="products" name="products"/>
																<input type="hidden" id="contents" name="contents"/>
																<input type="hidden" id="grade" name="grade" value="${CP_SESSION.grade}">
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
														<button class="btn green btn-sm loading-btn" data-loading-text="Loading..." onclick="javascript:fn_pay();">
															<i class="fa fa-search"></i>&nbsp;결제하기
														</button>
														<button class="btn green btn-sm loading-btn" data-loading-text="Loading..." onclick="javascrit:fn_sms();">
															<i class="fa fa-search"></i>&nbsp;SMS 결제하기
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
            // submitHandler: function (form) {
            //     error1.hide();
			//
			// 	var products = [];
			//       var product = new Object();
			//       product.name=$('#product').val();
			//       product.price=$('#amount').val();
			//       product.qty =1;
			//       product.desc ='WEBPAY';
			//
			//       products.push(product);
			//       MARU.pay({
			//         amount: $('#amount').val(),
			//         publicKey: $('#publicKey').val(),
			//         products: products,
			//         responseFunction: eventFnc,
			//         redirectUrl: '',
			//         webhookUrl: '',
			//         udf1: '',
			//         udf2: '',
			//         payerName: $('#payerName').val(),
			//         payerEmail: $('#payerEmail').val(),
			//         payerTel: $('#payerTel').val(),
			//         mode: 'layer'
			//       });
			//
			// }
		});
		$('#nav-mcht').addClass('active');
	</script>
	<script type="text/javascript">
		MARU.debug(false);

		var grade = $('#grade').val();

		function fn_valid(){
			if(!$.trim($('#product').val())){
				bootbox.alert({
					message: "구매상품명을 입력하세요.",
					callback:function(){
						setTimeout(function (){
							$('#product').focus();
						},10);
					}
				});
				return false;
			}
			if(!$.trim($('#amount').val())){
				bootbox.alert({
					message: "결제금액을 입력하세요.",
					callback:function(){
						setTimeout(function (){
							$('#amount').focus();
						},10);
					}
				});
				return false;
			}

			if(!$.trim($('#payerName').val())){
				bootbox.alert({
					message: "고객 이름 입력은 필수입니다.",
					callback:function(){
						setTimeout(function (){
							$('#payerName').focus();
						},10);
					}
				});
				return false;
			}

			if(!$.trim($('#payerTel').val())){
				bootbox.alert({
					message: "고객 전화번호 입력은 필수입니다.",
					callback:function(){
						setTimeout(function (){
							$('#name').focus();
						},10);
					}
				});
				return false;
			}
			return true;
		}

		function eventFnc(data) {
			if(grade == '하위가맹점') {
				location.href="/subMcht/trx/form.jsp";
			} else {
				location.href="/trx/cap/form.jsp"
			}
    	}

		function fn_pay() {
			error1.hide();
			if(!fn_valid()) {
				return false;
			}


			var products = [];
			var product = new Object();
			product.name=$('#product').val();
			product.price=$('#amount').val();
			product.qty =1;
			product.desc ='WEBPAY';

			products.push(product);
			MARU.pay({
				amount: $('#amount').val(),
				publicKey: $('#payKey').val(),
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

		function fn_sms() {
			error1.hide();
			if(!fn_valid()) {
				return false;
			}

			var products = [];
			var product = new Object();
			product.name=$('#product').val();
			product.price=$('#amount').val();
			product.qty =1;
			product.desc ='WEBPAY';
			products.push(product);

			$('#products').val(JSON.stringify(products));

			$.ajax({
				url: "<c:url value='/mcht/smsPay'/>",
				type: "POST",
				data: $('#writeFrm').serialize(),
				dataType: "json",
				success: function (data) {
					if(data.result === 'Y') {
						fn_sendSms(data.smsKey);
					} else {
						alert("결제키 생성을 실패했습니다.");
					}

				},
				error: function () {
					alert("처리중 오류가 발생했습니다.");
				}
			});
		}

		function fn_sendSms(smsKey){
			var payerTel = $('#payerTel').val();
			if(confirm(payerTel+"번호로 결제 URL을 전송하겠습니까?")){
				var baseUrl = 'https://sugi.bkwinners.kr/sms/';
				var url = baseUrl+smsKey+'/pay';
				var content = "상품명 : "+$('#product').val()+"\n결제금액 : "+numberWithCommas($.trim($('#amount').val()))+"원\n\n아래 URL을 누르시면, 결제창으로 연결됩니다.\n\n"+url + "\n\n 결제서비스제공사 : 부국위너스 ";
				//console.log(content);
				// if(navigator.userAgent.match(/Android/i) != null){
				// 	location.href = 'sms:'+payerTel+'?body='+content;
				// }else if(navigator.userAgent.match(/iPhone|iPad|iPod/i) != null){
				// 	location.href = 'sms:'+payerTel+'&body='+content;
				// }

				$('#contents').val(content);
				$.ajax({
					url: "<c:url value='/mcht/smsSend'/>",
					type: "POST",
					data: $('#writeFrm').serialize(),
					dataType: "json",
					success: function (data) {
						if(data.result === 'Y') {
							alert("문자전송을 완료했습니다.");
						} else {
							alert("문자전송을 실패했습니다.");
						}

					},
					error: function () {
						alert("처리중 오류가 발생했습니다.");
					}
				});
			}
		}

		function numberWithCommas(x) {
			return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
	</script>
</body>

</html>