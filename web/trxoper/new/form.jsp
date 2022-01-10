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
									<li><span>거래관리</span><i class="fa fa-circle"></i></li>
                  <li><span>거래생성</span></li>
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
												<span class="caption-title">
													거래생성
												</span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/trxoper/new/" method="post">
												<div class="form-body row">
													<input type="hidden" name="action_type" value="insert" data-reg="false" />
													<div class="form-body row">
														
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">에이전시
															</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" id="agencyName" name="agencyName" value="" readonly>
																<input type="hidden" id="agencyId" name="agencyId" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4 req-label">가맹점
															</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" id="mchtName" name="mchtName" value="" readonly>
																<input type="hidden" id="mchtId" name="mchtId" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4 req-label">터미널</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" id="tmnId" name="tmnId" value="">
															</div>
															<div class="row-sm-3">
			                                                    <button class="btn green btn-sm btn-tmn" type="button">확인</button>&nbsp;&nbsp;&nbsp;
			                                                    <span id="diffType"></span>
		                                                    </div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">승인구분</label>
															<select name="trnType" id="trnType" class="selectpicker trnType col-sm-6">
																<option value="" selected></option>
																<option value="승인">승인</option>
																<option value="승인취소">승인취소</option>
															</select>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">금액</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="12" id="amount" name="amount" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">할부</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="2" id="installment" name="installment" value="00">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">BIN</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="6" id="bin" name="bin" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">LAST4</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="4" id="last4" name="last4" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">승인번호</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="8" id="authCd" name="authCd" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">승인일자</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="8" id="trxDay" name="trxDay" placeholder="YYYYMMDD" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">승인시간</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="6" id="trxTime" name="trxTime" placeholder="HHMMSS" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">VAN거래번호</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="30" id="vanTrxId" name="vanTrxId" placeholder="" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">가맹점 주문번호</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="50" id="trackId" name="trackId" placeholder="" value="">
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">원거래번호</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="50" id="rootTrxId" name="rootTrxId" placeholder="" value="">
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
		$('#nav-trx').addClass('active');
		
		$(document).on('change','#trnType',function(e) {
			
			var trnType = $( "#trnType option:selected" ).val();
			
			if(trnType == '승인'){
				$('#rootTrxId').prop('disabled', true);
				$('#rootTrxId').val('');
				$('#bin').prop('disabled', false);
				$('#last4').prop('disabled', false);
				$('#authCd').prop('disabled', false);
			}else{
				$('#rootTrxId').prop('disabled', false);
				$('#bin').prop('disabled', true);
				$('#last4').prop('disabled', true);
				$('#authCd').prop('disabled', true);
				$('#bin').val('');
				$('#last4').val('');
				$('#authCd').val('');
			}
		});
		
		
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		var tmnCheck = false;
		
		form1.validate({
			rules : {
				amount : {
					required : true
				},
				installment : {
					min : 0,
					max : 24,
					required : true,
					digits: true
				},
				bin : {
					minlength : 6,
					required : true
				},
				last4 : {
					minlength : 4,
					required : true
				},
				authCd : {
					minlength : 6,
					required : true
				},
				trxDay : {
					minlength : 8,
					maxlength : 8,
					digits: true,
					required : true
				},
				trxTime : {
					minlength : 6,
					maxlength : 6,
					digits: true,
					required : true
				},
				vanTrxId : {
					required : true
				},
				trackId : {
					required : true
				},
				tmnId : {
					required : true
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
            	if(tmnCheck == false){
    				alert('터미널 입력 후 확인 버튼을 눌러주세요.');
    				return false;
    			}
                error1.hide();
				bootbox.confirm($('#bootbox_confirm').html(), function(result) {
					if (result) {
						ajaxFormSubmit(form, '/trxoper/load/form.jsp'); //PAGE 이동		
					}
				});
			}
		});
		
		$('#tmnId').on('keyup',function(e) {
			$('#tmnId').val($('#tmnId').val().replace(/\s/gi, ""));
		});
		
		
		$('#writeFrm .btn-tmn').click(function() {
			
        	var tmnId = $('input[name="tmnId"]').val();
        	if(tmnId == null || tmnId == ''){
        		bootbox.alert('터미널번호를 입력해주세요.');
        		return false;
        	}
        	
	        $.ajax({
				url: '/tmnId/check/' + $('input[name="tmnId"]').val(),
				type:'get',
				success: function(res) {
					if(res.tmnId == '' || res.tmnId == null){
						tmnCheck = false;
						alert('사용하지 않는 터미널 아이디입니다.');
						$('#mchtName').val('');
						$('#mchtId').val('');
						$('#agencyName').val('');
						$('#agencyId').val('');
					} else{
						tmnCheck = true;
						$('#mchtName').val(res.mchtName + ' ['+res.mchtId+']');
						$('#mchtId').val(res.mchtId);
						$('#agencyName').val(res.agencyName + ' ['+res.agencyId+']');
						$('#agencyId').val(res.agencyId);
					}
					
				}, 
				error: function(res, status) {
					console.log(res, status);
						
				}
			});
        	
        });
		
		$("#amount").keyup(function(){
			$("#amount").val(addComma(String($("#amount").val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
       	});

		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
       	
		$(".loading-btn").click(function(){
	    	$("#amount").val($("#amount").val().replace(/,/g, ''));
		});
		
		
		
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
	<!-- 확인창 생성을 위한 베이스 -->
	<div style="display: none;">
		<div class="panel panel-warning" id="bootbox_confirm">
			<!-- Default panel contents -->
			<div class="panel-heading">
				<h3 class="panel-title">입력정보 확인</h3>
			</div>
			<div class="panel-body">
				<p>거래를 생성하시겠습니까?</p>
			</div>
			
		</div>
	</div>
</body>

</html>