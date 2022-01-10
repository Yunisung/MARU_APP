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
									<li><span>가맹점 휴대폰 결제 정보 수정</span></li>
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
											${DATAMAP.mchtId} 가맹점 휴대폰 결제 정보 수정 </span>
									</div>
								</div>
						<div class="portlet-body form">
							<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/phone/mng/" method="post">
								<input type="hidden" name="action_type" value="update" data-reg="false" />
								<div class="form-body row">
									<div class="form-group col-sm-12 form-subtitle">
										<label><i class="fa fa-reorder"></i> 가맹점 : ${MCHT.name } &nbsp; ID : ${MCHT.mchtId } </label>
									</div>
									<input type="hidden" name="mchtId" data-key="true" value="${DATAMAP.mchtId}" />
									<div class="form-group col-sm-6">
										<label class="control-label input-sm col-sm-4 req-label">지불 사용여부</label>
										<select name="payStatus" class="selectpicker col-sm-6">
											<option value="사용">사용</option>
											<option value="중지">중지</option>
										</select>
										<script type="text/javascript"> document.forms.writeFrm.payStatus.value = '${DATAMAP.payStatus}' </script>
									</div>
									<div class="form-group col-sm-6">
										<label class="control-label input-sm col-sm-4 req-label">상품유형</label>
										<select name="prodType" class="selectpicker col-sm-6 prodType">
											<option value="1">실물</option>
											<option value="2">컨텐츠</option>
										</select>
										<script type="text/javascript"> document.forms.writeFrm.prodType.value = '${DATAMAP.prodType}' </script>
									</div>
									<div class="form-group col-sm-6">
										<label class="control-label col-sm-4 req-label">가맹점 수수료</label>
										<div class="col-sm-6">
											<div class="input-group input-group-sm">
												<input type="text" class="form-control input-sm rate2" data-reg="false" maxlength="9" name="rate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
												<input type="hidden" class="form-control input-sm rate" maxlength="9" name="rate" placeholder="" value="${DATAMAP.rate}"> 
												<span class="input-group-addon"> % (VAT 별도)</span>
											</div>
										</div>
									</div>
									<c:if test="${CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
										<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">에이전시 수수료</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control input-sm agencyRate2" data-reg="false" maxlength="9" name="agencyRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" <c:if test="${CP_SESSION.grade == '에이전시' }">readonly</c:if>>
													<input type="hidden" class="form-control input-sm agencyRate" maxlength="9" name="agencyRate" placeholder="" value="${DATAMAP.agencyRate}">
													<span class="input-group-addon"> % (VAT 별도)</span>
												</div>
											</div>
										</div>
									</c:if>
									<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
										<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">대행사 수수료</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control input-sm distRate2" data-reg="false" maxlength="9" name="distRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="">
													<input type="hidden" class="form-control input-sm distRate" maxlength="9" name="distRate" placeholder="" value="${DATAMAP.distRate}">
													<span class="input-group-addon"> % (VAT 별도)</span>
												</div>
											</div>
										</div>
									</c:if>
									<c:if test="${CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사' || CP_SESSION.grade == '지사'}">
										<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">지사 수수료</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control input-sm salesRate2" data-reg="false" maxlength="9" name="salesRate2" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="" <c:if test="${CP_SESSION.grade == '에이전시' }">readonly</c:if>>
													<input type="hidden" class="form-control input-sm salesRate" maxlength="9" name="salesRate" placeholder="" value="${DATAMAP.salesRate}">
													<span class="input-group-addon"> % (VAT 별도)</span>
												</div>
											</div>
										</div>
									</c:if>
									<div class="form-group col-sm-6">
										<label class="control-label col-sm-4 req-label">1회한도</label>
										<div class="col-sm-6">
											<div class="input-group input-group-sm">
												<input type="text" class="form-control input-sm limitOnce currency" maxlength="15" name="limitOnce" placeholder="" value="${DATAMAP.limitOnce}">
												<span class="input-group-addon"><i class="fa fa-krw"></i></span>
											</div>
										</div>
									</div>
									<div class="form-group col-sm-6">
										<label class="control-label col-sm-4 req-label">1일한도</label>
										<div class="col-sm-6">
											<div class="input-group input-group-sm">
												<input type="text" class="form-control input-sm limitDay currency" maxlength="15" name="limitDay" placeholder="" value="${DATAMAP.limitDay}">
												<span class="input-group-addon"><i class="fa fa-krw"></i></span>
											</div>
										</div>
									</div>
									<div class="form-group col-sm-6">
										<label class="control-label col-sm-4 req-label">1개월한도</label>
										<div class="col-sm-6">
											<div class="input-group input-group-sm">
												<input type="text" class="form-control input-sm limitMonth currency" maxlength="15" name="limitMonth" placeholder="" value="${DATAMAP.limitMonth}">
												<span class="input-group-addon"><i class="fa fa-krw"></i></span>
											</div>
										</div>
									</div>
									<div class="form-group col-sm-6">
										<label class="control-label col-sm-4 req-label">연한도</label>
										<div class="col-sm-6">
											<div class="input-group input-group-sm">
												<input type="text" class="form-control input-sm limitYear currency" maxlength="15" name="limitYear" placeholder="" value="${DATAMAP.limitYear}">
												<span class="input-group-addon"><i class="fa fa-krw"></i></span>
											</div>
										</div>
									</div>
									<div class="form-group col-sm-6">
										<label class="control-label col-sm-4 req-label">고액거래 기준</label>
										<div class="col-sm-6">
											<div class="input-group input-group-sm">
												<input type="text" class="form-control currency largeAmount" name="largeAmount" placeholder="" value="${DATAMAP.largeAmount}">
												<span class="input-group-addon"><i class="fa fa-krw"></i></span>
											</div>
										</div>
									</div>
									<div class="form-group col-sm-6">
										<label class="control-label  col-sm-4 req-label">거래시작일</label>
										<div class="col-sm-6">
											<c:choose>
												<c:when test="${DATAMAP.openDay ne ''}">
													<fmt:parseDate value="${DATAMAP.openDay}" var="dateStr" pattern="yyyyMMdd"/>
													<fmt:formatDate value="${dateStr }" pattern="yyyy-MM-dd" var="openDay"/>
													<input type="text" class="form-control input-sm datepicker openDay" maxlength="10" name="openDay" placeholder="" value="${openDay}">
												</c:when>
												<c:otherwise>
													<input type="text" class="form-control input-sm datepicker openDay now-date" maxlength="10" name="openDay" placeholder="" value="">
												</c:otherwise>
											</c:choose>
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
			limitYear : {
				money: true
			},
			settleType : {
				required: true
			},
			rate2 : {
				required: true,
			},
			<c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '본사'}">
				agencyRate2 : {
					required: true,
					min: function(element) {
						return Number($('input[name="distRate2"]').val());
					}
				},
				<%--distRate : {
					required: true,
					rate: true,
					max: function(element) {
						return Number($('input[name="agencyRate"]').val());
					}
				},--%>
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
			},
			tmnId : {
            	required: true,
				remote : {
					url : "/mcht/tmn/idCheck", //make sure to return true or false with a 200 status code
					type : "post",
					data : {
						id : function() {
							return form1.find('input[name="tmnId"]').val();
						}
					}
				}
			},
			van : {
				required: true
			},
			vanIdx : {
				required: true
			},
			email: {
				required: true,
				email:true
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
			bootbox.confirm("입력하신 정보로 ${MCHT.name} 서비스 정보를 수정 하시겠습니까?", function(result) {
				if (result) {
					ajaxFormSubmit(form, '/mcht/view/${MCHT.mchtId}/tab_phone'); //PAGE 이동		
				}
			});
		}
	});
	
	$('.distRate2').val(($('.distRate').val()*100).toFixed(3));
	$('.agencyRate2').val(($('.agencyRate').val()*100).toFixed(3));
	$('.salesRate2').val(($('.salesRate').val()*100).toFixed(3));
	$('.rate2').val(($('.rate').val()*100).toFixed(3));
	
	$('.limitOnce').val(addComma(String($('.limitOnce').val())));
	$('.limitDay').val(addComma(String($('.limitDay').val())));
	$('.limitMonth').val(addComma(String($('.limitMonth').val())));
	$('.limitYear').val(addComma(String($('.limitYear').val())));
	$('.largeAmount').val(addComma(String($('.largeAmount').val())));
	
	$('.limitOnce').keyup(function(){
		$('.limitOnce').val(addComma(String($('.limitOnce').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});
	
	$('.limitDay').keyup(function(){
		$('.limitDay').val(addComma(String($('.limitDay').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});
	
	$('.limitMonth').keyup(function(){
		$('.limitMonth').val(addComma(String($('.limitMonth').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});
	
	$('.limitYear').keyup(function(){
		$('.limitYear').val(addComma(String($('.limitYear').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});
	
	$('.largeAmount').keyup(function(){
		$('.largeAmount').val(addComma(String($('.largeAmount').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});
	
	function addComma(data) {
	    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

	$(".loading-btn").click(function(){
    	$(".limitOnce").val($(".limitOnce").val().replace(/,/g, ''));
    	$(".limitDay").val($(".limitDay").val().replace(/,/g, ''));
    	$(".limitMonth").val($(".limitMonth").val().replace(/,/g, ''));
    	$(".limitYear").val($(".limitYear").val().replace(/,/g, ''));
    	$(".largeAmount").val($(".largeAmount").val().replace(/,/g, ''));
    	
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