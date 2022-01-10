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
									<li><span>가맹점 대출 수정</span></li>
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
												<span class="caption-title"> 대출 정보 수정 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/loanSettle/" method="post">
												<!-- 업데이트 구문 -->
												<input type="hidden" name="action_type" value="update" data-reg="false" />
												<div class="form-body row">
													<input type="hidden" name="loanId" value="${DATAMAP.loanId}" data-key="true" />
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점ID</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm mchtId" name="mchtId" value="${DATAMAP.mchtId }" readonly>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">가맹점명</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm name" name="name" value="${DATAMAP.name }" readonly>
														</div>
													</div>
													<div class="form-group col-sm-12">
														<label class="control-label col-sm-2 req-label">대출 금액</label>
														<div class="col-sm-3">
															<input type="text" class="form-control input-sm" name="amount">
														</div>
													</div>
													<br>
													<div class="form-group col-sm-12">
														<label class="control-label col-sm-2 req-label">약정 납입횟수</label>
														<div class="col-sm-3">
															<input type="text" class="form-control input-sm numberOnly" name="conCnt" maxlength="3" value="${DATAMAP.conCnt }">
														</div>
														<p>* 약정 횟수는 100일을 넘을 수 없습니다.</p>
													</div>
														
													<div class="form-group col-sm-12">
														<label class="control-label col-sm-2 req-label">대출 실행 일자</label>
														<div class="col-sm-3">
															<input type="text" class="form-control input-sm" id="loanDay" name="loanDay" value="${DATAMAP.loanDay }">
														</div>
														<p>* 당일 이후 설정 가능하며, 공휴일 확인 후 설정 해 주세요.</p>
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
				amount : {
					required : true
				},
				conCnt : {
					required : true
				},
                loanDay : {
                    required : true
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
				bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 가맹점 대출정보를 수정 하시겠습니까?", function(result) {
					if (result) {
                        ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_basic'); //PAGE 이동
                    }
				});
			}
		});
		
		$('input:text[name=conCnt]').change(function(){
			var conCnt = $(this).val();
			if(conCnt > 100){
				alert('약정 횟수는 100이하로 입력해주세요.');
				$('input:text[name=conCnt]').val("");
				$('input:text[name=conCnt]').focus();
			}
		});
		
		$("#loanDay").datepicker({
			format: "yyyymmdd",
			startDate: "+1d",
			autoclose : true,
			daysOfWeekDisabled : [0,6]

		})
		var beforeAmt = ${DATAMAP.amount};
		var afterAmt = addComma(beforeAmt);
		$("input:text[name=amount]").val(afterAmt);
		
		$("input:text[name=amount]").on("keyup", function() {
			$(this).val(addComma($(this).val().replace(/[^0-9]/g,"")));
	   	});
		
		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		$(".loading-btn").click(function(){
        	var amount = $("input:text[name=amount]").val();
	    	var changeAmt = amount.replace(/,/g, '');
	    	$("input:text[name=amount]").val(changeAmt);
		});
		
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade modal-sm" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>