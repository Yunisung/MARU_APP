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
									<li><span>대출 정산</span><i class="fa fa-circle"></i></li>
                 	 				<li><span>중도상환 등록</span></li>
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
													중도상환
												</span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/loanSettle/prepay/" method="post">
												<div class="form-body row">
													<input type="hidden" name="action_type" value="insert" data-reg="false" />
													<div class="form-body row">
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">가맹점명(가맹점ID)</label>
															<select class="selectpicker col-sm-6 mchtId" name="mchtId" id="mchtId" data-oper="eq">
																<option value="">-- 초기화 --</option>
																<c:forEach items="${DATAMAP }" var="list">
																	<option value="${list.mchtId}">${list.mchtId}(${list.name})</option>
																</c:forEach>
															</select>
														</div>
														
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4">대출실행일자
															</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" id="loanDay" name="loanDay" value="" readonly>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">중도상환 종류</label>
															<select name="prepayType" id="prepayType" class="selectpicker prepayType col-sm-6">
																<option value="" selected>-- 초기화 --</option>
															</select>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4">대출잔액
															</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" id="balance" name="balance" value="" readonly>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label input-sm col-sm-4">연체금액
															</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" id="delayAmt" name="delayAmt" value="" readonly>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4 req-label">차감 횟수(최대 차감 횟수)</label>
															<div class="col-sm-3">
																<input type="text" class="form-control input-sm numberOnly" maxlength="2" id="prepayCnt" name="prepayCnt" value="">
															</div>
															<div class="col-sm-3">
																<input type="text" class="form-control input-sm" id="maxCnt" name="maxCnt" value="" readOnly>
															</div>
														</div>
														<div class="form-group col-sm-6">
															<label class="control-label col-sm-4">중도상환 금액</label>
															<div class="col-sm-6">
																<input type="text" class="form-control input-sm" maxlength="10" id="prepayAmt" name="prepayAmt" value="" readOnly>
																<input type="hidden" id="conAmt" name="conAmt">
															</div>
														</div>
														<input type="hidden" id="totPayAmt" name="totPayAmt">
														<input type="hidden" id="paySession" name="topaySessiontPayAmt">
														<input type="hidden" id="payCnt" name="payCnt">
														<input type="hidden" id="delayCnt" name="delayCnt">
														<input type="hidden" id="loanId" name="loanId">
														<input type="hidden" id="mchtId" name="mchtId">
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
		$('#nav-loan').addClass('active');
		

		$('#prepayType').on('change',function() {
			
			var mchtId = $("#mchtId option:selected").val();
       		if(mchtId == null || mchtId == ''){
       			alert('가맹점을 먼저 선택하세요.');
       			$(this).find('option:eq(0)').prop("selected", true);
       		}
			
			var prepayType = $( "#prepayType option:selected" ).val();
			
			if(prepayType == '전액차감'){
				$('#prepayCnt').prop('disabled', true);
				$('#prepayCnt').val('');
				$('#prepayAmt').val($('#balance').val());
			} else if(prepayType == '연체차감'){
				$('#prepayCnt').prop('disabled', true);
				$('#prepayCnt').val('');
				$('#prepayAmt').val($('#delayAmt').val());
			} else{
				$('#prepayCnt').prop('disabled', false);
				$('#prepayAmt').val('');
			}
		});
		
		
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		var tmnCheck = false;
		
		form1.validate({
			rules : {
				mchtId : {
					required : true
				}, 
				prepayType : {
					required : true
				}
			},
			invalidHandler: function(event, validator) { //display error alert on form submit              
                var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
                error1.html(error1Str);
                error1.show();
                App.scrollTo(error1, -200);
            },
            submitHandler: function(form) {
                error1.hide();
                bootbox.confirm("입력하신 정보로 중도상환을 진행하시겠습니까?", function(result) {
                    if (result) {
                        ajaxFormSubmit(form, '/loanSettle/form.jsp'); //PAGE 이동
                    }
                });
            }
		});
		
		
        	
       	$('.mchtId').change(function() {
   			
           	var mchtId = $(this).val();
        	
	        $.ajax({
				url: '/mchtId/loanCheck/' + mchtId,
				type:'get',
				success: function(res) {
					if(res.mchtId == '' || res.mchtId == null){
						loanCheck = false;
						alert('대출정산을 사용하지 않는 가맹점 아이디입니다.');
						$('#loanDay').val('');
						$('#balance').val('');
						$('#maxCnt').val('');
						$('#prepayAmt').val('');
						$('#conAmt').val('');
					} else{
						loanCheck = true;
						$('#loanDay').val(res.loanDay.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3'));
						$('#balance').val(addComma(res.balance.replace(/[^0-9]/g,"")));
						$('#maxCnt').val(res.maxCnt);
						$('#conAmt').val(res.conAmt);
						$('#totPayAmt').val(res.totPayAmt);
						$('#paySession').val(res.paySession);
						$('#payCnt').val(res.payCnt);
						$('#delayCnt').val(res.delayCnt);
						$('#loanId').val(res.loanId);
						$('#mchtId').val(res.mchtId);
						
						$('#prepayType').empty();
						if(res.delayCnt > 0){
							$('#delayAmt').val(addComma(res.delayAmt.replace(/[^0-9]/g,"")));
							$('#prepayType').append('<option value="" selected>-- 초기화 --</option>');
							$('#prepayType').append('<option value="연체차감">연체차감</option>');
							$('#prepayType').append('<option value="전액차감">전액차감</option>');
							$("#prepayType option[value='횟수차감']").remove();
							$('#prepayType').selectpicker('refresh');
						} else {
							$('#prepayType').append('<option value="" selected>-- 초기화 --</option>');
							$('#prepayType').append('<option value="횟수차감">횟수차감</option>');
							$('#prepayType').append('<option value="전액차감">전액차감</option>');
							$("#prepayType option[value='연체차감']").remove();
							$('#prepayType').selectpicker('refresh');
						}
					}
					
				}, 
				error: function(res, status) {
					console.log(res, status);
						
				}
			});
        	
        });
       	
       	$('#prepayCnt').keyup(function(){
       		var cnt = Number($(this).val());
       		var maxCnt = Number($('#maxCnt').val());
       		var amt = $('#conAmt').val() * cnt;
       		var changeAmt = addComma(String(amt).replace(/[^0-9]/g,""));
       		$('#prepayAmt').val(changeAmt);
       		
       		if(cnt > maxCnt){
       			alert('최대 차감 횟수를 초과할 수 없습니다.');
       			$(this).val('');
       			$(this).focus();
       			$('#prepayAmt').val('');
       		} 
       		
       	});
		
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
       	
		$(".loading-btn").click(function(){
        	var prepayAmt = $("input:text[name=prepayAmt]").val();
	    	var changeAmt = prepayAmt.replace(/,/g, '');
	    	$("input:text[name=prepayAmt]").val(changeAmt);
	    	
	    	var balance = $("input:text[name=balance]").val();
	    	var changebal = balance.replace(/,/g, '');
	    	$("input:text[name=balance]").val(changebal);
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
				<p>중도상환 처리를 하시겠습니까?</p>
			</div>
			
		</div>
	</div>
</body>

</html>