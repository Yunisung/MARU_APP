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
									<li><span>대행사 지불 및 정산정보 수정</span></li>
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
												<span class="caption-title"> ${DATAMAP.name} 대행사 지불 및 정산정보 수정 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/dist/mng/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
													</div>
													<input type="hidden" name="distId" data-key="true" value="${DATAMAP.distId}"/>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">지불 사용여부</label>
														<select name="payStatus" class="selectpicker col-sm-6">
															<option value="사용" selected>사용</option>
															<option value="중지">중지</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.payStatus.value = '${DATAMAP.status}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">대출정산 사용여부</label>
														<select name="loanSettleStatus" class="selectpicker col-sm-6">
															<option value="사용">사용</option>
															<option value="미사용" selected>미사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.loanSettleStatus.value = '${DATAMAP.loanSettleStatus}' </script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">정산유형</label>
<!-- 														<div class="col-sm-6"> -->
<%-- 																<input type="text" class="form-control input-sm settleType" name="settleType" readonly="readonly" value="${DATAMAP.settleType}"> --%>
<!-- 														</div> -->
														<select name="settleType" class="selectpicker col-sm-6">
															<option value="M+10" selected>다음달 10일 정산</option>
															<option value="M+15">다음달 15일 정산</option>
															<option value="M+25">다음달 25일 정산</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}' </script>
														
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">이체 건당 수수료 <br>(즉시결제시만 사용)</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm currency wireFee" maxlength="15" name="wireFee" placeholder="" value="${DATAMAP.wireFee}">
															<span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    		</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1회한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm currency limitOnce" maxlength="15" name="limitOnce" placeholder="" value="${DATAMAP.limitOnce}">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    		</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1일한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm currency limitDay" maxlength="15" name="limitDay" placeholder="" value="${DATAMAP.limitDay}">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    		</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">1개월한도</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control input-sm currency limitMonth" maxlength="15" name="limitMonth" placeholder="" value="${DATAMAP.limitMonth}">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    		</div>
														</div>
													</div>
													<div class="form-group col-sm-12">
														<label class="control-label col-sm-2 req-label">대행사 수수료</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control rate2" maxlength="100" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
																<input type="hidden" class="form-control rate" maxlength="100" name="rate" value="${DATAMAP.rate}">
																<span class="input-group-addon"> % (VAT 별도)</span>
                                                    		</div>
														</div>
													</div>									
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-bank"></i> 은행 정보 입력</label>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">은행이름</label>
														<select name="bankCd" class="selectpicker col-sm-6 bankCd">
															<option value="">은행 선택</option>
															<c:forEach var="entryMap" items="${BANK_OPTION}">
																<option value="${entryMap['code']}">${entryMap['codeName']}</option>
															</c:forEach>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.bankCd.value = '${DATAMAP.bankCd}' </script>
														<input type="hidden" name="bankName" value="${DATAMAP.bankName }">
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">계좌번호</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoPhone account" maxlength="20" name="account" placeholder="" value="${DATAMAP.account}">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">예금주</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm ceoTel accntHolder" maxlength="30" name="accntHolder" placeholder="" value="${DATAMAP.accntHolder}">
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
	
		//은행 선택 셀렉트박스 크기 조절
		$(document).ready(function() {
			$('.dropdown-toggle').click(function() {
				$('.dropdown-menu').css('max-height', '400px');
			});
			
		});
	
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
				rate2 : {
					required: true
				},
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
				bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 대행사 등록정보를 수정하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/member/dist/view/${DATAMAP.distId}'); //PAGE 이동		
					}
				});
			}
		});
		
		var rate = $('.rate').val().split(",");
		var rate2 = "";
		for(var i=0; i<rate.length; i++){
			if(i < rate.length-1){
				rate2 += (rate[i]*100).toFixed(3)+',';
			} else {
				rate2 += (rate[i]*100).toFixed(3);
			}
		}
		$('.rate2').val(rate2);
		
		$('.wireFee').val(addComma(String($('.wireFee').val()).replace(/[^(-?)0-9]/g,"")));
		$('.limitOnce').val(addComma(String($('.limitOnce').val()).replace(/[^(-?)0-9]/g,"")));
		$('.limitDay').val(addComma(String($('.limitDay').val()).replace(/[^(-?)0-9]/g,"")));
		$('.limitMonth').val(addComma(String($('.limitMonth').val()).replace(/[^(-?)0-9]/g,"")));
		
		$('.wireFee').keyup(function(){
			$('.wireFee').val(addComma(String($('.wireFee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		$('.limitOnce').keyup(function(){
			$('.limitOnce').val(addComma(String($('.limitOnce').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		$('.limitDay').keyup(function(){
			$('.limitDay').val(addComma(String($('.limitDay').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		$('.limitMonth').keyup(function(){
			$('.limitMonth').val(addComma(String($('.limitMonth').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
		
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
		
		$(".loading-btn").click(function(){
	    	$(".wireFee").val($(".wireFee").val().replace(/,/g, ''));
	    	$(".limitOnce").val($(".limitOnce").val().replace(/,/g, ''));
	    	$(".limitDay").val($(".limitDay").val().replace(/,/g, ''));
	    	$(".limitMonth").val($(".limitMonth").val().replace(/,/g, ''));
	    	
	    	var rate2 = $('.rate2').val().split(",");
			var rate = "";
			for(var i=0; i<rate2.length; i++){
				if(i < rate2.length-1){
					rate += (rate2[i]/100).toFixed(5)+',';
				} else {
					rate += (rate2[i]/100).toFixed(5);
				}
			}
			$('.rate').val(rate);
		});
		
		$('#nav-member').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>