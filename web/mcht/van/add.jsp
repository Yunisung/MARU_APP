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
									<li><span>	VAN ID 등록</span></li>
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
												<span class="caption-title"> VAN ID 등록 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/van/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												<div class="form-body row">
													<div class="form-group col-sm-12 form-subtitle">
														<label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">VAN 아이디</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm vanid" name="vanid" placeholder="VAN ID를 입력하세요." value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">VAN 아이디 이름</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm name" name="name" placeholder="VAN ID 이름을 입력하세요." value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">VAN 선택</label>
														<select name="van" class="selectpicker col-sm-6">
															<option value="">--선택--</option>
															<c:forEach items="${CP_SESSION.vanList }" var="list">
																<option value="${list.van}">${list.van}</option>
															</c:forEach>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">상태</label>
														<select name="status" class="selectpicker col-sm-6">
															<option value="예비">예비</option>
															<option value="사용" selected>사용</option>
															<option value="중지">중지</option>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">Crypto Key</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm cryptokey" maxlength="100" name="cryptokey" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">Second Key</label>
														<div class="col-sm-6">
															<input type="text" class="form-control input-sm secondkey" maxlength="100" name="secondkey" placeholder="" value="">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">휴대폰 정산타입</label>
														<select name="settleType" class="selectpicker col-sm-6">
															<option value="사용안함">사용안함</option>
															<option value="0">주정산</option>
															<option value="1">수납정산</option>
														</select>
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
				vanid : {
					required: true
  /*              	required: true,
					remote : {
						url : "/mcht/van/idCheck", //make sure to return true or false with a 200 status code
						type : "post",
						data : {
							id : function() {
								return form1.find('input[name="vanid"]').val();
							}
						}
					} */
				},
				name : {
					required: true
				},
				van : {
					required: true
				},
				cryptokey: {
					required: true
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
				bootbox.confirm("입력하신 정보로 VAN ID를 생성하시겠습니까?", function(result) {
					if (result) {
						ajaxFormSubmit(form, '/mcht/van/form.jsp'); //PAGE 이동		
					}
				});
			}
		});
		
		$('select[name="van"]').change(function() {
			if($(this).val() == 'DANAL') {
				$('input[name="cryptokey"]').val('ed390d5b4407ed3cdd467373a634e58562983e74f41bc5dfb7b92e6a55d7c6a2');
			//} else if ($(this).val() == 'NICE') {
			//	$('input[name="cryptokey"]').val('KXoPoRumu2Py5HpjKvVxjSks3Lj2SE0tvx7swrx2LY2zrHgd sv7Hws2WFAgf9qxloU40j9uXECk3FIZypf/yg==');
			} else {
				$('input[name="cryptokey"]').val('');
			}
		});
		
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>