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
									<li><span>차감정산 등록</span></li>
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
													${MCHT_MAP.mchtName} 차감정산 등록 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/ddct/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												
												<div class="form-body row">
													<input type="hidden" name="mchtId" value="${MCHT_MAP.mchtId }"/>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 항목</label>
														<select name="type" class="selectpicker col-sm-6">
															<option value="">선택하세요.</option>
															<c:forEach var="entry" items="${DDCTCODE}" varStatus="status" >
																<option value="${entry.code}">${entry.codeName }</option>
															</c:forEach>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 금액</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency monthlyAmt" maxlength="11" name="monthlyAmt" placeholder="" value="0"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 시작월</label>
														<div class="col-sm-6">
															<div class="input-icon">
																	<i class="fa fa-calendar font-blue"></i><input class="datepicker form-control" name="startMonth" id="startMonth">
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 종료월</label>
														<div class="col-sm-6">
															<div class="input-icon">
																	<i class="fa fa-calendar font-blue"></i><input class="datepicker form-control" name="endMonth" id="endMonth">
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">정기 차감 적용일자</label>
														<select name="settleType" class="selectpicker col-sm-6 settleType">
															<option value="M+1" selected>1일 정산</option>
															<c:forEach var="day" begin="2" end="31" step="1">
																<option value="M+${day }">${day }일 정산</option>
															</c:forEach>
														</select>
													</div>
													<%--
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">총차감정산액</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control currency" maxlength="11" name="totalAmt" placeholder="" value="0"> 
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													 --%>
													
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
				monthlyAmt : {
					money: true
				},
				type : {
					required:true
				},
				settleType : {
					required:true
				},
				startMonth : {
					required:true
				},
				endMonth : {
					required:true
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
				bootbox.confirm("입력하신 정보로 ${DATAMAP.mchtName} 차감정산 정보를 생성하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/view/${MCHT_MAP.mchtId}/tab_ddct'); //PAGE 이동		
					}
				});
			}
		});
		$(".datepicker").datepicker({
			format : "yyyymm",
			viewMode : "months",
			minViewMode : "months",
			locale : 'ko',
			autoclose : true
		});
		$("#startMonth").datepicker().datepicker('setDate',new Date());

		//정기 차감 종료월 수정
		$("#endMonth").datepicker().datepicker('setDate',new Date());

		var start = $('#startMonth').val();
		$('#endMonth').datepicker('setStartDate', start);
		
		$('#startMonth').datepicker().on('changeDate', function (selected){
			var startDate = new Date(selected.date.valueOf());
			$('#endMonth').datepicker('setStartDate', startDate);
		});
		
   		$('.monthlyAmt').val(addComma(String($('.monthlyAmt').val()).replace(/[^0-9]/g,"")));
		
   		$('.monthlyAmt').keyup(function(){
			$('.monthlyAmt').val(addComma(String($('.monthlyAmt').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
	   	});
   		
   		function addComma(data) {
		    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		$(".loading-btn").click(function(){
			$(".monthlyAmt").val($(".monthlyAmt").val().replace(/,/g, ''));
		});
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>