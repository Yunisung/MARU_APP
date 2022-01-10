<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
<c:import url="/include/head.jsp" />
</head>

<body class="page-container-bg-solid page-header-menu-fixed">
	<div class="page-wrapper">
		<c:import url="/include/nav.jsp" />
		<div class="page-wrapper-row full-height">
			<div class="page-wrapper-middle">
				<!-- BEGIN CONTAINER -->
				<div class="page-container">
					<div class="page-content-wrapper">
						<div class="page-head">
							<div class="mtouch-container">
								<div class="page-title">
									<h1>
										가맹점 추가 &nbsp;&nbsp; <small>* Home &gt; 가맹점 추가</small>
									</h1>
								</div>
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
						</div>
						<div class="page-content">
							<div class="mtouch-container">
								<!-- BEGIN PAGE CONTENT - UNUSED - INNER -->
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-reorder"></i> 등록
											</div>
											<div class="tools">
												<a href="" class="collapse"></a>
											</div>
										</div>
										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/" method="post">
												<input type="hidden" name="action_type" value="insert" data-reg="false" />
												
												<div class="form-body row">
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4" for="inputSuccess">이름<span class="required">*</span>
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control" maxlength="20" data-required="1" name="name">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4" for="inputSuccess">아이디<span class="required">*</span>
														</label>
														<div class="col-sm-6">
															<input type="text" class="form-control" maxlength="12" name="memberid" id="memberid">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">비밀번호<span class="required">*</span></label>
														<div class="col-sm-6">
															<input name="pw" type="password" id="pw" class="form-control" maxlength="20" placeholder="password">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">비밀번호확인<span class="required">*</span></label>
														<div class="col-sm-6">
															<input name="pw2" type="password" class="form-control" maxlength="20" placeholder="confirm password">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">연락처<span class="required">*</span></label>
														<div class="col-sm-6">
															<input type="text" class="form-control" maxlength="11" placeholder="" name="phone">
														</div>
													</div>

													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">이메일</label>
														<div class="col-sm-6">
															<input type="email" name="email" maxlength="50" class="form-control">
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">상태<span class="required">*</span></label>
														<select name="memstatus" class="selectpicker col-sm-6">
															<option value="ACTIVE">ACTIVE</option>
															<option value="INACTIVE">INACTIVE</option>
															<option value="TERMINATED">TERMINATED</option>
														</select>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">등급<span class="required">*</span></label>
														<select id="grade" name="grade" class="selectpicker col-sm-6">
															<option value="일반" selected>일반</option>
															<option value="관리자">관리자</option>
															<option value="마스터">마스터</option>
														</select>
													</div>
												</div>
												<input type="hidden" name="grade" value="N">

												<div class="alert alert-danger display-hide">
													<button class="close" data-close="alert"></button>
													You have some form errors. Please check above.
												</div>
												<div class="form-actions right">
													<div class="">
														<button type="submit" class="btn purple btn-sm loading-btn" data-loading-text="Loading...">
															<i class="fa fa-search"></i>&nbsp;Submit
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- END ADD FORM TABLE-->
									<!-- BEGIN FORM JAVASCRIPT -->

									<script>
										/* $(':input[maxlength]')
												.maxlength(
														{
															alwaysShow : true,
															warningClass : "label label-success",
															limitReachedClass : "label label-danger",
															separator : ' out of ',
															preText : 'You typed ',
															postText : ' chars available.',
															validate : true
														});

										var form1 = $('#writeFrm');
										var error1 = $('.alert-danger',
												form1);

										form1
												.validate({
													errorElement : 'span', //default input error message container
													errorClass : 'help-inline', // default input error message class
													focusInvalid : true, // do not focus the last invalid input
													ignore : "",
													rules : {
														memberid : {
															minlength : 5,
															required : true,
															remote : {
																url : "/idCheck/alluser", //make sure to return true or false with a 200 status code
																type : "post",
																data : {
																	memberid : function() {
																		return $(
																				"#memberid")
																				.val();
																	}
																}
															}
														},
														pw : {
															required : true,
															minlength : 4,
															required : true
														},
														pw2 : {
															required : true,
															minlength : 4,
															equalTo : "#pw"
														},
														phone : {
															minlength : 8,
															number : true,
															required : true
														},
														name : {
															required : true,
															minlength : 3
														}
													},

													invalidHandler : function(
															event,
															validator) { //display error alert on form submit
														error1.show();
														App.scrollTo(
																error1,
																-200);
													},
													highlight : function(
															element) { // hightlight error inputs
														$(element)
																.closest(
																		'.form-group')
																.addClass(
																		'has-error'); // set error class to the control group
													},
													unhighlight : function(
															element) { // revert the change done by hightlight
														$(element)
																.closest(
																		'.form-group')
																.removeClass(
																		'has-error'); // set error class to the control group
													},
													success : function(
															label) {
														label
																.closest(
																		'.form-group')
																.removeClass(
																		'has-error');
														label
																.closest(
																		'.form-group')
																.addClass(
																		'has-success');
													},
													submitHandler : function(
															form) {
														error1.hide();
														ajaxFormSubmit('/member/form.jsp'); //PAGE 이동
													}
												}); */
									</script>
									<!-- END FORM JAVASCRIPT -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>
