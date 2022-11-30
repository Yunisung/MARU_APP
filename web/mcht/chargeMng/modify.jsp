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
									<li><span>출금정산 정보 수정</span></li>
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
												<span class="caption-title"> 출금정산 정보 수정 </span>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/chargeMng/" method="post">
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 가맹점 ${MCHTMAP.name } &nbsp; ID : ${MCHTMAP.mchtId }</label>
														<input type="hidden" class="form-control input-sm" name="mchtId" data-key="true" value="${MCHTMAP.mchtId}">
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">출금설정</label>
														<select name="status" class="selectpicker col-sm-6 col-xs-12" >
															<option value="사용">사용</option>
															<option value="중지">중지</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.status.value = '${DATAMAP.status}'</script>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">출금고객적요(미사용시 공란)</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" name="recordInfo" value="${DATAMAP.recordInfo }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">출금수수료(VAT별도)</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text"
																	class="form-control currency withdrawFee" name="withdrawFee" value="${DATAMAP.withdrawFee }"> <span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">출금거래전달 주소(URL)</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" name="hookAddr" value="${DATAMAP.hookAddr }">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">출금키 전달 휴대폰</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm" name="transferKeyTel" value="${DATAMAP.transferKeyTel }">
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
				bootbox.confirm("입력하신 정보로 ${MCHT.name} 출금정산 정보를 수정 하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${MCHTMAP.mchtId}/tab_chargeMng'); //PAGE 이동		
					}
				});
			}
		});
		
   		$('.withdrawFee').val(addComma(String($('.withdrawFee').val()).replace(/[^0-9]/g,"")));
		
		$('.withdrawFee').keyup(function(){
			$('.withdrawFee').val(addComma(String($('.withdrawFee').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
       	});

		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
       	
		$(".loading-btn").click(function(){
	    	$('.withdrawFee').val($(".withdrawFee").val().replace(/,/g, ''));
		});
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>