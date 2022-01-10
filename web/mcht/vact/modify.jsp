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
								<li><span>가상계좌 정보 수정</span></li>
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
										<span class="caption-title"> ${DATAMAP.name} 가상계좌 정보 수정 </span>
									</div>
								</div>
								<div class="portlet-body form">
									<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/vact/"
									 method="post">
										<input type="hidden" name="action_type" value="update" data-reg="false" />
										<div class="form-body row">
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">가맹점 ID</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" name="mchtId" data-key="true" value="${MCHT_MAP.mchtId}"
													 readonly>
												</div>
											</div>

											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">기본 예금주명</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm holderName" maxlength="100" name="holderName" value="${DATAMAP.holderName}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">상태</label>
												<select name="status" class="selectpicker col-sm-6">
													<option value="중지" selected>중지</option>
													<option value="사용">사용</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.status.value = '${DATAMAP.status}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">발행유형</label>
												<select name="issueType" class="selectpicker col-sm-6">
													<option value="임시" selected>임시</option>
													<option value="영구">영구</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.issueType.value = '${DATAMAP.issueType}'
												</script>
											</div>

											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">만료일 지정</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm numberOnly" maxlength="3" name="expireSet" placeholder=""
													 value="${DATAMAP.expireSet}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">거래시작일</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" name="startDay" value="${DATAMAP.startDay}" readonly>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">정산대상여부</label>
												<select name="settleTarget" class="selectpicker col-sm-6">
													<option value="Y">Y</option>
													<option value="N">N</option>
												</select>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">수수료유형</label>
												<select name="feeType" class="selectpicker col-sm-6">
													<option value="0" selected>정액</option>
													<option value="1">정률</option>
												</select>
											</div>
											<script type="text/javascript">
												document.forms.writeFrm.feeType.value = '${DATAMAP.feeType}'
											</script>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">가맹점정산유형</label>
												<select name="settleType" class="selectpicker col-sm-6">
													<c:if test="${DATASVCMAP.settle != '충전정산'}">
														<option value="A+0">당일정산</option>
														<option value="A+1">자동정산</option>
														<option value="D+0">실시간정산</option>
														<option value="D+1">1일 후 정산</option>
														<option value="D+2">2일 후 정산</option>
														<option value="D+3">3일 후 정산</option>
														<option value="D+4">4일 후 정산</option>
														<option value="D+5">5일 후 정산</option>
													</c:if>
													<c:if test="${DATASVCMAP.settle == '충전정산'}">
														<option value="C+0">실시간 충전정산</option>
														<option value="B+1">1일 후 자동충전정산</option>
														<option value="C+1">1일 후 충전정산</option>
														<option value="C+2">2일 후 충전정산</option>
														<option value="C+3">3일 후 충전정산</option>
														<option value="C+4">4일 후 충전정산</option>
														<option value="C+5">5일 후 충전정산</option>
													</c:if>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">정산수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm fee" maxlength="20" name="fee" placeholder="" value="${DATAMAP.fee}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">정산수수료율</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm rate2" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
														<input type="hidden" class="form-control rate" maxlength="9" name="rate" placeholder="" value="${DATAMAP.rate}"> 
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">대행사정산유형</label>
												<select name="distSettleType" class="selectpicker col-sm-6">
													<option value="M+25" selected>M+25</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.distSettleType.value = '${DATAMAP.distSettleType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">대행사수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm distFee" maxlength="20" name="distFee" placeholder="" value="${DATAMAP.distFee}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">대행사수수료율</label> 
												<div class="col-sm-8">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm distRate2" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
														<input type="hidden" class="form-control input-sm distRate" maxlength="9" name="distRate" placeholder="" value="${DATAMAP.distRate}">
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">에이전시정산유형</label>
												<select name="agencySettleType" class="selectpicker col-sm-6">
													<option value="M+25" selected>M+25</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.agencySettleType.value = '${DATAMAP.agencySettleType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">에이전시수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm agencyFee" maxlength="20" name="agencyFee" placeholder="" value="${DATAMAP.agencyFee}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">에이전시수수료율</label>
												<div class="col-sm-8">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm agencyRate2" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
														<input type="hidden" class="form-control input-sm agencyRate" maxlength="9" name="agencyRate" placeholder="" value="${DATAMAP.agencyRate}">
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">지사정산유형</label>
												<select name="salesSettleType" class="selectpicker col-sm-6">
													<option value="M+25" selected>M+25</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.salesSettleType.value = '${DATAMAP.salesSettleType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">지사수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm salesFee" maxlength="20" name="salesFee" placeholder="" value="${DATAMAP.salesFee}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">지사수수료율</label>
												<div class="col-sm-8">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control input-sm salesRate2" maxlength="9" name="salesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" data-reg="false">
														<input type="hidden" class="form-control input-sm salesRate" maxlength="9" name="salesRate" placeholder="" value="${DATAMAP.salesRate}">
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">거래전달 프로토콜</label>
												<select name="hookType" class="selectpicker col-sm-6">
													<option value="HTTPS" selected>HTTPS</option>
													<option value="HTTP">HTTP</option>
													<option value="TCP">TCP</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.hookType.value = '${DATAMAP.hookType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">거래전달 주소(URL)</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" maxlength="100" name="hookAddr" placeholder="api.example.com"
													 value="${DATAMAP.hookAddr}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">1회한도</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text"
															class="form-control currency limitOnce" maxlength="10"
															name="limitOnce" placeholder="" value="${DATAMAP.limitOnce}"> <span
															class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">1일한도</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text"
															class="form-control currency limitDay" maxlength="10"
															name="limitDay" placeholder="" value="${DATAMAP.limitDay}"> <span
															class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">실시간정산<br>출금 수수료</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency payOutFee" maxlength="9" name="payOutFee" placeholder="" value="${DATAMAP.payOutFee}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">실시간정산<br>전송간격</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency transferInterval" maxlength="3" name="transferInterval" placeholder="" value="${DATAMAP.transferInterval}"> 
														<span class="input-group-addon">분</span>
													</div>
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
						<!-- END PAGE CONTENT INNER -->
					</div>
				</div>
			</div>
			<!-- BEGIN CONTAINER -->
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules: {
				mchtId : {
					required : true
				},
				holderName : {
					minlength : 2,
					required : true
				},
				issueType : {
					required : true
				},
				expireSet : {
					required : true
				},
				fee2 : {
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
				error1.hide();
				bootbox.confirm("입력하신 정보로 ${MCHT_MAP.name} 가상계좌 정보를 수정 하시겠습니까?", function (result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${MCHT_MAP.mchtId}/tab_virAccount'); //PAGE 이동		
					}
				});
			}
		});

		$(document).ready(function () {
			onIssueType($('[name="issueType"]'));
		});

		$('[name="issueType"]').change(function (e) {
			onIssueType(this);
		});

		function onIssueType(t) {
			var val = $(t).val();
			var expireSet = $('[name="expireSet"]');
			if (val == '임시') {
				
				if( expireSet.val() == '365' ){
					expireSet.val(3);
				}
				
				expireSet.removeAttr('disabled');
			} else {
				expireSet.val(365);
				expireSet.attr('disabled', 'disabled');
			}
		}
		
		$('.distRate2').val(($('.distRate').val()*100).toFixed(3));
		$('.agencyRate2').val(($('.agencyRate').val()*100).toFixed(3));
		$('.salesRate2').val(($('.salesRate').val()*100).toFixed(3));
		$('.rate2').val(($('.rate').val()*100).toFixed(3));

		$('.fee').val(addComma(String($('.fee').val()).replace(/[^0-9]/g,"")));
		$('.distFee').val(addComma(String($('.distFee').val()).replace(/[^0-9]/g,"")));
		$('.agencyFee').val(addComma(String($('.agencyFee').val()).replace(/[^0-9]/g,"")));
		$('.salesFee').val(addComma(String($('.salesFee').val()).replace(/[^0-9]/g,"")));
		$('.limitOnce').val(addComma(String($('.limitOnce').val()).replace(/[^0-9]/g,"")));
		$('.limitDay').val(addComma(String($('.limitDay').val()).replace(/[^0-9]/g,"")));
		$('.payOutFee').val(addComma(String($('.payOutFee').val()).replace(/[^0-9]/g,"")));
	
		$('.fee').keyup(function(){
			$('.fee').val(addComma(String($('.fee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
		$('.distFee').keyup(function(){
			$('.distFee').val(addComma(String($('.distFee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
		$('.agencyFee').keyup(function(){
			$('.agencyFee').val(addComma(String($('.agencyFee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
		$('.salesFee').keyup(function(){
			$('.salesFee').val(addComma(String($('.salesFee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
		$('.limitOnce').keyup(function(){
			$('.limitOnce').val(addComma(String($('.limitOnce').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
		$('.limitDay').keyup(function(){
			$('.limitDay').val(addComma(String($('.limitDay').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
		$('.payOutFee').keyup(function(){
			$('.payOutFee').val(addComma(String($('.payOutFee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
		});
	
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
	   	
		$(".loading-btn").click(function(){
	    	$(".fee").val($(".fee").val().replace(/,/g, ''));
	    	$(".distFee").val($(".distFee").val().replace(/,/g, ''));
	    	$(".agencyFee").val($(".agencyFee").val().replace(/,/g, ''));
	    	$(".salesFee").val($(".salesFee").val().replace(/,/g, ''));
	    	$(".limitOnce").val($(".limitOnce").val().replace(/,/g, ''));
	    	$(".limitDay").val($(".limitDay").val().replace(/,/g, ''));
	    	$(".payOutFee").val($(".payOutFee").val().replace(/,/g, ''));
	    	
			$('.distRate').val(($('.distRate2').val()/100).toFixed(5));
			$('.agencyRate').val(($('.agencyRate2').val()/100).toFixed(5));
			$('.salesRate').val(($('.salesRate2').val()/100).toFixed(5));
			$('.rate').val(($('.rate2').val()/100).toFixed(5));
		});
	
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>